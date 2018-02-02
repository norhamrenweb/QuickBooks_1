/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try {
            String lessonid = hsr.getParameter("LessonsSelected");

            String consulta = "SELECT * FROM public.lesson_detailes where id =" + lessonid;
            ResultSet rs1 = DBConect.eduweb.executeQuery(consulta);
            Lessons lesson = new Lessons();
            while (rs1.next()) {
                Objective obj = new Objective();
                obj.setName(rs1.getString("objectivename"));
                String[] ids = new String[1];
                ids[0] = String.valueOf(rs1.getInt("objective_id"));
                obj.setId(ids);
                lesson.setObjective(obj);
                Subject sub = new Subject();
                String name = sub.fetchName(rs1.getInt("subject_id"), hsr.getServletContext());
                sub.setName(name);
                String[] subids = new String[1];
                subids[0] = String.valueOf(rs1.getInt("subject_id"));
                sub.setId(ids);
                lesson.setSubject(sub);
                lesson.setName(rs1.getString("name"));
                lesson.setId(Integer.parseInt(lessonid));
            }
            consulta = "select presentedby from lessons where id = " + lessonid;
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {

                teacher.setId_students(rs.getInt("presentedby"));
            }
            List<Step> steps = new ArrayList<>();
            String[] objid = lesson.getObjective().getId();
            ResultSet rs3 = DBConect.eduweb.executeQuery("select name,id,storder from obj_steps where obj_id =" + objid[0]);
            while (rs3.next()) {
                Step s = new Step();
                s.setName(rs3.getString("name"));
                s.setId("" + rs3.getInt("id"));
                s.setOrder(rs3.getInt("storder"));
                steps.add(s);
            }
            ResultSet rs4 = DBConect.eduweb.executeQuery("select archive from lessons where id =" + lessonid);
            while (rs4.next()) {
                if (rs4.getBoolean("archive")) {
                    disable = "t";
                }
            }
            List<Progress> records = this.getRecords(lesson, hsr.getServletContext());
            mv.addObject("attendancelist", records);
            mv.addObject("lessondetailes", lesson);
            mv.addObject("disable", disable);
            mv.addObject("steps", steps);
            consulta = "select LastName,FirstName,MiddleName,PersonID from Person where PersonID in (select PersonID from Staff where Faculty = 1) order by LastName";
            ResultSet rs2 = DBConect.ah.executeQuery(consulta);
            while (rs2.next()) {
                Students s = new Students();
                s.setId_students(rs2.getInt("PersonID"));
                s.setNombre_students(rs2.getString("LastName") + ", " + rs2.getString("FirstName") + " " + rs2.getString("MiddleName"));
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
        }
        return mv;

    }

    public List<Progress> getRecords(Lessons lesson, ServletContext servlet) throws SQLException {

        List<Progress> records = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM public.lesson_stud_att where lesson_id =" + lesson.getId();
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);

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
                ResultSet rs3 = DBConect.eduweb.executeQuery(consulta);
                while (rs3.next()) {
                    record.setRating(rs3.getString("ratingname"));
                    record.setComment(rs3.getString("comment"));
                    record.setComment_date("" + rs3.getDate("comment_date"));
                    String steps = rs3.getString("step_id");
                    if (rs3.wasNull()) {
                        record.setSteps("0");
                    } else if (steps.isEmpty() || steps.equals("null") || steps == null || steps.equals("")) {
                        record.setSteps("0");

                    } else {
                        List<String> comma = Arrays.asList(steps.split(","));
                        record.setSteps("" + comma.size());
                    }
                }
            }
            consulta = "SELECT FirstName,LastName,MiddleName,StudentID FROM AH_ZAF.dbo.Students ";
            ResultSet rs2 = DBConect.ah.executeQuery(consulta);
            HashMap<String, String> map = new HashMap<String, String>();
            String first, LastName, middle, studentID;
            while (rs2.next()) {
                first = rs2.getString("FirstName");
                LastName = rs2.getString("LastName");
                middle = rs2.getString("MiddleName");
                studentID = rs2.getString("StudentID");
                map.put(studentID, LastName + ", " + first + " " + middle);
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
        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            String userId = "" + user.getId();
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

            if (!teacher[0].isEmpty()) {
                DBConect.eduweb.executeUpdate("update lessons set presentedby = " + teacher[0] + " where id = " + lessonid[0]);
            }
            for (int i = 0; i < studentids.length; i++) {
                String attAux = "";
                if (!att[i].equals("0")) {
                    attAux = att[i];
                }

                String note = "idPresentation: " + lessonid[0] + " | namePresentation: " + namePresentation +" | nameTeacher: "+nameTeacher+ " | rating: " + ratings[i];
                note += " | comment: " + comments[i] + " | attendace: " + attAux + " | work accomplished: " + steps[i];
                
                ActivityLog.log(user.getId(), "id: " + studentids[i] + " | studentName: " + studentNames[i], "Update Presentation Progress", note); //crear lesson        
                
                
                String test = "update lesson_stud_att set attendance = '" + att[i] + "',timestamp= now() where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'";
                DBConect.eduweb.executeUpdate(test);
            }
            ResultSet rs2 = DBConect.eduweb.executeQuery("select id from obj_steps where obj_id = '" + objectiveid[0] + "' order by storder");
            ArrayList<String> allsteps = new ArrayList();
            while (rs2.next()) {
                allsteps.add("" + rs2.getInt("id"));
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
                ResultSet rs1 = DBConect.eduweb.executeQuery("select id from rating where name = '" + ratings[i] + "'");

                while (rs1.next()) {
                    ratingid = "" + rs1.getInt("id");
                }

                ResultSet rs = DBConect.eduweb.executeQuery("select * from progress_report where lesson_id =" + lessonid[0] + " AND student_id = '" + studentids[i] + "'");
                if (!rs.next()) {
                    if (ratingid != null) {
                        DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,comment,rating_id,lesson_id,student_id,objective_id,generalcomment,step_id,createdby) values (now(),'" + comments[i] + "','" + ratingid + "','" + lessonid[0] + "','" + studentids[i] + "','" + objectiveid[0] + "',false,'" + step + "','" + user.getId() + "')");
                    } else {
                        DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,comment,lesson_id,student_id,objective_id,generalcomment,step_id,createdby) values (now(),'" + comments[i] + "','" + lessonid[0] + "','" + studentids[i] + "','" + objectiveid[0] + "',false,'" + step + "','" + user.getId() + "')");
                    }
                } else if (ratingid != null) {
                    String test = "update progress_report set comment_date = now(),comment = '" + comments[i] + "',rating_id ='" + ratingid + "' ,lesson_id = '" + lessonid[0] + "',student_id = '" + studentids[i] + "',objective_id ='" + objectiveid[0] + "',generalcomment = false ,step_id = '" + step + "' where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'";
                    DBConect.eduweb.executeUpdate("update progress_report set  modifyby = '" + user.getId() + "', comment_date = now(),comment = '" + comments[i] + "',rating_id ='" + ratingid + "' ,lesson_id = '" + lessonid[0] + "',student_id = '" + studentids[i] + "',objective_id ='" + objectiveid[0] + "',generalcomment = false ,step_id = '" + step + "' where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'");
                } else {
                    DBConect.eduweb.executeUpdate("update progress_report set  modifyby = '" + user.getId() + "',comment_date = now(),comment = '" + comments[i] + "',lesson_id = '" + lessonid[0] + "',student_id = '" + studentids[i] + "',objective_id ='" + objectiveid[0] + "',generalcomment = false ,step_id = '" + step + "' where lesson_id = " + lessonid[0] + " AND student_id = '" + studentids[i] + "'");
                }
            }
            if ("on".equals(archived)) {
                DBConect.eduweb.executeUpdate("update lessons set archive = TRUE where id = '" + lessonid[0] + "'");
            } else {
                DBConect.eduweb.executeUpdate("update lessons set archive = FALSE where id = '" + lessonid[0] + "'");
            }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return mv;

    }
}
