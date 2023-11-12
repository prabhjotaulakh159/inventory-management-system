CREATE OR REPLACE TYPE store_type AS OBJECT (
    name    VARCHAR2(100)
);
/

CREATE OR REPLACE PACKAGE store_pkg AS 
    invalid_store EXCEPTION;
        
    PRAGMA EXCEPTION_INIT(invalid_store, -20003);
    
    PROCEDURE check_if_store_exists(id IN NUMBER);
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
END store_pkg;
/