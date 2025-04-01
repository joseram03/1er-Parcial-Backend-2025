# 1er Parcial Backend 2025 - Ecommerce - JEE

![Java EE](https://img.shields.io/badge/Java_EE-8.0+-orange)
![WildFly](https://img.shields.io/badge/WildFly-26.1.3-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)

Backend para sistema de ecommerce desarrollado con Java EE (JPA, EJB, JAX-RS) siguiendo las especificaciones del primer parcial de Programación Web Backend.

## 📋 Características Principales

- **Módulos CRUD:**
  - Administración de categorías
  - Gestión de productos (con filtros por nombre y categoría)
  - Registro de clientes
- **Sistema de Ventas:**
  - Validación de stock
  - Actualización automática de inventario
  - Envío de correos electrónicos de confirmación
- **Consultas de Ventas:**
  - Resumen de ventas con filtros por fecha y cliente
  - Detalle completo de ventas con información extendida

## 🛠 Stack Tecnológico

- **Java EE 8+** (EJB 3, JAX-RS, JPA 2.2)
- **WildFly 26.1.3**
- **PostgreSQL 15**
- **Maven 3.9+**
- **Hibernate 5.6+**
- **Jackson** para serialización JSON

## ⚙️ Configuración Requerida

- Java JDK 8+
- Apache Maven
- WildFly 26.x
- PostgreSQL 15+
- Postman (para pruebas de API)

## 🚀 Instalación y Ejecución

### 1. Clonar Repositorio

```bash
git clone https://github.com/joseram03/1er-Parcial-Backend-2025.git
cd ecommerce-backend
```

### 2. Configurar Base de Datos

1. **Crear base de datos en PostgreSQL:**
```sql
CREATE DATABASE ecommerce_db;
```

2. **Configurar credenciales** en el archivo `standalone.xml` de WildFly:

```xml
<datasource jndi-name="java:jboss/datasources/EcommerceDS" pool-name="EcommerceDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:postgresql://localhost:5432/ecommerce_db</connection-url>
    <driver>postgresql</driver>
    <security>
        <user-name>postgres</user-name>
        <password>tu_contraseña</password>
    </security>
</datasource>
```

### 3. Instalar Driver PostgreSQL en WildFly

1. Crear estructura de directorios /modules/system/layers/base/org/postgresql/main

2. Descargar el driver JDBC de PostgreSQL y colocarlo en la carpeta creada
    [Driver postgres](https://jdbc.postgresql.org/download/postgresql-42.7.4.jar) 

3. Crear archivo `module.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.7.4.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

4. Agregar driver en `standalone.xml`:
```xml
<driver name="postgresql" module="org.postgresql">
    <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
</driver>
```

### 4. Compilar Proyecto

```bash
mvn clean install -DskipTests
```

### 5. Desplegar en WildFly

Ejecutar el proyecto desde un IDE como Intelillj

## 🧪 Pruebas

### Postman
Se incluye en el repositorio un archivo `TESTING_APIS_POSTMAN.json` con una colección completa para probar todos los endpoints.

## 📹 Demostración

- [Video: Despliegue en WildFly](https://drive.google.com/file/d/16UlHI3eD8i2vZxc957NbdPEwozkryZpb/view?usp=sharing)
- [Video: Testing con Postman](https://drive.google.com/file/d/16TDi7SNbIlA4VSHX-IOkalN89sibJYmy/view?usp=sharing)

## 📝 Configuración de JPA

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
             http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd"
             version="2.2">
    <persistence-unit name="ecommercePU" transaction-type="JTA">
        <jta-data-source>java:jboss/datasources/EcommerceDS</jta-data-source>
        <properties>
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQL95Dialect"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
        </properties>
    </persistence-unit>
</persistence>
```