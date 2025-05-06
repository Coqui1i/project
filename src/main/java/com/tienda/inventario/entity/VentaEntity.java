package com.tienda.inventario.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "ventas")
public class VentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserEntity usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVentaEntity> detalles;

    @Column(length = 100)
    private String observaciones;

    @Column(length = 20)
    private String estado; // PENDIENTE, COMPLETADA, CANCELADA

    @PrePersist
    protected void onCreate() {
        fecha = LocalDateTime.now();
        if (estado == null) {
            estado = "COMPLETADA"; // Valor por defecto
        }
    }

    public void calcularTotal() {
        if (detalles != null) {
            this.total = detalles.stream()
                    .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
                    .sum();
        } else {
            this.total = 0.0;
        }
    }

    public void agregarDetalle(DetalleVentaEntity detalle) {
        detalle.setVenta(this);
        this.detalles.add(detalle);
        calcularTotal();
    }
}