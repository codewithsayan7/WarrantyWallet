package com.codewithsayan.warrantywallet;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class WarrantyForm {
    @NotBlank(message="Enter the product name.") @Size(max=110)
    private String productName;
    @Size(max=80) private String brand;
    @NotBlank(message="Choose a category.") private String category;
    @NotNull(message="Enter the purchase date.") @PastOrPresent(message="Purchase date cannot be in the future.")
    private LocalDate purchaseDate=LocalDate.now();
    @NotNull(message="Enter warranty duration.") @Min(value=1,message="Warranty must be at least 1 month.")
    @Max(value=120,message="Warranty cannot exceed 120 months.")
    private Integer warrantyMonths=12;
    @Size(max=100) private String serialNumber;
    @Size(max=100) private String storeName;
    @Size(max=500) private String notes;

    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public String getBrand(){return brand;} public void setBrand(String v){brand=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public LocalDate getPurchaseDate(){return purchaseDate;} public void setPurchaseDate(LocalDate v){purchaseDate=v;}
    public Integer getWarrantyMonths(){return warrantyMonths;} public void setWarrantyMonths(Integer v){warrantyMonths=v;}
    public String getSerialNumber(){return serialNumber;} public void setSerialNumber(String v){serialNumber=v;}
    public String getStoreName(){return storeName;} public void setStoreName(String v){storeName=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}
