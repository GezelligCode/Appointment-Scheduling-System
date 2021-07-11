package View_Controller;

import Credentials.Credentials;
import DBAccess.DBCountries;
import DBAccess.DBUsers;
import Database.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login_Controller implements Initializable
{
    @FXML private TextField userName;
    @FXML private PasswordField userPassword;
    @FXML private Label zoneID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        zoneID();
        System.out.println("Get default locale: " + Locale.getDefault().toString());
    }

    public Locale getLocale()
    {
        return Locale.getDefault();
    }

    public void zoneID()
    {
        Locale currentLocale = Locale.getDefault();

        Time time = Time.valueOf(LocalTime.now());
        String zone = ZoneId.systemDefault().toString();
        zoneID.setText("System Zone: " + zone);

        FXMLLoader fxmlLoader = new FXMLLoader();

        //fxmlLoader.setResources(ResourceBundle.getBundle("Login_Controller", currentLocale));
        System.out.println(currentLocale.getDisplayLanguage());

    }

    public void loginHandler(ActionEvent event) throws IOException
    {
        if(Credentials.validate(userName.getText().toString(), userPassword.getText().toString()))
        {
            System.out.println("Login Successful");
            DBConnection.startConnection();
            DBCountries.checkDateConversion();
            DBUsers.loginUser(userName.getText().toString(), userPassword.getText().toString());

            // redirect to Appts
            Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
            Scene scene = new Scene(addProduct);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            // popup warning
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText("Login Failed");
            alert.setContentText("Incorrect username or password.");
            alert.showAndWait();
        }
    }



}
