package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents a category in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Category implements SQLData {
    private String category;
    private String type = "CATEGORY_TYPE";

    public Category() {

    }

    /**
     * Constructor
     * @param category Category name 
     */
    public Category(String category) {
        this.category = category;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.category = stream.readString();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(this.category);
    }

    /**
     * Gets the category
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category
     * @param category The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Category: " + this.category;
    }
}