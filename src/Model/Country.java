package Model;

/** Country Class: Handles the object manipulation methods for all Country objects. */
public class Country
{
    private int country_ID;

    private String country;

    /** Constructor for instantiating countries. */
   public Country(int country_ID, String country)
    {
        this.country_ID = country_ID;
        this.country = country;
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
