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
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class MappingTable {
   static Logger log = Logger.getLogger(MappingTable.class.getName());
    private java.sql.Statement statement;
    
    private final Driver myDriver = new org.postgresql.Driver();
       
        private final Connection conn;
         

    public MappingTable() throws SQLException {
        this.conn = DriverManager.getConnection(Runsync.edudburl,Runsync.edudbuser,Runsync.edudbpswd);//"jdbc:postgresql://localhost:5432/postgres","postgres","rapunzel");
    }
       
    
    public void updatemapping (List<QBCustomer> customers, List<String> ids ) throws SQLException, ClassNotFoundException
        {
       
        statement = conn.createStatement();
        for (int i =0;i<customers.size();i++)
        {
         statement.executeUpdate("insert into CUSTOMERMAPPING (RWFAMILYID,QBCUSTOMERID) values ('"+customers.get(i).getrwId()+"','"+ids.get(i)+"')");
        }
        
        }
    public String checkmapping(int rwFamilyId) throws SQLException
        { String result=null;
       statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("select QBCUSTOMERID FROM CUSTOMERMAPPING where RWFAMILYID ='"+rwFamilyId+"'");
      
     
      if(rs.next()){
      result = rs.getString("QBCUSTOMERID");//matching customer found
      
              }
        return result;
            
        }
}
