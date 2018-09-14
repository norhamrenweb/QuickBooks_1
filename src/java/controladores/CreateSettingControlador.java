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
import java.util.*;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.google.gson.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nmohamed
 */
@Controller
public class CreateSettingControlador {

    static Logger log = Logger.getLogger(CreateSettingControlador.class.getName());

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    @RequestMapping("/createsetting/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        ModelAndView mv = new ModelAndView("createsettings");

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            rs = stAux.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels order by GradeLevel ASC ");
            List<Level> grades = new ArrayList();
            Level l = new Level();
            l.setName("Select level");
            String[] y = new String[1];
            y[0] = "?";
            l.setId(y);
            grades.add(l);
            while (rs.next()) {
                Level x = new Level();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("GradeLevelID");
                x.setId(ids);
                x.setName(rs.getString("GradeLevel"));
                grades.add(x);
            }
            con.close();
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();

            rs = stAux.executeQuery("SELECT * FROM public.method");
            List<Method> methods = new ArrayList();
            while (rs.next()) {
                Method x = new Method();
                String[] ids = new String[1];
                ids[0] = "" + rs.getInt("id");
                x.setId(ids);
                x.setName(rs.getString("name"));
                x.setDescription(rs.getString("description"));
                methods.add(x);
            }

            mv.addObject("methods", methods);
            mv.addObject("gradelevels", grades);
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

    @RequestMapping("/createsetting/subjectlistLevel.htm")
    @ResponseBody
    public String subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        List<Subject> subjects = new ArrayList<>();
        List<Subject> activesubjects = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            String[] levelid = new String[1];
            levelid = hsr.getParameterValues("seleccion1");
            ResultSet rs1 = stAux.executeQuery("select title,courseid from courses where active = 1 and reportcard = 1 and courseid in (select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID = " + levelid[0] + "))order by title asc");
            //ResultSet rs1 = DBConect.ah.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID =" + levelid[0] + ")");
            while (rs1.next()) {
                Subject sub = new Subject();
                String[] ids = new String[1];
                ids[0] = "" + rs1.getInt("courseid");
                sub.setId(ids);
                sub.setName(rs1.getString("title"));
                subjects.add(sub);
            }
//            for (Subject su : subjects.subList(1, subjects.size())) {
//                String[] ids = new String[1];
//                ids = su.getId();
//                ResultSet rs2 = DBConect.ah.executeQuery("select Title,Active from Courses where CourseID = " + ids[0]+" order by Title ASC");
//                while (rs2.next()) {
//                    if (rs2.getBoolean("Active") == true) {
//                        su.setName(rs2.getString("Title"));
//                        activesubjects.add(su);
//                    }
//
//                }
//            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
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

        // return (new Gson()).toJson(activesubjects);
        return (new Gson()).toJson(subjects);
    }

    @RequestMapping("/createsetting/objectivelistSubject.htm")
    @ResponseBody
    public String objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        List<Objective> objectives = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String subjectid = null;
            subjectid = hsr.getParameter("seleccion2");
            String consulta = "select name,id,description from public.objective "
                    + "where subject_id=" + subjectid
                    //                    + " and year_id=" + hsr.getSession().getAttribute("yearId")
                    //                    + " and term_id=" + hsr.getSession().getAttribute("termId")
                    + "order by name";
            rs = stAux.executeQuery(consulta);
            while (rs.next()) {
                String[] ids = new String[1];
                Objective sub = new Objective();
                ids[0] = "" + rs.getInt("id");
                sub.setId(ids);
                sub.setName(rs.getString("name"));
                sub.setDescription(rs.getString("description"));
                objectives.add(sub);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Objectives: " + ex);
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

        return (new Gson()).toJson(objectives);
    }

    @RequestMapping(value = "/createsetting/contentlistObjective.htm")
    @ResponseBody
    public String contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        List<Content> contents = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        JSONObject obj = new JSONObject();
        Objective objective = new Objective();

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String objectiveid = hsr.getParameter("seleccion3");
            rs = stAux.executeQuery("SELECT name,id,description FROM content where content.id IN (select objective_content.content_id from objective_content where objective_content.objective_id = '" + objectiveid + "') order by name");

            while (rs.next()) {
                String[] ids = new String[1];
                Content eq = new Content();
                ids[0] = "" + rs.getInt("id");
                eq.setId(ids);
                eq.setName(rs.getString("name"));
                eq.setDescription(rs.getString("description"));
                contents.add(eq);
            }
            String id = hsr.getParameter("seleccion3");
            rs = stAux.executeQuery("SELECT reportcard,name,description,term_id FROM public.objective where id =" + id);

            while (rs.next()) {
                objective.setName(rs.getString("name"));
                objective.setDescription(rs.getString("description"));
                objective.setFinalrating(rs.getString("term_id"));
                objective.setReportCard(rs.getBoolean("reportcard"));
            }

            rs = stAux.executeQuery("SELECT count(id) as nooflessons FROM public.lessons where objective_id =" + id);

            while (rs.next()) {
                objective.setNooflessonsplanned(rs.getInt("nooflessons"));
            }
            ArrayList<Step> steps = new ArrayList<>();
            rs = stAux.executeQuery("SELECT name,id,storder FROM public.obj_steps where obj_id ='" + id + "' order by storder");

            while (rs.next()) {
                Step step = new Step();
                step.setName(rs.getString("name"));
                String[] stid = new String[1];
                step.setId("" + rs.getInt("id"));
                step.setOrder(rs.getInt("storder"));
                steps.add(step);
            }
            objective.setSteps(steps);
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
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
        String contentjson = new Gson().toJson(contents);
        String objjson = new Gson().toJson(objective);
        obj.put("content", contentjson);
        obj.put("objective", objjson);
        return obj.toString();
    }

    //saving the edited objective and loading the new objectives list
    @RequestMapping(value = "/createsetting/editObjective.htm")
    @ResponseBody
    public String editObjective(@RequestBody Objective obj, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        List<Objective> objectives = new ArrayList<>();
        String[] sid = hsr.getParameterValues("sid");
        String termIds = obj.getFinalrating();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        String message = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String[] obid = obj.getId();
            String name, descrip;
            name = obj.getName().replace("'", "\'\'");
            name = name.replace("\"", "\"\"");

            descrip = obj.getDescription().replace("'", "\'\'");
            descrip = descrip.replace("\"", "\"\"");

            String consulta = "update objective set term_id = '" + termIds + "' ,name = '" + name + "',description ='" + descrip + "'where id =" + obid[0];
            stAux.executeUpdate(consulta);
            message = "Objective edited successfully";
            Step s = new Step();

            s.compareandupdate(obj.getSteps(), obj.getId(), hsr.getServletContext());
            rs = stAux.executeQuery("select * from objective where subject_id = " + sid[0]);
            while (rs.next()) {
                Objective o = new Objective();
                o.setDescription(rs.getString("description"));
                String[] id = new String[1];
                id[0] = "" + rs.getInt("id");
                o.setId(id);
                o.setName(rs.getString("name"));
                objectives.add(o);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message = "Something went wrong";
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
        String objjson = new Gson().toJson(objectives);
        return objjson;
    }

    @RequestMapping(value = "/createsetting/addObjective.htm")
    @ResponseBody
    public String addObjective(@RequestBody Objective ob, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String message = null;
        Objective o = new Objective();
        String termIds = ob.getFinalrating();
        String[] subid = ob.getId();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        String yearId = "" + subid[1];
        //   termIds = termIds.split("#")[0];
        //  termIds = termIds;
        boolean reportCard = ob.isReportCard();

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "insert into objective(name,description,subject_id,year_id,term_id,reportcard) values('"
                    + ob.getName() + "','" + ob.getDescription() + "','" + subid[0] + "','" + yearId
                    + "','" + termIds + "','" + reportCard + "')";
            stAux.executeUpdate(consulta, Statement.RETURN_GENERATED_KEYS);
            rs = stAux.getGeneratedKeys();
            String[] id = new String[1];
            while (rs.next()) {
                id[0] = "" + rs.getInt(1);
                o.setId(id);
            }
            List<Step> steps = ob.getSteps();
            for (Step s : steps) {
                stAux.executeUpdate("insert into obj_steps(name,weight,storder,obj_id) values('" + s.getName() + "',0," + s.getOrder() + ",'" + id[0] + "')");
            }
            message = "Objective added successfully";
            o.setDescription(ob.getDescription());
            o.setName(ob.getName());
            o.setSteps(ob.getSteps());

            //SELECT * FROM "public".progress_report;
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message = "Something went wrong";
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
        String objjson = new Gson().toJson(o);
        return objjson;
    }

    @RequestMapping(value = "/createsetting/delObjective.htm")
    @ResponseBody
    public String delObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        String message = null;
        String[] id = hsr.getParameterValues("id");
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String consulta = "select name from lessons where objective_id = " + id[0];
            rs = stAux.executeQuery(consulta);
            if (rs.next()) {
                message = "This objective is linked to lessons and can not be deleted";
            } else {
                consulta = "delete from objective_content where objective_id= '" + id[0] + "'";
                stAux.executeUpdate(consulta);
                consulta = "delete from progress_report where objective_id= '" + id[0] + "'";//incase the objective has a general comment that is not linked to a lesson
                stAux.executeUpdate(consulta);
                consulta = "delete from obj_steps where obj_id= '" + id[0] + "'";//incase the objective has steps 
                stAux.executeUpdate(consulta);
                consulta = "DELETE FROM public.objective WHERE id=" + id[0];
                stAux.executeUpdate(consulta);
                message = "success";
            }
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
        return message;
    }

    @RequestMapping(value = "/createsetting/addContent.htm")
    @ResponseBody
    public String addContent(@RequestBody Content cont, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String[] oid = cont.getId();
        String message = null;
        Content c = new Content();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "insert into content(name,description) values('" + cont.getName() + "','" + cont.getDescription() + "')";
            stAux.executeUpdate(consulta, Statement.RETURN_GENERATED_KEYS);
            rs = stAux.getGeneratedKeys();
            String[] id = new String[1];
            while (rs.next()) {

                id[0] = "" + rs.getInt(1);
                c.setId(id);
            }

            c.setDescription(cont.getDescription());
            c.setName(cont.getName());
            consulta = "insert into objective_content(content_id,objective_id) values('" + id[0] + "','" + oid[0] + "')";
            stAux.executeUpdate(consulta);
            message = "Content added successfully";
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message = "Something went wrong";
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
        String cjson = new Gson().toJson(c);
        return cjson;
    }

    @RequestMapping(value = "/createsetting/editContent.htm")
    @ResponseBody
    public String editContent(@RequestBody Content cont, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        List<Content> contents = new ArrayList<>();
        String[] oid = hsr.getParameterValues("oid");
        String message = null;
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String[] cid = cont.getId();
            String consulta = "update content set name = '" + cont.getName() + "',description ='" + cont.getDescription() + "'where id =" + cid[0];
            stAux.executeUpdate(consulta);
            message = "Content edited successfully";
            ResultSet rs1 = stAux.executeQuery("SELECT name,id,description FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id = " + oid[0] + ")");

            while (rs1.next()) {
                String[] ids = new String[1];
                Content eq = new Content();
                ids[0] = String.valueOf(rs1.getInt("id"));
                eq.setId(ids);
                eq.setName(rs1.getString("name"));
                eq.setDescription(rs1.getString("description"));
                contents.add(eq);
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message = "Something went wrong";
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
        String cjson = new Gson().toJson(contents);

        return cjson;
    }

    @RequestMapping(value = "/createsetting/delContent.htm")
    @ResponseBody
    public String delContent(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        String message = null;
        String[] id = hsr.getParameterValues("id");
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "select lesson_id from lesson_content where content_id = " + id[0];
            rs = stAux.executeQuery(consulta);
            if (rs.next()) {
                message = "This equipment is linked to lessons";
            } else {
                consulta = "DELETE FROM objective_content WHERE content_id=" + id[0];
                stAux.executeUpdate(consulta);
                consulta = "DELETE FROM content WHERE id=" + id[0];
                stAux.executeUpdate(consulta);
                message = "success";

            }
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            message = "Something went wrong";
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

        return message;
    }

    @RequestMapping(value = "/createsetting/delMethod.htm")
    @ResponseBody
    public String delMethod(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        String message = null;
        String[] id = hsr.getParameterValues("id");
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String consulta = "select id from lessons where method_id = " + id[0];
            rs = stAux.executeQuery(consulta);
            if (rs.next()) {
                message = "This method is linked to lessons";
            } else {
                consulta = "DELETE FROM method WHERE id=" + id[0];
                stAux.executeUpdate(consulta);
                message = "success";

            }
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            message = "Something went wrong";
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

        return message;
    }

    @RequestMapping(value = "/createsetting/addMethod.htm")
    @ResponseBody
    public String addMethod(@RequestBody Method met, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        String message = null;
        Method m = new Method();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            String consulta = "insert into method(name,description) values('" + met.getName() + "','" + met.getDescription() + "')";
            stAux.executeUpdate(consulta, Statement.RETURN_GENERATED_KEYS);
            rs = stAux.getGeneratedKeys();

            while (rs.next()) {
                String[] id = new String[1];
                id[0] = "" + rs.getInt(1);
                m.setId(id);
            }
            message = "Method added successfully";
            m.setDescription(met.getDescription());
            m.setName(met.getName());

        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message = "Something went wrong";
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
        String objjson = new Gson().toJson(m);

        return objjson;
    }

    @RequestMapping(value = "/createsetting/editMethod.htm")
    @ResponseBody
    public String editMethod(@RequestBody Method met, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        List<Method> methods = new ArrayList<>();
        //   JSONObject obj = new JSONObject();
        String[] mid = met.getId();
//       JSONObject jsonObj = new JSONObject(hi[0]);
        String message = null;
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;

        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();

            String consulta = "update method set name = '" + met.getName() + "',description ='" + met.getDescription() + "'where id =" + mid[0];
            stAux.executeUpdate(consulta);
            message = "Objective edited successfully";
            rs = stAux.executeQuery("select * from method ");
            while (rs.next()) {
                Method m = new Method();
                m.setDescription(rs.getString("description"));
                String[] id = new String[1];
                id[0] = "" + rs.getInt("id");
                m.setId(id);
                m.setName(rs.getString("name"));
                methods.add(m);
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message = "Something went wrong";
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
        String objjson = new Gson().toJson(methods);
        return objjson;
    }

}
