package co.inventario.dto;

import co.inventario.domain.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(Long id, String sku, String name, String category, String description,
                              BigDecimal unitPrice, Integer stock, Integer lowStockThreshold,
                              BigDecimal stockValue, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getSku(), product.getName(), product.getCategory(),
                product.getDescription(), product.getUnitPrice(), product.getStock(),
                product.getLowStockThreshold(),
                product.getUnitPrice().multiply(BigDecimal.valueOf(product.getStock())),
                product.getCreatedAt(), product.getUpdatedAt());
    }
}
