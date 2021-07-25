package View_Controller;

import DBAccess.DBContacts;
import Model.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** FXML AddContacts_Controller Class: Handles the addition of new contacts. */
public class AddContacts_Controller implements Initializable
{
    @FXML private TextField contactID;
    @FXML private TextField contactName;
    @FXML private TextField contactEmail;

    /** Sets the initial conditions of the Add Contacts scene.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        contactID.setDisable(true);
        contactID.setText("Auto-generated");
    }

    /** Executes the addition of the contact to the database. */
    public void saveHandler(ActionEvent event) throws IOException
    {
        String name = contactName.getText();
        String email = contactEmail.getText();

        if(Contact.validateContact(name, email))
        {
            Contact addedContact = new Contact(name, email);

            DBContacts.addContact(addedContact);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consultants");
            alert.setHeaderText("Consultant Creation Successful");
            alert.setContentText("Consultant " + addedContact.getContactName() + " succesfully added.");
            alert.showAndWait();

            // Return to Contacts screen
            Parent contacts = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
            Scene scene = new Scene(contacts);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    /** Returns the user to the main Contacts screen. */
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
