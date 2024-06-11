package dot.paymentproject.services.serviceImplementation;

import dot.paymentproject.entities.Account;
import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.enums.TransactionStatus;
import dot.paymentproject.pojos.request.InflowRequest;
import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.TransferResponse;
import dot.paymentproject.repositories.AccountRepository;
import dot.paymentproject.repositories.TransactionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionImplementationTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionLogRepository transactionLogRepository;

    @InjectMocks
    private TransactionImplementation transactionImplementation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    TransactionLog createMockTransaction(Date date) {
        TransactionLog transactionLog = TransactionLog.builder().
                createdAt(date).updatedAt(date).
                status(TransactionStatus.SUCCESSFUL).amount(BigDecimal.valueOf(100)).
                transactionFee(BigDecimal.valueOf(100)).description("").
                commissionDetails("").commissionAmount(BigDecimal.valueOf(10)).
                fromAccountNumber("").fromAccountName("").
                fromBankName("").fromBankCode("").
                toAccountNumber("").toAccountName("").
                toBankName("").toBankCode("").
                build();
        return transactionLog;
    }

    @Test
    void outflowShouldReturnExpectedResponseWhenAccountNotFound() {
        TransferRequest request = new TransferRequest();
        when(accountRepository.findAccountByAccountNumber(request.getSenderAccountNumber())).thenReturn(Optional.empty());

        ResponseEntity<TransferResponse> actualResponse = transactionImplementation.outflow(request);

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
        verify(accountRepository, times(1)).findAccountByAccountNumber(request.getSenderAccountNumber());
    }

    @Test
    void outflowShouldReturnExpectedResponseWhenPinIsIncorrect() {
        TransferRequest request = new TransferRequest();
        request.setPin("1234");
        Account account = new Account();
        account.setPin("5678");
        when(accountRepository.findAccountByAccountNumber(request.getSenderAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<TransferResponse> actualResponse = transactionImplementation.outflow(request);

        assertEquals(HttpStatus.UNAUTHORIZED, actualResponse.getStatusCode());
        verify(accountRepository, times(1)).findAccountByAccountNumber(request.getSenderAccountNumber());
    }

    @Test
    void inflowShouldReturnExpectedResponseWhenAccountNotFound() {
        InflowRequest request = new InflowRequest();
        when(accountRepository.findAccountByAccountNumber(request.getBeneficiaryAccountNumber())).thenReturn(Optional.empty());

        ResponseEntity<TransferResponse> actualResponse = transactionImplementation.inflow(request);

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
        verify(accountRepository, times(1)).findAccountByAccountNumber(request.getBeneficiaryAccountNumber());
    }

    @Test
    void inflowShouldReturnExpectedResponseWhenAccountFound() {
        InflowRequest request = new InflowRequest();
        request.setAmount(BigDecimal.valueOf(100));
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(200));
        when(accountRepository.findAccountByAccountNumber(request.getBeneficiaryAccountNumber())).thenReturn(Optional.of(account));

        ResponseEntity<TransferResponse> actualResponse = transactionImplementation.inflow(request);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals("outflow transaction completed successfully", actualResponse.getBody().getMessage());
        assertEquals(true, actualResponse.getBody().getSuccess());
        verify(accountRepository, times(1)).findAccountByAccountNumber(request.getBeneficiaryAccountNumber());
    }

    @Test
    public void getDailySummaryShouldWriteToCSVWhenTransactionsExist() throws IOException {
        // Arrange
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Date dt = Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        TransactionLog mockTransaction = createMockTransaction(dt);
        List<TransactionLog> transactions = Collections.singletonList(mockTransaction);
        Mockito.when(transactionLogRepository.findAllByCreatedAtAndStatus(dt, TransactionStatus.SUCCESSFUL))
                .thenReturn(transactions);

        transactionImplementation.getDailySummary();

        Mockito.verify(transactionLogRepository, times(1))
                .findAllByCreatedAtAndStatus(dt, TransactionStatus.SUCCESSFUL);
    }
}