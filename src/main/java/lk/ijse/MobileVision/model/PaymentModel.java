package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.PaymentDto;
import lk.ijse.MobileVision.dto.RepairDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentModel {
    public boolean deletePayment(String p_id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM payments WHERE p_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,p_id);

        return pstm.executeUpdate()>0;
    }

    public boolean savePayment(PaymentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO payments VALUES(?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getP_id());
        pstm.setString(2,dto.getC_tel());
        pstm.setString(3, dto.getO_id());
        pstm.setString(4, dto.getDate());
        pstm.setString(5, dto.getDescription());
        pstm.setString(6, dto.getAmount());

        boolean isSaved = pstm.executeUpdate()>0;
        return isSaved;
    }

    public List<PaymentDto> getAllPayment() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from payments";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<PaymentDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String p_id = resultSet.getString(1);
            String c_tel = resultSet.getString(2);
            String o_id = resultSet.getString(3);
            String date = resultSet.getString(4);
            String description = resultSet.getString(5);
            String amount = resultSet.getString(6);

            var dto = new PaymentDto(p_id,c_tel,o_id,date,description,amount);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean updatePayment(PaymentDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE payments set c_contact = ?, o_id = ? , date=? ,p_description = ?, Amount = ?  WHERE p_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getC_tel());
        pstm.setString(2,dto.getO_id());
        pstm.setString(3, dto.getDate());
        pstm.setString(4, dto.getDescription());
        pstm.setString(5, dto.getAmount());
        pstm.setString(6, dto.getP_id());

        return pstm.executeUpdate()>0;
    }
}
