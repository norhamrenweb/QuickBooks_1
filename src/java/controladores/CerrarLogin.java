/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Jesús Aragón
 */
public class CerrarLogin implements Controller{
    
         @Override
    public ModelAndView handleRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
            ModelAndView mv =  new ModelAndView("redirect:/userform.htm?opcion=inicio");
            
            HttpSession sesion = hsr.getSession();
            
            sesion.invalidate();
            
            
           
            return mv;
    }

}
