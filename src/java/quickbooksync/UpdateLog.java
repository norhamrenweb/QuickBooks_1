/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author nmohamed
 */
public class UpdateLog {
    static Logger log = Logger.getLogger(MappingTable.class.getName());
    private java.sql.Statement statement;
    
    private final Driver myDriver = new org.postgresql.Driver();
       
        private Connection conn = null;
        public UpdateLog(Config config) {
//            try{
//        this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());//"jdbc:postgresql://localhost:5432/postgres","postgres","rapunzel");
//        }catch (SQLException ex) {
//            
//            StringWriter errors = new StringWriter();
//ex.printStackTrace(new PrintWriter(errors));
//log.error(ex+errors.toString());
//        }
    }
        public void updateinvoicelog (List<QBInvoice> invoices, String action, Config config )
        {
            try{
                 this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());
        statement = conn.createStatement();
        for(QBInvoice invoice : invoices)
        {ParseChargeID xml = new ParseChargeID();
                 String   chargeid = xml.parsechargeid(invoice.getcustomFields());
        statement.executeUpdate("insert into invoicelog (customerid,id,invoiceid,chargeid,amount,action,actiondate) values ('"+ invoice.getqbCustomer().getId() +"','"+invoice.getId()+ "','"+invoice.getinvoiceId()+"','"+chargeid+"','"+ invoice.getAmount()+ "','"+action+"',now())");
        
        }
       }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
}
         public void updatecustlog (List<QBCustomer> customers, String action,Config config ) throws SQLException, ParserConfigurationException, TransformerException, TransformerConfigurationException, SAXException, IOException
        {
            try{
                 this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());
        statement = conn.createStatement();
        for(QBCustomer customer : customers)
        {
        statement.executeUpdate("insert into custlog (custid,familyid,action,actiondate) values ('"+ customer.getId() +"','"+customer.getrwId()+ "','"+action+"',now())");
        
        }}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
        }
          public void updatepaymentlog (List<QBPayment> payments, String action, Config config ) throws SQLException, ParserConfigurationException, TransformerException, TransformerConfigurationException, SAXException, IOException
        {
            try{
                 this.conn = DriverManager.getConnection(config.getEdudburl(),config.getEdudbuser(),config.getEdudbpswd());
        statement = conn.createStatement();
        for(QBPayment payment : payments)
        {/*ParseChargeID xml = new ParseChargeID();
                 String   rwpaymentid = xml.parsepaymentid(payment.getcustomFields());*/
        statement.executeUpdate("insert into paymentlog (customerid,recievepaymentid,paymentid,amount,action,actiondate) values ('"+ payment.getqbCustomer().getId() +"','"+payment.getpaymentId()+"','"+payment.getcustomFields()+"','"+ payment.getAmount()+ "','"+action+"',now())");
        
        }
       }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
}
}
