package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.tm.CartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailModel {
    public boolean saveOrderDetail(String o_id, List<CartTm> tmList) throws SQLException {
        for (CartTm cartTm : tmList) {
            if(!saveOrderDetail(o_id, cartTm)) {
                return false;
            }
        }
        return true;
    }

    private boolean saveOrderDetail(String o_id, CartTm cartTm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO order_details VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, o_id);
        pstm.setString(2, cartTm.getCode());
        pstm.setInt(3, cartTm.getQty());
        pstm.setDouble(4, cartTm.getUnitPrice());

        return pstm.executeUpdate() > 0;
    }

}
