package Soal1;

import connection.DatabaseConnection;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginPage extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    Connection cn = DatabaseConnection.getConnection();

    public LoginPage() {
        setTitle("Login - Cinema System");
        setLayout(null);
        setSize(400, 250);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 50, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 50, 200, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                loginUser(email, password);
            }
        });

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loginUser(String email, String password) {
        try (Connection conn = cn) {

            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password");
                String role = rs.getString("role");
                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    if (role.equals("user")) {
                        JOptionPane.showMessageDialog(this, "Login user berhasil");
//                    new UserDashboard();
                    } else if (role.equals("staff")) {
                        JOptionPane.showMessageDialog(this, "Login staf berhasil");
//                    new StaffDashboard();
                    }
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "Invalid email or password!");
                }
                // Close login page
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
