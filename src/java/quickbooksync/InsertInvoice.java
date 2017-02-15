/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class InsertInvoice {
    static Logger log = Logger.getLogger(InsertInvoice.class.getName());
    public void insertInvoice (List<QBInvoice> addlist,Config config)
    {
try{    
// take the input and looping through them to insert in QB InvoiceLineItem table
         DBconnection connectQB = new DBconnection();
        connectQB.createconnQB(config);
        String fixamount;
      
        for(QBInvoice invoice : addlist)
        {
           double dd=  invoice.getAmount();///100;
         fixamount = Double.toString(dd);
            //fixamount = invoice.getAmount();
        connectQB.statementQB.executeUpdate("insert into InvoiceLineItems (CustomerId,Date,Memo,DueDate,CustomFields,ItemName,ItemQuantity,ItemAmount) values ('"+ invoice.getqbCustomer().getId() + "','"+invoice.getDate()+"','"+invoice.getMemo()+"','"+ invoice.getdueDate()+ "','"+invoice.getcustomFields()+"','"+invoice.getItem().getitemName()+"','"+invoice.getItem().getitemQuantity()+"','"+fixamount+"')");
        
        }
        }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
    }

    
}
