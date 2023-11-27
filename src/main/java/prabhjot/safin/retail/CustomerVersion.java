package prabhjot.safin.retail;

import java.sql.SQLException;

import prabhjot.safin.retail.apps.Application;
import prabhjot.safin.retail.apps.CustomerApp;

public class CustomerVersion  {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Application customer = new CustomerApp();
        customer.run(); 
    }
}
