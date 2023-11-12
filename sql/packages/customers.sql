CREATE OR REPLACE TYPE customer_type AS OBJECT (
    firstname       VARCHAR2(100),
    lastname        VARCHAR2(100),
    email           VARCHAR2(100),
    address         VARCHAR2(100),
    password        VARCHAR2(100)
);
/

CREATE OR REPLACE PACKAGE customer_pkg AS 
    invalid_customer EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_customer, -20004);
    
    PROCEDURE check_if_customer_exists(id IN NUMBER);
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
END customer_pkg;
/