/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.CommentObjective;
import Montessori.DBConect;
import Montessori.Level;
import Montessori.Objective;
import Montessori.Step;
import Montessori.Students;
import Montessori.Subject;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
import static controladores.ProgressbyStudent.log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
        ModelAndView mv = new ModelAndView("observations");
        List<Level> grades = new ArrayList();
        try {
            mv.addObject("listaAlumnos", Students.getStudents(log));
            ResultSet rs = DBConect.ah.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");

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
        mv.addObject("students", Students.getStudents(log));
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
        List<Subject> subs = ProgressbyStudent.getSubjects(Integer.parseInt(id));
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
        obj.add(0,objective);
        String objectives = new Gson().toJson(obj);
        json.put("objectives", objectives);
        return json.toString();
    }
    
    
    /**
     * funcion que devuelve los comentarios dado un objetivo y un id de estudiante
     * @param hsr
     * @param hsr1
     * @return 
     */
    @RequestMapping("/observations/comments.htm")
    @ResponseBody
    public String getComments(HttpServletRequest hsr, HttpServletResponse hsr1){
        String idstudent = hsr.getParameter("idstudent");
        String idobjective = hsr.getParameter("idobjective");
        ArrayList<CommentObjective> comments = new ArrayList<>();
        JSONObject json = new JSONObject();
        try {
            String consulta = "select * from progress_report where objective_id="+idobjective+" and student_id="+idstudent;
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            while(rs.next()){
                CommentObjective c = 
                        new CommentObjective(rs.getString("id"),
                                rs.getString("rating_id"),rs.getString("student_id"),
                                rs.getString("comment"),rs.getString("comment_date"),
                                rs.getString("objective_id"),rs.getBoolean("generalcomment"),
                                rs.getString("step_id"),rs.getString("createdby"),
                                rs.getString("modifyby"), rs.getString("term_id"),
                                rs.getString("yearterm_id"));
                comments.add(c);
            }
            for(CommentObjective c:comments){
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
            json.put("steps", new Gson().toJson(steps));
            json.put("comments",new Gson().toJson(comments));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return json.toString();
    }
    
    /**
     * crea un nuevo comentario
     * @param hsr
     * @param hsr1
     * @return 
     */
    @RequestMapping("/observations/newcomment.htm")
    @ResponseBody
    public String newComment(HttpServletRequest hsr, HttpServletResponse hsr1){
        String idstudent = hsr.getParameter("idstudent");
        String idobjective = hsr.getParameter("idobjective");
        String comment = hsr.getParameter("comment");
        try {
            String consulta = "insert into progress_report (student_id,objective_id,comment,comment_date,generalcomment)"+
                              "values("+idstudent+","+idobjective+",'"+comment+"',now(),true);";
            DBConect.eduweb.executeUpdate(consulta);
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ObservationControlador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return "error";
        }
        return "succes";
    }
    
    
}
