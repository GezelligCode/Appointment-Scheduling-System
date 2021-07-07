package View_Controller;

import Credentials.Credentials;
import DBAccess.DBCountries;
import DBAccess.DBUsers;
import Database.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class Login_Controller
{
    @FXML
    private TextField userName;

    @FXML
    private PasswordField userPassword;

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
            System.out.println("Login failed: invalid username or password");
        }
    }


}
