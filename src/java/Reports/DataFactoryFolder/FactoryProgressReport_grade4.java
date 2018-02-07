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
        
        HashMap<String, Profesor> mapTeachers = getTeachers(idStudent);
        HashMap<String, String> mapComentarios = getComments(idStudent);
        
        for (Map.Entry<String, Profesor> entry : mapTeachers.entrySet()) {
            String key = entry.getKey();
            Profesor value = entry.getValue();
            os4.add(value.getAsignatura()+":"+value.getFirstName());
            
            if(mapComentarios.containsKey(key)) as4.add(mapComentarios.get(key));
            else as4.add(" ");
        }
      
        if (coll.isEmpty()) {
            coll.add(new BeanWithList(null, os4, as4, nameStudent, dob, age, grade, term));
        }
        return coll;
    }

    private HashMap<String, String> getComments(String id) throws SQLException {
        HashMap<String, String> mapComment = new HashMap<>();

        try {         
            String consulta = "select * from subjects_comments where studentid = " + id +"order by date_created DESC";
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

            String consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID from Roster inner join Classes"
                    + " on Roster.ClassID = Classes.ClassID"
                    + " inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Roster.StudentID = " + id +"and Classes.yearid = "+this.yearid+"and Courses.ReportCard = 1";
            ResultSet rs = DBConect.ah.executeQuery(consulta);
            while (rs.next()) {
                staffids.add(rs.getInt("StaffID"));
                classids.add(rs.getString("CourseID"));
                coursesTitles.add(rs.getString("Title"));
            }
      
            consulta = "select FirstName,LastName,Email,PersonID from Person";
            ResultSet rs3 = DBConect.ah.executeQuery(consulta);
            while (rs3.next()) {
               mapNames.put(rs3.getString("PersonID"),rs3.getString("LastName")+", "+rs3.getString("FirstName"));
            }
            
            for (Integer i : staffids) {  
                if(mapNames.containsKey(""+i)){
                    listaProfesores.add(new Profesor(mapNames.get(""+i), "", i, ""));
                }
                else listaProfesores.add(new Profesor(" ", "", i, ""));
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
        return "progress_report_2017_gr4.jasper";
    }
}
