/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import static Montessori.Resource.RUTA_FTP;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.apache.commons.net.ftp.FTPClient;
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
    { String lessonid= null;
    List<String> equipmentids;
    DriverManagerDataSource dataSource;
    FTPClient ftpClient = new FTPClient();
    
    try{
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",this.servlet);
       this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
        String test = null;
        String server = "192.168.1.36";
		int port = 21;
		String user = "david";
		String pass = "david";
        if(newlessons.getMethod().getName() != ""){
        test = "insert into lessons(name,level_id,subject_id,objective_id,date_created,user_id,start,finish,comments,method_id,archive,presentedby,idea) values (' "+newlessons.getName()+"',"+newlessons.getLevel().getName()+","+newlessons.getSubject().getName()+","+newlessons.getObjective().getName()+",now(),"+newlessons.getTeacherid()+",'"+newlessons.getStart()+"','"+newlessons.getFinish()+"','"+newlessons.getComments()+"','"+newlessons.getMethod().getName()+"',false,0,false)";
        }
        else{
          test = "insert into lessons(name,level_id,subject_id,objective_id,date_created,user_id,start,finish,comments,archive,presentedby,idea) values (' "+newlessons.getName()+"',"+newlessons.getLevel().getName()+","+newlessons.getSubject().getName()+","+newlessons.getObjective().getName()+",now(),"+newlessons.getTeacherid()+",'"+newlessons.getStart()+"','"+newlessons.getFinish()+"','"+newlessons.getComments()+"',false,0,false)";  
        }
       st.executeUpdate(test,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = st.getGeneratedKeys();
        while(rs.next())
        {
        lessonid=""+rs.getInt(1);
        }
            
            for( int i = 0; i <= studentIds.length - 1; i++)
            {
                st.executeUpdate("insert into lesson_stud_att(lesson_id,student_id) values ('"+lessonid+"','"+studentIds[i]+"')");
            }
            //to avoid null pointer exception incase of lesson without content
            if(newlessons.getContentid()!=null){
                  equipmentids=newlessons.getContentid();
            
             for( int i = 0; i <= equipmentids.size() - 1; i++)
            {
                
                st.executeUpdate("insert into lesson_content(lesson_id,content_id) values ('"+lessonid+"','"+equipmentids.get(i)+"')");
            }
            }
//            File directorio=new File(RUTA_FTP+lessonid);
//            directorio.mkdir(); 
        // create a folder in the FTP to upload resources to the new presentation
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            boolean success = ftpClient.changeWorkingDirectory("/MontessoriTesting");
            if(success){
            ftpClient.mkd(lessonid+"-"+newlessons.getName());
            }
    }
    catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(Createlesson.class.getName()).log(Level.SEVERE, null, ex);
        }
  //          st.executeUpdate("insert into lessons_time(teacher_id,lesson_id,lesson_start,lesson_end) values (5,"+lessonid+",'"+newlessons.getStart()+"','"+newlessons.getFinish()+"')");
    }

    public void newidea(Lessons newlessons) throws SQLException {
     int lessonid=0;
    List<String> equipmentids;
    DriverManagerDataSource dataSource;
    try{
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",this.servlet);
       this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
         String test = null;
         //to avoid null pointer exception when there is no method
       if(newlessons.getMethod().getName() != ""){
        test = "insert into lessons(name,level_id,subject_id,objective_id,date_created,user_id,comments,method_id,archive,presentedby,idea) values (' "+newlessons.getName()+"',"+newlessons.getLevel().getName()+","+newlessons.getSubject().getName()+","+newlessons.getObjective().getName()+",now(),"+newlessons.getTeacherid()+",'"+newlessons.getComments()+"','"+newlessons.getMethod().getName()+"',false,0,true)";
       }
       else{
           test = "insert into lessons(name,level_id,subject_id,objective_id,date_created,user_id,comments,archive,presentedby,idea) values (' "+newlessons.getName()+"',"+newlessons.getLevel().getName()+","+newlessons.getSubject().getName()+","+newlessons.getObjective().getName()+",now(),"+newlessons.getTeacherid()+",'"+newlessons.getComments()+"',false,0,true)";
       }
       st.executeUpdate(test,Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = st.getGeneratedKeys();
        while(rs.next())
            {
            lessonid = rs.getInt(1);
                
            }

            //to avoid null pointer exception incase of lesson without content
            if(newlessons.getContentid()!=null){
                  equipmentids=newlessons.getContentid();
            
             for( int i = 0; i <= equipmentids.size()- 1; i++)
            {
                
                st.executeUpdate("insert into lesson_content(lesson_id,content_id) values ('"+lessonid+"','"+equipmentids.get(i)+"')");
            }
            }
          
    }
    catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }  
    
    }
       
       
       }
    

