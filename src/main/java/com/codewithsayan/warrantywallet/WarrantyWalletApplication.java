package com.codewithsayan.warrantywallet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WarrantyWalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarrantyWalletApplication.class, args);
    }
}
