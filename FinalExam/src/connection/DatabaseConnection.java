package connection;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    // Mengubah tipe menjadi java.sql.Connection
    public static java.sql.Connection getConnection(){
        try{
            // Memastikan Driver JDBC sudah dimuat
            Class.forName("com.mysql.jdbc.Driver");
            // Membuat koneksi ke database
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bioskop", "root", "");
            return conn;
        } catch (Exception e){
            // Menampilkan pesan kesalahan jika gagal
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
