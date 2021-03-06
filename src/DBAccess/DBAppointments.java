package DBAccess;

import Database.DBConnection;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;

/** DBAppointments Class: Handles all SQL querying for the Appointments table. */
public class DBAppointments
{
    private static ObservableList<Appointment> imminentAppointments = FXCollections.observableArrayList();

    /** Gets all appointments from the database.
     *
     * @return Returns an ObservableList of all Appointments.
     */
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

    /** Gets all appointment timestamps from all appointments.
     *
     * @return returns an ObservableList of Timestamp type from each appointment, without duplicates.
     */
    public static ObservableList<Timestamp> getAllAppointmentTimestamps()
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

    /** Gets all weeks from all appointments.
     *
     * @return Returns an ObservableList of String type reflecting the week for each appointment.
     */
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

    /** Gets all contact IDs from all appointments.
     *
     * @return Returns an ObservableList of Integer type reflecting all contact IDs from all appointments.
     */
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

    /** Gets all contact names from all appointments.
     *
     * @return Returns an ObservableList of String type reflecting all contact names from all appointments.
     */
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
                    String contactName = rs.getString("Contact_ID")+ ": " + rs.getString("Contact_Name");
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

    /** Gets all customer IDs from all appointments.
     *
     * @return returns an ObservableList of Integer type that reflects all customer IDs from all appointments.
     */
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

    /** Gets all customer names from all appointments
     *
     * @return Returns an ObservableList of String type reflecting all customer names from all appointments.
     */
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
                    String customerName = rs.getString("Customer_ID") + ": " + rs.getString("Customer_Name");
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

    /** Gets all types from all appointments.
     *
     * @return Returns an ObservableList of String type reflecting all types from all appointments.
     */
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

    /** Gets all appointments filtered by a selected customer.
     *
     * @param customer A string value selected from the Customer filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected customer.
     */
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
                if(Customer.getCustomerIDByName(customer) == rs.getInt("Customer_ID"))
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

    /** Gets all appointments filtered by a selected contact.
     *
     * @param contact A string value selected from the Consultant filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected contact.
     */
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
                if(Contact.getContactIDByName(contact) == rs.getInt("Contact_ID"))
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

    /** Gets all appointments filtered by a selected type.
     *
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected type.
     */
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

    /** Gets all appointments filtered by a selected customer, contact, and type.
     *
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
    public static ObservableList<Appointment> filterApptsViewByCustomerContactType(String customerName, String contactName,
                                                                                   String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = Customer.getCustomerIDByName(customerName);
        int contact_ID = Contact.getContactIDByName(contactName);

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

    /** Gets all appointments filtered by a selected customer and type.
     *
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
    public static ObservableList<Appointment> filterApptsViewByCustomerAndType(String customerName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = Customer.getCustomerIDByName(customerName);

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

    /** Gets all appointments filtered by a selected contact and type.
     *
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the input filters.
     */
    public static ObservableList<Appointment> filterApptsViewByContactAndType(String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int contact_ID = Contact.getContactIDByName(contactName);

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

    /** Gets all appointments filtered by a selected month, customer, and type.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
    public static ObservableList<Appointment> filterApptsViewByMonthCustomerType(String month, String customerName,
                                                                                 String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = Customer.getCustomerIDByName(customerName);

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

    /** Gets all appointments filtered by a selected month, contact, and type.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
    public static ObservableList<Appointment> filterApptsViewByMonthContactType(String month, String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int contact_ID = Contact.getContactIDByName(contactName);

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

    /** Gets all appointments filtered by a selected month and type.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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

    /** Gets all appointments filtered by a selected week and type.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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

    /** Gets all appointments filtered by a selected week, customer, and type.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
    public static ObservableList<Appointment> filterApptsViewByWeekCustomerType(String week, String customerName,
                                                                                String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = Customer.getCustomerIDByName(customerName);

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

    /** Gets all appointments filtered by a selected week, contact, and type.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
    public static ObservableList<Appointment> filterApptsViewByWeekContactType(String week, String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int contact_ID = Contact.getContactIDByName(contactName);

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
                       contact_ID == rs.getInt("Contact_ID"))
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

    /** Gets all appointments filtered by a selected customer and contact.
     *
     * @param customer A string value selected from the Customer filter in the main Appointments screen.
     * @param contact A string value selected from the Contact filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                if(Customer.getCustomerIDByName(customer) == rs.getInt("Customer_ID") &&
                        Contact.getContactIDByName(contact) == rs.getInt("Contact_ID"))
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

    /** Gets all appointments filtered by a selected month, customer, contact, and type.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
    public static ObservableList<Appointment> filterApptsViewByMonthCustomerContactType(String month, String customerName,
                                                                                    String contactName, String typeName)
    {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        int customer_ID = Customer.getCustomerIDByName(customerName);
        int contact_ID = Contact.getContactIDByName(contactName);

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

    /** Gets all appointments filtered by a selected month, customer, and contact.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                        Customer.getCustomerIDByName(customerName) == rs.getInt("Customer_ID") &&
                        Contact.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
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

    /** Gets all appointments filtered by a selected month and customer.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                        Customer.getCustomerIDByName(customerName) == rs.getInt("Customer_ID"))
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

    /** Gets all appointments filtered by a selected month and contact.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                        Contact.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
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

    /** Gets all appointments filtered by a selected month.
     *
     * @param month A string value selected from the Month filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected month.
     */
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

    /** Gets all appointments filtered by a selected week.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected week.
     */
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

    /** Gets all appointments filtered by a selected week, customer, contact, and type.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @param typeName A string value selected from the Type filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                        Customer.getCustomerIDByName(customerName) == rs.getInt("Customer_ID") &&
                        Contact.getContactIDByName(contactName) == rs.getInt("Contact_ID") &&
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

    /** Gets all appointments filtered by a selected week, customer, and contact.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                        Customer.getCustomerIDByName(customerName) == rs.getInt("Customer_ID") &&
                        Contact.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
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

    /** Gets all appointments filtered by a selected week and customer.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @param customerName A string value selected from the Customer filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                        Customer.getCustomerIDByName(customerName) == rs.getInt("Customer_ID"))
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

    /** Gets all appointments filtered by a selected week and contact.
     *
     * @param week A string value selected from the Week filter in the main Appointments screen.
     * @param contactName A string value selected from the Contact filter in the main Appointments screen.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the selected filters.
     */
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
                        Contact.getContactIDByName(contactName) == rs.getInt("Contact_ID"))
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

    /** Gets all appointments for a given customer.
     *
     * @param customer A Customer object passed by the calling function.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the given customer.
     */
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

    /** Gets all appointments for a given contact.
     *
     * @param contact A Contact object passed by the calling function.
     * @return Returns an ObservableList of Appointment type that reflects all appointments for the given contact.
     */
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

    /** Adds a new appointment to the database.
     *
     * @param appt An appointment object instantiated from the AddAppointments_Controller.
     * @param tsStart A timestamp object reflecting the appointment's start time, collected from the user-input date and time fields.
     * @param tsEnd A timestamp object reflecting the appointment's end time, collected from the user-input date and time fields.
     */
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

    /** Updates an appointment in the database.
     *
     * @param appt An appointment object instantiated from the ModifyAppointments_Controller.
     */
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

    /** Removes an appointment from the database.
     *
     * @param appt An appointment object instantiated from the Appointments_Controller.
     */
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

    /** Checks for and counts all appointments whose start time occurs within the next 15 minutes of user's login.
     * In addition, any appointments counted will also be added to the 'imminentAppointments' ObservableList.
     *
     * @return Returns an integer value corresponding to the count of appointments beginning within 15 minutes of user's login.
     */
    public static int checkImminentAppointments()
    {
        int apptCounter = 0;

        try
        {
            String sql = "SELECT Appointment_ID, Start, date(start), time(start) FROM appointments WHERE Start >= UTC_TIME()";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                if(rs.getTimestamp("Start").before(Timestamp.valueOf(LocalDateTime.now().plusMinutes(15))))
                {
                    Appointment imminentAppointment = new Appointment(rs.getInt("Appointment_ID"),
                            rs.getDate("date(start)"), rs.getTime("time(start)"));
                    apptCounter++;
                    imminentAppointments.add(imminentAppointment);
                }
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return apptCounter;
    }

    /** Gets the list of all appointments occuring within 15 minutes of user's login.
     *
     * @return Returns an ObservableList of Appointment type, reflecting all appointments occuring within 15 minutes of user's login.
     */
    public static ObservableList<Appointment> getImminentAppts()
    {
        return imminentAppointments;
    }

    /** Checks whether an added or modified appointment's timeframe conflicts with office hours in EST.
     *
     * @param appt An appointment object instantiated from either the AddAppointments_Controller or ModifyAppointments_Controller.
     * @return Returns true if an added or modified appointment's timeframe does not occur outside of 8am to 10pm EST; else returns false.
     */
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

    /** Checks whether an added or modified appointment's timeframe conflicts with an appointment also belonging to either the contact or customer.
     *
     * @param appt An appointment object instantiated from either the AddAppointments_Controller or ModifyAppointments_Controller.
     * @return Returns true if an added or modified appointment's timeframe conflicts with a pre-existing appointment for the same contact or customer.
     */
    public static boolean validateApptOverlap(Appointment appt)
    {
        String overlappingAppts = new String();

        boolean apptsOverlap = false;

        for(Appointment a : DBAppointments.getAllAppointments())
        {
            apptsOverlap =
                    (appt.getStart().after(a.getStart()) || appt.getStart().equals(a.getStart())) &&
                            (appt.getStart().before(a.getEnd()) || appt.getStart().equals(a.getEnd())) ||
                            (appt.getEnd().before(a.getEnd()) || appt.getEnd().equals(a.getEnd())) &&
                                    (appt.getEnd().after(a.getStart()) || appt.getEnd().equals(a.getStart())) ||
                            (a.getStart().after(appt.getStart()) && a.getEnd().before(appt.getEnd()));

            if(a.getApptID() != appt.getApptID())
            {
                // If selected appt starts within another appt
                if(apptsOverlap)
                {
                    if(appt.getContactID() == a.getContactID() && appt.getCustomerID() == a.getCustomerID())
                    {
                        if(overlappingAppts.isEmpty())
                        {
                            overlappingAppts = "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                    a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n"+
                            "Choose a different appointment time for " + DBCustomers.getCustomerNameByID(a.getCustomerID()) + ".\n";
                        }
                        else
                        {
                            overlappingAppts = overlappingAppts + "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                    a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n"+
                                    "Choose a different appointment time for " + DBCustomers.getCustomerNameByID(a.getCustomerID()) + ".\n";
                        }
                    }
                    else if(appt.getContactID() == a.getContactID())
                    {
                        if(overlappingAppts.isEmpty())
                        {
                            overlappingAppts = "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                    a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n"+
                                    "Choose a different appointment time for " + DBContacts.getContactNameByID(a.getContactID()) + ".\n"+
                            "Alternatively, you may select a different consultant for this appointment with \n" +
                            DBCustomers.getCustomerNameByID(appt.getCustomerID())+".\n";
                        }
                        else
                        {
                            overlappingAppts = overlappingAppts + "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                    a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n"+
                                    "Choose a different appointment time for " + DBCustomers.getCustomerNameByID(a.getCustomerID()) + ".\n"+
                                    "Alternatively, you may select a different consultant for this appointment with \n" +
                                    DBCustomers.getCustomerNameByID(appt.getCustomerID()) +".\n";
                        }
                    }
                    else if(appt.getCustomerID() == a.getCustomerID())
                    {
                        if(overlappingAppts.isEmpty())
                        {
                            overlappingAppts = "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                    a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n"+
                                    "Choose a different appointment time for " + DBCustomers.getCustomerNameByID(a.getCustomerID()) + ".\n";
                        }
                        else
                        {
                            overlappingAppts = overlappingAppts + "Appointment " + a.getApptID() + ": " + a.getTitle() + " starting at " +
                                    a.getStart() + "\n" + " and ending at " + a.getEnd() + " overlaps with this appointment.\n"+
                                    "Choose a different appointment time for " + DBCustomers.getCustomerNameByID(a.getCustomerID()) + ".\n";
                        }
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

    /** Gets the list of all contacts ranked by their appointment counts
     *
     * @return Returns an ObservableList of String type reflecting all contacts, ordered by their appointment counts.
     */
    public static ObservableList<String> rankContactsByApptCount()
    {
        String contactWithCountofAppts = "";
        ObservableList<String> contactsRankedByApptCount = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT Contact_ID, COUNT(*) as Count from appointments GROUP BY Contact_ID ORDER BY Count DESC";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                contactWithCountofAppts = DBContacts.getContactNameByID(rs.getInt("Contact_ID")) + " - "
                        + Integer.parseInt(rs.getString("Count"));
                contactsRankedByApptCount.add(contactWithCountofAppts);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return contactsRankedByApptCount;
    }

    /** Gets the list of all contacts ranked by total appointment time
     *
     * @return Returns an ObservableList of String type reflecting all contacts, ordered by their total appointment time.
     */
    public static ObservableList<String> rankContactsByApptTime()
    {
        String contactsRanked = "";
        ObservableList<String> contactsRankedByTime = FXCollections.observableArrayList();
        double totalHours = 0;
        try
        {
            String sql = "SELECT Contact_ID, timestampdiff(SECOND, Start, End) as timeDiff from appointments ORDER BY timeDiff desc";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                totalHours = Double.parseDouble(rs.getString("timeDiff"));
                //Convert Seconds to Hours
                BigDecimal bd = new BigDecimal(totalHours/3600).setScale(2, RoundingMode.HALF_UP);
                String totalHrsString = String.valueOf(bd);
                contactsRanked = DBContacts.getContactNameByID(rs.getInt("Contact_ID")) + " - " + totalHrsString;
                contactsRankedByTime.add(contactsRanked);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return contactsRankedByTime;
    }

    /** Gets the list of all divisions ranked by customer count
     *
     * @return Returns an ObservableList of String type reflecting all contacts, ordered by their total customer counts.
     */
    public static ObservableList<String> rankDivisionsByCustomerCount()
    {
        String divisionsRanked = "";
        ObservableList<String> divisionsRankedByCustomerCount = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT Division, COUNT(*) AS Count from first_level_divisions INNER JOIN customers ON " +
                    "first_level_divisions.Division_ID = customers.Division_ID GROUP BY Division ORDER BY Count DESC";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                divisionsRanked = rs.getString("Division") + " - "
                        + Integer.parseInt(rs.getString("Count"));
                divisionsRankedByCustomerCount.add(divisionsRanked);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return divisionsRankedByCustomerCount;
    }

    /** Gets the list of all types ranked by appointment count
     *
     * @return Returns an ObservableList of String type reflecting all types, ordered by their appointment count.
     */
    public static ObservableList<String> rankTypesByApptCount()
    {
        String typesRanked = "";
        ObservableList<String> typesRankedByCustomerCount = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT Type, COUNT(*) as Count from appointments GROUP BY Type ORDER BY Count DESC";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                typesRanked = rs.getString("Type") + " - "
                        + Integer.parseInt(rs.getString("Count"));
                typesRankedByCustomerCount.add(typesRanked);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return typesRankedByCustomerCount;
    }


}
