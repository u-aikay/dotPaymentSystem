package dot.paymentproject.controllers;

import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.TransferResponse;
import dot.paymentproject.services.serviceInterface.TransactionInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dot/transaction/")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionInterface transactionInterface;;
    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping("outflow")
    public ResponseEntity<TransferResponse> outflowTransaction(TransferRequest request) {
        logger.info("transaction outflow initiated...");
        return transactionInterface.outflow(request);
    }
}
