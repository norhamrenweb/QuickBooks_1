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
public class RetrievePayment {
    static Logger log = Logger.getLogger(RetrievePayment.class.getName());
    
    public List<QBPayment> retrievePayment(Config config)
   {List<QBPayment> payment = new ArrayList<>();
   //connect to DB
       //select * from invoicelineitem in QB and create the QBInvoices list
       try{
        DBconnection connectQB = new DBconnection();
        connectQB.createconnQB(config);
        
        
        ResultSet rs = connectQB.statementQB.executeQuery("Select * from ReceivePayments");
        
        while(rs.next())
        {
            QBPayment x = new QBPayment();
           // Item i = new Item();
            QBCustomer c = new QBCustomer();
            //i.setitemName(rs.getString("ItemName"));
            c.setId(rs.getString("CustomerId"));
           //x.setItem(i);
           x.setAmount(rs.getDouble("Amount"));
           x.setDate(rs.getDate("Date"));
           x.setpaymentId(rs.getString("ID"));
          
           x.setMemo(rs.getString("Memo"));
           x.setQBCustomer(c);
           x.setcustomFields(rs.getString("CustomFields"));
          // x.setdueDate(rs.getDate("DueDate"));
           
        payment.add(x);
        
        }}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        return payment;
        
   }
 public List<RWPayment> retrieverwPayment(Config config)
   {List<RWPayment> payment = new ArrayList<>();
   //connect to DB
       //select * from charges in RW and create the RWCharge list
       try{
       DBconnection connectRW = new DBconnection();
        connectRW.createconnRW(config);
        
        
        ResultSet rs = connectRW.statementRW.executeQuery("Select * from \"public\".\"payments\"");
        
        while(rs.next())
        {
            RWPayment x = new RWPayment();
           
            RWFamily f = new RWFamily();
            
            f.setId(rs.getInt("FamilyID"));
          
           x.setDate(rs.getDate("Date"));
           x.setpaymentId(rs.getInt("PaymentID"));
           x.setDescription(rs.getString("memo"));
           x.setrwFamily(f);
           x.setAmount(rs.getDouble("Amt"));
           
        payment.add(x);
        
        }}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        return payment;
   }
    
}
