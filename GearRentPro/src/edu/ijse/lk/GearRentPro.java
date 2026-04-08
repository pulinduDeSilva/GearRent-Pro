/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package edu.ijse.lk;

import edu.ijse.lk.ui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Pulindu
 */



public class GearRentPro {
    public static void main(String[] args) {
        
        //Java swing look and feel found from geekforgeek to improve design
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Look and Feel not set: " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

