package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.sql.Timestamp;
import java.util.regex.Pattern;

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

    // Constructor for adding new customers
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

    // Constructor for modifying a current customer
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

    // Constructor for modifying a customer from a country without a division
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode,
                    String customerPhone, User user)
    {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerModifier = user.getUserName();
    }

    // Constructor for getting all customers from DB
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

    // Constructor for modifying customers
    public Customer(Customer customer, Timestamp customerModifyDate, String customerModifier, int customerDivisionID)
    {
        this.customerName = customer.getCustomerName();
        this.customerAddress = customer.getCustomerAddress();
        this.customerPostalCode = customer.getCustomerPostalCode();
        this.customerPhone = customer.getCustomerPhone();
        this.customerModifyDate = customerModifyDate;
        this.customerModifier = customerModifier;
        this.customerDivisionID = customer.getCustomerDivisionID();
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

    public Timestamp getCustomerCreateDate()
    {
        return customerCreateDate;
    }

    public void setCustomerCreateDate(Timestamp customerCreateDate)
    {
        this.customerCreateDate = customerCreateDate;
    }

    public String getCustomerCreator()
    {
        return customerCreator;
    }

    public void setCustomerCreator(String customerCreator)
    {
        this.customerCreator = customerCreator;
    }

    public Timestamp getCustomerModifyDate()
    {
        return customerModifyDate;
    }

    public void setCustomerModifyDate(Timestamp customerModifyDate)
    {
        this.customerModifyDate = customerModifyDate;
    }

    public String getCustomerModifier()
    {
        return customerModifier;
    }

    public void setCustomerModifier(String customerModifier)
    {
        this.customerModifier = customerModifier;
    }

    public int getCustomerDivisionID()
    {
        return customerDivisionID;
    }

    public void setDivisionID(int customerDivision)
    {
        this.customerDivisionID = customerDivision;
    }

    public String getCustomerDivisionName() { return customerDivisionName; }

    public void setCustomerDivisionName(String customerDivisionName) { this.customerDivisionName = customerDivisionName; }

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
