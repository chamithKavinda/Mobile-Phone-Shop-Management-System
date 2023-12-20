package lk.ijse.MobileVision.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.MobileVision.dto.UserDto;
import lk.ijse.MobileVision.model.UserModel;

import java.io.IOException;
import java.security.cert.PolicyNode;
import java.sql.SQLException;

public class SignUpForm {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    void btnSignUpOnAction(ActionEvent event) {
        try {
            boolean userCheck = txtEmail.getText().equals(UserModel.getEmail(txtEmail.getText()));
            if (!userCheck) {
                UserDto dto = new UserDto();
                dto.setUserName(txtUserName.getText());
                dto.setPassword(txtPassword.getText());
                dto.setEmail(txtEmail.getText());

                boolean isSaved = UserModel.saveUser(dto);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "User Saved").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "user not saved").show();
                }
            }
        } catch ( SQLException | ClassNotFoundException throwables ){
            throwables.printStackTrace();
        }
    }

    @FXML
    void hyperLoginOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/loginForm.fxml"));

        Scene scene = new Scene(rootNode);


        root.getChildren().clear();
        Stage primaryStage = (Stage) root.getScene().getWindow();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
    }
}