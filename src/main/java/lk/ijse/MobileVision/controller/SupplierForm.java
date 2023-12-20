package lk.ijse.MobileVision.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.SupplierDto;
import lk.ijse.MobileVision.dto.tm.SupplierTm;
import lk.ijse.MobileVision.model.SupplierModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;


public class SupplierForm {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private TableView<SupplierTm> tblSupplier;

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
        loadAllSupplier();
    }

    private void setCellValueFactory() {
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void loadAllSupplier() {
        var model = new SupplierModel();

        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();

        try {
            List<SupplierDto> dtoList = model.getAllSupplier();

            for (SupplierDto dto : dtoList) {
                obList.add(
                        new SupplierTm(
                                dto.getTel(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getId()
                        )
                );
            }
            tblSupplier.setItems(obList);
            tblSupplier.refresh();
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

        var supplierModel = new SupplierModel();
        try {
            boolean isDeleted = supplierModel.deleteSupplier(tel);

            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier deleted! ").show();
                loadAllSupplier();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isSupplierValidated = validateSupplier();
        if (isSupplierValidated) {
            // new Alert(Alert.AlertType.INFORMATION, "Supplier Saved Successfully!").show();

        }
    }

    private boolean validateSupplier() {
        String tel = txtTel.getText();
        boolean isCustomerTelValidated = Pattern.matches("[0-9]{10}", tel);
        if (!isCustomerTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier Tel").show();
            return false;
        }

        String name = txtName.getText();
        boolean isCustomerNameValidated = Pattern.matches("^[A-Za-z\\s]+$", name);
        if (!isCustomerNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier Name").show();
            return false;
        }
        String address = txtAddress.getText();
        boolean isCustomerAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isCustomerAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier address").show();
            return false;
        }

        String id = txtId.getText();
        boolean isCustomerIDValidated = Pattern.matches("^SUP\\d{3}$", id);
        if (!isCustomerIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID!").show();
            return false;
        }


        var dto = new SupplierDto(tel, name, address, id);

        var model = new SupplierModel();
        try {
            boolean isSaved = model.saveSupplier(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier Saved!").show();
                loadAllSupplier();
                clearFields();
                tblSupplier.refresh();

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return true;
    }



    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String tel = txtTel.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String id = txtId.getText();

        boolean isSupplierValidated = validateUpdatedSupplier(tel, name, address, id);

        if (isSupplierValidated) {

            var dto = new SupplierDto(tel, name, address, id);

            var model = new SupplierModel();
            try {
                boolean isUpdated = model.updateSupplier(dto);
                System.out.println(isUpdated);
                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier Updated!").show();
                    loadAllSupplier();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedSupplier(String tel, String name, String address, String id) {
        boolean isCustomerTelValidated = Pattern.matches("[0-9]{10}", tel);
        if (!isCustomerTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier Tel").show();
            return false;
        }


        boolean isCustomerNameValidated = Pattern.matches("^[A-Za-z\\s]+$", name);
        if (!isCustomerNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier Name!").show();
            return false;
        }


        boolean isCustomerAddressValidated = Pattern.matches("^\\d+/\\d+,\\s?[A-Za-z0-9\\s.,'-]+|^[A-Za-z]+$", address);
        if (!isCustomerAddressValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier address").show();
            return false;
        }


        boolean isCustomerIDValidated = Pattern.matches("^SUP\\d{3}$", id);
        if (!isCustomerIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Supplier ID!").show();
            return false;
        }


        return true;
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String tel = txtTel.getText();

        var model = new SupplierModel();
        try {
            SupplierDto dto = model.searchSupplier(tel);

            if (dto != null) {

                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Supplier not found!").show();
                loadAllSupplier();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void fillFields(SupplierDto dto) {
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

    @FXML
    void btnResportOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/SupplierReport.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(load);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                        jasperReport, //compiled report
                        null,
                        DbConnection.getInstance().getConnection() //database connection
                );

        JasperViewer.viewReport(jasperPrint, false);
    }

}
