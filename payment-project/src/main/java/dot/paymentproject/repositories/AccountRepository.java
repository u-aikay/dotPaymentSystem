package dot.paymentproject.repositories;

import dot.paymentproject.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findAccountByAccountNumber(String accountNumber);

}
