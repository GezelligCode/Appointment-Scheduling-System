package DBAccess;

import Database.DBConnection;
import Model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts
{
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
