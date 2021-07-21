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

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class Login_Controller implements Initializable
{
    @FXML private TextField userName;
    @FXML private PasswordField userPassword;
    @FXML private Label zoneID;

    private static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT+0");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        zoneID();
        System.out.println("Get default locale: " + Locale.getDefault().toString());
    }

    public Locale getLocale()
    {
        return Locale.getDefault();
    }

    public void zoneID()
    {
        Locale currentLocale = Locale.getDefault();

        String zone = ZoneId.systemDefault().toString();
        zoneID.setText("System Zone: " + zone);

        // Check if appt is occurring within 15 minutes of time now. i.e., "appt's start <= time now".
        System.out.println(currentLocale.getDisplayLanguage());
    }

    public void loginHandler(ActionEvent event) throws IOException
    {
        if(Credentials.validate(userName.getText().toString(), userPassword.getText().toString()))
        {
            System.out.println("Login Successful");
            DBConnection.startConnection();
            DBCountries.checkDateConversion();
            DBUsers.loginUser(userName.getText().toString(), userPassword.getText().toString());

            imminentApptCheck();

            // redirect to Appts
            Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
            Scene scene = new Scene(addProduct);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            // popup warning
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText("Login Failed");
            alert.setContentText("Incorrect username or password.");
            alert.showAndWait();
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

    public void imminentApptCheck()
    {
        //Get time now in UTC
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Timestamp utcTimestamp = Timestamp.valueOf(formatter.format(getUTCDateForLocalDate()));

        int numberOfAppts = DBAppointments.checkImminentAppointments();
        String is_are = "are";
        String appt_appts = "appointments";
        if(!(numberOfAppts == 0 || numberOfAppts > 1))
        {
            is_are = "is";
            appt_appts = "appointment";
        }

        Alert alert = new Alert((Alert.AlertType.INFORMATION));
        alert.setTitle("Appointments");
        alert.setHeaderText("Upcoming Appointments");
        alert.setContentText("There " + is_are + " " + numberOfAppts + " " + appt_appts + " in the next 15 minutes. \n" +
                imminentAppointmentListing(DBAppointments.getImminentAppts()));
        alert.showAndWait();

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

}
