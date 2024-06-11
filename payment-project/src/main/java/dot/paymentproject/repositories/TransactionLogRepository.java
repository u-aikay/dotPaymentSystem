package dot.paymentproject.repositories;

import dot.paymentproject.entities.TransactionLog;
import dot.paymentproject.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface TransactionLogRepository extends CrudRepository<TransactionLog, Long>{
    @Query(value = "SELECT * FROM transaction_log t WHERE t.created_at BETWEEN ?1 AND ?2", nativeQuery = true)
    Page<TransactionLog> findAllByCreatedAt(Pageable pageable);

    Page<TransactionLog> findAllByCreatedAtAndStatus(Pageable pageable, TransactionStatus status);
    Page<TransactionLog> findAllByCreatedAtAndFromAccountName(Pageable pageable, String accountId);

    List<TransactionLog> findAllByCreatedAtAndStatus(Date createdAt, TransactionStatus status);

    List<TransactionLog> findAllByCommissionPickedAndStatus(Boolean commissionPicked, TransactionStatus status);
}
