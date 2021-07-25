package Main;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        System.out.println("javafx.runtime.version: " + System.getProperties().get("javafx.runtime.version"));
        ResourceBundle loginFrench = ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("../View_Controller/Login.fxml"), loginFrench);
        primaryStage.setTitle("WGU Appointments System");
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
        DBConnection.closedConnection();
    }
}
