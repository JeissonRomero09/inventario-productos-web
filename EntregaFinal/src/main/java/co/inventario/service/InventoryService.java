package co.inventario.service;

import co.inventario.domain.InventoryMovement;
import co.inventario.domain.MovementType;
import co.inventario.domain.Product;
import co.inventario.dto.DashboardResponse;
import co.inventario.dto.MovementRequest;
import co.inventario.dto.MovementResponse;
import co.inventario.dto.ProductRequest;
import co.inventario.dto.ProductResponse;
import co.inventario.repository.InventoryMovementRepository;
import co.inventario.repository.ProductRepository;
import co.inventario.strategy.InventoryMovementStrategy;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final ProductRepository products;
    private final InventoryMovementRepository movements;
    private final Map<MovementType, InventoryMovementStrategy> strategies;

    public InventoryService(ProductRepository products, InventoryMovementRepository movements,
                            List<InventoryMovementStrategy> strategies) {
        this.products = products;
        this.movements = movements;
        this.strategies = new EnumMap<>(MovementType.class);
        for (InventoryMovementStrategy strategy : strategies) {
            this.strategies.put(strategy.getType(), strategy);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        return products.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(ProductResponse::from).toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product(request.sku().trim(), request.name().trim(), request.category().trim(),
                safeDescription(request.description()), request.unitPrice(), 0,
                request.lowStockThreshold());
        product = products.saveAndFlush(product);
        if (request.stock() > 0) {
            InventoryMovementStrategy entryStrategy = strategies.get(MovementType.ENTRY);
            entryStrategy.apply(product, request.stock());
            products.save(product);
            movements.save(new InventoryMovement(product, MovementType.ENTRY, request.stock(),
                    product.getUnitPrice(), "Existencias iniciales"));
        }
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = getProduct(id);
        product.updateDetails(request.sku().trim(), request.name().trim(), request.category().trim(),
                safeDescription(request.description()), request.unitPrice(), request.lowStockThreshold());
        return ProductResponse.from(products.saveAndFlush(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProduct(id);
        try {
            products.delete(product);
            products.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException("No se puede eliminar un artículo que ya tiene movimientos.");
        }
    }

    @Transactional
    public MovementResponse registerMovement(Long productId, MovementRequest request) {
        Product product = products.findByIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artículo solicitado."));
        InventoryMovementStrategy strategy = strategies.get(request.type());
        if (strategy == null) {
            throw new IllegalArgumentException("Tipo de movimiento no soportado.");
        }
        strategy.apply(product, request.quantity());
        products.save(product);
        InventoryMovement movement = new InventoryMovement(product, request.type(), request.quantity(),
                product.getUnitPrice(), safeDescription(request.note()));
        return MovementResponse.from(movements.saveAndFlush(movement));
    }

    @Transactional(readOnly = true)
    public List<MovementResponse> getMovements(MovementType type) {
        List<InventoryMovement> result = type == null
                ? movements.findTop200ByOrderByCreatedAtDesc()
                : movements.findTop200ByTypeOrderByCreatedAtDesc(type);
        return result.stream().map(MovementResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MovementResponse> getProductMovements(Long productId) {
        getProduct(productId);
        return movements.findTop100ByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(MovementResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        List<Product> allProducts = products.findAll(Sort.by(Sort.Direction.ASC, "name"));
        List<Product> lowStock = allProducts.stream()
                .filter(product -> product.getStock() <= product.getLowStockThreshold()).toList();
        long totalUnits = allProducts.stream().mapToLong(Product::getStock).sum();
        BigDecimal value = allProducts.stream()
                .map(product -> product.getUnitPrice().multiply(BigDecimal.valueOf(product.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<MovementResponse> recent = movements.findTop200ByOrderByCreatedAtDesc().stream()
                .limit(8).map(MovementResponse::from).toList();
        return new DashboardResponse(allProducts.size(), totalUnits, lowStock.size(), value,
                lowStock.stream().map(ProductResponse::from).toList(), recent);
    }

    private Product getProduct(Long id) {
        return products.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el artículo solicitado."));
    }

    private String safeDescription(String value) {
        return value == null ? "" : value.trim();
    }
}
