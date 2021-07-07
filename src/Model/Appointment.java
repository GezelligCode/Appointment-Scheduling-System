package Model;

import java.sql.Time;
import java.sql.Timestamp;

public class Appointment
{
    private int apptID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String creatorName;
    private Timestamp modifiedDate;
    private String modifierName;
    private int customerID;
    private int userID;
    private int contactID;

    public Appointment(int apptID, String title, String description, String location, String type, Timestamp start,
                       Timestamp end, int customerID, int userID, int contactID)
    {
        this.apptID = apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }


    public int getApptID()
    {
        return this.apptID;
    }

    public void setApptID(int apptID)
    {
        this.apptID = apptID;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLocation() { return this.location; }

    public void setLocation(String location) { this.location = location; }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Timestamp getStart()
    {
        return this.start;
    }

    public void setStart(Timestamp start)
    {
        this.start = start;
    }

    public Timestamp getEnd()
    {
        return this.end;
    }

    public void setEnd(Timestamp end)
    {
        this.end = end;
    }

    public int getCustomerID()
    {
        return this.customerID;
    }

    public void setCustomerID(int ID)
    {
        this.customerID = ID;
    }

    public int getUserID()
    {
        return this.userID;
    }

    public void setUserID(int ID)
    {
        this.userID = ID;
    }

    public int getContactID()
    {
        return this.contactID;
    }

    public void setContactID(int ID)
    {
        this.contactID = ID;
    }


}
