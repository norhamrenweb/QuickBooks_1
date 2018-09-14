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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
@Controller
public class SOWTreeControlador {

    static Logger log = Logger.getLogger(SOWTreeControlador.class.getName());

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    @RequestMapping("/sowdisplay/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("sowdisplay");
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            rs = stAux.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels");
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

    @RequestMapping("/sowdisplay/loadtree.htm")
    @ResponseBody
    public String loadtree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("sowdisplay");
        JSONObject json = new JSONObject();
        ArrayList<DBRecords> steps = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> objectives = new ArrayList<>();
        Tree tree = new Tree();

        Connection conRW = null, conLocal = null;
        ResultSet rs = null;
        Statement stAux = null;

        Node<String> rootNode = new Node<String>("Subjects", "A", " {\"disabled\":true}");
        try {

            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();

            conRW = pool_renweb.getConnection();
            conLocal = pool_local.getConnection();

            stAux = conRW.createStatement();
            String idHash;
            String consulta = "SELECT Title,Active,CourseID FROM Courses", name;
            rs = stAux.executeQuery(consulta);
            HashMap<String, String> mapSubject = new HashMap<String, String>();

            while (rs.next()) {
                if (rs.getBoolean("Active")) {
                    name = rs.getString("Title");
                    idHash = "" + rs.getInt("CourseID");
                    mapSubject.put(idHash, name);
                }
            }
            String[] levelid = hsr.getParameterValues("seleccion1");
            //AQUI TARDA!!
            //ArrayList<Subject> subs = this.getSubjects(levelid);
            ArrayList<Subject> subs = this.getSubjectsHashMap(mapSubject, levelid);
//////

            stAux = conLocal.createStatement();

            for (Subject sub : subs) {
                String[] sid = sub.getId();
                consulta = "select obj_steps.id,obj_steps.name,objective.name as "
                        + "obj ,objective.subject_id from obj_steps inner join objective "
                        + "on obj_steps.obj_id = objective.id "
                        + "where objective.subject_id = '" + sid[0] + "' ";//need to think how we will apply the term in the objective
//                                + "and objective.year_id="+hsr.getSession().getAttribute("yearId")
//                                +" and objective.term_id="+hsr.getSession().getAttribute("termId");
                rs = stAux.executeQuery(consulta);

                while (rs.next()) {
                    DBRecords l = new DBRecords();
                    l.setCol1("" + rs.getInt("id"));
                    l.setCol2(rs.getString("name"));
                    l.setCol4(rs.getString("obj"));
                    l.setCol3("" + rs.getInt("subject_id"));
                    if (!objectives.contains(rs.getString("obj"))) {
                        objectives.add(rs.getString("obj"));
                    }
                    steps.add(l);

                }
            }

////////////////////// AQUI ES DONDE TARDA
            for (DBRecords x : steps) {
                Subject s = new Subject();
                String id = null;
                id = x.getCol3();
                //   x.setCol3(s.fetchName(Integer.parseInt(id), hsr.getServletContext()));
                x.setCol3(mapSubject.get(id));
                if (!subjects.contains(x.getCol3())) {
                    subjects.add(x.getCol3());
                }
            }

            String test = new Gson().toJson(steps);
////////////////////////////////////////////////////////////
            int i = 0;
            int z = 0;

            Collections.sort(subs, new Comparator<Subject>() {
                @Override
                public int compare(Subject o1, Subject o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            Collections.sort(steps, new Comparator<DBRecords>() {
                @Override
                public int compare(DBRecords o1, DBRecords o2) {
                    return o1.getCol2().compareTo(o2.getCol2());
                }
            });
            for (Subject x : subs)//subjects)
            {

                Node<String> nodeC = new Node<String>(x.getName(), "L" + i, " {\"disabled\":true}");
                rootNode.addChild(nodeC);
                i++;
                ArrayList<Objective> obj = this.getObjectives(hsr.getSession(), x.getId());
                for (Objective y : obj) {

                    Node<String> nodeA = new Node<String>(y.getName(), "C" + z, " {\"disabled\":true}");
                    nodeC.addChild(nodeA);
                    z++;
                    for (DBRecords l : steps) {

                        //match the subject with the objective
                        if (l.getCol3().equalsIgnoreCase(x.getName()) && l.getCol4().equalsIgnoreCase(y.getName())) {

                            //match the objective with the step
                            for (DBRecords k : steps) {
                                if (k.getCol4().equalsIgnoreCase(y.getName())) {
                                    Node<String> nodeB = new Node<String>(k.getCol2(), k.getCol1(), " {\"disabled\":false}");

                                    nodeA.addChild(nodeB);
                                }
                            }
                            break;
                        }

                    }
                }

            }
            conLocal.close();
            conRW.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
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

    public ArrayList<Subject> getSubjectsHashMap(HashMap<String, String> mapSubject, String[] levelid) throws SQLException {

        ArrayList<Subject> subjects = new ArrayList<>();
        ArrayList<Subject> activesubjects = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();

            ResultSet rs1 = stAux.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID =" + levelid[0] + ")");
            Subject s = new Subject();
            s.setName("Select Subject");
            subjects.add(s);

            while (rs1.next()) {
                Subject sub = new Subject();
                String[] ids = new String[1];
                ids[0] = "" + rs1.getInt("CourseID");
                sub.setId(ids);

                subjects.add(sub);
            }
            for (Subject su : subjects.subList(1, subjects.size())) {

                String key = "" + su.getId()[0];
                if (mapSubject.containsKey(key)) {
                    su.setName(mapSubject.get(key));
                    activesubjects.add(su);
                }
            }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SOWTreeControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(SOWTreeControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
            subjects.add(s);

            while (rs.next()) {
                Subject sub = new Subject();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("CourseID");
                sub.setId(ids);

                subjects.add(sub);
            }
            for (Subject su : subjects.subList(1, subjects.size())) {
                String[] ids = new String[1];
                ids = su.getId();
                rs = stAux.executeQuery("select Title,Active from Courses where CourseID = '" + ids[0] + "' order by Title");
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
            java.util.logging.Logger.getLogger(SOWTreeControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(SOWTreeControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

    public ArrayList<Objective> getObjectives(HttpSession session, String[] subjectid) throws SQLException {
        ArrayList<Objective> objectives = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance(); 
            con = pool_local.getConnection();
            stAux = con.createStatement();
            rs = stAux.executeQuery("select name,id from public.objective "
                    + "where subject_id=" + subjectid[0]
                    + //                    " and year_id="
                    //                    + session.getAttribute("yearId") +" and term_id="+session.getAttribute("termId")+
                    " ORDER BY name ASC");

            while (rs.next()) {
                String[] ids = new String[1];
                Objective sub = new Objective();
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
            java.util.logging.Logger.getLogger(SOWTreeControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(SOWTreeControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

}
