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
import Reports.DataFactoryFolder.Profesor;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapcusts.Getcusts;
import quickbooksync.*;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@RequestMapping("/")
public class Homepage extends MultiActionController {

    Connection cn;
    DBConect c;

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    public ModelAndView inicio(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        return new ModelAndView("userform");
    }

    @RequestMapping
    public ModelAndView login(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        DBConect.close();
        c = new DBConect(hsr, hsr1);
        HttpSession session = hsr.getSession();
        String schoolCode = "AH";
           
        session.setAttribute("yearsids", new Gson().toJson(this.getYears(schoolCode)));
        
        User user = new User();
        
        boolean result = true;
        LoginVerification login = new LoginVerification();
        if ("QuickBook".equals(hsr.getParameter("txtusuario"))) {
            ModelAndView mv = new ModelAndView("redirect:/suhomepage.htm?opcion=loadconfig");
            return mv;
        } else {
            user = login.consultUserDB(hsr.getParameter("txtusuario"), hsr.getParameter("txtpassword"));
        }

        if (user.getId() == 0) {
            ModelAndView mv = new ModelAndView("userform");
            String message = "Username or password incorrect";
            mv.addObject("message", message);
            return mv;
        } else {

            HashMap<Integer, String> mapGroups = login.getSecurityGroupID();
            int userGroup = login.fromGroup(user.getId());

            if (mapGroups.containsKey(userGroup)) {
                String groupName = mapGroups.get(userGroup);
                switch (groupName) {
                    case "MontessoriTeacher":
                        user.setType(1);
                        break;
                    case "MontessoriAdmin":
                        user.setType(0);
                        break;
                    case "MontessoriHead":
                        user.setType(2);
                        break;
                }
            } else {
                result = false;
            }
            if (result == true) {
                ModelAndView mv = new ModelAndView("redirect:/homepage/loadLessons.htm");
                String message = "welcome user";
                int termId = 1, yearId = 1;
                ResultSet rs2 = DBConect.ah.executeQuery("select defaultyearid,defaulttermid from ConfigSchool where configschoolid = 1");
                while (rs2.next()) {
                    termId = rs2.getInt("defaulttermid");
                    yearId = rs2.getInt("defaultyearid");
                }
                session.setAttribute("user", user);
                session.setAttribute("termId", termId);
                session.setAttribute("yearId", yearId);
                
                String nameTerm = "", nameYear = "";
                ResultSet rs3 = DBConect.ah.executeQuery("select name from SchoolTerm where TermID = " + termId + " and YearID = " + yearId);
                while (rs3.next()) {
                    nameTerm = "" + rs3.getString("name");
                }
                ResultSet rs4 = DBConect.ah.executeQuery("select SchoolYear from SchoolYear where yearID = " + yearId);
                while (rs4.next()) {
                    nameYear = "" + rs4.getString("SchoolYear");
                }

                session.setAttribute("termYearName", nameTerm + " / " + nameYear);     
            
                mv.addObject("message", message);
                return mv;
            } else {
                ModelAndView mv = new ModelAndView("userform");
                String message = "Username or Password incorrect";
                mv.addObject("message", message);
                return mv;
            }
        }

    }

    public ArrayList<Tupla<Integer,String>> getYears(String schoolCode){
        ArrayList<Tupla<Integer,String>> ret = new ArrayList<>();
        String consulta="select * from SchoolYear where SchoolCode ='"+schoolCode+"'";
        try {
            ResultSet rs = DBConect.ah.executeQuery(consulta);
            while(rs.next()){
                int yearid = rs.getInt("yearid");
                String yearName = rs.getString("SchoolYear");
                ret.add(new Tupla<>(yearid,yearName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return ret;
    } 
    
    @RequestMapping("/getyear.htm")
    @ResponseBody
    public String getTermYear(HttpServletRequest hsr, HttpServletResponse hsr1){
        ArrayList<Tupla<Integer,String>> ret = new ArrayList<>();
        String consulta="select * from SchoolTerm where YearID="+hsr.getParameter("id");
        try {
            ResultSet rs = DBConect.ah.executeQuery(consulta);
            while(rs.next()){
                int yearid = rs.getInt("TermID");
                String yearName = rs.getString("Name");
                ret.add(new Tupla<>(yearid,yearName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Homepage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return (new Gson()).toJson(ret);
    } 
    
    @RequestMapping("/changeTermYear.htm")
    @ResponseBody
    public String changeTermAndYear(HttpServletRequest hsr, HttpServletResponse hsr1){
        int yearid = Integer.parseInt(hsr.getParameter("yearid"));
        int termid = Integer.parseInt(hsr.getParameter("termid"));
        if ((new SessionCheck()).checkSession(hsr)) {
            return "ERROR";
        } else {
            String nameTerm = "", nameYear = "";
            try{
                ResultSet rs3 = DBConect.ah.executeQuery("select name from SchoolTerm where TermID = " + termid + " and YearID = " + yearid);
                while (rs3.next()) {
                    nameTerm = "" + rs3.getString("name");
                }
                ResultSet rs4 = DBConect.ah.executeQuery("select SchoolYear from SchoolYear where yearID = " + yearid);
                while (rs4.next()) {
                    nameYear = "" + rs4.getString("SchoolYear");
                }
            }catch(Exception e){
                
            }
            hsr.getSession().removeAttribute("yearId");
            hsr.getSession().removeAttribute("termId");
            hsr.getSession().setAttribute("termYearName", nameTerm + " / " + nameYear);
            hsr.getSession().setAttribute("yearId", yearid);
            hsr.getSession().setAttribute("termId", termid);
            
            return "OK";
        }
    }
    
    public ModelAndView save(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("suhomepage");
        String qbdburl = hsr.getParameter("qbdburl");
        String rwdburl = hsr.getParameter("rwdburl");
        String edudburl = hsr.getParameter("edudburl");
        String qbdbuser = hsr.getParameter("qbdbuser");
        String qbdbpswd = hsr.getParameter("qbdbpswd");
        String rwdbuser = hsr.getParameter("rwdbuser");
        String rwdbpswd = hsr.getParameter("rwdbpswd");
        String edudbuser = hsr.getParameter("edudbuser");
        String edudbpswd = hsr.getParameter("edudbpswd");
        String startdate = hsr.getParameter("startdate");
        String itemname = hsr.getParameter("itemname");
        DriverManagerDataSource dataSource;

        dataSource = (DriverManagerDataSource) this.getBean("dataSourceEDU", hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement ps = this.cn.createStatement(1004, 1007);
        ResultSet rs = ps.executeQuery("select * from syncconfig");
        if (rs.next()) {
            int id = rs.getInt("id");
            ps.executeUpdate("update syncconfig set qbdburl ='" + qbdburl + "',qbdbuser ='" + qbdbuser + "',qbdbpswd = '" + qbdbpswd + "',edudburl = '" + edudburl + "',edudbuser= '" + edudbuser + "',edudbpswd= '" + edudbpswd + "',rwdburl= '" + rwdburl + "', rwdbuser = '" + rwdbuser + "',rwdbpswd = '" + rwdbpswd + "',startdate ='" + startdate + "',itemname='" + itemname + "' where id= " + id);
        } else {
            ps.executeUpdate("insert into syncconfig(qbdburl,qbdbuser,qbdbpswd,edudburl,edudbuser,edudbpswd,rwdburl,rwdbuser,rwdbpswd,startdate,itemname) values ('" + qbdburl + "','" + qbdbuser + "','" + qbdbpswd + "','" + edudburl + "','" + edudbuser + "','" + edudbpswd + "','" + rwdburl + "','" + rwdbuser + "','" + rwdbpswd + "','" + startdate + "','" + itemname + "')");
        }

        String message = "Configuration setting saved";
        mv.addObject("message1", message);
        return mv;

    }

    public ModelAndView runsync(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv
                = new ModelAndView("suhomepage");

        Config config = new Config();
        GetConfig get = new GetConfig();
        config = get.getConfig();

        if (config.getQbdburl() != null && config.getQbdbuser() != null && config.getQbdbpswd() != null & config.getRwdburl() != null && config.getRwdbuser() != null && config.getRwdbpswd() != null && config.getEdudburl() != null && config.getEdudbuser() != null && config.getEdudbpswd() != null && config.getStartdate() != null && config.getItemname() != null) {
            Runsync s = new Runsync();
            s.runsync(config);

            mv.addObject("message1", "QuickBooks synchronized");
        } else {
            mv.addObject("message1", "A configuration setting is missing");

        }
        return mv;

    }

    public ModelAndView map(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("familymap2");
        Getcusts l = new Getcusts();
        List<QBCustomer> allcustomer = new ArrayList<>();
        Config config = new Config();
        GetConfig get = new GetConfig();
        config = get.getConfig();
        allcustomer = l.getCustomer();
        List<RWFamily> allfamily = new ArrayList<>();
        allfamily = l.getFamily();
        mv.addObject("allcustomer", allcustomer);
        mv.addObject("allfamily", allfamily);

        return mv;

    }

    public ModelAndView custsync(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("suhomepage");

        Config config = new Config();
        GetConfig get = new GetConfig();
        config = get.getConfig();

        CustomerSync s = new CustomerSync();
        s.customersync(config);

        mv.addObject("message1", "QuickBooks Customers synchronized");
        return mv;

    }

    public ModelAndView paysync(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("suhomepage");

        Config config = new Config();
        GetConfig get = new GetConfig();
        config = get.getConfig();

        PaymentSync s = new PaymentSync();
        s.paymentsync(config);

        mv.addObject("message1", "QuickBooks Payments synchronized");
        return mv;

    }

    public ModelAndView invoicesync(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("suhomepage");
        Config config = new Config();
        GetConfig get = new GetConfig();
        config = get.getConfig();

        InvoiceSync s = new InvoiceSync();
        s.invoicesync(config);

        mv.addObject("message1", "QuickBooks Invoices synchronized");
        return mv;

    }

    public ModelAndView loadconfig(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv
                = new ModelAndView("suhomepage");
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource) this.getBean("dataSourceEDU", hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement ps = this.cn.createStatement(1004, 1007);
        ResultSet rs = ps.executeQuery("Select * from syncconfig");

        Config config = new Config();
        while (rs.next()) {
            config.setQbdburl(rs.getString("qbdburl"));
            config.setQbdbuser(rs.getString("qbdbuser"));
            config.setQbdbpswd(rs.getString("qbdbpswd"));
            config.setRwdburl(rs.getString("rwdburl"));
            config.setRwdbuser(rs.getString("rwdbuser"));
            config.setRwdbpswd(rs.getString("rwdbpswd"));
            config.setEdudburl(rs.getString("edudburl"));
            config.setEdudbuser(rs.getString("edudbuser"));
            config.setEdudbpswd(rs.getString("edudbpswd"));
            config.setStartdate(rs.getDate("startdate").toString());
            config.setItemname(rs.getString("itemname"));
        }
        int checkpoint = 0;
        if (config.getQbdburl() != null && config.getQbdbuser() != null && config.getQbdbpswd() != null & config.getRwdburl() != null && config.getRwdbuser() != null && config.getRwdbpswd() != null && config.getEdudburl() != null && config.getEdudbuser() != null && config.getEdudbpswd() != null && config.getStartdate() != null && config.getItemname() != null) {
            checkpoint = 1;
        }

        mv.addObject("config", config);
        mv.addObject("check", checkpoint);
        return mv;

    }

}
