package dot.paymentproject.pojos.response;

import lombok.Data;

@Data
public class NIPResponse {
    private Boolean isNipUsed;
    private String message;
    private Boolean hasError;
    private String sessionID;




}
