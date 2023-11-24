package prabhjot.safin.retail;

import java.sql.SQLException;

import prabhjot.safin.retail.apps.Application;
import prabhjot.safin.retail.apps.CustomerApp;

public class CustomerVersion {
    public static void main(String[] args) {
        try {
            Application customer = new CustomerApp();
            customer.run();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
