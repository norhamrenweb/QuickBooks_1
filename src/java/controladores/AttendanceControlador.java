/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author nmohamed
 */
public class AttendanceControlador extends MultiActionController{
    
    
      Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
   
    
    public ModelAndView loadAttendance(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("lessonattendance");
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
     String lessonname = hsr.getParameter("seleccion4");
     DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
Date lessondate = format.parse(hsr.getParameter("seleccion5"));

    Statement st = this.cn.createStatement();
            
            String consulta = "SELECT id_lessons FROM public.lessons where nombre_lessons ='"+lessonname+ "' ";
            ResultSet rs = st.executeQuery(consulta);
          int lessonid = 0;
            while (rs.next())
            {
                lessonid = rs.getInt("id_lessons");
            }
           consulta = "SELECT id FROM public.lessons_time where lesson_id ="+lessonid+"and date = "+lessondate;//should include time as well as there might be more than 1 lesson in same day
            ResultSet rs1 = st.executeQuery(consulta);
          int lessontimeid = 0;
            while (rs1.next())
            {
                 lessontimeid = rs1.getInt("id_lessons");
            }
    List<Attendance> records = this.getAttendance(lessontimeid,hsr.getServletContext());
    mv.addObject("attendancelist", records);
        return mv;
        
    }
    
    
    public List<Attendance> getAttendance(int lessontimeid,ServletContext servlet) throws SQLException
    {
        
        List<Attendance> records = new ArrayList<>();
         try {
       
        
        
            
             Statement st = this.cn.createStatement();
           
          
            String consulta = "SELECT * FROM public.attendance where lesson_time_id ="+lessontimeid;
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Attendance att = new Attendance();
                att.setAttendancecode(rs.getString("attendancecode"));
                att.setStudentid(rs.getInt("student_id"));
                records.add(att);
            }
            cn.close();
             DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",servlet);
        this.cn = dataSource.getConnection();
        st = this.cn.createStatement();
            for(Attendance record : records)
            {
            consulta = "SELECT FirstName,LastName FROM AH_ZAF.dbo.Students where StudentID = "+record.getStudentid();
            ResultSet rs2 = st.executeQuery(consulta);
            while (rs2.next())
            {
              record.setStudentname(rs2.getString("FirstName")+","+rs2.getString("LastName"));
            }
            }
         
            
        } catch (SQLException ex) {
            System.out.println("Error  " + ex);
        }
    
    return records;
    }
    public ModelAndView saveAttendance(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("lessonattendance");
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
     String lessonname = hsr.getParameter("seleccion4");
     DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
Date lessondate = format.parse(hsr.getParameter("seleccion5"));

    Statement st = this.cn.createStatement();
            
            String consulta = "SELECT id_lessons FROM public.lessons where nombre_lessons ='"+lessonname+ "' ";
            ResultSet rs = st.executeQuery(consulta);
          int lessonid = 0;
            while (rs.next())
            {
                lessonid = rs.getInt("id_lessons");
            }
           consulta = "SELECT id FROM public.lessons_time where lesson_id ="+lessonid+"and date = "+lessondate;//should include time as well as there might be more than 1 lesson in same day
            ResultSet rs1 = st.executeQuery(consulta);
          int lessontimeid = 0;
            while (rs1.next())
            {
                 lessontimeid = rs1.getInt("id_lessons");
            }
    List<Attendance> records = this.getAttendance(lessontimeid,hsr.getServletContext());
    mv.addObject("attendancelist", records);
        return mv;
        
    }
}
