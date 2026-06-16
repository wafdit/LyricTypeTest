/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tubestes2;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
/**
 *
 * @author salma
 */
public abstract class UIComponent extends JPanel {
    protected Color bgColor;

    public UIComponent(Color bgColor) {
        this.bgColor = bgColor;
        setOpaque(false);
    }

    
    public abstract void render(Graphics g);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }
}
