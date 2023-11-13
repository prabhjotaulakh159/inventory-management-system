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