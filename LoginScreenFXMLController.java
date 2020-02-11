/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kevin
 */
public class LoginScreenFXMLController implements Initializable {
    @FXML
    private Label userLabel;
    
    @FXML
    private Button loginButton;
            
    @FXML
    private Label passwordLabel;
    
    @FXML
    private TextField userNameField;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label userErrorLabel;
    
    @FXML
    private Button exitButton;
    
   
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    ResourceBundle cb = ResourceBundle.getBundle("Nat", Locale.getDefault());
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        if(Locale.getDefault().getLanguage().equals("fr")){
            
            userLabel.setText(cb.getString("UserName")+":");
            passwordLabel.setText(cb.getString("Password")+":");
            loginButton.setText(cb.getString("Log"));
            exitButton.setText(cb.getString("Exit"));
         
                
        }

    }    
     @FXML
   private void exitProgram(ActionEvent event){
        
        System.exit(0);
    }
     
    
    
    @FXML
    private void logIn(ActionEvent event) {
        userErrorLabel.setText("");
        passwordErrorLabel.setText("");
       if(fieldsArePopulated()){ 
            try {
                if(Login.userNameFound(userNameField.getText())){
                    if(Login.passwordMatch(userNameField.getText(), passwordField.getText())){
                        Login user = new Login(userNameField.getText());
                        Parent mainParent;
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPageFXML.fxml"));
                        mainParent = loader.load();
                        Scene mainScene = new Scene(mainParent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(mainScene);
                        stage.show();
                        Login.addLoginToLog();
                         try {
                          if(Appointment.appointmentAlert()){
           
                         //THIS IS FOR REQUIREMENT G: LAMBDA EXPRESSION  - USED LAMBDA EXPRESSION TO EFFICIECTLY CREATE APPOINTMENT ALERT POP UP   
                         Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                         "You have an appointment at "+String.valueOf(Appointment.alertAppointments.get(0).getAppointmentStart())+"\n With Customer ID#"+Appointment.alertAppointments.get(0).getApptCustomerID()+".\n Please view schedule for details or to update information." ,
                          ButtonType.OK);
                          alert.showAndWait()
                          .filter(res -> res == ButtonType.OK);      
                           }
                            } catch (Exception ex) {
                            Logger.getLogger(MainPageFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        
                    }
                    else{
                        if(Locale.getDefault().getLanguage().equals("fr")){
                         passwordErrorLabel.setText(cb.getString("Password")+ " "+cb.getString("is")+" "+cb.getString("incorrect"));
                        }
                        else{
                        passwordErrorLabel.setText("Password is incorrect");
                        }
                    }
                }
                else{
                    if(Locale.getDefault().getLanguage().equals("fr")){
                     userErrorLabel.setText(cb.getString("UserName")+" "+cb.getString("not")+" "+cb.getString("found"));
                    }
                    else{
                    userErrorLabel.setText("UserName not found");
                    }
                }   } catch (Exception ex) {
                Logger.getLogger(LoginScreenFXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
    
    private Boolean fieldsArePopulated(){
        int error = 0;
        if(userNameField.getText().length()<1){
            if(Locale.getDefault().getLanguage().equals("fr")){
               userErrorLabel.setText(cb.getString("UserName")+" " +cb.getString("required")); 
                error++;
            }
            else{
            userErrorLabel.setText("UserName required");
            error++;
            }
        }
        if(passwordField.getText().length() < 1){
            if(Locale.getDefault().getLanguage().equals("fr")){
            passwordErrorLabel.setText(cb.getString("Password")+" "+cb.getString("required"));
              error++;
            }
            else{
            passwordErrorLabel.setText("Password required");
            error++;
            }
        }
        if(error > 0){
            return false;
        }
        else{
            return true;
        }
    }
  
        
}
