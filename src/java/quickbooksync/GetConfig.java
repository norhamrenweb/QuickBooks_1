/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nmohamed
 */
public class GetConfig {
    public java.sql.Statement statement;
    
 public Config getConfig() throws SQLException{
     Driver myDriver;
 Connection conn;
        
    
    myDriver = new cdata.jdbc.quickbooks.QuickBooksDriver();
       
     conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","rapunzel");//"jdbc:quickbooks:user=Admin;password=Admin;URL=http://localhost:8166");//Runsync.qbdburl);//
       
        statement = conn.createStatement();
          ResultSet rs = statement.executeQuery("Select * from syncconfig");
    Config config = new Config();
while(rs.next())
{
    
    config.setQbdburl(rs.getString("qbdburl"));
config.setQbdbuser(rs.getString("qbdbuser"));
config.setQbdbpswd(rs.getString("qbdbpswd"));
config.setRwdburl(rs.getString("rwdburl"));
config.setRwdbuser(rs.getString("rwdbuser"));
config.setRwdbpswd(rs.getString("rwdbpswd"));
config.setEdudburl(rs.getString("edudburl"));
config.setEdudbuser(rs.getString("edudbuser"));
config.setEdudbpswd(rs.getString("edudbpswd"));
config.setStartdate(rs.getDate("startdate").toString());
config.setItemname(rs.getString("itemname"));
    
    
}
 
 return config;
 }
}
