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
    order_date          DATE                NOT NULL
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
    flags               NUMBER              NOT NULL CHECK (flags > -1),
    rating              NUMBER              NOT NULL CHECK (rating >= 1 AND rating <= 5),
    description         VARCHAR2(100)       NOT NULL CHECK (LENGTH(description) > 0)
);

/*******************************************************************************
AUDIT TABLES
*******************************************************************************/
CREATE TABLE admins_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    admin_id           NUMBER               REFERENCES admins (admin_id) NOT NULL
);

CREATE TABLE customers_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    customer_id         NUMBER              REFERENCES customers (customer_id) NOT NULL
);

CREATE TABLE categories_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    category_id         NUMBER              REFERENCES categories (category_id) NOT NULL
);

CREATE TABLE warehouses_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    warehouse_id        NUMBER              REFERENCES warehouses (warehouse_id) NOT NULL
);

CREATE TABLE products_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    product_id          NUMBER              REFERENCES products (product_id) NOT NULL
);

CREATE TABLE products_warehouses_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    product_id          NUMBER              REFERENCES products (product_id) NOT NULL,
    warehouse_id        NUMBER              REFERENCES warehouses (warehouse_id) NOT NULL
);

CREATE TABLE stores_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    store_id            NUMBER              REFERENCES stores (store_id) NOT NULL
);

CREATE TABLE products_stores_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    store_id            NUMBER              REFERENCES stores (store_id) NOT NULL,
    product_id          NUMBER              REFERENCES products (product_id) NOT NULL
);

CREATE TABLE orders_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    order_id            NUMBER              REFERENCES orders (order_id) NOT NULL
);

CREATE TABLE orders_products_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    order_id            NUMBER              REFERENCES orders (order_id) NOT NULL,
    product_id          NUMBER              REFERENCES products (product_id) NOT NULL
);

CREATE TABLE reviews_audit (
    action              CHAR(6)             NOT NULL CHECK (action IN ('insert', 'update', 'delete')),
    audit_date          DATE                DEFAULT SYSDATE NOT NULL,
    review_id           NUMBER              REFERENCES reviews (review_id) NOT NULL
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
        INSERT INTO admins_audit (action, admin_id)
        VALUES ('insert', :NEW.admin_id);
    ELSIF UPDATING THEN 
        INSERT INTO admins_audit (action, admin_id)
        VALUES ('update', :NEW.admin_id);
    ELSIF DELETING THEN 
        INSERT INTO admins_audit (action, admin_id)
        VALUES ('delete', :OLD.admin_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER customers_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON customers 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO customers_audit (action, customer_id) 
        VALUES ('insert', :NEW.customer_id);
    ELSIF UPDATING THEN 
        INSERT INTO customers_audit (action, customer_id) 
        VALUES ('update', :NEW.customer_id);
    ELSIF DELETING THEN 
        INSERT INTO customers_audit (action, customer_id) 
        VALUES ('delete', :OLD.customer_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER categories_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON categories 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO categories_audit (action, category_id) 
        VALUES ('insert', :NEW.category_id);
    ELSIF UPDATING THEN 
        INSERT INTO categories_audit (action, category_id) 
        VALUES ('update', :NEW.category_id);
    ELSIF DELETING THEN 
        INSERT INTO categories_audit (action, category_id) 
        VALUES ('update', :OLD.category_id);
    END IF;
END;    
/

CREATE OR REPLACE TRIGGER warehouses_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON warehouses 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO warehouses_audit (action, warehouse_id) 
        VALUES ('insert', :NEW.warehouse_id);
    ELSIF UPDATING THEN 
        INSERT INTO warehouses_audit (action, warehouse_id) 
        VALUES ('update', :NEW.warehouse_id);
    ELSIF DELETING THEN 
        INSERT INTO warehouses_audit (action, warehouse_id) 
        VALUES ('delete', :OLD.warehouse_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER products_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON products 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO products_audit (action, product_id) 
        VALUES ('insert', :NEW.product_id);
    ELSIF UPDATING THEN 
        INSERT INTO products_audit (action, product_id) 
        VALUES ('update', :NEW.product_id);
    ELSIF DELETING THEN 
        INSERT INTO products_audit (action, product_id) 
        VALUES ('delete', :OLD.product_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER products_warehouses_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON products_warehouses 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO products_warehouses_audit (action, product_id, warehouse_id) 
        VALUES ('insert', :NEW.product_id, :NEW.warehouse_id);
    ELSIF UPDATING THEN 
        INSERT INTO products_warehouses_audit (action, product_id, warehouse_id) 
        VALUES ('update', :NEW.product_id, :NEW.warehouse_id);
    ELSIF DELETING THEN 
        INSERT INTO products_warehouses_audit (action, product_id, warehouse_id) 
        VALUES ('update', :OLD.product_id, :OLD.warehouse_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER stores_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON stores 
FOR EACH ROW 
BEGIN 
    IF INSERTING THEN 
        INSERT INTO stores_audit (action, store_id)
        VALUES ('insert', :NEW.store_id);
    ELSIF UPDATING THEN 
        INSERT INTO stores_audit (action, store_id) 
        VALUES ('update', :NEW.store_id);
    ELSIF DELETING THEN 
        INSERT INTO stores_audit (action, store_id) 
        VALUES ('update', :OLD.store_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER products_stores_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON products_stores 
FOR EACH ROW 
BEGIN 
    IF INSERTING THEN 
        INSERT INTO products_stores_audit (action, product_id, store_id) 
        VALUES ('insert', :NEW.product_id, :NEW.store_id);
    ELSIF UPDATING THEN 
        INSERT INTO products_stores_audit (action, product_id, store_id) 
        VALUES ('update', :NEW.product_id, :NEW.store_id);
    ELSIF DELETING THEN 
        INSERT INTO products_stores_audit (action, product_id, store_id) 
        VALUES ('update', :OLD.product_id, :OLD.store_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER orders_audit_log 
AFTER INSERT OR DELETE 
ON orders 
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO orders_audit (action, order_id) 
        VALUES ('insert', :NEW.order_id);
    ELSIF UPDATING THEN 
        INSERT INTO orders_audit (action, order_id) 
        VALUES ('update', :NEW.order_id);
    ELSIF DELETING THEN 
        INSERT INTO orders_audit (action, order_id) 
        VALUES ('update', :OLD.order_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER orders_products_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON orders_products
FOR EACH ROW
BEGIN 
    IF INSERTING THEN 
        INSERT INTO orders_products_audit (action, order_id, product_id) 
        VALUES ('insert', :NEW.order_id, :NEW.product_id);
    ELSIF UPDATING THEN 
        INSERT INTO orders_products_audit (action, order_id, product_id) 
        VALUES ('update', :NEW.order_id, :NEW.product_id);
    ELSIF DELETING THEN 
        INSERT INTO orders_products_audit (action, order_id, product_id) 
        VALUES ('delete', :OLD.order_id, :OLD.product_id);
    END IF;
END;
/

CREATE OR REPLACE TRIGGER reviews_audit_log 
AFTER INSERT OR UPDATE OR DELETE 
ON reviews 
FOR EACH ROW
BEGIN
    IF INSERTING THEN 
        INSERT INTO reviews_audit (action, review_id) 
        VALUES ('insert', :NEW.review_id);
    ELSIF UPDATING THEN 
        INSERT INTO reviews_audit (action, review_id) 
        VALUES ('update', :NEW.review_id);
    ELSIF DELETING THEN 
        INSERT INTO reviews_audit (action, review_id) 
        VALUES ('update', :OLD.review_id);
    END IF;
END;
/