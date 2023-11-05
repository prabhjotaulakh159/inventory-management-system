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

