/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import quickbooksync.QBInvoice;

/**
 *
 * @author nmohamed
 */
public class Updatelesson {

    Connection cn;
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
        try {
            String consulta = "DELETE FROM progress_report a WHERE lesson_id=" + idLesson + " and NOT EXISTS (SELECT * FROM lesson_stud_att b WHERE a.student_id = b.student_id and a.lesson_id = b.lesson_id)";
            DBConect.eduweb.executeUpdate(consulta);
        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }

    }

    public void updatelesson(String[] studentIds, Lessons newlessons) throws SQLException {
        String lessonid = null;
        List<String> equipmentids;
        List<String> oldstuds = new ArrayList<>();
        List<String> addstuds = new ArrayList<>();
        List<String> delstuds = new ArrayList<>();
        DriverManagerDataSource dataSource;
        try {
            
            String test = null;
            if (newlessons.getMethod().getName() != "") {
                test = "update lessons set name = '" + newlessons.getName() + "',level_id = '" + newlessons.getLevel().getName() + "' ,subject_id = '" + newlessons.getSubject().getName() + "',objective_id= '" + newlessons.getObjective().getName() + "',start ='" + newlessons.getStart() + "',finish='" + newlessons.getFinish() + "',comments='" + newlessons.getComments() + "',method_id='" + newlessons.getMethod().getName() + "' where id ='" + newlessons.getId() + "'";
            } else {
                test = "update lessons set name = '" + newlessons.getName() + "',level_id = '" + newlessons.getLevel().getName() + "' ,subject_id = '" + newlessons.getSubject().getName() + "',objective_id= '" + newlessons.getObjective().getName() + "',start ='" + newlessons.getStart() + "',finish='" + newlessons.getFinish() + "',comments='" + newlessons.getComments() + "' where id ='" + newlessons.getId() + "'";;
            }
            DBConect.eduweb.executeUpdate(test);
            String consulta = "select student_id from lesson_stud_att where lesson_id ='" + newlessons.getId() + "'";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
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
                    DBConect.eduweb.executeUpdate("insert into lesson_stud_att(lesson_id,student_id) values ('" + newlessons.getId() + "','" + x + "')");
                }
                newList = new LinkedList<String>(Arrays.asList(studentIds));
                // get the studs tp be deleted
                delstuds = newList;

                oldstuds.removeAll(delstuds);
                for (String y : oldstuds) {
                    DBConect.eduweb.executeUpdate("delete from lesson_stud_att where lesson_id = '" + newlessons.getId() + "'and student_id = '" + y + "'");
                }
            }
            //delete the old content list and add the new one
            //to avoid null pointer exception incase of lesson without content
            if (newlessons.getContentid() != null) {
                equipmentids = newlessons.getContentid();
                DBConect.eduweb.executeUpdate("delete from lesson_content where lesson_id = '" + newlessons.getId() + "'");
                for (int i = 0; i <= equipmentids.size() - 1; i++) {

                    DBConect.eduweb.executeUpdate("insert into lesson_content(lesson_id,content_id) values ('" + lessonid + "','" + equipmentids.get(i) + "')");
                }
            }
            cleanProgress("" + newlessons.getId());
        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }
        //          st.executeUpdate("insert into lessons_time(teacher_id,lesson_id,lesson_start,lesson_end) values (5,"+lessonid+",'"+newlessons.getStart()+"','"+newlessons.getFinish()+"')");
    }

    public void updateidea(Lessons newlessons) throws SQLException {

        List<String> equipmentids;
        DriverManagerDataSource dataSource;
        try {
            dataSource = (DriverManagerDataSource) this.getBean("dataSource", this.servlet);
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
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

        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }

    }

}
