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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        loginHeader.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("loginHeader"));
        loginLabel.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("loginLabel"));
        loginUserName.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("userName"));
        loginPassword.setText(ResourceBundle.getBundle("Main/LoginLanguageBundle", Locale.getDefault()).getString("passWord"));
        zoneID();
    }


    public void zoneID()
    {
        Locale currentLocale = Locale.getDefault();

        String zone = ZoneId.systemDefault().toString();
        zoneID.setText(zone + "\n" + currentLocale.getDisplayLanguage());
    }

    public void loginHandler(ActionEvent event) throws Exception
    {
        if(Credentials.validate(userName.getText(), userPassword.getText()))
        {
            DBConnection.startConnection();
            DBCountries.checkDateConversion();
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

    public void imminentApptCheck() {
        //Get time now in UTC
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Timestamp utcTimestamp = Timestamp.valueOf(formatter.format(getUTCDateForLocalDate()));

        int numberOfAppts = DBAppointments.checkImminentAppointments();
        String is_are = "There are";
        String is_are_fr = "Là sont";
        String appt_appts = "appointments";
        String appt_appts_fr = "rendez-vous";

        if (!(numberOfAppts == 0 || numberOfAppts > 1)) {
            is_are = "There is";
            is_are_fr = "Là est";
            appt_appts = "appointment";
        }

        if (Locale.getDefault().getDisplayLanguage().equals("français"))
        {
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setTitle(appt_appts_fr);
            alert.setHeaderText("Rendez-vous à venir");
            alert.setContentText(is_are_fr + " " + numberOfAppts + " " + appt_appts_fr + " "
                    + "dans les 15 prochaines minutes." + "\n" + imminentAppointmentListing(DBAppointments.getImminentAppts()));
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setTitle("Appointments");
            alert.setHeaderText("Upcoming Appointments");
            alert.setContentText(is_are + " " + numberOfAppts + " " + appt_appts + " in the next 15 minutes. \n" +
                    imminentAppointmentListing(DBAppointments.getImminentAppts()));
            alert.showAndWait();
        }
    }

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
