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