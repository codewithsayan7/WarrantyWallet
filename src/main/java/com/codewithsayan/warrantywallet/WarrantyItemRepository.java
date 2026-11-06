package com.codewithsayan.warrantywallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface WarrantyItemRepository extends JpaRepository<WarrantyItem,Long> {
    List<WarrantyItem> findAllByOrderByExpiryDateAsc();
    List<WarrantyItem> findByExpiryDateBetweenOrderByExpiryDateAsc(LocalDate start, LocalDate end);
    long countByExpiryDateBefore(LocalDate date);
}
