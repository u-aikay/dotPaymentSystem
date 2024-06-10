package dot.paymentproject.controllers;

import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.pojos.request.InflowRequest;
import dot.paymentproject.pojos.request.TransferRequest;
import dot.paymentproject.pojos.response.CustomPageResponse;
import dot.paymentproject.pojos.response.TransferResponse;
import dot.paymentproject.services.serviceInterface.TransactionInterface;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("inflow")
    public ResponseEntity<TransferResponse> inflowTransaction(InflowRequest request){
        logger.info("transaction inflow initiated");
        return transactionInterface.inflow(request);
    }

    @GetMapping("get_transaction_log")
    public CustomPageResponse<TransactionLog> getTransactionLog(@RequestParam("page") int page, @RequestParam("size") int size,
                                                                @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        logger.info("fetch transaction log initiated...");
        return transactionInterface.getLog(page,size,startDate,endDate);
    }
}
