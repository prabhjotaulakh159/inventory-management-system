CREATE OR REPLACE PACKAGE product_pkg AS 
    invalid_product EXCEPTION;
    
    PRAGMA EXCEPTION_INIT(invalid_product, -20005);
    
    TYPE products IS VARRAY(100) OF product_type;
    
    PROCEDURE check_if_product_exists(id IN NUMBER);
    PROCEDURE create_product(product IN product_type);
    PROCEDURE update_product(id IN NUMBER, product IN product_type);
    PROCEDURE delete_product(id IN NUMBER);
    FUNCTION get_product(id IN NUMBER) RETURN product_type;
    FUNCTION get_products RETURN product_pkg.products;

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
    
    PROCEDURE create_product(product IN product_type) AS 
    BEGIN 
        category_pkg.check_if_category_exists(product.category);
        
        IF product.name IS NULL THEN
            RAISE_APPLICATION_ERROR(-20005, 'Product name cannot be null');
        END IF;
        
        IF LENGTH(product.name) NOT BETWEEN 1 AND 100 THEN 
            RAISE_APPLICATION_ERROR(-20005, 'Product name must be between 1 and 100 characters');
        END IF;
        
        INSERT INTO products (name, category_id) VALUES (product.name, product.category);
    END;
    
    PROCEDURE update_product(id IN NUMBER, product IN product_type) AS 
    BEGIN 
        product_pkg.check_if_product_exists(id);
        category_pkg.check_if_category_exists(product.category);
        
        IF product.name IS NULL THEN
            RAISE_APPLICATION_ERROR(-20005, 'Product name cannot be null');
        END IF;
        
        IF LENGTH(product.name) NOT BETWEEN 1 AND 100 THEN 
            RAISE_APPLICATION_ERROR(-20005, 'Product name must be between 1 and 100 characters');
        END IF;
        
        UPDATE products SET name = product.name, category_id = product.category 
        WHERE product_id = id;
    END;
    
    PROCEDURE delete_product(id IN NUMBER) AS
    BEGIN 
        product_pkg.check_if_product_exists(id);
        DELETE FROM products WHERE product_id = id;
    END;

    FUNCTION get_product(id IN NUMBER) RETURN product_type AS 
        vname NUMBER;
        category NUMBER;
    BEGIN 
        product_pkg.check_if_product_exists(id);
        
        SELECT name, category_id INTO vname, category FROM products 
        WHERE product_id = id;
        
        RETURN product_type(vname, category);
    END;
    
    FUNCTION get_products RETURN product_pkg.products AS
        vname NUMBER;
        vcat NUMBER;
        products product_pkg.products;
        count_prod NUMBER;
    BEGIN 
        products := product_pkg.products();
        SELECT COUNT(*) INTO count_prod FROM products;
        
        IF count_prod = 0 THEN 
            RAISE_APPLICATION_ERROR(-20005, 'No products to fetch !');
        END IF;
        
        FOR i IN 1 .. count_prod LOOP
            SELECT name, category_id INTO vname, vcat FROM products 
            WHERE product_id = i;
            products.EXTEND;
            products(i) := product_type(vname, vcat);
        END LOOP;
        
        RETURN products;
    END;
END product_pkg;
/

