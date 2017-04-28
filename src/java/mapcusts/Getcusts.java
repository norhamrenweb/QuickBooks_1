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
import quickbooksync.*;


/**
 *
 * @author nmohamed
 */
public class Getcusts {
    
    
    
    public List<QBCustomer> getCustomer() throws SQLException, ClassNotFoundException, IOException
   {
       Config config = new Config();
       GetConfig get = new GetConfig();
       config = get.getConfig();
       
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
        connectQB.createconnQB(config);
        
        
        ResultSet rs = connectQB.statementQB.executeQuery("Select ID,Name from Customers");
      
       
        while(rs.next())
        {
             QBCustomer c = new QBCustomer();
            c.setName(rs.getString("Name"));
            c.setId(rs.getString("ID"));
        allcustomer.add(c);
        
        }
       List <QBCustomer> unmappedcustomer= new ArrayList<>(); 
       MappingTable m = new MappingTable(config);
       String result = null;
       for (QBCustomer x : allcustomer) {
       result = m.checkmappingqb(x.getId(),config);
       if(result==null) //no match found
       {
       unmappedcustomer.add(x);
      
       }
               }
            return unmappedcustomer;
            }
      public List<RWFamily> getFamily() throws SQLException, ClassNotFoundException, IOException
   {
       Config config = new Config();
       GetConfig get = new GetConfig();
       config = get.getConfig();
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
        connectRW.createconnRW(config);
        
        
        ResultSet rs = connectRW.statementRW.executeQuery("Select familyid,familyname from family");
        
        while(rs.next())
        { RWFamily c = new RWFamily();
            c.setFamilyName(rs.getString("familyname"));
            c.setId(rs.getInt("familyid"));
        allfamily.add(c);
        
        }
            List <RWFamily> unmappedfamily= new ArrayList<>(); 
       MappingTable m = new MappingTable(config);
       String result = null;
       for (RWFamily x : allfamily) {
       result = m.checkmappingrw(x.getId(),config);
       if(result==null) //no match found
       {
       unmappedfamily.add(x);
      
       }
               }
            return unmappedfamily;
            }
   }

