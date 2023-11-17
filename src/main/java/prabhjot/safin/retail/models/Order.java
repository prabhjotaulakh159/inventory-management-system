package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents an orders in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Order implements SQLData {
    private int customerId;
    private int storeId;
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

    /**
     * Gets the id of the customer
     * @return Id of the customer
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * Sets the id of the customer
     * @param customerId Id of the customer
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
