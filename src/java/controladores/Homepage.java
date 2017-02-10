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

import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapcusts.Getcusts;
import quickbooksync.*;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


public class Homepage extends MultiActionController  {
   Connection cn;
    private Object getBean(String nombrebean, ServletContext servlet)
{
ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
}
    public ModelAndView inicio(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    
        return new ModelAndView("userform");
    }
  
public ModelAndView login(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String nombreusu = hsr.getParameter("txtusuario");
        int usertype = 0;
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
      Statement ps = this.cn.createStatement(1004,1007);
      ResultSet rs = ps.executeQuery("SELECT typeuser FROM usuarios where nombre='"+ nombreusu+"'");
       if (!rs.next())
       {
           ModelAndView mv = new ModelAndView("userform");
        String message = "No access rights defined";
        mv.addObject("message", message);
        return mv;
       }
       else{
       rs.beforeFirst();
           while (rs.next())
            {
            usertype= rs.getInt("typeuser");
            }
        if (usertype == 3)
        {
        ModelAndView mv = new ModelAndView("suhomepage");
        String message = "Welcome super user";
        mv.addObject("message", message);
        return mv;
        }
        else
        {
         ModelAndView mv = new ModelAndView("homepage");
        String  message = "welcome user";
        mv.addObject("message", message);
        return mv;
        }

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

        dataSource = (DriverManagerDataSource)this.getBean("dataSourceEDU",hsr.getServletContext());
        this.cn = dataSource.getConnection();
      Statement ps = this.cn.createStatement(1004,1007);
      ps.executeUpdate("insert into sysconfig(qbdburl,qbdbuser,qbdbpswd,edudburl,edudbuser,edudbpswd,rwdburl,rwdbuser,rwdbpswd,startdate,itemname) values ('"+qbdburl+"','"+qbdbuser+"','"+qbdbpswd+"','"+edudburl+"','"+edudbuser+"','"+edudbpswd+"','"+rwdburl+"','"+rwdbuser+"','"+rwdbpswd+"','"+startdate+"','"+itemname+"','");
    
    /*FileOutputStream fis = new FileOutputStream("C:\\Users\\Public\\config.txt");
      ObjectOutputStream ois = new ObjectOutputStream(fis);
      try{
      ois..writeChars(qbdburl+";"+qbdbuser+";"+qbdbpswd+";"+rwdburl+";"+rwdbuser+";"+rwdbpswd+";"+edudburl+";"+edudbuser+";"+edudbpswd+";"+startdate+";"+itemname);
      }
      catch(IOException ex){
      
       mv.addObject("message", "Configuration setting not saved");
      }*/
//    FileOutputStream fop = null;
//		File file;
//		String content = qbdburl+"&"+qbdbuser+"&"+qbdbpswd+"&"+rwdburl+"&"+rwdbuser+"&"+rwdbpswd+"&"+edudburl+"&"+edudbuser+"&"+edudbpswd+"&"+startdate+"&"+itemname;// if a pswd has a . this will not work
//
//		
//
//			file = new File("C:\\Users\\Public\\config.txt");
//			fop = new FileOutputStream(file);
//
//			// if file doesnt exists, then create it
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//
//			// get the content in bytes
//			byte[] contentInBytes = content.getBytes();
//
//			fop.write(contentInBytes);
//			fop.flush();
//			fop.close();
String message = "Configuration setting saved";
      mv.addObject("message1",message );
    return mv;


}
public ModelAndView runsync(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
ModelAndView mv = 
                new ModelAndView("suhomepage");
DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceEDU",hsr.getServletContext());
        this.cn = dataSource.getConnection();
      Statement ps = this.cn.createStatement(1004,1007);
      ResultSet rs = ps.executeQuery("Select * from syncconfig");
//File file = new File("C:\\Users\\Public\\config.txt");
//		FileInputStream fis = null;
//                fis = new FileInputStream(file);
//
//			
//			int content;
//                        StringBuilder builder = new StringBuilder();
//                       
//			while ((content = fis.read()) != -1) {
//				// convert to char and display it
//				builder.append((char)content);
//			}
//                        String conf = builder.toString();
//String[] x = conf.split("&");
String[] x = null;
while(rs.next())
{
x[0]=rs.getString("qbdburl");
x[1]=rs.getString("qbdbuser");
x[2]=rs.getString("qbdbpswd");
x[3]=rs.getString("rwdburl");
x[4]=rs.getString("rwdbuser");
x[5]=rs.getString("rwdbpswd");
x[6]=rs.getString("edudburl");
x[7]=rs.getString("edudbuser");
x[8]=rs.getString("edudbpswd");
x[9]= rs.getDate("startdate").toString();
x[10]=rs.getString("itemname");
}
                        
                      Runsync s = new Runsync();
                      s.runsync(x);
                       
mv.addObject("message1", "QB synchronized");
return mv;



}
public ModelAndView map(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
ModelAndView mv = new ModelAndView("familymap2");
Getcusts l = new Getcusts();
List <QBCustomer> allcustomer= new ArrayList<>();
allcustomer = l.getCustomer();
List <RWFamily> allfamily= new ArrayList<>();
allfamily = l.getFamily();
mv.addObject("allcustomer", allcustomer);
mv.addObject("allfamily", allfamily);

return mv;



}
}
