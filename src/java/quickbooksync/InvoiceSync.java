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
import static quickbooksync.InvoiceSync.log;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author nmohamed
 */
public class InvoiceSync {
        static Logger log = Logger.getLogger(Runsync.class.getName());

    
    public void invoicesync(Config config){
            
        UpdateLog logdb = new UpdateLog(config);
        List <RWCharge> allcharge= new ArrayList<>();
        RetrieveInvoice x2 = new RetrieveInvoice();
        allcharge = x2.retrieveCharge(config);
        List <QBInvoice> allinvoice= new ArrayList<>();
        allinvoice = x2.retrieveInvoice(config);
        List <QBInvoice> addlist= new ArrayList<>();
        List <QBInvoice> discountlist= new ArrayList<>();
        List <QBInvoice> updatelist= new ArrayList<>();
        List <QBInvoice> deletelist= new ArrayList<>();
        if (allinvoice.isEmpty())// incase QB invoice table was empty then no need to check
        {for(RWCharge u : allcharge){
            QBInvoice n = new QBInvoice();
            QBCustomer cust = new QBCustomer();
            MappingTable family = new MappingTable(config);
            cust.setId(family.checkmappingrw(u.getrwFamily().getId(),config));
            Item item = new Item();
            
            item.setitemQuantity(1);
            n.setMemo(u.getDescription());
            
            n.setDate(u.getDate());
            n.setItem(item);
            n.setQBCustomer(cust);
            n.setcustomFields("<CustomField><Name>ChargeID</Name><Value>"+u.getchargeID()+"</Value></CustomField>");
            n.setdueDate(u.getdueDate());
            if(Math.signum(u.getAmount())== -1.0)
            {
                item.setitemName("Discount");// hard coded till we figurs out how it will get this input
                n.setItem(item);
                n.setAmount(Math.abs(u.getAmount()));
                discountlist.add(n);
            }
            else            
            {
                item.setitemName(config.getItemname());
                n.setItem(item);
                n.setAmount(u.getAmount());
                addlist.add(n);
            }
            
            
        }
        
        }
        else{
            //first check for deletion
            for (QBInvoice y2 : allinvoice) {
                String result = "";
                String chargeid ;
                ParseChargeID xml = new ParseChargeID();
                chargeid = xml.parsechargeid(y2.getcustomFields());
                
                if(chargeid != null)
                {
                    
                    Integer cid =Integer.parseInt(chargeid);
                    for(RWCharge z2 : allcharge)
                    {
                        
                        if(cid == z2.getchargeID())
                            //match found¨
                        {
                            result = "Match Found";
                            break;
                        }
                        
                    }
                }
                if(!result.equals("Match Found"))// if chargeid is null it will be added to the delete list
                {
                    QBInvoice n = new QBInvoice();
                    
                    n.setinvoiceId(y2.getinvoiceId());
                    n.setAmount(y2.getAmount());
                    n.setQBCustomer(y2.getqbCustomer());
                    n.setcustomFields(y2.getcustomFields());
                    
                    
                    deletelist.add(n);
                }
            }
            
            DeleteInvoice delete = new DeleteInvoice();
            if(!deletelist.isEmpty())
            {
                delete.deleteinvoice(deletelist,config);
                logdb.updateinvoicelog(deletelist,"delete",config);
            }
            
            allinvoice = x2.retrieveInvoice(config);// get the updated list of invoices after the deletion
            //----------------------------------------------------------------------
            //Second: update the amount field and insert
            
            
            
            String compareresult = "Match";
            for (RWCharge y3 : allcharge) {
                
                
                for(QBInvoice z3 : allinvoice)
                {
                    ParseChargeID xml = new ParseChargeID();
                    String m = z3.getcustomFields();
                    if (!m.equals("0")){
                        
                        String compare = xml.parsechargeid(m);
                        if(y3.getchargeID() == Integer.parseInt(compare))
                        {
                            //match is found
                            // check if the amount field is the same
                            double fixamount = z3.getAmount();//*100;
                            if(!Objects.equals(fixamount, y3.getAmount()))
                            { QBInvoice n = new QBInvoice();
                            n.setId(z3.getId());
                            n.setAmount(y3.getAmount());
                            n.setQBCustomer(z3.getqbCustomer());
                            n.setinvoiceId(z3.getinvoiceId());
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
                    QBInvoice n = new QBInvoice();
                    QBCustomer cust = new QBCustomer();
                    MappingTable family = new MappingTable(config);
                    cust.setId(family.checkmappingrw(y3.getrwFamily().getId(),config));
                    Item item = new Item();
                    // hard coded till we figurs out how it will get this input
                    n.setMemo(y3.getDescription());
                    
                    n.setDate(y3.getDate());
                    n.setQBCustomer(cust);
                    n.setcustomFields("<CustomField><Name>ChargeID</Name><Value>"+y3.getchargeID()+"</Value></CustomField>");
                    n.setdueDate(y3.getdueDate());
                    item.setitemQuantity(1);
                    if(Math.signum(y3.getAmount())== -1.0)
                    {
                        item.setitemName("Discount");
                        n.setItem(item);
                        n.setAmount(Math.abs(y3.getAmount()));
                        discountlist.add(n);
                    }
                    else
                    {
                        item.setitemName(config.getItemname());
                        n.setItem(item);
                        n.setAmount(y3.getAmount());
                        addlist.add(n);
                    }
                    
                }
                
            }}
        UpdateInvoice update = new UpdateInvoice();
        if (!updatelist.isEmpty())
        {
            update.updateInvoice(updatelist,config);
            logdb.updateinvoicelog(updatelist,"update",config);
        }
        InsertInvoice add = new InsertInvoice();
        if(!addlist.isEmpty())
        {
            add.insertInvoice(addlist,config);
            logdb.updateinvoicelog(addlist,"addition",config);
        }
        AddDiscounts discount = new AddDiscounts();
        if(!discountlist.isEmpty())
        {
            discount.addDiscounts(discountlist,config);
            logdb.updateinvoicelog(addlist,"addDiscount",config);
        }
    
}
}
