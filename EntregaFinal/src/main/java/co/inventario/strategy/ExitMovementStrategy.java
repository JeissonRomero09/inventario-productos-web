package co.inventario.strategy;

import co.inventario.domain.MovementType;
import co.inventario.domain.Product;
import co.inventario.service.InsufficientStockException;
import org.springframework.stereotype.Component;

@Component
public class ExitMovementStrategy implements InventoryMovementStrategy {
    @Override
    public MovementType getType() {
        return MovementType.EXIT;
    }

    @Override
    public void apply(Product product, int quantity) {
        if (quantity > product.getStock()) {
            throw new InsufficientStockException(
                    "Existencias insuficientes. Disponible: " + product.getStock() + " unidades.");
        }
        product.setStock(product.getStock() - quantity);
    }
}
