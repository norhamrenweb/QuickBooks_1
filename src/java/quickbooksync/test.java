/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class test {
      static Logger log = Logger.getLogger(InsertPayment.class.getName());
    public void insertPayment (List<QBPayment> addlist) throws SQLException, ClassNotFoundException, ParseException
    {
    DBconnection connectQB = new DBconnection();
        connectQB.createconnQB();
        String fixamount;
       
      
        for(QBPayment payment : addlist)
        {
          
            //fixamount = invoice.getAmount();
           //String chargeid = pc.getAppliedtoRefID(Integer.parseInt(payment.getpaymentId()));

           if(payment.getappliedTo()== null){
               double dd=  payment.getAmount();///100;
         fixamount = Double.toString(dd);
        connectQB.statementQB.executeUpdate("insert into ReceivePayments (CustomerId,Date,Memo,CustomFields,Amount,AutoApply) values ('"+ payment.getqbCustomer().getId() + "','"+payment.getDate()+"','"+payment.getMemo()+"','"+payment.getcustomFields()+"','"+fixamount+"','ExistingTransactions')");
           }
           else
           { 
               for(int i = 0; i <= payment.getappliedTo().size(); i++)
               {
            connectQB.statementQB.executeUpdate("insert into ReceivePaymentsAppliedTo (CustomerId,Date,Memo,CustomFields,Amount,AppliedToRefId) values ('"+ payment.getqbCustomer().getId() + "','"+payment.getDate()+"','"+payment.getMemo()+"','"+payment.getcustomFields()+"','"+payment.getappliedToAmount().get(i)+"','"+payment.getappliedTo().get(i)+"')");//amount or applied to amount (need to change)
           }
           }
        }
    
}
}
//when i find a payment in the add list i serach for its id in the pc table and the count, if the count is 1 i getthe charge id
//if the count is 2 i get two charge id and amounts and insert twice(for loop)
//for the count no <0 insert the payment