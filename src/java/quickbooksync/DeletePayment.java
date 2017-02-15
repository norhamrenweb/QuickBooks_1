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
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author nmohamed
 */
public class DeletePayment {
    static Logger log = Logger.getLogger(DeletePayment.class.getName());
    public void deletepayment (List<QBPayment> deletelist,Config config)
    {
        try{
            
        
       DBconnection connectQB = new DBconnection();
        connectQB.createconnQB(config);
        
    for(QBPayment y : deletelist)
    {
       //ResultSet rs= connectQB.statementQB.executeQuery("select InvoiceId from InvoiceLineItems where CustomFields='"+y.getcustomFields()+"'");
     
    connectQB.statementQB.executeUpdate("Delete From ReceivePayments where ID = '"+ y.getpaymentId()+"'");
        
    }
}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
    }
    
}
