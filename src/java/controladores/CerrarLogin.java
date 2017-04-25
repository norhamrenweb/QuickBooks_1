/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Jesús Aragón
 */
@Controller
public class CerrarLogin {
    
    //     @Override
    @RequestMapping(value="/cerrarLogin")
    public ModelAndView cerrarLogin(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
            ModelAndView mv =  new ModelAndView("redirect:/userform.htm?opcion=inicio");
            
            HttpSession sesion = hsr.getSession();
            
            sesion.invalidate();
            
            
           
            return mv;
    }

}
