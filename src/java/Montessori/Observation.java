/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.util.Date;

/**
 *
 * @author nmohamed
 */
public class Observation {
    
    private String observation;
    private String type;
private Date date;
private int logged_by;
private int studentid;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLogged_by() {
        return logged_by;
    }

    public void setLogged_by(int logged_by) {
        this.logged_by = logged_by;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }


    
}
