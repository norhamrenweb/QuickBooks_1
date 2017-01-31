/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

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
    
    public List<String> insertCustomer (List<QBCustomer> addlist) throws SQLException, ClassNotFoundException
    {
    // take the input and looping through them to insert in QB Customers table
        DBconnection connectQB = new DBconnection();
        connectQB.createconnQB();
        List<String> ids = new ArrayList<String>();
      
        for (QBCustomer customer : addlist) {
            connectQB.statementQB.executeUpdate("insert into Customers (Name) values ('"+ customer.getName()+"')");
            ResultSet rs = connectQB.statementQB.executeQuery("Select ID from Customers where Name = '"+ customer.getName() +"'");
            while(rs.next())
            {
                ids.add(rs.getString("ID"));
            }
        }
        return ids;
    }
    }

    

