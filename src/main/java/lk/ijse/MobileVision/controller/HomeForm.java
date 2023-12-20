package lk.ijse.MobileVision.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lk.ijse.MobileVision.dto.ItemDto;
import lk.ijse.MobileVision.model.ItemModel;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeForm {

    @FXML
    private Label time;
    private volatile boolean stop  = false;

    @FXML
    private Label date;
    private volatile boolean s  = false;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtSearchBar;

    public void initialize() {
      TimeNow();
      Date();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String code = txtSearchBar.getText();

        try {
            System.out.println(code);
            ItemDto dto = ItemModel.searchItem(code);

            System.out.println(dto.getDescription());
            System.out.println(dto.getUnitPrice());
            System.out.println(dto.getQtyOnHand());

            txtDescription.setText(String.valueOf(dto.getDescription()));
            txtPrice.setText(String.valueOf(dto.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void TimeNow(){
        Thread thread = new Thread(()->{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while(!stop){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
                final String timeNow = sdf.format(new Date());
                Platform.runLater(()->{
                    time.setText(timeNow);
                });
            }
        });
        thread.start();
    }

    private void Date(){
        Thread thread = new Thread(()->{
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            while (!s){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
                final String dateNow = sdf1.format(new Date());
                Platform.runLater(()->{
                    date.setText(dateNow);
                });
            }
        });
        thread.start();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtSearchBar.clear();
        txtDescription.clear();
        txtPrice.clear();
        txtQtyOnHand.clear();
    }




}
