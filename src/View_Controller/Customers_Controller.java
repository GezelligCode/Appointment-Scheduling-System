package View_Controller;

import DBAccess.DBAppointments;
import DBAccess.DBCustomers;
import Model.Appointment;
import Model.Country;
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

public class Customers_Controller implements Initializable
{
    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, Integer> customerID;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerAddress;
    @FXML private TableColumn<Customer, String> customerPostalCode;
    @FXML private TableColumn<Customer, String> customerPhone;
    @FXML private TableColumn<Customer, String> customerDivision;
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
    @FXML private TableColumn<Appointment, Integer> contactID;

    @FXML private MenuButton menuBar;

    private static Customer selectedCustomer = null;

    public static Customer getSelectedCustomer()
    {
        return selectedCustomer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        populateCustomers();
        updateCustomersTable();
        populateApptsTable();
    }

    public void addCustomerHandler(ActionEvent event) throws IOException
    {

        // Switch to Add Customer Scene
        MenuItem menuItem = (MenuItem)event.getTarget();
        ContextMenu cm = menuItem.getParentPopup();
        Parent addCustomer = FXMLLoader.load(getClass().getResource("AddCustomers.fxml"));
        Scene scene = new Scene(addCustomer);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void modifyCustomerHandler(ActionEvent event) throws IOException
    {
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if(selectedCustomer != null)
        {
            // Switch to Modify Customer Scene
//            MenuItem menuItem = (MenuItem)event.getTarget();
//            ContextMenu cm = menuItem.getParentPopup();
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

    public void deleteHandler(ActionEvent event) throws IOException
    {
        Customer customer = customersTable.getSelectionModel().getSelectedItem();

        if(customer != null)
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
                alert.setContentText("Please remove the associated appointment(s) for \n" + customer.getCustomerName() +
                        " before removing " + customer.getCustomerName()+".");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Customers");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select an customer to add");
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

    private void populateCustomers()
    {
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("customerDivisionName"));
    }

    private void updateCustomersTable()
    {
        customersTable.setItems(DBCustomers.getAllCustomers());
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
