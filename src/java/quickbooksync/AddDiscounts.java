/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
public class AddDiscounts {
     static Logger log = Logger.getLogger(Runsync.class.getName());
       
    public void addDiscounts(List<QBInvoice> discountlist,Config config) 
       {
           try {
          
         DBconnection connectQB = new DBconnection();
        connectQB.createconnQB(config);
        String fixamount;
        
        for(QBInvoice d : discountlist)
        {
        ResultSet rs = connectQB.statementQB.executeQuery("SELECT InvoiceId FROM InvoiceLineItems where CustomerId = '"+d.getqbCustomer().getId()+"' and Date = '"+d.getDate()+"'");
        while(rs.next())
        {
        if(rs.getString("InvoiceId")!= null)
        {
         
            double dd=  d.getAmount();///100;
         fixamount = Double.toString(dd);
        connectQB.statementQB.executeUpdate("insert into InvoiceLineItems (InvoiceId,CustomerId,Date,Memo,DueDate,CustomFields,ItemName,ItemAmount) values ('"+rs.getString("InvoiceId")+"','"+ d.getqbCustomer().getId() + "','"+d.getDate()+"','"+d.getMemo()+"','"+ d.getdueDate()+ "','"+d.getcustomFields()+"','"+d.getItem().getitemName()+"','"+fixamount+"')");//discount does not need quantity
        }
        else
        {
            connectQB.statementQB.executeUpdate("insert into CreditMemoLineItem (CustomerId, ItemName, ItemQuantity,ItemAmount,Date,Memo,DueDate,CustomFields) values ('"+d.getqbCustomer().getId()+"','"+d.getItem().getitemName()+"','"+d.getItem().getitemQuantity()+"','"+d.getAmount()+"','"+d.getDate()+"','"+d.getMemo()+"','"+d.getdueDate()+"','"+d.getcustomFields()+"'");
        }
            
        }
       
       }
}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
       }
}
