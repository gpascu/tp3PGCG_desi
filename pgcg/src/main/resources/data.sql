DELETE FROM historial_estado_incidente;
DELETE FROM incidente;
DELETE FROM visita;
DELETE FROM historial_estado_factura;
DELETE FROM factura;
DELETE FROM historial_estado_contrato;
DELETE FROM contrato;
DELETE FROM historial_estado_publicacion;
DELETE FROM publicacion;
DELETE FROM historial_estado_propiedad;
DELETE FROM propiedad;
DELETE FROM persona;
DELETE FROM ciudad;
DELETE FROM provincia;

INSERT INTO provincia (id, nombre, eliminada) VALUES (1, 'Buenos Aires', false), (2, 'Cordoba', false);
INSERT INTO ciudad (id, nombre, provincia_id, eliminada) VALUES (1, 'La Plata', 1, false), (2, 'Mar del Plata', 1, false), (3, 'Cordoba Capital', 2, false);
INSERT INTO persona (id, nombre, apellido, dni_cuit, telefono, email, domicilio, eliminada, ciudad_id) VALUES
(1, 'Ana', 'Lopez', '20111111112', '111111', 'ana@mail.com', 'Calle 1', false, 1),
(2, 'Carlos', 'Perez', '20222222223', '222222', 'carlos@mail.com', 'Calle 2', false, 2),
(3, 'Lucia', 'Gomez', '20333333334', '333333', 'lucia@mail.com', 'Calle 3', false, 1),
(4, 'Martin', 'Ruiz', '20444444445', '444444', 'martin@mail.com', 'Calle 4', false, 3);
INSERT INTO propiedad (id, direccion, tipo, cantidad_ambientes, metros_cuadrados, descripcion, comodidades, estado_disponibilidad, eliminada, propietario_id, ciudad_id) VALUES
(1, 'Av 7 123', 'DEPARTAMENTO', 3, 65.0, 'Departamento luminoso', 'Balcon y cocina separada', 'DISPONIBLE', false, 1, 1),
(2, 'Calle 45 456', 'CASA', 5, 120.0, 'Casa amplia', 'Patio y garage', 'ALQUILADA', false, 2, 2),
(3, 'Av Colon 900', 'LOCAL', 1, 40.0, 'Local comercial', 'Vidriera al frente', 'DISPONIBLE', false, 1, 3);
INSERT INTO historial_estado_propiedad (id, estado, fecha_hora, propiedad_id) VALUES (1, 'DISPONIBLE', NOW(), 1), (2, 'ALQUILADA', NOW(), 2), (3, 'DISPONIBLE', NOW(), 3);
INSERT INTO publicacion (id, precio_mensual, condiciones, fecha_publicacion, estado, eliminada, descripcion, propiedad_id) VALUES
(1, 85000.00, 'Deposito y garantia', '2026-05-01', 'ACTIVA', false, 'Departamento en alquiler', 1),
(2, 120000.00, 'Garantia propietaria', '2026-04-15', 'PAUSADA', false, 'Local zona centro', 3);
INSERT INTO historial_estado_publicacion (id, estado, fecha_hora, publicacion_id) VALUES (1, 'ACTIVA', NOW(), 1), (2, 'PAUSADA', NOW(), 2);
INSERT INTO visita (id, fecha_hora, estado, publicacion_id) VALUES (1, NOW(), 'PENDIENTE', 1), (2, NOW(), 'REALIZADA', 1);
INSERT INTO contrato (id, fecha_inicio, duracion_meses, importe_mensual, dia_vencimiento_mensual, descripcion, estado, eliminado, propiedad_id, inquilino_id) VALUES
(1, '2026-01-01', 24, 150000.00, 10, 'Contrato activo', 'ACTIVO', false, 2, 3),
(2, '2026-06-01', 12, 90000.00, 5, 'Contrato borrador', 'BORRADOR', false, 1, 4);
INSERT INTO historial_estado_contrato (id, estado, fecha_hora, contrato_id) VALUES (1, 'ACTIVO', NOW(), 1), (2, 'BORRADOR', NOW(), 2);
INSERT INTO incidente (id, titulo, descripcion, categoria, fecha_alta, prioridad, estado, eliminado, fecha_resolucion, observaciones_resolucion, costo_resolucion, responsable_tecnico, contrato_id) VALUES
(1, 'Perdida de agua', 'Fuga en cocina', 'PLOMERIA', NOW(), 'ALTA', 'ABIERTO', false, NULL, NULL, NULL, NULL, 1),
(2, 'Termica', 'Salta la termica', 'ELECTRICIDAD', NOW(), 'MEDIA', 'EN_PROCESO', false, NULL, NULL, NULL, 'Tecnico Juan', 1);
INSERT INTO historial_estado_incidente (id, estado, fecha_hora, incidente_id) VALUES (1, 'ABIERTO', NOW(), 1), (2, 'EN_PROCESO', NOW(), 2);
INSERT INTO factura (id, fecha_emision, fecha_vencimiento, importe, estado, eliminada, fecha_pago, medio, importe_pagado, interes, concepto_facturado, contrato_id) VALUES
(1, '2026-05-10', '2026-05-20', 10000.00, 'PENDIENTE', false, NULL, NULL, NULL, NULL, 'Reparacion de puerta', 1),
(2, '2026-04-01', '2026-04-10', 20000.00, 'PAGADA', false, '2026-04-09', 'TRANSFERENCIA', 20000.00, 0.00, 'Expensas extraordinarias', 1);
INSERT INTO historial_estado_factura (id, estado, fecha_hora, factura_id) VALUES (1, 'PENDIENTE', NOW(), 1), (2, 'PAGADA', NOW(), 2);
