-- ============================================================
-- DDL - Data Definition Language
-- Define la estructura física de la base de datos
-- ============================================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS banco
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE banco;

-- ── Tabla: clientes ─────────────────────────────────────────
-- Almacena la información personal de cada cliente del banco
CREATE TABLE IF NOT EXISTS clientes (
    id                    BIGINT          NOT NULL AUTO_INCREMENT,
    tipo_identificacion   VARCHAR(10)     NOT NULL,
    numero_identificacion VARCHAR(20)     NOT NULL,
    nombres               VARCHAR(100)    NOT NULL,
    apellidos             VARCHAR(100)    NOT NULL,
    correo                VARCHAR(150)    NOT NULL,
    fecha_nacimiento      DATE            NOT NULL,
    fecha_creacion        DATETIME,
    fecha_modificacion    DATETIME,

    -- Clave primaria autoincremental
    CONSTRAINT pk_clientes PRIMARY KEY (id),

    -- El número de identificación debe ser único entre todos los clientes
    CONSTRAINT uq_clientes_numero_identificacion UNIQUE (numero_identificacion),

    -- El correo electrónico debe ser único entre todos los clientes
    CONSTRAINT uq_clientes_correo UNIQUE (correo)

);

-- ── Tabla: cuentas ──────────────────────────────────────────
-- Almacena las cuentas bancarias (Ahorros o Corriente)
-- asociadas a un cliente
CREATE TABLE IF NOT EXISTS cuentas (
    id                BIGINT          NOT NULL AUTO_INCREMENT,
    cliente_id        BIGINT          NOT NULL,
    tipo_cuenta       VARCHAR(20)     NOT NULL,  -- AHORROS | CORRIENTE
    numero_cuenta     VARCHAR(10)     NOT NULL,
    estado            VARCHAR(20),               -- ACTIVA | INACTIVA | CANCELADA
    saldo             DECIMAL(15, 2),
    saldo_disponible  DECIMAL(15, 2),
    exenta_gmf        BOOLEAN,
    fecha_creacion    DATETIME,
    fecha_modificacion DATETIME,

    -- Clave primaria
    CONSTRAINT pk_cuentas PRIMARY KEY (id)

    -- El número de cuenta debe ser único
    CONSTRAINT uq_cuentas_numero_cuenta UNIQUE (numero_cuenta)

    -- Relación: una cuenta pertenece a un cliente
    -- Si el cliente se elimina, no se pueden borrar sus cuentas (RESTRICT)
    CONSTRAINT fk_cuentas_cliente
    FOREIGN KEY (cliente_id)
    REFERENCES clientes (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE

);

-- ── Tabla: transacciones ────────────────────────────────────
-- Registro histórico de todas las operaciones bancarias:
-- Consignaciones, Retiros y Transferencias
CREATE TABLE IF NOT EXISTS transacciones (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    tipo_transaccion VARCHAR(20)     NOT NULL,  -- CONSIGNACION | RETIRO | TRANSFERENCIA
    cuenta_origen_id  BIGINT,                   -- NULL en consignaciones
    cuenta_destino_id BIGINT,                   -- NULL en retiros
    monto            DECIMAL(15, 2)  NOT NULL,
    fecha            DATETIME,

    -- Clave primaria
    CONSTRAINT pk_transacciones PRIMARY KEY (id),

    -- Relación con cuenta origen (opcional - puede ser NULL en consignaciones)
    CONSTRAINT fk_transacciones_cuenta_origen
    FOREIGN KEY (cuenta_origen_id)
    REFERENCES cuentas (id),

    -- Relación con cuenta destino (opcional - puede ser NULL en retiros)
    CONSTRAINT fk_transacciones_cuenta_destino
    FOREIGN KEY (cuenta_destino_id)
    REFERENCES cuentas (id)

);