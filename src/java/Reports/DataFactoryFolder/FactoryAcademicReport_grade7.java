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
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nmoha
 */
public class FactoryAcademicReport_grade7 extends DataFactory {

    private String showGrade;


    public FactoryAcademicReport_grade7(String showgrade,String cTerm,String cYear) {
        nameStudent = "";
        dob = "";
        age = "";
        grade = "";
        term = "";
        showGrade = showgrade;
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

        String nameTerm = "", nameYear = "";
        ResultSet rs3 = DBConect.ah.executeQuery("select name from SchoolTerm where TermID = " + termId + " and YearID = " + yearId);
        while (rs3.next()) {
            nameTerm = "" + rs3.getString("name");
        }
        ResultSet rs4 = DBConect.ah.executeQuery("select SchoolYear from SchoolYear where yearID = " + yearId);
        while (rs4.next()) {
            nameYear = "" + rs4.getString("SchoolYear");
        }
        if (nameTerm.contains("Q2")) {
            nameTerm = "Q1/Q2";
        } else if (nameTerm.contains("Q4")) {
            nameTerm = "Q3/Q4";
        }
        this.dateNewTerm = "";
        this.daysAbsent = "";
        getDaysAbsent_And_DateNewTerm(termId, yearId, idStudent, nameTerm);
        this.term = nameTerm + " , " + nameYear + "#" + showGrade + "#" + this.daysAbsent + "#" + this.dateNewTerm;

        TreeMap<Integer, Profesor> mapTeachers = getTeachers(yearId, termId, idStudent,2);
        HashMap<String, String> mapComentarios = getComments(yearId, termId, idStudent);
        //  ArrayList<String> coursesAsignados = new ArrayList<>();
        ArrayList<String> lessons = new ArrayList<>();
        ResultSet rs;
        rs = DBConect.eduweb.executeQuery("SELECT lesson_id from lesson_stud_att where student_id = '" + studentId + "' and attendance != 'null' and attendance !=' '");
        while (rs.next()) {
            lessons.add("" + rs.getInt("lesson_id"));
        }

        for (Map.Entry<Integer, Profesor> entry : mapTeachers.entrySet()) {
            String key = entry.getValue().getClassId();
            String aux = "No Comments";
            Profesor value = entry.getValue();

            if (mapComentarios.containsKey(key)) {
                aux = mapComentarios.get(key);
            }

            os4 = new ArrayList<>();
            as4 = new ArrayList<>();
            os4 = getSkills(as4, key, studentId, yearId, termId);
            String nameAsignatura = limpiarNameAsignatura(value.getAsignatura());

            String auxOs = nameAsignatura + "#" + value.getFirstName() + "# #" + aux;

            // if (!os4.isEmpty() && !as4.isEmpty()) {
            //  coursesAsignados.add(value.getClassId());
            coll.add(new BeanWithList(auxOs, os4, as4, nameStudent, dob, age, grade, term));

        }
        /*
        for (Map.Entry<String, String> entry : mapComentarios.entrySet()) {
            String key = "" + entry.getKey();
            if (!coursesAsignados.contains(key)) {   
                
                String nameAsignatura =  key;
                String auxOs = nameAsignatura + "#" + "Teacher TEST" + "# #" + entry.getValue();

                coll.add(new BeanWithList(auxOs, os4, as4, nameStudent, dob, age, grade, term));
            }
        }*/

        coll.add(new BeanWithList("Head of School#Kim Euston-Brown# #" + getSuperComment(yearId, termId, idStudent), new ArrayList<>(), new ArrayList<>(), nameStudent, dob, age, grade, term));
        return coll;
    }

    

    /*
select count(Present) from AttendanceDaySummary where studentid = ... and "date" >= 'fechainicioT1'
                and "date" <= 'fechafinalT2' 

select count(attendance) as daysPresent
                from daysetup
                where DayType = 0<!---
                and MODIFIEDBY = '-433'--->
                and convert (varchar, DaySetupDate, 23) >= '#fechainicioT1#'
                and convert (varchar, DaySetupDate, 23) <= '#fechafinalT2#'*/
    private String getSuperComment(String yearId, String termId, String idStudent) {
        try {
                    String termQuery ;
        
        switch (termId) {
            case "2":
                termQuery = " and (term_id = 1 or term_id = 2) ";
                break;
            case "4":
                termQuery = " and (term_id = 3 or term_id = 4) ";
                break;
            default:
                termQuery = " and term_id = " + termId;
                break;
        }
            String consulta = "select * from report_comments where supercomment=true "+termQuery+" and yearterm_id = '"+yearId+"' and studentid = " + idStudent +"ORDER BY date_created DESC";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                return rs.getString("comment");
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        }
        return "No Comments";
    }

    private ArrayList<String> getSkills(ArrayList<String> as, String courseId, String stdId, String yearID, String termId) throws SQLException {
        ArrayList<String> aux = new ArrayList<>();
        try {
            String consulta = "select name,progress_report.term_id,level_id "
                    + "         from objective  inner join progress_report on (objective.id = progress_report.objective_id)"
                    + "         where subject_id = " + courseId + " and progress_report.student_id = " + stdId + " and objective.reportcard = true and year_id=" + yearID
                    + "         order by progress_report.comment_date DESC";

            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {

                String TermLevel = rs.getString("term_id");
                if (termId.contains("1") || termId.contains("2")) {
                    if (TermLevel.contains("1") || TermLevel.contains("2")) {
                        aux.add(rs.getString("name"));
                        as.add("" + rs.getInt("level_id"));
                    }
                } else if (TermLevel.contains("3") || TermLevel.contains("4")) {
                    aux.add(rs.getString("name"));
                    as.add("" + rs.getInt("level_id"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        }

        return aux;
    }

    private HashMap<String, String> getComments(String yearId, String termId, String id) throws SQLException {
        HashMap<String, String> mapComment = new HashMap<>();
        String termQuery ;
        
        switch (termId) {
            case "2":
                termQuery = " and (term_id = 1 or term_id = 2) ";
                break;
            case "4":
                termQuery = " and (term_id = 3 or term_id = 4) ";
                break;
            default:
                termQuery = " and term_id = " + termId;
                break;
        }
                
        try {
            String consulta = "select * from report_comments where supercomment=false and studentid = " + id + termQuery + " and yearterm_id=" + yearId + "order by date_created DESC";
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
        return "Academic_Report_grade7.jasper";
    }
}
