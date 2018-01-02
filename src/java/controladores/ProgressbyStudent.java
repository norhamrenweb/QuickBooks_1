/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import Montessori.Method;
import Montessori.Objective;
import Montessori.Students;
import Montessori.Subject;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.google.gson.*;
import static controladores.ResourcesControlador.log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author nmohamed
 */
@Controller
//@Scope("session")
public class ProgressbyStudent {

    Connection cn;
    static Logger log = Logger.getLogger(ProgressbyStudent.class.getName());
    private ServletContext servlet;
    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    // loads the levels
    @RequestMapping("/progressbystudent/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("progressbystudent");
        List<Level> grades = new ArrayList();
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSourceAH", hsr.getServletContext());
            this.cn = dataSource.getConnection();
            mv.addObject("listaAlumnos", this.getStudents());
            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");

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
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        mv.addObject("gradelevels", grades);

        return mv;
    }

    public ArrayList<Students> getStudents() throws SQLException {
//        this.conectarOracle();
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        try {

            Statement st = this.cn.createStatement();

            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled' order by lastname";
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setNextlevel("Placement");
                alumnos.setSubstatus("Substatus");
                listaAlumnos.add(alumnos);
            }
            //this.finalize();

        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return listaAlumnos;
    }

    // loads the students based on the selected level
    @RequestMapping("/progressbystudent/studentlistLevel.htm")
    @ResponseBody
    public String studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        ModelAndView mv = new ModelAndView("progressbystudent");

        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource) this.getBean("dataSourceAH", hsr.getServletContext());
        this.cn = dataSource.getConnection();
        List<Students> studentsgrades = new ArrayList();
        String[] levelid = hsr.getParameterValues("seleccion");
        String test = hsr.getParameter("levelStudent");
        studentsgrades = this.getStudentslevel(levelid[0]);
        String data = new Gson().toJson(studentsgrades);
//        mv.addObject("listaAlumnos",data );

        return data;
    }

    public List<Subject> getSubjects(int studentid) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        List<Subject> activesubjects = new ArrayList<>();
        HashMap<String, String> mapSubject = new HashMap<String,String>();
        String termid = null;
        String yearid = null;
        
        try {

            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery("select defaultyearid,defaulttermid from ConfigSchool where configschoolid = 1");
            while(rs.next())
            {
                termid = ""+rs.getInt("defaulttermid");
                yearid = ""+rs.getInt("defaultyearid");
            }
            ResultSet rs1 = st.executeQuery("select distinct courses.courseid, courses.title, courses.active from roster    inner join classes on roster.classid=classes.classid\n" +
"                 inner join courses on courses.courseid=classes.courseid\n" +
"                  where roster.studentid = "+studentid+" and roster.enrolled"+termid+"= 1 and courses.active = 1 and classes.yearid = '"+ yearid+"'");// the term and year need to be dynamic, check with vincent
            Subject first = new Subject();
            first.setName("Select Subject");
            subjects.add(first);
            String name9, id;
            while (rs1.next()) {
                Subject sub = new Subject();
                String[] ids = new String[1];
                ids[0] = "" + rs1.getInt("CourseID");
                sub.setId(ids);
                subjects.add(sub);
               
                    name9 = rs1.getString("Title");
                    id = rs1.getString("CourseID");
                    mapSubject.put(id, name9);
               
            }

            for (Subject s : subjects.subList(1, subjects.size())) {
                String[] ids = new String[1];
                ids = s.getId();
                s.setName(mapSubject.get(ids[0]));
                activesubjects.add(s);
            }
            //loop through subjects to get their names, skipping the first 
          /*  for (Subject s : subjects.subList(1, subjects.size())) {
                String[] ids = new String[1];
                ids = s.getId();
                ResultSet rs2 = st.executeQuery("select Title,Active from Courses where CourseID = " + ids[0]);
                while (rs2.next()) {
                    if (rs2.getBoolean("Active") == true) {
                        s.setName(rs2.getString("Title"));
                        activesubjects.add(s);
                    }
                }
            }*/

        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
               
        return activesubjects;
    }

    //loads list of subjects based on selected level
    @RequestMapping("/progressbystudent/subjectlistLevel.htm")
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("progressdetails");

        String[] levelid = new String[1];
        levelid = hsr.getParameterValues("seleccion1");
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource) this.getBean("dataSourceAH", hsr.getServletContext());
        this.cn = dataSource.getConnection();

//        mv.addObject("subjects", this.getSubjects(levelid[0]));

        return mv;
    }

    public ArrayList<Students> getStudentslevel(String gradeid) throws SQLException {
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        String gradelevel = null;
        try {

            Statement st = this.cn.createStatement();
            ResultSet rs1 = st.executeQuery("select GradeLevel from AH_ZAF.dbo.GradeLevels where GradeLevelID =" + gradeid);
            while (rs1.next()) {
                gradelevel = rs1.getString("GradeLevel");
            }

            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled' and GradeLevel = '" + gradelevel + "'";
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setNextlevel(rs.getString("Placement"));
                alumnos.setSubstatus(rs.getString("Substatus"));
                listaAlumnos.add(alumnos);
            }
            //this.finalize();

        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return listaAlumnos;

    }

    //OTEHER PAGINE
    @RequestMapping("/progressdetails.htm")
    @ResponseBody
    public ModelAndView progressdetails(@RequestBody DBRecords d, HttpServletRequest hsr, HttpServletResponse hsr1, Model model) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("progressdetails");
        Objective o = new Objective();
        //     String[] hi = hsr.getParameterValues("data");
        servlet = hsr.getServletContext();
        // JSONObject jsonObj = new JSONObject(hi[0]);
        List<Progress> progress = new ArrayList<>();
        String finalrating = null;
        String presenteddate = null;
        String attempteddate = null;
        String mastereddate = null;
        List<String> attemptdates = new ArrayList<>();
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSource", hsr.getServletContext());
            this.cn = dataSource.getConnection();

            Statement st = this.cn.createStatement(1004, 1007);
//will display only if there is a lesson that has a progress record,but if a lesson is only planned will not be displayed
            ResultSet rs1 = st.executeQuery("select comment,comment_date,ratingname,lessonname from public.progresslessonname where objective_id=" + d.getCol1() + " AND student_id = " + d.getCol2());
            if (!rs1.next()) {
                String message = "Student does not have progress under the selected objective";
                mv.addObject("message", message);
            } else {
                rs1.beforeFirst();
                while (rs1.next()) {
                    Progress p = new Progress();
                    p.setComment(rs1.getString("comment"));
                    p.setRating(rs1.getString("ratingname"));
                    p.setLesson_name(rs1.getString("lessonname"));
                    Timestamp stamp = rs1.getTimestamp("comment_date");
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = sdfDate.format(stamp);
                    p.setComment_date(dateStr);
                    progress.add(p);
                }
                // store an array of the attempted dates
                for (Progress x : progress) {
                    if (x.getRating().equals("Attempted")) {
                        attemptdates.add(x.getComment_date());
                    }
                }
                //           select the latest rating to be presented as the final rating for this objective
//                String consulta = "SELECT rating.name FROM rating where id in(select rating_id from progress_report where student_id = '" + d.getCol2() + "' AND comment_date = (select max(comment_date)   from public.progress_report where student_id =" + d.getCol2() + " AND objective_id =" + d.getCol1() + " and generalcomment = false) AND objective_id =" + d.getCol1() + "and generalcomment = false )";
//                ResultSet rs2 = st.executeQuery(consulta);
//                while (rs2.next()) {
//                    finalrating = rs2.getString("name");
//                }
                    finalrating = this.getfinalrating(d.getCol1(), d.getCol2());
                String consulta = "select min(comment_date) as date from progress_report where student_id =" + d.getCol2() + " and rating_id in (select id from rating where name = 'Presented') and objective_id =" + d.getCol1();
                ResultSet rs3 = st.executeQuery(consulta);
                if (rs3.next()) {
                    rs3.beforeFirst();
                    while (rs3.next()) {
                        Timestamp stamp = rs3.getTimestamp("date");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                        if (stamp != null) {
                            presenteddate = sdfDate.format(stamp);
                        }

                    }
                }
                consulta = "select min(comment_date) as date from progress_report where student_id =" + d.getCol2() + " and rating_id in (select id from rating where name = 'Attempted') and objective_id =" + d.getCol1();
                ResultSet rs4 = st.executeQuery(consulta);
                if (rs4.next()) {
                    rs4.beforeFirst();
                    while (rs4.next()) {
                        Timestamp stamp = rs4.getTimestamp("date");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                        if (stamp != null) {
                            attempteddate = sdfDate.format(stamp);
                        }
                    }
                }
                consulta = "select min(comment_date) as date from progress_report where student_id =" + d.getCol2() + " and rating_id in (select id from rating where name = 'Mastered') and objective_id =" + d.getCol1();
                ResultSet rs5 = st.executeQuery(consulta);
                if (rs5.next()) {
                    rs5.beforeFirst();
                    while (rs5.next()) {
                        Timestamp stamp = rs5.getTimestamp("date");
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                        if (stamp != null) {
                            mastereddate = sdfDate.format(stamp);
                        }

                    }
                }
                String prog = new Gson().toJson(progress);
                String rating = new Gson().toJson(finalrating);
                JSONObject obj = new JSONObject();
//        obj.put("progress", prog);
//        obj.put("finalrating", rating);
//        obj.put("attempteddate",attempteddate);
//        obj.put("mastereddate",mastereddate);
//        obj.put("presenteddate",presenteddate);
                //return obj.toString();
                // model.addAttribute("holas");
                mv.addObject("progress", progress);
                mv.addObject("finalrating", finalrating);
                mv.addObject("attempteddate", attempteddate);
                mv.addObject("mastereddate", mastereddate);
                mv.addObject("presenteddate", presenteddate);
                mv.addObject("studentname", d.getCol3());
                mv.addObject("gradelevel", d.getCol4());
                mv.addObject("subject", d.getCol5());
                mv.addObject("attempteddates", attemptdates);
                mv.addObject("objective", o.fetchName(Integer.parseInt(d.getCol1()), servlet));
            }
//        mv.addObject(obj);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    //FUNCION NUEVA
    @RequestMapping("/progressbystudent/loadtree.htm")
    @ResponseBody
    public String treeload(HttpServletRequest hsr, HttpServletResponse hsr1) {
        try {
            return this.loadtree(Integer.parseInt(hsr.getParameter("studentid")), hsr.getServletContext());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ProgressbyStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return "";
    }

    //load student demographics
    @RequestMapping("/progressbystudent/studentPage.htm")
    @ResponseBody
    public String studentPage(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        //    ModelAndView mv = new ModelAndView("progressbystudent");
        List<Subject> subjects= new ArrayList<>();
        HashMap<String, String> mapSubject= new HashMap<>();
        
        String[] studentIds = hsr.getParameterValues("selectStudent");
        Students student = new Students();
        JSONObject obj = new JSONObject();
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSourceAH", hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();

            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where StudentID = " + studentIds[0];
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {

                student.setId_students(rs.getInt("StudentID"));
                student.setNombre_students(rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName"));
                student.setFecha_nacimiento(rs.getString("Birthdate"));
                student.setFoto(rs.getString("PathToPicture"));
                student.setLevel_id(rs.getString("GradeLevel"));
                student.setNextlevel(rs.getString("NextGradeLevel"));
//                student.setSubstatus("Substatus");

            }
            //this.finalize();

        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
       // List<Subject> subjects = new ArrayList<>();
        subjects = this.getSubjects(student.getId_students());
        String info = new Gson().toJson(student);
        String sub = new Gson().toJson(subjects);
        obj.put("info", info);
        obj.put("sub", sub);
//    mv.addObject("student",student);

//     mv.addObject("subjects", this.getSubjects(student.getLevel_id()));//Integer.parseInt(alumnos.getLevel_id())));
        obj.put("prog", this.loadtree(student.getId_students(), hsr.getServletContext()));
        return obj.toString();
    }

    //loads list of objectives final rating & general comments based on the selected subject
    @RequestMapping("/progressbystudent/objGeneralcomments.htm")
    @ResponseBody
    public String objGeneralcomments(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String selection = hsr.getParameter("selection");
        String[] data = selection.split(",");
        String subjectid = data[0];
        String studentid = data[1];
//        List<Progress> progress = new ArrayList<>();
//        List<Objective> objectives = new ArrayList<>();
//        List<String> objname = new ArrayList<>();
//        List<String> objdscp = new ArrayList<>();
//        List<String> comment = new ArrayList<>();
//        List<Date> commentdate = new ArrayList<>();
//        List<Integer> objid = new ArrayList<>();
        List<DBRecords> result = new ArrayList<>();
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSource", hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();

//            String consulta = "SELECT * FROM objective where subject_id = "+subjectid;
//            ResultSet rs = st.executeQuery(consulta);
//          String consulta = " SELECT objective.id,objective.name,objective.description,progress_report.comment,progress_report.comment_date FROM progress_report  INNER JOIN objective ON progress_report.objective_id = objective.id where generalcomment = TRUE AND student_id = "+studentid+" AND subject_id = "+subjectid;
//          ResultSet rs = st.executeQuery(consulta);
//          int i = 0;
//           while (rs.next())
//            {
//                DBRecords r = new DBRecords();
//                r.setCol1(rs.getString("name"));
//                r.setCol2(rs.getString("description"));
//                r.setCol3(rs.getString("comment"));
//                r.setCol4(""+rs.getDate("comment_date"));
//                r.setCol5(""+rs.getInt("id"));
//                result.add(r);
//                objname.add(rs.getString("name"));
//                objdscp.add(rs.getString("description"));
//                comment.add(rs.getString("comment"));
//                commentdate.add(rs.getDate("comment_date"));
//                objid.add(rs.getInt("id"));
            String consulta = " SELECT id,name,description from objective where subject_id = " + subjectid;
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                DBRecords r = new DBRecords();
                r.setCol1(rs.getString("name"));
                r.setCol2(rs.getString("description"));
                r.setCol5("" + rs.getInt("id"));
                result.add(r);
            }
            for (DBRecords r : result) {

                consulta = "SELECT * FROM progress_report where objective_id =" + r.getCol5() + "AND generalcomment = TRUE AND student_id =" + studentid;
                ResultSet rs1 = st.executeQuery(consulta);
                while (rs1.next()) {
                    r.setCol3(rs1.getString("comment"));
                    r.setCol4("" + rs1.getDate("comment_date"));

                }

            }

        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

//      String jname = new Gson().toJson(objname);
//       String jdscp = new Gson().toJson(objdscp);
//       String jcomm = new Gson().toJson(comment);
//       String jcommd = new Gson().toJson(commentdate);
//       JSONObject json = new JSONObject();
//      json.put("objname",objname);
//      json.put("dscp",objdscp);
//      json.put("comment",comment);
//      json.put("commentdate",commentdate);
        String off = new Gson().toJson(result);
        return off;
        //           return pjson;
    }

    @RequestMapping("/progressbystudent/saveGeneralcomment.htm")
    @ResponseBody
    public String saveGeneralcomment(@RequestBody DBRecords data, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String message = "Comment was not saved";
        JSONObject obj = new JSONObject();
//    String[] hi = hsr.getParameterValues("data");
        // JSONObject jsonObj = new JSONObject(hi[0]);
        String objectiveid = data.getCol1();
        String comment = data.getCol3();
        String studentid = data.getCol2();
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSource", hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            String consulta = "select id from progress_report where objective_id = " + objectiveid + " and generalcomment = TRUE and student_id ='" + studentid + "'";
            ResultSet rs = st.executeQuery(consulta);
            if (!rs.next()) {
                st.executeUpdate("insert into progress_report(comment_date,comment,student_id,objective_id,generalcomment) values (now(),'" + comment + "','" + studentid + "','" + objectiveid + "',true)");
                message = "Comment successfully updated";

            } else {
                st.executeUpdate("update progress_report set comment_date = now(),comment = '" + comment + "' where objective_id = " + objectiveid + " AND student_id = '" + studentid + "' and generalcomment = true");
                message = "Comment successfully updated";
            }
            obj.put("message", message);
            obj.put("comment", comment);
            obj.put("objectiveid", objectiveid);
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return obj.toString();
    }

//    @RequestMapping("/progressbystudent/loadtree.htm")
//    @ResponseBody
    public String loadtree(int studentid, ServletContext hsr) throws Exception { // CAMBIAR ESTO PARA ADAPTARLO A LA NEUVA QUERY
       
        ModelAndView mv = new ModelAndView("progressbystudent");
        JSONObject json = new JSONObject();
        ArrayList<DBRecords> steps = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> objectives = new ArrayList<>();
        TreeGrid tree = new TreeGrid();
        HashMap<String, String> mapSubject = new HashMap<String,String>();
        List<Subject> subs  = new ArrayList<>();
        Nodetreegrid<String> rootNode = new Nodetreegrid<String>("Subjects", "A", "", "", "", "");
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSourceAH", hsr);
            this.cn = dataSource.getConnection();

            Statement st = this.cn.createStatement();
//        String[] levelid = hsr.getParameterValues("seleccion1");
            //List<Subject> subs = this.getSubjects(levelid);
            subs = getSubjects(studentid);
             
           
            String consulta = "select * from Courses";
            ResultSet rs9 = st.executeQuery(consulta);
            String name9, idHash;

            while (rs9.next()) {
                if (rs9.getBoolean("Active")) {
                    name9 = rs9.getString("Title");
                    idHash = rs9.getString("CourseID");
                    mapSubject.put(idHash, name9);
                }
            }
            
            dataSource = (DriverManagerDataSource) this.getBean("dataSource", hsr);
            this.cn = dataSource.getConnection();

            st = this.cn.createStatement();
            for (Subject sub : subs) {
                String[] sid = sub.getId();
                ResultSet rs = st.executeQuery("select obj_steps.id,obj_steps.name,objective.name as obj ,objective.id as objid,objective.subject_id from obj_steps inner join objective on obj_steps.obj_id = objective.id where objective.subject_id = '" + sid[0] + "'");

                while (rs.next()) {
                    DBRecords l = new DBRecords();
                    l.setCol1("" + rs.getInt("id"));// step id  
                    l.setCol2(rs.getString("name"));// step name
                    l.setCol4(rs.getString("obj"));// objective name
                    l.setCol3("" + rs.getInt("subject_id"));// subjectid
                    l.setCol6("" + rs.getInt("objid"));//will only be used to get other data,then later will be the progress 100% of the objective
                    if (!objectives.contains(rs.getString("obj"))) {
                        objectives.add(rs.getString("obj"));
                    }
                    steps.add(l);

                }
            }
            

            for (DBRecords x : steps) {// AQUI ES DONDE TARDA!!!
                Subject s = new Subject();
                String id = null;
                id = x.getCol3();
                //String t = s.fetchName(Integer.parseInt(id), hsr);
                String t = mapSubject.get(id);
                x.setCol3(t);
                if (!subjects.contains(x.getCol3())) {
                    subjects.add(x.getCol3());
                }
                //get the student progress for student 10101,getting the last step the student in, with the latest date
                    ResultSet rs5 = st.executeQuery("select comment_date,step_id from progress_report where objective_id='" + x.getCol6() + "' AND comment_date = (select max(comment_date) from public.progress_report where student_id = '" + studentid + "' AND objective_id = '" + x.getCol6() + "' and generalcomment = false) and generalcomment = false and student_id ='" + studentid + "'");
                if (rs5.next()) {
                    String stsdone = rs5.getString("step_id");
                    if (stsdone != null && !stsdone.equals("null") && !stsdone.equals("")) {
                        List<String> ste = Arrays.asList(stsdone.split(","));

                        if (ste.contains(x.getCol1())) {
                            x.setCol5("100");
                        } else {
                            x.setCol5("0");
                        }

                    }
                }
            }

            String test = new Gson().toJson(steps);

            int i = 0;
            int z = 0;
            for (Subject x : subs)//subjects)
            {
                Nodetreegrid<String> nodeC = new Nodetreegrid<String>("L" + i, x.getName(), "", "", "", "");
                rootNode.addChild(nodeC);
                i++;
                ArrayList<Objective> obj = this.getObjectives(x.getId());
                for (Objective y : obj) {
                    String[] id = y.getId();
                    Nodetreegrid<String> nodeA = new Nodetreegrid<String>("C" + z, y.getName(), this.getfinalrating(id[0], "" + studentid), this.getnoofplannedlessons(id[0], "" + studentid), this.getnoofarchivedlessons(id[0], "" + studentid), this.getpercent(id[0], "" + studentid));
                    nodeC.addChild(nodeA);
                    z++;
                    for (DBRecords l : steps) {

                        //match the subject with the objective
                        if (l.getCol3().equalsIgnoreCase(x.getName()) && l.getCol4().equalsIgnoreCase(y.getName())) {

                            //match the objective with the step
                            for (DBRecords k : steps) {
                           //     if (k.getCol4().equalsIgnoreCase(y.getName())) {
                           String[] match = y.getId();
                           if (k.getCol6().equalsIgnoreCase(match[0])) {
                                    Nodetreegrid<String> nodeB = new Nodetreegrid<String>(k.getCol1(), k.getCol2(), "", "", "", k.getCol5());

                                    nodeA.addChild(nodeB);
                                }
                            }
                            break;
                        }

                    }
                }

            }

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

    public String generateJSONfromTree(TreeGrid tree) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // buffer to write to string later
        JsonGenerator generator = factory.createJsonGenerator(out, JsonEncoding.UTF8);

        ObjectNode rootNode = generateJSON(tree.getRootElement(), mapper.createObjectNode());
        mapper.writeTree(generator, rootNode);

        return out.toString();
    }

    public ObjectNode generateJSON(Nodetreegrid<String> node, ObjectNode obN) throws JSONException {
        if (node == null) {
            return obN;
        }

        obN.put("id", node.getId());
        obN.put("name", node.getName());
        obN.put("noofplannedlessons", node.getNoofplannedlessons());
        obN.put("noofarchivedlessons", node.getNoofarchivedlessons());
        obN.put("progress", node.getProgress());
        obN.put("rating", node.getFinalrating());

        JSONObject j = new JSONObject();
//        j.put("opened",true);
//        j.put("disabled",false);
//        obN.put("state",j.toString());
        ArrayNode childN = obN.arrayNode();
        obN.set("children", childN);
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            return obN;
        }

        Iterator<Nodetreegrid<String>> it = node.getChildren().iterator();
        while (it.hasNext()) {
            childN.add(generateJSON(it.next(), new ObjectMapper().createObjectNode()));
        }
        return obN;
    }

//    public ArrayList<Subject> getSubjects(String levelid) throws SQLException
//       {
//           
//        ArrayList<Subject> subjects = new ArrayList<>();
//        ArrayList<Subject> activesubjects = new ArrayList<>();
//        try{
//           Statement st = this.cn.createStatement();
//             
//          ResultSet rs1 = st.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID ="+levelid+")");
//           Subject s = new Subject();
//          s.setName("Select Subject");
//          subjects.add(s);
//           
//           while (rs1.next())
//            {
//             Subject sub = new Subject();
//             String[] ids = new String[1];
//            ids[0]=""+rs1.getInt("CourseID");
//             sub.setId(ids);
//            
//                subjects.add(sub);
//            }
//           for(Subject su:subjects.subList(1,subjects.size()))
//          {
//              String[] ids = new String[1];
//              ids=su.getId();
//           ResultSet rs2 = st.executeQuery("select Title,Active from Courses where CourseID = "+ids[0]);
//           while(rs2.next())
//           {
//            if(rs2.getBoolean("Active")== true)
//               {
//                   su.setName(rs2.getString("Title"));
//                   activesubjects.add(su);
//               }
//           }
//          }
//        }catch(SQLException ex){
//        StringWriter errors = new StringWriter();
//        ex.printStackTrace(new PrintWriter(errors));
//        log.error(ex+errors.toString());
//        }
//           return activesubjects;
//       }
    //classroom observations
    @RequestMapping("/progressbystudent/savecomment.htm")
    public ModelAndView savecomment(@RequestBody Observation obs, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("progressbystudent");
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSource", hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            st.executeUpdate("insert into classobserv(logged_by,date_created,comment,category,student_id,commentdate)values('" + user.getId() + "',now(),'" + obs.getObservation() + "','" + obs.getType() + "','" + obs.getStudentid() + "','" + obs.getDate() + "')");
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    @RequestMapping("/progcal.htm")
    @ResponseBody
    public ModelAndView progcal(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("progcal");
        String studentId = hsr.getParameter("studentid");
        try {
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSourceAH", hsr.getServletContext());
            Statement st = this.cn.createStatement();

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        //    mv.addObject("message","works");
        String message = "works";
        mv.addObject("studentId", studentId);

        return mv;
    }

    public static String getNextDate(String curDate) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = format.parse(curDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return format.format(calendar.getTime());
    }

    /* FUNCIONA PARA 5 DIAS SEGUIDOS
    @RequestMapping("/loadComentsStudent.htm")
    @ResponseBody
    public String loadComentsStudent(@RequestBody Observation r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        final int DIAS_MAX = 5;
        String studentId = ""+r.getStudentid();
        DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");   
        String dateSelected = formatoFecha.format(r.getDate());
        int days=0;
        
        Observation oAux = new Observation();
        Date fechaActual = new Date();
        String currentDate = formatoFecha.format(fechaActual);
        
        ArrayList<ArrayList<Observation>> arrayObservations = new  ArrayList<ArrayList<Observation>>();
        ArrayList<Observation> arrayComments = new ArrayList<Observation>();
        String consulta = "SELECT * FROM classobserv WHERE student_id = "+studentId+" AND commentdate = '"+dateSelected+"'";
        
        try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            Statement st = this.cn.createStatement();
            while(days < DIAS_MAX && !currentDate.equals(dateSelected)){
                
                //dateSelected = getNextDate(dateSelected);
                arrayComments.clear();
                consulta = "SELECT * FROM classobserv WHERE student_id = "+studentId+" AND commentdate = '"+dateSelected+"' ORDER BY commentdate";
                ResultSet rs = st.executeQuery(consulta);
               
                
                while (rs.next()) {        
                    oAux.setId(rs.getInt("id"));
                    oAux.setLogged_by(rs.getInt("logged_by"));
                    oAux.setDate(rs.getDate("date_created"));
                    oAux.setObservation(rs.getString("comment"));
                    oAux.setType(rs.getString("category"));
                    oAux.setStudentid(Integer.parseInt(studentId));
                    oAux.setCommentDate(rs.getDate("commentdate"));
                    arrayComments.add(new Observation(oAux));
                }
                
                if(!arrayComments.isEmpty()){
                    arrayObservations.add(new ArrayList<Observation>(arrayComments));
                    days++;
                }
                dateSelected = getNextDate(dateSelected);
            }
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        return new Gson().toJson(arrayObservations);
    }*/
    
    
        
    @RequestMapping("/updateComment.htm")
    public ModelAndView updateComment(@RequestBody Observation r,HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception{
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("lessonresources");
        try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();       
            st.executeUpdate("update classobserv set date_created = now(), comment = '"+r.getObservation()+"' ,category = '"+r.getType()+"', commentdate = '"+r.getDateString()+"' where id = '"+r.getId()+"'");

        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        return mv;
    }
    
    @RequestMapping("/delComentario.htm")
    public ModelAndView delComment(@RequestBody Resource r,HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception{
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("lessonresources");
        try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            String commentId = r.getId();
            
            String consulta = "delete from classobserv where id = "+commentId;
            st.executeUpdate(consulta);
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        return mv;
    }
    
    @RequestMapping("/loadComentsStudent.htm")
    @ResponseBody
    public String loadComentsStudent(@RequestBody Observation r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        DateFormat formatoFecha;// = new SimpleDateFormat("M/d/yyyy");   
        String date = r.getDateString() + "-01";

        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));

        int DIAS_MAX = convertedDate.getDayOfMonth();

        String studentId = "" + r.getStudentid();

        String monthSelected = "" + r.getDateString().charAt(5) + r.getDateString().charAt(6);

        String yearSelected = "" + r.getDateString().charAt(0) + r.getDateString().charAt(1) + r.getDateString().charAt(2) + r.getDateString().charAt(3);
        int days = 0;

        Observation oAux = new Observation();

        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = new Date();
        String currentDate = formatoFecha.format(fechaActual);

        String dateSelected = yearSelected + "-" + monthSelected + "-" + "01";

        ArrayList<ArrayList<Observation>> arrayObservations = new ArrayList<ArrayList<Observation>>();
        ArrayList<Observation> arrayComments = new ArrayList<Observation>();
        String consulta = "SELECT * FROM public.classobserv WHERE student_id = " + studentId + " AND commentdate = '" + dateSelected + "'";

        try {

            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource) this.getBean("dataSource", hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();

            while (days < DIAS_MAX && !currentDate.equals(dateSelected)) {
                //dateSelected = getNextDate(dateSelected);
                arrayComments.clear();
                consulta = "SELECT * FROM classobserv WHERE student_id = " + studentId + " AND commentdate = '" + dateSelected + "' ORDER BY commentdate";
                ResultSet rs = st.executeQuery(consulta);

                while (rs.next()) {
                    oAux.setId(rs.getInt("id"));
                    oAux.setLogged_by(rs.getInt("logged_by"));
                    oAux.setDate(rs.getDate("date_created"));
                    oAux.setObservation(rs.getString("comment"));
                    oAux.setType(rs.getString("category"));
                    oAux.setStudentid(Integer.parseInt(studentId));
                    oAux.setCommentDate(rs.getDate("commentdate"));
                    arrayComments.add(new Observation(oAux));
                }

                arrayObservations.add(new ArrayList<Observation>(arrayComments));
                days++;
                dateSelected = getNextDate(dateSelected);

            }
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return new Gson().toJson(arrayObservations);
    }

    public ArrayList<Objective> getObjectives(String[] subjectid) throws SQLException {
        ArrayList<Objective> objectives = new ArrayList<>();
        try {

            Statement st = this.cn.createStatement();

            ResultSet rs1 = st.executeQuery("select name,id from public.objective where subject_id=" + subjectid[0]);
//          Objective s = new Objective();
//          s.setName("Select Objective");
//          objectives.add(s);

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

    public String getnoofplannedlessons(String objid, String studid) throws SQLException {
        String result = "";
        try {

            Statement st = this.cn.createStatement();

            ResultSet rs1 = st.executeQuery("select count(id) from lesson_stud_att where student_id = '" + studid + "' and lesson_id in (select id from lessons where objective_id ='" + objid + "' and COALESCE(archive, FALSE) = FALSE);");

            while (rs1.next()) {
                result = "" + rs1.getInt("count");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return result;
    }

    public String getnoofarchivedlessons(String objid, String studid) throws SQLException {
        String result = "";
        try {

            Statement st = this.cn.createStatement();

            ResultSet rs1 = st.executeQuery("select count(id) from lesson_stud_att where student_id = '" + studid + "' and lesson_id in (select id from lessons where objective_id ='" + objid + "' and archive = TRUE);");

            while (rs1.next()) {
                result = "" + rs1.getInt("count");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return result;
    }

    public String getfinalrating(String objid, String studid) throws SQLException {
        String result = "";
        try {

            Statement st = this.cn.createStatement();
// most recent rating excluding the NA & the empty rating which have ids 6 & 7
            String consulta = "SELECT rating.name FROM rating where id in"
                    + "(select rating_id from progress_report where student_id = '"+studid+"'"
                    + " AND comment_date = (select max(comment_date)   from public.progress_report "
                    + "where student_id = '"+studid+"' AND objective_id = '"+objid+"' "
                    + "and generalcomment = false and rating_id not in(6,7)) "
                    + "AND objective_id ='"+objid+"'and generalcomment = false )";

            ResultSet rs2 = st.executeQuery(consulta);
            while (rs2.next()) {
                result = rs2.getString("name");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return result;
    }

    public String getpercent(String objid, String studid) throws SQLException {
        String result = "";
        int count = 0;
        try {

            Statement st = this.cn.createStatement();

            ResultSet rs1 = st.executeQuery("select count(id) from obj_steps where obj_id = '" + objid + "'");
            while (rs1.next()) {
                count = rs1.getInt("count");
            }

            ResultSet rs2 = st.executeQuery("select comment_date,step_id from progress_report where objective_id='" + objid + "' AND comment_date = (select max(comment_date) from public.progress_report where student_id = '" + studid + "' AND objective_id = '" + objid + "' and generalcomment = false) and generalcomment = false");
            if (rs2.next()) {
                String stsdone = rs2.getString("step_id");
                if (stsdone != null && !stsdone.equals("null") && !stsdone.equals("")) {
                    List<String> ste = Arrays.asList(stsdone.split(","));
                    double percent = (ste.size() * 100) / count;
                    result = "" + percent;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return result;
    }
}
