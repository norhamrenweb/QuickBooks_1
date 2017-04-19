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
public class InsertCustomer {
    static Logger log = Logger.getLogger(InsertCustomer.class.getName());
    
    public List<String> insertCustomer (List<QBCustomer> addlist,Config config)
    {
        List<String> ids = new ArrayList<String>();
    
        try{
    // take the input and looping through them to insert in QB Customers table
        DBconnection connectQB = new DBconnection();
        connectQB.createconnQB(config);
        
      
        for (QBCustomer customer : addlist) {
            connectQB.statementQB.executeUpdate("insert into Customers (Name) values ('"+ customer.getName()+"')");
            ResultSet rs = connectQB.statementQB.executeQuery("Select ID from Customers where Name = '"+ customer.getName() +"'");
            while(rs.next())
            {
                ids.add(rs.getString("ID"));
            }
        }
        }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        return ids;
    }
    }

    

