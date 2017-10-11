/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;
import Montessori.*;
import Montessori.Level;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.google.gson.*;

import atg.taglib.json.util.JSONObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
//import static controladores.Testcontrol.log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
/**
 *
 * @author nmohamed
 */
@Controller
public class TestCommentControl {
     Connection cn;
     static Logger log = Logger.getLogger(SOWTreeControlador.class.getName());
     private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
     @RequestMapping("/testcomment/savecomment.htm")
    public ModelAndView savecomment(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
         ModelAndView mv = new ModelAndView("testcomment");
    try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
        st.executeUpdate("insert into classobserv(logged_by,date_created,comment,category,student_id,commentdate)values('"+user.getId()+"',now(),'hi','general','10101','2017-09-22')");
           }catch(SQLException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        log.error(ex+errors.toString());
    }
    return mv;
    }
     @RequestMapping("/testcomment/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
         ModelAndView mv = new ModelAndView("testcomment");
    try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
           }catch(SQLException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        log.error(ex+errors.toString());
    }
    return mv;
    }
//     @RequestMapping("/calendar.htm")
//    public ModelAndView calendar(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
//         ModelAndView mv = new ModelAndView("calendar");
//    try{
//        DriverManagerDataSource dataSource;
//        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
//        this.cn = dataSource.getConnection();
//        Statement st = this.cn.createStatement();
//     
//           }catch(SQLException ex){
//        StringWriter errors = new StringWriter();
//        ex.printStackTrace(new PrintWriter(errors));
//        log.error(ex+errors.toString());
//    }
//    mv.addObject("message","works");
//    return mv;
//    }
}
