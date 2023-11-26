package prabhjot.safin.retail.services.audit;

/**
 * Defines the names of all tables that are audited
 * @author Prabhjot Aulakh, Safin Haque
 */
public enum AuditTable {
    ADMINS("admins_audit", "admin_id"),
    CUSTOMERS("customers_audit", "customer_id"),
    CATEGORIES("categories_audit", "category_id"),
    WAREHOUSES("warehouses_audit", "warehouse_id"),
    PRODUCTS("products_audit", "product_id"),
    STORES("stores_audit", "store_id"),
    ORDERS("orders_audit", "order_id"),
    REVIEWS("reviews_audit", "review_id");

    private final String tableName;
    private final String idColName;

    private AuditTable(String tableName, String idColName) {
        this.tableName = tableName;
        this.idColName = idColName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getIdColName() {
        return this.idColName;
    }
}
