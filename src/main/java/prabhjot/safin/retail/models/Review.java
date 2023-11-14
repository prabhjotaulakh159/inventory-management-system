package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents a review in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Review implements SQLData {
    private int customerId;
    private int productId;
    private int flags;
    private int rating;
    private String description;
    private String type = "REVIEW_TYPE";

    public Review() {

    }

    public Review(int customerId, int productId, int flags, int rating, String description) {
        this.customerId = customerId;
        this.productId = productId;
        this.flags = flags;
        this.rating = rating;
        this.description = description;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.customerId = stream.readInt();
        this.productId = stream.readInt();
        this.flags = stream.readInt();
        this.rating = stream.readInt();
        this.description = stream.readString();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeInt(this.customerId);
        stream.writeInt(this.productId);
        stream.writeInt(this.flags);
        stream.writeInt(this.rating);
        stream.writeString(this.description);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
