/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FApp;

/**
 *
 * @author franq
 */

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class Splash {
    

    private JLabel SplashImage;
    private JLabel SplashText;
    private JWindow window;
    private JPanel panel;
    public Splash(int duration) {
        window=new JWindow();               
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setBounds((screen.width-881)/2,(screen.height-388)/2,881,388); 
        
        
        panel=(JPanel)window.getContentPane();
        SplashImage = new JLabel(new ImageIcon(ClassLoader.getSystemResource("images/splash.png")));
        SplashText=new JLabel("Loading data From www.nse.co.ke",SwingConstants.CENTER);
        panel.add(SplashImage, BorderLayout.CENTER);
        panel.add(SplashText,BorderLayout.SOUTH);
        
        
        window.setVisible(true);
        try{
            Thread.sleep(duration);            
        }catch(Exception ex){            
        }//try catch closed
        window.setVisible(false);
        window.dispose();
    }//constructr closed

}//class closed



