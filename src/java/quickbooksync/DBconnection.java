/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author nmohamed
 */
public class DBconnection {
    
    
   public java.sql.Statement statementQB;
   public java.sql.Statement statementRW;
   static Logger log = Logger.getLogger(DBconnection.class.getName());
    
     public void createconnQB(Config config){  
try {
 Driver myDriver;
 Connection conn;
        
    
    myDriver = new cdata.jdbc.quickbooks.QuickBooksDriver();
       
     conn = DriverManager.getConnection(config.getQbdburl());//"jdbc:quickbooks:user=Admin;password=Admin;URL=http://localhost:8166");//Runsync.qbdburl);//
       
        statementQB = conn.createStatement();
        }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        }
    
     public void createconnRW(Config config){  
try {
 Driver myDriver;
 Connection conn;
        
    
    myDriver = new org.postgresql.Driver();
       
     conn = DriverManager.getConnection(config.getRwdburl(),config.getRwdbuser(),config.getRwdbpswd());//Runsync.rwdburl,Runsync.rwdbuser,Runsync.rwdbpswd);//
       
        statementRW = conn.createStatement();
     }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        } 
     }
}
