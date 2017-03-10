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
public class ProgressControlador extends MultiActionController{
    
    
      Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
   
    
    public ModelAndView loadRecords(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("lessonprogress");
         try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
     String lessonname = hsr.getParameter("LessonsSelected");
//     DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
//Date lessondate = format.parse(hsr.getParameter("seleccion5"));

    Statement st = this.cn.createStatement();
//            
//            String consulta = "SELECT id_lessons FROM public.lessons where nombre_lessons ='"+lessonname+ "' ";
//            ResultSet rs = st.executeQuery(consulta);
//          int lessonid = 0;
//            while (rs.next())
//            {
//                lessonid = rs.getInt("id_lessons");
//            }
           String consulta = "SELECT * FROM public.lesson_detailes where id ="+lessonname;
            ResultSet rs1 = st.executeQuery(consulta);
        Lessons lesson = new Lessons();
            while (rs1.next())
            {Objective obj = new Objective();
            obj.setName(rs1.getString("objectivename"));
            String[] ids = new String[1];
            ids[0]= String.valueOf(rs1.getInt("objective_id"));
            obj.setId(ids);
            lesson.setObjective(obj);
            Subject sub = new Subject();
            String name = sub.fetchName(rs1.getInt("subject_id"),hsr.getServletContext());
            sub.setName(name);
            String[] subids = new String[1];
            subids[0]= String.valueOf(rs1.getInt("subject_id"));
            sub.setId(ids);
            lesson.setSubject(sub);
            lesson.setName(rs1.getString("name"));
            lesson.setId(Integer.parseInt(lessonname));
            }
    List<Progress> records = this.getRecords(lesson,hsr.getServletContext());
    mv.addObject("attendancelist", records);
    mv.addObject("lessondetailes",lesson);
         } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return mv;
        
    }
    
    
    public List<Progress> getRecords(Lessons lesson,ServletContext servlet) throws SQLException
    {
        
        List<Progress> records = new ArrayList<>();
         try {
       
        
        
            
             Statement st = this.cn.createStatement();
           
          
            String consulta = "SELECT * FROM public.lesson_stud_att where lesson_id ="+lesson.getId();
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Progress att = new Progress();
             
                att.setStudentid(rs.getInt("student_id"));
                att.setAttendancecode(rs.getString("attendance"));
                records.add(att);
            }
             for(Progress record : records)
            {
                String[] ids = new String[1];
                ids = lesson.getObjective().getId();
            consulta = "SELECT rating,comment FROM public.progress_report where student_id = '"+record.getStudentid()+"' AND comment_date = (select max(comment_date)   from public.progress_report where student_id ="+record.getStudentid()+") AND subject_id ="+ids[0];
            ResultSet rs3 = st.executeQuery(consulta);
            while (rs3.next())
            {
              record.setRating(rs3.getString("rating"));
              record.setComment(rs3.getString("comment"));
            }
            }
            cn.close();
             DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",servlet);
        this.cn = dataSource.getConnection();
        st = this.cn.createStatement();
            for(Progress record : records)
            {
            consulta = "SELECT FirstName,LastName FROM AH_ZAF.dbo.Students where StudentID = '"+record.getStudentid()+"'";
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
    public ModelAndView saveRecords(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("lessonprogress");
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        String[]lessonid=hsr.getParameterValues("TXTlessonid");
        String[] objectiveid = hsr.getParameterValues("TXTobjectiveid");
        String[] comments = hsr.getParameterValues("TXTcomment");
        String[] ratings= hsr.getParameterValues("TXTrating");
        String[] studentids= hsr.getParameterValues("TXTstudentid");
        String[] att= hsr.getParameterValues("TXTattendance");
      

    Statement st = this.cn.createStatement();
            
    for(int i=0;i<studentids.length;i++)
    {
    st.executeUpdate("update lesson_stud_att set attendance = '"+att[i]+"',timestamp= now() where lesson_id = "+lessonid[0]+" AND student_id = '"+studentids[i]+"'");
    }
      
     for(int i=0;i<studentids.length;i++)
    {
        
         st.executeUpdate("insert into progress_report(comment_date,comment,rating,lesson_id,student_id,subject_id) values (now(),'"+comments[i]+"','"+ratings[i]+"','"+lessonid[0]+"','"+studentids[i]+"','"+objectiveid[0]+"')");
       
    } 
        return mv;
        
    }
}
