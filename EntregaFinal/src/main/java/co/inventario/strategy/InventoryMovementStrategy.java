package co.inventario.strategy;

import co.inventario.domain.MovementType;
import co.inventario.domain.Product;

public interface InventoryMovementStrategy {
    MovementType getType();
    void apply(Product product, int quantity);
}
