/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class DBconnection {
    
   public java.sql.Statement statementQB;
   public java.sql.Statement statementRW;
   static Logger log = Logger.getLogger(DBconnection.class.getName());
    
     public void createconnQB() throws SQLException, ClassNotFoundException{  

 Driver myDriver;
 Connection conn;
        
    
    myDriver = new cdata.jdbc.quickbooks.QuickBooksDriver();
       
     conn = DriverManager.getConnection(Runsync.qbdburl);//"jdbc:quickbooks:user=Admin;password=Admin;URL=http://localhost:8166");
       
        statementQB = conn.createStatement();
        }
    
     public void createconnRW() throws SQLException, ClassNotFoundException{  

 Driver myDriver;
 Connection conn;
        
    
    myDriver = new org.postgresql.Driver();
       
     conn = DriverManager.getConnection(Runsync.rwdburl,Runsync.rwdbuser,Runsync.rwdbpswd);//"jdbc:postgresql://localhost:5432/RenWebTest","postgres","rapunzel");
       
        statementRW = conn.createStatement();
        }
}
