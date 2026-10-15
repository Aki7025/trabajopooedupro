CREATE DATABASE vitalfit;
USE vitalfit;

CREATE TABLE categoria (
  id_categoria INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL
);

CREATE TABLE producto (
  id_producto INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL,
  precio INT NOT NULL,
  stock INT NOT NULL,
  id_categoria INT,
  FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

CREATE TABLE cliente (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL,
  correo VARCHAR(80)
);

CREATE TABLE venta (
  id_venta INT AUTO_INCREMENT PRIMARY KEY,
  id_cliente INT,
  fecha DATE,
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE detalle_venta (
  id_detalle INT AUTO_INCREMENT PRIMARY KEY,
  id_venta INT,
  id_producto INT,
  cantidad INT,
  FOREIGN KEY (id_venta) REFERENCES venta(id_venta),
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

INSERT INTO categoria (nombre) VALUES
('Pesas'),('Ropa deportiva'),('Suplementos'),('Accesorios'),('Cardio');

INSERT INTO producto (nombre, precio, stock, id_categoria) VALUES
('Mancuerna 5kg', 12000, 30, 1),
('Polera dry-fit', 9990, 50, 2),
('Proteína 1kg', 29990, 20, 3),
('Cuerda para saltar', 4990, 40, 4),
('Cinta de correr', 249990, 3, 5),
('Colchoneta yoga', 11990, 25, 4);

INSERT INTO cliente (nombre, correo) VALUES
('Ana Pérez','ana@mail.com'),('Luis Soto','luis@mail.com'),
('Marta Díaz','marta@mail.com'),('Pedro Rojas','pedro@mail.com'),
('Sofía Vega','sofia@mail.com');

INSERT INTO venta (id_cliente, fecha) VALUES
(1,'2026-10-01'),(2,'2026-10-02'),(3,'2026-10-03'),(1,'2026-10-05');

INSERT INTO detalle_venta (id_venta, id_producto, cantidad) VALUES
(1,1,2),(1,4,1),(2,3,1),(3,2,3),(4,6,2);

SELECT c.nombre AS cliente, SUM(p.precio * d.cantidad) AS total_comprado
FROM cliente c
JOIN venta v ON c.id_cliente = v.id_cliente
JOIN detalle_venta d ON v.id_venta = d.id_venta
JOIN producto p ON d.id_producto = p.id_producto
GROUP BY c.nombre
HAVING total_comprado > 10000;

SET @iva = 0.19;
SELECT nombre, precio, precio * (1 + @iva) AS precio_con_iva FROM producto;
