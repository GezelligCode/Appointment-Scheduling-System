WGU Appointments System
The purpose of this application is to create appointments between Customers and Contacts (called 'Consultants' on the
front-end, for the end-users). The application is run by a third party, collectively referred to as Users. Users are
able to create, modify, and remove appointments, customers, and contacts. In addition, the application is
internationalized to accomodate for all main office locations: USA, Canada, and England. To accomodate for
French-speaking users in regions of Canada, the application also translates the login page to French if the default
locale indicates that the display language is French.

Author: Wassa Anspaugh
Contact Information: wanspau@my.wgu.edu
Student Application Version: 1.0
Date: 7.24.2021

IDE Version: IntelliJ IDEA 2021.1.2 (Ultimate Edition)
JDK Version: 14.0.2
JavaFX Version: 11.0.2+1
MySQL Connector Driver Version: 8.0.222

##########Directions for Running the Program##########
--Login--
Enter 'test' for both the username and password when logging in.

--Main Appointments Screen Overview--
After logging in successfully, the main appointments screen will present. The main features of this screen are as follows:
Listing all appointments: By default, all appointments previously entered will be listed. If you want to customize the
report of appointments, there is a row of button and combobox controls to filter the appointments. The appointments may
be filtered any one or all of the following criteria:
Time -- either by month or by week.
Customer
Consultant
Type

When applying a filter to the Appointment Listing, the 'Appointment Count' will automatically update to reflect what is
shown in the filtered results. Therefore, if you want to know, e.g. the number of appointments for a given combination of
customer + consultant + type, this number will reveal exactly that after applying the filters accordingly.

!!!Additional Report per Part A3f!!!
There are a series of four additional custom metrics that are provided, as shown under 'Appointment Metrics'. Each of
these metrics portray a ranking of some kind, as described by the titles in the table views. For instance, the
'Divisions by Customer Count' will display an ordered list of Divisions based on their customer count.

--Main Appointment Screen Functions--
Editing Appointments: To edit an appointment, the 'Edit Appointments' menu button is selected, from which a list of
three options will appear. The three options are to 1) Add, 2) Modify, or 3) Delete an appointment. Adding or Modifying
an appointment will take you to a separate screen, whereas the Delete option only first generates a confirmation message
to confirm the delete.

Navigation: You may navigate to two other screens, Customers or Consultants. This is done by clicking on either button
labeled accordingly. Alternatively, you may exit the program by clicking the 'Exit' button.

--Adding or Modifying Appointments--
Both Adding and Modifying appointments will generate the same form. In the case of modifying, the form will have its
fields pre-populated with the selected appointment's details, whereas adding an appoint generates a blank form.
In either modifying or adding, each of the fields must be populated. The Contact E-mail will auto-populate the e-mail
based on the contact selected. When the form for adding or modifying is completed, the operation is completed by clicking
the 'Save' button.

--Filtering the Appointment Listing--
By default, the appointments screen will initially display an unfiltered list of all appointments.
To filter the list of appointments by any one of the criteria shown under 'Report Criteria', you can click on the
button or combobox controls to choose from the available criteria. Each of the criteria available are based on all
appointments that have already been created. If a combination of criteria is selected for which no appointments exist,
the report will output zero appointments accordingly. As mentioned before, the total appointment count will automatically
reflect the output of your filtered criteria. In addition, the title 'Appointments List' will also automatically append
additional text to reflect the current criteria selected.

--Customers & Consultants Overview--
Clicking on either the Customers or Consultants button from the main appointments screen will produce a similar screen,
where all previously-entered customers or consultants are listed. Both screens offer the same core functionalities as
follows:
Editing: You may Add, Modify, or Delete from Customers or Consultants by selecting the 'Edit Customers' or 'Edit Contacts'
menu button and making the applicable selection.
Showing Associated Appointments: This tableview automatically updates when a customer or consultant is selected, and
shows all appointments for the selected person.
Navigation: To return to the main appointments screen, the 'Back' button can be clicked.

--Exiting--
To exit the program, click the 'Exit' button from the main appointments screen.



