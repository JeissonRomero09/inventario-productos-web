package co.edu.poli.sw2.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.poli.sw2.inventario.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
}
