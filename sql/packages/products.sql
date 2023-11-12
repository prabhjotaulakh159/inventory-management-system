CREATE OR REPLACE TYPE product AS OBJECT (
    name        VARCHAR2(100),
    category    NUMBER
);
/

CREATE OR REPLACE PACKAGE product_pkg AS 
    invalid_product EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_product, -20005);
    
    PROCEDURE check_if_product_exists(id IN NUMBER);
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
END product_pkg;
/

