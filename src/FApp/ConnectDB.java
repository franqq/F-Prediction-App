/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FApp;
/**
 *
 * @author franq
 */
//class to connect to the database and insert the data to the table
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.DatabaseMetaData;

//this class connects to the database creates todays data table if not created yet and gets data fromte arrayCretor class and adds the data to the database;

public class ConnectDB {
    
    private static Statement stmt = null;
    protected static Connection conn;
    private  static ArrayList<ArrayList<String>> mainarraylist;
    private static ArrayList<String> changearraylist;
    
    private static String companyName,lastPrice,previousPrice;
    private static double change;
    private static String months[];
    protected static String todaystablename;
    
    public ConnectDB(ArrayList<ArrayList<String>> arraylistf, ArrayList<String> arraylistchange)
    {
        mainarraylist = arraylistf;
        changearraylist = arraylistchange;
        
        
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/dailystatistics?" +"user=root&password=");
            
            // Do something with the Connection
   
            } catch (SQLException ex) {
    // handle any errors
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
}
        
    //functions to be executed on connection
    feedTable();
}
    
 private static Connection connect()
 { Connection connect = null;
     try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost/dailystatistics?" +"user=root&password=");
            
            // Do something with the Connection
   
            } catch (SQLException ex) {
    // handle any errors
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
     
    
    
}
     return connect;
 }
 
//feed the database with todays data    
private static void feedTable()
{
   
    ResultSet rs = null;
    //get the database table name first
    todaystablename = getDate();
    //drop todays table if it was created earlier
    
    dropTable();
    
    
    //create table if it does not exist
    createTable();
    
      
      //loop to insert data in to the table
      for(int n=0; n<=mainarraylist.get(0).size()-3;n+=3)
      {
          companyName = mainarraylist.get(0).get(n).toString();
          lastPrice = mainarraylist.get(0).get(n+1).toString();
          previousPrice = mainarraylist.get(0).get(n+2).toString();
          
          
           String query = "INSERT INTO " + todaystablename +"(`Company`, `Last Traded Price(Ksh)`, `Prev Price(Ksh)`, `Change(%)`) VALUES ('" + companyName +"', '" + lastPrice
                    + "', '" + previousPrice + "', 0.00);";
          
          try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          //System.out.println(mainarraylist.get(0).get(n));
      }
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException sqlEx) { } // ignore
        stmt = null;
    }
    
    
    //loop to update the percentage change column
    for(int i=0; i<changearraylist.size();i++ )
    {
        int x = i + 1;
        change = Double.parseDouble(changearraylist.get(i));
       //System.out.println(changearraylist.get(i)); 
       // System.out.println(i+1);
      // System.out.println(change);
        
        //get the double value from the array
        change = Double.parseDouble(changearraylist.get(i));
        String query = "UPDATE `"+ todaystablename +"` SET `Change(%)` = " + change + " WHERE `id`="+ x;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
    if (stmt != null) {
        try {
            stmt.close();
        } catch (SQLException sqlEx) { } // ignore
        stmt = null;
    }
    
    
    //delete the last kplc
    deleteKPLC();
    
    }


//function to use the gregorian callendar class to get todays date to be used as the database table name
 protected static String getDate()
    {
        GregorianCalendar gcalendar = new GregorianCalendar();
      months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
      String date = months[gcalendar.get(Calendar.MONTH)] +"_"+ Integer.toString(gcalendar.get(Calendar.DATE)) +"_"+ Integer.toString(gcalendar.get(Calendar.YEAR));
      return date;
    }
 
 
 
 //function to create new table in the database if it does not exist
 protected static void createTable()
 {
    
    ResultSet rs = null;
    
    String createquery = "CREATE TABLE IF NOT EXISTS `"+todaystablename+"` (\n" +
"  `id` int(3) NOT NULL AUTO_INCREMENT,\n" +
"  `Company` varchar(500) NOT NULL,\n" +
"  `Last Traded Price(Ksh)` varchar(10) NOT NULL,\n" +
"  `Prev Price(Ksh)` varchar(10) NOT NULL,\n" +
"  `Change(%)` decimal(10,2) NOT NULL,\n" +
"  PRIMARY KEY (`id`)\n" +
") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
    try{
            stmt = conn.createStatement();
            stmt.executeUpdate(createquery);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
   
    
 }
 
 //create todays advice table
 protected static void createTableAdvGainers()
 {
         ResultSet rs = null;
    
    String createquery = "CREATE TABLE IF NOT EXISTS `"+todaystablename+"advg` (\n" +
"  `id` int(3) NOT NULL AUTO_INCREMENT,\n" +
"  `Company` varchar(500) NOT NULL,\n" +
"  `Advice` varchar(10) NOT NULL,\n" +
"  PRIMARY KEY (`id`)\n" +
") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
    try{
            stmt = conn.createStatement();
            stmt.executeUpdate(createquery);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
 }
 
  protected static void createTableAdvLosers()
 {
         ResultSet rs = null;
    
    String createquery = "CREATE TABLE IF NOT EXISTS `"+todaystablename+"advl` (\n" +
"  `id` int(3) NOT NULL AUTO_INCREMENT,\n" +
"  `Company` varchar(500) NOT NULL,\n" +
"  `Advice` varchar(10) NOT NULL,\n" +
"  PRIMARY KEY (`id`)\n" +
") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";
    try{
            stmt = conn.createStatement();
            stmt.executeUpdate(createquery);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
 }
 
 
 //function to drop any database created today
  protected static void dropTable()
       {
           ResultSet rs = null;
                   
           String query = "DROP TABLE IF EXISTS `"+todaystablename+"`;";
           try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
       
       
       }
  
  
  //drop table advice if exists
  protected static void dropTableAdvG()
  {
                ResultSet rs = null;
                   
           String query = "DROP TABLE IF EXISTS `"+todaystablename+"advg`;";
           try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
  }
  
  
  protected static void dropTableAdvL()
  {
                ResultSet rs = null;
                   
           String query = "DROP TABLE IF EXISTS `"+todaystablename+"advl`;";
           try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    }
  }
 
  //function to delete the lat entry
  private static void deleteKPLC()
  {
      String query = "DELETE FROM `nov_27_2013` WHERE `Company`='Kenya Power & Lighting Ltd 7% Pref 20.00'";
      ResultSet rs = null;
      
      try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
          
    
      //close tha 
       if (rs != null) {
        try {
            rs.close();
        } catch (SQLException sqlEx) { } // ignore
        rs = null;
    
       }
  }
  
  
  
  protected static void UpdateAdvice()
  {
      Calculator calc = new Calculator();
      calc.binomialProbabilities();
  }
 
  protected static boolean isThereTable(String str)
  {
      Connection connz = connect();
      boolean ans = false;
        try {
            DatabaseMetaData md = connz.getMetaData();
            ResultSet rs = md.getTables(null, null,str, null);
              while (rs.next()) {
              if(rs.getString(3) != null)
              {
                  ans = true;
              }
              else 
                  ans=false;
      } } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
  }
}




