package ar.edu.unju.escmi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();

    private boolean estado = true; // eliminación lógica

    public Factura() {
    }

    public Factura(Cliente cliente) {
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.estado = true;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void addDetalle(DetalleFactura detalle) {
        detalle.setFactura(this);
        detalles.add(detalle);
    }

    public BigDecimal getTotal() {
        return detalles.stream()
                .map(DetalleFactura::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Factura{id=" + id + ", fecha=" + fecha + ", cliente=" + (cliente != null ? cliente.getId() : null)
                + ", total=" + getTotal() + ", estado=" + estado + '}';
    }
}
