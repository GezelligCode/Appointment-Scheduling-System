package Model;

import java.sql.Timestamp;

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
        //int userID = customerCreator.getUserID();

        //this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        //this.customerCreateDate = customerCreateDate;
        //this.customerCreator = customerCreator; // this may need to be a custom String instead
        //this.customerModifyDate = customerModifyDate;
        //this.customerModifier = customerModifier;
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
        //this.customerCreateDate = customerCreateDate;
        //this.customerCreator = customerCreator; // this may need to be a custom String instead
        //this.customerModifyDate = customerModifyDate;
        this.customerModifier = user.getUserName();
        this.customerDivisionID = customerDivisionID;
        //this.customerCreator = user.getUserName();
    }

    // Constructor for getting all customers from DB
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhone,
                    String customerCreator, String customerDivisionName)
    {
        //int userID = customerCreator.getUserID();
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        //this.customerCreateDate = customerCreateDate;
        this.customerCreator = customerCreator; // this may need to be a custom String instead
        //this.customerModifyDate = customerModifyDate;
        //this.customerModifier = customerModifier;
        //this.customerDivisionID = customerDivisionID;
        this.customerDivisionName = customerDivisionName;
        //this.customerCreator = user.getUserName();
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

}
