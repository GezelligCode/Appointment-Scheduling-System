package View_Controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBCustomers;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
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
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Contacts_Controller implements Initializable
{
    @FXML private TableView<Contact> contactsTable;
    @FXML private TableColumn<Contact, Integer> contactID;
    @FXML private TableColumn<Contact, String> contactName;
    @FXML private TableColumn<Contact, String> contactEmail;
    //@FXML private TableColumn<Country, String> customerCountry;
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

    public static Contact getSelectedContact()
    {
        return selectedContact;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        populateContacts();
        updateContactsTable();
        populateApptsTable();
    }

    public void addContactHandler(ActionEvent event) throws IOException
    {

        // Switch to Add Contacts Scene
        Parent addContacts = FXMLLoader.load(getClass().getResource("addContacts.fxml"));
        Scene scene = new Scene(addContacts);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

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
            alert.setTitle("Contacts");
            alert.setHeaderText("No Contact Selected");
            alert.setContentText("Please select a contact to modify");
            alert.showAndWait();
        }
    }

    public void deleteContactHandler(ActionEvent event) throws IOException
    {
        //ToDo Add an if-then check whether the customer to be deleted is in an appointment. If so, then advise
        // the user that the customer cannot be deleted until the associated appointments are deleted.
        // Idea: Maybe include a window/table view for associated appointments.
        // Maybe implement an evaluation of whether the count of customer's appointments is greater than zero.
        Contact contact = contactsTable.getSelectionModel().getSelectedItem();

        if(contact != null)
        {
            if(DBContacts.validateContactRemoval(contact))
            {
                if(DBContacts.removeContact(contact))
                {
                    updateContactsTable();
                    System.out.println("Contact removed");
                }
                else
                {
                    System.out.println("No contact by that ID found");
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Contacts");
                alert.setHeaderText("Contact Has Associated Appointments");
                alert.setContentText("Please remove the associated appointment(s) for " + contact.getContactName() +
                        " before removing " + contact.getContactName()+".");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Contacts");
            alert.setHeaderText("No Contact Selected");
            alert.setContentText("Please select a contact to add");
            alert.showAndWait();
        }
    }

    public void cancelHandler(ActionEvent event) throws IOException
    {

        // Switch to Appts Scene
        Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private void populateContacts()
    {
        contactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        contactName.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        contactEmail.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
    }

    private void updateContactsTable()
    {
        contactsTable.setItems(DBContacts.getAllContacts());
    }

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
        contactID.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
    }

    public void updateAssociatedApptsTable(MouseEvent event) throws IOException
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
