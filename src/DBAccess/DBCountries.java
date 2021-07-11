package DBAccess;

import Database.DBConnection;
import Model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBCountries
{
    public static ObservableList<Country> getAllCountries()
    {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from countries";

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

    public static void checkDateConversion()
    {
        System.out.println("CREATE DATE TEST");
        String sql = "select Create_Date from countries";
        try
        {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Timestamp ts = rs.getTimestamp("Create_Date");
                System.out.println("CD: " + ts.toLocalDateTime().toString());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
