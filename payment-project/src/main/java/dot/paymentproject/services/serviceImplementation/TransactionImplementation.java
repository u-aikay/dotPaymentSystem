package dot.paymentproject.services.serviceImplementation;

import dot.paymentproject.entities.Account;
import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.enums.AccountStatus;
import dot.paymentproject.enums.Charges;
import dot.paymentproject.enums.TransactionStatus;
import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.TransferResponse;
import dot.paymentproject.repositories.AccountRepository;
import dot.paymentproject.repositories.TransactionLogRepository;
import dot.paymentproject.services.serviceInterface.TransactionInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (customerAccount.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        //confirm user pin is correct
        //NOTE: on normal setup pin is not meant to be moved around in plain text
        if (!customerAccount.get().getPin().equals(request.getPin())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (customerAccount.get().getAccountStatus().equals(AccountStatus.PENDING)){
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
        BigDecimal chargeAmount = request.getAmount().multiply(charges);
        BigDecimal totalAmount = chargeAmount.add(request.getAmount());
        BigDecimal balance = customerAccount.get().getBalance();
        if (totalAmount.compareTo(balance) > 0 ){
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

        if (callNIBBS.getBody().equals("000")){
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
                    .status(TransactionStatus.SUCCESSFUL)
                    .build();
            TransferResponse resp = TransferResponse.builder().
                    message("outflow transaction completed successfully")
                    .success(true).build();
            transactionLogRepository.save(transactionLog);
            //return response
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }else {
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

    private ResponseEntity<String> mockNIBBSOutflowCall(TransferRequest request) {
        return new ResponseEntity<>("000", HttpStatus.OK);
    }
}
