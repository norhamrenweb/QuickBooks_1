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
public class RetrieveInvoice {
    static Logger log = Logger.getLogger(RetrieveInvoice.class.getName());
    
    public List<QBInvoice> retrieveInvoice() throws SQLException, ClassNotFoundException
   {
   //connect to DB
       //select * from invoicelineitem in QB and create the QBInvoices list
        DBconnection connectQB = new DBconnection();
        connectQB.createconnQB();
        List<QBInvoice> invoice = new ArrayList<>();
        
        ResultSet rs = connectQB.statementQB.executeQuery("Select * from InvoiceLineItems");
        
        while(rs.next())
        {
            QBInvoice x = new QBInvoice();
            Item i = new Item();
            QBCustomer c = new QBCustomer();
            i.setitemName(rs.getString("ItemName"));
            c.setId(rs.getString("CustomerId"));
           x.setItem(i);
           x.setAmount(rs.getDouble("ItemAmount"));
           x.setDate(rs.getDate("Date"));
           x.setinvoiceId(rs.getString("InvoiceId"));
           x.setId(rs.getString("ID"));
           x.setMemo(rs.getString("Memo"));
           x.setQBCustomer(c);
           x.setcustomFields(rs.getString("CustomFields"));
           x.setdueDate(rs.getDate("DueDate"));
           
        invoice.add(x);
        
        }
        return invoice;
        
   }
 public List<RWCharge> retrieveCharge() throws SQLException, ClassNotFoundException
   {
   //connect to DB
       //select * from charges in RW and create the RWCharge list
       DBconnection connectRW = new DBconnection();
        connectRW.createconnRW();
        List<RWCharge> charge = new ArrayList<>();
        
        ResultSet rs = connectRW.statementRW.executeQuery("Select * from \"public\".\"Charges\"");
        
        while(rs.next())
        {
            RWCharge x = new RWCharge();
           
            RWFamily f = new RWFamily();
            
            f.setId(rs.getInt("FamilyID"));
          
           x.setDate(rs.getDate("Date"));
           x.setchargeId(rs.getInt("ChargeID"));
           x.setDescription(rs.getString("Description"));
           x.setrwFamily(f);
           x.setAccountingSystemId(rs.getInt("AccountingSystemID"));
           x.setdueDate(rs.getDate("DueDate"));
           x.setAmount(rs.getDouble("Amount"));
           
        charge.add(x);
        
        }
        return charge;
   }
    
}
