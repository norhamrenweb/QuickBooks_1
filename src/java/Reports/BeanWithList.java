/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import java.util.List;

/**
 *
 * @author nmohamed
 */
public class BeanWithList {
    private List<String> objectives;
    private List<String> rating;
    private String subject;
    private String nameStudent;
    private String dob;
    private String age;

    public String getName() {
        return nameStudent;
    }

    public void setName(String name) {
        this.nameStudent = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    
    public BeanWithList(String subject,List<String> objs,List<String> rating) {
      this.objectives=objs;
      this.rating = rating;
      this.subject= subject;
      
    }

     public BeanWithList(String subject,List<String> objs,List<String> rating,String name, String dob,String age) {
      this.objectives=objs;
      this.rating = rating;
      this.subject= subject;
      this.nameStudent = name;
      this.dob = dob;
      this.age = age;
      
    }
     
    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }

    public List<String> getRating() {
        return rating;
    }

    public void setRating(List<String> rating) {
        this.rating = rating;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

   
    
}
