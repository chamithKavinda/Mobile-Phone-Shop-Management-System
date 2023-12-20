package lk.ijse.MobileVision;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Applnitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/loginForm.fxml"))));
        stage.setTitle("Login Form");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.centerOnScreen();

        stage.show();
    }
}
