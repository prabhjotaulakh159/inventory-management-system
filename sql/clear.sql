DROP PACKAGE admin_pkg;
DROP PACKAGE category_pkg;
DROP PACKAGE customer_pkg;
DROP PACKAGE order_pkg;
DROP PACKAGE product_pkg;
DROP PACKAGE review_pkg;
DROP PACKAGE store_pkg;
DROP PACKAGE warehouse_pkg;

DROP TRIGGER replenish_stock;
DROP TRIGGER validate_stock;
DROP TRIGGER reviews_audit_log;
DROP TRIGGER orders_products_audit_log;
DROP TRIGGER orders_audit_log;
DROP TRIGGER products_stores_audit_log;
DROP TRIGGER stores_audit_log;
DROP TRIGGER products_warehouses_audit_log;
DROP TRIGGER products_audit_log;
DROP TRIGGER warehouses_audit_log;
DROP TRIGGER categories_audit_log;
DROP TRIGGER customers_audit_log;
DROP TRIGGER admins_audit_log;

DROP TABLE reviews_audit;
DROP TABLE orders_products_audit;
DROP TABLE orders_audit;
DROP TABLE products_stores_audit;
DROP TABLE stores_audit;
DROP TABLE products_warehouses_audit;
DROP TABLE products_audit;
DROP TABLE warehouses_audit;
DROP TABLE categories_audit;
DROP TABLE customers_audit;
DROP TABLE admins_audit;

DROP TABLE reviews;
DROP TABLE orders_products;
DROP TABLE orders;
DROP TABLE products_stores;
DROP TABLE stores;
DROP TABLE products_warehouses;
DROP TABLE products;
DROP TABLE warehouses;
DROP TABLE categories;
DROP TABLE customers;
DROP TABLE admins;

DROP TYPE number_array FORCE;
DROP TYPE order_type;
DROP TYPE category_type;
DROP TYPE customer_type;
DROP TYPE product_type;
DROP TYPE review_type ;
DROP TYPE store_type;
DROP TYPE warehouse_type FORCE;
