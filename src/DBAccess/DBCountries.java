package DBAccess;

import Database.DBConnection;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** DBCountries Class: Handles all SQL querying for the Countries table. */
public class DBCountries
{
    /** Gets all countries from the database that have company offices.
     *
     * @return Returns an ObservableList of Country type, reflecting all applicable countries in the database.
     */
    public static ObservableList<Country> getAllApplicableCountries()
    {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * FROM countries";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country ctry = new Country(countryID, countryName);
                countryList.add(ctry);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return countryList;
    }

    /** Gets the country ID for a given country name.
     *
     * @param name A String value passed by the calling function, which reflects a country name.
     * @return Returns an integer value representing the country ID for the given country name.
     */
    public static int getCountryIDByName(String name)
    {
        int countryID = 0;

        try
        {
            String sql = "SELECT * from countries WHERE country = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                countryID = rs.getInt("Country_ID");
            }
            else
            {
                System.out.println("No country ID by that name is found.");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return countryID;
    }

    /** Gets the country name corresponding to a given country ID.
     *
     * @param ID An integer value passed by a calling function, that represents the country ID.
     * @return Returns a String value that represents the country name associated with the given country ID.
     */
    public static String getCountryNameByID(int ID)
    {
        String countryName = null;

        try
        {
            String sql = "SELECT * from countries WHERE COUNTRY_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                countryName = rs.getString("Country");
            }
            else
            {
                System.out.println("No country name by that ID is found.");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return countryName;
    }
}
