package dot.paymentproject.entities;

import dot.paymentproject.enums.TransactionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionReference;
    private BigDecimal amount;
    private BigDecimal fees;
    private String description;
    private TransactionStatus status;
    private String commissionDetails;
    private String fromAccountNumber;
    private String fromAccountName;
    private String fromBankName;
    private String fromBankCode;
    private String toAccountNumber;
    private String toAccountName;
    private String toBankName;
    private String toBankCode;

}
