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
import mapcusts.*;
import quickbooksync.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;



public class MappingFamilies extends MultiActionController{
   Connection cn;
   private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("familymap2");
        Config config = new Config();
GetConfig get = new GetConfig();
config = get.getConfig();
        Getcusts getlist = new Getcusts();
        List<QBCustomer> cust = getlist.getCustomer();
        mv.addObject("QBcust",cust);
        List<RWFamily> family = getlist.getFamily();
         mv.addObject("RWfamily",family);
        return mv;
    }
     public ModelAndView map(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("familymap2");
        String message = null;
        Map domap = new Map();
        QBCustomer x = new QBCustomer();
        RWFamily y = new RWFamily();
        x.setId(hsr.getParameter("origen"));
        y.setId(Integer.parseInt(hsr.getParameter("destino")));
        message = domap.mapCustomer(y,x);
         mv.addObject("message",message);
        return mv;
    }
}
