/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

 import Montessori.Objective;
import Montessori.PoolC3P0_Local;
import Montessori.PoolC3P0_RenWeb;
import Montessori.Subject;
import java.beans.PropertyVetoException;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
 import Montessori.Objective;
import Montessori.PoolC3P0_Local;
import Montessori.PoolC3P0_RenWeb;
import Montessori.Subject;
import java.beans.PropertyVetoException;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author nmoha
 */
public class FactoryProgressReport_grade4 extends DataFactory {

    public FactoryProgressReport_grade4(String cTerm, String cYear) {
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
        
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        String studentId = idStudent;
        java.util.Vector coll = new java.util.Vector();
      
        ArrayList<String> os4 = new ArrayList<>();
        ArrayList<String> as4 = new ArrayList<>();
        cargarAlumno(studentId);

        String yearId = this.currentYear;
        String termId = this.currentTerm;

        TreeMap<Integer, Profesor> mapTeachers = getTeachers(yearId, termId, idStudent, 1);
        HashMap<String, String> mapComentarios = getComments(yearId, termId, idStudent);
        
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
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

            String nameTerm = "", nameYear = "";
            rs = stAux.executeQuery("select name from SchoolTerm where TermID = " + termId + " and YearID = " + yearId);
            while (rs.next()) {
                nameTerm = "" + rs.getString("name");
            }
            rs = stAux.executeQuery("select SchoolYear from SchoolYear where yearID = " + yearId);
            while (rs.next()) {
                nameYear = "" + rs.getString("SchoolYear");
            }

            this.term = nameTerm + " / " + nameYear;

            term = term.replace("Q", "T");
            os4.add("Head of School#Kim Euston-Brown#" + getSuperComment(yearId, termId, idStudent));

            String nameTutor = "#Tutor";
            String consulta = "select firstName,lastName from(select StaffID from classes inner join roster on roster.classid = classes.classid where homeroom = 1 and roster.studentid = " + idStudent + " and roster.enrolled1 = 1 and classes.yearid =" + yearid + ") a inner join person on a.StaffID= person.personid";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                nameTutor = "#" + rs.getString("firstName") + " " + rs.getString("lastName");
            }

            coll.add(new BeanWithList(null, os4, as4, nameStudent, dob, age, grade, term + nameTutor));

        } catch (SQLException ex) {
 
        } catch (IOException ex) {
            Logger.getLogger(FactoryProgressReport_grade4.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(FactoryProgressReport_grade4.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stAux != null) {
                    stAux.close();
                }
            } catch (Exception e) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return coll;
    }

    private String getSuperComment(String yearId, String termId, String idStudent) {
       Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "select * from report_comments where supercomment=true and studentid = " + idStudent + "and term_id = " + termId + " and yearterm_id=" + yearId;
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                return rs.getString("comment");
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        } catch (IOException ex) {
            Logger.getLogger(FactoryProgressReport_grade4.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(FactoryProgressReport_grade4.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stAux != null) {
                    stAux.close();
                }
            } catch (Exception e) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return "No comments";
    }

    private HashMap<String, String> getComments(String yearId, String termId, String id) throws SQLException {
        HashMap<String, String> mapComment = new HashMap<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "select * from report_comments where supercomment=false and studentid = " + id + " and term_id = " + termId + " and yearterm_id=" + yearId + " order by date_created ASC";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                mapComment.put(rs.getString("subject_id"), rs.getString("comment"));
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        } catch (IOException ex) {
            Logger.getLogger(FactoryProgressReport_grade4.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(FactoryProgressReport_grade4.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stAux != null) {
                    stAux.close();
                }
            } catch (Exception e) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }

        return mapComment;
    }

    @Override
    public String getNameReport() {
        return "progress_report_2017_gr4.jasper";
    }

}
