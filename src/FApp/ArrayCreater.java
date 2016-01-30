/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//this class creates an array list from the data pulled from the website and calls the connectdb class to update the database


package FApp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;
//import java.util.Arrays;

/**
 *
 * @author franq
 */
public class ArrayCreater {
    protected static String thestring;
    private static ArrayList<ArrayList<String>> mainarraylist;
    private static ArrayList<String> node;
    private static double gain;
    private static DecimalFormat df;
    private static ArrayList<String> changearraylist;
    public static int stringcheck;
    
    public ArrayCreater()
    {
        //constructor
    }
    
    public static void getdata(){
        
    //get the content string from the datapull class
    thestring = datapull.maindatapull();
    mainarraylist = new ArrayList<ArrayList<String>>();
    changearraylist = new ArrayList<String>();
    node = new ArrayList<String>();
    df  = new DecimalFormat("#.00");
    if(thestring == null||"".equals(thestring))
    {
        stringcheck = 0;
        JOptionPane.showMessageDialog(null, "The Application wasnt able to connect to the internet.\n" +
                        "Please check your Connection settings and try again", "Failed", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
    else{
        stringcheck = 1;
        //split the string and add all the substrings to an array datatype
        for (String retval: thestring.split(",,")){
        boolean addAl = node.addAll(Arrays.asList(retval.split(",")));
            
        }
        mainarraylist.add(node);
        
    
        
        //calculating the percentage changes and stored in a different array list
        for(int n=1; n<mainarraylist.get(0).size()-1;n+=3)
        {
            
            
            String f = calculatePercentage(Double.parseDouble(mainarraylist.get(0).get(n)),Double.parseDouble(mainarraylist.get(0).get(n+1)));
            changearraylist.add(f);
           
            
      }
        
        ConnectDB cbobj = new ConnectDB(mainarraylist, changearraylist);
        
      
  
    }
    }
    
    
    private static String calculatePercentage(double last, double prev)
    {
        gain = ((last - prev)/prev)*100;
        String string = df.format(gain);
       
        
        return string;   
    }
    
}
