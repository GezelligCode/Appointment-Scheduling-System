package View_Controller;

import DBAccess.DBCountries;
import DBAccess.DBCustomers;
import DBAccess.DBDivisions;
import DBAccess.DBUsers;
import Model.Country;
import Model.Customer;
import Model.Division;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** FXML AddCustomers_Controller Class: Handles addition of new customers. */
public class AddCustomers_Controller implements Initializable
{
    @FXML private TextField customerID;
    @FXML private TextField customerName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPostalCode;
    @FXML private TextField customerPhone;
    @FXML private ComboBox customerCountry;
    @FXML private ComboBox customerDivision;

    /** Sets the initial conditions of the Add Customers scene, such as prepopulating the comboboxes.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        customerID.setDisable(true);
        customerID.setText("Auto-generated");
        customerCountry.setItems(countryList());
        customerDivision.setItems(divisionList());
    }

    /** Populates the division field based on the user-selected country. */
    public void countrySelectionHandler()
    {
        String countrySelected = customerCountry.getValue().toString();

        customerDivision.setItems(divisionsFilteredByCountry(countrySelected));
    }

    /** Produces the list of all countries to select from. */
    public ObservableList countryList()
    {
        ObservableList<String> countries = FXCollections.observableArrayList();

        for(Country country : DBCountries.getAllCountries())
        {
            countries.add(country.getCountry());
        }

        return countries;
    }

    /** Produces the list of all divisions from the database. */
    public ObservableList divisionList()
    {
        ObservableList<String> divisions = FXCollections.observableArrayList();

        for(Division division : DBDivisions.getAllDivisions())
        {
            divisions.add(division.getDivision());
        }

        return divisions;
    }

    /** Produces the list of all divisions to select from, based on selected country. */
    public ObservableList divisionsFilteredByCountry(String country)
    {
        ObservableList<String> divisions = FXCollections.observableArrayList();

        for(String divisionName : DBDivisions.getAllDivisionsByCountry(country))
        {
            divisions.add(divisionName);
        }

        return divisions;
    }

    /** Executes the addition of the customer to the database. */
    public void saveHandler(ActionEvent event) throws IOException
    {
        try
        {
            if(Customer.validateCustomer(customerName.getText(), customerAddress.getText(), customerPostalCode.getText().toString(),
                    customerPhone.getText()))
            {
                String name = customerName.getText();
                String address = customerAddress.getText();
                String postalCode = customerPostalCode.getText();
                String phone = customerPhone.getText();
                int division = DBDivisions.getDivisionIDByName(customerDivision.getValue().toString());
                User user = DBUsers.getCurrentUser();

                //Use the DBCustomers class to interface with the DB, in this case to add the customer
                // First create a customer instance
                Customer addedCustomer = new Customer(name, address, postalCode, phone, division, user);

                //DBCustomers Method
                DBCustomers.addCustomer(addedCustomer, user);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customers");
                alert.setHeaderText("Customer Creation Successful");
                alert.setContentText("Customer " + addedCustomer.getCustomerName() + " succesfully added.");
                alert.showAndWait();

                // Return to Customers screen
                Parent addProduct = FXMLLoader.load(getClass().getResource("Customers.fxml"));
                Scene scene = new Scene(addProduct);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NullPointerException npe)
        {
            if(customerCountry.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customers");
                alert.setHeaderText("Country Selection is Required");
                alert.setContentText("Please select a country.\nNote that our main offices "+
                        "are in the following locations:\nPhoenix, Arizona\nWhite Plains, New York\nMontreal, Canada\nLondon, England");
                alert.showAndWait();
            }
            else if(!customerDivision.getItems().isEmpty() && customerCountry.getValue() != null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customers");
                alert.setHeaderText("Division Selection is Required");
                alert.setContentText("Please select a division.\nNote that our main offices "+
                        "are in the following locations:\nPhoenix, Arizona\nWhite Plains, New York\nMontreal, Canada\nLondon, England");
                alert.showAndWait();
            }
            else if(customerDivision.getItems().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customers");
                alert.setHeaderText("Valid Country Selection is Required");
                alert.setContentText("Please select a valid country.\nNote that our main offices "+
                        "are in the following locations:\nPhoenix, Arizona\nWhite Plains, New York\nMontreal, Canada\nLondon, England");
                alert.showAndWait();
            }
        }

    }

    /** Returns the user to the main Customers screen. */
    public void cancelHandler(ActionEvent event) throws IOException
    {

        // Return to Customers screen
        Parent addProduct = FXMLLoader.load(getClass().getResource("Customers.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
