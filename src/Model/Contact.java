package Model;

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
}
