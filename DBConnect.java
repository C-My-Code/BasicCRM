
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class DBConnect {
    private static final String databasename = "XXXXXXX";
    public static final String username = "XXXXXX";
    private static final String password = "XXXXXXXX";
    private static final String DB_URL = "XXXXXXXXXX";
    private static final String driver = "com.mysql.jdbc.Driver";
    static Connection conn;
    
    
    public static void connectToDB()throws Exception{
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(DB_URL, username, password);

    }
    
    public static void closeConnection() throws Exception{
        conn.close();
    }
    
    public static ResultSet queryDB(String sql) throws SQLException, Exception{
        connectToDB();
        Statement statement = conn.createStatement();
         ResultSet resultSet = statement.executeQuery(sql);
        // DBConnect.closeConnection();
        return resultSet;
    }
    public static void updateDB(String sql) throws SQLException, Exception{
         connectToDB();
         Statement statement = conn.createStatement();
         statement.executeUpdate(sql);
         //DBConnect.closeConnection();
       
    }
    public static Date getServerTimestamp() throws SQLException, Exception{
   
        ResultSet result = queryDB("SELECT sysdate()");
        Date date = result.getTimestamp(1);
        return date;
    }
    
    
}
