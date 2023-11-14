package prabhjot.safin.retail.models;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 * Represents a product in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Product implements SQLData{
        private String name;
        private int category_id;
        private String type= "PRODUCT_TYPE";
        
        public Product(){

        }
        
        public Product(String name, int category_id){
            this.name= name;
            this.category_id= category_id;
        }

        

    @Override
    public String getSQLTypeName() throws SQLException {
        return this.type;
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        this.name = stream.readString();
        this.category_id = stream.readInt();
        this.type = typeName;
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(this.name);
        stream.writeInt(this.category_id);
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public int getCategory_id() {
        return category_id;
    }



    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }



    public String getType() {
        return type;
    }



    public void setType(String type) {
        this.type = type;
    }
    
}
