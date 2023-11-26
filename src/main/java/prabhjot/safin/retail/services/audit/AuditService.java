package prabhjot.safin.retail.services.audit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prabhjot.safin.retail.models.Audit;

/**
 * Performs CRUD operations of audit tables
 */
public class AuditService {
    private Connection connection;

    /**
     * Constructor
     * @param connection Current connection to db
     */
    public AuditService(Connection connection){
      this.connection= connection;
    }

    /**
     * Returns all audit logs on a table
     * @param audit Table to get audit logs on
     * @return List of audit logs on table
     * @throws SQLException
     */
    public List<Audit> getListOfAuditWanted(AuditTable audit) throws SQLException{
        List<Audit> auditList = new ArrayList<>();
        String input = "SELECT * FROM " + audit.getTableName();
        PreparedStatement preparedStatement = this.connection.prepareStatement(input);
        ResultSet results = preparedStatement.executeQuery();
        while(results.next()){
            auditList.add(new Audit(results.getString(1), results.getDate(2), results.getInt(3)));
        }
        preparedStatement.close();
        results.close();
        return auditList;
    }
}
