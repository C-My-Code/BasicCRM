


import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Main extends Application{

    /**
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreenFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
   
    }
    
    
    private static void main(String[] args)throws Exception{
       
        DBConnect.connectToDB();
        launch(args);
       DBConnect.closeConnection();
        
    }  
    
}
