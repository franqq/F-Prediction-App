/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//This class reads data from the database and does some calculations

package FApp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franq
 */
public class ReadandCalculate {
    
    protected static ArrayList<ArrayList<String>> gained;
    private static ArrayList<String> eachGained;
    protected static ArrayList<ArrayList<String>> lost;
    private static ArrayList<String> eachLost;
    //variable for number of companies required for both the gainers and the losers.
    protected static int noRequired;
    protected static int numselected;
    private static Connection cq;
    
    
    
    //private class to initiatlize the array lists coz the constuctor didnt work
    private static void setArrays()
    {
        gained= new ArrayList<ArrayList<String>>();
        lost = new ArrayList<ArrayList<String>>();
        eachGained =  new ArrayList<String>();
        eachLost = new ArrayList<String>();
        
    }
    
    //the class constructor
    protected static void setNumber()
    {
        cq = ConnectDB.conn;
        Combinations combinations = new Combinations(cq);
        
        noRequired = combinations.listNo;
        numselected = combinations.days;
    }
    
    
   protected static void getGainersDataFromDb()
   {
       
       setArrays();
       try {
           //call the get data class to get the database connection and pull data from the internet in to our database;
           ArrayCreater.getdata();
           
           //function to set the minimum number required
           setNumber();
           
           Connection c = ConnectDB.conn;
           
           Statement statement = c.createStatement();
           String s = "SELECT * FROM " + ConnectDB.getDate()+ " WHERE 1 ORDER BY `Change(%)` DESC LIMIT " + noRequired + ";";
           ResultSet rst = null;

           rst = statement.executeQuery(s);
           
           while (rst.next()) {
                //String entry = rs.getString(1);
               
               
               eachGained.add(rst.getString(1));
               eachGained.add(rst.getString(2));
               eachGained.add(rst.getString(3));
               eachGained.add(rst.getString(4));
               eachGained.add(rst.getString(5));
              
               //System.out.println(rst.getString(1) +"   "+ rst.getString(2)+"   "+rst.getString(3) +"   "+ rst.getString(4));
            }
            gained.add(eachGained);

       } catch (SQLException ex) {
           Logger.getLogger(ReadandCalculate.class.getName()).log(Level.SEVERE, null, ex);
       } 
          
       
    
   }
   
   protected static void getLosersDataFromDb()
   {
       //setArrays();
       try {
           //function to get minimum number required
           setNumber();
           
           //call the get data class to get the database connection and pull data from the internet in to our database;
           ArrayCreater.getdata();
           
           Connection c = ConnectDB.conn;
           
           Statement statement = c.createStatement();
           String s = "SELECT * FROM " + ConnectDB.getDate()+ " WHERE 1 ORDER BY `Change(%)` ASC LIMIT " + noRequired +";";
           ResultSet rst = null;

           rst = statement.executeQuery(s);
           
          while (rst.next()) {
                //String entry = rs.getString(1);
               
               
               eachLost.add(rst.getString(1));
               eachLost.add(rst.getString(2));
               eachLost.add(rst.getString(3));
               eachLost.add(rst.getString(4));
               eachLost.add(rst.getString(5));
              
              // System.out.println(rst.getString(1) +"   "+ rst.getString(2)+"   "+rst.getString(3) +"   "+ rst.getString(4) + "   "+ rst.getString(5));
            }
            lost.add(eachLost);

       } catch (SQLException ex) {
           Logger.getLogger(ReadandCalculate.class.getName()).log(Level.SEVERE, null, ex);
       }
    
   }
   
    
}
