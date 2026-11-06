package com.codewithsayan.warrantywallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="warrantywallet.reminders.enabled",havingValue="true",matchIfMissing=true)
public class ReminderScheduler {
    private static final Logger log=LoggerFactory.getLogger(ReminderScheduler.class);
    private final WarrantyService service;

    public ReminderScheduler(WarrantyService service){this.service=service;}

    @EventListener(ApplicationReadyEvent.class)
    public void createStartupReminders(){ createReminders(); }

    @Scheduled(cron="0 0 8 * * *")
    public void createReminders(){
        service.generateDailyReminders();
        log.info("Warranty expiry reminder job completed.");
    }
}
