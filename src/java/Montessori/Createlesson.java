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
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import quickbooksync.QBInvoice;

/**
 *
 * @author nmohamed
 */
public class Createlesson {
    Connection cn;
          private ServletContext servlet;
    
  
     public Createlesson(ServletContext s)
    {
        this.servlet = s;
    }   

    public Createlesson() {
      
    }
  
          
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    public void newlesson(String[] studentIds,Lessons newlessons) throws SQLException
    { int lessonid=0;
    DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",this.servlet);
       this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
       st.executeUpdate("insert into lessons(nombre_lessons,id_level,id_subject,id_subsection,date_created,id_usuario) values (' "+newlessons.getName()+"',"+newlessons.getLevel().getName()+","+newlessons.getSubject().getName()+","+newlessons.getSubsection().getName()+",now(),5)");
       
            ResultSet rs = st.executeQuery("select id_lessons from lessons where nombre_lessons =' "+newlessons.getName()+"'");
            while(rs.next())
            {
            lessonid = rs.getInt("id_lessons");
                
            }
            for( int i = 0; i <= studentIds.length - 1; i++)
            {
                st.executeUpdate("insert into lessons_students(id_lessons,id_students) values ('"+lessonid+"','"+studentIds[i]+"')");
            }
            st.executeUpdate("insert into lessons_time(teacher_id,lesson_id,lesson_start,lesson_end) values (5,"+lessonid+",'"+newlessons.getStart()+"','"+newlessons.getFinish()+"')");
    }
       
       
       }
    

