CREATE OR REPLACE TYPE review_type AS OBJECT (
    customer        NUMBER,
    product         NUMBER,
    flags           NUMBER,
    rating          NUMBER,
    description     VARCHAR2(100)
);
/

CREATE OR REPLACE PACKAGE review_pkg AS
    invalid_review EXCEPTION;
    PRAGMA EXCEPTION_INIT(invalid_review, -20000);
    
    PROCEDURE create_review(review IN review_type);
    PROCEDURE update_review(id IN NUMBER, vrating IN NUMBER, vdesc IN VARCHAR2);
    PROCEDURE delete_review(id IN NUMBER);
    FUNCTION get_review(id IN NUMBER) RETURN review_type;
END review_pkg;
/

CREATE OR REPLACE PACKAGE BODY review_pkg AS 
    PROCEDURE create_review(review IN review_type) AS
        count_cust      NUMBER;
        count_prod      NUMBER;
    BEGIN 
        IF review.customer IS NULL OR review.product IS NULL 
                OR review.rating IS NULL
                OR review.description IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review cannot have null values');
        END IF;
        
        IF review.rating NOT BETWEEN 1 AND 5 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review must be between 1 and 5');
        END IF;
        
        IF LENGTH(review.description) > 30 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Description is 30 characters max');
        END IF;
        
        SELECT COUNT(*) INTO count_cust FROM customers 
        WHERE customer_id = review.customer;
        
        SELECT COUNT(*) INTO count_prod FROM products 
        WHERE product_id = review.product;
        
        IF count_cust = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Customer not found');
        END IF;
        
        IF count_prod = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Product not found');
        END IF;
        
        INSERT INTO reviews (customer_id, product_id, rating, description)
        VALUES (review.customer, review.product, review.rating, 
            review.description);
    END;
    
    PROCEDURE update_review(id IN NUMBER, vrating IN NUMBER, vdesc IN VARCHAR2) AS
        count_rev      NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_rev FROM reviews 
        WHERE review_id = id;
        
        IF count_rev = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review not found');
        END IF;
        
        IF vrating NOT BETWEEN 1 AND 5 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review must be between 1 and 5');
        END IF;
        
        IF LENGTH(vdesc) > 30 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Description is 30 characters max');
        END IF;
        
        UPDATE reviews SET rating = vrating, description = vdesc 
        WHERE review_id = id;
    END;
    
    PROCEDURE delete_review(id IN NUMBER) AS 
         count_rev      NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_rev FROM reviews 
        WHERE review_id = id;
        
        IF count_rev = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review not found');
        END IF;
        
        DELETE FROM reviews WHERE review_id = id;
    END;
    
    FUNCTION get_review(id IN NUMBER) RETURN review_type AS 
        review          review_type;
        count_rev       NUMBER;
        custid          NUMBER;
        prodid          NUMBER;
        vflags          NUMBER;
        vrating         NUMBER;
        vdesc           VARCHAR2(30);
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_rev FROM reviews 
        WHERE review_id = id;
        
        IF count_rev = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review not found');
        END IF;
        
        SELECT customer_id, product_id, flags, rating, description 
        INTO custid, prodid, vflags, vrating, vdesc 
        FROM reviews WHERE review_id = id;
        
        review := review_type(custid, prodid, vflags, vrating, vdesc);
        
        RETURN review;
    END;
END review_pkg;
/