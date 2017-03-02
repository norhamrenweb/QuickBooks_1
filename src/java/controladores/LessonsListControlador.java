/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
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
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author nmohamed
 */
public class LessonsListControlador extends MultiActionController{
    
      Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
   
    public ModelAndView loadLessons(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("homepage");
       
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
        mv.addObject("lessonslist", this.getLessons(user.getId(),hsr.getServletContext()));
        
        
        return mv;
    }
    public ModelAndView loadLessonsTime(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("homepage");
       
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
//        HttpSession sesion = hsr.getSession();
//        User user = (User) sesion.getAttribute("user");
int lessonselectid = Integer.parseInt(hsr.getParameter("LessonsSelected"));
//String[] lessonselectid = hsr.getParameterValues("LessonsSelected");
Lessons lesson = new Lessons();
//lesson.setName(hsr.getParameter("LessonsSelected"));
lesson.setId(lessonselectid);
ArrayList<Lessons> lessonslist =  this.getLessonsTime(lesson,hsr.getServletContext());
        mv.addObject("lessonslist1",lessonslist);
        
        
        return mv;
    }
    public ArrayList<Lessons> getLessons(int userid,ServletContext servlet) throws SQLException
    {
//        this.conectarOracle();
        ArrayList<Lessons> lessonslist = new ArrayList<>();
        try {
            
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM \"public\".lessons where id_usuario = "+userid;
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Lessons lesson = new Lessons();
              //  lesson.setId(rs.getString("id_lessons"));
                lesson.setName(rs.getString("nombre_lessons"));
                lesson.setId(rs.getInt("id_lessons"));
                Level level = new Level();
                String name = level.fetchName(rs.getInt("id_level"),servlet);
               level.setName(name);
                lesson.setLevel(level);
                Objective sub = new Objective();
                name = sub.fetchName(rs.getInt("id_subsection"), servlet);
                sub.setName(name);
                lesson.setSubsection(sub);
                Subject subject = new Subject();
                name = subject.fetchName(rs.getInt("id_subject"), servlet);
                subject.setName(name);
                lesson.setSubject(subject);
                
                lessonslist.add(lesson);
                
            }
            for(Lessons x:lessonslist)
            {
           
             
            consulta = "SELECT * FROM public.lessons_time where lesson_id = "+ x.getId();
            ResultSet rs1 = st.executeQuery(consulta);
          //must put here if the result set is empty return that the lesson is not scheduled yet
            while (rs1.next())
            {
//               
               Timestamp stamp = rs1.getTimestamp("lesson_start");
               Timestamp finish = rs1.getTimestamp("lesson_end");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
               String dateStr = sdfDate.format(stamp);
                String timeStr = sdfTime.format(stamp);
                 
                String timeStr2 = sdfTime.format(finish);
                x.setDate(""+ dateStr);
                x.setStart(timeStr);
                x.setFinish(timeStr2);
               
                
            }
            }
            
            this.cn.close();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return lessonslist;
    }
     public ArrayList<Lessons> getLessonsTime(Lessons lessonselected,ServletContext servlet) throws SQLException
     {
       ArrayList<Lessons> lessonslist = new ArrayList<>();
        try {
            
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM public.lessons_time where lesson_id = "+ lessonselected.getId();
            ResultSet rs = st.executeQuery(consulta);
          //must put here if the result set is empty return that the lesson is not scheduled yet
            while (rs.next())
            {
                Lessons lesson = new Lessons();
              //  lesson.setId(rs.getString("id_lessons"));
               // lesson.setName(lessonselected.getName());
               Timestamp stamp = rs.getTimestamp("lesson_start");
               Timestamp finish = rs.getTimestamp("lesson_end");
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
          
            consulta = "select * from public.lessons_students where id_lessons = "+ lessonselected.getId() ;
            ResultSet rs2 = st.executeQuery(consulta);
            ArrayList<Students> studentlist = new ArrayList<>();
            
            while(rs2.next())
            {Students student = new Students();
            student.setId_students(rs2.getInt("id_students"));
            studentlist.add(student);
            }
            //this.finalize();
            this.cn.close();
            DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",servlet);
        this.cn = dataSource.getConnection();
        Statement st1 = this.cn.createStatement();
        int i = 0;
        for (Students s :studentlist){
        ResultSet rs3 = st1.executeQuery("select FirstName,LastName from AH_ZAF.dbo.Students where StudentID = "+s.getId_students());
                while(rs3.next()){
                 s.setNombre_students(rs3.getString("FirstName")+","+rs3.getString("LastName"));
                 Lessons l = new Lessons();
                 l = lessonslist.get(i);
                 l.setStudents(studentlist);
        }
        
        }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return lessonslist;
     
     }
}