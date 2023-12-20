package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.EmployeeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {

    public boolean deleteEmployee(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM employee WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,id);

        return pstm.executeUpdate()>0;
    }

    public List<EmployeeDto> getAllEmployee() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from employee";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<EmployeeDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String e_id = resultSet.getString(1);
            String e_name = resultSet.getString(2);
            String e_address = resultSet.getString(3);
            String e_contact = resultSet.getString(4);


            var dto = new EmployeeDto(e_id,e_name,e_address,e_contact);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean saveEmployee(final EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO employee VALUES(?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getId());
        pstm.setString(2,dto.getName());
        pstm.setString(3,dto.getAddress());
        pstm.setString(4,dto.getTel());

        boolean isSaved = pstm.executeUpdate()>0;
        return isSaved;
    }

    public boolean updateEmployee(final EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE employee set e_name = ?,e_address = ?,e_contact = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getName());
        pstm.setString(2,dto.getAddress());
        pstm.setString(3, dto.getTel());
        pstm.setString(4, dto.getId());

        return pstm.executeUpdate()>0;
    }

    public CustomerDto searchCustomer(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        CustomerDto dto = null;

        if(resultSet.next()) {
            String e_id = resultSet.getString(1);
            String e_name = resultSet.getString(2);
            String e_address = resultSet.getString(3);
            String e_tel = resultSet.getString(4);

            dto = new CustomerDto(e_id, e_name, e_address, e_tel);
        }

        return dto;
    }


}
