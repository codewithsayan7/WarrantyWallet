package com.codewithsayan.warrantywallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RepairRecordRepository extends JpaRepository<RepairRecord,Long> {
    List<RepairRecord> findByWarrantyItemIdOrderByServiceDateDesc(Long warrantyItemId);
    void deleteByWarrantyItemId(Long warrantyItemId);
}
