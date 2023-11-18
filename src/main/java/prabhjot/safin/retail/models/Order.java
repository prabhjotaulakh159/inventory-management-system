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
    public Order(int customerId, int storeId) {
        this.customerId = customerId;
        this.storeId = storeId;
        this.orderDate = null;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.customerId = stream.readInt();
        this.storeId = stream.readInt();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeInt(this.customerId);
        stream.writeInt(this.storeId);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
