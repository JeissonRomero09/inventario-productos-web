package co.edu.poli.sw2.inventario.controller;

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

import co.edu.poli.sw2.inventario.dto.ProductoDto;
import co.edu.poli.sw2.inventario.model.Producto;
import co.edu.poli.sw2.inventario.service.ServicioInventario;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ServicioInventario servicioInventario;

    public ProductoController(ServicioInventario servicioInventario) {
        this.servicioInventario = servicioInventario;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return servicioInventario.listarProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioInventario.obtenerProductoPorId(id));
    }

    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return servicioInventario.buscarPorNombre(nombre);
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoDto productoDto) {
        Producto productoCreado = servicioInventario.crearProducto(productoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDto productoDto) {
        Producto productoActualizado = servicioInventario.actualizarProducto(id, productoDto);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        servicioInventario.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}