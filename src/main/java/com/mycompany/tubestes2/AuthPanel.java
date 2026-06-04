/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes2;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author salma
 */
public class AuthPanel extends UIComponent {
    private JTextField userField;
    private JPasswordField passField;
    private RoundedButton loginBtn;
    private RoundedButton registerBtn;
    private RoundedButton cancelBtn;
    private JLabel statusLabel;

    public AuthPanel(java.util.function.BiConsumer<String, String> onLogin, 
                     java.util.function.BiConsumer<String, String> onRegister,
                     Runnable onCancel) {
        super(Theme.BG);
        setLayout(new GridBagLayout());

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JLabel title = new JLabel("Akun LyricType");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.TEXT_MAIN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        userField = new JTextField(15);
        passField = new JPasswordField(15);
        
        statusLabel = new JLabel("Silakan masuk atau daftarkan akun baru.");
        statusLabel.setForeground(Theme.ACCENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBtn = new RoundedButton("Login");
        registerBtn = new RoundedButton("Register");
        cancelBtn = new RoundedButton("Kembali");

        loginBtn.addActionListener(e -> onLogin.accept(userField.getText(), new String(passField.getPassword())));
        registerBtn.addActionListener(e -> onRegister.accept(userField.getText(), new String(passField.getPassword())));
        cancelBtn.addActionListener(e -> onCancel.run());

        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(createFieldPanel("Username:", userField));
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(createFieldPanel("Password:", passField));
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(loginBtn);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(registerBtn);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(cancelBtn);
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(statusLabel);

        add(container);
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(Theme.TEXT_MAIN);
        p.add(lbl);
        p.add(field);
        return p;
    }

    public void setStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setForeground(isError ? Theme.HIGHLIGHT_ERR : Theme.ACCENT);
    }

    public void clearFields() {
        userField.setText("");
        passField.setText("");
    }

    @Override
    public void render(Graphics g) {}
}
