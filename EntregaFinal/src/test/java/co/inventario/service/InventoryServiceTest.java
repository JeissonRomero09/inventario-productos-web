package co.inventario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.inventario.domain.InventoryMovement;
import co.inventario.domain.MovementType;
import co.inventario.domain.Product;
import co.inventario.dto.ProductRequest;
import co.inventario.dto.ProductResponse;
import co.inventario.repository.InventoryMovementRepository;
import co.inventario.repository.ProductRepository;
import co.inventario.strategy.EntryMovementStrategy;
import co.inventario.strategy.ExitMovementStrategy;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class InventoryServiceTest {
    @Test
    void creatingProductWithOpeningStockRecordsAnEntry() {
        ProductRepository products = org.mockito.Mockito.mock(ProductRepository.class);
        InventoryMovementRepository movements = org.mockito.Mockito.mock(InventoryMovementRepository.class);
        when(products.saveAndFlush(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(products.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        InventoryService service = new InventoryService(products, movements,
                List.of(new EntryMovementStrategy(), new ExitMovementStrategy()));

        ProductResponse created = service.createProduct(new ProductRequest(
                "SKU-OPENING", "Teclado", "Accesorios", "", BigDecimal.valueOf(80000), 6, 2));

        assertEquals(6, created.stock());
        ArgumentCaptor<InventoryMovement> movement = ArgumentCaptor.forClass(InventoryMovement.class);
        verify(movements).save(movement.capture());
        assertEquals(MovementType.ENTRY, movement.getValue().getType());
        assertEquals(6, movement.getValue().getQuantity());
        assertEquals("Existencias iniciales", movement.getValue().getNote());
    }
}
