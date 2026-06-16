/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.LyricType;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author salma
 */
public class AuthPanel extends UIComponent {
    private ModernComponents.ModernTextField userField;
    private ModernComponents.ModernPasswordField passField;
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

        JLabel title = new JLabel("Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.TEXT_MAIN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        userField = new ModernComponents.ModernTextField(18);
        passField = new ModernComponents.ModernPasswordField(18);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Theme.ACCENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        loginBtn    = new RoundedButton("Login");
        registerBtn = new RoundedButton("Register");
        cancelBtn   = new RoundedButton("Back");

        loginBtn.addActionListener(e ->
            onLogin.accept(userField.getText(), new String(passField.getPassword())));
        registerBtn.addActionListener(e ->
            onRegister.accept(userField.getText(), new String(passField.getPassword())));
        cancelBtn.addActionListener(e -> onCancel.run());

        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 28)));
        container.add(createFieldPanel("Username", userField));
        container.add(Box.createRigidArea(new Dimension(0, 14)));
        container.add(createFieldPanel("Password", passField));
        container.add(Box.createRigidArea(new Dimension(0, 24)));
        container.add(loginBtn);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(registerBtn);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(cancelBtn);
        container.add(Box.createRigidArea(new Dimension(0, 14)));
        container.add(statusLabel);

        add(container);
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(
            Theme.TEXT_MAIN.getRed(), Theme.TEXT_MAIN.getGreen(), Theme.TEXT_MAIN.getBlue(), 160));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 0));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        statusLabel.setText(" ");
    }

    @Override
    public void render(Graphics g) {}
}
