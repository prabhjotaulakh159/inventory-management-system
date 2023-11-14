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