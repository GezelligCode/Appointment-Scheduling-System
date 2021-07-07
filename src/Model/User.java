package Model;

import java.sql.Time;
import java.sql.Timestamp;

public class User
{
    private int userID;
    private String userName;
    private String userPassword;
    private Timestamp userCreateDate;
    private String userCreator;
    private Timestamp userModifyDate;
    private String userModifier;

    //Constructor
    public User(int userID, String userName, String userPassword, Timestamp userCreateDate, String userCreator,
                Timestamp userModifyDate, String userModifier)
    {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userCreateDate = userCreateDate;
        this.userCreator = userCreator;
        this.userModifyDate = userModifyDate;
        this.userModifier = userModifier;
    }

    // Constructor for initial login
    public User(int userID, String userName, String userPassword)
    {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }


    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }

    public Timestamp getUserCreateDate()
    {
        return userCreateDate;
    }

    public void setUserCreateDate(Timestamp userCreateDate)
    {
        this.userCreateDate = userCreateDate;
    }

    public String getUserCreator()
    {
        return userCreator;
    }

    public void setUserCreator(String userCreator)
    {
        this.userCreator = userCreator;
    }

    public Timestamp getUserModifyDate()
    {
        return userModifyDate;
    }

    public void setUserModifyDate(Timestamp userModifyDate)
    {
        this.userModifyDate = userModifyDate;
    }

    public String getUserModifier()
    {
        return userModifier;
    }

    public void setUserModifier(String userModifier)
    {
        this.userModifier = userModifier;
    }
}
