package Credentials;

/** Credentials Class*/
public class Credentials
{
    private static final String username = "b3a0c1309826f0";
    private static final String password = "5b30f68e";
    private static final String usernameTest = "test";
    private static final String passwordTest = "test";

    /** Validates user-input name and password against the list of username and password pairings in this class.
     * @param inputUsername Collected from Username textfield.
     * @param inputPassword Collected from Password textfield.
     * @return Returns true or false depending on whether the input username and password matches with a username and
     * passowrd pairing in this class.
     */
    public static boolean validate(String inputUsername, String inputPassword)
    {
        return (username.equals(inputUsername) && password.equals(inputPassword)) ||
                (usernameTest.equals(inputUsername) && passwordTest.equals(inputPassword));
    }
}
