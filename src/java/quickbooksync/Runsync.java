/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;



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
    public static String qbdburl;
    public static String rwdburl;
    public static String qbdbuser;
    public static String rwdbuser;
    public static String rwdbpswd;
    public static String qbdbpswd;
    public static String itemname;
    public static String startdate;
    public static String edudburl;
    public static String edudbpswd;
    public static String edudbuser;
    public void runsync(String[] args) {
       
    qbdburl = args[0];
    rwdburl = args[3];
    edudburl = args[6];
    qbdbuser = args[1];
    qbdbpswd = args[2];
    rwdbuser = args[4];
    rwdbpswd = args[5];
    edudbuser = args[7];
    edudbpswd = args[8];
    startdate = args[9];
    itemname = args[10];
  new CustomerSync().customersync(args);     
new InvoiceSync().invoicesync(args);
new PaymentSync().paymentsync(args);
        
    }
}
    

