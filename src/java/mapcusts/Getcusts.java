/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapcusts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import quickbooksync.DBconnection;
import quickbooksync.QBCustomer;
import quickbooksync.RWFamily;
import quickbooksync.Runsync;

/**
 *
 * @author nmohamed
 */
public class Getcusts {
    
    
    
    public List<QBCustomer> getCustomer() throws SQLException, ClassNotFoundException, IOException
   {
       
      List <QBCustomer> allcustomer= new ArrayList<>();
//       File file = new File("C:\\Users\\Public\\config.txt");
//		FileInputStream fis = null;
//                fis = new FileInputStream(file);
//
//			
//			int content;
//                        StringBuilder builder = new StringBuilder();
//                       
//			while ((content = fis.read()) != -1) {
//				// convert to char and display it
//				builder.append((char)content);
//			}
//                        String conf = builder.toString();
//                        String[] x = conf.split("&");
//                      
//                        if (fis != null)
//                        {fis.close();}
//                        
//                         Driver myDriver;
// Connection conn;
//        
//    
//    myDriver = new cdata.jdbc.quickbooks.QuickBooksDriver();
//       
//     conn = DriverManager.getConnection(x[0]);//"jdbc:quickbooks:user=Admin;password=Admin;URL=http://localhost:8166");
//       
//       java.sql.Statement statementQB = conn.createStatement();
DBconnection connectQB = new DBconnection();
        connectQB.createconnQB();
        
        
        ResultSet rs = connectQB.statementQB.executeQuery("Select ID,Name from Customers");
      
        QBCustomer c = new QBCustomer();
        while(rs.next())
        {
            c.setName(rs.getString("Name"));
            c.setId(rs.getString("ID"));
        allcustomer.add(c);
        
        }
            return allcustomer;
            }
      public List<RWFamily> getFamily() throws SQLException, ClassNotFoundException, IOException
   {
      List <RWFamily> allfamily= new ArrayList<>();
//       File file = new File("C:\\Users\\Public\\config.txt");
//		FileInputStream fis = null;
//                fis = new FileInputStream(file);
//
//			
//			int content;
//                        StringBuilder builder = new StringBuilder();
//                       
//			while ((content = fis.read()) != -1) {
//				// convert to char and display it
//				builder.append((char)content);
//			}
//                        String conf = builder.toString();
//                        String[] x = conf.split("&");
//                      
//                        if (fis != null)
//                        {fis.close();}
//                        
//                         Driver myDriver;
// Connection conn;
//        
//    
//    myDriver = new org.postgresql.Driver();
//       
//     conn = DriverManager.getConnection(x[3],x[4],x[5]);//"jdbc:postgresql://localhost:5432/RenWebTest","postgres","rapunzel");
//       
//       java.sql.Statement statementRW = conn.createStatement();
       
      DBconnection connectRW = new DBconnection();
        connectRW.createconnRW();
        
        
        ResultSet rs = connectRW.statementRW.executeQuery("Select familyid,familyname from family");
        
        while(rs.next())
        { RWFamily c = new RWFamily();
            c.setFamilyName(rs.getString("familyname"));
            c.setId(rs.getInt("familyid"));
        allfamily.add(c);
        
        }
            return allfamily;
            }
   }

