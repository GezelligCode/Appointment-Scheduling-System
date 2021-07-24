package Model;

import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class Contact
{
    private int contactID;
    private String contactName;
    private String eMail;

    // Constructor for creating new contacts
    public Contact(String contactName, String eMail)
    {
        this.contactName = contactName;
        this.eMail = eMail;
    }

    // Constructor for modifying contacts
    public Contact(int ID, String contactName, String eMail)
    {
        this.contactID = ID;
        this.contactName = contactName;
        this.eMail = eMail;
    }

    public int getContactID()
    {
        return contactID;
    }

    public void setContactID(int contactID)
    {
        this.contactID = contactID;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getContactEmail()
    {
        return eMail;
    }

    public void setContactEmail(String eMail)
    {
        this.eMail = eMail;
    }

    public static boolean validateContact(String name, String eMail)
    {
        String errors = "";

        Pattern validEMail = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)" +
                        "+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        );

        if(name.isEmpty() || name.isBlank())
        {
            errors = errors + " An entry for Name is required.\n";
        }
        if(eMail.isEmpty() || eMail.isBlank())
        {
            errors = errors + " An entry for E-Mail is required.\n";
        }
        if(!validEMail.matcher(eMail).matches())
        {
            errors = errors + " The e-mail entered is not valid.\n";
        }
        if(name.length() > 50)
        {
            errors = errors + " The entry for Name cannot exceed 50 characters.\n";
        }
        if(eMail.length() > 50)
        {
            errors = errors + " The entry for E-Mail cannot exceed 50 characters.\n";
        }

        if(errors.isEmpty())
        {
            return true;
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Consultants");
            alert.setHeaderText("Please check the form for the following error(s):");
            alert.setContentText(errors);
            alert.showAndWait();

            return false;
        }
    }
}
