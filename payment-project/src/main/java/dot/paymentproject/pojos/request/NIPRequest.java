package dot.paymentproject.pojos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NIPRequest {

    private String amount;
    private String beneficiaryAccountName;
    private String beneficiaryAccountNumber;
    private String beneficiaryBankVerificationNumber;
    private String beneficiaryKYCLevel;
    private String channelCode;
    private String destinationInstitutionCode;
    private String nameEnquiryRef;
    private String narration;
    private String originatorAccountName;
    private String originatorAccountNumber;
    private String originatorBankVerificationNumber;
    private String originatorInstitutionCode;
    private String originatorKYCLevel;
    private String sessionID;
    private String fee;
}
