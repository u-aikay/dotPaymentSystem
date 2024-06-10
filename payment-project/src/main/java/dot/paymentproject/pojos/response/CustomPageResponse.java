package dot.paymentproject.pojos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomPageResponse<T> {

    private boolean success;
    private int start;
    private int limit;
    private long size;
    private List<T> payload;

}
