package ar.edu.unju.escmi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Double precioUnitario;

    private Boolean estado = true;
    
    private Integer cantidad = 0;

    public Producto() {
    }

    public Producto(String descripcion, Double precioUnitario) {
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.estado = true;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Producto{id=" + id + ", descripcion='" + descripcion + '\'' + ", precioUnitario=" + precioUnitario
            + ", cantidad=" + cantidad + ", estado=" + estado + '}';
    }
}
