package Model;

import java.sql.Timestamp;

public class Division
{
    private int divisionID;
    private String division;
    private Timestamp createDate;
    private String divisionCreator;
    private Timestamp modifyDate;
    private String divisionModifier;
    private int countryID;

    public Division(String division)
    {
        //this.divisionID = divisionID;
        this.division = division;
        //this.createDate = createDate;
        //this.divisionCreator = divisionCreator;
        //this.modifyDate = modifyDate;
        //this.divisionModifier = divisionModifier;
        //this.countryID = countryID;
    }

    public int getDivisionID()
    {
        return divisionID;
    }

    public void setDivisionID(int divisionID)
    {
        this.divisionID = divisionID;
    }

    public String getDivision()
    {
        return division;
    }

    public void setDivision(String division)
    {
        this.division = division;
    }
}
