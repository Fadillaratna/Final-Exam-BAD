package Soal1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import connection.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class RegisterForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton loginButton;

    public RegisterForm() {
        setTitle("Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        roleComboBox = new JComboBox<>(new String[]{"user", "staff"});
        registerButton = new JButton("Register");
        loginButton = new JButton("Already have an account? Login");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));  // Adjust grid layout for extra button
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);
        panel.add(registerButton);
        panel.add(loginButton);

        add(panel);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close register form
                new LoginForm().setVisible(true);  // Open login form
            }
        });
    }

    private void registerUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        // Hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String query = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, role);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new LoginForm().setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegisterForm().setVisible(true);
    }
}

