/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class RetrieveCustomer {
    
    static Logger log = Logger.getLogger(RetrieveCustomer.class.getName());
   public List<QBCustomer> retrieveCustomer(Config config)
   {List<QBCustomer> customer = new ArrayList<>();
   //connect to DB
       //select * from customers in QB and create the QBCustomer list
       try{
       DBconnection connectQB = new DBconnection();
        connectQB.createconnQB(config);
        
        
        ResultSet rs = connectQB.statementQB.executeQuery("Select * from Customers");
        
        while(rs.next())
        {
            QBCustomer x = new QBCustomer();
            x.setId(rs.getString("ID"));
           x.setFirstName(rs.getString("FirstName"));
           x.setLastName(rs.getString("LastName"));
           x.setName("Name");
        customer.add(x);
        
        }}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        return customer;
        
   }
 public List<RWFamily> retrieveFamily(Config config)
   {List<RWFamily> family = new ArrayList<>();
   //connect to DB
       //select * from Family in RW and create the RWFamily list
       try{
       DBconnection connectRW = new DBconnection();
        connectRW.createconnRW(config);
       
        
        ResultSet rs;
       rs = connectRW.statementRW.executeQuery("Select * from public.family");//228. FamilyConfig table in renweb
        
        while(rs.next())
        {
            RWFamily x = new RWFamily();
            x.setId(rs.getInt("familyid"));
          
           x.setFamilyName(rs.getString("FamilyName"));
           
        family.add(x);
        
        }}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        return family;
   }
    
}
