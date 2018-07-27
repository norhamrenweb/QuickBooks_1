/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

import Montessori.DBConect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import Montessori.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nmohamed
 */
public abstract class DataFactory {

    String nameStudent, dob, age, grade, term, termid = "", yearid = "";

    public abstract Collection getDataSource(HttpServletRequest hsr, String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException;
    public abstract String getNameReport();

    protected void cargarAlumno(String studentId) throws SQLException {
        String consulta = "SELECT * FROM Students where StudentId = '" + studentId + "'";
        ResultSet rs = DBConect.ah.executeQuery(consulta);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        while (rs.next()) {
            this.nameStudent = rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName");
            this.dob = rs.getString("Birthdate");
            this.dob = dob.split(" ")[0];
            this.age = "" + (year - Integer.parseInt("" + dob.charAt(0) + dob.charAt(1) + dob.charAt(2) + dob.charAt(3)));
            this.grade = rs.getString("GradeLevel");
        }

        ResultSet rs2 = DBConect.ah.executeQuery("select defaultyearid,defaulttermid from ConfigSchool where configschoolid = 1");
        while (rs2.next()) {
            termid = "" + rs2.getInt("defaulttermid");
            yearid = "" + rs2.getInt("defaultyearid");
        }
        String nameTerm = "", nameYear = "";

        ResultSet rs3 = DBConect.ah.executeQuery("select name from SchoolTerm where TermID = " + this.termid + " and YearID = " + this.yearid);
        while (rs3.next()) {
            nameTerm = "" + rs3.getString("name");
        }
        ResultSet rs4 = DBConect.ah.executeQuery("select SchoolYear from SchoolYear where yearID = " + this.yearid);
        while (rs4.next()) {
            nameYear = "" + rs4.getString("SchoolYear");
        }

        this.term = nameTerm + " / " + nameYear;
    }
     protected String limpiarNameAsignatura(String name) { //SOLO POR QUE LAS ASIGNATURAS NO ESTABAN LIMPIAS
        int ini = 0;
        int posEspacio = name.indexOf(" ");
        if (posEspacio == -1) {
            return name;
        }
        if (!name.substring(0, posEspacio).toUpperCase().contains("GROSS") && ( name.substring(0, posEspacio).toUpperCase().contains("GR") || name.substring(0, posEspacio).toUpperCase().contains("JP") || name.substring(0, posEspacio).toUpperCase().contains("PP"))) {
            ini = posEspacio + 1;
        }

        return name.substring(ini, name.length());
    }
     
      protected TreeMap<Integer, Profesor> getTeachers(String yearId,String termId,String id,int reportno) throws SQLException {
        TreeMap<Integer, Profesor> mapTeachers = new TreeMap<>();
        ArrayList<Profesor> listaProfesores = new ArrayList<>();
        TreeMap<String, String> mapNames = new TreeMap<>();

        try {
            ArrayList<Integer> staffids = new ArrayList<>();
            ArrayList<String> classids = new ArrayList<>();
            ArrayList<String> coursesTitles = new ArrayList<>();
            ArrayList<Integer> rcs = new ArrayList<>();
            //the default query that shows all
             String consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID,courses.RCPlacement from Roster inner join Classes"
                    + "  on Roster.ClassID = Classes.ClassID"
                    + "  inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Classes.Term"+termId+"=1 and Roster.StudentID = " + id + " and Classes.yearid = " + yearId + " and Courses.ReportCard = 1 order by courses.RCPlacement";
//             the query that shows courses that has departemnt Reportcode1 or reportcard1,2
            if(reportno == 1){
             consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID,courses.RCPlacement from Roster inner join Classes"      
                    + "  on Roster.ClassID = Classes.ClassID"
                    + "  inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Classes.Term"+termId+"=1 and Roster.StudentID = " + id + " and Classes.yearid = " + yearId + " and Courses.ReportCard = 1 and (department='Report Codes 1' or department='Report Codes 1, 2' )order by courses.RCPlacement";
            }else if(reportno == 2){
//                  the query that shows courses that has departemnt Reportcode2 or reportcard1,2
               consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID,courses.RCPlacement from Roster inner join Classes"
                    + "  on Roster.ClassID = Classes.ClassID"
                    + "  inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Classes.Term"+termId+"=1 and Roster.StudentID = " + id + " and Classes.yearid = " + yearId + " and Courses.ReportCard = 1 and (department='Report Codes 2' or department='Report Codes 1, 2' )order by courses.RCPlacement";
            }
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
                mapNames.put(rs3.getString("PersonID"), rs3.getString("FirstName") + " " + rs3.getString("LastName"));
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
}
