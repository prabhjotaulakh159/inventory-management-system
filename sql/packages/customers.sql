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