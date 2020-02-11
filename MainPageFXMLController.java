/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileWriter;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kevin
 */
public class MainPageFXMLController implements Initializable {
    
    /**
     *
     */
    
    
    public static ObservableList<Customer> searchCustomers = FXCollections.observableArrayList();
    
    //Main Screen Properties

    //Add Customer Tab Properties
    @FXML
    private TextField addcustomername;
    @FXML
    private TextField addcustomerphone;
    @FXML
    private TextField addcustomerstreetaddress1;
    @FXML
    private TextField addcustomerstreetaddress2;
    @FXML 
    private TextField addcustomercountry;
    @FXML
    private TextField addcustomercity;
    @FXML
    private TextField addcustomerpostal;
    @FXML
    private Label addcustomernameerrorlabel;
    @FXML 
    private Label addcustomerphoneerrorlabel;
    @FXML
    private Label addcustomeraddresserrorlabel;
    @FXML
    private Label addcustomercountryerrorlabel;
    @FXML
    private Label addcustomercityerrorlabel;
    @FXML
    private Label addcustomerpostalerrorlabel;
    @FXML
    private Label addcustomererrorlabel;
    @FXML
    private Label alertLabel;
    @FXML
    private Label customerAddedLabel;
    
//ADD/UPDATE Appointment Tap Properties
@FXML
private TextField addappointmentsearchcustomerfield;
//Customer TableView
@FXML
private TableView<Customer> searchcustomertableview;
@FXML
private TableColumn<Customer,Integer> addappointmentcustomerid;
@FXML
private TableColumn<Customer,String> addappointmentcustomername;

//Appointments Tableview
@FXML
private TableView<Appointment> existingapointmenttableview;
@FXML
private TableColumn<Appointment,Integer> existingappointmentid;
@FXML
private TableColumn<Appointment,Date> existingappointmentdate;
@FXML
private TableColumn<Appointment,String> existingappointmenttype;
@FXML
private TableColumn<Appointment,String> existingappointmentlocation;

//CONSULTANT SCHEDULE TAB

    /**
     *
     */
public static ObservableList<String> consultantList = FXCollections.observableArrayList();
@FXML
private ChoiceBox selectconsultant;
//view consultant appointments tableview
@FXML
private TableView<Appointment> consultantappointmentstable;
@FXML
private TableColumn<Appointment,Integer> consultantappointmentid;
@FXML
private TableColumn<Appointment,Integer> consultantappointmencustomerID;
@FXML
private TableColumn<Appointment,Date> consultantappointmentstart;
@FXML
private TableColumn<Appointment,Date> consultantappointmentend;
@FXML
private TableColumn<Appointment,String> consultantappointmenttype;

//APPOINTMENT TYPE BY MONTH TAB
private static final ObservableList<String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
@FXML
private ChoiceBox selectmonth;
//appointment type by month tableview
@FXML
private ListView appointmentTypeListview;


//APPOINTMENTS BY LOCATION TAB
private static final ObservableList<String> locationList = FXCollections.observableArrayList("Phoenix", "New York", "London");
@FXML
private ChoiceBox selectLocation;
@FXML
private TableView<Appointment> appointmentsbylocationtable;
@FXML
private TableColumn<Appointment,Integer> appointmentsByLocationAppointmentID;
@FXML
private TableColumn<Appointment,Integer> appointmentsByLocationCustomerID;
@FXML
private TableColumn<Appointment,Date> appointmentsByLocationDate;
@FXML
private TableColumn<Appointment,Date> appointmentsByLocationType;
@FXML
private Label userLabel;
@FXML
private Label noAppointmentsFound;

//Calendat Tab

@FXML
private ChoiceBox calendarChoiceBox;
private static final ObservableList<String> calendarList = FXCollections.observableArrayList("This Week", "This Month");
@FXML
private TableView<Appointment> calendarTable;
@FXML
private TableColumn<Appointment, Integer> calendarAppointmentID;
@FXML
private TableColumn<Appointment, Integer> calendarCustID;
@FXML
private TableColumn<Appointment, Date> calendarDate;
@FXML
private TableColumn<Appointment, String> calendarLocation;
          
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchCustomers.clear();
        selectLocation.setItems(locationList);
        calendarChoiceBox.setItems(calendarList);
        selectconsultant.setItems(consultantList);
        selectmonth.setItems(monthList);
        userLabel.setText(Login.getUserName());
        
         //Query DB for consultants and create a list of options to be used in constultant schedule choicebox
        try {
            ResultSet consultantResults = DBConnect.queryDB("SELECT userName FROM "+DBConnect.username+".user");
            while(consultantResults.next()){
                String consultant = consultantResults.getString("userName");
                consultantList.add(consultant);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainPageFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }   
    
    
   @FXML
   private void exitProgram(ActionEvent event){
        
      System.exit(0);
    }
   //Goes to add appointment scene
   @FXML 
   private void addAppointmentScene(ActionEvent event) throws IOException{
       Customer customer = searchcustomertableview.getSelectionModel().getSelectedItem(); 
       if(customer != null){
        Parent addAppointmentParent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAppointmentFXML.fxml"));
        addAppointmentParent = loader.load();
        Scene addAppointment = new Scene(addAppointmentParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(addAppointment);
        stage.show();
        
       AddAppointmentFXMLController controller = loader.getController();
       controller.loadCustomer(customer);
    }
   }
   @FXML
   private void updateAppointment(ActionEvent event) throws IOException{
       Customer customer = searchcustomertableview.getSelectionModel().getSelectedItem(); 
       Appointment appointment = existingapointmenttableview.getSelectionModel().getSelectedItem(); 
       if(customer != null && appointment != null){
        Parent updateAppointmentParent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAppointmentFXML.fxml"));
        updateAppointmentParent = loader.load();
        Scene updateAppointment = new Scene(updateAppointmentParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(updateAppointment);
        stage.show();
        
       UpdateAppointmentFXMLController controller = loader.getController();
       controller.loadCustomer(customer);
       controller.loadAppointment(appointment);
    }    
   }
   
   //Clears textfields in Add Customer Tab
   @FXML
   private void clearCustomer(ActionEvent event){
       addcustomername.clear();
       addcustomerphone.clear();
       addcustomerstreetaddress1.clear();
       addcustomerstreetaddress2.clear();
       addcustomercity.clear();
       addcustomercountry.clear();
       addcustomerpostal.clear();
       addcustomererrorlabel.setText("");
   } 
    //Add Customer Tab -- Create Customer Function
   @FXML
   private void createCustomer(ActionEvent event) throws IOException, SQLException, Exception{
       
       if(customerIsValid()){
          String name = addcustomername.getText();
          String phone = addcustomerphone.getText();
          String address1 = addcustomerstreetaddress1.getText();
          String address2 = addcustomerstreetaddress2.getText();
          String city = addcustomercity.getText();
          String country = addcustomercountry.getText();
          int postal = Integer.parseInt(addcustomerpostal.getText());
           Customer newCustomer = new Customer(name,address1,address2,city,postal,country,phone);
           if(Customer.customerIsNotDuplicate(newCustomer)){
           Customer.sendCustomerToDB(newCustomer);
           customerAddedLabel.setText("Customer added successfully!");
           }
           else{
               addcustomererrorlabel.setText("Customer already exists in database");
           }
       }
       else{   
       }
   }

   //Checks add customer tab's text field entries for valid data types and controls error labels
     private boolean customerIsValid(){
     int errorCount = 0;
         if(addcustomername.getText().isEmpty() || addcustomername.getText().length() > 255){
             addcustomernameerrorlabel.setText("Invalid Customer Name");
             errorCount+=1;
         }
         if(!phoneIsValid(addcustomerphone.getText())){
            addcustomerphoneerrorlabel.setText("Invalid Phone Number");
             errorCount+=1;
         }
         if(addcustomerstreetaddress1.getText().length()<2 ||addcustomerstreetaddress1.getText().length() > 255 ){
           addcustomeraddresserrorlabel.setText("Invalid Address");
           errorCount+=1;
         }
         if(addcustomercountry.getText().length()<2 || !containsOnlyLetters(addcustomercountry.getText())){
             addcustomercountryerrorlabel.setText("Invalid Country");
             errorCount+=1;
         }
         if(addcustomercity.getText().length()<2 || !containsOnlyLetters(addcustomercity.getText())){
             addcustomercityerrorlabel.setText("Invalid City");
             errorCount+=1;
         }
         if(addcustomerpostal.getText().length()<5 || !containsOnlyNumbers(addcustomerpostal.getText())){
             addcustomerpostalerrorlabel.setText("*postal code is invalid");
             errorCount+=1;
         }

    if(errorCount>0){
        return false;
    }         
    else{         
       return true;
    }
   }
   
    //Checks for valid phone number format in US and UK format.
     private boolean phoneIsValid(String string){
         Pattern usFormat = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
         Pattern ukFormat = Pattern.compile("\\d{2}-\\d{4}-\\d{6}");
         Matcher usMatch = usFormat.matcher(string);
         Matcher ukMatch = ukFormat.matcher(string);
         if(usMatch.matches()|| ukMatch.matches()){
             return true;
         }
         else{
             return false;
         }
     }
     //Checks string for numbers
     private boolean containsOnlyLetters(String string){
         int numbers = 0;
         for(int i = 0;i<string.length();i++){
             String test = String.valueOf(string.charAt(i));
             int tested;
             try{tested = Integer.parseInt(test);
                 numbers++;
             }
             catch(NumberFormatException e){
             }
         }
         if(numbers>0){
         return false;
         }
         else{
             return true;
          }
     }
       private boolean containsOnlyNumbers(String string){
         for(int i = 0;i<string.length();i++){
             String test = String.valueOf(string.charAt(i));
             int tested;
             try{tested = Integer.parseInt(test);
             }
             catch(NumberFormatException e){
                 return false;
             }
             }
        return true;
         }
  
       //Search for customer in DB and adds match to search into tabelview
       @FXML
       private void searchCustomers(ActionEvent event) throws SQLException, Exception{
           searchCustomers.clear();
           existingapointmenttableview.getItems().clear();
           updateSearchCustomersTable();
           String searchTerm = addappointmentsearchcustomerfield.getText();  
           if(!searchTerm.isEmpty()){
           ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".customer WHERE customerId LIKE '"+searchTerm+"%' OR LOWER(customerName) LIKE '"+searchTerm.toLowerCase()+"%'");
           while(results.next()){
               if(results.getString("customerName").toLowerCase().contains(searchTerm.toLowerCase()) || String.valueOf(results.getInt("customerId")).contains(searchTerm)){
               Customer searchedCustomer = new Customer();
               searchedCustomer.setCustomerName(results.getString("customerName"));
               searchedCustomer.setCustomerID(results.getInt("customerId"));
               searchCustomers.add(searchedCustomer);
           }
           updateSearchCustomersTable();
           }
           }
       }
       @FXML
       private void viewAppointments(ActionEvent event) throws SQLException, Exception{
          existingapointmenttableview.getItems().clear();
          Customer customer = searchcustomertableview.getSelectionModel().getSelectedItem();
          if(customer != null){
          ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".appointment WHERE customerId = "+customer.getCustomerID()+"");
          while(results.next()){
           Customer.customerAppointments.add(Appointment.createAppointmentFromDB(results));
          }
           existingapointmenttableview.setItems(Customer.customerAppointments);
           existingappointmentid.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
           existingappointmentdate.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
           existingappointmenttype.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
           existingappointmentlocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
       }
          if(existingapointmenttableview.getItems().isEmpty()){
              noAppointmentsFound.setText("No appointments for this client");
          }
       }
       private void updateSearchCustomersTable(){
           searchcustomertableview.setItems(searchCustomers);
           addappointmentcustomerid.setCellValueFactory(new PropertyValueFactory<>("customerID"));
           addappointmentcustomername.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            
       }

       @FXML
       private void deleteAppointment(ActionEvent event) throws Exception{
            Appointment appointment = existingapointmenttableview.getSelectionModel().getSelectedItem(); 
            if(existingapointmenttableview.getSelectionModel().getSelectedItem().equals(appointment)){
            DBConnect.updateDB("DELETE FROM "+DBConnect.username+".appointment WHERE appointmentId = "+appointment.getAppointmentID()+" ");
             existingapointmenttableview.getItems().clear();
            viewAppointments(event);
            }
       }
       
       @FXML
       private void updateCustomer(ActionEvent event) throws IOException, Exception{
           Customer customer = searchcustomertableview.getSelectionModel().getSelectedItem();
           if(customer != null){
              Parent addAppointmentParent;
              FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewClientFileFXML.fxml"));
              addAppointmentParent = loader.load();
              Scene addAppointment = new Scene(addAppointmentParent);
              Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
              stage.setScene(addAppointment);
              stage.show();
        
              ViewClientFileFXMLController controller = loader.getController();
              controller.loadCustomer(customer); 
           }
       }
       
       @FXML
       private void showCalendar(ActionEvent event) throws Exception{
              LocalDateTime userTime = LocalDateTime.now();
           if(!calendarChoiceBox.getValue().toString().isEmpty()){
               if(String.valueOf(calendarChoiceBox.getValue()).equalsIgnoreCase("This Week")){
                   Appointment.loadWeeksAppointments(userTime);
                   calendarTable.setItems(Appointment.appointmentCalendar);
                   calendarAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                   calendarCustID.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));
                   calendarDate.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
                   calendarLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
               }
                if(String.valueOf(calendarChoiceBox.getValue()).equalsIgnoreCase("This Month")){
                   Appointment.loadMonthsAppointments(userTime);
                   calendarTable.setItems(Appointment.appointmentCalendar);
                   calendarAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                   calendarCustID.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));
                   calendarDate.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
                   calendarLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
                }
               }
           
       }
       @FXML
       private void showConsultantSchedule(ActionEvent event) throws Exception{
           Appointment.appointmentCalendar.clear();
           if(!selectconsultant.getValue().toString().isEmpty()){
               ResultSet appointmentResults = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".appointment WHERE LOWER(contact) = '"+selectconsultant.getValue().toString().toLowerCase()+"'");
               while(appointmentResults.next()){
                   Appointment.appointmentCalendar.add(Appointment.createAppointmentFromDB(appointmentResults));
               }
                     consultantappointmentstable.setItems(Appointment.appointmentCalendar);
                     consultantappointmentid.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                     consultantappointmencustomerID.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));
                     consultantappointmentstart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
                     consultantappointmentend.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
                     consultantappointmenttype.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
           }
       }
       @FXML
       private void showAppointmentTypeByMonth(ActionEvent event) throws Exception{
         if(!selectmonth.getValue().toString().isEmpty()){  
          Appointment.appointmentCalendar.clear();
          LocalDateTime userTime = LocalDateTime.now();
          String year = String.valueOf(userTime.getYear());
          String month = TimeDate.stringMonthNameToNumber(selectmonth.getValue().toString());
          Appointment.loadMonthsAppointments(TimeDate.stringToDateTime(year+"/"+month+"/01 00:00"));
          int phoneCount = 0;
          int officeCount = 0;
          for(int i = 0; i < Appointment.appointmentCalendar.size(); i++){
              if(Appointment.appointmentCalendar.get(i).getAppointmentType().equalsIgnoreCase("phone")){
                  phoneCount++;
              }
              else {
                  officeCount++;
              }
          }
          ObservableList<String> displayList = FXCollections.observableArrayList();
          displayList.add("Phone Appointments: "+phoneCount+"");
          displayList.add("Office Visit Appoitments: "+officeCount+"");
          appointmentTypeListview.setItems(displayList);
          
       }
    }
       
    @FXML
    private void showAppointmentsByLocation(ActionEvent event) throws Exception{
           Appointment.appointmentCalendar.clear();
        if(!selectLocation.getValue().toString().isEmpty()){
           Appointment.loadAppointmentsByLocation(selectLocation.getValue().toString());
           appointmentsbylocationtable.setItems(Appointment.appointmentCalendar);
           appointmentsByLocationAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
           appointmentsByLocationCustomerID.setCellValueFactory(new PropertyValueFactory<>("apptCustomerID"));
           appointmentsByLocationDate.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
           appointmentsByLocationType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
       
        }
    }
    @FXML
    private void updateAppointmentFromCalendar(ActionEvent event) throws Exception{
        Appointment appointment = calendarTable.getSelectionModel().getSelectedItem();
        if(appointment != null){
        ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".customer WHERE customerId = "+appointment.getApptCustomerID()+"");
        Customer customer = Customer.createCustomerFromDB(results);
        customer.addAppointment(appointment);
        Parent updateAppointmentParent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAppointmentFXML.fxml"));
        updateAppointmentParent = loader.load();
        Scene updateAppointment = new Scene(updateAppointmentParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(updateAppointment);
        stage.show();
       UpdateAppointmentFXMLController controller = loader.getController();
       controller.loadCustomer(customer);
       controller.loadAppointment(appointment);
        
    }
      
    }
    @FXML
    private void updateAppointmentFromConsultantSchedule(ActionEvent event) throws Exception{
        Appointment appointment = consultantappointmentstable.getSelectionModel().getSelectedItem();
        if(appointment != null){
        ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".customer WHERE customerId = "+appointment.getApptCustomerID()+"");
        Customer customer = Customer.createCustomerFromDB(results);
        customer.addAppointment(appointment);
        Parent updateAppointmentParent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAppointmentFXML.fxml"));
        updateAppointmentParent = loader.load();
        Scene updateAppointment = new Scene(updateAppointmentParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(updateAppointment);
        stage.show();
       UpdateAppointmentFXMLController controller = loader.getController();
       controller.loadCustomer(customer);
       controller.loadAppointment(appointment);
    }
    }
    @FXML
    private void updateAppointmentFromLocation(ActionEvent event) throws Exception{
        Appointment appointment = appointmentsbylocationtable.getSelectionModel().getSelectedItem();
        if(appointment != null){
        ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".customer WHERE customerId = "+appointment.getApptCustomerID()+"");
        Customer customer = Customer.createCustomerFromDB(results);
        customer.addAppointment(appointment);
        Parent updateAppointmentParent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateAppointmentFXML.fxml"));
        updateAppointmentParent = loader.load();
        Scene updateAppointment = new Scene(updateAppointmentParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(updateAppointment);
        stage.show();
       UpdateAppointmentFXMLController controller = loader.getController();
       controller.loadCustomer(customer);
       controller.loadAppointment(appointment);
    }
    }
    
    @FXML
    private void deleteCustomer(ActionEvent event)throws Exception{
       Customer customer = searchcustomertableview.getSelectionModel().getSelectedItem();
       Customer.deleteCustomerFromDB(customer);
       searchCustomers.clear();
       searchCustomers(event);
    }
    
    @FXML
    private void scheduleToTextFile(ActionEvent event) throws IOException{
         FileWriter fwriter = new FileWriter("Schedule.txt", true);
         PrintWriter output = new PrintWriter(fwriter);
        //THIS IS FOR REQUIREMENT G: LAMBDA EXPRESSION  - USED LAMBDA EXPRESSION TO EFFICIECTLY REPLACE FOR-LOOP TO PRINT SCHEDULE TO TEXTFILE  
         Appointment.appointmentCalendar.forEach((i) -> {
         output.println("Appt ID: "+i.getAppointmentID()+" CustID:"+i.getApptCustomerID()+" Location:"+ i.getAppointmentLocation()+" Start:"+ i.getAppointmentStart()+" Type:"+ i.getAppointmentType());
});
          output.close();
        
    }
}
