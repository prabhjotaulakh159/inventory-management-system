package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents a store in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Store implements SQLData{
    private String name;
    private String type= "STORE_TYPE";

    public Store(){

    }

    /**
     * Constructor
     * @param name Name of the store
     */
    public Store(String name){
        this.name= name;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

      @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.name = stream.readString();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(this.name);
    }

    /**
     * Gets the name of the store
     * @return Name of the store
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the store
     * @param name Name of the store
     */
    public void setName(String name) {
        this.name = name;
    }    

    @Override
    public String toString() {
        return "Store Name: " + this.name;
    }
}
