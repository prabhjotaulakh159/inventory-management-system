package prabhjot.safin.retail;

import java.sql.SQLException;

import prabhjot.safin.retail.apps.AdminApp;
import prabhjot.safin.retail.apps.Application;

public class AdminVersion {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Application admin = new AdminApp();
        admin.run();
    }
}