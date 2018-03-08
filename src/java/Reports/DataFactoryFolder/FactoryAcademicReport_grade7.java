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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContext;

/**
 *
 * @author nmoha
 */
public class FactoryAcademicReport_grade7 extends DataFactory {

    public FactoryAcademicReport_grade7() {
        nameStudent = "";
        dob = "";
        age = "";
        grade = "";
        term = "";
    }

    @Override
    public Collection getDataSource(String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException {

        String studentId = idStudent;
        java.util.Vector coll = new java.util.Vector();
        ArrayList<String> os4 = new ArrayList<>();
        ArrayList<String> as4 = new ArrayList<>();
        cargarAlumno(studentId);

        TreeMap<Integer, Profesor> mapTeachers = getTeachers(idStudent);
        HashMap<String, String> mapComentarios = getComments(idStudent);

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
            os4 = getSkills(key);
            String nameAsignatura = limpiarNameAsignatura(value.getAsignatura());
            String auxOs = nameAsignatura + ":" + value.getFirstName() + ": :" + aux;
            for (int i = 0; i < os4.size(); i++) {
                as4.add("");
            }

            //coll.add(new BeanWithList("History:Teacher Smith:40:comment a::bout History ...",new ArrayList<>(), new ArrayList<>(), nameStudent, dob, age, grade, term));
            coll.add(new BeanWithList(auxOs, os4, as4, nameStudent, dob, age, grade, term));

        }
        coll.add(new BeanWithList("Head of School:Kim Euston-Brown: :" + getSuperComment(idStudent), new ArrayList<>(), new ArrayList<>(), nameStudent, dob, age, grade, term));
        return coll;
    }

    private String getSuperComment(String idStudent) {
        try {
            String consulta = "select * from report_comments where supercomment=true and studentid = " + idStudent;
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

    private ArrayList<String> getSkills(String courseId) throws SQLException {
        ArrayList<String> aux = new ArrayList<>();
        try {
            String consulta = "select * from SS_Subjects where SS_Subjects.CourseID = " + courseId;
            ResultSet rs = DBConect.ah.executeQuery(consulta);
            while (rs.next()) {
                aux.add(rs.getString("Subject"));
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        }

        return aux;
    }

    private HashMap<String, String> getComments(String id) throws SQLException {
        HashMap<String, String> mapComment = new HashMap<>();

        try {
            String consulta = "select * from report_comments where studentid = " + id + "order by date_created DESC";
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
