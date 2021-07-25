package DBAccess;

import Database.DBConnection;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DBContacts Class: Handles all SQL querying for the Contacts table. */
public class DBContacts
{
    /** Gets all contacts from the database
     *
     * @return Returns an observable list of Contact type, reflecting all contacts.
     */
    public static ObservableList<Contact> getAllContacts()
    {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from contacts";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int ID = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                Contact contact = new Contact(ID, name, email);

                contactList.add(contact);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return contactList;
    }

    /** Gets all contact names from all contacts.
     *
     * @return Returns an ObservableList of String type, reflecting all contact names.
     */
    public static ObservableList<String> getAllContactNames()
    {
        ObservableList<String> contactNameList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from contacts";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                String name = rs.getString("Contact_Name");

                contactNameList.add(name);
            }
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return contactNameList;
    }

    /** Adds a new contact to the database.
     *
     * @param contact A contact object passed by the calling function in the AddContacts_Controller.
     */
    public static void addContact(Contact contact)
    {
        int contactID = 0;
        String contactName = contact.getContactName();
        String contactEmail = contact.getContactEmail();

        String sql = "INSERT INTO contacts(Contact_ID, Contact_Name, Email) VALUES (?, ?, ?)";

        try
        {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, contactID);
            ps.setString(2, contactName);
            ps.setString(3, contactEmail);

            ps.executeUpdate();
        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    /** Updates an existing contact in the database.
     *
     * @param contact A contact object passed by the calling function in the ModifyContacts_Controller.
     * @return Returns true if the update to the database is successful; else returns false.
     */
    public static boolean updateContact(Contact contact)
    {
        int contactID = contact.getContactID();
        String contactName = contact.getContactName();
        String contactEmail = contact.getContactEmail();

        try
        {
            String sql = "UPDATE contacts SET Contact_Name = ?, Email = ? WHERE Contact_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, contactName);
            ps.setString(2, contactEmail);
            ps.setInt(3, contactID);

            ps.executeUpdate();

            return true;
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();

            return false;
        }
    }

    /** Checks if a contact selected for reomval has an associated appointment.
     *
     * @param contact A Contact object passed by the calling function in the Contacts_Controller.
     * @return Returns true if the selected contact does not have any existing appointments associated with their contact ID; else returns false.
     */
    public static boolean validateContactRemoval(Contact contact)
    {
        int contactID = contact.getContactID();

        try
        {
            String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, contactID);

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

    /** Removes a contact from the database.
     *
     * @param contact A Contact object passed by the calling function in the Contacts_Controller.
     * @return Returns true if deletion from the database is successful; else returns false.
     */
    public static boolean removeContact(Contact contact)
    {
        int contactID = contact.getContactID();

        try
        {
            String sql = "DELETE FROM contacts WHERE Contact_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, contactID);

            ps.executeUpdate();

            return true;

        }
        catch(SQLException throwables)
        {
            throwables.printStackTrace();

            return false;
        }
    }

    /** Gets the contact ID corresponding to a given contact name.
     *
     * @param contactName A String object passed by a calling function, that represents the name of the contact.
     * @return Returns an integer value corresponding to the ID of the given contact.
     */
    public static int getContactIDByName(String contactName)
    {
        int contactID = 0;

        try
        {
            String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, contactName);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                contactID = rs.getInt("Contact_ID");
            }
            else
            {
                System.out.println("No contactID by that name is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return contactID;
    }

    /** Gets the contact name for a given contact ID.
     *
     * @param ID An integer value passed by a calling function, that corresponds to the contact ID.
     * @return Returns a String value representing the contact name for the given ID.
     */
    public static String getContactNameByID(int ID)
    {
        String contactName = null;

        try
        {
            String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                contactName = rs.getString("Contact_Name");
            }
            else
            {
                System.out.println("No contact name by that ID is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return contactName;
    }

    /** Gets the contact e-mail for a given contact ID.
     *
     * @param ID An integer value passed by a calling function, that represents the ID of the given contact.
     * @return Returns a String value representing the e-mail of the given contact.
     */
    public static String getContactEmailByID(int ID)
    {
        String contactEmail = null;

        try
        {
            String sql = "SELECT Email FROM contacts WHERE Contact_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                contactEmail = rs.getString("Email");
            }
            else
            {
                System.out.println("No contact e-mail by that ID is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return contactEmail;
    }
}
