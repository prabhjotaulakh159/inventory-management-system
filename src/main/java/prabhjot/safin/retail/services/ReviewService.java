package prabhjot.safin.retail.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import prabhjot.safin.retail.models.Review;

/**
 * Performs crud operations on the reviews table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class ReviewService {
    private Connection connection;

    /**
     * Constructs a new ReviewService with the specified database connection.
     *
     * @param connection The database connection to be used by the service.
     */
    public ReviewService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new review in the database.
     *
     * @param review The Review object representing the review to be created.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException If the class is not found
     */
    public void create(Review review) throws SQLException, ClassNotFoundException {
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put(review.getSQLTypeName(), Class.forName("prabhjot.safin.retail.models.Review"));
        String SQL = "{call review_pkg.create_review(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setObject(1, review);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Updates an existing review in the database.
     *
     * @param reviewId    The ID of the review to be updated.
     * @param rating      The new rating for the review.
     * @param description The new description for the review.
     * @throws SQLException            If a database access error occurs.
     */
    public void update(int reviewId, int rating, String description) throws SQLException, ClassNotFoundException {
        String SQL = "{call review_pkg.update_review(?, ?, ?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setInt(1, reviewId);
        callableStatement.setInt(2, rating);
        callableStatement.setString(3, description);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Deletes a review from the database based on its ID.
     *
     * @param reviewId The ID of the review to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void delete(int reviewId) throws SQLException {
        String SQL = "{call review_pkg.delete_review(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.setInt(1, reviewId);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Retrieves a review from the database based on its ID.
     *
     * @param reviewId The ID of the review to be retrieved.
     * @return The Review object representing the retrieved review, or null if not found.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Review getReview(int reviewId) throws SQLException, ClassNotFoundException { 
        Map<String, Class<?>> map = this.connection.getTypeMap();
        this.connection.setTypeMap(map);
        map.put("REVIEW_TYPE", Class.forName("prabhjot.safin.retail.models.Review"));
        String SQL = "{? = call review_pkg.get_review(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.STRUCT, "REVIEW_TYPE");
        callableStatement.setInt(2, reviewId);
        callableStatement.execute();
        Review review = (Review) callableStatement.getObject(1);
        callableStatement.close();
        return review;
    }

    /**
     * Retrieves all reviews from the database and returns them as a Map.
     *
     * @return A Map where keys are review IDs and values are Review objects.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Map<Integer, Review> getAllReviews() throws SQLException, ClassNotFoundException {
        Map<Integer, Review> reviews = new HashMap<Integer, Review>();
        String SQL = "{? = call review_pkg.get_all_reviews()}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            reviews.put(resultSet.getInt(2), this.getReview(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return reviews;
    }

    /**
     * Retrieves flagged reviews from the database and returns them as a Map.
     *
     * @return A Map where keys are review IDs and values are Review objects.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Map<Integer, Review> getFlaggedReviews() throws SQLException, ClassNotFoundException {
        Map<Integer, Review> reviews = new HashMap<Integer, Review>();
        String SQL = "{? = call review_pkg.get_flagged_reviews()}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            reviews.put(resultSet.getInt(2), this.getReview(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return reviews;
    }

    /**
     * Deletes flagged reviews from the database.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void deleteFlaggedReviews() throws SQLException {
        String SQL = "{call review_pkg.delete_flagged_reviews()}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Retrieves reviews for a specific product from the database and returns them as a Map.
     *
     * @param productId The ID of the product for which reviews are retrieved.
     * @return A Map where keys are review IDs and values are Review objects.
     * @throws SQLException            If a database access error occurs.
     * @throws ClassNotFoundException Thrown if the JVM cannot find the specified class in the classpath.
     */
    public Map<Integer, Review> getReviewForProduct(int productId) throws SQLException, ClassNotFoundException {
        Map<Integer, Review> reviews = new HashMap<Integer, Review>();
        String SQL = "{? = call review_pkg.get_review_for_product(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.setInt(2, productId);
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            reviews.put(resultSet.getInt(2), this.getReview(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return reviews;
    }

    /**
     * Flags a review
     * @param reviewId Id of review to flag
     * @throws SQLException
     */
    public void flagReview(int reviewId) throws SQLException {
        CallableStatement callableStatement = this.connection.prepareCall("{call review_pkg.flag_review(?)}");
        callableStatement.setInt(1, reviewId);
        callableStatement.execute();
        this.connection.commit();
        callableStatement.close();
    }

    /**
     * Gets all reviews from a customer
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Map<Integer, Review> getReviewsForCustomer(int customerId) throws SQLException, ClassNotFoundException {
        Map<Integer, Review> reviews = new HashMap<Integer, Review>();
        String SQL = "{? = call review_pkg.get_customer_reviews(?)}";
        CallableStatement callableStatement = this.connection.prepareCall(SQL);
        callableStatement.registerOutParameter(1, Types.ARRAY, "NUMBER_ARRAY");
        callableStatement.setInt(2, customerId);
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getArray(1).getResultSet();
        while (resultSet.next()) {
            reviews.put(resultSet.getInt(2), this.getReview(resultSet.getInt(2)));
        }
        callableStatement.close();
        resultSet.close();
        return reviews;
    }
}