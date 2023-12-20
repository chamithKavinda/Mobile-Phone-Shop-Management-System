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
import lk.ijse.MobileVision.dto.CustomerDto;
import lk.ijse.MobileVision.dto.ItemDto;
import lk.ijse.MobileVision.dto.tm.ItemTm;
import lk.ijse.MobileVision.model.CustomerModel;
import lk.ijse.MobileVision.model.ItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class ItemForm {

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private AnchorPane root4;

    @FXML
    private TableView<ItemTm> tblItem;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;


    public void initialize(){
        setCellValueFactory();
        loadAllItem();
    }

    private void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
    }

    private void loadAllItem(){
        var model = new ItemModel();

        ObservableList<ItemTm> obList = FXCollections.observableArrayList();

        try{
            List<ItemDto> dtoList = model.getAllItems();

            for(ItemDto dto : dtoList){
                obList.add(
                        new ItemTm(
                                dto.getId(),
                                dto.getDescription(),
                                dto.getUnitPrice(),
                                dto.getQtyOnHand()
                        )
                );
            }
            tblItem.setItems(obList);
            tblItem.refresh();
        }catch(SQLException e){
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

        var itemModel = new ItemModel();
        try{
            boolean isDeleted = itemModel.deleteItem(id);

            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION,"item deleted! ").show();
                loadAllItem();
            }
        }catch(SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        boolean isItemValidated = validateItem();
        if (isItemValidated) {
            // new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully!").show();
        }

    }

    private boolean validateItem() {
        String id = txtId.getText();
        boolean isItemIDValidated = Pattern.matches("[I][0-9]{3,}", id);
        if (!isItemIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item ID!").show();
            return false;
        }
        String description = txtDescription.getText();
        boolean isItemDescriptionValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", description);
        if (!isItemDescriptionValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item Description").show();
            return false;
        }
        String unitPrice = txtUnitPrice.getText();
        boolean isItemUnitPriceValidated = Pattern.matches("^[0-9]+(?:\\.[0-9]{1,2})?$", unitPrice);
        if (!isItemUnitPriceValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item Unit Price").show();
            return false;
        }

        String qtyOnHand = txtQtyOnHand.getText();
        boolean isItemQtyOnHandValidated = Pattern.matches("^[1-9]\\d*(\\.\\d+)?$", qtyOnHand);
        if (!isItemQtyOnHandValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item QtyOnHand").show();
            return false;
        }

        var dto = new ItemDto(id,description,unitPrice,qtyOnHand);

        var model = new ItemModel();
        try{
            boolean isSaved = model.saveItem(dto);
            if(isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"item Saved!").show();
                loadAllItem();
                clearFields();
                tblItem.refresh();
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String description = txtDescription.getText();
        String unitPrice = txtUnitPrice.getText();
        String qtyOnHand = txtQtyOnHand.getText();

        boolean isItemValidated = validateUpdatedItem(id, description , unitPrice, qtyOnHand);

        if (isItemValidated) {

            var  dto = new ItemDto(id,description,unitPrice,qtyOnHand);

            var model = new ItemModel();
            try{
                boolean isUpdated = model.updateItem(dto);
                System.out.println(isUpdated);
                if(isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION,"item Updated!").show();
                    loadAllItem();
                }
            }catch(SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    private boolean validateUpdatedItem(String id, String description, String unitPrice, String qtyOnHand) {

        boolean isItemIDValidated = Pattern.matches("[I][0-9]{3,}", id);
        if (!isItemIDValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item ID!").show();
            return false;
        }


        boolean isItemDescriptionValidated = Pattern.matches("^[A-Za-z0-9\\s.,;:'\"!?()-]+$", description);
        if (!isItemDescriptionValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item Description!").show();
            return false;
        }


        boolean isItemUnitPriceValidated = Pattern.matches("^[0-9]+(?:\\.[0-9]{1,2})?$", unitPrice);
        if (!isItemUnitPriceValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item Unit Price").show();
            return false;
        }


        boolean isItemQtyOnHandValidated = Pattern.matches("^[1-9]\\d*(\\.\\d+)?$", qtyOnHand);
        if (!isItemQtyOnHandValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Item QtyOnHand").show();
            return false;
        }

        return true;
    }


    @FXML
    void txtSearchOnAction(ActionEvent event) {
            String id = txtId.getText();

            var model = new ItemModel();
            try{
                ItemDto dto = model.searchItem(id);

                if(dto != null){
                    fillFields(dto);
                }else{
                    new Alert(Alert.AlertType.INFORMATION,"Item not found!").show();
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
    }

    private void fillFields(ItemDto dto) {
        txtId.setText(dto.getId());
        txtDescription.setText(dto.getDescription());
        txtUnitPrice.setText(dto.getUnitPrice());
        txtQtyOnHand.setText(dto.getQtyOnHand());
    }

    void clearFields() {
        txtId.clear();
        txtDescription.setText("");
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
    }

}
