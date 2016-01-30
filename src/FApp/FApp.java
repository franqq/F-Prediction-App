/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FApp;

/**
 *
 * @author franq
 */

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;


public class FApp extends Thread{
    



    private final JFrame frame;
    public FApp(JFrame frm){
        this.frame=frm;
    }//constructor closed

    protected FApp(LoginScreen loginScreen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void run(){
        frame.setVisible(true);
    }//run method closed
    public static void main(String args[]){
        
        Runnable r1 = new Runnable() {

            @Override
            public void run() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                new DBCheck();
            }
        };
        
        Runnable r2 = new Runnable() {

            @Override
            public void run() {
               //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                new Splash(2000);
            }
        };
        
        
         Runnable r3 = new Runnable() {

            @Override
            public void run() {
               //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
                new FApp(new LoginScreen());
            }
        };
        r1.run();
        r2.run();
        r3.run();
        
    }//main method closed

}//class closed


