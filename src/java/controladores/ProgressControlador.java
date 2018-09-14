/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nmohamed
 */
@Controller
public class ProgressControlador {

    static Logger log = Logger.getLogger(ProgressControlador.class.getName());
//      private ServletContext servlet;

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    @RequestMapping("/lessonprogress/loadRecords.htm")
    public ModelAndView loadRecords(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("lessonprogress");
        List<Students> instructors = new ArrayList<>();
        String disable = null;
        Students teacher = new Students();

        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
        PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();

        try {
            String lessonid = hsr.getParameter("LessonsSelected");
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String consulta = "SELECT * FROM public.lesson_detailes where id =" + lessonid;
            rs = stAux.executeQuery(consulta);
            Lessons lesson = new Lessons();
            while (rs.next()) {
                Objective obj = new Objective();
                obj.setName(rs.getString("objectivename"));
                String[] ids = new String[1];
                ids[0] = String.valueOf(rs.getInt("objective_id"));
                obj.setId(ids);
                lesson.setObjective(obj);
                Subject sub = new Subject();
                String name = sub.fetchName(rs.getInt("subject_id"), hsr.getServletContext());
                sub.setName(name);
                String[] subids = new String[1];
                subids[0] = String.valueOf(rs.getInt("subject_id"));
                sub.setId(ids);
                lesson.setSubject(sub);
                lesson.setName(rs.getString("name"));
                lesson.setId(Integer.parseInt(lessonid));
            }
            consulta = "select presentedby from lessons where id = " + lessonid;
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {

                teacher.setId_students(rs.getInt("presentedby"));
            }
            List<Step> steps = new ArrayList<>();
            String[] objid = lesson.getObjective().getId();
            rs = stAux.executeQuery("select name,id,storder from obj_steps where obj_id =" + objid[0]);
            while (rs.next()) {
                Step s = new Step();
                s.setName(rs.getString("name"));
                s.setId("" + rs.getInt("id"));
                s.setOrder(rs.getInt("storder"));
                steps.add(s);
            }
            rs = stAux.executeQuery("select archive from lessons where id =" + lessonid);
            while (rs.next()) {
                if (rs.getBoolean("archive")) {
                    disable = "t";
                }
            }
            List<Progress> records = this.getRecords(lesson, hsr.getServletContext());
            mv.addObject("attendancelist", records);
            mv.addObject("lessondetailes", lesson);
            mv.addObject("disable", disable);
            mv.addObject("steps", steps);
            consulta = "select LastName,FirstName,MiddleName,PersonID from Person where PersonID in (select PersonID from Staff where Faculty = 1) order by FirstName";

            con.close();

            con = pool_renweb.getConnection();
            stAux = con.createStatement();

            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                Students s = new Students();
                s.setId_students(rs.getInt("PersonID"));
                s.setNombre_students(rs.getString("FirstName") + " " + rs.getString("MiddleName") + " " + rs.getString("LastName"));
                if (teacher.getId_students() == s.getId_students()) {
                    teacher.setNombre_students(s.getNombre_students());
                }
                instructors.add(s);
            }
            mv.addObject("instructors", instructors);
            mv.addObject("selectedinst", teacher);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
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
        return mv;

    }

    public List<Progress> getRecords(Lessons lesson, ServletContext servlet) throws SQLException {

        List<Progress> records = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "SELECT * FROM public.lesson_stud_att where lesson_id =" + lesson.getId();
            rs = stAux.executeQuery(consulta);

            while (rs.next()) {
                Progress att = new Progress();

                att.setStudentid(rs.getInt("student_id"));
                if (rs.getString("attendance") == null || rs.getString("attendance").equals("") || rs.getString("attendance").equals("0")) {
                    att.setAttendancecode("0");
                } else {
                    att.setAttendancecode(rs.getString("attendance"));
                }
                records.add(att);
            }
            for (Progress record : records) {
                String[] ids = new String[1];
                ids = lesson.getObjective().getId();
                consulta = "SELECT rating.name as ratingname,progress_report.comment,progress_report.step_id,progress_report.comment_date FROM progress_report  left JOIN rating on progress_report.rating_id = rating.id where lesson_id =" + lesson.getId() + " AND student_id = '" + record.getStudentid() + "' ";
                rs = stAux.executeQuery(consulta);
                while (rs.next()) {
                    record.setRating(rs.getString("ratingname"));
                    record.setComment(rs.getString("comment"));
                    record.setComment_date("" + rs.getDate("comment_date"));
                    String steps = rs.getString("step_id");
                    if (rs.wasNull()) {
                        record.setSteps("0");
                    } else if (steps.isEmpty() || steps.equals("null") || steps == null || steps.equals("") || steps.equals("0")) {
                        record.setSteps("0");

                    } else {
                        List<String> comma = Arrays.asList(steps.split(","));
                        record.setSteps("" + comma.size());
                    }
                }
            }
            con.close();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();

            consulta = "SELECT FirstName,LastName,MiddleName,StudentID FROM Students ";
            rs = stAux.executeQuery(consulta);
            HashMap<String, String> map = new HashMap<String, String>();
            String first, LastName, middle, studentID;
            while (rs.next()) {
                first = rs.getString("FirstName");
                LastName = rs.getString("LastName");
                middle = rs.getString("MiddleName");
                studentID = rs.getString("StudentID");
                map.put(studentID, first + " " + middle + " " + LastName);
            }
            for (Progress record : records) {
                String id = "" + record.getStudentid();
                String name = map.get(id);
                record.setStudentname(name);
            }

        } catch (SQLException ex) {
            System.out.println("Error  " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ProgressControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(ProgressControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        return records;
    }

    @RequestMapping("/lessonprogress/saveRecords.htm")
    public ModelAndView saveRecords(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        String message = "Records successfully saved";

        String[] lessonid = hsr.getParameterValues("TXTlessonid");

        ModelAndView mv = new ModelAndView("redirect:/lessonprogress/loadRecords.htm?LessonsSelected=" + lessonid[0], "message", message);
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();

            con = pool_local.getConnection();
            stAux = con.createStatement();

            String[] objectiveid = hsr.getParameterValues("TXTobjectiveid");
            String[] comments = hsr.getParameterValues("TXTcomment");
            String[] ratings = hsr.getParameterValues("TXTrating");
            String[] studentids = hsr.getParameterValues("TXTstudentid");

            String[] studentNames = hsr.getParameterValues("TXTstudentname");
            String nameTeacher = hsr.getParameter("nameTeacher");
            String namePresentation = hsr.getParameter("namePresentation");

            String[] att = hsr.getParameterValues("TXTattendance");
            String[] teacher = hsr.getParameterValues("TXTinstructor");
            String[] steps = hsr.getParameterValues("your_awesome_parameter");
            String archived = hsr.getParameter("buttonAchived");// is equal to on or null

            String termId = hsr.getParameter("termId");
            String yearId = hsr.getParameter("yearId");

            if (!teacher[0].isEmpty()) {
                stAux.executeUpdate("update lessons set presentedby = " + teacher[0] + " where id = " + lessonid[0]);
            }
            for (int i = 0; i < studentids.length; i++) {
                String attAux = "";
                if (!att[i].equals("0")) {
                    attAux = att[i];
                }
                String auxArch = "off";
                if (archived != null) {
                    auxArch = "on";
                }

                String note = "idPresentation: " + lessonid[0] + " | namePresentation: " + namePresentation + " | nameTeacher: " + nameTeacher + " | rating: " + ratings[i];
                note += " | comment: " + comments[i] + " | attendace: " + attAux + " | work accomplished: " + steps[i] + " | archived : " + auxArch;

                ActivityLog.log(user, "id: " + studentids[i] + " | studentName: " + studentNames[i], "Update Presentation Progress", note); //crear lesson        

                String test = "update lesson_stud_att set attendance = '" + att[i] + "',timestamp= now() where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'";
                stAux.executeUpdate(test);
            }
            rs = stAux.executeQuery("select id from obj_steps where obj_id = '" + objectiveid[0] + "' order by storder");
            ArrayList<String> allsteps = new ArrayList();
            while (rs.next()) {
                allsteps.add("" + rs.getInt("id"));
            }

            for (int i = 0; i < studentids.length; i++) {
                String step = null;
                if (!steps[i].equals("0") && !allsteps.isEmpty() && !steps[i].equals("")) {

                    ArrayList<String> al2 = new ArrayList<String>(allsteps.subList(0, (Integer.parseInt(steps[i]))));
                    StringBuilder rString = new StringBuilder();

                    String sep = ",";
                    for (String each : al2) {
                        rString.append(each).append(sep);
                    }
                    step = rString.toString();
                    step = step.substring(0, step.length() - 1);
                }

                String ratingid = null;
                rs = stAux.executeQuery("select id from rating where name = '" + ratings[i] + "'");

                while (rs.next()) {
                    ratingid = "" + rs.getInt("id");
                }

                rs = stAux.executeQuery("select * from progress_report where lesson_id =" + lessonid[0] + " AND student_id = '" + studentids[i] + "'");
                if (!rs.next()) {
                    if (ratingid != null) {
                        stAux.executeUpdate("insert into progress_report(comment_date,comment,rating_id,lesson_id,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id) values (now(),'" + comments[i] + "','" + ratingid + "','" + lessonid[0] + "','" + studentids[i] + "','" + objectiveid[0] + "',false,'" + step + "','" + user.getId() + "'," + termId + "," + yearId + ")");

                        //clone
                        /*String testClone= "insert into progress_report(comment_date,comment,rating_id,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id,linklesson_id) values (now(),'" + comments[i] + "','" + ratingid + "','" + studentids[i] + "','" + objectiveid[0] + "',false,'" + step + "','" + user.getId() + "',"+ sesion.getAttribute("termId") +"," + sesion.getAttribute("yearId")  + "," + lessonid[0]  +")";
                        DBConect.eduweb.executeUpdate(testClone);  */
                    } else {
                        stAux.executeUpdate("insert into progress_report(comment_date,comment,lesson_id,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id) values (now(),'" + comments[i] + "','" + lessonid[0] + "','" + studentids[i] + "','" + objectiveid[0] + "',false,'" + step + "','" + user.getId() + "'," + termId + "," + yearId + ")");

                        //clone
                        //  DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,comment,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id,linklesson_id) values (now(),'" + comments[i] + "','" + studentids[i] + "','" + objectiveid[0] + "',false,'" + step + "','" + user.getId() + "',"+ sesion.getAttribute("termId") +"," + sesion.getAttribute("yearId")  + "," + lessonid[0] +")");                   
                    }
                } else if (ratingid != null) {
                    String test = "update progress_report set comment_date = now(),comment = '" + comments[i] + "',rating_id ='" + ratingid + "' ,lesson_id = '" + lessonid[0] + "',student_id = '" + studentids[i] + "',objective_id ='" + objectiveid[0] + "',generalcomment = false ,step_id = '" + step + "' where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'";
                    stAux.executeUpdate("update progress_report set  modifyby = '" + user.getId() + "', comment_date = now(),comment = '" + comments[i] + "',rating_id ='" + ratingid + "' ,lesson_id = '" + lessonid[0] + "',student_id = '" + studentids[i] + "',objective_id ='" + objectiveid[0] + "',generalcomment = false ,step_id = '" + step + "' where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'");
                } else {
                    stAux.executeUpdate("update progress_report set  modifyby = '" + user.getId() + "',comment_date = now(),comment = '" + comments[i] + "',lesson_id = '" + lessonid[0] + "',student_id = '" + studentids[i] + "',objective_id ='" + objectiveid[0] + "',generalcomment = false ,step_id = '" + step + "' where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'");
                }
            }
            if ("on".equals(archived)) {
                stAux.executeUpdate("update lessons set archive = TRUE where id = '" + lessonid[0] + "'");
            } else {
                stAux.executeUpdate("update lessons set archive = FALSE where id = '" + lessonid[0] + "'");
            }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
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

        return mv;

    }
}
