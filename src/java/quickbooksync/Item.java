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
public class Item {
    private String itemName;
    private String itemId;
    private int itemQuantity;
    private Double itemAmount;
    
    public void Item(){}
    public String getitemId() {
      return itemId;
   }
   public void setitemId( String id ) {
      this.itemId = id;
   }
 
    public String getitemName() {
        return itemName;
    }
 
    public void setitemName(String name) {
        this.itemName= name;
    }
 
   
 
    
 public int getitemQuantity() {
        return itemQuantity;
    }
 
    public void setitemQuantity(int quantity) {
        this.itemQuantity = quantity;
    }
 
    public void setitemAmount(Double amount) {
        this.itemAmount = amount;
    }
    public Double getitemAmount() {
        return itemAmount;
    }
    
}
