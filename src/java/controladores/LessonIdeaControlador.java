/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import Montessori.Level;
import atg.taglib.json.util.JSONException;
import com.google.gson.*;

import atg.taglib.json.util.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.beans.PropertyVetoException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
@Controller
public class LessonIdeaControlador {

    static Logger log = Logger.getLogger(LessonIdeaControlador.class.getName());

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    @RequestMapping("/lessonidea/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        ModelAndView mv = new ModelAndView("lessonidea");
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();

            rs = stAux.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels");
            List<Level> grades = new ArrayList();
            Level l = new Level();
            String[] ids = new String[1];
            ids[0] = "-1";
            l.setName("Select level");
            l.setId(ids);
            grades.add(l);
            while (rs.next()) {
                Level x = new Level();
                ids = new String[1];
                ids[0] = "" + rs.getInt("GradeLevelID");
                x.setId(ids);
                x.setName(rs.getString("GradeLevel"));
                grades.add(x);
            }
            mv.addObject("levels", grades);
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

    @RequestMapping("/lessonidea/loadtree.htm")
    @ResponseBody
    public String loadtree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("lessonidea");
        JSONObject json = new JSONObject();
        ArrayList<DBRecords> lessons = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> objectives = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String[] lessonid = hsr.getParameterValues("seleccion1");
            rs = stAux.executeQuery("SELECT lessons.id,lessons.subject_id,lessons.objective_id,objective.name as obj,lessons.name FROM lessons inner join objective on lessons.objective_id = objective.id where lessons.level_id= " + lessonid[0] + " and lessons.idea = true ");
            while (rs.next()) {
                DBRecords l = new DBRecords();
                l.setCol1("" + rs.getInt("id"));
                l.setCol2(rs.getString("name"));
                l.setCol4(rs.getString("obj"));
                l.setCol3("" + rs.getInt("subject_id"));
                if (!objectives.contains(rs.getString("obj"))) {
                    objectives.add(rs.getString("obj"));
                }
                lessons.add(l);

            }
            for (DBRecords x : lessons) {
                Subject s = new Subject();
                String id = null;
                id = x.getCol3();
                x.setCol3(s.fetchName(Integer.parseInt(id), hsr.getServletContext()));
                if (!subjects.contains(x.getCol3())) {
                    subjects.add(x.getCol3());
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
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

        String test = new Gson().toJson(lessons);
        Tree tree = new Tree();
        Node<String> rootNode = new Node<String>("root", "A", " {\"disabled\":true}");
        int i = 0;
        int z = 0;
        for (String x : subjects) {

            Node<String> nodeC = new Node<String>(x, "L" + i, " {\"disabled\":true}");
            rootNode.addChild(nodeC);
            i++;
            for (String y : objectives) {

                for (DBRecords l : lessons) {
                    if (l.getCol3().equalsIgnoreCase(x) && l.getCol4().equalsIgnoreCase(y)) {
                        Node<String> nodeA = new Node<String>(y, "C" + z, " {\"disabled\":true}");
                        nodeC.addChild(nodeA);
                        z++;
                        for (DBRecords k : lessons) {
                            if (k.getCol4().equalsIgnoreCase(y)) {
                                Node<String> nodeB = new Node<String>(k.getCol2(), k.getCol1(), " {\"disabled\":false}");
                                nodeA.addChild(nodeB);
                            }
                        }
                        break;
                    }
                }
            }

        }

        tree.setRootElement(rootNode);
        String test2 = this.generateJSONfromTree(tree);
        return test2;
    }

    public String generateJSONfromTree(Tree tree) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // buffer to write to string later
        JsonGenerator generator = factory.createJsonGenerator(out, JsonEncoding.UTF8);

        ObjectNode rootNode = generateJSON(tree.getRootElement(), mapper.createObjectNode());
        mapper.writeTree(generator, rootNode);

        return out.toString();
    }

    public ObjectNode generateJSON(Node<String> node, ObjectNode obN) throws JSONException {
        if (node == null) {
            return obN;
        }

        obN.put("text", node.getData());
        obN.put("id", node.getId());
        obN.put("state", node.getState());
        JSONObject j = new JSONObject();
//        j.put("opened",true);
//        j.put("disabled",false);
//        obN.put("state",j.toString());
        ArrayNode childN = obN.arrayNode();
        obN.set("children", childN);
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            return obN;
        }

        Iterator<Node<String>> it = node.getChildren().iterator();
        while (it.hasNext()) {
            childN.add(generateJSON(it.next(), new ObjectMapper().createObjectNode()));
        }
        return obN;
    }

    @RequestMapping("/editlessonidea.htm")
    public ModelAndView editlessonidea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("editlessonidea");
        String lessonid = hsr.getParameter("LessonsSelected");
        Lessons data = new Lessons();
        Level l = new Level();
        ArrayList<Content> c = new ArrayList<>();
        //   ArrayList<Students> stud = new ArrayList<>();
        Objective o = new Objective();
        Subject s = new Subject();
        Method m = new Method();
        String[] id = new String[1];

        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();

            con = pool_local.getConnection();
            stAux = con.createStatement();
            rs = stAux.executeQuery("select * from lessons where id= " + lessonid);
            while (rs.next()) {
                data.setComments(rs.getString("comments"));

                data.setId(Integer.parseInt(lessonid));
                l.setId(new String[]{"" + rs.getInt("level_id")});
                m.setId(new String[]{"" + rs.getInt("method_id")});
                data.setName(rs.getString("name"));
                o.setId(new String[]{"" + rs.getInt("objective_id")});
                s.setId(new String[]{"" + rs.getInt("subject_id")});

            }
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
            mv.addObject("data", data);
            ArrayList<Objective> test = this.getObjectives(data.getSubject().getId());
            mv.addObject("objectives", this.getObjectives(data.getSubject().getId()));
            mv.addObject("contents", this.getContent(data.getObjective().getId()));
            mv.addObject("subjects", this.getSubjects(data.getLevel().getId()));
            List<Lessons> ideas = new ArrayList();

            con.close();

            con = pool_renweb.getConnection();
            stAux = con.createStatement();

            rs = stAux.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels");
            List<Level> grades = new ArrayList();
            Level le = new Level();
            le.setName("Select level");
            String[] ids = new String[1];
            ids[0] = "-1";
            le.setId(ids);
            grades.add(le);
            while (rs.next()) {
                Level x = new Level();
                ids = new String[1];
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
            ids = new String[1];
            ids[0] = "-1";
            me.setId(ids);
            methods.add(me);
            while (rs.next()) {
                Method x = new Method();
                ids = new String[1];
                ids[0] = "" + rs.getInt("id");
                x.setId(ids);
                x.setName(rs.getString("name"));
                x.setDescription(rs.getString("description"));
                methods.add(x);
            }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods", methods);
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

    @RequestMapping("/lessonidea/deletetree.htm")
    @ResponseBody
    public String deletetree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("redirect:/lessonidea/start.htm");
        String lessonid = hsr.getParameter("selected");
        JSONObject jsonObj = new JSONObject();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String message = null;
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            String consulta = "DELETE FROM lesson_content WHERE lesson_id=" + lessonid;
            stAux.executeUpdate(consulta);
            consulta = "DELETE FROM public.lessons WHERE id=" + lessonid;
            stAux.executeUpdate(consulta);
            message = "Presentation deleted successfully";
            jsonObj.put("message", message);
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
        return jsonObj.toString();
    }

    public ArrayList<Subject> getSubjects(String[] levelid) throws SQLException {

        ArrayList<Subject> subjects = new ArrayList<>();
        ArrayList<Subject> activesubjects = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();

            rs = stAux.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID =" + levelid[0] + ")");
            Subject s = new Subject();
            s.setName("Select Subject");
            String[] ids = new String[1];
            ids[0] = "-1";

            s.setId(ids);
            subjects.add(s);
            activesubjects.add(new Subject(s));
            while (rs.next()) {
                Subject sub = new Subject();
                ids = new String[1];
                ids[0] = "" + rs.getInt("CourseID");
                sub.setId(ids);

                subjects.add(sub);
            }
            for (Subject su : subjects.subList(1, subjects.size())) {
                ids = new String[1];
                ids = su.getId();
                rs = stAux.executeQuery("select Title,Active from Courses where reportcard = 1 and CourseID = '" + ids[0] + "' order by Title");
                while (rs.next()) {
                    if (rs.getBoolean("Active") == true) {
                        su.setName(rs.getString("Title"));
                        activesubjects.add(su);
                    }
                }
            }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LessonIdeaControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(LessonIdeaControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

    public ArrayList<Objective> getObjectives(String[] subjectid) throws SQLException {
        ArrayList<Objective> objectives = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String[] ids = new String[1];
            Objective sub = new Objective();
            ids[0] = "-1";
            sub.setId(ids);
            sub.setName("Select objective");
            objectives.add(sub);

            rs = stAux.executeQuery("select name,id from public.objective where subject_id='" + subjectid[0] + "' order by name");
//          Objective s = new Objective();
//          s.setName("Select Objective");
//          objectives.add(s);

            while (rs.next()) {
                ids = new String[1];
                sub = new Objective();
                ids[0] = "" + rs.getInt("id");
                sub.setId(ids);
                sub.setName(rs.getString("name"));
                objectives.add(sub);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Objectives: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LessonIdeaControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(LessonIdeaControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        return objectives;
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
            java.util.logging.Logger.getLogger(LessonIdeaControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(LessonIdeaControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        return contents;
    }

    @RequestMapping("/savelessonidea.htm")
    public ModelAndView savelessonidea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        String id = hsr.getParameter("lessonid");
        String message = "Idea Updated";
        ModelAndView mv = new ModelAndView("redirect:/editlessonidea.htm?LessonsSelected=" + id, "message", message);
        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            Lessons newlesson = new Lessons();
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
            newlesson.setId(Integer.parseInt(id));
            newlesson.setLevel(level);
            newlesson.setSubject(subject);
            newlesson.setObjective(objective);

            newlesson.setName(hsr.getParameter("TXTnombreLessons"));
            Updatelesson c = new Updatelesson(hsr.getServletContext());
            c.updateidea(newlesson);
        } catch (Exception ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return mv;
    }

    @RequestMapping("/editlessonidea/subjectlistLevel.htm")
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("editlessonidea");

        mv.addObject("subjects", this.getSubjects(hsr.getParameterValues("seleccion1")));

        return mv;
    }

    @RequestMapping("/editlessonidea/objectivelistSubject.htm")
    public ModelAndView objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("editlessonidea");
        //    mv.addObject("templatessubsection", hsr.getParameter("seleccion2"));
        mv.addObject("objectives", this.getObjectives(hsr.getParameterValues("seleccion2")));

        return mv;
    }

    @RequestMapping("/editlessonidea/contentlistObjective.htm")
    public ModelAndView contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("editlessonidea");
        mv.addObject("contents", this.getContent(hsr.getParameterValues("seleccion3")));

        return mv;
    }
}
