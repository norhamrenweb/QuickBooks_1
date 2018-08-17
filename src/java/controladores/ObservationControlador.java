/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.CommentObjective;
import Montessori.DBConect;
import static Montessori.DBConect.eduweb;
import Montessori.DBRecords;
import Montessori.Level;
import Montessori.Objective;
import Montessori.Observation;
import Montessori.Resource;
import Montessori.Step;
import Montessori.Students;
import Montessori.Subject;
import Montessori.Teacher;
import Montessori.User;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.util.Base64;
import static controladores.ProgressbyStudent.log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Norhan
 */
@Controller
public class ObservationControlador {

    static Logger log = Logger.getLogger(ObservationControlador.class.getName());

    @RequestMapping("/observations/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        HashMap<String, String> mapPersons = new HashMap<String, String>();
        ModelAndView mv = new ModelAndView("observations");
        List<Level> grades = new ArrayList();
        try {
            mv.addObject("listaAlumnos", Students.getStudents(log));
            ResultSet rs = DBConect.ah.executeQuery("SELECT GradeLevel,GradeLevelID FROM GradeLevels order by gradelevel ASC");

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

            ResultSet rs7 = DBConect.ah.executeQuery("select * from Staff");
            // ResultSet rs4 = st.executeQuery(consulta);

            String first, lastName, staffID;
            while (rs7.next()) {
                first = rs7.getString("firstName");
                lastName = rs7.getString("lastName");
                staffID = rs7.getString("personid");
                mapPersons.put(staffID, lastName + ", " + first);
            }

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }

        mv.addObject("gradelevels", grades);
        mv.addObject("students", Students.getStudents(log));
        mv.addObject("teachers", new Gson().toJson(mapPersons));
        return mv;
    }

    @RequestMapping("/observations/studentslevel.htm")
    @ResponseBody
    public String getstudents(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String id = hsr.getParameter("idgrade");
        JSONObject json = new JSONObject();
        String subjects = new Gson().toJson(Students.getStudentslevel(id, log));
        json.put("subjects", subjects);
        return json.toString();
    }

    /**
     * funcion que devuelve dado un id de estudiante los subjects
     *
     * @param hsr
     * @param hsr1
     * @return
     * @throws Exception
     */
    @RequestMapping("/observations/subjects.htm")
    @ResponseBody
    public String getsubjects(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String id = hsr.getParameter("idstudent");
        JSONObject json = new JSONObject();
        List<Subject> subs = ProgressbyStudent.getSubjects(Integer.parseInt(id), hsr,"2","59");
        Subject sub = new Subject();
        sub.setName("Select Subject");
        String[] s = new String[1];
        s[0] = "vacio";
        sub.setId(s);
        subs.add(0, sub);
        String subjects = new Gson().toJson(subs);
        json.put("subjects", subjects);
        return json.toString();
    }

    /**
     * Esta funcion devuelve dado un id de un subject sus objetivos
     *
     * @param hsr
     * @param hsr1
     * @return
     * @throws Exception
     */
    @RequestMapping("/observations/objectives.htm")
    @ResponseBody
    public String getobjectives(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String id[] = hsr.getParameterValues("idsubject");
        JSONObject json = new JSONObject();
        ArrayList<Objective> obj = ProgressbyStudent.getObjectives(id);
        Objective objective = new Objective();
        String[] s = new String[1];
        s[0] = "vacio";
        objective.setId(s);
        objective.setName("Select Objective");
        obj.add(0, objective);
        String objectives = new Gson().toJson(obj);
        json.put("objectives", objectives);
        return json.toString();
    }

    /**
     * funcion que devuelve los comentarios dado un objetivo y un id de
     * estudiante
     *
     * @param hsr
     * @param hsr1
     * @return
     */
    @RequestMapping("/observations/comments.htm")
    @ResponseBody
    public String getComments(HttpServletRequest hsr, HttpServletResponse hsr1) {
        String idstudent = hsr.getParameter("idstudent");
        String idobjective = hsr.getParameter("idobjective");
        ArrayList<CommentObjective> comments = new ArrayList<>();
        HttpSession sesion;
        sesion = hsr.getSession();
 
        String termId = "" + hsr.getParameter("termId");
        String yearId = "" + hsr.getParameter("yearId");
        JSONObject json = new JSONObject();
        try {
            HashMap<Integer, String> lessons = new HashMap<>();
            String consulta = "select id,name from lessons where term_id=" + termId + " and yearterm_id=" + yearId;
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                lessons.put(rs.getInt(1), rs.getString(2));
            }

            consulta = "select * from progress_report inner join rating on progress_report.rating_id = rating.id where objective_id=" + idobjective + " and student_id=" + idstudent + " and term_id=" + termId + " and yearterm_id=" + yearId + /*" and lesson_id is null*/" ORDER BY comment_date DESC";
            rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                String auxName ="";
                if( lessons.containsKey(rs.getInt("lesson_id")))
                    auxName = lessons.get(rs.getInt("lesson_id"));
                
                comments.add(new CommentObjective(rs.getString("id"),
                                rs.getString("rating_id"), rs.getString("student_id"),
                                rs.getString("comment"), rs.getString("comment_date"),
                                rs.getString("objective_id"), rs.getBoolean("generalcomment"),
                                rs.getString("step_id"), rs.getString("createdby"),
                                rs.getString("modifyby"), rs.getString("term_id"),
                                rs.getString("yearterm_id"), rs.getString("colorcode"),
                                auxName));
            }
            for (CommentObjective c : comments) {
                ResultSet rs1 = DBConect.eduweb.executeQuery("select name from rating where id = " + c.getRating_id() + "");
                while (rs1.next()) {
                    c.setRating_name(rs1.getString("name"));
                }
            }

            List<Step> steps = new ArrayList<>();
            ResultSet rs3 = DBConect.eduweb.executeQuery("select name,id,storder from obj_steps where obj_id =" + idobjective);
            while (rs3.next()) {
                Step s = new Step();
                s.setName(rs3.getString("name"));
                s.setId("" + rs3.getInt("id"));
                s.setOrder(rs3.getInt("storder"));
                steps.add(s);
            }
            String recommend = "false";

            ResultSet rs2 = DBConect.eduweb.executeQuery("select * from recommendations where  id_student = " + idstudent + " and id_objective=" + idobjective);
            if (rs2.next()) {
                recommend = "true";
            }

            json.put("steps", new Gson().toJson(steps));
            json.put("comments", new Gson().toJson(comments));
            json.put("recommend", recommend);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return json.toString();
    }

    /**
     * crea un nuevo comentario
     *
     * @param hsr
     * @param hsr1
     * @return
     */
    @RequestMapping("/observations/newcomment.htm")
    @ResponseBody
    public String newComment(HttpServletRequest hsr, HttpServletResponse hsr1) {
        String idstudent = hsr.getParameter("idstudent");
        String idobjective = hsr.getParameter("idobjective");
        String comment = hsr.getParameter("comment");
        comment = comment.replace("'", "\'\'");
        comment = comment.replace("\"", "\"\"");
        String rating = hsr.getParameter("rating");
        String step = hsr.getParameter("step");
        String cbUseGrade = hsr.getParameter("cbUseGrade");
        
        String termId = "" + hsr.getParameter("termId");
        String yearId = "" + hsr.getParameter("yearId");
        
        HttpSession sesion;
        sesion = hsr.getSession();
        
    
        User user = (User) sesion.getAttribute("user");
        String ratingid = null;
        try {
            ResultSet rs2 = DBConect.eduweb.executeQuery("select id from obj_steps where obj_id = '" + idobjective + "' order by storder");
            ArrayList<String> allsteps = new ArrayList();
            while (rs2.next()) {
                allsteps.add("" + rs2.getInt("id"));
            }
            if (!step.equals("0") && !allsteps.isEmpty() && !step.equals("")) {
                ArrayList<String> al2 = new ArrayList<String>(allsteps.subList(0, (Integer.parseInt(step))));
                StringBuilder rString = new StringBuilder();

                String sep = ",";
                for (String each : al2) {
                    rString.append(each).append(sep);
                }
                step = rString.toString();
                step = step.substring(0, step.length() - 1);
            }
           // checkFirstCommentLessons(idstudent, idobjective, comment, ratingid, step, "" + sesion.getAttribute("yearId"), "" + sesion.getAttribute("termId"), "" + user.getId(), hsr);

            String consulta = "select id from rating where name = '" + rating + "'";
            ResultSet rs1 = DBConect.eduweb.executeQuery(consulta);
            while (rs1.next()) {
                ratingid = "" + rs1.getInt("id");
            }
            if (ratingid != null) {
                DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,comment,rating_id,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id) values (now(),'" + comment + "','" + ratingid + "','" + idstudent + "','" + idobjective + "'," + cbUseGrade + ",'" + step + "','" + user.getId() + "'," + termId + "," + yearId + ")");
            } else {
                DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,comment,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id) values (now(),'" + comment + "','" + idstudent + "','" + idobjective + "'," + cbUseGrade + ",'" + step + "','" + user.getId() + "'," + termId + "," + yearId + ")");
            }

           /* if (!cbUseGrade.equals("true")) {
                String aux = "UPDATE progress_report"
                        + " SET rating_id=" + ratingid + " , step_id= '" + step
                        + "' WHERE objective_id=" + idobjective + " and lesson_id is not null and student_id=" + idstudent + " and term_id=" + sesion.getAttribute("termId") + " and yearterm_id=" + sesion.getAttribute("yearId");
                DBConect.eduweb.executeUpdate(aux);
            }*/
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return "error";
        }
        return "succes";
    }

    private void checkFirstCommentLessons(String idStudent, String idObjective, String comment, String rating, String step, String yearId, String termId, String userId, HttpServletRequest hsr) throws SQLException {

        String auxCheck1 = "select * from  progress_report where objective_id=" + idObjective + " and lesson_id is not null and student_id=" + idStudent + " and term_id=" + termId + " and yearterm_id=" + yearId;
        ResultSet rs3 = DBConect.eduweb.executeQuery(auxCheck1);
        ArrayList<DBRecords> list = new ArrayList<>();
        DBRecords auxDB = new DBRecords();
        ArrayList<Integer> listLessonWithoutProgress = new ArrayList<>();

        while (rs3.next()) {
            auxDB.setCol1("" + rs3.getInt("rating_id"));//rating 
            auxDB.setCol2("" + rs3.getInt("lesson_id"));//lesson
            auxDB.setCol3(rs3.getString("comment"));//comment
            auxDB.setCol4(rs3.getString("step_id"));//step_id
            auxDB.setCol5("" + rs3.getInt("createdby"));//created by
            auxDB.setCol6("" + rs3.getInt("level_id"));
            list.add(new DBRecords(auxDB));
        }

        String auxCheck2 = "select id from lessons where id NOT IN (select lesson_id from progress_report where lesson_id is not null and  student_id=" + idStudent + " and term_id=" + termId + " and yearterm_id=" + yearId+") and objective_id=" + idObjective;
        rs3 = DBConect.eduweb.executeQuery(auxCheck2);
        while (rs3.next()) {
            listLessonWithoutProgress.add(rs3.getInt(1));
        }
        for (Integer listLesson1 : listLessonWithoutProgress) {
            DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,rating_id,comment,lesson_id,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id) values (now(),7,' ','" + listLesson1 + "','" + idStudent + "','" + idObjective + "',false,'','" + userId + "'," + termId + "," + yearId + ")");
            //clone
            DBConect.eduweb.executeUpdate("insert into progress_report(comment_date,rating_id,comment,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id,linklesson_id) values (now(),7,' ','" + idStudent + "','" + idObjective + "',false,'','" + userId + "'," + termId + "," + yearId + "," + listLesson1 + ")");

        }
        for (DBRecords list1 : list) {

            String checkLink = "select * from  progress_report where linklesson_id=" + list1.getCol2() +"  and  student_id=" + idStudent + " and term_id=" + termId + " and yearterm_id=" + yearId;
//            ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(hsr.getServletContext());
//            Object beanobject = contexto.getBean("dataSource");
//            
//            DriverManagerDataSource dataSource =  (DriverManagerDataSource) beanobject;
//            Connection c =  dataSource.getConnection();
//            Statement stat_aux = c.createStatement();
//
            ResultSet rs4 = DBConect.eduweb.executeQuery(checkLink);
            if (!rs4.next()) { // noe existe el comentario asociado
                String testClone = "insert into progress_report"
                        + "(comment_date,comment,rating_id,student_id,objective_id,generalcomment,step_id,createdby,term_id,yearterm_id,linklesson_id)"
                        + " values (now(),'" + list1.getCol3() + "','" + list1.getCol1() + "','" + idStudent + "','" + idObjective
                        + "',false,'" + list1.getCol4() + "','" + list1.getCol5() + "'," + termId + "," + yearId
                        + "," + list1.getCol2() + ")";
                DBConect.eduweb.executeUpdate(testClone);

            } else { // existe

            }
        }

    }

    @RequestMapping("/observations/loadComentsStudent.htm")
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

    public static String getNextDate(String curDate) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = format.parse(curDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return format.format(calendar.getTime());
    }

    @RequestMapping("/observations/delComentario.htm")
    public ModelAndView delComment(@RequestBody Resource r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("lessonresources");
        try {
            String commentId = r.getId();

            String consulta = "delete from classobserv where id = " + commentId;
            DBConect.eduweb.executeUpdate(consulta);
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
            if (ftpClient.listNames().length > 0) {
                ftpClient.deleteFile(ftpClient.listNames()[0]);
            }

            ftpClient.changeWorkingDirectory("/MontessoriObservations");
            ftpClient.removeDirectory(commentId);
        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return mv;
    }

    @RequestMapping("/observations/getimage.htm")
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

    @RequestMapping("/observations/studentlistLevel.htm")
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

    @RequestMapping("/observations/recommendStudent.htm")
    @ResponseBody
    public String recommendStudent(@RequestBody Resource r, HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        try {
            String objId = r.getId();
            String studentId = r.getName();
          
            String termid = r.getLink();
            String yearterm_id = r.getType();

            String consulta = "insert into recommendations(id_student,id_objective,term_id,yearterm_id) values (" + studentId + "," + objId + "," + termid + "," + yearterm_id + ")";
            ResultSet rs2 = DBConect.eduweb.executeQuery("select * from recommendations where  id_student = " + studentId + " and id_objective=" + objId);
            if (rs2.next()) {//existe
                consulta = "delete from recommendations where id_student = " + studentId + " and id_objective=" + objId;
            }
            DBConect.eduweb.executeUpdate(consulta);

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
        return "success";
    }

    @RequestMapping("/observations/delFoto.htm")
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

    @RequestMapping("/observations/editcomment.htm")
    @ResponseBody
    public String editcomment(HttpServletRequest hsr, HttpServletResponse hsr1) {
        String idcomment = hsr.getParameter("idcomment");
        String idstudent = hsr.getParameter("idstudent");
        String idobjective = hsr.getParameter("idobjective");
        String comment = hsr.getParameter("comment");
        comment = comment.replace("'", "\'\'");
        comment = comment.replace("\"", "\"\"");
        String rating = hsr.getParameter("rating");
        String step = hsr.getParameter("step");
        HttpSession sesion;
        sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
        String ratingid = null;
        try {
            ResultSet rs2 = DBConect.eduweb.executeQuery("select id from obj_steps where obj_id = '" + idobjective + "' order by storder");
            ArrayList<String> allsteps = new ArrayList();
            while (rs2.next()) {
                allsteps.add("" + rs2.getInt("id"));
            }
            if (!step.equals("0") && !allsteps.isEmpty() && !step.equals("")) {
                ArrayList<String> al2 = new ArrayList<String>(allsteps.subList(0, (Integer.parseInt(step))));
                StringBuilder rString = new StringBuilder();

                String sep = ",";
                for (String each : al2) {
                    rString.append(each).append(sep);
                }
                step = rString.toString();
                step = step.substring(0, step.length() - 1);
            }
            String consulta = "select id from rating where name = '" + rating + "'";
            ResultSet rs1 = DBConect.eduweb.executeQuery(consulta);
            while (rs1.next()) {
                ratingid = "" + rs1.getInt("id");
            }
            if (ratingid != null) {
                consulta = "update progress_report set comment = '" + comment + "',rating_id='" + ratingid + "' ,student_id='" + idstudent + "',objective_id='" + idobjective + "',step_id='" + step + "' where id=" + idcomment;
                DBConect.eduweb.executeUpdate(consulta);
            } else {
                consulta = "update progress_report set comment = '" + comment + "' ,student_id='" + idstudent + "',objective_id='" + idobjective + "',step_id='" + step + "' where id=" + idcomment;
                DBConect.eduweb.executeUpdate(consulta);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return "error";
        }
        return "succes";
    }

    /**
     * borra un comentario existente
     *
     * @param hsr
     * @param hsr1
     * @return
     */
    @RequestMapping("/observations/delcomment.htm")
    @ResponseBody
    public String delComment(HttpServletRequest hsr, HttpServletResponse hsr1) {
        String id = hsr.getParameter("id");
        try {
            DBConect.eduweb.executeUpdate("delete from progress_report where id=" + id);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return "error";
        }
        return "succes";
    }

}
