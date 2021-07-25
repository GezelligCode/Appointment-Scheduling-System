package DBAccess;

import Database.DBConnection;
import Model.User;
import java.sql.*;

/** DBUsers: Handles all SQL querying for the Users table. */
public class DBUsers
{
    private static User currentUser = null;

    /** Logs in the user, if credential check returns valid. If valid, then checks if the user is pre-existing in the database;
     * if not, then adds the user to the database.
     *
     * @param userNameInput A String value input by user in the Login screen.
     * @param passwordInput A String value input by user in the Login screen.
     * @return Returns a User object that reflects the currently-signed in user.
     */
    public static User loginUser(String userNameInput, String passwordInput)
    {
        try
        {
            String sql = "SELECT users.User_ID, users.User_Name FROM users WHERE users.User_Name = ? " +
                    "AND users.Password = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, userNameInput);

            ps.setString(2, passwordInput);

            ResultSet rs = ps.executeQuery();

            int userID = 0;
            String userName = "null";

            if(rs.next())
            {
                ResultSetMetaData rsMetaData = rs.getMetaData();
                for(int i = 1; i <=rsMetaData.getColumnCount(); i++)
                {
                    String colValue = rs.getString(i);
                    switch (rsMetaData.getColumnName(i))
                    {
                        case "User_ID":
                            userID = rs.getInt(i);
                            break;
                        case "User_Name":
                            userName = colValue;
                            break;
                    }
                }

                currentUser = new User(userID, userName, passwordInput);
            }
            else
            {
                currentUser = new User(userID, userNameInput, passwordInput);

                String updateUserTable = "INSERT INTO users(User_ID, User_Name, Password) VALUES (?, ?, ?)";

                PreparedStatement createUser = DBConnection.getConnection().prepareStatement(updateUserTable);
                createUser.setInt(1, currentUser.getUserID());
                createUser.setString(2, currentUser.getUserName());
                createUser.setString(3, currentUser.getUserPassword());

                createUser.executeUpdate();

            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return currentUser;
    }

    /** Gets the currently-logged-in user.
     *
     * @return Returns a User object representing the current user.
     */
    public static User getCurrentUser()
    {
        return currentUser;
    }

    /** Gets the ID of the current user.
     *
     * @return Returns an integer value corresponding to the ID of the current user.
     */
    public static int getCurrentUserID()
    {
        int userID = 0;

        return userID = getCurrentUser().getUserID();
    }
}
