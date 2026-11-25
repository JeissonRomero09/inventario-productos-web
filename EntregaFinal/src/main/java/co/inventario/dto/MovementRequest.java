package co.inventario.dto;

import co.inventario.domain.MovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovementRequest(
        @NotNull MovementType type,
        @NotNull @Positive Integer quantity,
        @Size(max = 255) String note) {
}
