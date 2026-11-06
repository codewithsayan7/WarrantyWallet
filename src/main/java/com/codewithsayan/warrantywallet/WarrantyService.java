package com.codewithsayan.warrantywallet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WarrantyService {
    private final WarrantyItemRepository warrantyRepo;
    private final RepairRecordRepository repairRepo;
    private final ReminderRecordRepository reminderRepo;
    private final ReceiptStorageService receiptStorage;

    public WarrantyService(WarrantyItemRepository warrantyRepo, RepairRecordRepository repairRepo,
                           ReminderRecordRepository reminderRepo, ReceiptStorageService receiptStorage) {
        this.warrantyRepo=warrantyRepo; this.repairRepo=repairRepo;
        this.reminderRepo=reminderRepo; this.receiptStorage=receiptStorage;
    }

    @Transactional
    public WarrantyItem create(WarrantyForm form, String receiptName) {
        WarrantyItem item=new WarrantyItem(form.getProductName().trim(), clean(form.getBrand()),
                form.getCategory(), form.getPurchaseDate(), form.getWarrantyMonths(),
                clean(form.getSerialNumber()), clean(form.getStoreName()), clean(form.getNotes()),
                receiptName, LocalDateTime.now(), false);
        return warrantyRepo.save(item);
    }

    @Transactional
    public RepairRecord addRepair(Long id, RepairForm form) {
        WarrantyItem item=get(id);
        return repairRepo.save(new RepairRecord(item,form.getServiceDate(),form.getDescription().trim(),
                clean(form.getServiceCentre()),form.getCost()==null ? java.math.BigDecimal.ZERO : form.getCost(),
                clean(form.getNotes())));
    }

    @Transactional
    public void delete(Long id) {
        WarrantyItem item=get(id);
        repairRepo.deleteByWarrantyItemId(id);
        reminderRepo.deleteByWarrantyItemId(id);
        warrantyRepo.delete(item);
        receiptStorage.delete(item.getReceiptFileName());
    }

    @Transactional(readOnly=true)
    public WarrantyItem get(Long id) {
        return warrantyRepo.findById(id).orElseThrow(()->new IllegalArgumentException("Product record not found."));
    }
    @Transactional(readOnly=true)
    public List<WarrantyItem> all() { return warrantyRepo.findAllByOrderByExpiryDateAsc(); }
    @Transactional(readOnly=true)
    public List<RepairRecord> repairs(Long id) { return repairRepo.findByWarrantyItemIdOrderByServiceDateDesc(id); }
    @Transactional(readOnly=true)
    public List<ReminderRecord> reminders() { return reminderRepo.findTop12ByOrderByCreatedAtDesc(); }

    public long countCovered() { return all().stream().filter(x->"Covered".equals(x.getWarrantyStatus())).count(); }
    public long countSoon() { return all().stream().filter(x->"Expiring soon".equals(x.getWarrantyStatus())).count(); }
    public long countExpired() { return all().stream().filter(x->"Expired".equals(x.getWarrantyStatus())).count(); }

    @Transactional
    public void generateDailyReminders() {
        LocalDate today=LocalDate.now();
        List<WarrantyItem> targets=warrantyRepo.findByExpiryDateBetweenOrderByExpiryDateAsc(today.minusDays(7),today.plusDays(30));
        for(WarrantyItem item:targets) {
            if(reminderRepo.existsByWarrantyItemIdAndReminderDate(item.getId(),today)) continue;
            long days=java.time.temporal.ChronoUnit.DAYS.between(today,item.getExpiryDate());
            String message=days<0
                    ? item.getProductName()+" warranty expired "+(-days)+" day(s) ago."
                    : item.getProductName()+" warranty expires in "+days+" day(s).";
            reminderRepo.save(new ReminderRecord(item,today,message,LocalDateTime.now()));
        }
    }

    private String clean(String value) { return value==null || value.isBlank() ? null : value.trim(); }
}
