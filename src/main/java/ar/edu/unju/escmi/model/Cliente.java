package ar.edu.unju.escmi.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;
    private String domicilio;
    private double total;
    private boolean estado = true;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String dni, String domicilio) {
        this.dni = dni;
        this.domicilio = domicilio;
        this.total = 0.0;
        this.estado = true;
    }

    public Long getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", dni='" + dni + '\'' + ", domicilio='" + domicilio + '\'' + ", total=" + total
                + ", estado=" + estado + '}';
    }
}
