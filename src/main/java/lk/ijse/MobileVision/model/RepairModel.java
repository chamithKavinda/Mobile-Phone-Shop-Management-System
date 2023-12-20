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

public class RepairModel {
    public boolean deleteRepair(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM repair_details WHERE r_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,id);

        return pstm.executeUpdate()>0;
    }

    public boolean updateRepair(RepairDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE repair_details set e_id = ?,r_description = ? ,r_price = ?,r_date = ?, c_contact = ?  WHERE r_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getE_id());
        pstm.setString(2,dto.getDescription());
        pstm.setString(3, dto.getPrice());
        pstm.setString(4, dto.getDate());
        pstm.setString(5, dto.getC_tel());
        pstm.setString(6, dto.getR_id());

        return pstm.executeUpdate()>0;
    }

    public boolean saveRepair(RepairDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();


        String sql = "INSERT INTO repair_details VALUES(?,?,?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);


        pstm.setString(1,dto.getR_id());
        pstm.setString(2,dto.getE_id());
        pstm.setString(3, dto.getDescription());
        pstm.setString(4, dto.getPrice());
        pstm.setString(5, dto.getDate());
        pstm.setString(6, dto.getC_tel());


        boolean isSaved = pstm.executeUpdate()>0;

        System.out.println(isSaved);
        return isSaved;

    }

    public List<RepairDto> getAllRepair() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from repair_details";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<RepairDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String r_id = resultSet.getString(1);
            String e_id = resultSet.getString(2);
            String c_tel = resultSet.getString(3);
            String description = resultSet.getString(4);
            String price = resultSet.getString(5);
            String date = resultSet.getString(6);

            var dto = new RepairDto(r_id,e_id,c_tel,description,price,date);
            dtoList.add(dto);
        }
        return dtoList;
    }
/*
    public RepairDto searchRepair(String r_id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM repair_details WHERE r_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, r_id);

        ResultSet resultSet = pstm.executeQuery();

        RepairDto dto = null;

        if(resultSet.next()) {
            String r_id = resultSet.getString(1);
            String e_id = resultSet.getString(2);
            String description = resultSet.getString(3);
            String price = resultSet.getString(4);
            String date = resultSet.getString(5);
            String c_tel = resultSet.getString(6);


            dto = new RepairDto(r_id,e_id,description,price,date,c_tel);
        }

        return dto;
    }*/
}
