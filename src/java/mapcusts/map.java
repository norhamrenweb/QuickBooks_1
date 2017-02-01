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
public class map {
    public String mapCustomer(String RWfamilyname,String QBcustname) throws SQLException, ClassNotFoundException, IOException, ParserConfigurationException, TransformerException, TransformerConfigurationException, SAXException
    {// there has to be a flag to know which string is which
        String message = null;
    String existingcustName = null;
    DBconnection connectQB = new DBconnection();
    UpdateLog logdb = new UpdateLog();
        connectQB.createconnQB();
        DBconnection connectRW = new DBconnection();
        connectRW.createconnRW();
        String QBcustID=null;
        int RWfamilyID= 0;
        ResultSet rs = connectQB.statementQB.executeQuery("select ID from Customers where Name ='"+QBcustname+"'");
        while(rs.next())
        {
        QBcustID = rs.getString("ID");
        }
        ResultSet rs1 = connectRW.statementRW.executeQuery("select familyid from family where familyname ='"+RWfamilyname+"'");
        while(rs1.next())
        {
        RWfamilyID = rs1.getInt("familyid");
        }
        MappingTable mapdb = new MappingTable();
      String existingcustID = mapdb.checkmapping(RWfamilyID);
      if(existingcustID.equals(null))
      {
      //get RW family details and insert to QB customer table then update mapping table
          List <QBCustomer> newcustomer= new ArrayList<>();
          QBCustomer n = new QBCustomer();
            n.setName(RWfamilyname);
            n.setrwId(RWfamilyID);
            newcustomer.add(n);
          InsertCustomer insert = new InsertCustomer();
            List<String> custids = new ArrayList<String>();
            custids = insert.insertCustomer(newcustomer);// after inserting returns the customer ID created by QB
            logdb.updatecustlog(newcustomer, "addition");
      mapdb.updatemapping(newcustomer,custids);
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
