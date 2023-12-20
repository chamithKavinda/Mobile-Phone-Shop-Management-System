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
import lk.ijse.MobileVision.dto.SalaryDto;
import lk.ijse.MobileVision.dto.tm.SalaryTm;
import lk.ijse.MobileVision.model.SalaryModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class SalaryForm {

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colSalaryId;

    @FXML
    private TableColumn<?, ?> colSalaryMonth;

    @FXML
    private TableView<SalaryTm> tblSalary;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtEmployeeId;

    @FXML
    private TextField txtSalaryId;

    @FXML
    private TextField txtSalaryMonth1;

    public void initialize() {
        setCellValueFactory();
        loadAllSalary();
    }

    private void setCellValueFactory() {
        colSalaryId.setCellValueFactory(new PropertyValueFactory<>("s_id"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("e_id"));
        colSalaryMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void loadAllSalary() {
        var model = new SalaryModel();

        ObservableList<SalaryTm> obList = FXCollections.observableArrayList();

        try {
            List<SalaryDto> dtoList = model.getAllSalary();

            for (SalaryDto dto : dtoList) {
                obList.add(
                        new SalaryTm(
                                dto.getS_id(),
                                dto.getE_id(),
                                dto.getMonth(),
                                dto.getAmount()
                        )
                );
            }
            tblSalary.setItems(obList);
            tblSalary.refresh();
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
        String s_id = txtSalaryId.getText();

        var salaryModel = new SalaryModel();
        try{
            boolean isDeleted = salaryModel.deleteSalary(s_id);

            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"salary deleted! ").show();
                loadAllSalary();
            }
        }catch(SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isSalaryValidated = validateSalary();
        if (isSalaryValidated) {
            // new Alert(Alert.AlertType.INFORMATION, "Salary Saved Successfully!").show();
        }
    }


    private boolean validateSalary() {

        String s_id = txtSalaryId.getText();
        boolean isSalaryIDValidated = Pattern.matches("[S][0-9]{3,}", s_id);
        if (!isSalaryIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary ID!").show();
            return false;
        }

        String e_id = txtEmployeeId.getText();
        boolean isEmployeeIdValidated = Pattern.matches("[E][0-9]{3,}", e_id);
        if (!isEmployeeIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Id").show();
            return false;
        }

        String month = txtSalaryMonth1.getText();
        boolean isSalaryMonthValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", month);
        if (!isSalaryMonthValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary Month").show();
            return false;
        }

        String amount = txtAmount.getText();
        boolean isAmountValidated = Pattern.matches("^[0-9]+(\\.[0-9]{1,2})?$", amount);
        if (!isAmountValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Amount").show();
            return false;
        }
        var dto = new SalaryDto(s_id,e_id,month,amount);

        var model = new SalaryModel();
        try{
            boolean isSaved = model.saveSalary(dto);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Salary Saved!").show();
                loadAllSalary();
                clearFields();
                tblSalary.refresh();
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String s_id = txtSalaryId.getText();
        String e_id = txtEmployeeId.getText();
        String month = txtSalaryMonth1.getText();
        String amount = txtAmount.getText();

        boolean isSalaryValidated = validateUpdatedSalary(s_id,e_id,month,amount);

        if (isSalaryValidated) {

            var  dto = new SalaryDto(s_id,e_id,month,amount);

            var model = new SalaryModel();
            try{
                boolean isUpdated = model.updateSalary(dto);
                System.out.println(isUpdated);
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION,"Salary Updated!").show();
                    loadAllSalary();
                }
            }catch(SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedSalary(String s_id, String e_id, String month, String amount) {

        boolean isSalaryIDValidated = Pattern.matches("[S][0-9]{3,}", s_id);
        if (!isSalaryIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary ID!").show();
            return false;
        }

        boolean isEmployeeIdValidated = Pattern.matches("[E][0-9]{3,}", e_id);
        if (!isEmployeeIdValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Employee Id").show();
            return false;
        }


        boolean isSalaryMonthValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", month);
        if (!isSalaryMonthValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Salary Month").show();
            return false;
        }


        boolean isAmountValidated = Pattern.matches("^[0-9]+(\\.[0-9]{1,2})?$", amount);
        if (!isAmountValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Amount").show();
            return false;
        }
        return true;
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String s_id = txtSalaryId.getText();

        var model = new SalaryModel();
        try{
            SalaryDto dto = model.searchSalary(s_id);

            if(dto != null){
                fillFields(dto);
            }else{
                new Alert(Alert.AlertType.INFORMATION,"Salary not found!").show();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void fillFields(SalaryDto dto) {
        txtSalaryId.setText(dto.getS_id());
        txtEmployeeId.setText(dto.getE_id());
        txtSalaryMonth1.setText(dto.getMonth());
        txtAmount.setText(dto.getAmount());
    }

    void clearFields() {
        txtSalaryId.clear();
        txtEmployeeId.setText("");
        txtSalaryMonth1.setText("");
        txtAmount.setText("");
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/SalaryRecipt.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(load);

        JasperPrint jasperPrint;
        jasperPrint = JasperFillManager.fillReport(
                jasperReport, //compiled report
                null,
                DbConnection.getInstance().getConnection() //database connection
        );

        JasperViewer.viewReport(jasperPrint, false);
    }


}
