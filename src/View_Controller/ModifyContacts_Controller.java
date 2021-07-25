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

import static View_Controller.Contacts_Controller.getSelectedContact;

/** FXML ModifyContacts_Controller Class: Handles the modification of a current contact. */
public class ModifyContacts_Controller implements Initializable
{
    @FXML private TextField contactID;
    @FXML private TextField contactName;
    @FXML private TextField contactEmail;

    private Contact selectedContact = getSelectedContact();

    /** Sets the initial conditions of the Modify Appointments scene, such as prepopulating the input fields.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        contactID.setDisable(true);
        setFields();
    }
    /** Executes the update of the contact in the database. */
    public void saveHandler(ActionEvent event) throws IOException
    {
        int ID = selectedContact.getContactID();
        String name = contactName.getText();
        String email = contactEmail.getText();

        if(Contact.validateContact(name, email))
        {
            Contact modifiedContact = new Contact(ID, name, email);

            if(DBContacts.updateContact(modifiedContact))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Consultants");
                alert.setHeaderText("Consultant Modification Successful");
                alert.setContentText("Consultant " + modifiedContact.getContactName() + " succesfully updated.");
                alert.showAndWait();

                // Switch to Contacts Scene
                Parent Contacts = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
                Scene scene = new Scene(Contacts);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
    }

    /** Redirects to the main Contacts scene. */
    public void cancelHandler(ActionEvent event) throws IOException
    {
        Parent Customers = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
        Scene scene = new Scene(Customers);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Prepopulates the form's fields with the data values from the pre-selected contact to modify. */
    public void setFields()
    {
        contactID.setText(Integer.toString(selectedContact.getContactID()));
        contactName.setText(selectedContact.getContactName());
        contactEmail.setText(selectedContact.getContactEmail());
    }
}
