package lk.ijse.MobileVision.model;

import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.ItemDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    private static Object ItemDto;

    public boolean deleteItem(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM item WHERE i_code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,id);

        return pstm.executeUpdate()>0;
    }

    public boolean saveItem(ItemDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO item VALUES(?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getId());
        pstm.setString(2,dto.getDescription());
        pstm.setString(3,dto.getUnitPrice());
        pstm.setString(4,dto.getQtyOnHand());

        boolean isSaved = pstm.executeUpdate()>0;
        return isSaved;
    }

    public boolean updateItem(final ItemDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE item set i_description = ?,i_unit_price = ?,i_qty_on_hand = ? WHERE i_code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getDescription());
        pstm.setString(2,dto.getUnitPrice());
        pstm.setString(3, dto.getQtyOnHand());
        pstm.setString(4, dto.getId());

        return pstm.executeUpdate()>0;
    }

    public List<ItemDto> getAllItems() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from item";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<ItemDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String i_id = resultSet.getString(1);
            String i_description = resultSet.getString(2);
            String i_unit_price  = resultSet.getString(3);
            String qty_on_hand = resultSet.getString(4);

            var dto = new ItemDto(i_id,i_description,i_unit_price,qty_on_hand);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static ItemDto searchItem(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "select * from item where i_code =  ? ";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,code);

        lk.ijse.MobileVision.dto.ItemDto itemDto = new ItemDto();

        ResultSet resultSet = pstm.executeQuery();


        while(resultSet.next()){
            String i_id = resultSet.getString(1);
            String i_description = resultSet.getString(2);
            String i_unit_price = resultSet.getString(3);
            String i_qty_on_hand = resultSet.getString(4);

            itemDto = new ItemDto(i_id,i_description,i_unit_price,i_qty_on_hand);
        }
        return itemDto;
    }


}
