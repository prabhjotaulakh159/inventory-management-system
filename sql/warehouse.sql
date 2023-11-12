CREATE OR REPLACE TYPE warehouse_type AS OBJECT (
    name        VARCHAR2(100),
    address     VARCHAR2(100)
);
/

CREATE OR REPLACE PACKAGE warehouse_pkg AS 
    invalid_warehouse EXCEPTION;
    PRAGMA EXCEPTION_INIT(invalid_warehouse, -20001);    
    
    PROCEDURE create_warehouse(warehouse IN warehouse_type);
    PROCEDURE update_warehouse(
            id IN NUMBER, vname IN VARCHAR2, vaddress IN VARCHAR2);
    PROCEDURE delete_warehouse(id IN NUMBER);
    FUNCTION get_warehouse(id IN NUMBER) RETURN warehouse_type;
END warehouse_pkg;
/

CREATE OR REPLACE PACKAGE BODY warehouse_pkg AS
    PROCEDURE create_warehouse(warehouse IN warehouse_type) AS 
    BEGIN 
        IF warehouse.name IS NULL OR warehouse.address IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Warehouse fields cannot be null');
        END IF;
        
        IF LENGTH(warehouse.name)       NOT BETWEEN 1 AND 100 OR
           LENGTH(warehouse.address)    NOT BETWEEN 1 AND 100 
           THEN RAISE_APPLICATION_ERROR(
                -20001, 'Name/address must be between 1 and 100 charecters');
        END IF;
        
        INSERT INTO warehouses (name, address) 
        VALUES (warehouse.name, warehouse.address);
    END;
    
    PROCEDURE update_warehouse(
            id IN NUMBER, vname IN VARCHAR2, vaddress IN VARCHAR2) AS
        count_warehouse     NUMBER;
    BEGIN 
        IF id IS NULL OR vname IS NULL OR vaddress IS NULL THEN 
            RAISE_APPLICATION_ERROR(
                -20001, 'Id, name and address cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_warehouse FROM warehouses 
        WHERE warehouse_id = id;
        
        IF count_warehouse = 0 THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Warehouse does not exist');
        END IF;
        
        IF LENGTH(vname)       NOT BETWEEN 1 AND 100 OR
           LENGTH(vaddress)    NOT BETWEEN 1 AND 100 
           THEN RAISE_APPLICATION_ERROR(
                -20001, 'Name/address must be between 1 and 100 charecters');
        END IF;  
        
        UPDATE warehouses SET name = vname, address = vaddress 
        WHERE warehouse_id = id;
    END;
    
    PROCEDURE delete_warehouse(id IN NUMBER) AS 
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
        
        DELETE FROM warehouses WHERE warehouse_id = id;
    END;
    
    FUNCTION get_warehouse(id IN NUMBER) RETURN warehouse_type AS 
        warehouse           warehouse_type;
        count_warehouse     NUMBER;
        vname               VARCHAR2(30);
        vaddress            VARCHAR2(30);
    BEGIN 
        IF id IS NULL THEN
            RAISE_APPLICATION_ERROR(-20001, 'Id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_warehouse FROM warehouses 
        WHERE warehouse_id = id;
        
        IF count_warehouse = 0 THEN 
            RAISE_APPLICATION_ERROR(-20001, 'Warehouse does not exist');
        END IF;
        
        SELECT name, address INTO vname, vaddress FROM warehouses
        WHERE warehouse_id = id;
        
        warehouse := warehouse_type(vname, vaddress);
        
        RETURN warehouse;
    END;
END warehouse_pkg;
/