/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.io.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ModelAndView setup(HttpServletRequest hsr, HttpServletResponse hsr1){
        return new ModelAndView("setup");
    }
    
    @RequestMapping("/setupControlador/save.htm")
    public ModelAndView save(HttpServletRequest hsr, HttpServletResponse hsr1) throws IOException{
        ServletContext context = hsr.getServletContext();
        String appPath = context.getRealPath("") + "recursos/css/estilocolegio.css";
        File archivo = new File(appPath);
        BufferedWriter bw;
        if(archivo.exists()) {
            FileWriter escribir = new FileWriter(archivo);
            String texto = "#infousuario{background-color:"+hsr.getParameter("headcolor")+"}";
            for(int i=0; i<texto.length();i++){
                escribir.write(texto.charAt(i));
            }
            texto = "body{background-color:"+hsr.getParameter("bodycolor")+"}";
            for(int i=0; i<texto.length();i++){
                escribir.write(texto.charAt(i));
            }
            escribir.close();
        } else {
            bw = new BufferedWriter(new FileWriter(archivo));
        }
        ModelAndView mv = new ModelAndView("setup");
        File dir = new File(context.getRealPath(""));
        System.out.println ("Directorio actual: " + dir.getCanonicalPath());
        mv.addObject("ruta", dir.getCanonicalPath());
        return mv;
    }
    
}
