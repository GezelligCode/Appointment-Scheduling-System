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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomers_Controller implements Initializable
{
    @FXML private TextField customerID;
    @FXML private TextField customerName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPostalCode;
    @FXML private TextField customerPhone;
    @FXML private ComboBox customerDivision;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        customerID.setDisable(true);
        customerID.setText("Auto-generated");
        customerDivision.setItems(divisions());
    }

    public ObservableList divisions()
    {
        ObservableList<String> divisionList = FXCollections.observableArrayList();

        for(Division div : DBDivisions.getAllDivisions())
        {
            divisionList.add(div.getDivision());
        }

        return divisionList;
    }

    public void saveHandler(ActionEvent event) throws IOException
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

        // Will need error handling

        // Return to Customers screen
        Parent addProduct = FXMLLoader.load(getClass().getResource("Customers.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

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
