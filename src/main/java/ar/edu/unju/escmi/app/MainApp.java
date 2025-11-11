package ar.edu.unju.escmi.app;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.*;
import ar.edu.unju.escmi.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private static final ProductoDAO productoDAO = new ProductoDAOImpl();
    private static final FacturaDAO facturaDAO = new FacturaDAOImpl();
    // detalleDAO no es necesario actualmente (la relación se maneja por cascade desde Factura)

    public static void main(String[] args) {
        try {
            loopMenu();
        } finally {
            EmfSingleton.close();
            scanner.close();
        }
    }

    private static void loopMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int opt = Integer.parseInt(scanner.nextLine().trim());
                switch (opt) {
                    case 1 -> altaCliente();
                    case 2 -> altaProducto();
                    case 3 -> realizarVenta();
                    case 4 -> buscarFactura();
                    case 5 -> eliminarFactura();
                    case 6 -> eliminarProducto();
                    case 7 -> modificarCliente();
                    case 8 -> modificarPrecioProducto();
                    case 9 -> eliminarProducto(); // duplicate in list, keep same
                    case 10 -> mostrarTodasFacturas();
                    case 11 -> mostrarTodosClientes();
                    case 12 -> mostrarFacturasMayorA();
                    case 0 -> running = false;
                    default -> System.out.println("Opción no válida");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Entrada inválida. Ingrese un número.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Menú ---");
        System.out.println("1- Alta de cliente");
        System.out.println("2- Alta de producto");
        System.out.println("3- Realizar la venta de productos (Alta de factura)");
        System.out.println("4- Buscar una factura por número");
        System.out.println("5- Eliminar una factura (lógica)");
        System.out.println("6- Eliminar un producto (lógica)");
        System.out.println("7- Modificar datos de cliente");
        System.out.println("8- Modificar precio de producto");
        System.out.println("9- Eliminar producto (lógica)");
        System.out.println("10- Mostrar todas las facturas");
        System.out.println("11- Mostrar todos los clientes");
        System.out.println("12- Mostrar facturas > $500000");
        System.out.println("0- Salir");
        System.out.print("Elija una opción: ");
    }

    private static void altaCliente() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        Cliente c = new Cliente(nombre, apellido, dni, direccion, telefono);
        clienteDAO.save(c);
        System.out.println("Cliente guardado con ID: " + c.getId());
    }

    private static void altaProducto() {
        System.out.print("Nombre producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Precio (ej: 1234.56): ");
        BigDecimal precio = new BigDecimal(scanner.nextLine());
        System.out.print("Stock (entero): ");
        int stock = Integer.parseInt(scanner.nextLine());

        Producto p = new Producto(nombre, precio, stock);
        productoDAO.save(p);
        System.out.println("Producto guardado con ID: " + p.getId());
    }

    private static void realizarVenta() {
        System.out.print("ID cliente (o ENTER para listar y elegir): ");
        String line = scanner.nextLine().trim();
        Long clienteId = null;
        if (line.isEmpty()) {
            mostrarTodosClientes();
            System.out.print("Ingrese ID cliente: ");
            clienteId = Long.parseLong(scanner.nextLine());
        } else {
            clienteId = Long.parseLong(line);
        }
        Cliente cliente = clienteDAO.findById(clienteId);
        if (cliente == null || !cliente.isEstado()) {
            System.out.println("Cliente no encontrado o está dado de baja.");
            return;
        }

        Factura factura = new Factura(cliente);

        boolean adding = true;
        while (adding) {
            mostrarProductosActivos();
            System.out.print("ID producto a agregar (o 0 para terminar): ");
            Long pid = Long.parseLong(scanner.nextLine());
            if (pid == 0)
                break;
            Producto producto = productoDAO.findById(pid);
            if (producto == null || !producto.isEstado()) {
                System.out.println("Producto no encontrado o está dado de baja.");
                continue;
            }
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            if (cantidad <= 0) {
                System.out.println("Cantidad inválida.");
                continue;
            }
            if (producto.getStock() < cantidad) {
                System.out.println("No hay stock suficiente. Stock actual: " + producto.getStock());
                continue;
            }
            DetalleFactura detalle = new DetalleFactura(producto, cantidad, producto.getPrecio());
            factura.addDetalle(detalle);

            // reducir stock y actualizar producto
            producto.setStock(producto.getStock() - cantidad);
            productoDAO.update(producto);
        }

        facturaDAO.save(factura);
        System.out.println("Factura registrada con ID: " + factura.getId() + " - Total: " + factura.getTotal());
    }

    private static void buscarFactura() {
        System.out.print("Número (ID) de factura: ");
        Long id = Long.parseLong(scanner.nextLine());
        Factura f = facturaDAO.findById(id);
        if (f == null) {
            System.out.println("Factura no encontrada.");
            return;
        }
        System.out.println(f);
        f.getDetalles().forEach(d -> System.out.println("  " + d));
    }

    private static void eliminarFactura() {
        System.out.print("ID factura a eliminar (lógica): ");
        Long id = Long.parseLong(scanner.nextLine());
        Factura f = facturaDAO.findById(id);
        if (f == null) {
            System.out.println("Factura no encontrada.");
            return;
        }
        facturaDAO.delete(f);
        System.out.println("Factura marcada como eliminada (estado=false). ID: " + id);
    }

    private static void eliminarProducto() {
        System.out.print("ID producto a eliminar (lógica): ");
        Long id = Long.parseLong(scanner.nextLine());
        Producto p = productoDAO.findById(id);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        productoDAO.delete(p);
        System.out.println("Producto marcado como eliminado (estado=false). ID: " + id);
    }

    private static void modificarCliente() {
        System.out.print("ID cliente a modificar: ");
        Long id = Long.parseLong(scanner.nextLine());
        Cliente c = clienteDAO.findById(id);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.print("Nuevo nombre (actual: " + c.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty())
            c.setNombre(nombre);
        System.out.print("Nuevo apellido (actual: " + c.getApellido() + "): ");
        String apellido = scanner.nextLine();
        if (!apellido.isEmpty())
            c.setApellido(apellido);
        System.out.print("Nueva dirección (actual: " + c.getDireccion() + "): ");
        String dir = scanner.nextLine();
        if (!dir.isEmpty())
            c.setDireccion(dir);
        System.out.print("Nuevo teléfono (actual: " + c.getTelefono() + "): ");
        String tel = scanner.nextLine();
        if (!tel.isEmpty())
            c.setTelefono(tel);

        clienteDAO.update(c);
        System.out.println("Cliente actualizado.");
    }

    private static void modificarPrecioProducto() {
        System.out.print("ID producto a modificar precio: ");
        Long id = Long.parseLong(scanner.nextLine());
        Producto p = productoDAO.findById(id);
        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }
        System.out.print("Nuevo precio: ");
        BigDecimal precio = new BigDecimal(scanner.nextLine());
        p.setPrecio(precio);
        productoDAO.update(p);
        System.out.println("Precio actualizado.");
    }

    private static void mostrarTodasFacturas() {
        List<Factura> facturas = facturaDAO.findAll();
        facturas.forEach(f -> {
            System.out.println(f);
            f.getDetalles().forEach(d -> System.out.println("  " + d));
        });
    }

    private static void mostrarTodosClientes() {
        List<Cliente> clientes = clienteDAO.findAll();
        clientes.forEach(System.out::println);
    }

    private static void mostrarFacturasMayorA() {
        BigDecimal umbral = BigDecimal.valueOf(500000L);
        List<Factura> res = facturaDAO.findByTotalGreaterThan(umbral);
        if (res.isEmpty())
            System.out.println("No hay facturas mayores a " + umbral);
        res.forEach(f -> System.out.println(f));
    }

    private static void mostrarProductosActivos() {
        List<Producto> activos = productoDAO.findActive();
        activos.forEach(System.out::println);
    }
}
