package lk.ijse.MobileVision.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.MobileVision.db.DbConnection;
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.ItemDto;
import lk.ijse.MobileVision.dto.PlaceOrderDto;
import lk.ijse.MobileVision.dto.tm.CartTm;
import lk.ijse.MobileVision.model.CustomerModel;
import lk.ijse.MobileVision.model.ItemModel;
import lk.ijse.MobileVision.model.OrderModel;
import lk.ijse.MobileVision.model.PlaceOrderModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaceOrderForm {
    private final CustomerModel customerModel = new CustomerModel();
    private final ItemModel itemModel = new ItemModel();
    private final OrderModel orderModel = new OrderModel();
    private final ObservableList<CartTm> obList = FXCollections.observableArrayList();


    @FXML
    private JFXButton btnPlaceOrderOnAction;

    @FXML
    private JFXButton btnReciptOnAction;

    @FXML
    private JFXComboBox<String> cmbCustomerContact;

    @FXML
    private JFXComboBox<String> cmbItemId;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane root6;

    @FXML
    private TableView<CartTm> tblOrderCart;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtNetTotal;

    @FXML
    private TextField txtOrderDate;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;

    private final PlaceOrderModel placeOrderModel = new PlaceOrderModel();

    public void initialize() {
        setCellValueFactory();
        generateNextOrderId();
        setDate();
        loadCustomerContacts() ;
        loadItemCodes();
    }

    private void setCellValueFactory() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void generateNextOrderId() {
        try {
            String o_id = orderModel.generateNextOrderId();
            txtOrderId.setText(o_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadItemCodes() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<ItemDto> itemList = itemModel.getAllItems();

            for (ItemDto itemDto : itemList) {
                obList.add(itemDto.getId());
            }

            cmbItemId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerContacts() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<CustomerDto> cusList = customerModel.getAllCustomers();

            for (CustomerDto dto : cusList) {
                obList.add(dto.getTel());
            }
            cmbCustomerContact.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        String date = String.valueOf(LocalDate.now());
        txtOrderDate.setText(date);
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String code = cmbItemId.getValue();
        String description = txtDescription.getText();
        //int qty = Integer.parseInt(txtQty.getText());


        int qtyhand= Integer.parseInt(txtQtyOnHand.getText());

        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText());
            if(qty>qtyhand){
                // System.out.println("Please enter a value less than"+qtyhand);
                new Alert(Alert.AlertType.ERROR, "Please enter a value less than Qty On hand ->"+qtyhand).show();

            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid numeric input for qtyOnHand!").show();
            return;
        }

        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double total = qty * unitPrice;
        Button btn = new Button("remove");
        btn.setCursor(Cursor.HAND);

        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int index = tblOrderCart.getSelectionModel().getSelectedIndex();
                obList.remove(index);
                tblOrderCart.refresh();

                calculateNetTotal();
            }
        });

        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            if (code.equals(colItemId.getCellData(i))) {
                qty += (int) colQty.getCellData(i);
                total = qty * unitPrice;

                obList.get(i).setQty(qty);
                obList.get(i).setTotal(total);

                tblOrderCart.refresh();
                calculateNetTotal();
                return;
            }
        }

        obList.add(new CartTm(
                code,
                description,
                qty,
                unitPrice,
                total,
                btn
        ));

        tblOrderCart.setItems(obList);
        calculateNetTotal();
        txtQty.clear();
    }

    private void calculateNetTotal() {
        double total = 0;
        for (int i = 0; i < tblOrderCart.getItems().size(); i++) {
            total += (double) colTotal.getCellData(i);
        }

        txtNetTotal.setText(String.valueOf(total));
    }

    @FXML
    void btnNewOnAction(ActionEvent event) throws IOException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Customer Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        String o_id = txtOrderId.getText();
        String c_tel =  cmbCustomerContact.getValue();
        LocalDate date = LocalDate.parse(txtOrderDate.getText());

        List<CartTm> tmList = new ArrayList<>();

        for (CartTm cartTm : obList) {
            tmList.add(cartTm);
        }

        var placeOrderDto = new PlaceOrderDto(
                o_id,
                c_tel,
                date,
                tmList
        );

        try {
            boolean isSuccess = placeOrderModel.placeOrder(placeOrderDto);
            if(isSuccess) {
                new Alert(Alert.AlertType.CONFIRMATION, "order completed!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtCustomerName.setText("");
        txtOrderId.setText("");
        txtOrderDate.setText("");
        txtQty.setText("");
        txtDescription.setText("");
        txtQtyOnHand.setText("");
        txtUnitPrice.setText("");
        txtNetTotal.setText("");

    }

    @FXML
    void cmbCustomerContactOnAction(ActionEvent event) throws SQLException {
        String tel = cmbCustomerContact.getValue();
        CustomerDto dto = customerModel.searchCustomer(tel);

        txtCustomerName.setText(dto.getName());
    }

    @FXML
    void cmbItemIdOnAction(ActionEvent event) {
        String code = cmbItemId.getValue();

        txtQty.requestFocus();

        try {

            System.out.println(code);
            ItemDto dto = itemModel.searchItem(code);

            System.out.println(dto.getDescription());
            System.out.println(dto.getUnitPrice());
            System.out.println(dto.getQtyOnHand());

            txtDescription.setText(dto.getDescription());
            txtUnitPrice.setText(String.valueOf(dto.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(dto.getQtyOnHand()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtQtyOnAction(ActionEvent event) {
        btnAddToCartOnAction(event);
    }


    @FXML
    void btnReciptOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/OrderReport.jrxml");
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
