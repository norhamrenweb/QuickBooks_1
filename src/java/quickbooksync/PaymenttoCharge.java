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
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class PaymenttoCharge {
     static Logger log = Logger.getLogger(Runsync.class.getName());
    
    public List<String> getAppliedtoRefID(int paymentId,Config config)
    {  List<String> results = new ArrayList<String>();
        try
    {
       
    
    String[] chargeid= new String[10];// assuming that the no. of charges applied to a single payment is not more than 10
    int count = 0;
    DBconnection connectQB = new DBconnection();
        
        DBconnection connectRW = new DBconnection();
        connectRW.createconnRW(config);
        ResultSet rs3 = connectRW.statementRW.executeQuery("SELECT count(paymentid) FROM \"public\".payment_charges where paymentid= ' "+paymentId+"' GROUP by paymentid " );
        while(rs3.next())
        {
        count =rs3.getInt("count");
        }
         ResultSet rs1 = connectRW.statementRW.executeQuery("Select chargeid from \"public\".payment_charges where paymentid = "+ paymentId );
    while(rs1.next())
    {
        chargeid[count]=rs1.getString("chargeid");
        count=count-1;
       // chargeid = rs1.getInt("ChargeID");
    }
       connectQB.createconnQB(config);
       for (String s: chargeid)
       {
           ResultSet rs2 = connectQB.statementQB.executeQuery("Select InvoiceId from InvoiceLineItems where CustomFields = '<CustomField><Name>ChargeID</Name><Value>"+s+"</Value></CustomField>'");
       
       while (rs2.next()){
        results.add(rs2.getString("InvoiceId"));
              
       
    }
       }
    }catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
       return results;
    }
  public List<String> getAppliedtoAmount(int paymentId,Config config)
    { List<String> amount = new ArrayList<String>();  
        try{
        DBconnection connectRW = new DBconnection();
        connectRW.createconnRW(config);
         ResultSet rs1 = connectRW.statementRW.executeQuery("Select amount from \"public\".payment_charges where paymentid = "+ paymentId );
    while(rs1.next())
    {
        amount.add(rs1.getString("amount"));
     
       // chargeid = rs1.getInt("ChargeID");
    }}catch (SQLException ex) {
            
            StringWriter errors = new StringWriter();
ex.printStackTrace(new PrintWriter(errors));
log.error(ex+errors.toString());
        }
      
       return amount;
    }   
}

