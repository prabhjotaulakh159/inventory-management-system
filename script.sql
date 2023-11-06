DROP TABLE orders;
DROP TABLE prod_stores_prices;
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
    whid        NUMBER          REFERENCES warehouses (whid) ON DELETE CASCADE NOT NULL,
    prodid      NUMBER          REFERENCES products (prodid) ON DELETE CASCADE NOT NULL,
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

CREATE TABLE prod_stores_prices (
    prodid      NUMBER          REFERENCES  products (prodid) ON DELETE CASCADE NOT NULL,
    storeid     NUMBER          REFERENCES  stores (storeid) ON DELETE CASCADE NOT NULL ,
    price       NUMBER(10,2)    NOT NULL CHECK (price > 0)
);

CREATE TABLE orders (
    ordid       NUMBER          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    custid      NUMBER          REFERENCES customers (custid) ON DELETE CASCADE NOT NULL,
    prodid      NUMBER          REFERENCES products (prodid) ON DELETE CASCADE NOT NULL,
    storeid     NUMBER          REFERENCES stores (storeid) ON DELETE CASCADE NOT NULL,
    quantity    NUMBER(10)      NOT NULL CHECK (quantity > 0),
    order_date  DATE            NOT NULL
);

INSERT INTO customers (fname, lname, email, address) VALUES ('alex', 'brown', 'alex@gmail.com', '090 boul saint laurent, montreal, quebec, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('Amanda', 'Harry', 'am.harry@yahioo.com', '100 boul saint laurent, montreal, quebec, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('daneil', 'hanne', 'daneil@yahoo.com', '100 atwater street, toronto, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('Jack', 'Jonhson', 'johnson.a@gmail.com', 'Calgary, Alberta, Canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('John', 'belle', 'abcd@yahoo.com', '105 Young street, toronto, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('John', 'boura', 'bdoura@gmail.com', '100 Young street, toronto, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('mahsa', 'sadeghi', 'msadeghi@dawsoncollege.qc.ca', 'dawson college, montreal, quebec, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('martin', 'Li', 'm.li@gmail.com', '87 boul saint laurent, montreal, quebec, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('martin', 'alexandre', 'marting@yahoo.com', 'brossard, quebec, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('Noah', 'Garcia', 'g.noah@yahoo.com', '22222 happy street, Laval, quebec, canada');
INSERT INTO customers (fname, lname, email, address) VALUES ('olivia', 'smith', 'smith@hotmail.com', '76 boul decalthon, laval, quebec, canada');

INSERT INTO categories (category) VALUES ('Beauty');
INSERT INTO categories (category) VALUES ('Cars');
INSERT INTO categories (category) VALUES ('DVD');
INSERT INTO categories (category) VALUES ('Electronics');
INSERT INTO categories (category) VALUES ('Grocery');
INSERT INTO categories (category) VALUES ('Health');
INSERT INTO categories (category) VALUES ('Toys');
INSERT INTO categories (category) VALUES ('Vehicle');
INSERT INTO categories (category) VALUES ('Video Games');

INSERT INTO warehouses (wname, waddress) VALUES ('Warehouse A','100 rue William, saint laurent, Quebec, Canada');
INSERT INTO warehouses (wname, waddress) VALUES ('Warehouse B', '304 Rue François-Perrault, Villera Saint-Michel, Montréal, QC');
INSERT INTO warehouses (wname, waddress) VALUES ('Warehouse C', '86700 Weston Rd, Toronto, Canada');
INSERT INTO warehouses (wname, waddress) VALUES ('Warehouse D', '170  Sideroad, Quebec City, Canada');
INSERT INTO warehouses (wname, waddress) VALUES ('Warehouse E', '1231 Trudea road, Ottawa, Canada ');
INSERT INTO warehouses (wname, waddress) VALUES ('Warehouse F', '16  Whitlock Rd, Alberta, Canada');

INSERT INTO products (catid, pname) VALUES (1, 'paper towel'); -- 1
INSERT INTO products (catid, pname) VALUES (2, 'BMW i6'); -- 2
INSERT INTO products (catid, pname) VALUES (3, 'Barbie Movie'); -- 3
INSERT INTO products (catid, pname) VALUES (4, 'laptop ASUS 104S'); -- 4
INSERT INTO products (catid, pname) VALUES (4, 'PS5'); -- 5
INSERT INTO products (catid, pname) VALUES (5, 'apple'); -- 6
INSERT INTO products (catid, pname) VALUES (5, 'chicken'); -- 7
INSERT INTO products (catid, pname) VALUES (5, 'orange'); -- 8
INSERT INTO products (catid, pname) VALUES (5, 'pasta'); -- 9
INSERT INTO products (catid, pname) VALUES (5, 'plum'); -- 10
INSERT INTO products (catid, pname) VALUES (6, 'L`Oreal Normal Hair'); -- 11
INSERT INTO products (catid, pname) VALUES (7, 'BMW iX Lego'); -- 12
INSERT INTO products (catid, pname) VALUES (7, 'Lamborghini Lego'); -- 13
INSERT INTO products (catid, pname) VALUES (8, 'Truck 500c'); -- 14
INSERT INTO products (catid, pname) VALUES (9, 'SIMS CD'); -- 15

INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (1, 2, 6);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (1, 13, 10);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (1, 4, 1000);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (1, 9, 2132);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (2, 6, 24980);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (2, 1, 39484);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (3, 11, 43242);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (3, 15, 103);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (4, 8, 35405);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (4, 11, 6579);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (4, 5, 123);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (5, 3, 40);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (5, 14, 98765);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (5, 15, 1000);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (6, 7, 43523);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (6, 12, 450);
INSERT INTO prod_warehouses (whid, prodid, quantity) VALUES (6, 1, 3532);

INSERT INTO stores (sname) VALUES ('dawson store');
INSERT INTO stores (sname) VALUES ('dealer montreal');
INSERT INTO stores (sname) VALUES ('Dealer one'); 
INSERT INTO stores (sname) VALUES ('marche adonis'); 
INSERT INTO stores (sname) VALUES ('marche atwater'); 
INSERT INTO stores (sname) VALUES ('movie start'); 
INSERT INTO stores (sname) VALUES ('movie store');
INSERT INTO stores (sname) VALUES ('star store'); 
INSERT INTO stores (sname) VALUES ('store magic'); 
INSERT INTO stores (sname) VALUES ('super rue champlain'); 
INSERT INTO stores (sname) VALUES ('toy r us'); 

INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (1, 6, 50);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (2, 3, 50000);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (3, 7, 30);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (3, 11, 45);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (4, 4, 970);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (5, 8, 200);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (6, 5, 10);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (7, 4, 9.5);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (8, 9, 2);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (9, 5, 13.5);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (9, 9, 15);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (10, 5, 10);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (11, 10, 10);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (12, 11, 40);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (13, 11, 40);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (14, 2, 856600);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (15, 1, 50);
INSERT INTO prod_stores_prices (prodid, storeid, price) VALUES (15, 7, 16);

INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (1, 3, 7, 1, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (1, 3, 11, 1, TO_DATE('10/02/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (1, 6, 5, 2, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (1, 15, 7, 1, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (2, 1, 6, 3, TO_DATE('10/11/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (3, 8, 9, 1, TO_DATE('10/23/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (4, 10, 5, 6, TO_DATE('05/06/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (5, 2, 3, 1, TO_DATE('08/10/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (6, 2, 3, 1, TO_DATE('10/10/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (7, 4, 4, 1, TO_DATE('04/21/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (7, 10, 5, 7, TO_DATE('05/06/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (7, 12, 11, 1, TO_DATE('10/11/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (7, 12, 11, 1, TO_DATE('10/11/2022', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (7, 13, 11, 1, TO_DATE('10/11/2010', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (7, 13, 11, 2, TO_DATE('10/07/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (8, 7, 4, 1, TO_DATE('04/03/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (9, 11, 10, 1, TO_DATE('10/10/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (9, 11, 10, 3, TO_DATE('09/12/2019', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (9, 15, 1, 2, TO_DATE('10/01/2023', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (10, 5, 8, 1, TO_DATE('01/20/2020', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (11, 9, 5, 3, TO_DATE('12/28/2021', 'MM/DD/YYYY'));
INSERT INTO orders (custid, prodid, storeid, quantity, order_date) VALUES (11, 9, 9, 3, TO_DATE('12/29/2021', 'MM/DD/YYYY'));

COMMIT;
