package dot.paymentproject.exceptionHandlers;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorDetails {
    private LocalDateTime timeStamp;
    private String message;
    private String details;

    public ApiErrorDetails(String message, String details){
        this.message =message;
        this.details = details;
        this.timeStamp = LocalDateTime.now();
    }
}
