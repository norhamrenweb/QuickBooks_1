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
            // CAMBIAR EL SPLIT 
        String studentId = idStudent;
        java.util.Vector coll = new java.util.Vector();
        ArrayList<String> os4 = new ArrayList<>();
        ArrayList<String> as4 = new ArrayList<>();
        cargarAlumno(studentId);

        os4.add("obj 1");
        as4.add("A");
        os4.add("obj 2");
        as4.add("P");
        os4.add("obj 3");
        as4.add("M");
        os4.add("obj 4");
        as4.add("M");

        if (coll.isEmpty()) {
            //coll.add(new BeanWithList("Matematicas:Profesor ALex:20:COMENTARIO DE MATEMATICAS", os4, as4, nameStudent, dob, age, grade, term));
        }
        ArrayList<String> os5 = new ArrayList<>();
        ArrayList<String> as5 = new ArrayList<>();
        os5.add("obj 7");
        as5.add("M");
        os5.add("obj 8");
        as5.add("P");

        coll.add(new BeanWithList("History:Teacher Smith:40:comment about History ...", os5, as5, nameStudent, dob, age, grade, term));
        coll.add(new BeanWithList("Computer Science:Teacher Jones:23:comment about Computer ...", os4, as4, nameStudent, dob, age, grade, term));
        coll.add(new BeanWithList("Chemistry:Teacher Williams:12:comment about Chemistry ...", os4, as4, nameStudent, dob, age, grade, term));
        coll.add(new BeanWithList("Drawing:Teacher Brown:90:...", os4, as4, nameStudent, dob, age, grade, term));
        coll.add(new BeanWithList("Economics:Teacher Taylor:80:comment about ...", os4, as4, nameStudent, dob, age, grade, term));
        coll.add(new BeanWithList("English Language:Teacher Davies:70: ", os4, as4, nameStudent, dob, age, grade, term));
        coll.add(new BeanWithList("Physics:Teacher Wilson:0:comment about Physics ...", new ArrayList<>(), new ArrayList<>(), nameStudent, dob, age, grade, term));

        return coll;
    }

    private HashMap<String, String> getComments(String id) throws SQLException {
        HashMap<String, String> mapComment = new HashMap<>();

        try {
            String consulta = "select * from subjects_comments where studentid = " + id + "order by date_created DESC";
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

    private HashMap<String, Profesor> getTeachers(String id) throws SQLException {
        HashMap<String, Profesor> mapTeachers = new HashMap<>();
        ArrayList<Profesor> listaProfesores = new ArrayList<>();
        HashMap<String, String> mapNames = new HashMap<>();

        try {
            ArrayList<Integer> staffids = new ArrayList<>();
            ArrayList<String> classids = new ArrayList<>();
            ArrayList<String> coursesTitles = new ArrayList<>();

            String consulta = "select StaffID, Classes.ClassID , Courses.Title from Roster inner join Classes"
                    + " on Roster.ClassID = Classes.ClassID"
                    + " inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Roster.StudentID = " + id + "and Classes.yearid = " + this.yearid;
            ResultSet rs = DBConect.ah.executeQuery(consulta);
            while (rs.next()) {
                staffids.add(rs.getInt("StaffID"));
                classids.add(rs.getString("ClassID"));
                coursesTitles.add(rs.getString("Title"));
            }

            consulta = "select FirstName,LastName,Email,PersonID from Person";
            ResultSet rs3 = DBConect.ah.executeQuery(consulta);
            while (rs3.next()) {
                mapNames.put(rs3.getString("PersonID"), rs3.getString("LastName") + ", " + rs3.getString("FirstName"));
            }

            for (Integer i : staffids) {
                if (mapNames.containsKey("" + i)) {
                    listaProfesores.add(new Profesor(mapNames.get("" + i), "", i, ""));
                } else {
                    listaProfesores.add(new Profesor(" ", "", i, ""));
                }
            }
            for (int i = 0; i < listaProfesores.size(); i++) {
                listaProfesores.get(i).setAsignatura(coursesTitles.get(i));
                mapTeachers.put(classids.get(i), listaProfesores.get(i));
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        }

        return mapTeachers;
    }

    @Override
    public String getNameReport() {
        return "Academic_Report_grade7.jasper";
    }
}
