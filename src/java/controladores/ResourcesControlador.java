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

@Controller
public class ResourcesControlador {
     Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
   @RequestMapping("/lessonresources/loadResources.htm")
    public ModelAndView loadResources(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("lessonresources");
       
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
             String lessonid = hsr.getParameter("LessonsSelected");
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
        
        mv.addObject("files",filersrcs);
        mv.addObject("others",otherrsrcs);
        return mv;
    }
}
