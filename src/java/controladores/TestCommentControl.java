/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;
import Montessori.*;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
//import static controladores.Testcontrol.log;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
/**
 *
 * @author nmohamed
 */
@Controller
public class TestCommentControl {
     static Logger log = Logger.getLogger(SOWTreeControlador.class.getName());
     private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
     @RequestMapping("/testcomment/savecomment.htm")
    public ModelAndView savecomment(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio"); 
        ModelAndView mv = new ModelAndView("testcomment");
    try{
            HttpSession sesion = hsr.getSession();
            User user = (User) sesion.getAttribute("user");
            DBConect.eduweb.executeUpdate("insert into classobserv(logged_by,date_created,comment,category,student_id,commentdate)values('"+user.getId()+"',now(),'hi','general','10101','2017-09-22')");
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
    return mv;
    }
     @RequestMapping("/testcomment/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("testcomment");
    return mv;
    }
}
