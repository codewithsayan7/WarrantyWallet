package com.codewithsayan.warrantywallet;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WarrantyItemTests {
    @Test void calculatesExpiryDate() {
        assertEquals(LocalDate.of(2026,2,10),
                WarrantyItem.calculateExpiryDate(LocalDate.of(2025,2,10),12));
    }
    @Test void handlesEndOfMonthExpiry() {
        assertEquals(LocalDate.of(2025,2,28),
                WarrantyItem.calculateExpiryDate(LocalDate.of(2025,1,31),1));
    }
}
