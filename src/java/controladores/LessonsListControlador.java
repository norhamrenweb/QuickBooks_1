/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import java.sql.*;
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
lesson.setName(hsr.getParameter("LessonsSelected"));
lesson.setId(lessonselectid);
        mv.addObject("lessonslist1", this.getLessonsTime(lesson,hsr.getServletContext()));
        
        
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
                Subsection sub = new Subsection();
                name = sub.fetchName(rs.getInt("id_subsection"), servlet);
                sub.setName(name);
                lesson.setSubsection(sub);
                Subject subject = new Subject();
                name = subject.fetchName(rs.getInt("id_subject"), servlet);
                subject.setName(name);
                lesson.setSubject(subject);
                
                lessonslist.add(lesson);
                
            }
            //this.finalize();
            
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
          
            while (rs.next())
            {
                Lessons lesson = new Lessons();
              //  lesson.setId(rs.getString("id_lessons"));
                lesson.setName(lessonselected.getName());
                lesson.setDate(consulta);
                lessonslist.add(lesson);
                
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return lessonslist;
     
     }
}