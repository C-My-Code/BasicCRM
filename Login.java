
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Login {
    private static String userName;

    public Login(String user) {
        Login.userName = user;
    }



public static String getUserName(){
return Login.userName;
}



public static Boolean userNameFound(String name) throws Exception{
    Boolean found = false;
    ResultSet results = DBConnect.queryDB("SELECT user.userName FROM "+DBConnect.username+".user WHERE userName = '"+name+"'");
    while(results.next()){
        if(results.getString("userName").contentEquals(name)){
            found = true;
            break;
        }
    }
    return found;
   
}
public static Boolean passwordMatch(String userName, String password) throws Exception{
    Boolean passwordMatch = false;
    ResultSet results = DBConnect.queryDB("SELECT * FROM "+DBConnect.username+".user WHERE userName = '"+userName+"'");
    while(results.next()){
        if(results.getString("password").contentEquals(password)){
            passwordMatch = true;
            break;
        }
    }
        return passwordMatch;
}
public static void addLoginToLog() throws FileNotFoundException, IOException{
    TimeZone myTimeZone = TimeZone.getDefault();
    String zone = myTimeZone.toString();
    FileWriter fwriter = new FileWriter("loginlog.txt", true);
    PrintWriter outputFile = new PrintWriter(fwriter);
    outputFile.println("Log In By User: "+Login.userName+" @ "+ZonedDateTime.now()+"");
    outputFile.close();
}
   
}