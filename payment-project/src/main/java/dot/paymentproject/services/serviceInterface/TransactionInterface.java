package dot.paymentproject.services.serviceInterface;

import dot.paymentproject.pojos.request.InflowRequest;
import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.TransferResponse;
import org.springframework.http.ResponseEntity;

public interface TransactionInterface {
    ResponseEntity<TransferResponse> outflow(TransferRequest request);

    ResponseEntity<TransferResponse> inflow(InflowRequest request);
}
