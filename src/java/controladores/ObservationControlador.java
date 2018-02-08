/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.DBConect;
import Montessori.Level;
import Montessori.Students;
import Montessori.Subject;
import atg.taglib.json.util.JSONArray;
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
    
    @RequestMapping("/observations/subjects.htm")
    @ResponseBody
    public String subjects(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String id = hsr.getParameter("idstudent");
        JSONObject json = new JSONObject();
        JSONArray ar = new JSONArray();
        String subjects = new Gson().toJson(ProgressbyStudent.getSubjects(Integer.parseInt(id)));
        json.put("subjects", subjects);
        return json.toString();
    }
    
}
