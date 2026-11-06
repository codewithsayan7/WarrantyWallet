package com.codewithsayan.warrantywallet;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="warranty_items", indexes=@Index(name="idx_warranty_expiry", columnList="expiry_date"))
public class WarrantyItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=110) private String productName;
    @Column(length=80) private String brand;
    @Column(nullable=false, length=40) private String category;
    @Column(nullable=false) private LocalDate purchaseDate;
    @Column(nullable=false) private int warrantyMonths;
    @Column(name="expiry_date", nullable=false) private LocalDate expiryDate;
    @Column(length=100) private String serialNumber;
    @Column(length=100) private String storeName;
    @Column(length=500) private String notes;
    @Column(length=120) private String receiptFileName;
    @Column(nullable=false) private LocalDateTime createdAt;
    @Column(nullable=false) private boolean demoData;

    protected WarrantyItem() {}

    public WarrantyItem(String productName, String brand, String category, LocalDate purchaseDate,
                        int warrantyMonths, String serialNumber, String storeName, String notes,
                        String receiptFileName, LocalDateTime createdAt, boolean demoData) {
        this.productName=productName; this.brand=brand; this.category=category;
        this.purchaseDate=purchaseDate; this.warrantyMonths=warrantyMonths;
        this.expiryDate=calculateExpiryDate(purchaseDate, warrantyMonths);
        this.serialNumber=serialNumber; this.storeName=storeName; this.notes=notes;
        this.receiptFileName=receiptFileName; this.createdAt=createdAt; this.demoData=demoData;
    }

    public static LocalDate calculateExpiryDate(LocalDate purchaseDate, int months) {
        return purchaseDate.plusMonths(months);
    }

    @Transient public String getWarrantyStatus() {
        LocalDate today=LocalDate.now();
        if (expiryDate.isBefore(today)) return "Expired";
        if (!expiryDate.isAfter(today.plusDays(30))) return "Expiring soon";
        return "Covered";
    }
    @Transient public String getStatusClass() {
        return switch(getWarrantyStatus()) {
            case "Expired" -> "expired";
            case "Expiring soon" -> "soon";
            default -> "covered";
        };
    }
    @Transient public long getDaysRemaining() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
    public Long getId(){return id;}
    public String getProductName(){return productName;}
    public String getBrand(){return brand;}
    public String getCategory(){return category;}
    public LocalDate getPurchaseDate(){return purchaseDate;}
    public int getWarrantyMonths(){return warrantyMonths;}
    public LocalDate getExpiryDate(){return expiryDate;}
    public String getSerialNumber(){return serialNumber;}
    public String getStoreName(){return storeName;}
    public String getNotes(){return notes;}
    public String getReceiptFileName(){return receiptFileName;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public boolean isDemoData(){return demoData;}
}
