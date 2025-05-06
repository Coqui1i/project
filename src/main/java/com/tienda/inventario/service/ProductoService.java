package com.tienda.inventario.service;

import com.tienda.inventario.entity.ProductoEntity;
import com.tienda.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<ProductoEntity> obtenerTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProductoEntity obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public ProductoEntity crear(ProductoEntity producto) {
        if (producto.getCodigoBarras() != null &&
                productoRepository.findByCodigoBarras(producto.getCodigoBarras()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con este código de barras");
        }
        return productoRepository.save(producto);
    }

    @Transactional
    public ProductoEntity actualizar(Long id, ProductoEntity producto) {
        var existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getCodigoBarras() != null &&
                !producto.getCodigoBarras().equals(existente.getCodigoBarras()) &&
                productoRepository.findByCodigoBarras(producto.getCodigoBarras()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con este código de barras");
        }

        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setCategoria(producto.getCategoria());
        existente.setCodigoBarras(producto.getCodigoBarras());

        return productoRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<ProductoEntity> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaIgnoreCase(categoria);
    }

    @Transactional
    public ProductoEntity guardarProducto(ProductoEntity producto) {
        // Validación de código de barras único
        if (producto.getCodigoBarras() != null && !producto.getCodigoBarras().isEmpty()) {
            productoRepository.findByCodigoBarras(producto.getCodigoBarras())
                    .ifPresent(p -> {
                        if (producto.getId() == null || !p.getId().equals(producto.getId())) {
                            throw new RuntimeException("Ya existe un producto con este código de barras");
                        }
                    });
        }

        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        // Verificar si el producto existe
        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Verificar si el producto tiene ventas asociadas
        if (!producto.getDetallesVenta().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el producto porque tiene ventas asociadas");
        }

        productoRepository.deleteById(id);
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductoEntity> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public List<ProductoEntity> productosConStockBajo() {
        return productoRepository.findByStockLessThan(10);
    }

}