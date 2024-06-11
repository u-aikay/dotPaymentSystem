package dot.paymentproject.pojos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public  class TransferResponse {
    private String message;
    private Boolean success;
}
