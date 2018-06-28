/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;
//x

import Montessori.*;
import Montessori.Objective;
import Montessori.Students;
import Montessori.Subject;
import Reports.DataFactoryFolder.Profesor;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import java.util.Base64;
import static controladores.ReportControlador.log;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * progress by student
 *
 * @author nmohamed
 */
@Controller
//@Scope("session")
public class ProgressbyStudent {

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
            mv.addObject("listaAlumnos", Students.getStudents(log));
            ResultSet rs = DBConect.ah.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels");

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

    @RequestMapping("/progressbystudent/studentlistLevel.htm")
    @ResponseBody
    public String studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        ModelAndView mv = new ModelAndView("progressbystudent");
        List<Students> studentsgrades = new ArrayList();
        String[] levelid = hsr.getParameterValues("seleccion");
        String test = hsr.getParameter("levelStudent");
        if (levelid[0] != "") {
            studentsgrades = Students.getStudentslevel(levelid[0], log);
        } else {
            studentsgrades = Students.getStudents(log);
        }
        String data = new Gson().toJson(studentsgrades);
//        mv.addObject("listaAlumnos",data );

        return data;
    }

    @RequestMapping("/progressbystudent/selectTreeByTerm.htm")
    @ResponseBody
    public String selectTreeByTerm(@RequestBody CommentSubject cSub, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        /*
              var myObj = {};
                myObj["idSubject"] = idSubject; //termId
                myObj["idStudent"] = studentId; // studentId
         */
        String idTerm = cSub.getIdSubject();
        int studentId = Integer.parseInt(cSub.getIdStudent());
        List<Subject> subjects = getSubjects(studentId, hsr);

        return this.loadtree(subjects, studentId, hsr, idTerm);
    }

    static List<Subject> getSubjects(int studentid, HttpServletRequest hsr) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        List<Subject> activesubjects = new ArrayList<>();
        HashMap<String, String> mapSubject = new HashMap<String, String>();
        String yearid = "" + hsr.getSession().getAttribute("yearId");
        String termid = "" + hsr.getSession().getAttribute("termId");

        try {
            ResultSet rs1 = DBConect.ah.executeQuery("select distinct courses.courseid,courses.rcplacement, courses.title, courses.active from roster    inner join classes on roster.classid=classes.classid\n"
                    + "                 inner join courses on courses.courseid=classes.courseid\n"
                    + "                  where roster.studentid = " + studentid + " and roster.enrolled" + termid + "=1 and courses.active = 1 and courses.reportcard = 1 and classes.yearid = '" + yearid + "' order by courses.rcplacement DESC");// the term and year need to be dynamic, check with vincent

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

            for (int i = 0; i < subjects.size(); i++) {
                String[] ids = new String[1];
                ids = subjects.get(i).getId();
                subjects.get(i).setName(mapSubject.get(ids[0]));
                activesubjects.add(subjects.get(i));
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return activesubjects;
    }

    @RequestMapping("/progressbystudent/objectiveListReport.htm")
    @ResponseBody
    public String objectivesReportCard(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String selection = hsr.getParameter("seleccion");
        String studentId = hsr.getParameter("studId");
        String[] data = selection.split(",");
        String subjectid = data[0];
        List<DBRecords> result = new ArrayList<>();
        String yearid = "" + hsr.getSession().getAttribute("yearId");
        String termid = "" + hsr.getSession().getAttribute("termId");
        JSONObject obj = new JSONObject();

        /**
         * TOCARA CAMBIAR DE QUERY PRIMERA VERSION SELECT
         * name,rating_id,level_id,student_id from objective left join (select *
         * from progress_report where student_id = 12) b on (objective.id =
         * b.objective_id) where objective.reportcard= 'true' and
         * objective.year_id= 59 and objective.subject_id = 348
         *
         *
         * VERSION 2 SELECT name,rating_id,level_id from objective left join
         * (select * from progress_report where student_id = 10115) b on
         * (objective.id = b.objective_id) where objective.reportcard= 'true'
         * and objective.year_id= 59 and objective.subject_id = 348
         *
         *
         */
        try {
            String consulta = "SELECT objective.id,name,rating_id,level_id,objective.term_id from objective left join \n"
                    + "(select * from progress_report where student_id = " + studentId + ") b\n"
                    + " on (objective.id = b.objective_id) where  objective.reportcard= 'true' and \n"
                    + "objective.year_id= " + yearid + " and objective.subject_id = " + subjectid;

            ResultSet rs = DBConect.eduweb.executeQuery(consulta);

             while (rs.next()) {
                String[] s = rs.getString("term_id").split(",");
                boolean encontro = false;
                int i = 0;
                while (!encontro && i < s.length) {
                    if (termid.equals("" + s[i])) {
                        encontro = true;
                    } else {
                        i++;
                    }
                }
                if (encontro) {
                    DBRecords r = new DBRecords();
                    r.setCol1(rs.getString("name"));
                    r.setCol3(rs.getString("id"));
                    
                    Integer rId = rs.getInt("rating_id");
                    if(rId == 0){
                        rId = 7;
                    }     
                    r.setCol2(""+rId);      
                    
                    Integer lId = rs.getInt("level_id");
                    if(lId == 0){
                        lId = 6;
                    }
                    r.setCol5("" + lId);
                    result.add(r);
                }
             }
            /*String consulta = " SELECT * from objective where reportcard= 'true' and year_id= " + yearid + " and subject_id = " + subjectid + " order by name ASC";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                String[] s = rs.getString("term_id").split(",");
                boolean encontro = false;
                int i = 0;
                while (!encontro && i < s.length) {
                    if (termid.equals("" + s[i])) {
                        encontro = true;
                    } else {
                        i++;
                    }
                }
                if (encontro) {
                    DBRecords r = new DBRecords();
                    r.setCol1(rs.getString("name"));
                    r.setCol2(rs.getString("description"));
                    r.setCol5("" + rs.getInt("id"));
                    result.add(r);
                }
            }*/
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        obj.put("result", new Gson().toJson(result));
        obj.put("ratings", new Gson().toJson(getRatings()));
        obj.put("levels", new Gson().toJson(getLevels()));
        return obj.toString();
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
        return mv;
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
        servlet = hsr.getServletContext();
        List<Progress> progress = new ArrayList<>();
        String finalrating = null;
        String presenteddate = null;
        String attempteddate = null;
        String mastereddate = null;
        List<String> attemptdates = new ArrayList<>();
        try {
//will display only if there is a lesson that has a progress record,but if a lesson is only planned will not be displayed

            ResultSet rs1 = DBConect.eduwebBeforeFirst.executeQuery("select comment,comment_date,ratingname,lessonname,createdby from public.progresslessonname where objective_id=" + d.getCol1() + " AND student_id = '" + d.getCol2() + "' order by comment_date DESC");

            if (!rs1.next()) {
                String message = "Student does not have progress under the selected objective";//if i change this message must change as well in the jsp
                mv.addObject("message", message);
            } else {
                rs1.beforeFirst();
                while (rs1.next()) {
                    Progress p = new Progress();
                    p.setCreatedby(fetchTeacher(rs1.getInt("createdby"), hsr));
                    p.setComment(rs1.getString("comment"));
                    p.setRating(rs1.getString("ratingname"));
                    if (rs1.getString("lessonname") != null) {
                        p.setLesson_name(rs1.getString("lessonname"));
                    } else {
                        p.setLesson_name("");// so that null will not appear in the table in case of a general comment
                    }
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

                finalrating = this.getfinalrating(d.getCol1(), d.getCol2());
                String consulta = "select min(comment_date) as date from progress_report where student_id =" + d.getCol2() + " and rating_id in (select id from rating where name = 'Presented') and objective_id =" + d.getCol1();
                ResultSet rs3 = DBConect.eduwebBeforeFirst.executeQuery(consulta);
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
                ResultSet rs4 = DBConect.eduwebBeforeFirst.executeQuery(consulta);
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
                ResultSet rs5 = DBConect.eduwebBeforeFirst.executeQuery(consulta);
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
//                String prog = new Gson().toJson(progress);
//                String rating = new Gson().toJson(finalrating);
//                JSONObject obj = new JSONObject();
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
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    @RequestMapping("/progressbystudent/loadtree.htm")
    @ResponseBody
    public String treeload(HttpServletRequest hsr, HttpServletResponse hsr1) {
        try {
            return this.loadtree(new ArrayList<>(), Integer.parseInt(hsr.getParameter("studentid")), hsr, "-1");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ProgressbyStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return "";
    }

    private String cargarFoto(String photoName, HttpServletResponse hsr1) {
        // $('#foto').attr('src', "ftp://AH-ZAF:e3f14+7mANDp@ftp2.renweb.com/Pictures/" + info.foto).css('width', '300px').css('height', '500px');

        String server = "ftp2.renweb.com";
        int port = 21;
        String user = "AH-ZAF";
        String pass = "e3f14+7mANDp";

        String filePath = "/Pictures/" + photoName;

        String total = "";
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream inStream = ftpClient.retrieveFileStream(filePath);

            System.err.println("");

        } catch (Exception e) {
            System.err.println(e);
        }
        return total;
    }

    public String getImage(String photoName, HttpServletRequest request) throws IOException {
        try {
            /*URL url = new URL("ftp://AH-ZAF:e3f14+7mANDp@ftp2.renweb.com/Pictures/" + photoName);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();*/
            //***********

            String server = "192.168.1.36";
            int port = 21;
            String user = "david";
            String pass = "david";

            String filepath = "/Pictures/" + photoName;
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //LIMITAR NO EXISTE

            InputStream inStream = ftpClient.retrieveFileStream(filepath);
            if (inStream != null) {
                // gets MIME type of the file
                String mimeType = "";
                //String filepath = url.getPath();
                int i = filepath.length() - 1;
                while (filepath.charAt(i) != '.') {
                    mimeType = filepath.charAt(i) + mimeType;
                    i--;
                }
                mimeType = '.' + mimeType;

                //
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
                byte[] buf = buffer.toByteArray();
                //
                // byte[] buf = new byte[inStream.available()];
                inStream.read(buf);
                String imagen = Base64.getEncoder().encodeToString(buf);
                return "data:image/" + mimeType + ";base64," + imagen;
                //**********
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //load student demographics
    @RequestMapping("/progressbystudent/studentPage.htm")
    @ResponseBody
    public String studentPage(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        //    ModelAndView mv = new ModelAndView("progressbystudent");
        List<Subject> subjects = new ArrayList<>();
        HashMap<String, String> mapSubject = new HashMap<>();

        String[] studentIds = hsr.getParameterValues("selectStudent");
        Students student = new Students();
        JSONObject obj = new JSONObject();
        //String prueba = "";
        String prueba = "";
        try {
            String consulta = "SELECT * FROM Students where StudentID = " + studentIds[0];
            ResultSet rs = DBConect.ah.executeQuery(consulta);

            while (rs.next()) {

                student.setId_students(rs.getInt("StudentID"));
                student.setNombre_students(rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName"));
                student.setFecha_nacimiento(rs.getString("Birthdate").substring(0, 10));
                student.setFoto(rs.getString("PathToPicture"));
                student.setLevel_id(rs.getString("GradeLevel"));
                student.setNextlevel(rs.getString("NextGradeLevel"));

            }
            if (student.getFoto() != null) {
                prueba = getImage(student.getFoto(), hsr);
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        // List<Subject> subjects = new ArrayList<>();
        subjects = getSubjects(student.getId_students(), hsr);
        
        String info = new Gson().toJson(student);
        String sub = new Gson().toJson(subjects);
        String prueba2 = new Gson().toJson(prueba);
        
        obj.put("info", info);
        obj.put("sub", sub);
        obj.put("prueba", prueba2);
        obj.put("commentHead", this.getCommentHead(student.getId_students(), hsr));
        obj.put("prog", this.loadtree(getAllSubjectsYear(student.getId_students(), hsr), student.getId_students(), hsr, "-1"));
        obj.put("nextPresentations", new Gson().toJson(this.getNextPresentations(student.getId_students())));
        return obj.toString();
    }

    private ArrayList<DBRecords> getNextPresentations(int idStudent){
        ArrayList<DBRecords> nextPresentations = new ArrayList<>(); // col1 = id Presentation 
                                                                    // col2 = name Presentation
                                                                    // col3 = name Teacher
        Timestamp timestampNow = new Timestamp(System.currentTimeMillis());
        try {
            HashMap<Integer,String> nameTeachers = new HashMap<>();
            String consulta = "select  PersonID,FirstName,LastName  from Person";
            ResultSet rs =DBConect.ah.executeQuery(consulta);
            
            while (rs.next()) {
              String name =  rs.getString(2)+" "+rs.getString(3);
              nameTeachers.put(rs.getInt(1),name);
            }
                    
            consulta = "SELECT lessons.id,lessons.name,user_id FROM lesson_stud_att inner join lessons "
                    + "                                         on lessons.id = lesson_stud_att.lesson_id "
                    + "                     where student_id = "+idStudent+" and start >= '"+timestampNow+"' order by timestamp ASC";
            rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
               DBRecords auxDB = new DBRecords();
               auxDB.setCol1(""+rs.getInt(1));
               auxDB.setCol2(rs.getString(2));
               
               
               String auxName;
               if(nameTeachers.containsKey(rs.getInt(3))){
                   auxName = nameTeachers.get(rs.getInt(3));
               }
               else{
                   auxName = "No teacher";
               }
               
               auxDB.setCol3(auxName);
               nextPresentations.add(new DBRecords(auxDB));
            }

        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        
        return nextPresentations;
    }
    private ArrayList<String[]> getRatings() {
        ArrayList<String[]> ratings = new ArrayList<>();

        try {
            String consulta = " SELECT DISTINCT * from rating ";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                String[] auxString = new String[2];
                auxString[0] = "" + rs.getInt(1);
                auxString[1] = rs.getString(2);
                ratings.add(auxString);
            }

        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return ratings;
    }

    private ArrayList<String[]> getLevels() {
        ArrayList<String[]> levels = new ArrayList<>();

        try {
            String consulta = " SELECT DISTINCT * from levels ";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                String[] auxString = new String[2];
                auxString[0] = "" + rs.getInt(1);
                auxString[1] = rs.getString(2);
                levels.add(auxString);
            }

        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return levels;
    }

    private List<Subject> getAllSubjectsYear(int studentid, HttpServletRequest hsr) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        List<Subject> activesubjects = new ArrayList<>();
        HashMap<String, String> mapSubject = new HashMap<String, String>();
        String yearid = "" + hsr.getSession().getAttribute("yearId");

        try {
            ResultSet rs1 = DBConect.ah.executeQuery("select distinct courses.courseid,courses.rcplacement, courses.title, courses.active from roster    inner join classes on roster.classid=classes.classid\n"
                    + "                 inner join courses on courses.courseid=classes.courseid\n"
                    + "                  where roster.studentid = " + studentid + " and courses.active = 1 and courses.reportcard = 1 and classes.yearid = '" + yearid + "' order by courses.rcplacement DESC");// the term and year need to be dynamic, check with vincent

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

            for (int i = 0; i < subjects.size(); i++) {
                String[] ids = new String[1];
                ids = subjects.get(i).getId();
                subjects.get(i).setName(mapSubject.get(ids[0]));
                activesubjects.add(subjects.get(i));
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        return activesubjects;
    }

    private String getCommentHead(int idStudent, HttpServletRequest hsr) {
        String yearid = "" + hsr.getSession().getAttribute("yearId");
        String termid = "" + hsr.getSession().getAttribute("termId");
        String consulta = " SELECT comment from report_comments where yearterm_id =" + yearid + " and term_id= " + termid + " and studentid = " + idStudent + " and supercomment = true", res = "";
        try {
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                res = rs.getString("comment");
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ProgressbyStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return res;
    }

    //loads list of objectives final rating & general comments based on the selected subject
    @RequestMapping("/progressbystudent/objGeneralcomments.htm")
    @ResponseBody
    public String objGeneralcomments(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String selection = hsr.getParameter("selection");
        String[] data = selection.split(",");
        HttpSession sesion = hsr.getSession();
        String subjectid = data[0];
        String studentid = data[1];
        List<DBRecords> result = new ArrayList<>();
        String comment = "";
        try {
            String yearid = "" + hsr.getSession().getAttribute("yearId");
            String termid = "" + hsr.getSession().getAttribute("termId");

            String consulta = " SELECT id,name,description from objective where "
                    + "year_id= " + yearid + " and term_id like '%" + termid + "%' and subject_id= '" + subjectid+"' and COALESCE(reportcard, FALSE) = FALSE";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                DBRecords r = new DBRecords();
                r.setCol1(rs.getString("name"));
                r.setCol2(rs.getString("description"));
                r.setCol5("" + rs.getInt("id"));
                result.add(r);
            }
            for (DBRecords r : result) {

                consulta = "SELECT comment,comment_date FROM progress_report where objective_id =" + r.getCol5() + "AND comment_date in(select max(comment_date) from progress_report where objective_id =" + r.getCol5() + "AND student_id ='" + studentid + "')AND student_id =" + studentid;
                ResultSet rs1 = DBConect.eduweb.executeQuery(consulta);
                while (rs1.next()) {
                    r.setCol3(rs1.getString("comment"));
                    r.setCol4("" + rs1.getDate("comment_date"));

                }

            }
            consulta = "select comment from report_comments where subject_id = " + subjectid + " and term_id = " + sesion.getAttribute("termId") + " and yearterm_id =" + sesion.getAttribute("yearId") + " and studentid =" + studentid + " order by date_created DESC";
            ResultSet rs2 = DBConect.eduweb.executeQuery(consulta);

            if (rs2.next()) {
                comment = rs2.getString("comment");
            }

        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        String objs = new Gson().toJson(result);
        JSONObject info = new JSONObject();
        info.put("objs", objs);
        info.put("comment", comment);
        String off = info.toString();
        return off;
        //           return pjson;
    }

//    @RequestMapping("/progressbystudent/getSubjectComment.htm")
//    @ResponseBody
//    public String getSubjectComment(@RequestBody String id, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
//
//        HttpSession sesion = hsr.getSession();
//        String subjectId = id.substring(0, id.indexOf("$"));
//        String studentId = id.substring(id.indexOf("$") + 1, id.length());
//        String comment = "";
//        try {
//            String consulta = "select comment from report_comments where subject_id = " + subjectId + " and term_id = " + sesion.getAttribute("termId") + " and yearterm_id =" + sesion.getAttribute("yearId") + " and studentid =" + studentId + " order by date_created DESC";
//            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
//            if (rs.next()) {
//                comment = rs.getString("comment");
//            }
//            return comment;
//        } catch (SQLException ex) {
//            System.out.println("Error leyendo Alumnos: " + ex);
//            StringWriter errors = new StringWriter();
//            ex.printStackTrace(new PrintWriter(errors));
//            log.error(ex + errors.toString());
//        }
//
//        return comment;
//    }
    @RequestMapping("/progressbystudent/saveGeneralcomment.htm")
    @ResponseBody
    public String saveGeneralcomment(@RequestBody DBRecords data, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String message = "Comment was not saved";
        JSONObject obj = new JSONObject();
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
        String objectiveid = data.getCol1();
        String comment = data.getCol3();
        String studentid = data.getCol2();
        try {
            String consulta = "select id from progress_report where objective_id = " + objectiveid + " and generalcomment = TRUE and student_id ='" + studentid + "'";
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            if (!rs.next()) {
                DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,comment,student_id,objective_id,generalcomment,createdby,term_id,yearterm_id) values (now(),'" + comment + "','" + studentid + "','" + objectiveid + "',true,'" + user.getId() + "'," + sesion.getAttribute("termId") + "," + sesion.getAttribute("yearId") + ")");
                message = "Comment successfully updated";

            } else {
                String s = "update progress_report set modifyby = '" + user.getId() + "',comment_date = now(),comment = '" + comment + "' where objective_id = " + objectiveid + " AND student_id = '" + studentid + "' and generalcomment = true";
                DBConect.eduweb.executeUpdate(s);
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

    public String loadtree(List<Subject> ls, int studentid, HttpServletRequest hsr, String idTerm) throws Exception { // CAMBIAR ESTO PARA ADAPTARLO A LA NEUVA QUERY

        ModelAndView mv = new ModelAndView("progressbystudent");
        JSONObject json = new JSONObject();
        ArrayList<DBRecords> steps = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> objectives = new ArrayList<>();
        TreeGrid tree = new TreeGrid();
        HashMap<String, String> mapSubject = new HashMap<String, String>();
        List<Subject> subs = new ArrayList<>();
        Nodetreegrid<String> rootNode = new Nodetreegrid<String>("Subjects", "A", "", "", "", "");

        try {
            if (ls.isEmpty()) {
                subs = getSubjects(studentid, hsr);
            } else {
                subs = ls;
            }

            String consulta = "select * from Courses";
            ResultSet rs9 = DBConect.ah.executeQuery(consulta);
            String name9, idHash;

            while (rs9.next()) {
                if (rs9.getBoolean("Active")) {
                    name9 = rs9.getString("Title");
                    idHash = rs9.getString("CourseID");
                    mapSubject.put(idHash, name9);
                }
            }

            for (Subject sub : subs) {
                String[] sid = sub.getId();
                String consulta3 = "select objective.term_id , obj_steps.id,obj_steps.name,objective.name as obj ,objective.id as objid,objective.subject_id "
                        + "from obj_steps inner join objective on obj_steps.obj_id = objective.id "
                        + "where objective.subject_id = '" + sid[0] + "' order by obj_steps.storder ASC";

                ResultSet rs = DBConect.eduweb.executeQuery(consulta3);

                while (rs.next()) {
                    String aux = rs.getString("term_id");
                    String[] termIds = aux.split(",");

                    if (idTerm.equals("-1") || comprobarTerm(termIds, idTerm)) {
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
            }
            consulta = "select * from progress_report a where comment_date = (select max(comment_date) from public.progress_report where objective_id = a.objective_id and generalcomment = false and student_id ='" + studentid + "') and generalcomment = false and student_id ='" + studentid + "'";
            ResultSet rs7 = DBConect.eduweb.executeQuery(consulta);
            HashMap<String, String> mapDBR = new HashMap<String, String>();

            while (rs7.next()) {
                String stsdone = rs7.getString("step_id");
                if (stsdone != null && !stsdone.equals("null") && !stsdone.equals("")) {

                    String[] stepsDone = stsdone.split(",");
                    for (String sDone : stepsDone) {
                        mapDBR.put(sDone, "");
                    }

                }

            }

            for (DBRecords x : steps) {
                Subject s = new Subject();
                String id = null;
                id = x.getCol3();
                String t = mapSubject.get(id);
                x.setCol3(t);
                if (!subjects.contains(x.getCol3())) {
                    subjects.add(x.getCol3());
                }
                if (mapDBR.containsKey(x.getCol1())) {
                    x.setCol5("100");
                } else {
                    x.setCol5("0");
                }
            }

            /*
            //tarda1 
           for (DBRecords x : steps) {
                Subject s = new Subject();
                String id = null;
                id = x.getCol3();
                //String t = s.fetchName(Integer.parseInt(id), hsr);
                String t = mapSubject.get(id);
                x.setCol3(t);
                if (!subjects.contains(x.getCol3())) {
                    subjects.add(x.getCol3());
                }
                ResultSet rs5 = DBConect.eduweb.executeQuery("select comment_date,step_id from progress_report where objective_id='" + x.getCol6() + "' AND comment_date = (select max(comment_date) from public.progress_report where student_id = '" + studentid + "' AND objective_id = '" + x.getCol6() + "' and generalcomment = false) and generalcomment = false and student_id ='" + studentid + "'");
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
            }*/
            String test = new Gson().toJson(steps);

            int i = 0;
            int z = 0;

            //subs.sort(c);
            //  subs.sort();
            //   Collections.sort(subs, new Sortbyroll);
            Collections.sort(subs, new Comparator<Subject>() {
                @Override
                public int compare(Subject o1, Subject o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            HashMap<String, String> plannedLess = getnoofplannedlessons();
            HashMap<String, String> archiveLess = getnoofarchivedlessons();
            HashMap<String, String> finalRatings = getfinalrating();
            HashMap<String, String> percents = getpercent();
            //aqui 2
            for (Subject x : subs)//subjects)
            {
                Nodetreegrid<String> nodeC = new Nodetreegrid<String>("L" + i, x.getName(), "", "", "", "");
                rootNode.addChild(nodeC);
                i++;
                ArrayList<Objective> obj = this.getObjectivesTree(x.getId(), idTerm);
                for (Objective y : obj) {
                    String[] id = y.getId();
                    Nodetreegrid<String> nodeA = new Nodetreegrid<String>("C" + z, y.getName(), finalRatings.get(studentid + "_" + id[0]), plannedLess.get(studentid + "_" + id[0]), archiveLess.get(studentid + "_" + id[0]), percents.get(studentid + "_" + id[0]) /*this.getpercent(id[0],""+studentid)*/);
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

    private ArrayList<Objective> getObjectivesTree(String[] subjectid, String termId) throws SQLException {
        ArrayList<Objective> objectives = new ArrayList<>();
        try {

            ResultSet rs1 = DBConect.eduweb.executeQuery("select objective.term_id,name,id from public.objective where subject_id=" + subjectid[0] + " and reportcard <> true ORDER BY name ASC");
            while (rs1.next()) {
                String aux = rs1.getString("term_id");
                String[] termIds = aux.split(",");

                if (termId.equals("-1") || comprobarTerm(termIds, termId)) {
                    String[] ids = new String[1];
                    Objective sub = new Objective();
                    ids[0] = "" + rs1.getInt("id");
                    sub.setId(ids);
                    sub.setName(rs1.getString("name"));
                    objectives.add(sub);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo Objectives: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return objectives;
    }

    private boolean comprobarTerm(String[] termIds, String termSelected) {
        for (int i = 0; i < termIds.length; i++) {
            if (termIds[i].equals(termSelected)) {
                return true;
            }
        }

        return false;
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

    /* @RequestMapping("/progressbystudent/savecomment.htm")
    @ResponseBody
    public String savecomment(@RequestBody Observation obs, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("progressbystudent");
        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            DBConect.eduweb.executeUpdate("insert into classobserv(logged_by,date_created,comment,category,student_id,commentdate,term_id,yearterm_id)values('" + user.getId() + "',now(),'" + obs.getObservation() + "','" + obs.getType() + "','" + obs.getStudentid() + "','" + obs.getDate() + "'," + sesion.getAttribute("termId") + "," + sesion.getAttribute("yearId") + ")");

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return "success";
    }
     */
    @RequestMapping("/progressbystudent/saveSubjectComment.htm")
    @ResponseBody
    public String saveSubjectComment(@RequestBody CommentSubject cSub, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("progressbystudent");
        try {
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            String superComment = "false";
            String comment = cSub.getComment();
            comment = comment.replace("'", "\'\'");
            comment = comment.replace("\"", "\"\"");

            if (cSub.getIdSubject().equals("-1")) {
                superComment = "true";
            }
            String test = "update report_comments set subject_id=" + cSub.getIdSubject() + " , date_created = 'now()' ,createdby_id =" + user.getId() + " ,comment= '" + comment + "',studentid =" + cSub.getIdStudent() + ",term_id =" + sesion.getAttribute("termId") + ",yearterm_id =" + sesion.getAttribute("yearId") + ",supercomment=" + superComment + " where yearterm_id =" + sesion.getAttribute("yearId") + " and term_id =" + sesion.getAttribute("termId") + " and subject_id=" + cSub.getIdSubject() + " and supercomment = " + superComment + " and studentid =" + cSub.getIdStudent();

            if (!DBConect.eduweb.executeQuery("select * from report_comments where yearterm_id =" + sesion.getAttribute("yearId") + " and term_id =" + sesion.getAttribute("termId") + " and createdby_id =" + user.getId() + " and subject_id=" + cSub.getIdSubject() + " and supercomment = " + superComment + " and studentid =" + cSub.getIdStudent()).next()) {
                test = "insert into report_comments(subject_id,date_created,createdby_id,comment,studentid,term_id,yearterm_id,supercomment)values('" + cSub.getIdSubject() + "',now(),'" + user.getId() + "','" + comment + "','" + cSub.getIdStudent() + "'," + sesion.getAttribute("termId") + "," + sesion.getAttribute("yearId") + "," + superComment + ")";
            }
            DBConect.eduweb.executeUpdate(test);

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return "success";
    }

    @RequestMapping("/progressbystudent/updateObjectivesReport.htm")
    @ResponseBody
    public String updateObjectivesReport(@RequestBody DBRecords dbRec, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        String newId = dbRec.getCol1();
        String id_objective = dbRec.getCol2();
        String nameColumn = dbRec.getCol3();
        String idStudent = dbRec.getCol4();
        String yearid = "" + hsr.getSession().getAttribute("yearId");
        String termid = "" + hsr.getSession().getAttribute("termId");
        
        ModelAndView mv = new ModelAndView("progressbystudent");
        try { 
            String test = "update progress_report set "+nameColumn+"=" + newId + ",comment_date = now() where yearterm_id =" +  yearid  + " and term_id =" +termid+ " and objective_id=" + id_objective +" and student_id="+idStudent ;
       //     String ifTest= "select * from progress_report where yearterm_id =" + yearid + " and term_id =" + termid + " and objective_id=" + id_objective +" and student_id="+idStudent;
           
            if (!DBConect.eduweb.executeQuery("select * from progress_report where yearterm_id =" + yearid + " and term_id =" + termid + " and objective_id=" + id_objective +" and student_id="+idStudent).next()) {
                test = "insert into progress_report(comment_date,"+nameColumn+",objective_id,yearterm_id,term_id,student_id)values(now(),"+newId+","+id_objective+","+yearid+","+termid+","+idStudent+")";
            }
            DBConect.eduweb.executeUpdate(test);

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return "success";
    }
    
    
    @RequestMapping("/progcal.htm")
    @ResponseBody
    public ModelAndView progcal(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("progcal");

        String[] output = hsr.getParameter("studentid").split("-");
        String studentId = output[0];
        String nameStudent = "'" + output[1];
        for (int i = 2; i < output.length; i++) {
            nameStudent += "-" + output[i];
        }
        nameStudent += "'";
        //    mv.addObject("message","works");
        String message = "works";
        mv.addObject("studentId", studentId);
        mv.addObject("nameStudent", nameStudent);
        return mv;
    }

    @RequestMapping("/getimage.htm")
    @ResponseBody
    public String getimage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String obsdate = request.getParameter("date");
        String obsid = request.getParameter("id");
        String server = "192.168.1.36";
        int port = 21;
        String user = "david";
        String pass = "david";

        String filePath = "/MontessoriObservations/" + obsid + "/";
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        if (ftpClient.changeWorkingDirectory(filePath)) {
            JSONObject json = new JSONObject();
            String s[] = ftpClient.listNames();
            filePath = s[0];
            InputStream inStream = ftpClient.retrieveFileStream(s[0]);
            // obtains ServletContext
            ServletContext context = request.getServletContext();
            String appPath = context.getRealPath("");
            System.out.println("appPath = " + appPath);

            // gets MIME type of the file
            String mimeType = context.getMimeType(filePath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            //
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] buf = buffer.toByteArray();
            //
            // byte[] buf = new byte[inStream.available()];
            inStream.read(buf);
            String imagen = Base64.getEncoder().encodeToString(buf);
            json.put("imagen", imagen);
            json.put("ext", mimeType);
            ftpClient.disconnect();
            return json.toString();
        }
        ftpClient.disconnect();
        //response.sendRedirect(request.getContextPath());
        return "";
    }

    public static String getNextDate(String curDate) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = format.parse(curDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return format.format(calendar.getTime());
    }

    @RequestMapping("/updateComment.htm")
    public ModelAndView updateComment(@RequestBody Observation r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("lessonresources");
        try {
            DBConect.eduweb.executeUpdate("update classobserv set date_created = now(), comment = '" + r.getObservation() + "' ,category = '" + r.getType() + "', commentdate = '" + r.getDateString() + "' where id = '" + r.getId() + "'");

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    @RequestMapping("/delFoto.htm")
    public ModelAndView delFoto(@RequestBody Resource r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("lessonresources");
        try {
            String commentId = r.getId();

            String consulta = "update classobserv set foto = false where id = " + commentId;
            DBConect.eduweb.executeUpdate(consulta);

            String server = "192.168.1.36";
            int port = 21;
            String user = "david";
            String pass = "david";

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.mkd("/MontessoriObservations/");
            String rutaCompleta = "/MontessoriObservations/" + commentId;

            if (!ftpClient.changeWorkingDirectory(rutaCompleta));
            {
                ftpClient.changeWorkingDirectory("/MontessoriObservations");

                ftpClient.mkd(commentId);
                ftpClient.changeWorkingDirectory(commentId);
                ftpClient.deleteFile(ftpClient.listNames()[0]);
            }

            ftpClient.logout();

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    @RequestMapping("/delComentario.htm")
    public ModelAndView delComment(@RequestBody Resource r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("lessonresources");
        try {
            String commentId = r.getId();
            String consulta = "delete from classobserv where id = " + commentId;

            String server = "192.168.1.36";
            int port = 21;
            String user = "david";
            String pass = "david";

            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);

            ftpClient.changeWorkingDirectory("/MontessoriObservations");
            ftpClient.mkd(commentId);
            ftpClient.changeWorkingDirectory(commentId);
            ftpClient.deleteFile(ftpClient.listNames()[0]);

            ftpClient.removeDirectory("/MontessoriObservations/" + commentId);
            ftpClient.logout();

            DBConect.eduweb.executeUpdate(consulta);
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    private String fetchTeacher(int id, HttpServletRequest hsr) throws Exception {
        String first, LastName, name = "";
        ResultSet rs7 = DBConect.ah.executeQuery("select lastname,firstname from person where personid =" + id);
        while (rs7.next()) {
            first = rs7.getString("firstName");
            LastName = rs7.getString("lastName");
            name = LastName + ", " + first;
        }
        return name;
    }

    @RequestMapping("/loadComentsStudent.htm")
    @ResponseBody
    public String loadComentsStudent(@RequestBody Observation r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        ResultSet rs7 = DBConect.ah.executeQuery("select lastname,firstname,personid from person");
        // ResultSet rs4 = st.executeQuery(consulta);
        HashMap<String, String> mapPersons = new HashMap<String, String>();
        String first, LastName, studentID;
        while (rs7.next()) {
            first = rs7.getString("firstName");
            LastName = rs7.getString("lastName");
            studentID = rs7.getString("personid");
            mapPersons.put(studentID, LastName + ", " + first);
        }

        DateFormat formatoFecha;// = new SimpleDateFormat("M/d/yyyy");
        String date = r.getDateString() + "-01";

        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));
        convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));

        int DIAS_MAX = convertedDate.getDayOfMonth();

        String studentId = "" + r.getStudentid();

        String monthSelected = "" + r.getDateString().charAt(5) + r.getDateString().charAt(6);

        String yearSelected = "" + r.getDateString().charAt(0) + r.getDateString().charAt(1) + r.getDateString().charAt(2) + r.getDateString().charAt(3);
        int days = 0, logId;

        Observation oAux = new Observation();

        formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        String dateSelected = yearSelected + "-" + monthSelected + "-" + "01";

        ArrayList<ArrayList<Observation>> arrayObservations = new ArrayList<ArrayList<Observation>>();
        ArrayList<Observation> arrayComments = new ArrayList<Observation>();
        String consulta = "SELECT * FROM public.classobserv WHERE student_id = " + studentId + " AND commentdate = '" + dateSelected + "'";
        String s;
        try {

            // while (days < DIAS_MAX && !currentDate.equals(dateSelected)) {
            while (days < DIAS_MAX) {
                //dateSelected = getNextDate(dateSelected);
                arrayComments.clear();
                consulta = "SELECT * FROM classobserv WHERE student_id = " + studentId + " AND commentdate = '" + dateSelected + "' ORDER BY commentdate";
                ResultSet rs = DBConect.eduweb.executeQuery(consulta);

                while (rs.next()) {
                    oAux.setId(rs.getInt("id"));
                    logId = rs.getInt("logged_by");
                    oAux.setNameTeacher(mapPersons.get("" + logId));
                    oAux.setLogged_by(logId);
                    oAux.setDate("" + rs.getDate("date_created"));
                    oAux.setObservation(rs.getString("comment"));
                    oAux.setType(rs.getString("category"));
                    oAux.setStudentid(Integer.parseInt(studentId));
                    oAux.setFoto(rs.getBoolean("foto"));
                    Date d = rs.getDate("commentdate");
                    oAux.setCommentDate("" + d);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.setMinimalDaysInFirstWeek(1);
                    oAux.setNumSemana("" + cal.get(Calendar.WEEK_OF_MONTH));

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

    static ArrayList<Objective> getObjectives(String[] subjectid) throws SQLException {
        ArrayList<Objective> objectives = new ArrayList<>();
        try {

            ResultSet rs1 = DBConect.eduweb.executeQuery("select name,id from public.objective where subject_id='" + subjectid[0] + "' and COALESCE(reportcard, FALSE) = FALSE ORDER BY name ASC");
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

    private HashMap<String, String> getnoofplannedlessons() throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try {
            ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT student_id,objective_id,count(*) from (SELECT * FROM lesson_stud_att left join lessons on lesson_stud_att.lesson_id = lessons.id where COALESCE(lessons.archive, FALSE) = FALSE) b group by b.student_id,b.objective_id;");
            while (rs1.next()) {
                result.put(rs1.getInt("student_id") + "_" + rs1.getInt("objective_id"), "" + rs1.getInt("count"));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return result;
    }

    private HashMap<String, String> getnoofarchivedlessons() throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try {
            ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT student_id,objective_id,count(*) from (SELECT * FROM lesson_stud_att left join lessons on lesson_stud_att.lesson_id = lessons.id where archive = TRUE) b group by b.student_id,b.objective_id;");
            while (rs1.next()) {
                result.put(rs1.getInt("student_id") + "_" + rs1.getInt("objective_id"), "" + rs1.getInt("count"));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return result;
    }

    private HashMap<String, String> getfinalrating() throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try {
            ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT rating.name,student_id,objective_id FROM rating inner join (select rating_id,student_id,objective_id from progress_report a where comment_date = (select max(comment_date) from progress_report \n"
                    + "                                                        where student_id = a.student_id AND objective_id =a.objective_id and rating_id not in(6,7))) c ON id = c.rating_id");
            while (rs1.next()) {
                result.put(rs1.getInt("student_id") + "_" + rs1.getInt("objective_id"), rs1.getString("name"));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return result;
    }

    private HashMap<String, String> getpercent() throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        HashMap<Integer, Integer> auxTotal = new HashMap<>();

        try {
            ResultSet rs1 = DBConect.eduweb.executeQuery("select count(id),obj_id from obj_steps group by obj_id");
            while (rs1.next()) {
                auxTotal.put(rs1.getInt("obj_id"), rs1.getInt("count"));
            }

            ResultSet rs2 = DBConect.eduweb.executeQuery("select comment_date,step_id,objective_id,student_id from progress_report a where comment_date = (select max(comment_date) from public.progress_report where student_id = a.student_id AND objective_id = a.objective_id and generalcomment = false) and generalcomment = false");
            while (rs2.next()) {
                String stsdone = rs2.getString("step_id");
                if (stsdone != null && !stsdone.equals("null") && !stsdone.equals("")) {
                    List<String> ste = Arrays.asList(stsdone.split(","));
                    double percent = (ste.size() * 100) / auxTotal.get(rs2.getInt("objective_id"));
                    result.put(rs2.getInt("student_id") + "_" + rs2.getInt("objective_id"), "" + percent);
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

    public String getnoofplannedlessons(String objid, String studid) throws SQLException {
        String result = "";
        try {

            ResultSet rs1 = DBConect.eduweb.executeQuery("select count(id) from lesson_stud_att where student_id = '" + studid + "' and lesson_id in (select id from lessons where objective_id ='" + objid + "' and COALESCE(archive, FALSE) = FALSE);");

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

            ResultSet rs1 = DBConect.eduweb.executeQuery("select count(id) from lesson_stud_att where student_id = '" + studid + "' and lesson_id in (select id from lessons where objective_id ='" + objid + "' and archive = TRUE);");

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
            String consulta = "SELECT rating.name FROM rating where id in"
                    + "(select rating_id from progress_report where student_id = '" + studid + "'"
                    + " AND comment_date = (select max(comment_date)   from public.progress_report "
                    + "where student_id = '" + studid + "' AND objective_id = '" + objid + "' "
                    + " and rating_id not in(6,7)) "
                    + "AND objective_id ='" + objid + "' )";

            ResultSet rs2 = DBConect.eduweb.executeQuery(consulta);
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

            ResultSet rs1 = DBConect.eduweb.executeQuery("select count(id) from obj_steps where obj_id = '" + objid + "'");
            while (rs1.next()) {
                count = rs1.getInt("count");
            }

            ResultSet rs2 = DBConect.eduweb.executeQuery("select comment_date,step_id from progress_report where objective_id='" + objid + "' AND comment_date = (select max(comment_date) from public.progress_report where student_id = '" + studid + "' AND objective_id = '" + objid + "' and generalcomment = false) and generalcomment = false");
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
