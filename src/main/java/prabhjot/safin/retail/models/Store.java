package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class Store implements SQLData{
    private String name;
    private String type= "STORE_TYPE";

    public Store(){

    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
