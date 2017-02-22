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
    private Timestamp start;
    private Timestamp finish;
    private boolean template;
    private Level level;
    private Subsection subsection;
    private Subject subject;
    private String[] equipmentid;

    public String[] getEquipmentid() {
        return equipmentid;
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

    public Subsection getSubsection() {
        return subsection;
    }

    public void setSubsection(Subsection subsection) {
        this.subsection = subsection;
    }

    public Subject getSubject() {
        return subject;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getFinish() {
        return finish;
    }

    public void setFinish(Timestamp finish) {
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
    private String date;

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
