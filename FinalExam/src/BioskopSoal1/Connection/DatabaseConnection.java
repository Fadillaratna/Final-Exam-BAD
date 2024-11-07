package BioskopSoal1.Connection;

import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    public static java.sql.Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bioskop", "root", "");
            return conn;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
