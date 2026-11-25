package co.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, columnDefinition = "ENUM('ENTRY','EXIT')")
    private MovementType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_snapshot", nullable = false, precision = 14, scale = 2)
    private BigDecimal unitPriceSnapshot;

    @Column(nullable = false, length = 255)
    private String note;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected InventoryMovement() {
    }

    public InventoryMovement(Product product, MovementType type, Integer quantity,
                             BigDecimal unitPriceSnapshot, String note) {
        this.product = product;
        this.type = type;
        this.quantity = quantity;
        this.unitPriceSnapshot = unitPriceSnapshot;
        this.note = note;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public MovementType getType() { return type; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPriceSnapshot() { return unitPriceSnapshot; }
    public String getNote() { return note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
