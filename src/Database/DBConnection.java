package Database;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** DBConnection Class*/
public class DBConnection
{
    // JDBC URL parse
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ081Ko";

    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;

    // Driver interface reference
    private static final String mySQLJDBCDriver = "com.mysql.cj.jdbc.Driver";

    // Driver connection reference
    private static Connection conn = null;

    // Username
    private static final String username = "U081Ko";

    // Password
    private static final String password = "53689194407";

    /** Establishes a connection with the database.
     *
     * @return Returns a Connection object that will be either null or a valid Database Connection.
     */
    public static Connection startConnection()
    {
        try
        {
            Class.forName(mySQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(CommunicationsException ce)
        {
            ce.printStackTrace();

            Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
            alert.setTitle("Database Connection");
            alert.setHeaderText("Connection Timed Out");
            alert.setContentText("The database connection has timed out due to idle activity.\n"+
                    "To resume the program, please exit and log back in.");
            alert.showAndWait();

            if(alert.getResult() == ButtonType.OK)
            {
                System.exit(0);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return conn;
    }

    /** A getter for the current connection object
     *
     * @return Returns the current connection and by extension its current state, i.e. null or valid.
     */
    public static Connection getConnection()
    {
        return conn;
    }

    /** Closes the database connection after the program closes.
     */
    public static void closedConnection()
    {
        try
        {
            conn.close();
            System.out.println("Connection closed");
        }
        catch (SQLException e)
        {
            // Do nothing (sometimes there'll be a race condition that creates this exception, but
            // at this point during runtime we already want the program to close, so we need not take action
        }
    }

}
