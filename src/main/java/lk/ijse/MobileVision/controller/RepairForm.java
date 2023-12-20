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
import javafx.scene.layout.AnchorPane;
import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.RepairDto;
import lk.ijse.MobileVision.dto.tm.RepairTm;
import lk.ijse.MobileVision.model.CustomerModel;
import lk.ijse.MobileVision.model.RepairModel;
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

public class RepairForm {

    @FXML
    private TableColumn<?, ?> colCustomerContact;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colRepairDate;

    @FXML
    private TableColumn<?, ?> colRepairDescription;

    @FXML
    private TableColumn<?, ?> colRepairId;

    @FXML
    private TableColumn<?, ?> colRepairPrice;

    @FXML
    private AnchorPane root5;

    @FXML
    private TableView<RepairTm> tblRepair;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtId1;

    @FXML
    private TextField txtCustomerContact;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtRepairDescription;

    public void initialize(){
        setCellValueFactory();
        loadAllRepair();
    }

    private void setCellValueFactory(){
        colRepairId.setCellValueFactory(new PropertyValueFactory<>("r_id"));
        colRepairDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colRepairPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerContact.setCellValueFactory(new PropertyValueFactory<>("c_tel"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("e_id"));
        colRepairDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadAllRepair(){
        var model = new RepairModel();

        ObservableList<RepairTm> obList = FXCollections.observableArrayList();

        try{
            List<RepairDto> dtoList = model.getAllRepair();

            for (RepairDto dto : dtoList){
                obList.add(
                        new RepairTm(
                                dto.getR_id(),
                                dto.getE_id(),
                                dto.getC_tel(),
                                dto.getDescription(),
                                dto.getPrice(),
                                dto.getDate()
                        )
                );
            }
            tblRepair.setItems(obList);
            tblRepair.refresh();
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

        var repairModel = new RepairModel();
        try{
            boolean isDeleted = repairModel.deleteRepair(id);

            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"repair deleted! ").show();
                loadAllRepair();
            }
        }catch(SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isRepairValidated = validateRepair();
        if (isRepairValidated) {
            // new Alert(Alert.AlertType.INFORMATION, "Repair Saved Successfully!").show();
        }
    }

    private boolean validateRepair() {

        String r_id = txtId.getText();
        boolean isRepairIdValidated = Pattern.matches("[R][0-9]{3,}", r_id);
        if (!isRepairIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Repair Id").show();
            return false;
        }

        String e_id = txtId1.getText();
        boolean isEmployeeIdValidated = Pattern.matches("[E][0-9]{3,}", e_id);
        if (!isEmployeeIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Id").show();
            return false;
        }

        String description = txtRepairDescription.getText();
        boolean isDescriptionValidated = Pattern.matches("^[a-zA-Z0-9,.!\\s]+$", description);
        if (!isDescriptionValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Repair Description").show();
            return false;
        }

        String price = txtPrice.getText();
        boolean isPriceValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", price);
        if (!isPriceValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Price").show();
            return false;
        }

        String date =txtDate.getText();
        boolean isDateValidated = Pattern.matches("^\\d{4}.\\d{2}.\\d{2}$", date);
        if (!isDateValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Date").show();
            return false;
        }

        String c_tel = txtCustomerContact.getText();
        boolean isCustomerTelValidated = Pattern.matches("[0-9]{10}", c_tel);
        if (!isCustomerTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer Tel").show();
            return false;
        }

        var dto = new RepairDto(r_id,e_id,description,price,date,c_tel);

        var model = new RepairModel();
        try{
            boolean isSaved = model.saveRepair(dto);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Repair Saved!").show();
                clearFields();
                tblRepair.refresh();
                loadAllRepair();
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String r_id = txtId.getText();
        String e_id = txtId1.getText();
        String description = txtRepairDescription.getText();
        String price = txtPrice.getText();
        String date =txtDate.getText();
        String c_tel = txtCustomerContact.getText();

        boolean isRepairValidated = validateUpdatedRepair(r_id, e_id, description, price,date,c_tel);

        if(isRepairValidated){

            var dto = new RepairDto(r_id,e_id,description,price,date,c_tel);

            var model = new RepairModel();
            try{
                boolean isUpdated = model.updateRepair(dto);
                System.out.println(isUpdated);
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION,"Repair Updated!").show();
                    loadAllRepair();
                }
            }catch(SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedRepair(String r_id, String e_id, String description, String price, String date, String c_tel) {

        boolean isRepairIdValidated = Pattern.matches("[R][0-9]{3,}", r_id);
        if (!isRepairIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Repair Id").show();
            return false;
        }


        boolean isEmployeeIdValidated = Pattern.matches("[E][0-9]{3,}", e_id);
        if (!isEmployeeIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Id").show();
            return false;
        }


        boolean isDescriptionValidated = Pattern.matches("^[a-zA-Z0-9,.!\\s]+$", description);
        if (!isDescriptionValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Repair Description").show();
            return false;
        }


        boolean isPriceValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", price);
        if (!isPriceValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Price").show();
            return false;
        }


        boolean isDateValidated = Pattern.matches("^\\d{4}.\\d{2}.\\d{2}$", date);
        if (!isDateValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Date").show();
            return false;
        }


        boolean isCustomerTelValidated = Pattern.matches("[0-9]{10}", c_tel);
        if (!isCustomerTelValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid customer Tel").show();
            return false;
        }
        return true;
    }


    @FXML
    void txtSearchOnAction(ActionEvent event) {
    /*    String r_id = txtCustomerContact.getText();

        var model = new RepairModel();
        try {
           RepairDto dto = model.searchRepair(r_id);

            if (dto != null) {

                fillFields(dto);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Repair not found!").show();
                loadAllRepair();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }*/
    }

   /* private void fillFields(RepairDto dto) {
        txtId.setText(dto.getR_id());
        txtId1.setText(dto.getE_id());
        txtCustomerContact.setText(dto.getC_tel());
        txtPrice.setText(dto.getPrice());
        txtRepairDescription.setText(dto.getDescription());
        txtDate.setText(dto.getDate());
    }

     */

    void clearFields() {
        txtId.clear();
        txtId1.clear();
        txtCustomerContact.setText("");
        txtPrice.setText("");
        txtRepairDescription.setText("");
        txtDate.setText("");

    }

    @FXML
    void btnPrintOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/RepairReport.jrxml");
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
