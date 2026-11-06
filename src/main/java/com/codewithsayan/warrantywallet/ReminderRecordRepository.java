package com.codewithsayan.warrantywallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReminderRecordRepository extends JpaRepository<ReminderRecord,Long> {
    boolean existsByWarrantyItemIdAndReminderDate(Long warrantyItemId, LocalDate reminderDate);
    List<ReminderRecord> findTop12ByOrderByCreatedAtDesc();
    void deleteByWarrantyItemId(Long warrantyItemId);
}
