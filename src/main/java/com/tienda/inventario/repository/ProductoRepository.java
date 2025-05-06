package com.tienda.inventario.repository;

import com.tienda.inventario.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    List<ProductoEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ProductoEntity> findByCategoriaIgnoreCase(String categoria);
    Optional<ProductoEntity> findByCodigoBarras(String codigoBarras);
    List<ProductoEntity> findByStockLessThan(Integer stock);
}