package co.edu.poli.sw2.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.poli.sw2.inventario.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}