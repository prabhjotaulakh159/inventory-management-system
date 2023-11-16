package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents a customer in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Customer implements SQLData {
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String password;
    private String type = "CUSTOMER_TYPE";

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.firstname = stream.readString();
        this.lastname = stream.readString();
        this.email = stream.readString();
        this.address = stream.readString();
        this.password = stream.readString();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(this.firstname);
        stream.writeString(this.lastname);
        stream.writeString(this.email);
        stream.writeString(this.address);
        stream.writeString(this.password);
    }   
    
     /**
     * Gets the first name of the customer.
     *
     * @return The first name of the customer.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the first name of the customer.
     *
     * @param firstname The new first name for the customer.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Gets the last name of the customer.
     *
     * @return The last name of the customer.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the last name of the customer.
     *
     * @param lastname The new last name for the customer.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Gets the email address of the customer.
     *
     * @return The email address of the customer.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the customer.
     *
     * @param email The new email address for the customer.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the address of the customer.
     *
     * @return The address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address The new address for the customer.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the password of the customer.
     *
     * @return The password of the customer.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the customer.
     *
     * @param password The new password for the customer.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}