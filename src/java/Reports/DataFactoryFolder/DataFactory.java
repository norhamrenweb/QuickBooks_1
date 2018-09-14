/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

import Montessori.BambooConfig;
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
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nmohamed
 */
public abstract class DataFactory {

    protected String nameStudent, dob, age, grade, term, termid = "", yearid = "", dateNewTerm = "", daysAbsent = "";
    protected String currentTerm, currentYear;

    public abstract Collection getDataSource(HttpServletRequest hsr, String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException;

    public abstract String getNameReport();

    protected void cargarAlumno(String studentId) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            String consulta = "SELECT * FROM Students where StudentId = '" + studentId + "'";
            rs = stAux.executeQuery(consulta);

            int year = Calendar.getInstance().get(Calendar.YEAR);
            while (rs.next()) {
                this.nameStudent = rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName");
                this.dob = rs.getString("Birthdate");
                this.dob = dob.split(" ")[0];
                this.age = "" + (year - Integer.parseInt("" + dob.charAt(0) + dob.charAt(1) + dob.charAt(2) + dob.charAt(3)));
                this.grade = rs.getString("GradeLevel");
            }

            rs = stAux.executeQuery("select defaultyearid,defaulttermid from ConfigSchool where configschoolid = 1");
            while (rs.next()) {
                termid = "" + rs.getInt("defaulttermid");
                yearid = "" + rs.getInt("defaultyearid");
            }
            String nameTerm = "", nameYear = "";

            rs = stAux.executeQuery("select name from SchoolTerm where TermID = " + this.termid + " and YearID = " + this.yearid);
            while (rs.next()) {
                nameTerm = "" + rs.getString("name");
            }
            rs = stAux.executeQuery("select SchoolYear from SchoolYear where yearID = " + this.yearid);
            while (rs.next()) {
                nameYear = "" + rs.getString("SchoolYear");
            }

            this.term = nameTerm + " / " + nameYear;
            con.close();
        } catch (SQLException ex) {

        } catch (IOException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }  
    }

    protected String limpiarNameAsignatura(String name) { //SOLO POR QUE LAS ASIGNATURAS NO ESTABAN LIMPIAS
        int ini = 0;
        int posEspacio = name.indexOf(" ");
        if (posEspacio == -1) {
            return name;
        }
        if (!name.substring(0, posEspacio).toUpperCase().contains("GROSS") && (name.substring(0, posEspacio).toUpperCase().contains("GR") || name.substring(0, posEspacio).toUpperCase().contains("JP") || name.substring(0, posEspacio).toUpperCase().contains("PP"))) {
            ini = posEspacio + 1;
        }

        return name.substring(ini, name.length());
    }

    protected TreeMap<Integer, Profesor> getTeachers(String yearId, String termId, String id, int reportno) throws SQLException {
        TreeMap<Integer, Profesor> mapTeachers = new TreeMap<>();
        ArrayList<Profesor> listaProfesores = new ArrayList<>();
        TreeMap<String, String> mapNames = new TreeMap<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        
        try {
            ArrayList<Integer> staffids = new ArrayList<>();
            ArrayList<String> classids = new ArrayList<>();
            ArrayList<String> coursesTitles = new ArrayList<>();
            ArrayList<Integer> rcs = new ArrayList<>();
            //the default query that shows all
            String consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID,courses.RCPlacement from Roster inner join Classes"
                    + "  on Roster.ClassID = Classes.ClassID"
                    + "  inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Classes.Term" + termId + "=1 and Roster.StudentID = " + id + " and Classes.yearid = " + yearId + " and Courses.ReportCard = 1 order by courses.RCPlacement";
//             the query that shows courses that has departemnt Reportcode1 or reportcard1,2
            if (reportno == 1) {
                consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID,courses.RCPlacement from Roster inner join Classes"
                        + "  on Roster.ClassID = Classes.ClassID"
                        + "  inner join Courses on  Classes.CourseID = Courses.CourseID"
                        + "  where Classes.Term" + termId + "=1 and Roster.StudentID = " + id + " and Classes.yearid = " + yearId + " and Courses.ReportCard = 1 and (department='Report Codes 1' or department='Report Codes 1, 2' )order by courses.RCPlacement";
            } else if (reportno == 2) {
//                  the query that shows courses that has departemnt Reportcode2 or reportcard1,2
                consulta = "select StaffID, Classes.ClassID , Courses.Title ,Courses.CourseID,courses.RCPlacement from Roster inner join Classes"
                        + "  on Roster.ClassID = Classes.ClassID"
                        + "  inner join Courses on  Classes.CourseID = Courses.CourseID"
                        + "  where Classes.Term" + termId + "=1 and Roster.StudentID = " + id + " and Classes.yearid = " + yearId + " and Courses.ReportCard = 1 and (department='Report Codes 2' or department='Report Codes 1, 2' )order by courses.RCPlacement";
            }
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                staffids.add(rs.getInt("StaffID"));
                classids.add(rs.getString("CourseID"));
                coursesTitles.add(rs.getString("Title"));
                rcs.add(rs.getInt("RCPlacement"));
            }

            consulta = "select FirstName,LastName,Email,PersonID from Person";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                mapNames.put(rs.getString("PersonID"), rs.getString("FirstName") + " " + rs.getString("LastName"));
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
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        } catch (IOException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 

        return mapTeachers;
    }

    protected void getDaysAbsent_And_DateNewTerm(String termId, String yearId, String studentID, String nameTerm) {
        String consulta = "";
         Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        Timestamp aux = null;
        String schoolCode = BambooConfig.schoolCode;//IMPORTANT!! NO ESTAMOS TENIENDO ENCUENTA ESTO
        int numDays = 0, numTotal = 0;
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            
            String startDate = "";
            String endDate = "";
            String termIni = termId;
            if (termId == "2") {
                termIni = "1";
            } else if (termId == "4") {
                termIni = "3";
            }

            consulta = " select firstday from schoolterm where termid =" + termIni + " and yearid =" + yearId;
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                startDate = "" + rs.getTimestamp("firstday");
            }
            consulta = " select lastday from schoolterm where termid =" + termId + " and yearid =" + yearId;
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                endDate = "" + rs.getTimestamp(1);
            }

            consulta = "select count(*) from AttendanceDaySummary "
                    + " where absent =1 and SchoolCode= '" + schoolCode + "' and studentid =" + studentID + " and \"date\" >= '"
                    + startDate + "'  and \"date\" <=  '" + endDate + "'";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                numDays = rs.getInt(1);
            }

            consulta = "select count(*) as daysPresent"
                    + " from daysetup where attendance=1 and SchoolCode='" + schoolCode + "' and DayType = 0 "
                    + " and convert (varchar, DaySetupDate, 23) >= '" + startDate
                    + "' and convert (varchar, DaySetupDate, 23) <= '" + endDate + "'";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                numTotal = rs.getInt(1);
            }
            Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy ", Locale.US);

            if (termId.equals("4")) {
                consulta = "select * from schoolYear where firstDay >= (select LastDay from schoolYear where yearId = " + yearId + ") order by FirstDay";
                rs = stAux.executeQuery(consulta);

                if (rs.next()) {
                    aux = rs.getTimestamp("FirstDay");
                    this.dateNewTerm = formatter.format(aux);
                }
            } else {
                consulta = " select firstday from schoolterm where termid =" + (Integer.parseInt(termIni) + 1) + " and yearid =" + yearId;
                rs = stAux.executeQuery(consulta);

                while (rs.next()) {
                    aux = rs.getTimestamp("firstday");
                }
                this.dateNewTerm = formatter.format(aux);
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        } catch (IOException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(DataFactory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        this.daysAbsent = numDays + " / " + numTotal;

        //   return "No Comments";
    }
}
