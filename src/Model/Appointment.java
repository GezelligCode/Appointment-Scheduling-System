package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

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
    private Date date;
    private Time time;

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

    // Constructor for holding imminent appointments
    public Appointment(int apptID, Date date, Time time)
    {
        this.apptID = apptID;
        this.date = date;
        this.time = time;
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

    public Date getApptDate()
    {
        return this.date;
    }

    public Time getApptTime()
    {
        return this.time;
    }

    public static boolean validateAppt(ComboBox customer, TextField title, TextField description, TextField location,
                                TextField type, ComboBox contact, Timestamp start, Timestamp end)
    {
        String errors = "";

        if (customer.getValue() == null)
        {
            errors = errors + " A customer selection is required.\n";
        }
        if (title.getText().isEmpty())
        {
            errors = errors + " An entry for Title is required.\n";
        }
        if(title.getText().length() > 50)
        {
            errors = errors + " The entry for Title cannot exceed 50 characters.\n";
        }
        if (description.getText().isEmpty())
        {
            errors = errors + " An entry for Description is required.\n";
        }
        if(description.getText().length() > 50)
        {
            errors = errors + " The entry for Description cannot exceed 50 characters.\n";
        }
        if (location.getText().isEmpty())
        {
            errors = errors + " An entry for Location is required.\n";
        }
        if(location.getText().length() > 50)
        {
            errors = errors + " The entry for Location cannot exceed 50 characters.\n";
        }
        if (type.getText().isEmpty())
        {
            errors = errors + " An entry for Type is required.\n";
        }
        if(type.getText().length() > 50)
        {
            errors = errors + " The entry for Type cannot exceed 50 characters.\n";
        }
        if (contact.getValue() == null)
        {
            errors = errors + " A Contact selection is required.\n";
        }
        if(end.before(start) || start.equals(end))
        {
            errors = errors + " The appointment end time must be after the start time.\n";
        }
        if(end.before(Timestamp.from(Instant.now())) || start.before(Timestamp.from(Instant.now())))
        {
            errors = errors + " The appointment time must be set in the future.\n";
        }

        if(errors.isEmpty())
        {
            return true;
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointments");
            alert.setHeaderText("Please check the form for the following error(s):");
            alert.setContentText(errors);
            alert.showAndWait();
            return false;
        }
    }

}
