package prabhjot.safin.retail;

import java.sql.SQLException;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Warehouse;
import prabhjot.safin.retail.services.WarehouseService;

public class AdminApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();
            WarehouseService warehouseService = new WarehouseService(connectionProvider.getConnection());
            Warehouse warehouse = new Warehouse("Warehouse C", "123 main street");
            warehouseService.create(warehouse);
            System.out.println("Created warehouse !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
