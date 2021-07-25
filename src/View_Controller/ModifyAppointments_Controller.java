package View_Controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBCustomers;
import DBAccess.DBUsers;
import Model.Appointment;
import static View_Controller.Appointments_Controller.*;

import Model.Contact;
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
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** FXML ModifyAppointments_Controller Class: Handles the modification of a current appointment. */
public class ModifyAppointments_Controller implements Initializable
{
    @FXML private TextField apptID;
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
    @FXML private ToggleButton startAM;
    @FXML private ToggleButton startPM;
    @FXML private ToggleGroup am_pmEnd;
    @FXML private ToggleButton endAM;
    @FXML private  ToggleButton endPM;

    /** A private data member holding an object passed from the main Appointments_Controller, that represents the
     * user-selected appointment to modify.
     */
    private Appointment appt = getSelectedAppt();

    private ObservableList<Integer> selectableHour = FXCollections.observableArrayList();
    private ObservableList<String> selectableMinute = FXCollections.observableArrayList();
    private ObservableList<String> customerList = FXCollections.observableArrayList();

    /** Sets the initial conditions of the Modify Appointments scene, e.g. pre-populating all the input fields.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        apptID.setDisable(true);
        apptContactEmail.setDisable(true);
        apptContactName.setItems(DBContacts.getAllContactNames());

        apptStartHour.setItems(apptHour());
        apptStartMin.setItems(apptMin());

        apptEndHour.setItems(apptHour());
        apptEndMin.setItems(apptMin());

        apptCustomerName.setItems(customers());

        try
        {
            setFields();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    /** Populates the E-mail field based on the selected contact. */
    public void contactEmailHandler()
    {
        String contactName = apptContactName.getValue().toString();

        apptContactEmail.setText(DBContacts.getContactEmailByID(Contact.getContactIDByName(contactName)));
    }

    /** Executes the update of the appointment in the database. */
    public void saveHandler(ActionEvent event) throws IOException, ParseException
    {
        try
        {
            if(Appointment.validateAppt(apptCustomerName, apptTitle, apptDescription, apptLocation, apptType,
                    apptContactName, startTimeStamper(), endTimeStamper()))
            {
                int apptID = appt.getApptID();
                String title = apptTitle.getText();
                String description = apptDescription.getText();
                String location = apptLocation.getText();
                String type = apptType.getText();
                Timestamp start = startTimeStamper();
                Timestamp end = endTimeStamper();
                int customerID = Customer.getCustomerIDByName(apptCustomerName.getValue().toString());
                int userID = DBUsers.getCurrentUserID();
                int contactID = Contact.getContactIDByName(apptContactName.getValue().toString());


                Appointment modifiedAppt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                        userID, contactID);

                if(DBAppointments.validateBusinessHours(modifiedAppt))
                {
                    if(DBAppointments.validateApptOverlap(modifiedAppt))
                    {
                        if(DBAppointments.updateAppt(modifiedAppt))
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Appointments");
                            alert.setHeaderText("Appointment Modification Successful");
                            alert.setContentText("Appointment " + appt.getApptID() + " - " + appt.getTitle() + " - " +
                                    appt.getType() + " succesfully updated.");
                            alert.showAndWait();

                            // Switch to Appts Scene
                            Parent Appointments = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
                            Scene scene = new Scene(Appointments);
                            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            window.setScene(scene);
                            window.show();
                        }
                    }
                }
            }
        }
        catch(NullPointerException npe)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointments");
            alert.setHeaderText("Appointment Time is Incomplete");
            alert.setContentText("Please enter all date, hour, minute, and AM/PM fields for the\n" +
                    "Start and End times.");
            alert.showAndWait();
        }
    }

    /** Redirects to the main Appointments screen. */
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

    /** Prepopulates the form's fields with the data values from the pre-selected appointment to modify. */
    public void setFields() throws ParseException
    {
        apptID.setText(Integer.toString(appt.getApptID()));
        apptTitle.setText(appt.getTitle());
        apptCustomerName.setValue(DBCustomers.getCustomerNameByID(appt.getCustomerID()));
        apptDescription.setText(appt.getDescription());
        apptLocation.setText(appt.getLocation());
        apptType.setText(appt.getType());
        apptStartDate.setValue(appt.getStart().toLocalDateTime().toLocalDate());
        apptEndDate.setValue(appt.getEnd().toLocalDateTime().toLocalDate());

        // Use this as the input to the converter function
        Timestamp tStart = appt.getStart();
        timeConverter(tStart, am_pmStart, apptStartHour);

        Timestamp tEnd = appt.getEnd();
        timeConverter(tEnd, am_pmEnd, apptEndHour);

        apptStartMin.setValue(String.format("%02d", appt.getStart().toLocalDateTime().getMinute()));
        apptEndMin.setValue(String.format("%02d", appt.getEnd().toLocalDateTime().getMinute()));

        apptContactName.setValue(DBContacts.getContactNameByID(appt.getContactID()));
        apptContactEmail.setText(DBContacts.getContactEmailByID(appt.getContactID()));
    }

    /** Produces the list of all customers to select from. */
    public ObservableList customers()
    {
        for(Customer customer : DBCustomers.getAllCustomers())
        {
            customerList.add(String.valueOf(customer.getCustomerID()) + ": " + customer.getCustomerName());
        }

        return customerList;
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

    /** Converts time from 12-hour format into 24-hour format. */
    public void timeConverter(Timestamp datetime, ToggleGroup start_end, ComboBox hour)
    {

        String hourInput = String.valueOf(datetime.toLocalDateTime().getHour());
        System.out.println("Hour input :"+hourInput);
        System.out.println("Hour cmbox source: " + hour.getId());

        int hourNumber = Integer.parseInt(hourInput);
        int hourBase = 12;
        int hourDiff = hourNumber - hourBase;

        if(hour.getId() == apptStartHour.getId())
        {
            if (hourNumber == 0 || (hourNumber >= 1 && hourNumber <= 11))
            {
                start_end.selectToggle(startAM);

                if (hourNumber == 0)
                {
                    hour.setValue("12");
                }
                else
                {
                    hour.setValue(String.valueOf(hourNumber));
                }
            }
            else if (hourNumber >= 12 && hourNumber <= 23)
            {
                start_end.selectToggle(startPM);

                if (hourNumber == 12)
                {
                    apptStartHour.setValue(String.valueOf(hourNumber));
                }
                else
                {
                    apptStartHour.setValue(String.valueOf(hourDiff));
                }
            }
        }
        else if(hour.getId() == apptEndHour.getId())
        {
            if(hourNumber == 0 || (hourNumber >= 1 && hourNumber <= 11))
            {
                start_end.selectToggle(endAM);

                if(hourNumber == 0)
                {
                    hour.setValue("12");
                }
                else
                {
                    hour.setValue((String.valueOf(hourNumber)));
                }
            }
            else if(hourNumber >= 12 && hourNumber <= 23)
            {
                start_end.selectToggle(endPM);

                if(hourNumber == 12)
                {
                    apptEndHour.setValue(String.valueOf(hourNumber));
                }
                else
                {
                    apptEndHour.setValue(String.valueOf(hourDiff));
                }
            }
        }
    }
}
