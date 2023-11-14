-- For all documentation on package procedures/functions
-- Please refer to the packages folder within this directory

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
CREATE OR REPLACE TYPE order_type AS OBJECT (
    customer    NUMBER,
    store       NUMBER,
    order_date  DATE
);
/

CREATE OR REPLACE TYPE order_products_type AS OBJECT (
    product     NUMBER,
    quantity    NUMBER
);
/

CREATE OR REPLACE TYPE category_type AS OBJECT (
    category    VARCHAR2(100)
);
/

CREATE OR REPLACE TYPE customer_type AS OBJECT (
    firstname       VARCHAR2(100),
    lastname        VARCHAR2(100),
    email           VARCHAR2(100),
    address         VARCHAR2(100),
    password        VARCHAR2(100)
);
/

CREATE OR REPLACE TYPE product_type AS OBJECT (
    name        VARCHAR2(100),
    category    NUMBER
);
/

CREATE OR REPLACE TYPE review_type AS OBJECT (
    customer        NUMBER,
    product         NUMBER,
    flags           NUMBER,
    rating          NUMBER,
    description     VARCHAR2(100)
);
/

CREATE OR REPLACE TYPE store_type AS OBJECT (
    name    VARCHAR2(100)
);
/

CREATE OR REPLACE TYPE warehouse_type AS OBJECT (
    name        VARCHAR2(100),
    address     VARCHAR2(100)
);
/
--/*******************************************************************************
--PACKAGES
--*******************************************************************************/
CREATE OR REPLACE  PACKAGE admin_pkg AS 
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN;
END admin_pkg;
/

CREATE OR REPLACE PACKAGE BODY admin_pkg AS 
    PROCEDURE check_if_admin_exists(id IN NUMBER) AS 
        count_admin  NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20004, 'Customer id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_admin FROM admins WHERE admin_id = id;
        
        IF count_admin = 0 THEN 
            RAISE_APPLICATION_ERROR(-20004, 'Admin does not exist');
        END IF;
    END;
    
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN AS 
        vpassword VARCHAR2(100);
    BEGIN 
        admin_pkg.check_if_admin_exists(id);
        SELECT password INTO vpassword FROM admins 
        WHERE admin_id = id;
        
        RETURN password = vpassword;
    END;

END admin_pkg;
/

CREATE OR REPLACE  PACKAGE admin_pkg AS 
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN;
END admin_pkg;
/

CREATE OR REPLACE PACKAGE BODY admin_pkg AS 
    PROCEDURE check_if_admin_exists(id IN NUMBER) AS 
        count_admin  NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20004, 'Customer id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_admin FROM admins WHERE admin_id = id;
        
        IF count_admin = 0 THEN 
            RAISE_APPLICATION_ERROR(-20004, 'Admin does not exist');
        END IF;
    END;
    
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN AS 
        vpassword VARCHAR2(100);
    BEGIN 
        admin_pkg.check_if_admin_exists(id);
        SELECT password INTO vpassword FROM admins 
        WHERE admin_id = id;
        
        RETURN password = vpassword;
    END;

END admin_pkg;
/

CREATE OR REPLACE PACKAGE category_pkg AS 
    invalid_category EXCEPTION;
    PRAGMA EXCEPTION_INIT(invalid_category, -20006);
    TYPE categories IS VARRAY(100) OF category_type;
    PROCEDURE check_if_category_exists(id IN NUMBER);
    PROCEDURE create_category(category IN category_type);
    PROCEDURE update_category(id IN NUMBER, vcategory IN VARCHAR2);
    PROCEDURE delete_category(id IN NUMBER);
    FUNCTION get_categories RETURN category_pkg.categories;
    FUNCTION get_category(id IN NUMBER) RETURN category_type;
END category_pkg;
/

CREATE OR REPLACE PACKAGE BODY category_pkg AS 
    PROCEDURE check_if_category_exists(id IN NUMBER) AS 
        count_category NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_category FROM categories 
        WHERE category_id = id;
        
        IF count_category = 0 THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category does not exist');
        END IF;
    END;

    PROCEDURE create_category(category IN category_type) AS 
    BEGIN
        IF category.category IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category cannot be null');
        END IF;
        
        INSERT INTO categories (category) VALUES (category.category);
    END;
    
    PROCEDURE update_category(id IN NUMBER, vcategory IN VARCHAR2) AS
    BEGIN 
        category_pkg.check_if_category_exists(id);
        
        IF vcategory IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category cannot be null');
        END IF;
        
        UPDATE categories SET category = vcategory 
        WHERE category_id = id;
    END;
    
    PROCEDURE delete_category(id IN NUMBER) AS
    BEGIN 
        category_pkg.check_if_category_exists(id);
        DELETE FROM categories WHERE category_id = id;
    END;
    
    FUNCTION get_categories RETURN category_pkg.categories AS
        count_category NUMBER;
        vcategory VARCHAR2(100);
        categories category_pkg.categories;
    BEGIN 
        categories := category_pkg.categories();

        SELECT COUNT(*) INTO count_category FROM categories;
        
        IF count_category = 0 THEN  
            RAISE_APPLICATION_ERROR(-20006, 'No categories to find !');
        END IF;
        
        FOR i IN 1 .. count_category LOOP
            SELECT category INTO vcategory FROM categories 
            WHERE category_id = i;
            
            categories.EXTEND;
            categories(i) := category_type(vcategory);
        END LOOP;
            
        RETURN categories;
    END;
    
    FUNCTION get_category (id IN NUMBER) RETURN category_type AS
        vcategory VARCHAR2(100);
    BEGIN 
        category_pkg.check_if_category_exists(id);
        SELECT category INTO vcategory FROM categories WHERE category_id = id;
        RETURN category_type(vcategory);
    END;
END category_pkg;
/

CREATE OR REPLACE PACKAGE customer_pkg AS 
    invalid_customer EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_customer, -20004);

    PROCEDURE check_if_customer_exists(id IN NUMBER);
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN;
END customer_pkg;
/

CREATE OR REPLACE PACKAGE BODY customer_pkg AS 
    PROCEDURE check_if_customer_exists(id IN NUMBER) AS 
        count_cust  NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20004, 'Customer id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_cust FROM customers WHERE customer_id = id;
        
        IF count_cust = 0 THEN 
            RAISE_APPLICATION_ERROR(-20004, 'Customer does not exist');
        END IF;
    END;
    
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN AS 
        vpassword VARCHAR2(100);
    BEGIN 
        customer_pkg.check_if_customer_exists(id);
        SELECT password INTO vpassword FROM customers 
        WHERE customer_id = id;
        
        RETURN password = vpassword;
    END;

END customer_pkg;
/

CREATE OR REPLACE PACKAGE product_pkg AS 
    invalid_product EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_product, -20005);
    
    TYPE products IS VARRAY(100) OF product_type;
    
    PROCEDURE check_if_product_exists(id IN NUMBER);
    PROCEDURE create_product(product IN product_type);
    PROCEDURE update_product(id IN NUMBER, product IN product_type);
    PROCEDURE delete_product(id IN NUMBER);
    FUNCTION get_product(id IN NUMBER) RETURN product_type;
    FUNCTION get_products RETURN product_pkg.products;

END product_pkg;
/

CREATE OR REPLACE PACKAGE BODY product_pkg AS 
    PROCEDURE check_if_product_exists(id IN NUMBER) AS
        count_prod  NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20005, 'Product id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_prod FROM products WHERE product_id = id;
        
        IF count_prod = 0 THEN 
            RAISE_APPLICATION_ERROR(-20005, 'Product does not exist');
        END IF;
    END;
    
    PROCEDURE create_product(product IN product_type) AS 
    BEGIN 
        category_pkg.check_if_category_exists(product.category);
        
        IF product.name IS NULL THEN
            RAISE_APPLICATION_ERROR(-20005, 'Product name cannot be null');
        END IF;
        
        IF LENGTH(product.name) NOT BETWEEN 1 AND 100 THEN 
            RAISE_APPLICATION_ERROR(-20005, 'Product name must be between 1 and 100 characters');
        END IF;
        
        INSERT INTO products (name, category_id) VALUES (product.name, product.category);
    END;
    
    PROCEDURE update_product(id IN NUMBER, product IN product_type) AS 
    BEGIN 
        product_pkg.check_if_product_exists(id);
        category_pkg.check_if_category_exists(product.category);
        
        IF product.name IS NULL THEN
            RAISE_APPLICATION_ERROR(-20005, 'Product name cannot be null');
        END IF;
        
        IF LENGTH(product.name) NOT BETWEEN 1 AND 100 THEN 
            RAISE_APPLICATION_ERROR(-20005, 'Product name must be between 1 and 100 characters');
        END IF;
        
        UPDATE products SET name = product.name, category_id = product.category 
        WHERE product_id = id;
    END;
    
    PROCEDURE delete_product(id IN NUMBER) AS
    BEGIN 
        product_pkg.check_if_product_exists(id);
        DELETE FROM products WHERE product_id = id;
    END;

    FUNCTION get_product(id IN NUMBER) RETURN product_type AS 
        vname NUMBER;
        category NUMBER;
    BEGIN 
        product_pkg.check_if_product_exists(id);
        
        SELECT name, category_id INTO vname, category FROM products 
        WHERE product_id = id;
        
        RETURN product_type(vname, category);
    END;
    
    FUNCTION get_products RETURN product_pkg.products AS
        vname NUMBER;
        vcat NUMBER;
        products product_pkg.products;
        count_prod NUMBER;
    BEGIN 
        products := product_pkg.products();
        SELECT COUNT(*) INTO count_prod FROM products;
        
        IF count_prod = 0 THEN 
            RAISE_APPLICATION_ERROR(-20005, 'No products to fetch !');
        END IF;
        
        FOR i IN 1 .. count_prod LOOP
            SELECT name, category_id INTO vname, vcat FROM products 
            WHERE product_id = i;
            products.EXTEND;
            products(i) := product_type(vname, vcat);
        END LOOP;
        
        RETURN products;
    END;
END product_pkg;
/

CREATE OR REPLACE PACKAGE store_pkg AS 
    invalid_store EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_store, -20003);
    
    TYPE stores IS VARRAY(100) OF store_type;
    TYPE prices IS VARRAY(100) OF NUMBER;
    
    PROCEDURE check_if_store_exists(id IN NUMBER);
    PROCEDURE create_store(store IN store_type);
    PROCEDURE update_store(id IN NUMBER, vname IN VARCHAR2);
    PROCEDURE delete_store(id IN NUMBER);
    PROCEDURE create_price(vstoreid IN NUMBER, vproductid IN NUMBER, vprice IN NUMBER);
    PROCEDURE update_price(vstoreid IN NUMBER, vproductid IN NUMBER, vprice IN NUMBER);
    FUNCTION get_prices_of_product(id IN NUMBER) RETURN store_pkg.prices;
    FUNCTION get_store(vstoreid IN NUMBER) RETURN store_type;
    FUNCTION get_stores RETURN store_pkg.stores;
END store_pkg;
/

CREATE OR REPLACE PACKAGE BODY store_pkg AS 
    PROCEDURE check_if_store_exists(id IN NUMBER) AS 
        count_store NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Store id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_store FROM stores WHERE store_id = id;
        
        IF count_store = 0 THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Store does not exist');
        END IF;
    END;
    
    PROCEDURE create_store(store IN store_type) AS 
    BEGIN 
        IF store.name IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Store name cannot be null');
        END IF;
        
        IF LENGTH(store.name) NOT BETWEEN 1 AND 100 THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Name of store must be between 1 and 100 characters');
        END IF;
        
        INSERT INTO stores (name) VALUES (store.name);
    END;
    
    PROCEDURE update_store(id IN NUMBER, vname IN VARCHAR2) AS
    BEGIN 
        store_pkg.check_if_store_exists(id);
        
        IF vname IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Store name cannot be null');
        END IF;
        
        IF LENGTH(vname) NOT BETWEEN 1 AND 100 THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Name of store must be between 1 and 100 characters');
        END IF;
        
        UPDATE stores SET name = vname WHERE store_id = id;
    END;
    
    PROCEDURE delete_store(id IN NUMBER) AS
    BEGIN 
        store_pkg.check_if_store_exists(id);
        DELETE FROM stores WHERE store_id = id;
    END;
    
    PROCEDURE create_price(vstoreid IN NUMBER, vproductid IN NUMBER, vprice IN NUMBER) AS
    BEGIN 
        store_pkg.check_if_store_exists(vstoreid);
        product_pkg.check_if_product_exists(vproductid);
        
        IF vprice <= 0 THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Price cannot be equal to or lower than 0');
        END IF;
        
        INSERT INTO products_stores (store_id, product_id, price) VALUES (vstoreid, vproductid, vprice);
        
        EXCEPTION 
            WHEN value_error THEN 
                RAISE_APPLICATION_ERROR(-20003, 'Price is invalid, try another number');
    END;
        
    PROCEDURE update_price(vstoreid IN NUMBER, vproductid IN NUMBER, vprice IN NUMBER) AS
    BEGIN 
        store_pkg.check_if_store_exists(vstoreid);
        product_pkg.check_if_product_exists(vproductid);
        
        IF vprice <= 0 THEN 
            RAISE_APPLICATION_ERROR(-20003, 'Price cannot be equal to or lower than 0');
        END IF;
        
        UPDATE products_stores SET price = vprice 
        WHERE store_id = vstoreid AND product_id = vproductid;
        
        EXCEPTION 
            WHEN value_error THEN 
                RAISE_APPLICATION_ERROR(-20003, 'Price is invalid, try another number');
    END;

    FUNCTION get_prices_of_product(id IN NUMBER) RETURN store_pkg.prices AS
        prices store_pkg.prices;
    BEGIN 
        prices := store_pkg.prices();
        product_pkg.check_if_product_exists(id);
        SELECT price BULK COLLECT INTO prices FROM products_stores 
        WHERE product_id = id ORDER BY store_id ASC;
        
        RETURN prices;
    END;

    FUNCTION get_store(vstoreid IN NUMBER) RETURN store_type AS 
        vname VARCHAR2(100);
        store store_type;
    BEGIN 
        store_pkg.check_if_store_exists(vstoreid);
        SELECT name INTO vname FROM stores WHERE store_id = vstoreid;
        RETURN store_type(vname);
    END;
    
    FUNCTION get_stores RETURN store_pkg.stores AS
        stores store_pkg.stores;
        count_stores NUMBER;
        vname VARCHAR2(100);
    BEGIN 
        stores := store_pkg.stores();
        SELECT COUNT(*) INTO count_stores FROM stores;
        
        IF count_stores = 0 THEN 
            RAISE_APPLICATION_ERROR(-20003, 'No stores to fetch !');
        END IF;
        
        FOR i IN 1 .. count_stores LOOP
            SELECT name INTO vname FROM stores WHERE store_id = i;
            stores.EXTEND;
            stores(i) := store_type(vname);
        END LOOP;
        
        RETURN stores;
    END;
END store_pkg;
/

CREATE OR REPLACE PACKAGE order_pkg AS 
    depleted_stock EXCEPTION;
    invalid_order EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_order, -20002);
    
    TYPE products IS VARRAY(100) OF order_products_type;
    TYPE orders IS VARRAY(100) OF order_type;    
    
    PROCEDURE check_if_order_exists(id IN NUMBER);
    PROCEDURE create_order (vorder IN order_type, products IN order_pkg.products);
    PROCEDURE delete_order (id IN NUMBER);
    FUNCTION get_all_orders RETURN order_pkg.orders;
    FUNCTION get_all_orders_by_customer(vcustomer_id IN NUMBER) RETURN order_pkg.orders;
    FUNCTION get_order_details(vorder_id IN NUMBER) RETURN order_pkg.products;
    FUNCTION price_order(order_number IN NUMBER) RETURN NUMBER;
END order_pkg;
/

CREATE OR REPLACE PACKAGE BODY order_pkg AS 
    PROCEDURE check_if_order_exists(id IN NUMBER) AS 
        count_order NUMBER;
    BEGIN
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20002, 'Order id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_order FROM orders WHERE order_id = id;
        
        IF count_order = 0 THEN 
            RAISE_APPLICATION_ERROR(-20002, 'Order does not exists');
        END IF;
    END;
    
    PROCEDURE create_order (vorder IN order_type, products IN order_pkg.products) AS 
        vorder_id   NUMBER;
    BEGIN 
        customer_pkg.check_if_customer_exists(vorder.customer);
        store_pkg.check_if_store_exists(vorder.store);
        
        FOR i in 1 .. products.COUNT LOOP
            product_pkg.check_if_product_exists(products(i).product);
            IF products(i).quantity <= 0 THEN 
                RAISE_APPLICATION_ERROR(-20002, 'Quantity cannot be negative');
            END IF;
        END LOOP;
        
        INSERT INTO orders (customer_id, store_id) 
        VALUES (vorder.customer, vorder.store) RETURNING order_id INTO vorder_id;
        
        FOR i IN 1 .. products.COUNT LOOP 
            INSERT INTO orders_products (order_id, product_id, quantity) 
            VALUES (vorder_id, products(i).product, products(i).quantity);
        END LOOP;
        
        EXCEPTION 
            WHEN order_pkg.depleted_stock THEN 
                RAISE_APPLICATION_ERROR(-20002, 'Not enough stock for all items');
                ROLLBACK;
    END;
    
    PROCEDURE delete_order (id IN NUMBER) AS BEGIN 
        order_pkg.check_if_order_exists(id);
        DELETE FROM orders WHERE order_id = id;
    END;
    
    FUNCTION get_all_orders RETURN order_pkg.orders AS
        custid  NUMBER;
        storeid NUMBER;
        o_date  DATE;
        count_order NUMBER;
        order_arr order_pkg.orders;
    BEGIN 
        order_arr := order_pkg.orders();
        
        SELECT COUNT(*) INTO count_order FROM orders;
        
        IF count_order = 0 THEN 
            RAISE_APPLICATION_ERROR(-20002, 'No orders to get');
        END IF;
        
        FOR i IN 1 .. count_order LOOP
            SELECT customer_id, store_id, order_date
            INTO custid, storeid, o_date FROM orders 
            WHERE order_id = i;
            order_arr.EXTEND;
            order_arr(i) := order_type(custid, storeid, o_date);
        END LOOP;
        
        RETURN order_arr;
    END;
    
    FUNCTION get_all_orders_by_customer(vcustomer_id IN NUMBER) RETURN order_pkg.orders AS
        storeid NUMBER;
        o_date  DATE;
        count_order NUMBER;
        order_arr order_pkg.orders;
        TYPE id_arr IS VARRAY(100) OF NUMBER;
        arr id_arr;
    BEGIN 
        customer_pkg.check_if_customer_exists(vcustomer_id);
        order_arr := order_pkg.orders();
        arr := id_arr();
        
        SELECT COUNT(*) INTO count_order FROM orders WHERE customer_id = vcustomer_id;
        
        IF count_order = 0 THEN 
            RAISE_APPLICATION_ERROR(-20002, 'No orders to get for customer');
        END IF;
        
        SELECT order_id BULK COLLECT INTO arr FROM orders 
        WHERE customer_id = vcustomer_id;
        
        FOR i IN 1 .. arr.COUNT LOOP
            SELECT store_id, order_date INTO storeid, o_date 
            FROM orders 
            WHERE customer_id = vcustomer_id AND order_id = arr(i);
            order_arr.EXTEND;
            order_arr(i) := order_type(vcustomer_id, storeid, o_date);
        END LOOP;
        
        RETURN order_arr;
    END;
    
    FUNCTION get_order_details(vorder_id IN NUMBER) RETURN order_pkg.products AS
        quant       NUMBER;
        products    order_pkg.products;
        TYPE id_arr IS VARRAY(100) OF NUMBER;
        product_ids id_arr;
    BEGIN 
        product_ids := id_arr();
        products := order_pkg.products();
        
        order_pkg.check_if_order_exists(vorder_id);
       
        SELECT product_id BULK COLLECT INTO product_ids FROM orders_products 
        WHERE order_id = vorder_id;
       
        FOR i in 1 .. product_ids.COUNT LOOP
            SELECT quantity INTO quant FROM orders_products 
            WHERE product_id = product_ids(i) AND order_id = vorder_id;
            products.EXTEND;
            products(i) := order_products_type(product_ids(i), quant);
       END LOOP;
       
       RETURN products;
    END;
    
    FUNCTION price_order(order_number NUMBER) RETURN NUMBER AS 
        spent NUMBER;
    BEGIN 
        order_pkg.check_if_order_exists(order_number);
        SELECT SUM(ps.price * op.quantity) INTO spent FROM orders o
        INNER JOIN orders_products op ON o.order_id = op.order_id 
        INNER JOIN products p ON op.product_id = p.product_id
        INNER JOIN products_stores ps ON p.product_id = ps.product_id
        WHERE o.order_id = order_number;
        RETURN spent;
    END;
END order_pkg;
/

CREATE OR REPLACE PACKAGE review_pkg AS
    invalid_review EXCEPTION;
    PRAGMA EXCEPTION_INIT(invalid_review, -20000);
    
    TYPE review_array IS VARRAY(1000) OF review_type;
    
    PROCEDURE check_if_review_exists(id IN NUMBER);
    PROCEDURE create_review(review IN review_type);
    PROCEDURE update_review(id IN NUMBER, vrating IN NUMBER, vdescription IN VARCHAR2);
    PROCEDURE delete_review(id IN NUMBER);
    PROCEDURE delete_flagged_reviews;
    
    FUNCTION get_review(id IN NUMBER) RETURN review_type;
    FUNCTION get_all_reviews RETURN review_pkg.review_array;
    FUNCTION get_flagged_reviews RETURN review_pkg.review_array;
END review_pkg;
/

CREATE OR REPLACE PACKAGE BODY review_pkg AS 
    PROCEDURE check_if_review_exists(id IN NUMBER) AS 
        count_review   NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_review FROM reviews WHERE review_id = id;
        
        IF count_review = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review not found');
        END IF;
    END;

    PROCEDURE check_null(review IN review_type) AS 
    BEGIN 
        IF review.customer IS NULL OR review.product IS NULL 
                OR review.rating IS NULL
                OR review.description IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review cannot have null values');
        END IF;
    END;
    
    PROCEDURE validate_rating(vrating IN NUMBER) AS
    BEGIN
        IF vrating NOT BETWEEN 1 AND 5 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Rating must be between 1 and 5');
        END IF;
    END;
    
    PROCEDURE validate_description(vdescription IN VARCHAR2) AS
    BEGIN 
        IF LENGTH(vdescription) > 30 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Description is 30 characters max');
        END IF;
    END;
    
    PROCEDURE create_review(review IN review_type) AS
        count_cust      NUMBER;
        count_prod      NUMBER;
    BEGIN 
        customer_pkg.check_if_customer_exists(review.customer);
        product_pkg.check_if_product_exists(review.product);
        
        review_pkg.check_null(review);
        review_pkg.validate_rating(review.rating);
        review_pkg.validate_description(review.description);
              
        INSERT INTO reviews (customer_id, product_id, rating, description)
        VALUES (review.customer, review.product, review.rating, 
            review.description);
    END;
    
    PROCEDURE update_review(id IN NUMBER, vrating IN NUMBER, vdescription IN VARCHAR2) AS
        count_rev      NUMBER;
    BEGIN 
        review_pkg.check_if_review_exists(id);
        review_pkg.validate_rating(vrating);
        review_pkg.validate_description(vdescription);
        
        UPDATE reviews SET rating = vrating, description = vdescription
        WHERE review_id = id;
        
        EXCEPTION 
            WHEN others THEN 
                RAISE_APPLICATION_ERROR(-20000, 'Cannot update review');
    END;
    
    PROCEDURE delete_review(id IN NUMBER) AS 
    BEGIN 
        review_pkg.check_if_review_exists(id);
        DELETE FROM reviews WHERE review_id = id;
    END;
    
    PROCEDURE delete_flagged_reviews AS 
     BEGIN 
        DELETE FROM reviews WHERE flags > 2;
     END;
    
    FUNCTION get_review(id IN NUMBER) RETURN review_type AS 
        review          review_type;
        count_rev       NUMBER;
        custid          NUMBER;
        prodid          NUMBER;
        vflags          NUMBER;
        vrating         NUMBER;
        vdesc           VARCHAR2(30);
    BEGIN 
        review_pkg.check_if_review_exists(id);
        
        SELECT customer_id, product_id, flags, rating, description 
        INTO custid, prodid, vflags, vrating, vdesc 
        FROM reviews WHERE review_id = id;
        
        review := review_type(custid, prodid, vflags, vrating, vdesc);
        
        RETURN review;
    END;
    
    FUNCTION get_all_reviews RETURN review_pkg.review_array AS 
        custid NUMBER;
        prodid NUMBER;
        vrating NUMBER;
        vflags NUMBER;
        vdesc VARCHAR2(30);
        review review_type;
        reviews review_pkg.review_array;
        num_reviews NUMBER;
    BEGIN 
        reviews := review_pkg.review_array();
        
        SELECT COUNT(*) INTO num_reviews FROM reviews;
        
        IF num_reviews = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'No reviews to get !');
        END IF;
        
        FOR i IN 1 .. num_reviews LOOP
            SELECT customer_id, product_id, flags, rating, description 
            INTO custid, prodid, vflags, vrating, vdesc FROM reviews
            WHERE review_id = i;
            
            reviews.EXTEND;
            reviews(i) := review_type(custid, prodid, vflags, vrating, vdesc);
        END LOOP;
        
        RETURN reviews;
        
        EXCEPTION 
            WHEN others THEN 
                RAISE_APPLICATION_ERROR(-20000, 'Cannot get reviews');
    END;
    
    FUNCTION get_flagged_reviews RETURN review_pkg.review_array AS
        custid NUMBER;
        prodid NUMBER;
        vrating NUMBER;
        vflags NUMBER;
        vdesc VARCHAR2(30);
        
        reviews review_pkg.review_array;
        
        TYPE num_arr IS VARRAY(100) OF NUMBER;
        arr num_arr;
        
    BEGIN 
        arr := num_arr();
        reviews := review_pkg.review_array();
        
        SELECT review_id BULK COLLECT INTO arr FROM reviews WHERE flags > 2;
        
        IF arr.COUNT = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'No flagged reviews te be found');
        END IF;
        
        FOR i IN 1 .. arr.COUNT LOOP 
            SELECT customer_id, product_id, rating, flags, description 
            INTO custid, prodid, vrating, vflags, vdesc 
            FROM reviews WHERE review_id = arr(i);
            
            reviews.EXTEND;
            reviews(i) := review_type(custid, prodid, vflags, vrating, vdesc);
        END LOOP;
        
        RETURN reviews;
    END;
END review_pkg;
/

CREATE OR REPLACE PACKAGE warehouse_pkg AS 
    invalid_warehouse EXCEPTION;
    PRAGMA EXCEPTION_INIT(invalid_warehouse, -20001);    
    
    TYPE warehouse_array IS VARRAY(100) OF warehouse_type;
    
    PROCEDURE check_if_warehouse_exists(id IN NUMBER);
    PROCEDURE create_warehouse(warehouse IN warehouse_type);
    PROCEDURE update_warehouse(
            id IN NUMBER, vname IN VARCHAR2, vaddress IN VARCHAR2);
    PROCEDURE delete_warehouse(id IN NUMBER);
    PROCEDURE update_stock(vwarehouseid IN NUMBER, vproductid IN NUMBER, vquantity IN NUMBER);
    PROCEDURE insert_product_into_warehouse(vwarehouseid IN NUMBER, vproductid IN NUMBER, initial_quant IN NUMBER);
    FUNCTION get_warehouse(id IN NUMBER) RETURN warehouse_type;
    FUNCTION get_all_warehouses RETURN warehouse_pkg.warehouse_array;
    FUNCTION get_stock(id IN NUMBER) RETURN NUMBER;
END warehouse_pkg;
/

CREATE OR REPLACE PACKAGE BODY warehouse_pkg AS
    PROCEDURE check_if_warehouse_exists(id IN NUMBER) AS
         count_warehouse     NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_warehouse FROM warehouses 
        WHERE warehouse_id = id;
        
        IF count_warehouse = 0 THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Warehouse does not exist');
        END IF;
    END;
    
    PROCEDURE check_null(warehouse IN warehouse_type) AS 
    BEGIN 
        IF warehouse.name IS NULL OR warehouse.address IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Warehouse fields cannot be null');
        END IF;
    END;
    
    PROCEDURE check_len(warehouse IN warehouse_type) AS 
    BEGIN 
        IF LENGTH(warehouse.name)       NOT BETWEEN 1 AND 100 OR
           LENGTH(warehouse.address)    NOT BETWEEN 1 AND 100 
           THEN RAISE_APPLICATION_ERROR(
                -20001, 'Name/address must be between 1 and 100 charecters');
        END IF;
    END;
    
    PROCEDURE create_warehouse(warehouse IN warehouse_type) AS 
    BEGIN 
        warehouse_pkg.check_null(warehouse);
        warehouse_pkg.check_len(warehouse);
        
        INSERT INTO warehouses (name, address) 
        VALUES (warehouse.name, warehouse.address);
    END;
    
    PROCEDURE update_warehouse(
            id IN NUMBER, vname IN VARCHAR2, vaddress IN VARCHAR2) AS
        count_warehouse     NUMBER;
    BEGIN 
        warehouse_pkg.check_if_warehouse_exists(id);
        warehouse_pkg.check_null(warehouse_type(vname, vaddress));
        warehouse_pkg.check_len(warehouse_type(vname, vaddress));
        
        UPDATE warehouses SET name = vname, address = vaddress 
        WHERE warehouse_id = id;
    END;
    
    PROCEDURE delete_warehouse(id IN NUMBER) AS 
        count_warehouse     NUMBER;
    BEGIN 
        warehouse_pkg.check_if_warehouse_exists(id);
        DELETE FROM warehouses WHERE warehouse_id = id;
    END;
    
    FUNCTION get_warehouse(id IN NUMBER) RETURN warehouse_type AS 
        warehouse           warehouse_type;
        count_warehouse     NUMBER;
        vname               VARCHAR2(30);
        vaddress            VARCHAR2(30);
    BEGIN 
        warehouse_pkg.check_if_warehouse_exists(id);
        
        SELECT name, address INTO vname, vaddress FROM warehouses
        WHERE warehouse_id = id;
        
        warehouse := warehouse_type(vname, vaddress);
        
        RETURN warehouse;
    END;
    
    PROCEDURE insert_product_into_warehouse(vwarehouseid IN NUMBER, vproductid IN NUMBER, initial_quant IN NUMBER) AS
        count_prod NUMBER;
    BEGIN 
        warehouse_pkg.check_if_warehouse_exists(vwarehouseid);
        product_pkg.check_if_product_exists(vproductid);
        
        IF initial_quant < 0 THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Initial quantity must not be negative');
        END IF;
        
        SELECT COUNT(*) INTO count_prod FROM products_warehouses 
        WHERE product_id = vproductid;
        
        IF count_prod > 0 THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Product already in warehouse');
        END IF;
        
        INSERT INTO products_warehouses (warehouse_id, product_id, quantity) 
        VALUES (vwarehouseid, vproductid, initial_quant);
    END;

    
    PROCEDURE update_stock(vwarehouseid IN NUMBER, vproductid IN NUMBER, vquantity IN NUMBER) AS
    BEGIN 
        warehouse_pkg.check_if_warehouse_exists(vwarehouseid);
        product_pkg.check_if_product_exists(vproductid);
        
        IF vquantity < 0 THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Quantity cannot be negative');
        END IF;
        
        UPDATE products_warehouses SET quantity = vquantity 
        WHERE warehouse_id = vwarehouseid AND product_id = vproductid;
        
        EXCEPTION 
            WHEN value_error THEN 
                RAISE_APPLICATION_ERROR(-20001, 'Invalid quantity, try another number');
    END;

    
    FUNCTION get_all_warehouses RETURN warehouse_pkg.warehouse_array AS 
        vname VARCHAR2(100);
        vaddress VARCHAR2(100);
        count_warehouse NUMBER;
        warehouses warehouse_pkg.warehouse_array;
    BEGIN 
        warehouses := warehouse_pkg.warehouse_array();
        
        SELECT COUNT(*) INTO count_warehouse FROM warehouses;
        
        IF count_warehouse = 0 THEN 
            RAISE_APPLICATION_ERROR(-20001, 'No warehouses to find');
        END IF;
        
        FOR i IN 1 .. count_warehouse LOOP
            SELECT name, address INTO vname, vaddress FROM warehouses
            WHERE warehouse_id = i;
            
            warehouses.EXTEND;
            warehouses(i) := warehouse_type(vname, vaddress);
        END LOOP;
        
        RETURN warehouses;
    END;
    
    FUNCTION get_stock(id IN NUMBER) RETURN NUMBER AS
        stock NUMBER;
    BEGIN 
        product_pkg.check_if_product_exists(id);
        SELECT SUM(quantity) INTO stock FROM products_warehouses 
        WHERE product_id = id;
        RETURN stock;
    END;
END warehouse_pkg;
/

/*******************************************************************************
SPECIAL TRIGGERS
********************************************************************************/
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
    UPDATE products_warehouses SET quantity = quantity - :NEW.quantity WHERE warehouse_id = 
        (SELECT warehouse_id FROM products_warehouses WHERE product_id = :NEW.product_id ORDER BY quantity ASC FETCH FIRST ROW ONLY)
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