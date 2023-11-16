package prabhjot.safin.retail.models;

/**
 * Represents an admin in the database
 * @author Prabhjot Aulakh, Safin Haque
 */
public class Admin {
    private int id;
    private String password;
    
    public Admin(int id, String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * Gets the id of the admin.
     * @return the id of the admin
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the admin
     * @param id the new id of the admin
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Gets the password of the admin.
     * @return the password of the admin
     */
    public String getPassword() {
        return password;
    }   

    /**
     * Sets the password of the admin
     * @param password the new password of the admin
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
