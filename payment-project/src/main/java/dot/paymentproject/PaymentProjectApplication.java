package dot.paymentproject;

import dot.paymentproject.repositories.AccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static dot.paymentproject.pojos.localDbEntry.LocalEntityDatabaseData.accountDB;

@SpringBootApplication
public class PaymentProjectApplication {
    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init(){
        accountRepository.saveAll(accountDB());
    }

    public static void main(String[] args) {
        SpringApplication.run(PaymentProjectApplication.class, args);
    }

}
