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