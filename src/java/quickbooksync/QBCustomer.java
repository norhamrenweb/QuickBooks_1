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
public class QBCustomer {
     private String id;
   private String name;
   private String firstName;
   private String lastName;
   private int rwid;
   public QBCustomer() {}
     
    public int getrwId(){
      return rwid;
   }
  public void setrwId( int rwid ) {
      this.rwid =rwid;
   }
   public String getId(){
      return id;
   }
  public void setId( String id ) {
      this.id = id;
   }
 
 
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
  public String getFirstName() {
        return firstName;
    }
 
    public void setFirstName(String name) {
        this.firstName = name;
    }
    public String getLastName() {
        return lastName;
    }
 
    public void setLastName(String name) {
        this.lastName = name;
    }
 
}
