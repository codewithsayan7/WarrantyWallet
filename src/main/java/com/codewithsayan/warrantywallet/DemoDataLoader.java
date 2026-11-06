package com.codewithsayan.warrantywallet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DemoDataLoader implements CommandLineRunner {
    private final WarrantyItemRepository repo;
    public DemoDataLoader(WarrantyItemRepository repo){this.repo=repo;}
    @Override public void run(String... args) {
        if(repo.count()>0) return;
        LocalDate today=LocalDate.now();
        repo.save(new WarrantyItem("Demo Laptop","Lenovo","Computer",today.minusMonths(11).minusDays(10),12,
                "DEMO-LAPTOP-001","Demo Electronics Store","Illustrative sample record.",null,LocalDateTime.now(),true));
        repo.save(new WarrantyItem("Demo Refrigerator","Samsung","Home appliance",today.minusMonths(22),24,
                "DEMO-FRIDGE-002","Demo Home Store","Illustrative sample record.",null,LocalDateTime.now(),true));
        repo.save(new WarrantyItem("Demo Smartphone","Motorola","Mobile",today.minusMonths(16),12,
                "DEMO-PHONE-003","Demo Mobile Store","Illustrative sample record.",null,LocalDateTime.now(),true));
    }
}
