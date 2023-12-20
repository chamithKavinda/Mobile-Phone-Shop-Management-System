package lk.ijse.MobileVision.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import lk.ijse.MobileVision.dto.tm.CustomerTm;
import lk.ijse.MobileVision.model.CustomerModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerForm {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    public void initialize() {
        setCellValueFactory();
        loadAllCustomer();
    }

    private void setCellValueFactory() {
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void loadAllCustomer() {
        var model = new CustomerModel();

        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<CustomerDto> dtoList = model.getAllCustomers();

            for (CustomerDto dto : dtoList) {
                obList.add(
                        new CustomerTm(
                                dto.getTel(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getId()
                        )
                );
            }
            tblCustomer.setItems(obList);
            tblCustomer.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String tel = txtTel.getText();

        var customerModel = new CustomerModel();
        try {
            boolean isDeleted = customerModel.deleteCustomer(tel);

            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted! ").show();
                loadAllCustomer();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isCustomerValidated = validateCustomer();
        if (isCustomerValidated) {
           // new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully!").show();

        }
    }

    private boolean validateCustomer() {

        String tel = txtTel.getText();
        boolean isCustomerTelValidated = Pattern.matches("[0-9]{10}", tel);
        if (!isCustomerTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer Tel").show();
            return false;
        }

        String name = txtName.getText();
        boolean isCustomerNameValidated = Pattern.matches("^[A-Za-z\\s]+$", name);
        if (!isCustomerNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer Name").show();
            return false;
        }
        String address = txtAddress.getText();
        boolean isCustomerAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isCustomerAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer address").show();
            return false;
        }

        String id = txtId.getText();
        boolean isCustomerIDValidated = Pattern.matches("[C][0-9]{3,}", id);
        if (!isCustomerIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID!").show();
            return false;
        }


        var dto = new CustomerDto(tel, name, address, id);

        var model = new CustomerModel();
        try {
            boolean isSaved = model.saveCustomer(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved!").show();
                loadAllCustomer();
                clearFields();
                tblCustomer.refresh();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return true;
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String tel = txtTel.getText();

        var model = new CustomerModel();
        try {
            CustomerDto dto = model.searchCustomer(tel);

            if (dto != null) {

                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
                loadAllCustomer();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String tel = txtTel.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String id = txtId.getText();

        boolean isCustomerValidated = validateUpdatedCustomer(tel, name, address, id);

        if (isCustomerValidated) {

            var dto = new CustomerDto(tel, name, address, id);

            var model = new CustomerModel();
            try {
                boolean isUpdated = model.updateCustomer(dto);
                System.out.println(isUpdated);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Updated!").show();
                    loadAllCustomer();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedCustomer(String tel, String name, String address, String id) {

        boolean isCustomerTelValidated = Pattern.matches("[0-9]{10}", tel);
        if (!isCustomerTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer Tel").show();
            return false;
        }


        boolean isCustomerNameValidated = Pattern.matches("^[A-Za-z\\s]+$", name);
        if (!isCustomerNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer Name!").show();
            return false;
        }


        boolean isCustomerAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isCustomerAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer address").show();
            return false;
        }


        boolean isCustomerIDValidated = Pattern.matches("[C][0-9]{3,}", id);
        if (!isCustomerIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Customer ID!").show();
            return false;
        }


        return true;
    }

    private void fillFields(CustomerDto dto) {
        txtTel.setText(dto.getTel());
        txtName.setText(dto.getName());
        txtAddress.setText(dto.getAddress());
        txtId.setText(dto.getId());
    }

    void clearFields() {
        txtTel.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtId.clear();
    }

}

