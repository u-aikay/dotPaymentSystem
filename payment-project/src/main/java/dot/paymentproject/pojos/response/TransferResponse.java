package dot.paymentproject.pojos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public  class TransferResponse {
    private String message;
    private Boolean success;
}
