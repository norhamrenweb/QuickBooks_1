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
    String existingcustName = null;
    DBconnection connectQB = new DBconnection();
        connectQB.createconnQB();
       
        MappingTable mapdb = new MappingTable();
      String existingcustID = mapdb.checkmapping(family.getId());
      if(existingcustID.equals(null))
      {
      //get RW family details and insert to QB customer table then update mapping table
          List <QBCustomer> newcustomer= new ArrayList<>();
          QBCustomer n = new QBCustomer();
            n.setName(family.getFamilyName());
            n.setrwId(family.getId());
            newcustomer.add(n);
        List<String> ids = null;
         ids.add(cust.getId());
      mapdb.updatemapping(newcustomer,ids);
      message = "Customer successfully mapped";
      }
      else{
      ResultSet rs2 = connectQB.statementQB.executeQuery("select Name from Customers where ID = '"+existingcustID+"'");
       while(rs2.next())
        {
        existingcustName = rs2.getString("Name");
        }
       
      message = "Customer already mapped to "+ existingcustName;
      }
        return message;
        
    }
}
