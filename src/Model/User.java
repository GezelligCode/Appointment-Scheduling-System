package Model;

import java.sql.Timestamp;

/** User Class: Handles all manipulation methods for User objects. */
public class User
{
    private int userID;
    private String userName;
    private String userPassword;
    private Timestamp userCreateDate;
    private String userCreator;
    private Timestamp userModifyDate;
    private String userModifier;

    /** Constructor for creating users. */
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

    /** Constructor for initial login. */
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

}
