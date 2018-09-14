/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

 import Montessori.Objective;
import Montessori.PoolC3P0_Local;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.ntp.TimeStamp;

/**
 *
 * @author nmoha
 */
public class FactoryActivityLog extends DataFactory {

    public FactoryActivityLog() {
        nameStudent = "";
        dob = "";
        age = "";
        grade = "";
        term = "";
    }

    @Override
    public Collection getDataSource(HttpServletRequest hsr,  String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException {
        /*
        String studentId = idStudent; 
        java.util.Vector coll = new java.util.Vector();
        ArrayList<String> os4 = new ArrayList<>();
        ArrayList<String> as4 = new ArrayList<>();
        cargarAlumno(studentId);
        
        HashMap<String, Profesor> mapTeachers = getTeachers(idStudent);
        HashMap<String, String> mapComentarios = getComments(idStudent);
        
        for (Map.Entry<String, Profesor> entry # mapTeachers.entrySet()) {
            String key = entry.getKey();
            Profesor value = entry.getValue();
            os4.add(value.getAsignatura()+"#"+value.getFirstName());
            
            if(mapComentarios.containsKey(key)) as4.add(mapComentarios.get(key));
            else as4.add(" ");
        }
      
        if (coll.isEmpty()) {
            coll.add(new BeanWithList(null, os4, as4, nameStudent, dob, age, grade, term));
        }
        return coll;*/
        return null;
    }

    public static Collection getDataSource(HttpServletRequest hsr, String nameUser,String start, String finish, String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException, ParseException {

        java.util.Vector coll = new java.util.Vector();
        ArrayList<String> os4 = new ArrayList<>();
        ArrayList<String> as4 = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String idTeacher = idStudent.substring(0,idStudent.indexOf("$"));
        String nameTeacher = idStudent.substring(idStudent.indexOf("$")+1,idStudent.length());
        Date ini = format.parse(start);
        Date fin = format.parse(finish);
        
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "select * from activitylog where userid = "+idTeacher+" and date between '" + ini.toString() + "' and '" + fin.toString() + "' order by date DESC";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                String aux = "";
                aux += rs.getString("username") + "#";
                String h = rs.getTimestamp("date").toString();
                aux +=  h.substring(0, h.indexOf("."))  + "#";
                aux += rs.getString("type") + "#";
                aux += rs.getString("studentid") + "#";
                aux += rs.getString("note");
                os4.add(aux);
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos# " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
        } catch (IOException ex) {
            Logger.getLogger(FactoryActivityLog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(FactoryActivityLog.class.getName()).log(Level.SEVERE, null, ex);
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
        if(os4.isEmpty()) os4.add(nameTeacher+"#No Data#No Data#No Data#No Data");
        coll.add(new BeanWithList(null, os4, as4,nameUser, "", ini.toString(), fin.toString(), ""));

        return coll;
    }

    @Override
    public String getNameReport() {
        return "activityLog_BBDD.jasper";
    }
}
