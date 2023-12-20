package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    public static boolean saveUser(UserDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO USER VALUES(?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getUserName());
        pstm.setString(2,dto.getEmail());
        pstm.setString(3,dto.getPassword());

        boolean isSaved = pstm.executeUpdate() > 0;
        return isSaved;
    }

    public static String getEmail(String Email) throws SQLException , ClassNotFoundException{
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Email FROM user WHERE Email=?");

        pstm.setObject(1,Email);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public static boolean isExistUser(String UserName,String Password) throws SQLException,ClassNotFoundException{
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT Password , UserName FROM user WHERE UserName=? and Password=?");

        pstm.setObject(1,UserName);
        pstm.setObject(2,Password);
        ResultSet resultSet = pstm.executeQuery();
        String dbUserName = null;
        String dbPassword = null;
        if(resultSet.next()){
            dbPassword = resultSet.getString(1);
            dbUserName = resultSet.getString(2);
           // DbConnection.Email = resultSet.getString(3);
        }
        return UserName.equals(dbUserName) && Password.equals(dbPassword);
    }
}



