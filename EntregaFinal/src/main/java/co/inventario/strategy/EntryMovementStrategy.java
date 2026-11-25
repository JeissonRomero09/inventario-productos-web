package co.inventario.strategy;

import co.inventario.domain.MovementType;
import co.inventario.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class EntryMovementStrategy implements InventoryMovementStrategy {
    @Override
    public MovementType getType() {
        return MovementType.ENTRY;
    }

    @Override
    public void apply(Product product, int quantity) {
        product.setStock(Math.addExact(product.getStock(), quantity));
    }
}
