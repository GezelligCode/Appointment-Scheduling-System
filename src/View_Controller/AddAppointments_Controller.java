package View_Controller;

import DBAccess.*;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.Division;
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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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

    public ObservableList customers()
    {
        for(Customer customer : DBCustomers.getAllCustomers())
        {
            customerList.add(customer.getCustomerName());
        }

        return customerList;
    }

    public void contactEmailHandler() throws IOException
    {
        String contactName = apptContactName.getValue().toString();

        apptContactEmail.setText(DBContacts.getContactEmailByID(DBContacts.getContactIDByName(contactName)));
    }

    public void saveHandler(ActionEvent event) throws IOException, ParseException {
        int ApptID = 0;
        String customer = apptCustomerName.getValue().toString();
        String title = apptTitle.getText();
        String description = apptDescription.getText();
        String location = apptLocation.getText();
        String type = apptType.getText();
        Timestamp start = startTimeStamper();
        Timestamp end = endTimeStamper();
        String contactName = apptContactName.getValue().toString();
        String contactEmail = apptContactEmail.getText();
        Contact contact = new Contact(contactName, contactEmail);

        DBContacts.addContact(contact);

        int contactID = DBContacts.getContactIDByName(contactName);
        int customerID = DBCustomers.getCustomerIDByName(customer);
        int userID = DBUsers.getCurrentUserID();

        Appointment appt = new Appointment(ApptID, title, description, location, type, start, end, customerID, userID, contactID);

        DBAppointments.addAppointment(appt, start, end);


        // Switch to Appts Scene
        Parent addProduct = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene scene = new Scene(addProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void cancelHandler(ActionEvent event) throws IOException
    {
        // Switch to Appts Scene
        Parent Appointments = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        Scene scene = new Scene(Appointments);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public Timestamp startTimeStamper() throws ParseException
    {
        return getTimestamp(apptStartDate, apptStartHour, apptStartMin, am_pmStart);
    }

    public Timestamp endTimeStamper() throws ParseException
    {
        return getTimestamp(apptEndDate, apptEndHour, apptEndMin, am_pmEnd);
    }

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
































    public SpinnerValueFactory hourGenerator()
    {
        String hourPattern = "HH";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(hourPattern);

        SpinnerValueFactory time = new SpinnerValueFactory()
        {
            @Override
            public void decrement(int step)
            {
                if(getValue() == null)
                {
                    setValue(LocalTime.now());
                }
                else
                {
                    LocalTime apptStartTime = (LocalTime) getValue();
                    setValue(apptStartTime.minusHours(step).format(formatter));
                }
            }

            @Override
            public void increment(int step)
            {
                if(this.getValue() == null)
                {
                    setValue(LocalTime.now());
                }
                else
                {
                    LocalTime apptStartTime = (LocalTime) getValue();
                    setValue(apptStartTime.plusHours(step).format(formatter));
                }
            }
        };

        return time;
    }

    public SpinnerValueFactory minGenerator()
    {
        String minPattern = "mm";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(minPattern);

        SpinnerValueFactory time = new SpinnerValueFactory()
        {
            @Override
            public void decrement(int step)
            {
                if(getValue() == null)
                {
                    setValue(LocalTime.NOON.format(formatter));
                }
                else
                {
                    LocalTime apptStartTime = (LocalTime) getValue();
                    //apptStartTime = apptStartTime.format(formatter);
                    setValue(apptStartTime.minusMinutes(step));
                }
            }

            @Override
            public void increment(int step)
            {
                if(this.getValue() == null)
                {
                    setValue(LocalTime.NOON.format(formatter));
                }
                else
                {
                    LocalTime apptStartTime = (LocalTime) getValue();
                    setValue(apptStartTime.plusMinutes(step).format(formatter));
                }
            }
        };

        return time;

    }

}
