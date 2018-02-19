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
    
    private int id;
    private String observation;
    private String type;
    private String date;
    private String nameTeacher;
    private String dateString;
    private String numSemana;
    private boolean foto;

    public Boolean getFoto() {
        return foto;
    }

    public void setFoto(Boolean foto) {
        this.foto = foto;
    }
    
    public String getNameTeacher() {
        return nameTeacher;
    }

    public void setNameTeacher(String nameTeacher) {
        this.nameTeacher = nameTeacher;
    }
    private String commentDate;
    private int logged_by;
    private int studentid;
    
    public String getDateString() {
        return dateString;
    }

    public void setDateString(String DateString) {
        this.dateString = DateString;
    }
   
    
    public Observation() {
    }
    public Observation(Observation o2) {
        id = o2.getId();
        observation = o2.getObservation();
        type = o2.getType();
        date = o2.getDate();
        commentDate = o2.getCommentDate();
        logged_by = o2.getLogged_by();
        studentid = o2.getStudentid();
        nameTeacher = o2.getNameTeacher();
        numSemana = o2.getNumSemana();
        foto = o2.getFoto();
    }

    public String getNumSemana() {
        return numSemana;
    }

    public void setNumSemana(String numSemana) {
        this.numSemana = numSemana;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
