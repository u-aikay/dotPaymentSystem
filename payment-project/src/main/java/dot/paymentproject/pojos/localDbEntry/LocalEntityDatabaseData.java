package dot.paymentproject.pojos.localDbEntry;

import dot.paymentproject.entities.Account;
import dot.paymentproject.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalEntityDatabaseData {
    public static List<Account> accountDB() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1L,"0012345678", "Uche Ikenna", "Savings","aikay@yopmail.com","c178c872-5541-4bdf-9459-bfd0d9c57f27","1234", new BigDecimal("167298.52"), AccountStatus.FREE, true, null, new Date(), null));
        accounts.add(new Account(2L,"0012345679", "Artus Fulks", "Current","afulks0@timesonline.co.uk","dde8ccae-af00-4b51-8853-c14d8e72dbde","2345", new BigDecimal("3318.75"), AccountStatus.PENDING, true, null, new Date(), null));
        accounts.add(new Account(3L, "0012345680", "Bert Enoch", "Savings","benoch1@timesonline.co.uk","abf83116-91c8-4235-b478-94e29d04eaee","3456", new BigDecimal("18008.53"), AccountStatus.FREE, true, null, new Date(), null));
        accounts.add(new Account(4L, "0012345681", "Marwin Damrel", "Current","mdamrel2@cornell.edu","7c22b2d2-2441-40e0-aaea-624a5e6b8485","4567", new BigDecimal("21074.65"), AccountStatus.LOCKED, false, "SUSPECTED FRAUD", new Date(), null));
        accounts.add(new Account(5L, "0012345682", "Lesli Sammon", "Savings","lsammon3@state.tx.us","043f54fa-44fd-4b8d-a257-432caeaf1472","5678", new BigDecimal("78415.01"), AccountStatus.FREE, true, null, new Date(), null));
        accounts.add(new Account(6L, "0012345683", "Jeanne De'Ath", "Current","jdeath4@earthlink.net","f8ff16a0-6e79-4bcb-af23-58eaa9a6ef4a","6789", new BigDecimal("78911.69"), AccountStatus.FREE, true, null, new Date(), null));
        accounts.add(new Account(7L, "0012345684", "Dew Legrice", "Savings","dlegrice5@mayoclinic.com","db2b7e92-0523-4c9c-86f8-a1c4dead1039","7890", new BigDecimal("52514.18"), AccountStatus.LOCKED, false, "SUSPECTED FRAUD", new Date(), null));
        accounts.add(new Account(8L, "0012345685", "Aundrea Silcox", "Current","asilcox6@mozilla.com","4af380eb-e327-46af-a6b2-f4e258c66dad","8901", new BigDecimal("92045.06"), AccountStatus.FREE, true, null, new Date(), null));
        accounts.add(new Account(9L, "0012345686", "Lavena Stanyer", "Savings","lstanyer7@businessinsider.com","83abfc10-b9b1-4da4-bab4-203357f8a131","9012", new BigDecimal("23048.54"), AccountStatus.FREE, true, null, new Date(), null));
        accounts.add(new Account(10L, "0012345687", "Tori Vaan", "Current","tvaan8@live.com","61536976-237d-4013-8455-c2dcf3376bf7","0123", new BigDecimal("68517.95"), AccountStatus.PENDING, true, null, new Date(), null));

        return accounts;
    }
}
