package View_Controller;

import DBAccess.DBCountries;
import DBAccess.DBCustomers;
import DBAccess.DBDivisions;
import DBAccess.DBUsers;
import Model.Country;
import Model.Customer;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.Customers_Controller.getSelectedCustomer;

/** FXML ModifyCustomers_Controller Class: Handles the modification of a current customer. */
public class ModifyCustomers_Controller implements Initializable
{
    @FXML private TextField customerID;
    @FXML private TextField customerName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPostalCode;
    @FXML private TextField customerPhone;
    @FXML private ComboBox customerDivision;
    @FXML private ComboBox customerCountry;

    private Customer selectedCustomer = getSelectedCustomer();

    /** Sets the initial conditions of the Modify Appointments scene, such as prepopulating the input fields.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        customerID.setDisable(true);
        customerCountry.setItems(countryList());
        customerDivision.setItems(divisionList());
        if(customerDivision.getItems().isEmpty())
        {
            customerDivision.setDisable(true);
        }
        else
        {
            customerDivision.setDisable(false);
        }

        setFields();
    }

    /** Populates the division field based on the user-selected country. */
    public void countrySelectionHandler()
    {
        String countrySelected = customerCountry.getValue().toString();

        customerDivision.setItems(divisionsFilteredByCountry(countrySelected));
        if(customerDivision.getItems().isEmpty())
        {
            customerDivision.setDisable(true);
        }
        else
        {
            customerDivision.setDisable(false);
        }
    }

    /** Produces the list of all countries to select from. */
    public ObservableList countryList()
    {
        ObservableList<String> countries = FXCollections.observableArrayList();

        for(Country country : DBCountries.getAllApplicableCountries())
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

    /** Executes the update of the customer in the database. */
    public void saveHandler(ActionEvent event) throws IOException
    {
        try
        {
            if(Customer.validateCustomer(customerName.getText(), customerAddress.getText(), customerPostalCode.getText().toString(),
                    customerPhone.getText()))
            {
                int ID = selectedCustomer.getCustomerID();
                String name = customerName.getText();
                String address = customerAddress.getText();
                String postalCode = customerPostalCode.getText();
                String phone = customerPhone.getText();
                int divisionID = DBDivisions.getDivisionIDByName(customerDivision.getValue().toString());

                Customer modifiedCustomer = new Customer(ID, name, address, postalCode, phone, divisionID, DBUsers.getCurrentUser());

                if(DBCustomers.updateCustomer(modifiedCustomer))
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Customers");
                    alert.setHeaderText("Customer Modification Successful");
                    alert.setContentText("Consultant " + modifiedCustomer.getCustomerName() + " succesfully updated.");
                    alert.showAndWait();

                    // Switch to Customers Scene
                    Parent Customers = FXMLLoader.load(getClass().getResource("Customers.fxml"));
                    Scene scene = new Scene(Customers);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                }
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

    /** Redirects to the main Customers scene. */
    public void cancelHandler(ActionEvent event) throws IOException
    {
        // Switch to Customers Scene
        Parent Customers = FXMLLoader.load(getClass().getResource("Customers.fxml"));
        Scene scene = new Scene(Customers);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Prepopulates the form's fields with the data values from the pre-selected customer to modify. */
    public void setFields()
    {
        customerID.setText(Integer.toString(selectedCustomer.getCustomerID()));
        customerName.setText(selectedCustomer.getCustomerName());
        customerAddress.setText(selectedCustomer.getCustomerAddress());
        customerPostalCode.setText(selectedCustomer.getCustomerPostalCode());
        customerPhone.setText(selectedCustomer.getCustomerPhone());
        customerCountry.setValue(DBCustomers.getCustomerCountry(selectedCustomer));
        customerDivision.setValue(selectedCustomer.getCustomerDivisionName());

    }
}
