/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import java.lang.ProcessBuilder.Redirect;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.*;

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
import org.springframework.web.servlet.mvc.*;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.gson.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.ui.ModelMap;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
/**
 *
 * @author nmohamed
 */
@Controller
public class CreateSettingControlador{
    
      Connection cn;
      static Logger log = Logger.getLogger(CreateSettingControlador.class.getName());
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    @RequestMapping("/createsetting/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
       try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        ResultSet rs = st.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");
        List <Level> grades = new ArrayList();
        Level l = new Level();
        l.setName("Select level");
        String[] y = new String[1];
             y[0]="?";
           l.setId(y);
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
        ResultSet rs1 = st2.executeQuery("SELECT * FROM public.method");
        List <Method> methods = new ArrayList();
        while(rs1.next())
        {
            Method x = new Method();
             String[] ids = new String[1];
             ids[0]=""+rs1.getInt("id");
            x.setId(ids);
            x.setName(rs1.getString("name"));
            x.setDescription(rs1.getString("description"));
        methods.add(x);
        }
          
            mv.addObject("methods",methods);
            mv.addObject("gradelevels", grades);
       }catch(SQLException ex){
           StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
       }
      
        return mv;
    }

   @RequestMapping("/createsetting/subjectlistLevel.htm")
   
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
        List<Subject> subjects = new ArrayList<>();
        List<Subject> activesubjects = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String[] levelid = new String[1];
            dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
             this.cn = dataSource.getConnection();
             st = this.cn.createStatement();
             levelid= hsr.getParameterValues("seleccion1");
          ResultSet rs1 = st.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID ="+levelid[0]+")");
//           Subject s = new Subject();
//          s.setName("Select Subject");
//          subjects.add(s);
           
           while (rs1.next())
            {
             Subject sub = new Subject();
             String[] ids = new String[1];
            ids[0]=""+rs1.getInt("CourseID");
             sub.setId(ids);
            
                subjects.add(sub);
            }
           for(Subject su:subjects.subList(1,subjects.size()))
          {
              String[] ids = new String[1];
              ids=su.getId();
           ResultSet rs2 = st.executeQuery("select Title,Active from Courses where CourseID = "+ids[0]);
           while(rs2.next())
           {
               if(rs2.getBoolean("Active")== true)
               {
                   su.setName(rs2.getString("Title"));
                   activesubjects.add(su);
               }
           
           }
          }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        
        
         mv.addObject("subjects", activesubjects);
        
        return mv;
    }
    @RequestMapping("/createsetting/objectivelistSubject.htm")
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
            
            
          ResultSet rs1 = st.executeQuery("select name,id,description from public.objective where subject_id="+subjectid);
//          Objective s = new Objective();
//          s.setName("Select Objective");
//          objectives.add(s);
           
           while (rs1.next())
            {
             String[] ids = new String[1];
                Objective sub = new Objective();
            ids[0] = ""+rs1.getInt("id");
             sub.setId(ids);
             sub.setName(rs1.getString("name"));
             sub.setDescription(rs1.getString("description"));
                objectives.add(sub);
            }
          
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Objectives: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        
        mv.addObject("templatessubsection", hsr.getParameter("seleccion2"));
        mv.addObject("objectives", objectives);
        
        return mv;
    }

    @RequestMapping(value="/createsetting/contentlistObjective.htm")
    @ResponseBody
    public String contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
//       ModelAndView mv = new ModelAndView("createsettings");
       List<Content> contents = new ArrayList<>();

      JSONObject obj = new JSONObject();
      Objective objective = new Objective();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();
            String objectiveid = hsr.getParameter("seleccion3");
            
          ResultSet rs1 = st.executeQuery("SELECT name,id,description FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id = "+objectiveid+")");
         
           while (rs1.next())
            {
             String[] ids = new String[1];
             Content eq = new Content();
            ids[0] = String.valueOf(rs1.getInt("id"));
             eq.setId(ids);
             eq.setName(rs1.getString("name"));
             eq.setDescription(rs1.getString("description"));
          
 //    obj.put("name", rs1.getString("name"));
//       list.add(obj);
//       System.out.print(list);
               contents.add(eq);
            }
            
//        } catch (SQLException ex) {
//            System.out.println("Error leyendo contents: " + ex);
//        }
//       Gson gson = new GsonBuilder().setPrettyPrinting().create();
//       String json = gson.toJson(contents);
//       hsr1.setContentType("application/json");
//    
//		System.out.print(json);
//      json = json.replaceAll("\n", "");
//      json = json.trim();
//      json = json.substring(1,json.length()-1);
//      json = json.replace("\\","");
//   String json = JSONObject.escape(obj.toString());
 //            hsr1.setCharacterEncoding("UTF-8");
//              hsr1.getWriter().write(json);
          
        String id = hsr.getParameter("seleccion3");
 
      
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
              st = this.cn.createStatement();          
          ResultSet rs2 = st.executeQuery("SELECT name,description FROM public.objective where id ="+ id);
        
           while (rs2.next())
            {
             objective.setName(rs2.getString("name"));
             objective.setDescription(rs2.getString("description"));
 //            objective.setId(id);
            }
           
            ResultSet rs3 = st.executeQuery("SELECT count(id) as nooflessons FROM public.lessons where objective_id ="+ id);
        
           while (rs3.next())
            {
             objective.setNooflessonsplanned(rs3.getInt("nooflessons"));
            }
           ArrayList<Step> steps = new ArrayList<>();
            ResultSet rs4 = st.executeQuery("SELECT name,id,storder FROM public.obj_steps where obj_id ="+ id);
        
           while (rs4.next())
            {
             Step step = new Step();
             step.setName(rs4.getString("name"));
             String[] stid = new String[1];
             stid[0]=""+rs4.getInt("id");
             step.setId(stid);
             step.setOrder(rs4.getInt("storder"));
             steps.add(step);
            }
           objective.setSteps(steps);
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        
//      
                String contentjson = new Gson().toJson(contents);
                String objjson = new Gson().toJson(objective);
                obj.put("content",contentjson);
                obj.put("objective",objjson);
 
               
//            hsr1.setContentType("application/json");
//            hsr1.setCharacterEncoding("UTF-8");
//            hsr1.getWriter().write(json);
//    
//            mv.addObject(json);
        return obj.toString();
    }   
    //saving the edited objective and loading the new objectives list
    @RequestMapping(value="/createsetting/editObjective.htm")
    @ResponseBody
    public String editObjective(@RequestBody Objective obj,@RequestBody HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
        List<Objective> objectives = new ArrayList<>();
      String[] sid = hsr.getParameterValues("sid");
        String message = null;
        try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String[] obid = obj.getId();
        String consulta = "update objective set name = '"+obj.getName()+"',description ='"+obj.getDescription()+"'where id ="+obid[0]; 
        st.executeUpdate(consulta);
        // we need here to update the edited steps and delete the deleted and add the new
//        consulta ="select id from obj_steps where obj_id ='"+obid[0]+"'";
//        ResultSet rs1 =st.executeQuery(consulta);

        message = "Objective edited successfully";   
        ResultSet rs = st.executeQuery("select * from objective where subject_id = "+sid[0]);
        while(rs.next()){
        Objective o = new Objective();
        o.setDescription(rs.getString("description"));
        String[] id = new String[1];
        id[0]=""+rs.getInt("id");
        o.setId(id);
        o.setName(rs.getString("name"));
        objectives.add(o);
        }
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message ="Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        String objjson = new Gson().toJson(objectives);
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return objjson;
    }     
    @RequestMapping(value="/createsetting/addObjective.htm")
    @ResponseBody
    public String addObjective(@RequestBody Objective ob,HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
     
      // String[] input = hsr.getParameterValues("data");
      // JSONObject jsonObj = new JSONObject(input[0]);
        String message = null;
        Objective o = new Objective();
        try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String[] subid = ob.getId();
        String consulta = "insert into objective(name,description,subject_id) values('"+ob.getName()+"','"+ob.getDescription()+"','"+subid[0]+"')"; 
        st.executeUpdate(consulta,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = st.getGeneratedKeys();
       String[] id = new String[1];
        while(rs.next())
        {
         
        id[0]=""+rs.getInt(1);
        o.setId(id);
        }
        ArrayList<Step> steps = ob.getSteps();
        for(Step s:steps)
        {
        st.executeUpdate("insert into obj_steps(name,weight,storder,obj_id) values('"+s.getName()+"',0,"+s.getOrder()+",'"+id[0]+"')");
        }
        message = "Objective added successfully";   
        o.setDescription(ob.getDescription());
        o.setName(ob.getName());
        o.setSteps(ob.getSteps());
       
        
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message ="Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        String objjson = new Gson().toJson(o);
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return objjson;
    }  
    @RequestMapping(value="/createsetting/delObjective.htm")
    @ResponseBody
    public String delObjective(HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
     
        String message = null;
       String[] id = hsr.getParameterValues("id");
     
       try {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
         Statement st = this.cn.createStatement();
         String consulta = "select name from lessons where objective_id = "+ id[0];
          ResultSet rs = st.executeQuery(consulta );
          if(rs.next()){
            message="This objective is linked to lessons";  
          }
          else{
        consulta = "DELETE FROM public.objective WHERE id="+id[0];
           st.executeUpdate(consulta);
           consulta="delete from objective_content where objective_id= '"+id[0]+"'";
           st.executeUpdate(consulta);
           message="success";
           // need to decide what to do with the contents also if the objective has a record in the progress_report
          }
       }catch (SQLException ex) {
            System.out.println("Error : " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
     
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return message;
    }  
    @RequestMapping(value="/createsetting/addContent.htm")
    @ResponseBody
    public String addContent(@RequestBody Content cont,HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception 
{
    String[] oid = cont.getId();
    //   JSONObject jsonObj = new JSONObject(input[0]);
        String message = null;
        Content c = new Content();
        try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String consulta = "insert into content(name,description) values('"+cont.getName()+"','"+cont.getDescription()+"')"; 
        st.executeUpdate(consulta,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = st.getGeneratedKeys();
        String[] id = new String[1];
        while(rs.next())
        {
        
        id[0]=""+rs.getInt(1);
        c.setId(id);
        }
           
        c.setDescription(cont.getDescription());
        c.setName(cont.getName());
        consulta = "insert into objective_content(content_id,objective_id) values('"+id[0]+"','"+oid[0]+"')"; 
        st.executeUpdate(consulta);
        message = "Content added successfully";
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message ="Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        String cjson = new Gson().toJson(c);
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return cjson;
}
             @RequestMapping(value="/createsetting/editContent.htm")
    @ResponseBody
    public String editContent(@RequestBody Content cont,HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception 
{
     List<Content> contents = new ArrayList<>();
   //   JSONObject obj = new JSONObject();
       String[] oid = hsr.getParameterValues("oid");
    //   JSONObject jsonObj = new JSONObject(hi[0]);
        String message = null;
        try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String[] cid = cont.getId();
        String consulta = "update content set name = '"+cont.getName()+"',description ='"+cont.getDescription()+"'where id ="+cid[0]; 
        st.executeUpdate(consulta);
        message = "Content edited successfully";   
          ResultSet rs1 = st.executeQuery("SELECT name,id,description FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id = "+oid[0]+")");
          
           while (rs1.next())
            {
                String[] ids = new String[1];
             Content eq = new Content();
            ids[0] = String.valueOf(rs1.getInt("id"));
             eq.setId(ids);
             eq.setName(rs1.getString("name"));
             eq.setDescription(rs1.getString("description"));
               contents.add(eq);
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message ="Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        String cjson = new Gson().toJson(contents);
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return cjson;
}
    @RequestMapping(value="/createsetting/delContent.htm")
    @ResponseBody
    public String delContent(HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
     
        String message = null;
       String[] id = hsr.getParameterValues("id");
     
       try {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
         Statement st = this.cn.createStatement();
         String consulta = "select lesson_id from lesson_content where content_id = "+ id[0];
          ResultSet rs = st.executeQuery(consulta );
          if(rs.next()){
            message="This content is linked to lessons";  
          }
          else{
        consulta = "DELETE FROM objective_content WHERE content_id="+id[0];
           st.executeUpdate(consulta);
           consulta = "DELETE FROM content WHERE id="+id[0];
           st.executeUpdate(consulta);
           message="success";
          
          }
       }catch (SQLException ex) {
            System.out.println("Error : " + ex);
            message = "Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
     
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return message;
    } 
    @RequestMapping(value="/createsetting/delMethod.htm")
    @ResponseBody
    public String delMethod(HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
     
        String message = null;
       String[] id = hsr.getParameterValues("id");
     
       try {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
         Statement st = this.cn.createStatement();
         String consulta = "select id from lessons where method_id = "+ id[0];
          ResultSet rs = st.executeQuery(consulta );
          if(rs.next()){
            message="This method is linked to lessons";  
          }
          else{
        consulta = "DELETE FROM method WHERE id="+id[0];
           st.executeUpdate(consulta);
           message="success";
          
          }
       }catch (SQLException ex) {
            System.out.println("Error : " + ex);
            message = "Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
     
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return message;
    } 
        @RequestMapping(value="/createsetting/addMethod.htm")
    @ResponseBody
    public String addMethod(@RequestBody Method met,HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
     
//       String[] input = hsr.getParameterValues("data");
//       JSONObject jsonObj = new JSONObject(input[0]);
        String message = null;
        Method m = new Method();
        try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String consulta = "insert into method(name,description) values('"+met.getName()+"','"+met.getDescription()+"')"; 
        st.executeUpdate(consulta,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = st.getGeneratedKeys();
       
        while(rs.next())
        {
         String[] id = new String[1];
        id[0]=""+rs.getInt(1);
        m.setId(id);
        }
        message = "Method added successfully";   
        m.setDescription(met.getDescription());
        m.setName(met.getName());
       
        
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message ="Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        String objjson = new Gson().toJson(m);
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return objjson;
    }          
@RequestMapping(value="/createsetting/editMethod.htm")
    @ResponseBody
    public String editMethod(@RequestBody Method met,HttpServletRequest hsr,HttpServletResponse hsr1) throws Exception {
        List<Method> methods = new ArrayList<>();
   //   JSONObject obj = new JSONObject();
      String[] mid =met.getId();
//       JSONObject jsonObj = new JSONObject(hi[0]);
        String message = null;
        try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String consulta = "update method set name = '"+met.getName()+"',description ='"+met.getDescription()+"'where id ="+mid[0]; 
        st.executeUpdate(consulta);
        message = "Objective edited successfully";   
        ResultSet rs = st.executeQuery("select * from method ");
        while(rs.next()){
        Method m = new Method();
        m.setDescription(rs.getString("description"));
        String[] id = new String[1];
        id[0]=""+rs.getInt("id");
        m.setId(id);
        m.setName(rs.getString("name"));
        methods.add(m);
        }
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
            message ="Something went wrong";
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        String objjson = new Gson().toJson(methods);
              
    //            obj.put("newobjs",objjson);
  //              obj.put("message",message);
      
         return objjson;
    }     


}


