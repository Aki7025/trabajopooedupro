--el drop lo coloque pq cambie u monton la base de datos asi q tuve que reinciarla pero no hagas caso si no habia una base antes 
DROP DATABASE IF EXISTS vitalfit;
CREATE DATABASE vitalfit;
USE tienda_fitness;

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
('Fuerza'),('Estiramiento'),('Colchonetas'),('Cardio'),('Accesorios');

INSERT INTO producto (nombre, precio, stock, id_categoria) VALUES
('Ejercitador de mano', 2990, 25, 1),
('Banda de resistencia para estiramiento', 1090, 40, 2),
('Colchoneta MAT', 11990, 4, 3),
('Cuerda para saltar', 3990, 30, 4),
('Pelota pequeña', 4290, 20, 5),
('Correa elástica para estiramiento', 1036, 35, 2);

INSERT INTO cliente (nombre, correo) VALUES
('Ana Pérez','ana@mail.com'),('Luis Soto','luis@mail.com'),
('Marta Díaz','marta@mail.com'),('Pedro Rojas','pedro@mail.com'),
('Sofía Vega','sofia@mail.com');

INSERT INTO venta (id_cliente, fecha) VALUES
(1,'2026-10-01'),(2,'2026-10-02'),(3,'2026-10-03'),(1,'2026-10-05');

INSERT INTO detalle_venta (id_venta, id_producto, cantidad) VALUES
(1,1,2),(1,4,1),(2,3,1),(3,2,3),(4,6,2),(4,5,1); 
