/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

/**
 *
 * @author nmohamed
 */
import Montessori.*;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

@Controller
@MultipartConfig
public class ResourcesControlador {
    
     Connection cn;
      static Logger log = Logger.getLogger(ResourcesControlador.class.getName());

//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    
   @RequestMapping("/lessonresources/loadInfResource.htm")
   @ResponseBody
    public String loadInfResource(@RequestBody Resource r,HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        Resource resourceLoad = new Resource();
        //ModelAndView mv = new ModelAndView("lessonresources");
       try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            String resourceId = r.getId();
           // String name="", link="", type="";

            String consulta = "SELECT * FROM resources WHERE id = "+resourceId;
            ResultSet rs = st.executeQuery(consulta);
            resourceLoad.setId(resourceId);
            
            while (rs.next()) {        
                resourceLoad.setName(rs.getString("name"));
                resourceLoad.setLink(rs.getString("link"));
                resourceLoad.setType(rs.getString("type"));
                resourceLoad.setLesson_id(rs.getString("lesson_id"));
            }
            resourceLoad.setId(resourceId);
       }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
       }
       return new Gson().toJson(resourceLoad);
    }
    
    private Resource loadResource(String id, Statement st) throws Exception {
        Resource resourceLoad = new Resource();
        //ModelAndView mv = new ModelAndView("lessonresources");
       try{
            String resourceId = id;

            String consulta = "SELECT * FROM resources WHERE id = "+resourceId;
            ResultSet rs = st.executeQuery(consulta);
            resourceLoad.setId(resourceId);
            
            while (rs.next()) {        
                resourceLoad.setName(rs.getString("name"));
                resourceLoad.setLink(rs.getString("link"));
                resourceLoad.setType(rs.getString("type"));
                resourceLoad.setLesson_id(rs.getString("lesson_id"));
            }
            resourceLoad.setId(resourceId);
       }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
       }
       return resourceLoad;
    }
    
    protected Resource loadResource(String id, HttpServletRequest hsr) throws Exception {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        Resource resourceLoad = new Resource();
        //ModelAndView mv = new ModelAndView("lessonresources");
       try{
            String resourceId = id;

            String consulta = "SELECT * FROM resources WHERE id = "+resourceId;
            ResultSet rs = st.executeQuery(consulta);
            resourceLoad.setId(resourceId);
            
            while (rs.next()) {        
                resourceLoad.setName(rs.getString("name"));
                resourceLoad.setLink(rs.getString("link"));
                resourceLoad.setType(rs.getString("type"));
                resourceLoad.setLesson_id(rs.getString("lesson_id"));
            }
            resourceLoad.setId(resourceId);
       }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
       }
       return resourceLoad;
    }
    
   @RequestMapping("/lessonresources/loadResources.htm")
    public ModelAndView loadResources(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("lessonresources");
       try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
            String presentationName = hsr.getParameter("LessonsSelected");
            presentationName =  presentationName.split("-")[1];
            String lessonid = hsr.getParameter("LessonsSelected");
            lessonid = lessonid.split("-")[0];
            String consulta = "SELECT * FROM public.resources where lesson_id = "+lessonid;
            ResultSet rs = st.executeQuery(consulta);
            ArrayList<Resource> filersrcs = new ArrayList<>(); 
            ArrayList<Resource> otherrsrcs = new ArrayList<>(); 
       while(rs.next())
       {
           Resource r = new Resource();
           r.setId(""+rs.getInt("id"));
           r.setName(rs.getString("name"));
           r.setLink(rs.getString("link"));
           String type = rs.getString("type");
           r.setType(type);
           if(type.equals("File"))
           {
           filersrcs.add(r);
           }
           else{
               otherrsrcs.add(r);
           }
       }
        mv.addObject("lessonid",lessonid);
        /*
        int cantElem = presentationName.split("-").length;
        for (int i = 0; i < cantElem; i++) {
               
        }*/
        presentationName = "'"+presentationName+"'";
        mv.addObject("lessonsName",presentationName);
        mv.addObject("files",filersrcs);
        mv.addObject("others",otherrsrcs);
       }catch(SQLException ex){
           StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
       }
        return mv;
    }
   
    @RequestMapping("/lessonresources/addResources.htm")
    @ResponseBody
    public String addResources(@RequestBody Resource r,HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception{
       
        String name = r.getName();
        String link =r.getLink() ;
        String type = r.getType();
        String lessonId = r.getLesson_id();
        String id="";
        try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            //String lessonid = hsr.getParameter("LessonsSelected");
            String consulta = "insert into resources(name,link,type,lesson_id) values('"+name+"','"+link+"','"+type+"','"+lessonId+"')";
            st.executeUpdate(consulta,Statement.RETURN_GENERATED_KEYS); 
            ResultSet rs = st.getGeneratedKeys();     
            while(rs.next())  id = ""+rs.getInt(1);
        }
        catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
       }
        
        return id;
    }
    
    @RequestMapping("/lessonresources/updateResources.htm")
    public ModelAndView updateResources(@RequestBody Resource r,HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception{
        
        ModelAndView mv = new ModelAndView("lessonresources");
        String Idresource =r.getId();
        String name = r.getName();
        String link =r.getLink() ;
        String type = r.getType();

        try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
           // String lessonid = hsr.getParameter("LessonsSelected");
            //String consulta = "insert into resources(lesson_id) values('"+lessonid+"','')";
            String consulta = "update resources set name='"+name+"',link='"+link+"',type='"+type+"' where id = "+Idresource;
            st.executeQuery(consulta);
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        return mv;
    }
   
    @RequestMapping("/lessonresources/delResources.htm")
    public ModelAndView delResources(@RequestBody Resource r,HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception{
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("lessonresources");
        try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            String resourceId = r.getId();
            String lessonName = r.getName();
            Resource resourceLoad = loadResource(resourceId,st);
            
            if(resourceLoad.getType().equals("File")){
                /*File fileToDelete = new File(resourceLoad.getLink());
                Files.delete(fileToDelete.toPath());*/
                String server = "192.168.1.36";
		int port = 21;
		String user = "david";
		String pass = "david";

		FTPClient ftpClient = new FTPClient();
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);

                //boolean success = ftpClient.changeWorkingDirectory("/MontessoriTesting/"+resourceLoad.getLesson_id());

                ftpClient.deleteFile("/MontessoriTesting/"+resourceLoad.getLesson_id()+"-"+lessonName+"/"+resourceLoad.getLink());
                ftpClient.logout();
            }
            String consulta = "delete from resources where id = "+resourceId;
            st.executeQuery(consulta);
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        return mv;
    }

    @RequestMapping("/lessonresources/downloadFile.htm")
    public String downloadFile(@RequestBody Resource r,HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception{
        
        try{
            DriverManagerDataSource dataSource;
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
            this.cn = dataSource.getConnection();
            Statement st = this.cn.createStatement();
            String resourceId = r.getId();
            Resource resourceLoad = loadResource(resourceId,st);
           
            
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        return "mv";
    }
    
    protected boolean existe(String name, HttpServletRequest hsr) throws Exception {
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        Resource resourceLoad = new Resource();
        //ModelAndView mv = new ModelAndView("lessonresources");
       try{
            String resourceId = name;

            String consulta = "SELECT * FROM resources WHERE name = %"+name;
            ResultSet rs = st.executeQuery(consulta);
            resourceLoad.setId(resourceId);
            
            while (rs.next()) {        
                resourceLoad.setName(rs.getString("name"));
                resourceLoad.setLink(rs.getString("link"));
                resourceLoad.setType(rs.getString("type"));
                resourceLoad.setLesson_id(rs.getString("lesson_id"));
            }
            resourceLoad.setId(resourceId);
       }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
       }
       return resourceLoad.getId() != null;
    }
}
