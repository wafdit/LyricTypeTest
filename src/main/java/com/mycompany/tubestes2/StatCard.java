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
public class StatCard extends RoundedPanel {
    public StatCard(String title, JLabel valueLabel) {
        super(25, Theme.BG); 
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BG_DARK, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        valueLabel.setForeground(Theme.TEXT_MAIN);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setForeground(Theme.TEXT_MUTED);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(valueLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(titleLabel);
        add(Box.createVerticalGlue());
    }
}
