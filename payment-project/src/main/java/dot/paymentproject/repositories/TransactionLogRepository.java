package dot.paymentproject.repositories;

import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransactionLogRepository extends CrudRepository<TransactionLog, Long> {
    @Query(value = "SELECT * FROM transaction_log t WHERE t.created_at BETWEEN ?1 AND ?2", nativeQuery = true)
    Page<TransactionLog> findAllByCreatedAt(Pageable pageable);

    @Query(value = "SELECT * FROM transaction_log t WHERE t.created_at BETWEEN ?1 AND ?2 AND t.status = ?3",
            countQuery = "SELECT COUNT(*) FROM transaction_log t WHERE t.created_at BETWEEN ?1 AND ?2 AND t.status = ?3",
            nativeQuery = true)
    Page<TransactionLog> findAllByCreatedAtAndStatus(Date startDate, Date endDate, TransactionStatus status, Pageable pageable);

    @Query(value = "SELECT * FROM transaction_log t WHERE t.created_at BETWEEN ?1 AND ?2 AND t.account_id = ?3",
            countQuery = "SELECT COUNT(*) FROM transaction_log t WHERE t.created_at BETWEEN ?1 AND ?2 AND t.status = ?3",
            nativeQuery = true)
    Page<TransactionLog> findAllByCreatedAtAndFromAccountName(Date startDate, Date endDate, String accountId, Pageable pageable);

    List<TransactionLog> findAllByCreatedAtAndStatus(Date createdAt, TransactionStatus status);

    List<TransactionLog> findAllByCommissionPickedAndStatus(Boolean commissionPicked, TransactionStatus status);
}
