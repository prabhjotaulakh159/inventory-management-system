package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents a warehouse in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Warehouse implements SQLData {
    private String name;
    private String address;
    private String type = "WAREHOUSE_TYPE";

    public Warehouse() {

    }

    public Warehouse(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.name = stream.readString();
        this.address = stream.readString();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(this.name);
        stream.writeString(this.address);
    }

    /**
     * Gets the name of the warehouse.
     * @return The name of the warehouse.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the warehouse.
     * @param name The name to set for the warehouse.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the warehouse.
     * @return The address of the warehouse.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the warehouse.
     * @param address The address to set for the warehouse.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address: " + this.address + ", Name: " + this.name;
    }
}