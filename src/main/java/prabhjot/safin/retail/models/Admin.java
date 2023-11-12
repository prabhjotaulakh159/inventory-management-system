package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents an admin in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Admin implements SQLData {
    private int id;
    private String password;
    private String type = "ADMIN_OBJ";

    public Admin() {

    }

    public Admin(int id, String password) {
        this.id = id;
        this.password = password;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.id = stream.readInt();
        this.password = stream.readString();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeInt(this.id);
        stream.writeString(this.password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
