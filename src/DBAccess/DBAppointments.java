package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

public class DBAppointments
{
    private static ObservableList<Appointment> imminentAppointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAllAppointments()
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);
                appointmentList.add(appt);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Timestamp> getAllAppointmentMonths()
    {
        ObservableList<Timestamp> appointmentMonthList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                Timestamp start = rs.getTimestamp("Start");
                appointmentMonthList.add(start);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentMonthList;
    }

    public static ObservableList<String> getAllAppointmentWeeks()
    {
        ObservableList<String> appointmentWeekList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                String startWeekDay = rs.getString("weekStartDate");
                appointmentWeekList.add(startWeekDay);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentWeekList;
    }

    public static ObservableList<Integer> getAllAppointmentContactIDs()
    {
        ObservableList<Integer> appointmentContactIDList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int contactID = rs.getInt("Contact_ID");
                appointmentContactIDList.add(contactID);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentContactIDList;
    }

    public static ObservableList<String> getAllAppointmentContactNames()
    {
        ObservableList<String> appointmentContactNames = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM contacts";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(getAllAppointmentContactIDs().contains(rs.getInt("Contact_ID")))
                {
                    String contactName = rs.getString("Contact_Name");
                    appointmentContactNames.add(contactName);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentContactNames;
    }

    public static ObservableList<Integer> getAllAppointmentCustomerIDs()
    {
        ObservableList<Integer> appointmentCustomerIDList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int customerID = rs.getInt("Customer_ID");
                appointmentCustomerIDList.add(customerID);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentCustomerIDList;
    }

    public static ObservableList<String> getAllAppointmentCustomerNames()
    {
        ObservableList<String> appointmentCustomerNames = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM customers";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(getAllAppointmentCustomerIDs().contains(rs.getInt("Customer_ID")))
                {
                    String customerName = rs.getString("Customer_Name");
                    appointmentCustomerNames.add(customerName);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentCustomerNames;
    }

    public static ObservableList<String> getAllAppointmentTypes()
    {
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(getAllAppointmentCustomerIDs().contains(rs.getInt("Customer_ID")))
                {
                    String typeName = rs.getString("Type");
                    appointmentTypes.add(typeName);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentTypes;
    }

    public static ObservableList<Appointment> filterApptsViewByCustomer(String customer)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(DBCustomers.getCustomerIDByName(customer) == rs.getInt("Customer_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByContact(String contact)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(DBContacts.getContactIDByName(contact) == rs.getInt("Contact_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByType(String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments WHERE type = ?";



            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);
                appointmentList.add(appt);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByCustomerContactType(String customerName, String contactName,
                                                                                   String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = DBCustomers.getCustomerIDByName(customerName);
        int contact_ID = DBContacts.getContactIDByName(contactName);

        try
        {
            String sql = "SELECT * from appointments WHERE Customer_ID = ? AND Contact_ID = ? AND Type = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, customer_ID);
            ps.setInt(2, contact_ID);
            ps.setString(3, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);
                appointmentList.add(appt);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByCustomerAndType(String customerName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = DBCustomers.getCustomerIDByName(customerName);

        try
        {
            String sql = "SELECT * from appointments WHERE Customer_ID = ? AND Type = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, customer_ID);
            ps.setString(2, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);
                appointmentList.add(appt);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByContactAndType(String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int contact_ID = DBContacts.getContactIDByName(contactName);

        try
        {
            String sql = "SELECT * from appointments WHERE Contact_ID = ? AND Type = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, contact_ID);
            ps.setString(2, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);
                appointmentList.add(appt);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonthCustomerType(String month, String customerName,
                                                                                 String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = DBCustomers.getCustomerIDByName(customerName);

        try
        {
            String sql = "SELECT * from appointments WHERE Customer_ID = ? AND Type = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, customer_ID);
            ps.setString(2, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonthContactType(String month, String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int contact_ID = DBContacts.getContactIDByName(contactName);

        try
        {
            String sql = "SELECT * from appointments WHERE Contact_ID = ? AND Type = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, contact_ID);
            ps.setString(2, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonthAndType(String month, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments WHERE Type = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeekAndType(String week, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(typeName.equals(rs.getString("Type")) && week.equals(rs.getString("weekStartDate")))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeekCustomerType(String week, String customerName,
                                                                                String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = DBCustomers.getCustomerIDByName(customerName);

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(typeName.equals(rs.getString("Type")) && week.equals(rs.getString("weekStartDate")) &&
                        customer_ID == rs.getInt("Customer_ID"));
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeekContactType(String week, String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int contact_ID = DBContacts.getContactIDByName(contactName);

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(typeName.equals(rs.getString("Type")) && week.equals(rs.getString("weekStartDate")) &&
                       contact_ID == rs.getInt("Contact_ID"));
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByCustomerAndContact(String customer, String contact)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(DBCustomers.getCustomerIDByName(customer) == rs.getInt("Customer_ID") &&
                        DBContacts.getContactIDByName(contact) == rs.getInt("Contact_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonthCustomerContactType(String month, String customerName,
                                                                                    String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = DBCustomers.getCustomerIDByName(customerName);
        int contact_ID = DBContacts.getContactIDByName(contactName);

        try
        {
            String sql = "SELECT * from appointments WHERE Customer_ID = ? AND Contact_ID = ? AND Type = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, customer_ID);
            ps.setInt(2, contact_ID);
            ps.setString(3, typeName);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonthCustomerContact(String month, String customerName,
                                                                                    String contactName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()) &&
                        DBCustomers.getCustomerIDByName(customerName) == rs.getInt("Customer_ID") &&
                        DBContacts.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonthAndCustomer(String month, String customerName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()) &&
                        DBCustomers.getCustomerIDByName(customerName) == rs.getInt("Customer_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonthAndContact(String month, String contactName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()) &&
                        DBContacts.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByMonth(String month)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(month.equals(rs.getTimestamp("Start").toLocalDateTime().getMonth().toString()))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeek(String week)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(week.equals(rs.getString("weekStartDate")))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeekCustomerContactType(String week, String customerName,
                                                                                   String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(week.equals(rs.getString("weekStartDate")) &&
                        DBCustomers.getCustomerIDByName(customerName) == rs.getInt("Customer_ID") &&
                        DBContacts.getContactIDByName(contactName) == rs.getInt("Contact_ID") &&
                        typeName.equals(rs.getString("Type")))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeekCustomerContact(String week, String customerName,
                                                                                   String contactName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(week.equals(rs.getString("weekStartDate")) &&
                        DBCustomers.getCustomerIDByName(customerName) == rs.getInt("Customer_ID") &&
                        DBContacts.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeekAndCustomer(String week, String customerName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(week.equals(rs.getString("weekStartDate")) &&
                        DBCustomers.getCustomerIDByName(customerName) == rs.getInt("Customer_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> filterApptsViewByWeekAndContact(String week, String contactName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try
        {
            String getWeek = "SELECT *, STR_TO_DATE((IF( CAST(WEEK(start,5) AS UNSIGNED) = 0,(CONCAT(CAST((CAST(YEAR(start) " +
                    "AS UNSIGNED) - 1) AS CHAR),'52 Monday')),(CONCAT(CAST(YEAR(start) AS CHAR),IF( CAST(WEEK(start,5) "+
                    "AS UNSIGNED) < 10,'0','' ),CAST(WEEK(start,5) AS CHAR),' Monday')))),'%X%V %W') AS weekStartDate "+
                    " from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(getWeek);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(week.equals(rs.getString("weekStartDate")) &&
                        DBContacts.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
                {
                    int apptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp start = rs.getTimestamp("Start");
                    Timestamp end = rs.getTimestamp("End");
                    int customerID = rs.getInt("Customer_ID");
                    int userID = rs.getInt("User_ID");
                    int contactID = rs.getInt("Contact_ID");
                    Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                            userID, contactID);
                    appointmentList.add(appt);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    public static ObservableList<Appointment> getAllAppointmentsByCustomerID(Customer customer)
    {
        ObservableList<Appointment> associatedApptsList = FXCollections.observableArrayList();

        int ID = customer.getCustomerID();

        try
        {
            String sql = "SELECT * from appointments WHERE Customer_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);
                associatedApptsList.add(appt);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return associatedApptsList;
    }

    public static ObservableList<Appointment> getAllAppointmentsByContactID(Contact contact)
    {
        ObservableList<Appointment> associatedApptsList = FXCollections.observableArrayList();

        int ID = contact.getContactID();

        try
        {
            String sql = "SELECT * from appointments WHERE Contact_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);
                associatedApptsList.add(appt);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return associatedApptsList;
    }

    public static void addAppointment(Appointment appt, Timestamp tsStart, Timestamp tsEnd)
    {
        int apptID = 0;
        int userID = appt.getUserID();
        int customerID = appt.getCustomerID();
        String title = appt.getTitle();
        String description = appt.getDescription();
        String location = appt.getLocation();
        int contactID = appt.getContactID();
        String type = appt.getType();
        Timestamp start = tsStart;
        Timestamp end = tsEnd;

        String creator = DBUsers.getCurrentUser().getUserName();

        try
        {
            String sql = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, " +
                    "Created_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, apptID);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, location);
            ps.setString(5,type);
            ps.setTimestamp(6, tsStart);
            ps.setTimestamp(7, tsEnd);
            ps.setString(8, creator);
            ps.setInt(9, customerID);
            ps.setInt(10, userID);
            ps.setInt(11, contactID);

            ps.executeUpdate();

        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

    }

    public static boolean updateAppt(Appointment appt)
    {
        int apptID = appt.getApptID();
        int userID = appt.getUserID();
        int customerID = appt.getCustomerID();
        String title = appt.getTitle();
        String description = appt.getDescription();
        String location = appt.getLocation();
        int contactID = appt.getContactID();
        String type = appt.getType();
        Timestamp start = appt.getStart();
        Timestamp end = appt.getEnd();

        String modifier = DBUsers.getCurrentUser().getUserName();

        try
        {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, "+
                    "Start = ?, End = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setString(7, modifier);
            ps.setInt(8, customerID);
            ps.setInt(9, userID);
            ps.setInt(10, contactID);
            ps.setInt(11, apptID);

            ps.executeUpdate();

            return true;

        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();

            return false;
        }
    }

    public static boolean removeAppt(Appointment appt)
    {
        int apptID = appt.getApptID();

        try
        {
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, apptID);

            ps.executeUpdate();

            return true;

        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();

            return false;
        }
    }

    public static int checkImminentAppointments()
    {
        int apptCounter = 0;

        try
        {
            String sql = "SELECT Appointment_ID, date(start), time(start) FROM appointments WHERE Start >= UTC_TIME() " +
                    "AND Start <= date_add(UTC_TIMESTAMP(), INTERVAL 15 minute)";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                Appointment imminentAppointment = new Appointment(rs.getInt("Appointment_ID"),
                        rs.getDate("date(start)"), rs.getTime("time(start)"));
                apptCounter++;
                imminentAppointments.add(imminentAppointment);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return apptCounter;
    }

    public static ObservableList getImminentAppts()
    {
        return imminentAppointments;
    }

    public static boolean validateBusinessHours(Appointment appt)
    {
        ZoneId currentZoneId = ZoneId.systemDefault();
        ZoneId estZoneId = ZoneId.of("America/New_York");

        ZonedDateTime zonedStartDateTimeEST = appt.getStart().toInstant().atZone(estZoneId);
        ZonedDateTime zonedEndDateTimeEST = appt.getEnd().toInstant().atZone(estZoneId);
        int apptYear = appt.getStart().toLocalDateTime().getYear();
        int apptMonth = appt.getStart().toLocalDateTime().getMonth().getValue();
        int apptDay = appt.getStart().toLocalDateTime().getDayOfMonth();
        int apptHourAM = 8;
        int apptHourPM = 22;
        int apptMin = 00;
        int apptSec = 00;

        LocalDateTime startTime = LocalDateTime.of(apptYear, apptMonth, apptDay, apptHourAM, apptMin, apptSec);
        LocalDateTime endTime = LocalDateTime.of(apptYear, apptMonth, apptDay, apptHourPM, apptMin, apptSec);

        ChronoZonedDateTime zonedStartTime = ZonedDateTime.of(startTime, estZoneId);
        ChronoZonedDateTime zonedEndTime = ZonedDateTime.of(endTime, estZoneId);

        System.out.println("ZonedStartTime: " + zonedStartTime);
        System.out.println("ZonedEndTime: " + zonedEndTime);

        if(zonedStartDateTimeEST.isBefore(zonedStartTime))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Appointments");
            alert.setHeaderText("Appointment Time is Outside of Office Hours");
            alert.setContentText("Appointment cannot occur before 8:00AM EST. \n Office hours are from 8AM to 10PM EST.");
            alert.showAndWait();
            return false;
        }
        else if(zonedEndDateTimeEST.isAfter(zonedEndTime) || zonedStartDateTimeEST.isAfter(zonedEndTime))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Appointments");
            alert.setHeaderText("Appointment Time is Outside of Office Hours");
            alert.setContentText("Appointment cannot occur after 10:00PM EST. \nOffice hours are from 8AM to 10PM EST.");
            alert.showAndWait();
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean validateApptOverlap(Appointment appt)
    {
        String overlappingAppts = new String();

        for(Appointment a : DBAppointments.getAllAppointments())
        {
            if(a.getApptID() != appt.getApptID())
            {
                // If selected appt starts within another appt
                if((appt.getStart().after(a.getStart()) || appt.getStart().equals(a.getStart())) &&
                        (appt.getStart().before(a.getEnd()) || appt.getStart().equals(a.getEnd())))
                {
                    if(overlappingAppts.isEmpty())
                    {
                        overlappingAppts = "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n";
                    }
                    else
                    {
                        overlappingAppts = overlappingAppts + "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n";
                    }
                }
                // If selected appt ends within another appt
                else if((appt.getEnd().before(a.getEnd()) || appt.getEnd().equals(a.getEnd())) &&
                        (appt.getEnd().after(a.getStart()) || appt.getEnd().equals(a.getStart())))
                {
                    if(overlappingAppts.isEmpty())
                    {
                        overlappingAppts = "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n";
                    }
                    else
                    {
                        overlappingAppts = overlappingAppts + "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n";
                    }
                }
                else if(a.getStart().after(appt.getStart()) && a.getEnd().before(appt.getEnd()))
                {
                    if(overlappingAppts.isEmpty())
                    {
                        overlappingAppts = "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n";
                    }
                    else
                    {
                        overlappingAppts = overlappingAppts + "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n";
                    }
                }
                else
                {
                    continue;
                }
            }
        }
        if(overlappingAppts.isEmpty())
        {
            return true;
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Appointments");
            alert.setHeaderText("Appointment Time Overlaps with Pre-existing Appointment(s)");
            alert.setContentText(overlappingAppts);
            alert.showAndWait();
            return false;
        }
    }
}
