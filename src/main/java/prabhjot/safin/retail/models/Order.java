package prabhjot.safin.retail.models;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents an order in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Order implements SQLData {
    private int customerId;
    private int storeId;
    private Date orderDate;
    private String type = "ORDER_TYPE";
    
    public Order() {
    
    }

    /**
     * Constructor
     * @param customerId Id of the customer
     * @param storeId Id of the store
     */
    public Order(int customerId, int storeId, Date date) {
        this.customerId = customerId;
        this.storeId = storeId;
        this.orderDate = date;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.customerId = stream.readInt();
        this.storeId = stream.readInt();
        this.orderDate = stream.readDate();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeInt(this.customerId);
        stream.writeInt(this.storeId);
        stream.writeDate(this.orderDate);
    }

    /**
     * Accessor for customer id
     * @return Customer who made the order
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Mutator for customer id
     * @param customerId New customer id
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Accessor for store id
     * @return Store id where order is placed
     */
    public int getStoreId() {
        return storeId;
    }

    /**
     * Mutator for store id
     * @param storeId New store id
     */
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    /**
     * Accessor for order date
     * @return Date the order was placed
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * Mutator for order date
     * @param orderDate New order date
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Customer id: " + this.customerId + ", Store id: " + this.storeId + ", Date ordered: " + this.orderDate; 
    }
}
