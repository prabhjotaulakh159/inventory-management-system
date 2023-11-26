package prabhjot.safin.retail.models;

import java.sql.Date;

/**
 * Represents an audit row in audit tables
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Audit {
    private String change;
    private Date date;
    private int objId;
    
    /**
     * Constructor
     * @param change Change that occured in the table
     * @param date Date of the change
     * @param objId Id of entity changed
     */
    public Audit(String change, Date date, int objId) {
      this.change = change;
      this.date = date;
      this.objId = objId;
    }

    /**
     * Accessor for change
     * @return Change on row
     */
    public String getChange() {
      return change;
    }

    /**
     * Mutator for change
     * @param change Change on row
     */
    public void setChange(String change) {
      this.change = change;
    }

    /**
     * Accessor for date
     * @return Date of change
     */
    public Date getDate() {
      return date;
    }

    /**
     * Mutator for date
     * @param Date of change
     */
    public void setDate(Date date) {
      this.date = date;
    }

    /**
     * Accessor for obj id
     * @return Id of entity changed
     */
    public int getObjId() {
      return objId;
    }

    /**
     * mutator for obj id
     * @param Id of entity changed
     */
    public void setObjId(int objId) {
      this.objId = objId;
    }    

    @Override
    public String toString() {
        return "Action: " + this.change + ", Date: " + this.date + ", Id: " + this.objId; 
    }
}
