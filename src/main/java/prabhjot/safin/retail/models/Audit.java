package prabhjot.safin.retail.models;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class Audit implements SQLData {
    public String change;
    public Date date;
    public int obj_id;
    public String type= "AUDIT_TYPE";


    public Audit(){
      
    }
    
    public Audit(String change, Date date, int obj_id) {
      this.change = change;
      this.date = date;
      this.obj_id = obj_id;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
      return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
      this.change= stream.readNString();
      this.date= stream.readDate();
      this.obj_id= stream.readInt();
      this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
      stream.writeString(this.change);
      stream.writeDate(this.date);
      stream.writeInt(this.obj_id);
    }

    public String getChange() {
      return change;
    }

    public void setChange(String change) {
      this.change = change;
    }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    public int getObj_id() {
      return obj_id;
    }

    public void setObj_id(int obj_id) {
      this.obj_id = obj_id;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    

    

    
}
