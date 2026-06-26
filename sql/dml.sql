-- ============================================================
-- DML - Data Manipulation Language
-- Datos de ejemplo para poblar la base de datos (datos semilla)
-- ============================================================

USE banco;

-- ── Insertar Clientes ───────────────────────────────────────
INSERT INTO clientes (tipo_identificacion, numero_identificacion, nombres, apellidos, correo, fecha_nacimiento, fecha_creacion)
VALUES
    ('CC',  '1587695367', 'Marin',    'Kitagawa',    'Marin@email.com',    '2005-03-05', NOW()),
    ('CC',  '126987432', 'Itsuki',   'Nakano',    'itsuki-nakano@email.com',   '2000-03-24', NOW()),
    ('NIT', '9001234567', 'Juan',  'Rojas','juan.rojas@email.com',    '2006-03-10', NOW()),
    ('CC',  '2587119315', 'Nino',    'Nakano',   'nino.nakano@email.com',   '2002-03-05', NOW());

-- ── Insertar Cuentas ────────────────────────────────────────
-- cliente_id 1 → Marin Kitagawa
-- cliente_id 2 → Itsuki Nakano
INSERT INTO cuentas (cliente_id, tipo_cuenta, numero_cuenta, estado, saldo, saldo_disponible, exenta_gmf, fecha_creacion)
VALUES
    (1, 'AHORROS',   '4930123456', 'ACTIVA',   50000.00,  50000.00,  false, NOW()),
    (1, 'CORRIENTE', '4930654321', 'ACTIVA',  130000.00, 130000.00,  true,  NOW()),
    (2, 'AHORROS',   '4930111222', 'ACTIVA',   75000.00,  75000.00,  false, NOW()),
    (3, 'CORRIENTE', '4930999888', 'INACTIVA', 200000.00, 200000.00, false, NOW());

-- ── Insertar Transacciones de Ejemplo ───────────────────────
-- Consignación a cuenta 1 (cuenta_origen = NULL porque viene de fuera)
INSERT INTO transacciones (tipo_transaccion, cuenta_origen_id, cuenta_destino_id, monto, fecha)
VALUES
    ('CONSIGNACION', NULL, 1,    50000.00, NOW()),
    ('CONSIGNACION', NULL, 2,   130000.00, NOW()),
    ('RETIRO',       1,    NULL,  20000.00, NOW()),
    ('TRANSFERENCIA',1,    2,     20000.00, NOW());