package View_Controller;

import DBAccess.DBCustomers;
import DBAccess.DBDivisions;
import DBAccess.DBUsers;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.Customers_Controller.getSelectedCustomer;

public class ModifyCustomers_Controller implements Initializable
{
    @FXML private TextField customerID;
    @FXML private TextField customerName;
    @FXML private TextField customerAddress;
    @FXML private TextField customerPostalCode;
    @FXML private TextField customerPhone;
    @FXML private ComboBox customerDivision;

    private Customer selectedCustomer = getSelectedCustomer();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        customerID.setDisable(true);

        customerDivision.setItems(divisions());

        setFields();
    }

    public void saveHandler(ActionEvent event) throws IOException
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
            System.out.println("Customer updated successfully");

            // Switch to Customers Scene
            Parent Customers = FXMLLoader.load(getClass().getResource("Customers.fxml"));
            Scene scene = new Scene(Customers);
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
        Parent Customers = FXMLLoader.load(getClass().getResource("Customers.fxml"));
        Scene scene = new Scene(Customers);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
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

    public void setFields()
    {
        customerID.setText(Integer.toString(selectedCustomer.getCustomerID()));
        customerName.setText(selectedCustomer.getCustomerName());
        customerAddress.setText(selectedCustomer.getCustomerAddress());
        customerPostalCode.setText(selectedCustomer.getCustomerPostalCode());
        customerPhone.setText(selectedCustomer.getCustomerPhone());
        customerDivision.setValue(selectedCustomer.getCustomerDivisionName());

    }
}
