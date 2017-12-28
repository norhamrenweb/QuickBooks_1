/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

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

/**
 *
 * @author nmohamed
 */
public class DataFactory {

    public DataFactory() {
    }
    ;
   // Connection cn;
      
//    private static ServletContext servlet;
  private static BeanWithList[] result;

//    private Object getBean(String nombrebean, ServletContext servlet)
//    {
//        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
//        Object beanobject = contexto.getBean(nombrebean);
//        return beanobject;
//    }
    public static Collection getDataSource(ServletContext servlet) throws SQLException, ClassNotFoundException {
        //AGREGAR CODIGO PARA ACCESO A LA BBDD RENWEB Y BUSCAR DATOS DEL ALUMNO

        //new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource($F{studentId});
        Collection<String> c;
        //JRDataSource datasource = new JRBeanCollectionDataSource(c, true);

        String studentId = "10101";
        Class.forName("org.postgresql.Driver");
        Connection cn = DriverManager.getConnection("jdbc:postgresql://192.168.1.3:5432/Lessons?user=eduweb&password=Madrid2016");
        Statement st = cn.createStatement();
        ArrayList<String> lessons = new ArrayList<>();
        java.util.Vector coll = new java.util.Vector();
        // ArrayList<BeanWithList> coll = new ArrayList<BeanWithList>();
        //   ArrayList<Object> coll = new ArrayList<Object>();
        //ArrayList<BeanWithList> coll = new ArrayList<BeanWithList>();
        // get the lessons attended by the student
        ArrayList<Subject> subjects = new ArrayList<>();
        ResultSet rs = st.executeQuery("SELECT lesson_id from lesson_stud_att where student_id = '" + studentId + "' and attendance != 'null' and attendance !=' '");
        while (rs.next()) {
            lessons.add("" + rs.getInt("lesson_id"));
        }
        //select the subjects of these lessons
        ArrayList<String> subids = new ArrayList<>();
        for (String b : lessons) {
            ResultSet rs2 = st.executeQuery("select subject_id from lessons where id =" + b);

            while (rs2.next()) {
                // this for avoidind duplicate subjects
                if (!subids.contains("" + rs2.getInt("subject_id"))) {
                    subids.add("" + rs2.getInt("subject_id"));
                    Subject su = new Subject();
                    String[] id = new String[1];
                    id[0] = "" + rs2.getInt("subject_id");
                    su.setId(id);
                    subjects.add(su);
                }
            }
        }

        for (Subject x : subjects) {
            Class.forName("org.postgresql.Driver");
            cn = DriverManager.getConnection("jdbc:postgresql://192.168.1.3:5432/Lessons?user=eduweb&password=Madrid2016");
            st = cn.createStatement();
            String[] id = x.getId();
            ArrayList<String> os = new ArrayList<>();
            for (String b : lessons) {
                ResultSet rs1 = st.executeQuery("select objective_id from lessons where id = " + b + " and subject_id =" + id[0]);
                while (rs1.next()) {
                    if (!os.contains("" + rs1.getInt("objective_id"))) {
                        os.add("" + rs1.getInt("objective_id"));
                    }
                }
            }
            int counter = 0;
            ArrayList<String> finalratings = new ArrayList<>();
            ArrayList<Integer> indEliminarObjectives =new ArrayList<Integer>();
            for (String d : os){
                Objective b = new Objective();
                String name = b.fetchName(Integer.parseInt(d), servlet);
                String consulta = "SELECT rating.name FROM rating where id in(select rating_id from progress_report where student_id = '" + studentId + "' AND comment_date = (select max(comment_date)   from public.progress_report where student_id = '" + studentId + "' AND objective_id =" + d + " and generalcomment = false) AND objective_id =" + d + " and generalcomment = false )";
                ResultSet rs2 = st.executeQuery(consulta);
                if(!rs2.next()){
                   indEliminarObjectives.add(counter);
                }
                else{
                    rs2 = st.executeQuery(consulta);
                    while (rs2.next()) {
                        String var = rs2.getString("name");
                        if(rs2.getString("name").equals("N/A") || rs2.getString("name").equals(" ")  || rs2.getString("name").equals("")){
                             indEliminarObjectives.add(counter);
                        }
                        else{
                            var = rs2.getString("name");
                            //  finalratings.add("Mastered");//rs2.getString("name"));
                            finalratings.add(rs2.getString("name"));
                       
                        }
                    }
                }
                os.set(counter, name);
                counter = counter + 1;
            }
            x.setObjectives(os);
            Subject t = new Subject();
            cn.close();
            
            for (int i = indEliminarObjectives.size()-1; i >=0; i--) {
                int k = indEliminarObjectives.get(i);
                os.remove(k);
            }
            //FUNCIONA PERO LO HACE INNECESARIAMENTE 3 VECES NO SE PORQUE CUANDO LO HAGO FUERA FALLA.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            cn = DriverManager.getConnection("jdbc:sqlserver://ah-zaf.odbc.renweb.com\\ah_zaf:1433;databaseName=ah_zaf", "AH_ZAF_CUST", "BravoJuggle+396");
            st = cn.createStatement();

            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where StudentId = '" + studentId + "'";
            rs = st.executeQuery(consulta);
            String nameStudent = "", dob = "", age = "";
            int year = Calendar.getInstance().get(Calendar.YEAR);
            while (rs.next()) {
                nameStudent = rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName");
                dob = rs.getString("Birthdate");
                age = "" + (year - Integer.parseInt("" + dob.charAt(0) + dob.charAt(1) + dob.charAt(2) + dob.charAt(3)));
            }
            //============================================================

            BeanWithList bean = new BeanWithList(x.fetchName(Integer.parseInt(id[0]), servlet), os, finalratings, nameStudent, dob, age);
            coll.add(bean);
            cn.close();
        }
        //     FALTA TERMINAR PODRIAMOS TOMAR LOS SUBJECT_ID DE LOS ALUMNOS QUE SEAN ELECTIVOS Y COMPROBAR QUE NO EXISTAN EN LA UQERY SIGUIENTE
        Class.forName("org.postgresql.Driver");
        cn = DriverManager.getConnection("jdbc:postgresql://192.168.1.3:5432/Lessons?user=eduweb&password=Madrid2016");
        st = cn.createStatement();

        ArrayList<String> os = new ArrayList<>();
        ArrayList<String> as = new ArrayList<>();

        String consulta = "SELECT lessons.subject_id,progress_report.comment FROM progress_report join lessons on (progress_report.lesson_id = lessons.id) where student_id= '" + studentId + "'";
        rs = st.executeQuery(consulta);
        while (rs.next()) {
            String comment = rs.getString("comment");
            if (!comment.equals("")) {
                os.add("" + rs.getInt("subject_id"));
                as.add(comment);
            }
        }
        //============================================================

        BeanWithList bean = new BeanWithList("Comments", os, as, "david", "1991/25/02", "26");
        coll.add(bean);

        //JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(coll);
        //return coll.toArray(new BeanWithList[coll.size()]);
        /*
    
    ArrayList<String> os = new ArrayList<>();
    os.add("art");
    os.add("music");
    
    ArrayList<String> as = new ArrayList<>();
    as.add("art comment");
    as.add("music comment");
    BeanWithList bean = new BeanWithList("Comments",os,as,"david","1991/25/02","26");
    coll.add(bean);
         */
        //==========================================
        ArrayList<String> os2 = new ArrayList<>();
        os2.add("Informacion relacionada con Social Development");

        BeanWithList bean2 = new BeanWithList("SocialDevelopment", os2, new ArrayList<String>(), "ultimosComentarios", "1991/25/02", "26");

        coll.add(bean2);
        //============================

        ArrayList<String> os3 = new ArrayList<>();
        os3.add("Informacion relacionada con General");

        BeanWithList bean3 = new BeanWithList("General", os3, new ArrayList<String>(), "ultimosComentario", "1991/25/02", "26");

        coll.add(bean3);
        //============================
        cn.close();
        return coll;
        // return new JRBeanCollectionDataSource(coll);
    }
//     public static Collection getDataSource(String studentid) throws SQLException, ClassNotFoundException {
//     
//            Class.forName("org.postgresql.Driver");
//           Connection cn = DriverManager.getConnection("jdbc:postgresql://192.168.1.3:5432/Lessons?user=eduweb&password=Madrid2016");
//        Statement st = cn.createStatement();
//        ArrayList<String> lessons = new ArrayList<>();
//        //  java.util.Vector coll = new java.util.Vector();
//        ArrayList<BeanWithList> coll = new ArrayList<BeanWithList>();
//         //   ArrayList<Object> coll = new ArrayList<Object>();
//          //ArrayList<BeanWithList> coll = new ArrayList<BeanWithList>();
//        // get the lessons attended by the student
//        ArrayList<Subject> subjects = new ArrayList<>();
//        ResultSet rs = st.executeQuery("SELECT lesson_id from lesson_stud_att where student_id = '10101' and attendance != 'null'");
//       while(rs.next())
//       {
//           lessons.add(""+rs.getInt("lesson_id"));
//       }
//        //select the subjects of these lessons
//         ArrayList<String> subids = new ArrayList<>();
//        for(String b:lessons){
//            ResultSet rs2 = st.executeQuery( "select subject_id from lessons where id ="+b);
//       
//        while(rs2.next())
//        {
//            // this for avoidind duplicate subjects
//            if(!subids.contains(""+rs2.getInt("subject_id")))
//            {
//            subids.add(""+rs2.getInt("subject_id"));
//            Subject su = new Subject();
//            String[] id = new String[1];
//            id[0]= ""+rs2.getInt("subject_id");
//            su.setId(id);
//          subjects.add(su);
//          }
//        }
//        }
//        
//        for (Subject x: subjects)
//        {
//             Class.forName("org.postgresql.Driver");
//          cn = DriverManager.getConnection("jdbc:postgresql://192.168.1.3:5432/Lessons?user=eduweb&password=Madrid2016");
//            String[] id = x.getId();
//            ArrayList<String> os = new ArrayList<>();
//            for(String b:lessons){
//        ResultSet rs1 = st.executeQuery("select objective_id from lessons where id = "+b+" and subject_id ="+ id[0]);
//        while(rs1.next())
//        {
//            if(!os.contains(""+rs1.getInt("objective_id")))
//            {
//            os.add(""+rs1.getInt("objective_id"));
//            }
//        }
//            }
//        int counter = 0;
//        ArrayList<String> finalratings = new ArrayList<>();
//        for(String d:os){
//            Objective b = new Objective();
//            
//        String name = b.fetchName(Integer.parseInt(d), cn);
//          String consulta = "SELECT rating.name FROM rating where id in(select rating_id from progress_report where student_id = 10102 AND comment_date = (select max(comment_date)   from public.progress_report where student_id = 10102 AND objective_id ="+d+" and generalcomment = false) AND objective_id ="+d+" and generalcomment = false )";
//                ResultSet rs2 = st.executeQuery(consulta);
//                while(rs2.next())
//                {
//                    finalratings.add(rs2.getString("name"));
//                }
//        os.set(counter,name);
//        counter = counter+1;
//        }
//        x.setObjectives(os);
//        Subject t = new Subject();
//        cn.close();
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        cn = DriverManager.getConnection("jdbc:sqlserver://ah-zaf.odbc.renweb.com\\ah_zaf:1433;databaseName=ah_zaf","AH_ZAF_CUST","BravoJuggle+396");
//        BeanWithList bean = new BeanWithList(x.fetchName(Integer.parseInt(id[0]),cn),os,finalratings);
//
//    coll.add(bean);
//    cn.close();
//        }
////JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(coll);
//    return Arrays.asList(coll);
////    return Arrays.asList(coll);
//}
}
