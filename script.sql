DROP TABLE orders;
DROP TABLE prod_stores;
DROP TABLE stores;
DROP TABLE customers;
DROP TABLE prod_warehouses;
DROP TABLE warehouses;
DROP TABLE products;
DROP TABLE categories;

CREATE TABLE categories (
    catid       NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category    VARCHAR2(100)   NOT NULL CHECK (LENGTH(category) > 0)
);

CREATE TABLE products (
    prodid      NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    catid       NUMBER          REFERENCES categories(catid) ON DELETE CASCADE NOT NULL,
    pname       VARCHAR2(100)   NOT NULL CHECK (LENGTH(pname) > 0)
);

CREATE TABLE warehouses (
    whid        NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    wname       VARCHAR2(100)   NOT NULL CHECK (LENGTH(wname) > 0),
    waddress    VARCHAR2(100)   NOT NULL CHECK (LENGTH(waddress) > 0)
);

CREATE TABLE prod_warehouses (
    prodid      NUMBER          REFERENCES products (prodid) ON DELETE CASCADE NOT NULL,
    whid        NUMBER          REFERENCES warehouses (whid) ON DELETE CASCADE NOT NULL,
    quantity    NUMBER(10)      NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE customers (
    custid      NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fname       VARCHAR2(100)       NOT NULL CHECK (LENGTH(fname) > 0),
    lname       VARCHAR2(100)       NOT NULL CHECK (LENGTH(lname) > 0),
    email       VARCHAR2(100)       NOT NULL CHECK (LENGTH(email) > 0),
    address     VARCHAR2(100)       NOT NULL CHECK (LENGTH(address) > 0)
);

CREATE TABLE stores (
    storeid     NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sname       VARCHAR2(100)       NOT NULL CHECK (LENGTH(sname) > 0)
);

CREATE TABLE prod_stores(
    prodid      NUMBER          REFERENCES  products (prodid) ON DELETE CASCADE NOT NULL,
    storeid     NUMBER          REFERENCES  stores (storeid) ON DELETE CASCADE NOT NULL ,
    price       NUMBER(10,2)    NOT NULL CHECK (price > 0)
);

CREATE TABLE orders (
    ordid       NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cusid       NUMBER          REFERENCES customers (custid) ON DELETE CASCADE NOT NULL,
    storeid     NUMBER          REFERENCES stores (storeid) ON DELETE CASCADE NOT NULL,
    prodid      NUMBER          REFERENCES products (prodid) ON DELETE CASCADE NOT NULL,
    quantity    NUMBER(10)      NOT NULL CHECK (quantity > 0)
);


INSERT INTO categories(category) VALUES('Beauty');
INSERT INTO categories(category) VALUES('Cars');
INSERT INTO categories(category) VALUES('DVD');
INSERT INTO categories(category) VALUES('Electronics');
INSERT INTO categories(category) VALUES('Grocery');
INSERT INTO categories(category) VALUES('Health');
INSERT INTO categories(category) VALUES('Toys');
INSERT INTO categories(category) VALUES('Vehicle');
INSERT INTO categories(category) VALUES('Video Games');


INSERT INTO warehouses(wname, waddress) 
VALUES('Warehouse A','100 rue William, saint laurent, Quebec, Canada');


INSERT INTO warehouses(wname, waddress) 
VALUES('Warehouse B', '304 Rue François-Perrault, Villera Saint-Michel, Montréal, QC');

INSERT INTO warehouses(wname, waddress)
VALUES('Warehouse C', '86700 Weston Rd, Toronto, Canada');

INSERT INTO warehouses(wname, waddress)
VALUES('Warehouse D', '170  Sideroad, Quebec City, Canada');

INSERT INTO warehouses(wname, waddress)
VALUES('Warehouse E', '1231 Trudea road, Ottawa, Canada ');

INSERT INTO warehouses(wname, waddress)
VALUES('Warehouse F', '16  Whitlock Rd, Alberta, Canada');
