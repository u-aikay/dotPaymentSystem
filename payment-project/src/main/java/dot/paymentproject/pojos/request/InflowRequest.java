package dot.paymentproject.pojos.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InflowRequest {

    private BigDecimal amount;
    private String currencyCode;
    private String senderAccountNumber;
    private String senderAccountName;
    private String senderBankName;
    private String beneficiaryAccountNumber;
    private String beneficiaryAccountName;
    private String beneficiaryBankName;
    private String narration;
}
