package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.ItemDto;
import lk.ijse.MobileVision.dto.SalaryDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryModel {

    public boolean deleteSalary(String s_id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM salary WHERE sal_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,s_id);

        return pstm.executeUpdate()>0;
    }

    public boolean saveSalary(SalaryDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO salary VALUES(?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getS_id());
        pstm.setString(2,dto.getE_id());
        pstm.setString(3,dto.getMonth());
        pstm.setString(4,dto.getAmount());

        boolean isSaved = pstm.executeUpdate()>0;
        return isSaved;
    }

    public boolean updateSalary(SalaryDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE salary set e_id = ?,sal_month = ?,amount = ? WHERE sal_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getE_id());
        pstm.setString(2,dto.getMonth());
        pstm.setString(3, dto.getAmount());
        pstm.setString(4, dto.getS_id());

        return pstm.executeUpdate()>0;
    }


    public SalaryDto searchSalary(String s_id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM salary WHERE sal_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, s_id);

        ResultSet resultSet = pstm.executeQuery();

        SalaryDto dto = null;

        if(resultSet.next()) {
            String sal_id = resultSet.getString(1);
            String e_id = resultSet.getString(2);
            String sal_month = resultSet.getString(3);
            String amount = resultSet.getString(4);

            dto = new SalaryDto(sal_id,e_id,sal_month,amount);
        }

        return dto;
    }

    public List<SalaryDto> getAllSalary() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from salary";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<SalaryDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String s_id = resultSet.getString(1);
            String e_id = resultSet.getString(2);
            String month = resultSet.getString(3);
            String amount = resultSet.getString(4);

            var dto = new SalaryDto(s_id,e_id,month,amount);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
