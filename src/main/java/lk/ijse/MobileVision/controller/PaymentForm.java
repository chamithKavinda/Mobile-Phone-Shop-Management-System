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
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.PaymentDto;
import lk.ijse.MobileVision.dto.tm.PaymentTm;
import lk.ijse.MobileVision.model.CustomerModel;
import lk.ijse.MobileVision.model.PaymentModel;
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

public class PaymentForm {
    private static String payment1_id;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colCustomerContact;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colPaymentId;

    @FXML
    private TableView<PaymentTm> tblPayment;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtCustomerContact;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtPaymentId;

    public void initialize(){
        setCellValueFactory();
        loadAllPayment();
    }

    private void setCellValueFactory(){
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("p_id"));
        colCustomerContact.setCellValueFactory(new PropertyValueFactory<>("c_tel"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("o_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void loadAllPayment() {
        var model = new PaymentModel();

        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        try{
            List<PaymentDto> dtoList = model.getAllPayment();

            for (PaymentDto dto : dtoList){
                obList.add(
                        new PaymentTm(
                                dto.getP_id(),
                                dto.getC_tel(),
                                dto.getO_id(),
                                dto.getDate(),
                                dto.getDescription(),
                                dto.getAmount()
                        )
                );
            }
            tblPayment.setItems(obList);
            tblPayment.refresh();
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
        String p_id = txtPaymentId.getText();

        var paymentModel = new PaymentModel();
        try{
            boolean isDeleted = paymentModel.deletePayment(p_id);

            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"payment deleted! ").show();
                loadAllPayment();
            }
        }catch(SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String p_id = txtPaymentId.getText();
        String c_tel = txtCustomerContact.getText();
        String o_id = txtOrderId.getText();
        String date = txtDate.getText();
        String description = txtDescription.getText();
        String amount =txtAmount.getText();


        var dto = new PaymentDto(p_id,c_tel,o_id,date,description,amount);

        var model = new PaymentModel();
        try{
            boolean isSaved = model.savePayment(dto);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Payment Saved!").show();
                clearFields();
                tblPayment.refresh();
                loadAllPayment();
                payment1_id=p_id;
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String p_id = txtPaymentId.getText();
        String c_tel = txtCustomerContact.getText();
        String o_id = txtOrderId.getText();
        String date = txtDate.getText();
        String description = txtDescription.getText();
        String amount =txtAmount.getText();


        var dto = new PaymentDto(p_id,c_tel,o_id,date,description,amount);

        var model = new PaymentModel();
        try{
            boolean isUpdated = model.updatePayment(dto);
            System.out.println(isUpdated);
            if(isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"Payment Updated!").show();
                loadAllPayment();
            }
        }catch(SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }


    void clearFields() {
        txtPaymentId.clear();
        txtCustomerContact.setText("");
        txtOrderId.setText("");
        txtAmount.setText("");
        txtDescription.setText("");
        txtDate.setText("");
    }


    @FXML
    void btnReciptOnAction(ActionEvent event) throws JRException, SQLException {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/PaymentReport.jrxml");

            JRDesignQuery jrDesignQuery = new JRDesignQuery();

            jrDesignQuery.setText("select * from payments where p_id=  \"" + payment1_id + "\"");

            jasperDesign.setQuery(jrDesignQuery);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(
                            jasperReport, //compiled report
                            null,
                            DbConnection.getInstance().getConnection() //database connection
                    );

            JFrame frame = new JFrame("Jasper Report Viewer");
            JRViewer viewer = new JRViewer(jasperPrint);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(viewer);
            frame.setSize(new Dimension(1200, 800));
            frame.setVisible(true);

        }catch (JRException | SQLException e){
            e.printStackTrace();
        }
    }

}
