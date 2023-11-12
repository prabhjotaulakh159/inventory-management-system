/*******************************************************************************
REGULAR TABLES 
*******************************************************************************/
CREATE TABLE admins (
    admin_id            NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    password            VARCHAR2(100)       NOT NULL CHECK (LENGTH(password) > 0)
);

CREATE TABLE customers (
    customer_id         NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    password            VARCHAR2(100)       NOT NULL CHECK (LENGTH(password) > 1),
    firstname           VARCHAR2(100)       NOT NULL CHECK (LENGTH(firstname) > 0),
    lastname            VARCHAR2(100)       NOT NULL CHECK (LENGTH(lastname) > 0),
    email               VARCHAR2(100)       NOT NULL CHECK (LENGTH(email) > 0),
    address             VARCHAR2(100)       NOT NULL CHECK (LENGTH(address) > 0)
);

CREATE TABLE categories (
    category_id         NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category            VARCHAR2(100)       NOT NULL CHECK (LENGTH(category) > 0)
);

CREATE TABLE warehouses (
    warehouse_id        NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                VARCHAR2(100)       NOT NULL CHECK (LENGTH(name) > 0),
    address             VARCHAR2(100)       NOT NULL CHECK (LENGTH(address) > 0)
);

CREATE TABLE products (
    product_id          NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_id         NUMBER              REFERENCES categories (category_id) ON DELETE CASCADE NOT NULL,
    name                VARCHAR2(100)       NOT NULL CHECK (LENGTH(name) > 0)
);

CREATE TABLE products_warehouses (
    warehouse_id        NUMBER              REFERENCES warehouses (warehouse_id) ON DELETE CASCADE NOT NULL,
    product_id          NUMBER              REFERENCES products (product_id) ON DELETE CASCADE NOT NULL,
    quantity            NUMBER(10)          NOT NULL CHECK (quantity >= 0)
);

CREATE TABLE stores (
    store_id            NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                VARCHAR2(100)       NOT NULL CHECK (LENGTH(name) > 0)
);

CREATE TABLE products_stores (
    product_id          NUMBER              REFERENCES  products (product_id) ON DELETE CASCADE NOT NULL,
    store_id            NUMBER              REFERENCES  stores (store_id) ON DELETE CASCADE NOT NULL ,
    price               NUMBER(10,2)        NOT NULL CHECK (price > 0)
);

CREATE TABLE orders (
    order_id            NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id         NUMBER              REFERENCES customers (customer_id) ON DELETE CASCADE NOT NULL,
    store_id            NUMBER              REFERENCES stores (store_id) ON DELETE CASCADE NOT NULL,
    order_date          DATE                DEFAULT SYSDATE NOT NULL
);

CREATE TABLE orders_products (
    order_id            NUMBER              REFERENCES orders (order_id) ON DELETE CASCADE NOT NULL,
    product_id          NUMBER              REFERENCES products (product_id) ON DELETE CASCADE NOT NULL,
    quantity            NUMBER(3)           NOT NULL CHECK (quantity > 0)
);

CREATE TABLE reviews (
    review_id           NUMBER              GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id         NUMBER              REFERENCES customers (customer_id) ON DELETE CASCADE NOT NULL,
    product_id          NUMBER              REFERENCES products (product_id) ON DELETE CASCADE NOT NULL,
    flags               NUMBER              DEFAULT 0 NOT NULL CHECK (flags > -1),
    rating              NUMBER              NOT NULL CHECK (rating >= 1 AND rating <= 5),
    description         VARCHAR2(30)        NOT NULL CHECK (LENGTH(description) > 0)
);

/*******************************************************************************
AUDIT TABLES
*******************************************************************************/
CREATE TABLE admins_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    admin_id           NUMBER               NOT NULL
);

CREATE TABLE customers_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    customer_id         NUMBER              NOT NULL
);

CREATE TABLE categories_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    category_id         NUMBER              NOT NULL
);

CREATE TABLE warehouses_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    warehouse_id        NUMBER              NOT NULL
);

CREATE TABLE products_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    product_id          NUMBER              NOT NULL
);

CREATE TABLE products_warehouses_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    product_id          NUMBER              NOT NULL,
    warehouse_id        NUMBER              NOT NULL
);

CREATE TABLE stores_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    store_id            NUMBER              NOT NULL
);

CREATE TABLE products_stores_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    store_id            NUMBER              NOT NULL,
    product_id          NUMBER              NOT NULL
);

CREATE TABLE orders_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    order_id            NUMBER              NOT NULL
);

CREATE TABLE orders_products_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    order_id            NUMBER              NOT NULL,
    product_id          NUMBER              NOT NULL
);

CREATE TABLE reviews_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    review_id           NUMBER              NOT NULL
);

/*******************************************************************************
AUDIT TRIGGERS
*******************************************************************************/
CREATE OR REPLACE TRIGGER admins_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON admins
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO admins_audit (action, admin_id) VALUES ('insert', :NEW.admin_id);
    ELSIF UPDATING THEN 
        INSERT INTO admins_audit (action, admin_id) VALUES ('update', :NEW.admin_id);
    ELSIF DELETING THEN 
        INSERT INTO admins_audit (action, admin_id) VALUES ('delete', :OLD.admin_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER customers_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON customers 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO customers_audit (action, customer_id) VALUES ('insert', :NEW.customer_id);
    ELSIF UPDATING THEN 
        INSERT INTO customers_audit (action, customer_id) VALUES ('update', :NEW.customer_id);
    ELSIF DELETING THEN 
        INSERT INTO customers_audit (action, customer_id) VALUES ('delete', :OLD.customer_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER categories_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON categories 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO categories_audit (action, category_id) VALUES ('insert', :NEW.category_id);
    ELSIF UPDATING THEN 
        INSERT INTO categories_audit (action, category_id) VALUES ('update', :NEW.category_id);
    ELSIF DELETING THEN 
        INSERT INTO categories_audit (action, category_id) VALUES ('update', :OLD.category_id);
    END IF;
END;    
/

CREATE OR REPLACE TRIGGER warehouses_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON warehouses 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO warehouses_audit (action, warehouse_id) VALUES ('insert', :NEW.warehouse_id);
    ELSIF UPDATING THEN 
        INSERT INTO warehouses_audit (action, warehouse_id) VALUES ('update', :NEW.warehouse_id);
    ELSIF DELETING THEN 
        INSERT INTO warehouses_audit (action, warehouse_id) VALUES ('delete', :OLD.warehouse_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER products_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON products 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO products_audit (action, product_id) VALUES ('insert', :NEW.product_id);
    ELSIF UPDATING THEN 
        INSERT INTO products_audit (action, product_id) VALUES ('update', :NEW.product_id);
    ELSIF DELETING THEN 
        INSERT INTO products_audit (action, product_id) VALUES ('delete', :OLD.product_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER products_warehouses_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON products_warehouses 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO products_warehouses_audit (action, product_id, warehouse_id) VALUES ('insert', :NEW.product_id, :NEW.warehouse_id);
    ELSIF UPDATING THEN 
        INSERT INTO products_warehouses_audit (action, product_id, warehouse_id) VALUES ('update', :NEW.product_id, :NEW.warehouse_id);
    ELSIF DELETING THEN 
        INSERT INTO products_warehouses_audit (action, product_id, warehouse_id) VALUES ('update', :OLD.product_id, :OLD.warehouse_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER stores_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON stores 
FOR EACH ROW 
BEGIN 
    IF INSERTING THEN 
        INSERT INTO stores_audit (action, store_id)VALUES ('insert', :NEW.store_id);
    ELSIF UPDATING THEN 
        INSERT INTO stores_audit (action, store_id) VALUES ('update', :NEW.store_id);
    ELSIF DELETING THEN 
        INSERT INTO stores_audit (action, store_id) VALUES ('update', :OLD.store_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER products_stores_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON products_stores 
FOR EACH ROW 
BEGIN 
    IF INSERTING THEN 
        INSERT INTO products_stores_audit (action, product_id, store_id) VALUES ('insert', :NEW.product_id, :NEW.store_id);
    ELSIF UPDATING THEN 
        INSERT INTO products_stores_audit (action, product_id, store_id) VALUES ('update', :NEW.product_id, :NEW.store_id);
    ELSIF DELETING THEN 
        INSERT INTO products_stores_audit (action, product_id, store_id) VALUES ('update', :OLD.product_id, :OLD.store_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER orders_audit_log 
AFTER INSERT OR DELETE 
ON orders 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO orders_audit (action, order_id) VALUES ('insert', :NEW.order_id);
    ELSIF UPDATING THEN 
        INSERT INTO orders_audit (action, order_id) VALUES ('update', :NEW.order_id);
    ELSIF DELETING THEN 
        INSERT INTO orders_audit (action, order_id) VALUES ('update', :OLD.order_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER orders_products_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON orders_products
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO orders_products_audit (action, order_id, product_id) VALUES ('insert', :NEW.order_id, :NEW.product_id);
    ELSIF UPDATING THEN 
        INSERT INTO orders_products_audit (action, order_id, product_id) VALUES ('update', :NEW.order_id, :NEW.product_id);
    ELSIF DELETING THEN 
        INSERT INTO orders_products_audit (action, order_id, product_id) VALUES ('delete', :OLD.order_id, :OLD.product_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER reviews_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON reviews 
FOR EACH ROW
BEGIN
    IF INSERTING THEN 
        INSERT INTO reviews_audit (action, review_id) VALUES ('insert', :NEW.review_id);
    ELSIF UPDATING THEN 
        INSERT INTO reviews_audit (action, review_id) VALUES ('update', :NEW.review_id);
    ELSIF DELETING THEN 
        INSERT INTO reviews_audit (action, review_id) VALUES ('update', :OLD.review_id);
    END IF;
END;
/

--/*******************************************************************************
--OBJECTS
--*******************************************************************************/
--CREATE TYPE order_obj AS OBJECT (
--    product     NUMBER,
--    quantity    NUMBER
--);
--/
--
--CREATE TYPE products_obj AS OBJECT (
--    prodid      NUMBER,
--    catid       NUMBER,
--    pname       VARCHAR(100)
--);
--/
--
--CREATE TYPE admin_obj AS OBJECT (
--    id          NUMBER,
--    password    VARCHAR2(100)
--);
--/
--
--/*******************************************************************************
--PACKAGES
--*******************************************************************************/
--CREATE OR REPLACE PACKAGE admin_pkg AS
--    FUNCTION login (admin_v admin_obj) RETURN BOOLEAN;
--END admin_pkg;
--/
--
--CREATE OR REPLACE PACKAGE BODY admin_pkg AS 
--    FUNCTION login (admin_v admin_obj) RETURN BOOLEAN AS 
--        pword VARCHAR2(100);
--    BEGIN 
--        SELECT password INTO pword FROM admins WHERE admin_id = admin_v.id;
--        IF admin_v.password = pword THEN
--            RETURN TRUE;
--        ELSE 
--            RETURN FALSE;
--        END IF;
--        EXCEPTION 
--            WHEN no_data_found THEN 
--                    DBMS_OUTPUT.PUT_LINE('admin not found');
--    END;
--END admin_pkg;
--/
--
--CREATE OR REPLACE PACKAGE order_pkg AS 
--    depleted_stock EXCEPTION;
--    TYPE order_products IS VARRAY(100) OF order_obj;
--    PROCEDURE create_order(customer IN NUMBER, store IN NUMBER, products IN order_pkg.order_products, order OUT NUMBER);
--    PROCEDURE delete_order(v_order_id IN NUMBER);
--    FUNCTION get_order(v_order_id IN NUMBER) RETURN order_pkg.order_array;
--    FUNCTION total_spent(customer NUMBER) RETURN NUMBER;
--    FUNCTION price_order(order_number IN NUMBER) RETURN NUMBER;
--END order_pkg;
--/
--
--CREATE OR REPLACE PACKAGE BODY order_pkg AS 
--    PROCEDURE create_order (customer IN NUMBER, store IN NUMBER, products IN order_pkg.order_products, order OUT NUMBER) AS 
--    BEGIN 
--        INSERT INTO orders (customer_id, store_id) VALUES (customer, store) RETURNING order_id INTO order;
--        FOR  i IN 1 .. products.COUNT LOOP
--            INSERT INTO orders_products (order_id, product_id, quantity) VALUES (order, products(i).product, products(i).quantity);
--        END LOOP;
--    EXCEPTION 
--        WHEN order_pkg.depleted_stock THEN 
--            DBMS_OUTPUT.PUT_LINE('One of the items is out of stock');
--            ROLLBACK;
--    END;
--     
--    PROCEDURE delete_order (v_order_id IN NUMBER) AS
--    BEGIN 
--        DELETE FROM orders WHERE order_id = v_order_id;
--    END;
--        
--    FUNCTION total_spent(customer NUMBER) RETURN NUMBER AS
--        total_spent NUMBER;
--    BEGIN 
--        SELECT SUM(ps.price * op.quantity) INTO total_spent FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id
--        INNER JOIN orders_products op ON o.order_id = op.order_id 
--        INNER JOIN products p ON op.product_id = p.product_id
--        INNER JOIN products_stores ps ON p.product_id = ps.product_id
--        WHERE c.customer_id = customer;
--        RETURN total_spent;
--    END;
--    
--    FUNCTION price_order(order_number NUMBER) RETURN NUMBER AS 
--        spent NUMBER;
--    BEGIN 
--        SELECT SUM(ps.price * op.quantity) INTO spent FROM orders o
--        INNER JOIN orders_products op ON o.order_id = op.order_id 
--        INNER JOIN products p ON op.product_id = p.product_id
--        INNER JOIN products_stores ps ON p.product_id = ps.product_id
--        WHERE o.order_id = order_number;
--        RETURN spent;
--    END;
--END order_pkg;
--/
--
--CREATE OR REPLACE PACKAGE products_pckg AS
--    PROCEDURE add_product(vproducts IN products_obj, products_id_o OUT NUMBER);
--    PROCEDURE delete_product(vproducts IN products_obj, products_id_o OUT NUMBER);
--    PROCEDURE update_product(vproducts IN products_obj, products_id_o OUT NUMBER);
--END products_pckg;
--/
--
--CREATE OR REPLACE PACKAGE BODY products_pckg AS
--
--     PROCEDURE add_product(vproducts IN products_obj, products_id_o OUT NUMBER) IS
--        BEGIN
--            INSERT INTO products(category_id, name)
--            VALUES(vproducts.catid, vproducts.pname)
--            RETURNING product_id INTO products_id_o;
--        END;
--
--    PROCEDURE delete_product(vproducts IN products_obj, products_id_o OUT NUMBER) IS
--        BEGIN
--            DELETE FROM products WHERE category_id=vproducts.catid AND name=vproducts.pname 
--            RETURNING product_id INTO products_id_o;
--        END;
--    PROCEDURE update_product(vproducts IN products_obj, products_id_o OUT NUMBER) IS
--        BEGIN
--            UPDATE products SET name=vproducts.pname WHERE product_id=vproducts.prodid 
--            RETURNING product_id INTO products_id_o;
--        END;
--END products_pckg;
--/
--
/*******************************************************************************
SPECIAL TRIGGERS
*******************************************************************************/
CREATE OR REPLACE TRIGGER validate_stock
BEFORE INSERT OR UPDATE 
ON orders_products
FOR EACH ROW
DECLARE 
    stock   NUMBER;
BEGIN 
    SELECT SUM(quantity) INTO stock FROM products_warehouses WHERE 
    product_id = :NEW.product_id;
    IF :NEW.quantity > stock THEN 
        RAISE order_pkg.depleted_stock;
    END IF;
    UPDATE products_warehouses SET quantity = quantity - :NEW.quantity WHERE warehouse_id = (SELECT warehouse_id FROM products_warehouses WHERE product_id = :NEW.product_id ORDER BY quantity ASC FETCH FIRST ROW ONLY)
    AND product_id = :NEW.product_id;
END;
/

CREATE OR REPLACE TRIGGER replenish_stock 
BEFORE DELETE 
ON orders_products 
FOR EACH ROW 
BEGIN 
    UPDATE products_warehouses SET quantity = quantity + :OLD.quantity WHERE warehouse_id = (SELECT warehouse_id FROM products_warehouses WHERE product_id = :OLD.product_id ORDER BY quantity ASC FETCH FIRST ROW ONLY)
    AND product_id = :OLD.product_id;
END;
/

/*******************************************************************************
TEST DATA
*******************************************************************************/
INSERT INTO admins (password) VALUES ('admin');

INSERT INTO customers (firstname, lastname, email, address, password) VALUES ('Prabhjot', 'Aulakh', 'prabhjot@email.com', 'Dawson College', 'customer');

INSERT INTO categories (category) VALUES ('Toys');
INSERT INTO categories (category) VALUES ('Grocery');

INSERT INTO stores (name) VALUES ('Marche Atwater');

INSERT INTO products (category_id, name) VALUES (1, 'Lego Batman');
INSERT INTO products (category_id, name) VALUES (1, 'Lego Superman');
INSERT INTO products (category_id, name) VALUES (2, 'Apple');

INSERT INTO products_stores (product_id, store_id, price) VALUES (1, 1, 20);
INSERT INTO products_stores (product_id, store_id, price) VALUES (2, 1, 25);
INSERT INTO products_stores (product_id, store_id, price) VALUES (3, 1, 5);

INSERT INTO warehouses (name, address) VALUES ('Warehouse A', 'Brossard, Quebec');
INSERT INTO warehouses (name, address) VALUES ('Warehouse B', 'Terrbonne, Quebec');

INSERT INTO products_warehouses (warehouse_id, product_id, quantity) VALUES (1, 1, 20);
INSERT INTO products_warehouses (warehouse_id, product_id, quantity) VALUES (1, 2, 25);
INSERT INTO products_warehouses (warehouse_id, product_id, quantity) VALUES (2, 3, 10);

INSERT INTO reviews (customer_id, product_id, rating, description) VALUES (1, 1, 5, 'Nice ! Might order');
INSERT INTO reviews (customer_id, product_id, rating, description) VALUES (1, 2, 5, 'Amazing !');
INSERT INTO reviews (customer_id, product_id, rating, description) VALUES (1, 3, 1, 'It was rotten :(');

INSERT INTO orders (customer_id, store_id) VALUES (1, 1);
INSERT INTO orders (customer_id, store_id) VALUES (1, 1);

INSERT INTO orders_products (order_id, product_id, quantity) VALUES (1, 1, 10);
INSERT INTO orders_products (order_id, product_id, quantity) VALUES (1, 2, 10);
INSERT INTO orders_products (order_id, product_id, quantity) VALUES (1, 3, 1);
INSERT INTO orders_products (order_id, product_id, quantity) VALUES (2, 3, 1);
COMMIT;
/