/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class InsertPayment {
    static Logger log = Logger.getLogger(InsertPayment.class.getName());
    public void insertPayment (List<QBPayment> addlist,Config config) throws SQLException, ClassNotFoundException, ParseException
    {
    // take the input and looping through them to insert in QB InvoiceLineItem table
    /*     DBconnection connectQB = new DBconnection();
        connectQB.createconnQB();
        String fixamount;
       int size = addlist.size();
      
        for(QBPayment payment : addlist)
        {
           double dd=  payment.getAmount();///100;
         fixamount = Double.toString(dd);
            //fixamount = invoice.getAmount();
           
           //String chargeid = pc.getAppliedtoRefID(Integer.parseInt(payment.getpaymentId()));
           if(payment.getappliedTo().isEmpty()){
        connectQB.statementQB.executeUpdate("insert into ReceivePayments (CustomerId,Date,Memo,CustomFields,Amount,AutoApply) values ('"+ payment.getqbCustomer().getId() + "','"+payment.getDate()+"','"+payment.getMemo()+"','"+payment.getcustomFields()+"','"+fixamount+"','ExistingTransactions')");
           }
           else
           {
              // here we need to put a while loop for every chargeid and amount then size-1
            connectQB.statementQB.executeUpdate("insert into ReceivePaymentsAppliedTo (CustomerId,Date,Memo,CustomFields,Amount,AppliedToRefId) values ('"+ payment.getqbCustomer().getId() + "','"+payment.getDate()+"','"+payment.getMemo()+"','"+payment.getcustomFields()+"','"+fixamount+"','"+payment.getappliedTo()+"')");//amount or applied to amount (need to change)
           }
        }*/
        
        DBconnection connectQB = new DBconnection();
        connectQB.createconnQB(config);
        String fixamount;
       
      
        for(QBPayment payment : addlist)
        {
          
            //fixamount = invoice.getAmount();
           //String chargeid = pc.getAppliedtoRefID(Integer.parseInt(payment.getpaymentId()));

           if(payment.getappliedTo().isEmpty()){
               double dd=  payment.getAmount();///100;
         fixamount = Double.toString(dd);
        connectQB.statementQB.executeUpdate("insert into ReceivePayments (CustomerId,Date,Memo,ReferenceNumber,Amount,AutoApply) values ('"+ payment.getqbCustomer().getId() + "','"+payment.getDate()+"','"+payment.getMemo()+"','"+payment.getcustomFields()+"','"+fixamount+"','ExistingTransactions')");
           }
           else
           { 
               for(int i = 0; i < payment.getappliedTo().size(); i++)
               {
            connectQB.statementQB.executeUpdate("insert into ReceivePaymentsAppliedTo (CustomerId,Date,ReferenceNumber,Amount,AutoApply,Memo,AppliedToPaymentAmount#1,AppliedToRefId#1) values ('"+ payment.getqbCustomer().getId() + "','"+payment.getDate()+"','"+payment.getcustomFields()+"','"+payment.getAmount()+"','Custom','"+payment.getMemo()+"','"+payment.getappliedToAmount().get(i)+"','"+payment.getappliedTo().get(i)+"')");//amount or applied to amount (need to change)
           }
           }
        }
    }

    
}
