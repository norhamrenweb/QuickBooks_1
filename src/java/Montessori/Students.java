/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author nmohamed
 */
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Students {

    private int id_students;
    private String nombre_students;// will get from firstname,lastname
    private String fecha_nacimiento;
    private String foto;
    private String level_id;// grade level
    private ServletContext servlet;
    private Connection cn;
    private CachedRowSet rs;
    private String nextlevel;//new
    private String substatus;//new

    public String getNextlevel() {
        return nextlevel;
    }

    public void setNextlevel(String nextlevel) {
        this.nextlevel = nextlevel;
    }

    public String getSubstatus() {
        return substatus;
    }

    public void setSubstatus(String substatus) {
        this.substatus = substatus;
    }

    public Students() {

    }

    public Students(int id_students, String nombre_students, String fecha_nacimiento, String foto, String level_id) {
        this.id_students = id_students;
        this.nombre_students = nombre_students;
        this.fecha_nacimiento = fecha_nacimiento;
        this.foto = foto;
        this.level_id = level_id;
    }

    public int getId_students() {
        return id_students;
    }

    public void setId_students(int id_students) {
        this.id_students = id_students;
    }

    public String getNombre_students() {
        return nombre_students;
    }

    public void setNombre_students(String nombre_students) {
        this.nombre_students = nombre_students;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public static ArrayList<Students> getStudents(Logger log) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
//        this.conectarOracle();
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            String consulta = "SELECT * FROM Students where Status = 'Enrolled' order by LastName";
            rs = stAux.executeQuery(consulta);

            while (rs.next()) {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("FirstName") + " " + rs.getString("MiddleName") + " " + rs.getString("LastName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setNextlevel("Placement");
                alumnos.setSubstatus("Substatus");
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            Collections.sort(listaAlumnos, new Comparator<Students>() {
                @Override
                public int compare(Students o1, Students o2) {
                    return o1.getNombre_students().compareTo(o2.getNombre_students());
                }
            });
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {  
            java.util.logging.Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaAlumnos;
    }

    public static ArrayList<Students> getStudentslevel(String gradeid, Logger log) throws SQLException {
        ArrayList<Students> listaAlumnos = new ArrayList<>();
         Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        String gradelevel = null;
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            
            rs = stAux.executeQuery("select GradeLevel from GradeLevels where GradeLevelID =" + gradeid);
            while (rs.next()) {
                gradelevel = rs.getString("GradeLevel");
            }
            String consulta = "SELECT * FROM Students where Status = 'Enrolled' and GradeLevel = '" + gradelevel + "'";
            rs = stAux.executeQuery(consulta);

            while (rs.next()) {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("FirstName") + " " + rs.getString("MiddleName") + " " + rs.getString("LastName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setNextlevel(rs.getString("Placement"));
                alumnos.setSubstatus(rs.getString("Substatus"));
                listaAlumnos.add(alumnos);
            }

            //this.finalize();
            Collections.sort(listaAlumnos, new Comparator<Students>() {
                @Override
                public int compare(Students o1, Students o2) {
                    return o1.getNombre_students().compareTo(o2.getNombre_students());
                }
            });
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return listaAlumnos;

    }

//    public ArrayList<Students> getStudentsForLevel(int idLevel) throws SQLException{
//        this.conectarOracle();
//        ArrayList<Students> listaAlumnos = new ArrayList<>();
//        if(idLevel != 0){
//            try {
//                String consulta = "SELECT * FROM AH_ZAF.dbo.Students where GradeLevel = "+ idLevel;
//                this.rs.setCommand(consulta);
//                this.rs.execute(this.cn);
//                this.rs.beforeFirst();
//                while (rs.next())
//                {
//                    Students alumnos = new Students();
//                    alumnos.setId_students(rs.getInt("StudentID"));
//                alumnos.setNombre_students(rs.getString("FirstName")+","+rs.getString("LastName"));
//                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
//                alumnos.setFoto(rs.getString("PathToPicture"));
//                alumnos.setLevel_id(rs.getInt("GradeLevel"));
//                alumnos.setPlacement("Placement");
//                alumnos.setSubstatus("Substatus");
//                    listaAlumnos.add(alumnos);
//                }
//
//            } catch (SQLException ex) {
//                System.out.println("Error leyendo Alumnos: " + ex);
//            }
//        }else{
//            listaAlumnos = this.getStudents();
//        }
//        //this.finalize();
//        this.rs.close();
//        this.cn.close();
//        return listaAlumnos;
//    }    
//    
//    public ArrayList<Students> getParseStudents(String students) throws SQLException
//    {
//        this.conectarOracle();
//        ArrayList<Integer> idAlumnos = new ArrayList<>();
//        ArrayList<Students> listaAlumnos = new ArrayList<>();
//        idAlumnos=this.StudentsParse(students);
//        try {
//            for(int id: idAlumnos){
//                String consulta = "SELECT * FROM AH_ZAF.dbo.Students where StudentID=" + id;
//                this.rs.setCommand(consulta);
//                this.rs.execute(this.cn);
//                this.rs.beforeFirst();
//                while (rs.next())
//                {
//                    Students alumnos = new Students();
//                     alumnos.setId_students(rs.getInt("StudentID"));
//                alumnos.setNombre_students(rs.getString("FirstName")+","+rs.getString("LastName"));
//                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
//                alumnos.setFoto(rs.getString("PathToPicture"));
//                alumnos.setLevel_id(rs.getInt("GradeLevel"));
//                alumnos.setPlacement("Placement");
//                alumnos.setSubstatus("Substatus");
//                    listaAlumnos.add(alumnos);
//                }
//            //this.finalize();
//            }   
//        } catch (SQLException ex) {
//            System.out.println("Error leyendo Alumnos: " + ex);
//        }
//        this.rs.close();
//        this.cn.close();
//        return listaAlumnos;
//    }
//    //need to modify
//    public ArrayList<Students> getStudentsFromLesson(int idLesson) throws SQLException{
//        this.conectarOracle();
//        ArrayList<Students> listaAlumnos = new ArrayList<>();
//        ArrayList<Integer> idStudents = new ArrayList<>();
//        try {
//            String consulta = "SELECT * FROM lessons_students where id_lessons=" + idLesson;
//            this.rs.setCommand(consulta);
//            this.rs.execute(this.cn);
//            this.rs.beforeFirst();
//            while (rs.next())
//            {
//                idStudents.add((rs.getInt("id_students")));
//            }
//            
//            for(int i:idStudents){
//                consulta = "SELECT * FROM students where id_students=" + i;
//                this.rs.setCommand(consulta);
//                this.rs.execute(this.cn);
//                this.rs.beforeFirst();
//                while (rs.next())
//                {
//                    Students alumnos = new Students();
//                    alumnos.setId_students(rs.getInt("id_students"));
//                    alumnos.setNombre_students(rs.getString("nombre_students"));
//                    alumnos.setFecha_nacimiento(rs.getString("fecha_nacimiento"));
//                    alumnos.setFoto(rs.getString("foto"));
//                    alumnos.setLevel_id(rs.getInt("level_id"));
//                    listaAlumnos.add(alumnos);
//                }
//            }
//        } catch (SQLException ex) {
//            System.out.println("Error leyendo Alumnos: " + ex);
//        }
//        this.rs.close();
//        this.cn.close();
//        return listaAlumnos;
//    }
//    //hwta does that 
//    public ArrayList<Integer> StudentsParse(String students){
//        //String students = " Esto es=un archivo data-id='12567867' que aburrido=estoy  data-id='4578578' jeje";
//        String buscar = "data-id";
//        int pos = students.indexOf(buscar);
//        ArrayList<Integer> idStudents = new ArrayList<Integer>();
//        while (pos > -1){
//            students = students.substring(pos + buscar.length()+2 ,students.length());
//            idStudents.add(Integer.parseInt(students.substring(0 ,students.indexOf(" ")-1)));
//            
//            
//            pos = students.indexOf(buscar);
//        }
//        return idStudents;
//    }
//    
//    //Funcion que sirve para quitar del primer cajon los estudiantes que están en el segundo
//    public boolean removeStudents(ArrayList<Students> listStudents){
//        for(int i = 0; i<listStudents.size();i++){
//            if (this.id_students == listStudents.get(i).id_students){
//                return true;
//            }
//        }
//        return false;
//    }
// 
//    public void insertStudents(int idLesson, ArrayList<Students> listStudents) throws SQLException{
//        this.conectarOracle();
//        int id_student_lesson = 0;
//        try { 
//            String getLesson = "SELECT * FROM lessons_students WHERE id_lessons_students =(SELECT max(id_lessons_students) FROM lessons_students)";
//            this.rs.setCommand(getLesson);
//            this.rs.execute(this.cn);
//            this.rs.beforeFirst();
//            while (rs.next()){
//                id_student_lesson = rs.getInt(1) + 1;
//            }
//            for(int i=0; i < listStudents.size(); i++){
//                String consulta = "INSERT INTO lessons_students (id_lessons_students, id_lessons, id_students) values (?,?,?)"; 
//                PreparedStatement pst = this.cn.prepareStatement(consulta);
//                pst.setInt(1, id_student_lesson + i);
//                pst.setInt(2, idLesson);
//                pst.setInt(3, listStudents.get(i).getId_students());
//                pst.executeUpdate();
//            }
//        } catch (SQLException ex) {
//            System.out.println(ex.toString());
//        }
//        this.rs.close();
//        this.cn.close();
//    }
//    
//    public void deleteStudent(int idLesson) throws SQLException{
//        this.conectarOracle();
//        String consulta = "delete from lessons_students where id_lessons=" + idLesson;
//        this.rs.setCommand(consulta);
//        this.rs.execute(this.cn);
//        this.rs.beforeFirst();
//        this.rs.close();
//        this.cn.close();
//    }
//    
//    public void updateStudent(int idLesson, ArrayList<Students> listStudents) throws SQLException{
//        this.deleteStudent(idLesson);
//        this.insertStudents(idLesson, listStudents);
//    }
}
