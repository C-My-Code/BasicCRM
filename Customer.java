

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
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
public class Customer {
    
    private int customerID;
    private String customerName;
    private String customerStreetAddress1;
    private String customerStreetAddress2;
    private String customerCity;
    private int customerPostal;
    private String customerCountry;
    private String customerPhone;
   @SuppressWarnings("FieldMayBeFinal")
    public static ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
   
     public Customer(){
       
    }
   
     public Customer(String customerName, String customerStreetAddress1, String customerStreetAddress2, String customerCity, int customerPostal, String customerCountry, String customerPhone) {
        this.customerName = customerName;
        this.customerStreetAddress1 = customerStreetAddress1;
        this.customerStreetAddress2 = customerStreetAddress2;
        this.customerCity = customerCity;
        this.customerPostal = customerPostal;
        this.customerCountry = customerCountry;
        this.customerPhone = customerPhone;
    }
   
   

    public Customer(int customerID, String customerName, String customerStreetAddress1, String customerStreetAddress2, String customerCity, int customerPostal, String customerCountry, String customerPhone) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerStreetAddress1 = customerStreetAddress1;
        this.customerStreetAddress2 = customerStreetAddress2;
        this.customerCity = customerCity;
        this.customerPostal = customerPostal;
        this.customerCountry = customerCountry;
        this.customerPhone = customerPhone;
    }

    public static Customer createCustomerFromDB(ResultSet results) throws SQLException{
        Customer customer = new Customer();
        while(results.next()){
        customer.setCustomerID(results.getInt("customerid"));
        customer.setCustomerName(results.getString("customerName"));
        }
        return customer;
    }
        //Checks DB for customer with matching name
       public static boolean customerIsNotDuplicate(Customer customer) throws SQLException{
         Boolean customerIsNotDuplicate = true;
         ArrayList<Customer> customerResults = new ArrayList<>();
         try{
         ResultSet results = DBConnect.queryDB("SELECT LOWER(customerName) FROM customer WHERE LOWER(customerName) LIKE '%"+customer.getCustomerName().toLowerCase()+"%'");
           while(results.next()){
               Customer newCustomer = createCustomerFromDB(results);
               customerResults.add(newCustomer);
           }
          while(!customerResults.isEmpty()){
          for(int i = 0;i<customerResults.size();i++){
              
           if(customer.getCustomerName().toLowerCase() == customerResults.get(i).getCustomerName().toLowerCase())
               customerIsNotDuplicate= false;
           break;
          }
           }
         }
         catch(Exception e){
          return customerIsNotDuplicate;
         }
        return customerIsNotDuplicate;
       }
        
   //Adds Customer To DB
   public static void sendCustomerToDB(Customer customer) throws SQLException, IOException, Exception{
       String custCountry = customer.getCustomerCountry();

       DBConnect.updateDB("INSERT IGNORE INTO "+DBConnect.username+".country(country , createDate , createdBy, lastUpdateBy ) VALUES('"+custCountry+"' , sysdate() , '"+Login.getUserName()+"','"+Login.getUserName()+"' )");
       ResultSet countID = DBConnect.queryDB("SELECT countryid FROM "+DBConnect.username+".country WHERE country = '"+customer.getCustomerCountry()+"' ");
       countID.first();
       int countryID = countID.getInt("countryId");
       DBConnect.updateDB("INSERT IGNORE INTO "+DBConnect.username+".city(city,countryId,createDate,createdBy,lastUpdateBy) VALUES('"+customer.getCustomerCity()+"', "+countryID+", sysdate(), '"+Login.getUserName()+"','"+Login.getUserName()+"')");
       ResultSet citID = DBConnect.queryDB("SELECT cityId FROM "+DBConnect.username+".city WHERE city = '"+customer.getCustomerCity()+"'");
       citID.first();
       int cityID = citID.getInt("cityId");
       DBConnect.updateDB("INSERT IGNORE INTO "+DBConnect.username+".address(address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdateBy) VALUES('"+customer.getCustomerStreetAddress1()+"', '"+customer.getCustomerStreetAddress2()+"', "+cityID+", "+customer.getCustomerPostal()+", '"+customer.getCustomerPhone()+"', sysdate(), '"+Login.getUserName()+"','"+Login.getUserName()+"')");
       ResultSet addID = DBConnect.queryDB("SELECT addressId FROM "+DBConnect.username+".address WHERE address ='"+customer.getCustomerStreetAddress1()+"'");
       addID.first();
       int addressID = addID.getInt("addressId"); 
       DBConnect.updateDB("INSERT INTO "+DBConnect.username+".customer(customerName,addressId, createDate, createdBy,lastUpdateBy) VALUES('"+customer.getCustomerName()+"',"+addressID+", sysdate(),'"+Login.getUserName()+"','"+Login.getUserName()+"')");

   }
   public static void updateCustomerInDB(Customer customer) throws Exception{
       ResultSet results = DBConnect.queryDB("SELECT address.addressId, address.address, address.address2,\n" +
" address.postalCode, address.phone, city.city, country.country, country.countryId " +
" FROM U05YHb.customer INNER JOIN U05YHb.address ON customer.addressId = address.addressId INNER JOIN U05YHb.city ON address.cityId = city.cityId " +
" INNER JOIN U05YHb.country ON city.countryId = country.countryId WHERE customer.customerId = "+customer.getCustomerID()+";");
       results.first();
       DBConnect.updateDB("UPDATE "+DBConnect.username+".customer SET customerName = '"+customer.customerName+"', lastUpdate = sysdate(), lastUpdateBy = '"+Login.getUserName()+"' WHERE customerId = "+customer.customerID+";");
       int newCountryID = 0;
       int newCityID = 0;
       Boolean cityChanged = false;
       Boolean countryChanged = false;
       if(!customer.getCustomerCountry().equalsIgnoreCase(results.getString("country"))){
           countryChanged = true;
           if(countryAlreadyExists(customer.customerCountry)){
               ResultSet countryResult = DBConnect.queryDB("SELECT countryId FROM "+DBConnect.username+".country WHERE LOWER(country) = '"+customer.customerCountry.toLowerCase()+"' ");
               countryResult.first();
               newCountryID = countryResult.getInt("countryId");
           }
           else{
               DBConnect.updateDB("INSERT INTO "+DBConnect.username+".country(country,createDate,createdBy,lastUpdate,lastUpdateBy) VALUES('"+customer.customerCountry+"', sysdate(), '"+Login.getUserName()+"', sysdate(),'"+Login.getUserName()+"')");
               ResultSet countryResult = DBConnect.queryDB("SELECT countryId FROM "+DBConnect.username+".country WHERE LOWER(country) = '"+customer.customerCountry.toLowerCase()+"'");
               countryResult.first();
               newCountryID = countryResult.getInt("countryId");
           }
       }
       if(!customer.customerCity.equalsIgnoreCase(results.getString("city"))){
           cityChanged = true;
           if(cityAlreadyExists(customer.customerCity)){
               ResultSet cityResult = DBConnect.queryDB("SELECT cityId FROM "+DBConnect.username+".city WHERE LOWER(city) = '"+customer.customerCity.toLowerCase()+"'");
               cityResult.first();
               newCityID = cityResult.getInt("cityId");
           }
           else{
               if(countryChanged){
                DBConnect.updateDB("INSERT INTO "+DBConnect.username+".city(city,countryId,createDate,createdBy,lastUpdate,lastUpdateBy) VALUES('"+customer.customerCity+"', "+newCountryID+", sysdate(), '"+Login.getUserName()+"', sysdate(),'"+Login.getUserName()+"'");
               ResultSet cityResult = DBConnect.queryDB("SELECT cityId FROM "+DBConnect.username+".city WHERE LOWER(city) = '"+customer.customerCity.toLowerCase()+"' ");
               cityResult.first();
               newCityID = cityResult.getInt("cityId");
               }
               else{
               DBConnect.updateDB("INSERT INTO "+DBConnect.username+".city(city,countryId,createDate,createdBy,lastUpdate,lastUpdateBy) VALUES('"+customer.customerCity+"', "+results.getInt("countryId")+", sysdate(), '"+Login.getUserName()+"', sysdate(),'"+Login.getUserName()+"')");
               ResultSet cityResult = DBConnect.queryDB("SELECT cityId FROM "+DBConnect.username+".city WHERE LOWER(city) = '"+customer.customerCity.toLowerCase()+"'");
               cityResult.first();
               newCityID = cityResult.getInt("cityId");
               }
           }
       }
       if(cityChanged){
           DBConnect.updateDB("UPDATE "+DBConnect.username+".address SET cityId = "+newCityID+" WHERE addressId = "+results.getInt("addressId")+"");
           
       }
       DBConnect.updateDB("UPDATE "+DBConnect.username+".address SET address = '"+customer.customerStreetAddress1+"', address2 = '"+customer.customerStreetAddress2+"', phone = '"+customer.customerPhone+"', postalCode = '"+customer.customerPostal+"', lastUpdateBy = '"+Login.getUserName()+"', lastUpdate = sysdate() WHERE addressId = "+results.getInt("addressId")+"");
       
    
   }
   
   private static Boolean countryAlreadyExists(String string) throws Exception{
       Boolean found = false;
        ResultSet result = DBConnect.queryDB("SELECT country FROM "+DBConnect.username+".country WHERE LOWER(country) LIKE '"+string.toLowerCase()+"'");
        while(result.next()){
        if(string.equalsIgnoreCase(result.getString("country"))){
         found = true;   
        }
        }
        return found;
   }
       
   private static Boolean cityAlreadyExists(String string) throws Exception{
       Boolean found = false;
        ResultSet result = DBConnect.queryDB("SELECT city FROM "+DBConnect.username+".city WHERE LOWER(city) LIKE '"+string.toLowerCase()+"'");
        while(result.next()){
        if(string.equalsIgnoreCase(result.getString("city"))){
         found = true;   
        }
        }
      return found;
       
   }
   
   public static void deleteCustomerFromDB(Customer customer) throws Exception{
       DBConnect.updateDB("DELETE FROM "+DBConnect.username+".appointment WHERE customerId = "+customer.customerID+"");
       DBConnect.updateDB("DELETE FROM "+DBConnect.username+".address WHERE address = '"+customer.customerStreetAddress1+"'");
       DBConnect.updateDB("DELETE FROM "+DBConnect.username+".customer WHERE customerId = '"+customer.customerID+"'");
   }
   
    public void addAppointment(Appointment appointment){
        customerAppointments.add(appointment);
    }
    public void removeAppointment(Appointment appointment){
        
        customerAppointments.remove(appointment);
    }
    public void setCustomerID(int id){
        this.customerID = id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerStreetAddress1(){
        return this.customerStreetAddress1;
    }
    
    public void setCustomerStreetAddress1(String customerStreetAddress1){
        this.customerStreetAddress1 = customerStreetAddress1;
    }

    public String getCustomerStreetAddress2() {
        return customerStreetAddress2;
    }

    public void setCustomerStreetAddress2(String customerStreetAddress2) {
        this.customerStreetAddress2 = customerStreetAddress2;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public int getCustomerPostal() {
        return customerPostal;
    }

    public void setCustomerPostal(int customerPostal) {
        this.customerPostal = customerPostal;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    
    
 
    
}
