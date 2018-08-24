/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Norhan
 */
public class DBConect {
    
    public static Connection cn;
    public static Connection cn2;
    public static Connection cn3;
    public static String codeSchool;
    public static Statement eduweb;
    public static Statement ah;
    public static Statement eduwebBeforeFirst;
    
    public static String serverFtp;
    public static String userFTP;
    public static String passFTP;
    public static int portFTP;
    
    private Object getBean(String nombrebean, ServletContext servlet){
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    
    public static void close() throws SQLException{
        if(cn!=null)
            cn.close();
        if(cn2!=null)
            cn2.close();
        if(cn3!=null)
            cn3.close();
    }
    
    public DBConect (HttpServletRequest hsr, HttpServletResponse hsr1,String cSchool) throws Exception {
        //connection to comunication
        codeSchool = cSchool;
        serverFtp ="95.216.37.137";
        userFTP = "david";
        passFTP = "david";
        portFTP = 21;
        
        DriverManagerDataSource dataSource = (DriverManagerDataSource) this.getBean("dataSource", hsr.getServletContext());
        if(this.cn!=null)
            this.cn.close();
        this.cn = dataSource.getConnection();
        eduweb = this.cn.createStatement(); 
        //connection to datasourceAH
        DriverManagerDataSource dataSource2 = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        if(this.cn2!=null)
            this.cn2.close();
        this.cn2 = dataSource2.getConnection();
        this.ah = cn2.createStatement();      
         //connection to comunication
        DriverManagerDataSource dataSource3 = (DriverManagerDataSource) this.getBean("dataSource", hsr.getServletContext());
        if(this.cn3!=null)
            this.cn3.close();
        this.cn3 = dataSource3.getConnection();
        eduwebBeforeFirst = this.cn3.createStatement(1004,1007);
    }
}
