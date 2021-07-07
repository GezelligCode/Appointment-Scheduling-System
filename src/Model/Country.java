package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Country
{
    private int country_ID;

    private String country;

    //private ObservableList<Countries> countryList = FXCollections.observableArrayList();

    //Class Constructor needed
   public Country(int country_ID, String country)
    {
        this.country_ID = country_ID;
        this.country = country;
    }

    public void setCountry_ID(int ID)
    {
        this.country_ID = ID;
    }

    public int getCountry_ID()
    {
        return this.country_ID;
    }

    public void setCountry(String name)
    {
        this.country = name;
    }

    public String getCountry()
    {
        return this.country;
    }

}
