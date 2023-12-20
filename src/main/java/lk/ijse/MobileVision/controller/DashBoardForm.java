package lk.ijse.MobileVision.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardForm {

    @FXML
    private JFXButton btnLogout;

    @FXML
    private AnchorPane root;

    @FXML
    void  btnEmployeeOnAction(ActionEvent event) throws IOException,NullPointerException {
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.<AnchorPane>load(getClass().getResource("/view/EmployeeForm.fxml"));
        this.root.getChildren().clear();
        this.root.getChildren().add(anchorPane);
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.<AnchorPane>load(getClass().getResource("/view/CustomerForm.fxml"));
//        Scene scene = new Scene(anchorPane);
//        Stage stage = (Stage) root.getScene().getWindow();
//        stage.setScene(scene);
//        stage.setTitle("Customer Manage");
//        stage.centerOnScreen();
        this.root.getChildren().clear();
        this.root.getChildren().add(anchorPane);
    }

    @FXML
    void btnItemOnAction(ActionEvent event) throws IOException {
        this.root.getChildren().clear();
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.load(getClass().getResource("/view/ItemForm.fxml"));
        this.root.getChildren().add(anchorPane);
    }

    @FXML
    void btnRepairOnAction(ActionEvent event) throws IOException {
        this.root.getChildren().clear();
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.load(getClass().getResource("/view/RepairForm.fxml"));
        this.root.getChildren().add(anchorPane);
    }


    @FXML
    void btnHomeOnAction(ActionEvent event) throws IOException {
        this.root.getChildren().clear();
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.load(getClass().getResource("/view/HomeForm.fxml"));
        this.root.getChildren().add(anchorPane);
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) throws IOException {
        this.root.getChildren().clear();
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.load(getClass().getResource("/view/PaymentForm.fxml"));
        this.root.getChildren().add(anchorPane);
    }

   @FXML
    void btnOrderOnAction(ActionEvent event) throws IOException {
        this.root.getChildren().clear();
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.load(getClass().getResource("/view/PlaceOrderForm.fxml"));
        this.root.getChildren().add(anchorPane);
    }

    @FXML
    void btnSalaryOnAction(ActionEvent event) throws IOException {
        this.root.getChildren().clear();
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.load(getClass().getResource("/view/SalaryForm.fxml"));
        this.root.getChildren().add(anchorPane);
    }


    @FXML
    void btnSupplierOnAction(ActionEvent event) throws IOException {
        this.root.getChildren().clear();
        AnchorPane anchorPane;
        anchorPane = FXMLLoader.load(getClass().getResource("/view/SupplierForm.fxml"));
        this.root.getChildren().add(anchorPane);
    }


    @FXML
    void btnLogOutOnAction() throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/LoginForm.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Login");
        stage.show();

        //Close the Current Window
         Stage dashboard= (Stage) btnLogout.getScene().getWindow();
         dashboard.close();
    }

}
