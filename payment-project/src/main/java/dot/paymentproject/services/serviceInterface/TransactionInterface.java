package dot.paymentproject.services.serviceInterface;

import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.pojos.request.InflowRequest;
import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.CustomPageResponse;
import dot.paymentproject.pojos.response.TransferResponse;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface TransactionInterface {
    ResponseEntity<TransferResponse> outflow(TransferRequest request);

    ResponseEntity<TransferResponse> inflow(InflowRequest request);

    CustomPageResponse<TransactionLog> getLogByStatus(int page, int size, String startDate, String endDate, String status) throws ParseException;

    CustomPageResponse<TransactionLog> getLogByAcctId(int page, int size, String startDate, String endDate, String accountId) throws ParseException;
}
