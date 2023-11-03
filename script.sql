DROP TABLE products_warehouses;
DROP TABLE orders_products;
DROP TABLE warehouses;
DROP TABLE orders;
DROP TABLE reviews;
DROP TABLE products;
DROP TABLE customers;

CREATE TABLE customers (
    customer_id     NUMBER          GENERATED ALWAYS AS IDENTITY,
    firstname       VARCHAR2(100)    NOT NULL,
    lastname        VARCHAR2(100)    NOT NULL,
    email           VARCHAR2(100)    NOT NULL,
    address         VARCHAR2(100)    NOT NULL,
    
    CONSTRAINT customer_id_pk PRIMARY KEY (customer_id)
);

CREATE TABLE products (
    product_id      NUMBER          GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR2(100)    NOT NULL,
    category        VARCHAR2(100)    NOT NULL,
    price           NUMBER(8,2)     NOT NULL,
    
    CONSTRAINT product_id_pk PRIMARY KEY (product_id),
    CONSTRAINT price_above_0 CHECK (price > 0)
);

CREATE TABLE reviews (
    review_id       NUMBER          GENERATED ALWAYS AS IDENTITY,
    customer_id     NUMBER          NOT NULL,
    product_id      NUMBER          NOT NULL,
    rating          NUMBER(1)       NOT NULL,
    flags           NUMBER(1)       DEFAULT 0 NOT NULL,
    description     VARCHAR2(100),
    
    
    CONSTRAINT review_id_pk PRIMARY KEY (review_id),
    CONSTRAINT reviews_customer_id_fk FOREIGN KEY (customer_id) 
        REFERENCES customers (customer_id) 
        ON DELETE CASCADE,
    CONSTRAINT reviews_product_id_fk FOREIGN KEY (product_id) 
        REFERENCES products (product_id) 
        ON DELETE CASCADE,
    CONSTRAINT rating_between_1_and_5 CHECK (rating >= 1 AND rating <= 5),
    CONSTRAINT flags_not_neg CHECK (flags >= 0)
);

CREATE TABLE orders (
    order_id        NUMBER          GENERATED ALWAYS AS IDENTITY,
    customer_id     NUMBER          NOT NULL,
    store           VARCHAR2(100)   NOT NULL,
    order_date      DATE            NOT NULL,
    
    CONSTRAINT order_id_pk PRIMARY KEY (order_id),
    CONSTRAINT orders_customer_id_fk FOREIGN KEY (customer_id) 
        REFERENCES customers (customer_id) 
        ON DELETE CASCADE
);

CREATE TABLE warehouses (
    warehouse_id        NUMBER          GENERATED ALWAYS AS IDENTITY,
    name                NUMBER          NOT NULL,
    address             VARCHAR2(100)   NOT NULL,
    
    CONSTRAINT warehouse_id_pk PRIMARY KEY (warehouse_id)
);

CREATE TABLE orders_products (
    order_id        NUMBER          NOT NULL,
    product_id      NUMBER          NOT NULL,
    quantity        NUMBER(3)       NOT NULL,
    
    CONSTRAINT orders_products_order_id_fk FOREIGN KEY (order_id) 
        REFERENCES orders (order_id)
        ON DELETE CASCADE,
    CONSTRAINT orders_products_product_id_fk FOREIGN KEY (product_id) 
        REFERENCES products (product_id) 
        ON DELETE CASCADE,
    CONSTRAINT orders_products_quantity_not_negatif CHECK (quantity >= 0)
    
);

CREATE TABLE products_warehouses (
    product_id      NUMBER      NOT NULL,
    warehouse_id    NUMBER      NOT NULL,
    quantity        NUMBER(8)   NOT NULL,
    
    CONSTRAINT product_warehouses_product_id_fk FOREIGN KEY (product_id) 
        REFERENCES products (product_id)
        ON DELETE CASCADE,
    CONSTRAINT product_warehouses_warehouse_id_fk FOREIGN KEY (warehouse_id) 
        REFERENCES warehouses (warehouse_id)
        ON DELETE CASCADE,
    CONSTRAINT product_warehouses_quantity_not_negatif CHECK (quantity >= 0)
);