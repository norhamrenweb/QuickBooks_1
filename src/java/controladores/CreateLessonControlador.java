/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.google.gson.*;
import static controladores.ProgressbyStudent.getNextDate;
import static controladores.ProgressbyStudent.log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author nmohamed
 */
@Controller
public class CreateLessonControlador {

    static Logger log = Logger.getLogger(CreateLessonControlador.class.getName());
//      private ServletContext servlet;

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    @RequestMapping("/createlesson/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("createlesson");
        try {
            List<Lessons> ideas = new ArrayList();
            mv.addObject("listaAlumnos", Students.getStudents(log));
            ResultSet rs = DBConect.ah.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels");
            List<Level> grades = new ArrayList();
            Level l = new Level();
            l.setName("Select level");
            String[] aux = new String[1];
            aux[0] = "-1";
            l.setId(aux);
            grades.add(l);
            while (rs.next()) {
                Level x = new Level();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("GradeLevelID");
                x.setId(ids);
                x.setName(rs.getString("GradeLevel"));
                grades.add(x);
            }
            ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT * FROM public.method");
            List<Method> methods = new ArrayList();
            Method m = new Method();
            m.setName("Select Method");
            methods.add(m);
            while (rs1.next()) {
                Method x = new Method();
                String[] ids = new String[1];
                ids[0] = "" + rs1.getInt("id");
                x.setId(ids);
                x.setName(rs1.getString("name"));
                x.setDescription(rs1.getString("description"));
                methods.add(x);
            }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods", methods);
            //get lesson ideas
            ResultSet rs2 = DBConect.eduweb.executeQuery("SELECT * FROM public.lessons where idea = true");
            Lessons d = new Lessons();
            d.setName("Select an idea");
            ideas.add(d);
            while (rs2.next()) {
                Lessons idea = new Lessons();
                idea.setId(rs2.getInt("id"));
                idea.setName(rs2.getString("name"));
                ideas.add(idea);
            }
            mv.addObject("ideas", ideas);
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    @RequestMapping("/createlesson/startIdea.htm")
    public ModelAndView startIdea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("lessonidea");
        try {
            List<Lessons> ideas = new ArrayList();
            mv.addObject("listaAlumnos", Students.getStudents(log));
            ResultSet rs = DBConect.ah.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels");
            List<Level> grades = new ArrayList();
            Level l = new Level();
            l.setName("Select level");
            grades.add(l);
            while (rs.next()) {
                Level x = new Level();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("GradeLevelID");
                x.setId(ids);
                x.setName(rs.getString("GradeLevel"));
                grades.add(x);
            }
            ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT * FROM public.method");
            List<Method> methods = new ArrayList();
            Method m = new Method();
            m.setName("Select Method");
            methods.add(m);
            while (rs1.next()) {
                Method x = new Method();
                String[] ids = new String[1];
                ids[0] = "" + rs1.getInt("id");
                x.setId(ids);
                x.setName(rs1.getString("name"));
                x.setDescription(rs1.getString("description"));
                methods.add(x);
            }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods", methods);
            //get lesson ideas
            ResultSet rs2 = DBConect.eduweb.executeQuery("SELECT * FROM public.lessons where idea = true");
            while (rs2.next()) {
                Lessons idea = new Lessons();
                idea.setId(rs2.getInt("id"));
                idea.setName(rs2.getString("name"));
                ideas.add(idea);
            }
            mv.addObject("ideas", ideas);
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return mv;
    }

    @RequestMapping("/createlesson/studentlistLevel.htm")
    public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("createlesson");

        List<Students> studentsgrades = new ArrayList();
        String[] levelid = hsr.getParameterValues("seleccion");
        String test = hsr.getParameter("levelStudent");
        if (!levelid[0].equals("-1")) {
            studentsgrades = Students.getStudentslevel(levelid[0], log);
        } else {
            studentsgrades = Students.getStudents(log);
        }
        mv.addObject("listaAlumnos", studentsgrades);

        return mv;
    }

    @RequestMapping("/createlesson/subjectlistLevel.htm")
    @ResponseBody
    public String subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return (new Gson()).toJson(this.getSubjects(hsr.getParameterValues("seleccion1")));
    }

    @RequestMapping("/createlesson/objectivelistSubject.htm")
    @ResponseBody
    public String objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return (new Gson()).toJson(this.getObjectives(hsr.getSession(),hsr.getParameterValues("seleccion2")));
    }

    @RequestMapping("/createlesson/contentlistObjective.htm")
    @ResponseBody
    public String contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return (new Gson()).toJson(this.getContent(hsr.getParameterValues("seleccion3")));
    }

    @RequestMapping("/createlesson/createlesson.htm")
    public ModelAndView createlesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        String message = new String();
        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            Lessons newlesson = new Lessons();
            List<String> contentids;
            Subject subject = new Subject();
            Objective objective = new Objective();
            Level level = new Level();
            level.setName(hsr.getParameter("TXTlevel"));
            String[] gs = hsr.getParameterValues("TXTlevel");
            level.setId(hsr.getParameterValues("TXTlevel"));

            // Object s1 = (hsr.getParameter("TXTlevel"));
            String s = hsr.getParameter("lessonName");

            subject.setName(hsr.getParameter("TXTsubject"));
            subject.setId(hsr.getParameterValues("TXTsubject"));
            objective.setName(hsr.getParameter("TXTobjective"));
            objective.setId(hsr.getParameterValues("TXTobjective"));
            String[] test = hsr.getParameterValues("TXTcontent");

            String lvlName = hsr.getParameter("levelName");
            String subjectName = hsr.getParameter("subjectName");
            String objName = hsr.getParameter("objectiveName");
            String studNames = hsr.getParameter("studentsName");

            //optional field, avoid null pointer exception
            if (test != null && test.length > 0) {
                contentids = Arrays.asList(hsr.getParameterValues("TXTcontent"));
                newlesson.setContentid(contentids);
            }
            newlesson.setComments(hsr.getParameter("TXTdescription"));
            Method m = new Method();
            String[] test2 = hsr.getParameterValues("TXTmethod");
            //optional field, avoid null pointer exception
            if (test2 != null && test2.length > 0) {
                m.setId(hsr.getParameterValues("TXTmethod"));
                m.setName(hsr.getParameter("TXTmethod"));
                newlesson.setMethod(m);
            } else {
                m.setName("");
                newlesson.setMethod(m);
            }
            String[] ideaCheck = hsr.getParameterValues("ideaCheck");

            newlesson.setTeacherid(user.getId());

            newlesson.setLevel(level);
            newlesson.setSubject(subject);
            newlesson.setObjective(objective);

            newlesson.setName(hsr.getParameter("TXTnombreLessons"));
            newlesson.setTemplate(false);

            Createlesson c = new Createlesson(hsr.getServletContext());
            // gives a null pointer exception , need to fix  

            String note = " name: " + hsr.getParameter("TXTnombreLessons") + " | level: " + lvlName + " | subject: " + subjectName + " | objective: " + objName;
            note += " | Date: " + hsr.getParameter("TXTfecha") + " | start: " + hsr.getParameter("TXThorainicio") + " | finish: " + hsr.getParameter("TXThorafin");

            if (ideaCheck != null) {
                if (ideaCheck[0].equals("on")) {
                    c.newidea(hsr, note, newlesson);
                    message = "Presentation idea created";
                }
            } else {
                java.sql.Timestamp timestampstart = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha") + " " + hsr.getParameter("TXThorainicio") + ":00.000");
                java.sql.Timestamp timestampend = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha") + " " + hsr.getParameter("TXThorafin") + ":00.000");
                String[] studentIds = hsr.getParameterValues("destino[]");

                newlesson.setStart("" + timestampstart);
                newlesson.setFinish("" + timestampend);
                c.newlesson(hsr, note, studNames, studentIds, newlesson);
                message = "Presentation created";
            }

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        ModelAndView mv = new ModelAndView("redirect:/createlesson/start.htm", "message", message);
        return mv;
    }
//coge la nombre de subject seleccionado y devuelve la lista de lessons que son templates 

    @RequestMapping("/createlesson/namelistSubject.htm")
    public ModelAndView namelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("createlesson");
        List<Lessons> lessons = new ArrayList<>();

        try {

            String subjectid = null;
            subjectid = hsr.getParameter("seleccionTemplate");
            ResultSet rs1 = DBConect.eduweb.executeQuery("select name,id from lesson_plan where subject_id= " + subjectid);
            Lessons l = new Lessons();
            l.setName("Select lesson name");
            lessons.add(l);
            while (rs1.next()) {
                Lessons ll = new Lessons();
                ll.setName(rs1.getString("name"));
                ll.setId(rs1.getInt("id"));
                lessons.add(ll);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        mv.addObject("lessons", lessons);

        return mv;
    }

    @RequestMapping("/createlesson/copyfromIdea.htm")
    @ResponseBody
    public String copyfromIdea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("createlesson");
        JSONObject json = new JSONObject();
        String[] lessonplanid = hsr.getParameterValues("seleccionidea");
        Subject sub = new Subject();
        Objective obj = new Objective();
        Method meth = new Method();
        Level lev = new Level();
        String comment = "";
        int levelid = 0;
        List<String> contents = new ArrayList<>();
        DriverManagerDataSource dataSource;
        try {
            String[] oid = new String[1];
            String[] sid = new String[1];
            String[] mid = new String[1];
            String[] cid = new String[1];

            String consulta = "SELECT objective_id,subject_id,level_id,method_id,comments FROM public.lessons where id =" + lessonplanid[0];
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);

            while (rs.next()) {
                oid[0] = "" + rs.getInt("objective_id");
                obj.setId(oid);
                sid[0] = "" + rs.getInt("subject_id");
                sub.setId(sid);
                mid[0] = "" + rs.getInt("method_id");
                meth.setId(mid);
                levelid = rs.getInt("level_id");
                comment = rs.getString("comments");
            }

            ResultSet rs2 = DBConect.eduweb.executeQuery("select content_id from public.lesson_content where lesson_id = " + lessonplanid[0]);

            while (rs2.next()) {
                //  Content eq = new Content();
                String ids = null;
                ids = "" + rs2.getInt("content_id");

                //   eq.setId(ids);
                contents.add(ids);
            }

            json.put("level", "" + levelid);
            json.put("subject", new Gson().toJson(sub));
            json.put("objective", new Gson().toJson(obj));
            json.put("method", new Gson().toJson(meth));
            json.put("content", new Gson().toJson(contents));
            json.put("comment", comment);

        } catch (SQLException ex) {
            System.out.println("Error  " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        String hi = json.toString();
        String[] ids = new String[1];
        ids[0] = "" + levelid;
        json.put("objectiveslist", new Gson().toJson(this.getObjectives(hsr.getSession(),sub.getId())));
        json.put("contentslist", new Gson().toJson(this.getContent(obj.getId())));
        json.put("subjectslist", new Gson().toJson(this.getSubjects(ids)));

        return json.toString();

    }

    @RequestMapping("/createlesson/loadRecommend.htm")
    @ResponseBody
    public String loadRecommend(@RequestBody Observation r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        int objId = r.getId();
        ArrayList<String> studentIds = new ArrayList<>();

        ResultSet rs2 = DBConect.eduweb.executeQuery("select * from recommendations where id_objective=" + objId);
        while (rs2.next()) {//existe
            studentIds.add("" + rs2.getInt("id_student"));
        }

        return new Gson().toJson(studentIds);
    }

    public ArrayList<Subject> getSubjects(String[] levelid) throws SQLException {

        ArrayList<Subject> subjects = new ArrayList<>();
        ArrayList<Subject> activesubjects = new ArrayList<>();

        HashMap<String, String> mapSubject = new HashMap<String, String>();
        String termid = null;
        String yearid = null;

        try {
            ResultSet rs = DBConect.ah.executeQuery("select defaultyearid,defaulttermid from ConfigSchool where configschoolid = 1");
            while (rs.next()) {
                termid = "" + rs.getInt("defaulttermid");
                yearid = "" + rs.getInt("defaultyearid");
            }
            ResultSet rs1 = DBConect.ah.executeQuery("select distinct courses.courseid,courses.rcplacement, courses.title, courses.active from courses where courses.active = 1 " );// the term and year need to be dynamic, check with vincent

            String name9, id;
            while (rs1.next()) {
                name9 = rs1.getString("Title");
                id = rs1.getString("CourseID");
                mapSubject.put(id, name9);
            }

            ResultSet rs4 = DBConect.ah.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID =" + levelid[0] + ")");

            while (rs4.next()) {
                Subject sub = new Subject();
                String[] ids = new String[1];
                ids[0] = "" + rs4.getInt("CourseID");
                sub.setId(ids);
                subjects.add(sub);
            }

            String[] aux = new String[1];
            aux[0] = "-1";
            Subject sub = new Subject();
            sub.setId(aux);
            sub.setName("Select subject");
            activesubjects.add(sub);

            for (int i = 1; i < subjects.size(); i++) {
                if (mapSubject.containsKey(subjects.get(i).getId()[0])) {
                    subjects.get(i).setName(mapSubject.get(subjects.get(i).getId()[0]));
                    activesubjects.add(subjects.get(i));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return activesubjects;
    }

    public static ArrayList<Objective> getObjectives(HttpSession session,String[] subjectid) throws SQLException {
        ArrayList<Objective> objectives = new ArrayList<>();
        try {
            String consulta = "select name,id from public.objective where subject_id='" + subjectid[0] + "' "
//                    + " and year_id=" + session.getAttribute("yearId")
//                    + " and term_id=" + session.getAttribute("termId")
                    + " order by name";
            ResultSet rs1 = DBConect.eduweb.executeQuery(consulta);
            Objective s = new Objective();
            s.setName("Select Objective");
            objectives.add(s);

            while (rs1.next()) {
                String[] ids = new String[1];
                Objective sub = new Objective();
                ids[0] = "" + rs1.getInt("id");
                sub.setId(ids);
                sub.setName(rs1.getString("name"));
                objectives.add(sub);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Objectives: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return objectives;
    }

    public ArrayList<Content> getContent(String[] objectiveid) throws SQLException {
        ArrayList<Content> contents = new ArrayList<>();
        try {
            ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id =" + objectiveid[0] + ")");

            while (rs1.next()) {
                Content eq = new Content();
                String[] id = new String[1];
                id[0] = "" + rs1.getInt("id");

                eq.setId(id);
                eq.setName(rs1.getString("name"));
                contents.add(eq);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return contents;
    }
}
