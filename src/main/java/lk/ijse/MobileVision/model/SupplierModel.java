package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.SupplierDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    public boolean deleteSupplier(String tel) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM supplier WHERE sup_contact = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,tel);

        return pstm.executeUpdate()>0;
    }

    public boolean saveSupplier(SupplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO supplier VALUES(?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getTel());
        pstm.setString(2,dto.getName());
        pstm.setString(3,dto.getAddress());
        pstm.setString(4,dto.getId());

        boolean isSaved = pstm.executeUpdate()>0;
        return isSaved;
    }

    public SupplierDto searchSupplier(String tel) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier WHERE sup_contact = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, tel);

        ResultSet resultSet = pstm.executeQuery();

        SupplierDto dto = null;

        if(resultSet.next()) {
            String sup_tel = resultSet.getString(1);
            String sup_name = resultSet.getString(2);
            String sup_address = resultSet.getString(3);
            String sup_id = resultSet.getString(4);

            dto = new SupplierDto(sup_tel,sup_name,sup_address,sup_id);
        }

        return dto;
    }

    public boolean updateSupplier(SupplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE supplier set sup_name = ?,sup_address = ?, sup_id = ? WHERE sup_contact = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getName());
        pstm.setString(2,dto.getAddress());
        pstm.setString(3, dto.getId());
        pstm.setString(4, dto.getTel());

        return pstm.executeUpdate()>0;
    }

    public List<SupplierDto> getAllSupplier() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from supplier";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<SupplierDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String sup_contact = resultSet.getString(1);
            String sup_name = resultSet.getString(2);
            String sup_address= resultSet.getString(3);
            String sup_id = resultSet.getString(4);

            var dto = new SupplierDto(sup_contact,sup_name,sup_address,sup_id);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
