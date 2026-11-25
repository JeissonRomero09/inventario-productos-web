package co.inventario.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.inventario.domain.Product;
import co.inventario.service.InsufficientStockException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MovementStrategyTest {
    @Test
    void entryAddsUnitsToStock() {
        Product product = productWithStock(7);

        new EntryMovementStrategy().apply(product, 4);

        assertEquals(11, product.getStock());
    }

    @Test
    void exitSubtractsUnitsWhenStockIsSufficient() {
        Product product = productWithStock(7);

        new ExitMovementStrategy().apply(product, 4);

        assertEquals(3, product.getStock());
    }

    @Test
    void exitRejectsMoreUnitsThanAvailableWithoutChangingStock() {
        Product product = productWithStock(3);

        assertThrows(InsufficientStockException.class,
                () -> new ExitMovementStrategy().apply(product, 4));

        assertEquals(3, product.getStock());
    }

    private Product productWithStock(int stock) {
        return new Product("SKU-1", "Artículo", "General", "", BigDecimal.TEN, stock, 2);
    }
}
