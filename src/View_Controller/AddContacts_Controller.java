package View_Controller;

import DBAccess.DBContacts;
import DBAccess.DBCustomers;
import DBAccess.DBDivisions;
import DBAccess.DBUsers;
import Model.Contact;
import Model.Customer;
import Model.Division;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddContacts_Controller implements Initializable
{
    @FXML private TextField contactID;
    @FXML private TextField contactName;
    @FXML private TextField contactEmail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        contactID.setDisable(true);
        contactID.setText("Auto-generated");
    }


    public void saveHandler(ActionEvent event) throws IOException
    {
        String name = contactName.getText();
        String email = contactEmail.getText();

        //Use the DBCustomers class to interface with the DB, in this case to add the customer
        // First create a customer instance
        Contact addedContact = new Contact(name, email);

        //DBCustomers Method
        DBContacts.addContact(addedContact);

        // Will need error handling

        // Return to Contacts screen
        Parent contacts = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
        Scene scene = new Scene(contacts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    public void cancelHandler(ActionEvent event) throws IOException
    {

        // Return to Contacts screen
        Parent contacts = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
        Scene scene = new Scene(contacts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }




}
