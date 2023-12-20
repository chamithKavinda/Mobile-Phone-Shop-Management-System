package lk.ijse.MobileVision.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.EmployeeDto;
import lk.ijse.MobileVision.dto.tm.EmployeeTm;
import lk.ijse.MobileVision.model.CustomerModel;
import lk.ijse.MobileVision.model.EmployeeModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeForm {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private AnchorPane root1s;

    @FXML
    private TableView<EmployeeTm> TblEmployee;

    @FXML
    private Label txt;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    public void initialize(){
        setCellValueFactory();
        loadAllEmployee();
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
    }

    private void loadAllEmployee(){
        var model = new EmployeeModel();

        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try{
            List<EmployeeDto> dtoList = model.getAllEmployee();

            for (EmployeeDto dto : dtoList){
                obList.add(
                        new EmployeeTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getTel()
                        )
                );
            }

            tblEmployee.setItems(obList);
            tblEmployee.refresh();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
            clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        var EmployeeModel = new EmployeeModel();
        try{
            boolean isDeleted = EmployeeModel.deleteEmployee(id);

            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"employee deleted! ").show();
                loadAllEmployee();
            }
        }catch(SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        boolean isEmployeeValidated = validateEmployee();
        if (isEmployeeValidated) {
           // new Alert(Alert.AlertType.INFORMATION, "Employee Saved Successfully!").show();

        }
    }

    private boolean validateEmployee(){
        String id = txtId.getText();
        boolean isEmployeeIDValidated = Pattern.matches("[E][0-9]{3,}", id);
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID!").show();
            return false;
        }
        String name = txtName.getText();
        boolean isEmployeeNameValidated = Pattern.matches("^[A-Za-z\\s]+$", name);
        if (!isEmployeeNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee name ").show();
            return false;
        }
        String address = txtAddress.getText();
        boolean isEmployeeAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isEmployeeAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee address").show();
            return false;
        }

        String tel = txtTel.getText();
        boolean isEmployeeTelValidated = Pattern.matches("[0-9]{10}", tel);
        if (!isEmployeeTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Tel").show();
            return false;
        }

        var dto = new EmployeeDto(id,name,address,tel);

        var model = new EmployeeModel();
        try{
            boolean isSaved = model.saveEmployee(dto);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Employee Saved!").show();
                loadAllEmployee();
                clearFields();
                tblEmployee.refresh();
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return true;
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String id = txtId.getText();

        var model = new EmployeeModel();
        try {
            CustomerDto dto = model.searchCustomer(id);

            if(dto != null) {
                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "employee not found!").show();
                loadAllEmployee();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String tel = txtTel.getText();

        boolean isEmployeeValidated = validateUpdatedEmployee(id, name, address, tel);

        if (isEmployeeValidated) {

            var dto = new EmployeeDto(id, name, address, tel);

            var model = new EmployeeModel();
            try {
                boolean isUpdated = model.updateEmployee(dto);
                System.out.println(isUpdated);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee Updated!").show();
                    loadAllEmployee();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedEmployee(String id, String name, String address, String tel) {

        boolean isEmployeeIDValidated = Pattern.matches("[E][0-9]{3,}", id);
        if (!isEmployeeIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee ID!").show();
            return false;
        }


        boolean isEmployeeNameValidated = Pattern.matches("^[A-Za-z\\s]+$", name);
        if (!isEmployeeNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Name!").show();
            return false;
        }


        boolean isEmployeeAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isEmployeeAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee address").show();
            return false;
        }


        boolean isEmployeeTelValidated = Pattern.matches("[0-9]{10}", tel);
        if (!isEmployeeTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Tel").show();
            return false;
        }

        return true;
    }

    void clearFields() {
        txtId.clear();
        txtName.setText("");
        txtAddress.setText("");
        txtTel.setText("");
    }

    private void fillFields(CustomerDto dto) {
        txtTel.setText(dto.getId());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtId.setText(dto.getTel());
    }

}
