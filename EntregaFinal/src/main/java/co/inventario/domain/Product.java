package co.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Product() {
    }

    public Product(String sku, String name, String category, String description, BigDecimal unitPrice,
                   Integer stock, Integer lowStockThreshold) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.lowStockThreshold = lowStockThreshold;
    }

    public void updateDetails(String sku, String name, String category, String description, BigDecimal unitPrice,
                              Integer lowStockThreshold) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.description = description;
        this.unitPrice = unitPrice;
        this.lowStockThreshold = lowStockThreshold;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public Integer getStock() { return stock; }
    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setStock(Integer stock) { this.stock = stock; }
}
