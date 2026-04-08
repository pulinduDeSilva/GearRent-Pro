package edu.ijse.lk.ui;

import edu.ijse.lk.controller.AuthController;
import edu.ijse.lk.dto.UserDTO;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final AuthController authController = new AuthController();

    public LoginFrame() {
        setTitle("GearRent Pro - Login");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Username"), gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> login());
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(loginBtn, gbc);

        add(panel);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.");
            return;
        }

        try {
            UserDTO user = authController.login(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
                return;
            }
            dispose();
            new MainUi(user).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
