package dot.paymentproject.repositories;

import dot.paymentproject.entities.Commission;
import org.springframework.data.repository.CrudRepository;

public interface CommissionRepository extends CrudRepository<Commission, Long> {
}
