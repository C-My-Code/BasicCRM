
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Appointment {
    private int appointmentID;
    private int apptCustomerID;
    private String appointmentTitle;
    private String appointmentLocation;
    private String appointmentType;
    private String appointmentContact;
    private String appointmentDescription;
    private String appointmentURL;
    private Date appointmentStart;
    private Date appointmentEnd;
    private String consultant;
   
    public static ArrayList<Appointment> alertAppointments = new ArrayList<>();
    public static ObservableList<Appointment> appointmentCalendar = FXCollections.observableArrayList();

    
    
    public Appointment(){
        
    }

    
    public static Appointment createAppointmentFromDB(ResultSet results) throws SQLException, ParseException{
               Appointment appointment = new Appointment();  
               if(!duplicateAppointment(results.getInt("appointmentId"))){    
               appointment.setAppointmentID(results.getInt("appointmentId")); 
               appointment.setApptCustomerID(results.getInt("customerId"));
               appointment.setAppointmentTitle(results.getString("title"));
               appointment.setAppointmentContact(results.getString("contact"));
               appointment.setAppointmentDescription(results.getString("description"));
             
               //Get start from DB and convert it to users time
               TimeZone myTimeZone = TimeZone.getDefault();
               String zone = myTimeZone.getID();
               Date startFromDB = new Date(results.getTimestamp("start").getTime());
               String startString = ""+TimeDate.dateToString(startFromDB)+" "+TimeDate.timeToString(startFromDB)+"";
               ZonedDateTime startZonedUTC = TimeDate.localDateTimeToZoned(TimeDate.stringToDateTime(startString), "UTC");
               ZonedDateTime startZonedUser = startZonedUTC.withZoneSameInstant(ZoneId.of(zone));
               String startConverted = String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(startZonedUser));
               appointment.setAppointmentStart(TimeDate.stringToDate(startConverted));
               
               //Get end from DB and convert it to users time
               Date endFromDB = new Date(results.getTimestamp("end").getTime());
               String endString = ""+TimeDate.dateToString(endFromDB)+" "+TimeDate.timeToString(endFromDB)+"";
               ZonedDateTime endZonedUTC = TimeDate.localDateTimeToZoned(TimeDate.stringToDateTime(endString), "UTC");
               ZonedDateTime endZonedUser = endZonedUTC.withZoneSameInstant(ZoneId.of(zone));
               String endConverted = String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(endZonedUser));
               appointment.setAppointmentEnd(TimeDate.stringToDate(endConverted));
               
               appointment.setAppointmentLocation(results.getString("location"));
               appointment.setAppointmentType(results.getString("type"));
               appointment.setAppointmentURL(results.getString("url"));
               }
        return appointment;
    } 
    
            public static Boolean overlappingAppointment(String startDate, String endDate) throws Exception{
                Boolean overlap = null;
                ArrayList<Appointment> existingAppointments = new ArrayList<>();
                int overlapping = 0;
                Date start = TimeDate.stringToDate(startDate);
                Date end = TimeDate.stringToDate(endDate);
                ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".appointment WHERE start BETWEEN '"+startDate+"' AND '"+endDate+"' OR end BETWEEN '"+startDate+"' AND '"+endDate+"'");
                while(results.next()){
                    Appointment appointment = new Appointment();
                    appointment.appointmentStart = new Date(results.getTimestamp("start").getTime());
                    appointment.appointmentEnd = new Date(results.getTimestamp("end").getTime());
                    existingAppointments.add(appointment);
                }
                for(int i = 0; i < existingAppointments.size();i++){
                    if(existingAppointments.get(i).appointmentStart.after(start) && existingAppointments.get(i).appointmentStart.before(end)){
                        overlapping+=1;
                    }
                    if(existingAppointments.get(i).appointmentEnd.after(start) && existingAppointments.get(i).appointmentEnd.before(end) )
                        overlapping+=1;
                }
              if(overlapping > 0){
                    return true;
                }
              if(overlapping<1){
                    overlap = false;
        }
           return overlap;
                
            }
            
    public static Boolean appointmentAlert() throws Exception{
                alertAppointments.clear();
                Boolean alert = false;
                TimeZone myTimeZone = TimeZone.getDefault();
                String zone = myTimeZone.getID();
                LocalDateTime userTime = LocalDateTime.now();
                ZonedDateTime zonedLocal = userTime.atZone(ZoneId.of(zone));
                ZonedDateTime zonedUTC = zonedLocal.withZoneSameInstant(ZoneId.of("UTC"));
                String currentTimeConverted = String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm").format(zonedUTC));
                ZonedDateTime fifteenAheadZoned = zonedUTC.plusMinutes(15);
                String fifteenAhead = String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm").format(fifteenAheadZoned));
                ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".appointment WHERE start BETWEEN '"+currentTimeConverted+"' AND '"+fifteenAhead+"'");
                while(results.next()){
                    alertAppointments.add(createAppointmentFromDB(results));
                }
                if(!alertAppointments.isEmpty()){
                    alert = true;
                }
                if(alertAppointments.isEmpty()){
                    alert = false;
                }
        return alert;
    
}
            
             
            
    public static void sendAppointmentToDB(Appointment appointment, Customer customer, String start, String end) throws Exception{
        DBConnect.updateDB("INSERT INTO U05YHb.appointment(customerId,userId,title,description,location,contact,type,url,start,end,createDate,createdBy,lastUpdateBy)"+
                "VALUES('"+customer.getCustomerID()+"', 01, '"+appointment.getAppointmentTitle()+"', '"+appointment.getAppointmentDescription()+"', '"+appointment.appointmentLocation+""
                        + "', '"+Login.getUserName()+"', '"+appointment.getAppointmentType()+"', '"+appointment.getAppointmentURL()+"', '"+start+"', '"+end
                        +"', sysdate(), '"+Login.getUserName()+"','"+Login.getUserName()+"')");
    }
    public static void updateAppointmentInDB(Appointment appointment, Customer customer, String start, String end) throws Exception{{
        DBConnect.updateDB("UPDATE U05YHb.appointment SET title = '"+appointment.getAppointmentTitle()+"', url = '"+appointment.getAppointmentURL()+"', description = '"+appointment.getAppointmentDescription()+
                "', location = '"+appointment.getAppointmentLocation()+"', type = '"+appointment.getAppointmentType()+"', start = '"+start+"', end = '"+end+"', lastUpdate = sysdate(), lastUpdateBy = '"+Login.getUserName()+"' "+
                "WHERE appointmentId = "+appointment.getAppointmentID()+"");
    }

        
    }
    private static Boolean duplicateAppointment(int appointmentId){
        Boolean duplicateFound = false;
        for(int i = 0; i < Customer.customerAppointments.size();i++) {
            if(appointmentId == Customer.customerAppointments.get(i).getAppointmentID()){
                duplicateFound = true;
                break;
            }
        }
        return duplicateFound;
    }
    public static void loadWeeksAppointments(LocalDateTime date) throws Exception{
        appointmentCalendar.clear();
        String dateWeek = String.valueOf(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm").format(date));
        ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".appointment WHERE WEEK(DATE(start)) = WEEK('"+dateWeek+"')");
        while(results.next()){
            appointmentCalendar.add(createAppointmentFromDB(results)); 
        }
    }
    public static void loadMonthsAppointments(LocalDateTime date) throws Exception{
        appointmentCalendar.clear();
        String month = String.valueOf(date.getMonthValue());
        ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".appointment WHERE MONTH(start) = '"+month+"'");
        while(results.next()){
            appointmentCalendar.add(createAppointmentFromDB(results)); 
        }
    }
    public static void loadAppointmentsByLocation(String location) throws Exception{
        ResultSet results =DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".appointment WHERE LOWER(location) = '"+location.toLowerCase()+"'");
        while(results.next()){
            appointmentCalendar.add(createAppointmentFromDB(results));
        }
    }
    public void setAppointmentID(int id){
        this.appointmentID = id;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAppointmentContact() {
        return appointmentContact;
    }

    public void setAppointmentContact(String appointmentContact) {
        this.appointmentContact = appointmentContact;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getAppointmentURL() {
        return appointmentURL;
    }

    public void setAppointmentURL(String appointmentURL) {
        this.appointmentURL = appointmentURL;
    }

    public Date getAppointmentStart() {
        return appointmentStart;
    }

    public void setAppointmentStart(Date appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    public Date getAppointmentEnd() {
        return appointmentEnd;
    }

    public void setAppointmentEnd(Date appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }
    
    public String getConsultant(){
        return this.consultant;
    }

    void setAppointmentID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getApptCustomerID() {
        return apptCustomerID;
    }

    public void setApptCustomerID(int apptCustomerID) {
        this.apptCustomerID = apptCustomerID;
    }
   
    
    
    
}
