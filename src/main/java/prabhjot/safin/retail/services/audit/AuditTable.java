package prabhjot.safin.retail.services.audit;

/**
 * Defines the names of all tables that are audited
 * @author Prabhjot Aulakh, Safin Haque
 */
public enum AuditTable {
    ADMINS("admins_audit"),
    CUSTOMERS("customers_audit"),
    CATEGORIES("categories_audit"),
    WAREHOUSES("warehouses_audit"),
    PRODUCTS("products_audit"),
    PRODUCTS_WAREHOUSES("products_warehouses_audit"),
    STORES("stores_audit"),
    PRODUCTS_STORES("products_stores_audit"),
    ORDERS("orders_audit"),
    ORDERS_PRODUCTS("orders_products_audit"),
    REVIEWS("reviews_audit");

    private final String tableName;

    private AuditTable(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return this.tableName;
    }
}
