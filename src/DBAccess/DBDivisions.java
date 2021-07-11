package DBAccess;

import Database.DBConnection;
import Model.Country;
import Model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDivisions
{
    public static ObservableList<Division> getAllDivisions()
    {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from first_level_divisions";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                //int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                Division div = new Division(division);
                divisionList.add(div);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return divisionList;
    }

    public static ObservableList<String> getAllDivisionsByCountry(String country)
    {
        ObservableList<String> divisionList = FXCollections.observableArrayList();

        //Get country ID by country name
        int countryID = DBCountries.getCountryIDByName(country);

        try
        {
            String sql = "SELECT * from first_level_divisions WHERE Country_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, countryID);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                String division = rs.getString("Division");
                divisionList.add(division);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return divisionList;
    }

    public static int getDivisionIDByName(String name)
    {
        int divisionID = 0;

        try
        {
            String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                divisionID = rs.getInt("Division_ID");
            }
            else
            {
                System.out.println("No division by that name is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return divisionID;
    }

    public static String getDivisionNameByID(int ID)
    {
        //int divisionID = 0;
        String divisionName = null;

        try
        {
            String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, ID);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                divisionName = rs.getString("Division");
            }
            else
            {
                System.out.println("No division by that ID is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return divisionName;
    }

    public static Integer getCountryIDByDivisionID(int divisionID)
    {
        int countryID = 0;

        try
        {
            String sql = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division_ID = ?";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, divisionID);

            ResultSet rs = ps.executeQuery();

            if(rs.next())
            {
                countryID = rs.getInt("COUNTRY_ID");
            }
            else
            {
                System.out.println("No country ID by that division ID is found");
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return countryID;
    }

}
