/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author nmohamed
 */
public class QBPayment {
    
    
   private QBCustomer qbCustomer ;//customer id
   //private Item item;
   private String paymentId;
   private Date date;
   private String memo;
   private Double amount;
   //private Date dueDate;
   private String customFields;
  private List<String> appliedto = new ArrayList<>();
 private List<String> appliedtoAmount = new ArrayList<>();
    public QBPayment() {
       
    }
   

   
 
   public void QBPayment() {}
  
   public List<String> getappliedTo() {
      return appliedto;
   }
   public void setappliedTo( List<String> appliedto) {
      this.appliedto = appliedto;
   }
   public List<String> getappliedToAmount() {
      return appliedtoAmount;
   }
   public void setappliedToAmount( List<String> appliedtoamount) {
      this.appliedtoAmount = appliedtoamount;
   }
   public String getpaymentId() {
      return paymentId;
   }
   public void setpaymentId( String invoiceid ) {
      this.paymentId = invoiceid;
   }
   /*public String getId() { // this is the item id inside the invoice
      return ID;
   }
   public void setId( String id ) {
      this.ID = id;
   }*/
 
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
   /* public Date getdueDate() {
        return dueDate;
    }
 
    public void setdueDate(Date duedate) {
        this.dueDate = duedate;
    }*/
    public String getcustomFields() {
        return customFields;
    }
 
    public void setcustomFields(String customFields) {
        this.customFields = customFields;
    }
    
  /*  public Item getItem() {
        return item;
    }
 
    public void setItem(Item item) {
        this.item = item;
    }*/


}
