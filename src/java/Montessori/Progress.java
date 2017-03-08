/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author nmohamed
 */
public class Progress {
    private String studentname;
    private String attendancecode;
    private int studentid;
    private String rating;
    private String comment;
    
    public int getStudentid() {
        return studentid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getAttendancecode() {
        return attendancecode;
    }

    public void setAttendancecode(String attendancecode) {
        this.attendancecode = attendancecode;
    }
    
}
