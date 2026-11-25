package co.inventario.dto;

import co.inventario.domain.InventoryMovement;
import co.inventario.domain.MovementType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementResponse(Long id, Long productId, String productName, String productSku,
                               MovementType type, Integer quantity, BigDecimal unitPriceSnapshot,
                               BigDecimal totalValue, String note, LocalDateTime createdAt) {
    public static MovementResponse from(InventoryMovement movement) {
        return new MovementResponse(movement.getId(), movement.getProduct().getId(),
                movement.getProduct().getName(), movement.getProduct().getSku(), movement.getType(),
                movement.getQuantity(), movement.getUnitPriceSnapshot(),
                movement.getUnitPriceSnapshot().multiply(BigDecimal.valueOf(movement.getQuantity())),
                movement.getNote(), movement.getCreatedAt());
    }
}
