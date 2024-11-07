package Soal1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import connection.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
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
//                            new UserDashboard();
                        } else if (role.equals("staff")) {
                            JOptionPane.showMessageDialog(this, "Login staff berhasil");
//                            new StaffDashboard();
                        }
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid email or password!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginForm().setVisible(true);
    }
}
