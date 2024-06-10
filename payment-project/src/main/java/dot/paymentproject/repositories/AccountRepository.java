package dot.paymentproject.repositories;

import dot.paymentproject.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
