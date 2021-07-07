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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
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
    @FXML private TextField apptContactName;
    @FXML private TextField apptContactEmail;

    private Appointment appt = getSelectedAppt();
    private ObservableList<Integer> selectableHour = FXCollections.observableArrayList();
    private ObservableList<String> selectableMinute = FXCollections.observableArrayList();
    private ObservableList<String> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        apptID.setDisable(true);

        apptStartHour.setItems(apptHour());
        apptStartMin.setItems(apptMin());

        apptEndHour.setItems(apptHour());
        apptEndMin.setItems(apptMin());

        apptCustomerName.setItems(customers());

        setFields();
    }

    public void saveHandler(ActionEvent event) throws IOException
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
        int contactID = DBContacts.getContactIDByName(apptContactName.getText());


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

    public void setFields()
    {
        apptID.setText(Integer.toString(appt.getApptID()));
        apptTitle.setText(appt.getTitle());
        apptCustomerName.setValue(DBCustomers.getCustomerNameByID(appt.getCustomerID()));
        apptDescription.setText(appt.getDescription());
        apptLocation.setText(appt.getLocation());
        apptType.setText(appt.getType());
        apptStartDate.setValue(appt.getStart().toLocalDateTime().toLocalDate());
        apptStartHour.setValue(appt.getStart().toLocalDateTime().getHour());
        apptStartMin.setValue(appt.getStart().toLocalDateTime().getMinute());
        apptEndDate.setValue(appt.getEnd().toLocalDateTime().toLocalDate());
        apptEndHour.setValue(appt.getEnd().toLocalDateTime().getHour());
        apptEndMin.setValue(appt.getEnd().toLocalDateTime().getMinute());
        apptContactName.setText(DBContacts.getContactNameByID(appt.getContactID()));
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

}
