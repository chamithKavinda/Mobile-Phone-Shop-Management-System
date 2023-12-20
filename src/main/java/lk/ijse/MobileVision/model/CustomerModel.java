package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.RepairDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    public List<CustomerDto> getAllCustomers() throws SQLException{
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from customer";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<CustomerDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String c_contact = resultSet.getString(1);
            String c_name = resultSet.getString(2);
            String c_address = resultSet.getString(3);
            String c_id = resultSet.getString(4);

            var dto = new CustomerDto(c_contact,c_name,c_address,c_id);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean saveCustomer(final CustomerDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO customer VALUES(?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getTel());
        pstm.setString(2,dto.getName());
        pstm.setString(3,dto.getAddress());
        pstm.setString(4,dto.getId());

        boolean isSaved = pstm.executeUpdate()>0;
        return isSaved;
    }

    public boolean updateCustomer(final CustomerDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE customer set c_name = ?,c_address = ?, c_id = ? WHERE c_contact = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getName());
        pstm.setString(2,dto.getAddress());
        pstm.setString(3, dto.getId());
        pstm.setString(4, dto.getTel());

        return pstm.executeUpdate()>0;
    }


    public boolean deleteCustomer(String tel) throws SQLException{
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM customer WHERE c_contact = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,tel);

        return pstm.executeUpdate()>0;
    }


    public CustomerDto searchCustomer(String tel) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM customer WHERE c_contact = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, tel);

        ResultSet resultSet = pstm.executeQuery();

        CustomerDto dto = null;

        if(resultSet.next()) {
            String c_tel = resultSet.getString(1);
            String c_name = resultSet.getString(2);
            String c_address = resultSet.getString(3);
            String c_id= resultSet.getString(4);

            dto = new CustomerDto(c_tel, c_name, c_address, c_id);
        }

        return dto;
    }


}
