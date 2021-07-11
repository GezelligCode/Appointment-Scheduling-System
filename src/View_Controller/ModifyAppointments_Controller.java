package View_Controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import DBAccess.DBCustomers;
import DBAccess.DBUsers;
import Model.Appointment;
import static View_Controller.Appointments_Controller.*;

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
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

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

    private Appointment appt = getSelectedAppt();
    private ObservableList<Integer> selectableHour = FXCollections.observableArrayList();
    private ObservableList<String> selectableMinute = FXCollections.observableArrayList();
    private ObservableList<String> customerList = FXCollections.observableArrayList();

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

    public void contactEmailHandler() throws IOException
    {
        String contactName = apptContactName.getValue().toString();

        apptContactEmail.setText(DBContacts.getContactEmailByID(DBContacts.getContactIDByName(contactName)));
    }

    public void saveHandler(ActionEvent event) throws IOException, ParseException
    {
        int apptID = appt.getApptID();
        String title = apptTitle.getText();
        String description = apptDescription.getText();
        String location = apptLocation.getText();
        String type = apptType.getText();
        Timestamp start = startTimeStamper();
        Timestamp end = endTimeStamper();
        //ToDo find a better way to get the customer ID than by referring to name; there can be duplicative name values
        // in any real-world data set, but the IDs will be unique.
        int customerID = DBCustomers.getCustomerIDByName(apptCustomerName.getValue().toString());
        int userID = DBUsers.getCurrentUserID();
        //ToDo same as ToDo above; need better way to get ID than by name.
        int contactID = DBContacts.getContactIDByName(apptContactName.getValue().toString());


        Appointment modifiedAppt = new Appointment(apptID, title, description, location, type, start, end, customerID,
                userID, contactID);

        if(DBAppointments.updateAppt(modifiedAppt))
        {
            System.out.println("Appointment updated successfully");
            // Switch to Appts Scene
            Parent Appointments = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
            Scene scene = new Scene(Appointments);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            System.out.println("Check form for errors");
        }

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

    public ObservableList customers()
    {
        for(Customer customer : DBCustomers.getAllCustomers())
        {
            customerList.add(customer.getCustomerName());
        }

        return customerList;
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
