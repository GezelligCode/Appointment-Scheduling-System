package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            //System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            //System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }

    public static Connection getConnection()
    {
        return conn;
    }

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
