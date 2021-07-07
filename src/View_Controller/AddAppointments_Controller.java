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
import java.sql.SQLException;
import java.sql.Timestamp;
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
    @FXML private TextField apptContactName;
    @FXML private TextField apptContactEmail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        apptStartHour.setItems(apptHour());
        apptStartMin.setItems(apptMin());

        apptEndHour.setItems(apptHour());
        apptEndMin.setItems(apptMin());

        apptCustomerName.setItems(customers());
    }

    private ObservableList<Integer> selectableHour = FXCollections.observableArrayList();
    private ObservableList<String> selectableMinute = FXCollections.observableArrayList();
    private ObservableList<String> customerList = FXCollections.observableArrayList();

    public ObservableList customers()
    {
        for(Customer customer : DBCustomers.getAllCustomers())
        {
            customerList.add(customer.getCustomerName());
        }

        return customerList;
    }


    public void saveHandler(ActionEvent event) throws IOException
    {
        // Upon saving, the DBAccess method, addAppt can be called.

        // So at minimum, the form needs to produce the following elements of the Appointment object:
        // int Appt ID, String title, String description, String location, String type, String start, String end,
        // int customerID, int userID, int contactID

        int ApptID = 0;
        String customer = apptCustomerName.getValue().toString();
        String title = apptTitle.getText();
        String description = apptDescription.getText();
        String location = apptLocation.getText();
        String type = apptType.getText();
        Timestamp start = startTimeStamper();
        Timestamp end = endTimeStamper();
        String contactName = apptContactName.getText();
        String contactEmail = apptContactEmail.getText();
        //ToDo generate contactID from Customers Class, based on name input
        Contact contact = new Contact(contactName, contactEmail);
        //Todo Compare code for adding contacts to code for adding customers and see if an error is found for contacts

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

    public Timestamp startTimeStamper()
    {
        return getTimestamp(apptStartDate, apptStartHour, apptStartMin);
    }

    public Timestamp endTimeStamper()
    {
        return getTimestamp(apptEndDate, apptEndHour, apptEndMin);
    }

    private Timestamp getTimestamp(DatePicker datePicker, ComboBox hourPicker, ComboBox minutePicker)
    {
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));

        String hour = hourPicker.getValue().toString();

        String min = minutePicker.getValue().toString();

        String concatTimeStamp = date + " " + hour + ":" + min + ":00";

        return Timestamp.valueOf(concatTimeStamp);
    }

    public ObservableList apptHour()
    {
        int[] hours = new int[]{12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        for(Integer H : hours)
        {
            selectableHour.add(H);
        }

        return selectableHour;
    }

    public ObservableList apptMin()
    {
        String[] mins = new String[]{"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};

        for(String M : mins)
        {
            selectableMinute.add(M);
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
