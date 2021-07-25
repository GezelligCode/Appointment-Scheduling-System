package View_Controller;

import DBAccess.DBAppointments;
import DBAccess.DBCustomers;
import Model.Appointment;
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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** FXML Customers_Controller Class: Handles the main customers screen and display of all customers and their associated
 * appointments. Also redirects to Add or Modify Customers scene. Deletion of customers is handled directly.
 */
public class Customers_Controller implements Initializable
{
    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, Integer> customerID;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerAddress;
    @FXML private TableColumn<Customer, String> customerPostalCode;
    @FXML private TableColumn<Customer, String> customerPhone;
    @FXML private TableColumn<Customer, String> customerDivision;
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
    @FXML private TableColumn<Appointment, Integer> contactID;
    @FXML private MenuButton menuBar;

    private static Customer selectedCustomer = null;

    /** Public getter for the private selectedCustomer variable for the ModifyCustomers_Controller to use. */
    public static Customer getSelectedCustomer()
    {
        return selectedCustomer;
    }

    /** Sets the initial conditions of the Contacts scene, e.g. pre-populating the table views.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        populateCustomers();
        updateCustomersTable();
        populateApptsTable();
    }

    /** Redirects to the Add Customer scene. */
    public void addCustomerHandler(ActionEvent event) throws IOException
    {
        MenuItem menuItem = (MenuItem)event.getTarget();
        ContextMenu cm = menuItem.getParentPopup();
        Parent addCustomer = FXMLLoader.load(getClass().getResource("AddCustomers.fxml"));
        Scene scene = new Scene(addCustomer);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Redirects to the Modify Customer scene. */
    public void modifyCustomerHandler(ActionEvent event) throws IOException
    {
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer != null)
        {
            Parent modifyCustomer = FXMLLoader.load(getClass().getResource("ModifyCustomers.fxml"));
            Scene scene = new Scene(modifyCustomer);
            Stage window = (Stage)menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Customers");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer to modify");
            alert.showAndWait();
        }
    }

    /** Executes the removal of a customer from the database. */
    public void deleteHandler()
    {
        Customer customer = customersTable.getSelectionModel().getSelectedItem();

        if(customer != null)
        {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Customers");
            confirm.setHeaderText("Confirm Customer Deletion");
            confirm.setContentText("Are you sure you want to delete the following consultant?\n" + customer.getCustomerName());
            confirm.showAndWait();

            if(confirm.getResult() == ButtonType.OK)
            {
                if(DBCustomers.validateCustomerRemoval(customer))
                {
                    if(DBCustomers.removeCustomer(customer))
                    {
                        updateCustomersTable();
                        Alert alert = new Alert((Alert.AlertType.INFORMATION));
                        alert.setTitle("Customers");
                        alert.setHeaderText("Successful Deletion");
                        alert.setContentText(customer.getCustomerName() + " is removed.");
                        alert.showAndWait();
                    }
                    else
                    {
                        System.out.println("No customer by that ID found");
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Customers");
                    alert.setHeaderText("Customer Has Associated Appointments");
                    alert.setContentText("Please update or remove the associated appointment(s) for \n" + customer.getCustomerName() +
                            " before removing " + customer.getCustomerName()+".");
                    alert.showAndWait();
                }
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Customers");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select an customer to delete");
            alert.showAndWait();
        }
    }

    /** Redirects to the main Appointments screen. */
    public void cancelHandler(ActionEvent event) throws IOException
    {
        Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Maps the Customer fields to the table view columns. */
    private void populateCustomers()
    {
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("customerDivisionName"));
    }

    /** Updates the Customers table view with all contacts. */
    private void updateCustomersTable()
    {
        customersTable.setItems(DBCustomers.getAllCustomers());
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
        contactID.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
    }

    /** Updates the Associated Appointments table with the appointments associated with a customer selected by the user. */
    public void updateAssociatedApptsTable(MouseEvent event)
    {
        if(event.getSource() == customersTable)
        {
            selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            if(getSelectedCustomer() != null)
            {
                assocApptsTable.setItems(DBAppointments.getAllAppointmentsByCustomerID(getSelectedCustomer()));
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Customers");
                alert.setHeaderText("No Customer Selected");
                alert.setContentText("Please select an customer to view associated appointments");
                alert.showAndWait();
            }
        }
        else
        {
            System.out.println("No event found");
        }
    }
}
