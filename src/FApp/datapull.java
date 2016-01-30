/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FApp;

import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import static nsepredictionapp.NsepredictionApp.links;
//import static nsepredictionapp.NsepredictionApp.setLinks;

/**
 *
 * @author franq
 */

//The data pull class pulls the information from the nseshareindex website www.nse.com
public class datapull {

private static List<String> nselist;
private static List<String> lstest;
private static String exturl;
private static Pattern htmltag;
private static Pattern link;
protected static List<String> links;
private static String linkz,teststr;
private static Matcher matcher;


    
public datapull() {
       exturl = "https://www.nse.co.ke/market-statistics/equity-statistics.html";
       
    }
    
public static String maindatapull(){
    
    String string = setLinks(exturl);
    return string;
    
  }

public static String setLinks(String str) {
    links = new ArrayList<String>();
    String string = null;
    
    String url = "https://www.nse.co.ke/market-statistics/equity-statistics.html";
    try {
      BufferedReader bufferedReader;
      
      URL myurl = new URL(url);
      teststr = "Crap";
      bufferedReader = new BufferedReader(new InputStreamReader(myurl.openStream()));
      String s;
      StringBuilder builder = new StringBuilder();
      while ((s = bufferedReader.readLine()) != null) {
          
            builder.append(s);
          
      }
      
      String strbuilder = builder.toString();
      String mystring = matchCheck(strbuilder);
      
      string = mystring;
    } catch (MalformedURLException e) {
      ///e.printStackTrace();
    } catch (IOException e) {
      //e.printStackTrace();
    }
    //return links;
    return string;
    
    
  }

    private static String matchCheck(String str) {
        
   
        //test
        String regextxt = "<tr class=\"row[0,1]\">[^>]*(.*?)</tr>";
        String text = str;
        String replace = ",";
        String reptxt = "<tr class=\"row[0,1]\">    	<td valign=\"middle\" class=\"itemt\">";
        String reptxt2 = "</td>                 <td valign=\"middle\">";
        String reptxt3 = "</td>[^>]*(.*?)</tr>";
        String reptxt4 = "<tr class=\"row[0,1]\">jagklj<td valign=\"middle\" class=\"itemt\">ICDC";
        String reptxtfin = "<tr class=\"row0\">				<td class=\"alignl fontcap\">(.*)";
        
        StringBuilder nstr = new StringBuilder();
        
        htmltag = Pattern.compile(regextxt);
        matcher = htmltag.matcher(text);
        
        
        
        while(matcher.find()){
        if(matcher.group().length()!=0)
        {
           nstr.append(matcher.group());
        }
        }
        
        String nstring = nstr.toString();
        
        //nstring = nstring.trim();
        
        Pattern reppartern1 = Pattern.compile(reptxt);
        Matcher repmatch1 = reppartern1.matcher(nstring);
        
        nstring = repmatch1.replaceAll(replace);
        
        reppartern1 = Pattern.compile(reptxt2);
        
        repmatch1 = reppartern1.matcher(nstring);
        
        nstring = repmatch1.replaceAll(replace);
        
        reppartern1 = Pattern.compile(reptxt3);
        
        repmatch1 = reppartern1.matcher(nstring);
        
        nstring = repmatch1.replaceAll(replace);
        
        reppartern1 = Pattern.compile(reptxt4);
        
        repmatch1 = reppartern1.matcher(nstring);
        
        nstring = repmatch1.replaceAll(replace);
        
        reppartern1 = Pattern.compile(reptxtfin);
        
        repmatch1 = reppartern1.matcher(nstring);
        
        nstring = repmatch1.replaceAll(replace);
        
        //remove the first comma from the text and leave the first name
        //substring function
        nstring = nstring.substring(1);
        
       // System.out.println(nstring);
        return nstring;
    }
    
    private boolean checkTableExists()
    {
        boolean b = false;
    try {
        LoginScreen.ConnectDb();
        Connection connection;
        connection = LoginScreen.c;
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, ConnectDB.getDate(), null);
        if (rs.next()) {
      //Column in table exist
            b=true;
    }
        else
            b=false;
    } catch (SQLException ex) {
        Logger.getLogger(datapull.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return b;
    }
    
    
}