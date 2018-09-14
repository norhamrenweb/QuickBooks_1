/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import quickbooksync.QBInvoice;

/**
 *
 * @author nmohamed
 */
public class Updatelesson {

   
    private ServletContext servlet;

    public Updatelesson(ServletContext s) {
        this.servlet = s;
    }

    public Updatelesson() {

    }

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    private void cleanProgress(String idLesson) {
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "DELETE FROM progress_report a WHERE lesson_id=" + idLesson + " and NOT EXISTS (SELECT * FROM lesson_stud_att b WHERE a.student_id = b.student_id and a.lesson_id = b.lesson_id)";
            stAux.executeUpdate(consulta);
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(Updatelesson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Updatelesson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updatelesson(HttpServletRequest hsr, String note, String nameStudents, String[] studentIds, Lessons newlessons) throws SQLException {
        String lessonid = "" + newlessons.getId();
        List<String> equipmentids;
        List<String> oldstuds = new ArrayList<>();
        List<String> addstuds = new ArrayList<>();
        List<String> delstuds = new ArrayList<>();
        DriverManagerDataSource dataSource;
        String comment = newlessons.getComments();
        comment = comment.replaceAll("\"", "\"\"");
        comment = comment.replaceAll("'", "''");
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String test = null;
            if (newlessons.getMethod().getName() != "") {
                test = "update lessons set name = '" + newlessons.getName() + "',level_id = '" + newlessons.getLevel().getName() + "' ,subject_id = '" + newlessons.getSubject().getName() + "',objective_id= '" + newlessons.getObjective().getName() + "',start ='" + newlessons.getStart() + "',finish='" + newlessons.getFinish() + "',comments='" + comment + "',method_id='" + newlessons.getMethod().getName() + "' where id ='" + newlessons.getId() + "'";
            } else {
                test = "update lessons set name = '" + newlessons.getName() + "',level_id = '" + newlessons.getLevel().getName() + "' ,subject_id = '" + newlessons.getSubject().getName() + "',objective_id= '" + newlessons.getObjective().getName() + "',start ='" + newlessons.getStart() + "',finish='" + newlessons.getFinish() + "',comments='" + comment + "' where id ='" + newlessons.getId() + "'";;
            }

            stAux.executeUpdate(test);
            String consulta = "select student_id from lesson_stud_att where lesson_id ='" + newlessons.getId() + "'";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                oldstuds.add("" + rs.getInt("student_id"));
            }
            List<String> newList = new LinkedList<String>(Arrays.asList(studentIds));
            //check if the students were unchanged inorder to skip all the code below
            //test if the old list and the new list were exactly the same length but different values
            if (!oldstuds.equals(newList)) {
                //get the new students to be added

                addstuds = newList;
                for (String v : oldstuds) {
                    boolean prueba = addstuds.removeAll(Collections.singleton(v));
                }
                for (String x : addstuds) {
                    stAux.executeUpdate("insert into lesson_stud_att(lesson_id,student_id) values ('" + newlessons.getId() + "','" + x + "')");
                }
                newList = new LinkedList<String>(Arrays.asList(studentIds));
                // get the studs tp be deleted
                delstuds = newList;

                oldstuds.removeAll(delstuds);
                for (String y : oldstuds) {
                    stAux.executeUpdate("delete from lesson_stud_att where lesson_id = '" + newlessons.getId() + "'and student_id = '" + y + "'");
                }
            }
            //delete the old content list and add the new one
            //to avoid null pointer exception incase of lesson without content
            if (newlessons.getContentid() != null) {
                equipmentids = newlessons.getContentid();
                stAux.executeUpdate("delete from lesson_content where lesson_id = '" + newlessons.getId() + "'");
                for (int i = 0; i <= equipmentids.size() - 1; i++) {

                    stAux.executeUpdate("insert into lesson_content(lesson_id,content_id) values ('" + lessonid + "','" + equipmentids.get(i) + "')");
                }
            }
            cleanProgress("" + newlessons.getId());
            note = "id: " + lessonid + " |" + note + " | comment: " + comment;
            ActivityLog.log(((User) (hsr.getSession().getAttribute("user"))), "[" + nameStudents + "]", "Update Presentation", note); //crear lesson
            con.close();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(Updatelesson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Updatelesson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateidea(Lessons newlessons) throws SQLException {

        List<String> equipmentids;
        //DriverManagerDataSource dataSource;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            Connection con = pool_local.getConnection();
        //    dataSource = (DriverManagerDataSource) this.getBean("dataSource", this.servlet);
           // this.cn = dataSource.getConnection();
            Statement st = con.createStatement();
            String test = null;
            if (newlessons.getMethod().getName() != null && !"".equals(newlessons.getMethod().getName())) {
                test = "update lessons set name = '" + newlessons.getName() + "',level_id = '" + newlessons.getLevel().getName() + "' ,subject_id = '" + newlessons.getSubject().getName() + "',objective_id= '" + newlessons.getObjective().getName() + "',comments='" + newlessons.getComments() + "',method_id='" + newlessons.getMethod().getName() + "' where id ='" + newlessons.getId() + "'";
            } else {
                test = "update lessons set name = '" + newlessons.getName() + "',level_id = '" + newlessons.getLevel().getName() + "' ,subject_id = '" + newlessons.getSubject().getName() + "',objective_id= '" + newlessons.getObjective().getName() + "',comments='" + newlessons.getComments() + "',method_id=null where id ='" + newlessons.getId() + "'";;
            }
            st.executeUpdate(test);

            //delete the old content list and add the new one
            //to avoid null pointer exception incase of lesson without content
            st.executeUpdate("delete from lesson_content where lesson_id = '" + newlessons.getId() + "'");
            if (newlessons.getContentid() != null) {
                equipmentids = newlessons.getContentid();
                for (int i = 0; i <= equipmentids.size() - 1; i++) {
                    st.executeUpdate("insert into lesson_content(lesson_id,content_id) values ('" + newlessons.getId() + "','" + equipmentids.get(i) + "')");
                }
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(Updatelesson.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Updatelesson.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
