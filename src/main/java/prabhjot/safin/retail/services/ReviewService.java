package prabhjot.safin.retail.services;

import java.sql.Connection;
import java.sql.SQLException;

import prabhjot.safin.retail.models.Review;

/**
 * Performs crud operations on the reviews table
 * @author Prabhjot Aulakh, Safin Haque
 */
public class ReviewService {
    private Connection connection;

    public ReviewService(Connection connection) {
        this.connection = connection;
    }

    public void createReview(Review review) throws SQLException {
        throw new UnsupportedOperationException("TODO");
    }
}
