/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapcusts;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import quickbooksync.*;


/**
 *
 * @author nmohamed
 */
public class Map {
  
    public String mapCustomer(RWFamily family, QBCustomer cust) throws SQLException, ClassNotFoundException, IOException, ParserConfigurationException, TransformerException, TransformerConfigurationException, SAXException
    {// there has to be a flag to know which string is which
        String message = null;
    Config config = new Config();
       GetConfig get = new GetConfig();
       config = get.getConfig();
       
        MappingTable mapdb = new MappingTable(config);
      
      //get RW family details and update mapping table
          List <QBCustomer> newcustomer= new ArrayList<>();
          QBCustomer n = new QBCustomer();
          
            n.setrwId(family.getId());
            newcustomer.add(n);
        List<String> ids = new ArrayList<>();
         ids.add(cust.getId());
         if(!ids.isEmpty())
         {
             mapdb.updatemapping(newcustomer,ids,config);
         }
      message = "Customer successfully mapped";
     
    
        return message;
        
    }
}
