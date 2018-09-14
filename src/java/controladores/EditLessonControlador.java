/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.google.gson.*;
import static controladores.CreateLessonControlador.log;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
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
public class EditLessonControlador {

    static Logger log = Logger.getLogger(EditLessonControlador.class.getName());
//      private ServletContext servlet;

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    private boolean existInProgress_report(String id, HttpServletRequest hsr) {
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String presentationId = id;
            String consulta = "SELECT lesson_id FROM progress_report WHERE lesson_id = '" + presentationId + "' and (rating_id not in (6,7) or comment <> '')";//incase there was a progress record created and then removed or placed NA
            rs = stAux.executeQuery(consulta);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EditLessonControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(EditLessonControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        return false;
    }

    @RequestMapping("/editlesson/loadRecommend.htm")
    @ResponseBody
    public String loadRecommend(@RequestBody Observation r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        int objId = r.getId();
        ArrayList<String> studentIds = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            
            ResultSet rs2 = stAux.executeQuery("select * from recommendations where id_objective=" + objId);
            while (rs2.next()) {//existe
                studentIds.add("" + rs2.getInt("id_student"));
            }
            
        } catch (SQLException e) {
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
        return new Gson().toJson(studentIds);
    }

    @RequestMapping("/editlesson/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("editlesson");
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            
            Lessons data = new Lessons();
            Level l = new Level();
            ArrayList<Content> c = new ArrayList<>();
            ArrayList<Students> stud = new ArrayList<>();
            Objective o = new Objective();
            Subject s = new Subject();
            Method m = new Method();
            String[] id = new String[1];
            String lessonid = hsr.getParameter("LessonsSelected");
            rs = stAux.executeQuery("select * from lessons where id= " + lessonid);
            while (rs.next()) {
                data.setComments(rs.getString("comments"));
                Timestamp stamp = rs.getTimestamp("start");
                Timestamp finish = rs.getTimestamp("finish");
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
                String dateStr = sdfDate.format(stamp);
                String timeStr = sdfTime.format(stamp);
                String timeStr2 = sdfTime.format(finish);
                data.setDate("" + dateStr);
                data.setStart(timeStr);
                data.setFinish(timeStr2);
                data.setId(Integer.parseInt(lessonid));
                l.setId(new String[]{"" + rs.getInt("level_id")});
                m.setId(new String[]{"" + rs.getInt("method_id")});
                data.setName(rs.getString("name"));
                o.setId(new String[]{"" + rs.getInt("objective_id")});
                s.setId(new String[]{"" + rs.getInt("subject_id")});
            }

            //=========================================
            id = s.getId();
            s.setName(s.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
            data.setSubject(s);
            id = null;
            id = m.getId();
            m.setName(m.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
            data.setMethod(m);
            id = null;
            id = o.getId();
            o.setName(o.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
            data.setObjective(o);
            id = null;
            id = l.getId();
            l.setName(l.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
            data.setLevel(l);
            id = null;

            rs = stAux.executeQuery("select * from lesson_content where lesson_id = " + lessonid);
            List<String> cid = new ArrayList<>();
            while (rs.next()) {
                cid.add(rs.getString("content_id"));

            }

            data.setContentid(cid);

            rs = stAux.executeQuery("select student_id from lesson_stud_att where lesson_id = " + lessonid);
            while (rs.next()) {
                Students learner = new Students();
                learner.setId_students(rs.getInt("student_id"));
                stud.add(learner);

            }
            con.close();
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            rs = stAux.executeQuery("SELECT FirstName,LastName,StudentID FROM Students ORDER BY LastName DESC");
            // ResultSet rs4 = st.executeQuery(consulta);
            HashMap<String, String> mapStudents = new HashMap<String, String>();
            String first, LastName, studentID;
            while (rs.next()) {
                first = rs.getString("FirstName");
                LastName = rs.getString("LastName");
                studentID = rs.getString("StudentID");
                mapStudents.put(studentID, LastName + ", " + first);
            }

            for (Students y : stud) {
                /* ResultSet rs4 =st2.executeQuery("SELECT FirstName,LastName FROM Students where StudentID = '"+y.getId_students()+"'");
           while(rs4.next())
           {*/
                y.setNombre_students(mapStudents.get("" + y.getId_students()));
                //  }
            }
            data.setStudents(stud);

            /*
        
        
        
             */
            mv.addObject("DatainBBDD", existInProgress_report(lessonid, hsr));
            /*
        
        
        
             */
            mv.addObject("data", data); //TARDA MUCHISMO
            ArrayList<Objective> test = CreateLessonControlador.getObjectives(hsr.getSession(), data.getSubject().getId());
            mv.addObject("objectives", CreateLessonControlador.getObjectives(hsr.getSession(), data.getSubject().getId()));
            mv.addObject("contents", this.getContent(data.getObjective().getId()));
            mv.addObject("subjects", this.getSubjects(data.getLevel().getId()));
            List<Lessons> ideas = new ArrayList();
            mv.addObject("listaAlumnos", Students.getStudents(log)); //tarda muchisimo
            rs = stAux.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels");
            List<Level> grades = new ArrayList();
            Level le = new Level();
            le.setName("Select level");
            String[] aux = new String[1];
            aux[0] = "-1";
            le.setId(aux);
            grades.add(le);
            while (rs.next()) {
                Level x = new Level();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("GradeLevelID");
                x.setId(ids);
                x.setName(rs.getString("GradeLevel"));
                grades.add(x);
            }
            con.close();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            
            rs = stAux.executeQuery("SELECT * FROM public.method");
            List<Method> methods = new ArrayList();
            Method me = new Method();
            me.setName("Select Method");
            methods.add(me);
            while (rs.next()) {
                Method x = new Method();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("id");
                x.setId(ids);
                x.setName(rs.getString("name"));
                x.setDescription(rs.getString("description"));
                methods.add(x);
            }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods", methods);
            //get lesson ideas
            rs = stAux.executeQuery("SELECT * FROM public.lessons where idea = true");
            Lessons d = new Lessons();
            d.setName("Select an idea");
            ideas.add(d);
            while (rs.next()) {
                Lessons idea = new Lessons();
                idea.setId(rs.getInt("id"));
                idea.setName(rs.getString("name"));
                ideas.add(idea);
            }
            mv.addObject("ideas", ideas);
            mv.addObject("id", lessonid);
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        finally {
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

    public ArrayList<Subject> getSubjects(String[] levelid) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
            
        ArrayList<Subject> subjects = new ArrayList<>();
        ArrayList<Subject> activesubjects = new ArrayList<>();
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            
            rs = stAux.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID =" + levelid[0] + ")");
            Subject s = new Subject();
            s.setName("Select Subject");
            String[] aux = new String[1];
            aux[0] = "-1";
            s.setId(aux);
            subjects.add(s);

            while (rs.next()) {
                Subject sub = new Subject();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("CourseID");
                sub.setId(ids);

                subjects.add(new Subject(sub));
            }
            aux = new String[1];
            aux[0] = "-1";
            Subject sub = new Subject();
            sub.setId(aux);
            sub.setName("Select subject");
            activesubjects.add(sub);
            for (Subject su : subjects.subList(1, subjects.size())) {
                String[] ids = new String[1];
                ids = su.getId();
                rs = stAux.executeQuery("select Title,Active from Courses where reportcard = 1 and CourseID = '" + ids[0] + "' order by Title");
                while (rs.next()) {
                    if (rs.getBoolean("Active") == true) {
                        su.setName(rs.getString("Title"));
                        activesubjects.add(new Subject(su));
                    }
                }
            }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EditLessonControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(EditLessonControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        return activesubjects;
    }

    public ArrayList<Content> getContent(String[] objectiveid) throws SQLException {
        ArrayList<Content> contents = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            
            rs = stAux.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id =" + objectiveid[0] + ")");

            while (rs.next()) {
                Content eq = new Content();
                String[] id = new String[1];
                id[0] = "" + rs.getInt("id");

                eq.setId(id);
                eq.setName(rs.getString("name"));
                contents.add(eq);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(EditLessonControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(EditLessonControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }finally {
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
        return contents;
    }

    @RequestMapping("/editlesson/studentlistLevel.htm")
    public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("editlesson");
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

    @RequestMapping("/editlesson/subjectlistLevel.htm")
    @ResponseBody
    public String subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return (new Gson()).toJson(this.getSubjects(hsr.getParameterValues("seleccion1")));
    }

    @RequestMapping("/editlesson/objectivelistSubject.htm")
    @ResponseBody
    public String objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return (new Gson()).toJson(CreateLessonControlador.getObjectives(hsr.getSession(), hsr.getParameterValues("seleccion2")));
    }

    @RequestMapping("/editlesson/contentlistObjective.htm")
    @ResponseBody
    public String contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return (new Gson()).toJson(this.getContent(hsr.getParameterValues("seleccion3")));
    }

    @RequestMapping("/editlesson/save.htm")
    public ModelAndView save(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        Lessons newlesson = new Lessons();
        try {
            String id = hsr.getParameter("id");
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            List<String> contentids;
            Subject subject = new Subject();
            Objective objective = new Objective();
            Level level = new Level();
            level.setName(hsr.getParameter("TXTlevel"));
            level.setId(hsr.getParameterValues("TXTlevel"));
            subject.setName(hsr.getParameter("TXTsubject"));
            subject.setId(hsr.getParameterValues("TXTsubject"));
            objective.setName(hsr.getParameter("TXTobjective"));
            objective.setId(hsr.getParameterValues("TXTobjective"));
            String[] test = hsr.getParameterValues("TXTcontent");

            String lvlName = hsr.getParameter("levelName");
            String subjectName = hsr.getParameter("subjectName");
            String objName = hsr.getParameter("objectiveName");
            String studNames = hsr.getParameter("studentsName");

            String note = " name: " + hsr.getParameter("TXTnombreLessons") + " | level: " + lvlName + " | subject: " + subjectName + " | objective: " + objName;
            note += " | Date: " + hsr.getParameter("TXTfecha") + " | start: " + hsr.getParameter("TXThorainicio") + " | finish: " + hsr.getParameter("TXThorafin");

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
            // String[] ideaCheck = hsr.getParameterValues("ideaCheck");

            newlesson.setTeacherid(user.getId());
            newlesson.setId(Integer.parseInt(id));
            newlesson.setLevel(level);
            newlesson.setSubject(subject);
            newlesson.setObjective(objective);

//       String test = hsr.getParameter("TXTloadtemplates");
//       if(test != null)
//       {        
//       newlesson.setName(hsr.getParameter("lessons"));
//       newlesson.setTemplate(true);
//       String x = hsr.getParameter("lessons");
//       newlesson.setId(Integer.parseInt(hsr.getParameter("lessons")));
//       }
//       else
//       {
            newlesson.setName(hsr.getParameter("TXTnombreLessons"));

            //  }
            Updatelesson c = new Updatelesson(hsr.getServletContext());
            // gives a null pointer exception , need to fix  
//     if(ideaCheck!= null){
//       if(ideaCheck[0].equals("on")){
//           c.updateidea(newlesson);
//       }}
//       else{
            java.sql.Timestamp timestampstart = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha") + " " + hsr.getParameter("TXThorainicio") + ":00.000");
            java.sql.Timestamp timestampend = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha") + " " + hsr.getParameter("TXThorafin") + ":00.000");
            String[] studentIds = hsr.getParameterValues("destino[]");

            newlesson.setStart("" + timestampstart);
            newlesson.setFinish("" + timestampend);
            c.updatelesson(hsr, note, studNames, studentIds, newlesson);
//       }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        String message = "Presentation updated";

        ModelAndView mv = new ModelAndView("redirect:/editlesson/start.htm?LessonsSelected=" + newlesson.getId(), "message", message);

        return mv;

    }

    @RequestMapping("/editlesson/copyfromIdea.htm")
    @ResponseBody
    public String copyfromIdea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("editlesson");

        JSONObject json = new JSONObject();
        String[] lessonplanid = hsr.getParameterValues("seleccionidea");
        Subject sub = new Subject();
        Objective obj = new Objective();
        Method meth = new Method();
        String comment = "";
        Level lev = new Level();
        int levelid = 0;
        List<String> contents = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            
            String[] oid = new String[1];
            String[] sid = new String[1];
            String[] mid = new String[1];
            String[] cid = new String[1];

            String consulta = "SELECT objective_id,subject_id,level_id,method_id,comments FROM public.lessons where id =" + lessonplanid[0];
            rs = stAux.executeQuery(consulta);

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

            rs = stAux.executeQuery("select content_id from public.lesson_content where lesson_id = " + lessonplanid[0]);

            while (rs.next()) {
                //  Content eq = new Content();
                String ids = null;
                ids = "" + rs.getInt("content_id");

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
        
        String hi = json.toString();
        String[] ids = new String[1];
        ids[0] = "" + levelid;
        json.put("objectiveslist", new Gson().toJson(CreateLessonControlador.getObjectives(hsr.getSession(), sub.getId())));
        json.put("contentslist", new Gson().toJson(this.getContent(obj.getId())));

        json.put("subjectslist", new Gson().toJson(this.getSubjects(ids)));

        return json.toString();

    }
}
