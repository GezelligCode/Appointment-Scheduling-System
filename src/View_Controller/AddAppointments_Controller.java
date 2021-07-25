package View_Controller;

import DBAccess.*;
import Model.Appointment;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** FXML AddAppointments_Controller Class: Handles addition of new appointments. */
public class AddAppointments_Controller implements Initializable
{
    @FXML private TextField apptTitle;
    @FXML private ComboBox apptCustomerName;
    @FXML private TextField apptDescription;
    @FXML private TextField apptLocation;
    @FXML private TextField apptType;
    @FXML private DatePicker apptStartDate;
    @FXML private ComboBox apptStartHour;
    @FXML private ComboBox apptStartMin;
    @FXML private DatePicker apptEndDate;
    @FXML private ComboBox apptEndHour;
    @FXML private ComboBox apptEndMin;
    @FXML private ComboBox apptContactName;
    @FXML private TextField apptContactEmail;
    @FXML private ToggleGroup am_pmStart;
    @FXML private ToggleGroup am_pmEnd;

    /** Sets the initial conditions of the Add Appointments scene, such as prepopulating the comboboxes.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        apptContactEmail.setDisable(true);
        apptContactName.setItems(DBContacts.getAllContactNames());
        apptStartHour.setItems(apptHour());
        apptStartMin.setItems(apptMin());

        apptEndHour.setItems(apptHour());
        apptEndMin.setItems(apptMin());

        apptCustomerName.setItems(customers());
    }

    private final ObservableList<Integer> selectableHour = FXCollections.observableArrayList();
    private final ObservableList<String> selectableMinute = FXCollections.observableArrayList();
    private final ObservableList<String> customerList = FXCollections.observableArrayList();

    /** Produces the list of all customers to select from. */
    public ObservableList customers()
    {
        for(Customer customer : DBCustomers.getAllCustomers())
        {
            customerList.add(customer.getCustomerName());
        }

        return customerList;
    }

    /** Populates the contact e-mail field based on the user-selected contact. */
    public void contactEmailHandler()
    {
        String contactName = apptContactName.getValue().toString();

        apptContactEmail.setText(DBContacts.getContactEmailByID(DBContacts.getContactIDByName(contactName)));
    }

    /** Executes the addition of the appointment to the database. */
    public void saveHandler(ActionEvent event) throws IOException, ParseException, InvocationTargetException
    {
        try
        {
            if(Appointment.validateAppt(apptCustomerName, apptTitle, apptDescription, apptLocation, apptType,
                    apptContactName, startTimeStamper(), endTimeStamper()))
            {
                int ApptID = 0;
                String customer = apptCustomerName.getValue().toString();
                String title = apptTitle.getText();
                String description = apptDescription.getText();
                String location = apptLocation.getText();
                String type = apptType.getText();
                Timestamp start = startTimeStamper();
                Timestamp end = endTimeStamper();
                String contactName = apptContactName.getValue().toString();

                int contactID = DBContacts.getContactIDByName(contactName);
                int customerID = DBCustomers.getCustomerIDByName(customer);
                int userID = DBUsers.getCurrentUserID();

                Appointment appt = new Appointment(ApptID, title, description, location, type, start, end, customerID, userID, contactID);

                if(DBAppointments.validateBusinessHours(appt))
                {
                    if(DBAppointments.validateApptOverlap(appt))
                    {
                        DBAppointments.addAppointment(appt, start, end);
                        // Switch to Appts Scene
                        Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
                        Scene scene = new Scene(addProduct);
                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.setScene(scene);
                        window.show();
                    }
                }
            }
        }
        catch (NullPointerException npe)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointments");
            alert.setHeaderText("Appointment Time is Incomplete");
            alert.setContentText("Please enter all date, hour, minute, and AM/PM fields for the\n" +
                            "Start and End times.");
            alert.showAndWait();
        }
    }

    /** Returns the user to the main Appointments screen. */
    public void cancelHandler(ActionEvent event) throws IOException
    {
        // Switch to Appts Scene
        Parent Appointments = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene scene = new Scene(Appointments);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Outputs a timestamp for the start date and time based on user input. */
    public Timestamp startTimeStamper() throws ParseException
    {
        return getTimestamp(apptStartDate, apptStartHour, apptStartMin, am_pmStart);
    }

    /** Outputs a timestamp for the end date and time based on user input. */
    public Timestamp endTimeStamper() throws ParseException
    {
        return getTimestamp(apptEndDate, apptEndHour, apptEndMin, am_pmEnd);
    }

    /** Parses the date and times as selected by the user.
     *
     * @param datePicker A DatePicker control, representing either the start date or the end date.
     * @param hourPicker A ComboBox control, representing either the start hour or the end hour.
     * @param minutePicker A ComboBox control, representing either the start minute or the end minute.
     * @param am_pm A ToggleGroup control, representing AM or PM for either the start or end time.
     * @return Returns a Timestamp object that reflects the complete date and time for the start or end time.
     * @throws ParseException Necessary for the conversion of 12-hour format to 24-hour format.
     */
    private Timestamp getTimestamp(DatePicker datePicker, ComboBox hourPicker, ComboBox minutePicker, ToggleGroup am_pm) throws ParseException
    {
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));

        String hour = hourPicker.getValue().toString();
        String min = minutePicker.getValue().toString();

        ToggleButton selectedToggleButton = (ToggleButton) am_pm.getSelectedToggle();
        String am_pmValue = selectedToggleButton.getText();

        String time = hour + ":" + min + " " + am_pmValue;

        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
        String time24HourFormat = date24Format.format(date12Format.parse(time));

        String concatTimeStamp = date + " " + time24HourFormat + ":00";

        return Timestamp.valueOf(concatTimeStamp);
    }

    /** Creates an array of numbers, representing hours of the clock.
     *
     * @return Returns an array of integer type, holding all hours in 12-hour format.
     */
    public ObservableList apptHour()
    {
        int[] hours = new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        for(Integer H : hours)
        {
            if(!(selectableHour.contains(H)))
            {
                selectableHour.add(H);
            }
        }

        return selectableHour;
    }

    /** Creates an array of numbers in String form, representing minutes in intervals of 5 minutes.
     *
     * @return Returns an array of String type, holding minutes at intervals of 5 minutes from 00 to 55.
     */
    public ObservableList apptMin()
    {
        String[] mins = new String[]{"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};

        for(String M : mins)
        {
            if(!(selectableMinute.contains(M)))
            {
                selectableMinute.add(M);
            }
        }

        return selectableMinute;
    }
}
