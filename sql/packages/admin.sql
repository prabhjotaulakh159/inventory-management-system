CREATE OR REPLACE  PACKAGE admin_pkg AS 
    invalid_admin EXCEPTION;
    PRAGMA EXCEPTION_INIT(invalid_admin, -20010);
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN;
END admin_pkg;
/

CREATE OR REPLACE PACKAGE BODY admin_pkg AS 
    PROCEDURE check_if_admin_exists(id IN NUMBER) AS 
        count_admin  NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20010, 'Customer id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_admin FROM admins WHERE admin_id = id;
        
        IF count_admin = 0 THEN 
            RAISE_APPLICATION_ERROR(-20010, 'Admin does not exist');
        END IF;
    END;
    
    FUNCTION login(id IN NUMBER, password IN VARCHAR2) RETURN BOOLEAN AS 
        vpassword VARCHAR2(100);
    BEGIN 
        admin_pkg.check_if_admin_exists(id);
        SELECT password INTO vpassword FROM admins 
        WHERE admin_id = id;
        
        RETURN password = vpassword;
    END;

END admin_pkg;
/