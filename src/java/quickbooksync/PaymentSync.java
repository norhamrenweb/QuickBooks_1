/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author nmohamed
 */
public class PaymentSync {
    
    /*
    
    
    
    
    
    */
     static Logger log = Logger.getLogger(Runsync.class.getName());

    
    public void paymentsync(Config config){
            
            try {
       
            //----------------------------------------------------------------------
            UpdateLog logdb = new UpdateLog(config);
            //Retrieve QB and RW payments based on a start date (start date not done yet)
            List <RWPayment> allrwpayment= new ArrayList<>();
            RetrievePayment x2 = new RetrievePayment();
            allrwpayment = x2.retrieverwPayment(config);
            List <QBPayment> allpayment= new ArrayList<>();
            
            allpayment = x2.retrievePayment(config);
            //-------------------------------------------------------------------------
            //Check QB payments against RW to check for updates or deletion
            List <QBPayment> addlist= new ArrayList<>();
            List <QBPayment> updatelist= new ArrayList<>();
            List <QBPayment> deletelist= new ArrayList<>();
            PaymenttoCharge pc = new PaymenttoCharge();
            
            if (allpayment.isEmpty())// incase QB payment table was empty then no need to check
            {for(RWPayment u : allrwpayment){
                QBPayment n = new QBPayment();
                QBCustomer cust = new QBCustomer();
                MappingTable family = new MappingTable(config);
                cust.setId(family.checkmappingrw(u.getrwFamily().getId(),config));
                n.setMemo(u.getDescription());
              n.setappliedTo(pc.getAppliedtoRefID(u.getpaymentId(),config));
              n.setappliedToAmount(pc.getAppliedtoAmount(u.getpaymentId(),config));
           
                n.setDate(u.getDate());
                n.setQBCustomer(cust);
                n.setcustomFields(""+u.getpaymentId());
                n.setAmount(u.getAmount());
                addlist.add(n);
                                      
            }
            
            }
            else{
                //first check for deletion
                for (QBPayment y2 : allpayment) {
                    String result = "";
                    String paymentid ;
                    ParseChargeID xml = new ParseChargeID();
                    paymentid = xml.parsepaymentid(y2.getcustomFields());
                    
                    if(paymentid != null)
                    {
                       
                       Integer cid =Integer.parseInt(paymentid);
                        for(RWPayment z2 : allrwpayment)
                        {
                            
                            if(cid == z2.getpaymentId())
                                //match found¨
                            {
                                result = "Match Found";
                                break;
                            }
                            
                        }
                    }
                    if(!result.equals("Match Found"))// if chargeid is null it will be added to the delete list
                    {
                        QBPayment n = new QBPayment();
                        
                        n.setpaymentId(y2.getpaymentId());
                        n.setAmount(y2.getAmount());
                        n.setQBCustomer(y2.getqbCustomer());
                        n.setcustomFields(y2.getcustomFields());
                        
                        
                        deletelist.add(n);
                    }
                }
                DeletePayment delete = new DeletePayment();
                 if(!deletelist.isEmpty())
            {
                 delete.deletepayment(deletelist,config);
                logdb.updatepaymentlog(deletelist,"delete",config);
            }
                
             allpayment = x2.retrievePayment(config);// get the updated list of payments after the deletion  
                
                //----------------------------------------------------------------------
                //Second: update the amount field and insert
                
                
                
                String compareresult = "Match";
                for (RWPayment y3 : allrwpayment) {
                    
                    
                    for(QBPayment z3 : allpayment)
                    {
                        ParseChargeID xml = new ParseChargeID();
                        String m = z3.getcustomFields();
                        if (!m.equals("0")){
                            
                            String compare = xml.parsepaymentid(m);
                            if(y3.getpaymentId() == Integer.parseInt(compare))
                            {
                                //match is found
                                // check if the amount field is the same
                                double fixamount = z3.getAmount();//*100;
                                if(!Objects.equals(fixamount, y3.getAmount()))
                                { QBPayment n = new QBPayment();
                                
                                n.setAmount(y3.getAmount());
                                n.setQBCustomer(z3.getqbCustomer());
                                n.setpaymentId(z3.getpaymentId());
                                n.setcustomFields(z3.getcustomFields());
                                updatelist.add(n);
                                
                                }
                                break;
                            }
                             else   compareresult = "Match not found";
                        }
                       
                    }
                    if(compareresult.equals("Match not found"))
                    {
                        QBPayment n = new QBPayment();
                        QBCustomer cust = new QBCustomer();
                    MappingTable family = new MappingTable(config);
                cust.setId(family.checkmappingrw(y3.getrwFamily().getId(),config));
                        //Item item = new Item();
                        // hard coded till we figurs out how it will get this input
                        n.setMemo(y3.getDescription());
                        
                        n.setDate(y3.getDate());
                        n.setQBCustomer(cust);
                        n.setcustomFields("<CustomField><Name>PaymentID</Name><Value>"+y3.getpaymentId()+"</Value></CustomField>");
                       // n.setdueDate(y3.getdueDate());
                        //item.setitemQuantity(1);
                        
                         //item.setitemName("admission fees");
                        //    n.setItem(item);
                        n.setappliedTo(pc.getAppliedtoRefID(y3.getpaymentId(),config));
                            n.setAmount(y3.getAmount());
                            addlist.add(n);
                                                
                    }
                    
                }}
            UpdatePayment update = new UpdatePayment();
            if (!updatelist.isEmpty())
            {
                update.updatePayment(updatelist,config);
                logdb.updatepaymentlog(updatelist,"update",config);
            }
            
            InsertPayment add = new InsertPayment();
            if(!addlist.isEmpty())
            {
                add.insertPayment(addlist,config);
                logdb.updatepaymentlog(addlist,"addition",config);
            }
           
        } catch (SQLException | ParserConfigurationException | TransformerException | SAXException | IOException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
    
}
}
