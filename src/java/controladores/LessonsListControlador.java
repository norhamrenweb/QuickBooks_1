/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
   @RequestMapping("/homepage/loadLessons.htm")
    public ModelAndView loadLessons(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
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
//    public ModelAndView loadLessonsTime(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
//        
//        ModelAndView mv = new ModelAndView("homepage");
//       
//        DriverManagerDataSource dataSource;
//        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
//        this.cn = dataSource.getConnection();
////        HttpSession sesion = hsr.getSession();
////        User user = (User) sesion.getAttribute("user");
//int lessonselectid = Integer.parseInt(hsr.getParameter("LessonsSelected"));
////String[] lessonselectid = hsr.getParameterValues("LessonsSelected");
//Lessons lesson = new Lessons();
////lesson.setName(hsr.getParameter("LessonsSelected"));
//lesson.setId(lessonselectid);
//ArrayList<Lessons> lessonslist =  this.getLessonsTime(lesson,hsr.getServletContext());
//        mv.addObject("lessonslist1",lessonslist);
        
//        
//        return mv;
//    }
    
    public ArrayList<Lessons> getLessons(int userid,ServletContext servlet) throws SQLException
    {
//        this.conectarOracle();
        ArrayList<Lessons> lessonslist = new ArrayList<>();
        try {
            
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM public.lessons where user_id = "+userid+" and COALESCE(idea, FALSE) = FALSE";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Lessons lesson = new Lessons();
              //  lesson.setId(rs.getString("id_lessons"));
                lesson.setName(rs.getString("name"));
                lesson.setId(rs.getInt("id"));
                Level level = new Level();
                String name = level.fetchName(rs.getInt("level_id"),servlet);
               level.setName(name);
                lesson.setLevel(level);
                Objective sub = new Objective();
                name = sub.fetchName(rs.getInt("objective_id"), servlet);
                sub.setName(name);
                lesson.setObjective(sub);
                Subject subject = new Subject();
                name = subject.fetchName(rs.getInt("subject_id"), servlet);
                subject.setName(name);
                lesson.setSubject(subject);
                 Timestamp stamp = rs.getTimestamp("start");
               Timestamp finish = rs.getTimestamp("finish");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
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
        }
       
        return lessonslist;
    }
//     public ArrayList<Lessons> getLessonsTime(Lessons lessonselected,ServletContext servlet) throws SQLException
//     {
//       ArrayList<Lessons> lessonslist = new ArrayList<>();
//        try {
//            
//             Statement st = this.cn.createStatement();
//             
//            String consulta = "SELECT * FROM public.lessons_time where lesson_id = "+ lessonselected.getId();
//            ResultSet rs = st.executeQuery(consulta);
//          //must put here if the result set is empty return that the lesson is not scheduled yet
//            while (rs.next())
//            {
//                Lessons lesson = new Lessons();
//              //  lesson.setId(rs.getString("id_lessons"));
//               // lesson.setName(lessonselected.getName());
//               Timestamp stamp = rs.getTimestamp("lesson_start");
//               Timestamp finish = rs.getTimestamp("lesson_end");
//               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//               SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
//               String dateStr = sdfDate.format(stamp);
//                String timeStr = sdfTime.format(stamp);
//                 
//                String timeStr2 = sdfTime.format(finish);
//                lesson.setDate(""+ dateStr);
//                lesson.setStart(timeStr);
//                lesson.setFinish(timeStr2);
//                lessonslist.add(lesson);
//                
//            }
//          
//            consulta = "select * from public.lessons_students where id_lessons = "+ lessonselected.getId() ;
//            ResultSet rs2 = st.executeQuery(consulta);
//            ArrayList<Students> studentlist = new ArrayList<>();
//            
//            while(rs2.next())
//            {Students student = new Students();
//            student.setId_students(rs2.getInt("id_students"));
//            studentlist.add(student);
//            }
//            //this.finalize();
//            this.cn.close();
//            DriverManagerDataSource dataSource;
//        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",servlet);
//        this.cn = dataSource.getConnection();
//        Statement st1 = this.cn.createStatement();
//        int i = 0;
//        for (Students s :studentlist){
//        ResultSet rs3 = st1.executeQuery("select FirstName,LastName from AH_ZAF.dbo.Students where StudentID = "+s.getId_students());
//                while(rs3.next()){
//                 s.setNombre_students(rs3.getString("FirstName")+","+rs3.getString("LastName"));
//                 Lessons l = new Lessons();
//                 l = lessonslist.get(i);
//                 l.setStudents(studentlist);
//        }
//        
//        }
//        } catch (SQLException ex) {
//            System.out.println("Error leyendo Alumnos: " + ex);
//        }
//       
//        return lessonslist;
//     
//     }
    @RequestMapping("/homepage/deleteLesson.htm")
             @ResponseBody
        public String deleteLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    //public ModelAndView deleteLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        //ModelAndView mv = new ModelAndView("homepage");
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
             if(text != null){
                 message="Presentation has attendance records,it can not be deleted";
                 break;
             }
         }
         consulta = "select * from progress_Report where lesson_id ="+ id [0];
         ResultSet rs1 = st.executeQuery(consulta);
         if(rs1.next()){
         message = "Presentation has progress records,it can not be deleted";
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
        }
       
        return jsonObj.toString();
        //return mv;
    }
    @RequestMapping("/homepage/editLesson.htm")
      public ModelAndView editLesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
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
       jsonObj.put("method",m.fetchName(rs.getInt("method_id"),hsr.getServletContext()));
       Timestamp date = rs.getTimestamp("date_created");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               String dateStr = sdfDate.format(date);
       jsonObj.put("datecreated",dateStr);
       jsonObj.put("comment",rs.getString("comments"));
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
            for(Progress record : records)
            {
            consulta = "SELECT FirstName,LastName FROM AH_ZAF.dbo.Students where StudentID = '"+record.getStudentid()+"'";
            ResultSet rs3 = st.executeQuery(consulta);
            while (rs3.next())
            {
              record.setStudentname(rs3.getString("FirstName")+","+rs3.getString("LastName"));
            }
            }
            jsonObj.put("students",new Gson().toJson(records));
       }catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
       
        
        return jsonObj.toString();
    }
}