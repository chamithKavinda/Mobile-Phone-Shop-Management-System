package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.ItemDto;
import lk.ijse.MobileVision.dto.PlaceOrderDto;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderModel {
    private final OrderModel orderModel = new OrderModel();
    private final OrderDetailModel orderDetailModel = new OrderDetailModel();
    
    public boolean placeOrder(PlaceOrderDto placeOrderDto) throws SQLException {
        boolean result = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOrderSaved = OrderModel.saveOrder(placeOrderDto.getO_id(), placeOrderDto.getC_tel(), placeOrderDto.getDate());
            if (isOrderSaved) {
                boolean isOrderDetailSaved = orderDetailModel.saveOrderDetail(placeOrderDto.getO_id(), placeOrderDto.getTmList());
                if (isOrderDetailSaved) {
                    connection.commit();
                    result = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);

            }
        }
        return result;
    }
}
