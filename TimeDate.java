
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class TimeDate {
    
    private final Date openingTime = null;
    private final Date closingTime = null;

    

    public static Boolean timeInputValid(String string){
        Pattern timeFormat = Pattern.compile("\\d{2}:\\d{2}");
        Matcher timeFormatMatch = timeFormat.matcher(string);
        if(timeFormatMatch.matches()){
            return true;
        }
        else return false;
    }
    public static Boolean dateInputValid(String string){
        Pattern dateFormat = Pattern.compile("\\d{4}/\\d{2}/\\d{2}");
        Matcher dateFormatMatch = dateFormat.matcher(string);
        if(dateFormatMatch.matches()){
            return true;
        }
        else return false;
    }
    public static String dateToString(Date date){
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");  
       String strDate = formatter.format(date);  
        return strDate;
    }
    public static String timeToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");  
        String strDate = formatter.format(date);  
        return strDate;
        
    }
    public static LocalDateTime stringToDateTime(String string){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime formatDateTime = LocalDateTime.parse(string, formatter);
        
        return formatDateTime;
    }
   
    public static ZonedDateTime localDateTimeToZoned(LocalDateTime localDateTime, String location){
        ZonedDateTime dateTime = null;
        if(location.equalsIgnoreCase("Phoenix")){
             dateTime = localDateTime.atZone(ZoneId.of("America/Phoenix"));
        }
        if(location.equalsIgnoreCase("New York") || location.equalsIgnoreCase("NewYork")){
             dateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
        }
        if(location.equalsIgnoreCase("london")){
            dateTime = localDateTime.atZone(ZoneId.of("Europe/London"));
        }
        if(location.equalsIgnoreCase("UTC")){
            dateTime = localDateTime.atZone(ZoneId.of("UTC"));
        }
        return dateTime;
    }
    
    public static Date stringToDate(String string) throws ParseException{
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
          Date date = formatter.parse(string);
          return date;
    }
    public static Date stringToTime(String string) throws ParseException{
         DateFormat dateFormat = new SimpleDateFormat("kk:mm");
          Date date = dateFormat.parse(string);
      
        return date;
        
    }
    public static String locationID(String string){
        if(string.equalsIgnoreCase("phoenix")){
            return "America/Phoenix";
        }
        if(string.equalsIgnoreCase("New York") || string.equalsIgnoreCase("NewYork")){
            return "America/New_York";
        }
        if(string.equalsIgnoreCase("London")){
            return "Europe/London";
        }
        else return null;
      
    }
    
    public static Date getOpeningHours() throws ParseException{
        Date date = stringToTime("08:00");
        return date;
    }
    public static Date getClosingingHours() throws ParseException{
        Date date = stringToTime("18:00");
        return date;
    }
    public static String stringMonthNameToNumber(String month){
        if(month.equalsIgnoreCase("January")){
           String monthNumber ="01";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("February")){
           String monthNumber ="02";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("March")){
            String monthNumber ="03";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("April")){
            String monthNumber ="04";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("May")){
            String monthNumber ="05";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("June")){
           String monthNumber ="06";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("July")){
          String monthNumber ="07";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("August")){
          String monthNumber ="08";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("September")){
          String monthNumber ="09";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("October")){
          String monthNumber ="10";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("November")){
          String monthNumber ="11";
          return monthNumber;
        }
        if(month.equalsIgnoreCase("December")){
          String monthNumber ="12";
          return monthNumber;
          
        }
        else{
        return null;
        }
        
    }
    
}
