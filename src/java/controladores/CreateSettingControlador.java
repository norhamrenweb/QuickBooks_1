/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import java.lang.ProcessBuilder.Redirect;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author nmohamed
 */
public class CreateSettingControlador extends MultiActionController{
    
      Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
       
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
         DriverManagerDataSource dataSource2;
        dataSource2 = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource2.getConnection();
         Statement st2 = this.cn.createStatement();
        ResultSet rs1 = st2.executeQuery("SELECT name,id FROM public.method");
        List <Method> methods = new ArrayList();
        Method m = new Method();
        m.setName("Select Method");
        methods.add(m);
        while(rs1.next())
        {
            Method x = new Method();
             String[] ids = new String[1];
             ids[0]=""+rs1.getInt("id");
            x.setId(ids);
            x.setName(rs1.getString("name"));
        methods.add(x);
        }
          
            mv.addObject("methods",methods);
            mv.addObject("gradelevels", grades);
        
        return mv;
    }

   
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
        List<Subject> subjects = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String[] levelid = new String[1];
      
             st = this.cn.createStatement();
             levelid= hsr.getParameterValues("seleccion1");
//             String namesubject= hsr.getParameter("namesubject");
          ResultSet rs1 = st.executeQuery("select nombre_subject,id from subject where id_level="+levelid[0]);
           Subject s = new Subject();
          s.setName("Select Subject");
          subjects.add(s);
           
           while (rs1.next())
            {
             Subject sub = new Subject();
             String[] ids = new String[1];
            ids[0]=""+rs1.getInt("id");
             sub.setId(ids);
             sub.setName(rs1.getString("nombre_subject"));
                subjects.add(sub);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
        }
        
        
         mv.addObject("subjects", subjects);
        
        return mv;
    }
    
    public ModelAndView objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
        List<Objective> objectives = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String subjectid = null;
          
            
                subjectid = hsr.getParameter("seleccion2");
            
            
          ResultSet rs1 = st.executeQuery("select name,id from public.objective where subject_id="+subjectid);
          Objective s = new Objective();
          s.setName("Select Objective");
          objectives.add(s);
           
           while (rs1.next())
            {
             String[] ids = new String[1];
                Objective sub = new Objective();
            ids[0] = ""+rs1.getInt("id");
             sub.setId(ids);
             sub.setName(rs1.getString("name"));
                objectives.add(sub);
            }
          
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Objectives: " + ex);
        }
        
        mv.addObject("templatessubsection", hsr.getParameter("seleccion2"));
        mv.addObject("objectives", objectives);
        
        return mv;
    }
    
    public ModelAndView contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
        List<Content> contents = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String objectiveid = null;
            
                objectiveid = hsr.getParameter("seleccion3");
            
            
          ResultSet rs1 = st.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id = "+objectiveid+")");
          String[] ids = new String[1];
           while (rs1.next())
            {
             Content eq = new Content();
            ids[0] = String.valueOf(rs1.getInt("id"));
             eq.setId(ids);
             eq.setName(rs1.getString("name"));
                contents.add(eq);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
        }
        
      
       mv.addObject("contents", contents);
        String[] id = hsr.getParameterValues("seleccion3");
 Objective objective = new Objective();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();          
          ResultSet rs1 = st.executeQuery("SELECT name,description FROM public.objective where id ="+ id[0]);
        
           while (rs1.next())
            {
             objective.setName(rs1.getString("name"));
             objective.setDescription(rs1.getString("description"));
             objective.setId(id);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
        }
        
      
         mv.addObject("objective", objective);
    
        return mv;
    }   
    
    

     public ModelAndView createsettingObjective(HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
        
         String message;
         message = "Setting Created";
         List<Objective> objectives = new ArrayList<>();
        ModelAndView mv = new ModelAndView("redirect:/createsetting.htm?select=start", "message", message);
      
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
       
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
     //   int level = Integer.parseInt( hsr.getParameter("TXTlevel"));
     String[] subject = hsr.getParameterValues("namenewsubject");
       
        String nameobjective = hsr.getParameter("namenewobjective");
        

        String consulta = "insert into objective(subject_id,name) values ('"+subject[0]+"','"+nameobjective+"')";
       Statement pst = this.cn.createStatement();
       pst.executeUpdate(consulta);
      
//       ResultSet rs1 = pst.executeQuery("select name,id from public.objective where subject_id="+subject[0]);
//          Objective s = new Objective();
//          s.setName("Select Objective");
//          objectives.add(s);
//           
//           while (rs1.next())
//            {
//             String[] ids = new String[1];
//                Objective sub = new Objective();
//            ids[0] = ""+rs1.getInt("id");
//             sub.setId(ids);
//             sub.setName(rs1.getString("name"));
//                objectives.add(sub);
//            }
//          }
//           catch (SQLException ex) {
//            System.out.println("Error : " + ex);
//        }
//        
//       mv.addObject("objectives",objectives);
   
        return mv;
    }
 public ModelAndView showsettingMethod(HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception
 {ModelAndView mv = new ModelAndView("createsetting");
 String[] id = hsr.getParameterValues("id");
 Method method = new Method();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();          
          ResultSet rs1 = st.executeQuery("SELECT name,description FROM public.method where id ="+ id[0]);
        
           while (rs1.next())
            {
             method.setName(rs1.getString("name"));
             method.setDescription(rs1.getString("description"));
             method.setId(id);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
        }
        
      
         mv.addObject("methods", method);
 return mv;
 }
  public ModelAndView showsettingContent(HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception
 {ModelAndView mv = new ModelAndView("createsetting");
 String[] id = hsr.getParameterValues("id");
 Method method = new Method();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();          
          ResultSet rs1 = st.executeQuery("SELECT name,description FROM public.method where id ="+ id[0]);
        
           while (rs1.next())
            {
             method.setName(rs1.getString("name"));
             method.setDescription(rs1.getString("description"));
             method.setId(id);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
        }
        
      
         mv.addObject("methods", method);
 return mv;
 }
}


