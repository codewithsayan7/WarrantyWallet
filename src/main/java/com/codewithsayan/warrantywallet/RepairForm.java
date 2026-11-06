package com.codewithsayan.warrantywallet;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RepairForm {
    @NotNull(message="Choose the service date.") @PastOrPresent(message="Service date cannot be in the future.")
    private LocalDate serviceDate=LocalDate.now();
    @NotBlank(message="Describe the service or repair.") @Size(max=180)
    private String description;
    @Size(max=120) private String serviceCentre;
    @DecimalMin(value="0.00",message="Cost cannot be negative.") @Digits(integer=8,fraction=2)
    private BigDecimal cost;
    @Size(max=400) private String notes;

    public LocalDate getServiceDate(){return serviceDate;} public void setServiceDate(LocalDate v){serviceDate=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getServiceCentre(){return serviceCentre;} public void setServiceCentre(String v){serviceCentre=v;}
    public BigDecimal getCost(){return cost;} public void setCost(BigDecimal v){cost=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}
