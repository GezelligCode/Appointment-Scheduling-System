package View_Controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import Model.Appointment;
import Model.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** FXML Contacts_Controller Class: Handles the main contacts screen and display of all contacts and their associated
 * appointments. Also redirects to Add or Modify Contacts scene. Deletion of contacts is handled directly.
 */
public class Contacts_Controller implements Initializable
{
    @FXML private TableView<Contact> contactsTable;
    @FXML private TableColumn<Contact, Integer> contactID;
    @FXML private TableColumn<Contact, String> contactName;
    @FXML private TableColumn<Contact, String> contactEmail;
    @FXML private TableView<Appointment> assocApptsTable;
    @FXML private TableColumn<Appointment, Integer> apptID;
    @FXML private TableColumn<Appointment, String> title;
    @FXML private TableColumn<Appointment, String> description;
    @FXML private TableColumn<Appointment, String> location;
    @FXML private TableColumn<Appointment, String> type;
    @FXML private TableColumn<Appointment, String> start;
    @FXML private TableColumn<Appointment, String> end;
    @FXML private TableColumn<Appointment, Integer> assocCustomerID;
    @FXML private TableColumn<Appointment, Integer> userID;
    @FXML private TableColumn<Appointment, Integer> assocContactID;
    @FXML private MenuButton menuBar;

    private static Contact selectedContact = null;

    /** Public getter for the private selectedContact variable for the ModifyContacts_Controller to use. */
    public static Contact getSelectedContact()
    {
        return selectedContact;
    }

    /** Sets the initial conditions of the Contacts scene, e.g. pre-populating the table views.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        populateContacts();
        updateContactsTable();
        populateApptsTable();
    }

    /** Redirects to the Add Contact scene. */
    public void addContactHandler(ActionEvent event) throws IOException
    {

        // Switch to Add Contacts Scene
        Parent addContacts = FXMLLoader.load(getClass().getResource("addContacts.fxml"));
        Scene scene = new Scene(addContacts);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Redirects to the Modify Contact scene. */
    public void modifyContactHandler(ActionEvent event) throws IOException
    {
        selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if(selectedContact != null)
        {
            // Switch to Modify Contacts Scene
            Parent modifyContacts = FXMLLoader.load(getClass().getResource("ModifyContacts.fxml"));
            Scene scene = new Scene(modifyContacts);
            Stage window = (Stage)menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Consultants");
            alert.setHeaderText("No Consultants Selected");
            alert.setContentText("Please select a consultant to modify");
            alert.showAndWait();
        }
    }

    /** Executes the removal of a contact from the database. */
    public void deleteContactHandler()
    {
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();

        if(contact != null)
        {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Consultants");
            confirm.setHeaderText("Confirm Consultant Deletion");
            confirm.setContentText("Are you sure you want to delete the following consultant?\n" + contact.getContactName());
            confirm.showAndWait();

            if(confirm.getResult() == ButtonType.OK)
            {
                if(DBContacts.validateContactRemoval(contact))
                {
                    if(DBContacts.removeContact(contact))
                    {
                        updateContactsTable();
                        Alert alert = new Alert((Alert.AlertType.INFORMATION));
                        alert.setTitle("Consultants");
                        alert.setHeaderText("Successful Deletion");
                        alert.setContentText(contact.getContactName() + " is removed.");
                        alert.showAndWait();
                    }
                    else
                    {
                        System.out.println("No contact by that ID found");
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Consultants");
                    alert.setHeaderText("Consultants Has Associated Appointments");
                    alert.setContentText("Please update or cancel the associated appointment(s) for \n" + contact.getContactName() +
                            " before removing " + contact.getContactName()+".");
                    alert.showAndWait();
                }
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Consultants");
            alert.setHeaderText("No Consultant Selected");
            alert.setContentText("Please select a consultant to add");
            alert.showAndWait();
        }
    }

    /** Redirects to the main Appointments screen. */
    public void cancelHandler(ActionEvent event) throws IOException
    {

        // Switch to Appts Scene
        Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Maps the Contact fields to the table view columns. */
    private void populateContacts()
    {
        contactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        contactName.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        contactEmail.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
    }

    /** Updates the Contacts table view with all contacts. */
    private void updateContactsTable()
    {
        contactsTable.setItems(DBContacts.getAllContacts());
    }

    /** Maps the Appointment fields to the Associated Appointments table view columns. */
    private void populateApptsTable()
    {
        apptID.setCellValueFactory(new PropertyValueFactory<>("ApptID"));
        title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        end.setCellValueFactory(new PropertyValueFactory<>("End"));
        assocCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        userID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        assocContactID.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
    }

    /** Updates the Associated Appointments table with the appointments associated with a contact selected by the user. */
    public void updateAssociatedApptsTable(MouseEvent event)
    {
        if(event.getSource() == contactsTable)
        {
            selectedContact = contactsTable.getSelectionModel().getSelectedItem();
            if(getSelectedContact() != null)
            {
                assocApptsTable.setItems(DBAppointments.getAllAppointmentsByContactID(getSelectedContact()));
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Contacts");
                alert.setHeaderText("No Contact Selected");
                alert.setContentText("Please select a contact to view associated appointments");
                alert.showAndWait();
            }
        }
        else
        {
            System.out.println("No event found");
        }
    }
}
