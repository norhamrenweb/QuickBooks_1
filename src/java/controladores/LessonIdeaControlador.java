/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import Montessori.Level;
import atg.taglib.json.util.JSONObject;
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

/**
 *
 * @author nmohamed
 */
@Controller
public class LessonIdeaControlador {
     Connection cn;
     private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    @RequestMapping("/lessonidea/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    ModelAndView mv = new ModelAndView("lessonidea");
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        ResultSet rs = st.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");
        List <Level> grades = new ArrayList();
        Level l = new Level();
        l.setName("Select level");
        grades.add(l);
        while(rs.next())
        {
            Level x = new Level();
             String[] ids = new String[1];
             ids[0]=""+rs.getInt("GradeLevelID");
            x.setId(ids);
            x.setName(rs.getString("GradeLevel"));
        grades.add(x);
        }
    mv.addObject("levels", grades);
    return mv;
    }
    @RequestMapping("/lessonidea/loadtree.htm")
    @ResponseBody
    public String loadtree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    ModelAndView mv = new ModelAndView("lessonidea");
     JSONObject json = new JSONObject();
      ArrayList<DBRecords> lessons = new ArrayList<>();
    try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
       
       
        Statement st = this.cn.createStatement();
        String[] lessonid = hsr.getParameterValues("seleccion1");
        ResultSet rs = st.executeQuery("SELECT lessons.id,lessons.subject_id,lessons.objective_id,objective.name as obj,lessons.name FROM lessons inner join objective on lessons.objective_id = objective.id where lessons.level_id= "+lessonid[0]+" and lessons.idea = true ");
        while(rs.next())
        {
            DBRecords l = new DBRecords();
            l.setCol1(""+rs.getInt("id"));
            l.setCol2(rs.getString("name"));
            l.setCol4(rs.getString("obj"));
            l.setCol3(""+rs.getInt("subject_id"));
         //   l.setCol5(rs.getString("obj"));
//            String[] sid = new String[1];
//            String[] oid = new String[1];
//            Subject s = new Subject();
//            sid[0] = ""+rs.getInt("subject_id");
            //s.setId(sid);
//            l.setSubject(s);
//            Objective o = new Objective();
//            oid[0] = ""+rs.getInt("objective_id");
//            o.setId(oid);
//            o.setName(rs.getString("obj"));
//            l.setObjective(o);
            lessons.add(l); 
          
        }
        for (DBRecords x :lessons)
        {
            Subject s = new Subject();
            String id = null;
            id = x.getCol3();
            x.setCol3(""+s.fetchName(Integer.parseInt(id), hsr.getServletContext()));
            
        }
  
    }catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
    String test = new Gson().toJson(lessons);
    return test;
    }
}
