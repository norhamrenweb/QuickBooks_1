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
import javax.servlet.ServletContext;
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
    public Collection getDataSource(String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException {
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

    public static Collection getDataSource(String nameUser,String start, String finish, String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException, ParseException {

        java.util.Vector coll = new java.util.Vector();
        ArrayList<String> os4 = new ArrayList<>();
        ArrayList<String> as4 = new ArrayList<>();

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String idTeacher = idStudent;
        Date ini = format.parse(start);
        Date fin = format.parse(finish);
        
        try {
            String consulta = "select * from activitylog where userid = "+idTeacher+" and date between '" + ini.toString() + "' and '" + fin.toString() + "' order by date DESC";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
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
        }

        coll.add(new BeanWithList(null, os4, as4,nameUser, "", ini.toString(), fin.toString(), ""));

        return coll;
    }

    @Override
    public String getNameReport() {
        return "activityLog_BBDD.jasper";
    }
}
