/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
import static controladores.LessonsListControlador.log;
import java.beans.PropertyVetoException;
import java.io.IOException;
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
        mv.addObject("lessonslist", this.getLessons(user, hsr));
        mv.addObject("username", user.getName());
        int iduser = ((User) hsr.getSession().getAttribute("user")).getId();
        mv.addObject("teacherlist", LessonsListControlador.getTeachers(iduser));
        return mv;
    }

    public ArrayList<Lessons> getLessons(User user, HttpServletRequest hsr) throws SQLException {
        ServletContext servlet = hsr.getServletContext();
        int userid = user.getId();
        ArrayList<Lessons> lessonslist = new ArrayList<>();

        try {
            PoolC3P0_RenWeb pool_rw = PoolC3P0_RenWeb.getInstance();
            Connection c_ah = pool_rw.getConnection();

            PoolC3P0_Local pool_lcl = PoolC3P0_Local.getInstance();
            Connection c_local = pool_lcl.getConnection();

            Statement stAux = c_ah.createStatement();
            String consulta = "SELECT GradeLevel,GradeLevelID FROM GradeLevels";
            ResultSet rs = stAux.executeQuery(consulta);
            HashMap<Integer, String> mapLevel = new HashMap<Integer, String>();
            String name;
            int id;
            while (rs.next()) {
                name = rs.getString("GradeLevel");
                id = rs.getInt("GradeLevelID");
                mapLevel.put(id, name);
            }

            //con_edu = DBCPDataSource.getConnection_eduWeb();
            stAux = c_local.createStatement();

            consulta = "SELECT * FROM public.objective";
            rs = stAux.executeQuery(consulta);

            HashMap<Integer, String> mapObjective = new HashMap<Integer, String>();
            while (rs.next()) {
                name = rs.getString("name");
                id = rs.getInt("id");
                mapObjective.put(id, name);
            }

            //con_ah = DBCPDataSource.getConnection_ah();
            stAux = c_ah.createStatement();
            consulta = "SELECT Title,CourseID FROM Courses";
            rs = stAux.executeQuery(consulta);

            HashMap<Integer, String> mapSubject = new HashMap<Integer, String>();
            while (rs.next()) {
                name = rs.getString("Title");
                id = rs.getInt("CourseID");
                mapSubject.put(id, name);
            }

             stAux = c_local.createStatement();
            if (user.getType() == 1) {
                consulta = "SELECT * FROM public.lessons where user_id = " + userid + " and COALESCE(idea, FALSE) = FALSE and archive = true "
                        + " and yearterm_id=" + hsr.getSession().getAttribute("yearId") + " and term_id=" + hsr.getSession().getAttribute("termId");
            } else {
                consulta = "SELECT * FROM public.lessons where COALESCE(idea, FALSE) = FALSE and archive = true"
                        + " and yearterm_id=" + hsr.getSession().getAttribute("yearId") + " and term_id=" + hsr.getSession().getAttribute("termId");
            }

            rs = stAux.executeQuery(consulta);
            ArrayList<Integer> objectives = new ArrayList<>();
            ArrayList<Integer> subjects = new ArrayList<>();
            while (rs.next()) {
                Lessons lesson = new Lessons();
                //  lesson.setId(rs.getString("id_lessons"));
                lesson.setName(rs.getString("name"));
                lesson.setId(rs.getInt("id"));
                Level level = new Level();
                int idLevel = rs.getInt("level_id");
                name = mapLevel.get(idLevel);
                level.setName(name);
                lesson.setLevel(level);

                Objective sub = new Objective();
                int idObjective = rs.getInt("objective_id");
                name = mapObjective.get(idObjective);
                sub.setName(name);
                lesson.setObjective(sub);

                Subject subject = new Subject();
                int idSubject = rs.getInt("subject_id");
                name = mapSubject.get(idSubject);
                subject.setName(name);
                lesson.setSubject(subject);

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
                lesson.setShare(false);
                lessonslist.add(lesson);
            }

            if (user.getType() == 1) {
                consulta = "SELECT * FROM lessons inner join lessonpresentedby on lessonid=id where teacherid=" + user.getId() + " AND COALESCE(idea, FALSE) = FALSE and archive = true";

                rs = stAux.executeQuery(consulta);

                while (rs.next()) {
                    Lessons lesson = new Lessons();
                    //  lesson.setId(rs.getString("id_lessons"));
                    lesson.setName(rs.getString("name"));
                    lesson.setId(rs.getInt("id"));
                    Level level = new Level();
                    int idLevel = rs.getInt("level_id");
                    name = mapLevel.get(idLevel);
                    level.setName(name);
                    lesson.setLevel(level);

                    Objective sub = new Objective();
                    int idObjective = rs.getInt("objective_id");
                    name = mapObjective.get(idObjective);
                    sub.setName(name);
                    lesson.setObjective(sub);

                    Subject subject = new Subject();
                    int idSubject = rs.getInt("subject_id");
                    name = mapSubject.get(idSubject);
                    subject.setName(name);
                    lesson.setSubject(subject);

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
                    lesson.setShare(true);
                    lessonslist.add(lesson);
                }
            }
            /*   for(int i = 0;i<lessonslist.size();i++){
                Objective sub = new Objective();
                sub.setName(sub.fetchName(objectives.get(i), servlet));
                lessonslist.get(i).setObjective(sub);
                Subject subject = new Subject();
                subject.setName(subject.fetchName(subjects.get(i), servlet));
                lessonslist.get(i).setSubject(subject);
            }*/
            c_local.close();
            c_ah.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LessonArchiveControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(LessonArchiveControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        return lessonslist;
    }

    @RequestMapping("/lessonarchive/detailsLesson.htm")
    @ResponseBody
    public String detailsLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        //     ModelAndView mv = new ModelAndView("homepage");
        JSONObject jsonObj = new JSONObject();
        String[] id = hsr.getParameterValues("LessonsSelected");
        ArrayList<Progress> records = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();

        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();

            con = pool_local.getConnection();
            stAux = con.createStatement();

            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            String consulta = "select * FROM public.lessons WHERE id=" + id[0];
            rs = stAux.executeQuery(consulta);
            Objective o = new Objective();
            int idobj = 0;
            while (rs.next()) {
                Method m = new Method();
                jsonObj.put("nameteacher", LessonsListControlador.fetchNameTeacher(rs.getInt("user_id"), hsr.getServletContext()));

                jsonObj.put("method", m.fetchName(rs.getInt("method_id"), hsr.getServletContext()));
                Timestamp date = rs.getTimestamp("date_created");
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdfDate.format(date);
                jsonObj.put("datecreated", dateStr);
                jsonObj.put("comment", rs.getString("comments"));
                idobj = rs.getInt("objective_id");
            }
            jsonObj.put("objective", o.fetchName(idobj, hsr.getServletContext()));
            consulta = "select name from content where id in (select content_id from lesson_content where lesson_id = " + id[0] + ")";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                contents.add(rs.getString("name"));
            }
            jsonObj.put("contents", new Gson().toJson(contents));
            consulta = "SELECT * FROM public.lesson_stud_att where lesson_id =" + id[0];
            rs = stAux.executeQuery(consulta);

            while (rs.next()) {
                Progress att = new Progress();

                att.setStudentid(rs.getInt("student_id"));
                records.add(att);
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
                map.put(studentID, LastName + ", " + first + " " + middle);
            }
            for (Progress record : records) {
                String id2 = "" + record.getStudentid();
                String name = map.get(id2);
                record.setStudentname(name);
            }
            con.close();

            con = pool_local.getConnection();
            stAux = con.createStatement();

            jsonObj.put("students", new Gson().toJson(records));
            consulta = "select name from obj_steps where obj_id=" + idobj;
            rs = stAux.executeQuery(consulta);
            ArrayList<String> steps = new ArrayList<>();
            while (rs.next()) {
                steps.add(rs.getString("name"));
            }
            jsonObj.put("steps", new Gson().toJson(steps));
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
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

        return jsonObj.toString();

    }

    @RequestMapping("/lessonarchive/compartir.htm")
    @ResponseBody
    public String compartirLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws JSONException {
        String obj = hsr.getParameter("obj");
        JSONObject json = new JSONObject(obj);
        JSONArray ids = json.getJSONArray("teachers");
        String idlesson = json.getString("id");

        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String consulta;
            consulta = "delete from lessonpresentedby where lessonid=" + idlesson;
            stAux.executeUpdate(consulta);
            for (int i = 0; i < ids.length(); i++) {
                consulta = "insert into lessonpresentedby values(" + idlesson + "," + ids.getString(i) + ")";
                stAux.executeUpdate(consulta);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(LessonsListControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return "error";
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LessonArchiveControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(LessonArchiveControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        return "Succesfully share";
    }

    @RequestMapping("/lessonarchive/cargarcompartidos.htm")
    @ResponseBody
    public String compartirSelect(HttpServletRequest hsr, HttpServletResponse hsr1) throws JSONException {
        String idlesson = hsr.getParameter("seleccion");
        String consulta = "select * from lessonpresentedby where lessonid=" + idlesson;
        ArrayList<Integer> teacherids = new ArrayList<>();
        ArrayList<Teacher> tlist = new ArrayList<>();

        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                teacherids.add(rs.getInt("teacherid"));
            }
            String name;
            for (Integer s : teacherids) {
                name = LessonsListControlador.fetchNameTeacher(s, hsr.getServletContext());
                tlist.add(new Teacher(name, s));
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(LessonsListControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LessonArchiveControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(LessonArchiveControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("t", new Gson().toJson(tlist));
        return jsonObj.toString();
    }

    @RequestMapping("/lessonarchive/deleteLesson.htm")
    @ResponseBody
    public String deleteLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        JSONObject jsonObj = new JSONObject();
        String[] id = hsr.getParameterValues("LessonsSelected");
        String message = null;
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            String consulta;
            if (message == null) {
                consulta = "DELETE FROM lesson_content WHERE lesson_id=" + id[0];
                stAux.executeUpdate(consulta);
                consulta = "DELETE FROM lesson_stud_att WHERE lesson_id=" + id[0];
                stAux.executeUpdate(consulta);
                consulta = "DELETE FROM public.lessons WHERE id=" + id[0];
                stAux.executeUpdate(consulta);
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

        return jsonObj.toString();
        //return mv;
    }

}
