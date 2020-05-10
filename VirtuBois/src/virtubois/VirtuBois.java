/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois;

import javax.swing.JFrame;

/**
 *
 * @author maximeprieur
 */
public class VirtuBois 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
        virtubois.gui.MainWindow mainWindow = new virtubois.gui.MainWindow();
        mainWindow.setExtendedState(mainWindow.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        mainWindow.setVisible(true);    
    }
    
}
