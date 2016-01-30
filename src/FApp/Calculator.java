/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FApp;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franq
 */
public class Calculator {
    private static ReadandCalculate rd;
    private static double[] gainersprice,gainerchange;
    private static double[] losersprice,loserschange;
    private static double[] binomprobgainers,binomproblosers; 
    private static double[] pgainers,plosers;
    private static double[] PRgainers,PRlosers;
    private static double ERgainers,ERlosers;
    private static int num;
    private static double[] gainerVariance,loservariance;
    private static double[] gainerSD,loserSD;
    private static double[] finalFigureGainer;
    private static double[] finalFigureLoser;
    private static String[] gainersAdvice,losersAdvice;
    private static String[] gainersName,losersName;
   
    //no of times we interested
    private static int numSelected;
    
    
    //initialises arrays and stuff
    private static void initArraysnStuff()
    {
        rd = new ReadandCalculate();
        ReadandCalculate.getGainersDataFromDb();
        ReadandCalculate.getLosersDataFromDb();
        num = ReadandCalculate.noRequired;
        num=9;
        
        gainersprice = new double[num];
        gainerchange = new double[num];
        losersprice = new double[num];
        loserschange = new double[num];
        binomprobgainers = new double[num];
        binomproblosers = new double[num];
        pgainers = new double[num];
        plosers = new double[num];
        PRgainers = new double[num];
        PRlosers = new double[num];
        gainerVariance = new double[num];
        loservariance = new double[num];
        gainerSD = new double[num];
        loserSD = new double[num];
        finalFigureGainer = new double[num];
        finalFigureLoser = new double[num];
        gainersAdvice = new String[num];
        losersAdvice = new String[num];
        gainersName = new String[num];
        losersName = new String[num];
        
                
        //number of times selected
        numSelected = ReadandCalculate.numselected;
        
    }
    
    private static void getDatas()
    {
       initArraysnStuff();
       for(int i=0;i<num*5-1;i+=5)
       {
           losersName[i/5] = ReadandCalculate.lost.get(0).get(i+1);
           losersprice[i/5] = Double.parseDouble(ReadandCalculate.lost.get(0).get(i+2));
           loserschange[i/5] = Double.parseDouble(ReadandCalculate.lost.get(0).get(i+4));
           
           gainersName[i/5] = ReadandCalculate.gained.get(0).get(i+1);
           gainersprice[i/5]= Double.parseDouble(ReadandCalculate.gained.get(0).get(i+2));
           gainerchange[i/5]= Double.parseDouble(ReadandCalculate.gained.get(0).get(i+4));
           
       }
    }
    
    protected static void  binomialProbabilities()
    {
        getDatas();
        createTables();
        double totalGainers = 0;
        double totalLosers = 0;
        for(int i=0; i<num; i++)
        {
         totalGainers += gainersprice[i];
         totalLosers += losersprice[i];
        }
        
      
        //loop to calculate binomial probabilities
        double combinationRequired = combination(5, numSelected);
        
        ERgainers =0;
        ERlosers = 0;
        
        //loop to get the binomial probabilities as well as compute for ER and PR to make program faster
         for(int i=0; i<num; i++)
        { 
            double p,q,p2,q2;
            p = gainersprice[i]/totalGainers;
            q = 1-p;
            p2 = losersprice[i]/totalLosers;
            q2= 1-p2;
          binomprobgainers[i] = combinationRequired * Math.pow(p, numSelected) * Math.pow(q,(5-numSelected));
          binomproblosers[i] = combinationRequired * Math.pow(p2, numSelected) * Math.pow(q2,(5-numSelected));
          //calculate the PR
          PRgainers[i] = gainerchange[i] * binomprobgainers[i];
          ERgainers += PRgainers[i];
          PRlosers[i] = loserschange[i] * binomproblosers[i];
          ERlosers += PRlosers[i];
          
        }
         absVal(ERlosers);
         //for loop to get the variance and standard deviation
         for(int i=0; i<num; i++)
         {
            gainerVariance[i] = binomprobgainers[i] * Math.pow((gainerchange[i] - ERgainers),2);
            //gainerVariance[i] = absVal(gainerVariance[i]);
            loservariance[i]= binomproblosers[i] * Math.pow((loserschange[i] - ERlosers),2) ;
            //loservariance[i] = absVal(loservariance[i]);
            gainerSD[i] = Math.sqrt(gainerVariance[i]);
            loserSD[i] = Math.sqrt(loservariance[i]);
            
            
            //final figure;
            finalFigureGainer[i] = gainerSD[i]/ERgainers;
            finalFigureLoser[i] = loserSD[i]/ERlosers;
            
            //loop through again to get specific advice for each
            //map each final figure for both gainers and losers with specific advice
            gainersAdvice[i] = MapGainer(finalFigureGainer[i]);
            losersAdvice[i] = MapLoser(finalFigureLoser[i]);
            
            //update the gainers and losers tables
            UpdateGainers(gainersName[i],gainersAdvice[i]);
            UpdateLosers(losersName[i], losersAdvice[i]);
            
         }
         
  
    }
    
    //function to get absoluste value
    private static double absVal(double d)
    {
       if(d<0)
           d=-d;
       
   
       return d;
    }
    
    //function to calculate factorial
    private static double factorial(double number) {
      if (number <= 1)
         return 1;
      else
         return number * factorial(number - 1);
   }
    
    
    //ffunction to calculate mathematical combinations
    protected static double combination(double i, double j)
    {
      double combi = factorial(i)/(factorial(j)*factorial(i-j));
      return combi;  
    }
    
    private static String MapGainer(double d)
    {
        //conditional statements to check the resulsts in a graph
        String result = "Didnt work";
        
        if(d>0&&d<=0.1)
            result = "Unknown";
        else if(d>0.1&&d<=0.5)
            result = "Sell";
        else if(d>0.5&&d<=1.0)
            result = "Buy";
       
        
        return result;
            
    }
    
    
    
    private static String MapLoser(double d)
    {
        //conditional statements to check the resulsts in a graph
        String result = "Didnt work";
        
        if(d>=-0.1&&d<0)
            result = "Unknown";
        else if(d>-0.5&&d<=-0.1)
            result = "Buy";
        else if(d>-1.0&&d<=-0.5)
            result = "Sell";
       
        
        return result;
    }
    
    private static void createTables()
    {
        //get the database table name first
        ConnectDB.getDate();
      //drop todays table if it was created earlier
    
       ConnectDB.dropTableAdvG();
       ConnectDB.dropTableAdvL();
       
       //create table if it does not exist
       ConnectDB.createTableAdvGainers();
       ConnectDB.createTableAdvLosers();
    }
    
    
    private static void UpdateLosers(String companyname,String advice)
    {
        //ResultSet rs = null;
        Statement stmt;
        
        String query = "INSERT INTO `" + ConnectDB.todaystablename +"advl`(`Company`, `Advice`) VALUES ('" + companyname +"', '" + advice + "');";
                   
          
          try {
            stmt = ConnectDB.conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
     private static void UpdateGainers(String companyname,String advice)
    {
        
        Statement stmt;
        
        String query = "INSERT INTO " + ConnectDB.todaystablename +"advg(`Company`, `Advice`) VALUES ('" + companyname +"', '" + advice + "');";
                   
          
          try {
            stmt = ConnectDB.conn.createStatement();
            stmt.executeUpdate(query);
            
            } catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
     
}
