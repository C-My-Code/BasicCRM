/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kevin
 */
public class UpdateAppointmentFXMLController implements Initializable {
   
    private static final ObservableList<String> locationsList = FXCollections.observableArrayList("Phoenix", "New York", "London");
    private static final ObservableList<String> appointmentTypeList = FXCollections.observableArrayList("Phone", "Office Visit");
    @FXML
    private TextField appointmenturl;

    @FXML
    private TextArea appointmentdescription;

    @FXML
    private Label endErrorLabel;

    @FXML
    private Label urlErrorLabel;

    @FXML
    private TextField endtime;

    @FXML
    private Label startTimeErrorLabel;

    @FXML
    private TextField appointmenttitle;

    @FXML
    private TextField starttime;

    @FXML
    private TextField startdate;

    @FXML
    private Label titleErrorLabel;

    @FXML
    private TextField customerid;

    @FXML
    private Label descriptionErrorLabel;

    @FXML
    private ChoiceBox officelocation;

    @FXML
    private Label startErrorLabel;

    @FXML
    private TextField customername;

    @FXML
    private Label successLabel;

    @FXML
    private ChoiceBox appointmenttype;
    
    private Customer customer;
    private Appointment appointment;


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
     officelocation.setItems(locationsList);
     appointmenttype.setItems(appointmentTypeList);
    } 
    
     public UpdateAppointmentFXMLController(){
        
    }
    @FXML
    private void goBack(ActionEvent event) throws IOException{
        Parent goBackParent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPageFXML.fxml"));
        goBackParent = loader.load();
        Scene addPartScene = new Scene(goBackParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(addPartScene);
        stage.show();
    }
    
     public void loadCustomer(Customer customer){
        this.customer = customer;
        customerid.setText(Integer.toString(customer.getCustomerID()));
        customername.setText(customer.getCustomerName());
    }
     public void loadAppointment(Appointment appointment){
         TimeZone myTimeZone = TimeZone.getDefault();
         String zone = myTimeZone.getID();
         this.appointment = appointment;
         appointmenturl.setText(appointment.getAppointmentURL());
         officelocation.setValue(appointment.getAppointmentLocation());
          appointmenttitle.setText(appointment.getAppointmentTitle());
         appointmenttype.setValue(appointment.getAppointmentType());
         appointmentdescription.setText(appointment.getAppointmentDescription());
         
         Date startDateFromObject = appointment.getAppointmentStart();
         String startString = ""+TimeDate.dateToString(startDateFromObject)+" "+TimeDate.timeToString(startDateFromObject)+"";
         ZonedDateTime userZonedStart = TimeDate.stringToDateTime(startString).atZone(ZoneId.of(zone));
         ZonedDateTime locationZonedStart = userZonedStart.withZoneSameInstant(ZoneId.of(TimeDate.locationID(appointment.getAppointmentLocation())));
         startdate.setText(String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd").format(locationZonedStart)));
         starttime.setText(String.valueOf(DateTimeFormatter.ofPattern("kk:mm").format(locationZonedStart)));
         
         Date endDateFromObject = appointment.getAppointmentEnd();
         String endString = ""+TimeDate.dateToString(endDateFromObject)+" "+TimeDate.timeToString(endDateFromObject)+"";
         ZonedDateTime userZonedEnd = TimeDate.stringToDateTime(endString).atZone(ZoneId.of(zone));
         ZonedDateTime locationZonedEnd = userZonedEnd.withZoneSameInstant(ZoneId.of(TimeDate.locationID(appointment.getAppointmentLocation())));
         endtime.setText(String.valueOf(DateTimeFormatter.ofPattern("kk:mm").format(locationZonedEnd)));
         
     }
     
      private Boolean appointmentIsValid() throws ParseException{
        int errorCount = 0;
      if(!TimeDate.timeInputValid(starttime.getText())){
           startTimeErrorLabel.setText("Invalid format");
           errorCount++;
      } 
      if(!TimeDate.dateInputValid(startdate.getText())){
          startErrorLabel.setText("Invalid format");
          errorCount++;
      }
      if(!TimeDate.timeInputValid(endtime.getText())){
          endErrorLabel.setText("Invalid format");
            errorCount++;
      }
          
      if(appointmenttitle.getText().length() < 5 || appointmenttitle.getText().length()>255){
          titleErrorLabel.setText("Title must be between 5 & 255 characters");
          errorCount++;
      }
      if(!appointmenturl.getText().contains(".") || appointmenturl.getText().length() < 5 || appointmenturl.getText().length() > 255){
          urlErrorLabel.setText("Please insert valid URL between 5 & 255 characters");   
          errorCount++;
      }
      if(appointmentdescription.getText().length() < 5 || appointmentdescription.getText().length() > 500 ){
          descriptionErrorLabel.setText("Description must be between 5 & 500 characters");
          errorCount++;
      }
      if(TimeDate.stringToTime(starttime.getText()).after(TimeDate.stringToTime(endtime.getText()))){
          endErrorLabel.setText("Appointment cannot end before it begins");
          errorCount++;
      }
      if(TimeDate.stringToTime(starttime.getText()).before(TimeDate.getOpeningHours())){
          startErrorLabel.setText("Office opens at 08:00");
          errorCount++;
      }
      if(TimeDate.stringToTime(endtime.getText()).after(TimeDate.getClosingingHours())){
          endErrorLabel.setText("Office closes at 18:00");
          errorCount++;
      }
      if(errorCount > 0){
          return false;
      }
      else{
          return true;
      }
        
    }
        private void clearLabels(){
   
          startErrorLabel.setText("");
          endErrorLabel.setText("");
          titleErrorLabel.setText("");
          urlErrorLabel.setText("");
          descriptionErrorLabel.setText("");
          startTimeErrorLabel.setText("");
    }
         private void clearTextFields(){
        startdate.clear();
        starttime.clear();
        endtime.clear();
        appointmenttitle.clear();
        appointmenturl.clear();
        appointmentdescription.clear();
        
    }
      @FXML
      private void updateAppointment(ActionEvent event) throws Exception{
        clearLabels();
        if(appointmentIsValid()){
        appointment.setAppointmentTitle(appointmenttitle.getText());
        appointment.setAppointmentLocation((String) officelocation.getValue());
        appointment.setAppointmentType((String) appointmenttype.getValue());
         //Convert start time to local to UTC for storage
        String startDateTime = startdate.getText()+" "+starttime.getText();
        ZonedDateTime startZonedLocal = TimeDate.localDateTimeToZoned(TimeDate.stringToDateTime(startDateTime), appointment.getAppointmentLocation());
        ZonedDateTime startZonedUTC = startZonedLocal.withZoneSameInstant(ZoneId.of("UTC"));
        String startConverted = String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm").format(startZonedUTC));
        
        //Convert end time to local to UTC for storage
        String endDateTime = startdate.getText()+" "+endtime.getText();
        ZonedDateTime endZonedLocal = TimeDate.localDateTimeToZoned(TimeDate.stringToDateTime(endDateTime), appointment.getAppointmentLocation());
        ZonedDateTime endZonedUTC = endZonedLocal.withZoneSameInstant(ZoneId.of("UTC"));
        String endConverted = String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm").format(endZonedUTC));
         if(!Appointment.overlappingAppointment(startConverted, endConverted)){
        appointment.setAppointmentURL(appointmenturl.getText());
        appointment.setAppointmentDescription(appointmentdescription.getText());
        Appointment.updateAppointmentInDB(appointment, customer, startConverted, endConverted);
        Customer.customerAppointments.clear();
        successLabel.setText("Success! Appointment Updated.");
        clearTextFields();
         }
         else{
           // successLabel.setTextFill(Color.RED);
            successLabel.setText("Cannot set overlapping appointment\n please check your schedule");
         }
        }
        else{
        }
        }
      
              
    
}
