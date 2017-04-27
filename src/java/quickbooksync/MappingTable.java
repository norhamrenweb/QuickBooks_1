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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class MappingTable {
   static Logger log = Logger.getLogger(MappingTable.class.getName());
    private java.sql.Statement statement;
    
    private final Driver myDriver = new org.postgresql.Driver();
       
        private Connection conn = null;
         

    public MappingTable(Config config) {
//        try{
//        this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());//Runsync.edudburl,Runsync.edudbuser,Runsync.edudbpswd);//
//   }catch (SQLException ex) {
//            
//            StringWriter errors = new StringWriter();
//ex.printStackTrace(new PrintWriter(errors));
//log.error(ex+errors.toString());
//        }
        }
       
    
    public void updatemapping (List<QBCustomer> customers, List<String> ids,Config config )
        {try{
               this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());//Runsync.edudburl,Runsync.edudbuser,Runsync.edudbpswd);//
        statement = conn.createStatement();
        for (int i =0;i<customers.size();i++)
        {
         statement.executeUpdate("insert into CUSTOMERMAPPING (RWFAMILYID,QBCUSTOMERID) values ('"+customers.get(i).getrwId()+"','"+ids.get(i)+"')");
        }
        }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        }
    public String checkmappingrw(int rwFamilyId, Config config)
        { String result=null;
            try{
            
                    this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());//Runsync.edudburl,Runsync.edudbuser,Runsync.edudbpswd);//
       statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("select qbcustomerid FROM customermapping where rwfamilyid ='"+rwFamilyId+"'");
      
     
      if(rs.next()){
      result = rs.getString("qbcustomerid");//matching customer found
      
              }
      }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        return result;
            
        }
    public String checkmappingqb(String qbCustId, Config config) throws SQLException
        { String result=null;
        try
        {
                    this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());//Runsync.edudburl,Runsync.edudbuser,Runsync.edudbpswd);//
       statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("select rwfamilyid FROM customermapping where qbcustomerid ='"+qbCustId+"'");
      
     
      if(rs.next()){
      result = rs.getString("rwfamilyid");//matching customer found
      
              }
        }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        return result;
            
        }
}
