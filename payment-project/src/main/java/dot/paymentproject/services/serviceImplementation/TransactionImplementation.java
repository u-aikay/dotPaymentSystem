package dot.paymentproject.services.serviceImplementation;

import com.opencsv.CSVWriter;
import dot.paymentproject.entities.Account;
import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.enums.AccountStatus;
import dot.paymentproject.enums.Charges;
import dot.paymentproject.enums.TransactionStatus;
import dot.paymentproject.pojos.request.InflowRequest;
import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.CustomPageResponse;
import dot.paymentproject.pojos.response.TransferResponse;
import dot.paymentproject.repositories.AccountRepository;
import dot.paymentproject.repositories.TransactionLogRepository;
import dot.paymentproject.services.serviceInterface.TransactionInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionImplementation implements TransactionInterface {
    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    Logger logger = LoggerFactory.getLogger(TransactionImplementation.class);

    @Override
    public ResponseEntity<TransferResponse> outflow(TransferRequest request) {
        logger.info("transaction outflow in progress...");
        //get customer wallet
        Optional<Account> customerAccount = accountRepository.findAccountByAccountNumber(request.getSenderAccountNumber());
        if (customerAccount.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        //confirm user pin is correct
        //NOTE: on normal setup pin is not meant to be moved around in plain text
        if (!customerAccount.get().getPin().equals(request.getPin())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (customerAccount.get().getAccountStatus().equals(AccountStatus.PENDING)) {
            TransferResponse resp = TransferResponse.builder().
                    message("resource in use, please try again later")
                    .success(false).build();
            return new ResponseEntity<>(resp, HttpStatus.EXPECTATION_FAILED);
        } else if (customerAccount.get().getAccountStatus().equals(AccountStatus.LOCKED)) {
            TransferResponse resp = TransferResponse.builder().
                    message("User account is restricted, please contact customer care for more details")
                    .success(false).build();
            return new ResponseEntity<>(resp, HttpStatus.FORBIDDEN);
        }
        //calculate charges and confirm if user balance is sufficient
        BigDecimal charges = new BigDecimal(String.valueOf(Charges.TRANSACTION_FEE));
        BigDecimal commissionFee = new BigDecimal(String.valueOf(Charges.COMMISSION));
        BigDecimal chargeAmt = request.getAmount().multiply(charges);
        BigDecimal chargeAmount = chargeAmt.compareTo(BigDecimal.valueOf(100)) > 0 ? new BigDecimal(100) : chargeAmt;
        BigDecimal commissionFeeAmount = chargeAmount.multiply(commissionFee);
        BigDecimal balance = customerAccount.get().getBalance();
        BigDecimal totalAmount = chargeAmount.add(request.getAmount());
        if (totalAmount.compareTo(balance) > 0) {
            TransferResponse resp = TransferResponse.builder().
                    message("User balance is insufficient for this transaction")
                    .success(false).build();
            return new ResponseEntity<>(resp, HttpStatus.EXPECTATION_FAILED);
        }
        //set account status to pending
        customerAccount.get().setAccountStatus(AccountStatus.PENDING);
        accountRepository.save(customerAccount.get());
        //deduct amount from customer balance
        BigDecimal newBalance = balance.subtract(totalAmount);
        customerAccount.get().setBalance(newBalance);
        //update account status to free and save
        customerAccount.get().setAccountStatus(AccountStatus.FREE);
        accountRepository.save(customerAccount.get());
        //send transaction via NIBBS to beneficiary bank
        ResponseEntity<String> callNIBBS = mockNIBBSOutflowCall(request);

        if (callNIBBS.getBody().equals("000")) {
            //on success
            //update transaction log with corresponding data
            TransactionLog transactionLog = TransactionLog.builder()
                    .amount(request.getAmount())
                    .toAccountNumber(request.getBeneficiaryAccountNumber())
                    .toAccountName(request.getBeneficiaryAccountName())
                    .toBankCode(request.getDestinationBankCode())
                    .toBankName(request.getBeneficiaryBankName())
                    .fromAccountNumber(request.getSenderAccountNumber())
                    .fromAccountName(customerAccount.get().getAccountName())
                    .fromBankName("DOT BANK PLC")
                    .fromBankCode("111")
                    .transactionFee(chargeAmount)
                    .commissionAmount(commissionFeeAmount)
                    .commissionDetails("0.5% NIP charges")
                    .status(TransactionStatus.SUCCESSFUL)
                    .build();
            TransferResponse resp = TransferResponse.builder().
                    message("outflow transaction completed successfully")
                    .success(true).build();
            transactionLogRepository.save(transactionLog);
            //return response
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } else {
            //REVERSAL
            customerAccount.get().setAccountStatus(AccountStatus.PENDING);
            accountRepository.save(customerAccount.get());
            customerAccount.get().setBalance(balance);
            customerAccount.get().setAccountStatus(AccountStatus.FREE);
            accountRepository.save(customerAccount.get());
            TransferResponse resp = TransferResponse.builder().
                    message("outflow transaction completed successfully")
                    .success(true).build();
            return new ResponseEntity<>(resp, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Override
    public ResponseEntity<TransferResponse> inflow(InflowRequest request) {
        logger.info("transaction inflow in progress...");
        Optional<Account> customerAccount = accountRepository.findAccountByAccountNumber(request.getBeneficiaryAccountNumber());
        if (customerAccount.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        customerAccount.get().setAccountStatus(AccountStatus.PENDING);
        accountRepository.save(customerAccount.get());
        BigDecimal currentBalance = customerAccount.get().getBalance();
        customerAccount.get().setBalance(currentBalance.add(request.getAmount()));
        customerAccount.get().setAccountStatus(AccountStatus.FREE);
        accountRepository.save(customerAccount.get());

        TransactionLog transactionLog = TransactionLog.builder()
                .amount(request.getAmount())
                .toAccountNumber(customerAccount.get().getAccountNumber())
                .toAccountName(customerAccount.get().getAccountName())
                .toBankCode("111")
                .toBankName("DOT BANK PLC")
                .fromAccountNumber(request.getSenderAccountNumber())
                .fromAccountName(request.getSenderAccountName())
                .fromBankName(request.getSenderBankName())
                .status(TransactionStatus.SUCCESSFUL)
                .build();
        TransferResponse resp = TransferResponse.builder().
                message("outflow transaction completed successfully")
                .success(true).build();
        transactionLogRepository.save(transactionLog);
        //return response
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Override
    public CustomPageResponse<TransactionLog> getLog(int page, int size, String startDate, String endDate) {
        logger.info("fetch transaction log in progress");
        if (page < 0) page = 0;
        if (size < 1) size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        Page<TransactionLog> transactionLogs = transactionLogRepository.findAllByCreatedAt(pageable);
        return new CustomPageResponse<>(true, page, (int) transactionLogs.getTotalElements(), transactionLogs.getNumberOfElements(), transactionLogs.getContent());
    }

    public void getDailySummary() {
        LocalDate today = LocalDate.now();
        LocalDate ystdy = today.minusDays(1);
        Date yesterday = convertToDateViaInstant(ystdy);
        Date endDate = convertToDateViaInstant(ystdy.plusDays(1));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = ystdy.format(formatter);

        List<TransactionLog> transactionLogs = transactionLogRepository.findAllByCreatedAtAndStatus(yesterday, TransactionStatus.SUCCESSFUL);
        String csvFilePath = "src/main/resources/transaction_logs/transaction_log_" + formattedDate + ".csv";
        //write to csv
        writeTransactionsToCSV(transactionLogs, csvFilePath);
    }

    private void writeTransactionsToCSV(List<TransactionLog> transactionLogs, String csvFilePath) {
        long sn = 1L;
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            // Write header
            writer.writeNext(new String[]{"S/N","TRANSACTION-REFERENCE", "AMOUNT", "TRANSACTION-FEE", "DESCRIPTION", "STATUS",
                    "COMMISSION-DETAILS", "COMMISSION-AMOUNT", "FROM-ACCOUNT-NUMBER", "FROM-ACCOUNT-NAME", "FROM-BANK-NAME",
                    "FROM-BANK-CODE", "TO-ACCOUNT-NUMBER", "TO-ACCOUNT-NAME", "TO-BANK-NAME", "TO-BANK-CODE", "CREATED-AT", "UPDATED-AT"});

            // Write transactionLog data
            for (TransactionLog transaction : transactionLogs) {
                writer.writeNext(new String[]{
                        Long.toString(sn),
                        transaction.getTransactionReference(),
                        transaction.getAmount().toString(),
                        transaction.getTransactionFee().toString(),
                        transaction.getDescription(),
                        transaction.getStatus().toString(),
                        transaction.getCommissionDetails(),
                        transaction.getCommissionAmount().toString(),
                        transaction.getFromAccountNumber(),
                        transaction.getFromAccountName(),
                        transaction.getFromBankName(),
                        transaction.getFromBankCode(),
                        transaction.getToAccountNumber(),
                        transaction.getToAccountName(),
                        transaction.getToBankName(),
                        transaction.getToBankCode(),
                        transaction.getCreatedAt().toString(),
                        transaction.getUpdatedAt().toString()
                });
                sn++;
            }
        } catch (IOException e) {
            logger.info("Error occurred while writing transaction logs to csv file");
            e.printStackTrace();
        }

    }

    private ResponseEntity<String> mockNIBBSOutflowCall(TransferRequest request) {
        return new ResponseEntity<>("000", HttpStatus.OK);
    }

    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
