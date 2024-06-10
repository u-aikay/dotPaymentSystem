package dot.paymentproject.repositories;

import dot.paymentproject.entities.TransactionLog;
import org.springframework.data.repository.CrudRepository;

public interface TransactionLogRepository extends CrudRepository<TransactionLog, Long>{
}
