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

    /**
     * Constructs a new Review.
     *
     * @param customerId The ID of the customer submitting the review.
     * @param productId  The ID of the product being reviewed.
     * @param flags      Flags associated with the review.
     * @param rating     The rating given in the review.
     * @param description The textual description of the review.
     */
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

    /**
     * Gets the customer ID associated with the review.
     *
     * @return The customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID associated with the review.
     *
     * @param customerId The customer ID to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the product ID associated with the review.
     *
     * @return The product ID.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID associated with the review.
     *
     * @param productId The product ID to set.
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the flags associated with the review.
     *
     * @return The flags.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Sets the flags associated with the review.
     *
     * @param flags The flags to set.
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Gets the rating given in the review.
     *
     * @return The rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the rating for the review.
     *
     * @param rating The rating to set.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the textual description of the review.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the textual description of the review.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Customer Id: " + this.customerId + ", Product Id: " + this.productId + ", Flags: " + this.flags + ", Rating: " + this.rating + ", Description: " + this.description; 
    }
}