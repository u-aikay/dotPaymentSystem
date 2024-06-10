package dot.paymentproject.pojos.request;

import lombok.Data;

import java.math.BigDecimal;

public @Data class TransferRequest {
    private String pin;
    private BigDecimal amount;
    private String currencyCode;
    private String senderAccountNumber;
    private String destinationBankCode;
    private String nameEnquirySessionId;
    private String beneficiaryAccountNumber;
    private String beneficiaryAccountName;
    private String beneficiaryBankName;
    private String narration;
    private String channelCode;
}
