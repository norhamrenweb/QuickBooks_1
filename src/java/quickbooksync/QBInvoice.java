/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.util.Date;

/**
 *
 * @author nmohamed
 */
public class QBInvoice {
    
    
   private QBCustomer qbCustomer ;//customer id
   private Item item;
   private String invoiceId;
   private Date date;
   private String memo;
   private Double amount;
   private Date dueDate;
   private String customFields;
   private String ID;
   

   
 
   public void QBInvoice() {}
  
   public String getinvoiceId() {
      return invoiceId;
   }
   public void setinvoiceId( String invoiceid ) {
      this.invoiceId = invoiceid;
   }
   public String getId() { // this is the item id inside the invoice
      return ID;
   }
   public void setId( String id ) {
      this.ID = id;
   }
 
    public QBCustomer getqbCustomer() {
        return qbCustomer;
    }
 
    public void setQBCustomer(QBCustomer qbCustomer) {
        this.qbCustomer = qbCustomer;
    }
 
   
 
    
 public Date getDate() {
        return date;
    }
 
    public void setDate(Date date) {
        this.date = date;
    }
    public String getMemo() {
        return memo;
    }
 
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Double getAmount() {
        return amount;
    }
 
    public void setAmount(Double amount) {
        this.amount = amount;
    } 
    public Date getdueDate() {
        return dueDate;
    }
 
    public void setdueDate(Date duedate) {
        this.dueDate = duedate;
    }
    public String getcustomFields() {
        return customFields;
    }
 
    public void setcustomFields(String customFields) {
        this.customFields = customFields;
    }
    
    public Item getItem() {
        return item;
    }
 
    public void setItem(Item item) {
        this.item = item;
    }


}
