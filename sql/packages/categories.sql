CREATE OR REPLACE TYPE category_type AS OBJECT (
    category    VARCHAR2(100)
);
/

CREATE OR REPLACE PACKAGE category_pkg AS 
    invalid_category EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_category, -20006);
    
    TYPE categories IS VARRAY(100) OF category_type;
    
    PROCEDURE check_if_category_exists(id IN NUMBER);
    PROCEDURE create_category(category IN category_type);
    PROCEDURE update_category(id IN NUMBER, vcategory IN VARCHAR2);
    PROCEDURE delete_category(id IN NUMBER);
    FUNCTION get_categories RETURN category_pkg.categories;
    FUNCTION get_category(id IN NUMBER) RETURN category_type;
    
END category_pkg;
/

CREATE OR REPLACE PACKAGE BODY category_pkg AS 
    PROCEDURE check_if_category_exists(id IN NUMBER) AS 
        count_category NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_category FROM categories 
        WHERE category_id = id;
        
        IF count_category = 0 THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category does not exist');
        END IF;
    END;

    PROCEDURE create_category(category IN category_type) AS 
    BEGIN
        IF category.category IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category cannot be null');
        END IF;
        
        INSERT INTO categories (category) VALUES (category.category);
    END;
    
    PROCEDURE update_category(id IN NUMBER, vcategory IN VARCHAR2) AS
    BEGIN 
        category_pkg.check_if_category_exists(id);
        
        IF vcategory IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20006, 'Category cannot be null');
        END IF;
        
        UPDATE categories SET category = vcategory 
        WHERE category_id = id;
    END;
    
    PROCEDURE delete_category(id IN NUMBER) AS
    BEGIN 
        category_pkg.check_if_category_exists(id);
        DELETE FROM categories WHERE category_id = id;
    END;
    
    FUNCTION get_categories RETURN category_pkg.categories AS
        count_category NUMBER;
        vcategory VARCHAR2(100);
        categories category_pkg.categories;
    BEGIN 
        categories := category_pkg.categories();

        SELECT COUNT(*) INTO count_category FROM categories;
        
        IF count_category = 0 THEN  
            RAISE_APPLICATION_ERROR(-20006, 'No categories to find !');
        END IF;
        
        FOR i IN 1 .. count_category LOOP
            SELECT category INTO vcategory FROM categories 
            WHERE category_id = i;
            
            categories.EXTEND;
            categories(i) := category_type(vcategory);
        END LOOP;
            
        RETURN categories;
    END;
    
    FUNCTION get_category (id IN NUMBER) RETURN category_type AS
        vcategory VARCHAR2(100);
    BEGIN 
        category_pkg.check_if_category_exists(id);
        SELECT category INTO vcategory FROM categories WHERE category_id = id;
        RETURN category_type(vcategory);
    END;
END category_pkg;
/

DECLARE 
    category category_type;
BEGIN 
    category := category_pkg.get_category(1);
    dbms_output.put_line(category.category);
END;