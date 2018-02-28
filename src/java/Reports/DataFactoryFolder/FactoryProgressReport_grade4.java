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

/**
 *
 * @author nmoha
 */
public class FactoryProgressReport_grade4 extends DataFactory {

    public FactoryProgressReport_grade4() {
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

        for (Map.Entry<Integer, Profesor> entry : mapTeachers.entrySet()) {
            String key = entry.getValue().getClassId();
            String aux = "No Comments";
            Profesor value = entry.getValue();

            if (mapComentarios.containsKey(key)) {
                aux = mapComentarios.get(key);
            }

            String nameAsignatura = limpiarNameAsignatura(value.getAsignatura());
            String auxOs = nameAsignatura + "#" + value.getFirstName() + "#" + aux;
            os4.add(auxOs);
        }
        os4.add("Head of School#Kim Euston-Brown#" + getSuperComment(idStudent));
        coll.add(new BeanWithList(null, os4, as4, nameStudent, dob, age, grade, term));

        return coll;
    }

    private String limpiarNameAsignatura(String name) {
        int ini = 0;
        int posEspacio = name.indexOf(" ");
        if (posEspacio == -1) {
            return name;
        }
        if (name.substring(0, posEspacio).toUpperCase().contains("GR") || name.substring(0, posEspacio).toUpperCase().contains("JP") || name.substring(0, posEspacio).toUpperCase().contains("PP")) {
            ini = posEspacio + 1;
        }

        return name.substring(ini, name.length());
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
        return "";
    }

    private HashMap<String, String> getComments(String id) throws SQLException {
        HashMap<String, String> mapComment = new HashMap<>();

        try {
            String consulta = "select * from report_comments where supercomment=false and studentid = " + id + "order by date_created ASC";
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

    private TreeMap<Integer, Profesor> getTeachers(String id) throws SQLException {
        TreeMap<Integer, Profesor> mapTeachers = new TreeMap<>();
        ArrayList<Profesor> listaProfesores = new ArrayList<>();
        TreeMap<String, String> mapNames = new TreeMap<>();

        try {
            ArrayList<Integer> staffids = new ArrayList<>();
            ArrayList<String> classids = new ArrayList<>();
            ArrayList<String> coursesTitles = new ArrayList<>();
            ArrayList<Integer> rcs = new ArrayList<>();

            String consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID,courses.RCPlacement from Roster inner join Classes"
                    + " on Roster.ClassID = Classes.ClassID"
                    + " inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Roster.StudentID = " + id + "and Classes.yearid = " + this.yearid + "and Courses.ReportCard = 1 order by courses.RCPlacement";
            ResultSet rs = DBConect.ah.executeQuery(consulta);
            while (rs.next()) {
                staffids.add(rs.getInt("StaffID"));
                classids.add(rs.getString("CourseID"));
                coursesTitles.add(rs.getString("Title"));
                rcs.add(rs.getInt("RCPlacement"));
            }

            consulta = "select FirstName,LastName,Email,PersonID from Person";
            ResultSet rs3 = DBConect.ah.executeQuery(consulta);
            while (rs3.next()) {
                mapNames.put(rs3.getString("PersonID"), rs3.getString("FirstName") + ", " + rs3.getString("LastName"));
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
                listaProfesores.get(i).setClassId(classids.get(i));
                mapTeachers.put(rcs.get(i), listaProfesores.get(i));
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
        return "progress_report_2017_gr4.jasper";
    }

}
