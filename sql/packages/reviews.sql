CREATE OR REPLACE PACKAGE review_pkg AS
    invalid_review EXCEPTION;
    PRAGMA EXCEPTION_INIT(invalid_review, -20000);
    
    TYPE review_array IS VARRAY(1000) OF review_type;
    
    PROCEDURE check_if_review_exists(id IN NUMBER);
    PROCEDURE create_review(review IN review_type);
    PROCEDURE update_review(id IN NUMBER, vrating IN NUMBER, vdescription IN VARCHAR2);
    PROCEDURE delete_review(id IN NUMBER);
    PROCEDURE delete_flagged_reviews;
    
    FUNCTION get_review(id IN NUMBER) RETURN review_type;
    FUNCTION get_all_reviews RETURN review_pkg.review_array;
    FUNCTION get_flagged_reviews RETURN review_pkg.review_array;
END review_pkg;
/

CREATE OR REPLACE PACKAGE BODY review_pkg AS 
    PROCEDURE check_if_review_exists(id IN NUMBER) AS 
        count_review   NUMBER;
    BEGIN 
        IF id IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review id cannot be null');
        END IF;
        
        SELECT COUNT(*) INTO count_review FROM reviews WHERE review_id = id;
        
        IF count_review = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review not found');
        END IF;
    END;

    PROCEDURE check_null(review IN review_type) AS 
    BEGIN 
        IF review.customer IS NULL OR review.product IS NULL 
                OR review.rating IS NULL
                OR review.description IS NULL THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Review cannot have null values');
        END IF;
    END;
    
    PROCEDURE validate_rating(vrating IN NUMBER) AS
    BEGIN
        IF vrating NOT BETWEEN 1 AND 5 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Rating must be between 1 and 5');
        END IF;
    END;
    
    PROCEDURE validate_description(vdescription IN VARCHAR2) AS
    BEGIN 
        IF LENGTH(vdescription) > 30 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'Description is 30 characters max');
        END IF;
    END;
    
    PROCEDURE create_review(review IN review_type) AS
        count_cust      NUMBER;
        count_prod      NUMBER;
    BEGIN 
        customer_pkg.check_if_customer_exists(review.customer);
        product_pkg.check_if_product_exists(review.product);
        
        review_pkg.check_null(review);
        review_pkg.validate_rating(review.rating);
        review_pkg.validate_description(review.description);
              
        INSERT INTO reviews (customer_id, product_id, rating, description)
        VALUES (review.customer, review.product, review.rating, 
            review.description);
    END;
    
    PROCEDURE update_review(id IN NUMBER, vrating IN NUMBER, vdescription IN VARCHAR2) AS
        count_rev      NUMBER;
    BEGIN 
        review_pkg.check_if_review_exists(id);
        review_pkg.validate_rating(vrating);
        review_pkg.validate_description(vdescription);
        
        UPDATE reviews SET rating = vrating, description = vdescription
        WHERE review_id = id;
        
        EXCEPTION 
            WHEN others THEN 
                RAISE_APPLICATION_ERROR(-20000, 'Cannot update review');
    END;
    
    PROCEDURE delete_review(id IN NUMBER) AS 
    BEGIN 
        review_pkg.check_if_review_exists(id);
        DELETE FROM reviews WHERE review_id = id;
    END;
    
    PROCEDURE delete_flagged_reviews AS 
     BEGIN 
        DELETE FROM reviews WHERE flags > 2;
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
        review_pkg.check_if_review_exists(id);
        
        SELECT customer_id, product_id, flags, rating, description 
        INTO custid, prodid, vflags, vrating, vdesc 
        FROM reviews WHERE review_id = id;
        
        review := review_type(custid, prodid, vflags, vrating, vdesc);
        
        RETURN review;
    END;
    
    FUNCTION get_all_reviews RETURN review_pkg.review_array AS 
        custid NUMBER;
        prodid NUMBER;
        vrating NUMBER;
        vflags NUMBER;
        vdesc VARCHAR2(30);
        review review_type;
        reviews review_pkg.review_array;
        num_reviews NUMBER;
    BEGIN 
        reviews := review_pkg.review_array();
        
        SELECT COUNT(*) INTO num_reviews FROM reviews;
        
        IF num_reviews = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'No reviews to get !');
        END IF;
        
        FOR i IN 1 .. num_reviews LOOP
            SELECT customer_id, product_id, flags, rating, description 
            INTO custid, prodid, vflags, vrating, vdesc FROM reviews
            WHERE review_id = i;
            
            reviews.EXTEND;
            reviews(i) := review_type(custid, prodid, vflags, vrating, vdesc);
        END LOOP;
        
        RETURN reviews;
        
        EXCEPTION 
            WHEN others THEN 
                RAISE_APPLICATION_ERROR(-20000, 'Cannot get reviews');
    END;
    
    FUNCTION get_flagged_reviews RETURN review_pkg.review_array AS
        custid NUMBER;
        prodid NUMBER;
        vrating NUMBER;
        vflags NUMBER;
        vdesc VARCHAR2(30);
        
        reviews review_pkg.review_array;
        
        TYPE num_arr IS VARRAY(100) OF NUMBER;
        arr num_arr;
        
    BEGIN 
        arr := num_arr();
        reviews := review_pkg.review_array();
        
        SELECT review_id BULK COLLECT INTO arr FROM reviews WHERE flags > 2;
        
        IF arr.COUNT = 0 THEN 
            RAISE_APPLICATION_ERROR(-20000, 'No flagged reviews te be found');
        END IF;
        
        FOR i IN 1 .. arr.COUNT LOOP 
            SELECT customer_id, product_id, rating, flags, description 
            INTO custid, prodid, vrating, vflags, vdesc 
            FROM reviews WHERE review_id = arr(i);
            
            reviews.EXTEND;
            reviews(i) := review_type(custid, prodid, vflags, vrating, vdesc);
        END LOOP;
        
        RETURN reviews;
    END;
END review_pkg;
/