package co.inventario.web;

import co.inventario.domain.MovementType;
import co.inventario.dto.DashboardResponse;
import co.inventario.dto.MovementRequest;
import co.inventario.dto.MovementResponse;
import co.inventario.dto.ProductRequest;
import co.inventario.dto.ProductResponse;
import co.inventario.service.InventoryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class InventoryController {
    private final InventoryService inventory;

    public InventoryController(InventoryService inventory) {
        this.inventory = inventory;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        return inventory.getDashboard();
    }

    @GetMapping("/products")
    public List<ProductResponse> products() {
        return inventory.getProducts();
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = inventory.createProduct(request);
        return ResponseEntity.created(URI.create("/api/products/" + created.id())).body(created);
    }

    @PutMapping("/products/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return inventory.updateProduct(id, request);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        inventory.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movements")
    public List<MovementResponse> movements(@RequestParam(required = false) MovementType type) {
        return inventory.getMovements(type);
    }

    @GetMapping("/products/{id}/movements")
    public List<MovementResponse> productMovements(@PathVariable Long id) {
        return inventory.getProductMovements(id);
    }

    @PostMapping("/products/{id}/movements")
    public ResponseEntity<MovementResponse> registerMovement(
            @PathVariable Long id, @Valid @RequestBody MovementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory.registerMovement(id, request));
    }
}
