package View_Controller;

import DBAccess.DBAppointments;
import Model.Appointment;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/** FXML Appointments_Controller Class: Handles the main Appointment screen, reporting, and redirects to other screens. */
public class Appointments_Controller implements Initializable
{
    @FXML private TableView<Appointment> apptsTable;
    @FXML private TableColumn<Appointment, Integer> apptID;
    @FXML private TableColumn<Appointment, String> title;
    @FXML private TableColumn<Appointment, String> description;
    @FXML private TableColumn<Appointment, String> location;
    @FXML private TableColumn<Appointment, String> type;
    @FXML private TableColumn<Appointment, String> start;
    @FXML private TableColumn<Appointment, String> end;
    @FXML private TableColumn<Appointment, Integer> customerID;
    @FXML private TableColumn<Appointment, Integer> userID;
    @FXML private TableColumn<Appointment, Integer> contactID;

    @FXML private TableView contactsByApptCt;
    @FXML private TableColumn<String, String> contactName;

    @FXML private TableView contactsByApptTime;
    @FXML private TableColumn<String, String> contactApptTimeRanking;

    @FXML private TableView divisionsByCustomerCt;
    @FXML private TableColumn<String, String> divisionRankings;

    @FXML private TableView typesByApptCt;
    @FXML private TableColumn<String, String> typeRankings;

    @FXML private MenuButton menuBar;
    @FXML private ToggleButton allTime;
    @FXML private ToggleButton allCustomers;
    @FXML private ToggleButton allContacts;
    @FXML private ToggleButton allTypes;
    @FXML private ComboBox monthFilter;
    @FXML private ComboBox weekFilter;
    @FXML private ComboBox customerFilter;
    @FXML private ComboBox contactFilter;
    @FXML private ComboBox typeFilter;
    @FXML private Label apptCtr;
    @FXML private Label reportVariables;

    private static Appointment selectedAppt = null;

    /** Public getter for the private selectedAppt variable for the ModifyAppointments_Controller to use. */
    public static Appointment getSelectedAppt()
    {
        return selectedAppt;
    }

    /** Sets the initial conditions of the Appointments scene, such as prepopulating the table views.
     *
     * @param url Resolves the relative file path of the root object.
     * @param resourceBundle Localizes the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        populateApptsTable();
        updateApptsTable();
        resetAllFilters();
        monthFilter.setItems(monthNames());
        weekFilter.setItems(weekNames());
        customerFilter.setItems(customerNames());
        contactFilter.setItems(contactNames());
        typeFilter.setItems(typeNames());
        updateApptCtr();
        reportVariables.setText(" - All Appointments");
    }

    /** Produces a list of all Months to filter appointments on. */
    public ObservableList<String> monthNames()
    {
        ObservableList<String> monthList = FXCollections.observableArrayList();

        for(Timestamp ts : DBAppointments.getAllAppointmentTimestamps())
        {
            if(!(monthList.contains(ts.toLocalDateTime().getMonth().toString())))
            {
                monthList.add(ts.toLocalDateTime().getMonth().toString());
            }
        }
        return monthList;
    }

    /** Produces a list of all Weeks to filter appointments on. */
    public ObservableList<String> weekNames()
    {
        ObservableList<String> weekList = FXCollections.observableArrayList();

        for(String week : DBAppointments.getAllAppointmentWeeks())
        {
            if(!(weekList.contains(week)))
            {
                weekList.add(week);
            }
        }
        return weekList;
    }

    /** Produces a list of all Customers to filter appointments on. */
    public ObservableList<String> customerNames()
    {
        ObservableList<String> customerList;

        customerList = DBAppointments.getAllAppointmentCustomerNames();

        return customerList;
    }

    /** Products a list of all Contacts to filter appointments on. */
    public ObservableList<String> contactNames()
    {
        ObservableList<String> contactList;

        contactList = DBAppointments.getAllAppointmentContactNames();

        return contactList;
    }

    /** Produces a list of all Types to filter appointments on. */
    public ObservableList<String> typeNames()
    {
        ObservableList<String> typeList = FXCollections.observableArrayList();

        for(String type : DBAppointments.getAllAppointmentTypes())
        {
            if(!(typeList.contains(type)))
            {
                typeList.add(type);
            }
        }

        return typeList;
    }

    /** Redirects to Customers scene. */
    public void customerMenuHandler(ActionEvent event) throws IOException
    {
        // Redirect to Customers Screen
        Parent customers = FXMLLoader.load(getClass().getResource("Customers.fxml"));
        Scene scene = new Scene(customers);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Redirects to Contacts scene. */
    public void consultantsMenuHandler(ActionEvent event) throws IOException
    {
        // Redirect to consultants Screen
        Parent consultants = FXMLLoader.load(getClass().getResource("Contacts.fxml"));
        Scene scene = new Scene(consultants);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Redirects to Add Appointment scene. */
    public void addApptHandler(ActionEvent event) throws IOException
    {
        // Redirect to addAppt screen
        Parent addAppt = FXMLLoader.load(getClass().getResource("AddAppointments.fxml"));
        Scene scene = new Scene(addAppt);
        Stage window = (Stage)menuBar.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /** Redirects to Modify Appointment scene. */
    public void modifyApptHandler(ActionEvent event) throws IOException
    {
        selectedAppt = apptsTable.getSelectionModel().getSelectedItem();
        if(selectedAppt != null)
        {
            // redirect to Modify Appt Screen
            Parent modifyAppt = FXMLLoader.load(getClass().getResource("ModifyAppointments.fxml"));
            Scene scene = new Scene(modifyAppt);
            Stage window = (Stage)menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Appointments");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to modify");
            alert.showAndWait();
        }
    }

    /** Handles the deletion of an appointemnt from the database. */
    public void removeApptHandler(ActionEvent event) throws IOException
    {
        Appointment appt = apptsTable.getSelectionModel().getSelectedItem();

        if(appt != null)
        {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Appointments");
            confirm.setHeaderText("Confirm Appointment Cancellation");
            confirm.setContentText("Are you sure you want to delete the following appointment?\n"+
                    "Appointment " + appt.getApptID() + ": " + appt.getTitle() + " - " + appt.getType());
            confirm.showAndWait();

            if(confirm.getResult() == ButtonType.OK)
            {
                if(DBAppointments.removeAppt(appt))
                {
                    updateApptsTable();
                    Alert alert = new Alert((Alert.AlertType.INFORMATION));
                    alert.setTitle("Appointments");
                    alert.setHeaderText("Cancellation Complete");
                    alert.setContentText("Appointment " + appt.getApptID() + ": " + appt.getTitle() + " - " +
                            appt.getType() + " is cancelled.");
                    alert.showAndWait();
                }
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Appointments");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to delete");
            alert.showAndWait();
        }

    }

    /** Maps the appointment fields to the table columns. */
    private void populateApptsTable()
    {
        apptID.setCellValueFactory(new PropertyValueFactory<>("ApptID"));
        title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        location.setCellValueFactory(new PropertyValueFactory<>("Location"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        end.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        userID.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        contactID.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
    }

    /** Updates the appointments table view to show all appointments. */
    private void updateApptsTable()
    {
        apptsTable.setItems(DBAppointments.getAllAppointments());
        updateApptCtr();
    }

    /** Sets all filters to 'All', thus showing all appointments. */
    public void resetAllFilters()
    {
        allTime.setSelected(true);
        allCustomers.setSelected(true);
        allContacts.setSelected(true);
        allTypes.setSelected(true);
    }

    /** Updates the Appointment Count to reflect the count of appointments as shown in the table view. */
    public void updateApptCtr()
    {
        int apptCount = 0;

        for(Appointment appt : apptsTable.getSelectionModel().getTableView().getItems())
        {
            apptCount++;
        }

        apptCtr.setText(Integer.toString(apptCount));
        contactsRankedByApptCt();
        divisionsRankedByCustomerCt();
        typesRankedByApptType();
        contactsRankedByApptTime();
    }

    /** Updates the contactsRankedByApptCt table view to show all contacts ranked by their appointment count.
     * Lambda function is used here.
     */
    public void contactsRankedByApptCt()
    {
        /** Lambda function:  Enables an efficient way for table cell to take any data and convert to string. */
        contactName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));

        contactsByApptCt.setItems(DBAppointments.rankContactsByApptCount());
        contactsByApptCt.refresh();
    }

    /** Updates the contactsRankedByApptTime table view to show all contacts ranked by their total appointment time.
     * Lambda function is used here.
     */
    public void contactsRankedByApptTime()
    {
        /** Lambda function:  Enables an efficient way for table cell to take any data and convert to string. */
        contactApptTimeRanking.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));

        contactsByApptTime.setItems(DBAppointments.rankContactsByApptTime());
        contactsByApptTime.refresh();
    }

    /** Updates the divisionsRankedByCustomerCt table view to show all divisions ranked by their customer count.
     * Lambda function is used here.
     */
    public void divisionsRankedByCustomerCt()
    {
        /** Lambda function:  Enables an efficient way for table cell to take any data and convert to string. */
        divisionRankings.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));

        divisionsByCustomerCt.setItems(DBAppointments.rankDivisionsByCustomerCount());
        divisionsByCustomerCt.refresh();
    }

    /** Updates the typesRankedByApptType table view to show all types ranked by their appointment count.
     * Lambda function is used here.
     */
    public void typesRankedByApptType()
    {
        /** Lambda function:  Enables an efficient way for table cell to take any data and convert to string. */
        typeRankings.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));

        typesByApptCt.setItems(DBAppointments.rankTypesByApptCount());
        typesByApptCt.refresh();
    }

    /** Exits the user from the program. */
    public void exitProgramHandler()
    {
        Alert alert = new Alert((Alert.AlertType.CONFIRMATION));
        alert.setTitle("Appointments");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to close the program?");
        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK)
        {
            System.exit(0);
        }
    }

    /** Controls the displayed appointments listing based on which 'All' filters are selected for any of the report criteria. */
    public void resetFilterHandler(ActionEvent event) throws IOException
    {
        if(event.getSource() == allTime)
        {
            allTime.setSelected(true);
            monthFilter.getSelectionModel().clearSelection();
            weekFilter.getSelectionModel().clearSelection();

            if(allCustomers.isSelected() && allContacts.isSelected() && allTypes.isSelected())
            {
                // All appointments
                monthFilter.getSelectionModel().clearSelection();
                weekFilter.getSelectionModel().clearSelection();
                apptsTable.setItems(DBAppointments.getAllAppointments());
                updateApptCtr();
                reportVariables.setText(" - All Appointments");
            }
            else if(!(allCustomers.isSelected()) && allContacts.isSelected() && allTypes.isSelected())
            {
                // All appointments filtered by selected customer
                String customerName = customerFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomer(customerName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName);
            }
            else if(allCustomers.isSelected() && !(allContacts.isSelected()) && allTypes.isSelected())
            {
                // All appointments filtered by selected contact
                String contactName = contactFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByContact(contactName));
                updateApptCtr();
                reportVariables.setText(" - " + contactName);
            }
            else if(allCustomers.isSelected() && allContacts.isSelected() && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected type
                String typeName = typeFilter.getValue().toString();
                apptsTable.setItems((DBAppointments.filterApptsViewByType(typeName)));
                updateApptCtr();
                reportVariables.setText(" - " + typeName);
            }
            else if(allCustomers.isSelected() && !(allContacts.isSelected()) && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected contact and type
                String typeName = typeFilter.getValue().toString();
                String contactName = contactFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByContactAndType(contactName, typeName));
                updateApptCtr();
                reportVariables.setText(" - " + contactName + " & " + typeName);
            }
            else if(!(allCustomers.isSelected()) && allContacts.isSelected() && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected customer and type
                String customerName = customerFilter.getValue().toString();
                String typeName = typeFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndType(customerName, typeName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName + " & " + typeName);
            }
            else if(!(allCustomers.isSelected()) && !(allContacts.isSelected()) && allTypes.isSelected())
            {
                // All appointments filtered by selected customer and contact
                String customerName = customerFilter.getValue().toString();
                String contactName = contactFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndContact(customerName, contactName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName + " & " + contactName);
            }
            else if(!(allCustomers.isSelected()) && !(allContacts.isSelected()) && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected customer, contact, and type
                String customerName = customerFilter.getValue().toString();
                String contactName = contactFilter.getValue().toString();
                String typeName = typeFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomerContactType(customerName, contactName, typeName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName + " & " + contactName + " & " + typeName);
            }
        }
        else if(event.getSource() == allCustomers)
        {
            allCustomers.setSelected(true);
            customerFilter.getSelectionModel().clearSelection();

            if(allTime.isSelected() && allContacts.isSelected() && allTypes.isSelected())
            {
                // All appointments
                apptsTable.setItems(DBAppointments.getAllAppointments());
                updateApptCtr();
                reportVariables.setText(" - All Appointments");
            }
            else if(!(allTime.isSelected()) && allContacts.isSelected() && allTypes.isSelected())
            {
                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified month
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonth(month));
                    updateApptCtr();
                    reportVariables.setText(" - " + month);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified week
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeek(week));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week);
                }
            }
            else if(allTime.isSelected() && !(allContacts.isSelected()) && allTypes.isSelected())
            {
                // All appointments filtered by specified contact
                String contactName = contactFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByContact(contactName));
                updateApptCtr();
                reportVariables.setText(" - " + contactName);
            }
            else if(allTime.isSelected() && allContacts.isSelected() && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected type
                String typeName = typeFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByType(typeName));
                updateApptCtr();
                reportVariables.setText(" - " + typeName);
            }
            else if(allTime.isSelected() && !(allContacts.isSelected()) && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected contact and type
                String contactName = contactFilter.getValue().toString();
                String typeName = typeFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByContactAndType(contactName, typeName));
                updateApptCtr();
                reportVariables.setText(" - " + contactName + " & " + typeName);
            }
            else if(!(allTime.isSelected()) && allContacts.isSelected() && !(allTypes.isSelected()))
            {
                String typeName = typeFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified by month and type
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndType(month, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + typeName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified week and type
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndType(week, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + typeName);
                }
            }
            else if(!(allTime.isSelected()) && !(allContacts.isSelected()) && allTypes.isSelected())
            {
                String contactName = contactFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified month and contact
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndContact(month, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + contactName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified week and contact
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndContact(week, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + contactName);
                }
            }
            else if(!(allTime.isSelected()) && !(allContacts.isSelected()) && !(allTypes.isSelected()))
            {
                String contactName = contactFilter.getValue().toString();
                String type = typeFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified contact, month, and type
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthContactType(month, contactName, type));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + contactName + " & " + type);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified contact, week, and type
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekContactType(week, contactName, type));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + contactName + " & " + type);
                }
            }
        }
        else if(event.getSource() == allContacts)
        {
            allContacts.setSelected(true);
            contactFilter.getSelectionModel().clearSelection();

            if(allTime.isSelected() && allCustomers.isSelected() && allTypes.isSelected())
            {
                resetAllFilters();
                contactFilter.getSelectionModel().clearSelection();
                apptsTable.setItems(DBAppointments.getAllAppointments());
                updateApptCtr();
                reportVariables.setText(" - All Appointments");
            }
            else if(!(allTime.isSelected()) && allCustomers.isSelected() && allTypes.isSelected())
            {
                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified month
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonth(month));
                    updateApptCtr();
                    reportVariables.setText(" - " + month);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified week
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeek(week));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week);
                }
            }
            else if(allTime.isSelected() && !(allCustomers.isSelected()) && allTypes.isSelected())
            {
                // All appointments filtered by specified customer
                String customerName = customerFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomer(customerName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName);
            }
            else if(allTime.isSelected() && allCustomers.isSelected() && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected type
                String typeName = typeFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByType(typeName));
                updateApptCtr();
                reportVariables.setText(" - " + typeName);
            }
            else if(allTime.isSelected() && !(allCustomers.isSelected()) && !(allTypes.isSelected()))
            {
                // All appointments filtered by selected customer and type
                String customerName = typeFilter.getValue().toString();
                String typeName = typeFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndType(customerName, typeName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName + " & " + typeName);
            }
            else if(!(allTime.isSelected()) && allCustomers.isSelected() && !(allTypes.isSelected()))
            {
                String typeName = typeFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified month and type
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndType(month, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + typeName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified week and type
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndType(week, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + typeName);
                }
            }
            else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && allTypes.isSelected())
            {
                String customerName = customerFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer and by specified month
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndCustomer(month, customerName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer and by specified week
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndCustomer(week, customerName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName);
                }
            }
            else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && !(allTypes.isSelected()))
            {
                String typeName = typeFilter.getValue().toString();
                String customerName = customerFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer, month, and type
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerType(month, customerName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName + " & " + typeName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer, week, and type
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerType(week, customerName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + typeName);
                }
            }
        }
        else if(event.getSource() == allTypes)
        {
            allTypes.setSelected(true);
            typeFilter.getSelectionModel().clearSelection();

            if(allTime.isSelected() && allCustomers.isSelected() && allContacts.isSelected())
            {
                resetAllFilters();
                apptsTable.setItems(DBAppointments.getAllAppointments());
                updateApptCtr();
            }
            else if(!(allTime.isSelected()) && allCustomers.isSelected() && allContacts.isSelected())
            {
                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified month
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonth(month));
                    updateApptCtr();
                    reportVariables.setText(" - " + month);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified week
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeek(week));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week);
                }
            }
            else if(allTime.isSelected() && !(allCustomers.isSelected()) && allContacts.isSelected())
            {
                // All appointments filtered by specified customer
                String customerName = customerFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomer(customerName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName);
            }
            else if(allTime.isSelected() && allCustomers.isSelected() && !(allContacts.isSelected()))
            {
                // All appointments filtered by specified contact
                String contactName = contactFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByContact(contactName));
                updateApptCtr();
                reportVariables.setText(" - " + contactName);
            }
            else if(allTime.isSelected() && !(allCustomers.isSelected()) && !(allContacts.isSelected()))
            {
                // All appointments filtered by specified customer and contact
                String customerName = customerFilter.getValue().toString();
                String contactName = contactFilter.getValue().toString();
                apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndContact(customerName, contactName));
                updateApptCtr();
                reportVariables.setText(" - " + customerName + " & " + contactName);
            }
            else if(!(allTime.isSelected()) && allCustomers.isSelected() && !(allContacts.isSelected()))
            {
                String contactName = contactFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer and by specified month
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndCustomer(month, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + contactName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer and by specified week
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndCustomer(week, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + contactName);
                }
            }
            else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && allContacts.isSelected())
            {
                String customerName = customerFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer and by specified month
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndCustomer(month, customerName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer and by specified week
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndCustomer(week, customerName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName);
                }
            }
            else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && !(allContacts.isSelected()))
            {
                String customerName = customerFilter.getValue().toString();
                String contactName = contactFilter.getValue().toString();

                if(monthFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified custome month, customer, and contact
                    String month = monthFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContact(month, customerName, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName);
                }
                else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                {
                    // All appointments filtered by specified customer and by specified week
                    String week = weekFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContact(week, customerName, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName);
                }
            }
        }
    }

    /** Controls the displayed appointments listing based on which non-All filters are selected for any of the report criteria. */
    public void filterHandler(ActionEvent event) throws IOException
    {
        ComboBox cb = (ComboBox) event.getSource();

        if(cb.getValue() != null)
        {
            if(event.getSource() == monthFilter)
            {
                String month = monthFilter.getValue().toString();
                allTime.setSelected(false);
                weekFilter.getSelectionModel().clearSelection();

                if(allCustomers.isSelected() && allContacts.isSelected() && allTypes.isSelected())
                {
                    // Filter all appointments by selected month
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonth(month));
                    updateApptCtr();
                    reportVariables.setText(" - " + month);
                }
                else if (!(allCustomers.isSelected()) && allContacts.isSelected() && allTypes.isSelected())
                {
                    // Filter all appoints by selected month and customer
                    String customerName = customerFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndCustomer(month, customerName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName);
                }
                else if(allCustomers.isSelected() && !(allContacts.isSelected()) && allTypes.isSelected())
                {
                    // Filter all appointments by selected month and contact
                    String contactName = contactFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndContact(month, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + contactName);
                }
                else if(allCustomers.isSelected() && allContacts.isSelected() && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected month and type
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndType(month, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + typeName);
                }
                else if(allCustomers.isSelected() && !(allContacts.isSelected()) && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected month, contact, and type
                    String contactName = contactFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthContactType(month, contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + contactName + " & " + typeName);
                }
                else if(!(allCustomers.isSelected()) && allContacts.isSelected() && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected month, customer, and type
                    String customerName = customerFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerType(month, customerName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName + " & " + typeName);
                }
                else if(!(allCustomers.isSelected()) && !(allContacts.isSelected()) && allTypes.isSelected())
                {
                    // Filter all appointments by selected month, customer, contact
                    String customerName = customerFilter.getValue().toString();
                    String contactName = contactFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContact(month, customerName, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName);
                }
                else if(!(allCustomers.isSelected()) && !(allContacts.isSelected()) && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected month, customer, contact, type
                    String customerName = customerFilter.getValue().toString();
                    String contactName = contactFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContactType(month, customerName, contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName + " & " + typeName);
                }
            }
            else if(event.getSource() == weekFilter)
            {
                String week = weekFilter.getValue().toString();
                allTime.setSelected(false);
                monthFilter.getSelectionModel().clearSelection();

                if(allCustomers.isSelected() && allContacts.isSelected() && allTypes.isSelected())
                {
                    // Filter all appointments by selected week
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeek(week));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week);
                }
                else if (!(allCustomers.isSelected()) && allContacts.isSelected() && allTypes.isSelected())
                {
                    // Filter all appointments by selected week and customer
                    String customerName = customerFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndCustomer(week, customerName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName);
                }
                else if(allCustomers.isSelected() && !(allContacts.isSelected()) && allTypes.isSelected())
                {
                    // Filter all appointments by selected week and contact
                    String contactName = contactFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndContact(week, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + contactName);
                }
                else if(allCustomers.isSelected() && allContacts.isSelected() && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected week and type
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndType(week, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + typeName);
                }
                else if(allCustomers.isSelected() && !(allContacts.isSelected()) && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected week, contact, type
                    String contactName = contactFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekContactType(week, contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + contactName + " & " + typeName);
                }
                else if(!(allCustomers.isSelected()) && allContacts.isSelected() && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected week, customer, type
                    String customerName = customerFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerType(week, customerName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + typeName);
                }
                else if(!(allCustomers.isSelected()) && !(allContacts.isSelected()) && allTypes.isSelected())
                {
                    // Filter all appointments by selected week, customer, contact
                    String customerName = customerFilter.getValue().toString();
                    String contactName = contactFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContact(week, customerName, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName);
                }
                else if(!(allCustomers.isSelected()) && !(allContacts.isSelected()) && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected week, customer, contact, type
                    String customerName = customerFilter.getValue().toString();
                    String contactName = contactFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContactType(week, customerName, contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName + " & " + typeName);
                }
            }
            else if(event.getSource() == customerFilter)
            {
                String customerName = customerFilter.getValue().toString();
                allCustomers.setSelected(false);

                if(allTime.isSelected() && allContacts.isSelected() && allTypes.isSelected())
                {
                    // Filter all appointments by selected customer
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomer(customerName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName);
                }
                else if(!(allTime.isSelected()) && allContacts.isSelected() && allTypes.isSelected())
                {
                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // All appointments filtered by specified month and customer
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndCustomer(month, customerName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week and customer
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndCustomer(week, customerName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName);
                    }
                }
                else if(allTime.isSelected() && !(allContacts.isSelected()) && allTypes.isSelected())
                {
                    // Filter all appointments by selected customer and contact
                    String contactName = contactFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndContact(customerName, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName + " & " + contactName);
                }
                else if(allTime.isSelected() && allContacts.isSelected() && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected customer and type
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndType(customerName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName + " & " + typeName);
                }
                else if(allTime.isSelected() && !(allContacts.isSelected()) && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected customer, contact, type
                    String contactName = contactFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomerContactType(customerName, contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName + " & " + contactName + " & " + typeName);
                }
                else if(!(allTime.isSelected()) && allContacts.isSelected() && !(allTypes.isSelected()))
                {
                    String typeName = typeFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected month, customer, type
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerType(month, customerName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week, customer, type
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerType(week, customerName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + typeName);
                    }
                }
                else if(!(allTime.isSelected()) && !(allContacts.isSelected()) && allTypes.isSelected())
                {
                    String contactName = contactFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected month, customer, contact
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContact(month, customerName, contactName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week, customer, contact
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContact(week, customerName, contactName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName);
                    }
                }
                else if(!(allTime.isSelected()) && !(allContacts.isSelected()) && !(allTypes.isSelected()))
                {
                    String contactName = contactFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected month, customer, contact, type
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContactType(month, customerName, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week, customer, contact, type
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContactType(week, customerName, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName + " & " + typeName);
                    }
                }
            }
            else if(event.getSource() == contactFilter)
            {
                String contactName = contactFilter.getValue().toString();
                allContacts.setSelected(false);

                if(allTime.isSelected() && allCustomers.isSelected() && allTypes.isSelected())
                {
                    // Filter all appointments by selected contact
                    apptsTable.setItems(DBAppointments.filterApptsViewByContact(contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + contactName);
                }
                else if(!(allTime.isSelected()) && allCustomers.isSelected() && allTypes.isSelected())
                {
                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected month and contact
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndContact(month, contactName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + contactName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week and contact
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndContact(week, contactName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + contactName);
                    }
                }
                else if(allTime.isSelected() && !(allCustomers.isSelected()) && allTypes.isSelected())
                {
                    // Filter all appointments by selected contact and customer
                    String customerName = customerFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndContact(customerName, contactName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName + " & " + contactName);
                }
                else if(allTime.isSelected() && allCustomers.isSelected() && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected contact and type
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByContactAndType(contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + contactName + " & " + typeName);
                }
                else if(allTime.isSelected() && !(allCustomers.isSelected()) && !(allTypes.isSelected()))
                {
                    // Filter all appointments by selected contact, customer, type
                    String customerName = customerFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomerContactType(customerName, contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName + " & " + contactName + " & " + typeName);
                }
                else if(!(allTime.isSelected()) && allCustomers.isSelected() && !(allTypes.isSelected()))
                {
                    String typeName = typeFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected month, contact, type
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthContactType(month, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + contactName + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week, contact, type
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekContactType(week, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + contactName + " & " + typeName);
                    }
                }
                else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && allTypes.isSelected())
                {
                    String customerName = customerFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected month, customer, contact
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContact(month, customerName, contactName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week, customer, contact
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContact(week, customerName, contactName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName);
                    }
                }
                else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && !(allTypes.isSelected()))
                {
                    String customerName = customerFilter.getValue().toString();
                    String typeName = typeFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected month, customer, contact, type
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContactType(month, customerName, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected week, customer, contact, type
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContactType(week, customerName, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName + " & " + typeName);
                    }
                }
            }
            else if(event.getSource() == typeFilter)
            {
                String typeName = typeFilter.getValue().toString();
                allTypes.setSelected(false);

                if(allTime.isSelected() && allCustomers.isSelected() && allContacts.isSelected())
                {
                    // Filter all appointments by selected type
                    apptsTable.setItems(DBAppointments.filterApptsViewByType(typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + typeName);
                }
                else if(!(allTime.isSelected()) && allCustomers.isSelected() && allContacts.isSelected())
                {
                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected type and month
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthAndType(month, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // Filter all appointments by selected type and week
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekAndType(week, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + typeName);
                    }
                }
                else if(allTime.isSelected() && !(allCustomers.isSelected()) && allContacts.isSelected())
                {
                    // Filter all appointments by selected type and customer
                    String customerName = customerFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomerAndType(customerName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName + " & " + typeName);
                }
                else if(allTime.isSelected() && allCustomers.isSelected() && !(allContacts.isSelected()))
                {
                    // Filter all appointments by selected type and contact
                    String contactName = contactFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByContactAndType(contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + contactName + " & " + typeName);
                }
                else if(allTime.isSelected() && !(allCustomers.isSelected()) && !(allContacts.isSelected()))
                {
                    // Filter all appointments by selected type, customer, contact
                    String contactName = contactFilter.getValue().toString();
                    String customerName = customerFilter.getValue().toString();
                    apptsTable.setItems(DBAppointments.filterApptsViewByCustomerContactType(customerName, contactName, typeName));
                    updateApptCtr();
                    reportVariables.setText(" - " + customerName + " & " + contactName + " & " + typeName);
                }
                else if(!(allTime.isSelected()) && allCustomers.isSelected() && !(allContacts.isSelected()))
                {
                    String contactName = contactFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // All appointments filtered by specified contact, month, and type
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthContactType(month, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + contactName + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // All appointments filtered by specified contact, week, and type
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekContactType(week, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + contactName + " & " + typeName);
                    }
                }
                else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && allContacts.isSelected())
                {
                    String customerName = customerFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // All appointments filtered by specified customer, month, and type
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerType(month, customerName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // All appointments filtered by specified customer, week, and type
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerType(week, customerName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + typeName);
                    }
                }
                else if(!(allTime.isSelected()) && !(allCustomers.isSelected()) && !(allContacts.isSelected()))
                {
                    String customerName = customerFilter.getValue().toString();
                    String contactName = contactFilter.getValue().toString();

                    if(monthFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // All appointments filtered by specified month, customer, contact, and type
                        String month = monthFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByMonthCustomerContactType(month, customerName, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - " + month + " & " + customerName + " & " + contactName + " & " + typeName);
                    }
                    else if(weekFilter.getSelectionModel().getSelectedItem() != null)
                    {
                        // All appointments filtered by specified week, customer, contact, and type
                        String week = weekFilter.getValue().toString();
                        apptsTable.setItems(DBAppointments.filterApptsViewByWeekCustomerContactType(week, customerName, contactName, typeName));
                        updateApptCtr();
                        reportVariables.setText(" - Week of " + week + " & " + customerName + " & " + contactName + " & " + typeName);
                    }
                }
            }
        }
    }
}
