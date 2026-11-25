package co.inventario.repository;

import co.inventario.domain.InventoryMovement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    List<InventoryMovement> findTop200ByOrderByCreatedAtDesc();
    List<InventoryMovement> findTop200ByTypeOrderByCreatedAtDesc(co.inventario.domain.MovementType type);
    List<InventoryMovement> findTop100ByProductIdOrderByCreatedAtDesc(Long productId);
}
