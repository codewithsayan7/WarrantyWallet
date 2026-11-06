package com.codewithsayan.warrantywallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WarrantyServiceTests {

    private WarrantyItemRepository warrantyRepo;
    private RepairRecordRepository repairRepo;
    private ReminderRecordRepository reminderRepo;
    private ReceiptStorageService receiptStorage;
    private WarrantyService service;

    @BeforeEach
    void setUp() {
        warrantyRepo = mock(WarrantyItemRepository.class);
        repairRepo = mock(RepairRecordRepository.class);
        reminderRepo = mock(ReminderRecordRepository.class);
        receiptStorage = mock(ReceiptStorageService.class);
        service = new WarrantyService(warrantyRepo, repairRepo, reminderRepo, receiptStorage);
    }

    @Test
    void createsProductAndTrimsOptionalText() {
        WarrantyForm form = new WarrantyForm();
        form.setProductName("  Laptop  ");
        form.setBrand("  Example Brand  ");
        form.setCategory("Computer");
        form.setPurchaseDate(LocalDate.of(2025, 11, 6));
        form.setWarrantyMonths(24);
        form.setSerialNumber("  SN-123  ");
        form.setStoreName("  Local Store  ");
        form.setNotes("   ");

        when(warrantyRepo.save(any(WarrantyItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WarrantyItem saved = service.create(form, "receipt.pdf");

        assertEquals("Laptop", saved.getProductName());
        assertEquals("Example Brand", saved.getBrand());
        assertEquals(LocalDate.of(2027, 11, 6), saved.getExpiryDate());
        assertEquals("SN-123", saved.getSerialNumber());
        assertEquals("Local Store", saved.getStoreName());
        assertNull(saved.getNotes());
        assertEquals("receipt.pdf", saved.getReceiptFileName());
        assertFalse(saved.isDemoData());
        verify(warrantyRepo).save(any(WarrantyItem.class));
    }

    @Test
    void addsRepairToExistingProductAndDefaultsMissingCostToZero() {
        WarrantyItem item = new WarrantyItem(
                "Laptop", "Example Brand", "Computer",
                LocalDate.of(2025, 1, 10), 24,
                null, null, null, null, LocalDateTime.now(), false
        );
        when(warrantyRepo.findById(7L)).thenReturn(Optional.of(item));
        when(repairRepo.save(any(RepairRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RepairForm form = new RepairForm();
        form.setServiceDate(LocalDate.of(2026, 10, 2));
        form.setDescription("  Keyboard replacement  ");
        form.setServiceCentre("  Repair Centre  ");
        form.setCost(null);
        form.setNotes("  Checked and tested  ");

        RepairRecord saved = service.addRepair(7L, form);

        assertSame(item, saved.getWarrantyItem());
        assertEquals("Keyboard replacement", saved.getDescription());
        assertEquals("Repair Centre", saved.getServiceCentre());
        assertEquals(BigDecimal.ZERO, saved.getCost());
        assertEquals("Checked and tested", saved.getNotes());
        verify(repairRepo).save(any(RepairRecord.class));
    }

    @Test
    void createsDueAndRecentlyExpiredRemindersButSkipsExistingDailyReminder() {
        LocalDate today = LocalDate.now();
        WarrantyItem expiring = warrantyItem(1L, "Phone", today.plusDays(10));
        WarrantyItem recentlyExpired = warrantyItem(2L, "Monitor", today.minusDays(3));
        WarrantyItem alreadyReminded = warrantyItem(3L, "Tablet", today.plusDays(5));

        when(warrantyRepo.findByExpiryDateBetweenOrderByExpiryDateAsc(
                today.minusDays(7), today.plusDays(30)))
                .thenReturn(List.of(expiring, recentlyExpired, alreadyReminded));
        when(reminderRepo.existsByWarrantyItemIdAndReminderDate(eq(3L), eq(today)))
                .thenReturn(true);

        service.generateDailyReminders();

        ArgumentCaptor<ReminderRecord> captor = ArgumentCaptor.forClass(ReminderRecord.class);
        verify(reminderRepo, times(2)).save(captor.capture());

        List<ReminderRecord> created = captor.getAllValues();
        assertEquals(2, created.size());

        assertEquals(expiring, created.get(0).getWarrantyItem());
        assertEquals(today, created.get(0).getReminderDate());
        assertEquals("Phone warranty expires in 10 day(s).", created.get(0).getMessage());

        assertEquals(recentlyExpired, created.get(1).getWarrantyItem());
        assertEquals("Monitor warranty expired 3 day(s) ago.", created.get(1).getMessage());

        verify(reminderRepo).existsByWarrantyItemIdAndReminderDate(3L, today);
    }

    @Test
    void deletingProductAlsoDeletesRelatedRecordsAndReceipt() {
        WarrantyItem item = new WarrantyItem(
                "Camera", "Example", "Electronics",
                LocalDate.of(2025, 5, 1), 12,
                null, null, null, "camera-receipt.pdf", LocalDateTime.now(), false
        );
        when(warrantyRepo.findById(12L)).thenReturn(Optional.of(item));

        service.delete(12L);

        verify(repairRepo).deleteByWarrantyItemId(12L);
        verify(reminderRepo).deleteByWarrantyItemId(12L);
        verify(warrantyRepo).delete(item);
        verify(receiptStorage).delete("camera-receipt.pdf");
    }

    private WarrantyItem warrantyItem(Long id, String name, LocalDate expiryDate) {
        WarrantyItem item = mock(WarrantyItem.class);
        when(item.getId()).thenReturn(id);
        when(item.getProductName()).thenReturn(name);
        when(item.getExpiryDate()).thenReturn(expiryDate);
        return item;
    }
}
