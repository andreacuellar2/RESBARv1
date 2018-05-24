INSERT INTO Categoria(idCategoria,nombre) VALUES(1,"Vegetarianos");
INSERT INTO Categoria(idCategoria,nombre) VALUES(2,"Pescados y mariscos");
INSERT INTO Categoria(idCategoria,nombre) VALUES(3,"Carnes Rojas");
INSERT INTO Categoria(idCategoria,nombre) VALUES(4,"Emparedados");
INSERT INTO Categoria(idCategoria,nombre) VALUES(5,"Bebidas");

INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(1, 1, "Ensalada de verduras", 'A', 6.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(2, 2, "Pescado asado", 'P', 8.50);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(3, 2, "Coctel de camaron", 'C', 7.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(4, 3, "Carne asada", 'C', 10.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(5, 3, "Bistec a la olla", 'C', 9.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(6, 4, "Sandwich de pollo", 'S', 5.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(7, 4, "Sandwich de jamon", 'S', 5.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(8, 5, "Jugos Naturales", 'J', 3.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(9, 5, "Licuados", 'L', 2.00);
INSERT INTO Producto(idProducto,idCategoria,nombre,area,precio) VALUES(10, 5, "Cerveza", 'C', 2.00);

INSERT INTO
Orden(idOrden,mesero,mesa,cliente,fecha,comentario,total,activa)
VALUES(1,"Juan Carlos","Mesa 13","Cave Johnson",DATE '2010-12-31',"Caroline quiere pastel de postre",99.99,true);
INSERT INTO
Orden(idOrden,mesero,mesa,cliente,fecha,comentario,total,activa)
VALUES(2,"Diana Melissa","Mesa 3","Gordon Freeman",DATE '2015-07-05',"Sopa extra",50.50,true);
INSERT INTO
Orden(idOrden,mesero,mesa,cliente,fecha,comentario,total,activa)
VALUES(3,"Luis Enrique","Mesa 5","Isaac Clarke",DATE '2016-01-31',"Plato de efigie",34.99,true);
INSERT INTO
Orden(idOrden,mesero,mesa,cliente,fecha,comentario,total,activa)
VALUES(4,"Andrea Alejandra","Mesa 7","Thanos",DATE '2017-06-12',"La mitad de comida",50.00,true);
INSERT INTO
Orden(idOrden,mesero,mesa,cliente,fecha,comentario,total,activa)
VALUES(5,"Jack Shephard","Mesa 815","Claire Littleton",DATE '2018-08-15',"Plato de bebe",815.00,false);
