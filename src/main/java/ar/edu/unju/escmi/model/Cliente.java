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
    private String nombre;
    private String apellido;
    private boolean estado = true;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String dni, String domicilio) {
        this.dni = dni;
        this.domicilio = domicilio;
        this.nombre = "";
        this.apellido = "";
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
  public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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
        return "Cliente{id=" + id + ", dni='" + dni + '\'' + ", domicilio='" + domicilio + '\'' + ", nombre='" + nombre + '\'' + ", apellido='" + apellido + '\'' +
                ", estado=" + estado + '}';
    }
}
