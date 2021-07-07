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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.Contacts_Controller.getSelectedContact;

public class ModifyContacts_Controller implements Initializable
{
    @FXML private TextField contactID;
    @FXML private TextField contactName;
    @FXML private TextField contactEmail;


    private Contact selectedContact = getSelectedContact();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        contactID.setDisable(true);

        setFields();
    }

    public void saveHandler(ActionEvent event) throws IOException
    {
        int ID = selectedContact.getContactID();
        String name = contactName.getText();
        String email = contactEmail.getText();

        Contact modifiedContact = new Contact(ID, name, email);

        if(DBContacts.updateContact(modifiedContact))
        {
            System.out.println("Contact updated successfully");

            // Switch to Contacts Scene
            Parent Contacts = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
            Scene scene = new Scene(Contacts);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            System.out.println("Check form for errors");
        }

    }

    public void cancelHandler(ActionEvent event) throws IOException
    {
        // Switch to Customers Scene
        Parent Customers = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
        Scene scene = new Scene(Customers);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void setFields()
    {
        contactID.setText(Integer.toString(selectedContact.getContactID()));
        contactName.setText(selectedContact.getContactName());
        contactEmail.setText(selectedContact.getContactEmail());
    }
}
