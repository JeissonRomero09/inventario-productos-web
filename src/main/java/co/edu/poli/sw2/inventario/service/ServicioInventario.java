package co.edu.poli.sw2.inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.poli.sw2.inventario.dto.ProductoDto;
import co.edu.poli.sw2.inventario.model.Producto;
import co.edu.poli.sw2.inventario.repository.ProductoRepository;

@Service
public class ServicioInventario {

    private final ProductoRepository productoRepository;

    public ServicioInventario(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Producto crearProducto(ProductoDto productoDto) {
        Producto producto = new Producto();
        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setPrecio(productoDto.getPrecio());
        producto.setCantidad(productoDto.getCantidad());
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, ProductoDto productoDto) {
        Producto producto = obtenerProductoPorId(id);
        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setPrecio(productoDto.getPrecio());
        producto.setCantidad(productoDto.getCantidad());
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = obtenerProductoPorId(id);
        productoRepository.delete(producto);
    }
}