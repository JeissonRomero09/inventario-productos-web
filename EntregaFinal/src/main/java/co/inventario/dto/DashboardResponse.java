package co.inventario.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(long productCount, long totalUnits, long lowStockCount,
                                BigDecimal stockValue, List<ProductResponse> lowStockProducts,
                                List<MovementResponse> recentMovements) {
}
