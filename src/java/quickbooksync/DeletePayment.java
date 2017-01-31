/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class DeletePayment {
    static Logger log = Logger.getLogger(DeletePayment.class.getName());
    public void deletepayment (List<QBPayment> deletelist) throws SQLException, ClassNotFoundException
    {
       DBconnection connectQB = new DBconnection();
        connectQB.createconnQB();
        
    for(QBPayment y : deletelist)
    {
       //ResultSet rs= connectQB.statementQB.executeQuery("select InvoiceId from InvoiceLineItems where CustomFields='"+y.getcustomFields()+"'");
     
    connectQB.statementQB.executeUpdate("Delete From ReceivePayments where ID = '"+ y.getpaymentId()+"'");
        
    }

    }
    
}
