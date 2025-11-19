package ar.edu.unju.escmi.app;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.*;
import ar.edu.unju.escmi.model.*;
import ar.edu.unju.escmi.util.InputUtil;

import java.time.LocalDate;
import java.util.List;

public class Principal {

    private static final IClienteDao clienteDao = new ClienteDAOImpl();
    private static final IProductoDao productoDao = new ProductoDAOImpl();
    private static final IFacturaDao facturaDao = new FacturaDAOImpl();

    public static void main(String[] args) {
        try {
            loopMenu();
        } catch (Exception e) {
            System.out.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            EmfSingleton.close();
            InputUtil.closeScanner();
        }
    }

    private static void loopMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int opt = InputUtil.inputInt("");
                switch (opt) {
                    case 1 -> altaCliente();
                    case 2 -> altaProducto();
                    case 3 -> realizarVenta();
                    case 4 -> buscarFactura();
                    case 5 -> eliminarFactura();
                    case 6 -> eliminarProducto();
                    case 7 -> modificarCliente();
                    case 8 -> modificarPrecioProducto();
                    case 9 -> eliminarProducto();
                    case 10 -> mostrarTodasFacturas();
                    case 11 -> mostrarTodosClientes();
                    case 12 -> mostrarFacturasMayorA500000();
                    case 0 -> running = false;
                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("¡Hasta luego!");
    }

    private static void printMenu() {
        System.out.println("\n========== MENÚ ==========");
        System.out.println("1- Alta de cliente");
        System.out.println("2- Alta de producto");
        System.out.println("3- Realizar venta (Alta de factura)");
        System.out.println("4- Buscar factura por ID");
        System.out.println("5- Eliminar factura");
        System.out.println("6- Eliminar producto (lógica)");
        System.out.println("7- Modificar datos de cliente");
        System.out.println("8- Modificar precio de producto");
        System.out.println("9- Eliminar producto (lógica)");
        System.out.println("10- Mostrar todas las facturas");
        System.out.println("11- Mostrar todos los clientes");
        System.out.println("12- Mostrar facturas > $500.000");
        System.out.println("0- Salir");
        System.out.print("Elija una opción: ");
    }

    private static void altaCliente() {
        try {
            String dni = InputUtil.inputString("DNI: ");
            String domicilio = InputUtil.inputString("Domicilio: ");

            Cliente c = new Cliente(dni, domicilio);
            clienteDao.guardarCliente(c);
            System.out.println("✓ Cliente guardado exitosamente.");
        } catch (Exception e) {
            System.out.println("✗ Error al guardar cliente: " + e.getMessage());
        }
    }

    private static void altaProducto() {
        try {
            String descripcion = InputUtil.inputString("Descripción del producto: ");
            double precio = InputUtil.inputDouble("Precio unitario: ");
            int cantidad = InputUtil.inputInt("Cantidad disponible: ");

            Producto p = new Producto(descripcion, precio);
            p.setCantidad(cantidad);
            productoDao.guardarProducto(p);
            System.out.println("✓ Producto guardado exitosamente.");
        } catch (Exception e) {
            System.out.println("✗ Error al guardar producto: " + e.getMessage());
        }
    }

    private static void realizarVenta() {
        try {
            System.out.println("\n--- Nueva Factura ---");
            mostrarTodosClientes();
            String clienteBusqueda = InputUtil.inputString("DNI o ID del cliente: ");
            Cliente cliente = null;
            // Intentar buscar por ID si el usuario ingresó un número
            try {
                long clienteId = Long.parseLong(clienteBusqueda);
                List<Cliente> clientes = clienteDao.obtenerClientes();
                cliente = clientes.stream().filter(cl -> cl.getId() == clienteId).findFirst().orElse(null);
            } catch (NumberFormatException ignored) {
                // no es un número, seguiremos buscando por DNI
            }
            if (cliente == null) {
                cliente = clienteDao.buscarPorDni(clienteBusqueda);
            }
            if (cliente == null) {
                System.out.println("✗ Cliente no encontrado.");
                return;
            }

            Factura factura = new Factura(cliente);
            double totalFactura = 0.0;

            boolean addingDetalles = true;
            while (addingDetalles) {
                System.out.println("\nProductos disponibles:");
                mostrarProductosActivos();
                long productoId = InputUtil.inputLong("ID producto (0 para terminar): ");
                if (productoId == 0)
                    break;

                Producto producto = productoDao.buscarPorId(productoId);
                if (producto == null || !producto.getEstado()) {
                    System.out.println("✗ Producto no encontrado o está dado de baja.");
                    continue;
                }

                int cantidad = InputUtil.inputInt("Cantidad: ");
                if (cantidad <= 0) {
                    System.out.println("✗ Cantidad inválida.");
                    continue;
                }

                Integer stock = producto.getCantidad();
                if (stock == null) stock = 0;
                if (stock < cantidad) {
                    System.out.println("✗ Stock insuficiente. Stock actual: " + stock);
                    continue;
                }

                double subtotal = cantidad * producto.getPrecioUnitario();
                DetalleFactura detalle = new DetalleFactura(cantidad, subtotal, producto);
                factura.addDetalle(detalle);
                totalFactura += subtotal;

                // Descontar stock y actualizar el producto
                producto.setCantidad(stock - cantidad);
                productoDao.modificarProducto(producto);
            }

            if (factura.getDetalles().isEmpty()) {
                System.out.println("✗ No se agregaron productos a la factura.");
                return;
            }

            factura.setFecha(LocalDate.now());
            factura.setTotal(totalFactura);
            facturaDao.guardarFactura(factura);
            System.out.println("✓ Factura ID " + factura.getId() + " registrada. Total: $" + totalFactura);
        } catch (Exception e) {
            System.out.println("✗ Error al crear factura: " + e.getMessage());
        }
    }

    private static void buscarFactura() {
        try {
            long id = InputUtil.inputLong("ID de factura: ");
            Factura f = facturaDao.buscarFacturaPorId(id);
            if (f == null) {
                System.out.println("✗ Factura no encontrada.");
                return;
            }
            System.out.println("\n" + f);
            System.out.println("Detalles:");
            f.getDetalles().forEach(d -> System.out.println("  - " + d));
        } catch (Exception e) {
            System.out.println("✗ Error al buscar factura: " + e.getMessage());
        }
    }

    private static void eliminarFactura() {
        try {
            long id = InputUtil.inputLong("ID de factura a eliminar: ");
            Factura f = facturaDao.buscarFacturaPorId(id);
            if (f == null) {
                System.out.println("✗ Factura no encontrada.");
                return;
            }
            facturaDao.eliminarFactura(f);
            System.out.println("✓ Factura marcada como eliminada.");
        } catch (Exception e) {
            System.out.println("✗ Error al eliminar factura: " + e.getMessage());
        }
    }

    private static void eliminarProducto() {
        try {
            long id = InputUtil.inputLong("ID de producto a eliminar (lógica): ");
            Producto p = productoDao.buscarPorId(id);
            if (p == null) {
                System.out.println("✗ Producto no encontrado.");
                return;
            }
            productoDao.eliminarProducto(p);
            System.out.println("✓ Producto marcado como eliminado.");
        } catch (Exception e) {
            System.out.println("✗ Error al eliminar producto: " + e.getMessage());
        }
    }

    private static void modificarCliente() {
        try {
            long id = InputUtil.inputLong("ID del cliente a modificar: ");
            List<Cliente> clientes = clienteDao.obtenerClientes();
            Cliente c = clientes.stream().filter(cl -> cl.getId() == id).findFirst().orElse(null);
            if (c == null) {
                System.out.println("✗ Cliente no encontrado.");
                return;
            }

            System.out.println("Datos actuales: " + c);
            String nuevodom = InputUtil.inputString("Nuevo domicilio (actual: " + c.getDomicilio() + "): ");
            if (!nuevodom.isEmpty())
                c.setDomicilio(nuevodom);

            clienteDao.modificarCliente(c);
            System.out.println("✓ Cliente actualizado.");
        } catch (Exception e) {
            System.out.println("✗ Error al modificar cliente: " + e.getMessage());
        }
    }

    private static void modificarPrecioProducto() {
        try {
            long id = InputUtil.inputLong("ID de producto: ");
            Producto p = productoDao.buscarPorId(id);
            if (p == null) {
                System.out.println("✗ Producto no encontrado.");
                return;
            }
            System.out.println("Precio actual: $" + p.getPrecioUnitario());
            double nuevoPrecio = InputUtil.inputDouble("Nuevo precio: ");
            productoDao.modificarPrecio(id, nuevoPrecio);
            System.out.println("✓ Precio actualizado.");
        } catch (Exception e) {
            System.out.println("✗ Error al modificar precio: " + e.getMessage());
        }
    }

    private static void mostrarTodasFacturas() {
        try {
            List<Factura> facturas = facturaDao.buscarFacturas();
            if (facturas.isEmpty()) {
                System.out.println("No hay facturas registradas.");
                return;
            }
            System.out.println("\n========== TODAS LAS FACTURAS ==========");
            facturas.forEach(f -> {
                System.out.println(f);
                f.getDetalles().forEach(d -> System.out.println("  - " + d));
            });
        } catch (Exception e) {
            System.out.println("✗ Error al mostrar facturas: " + e.getMessage());
        }
    }

    private static void mostrarTodosClientes() {
        try {
            List<Cliente> clientes = clienteDao.obtenerClientes();
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
                return;
            }
            System.out.println("\n========== TODOS LOS CLIENTES ==========");
            clientes.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("✗ Error al mostrar clientes: " + e.getMessage());
        }
    }

    private static void mostrarFacturasMayorA500000() {
        try {
            List<Factura> res = facturaDao.buscarFacturasConMontoMayorA(500000.0);
            if (res.isEmpty()) {
                System.out.println("No hay facturas mayores a $500.000");
                return;
            }
            System.out.println("\n========== FACTURAS > $500.000 ==========");
            res.forEach(f -> System.out.println(f));
        } catch (Exception e) {
            System.out.println("✗ Error al mostrar facturas: " + e.getMessage());
        }
    }

    private static void mostrarProductosActivos() {
        try {
            List<Producto> activos = productoDao.obtenerProductos();
            activos.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("✗ Error al mostrar productos: " + e.getMessage());
        }
    }
}
