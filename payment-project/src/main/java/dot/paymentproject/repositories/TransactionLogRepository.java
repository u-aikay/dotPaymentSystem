package dot.paymentproject.repositories;

import dot.paymentproject.entities.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionLogRepository extends CrudRepository<TransactionLog, Long>{
    @Query(value = "SELECT * FROM transaction_log t WHERE t.created_at BETWEEN ?1 AND ?2", nativeQuery = true)
    Page<TransactionLog> findAllByCreatedAt(Pageable pageable);
}
