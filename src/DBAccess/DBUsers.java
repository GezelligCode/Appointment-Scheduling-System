package DBAccess;

import Database.DBConnection;
import Model.User;

import java.sql.*;

public class DBUsers
{
    //Upon login, this class should instantiate a user and add to the DB. The information should come from the login
    //screen.
    // So build out the method required and then call the method in the login controller.
    //boolean added = false;
    private static User currentUser = null;

    public static User loginUser(String userNameInput, String passwordInput)
    {
        try
        {
            String sql = "SELECT users.User_ID, users.User_Name " + "FROM users WHERE users.User_Name = ? " +
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

    public static User getCurrentUser()
    {
        return currentUser;
    }

    public static int getCurrentUserID()
    {
        int userID = 0;

        return userID = getCurrentUser().getUserID();
    }
}
