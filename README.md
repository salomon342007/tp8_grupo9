# TP8 - Grupo 9: Persistencia de Objetos con JPA

## Descripción

Aplicación de gestión de facturas, clientes y productos utilizando **JPA/Hibernate** con patrón **DAO** y **Singleton** para acceso a base de datos.

### Requisitos Implementados

✅ **Punto 5**: Mapeo de entidades persistentes (Cliente, Producto, Factura, DetalleFactura) con relaciones
✅ **Punto 6**: Patrón DAO + Singleton (`EmfSingleton`)
✅ **Punto 7**: Menú interactivo con 12 opciones principales
✅ **Punto 8**: Manejo de excepciones para evitar interrupciones del flujo

## Estructura del Proyecto

```
src/main/java/ar/edu/unju/escmi/
├── config/
│   └── EmfSingleton.java          # Singleton para EntityManagerFactory
├── model/
│   ├── Cliente.java                # Entidad: id, dni, domicilio, total, estado
│   ├── Producto.java               # Entidad: id, descripcion, precioUnitario, estado
│   ├── Factura.java                # Entidad: id, fecha, domicilio, total, estado (1..* con Cliente)
│   └── DetalleFactura.java         # Entidad: id, cantidad, subtotal (0..1 con Producto, 1 con Factura)
├── dao/
│   ├── GenericDAO.java             # Interfaz genérica
│   ├── GenericDAOImpl.java          # Implementación base
│   ├── IClienteDao.java            # Interfaz específica
│   ├── ClienteDAOImpl.java          # Implementación
│   ├── IProductoDao.java
│   ├── ProductoDAOImpl.java
│   ├── IFacturaDao.java
│   ├── FacturaDAOImpl.java
│   ├── IDetalleFacturaDao.java
│   └── DetalleFacturaDAOImpl.java
├── util/
│   └── InputUtil.java              # Métodos para lectura segura: inputInt(), inputLong(), inputString(), inputDouble()
└── app/
    └── Principal.java              # Menú principal con 12 opciones

src/main/resources/
└── META-INF/
    └── persistence.xml             # Configuración JPA (URL, usuario, contraseña de MySQL)
```

## Relaciones de Base de Datos

```
Factura 1..* ←→ 1 Cliente
Factura 1 ←→ 0...1 DetalleFactura
DetalleFactura 0...1 ←→ 1 Producto
```

Todas las eliminaciones son **lógicas** (se marca `estado = false`).

## Menú de Opciones

1. **Alta de cliente** → guardar nuevo cliente
2. **Alta de producto** → guardar nuevo producto
3. **Realizar venta** → crear nueva factura con detalles
4. **Buscar factura** → buscar por ID y mostrar todos sus datos
5. **Eliminar factura** → eliminación lógica (estado=false)
6. **Eliminar producto** → eliminación lógica (estado=false)
7. **Modificar datos de cliente** → actualizar domicilio
8. **Modificar precio de producto** → actualizar precioUnitario
9. **Eliminar producto** → (duplicado de opción 6)
10. **Mostrar todas las facturas** → listar todas con detalles
11. **Mostrar todos los clientes** → listar clientes activos
12. **Mostrar facturas > $500.000** → filtrar por monto total
13. **Salir** → terminar aplicación

## Cómo Ejecutar

### Requisitos

- **Java 21** (actualizado desde Java 11)
- **Maven 3.9+**
- **MySQL Server** con base de datos y usuario configurados

### Pasos

1. **Configurar `persistence.xml`** con tu servidor MySQL:

   ```xml
   <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
   <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/tu_base_de_datos"/>
   <property name="jakarta.persistence.jdbc.user" value="root"/>
   <property name="jakarta.persistence.jdbc.password" value="tu_password"/>
   ```

2. **Compilar el proyecto:**

   ```bash
   mvn clean package
   ```

3. **Ejecutar la aplicación:**
   ```bash
   java -cp target/tp8-grupo9-1.0-SNAPSHOT.jar ar.edu.unju.escmi.app.Principal
   ```

## Manejo de Excepciones

- Cada operación está envuelta en try-catch
- Los errores se muestran con prefijo `✗` sin interrumpir el menú
- Las transacciones se revierten automáticamente en caso de error
- El EntityManager se cierra siempre en bloques finally

## Características Destacadas

- **InputUtil**: métodos seguros para lectura de entrada (reintentos en caso de error)
- **DAO Pattern**: abstracción de acceso a datos con interfaces
- **Singleton**: `EmfSingleton` garantiza una única instancia de `EntityManagerFactory`
- **Eliminación Lógica**: todas las eliminaciones usan el atributo `estado`
- **Relaciones JPA**: cascadas y mapeos bidireccionales correctamente configurados
- **Java 21**: código moderno con switch expressions

## Dependencias Principales

- **Hibernate ORM 6.4.0.Final** (migrado desde 5.6.7)
- **Jakarta Persistence 3.2.0**
- **MySQL Connector/J 8.2.0**
- **Maven Compiler Plugin 3.11.0** (soporte para Java 21)

## Notas

- Las imágenes del diagrama de clases deben colocarse en `docs/` o en la raíz
- Para agregar una imagen al README: `![Diagrama](./docs/diagrama.png)`
- Todas las DAOs implementan tanto la interfaz genérica como la específica
- Los métodos de búsqueda retornan `null` si no hay resultados
