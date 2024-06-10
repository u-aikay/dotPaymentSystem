package dot.paymentproject.exceptionHandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<?> handleTransactionException(TransactionException ex, WebRequest request){
        log.error("Transaction Exception: ", ex);
        ApiErrorDetails errorDetail = new ApiErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }
}
