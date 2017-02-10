/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

/**
 *
 * @author Jesús Aragón
 */


import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.support.RequestContextUtils;


public class DatosUsuarioControlador implements Controller
{
    @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        try {
            RequestContextUtils.getLocaleResolver(request).setLocale(request, response, new Locale(request.getParameter("lenguaje")));
        } catch (Exception ex) {
        }
 
        return new ModelAndView("userform");
    }




   
}

