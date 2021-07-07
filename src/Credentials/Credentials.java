package Credentials;

public class Credentials
{
    private static final String username = "U081Ko";
    private static final String password = "53689194407";

    public static boolean validate(String inputUsername, String inputPassword)
    {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }


}
