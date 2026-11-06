package com.codewithsayan.warrantywallet;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="repair_records")
public class RepairRecord {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false, fetch=FetchType.EAGER) @JoinColumn(name="warranty_item_id")
    private WarrantyItem warrantyItem;
    @Column(nullable=false) private LocalDate serviceDate;
    @Column(nullable=false, length=180) private String description;
    @Column(length=120) private String serviceCentre;
    @Column(precision=10, scale=2) private BigDecimal cost;
    @Column(length=400) private String notes;

    protected RepairRecord() {}
    public RepairRecord(WarrantyItem item, LocalDate date, String description,
                        String centre, BigDecimal cost, String notes) {
        this.warrantyItem=item; this.serviceDate=date; this.description=description;
        this.serviceCentre=centre; this.cost=cost; this.notes=notes;
    }
    public Long getId(){return id;}
    public WarrantyItem getWarrantyItem(){return warrantyItem;}
    public LocalDate getServiceDate(){return serviceDate;}
    public String getDescription(){return description;}
    public String getServiceCentre(){return serviceCentre;}
    public BigDecimal getCost(){return cost;}
    public String getNotes(){return notes;}
}
