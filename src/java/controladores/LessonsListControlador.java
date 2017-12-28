/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

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
public class LessonsListControlador{
    
      Connection cn;
      static Logger log = Logger.getLogger(LessonsListControlador.class.getName());
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
   @RequestMapping("/homepage/loadLessons.htm")
    public ModelAndView loadLessons(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("homepage");
       
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
        mv.addObject("lessonslist", this.getLessons(user.getId(),hsr.getServletContext()));
        mv.addObject("username",user.getName());

        return mv;
    }
//   
    public ArrayList<Lessons> getLessons(int userid,ServletContext servlet) throws SQLException
    {
//        this.conectarOracle();
        ArrayList<Lessons> lessonslist = new ArrayList<>();
        try {
            
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",servlet);
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            
            String consulta = "SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels";
            ResultSet rs2 = st.executeQuery(consulta);
            HashMap<Integer, String> mapLevel = new HashMap<Integer, String>();
            String name;
            int id;
            while(rs2.next()){
                    name = rs2.getString("GradeLevel");
                    id = rs2.getInt("GradeLevelID");
                    mapLevel.put(id,name);	
            }
            
            
            DriverManagerDataSource dataSource2;
            dataSource2 = (DriverManagerDataSource)this.getBean("dataSource",servlet);
            this.cn = dataSource2.getConnection();
            Statement st2 = this.cn.createStatement();
            
            consulta = "SELECT * FROM public.objective";
            ResultSet rs3 = st2.executeQuery(consulta);
            
            HashMap<Integer, String> mapObjective = new HashMap<Integer, String>();
            while(rs3.next()){
                    name = rs3.getString("name");
                    id = rs3.getInt("id");
                    mapObjective.put(id,name);	
            }
            
            
            dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",servlet);
            this.cn = dataSource.getConnection();
            st = this.cn.createStatement();
            
            consulta = "SELECT Title,CourseID FROM AH_ZAF.dbo.Courses";
            ResultSet rs4 = st.executeQuery(consulta);
            HashMap<Integer, String> mapSubject = new HashMap<Integer, String>();
            while(rs4.next()){
                    name = rs4.getString("Title");
                    id = rs4.getInt("CourseID");
                    mapSubject.put(id,name);	
            }
            
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",servlet);
            this.cn = dataSource.getConnection();
            st = this.cn.createStatement();
            
            consulta = "SELECT * FROM public.lessons where user_id = "+userid+" and COALESCE(idea, FALSE) = FALSE and COALESCE(archive, FALSE) = FALSE";
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next())
            {
                Lessons lesson = new Lessons();
              //  lesson.setId(rs.getString("id_lessons"));
                lesson.setName(rs.getString("name"));
                lesson.setId(rs.getInt("id"));
                
                Level level = new Level();
                int idLevel = rs.getInt("level_id");           
               // String name = level.fetchName(rs.getInt("level_id"),servlet);
                name = mapLevel.get(idLevel);
                level.setName(name);
                lesson.setLevel(level);
                
                Objective sub = new Objective();
                int idObjective = rs.getInt("objective_id");       
              //  name = sub.fetchName(rs.getInt("objective_id"), servlet);
                name =  mapObjective.get(idObjective);
                sub.setName(name);
                lesson.setObjective(sub);
                
                Subject subject = new Subject();
                int idSubject = rs.getInt("subject_id");
                name = mapSubject.get(idSubject);
               // name = subject.fetchName(rs.getInt("subject_id"), servlet);
                subject.setName(name);
                lesson.setSubject(subject);
                
                Timestamp stamp = rs.getTimestamp("start");
                Timestamp finish = rs.getTimestamp("finish");
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
                String dateStr = sdfDate.format(stamp);
                String timeStr = sdfTime.format(stamp);
                 
                String timeStr2 = sdfTime.format(finish);
                lesson.setDate(""+ dateStr);
                lesson.setStart(timeStr);
                lesson.setFinish(timeStr2);
                lessonslist.add(lesson);
                
            }
           
            
            this.cn.close();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       
        return lessonslist;
    }
//     
    @RequestMapping("/homepage/deleteLesson.htm")
             @ResponseBody
        public String deleteLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {

        JSONObject jsonObj = new JSONObject();
       String[] id = hsr.getParameterValues("LessonsSelected");
       String message = null;
       try {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
        Statement st = this.cn.createStatement();
        String consulta = "select attendance from lesson_stud_att where lesson_id = "+id[0];
        ResultSet rs = st.executeQuery(consulta);
        
        while(rs.next()){
            String text = rs.getString("attendance");
            if(text!=null){
                 if(text!= null && !text.equals("") && !text.equals("0")){
                    message="Presentation has attendance records,it can not be deleted";
                    break;
                }
            }
        }
         consulta = "select * from progress_Report where lesson_id ="+ id [0];
         ResultSet rs1 = st.executeQuery(consulta);
         while(rs1.next()){
             int check = rs1.getInt("rating_id");
             if(check != 7)//empty rating
             {
         message = "Presentation has progress records,it can not be deleted";
             }
         }
         if(message == null)
         {
           consulta = "DELETE FROM lesson_content WHERE lesson_id="+id[0];
          st.executeUpdate(consulta);
          consulta = "DELETE FROM lesson_stud_att WHERE lesson_id="+id[0];
          st.executeUpdate(consulta);
        consulta = "DELETE FROM public.lessons WHERE id="+id[0];
           st.executeUpdate(consulta);
           message = "Presentation deleted successfully";
         }
        //mv.addObject("lessonslist", this.getLessons(user.getId(),hsr.getServletContext()));
        //mv.addObject("messageDelete",message);
        jsonObj.put("message", message);
       }catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       
        return jsonObj.toString();
        //return mv;
    }
    @RequestMapping("/homepage/editLesson.htm")
      public ModelAndView editLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("homepage");
       String[] id = hsr.getParameterValues("seleccion");
       try {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
         Statement st = this.cn.createStatement();
          
        String consulta = "DELETE FROM public.lessons WHERE id="+id[0];
           st.executeUpdate(consulta);
        mv.addObject("lessonslist", this.getLessons(user.getId(),hsr.getServletContext()));
       }catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       
        return mv;
    }
    @RequestMapping("/homepage/detailsLesson.htm")
      @ResponseBody
        public String detailsLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
    //    ModelAndView mv = new ModelAndView("homepage");
        JSONObject jsonObj = new JSONObject();
       String[] id = hsr.getParameterValues("LessonsSelected");
        ArrayList<Progress> records = new ArrayList<>();
       ArrayList<String> contents = new ArrayList<>();
       try {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
         Statement st = this.cn.createStatement();
          
        String consulta = "select * FROM public.lessons WHERE id="+id[0];
           ResultSet rs = st.executeQuery(consulta);
       while(rs.next())
       {
        Method m = new Method();
        Objective o = new Objective();
       jsonObj.put("method",m.fetchName(rs.getInt("method_id"),hsr.getServletContext()));
       Timestamp date = rs.getTimestamp("date_created");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               String dateStr = sdfDate.format(date);
       jsonObj.put("datecreated",dateStr);
       jsonObj.put("comment",rs.getString("comments"));
       jsonObj.put("objective",o.fetchName(rs.getInt("objective_id"),hsr.getServletContext()));
       }
       consulta = "select name from content where id in (select content_id from lesson_content where lesson_id = "+id[0]+")";
       ResultSet rs1 = st.executeQuery(consulta);
       while(rs1.next())
       {
           contents.add(rs1.getString("name"));
       }
       jsonObj.put("contents",new Gson().toJson(contents));
       consulta = "SELECT * FROM public.lesson_stud_att where lesson_id ="+id[0];
       ResultSet rs2 = st.executeQuery(consulta);
          
            while (rs2.next())
            {
                Progress att = new Progress();
             
                att.setStudentid(rs2.getInt("student_id"));
                records.add(att);
            }
            cn.close();
            dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            st = this.cn.createStatement();
            consulta = "SELECT FirstName,LastName,MiddleName,StudentID FROM AH_ZAF.dbo.Students ";
            ResultSet rs3 = st.executeQuery(consulta);
            HashMap<String, String> map = new HashMap<String, String>();
            String first,LastName,middle,studentID;
            while(rs3.next()){
                    first = rs3.getString("FirstName");
                    LastName =rs3.getString("LastName");
                    middle = rs3.getString("MiddleName");
                    studentID = rs3.getString("StudentID");
                    map.put(studentID, LastName+", "+first+" "+middle);	
            }
            for(Progress record : records)
            {
                  String id2 = ""+record.getStudentid();
                  String name = map.get(id2);
                  record.setStudentname(name);
            }
            jsonObj.put("students",new Gson().toJson(records));
       }catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       
        
        return jsonObj.toString();
    }
}