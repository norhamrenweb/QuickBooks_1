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
import java.util.ArrayList;
import java.util.List;
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
    private List<String> objectives;

    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }
    
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
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",servlet);
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT Title FROM AH_ZAF.dbo.Courses where CourseID = "+id;
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                subjectName = rs.getString("Title");
                
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
       
        return subjectName;
    
    }   
    
    public ArrayList<String> fetchNameAndElective(int id, ServletContext servlet)
    { ArrayList<String> subjectName = new ArrayList<String>() ;
        try {
             
            String consulta = "SELECT Title,Elective FROM AH_ZAF.dbo.Courses where CourseID = "+id;
            ResultSet rs = DBConect.ah.executeQuery(consulta);
          
            while (rs.next())
            {
                subjectName.add(rs.getString("Title"));
                subjectName.add(""+rs.getBoolean("Elective"));
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
       
        return subjectName;
    }  
}
