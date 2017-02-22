/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author nmohamed
 */
public class Subject {
    private String[] id;
    private String name;
    Connection cn;
    private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    public String[] getId() {
        return id;
    }

    public void setId(String[] id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String fetchName(int id, ServletContext servlet)
    { String subjectName = null ;
        try {
             DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",servlet);
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT nombre_subject FROM public.subject where id = "+id;
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                subjectName = rs.getString("nombre_subject");
                
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return subjectName;
    
    }   
    
}
