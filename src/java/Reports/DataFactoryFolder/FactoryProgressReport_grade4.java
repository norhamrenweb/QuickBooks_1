/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

import Montessori.DBConect;
import Montessori.Objective;
import Montessori.Subject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nmoha
 */
public class FactoryProgressReport_grade4 extends DataFactory {

    public FactoryProgressReport_grade4(String cTerm,String cYear) {
        nameStudent = "";
        dob = "";
        age = "";
        grade = "";
        term = "";
         this.currentTerm = cTerm;
        this.currentYear = cYear;
    }

    @Override
    public Collection getDataSource(HttpServletRequest hsr, String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException {

        String studentId = idStudent;
        java.util.Vector coll = new java.util.Vector();
        ArrayList<String> os4 = new ArrayList<>();
        ArrayList<String> as4 = new ArrayList<>();
        cargarAlumno(studentId);
        
        String yearId = this.currentYear;
        String termId = this.currentTerm;
        
        TreeMap<Integer , Profesor> mapTeachers = getTeachers(yearId, termId,idStudent,1);
        HashMap<String, String> mapComentarios = getComments(yearId, termId, idStudent);

        for (Map.Entry<Integer, Profesor> entry : mapTeachers.entrySet()) {
            String key = entry.getValue().getClassId();
            String aux = "No Comments";
            Profesor value = entry.getValue();

            if (mapComentarios.containsKey(key)) {
                aux = mapComentarios.get(key);
            }

            String nameAsignatura = value.getAsignatura(); //limpiarNameAsignatura(value.getAsignatura());
            String auxOs = nameAsignatura + "#" + value.getFirstName() + "#" + aux;
            os4.add(auxOs);
        }
        
        String nameTerm="",nameYear="";
        ResultSet rs3 = DBConect.ah.executeQuery("select name from SchoolTerm where TermID = " + termId + " and YearID = " + yearId);
        while (rs3.next()) {
            nameTerm = "" + rs3.getString("name");
        }
        ResultSet rs4 = DBConect.ah.executeQuery("select SchoolYear from SchoolYear where yearID = " + yearId);
        while (rs4.next()) {
            nameYear = "" + rs4.getString("SchoolYear");
        }

        this.term = nameTerm + " / " + nameYear;

        term = term.replace("Q", "T");
        os4.add("Head of School#Kim Euston-Brown#" + getSuperComment(yearId, termId, idStudent));

        String nameTutor = "#Tutor";
        String consulta = "select firstName,lastName from(select StaffID from classes inner join roster on roster.classid = classes.classid where homeroom = 1 and roster.studentid = " + idStudent + " and roster.enrolled1 = 1 and classes.yearid =" + yearid + ") a inner join person on a.StaffID= person.personid";
        ResultSet rs = DBConect.ah.executeQuery(consulta);
        while (rs.next()) {
            nameTutor = "#" + rs.getString("firstName") + " " + rs.getString("lastName");
        }

        coll.add(new BeanWithList(null, os4, as4, nameStudent, dob, age, grade, term + nameTutor));

        return coll;
    }

    private String getSuperComment(String yearId, String termId, String idStudent) {
        try {
            String consulta = "select * from report_comments where supercomment=true and studentid = " + idStudent + "and term_id = " + termId + " and yearterm_id=" + yearId;
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                return rs.getString("comment");
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        }
        return "No comments";
    }

    private HashMap<String, String> getComments(String yearId, String termId, String id) throws SQLException {
        HashMap<String, String> mapComment = new HashMap<>();

        try {
            String consulta = "select * from report_comments where supercomment=false and studentid = " + id + " and term_id = " + termId + " and yearterm_id=" + yearId + " order by date_created ASC";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                mapComment.put(rs.getString("subject_id"), rs.getString("comment"));
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        }

        return mapComment;
    }

    @Override
    public String getNameReport() {
        return "progress_report_2017_gr4.jasper";
    }

}
