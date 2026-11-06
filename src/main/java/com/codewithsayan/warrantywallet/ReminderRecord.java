package com.codewithsayan.warrantywallet;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="reminder_records",
 uniqueConstraints=@UniqueConstraint(name="uk_reminder_item_day", columnNames={"warranty_item_id","reminder_date"}))
public class ReminderRecord {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false, fetch=FetchType.EAGER) @JoinColumn(name="warranty_item_id")
    private WarrantyItem warrantyItem;
    @Column(name="reminder_date", nullable=false) private LocalDate reminderDate;
    @Column(nullable=false, length=220) private String message;
    @Column(nullable=false) private LocalDateTime createdAt;

    protected ReminderRecord() {}
    public ReminderRecord(WarrantyItem item, LocalDate date, String message, LocalDateTime createdAt) {
        this.warrantyItem=item; this.reminderDate=date; this.message=message; this.createdAt=createdAt;
    }
    public Long getId(){return id;}
    public WarrantyItem getWarrantyItem(){return warrantyItem;}
    public LocalDate getReminderDate(){return reminderDate;}
    public String getMessage(){return message;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
