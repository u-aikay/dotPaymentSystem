package dot.paymentproject.entities;

import dot.paymentproject.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    private String accountId;
    private BigDecimal transactionFee;
    private String description;
    private TransactionStatus status;
    private String commissionDetails;
    private BigDecimal commissionAmount;
    private Boolean commissionPicked = false;
    private String fromAccountNumber;
    private String fromAccountName;
    private String fromBankName;
    private String fromBankCode;
    private String toAccountNumber;
    private String toAccountName;
    private String toBankName;
    private String toBankCode;
    private String direction;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
