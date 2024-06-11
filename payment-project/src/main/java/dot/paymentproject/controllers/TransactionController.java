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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * This is the main controller for handling transaction related requests.
 * It provides endpoints for initiating outflow and inflow transactions and for fetching transaction logs.
 */
@RestController
@RequestMapping("/api/v1/dot/transaction/")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionInterface transactionInterface;;
    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    /**
     * This endpoint is used to initiate an outflow transaction.
     * @param request The request body should contain the details of the outflow transaction.
     * @return Returns a response entity containing the result of the transaction.
     */
    @PostMapping("outflow")
    public ResponseEntity<TransferResponse> outflowTransaction(@RequestBody TransferRequest request) {
        logger.info("transaction outflow initiated...");
        return transactionInterface.outflow(request);
    }

    /**
     * This endpoint is used to initiate an inflow transaction.
     * @param request The request body should contain the details of the inflow transaction.
     * @return Returns a response entity containing the result of the transaction.
     */
    @PostMapping("inflow")
    public ResponseEntity<TransferResponse> inflowTransaction(@RequestBody InflowRequest request){
        logger.info("transaction inflow initiated");
        return transactionInterface.inflow(request);
    }

    /**
     * This endpoint is used to fetch transaction logs by status.
     * @param page The page number for pagination.
     * @param size The number of records per page for pagination.
     * @param startDate The start date for the transaction log.
     * @param endDate The end date for the transaction log.
     * @return Returns a custom page response containing the transaction logs.
     */
    //todo: bystatus -done
    @GetMapping("get_transaction_log/status")
    public CustomPageResponse<TransactionLog> getTransactionLogbyStatus(@RequestParam("page") int page, @RequestParam("size") int size,
                                                                        @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                                                        @RequestParam("status") String status) throws ParseException {
        logger.info("fetch transaction log by status initiated...");
        return transactionInterface.getLogByStatus(page,size,startDate,endDate, status);
    }

    //todo: by accountId
    @GetMapping("get_transaction_log/account")
    public CustomPageResponse<TransactionLog> getTransactionLogByAccount(@RequestParam("page") int page, @RequestParam("size") int size,
                                                                         @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                                                         @RequestParam("accountId")String accountId) throws ParseException {
        logger.info("fetch transaction log by accountId initiated...");
        return transactionInterface.getLogByAcctId(page,size,startDate,endDate, accountId);
    }
}