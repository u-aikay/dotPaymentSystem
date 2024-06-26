package dot.paymentproject.enums;

import lombok.Getter;

@Getter
public enum Charges {
    TRANSACTION_FEE("0.005"),
    COMMISSION("0.2"),
    VAT("0.075");

    private final String percentageValue;

    Charges(String percentageValue) {
        this.percentageValue = percentageValue;
    }

}
