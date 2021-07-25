package View_Controller;

import Credentials.Credentials;
import DBAccess.DBAppointments;
import DBAccess.DBCountries;
import DBAccess.DBUsers;
import Database.DBConnection;
import Model.Appointment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** FXML Login_Controller Class: Handles the login of users, logging of login events, and error control messages in the
 * event of failed logins. */
public class Login_Controller implements Initializable
{
    @FXML private TextField userName;
    @FXML private PasswordField userPassword;
    @FXML private Label zoneID;
    @FXML private Label loginHeader;
    @FXML private Label loginLabel;
    @FXML private Label loginUserName;
    @FXML private Label loginPassword;

    private static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT+0");

    /** Sets the initial conditions of the Login scene, e.g. pre-populating the Zone and detected language.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        loginHeader.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("loginHeader"));
        loginLabel.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("loginLabel"));
        loginUserName.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("userName"));
        loginPassword.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("passWord"));
        zoneID();
    }

    /** Gets the zoneID of the user's system. */
    public void zoneID()
    {
        Locale currentLocale = Locale.getDefault();

        String zone = ZoneId.systemDefault().toString();
        zoneID.setText(zone + "\n" + currentLocale.getDisplayLanguage());
    }

    /** Handles the login of the user and error control messages for failed logins.
     *
     * @param event
     * @throws Exception
     */
    public void loginHandler(ActionEvent event) throws Exception
    {
        if(Credentials.validate(userName.getText(), userPassword.getText()))
        {
            DBConnection.startConnection();
            DBUsers.loginUser(userName.getText(), userPassword.getText());

            imminentApptCheck();

            //Update log
            updateLoginActivity(true);

            // redirect to Appts
            Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
            Scene scene = new Scene(addProduct);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            updateLoginActivity(false);
            if(Locale.getDefault().getDisplayLanguage().equals("français"))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("alertTitle"));
                alert.setHeaderText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("headerText"));
                alert.setContentText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("contentText"));
                alert.showAndWait();
            }
            else
            {
                // popup warning
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("alertTitle"));
                alert.setHeaderText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("headerText"));
                alert.setContentText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("contentText"));
                alert.showAndWait();
            }
        }
    }

    /** Gets the UTC equivalent for the local time, incremented by 15 minutes. Used for checking for imminent appointments. */
    public static Date getUTCDateForLocalDate()
    {
        Calendar local = Calendar.getInstance();

        int offset = local.getTimeZone().getOffset(local.getTimeInMillis());

        GregorianCalendar utc = new GregorianCalendar(gmtTimeZone);

        utc.setTimeInMillis(local.getTimeInMillis());
        utc.add(Calendar.MINUTE, 15);
        utc.add(Calendar.MILLISECOND, -offset);

        return utc.getTime();
    }

    /** Generates a message to display whether there are any appointments occuring within 15 minutes of user's login.
     * This is where one of two types of lambda functions in this program are used. */
    public void imminentApptCheck()
    {
        int numberOfAppts = DBAppointments.checkImminentAppointments();

        interface pluralSingular
        {
            String runCheck(String str);
        }

        /** Lambda Function: lambda functions are used here as a way to simplify the various types of outputs that can
         * happen -- depending on 1) Whether there are more than 1 imminent appointments and 2) whether the system's
         * default display language is French or English. -- down to just a couple of variables:
         * is_are or is_are_Fr. */
        pluralSingular plural_en = (s) -> s + " are";
        pluralSingular singular_en = (s) -> s + " is";
        pluralSingular pluralAppts_en = (s) -> s + " appointments in the next 15 minutes.";
        pluralSingular pluralAppts_fr = (s) -> s + " rendez-vous dans les 15 prochaines minutes.";
        pluralSingular singularAppt_en = (s) -> s + " appointment in the next 15 minutes.";
        pluralSingular plural_fr = (s) -> s + " Là sont";
        pluralSingular singular_fr = (s) -> s + " Là est";

        String is_are;
        String is_are_fr;
        String appt_appts;
        String appt_appts_fr = "rendez-vous";

        if (numberOfAppts == 0 || numberOfAppts > 1)
        {
            is_are = plural_en.runCheck("There");
            is_are_fr = plural_fr.runCheck("");
            appt_appts = pluralAppts_en.runCheck(is_are + " " + numberOfAppts);
            appt_appts_fr = pluralAppts_fr.runCheck(is_are_fr + " " + numberOfAppts);
        }
        else
        {
            is_are = singular_en.runCheck("There");
            is_are_fr = singular_fr.runCheck("");
            appt_appts = singularAppt_en.runCheck(is_are + " " + numberOfAppts);
            appt_appts_fr = pluralAppts_fr.runCheck(is_are_fr + " " + numberOfAppts);
        }

        if (Locale.getDefault().getDisplayLanguage().equals("français"))
        {
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setTitle("Rendez-vous");
            alert.setHeaderText("Rendez-vous à venir");
            alert.setContentText(appt_appts_fr + "\n" + imminentAppointmentListing(DBAppointments.getImminentAppts()));
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setTitle("Appointments");
            alert.setHeaderText("Upcoming Appointments");
            alert.setContentText(appt_appts +"\n" + imminentAppointmentListing(DBAppointments.getImminentAppts()));
            alert.showAndWait();
        }
    }

    /** Produces the list of appointments, if any, that occur within 15 minutes of user's login. */
    public String imminentAppointmentListing(ObservableList<Appointment> imminentAppts)
    {
        String imminentApptListing = "";

        for(Appointment appt : imminentAppts)
        {
            if(imminentApptListing.isEmpty())
            {
                imminentApptListing = String.valueOf(appt.getApptID()) + ": " + String.valueOf(appt.getApptDate()) +
                        " " + String.valueOf(appt.getApptTime()) + "\n";
            }
            else
            {
                imminentApptListing = imminentApptListing +
                        String.valueOf(appt.getApptID()) + ": " + String.valueOf(appt.getApptDate()) +
                        " " + String.valueOf(appt.getApptTime()) + "\n";
            }
        }

        return imminentApptListing;
    }

    /** Updates the login_activity.txt file with a description of each successful or failed login. */
    public void updateLoginActivity(Boolean bool) throws Exception
    {
        DateTimeFormatter localLoginTime = DateTimeFormatter.ofPattern("MM/yyyy/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if(bool == true)
        {
            FileOutputStream log = new FileOutputStream("login_activity.txt", true);
            log.write(("Successful Login: \r\n" + "User ID - " + DBUsers.getCurrentUserID() + ", User Name - " +
                    DBUsers.getCurrentUser().getUserName() + "\r\n" + "Timestamp - " +
                    localLoginTime.format(now) + "\r\n").getBytes());
            log.close();
        }
        else
        {
            FileOutputStream log = new FileOutputStream("login_activity.txt", true);
            log.write(("Failed Login: \r\n" + "User Name Entered - " +
                    userName.getText() + "\r\n" + "Timestamp - " +
                    localLoginTime.format(now) + "\r\n").getBytes());
            log.close();
        }
    }
}
