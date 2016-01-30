/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FApp;

import java.awt.EventQueue;
import javax.swing.JOptionPane;

/**
 *
 * @author franq
 */




public class DBCheck implements Runnable{
    public void run()
    {
         if(ConnectDB.isThereTable(ConnectDB.getDate())==false)
            {
                 new LoadData();
            }
         else {
                if(ArrayCreater.stringcheck==0)
                {
                    
                }
              }
    }
    public DBCheck()
    {
       this.run();
    }
    
    
    
    public static int totaldays;
    public  static int predictdays;
}
