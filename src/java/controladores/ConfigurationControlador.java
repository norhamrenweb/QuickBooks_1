/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

 import Montessori.User;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Norhan
 */
@Controller
public class ConfigurationControlador {
    
    @RequestMapping("/setupControlador/start.htm")
    public ModelAndView setup(HttpServletRequest hsr, HttpServletResponse hsr1) throws SQLException{
        
        if ((new SessionCheck()).checkSession(hsr)) {
            return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        }
        ModelAndView mv = new ModelAndView("setup");
        int iduser = ((User)hsr.getSession().getAttribute("user")).getId();
        mv.addObject("teacherlist",LessonsListControlador.getTeachers(-1));
        return mv;
        
    
    }
    
    @RequestMapping("/setupControlador/save.htm")
    public ModelAndView save(HttpServletRequest hsr, HttpServletResponse hsr1) throws IOException{
        ServletContext context = hsr.getServletContext();
        String appPath = context.getRealPath("") + "recursos/css/estilocolegio.css";
        File archivo = new File(appPath);
        String headcolor = hsr.getParameter("headcolor");
        String bodycolor = hsr.getParameter("bodycolor");
        BufferedWriter bw;
        if(!archivo.exists()) {
            bw = new BufferedWriter(new FileWriter(archivo));
        }
        String texto;
        FileWriter escribir = new FileWriter(archivo);
        if(headcolor!=null){
            texto = "#infousuario{background-color:" + headcolor + "}";
            for (int i = 0; i < texto.length(); i++) {
                escribir.write(texto.charAt(i));
            }
        }
        if(bodycolor!=null){
            texto = "body{background-color:" + bodycolor + "}";
            for (int i = 0; i < texto.length(); i++) {
                escribir.write(texto.charAt(i));
            }
        }
        escribir.close();
        ModelAndView mv = new ModelAndView("setup");
        return mv;
    }
    
}
