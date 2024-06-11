package dot.paymentproject.controllers;

import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.pojos.request.InflowRequest;
import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.CustomPageResponse;
import dot.paymentproject.pojos.response.TransferResponse;
import dot.paymentproject.services.serviceInterface.TransactionInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionInterface transactionInterface;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void outflowTransactionShouldReturnExpectedResponse() {
        TransferRequest request = new TransferRequest();
        TransferResponse expectedResponse = new TransferResponse("Success message", true);
        when(transactionInterface.outflow(request)).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<TransferResponse> actualResponse = transactionController.outflowTransaction(request);

        assertEquals(expectedResponse, actualResponse.getBody());
        verify(transactionInterface, times(1)).outflow(request);
    }

    @Test
    void inflowTransactionShouldReturnExpectedResponse() {
        InflowRequest request = new InflowRequest();
        TransferResponse expectedResponse = new TransferResponse("Success message", true);
        when(transactionInterface.inflow(request)).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<TransferResponse> actualResponse = transactionController.inflowTransaction(request);

        assertEquals(expectedResponse, actualResponse.getBody());
        verify(transactionInterface, times(1)).inflow(request);
    }

    @Test
    void getTransactionLogShouldReturnExpectedResponse() {
        CustomPageResponse<TransactionLog> expectedResponse = new CustomPageResponse<>(true,0,10,0L,Collections.emptyList() );
        when(transactionInterface.getLog(anyInt(), anyInt(), anyString(), anyString())).thenReturn(expectedResponse);

        CustomPageResponse<TransactionLog> actualResponse = transactionController.getTransactionLog(1, 10, "2022-01-01", "2022-12-31");

        assertEquals(expectedResponse, actualResponse);
        verify(transactionInterface, times(1)).getLog(1, 10, "2022-01-01", "2022-12-31");
    }
}