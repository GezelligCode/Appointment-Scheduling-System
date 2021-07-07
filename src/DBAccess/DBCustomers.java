package DBAccess;

import Database.DBConnection;
import Model.Customer;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBCustomers
{
    public static ObservableList<Customer> getAllCustomers()
    {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from customers";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerPostalcode = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");
                Timestamp customerCreateDate = rs.getTimestamp("Create_Date");
                String customerCreator = rs.getString("Created_By");
                Timestamp customerModifyDate = rs.getTimestamp("Last_Update");
                String customerModifier = rs.getString("Last_Updated_By");
                int customerDivisionID = rs.getInt("Division_ID");

                String customerDivisionName = DBDivisions.getDivisionNameByID(customerDivisionID);

                Customer customer = new Customer(customerID, customerName, customerAddress, customerPostalcode, customerPhone,
                        customerCreator, customerDivisionName);
                customerList.add(customer);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return customerList;
    }

    public static void addCustomer(Customer customer, User user)
    {
        // Removed TimeStamp from parameters. Not necessary (so far as I can tell).

        int Customer_ID = 0;
        String Customer_Name = customer.getCustomerName();
        String Address = customer.getCustomerAddress();
        String Postal_Code = customer.getCustomerPostalCode();
        String Phone = customer.getCustomerPhone();
        String Created_By = user.getUserName();
        int Division = customer.getCustomerDivisionID();

        String addCustomer = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone," +
                "Created_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try
        {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(addCustomer);
            ps.setInt(1, Customer_ID);
            ps.setString(2, Customer_Name);
            ps.setString(3, Address);
            ps.setString(4, Postal_Code);
            ps.setString(5, Phone);
            ps.setString(6, Created_By);
            ps.setInt(7, Division);

            ps.executeUpdate();

        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

    }

    public static boolean validateCustomerRemoval(Customer customer)
    {
        int customerID = customer.getCustomerID();

        try
        {
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, customerID);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();

            return false;
        }
    }

    public static boolean removeCustomer(Customer customer)
    {
        int customerID = customer.getCustomerID();

        try
        {
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, customerID);

            ps.executeUpdate();

            return true;

        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();

            return false;
        }
    }

    public static boolean updateCustomer(Customer customer)
    {
        int customerID = customer.getCustomerID();
        String customerName = customer.getCustomerName();
        String customerAddress = customer.getCustomerAddress();
        String customerPostalCode = customer.getCustomerPostalCode();
        String customerPhone = customer.getCustomerPhone();
        int customerDivisionID = customer.getCustomerDivisionID();
        String customerModifier = customer.getCustomerModifier();

        try
        {
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, "+
                    "Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhone);
            ps.setString(5, customerModifier);
            ps.setInt(6, customerDivisionID);
            ps.setInt(7, customerID);

            ps.executeUpdate();

            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();

            return false;
        }
    }

    public static int getCustomerIDByName(String name)
    {
        int customerID = 0;

        try
        {
            String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                customerID = rs.getInt("Customer_ID");
            }
            else
            {
                System.out.println("No customerID by that name is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return customerID;
    }

    public static String getCustomerNameByID(int ID)
    {
        String customerName = null;

        try
        {
            String sql = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                customerName = rs.getString("Customer_Name");
            }
            else
            {
                System.out.println("No customer name by that ID is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return customerName;
    }
}
