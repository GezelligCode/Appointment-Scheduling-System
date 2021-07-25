package Model;

import javafx.scene.control.Alert;
import java.sql.Timestamp;

/** Customer Class: Handles all manipulation methods for Customer objects. */
public class Customer
{
    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private Timestamp customerCreateDate;
    private String customerCreator;
    private Timestamp customerModifyDate;
    private String customerModifier;
    private int customerDivisionID;
    private String customerDivisionName;

    /** Constructor for creating new customers. */
    public Customer(String customerName, String customerAddress, String customerPostalCode, String customerPhone,
                    int customerDivisionID, User user)
    {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerDivisionID = customerDivisionID;
        this.customerCreator = user.getUserName();
    }

    /** Constructor for modifying current customers. */
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode,
                    String customerPhone, int customerDivisionID, User user)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerModifier = user.getUserName();
        this.customerDivisionID = customerDivisionID;
    }

    /** Constructor for getting all Customer objects from the database. */
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhone,
                    String customerCreator, String customerDivisionName)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerCreator = customerCreator;
        this.customerDivisionName = customerDivisionName;
    }

    public int getCustomerID()
    {
        return customerID;
    }

    public void setCustomerID(int customerID)
    {
        this.customerID = customerID;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerAddress()
    {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress)
    {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPostalCode()
    {
        return customerPostalCode;
    }

    public void setCustomerPostalCode(String customerPostalCode)
    {
        this.customerPostalCode = customerPostalCode;
    }

    public String getCustomerPhone()
    {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

    public String getCustomerModifier()
    {
        return customerModifier;
    }

    public int getCustomerDivisionID()
    {
        return customerDivisionID;
    }

    /** Gets customer ID from a customer name, where the name given is from a String object that contains the ID. */
    public static int getCustomerIDByName(String customerName)
    {
        int customerID = Integer.parseInt(customerName.substring(0, customerName.indexOf(":")));

        return customerID;
    }

    public String getCustomerDivisionName() { return customerDivisionName; }

    /** Checks whether the addition or modification of a customer is valid. The parameters come from the input fields
     * of the Add or Modify Customer screens. */
    public static boolean validateCustomer(String name, String address, String postalCode, String phone)
    {
        String errors = "";


        if(name.isEmpty() || name.isBlank())
        {
            errors = errors + " An entry for Name is required.\n";
        }
        if(name.length() > 50)
        {
            errors = errors + " The entry for Name cannot exceed 50 characters.\n";
        }
        if(address.isEmpty() || address.isBlank())
        {
            errors = errors + " An entry for Address is required.\n";
        }
        if(address.length() > 100)
        {
            errors = errors + " The entry for Name cannot exceed 100 characters.\n";
        }
        if(postalCode.isEmpty() || postalCode.isBlank())
        {
            errors = errors + " An entry for Postal Code is required.\n";
        }
        if(postalCode.length() > 50)
        {
            errors = errors + " The entry for Postal Code cannot exceed 50 characters.\n";
        }
        if(phone.isEmpty() || phone.isBlank())
        {
            errors = errors + " An entry for Phone is required.\n";
        }
        if(phone.length() > 50)
        {
            errors = errors + " The entry for Phone cannot exceed 50 characters.\n";
        }

        if(errors.isEmpty())
        {
            return true;
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customers");
            alert.setHeaderText("Please check the form for the following error(s):");
            alert.setContentText(errors);
            alert.showAndWait();

            return false;
        }
    }

}
