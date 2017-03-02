/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nmohamed
 */
public class Lessons {
    
    private int id;
    private String name;
    private String start;
    private String finish;
    private boolean template;
    private Level level;
    private Objective subsection;
    private Subject subject;
    private String[] equipmentid;
    private int teacherid;
  

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }
    private String date;
    private List<Students> students;

    public String[] getEquipmentid() {
        return equipmentid;
    }

    public int getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public void setEquipmentid(String[] equipmentid) {
        this.equipmentid = equipmentid;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Objective getSubsection() {
        return subsection;
    }

    public void setSubsection(Objective subsection) {
        this.subsection = subsection;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

   

  

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
   

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    
    
}
