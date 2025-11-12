package ar.edu.unju.escmi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private String domicilio;

    private double total;

    private boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();

    public Factura() {
        this.fecha = LocalDate.now();
        this.total = 0.0;
        this.estado = true;
    }

    public Factura(Cliente cliente) {
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.total = 0.0;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double calcularTotal() {
        return detalles.stream()
                .mapToDouble(DetalleFactura::getSubtotal)
                .sum();
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
                + ", total=" + calcularTotal() + ", estado=" + estado + '}';
    }
}
