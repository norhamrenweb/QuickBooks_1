/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
import static controladores.LessonsListControlador.log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nmohamed
 */
@Controller
public class LessonArchiveControlador {

    static Logger log = Logger.getLogger(LessonArchiveControlador.class.getName());

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    @RequestMapping("/lessonarchive/loadLessons.htm")
    public ModelAndView loadLessons(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("lessonarchive");
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
        mv.addObject("lessonslist", this.getLessons(user, hsr.getServletContext()));
        mv.addObject("username", user.getName());

        return mv;
    }

    public ArrayList<Lessons> getLessons(User user,ServletContext servlet) throws SQLException {
        int userid=user.getId();
        ArrayList<Lessons> lessonslist = new ArrayList<>();
        try {
            String consulta;
            if(user.getType()==1)
                consulta = "SELECT * FROM public.lessons where user_id = " + userid + " and COALESCE(idea, FALSE) = FALSE and archive = true";
            else 
                consulta = "SELECT * FROM public.lessons where COALESCE(idea, FALSE) = FALSE and archive = true";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            ArrayList<Integer> objectives = new ArrayList<>();
            ArrayList<Integer> subjects = new ArrayList<>();
            while (rs.next()) {
                Lessons lesson = new Lessons();
                //  lesson.setId(rs.getString("id_lessons"));
                lesson.setName(rs.getString("name"));
                lesson.setId(rs.getInt("id"));
                Level level = new Level();
                String name = level.fetchName(rs.getInt("level_id"), servlet);
                level.setName(name);
                lesson.setLevel(level);
                objectives.add(rs.getInt("objective_id"));
                subjects.add(rs.getInt("subject_id"));
                Timestamp stamp = rs.getTimestamp("start");
                Timestamp finish = rs.getTimestamp("finish");
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
                String dateStr = sdfDate.format(stamp);
                String timeStr = sdfTime.format(stamp);

                String timeStr2 = sdfTime.format(finish);
                lesson.setDate("" + dateStr);
                lesson.setStart(timeStr);
                lesson.setFinish(timeStr2);
                lessonslist.add(lesson);
            }
            
            for(int i = 0;i<lessonslist.size();i++){
                Objective sub = new Objective();
                sub.setName(sub.fetchName(objectives.get(i), servlet));
                lessonslist.get(i).setObjective(sub);
                Subject subject = new Subject();
                subject.setName(subject.fetchName(subjects.get(i), servlet));
                lessonslist.get(i).setSubject(subject);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return lessonslist;
    }

    @RequestMapping("/lessonarchive/detailsLesson.htm")
    @ResponseBody
    public String detailsLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        //    ModelAndView mv = new ModelAndView("homepage");
        JSONObject jsonObj = new JSONObject();
        String[] id = hsr.getParameterValues("LessonsSelected");
        ArrayList<Progress> records = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();
        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            String consulta = "select * FROM public.lessons WHERE id=" + id[0];
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            Objective o = new Objective();
            int idobj=0;
            while (rs.next()) {
                Method m = new Method();
                jsonObj.put("nameteacher", LessonsListControlador.fetchNameTeacher(rs.getInt("user_id"), hsr.getServletContext()));

                jsonObj.put("method", m.fetchName(rs.getInt("method_id"), hsr.getServletContext()));
                Timestamp date = rs.getTimestamp("date_created");
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdfDate.format(date);
                jsonObj.put("datecreated", dateStr);
                jsonObj.put("comment", rs.getString("comments"));
                idobj=rs.getInt("objective_id");
            }
            jsonObj.put("objective", o.fetchName(idobj, hsr.getServletContext()));
            consulta = "select name from content where id in (select content_id from lesson_content where lesson_id = " + id[0] + ")";
            ResultSet rs1 = DBConect.eduweb.executeQuery(consulta);
            while (rs1.next()) {
                contents.add(rs1.getString("name"));
            }
            jsonObj.put("contents", new Gson().toJson(contents));
            consulta = "SELECT * FROM public.lesson_stud_att where lesson_id =" + id[0];
            ResultSet rs2 = DBConect.eduweb.executeQuery(consulta);

            while (rs2.next()) {
                Progress att = new Progress();

                att.setStudentid(rs2.getInt("student_id"));
                records.add(att);
            }
            consulta = "SELECT FirstName,LastName,MiddleName,StudentID FROM AH_ZAF.dbo.Students ";
            ResultSet rs3 = DBConect.ah.executeQuery(consulta);
            HashMap<String, String> map = new HashMap<String, String>();
            String first, LastName, middle, studentID;
            while (rs3.next()) {
                first = rs3.getString("FirstName");
                LastName = rs3.getString("LastName");
                middle = rs3.getString("MiddleName");
                studentID = rs3.getString("StudentID");
                map.put(studentID, LastName + ", " + first + " " + middle);
            }
            for (Progress record : records) {
                String id2 = "" + record.getStudentid();
                String name = map.get(id2);
                record.setStudentname(name);
            }
            jsonObj.put("students", new Gson().toJson(records));
            consulta = "select name from obj_steps where id="+idobj;
            ResultSet rs4 = DBConect.eduweb.executeQuery(consulta);
            ArrayList<String> steps = new ArrayList<>();
            while(rs4.next()){
                steps.add(rs4.getString("name"));
            }
            jsonObj.put("steps", new Gson().toJson(steps));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return jsonObj.toString();
    
    }

    @RequestMapping("/lessonarchive/deleteLesson.htm")
    @ResponseBody
    public String deleteLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        JSONObject jsonObj = new JSONObject();
        String[] id = hsr.getParameterValues("LessonsSelected");
        String message = null;
        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            String consulta;
            if (message == null) {
                consulta = "DELETE FROM lesson_content WHERE lesson_id=" + id[0];
                DBConect.eduweb.executeUpdate(consulta);
                consulta = "DELETE FROM lesson_stud_att WHERE lesson_id=" + id[0];
                DBConect.eduweb.executeUpdate(consulta);
                consulta = "DELETE FROM public.lessons WHERE id=" + id[0];
                DBConect.eduweb.executeUpdate(consulta);
                message = "Presentation deleted successfully";
            }
            //mv.addObject("lessonslist", this.getLessons(user.getId(),hsr.getServletContext()));
            //mv.addObject("messageDelete",message);
            jsonObj.put("message", message);
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return jsonObj.toString();
        //return mv;
    }

}
