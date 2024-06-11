package dot.paymentproject.entities;

import dot.paymentproject.enums.CommissionStatus;
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
public class Commission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long transactionId;
    private String transactionRef;
    private BigDecimal commissionAmount;
    private CommissionStatus commissionStatus;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
