/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.util.List;



/**
 *
 * @author nmohamed
 */
public class Runsync {

   

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    
    public void runsync(Config config) {
       
  
  new CustomerSync().customersync(config);     
new InvoiceSync().invoicesync(config);
new PaymentSync().paymentsync(config);
        
    }
}
    

