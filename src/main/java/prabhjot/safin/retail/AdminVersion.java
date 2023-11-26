package prabhjot.safin.retail;

import prabhjot.safin.retail.apps.AdminApp;
import prabhjot.safin.retail.apps.Application;

public class AdminVersion {
    public static void main(String[] args) {
        Application admin = new AdminApp();
        admin.run();
    }
}