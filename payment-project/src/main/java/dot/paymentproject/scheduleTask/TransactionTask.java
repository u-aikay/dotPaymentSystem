package dot.paymentproject.scheduleTask;

import dot.paymentproject.services.serviceImplementation.TransactionImplementation;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionTask {
    private final TransactionImplementation transactionImplementation;
    Logger logger = LoggerFactory.getLogger(TransactionTask.class);

    @Scheduled(cron = "0 1 * * *")
    @SchedulerLock(name = "transaction_log_cron", lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    public void transactionLogCron() {
        logger.info("running cron job to pick end of the day transaction...");
        transactionImplementation.getDailySummary();
    }
}
