/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author nmohamed
 */
public class CustomerSync {
     static Logger log = Logger.getLogger(Runsync.class.getName());

    
    public void customersync(Config config){
            
            try {
            // TODO code application logic here
            //Retrieve families from RW and Customers from QB
           
            List <RWFamily> allfamily= new ArrayList<>();
            RetrieveCustomer x = new RetrieveCustomer();
            allfamily = x.retrieveFamily(config);
            List <QBCustomer> allcustomer= new ArrayList<>();
            UpdateLog logdb = new UpdateLog(config);
            
            allcustomer = x.retrieveCustomer(config);
            //-----------------------------------------------------------------------
            //Compare families and add new families in a list
            List <QBCustomer> newcustomer= new ArrayList<>();
            MappingTable map = new MappingTable(config);
            
            for (RWFamily y : allfamily) {
                String result = "";
                
                if(map.checkmappingrw(y.getId(),config) != null)
                    //match found¨
                {
                    result = "Match Found";
                    
                }
                
                
                if(!result.equals("Match Found"))
                {
                    QBCustomer n = new QBCustomer();
                  
                    n.setName(y.getFamilyName());
                    n.setrwId(y.getId());
                    newcustomer.add(n);
                    
                }
            }
            //---------------------------------------------------------------------
            //Add the new families in the list to QB DB
            InsertCustomer insert = new InsertCustomer();
            List<String> custids = new ArrayList<String>();
            custids = insert.insertCustomer(newcustomer,config);// after inserting returns the customer ID created by QB
            logdb.updatecustlog(newcustomer, "addition",config);
            //--------------------------------------------------------------------
            //Update the family ids mapping table
            
            map.updatemapping(newcustomer,custids,config);
            //----------------------------------------------------------------------
           
        } catch (SQLException | ParserConfigurationException | TransformerException | SAXException | IOException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
    
}
    
}
