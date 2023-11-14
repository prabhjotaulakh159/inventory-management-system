package prabhjot.safin.retail;

import java.sql.SQLException;
import java.util.List;

import prabhjot.safin.retail.connection.ConnectionProvider;
import prabhjot.safin.retail.models.Warehouse;
import prabhjot.safin.retail.services.WarehouseService;

public class AdminApp {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        
        try {
            ConnectionProvider connectionProvider = new ConnectionProvider();
            WarehouseService warehouseService = new WarehouseService(connectionProvider.getConnection());
            List<Warehouse> warehouses = warehouseService.getWarehouses();
            for (Warehouse w : warehouses) {
                System.out.println(w.getName() + ", " + w.getAddress());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
