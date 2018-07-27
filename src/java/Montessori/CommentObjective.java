/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author Norhan
 */
public class CommentObjective {
    private String id;
    private String rating_id;
    private String student_id;
    private String comment;
    private String comment_date;
    private String objective_id;
    private boolean generalcomment;
    private String step_id;
    private String createdby;
    private String rating_name;
    private String modifyby;
    private String term_id;
    private String yearterm_id;
    private String color;
    private String namePresentation;
    
    public CommentObjective(String id, String rating_id, String student_id, String comment, String comment_date, String objective_id, boolean generalcomment, String step_id, String createdby, String modifyby, String term_id, String yearterm_id,String c,String np) {
        this.id = id;
        this.rating_id = rating_id;
        this.student_id = student_id;
        this.comment = comment;
        this.comment_date = comment_date;
        this.objective_id = objective_id;
        this.generalcomment = generalcomment;
        this.step_id = step_id;
        this.createdby = createdby;
        this.modifyby = modifyby;
        this.term_id = term_id;
        this.yearterm_id = yearterm_id;
        this.color = c;
        this.namePresentation = np;
    }

    public String getNamePresentation() {
        return namePresentation;
    }

    public void setNamePresentation(String namePresentation) {
        this.namePresentation = namePresentation;
    }
    
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getRating_name() {
        return rating_name;
    }

    public void setRating_name(String rating_name) {
        this.rating_name = rating_name;
    }
    
    public String getId() {
        return id;
    }

    public String getRating_id() {
        return rating_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getComment() {
        return comment;
    }

    public String getComment_date() {
        return comment_date;
    }

    public String getObjective_id() {
        return objective_id;
    }

    public boolean isGeneralcomment() {
        return generalcomment;
    }

    public String getStep_id() {
        return step_id;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getModifyby() {
        return modifyby;
    }

    public String getTerm_id() {
        return term_id;
    }

    public String getYearterm_id() {
        return yearterm_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public void setObjective_id(String objective_id) {
        this.objective_id = objective_id;
    }

    public void setGeneralcomment(boolean generalcomment) {
        this.generalcomment = generalcomment;
    }

    public void setStep_id(String step_id) {
        this.step_id = step_id;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public void setModifyby(String modifyby) {
        this.modifyby = modifyby;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public void setYearterm_id(String yearterm_id) {
        this.yearterm_id = yearterm_id;
    }

}
