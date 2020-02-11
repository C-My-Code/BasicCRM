/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kevin
 */
public class ViewClientFileFXMLController implements Initializable {

    
    @FXML
    private TextField customerphone;

    @FXML
    private TextField customercountry;

    @FXML
    private TextField customerpostal;

    @FXML
    private TextField customerid;

    @FXML
    private TextField customercity;

    @FXML
    private TextField customeraddress2;

    @FXML
    private TextField customername;

    @FXML
    private TextField customeraddress1;
    @FXML
    private Label customernameerrorlabel;
    @FXML 
    private Label customerphoneerrorlabel;
    @FXML
    private Label customeraddresserrorlabel;
    @FXML
    private Label customercityerrorlabel;
    @FXML
    private Label customercountryerrorlabel;
    @FXML
    private Label customerpostalerrorlabel;
    @FXML
    private Label successLabel;
    
    
    
    
    private Customer customer;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    public void goBack(ActionEvent event) throws IOException{
        Parent goBackParent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPageFXML.fxml"));
        goBackParent = loader.load();
        Scene addPartScene = new Scene(goBackParent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(addPartScene);
        stage.show();
    }
    
    public void loadCustomer(Customer customer) throws Exception{
        this.customer = customer;
        ResultSet results = DBConnect.queryDB("SELECT address.address, address.address2,\n" +
" address.postalCode, address.phone, city.city, country.country\n" +
" FROM U05YHb.customer INNER JOIN U05YHb.address ON customer.addressId = address.addressId INNER JOIN U05YHb.city ON address.cityId = city.cityId \n" +
" INNER JOIN U05YHb.country ON city.countryId = country.countryId WHERE customer.customerId = "+customer.getCustomerID()+";");
        results.first();
        customer.setCustomerStreetAddress1(results.getString("address"));
        customer.setCustomerStreetAddress2(results.getString("address2"));
        customer.setCustomerPostal(results.getInt("postalCode"));
        customer.setCustomerPhone(results.getString("phone"));
        customer.setCustomerCity(results.getString("city"));
        customer.setCustomerCountry(results.getString("country"));
        customerid.setText(String.valueOf(customer.getCustomerID()));
        customername.setText(customer.getCustomerName());
        customerphone.setText(customer.getCustomerPhone());
        customercountry.setText(customer.getCustomerCountry());
        customerpostal.setText(String.valueOf(customer.getCustomerPostal()));
        customercity.setText(customer.getCustomerCity());
        customeraddress1.setText(customer.getCustomerStreetAddress1());
        customeraddress2.setText(customer.getCustomerStreetAddress2());
        
    }
    @FXML
    private void saveCustomer(ActionEvent event) throws Exception{
        if(customerIsValid()){
       customer.setCustomerName(customername.getText());
       customer.setCustomerStreetAddress1(customeraddress1.getText());
       customer.setCustomerStreetAddress2(customeraddress2.getText());
       customer.setCustomerPostal(Integer.parseInt(customerpostal.getText()));
       customer.setCustomerPhone(customerphone.getText());
       customer.setCustomerCity(customercity.getText());
       customer.setCustomerCountry(customercountry.getText());
       Customer.updateCustomerInDB(customer);
       successLabel.setText("Customer successfully updated!");
    }
        
    }
     private boolean customerIsValid(){
     int errorCount = 0;
         if(customername.getText().isEmpty() || customername.getText().length() > 255){
             customernameerrorlabel.setText("Invalid Customer Name");
             errorCount+=1;
         }
         if(!phoneIsValid(customerphone.getText())){
            customerphoneerrorlabel.setText("Invalid Phone Number");
             errorCount+=1;
         }
         if(customeraddress1.getText().length()<2 ||customeraddress1.getText().length() > 255 ){
           customeraddresserrorlabel.setText("Invalid Address");
           errorCount+=1;
         }
         if(customercountry.getText().length()<2 || !containsOnlyLetters(customercountry.getText())){
             customercountryerrorlabel.setText("Invalid Country");
             errorCount+=1;
         }
         if(customercity.getText().length()<2 || !containsOnlyLetters(customercity.getText())){
             customercityerrorlabel.setText("Invalid City");
             errorCount+=1;
         }
         if(customerpostal.getText().length()<5 || !containsOnlyNumbers(customerpostal.getText())){
             customerpostalerrorlabel.setText("*postal code is invalid");
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
}
