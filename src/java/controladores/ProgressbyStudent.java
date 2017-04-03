/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import Montessori.Method;
import Montessori.Objective;
import Montessori.Students;
import Montessori.Subject;
import atg.taglib.json.util.JSONObject;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.google.gson.*;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author nmohamed
 */
@Controller
public class ProgressbyStudent {
     Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    // loads the levels
    @RequestMapping("/progressbystudent/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("progressbystudent");
       
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        mv.addObject("listaAlumnos", this.getStudents());
        Statement st = this.cn.createStatement();
        ResultSet rs = st.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");
        List <Level> grades = new ArrayList();
        Level l = new Level();
        l.setName("Select level");
        grades.add(l);
        while(rs.next())
        {
            Level x = new Level();
             String[] ids = new String[1];
             ids[0]=""+rs.getInt("GradeLevelID");
            x.setId(ids);
            x.setName(rs.getString("GradeLevel"));
        grades.add(x);
        }
        
            mv.addObject("gradelevels", grades);
        
        return mv;
    }
    public ArrayList<Students> getStudents() throws SQLException
    {
//        this.conectarOracle();
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        try {
            
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("FirstName")+","+rs.getString("LastName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setPlacement("Placement");
                alumnos.setSubstatus("Substatus");
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return listaAlumnos;
    }
    // loads the students based on the selected level
    @RequestMapping("/progressbystudent/studentlistLevel.htm")
    public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("progressbystudent");
       
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        List <Students> studentsgrades = new ArrayList();
        String[] levelid = hsr.getParameterValues("seleccion");
        String test = hsr.getParameter("levelStudent");
        studentsgrades =this.getStudentslevel(levelid[0]);
        mv.addObject("listaAlumnos",studentsgrades );
        
        return mv;
    }
    public List<Subject> getSubjects(String levelname) throws SQLException
    {
        List<Subject> subjects = new ArrayList<>();
         try {
            
             Statement st = this.cn.createStatement();
            
          ResultSet rs1 = st.executeQuery("select CourseID from Course_GradeLevel where GradeLevel= '"+levelname+"'");
     
           while (rs1.next())
            {
             Subject sub = new Subject();
             String[] ids = new String[1];
            ids[0]=""+rs1.getInt("CourseID");
             sub.setId(ids);
                subjects.add(sub);
            }
          for(Subject s:subjects)
          {
              String[] ids = new String[1];
              ids=s.getId();
           ResultSet rs2 = st.executeQuery("select Title from Courses where CourseID = "+ids[0]);
           while(rs2.next())
           {
           s.setName(rs2.getString("Title"));
           }
          }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
        }
         return subjects;
    }
    //loads list of subjects based on selected level
    @RequestMapping("/progressbystudent/subjectlistLevel.htm")
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("studentpage");
        
        String[] levelid = new String[1];
         levelid= hsr.getParameterValues("seleccion1");
      DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
         mv.addObject("subjects", this.getSubjects(levelid[0]));
        
        return mv;
    }
    // loads the list of objectives based on the selected subject
    @RequestMapping("/progressbystudent/objectivelistSubject.htm")
    public ModelAndView objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("studentpage");
        List<Objective> objectives = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String subjectid = null;
          
            
                subjectid = hsr.getParameter("seleccion2");
            
            
          ResultSet rs1 = st.executeQuery("select name,id from public.objective where subject_id="+subjectid);
         
           
           while (rs1.next())
            {
             String[] ids = new String[1];
                Objective sub = new Objective();
            ids[0] = ""+rs1.getInt("id");
             sub.setId(ids);
             sub.setName(rs1.getString("name"));
                objectives.add(sub);
            }
          
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Objectives: " + ex);
        }
        
   
        mv.addObject("objectives", objectives);
        
        
        return mv;
    }
    
    public ArrayList<Students> getStudentslevel(String gradeid) throws SQLException
    {
//        this.conectarOracle();
         
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        String gradelevel = null;
        try {
            
             Statement st = this.cn.createStatement();
            ResultSet rs1= st.executeQuery("select GradeLevel from AH_ZAF.dbo.GradeLevels where GradeLevelID ="+gradeid);
             while(rs1.next())
             {
             gradelevel = rs1.getString("GradeLevel");
             }
           
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled' and GradeLevel = '"+gradelevel+"'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("FirstName")+","+rs.getString("LastName"));
                alumnos.setFecha_nacimiento(rs.getString("Birthdate"));
                alumnos.setFoto(rs.getString("PathToPicture"));
                alumnos.setLevel_id(rs.getString("GradeLevel"));
                alumnos.setPlacement(rs.getString("Placement"));
                alumnos.setSubstatus(rs.getString("Substatus"));
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return listaAlumnos;
         
         
    }
    //based on student selected and objective selected
    @RequestMapping("/progressbystudent/loaddailyProgress.htm")
    @ResponseBody
    public String loaddailyProgress(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
    {
      //    ModelAndView mv = new ModelAndView("studentpage");
          String[] objectiveid = hsr.getParameterValues("seleccion3");
          String[] studentid = hsr.getParameterValues("seleccion");
            List<Progress> progress = new ArrayList<>();
            String finalrating = null;
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
             Statement st = this.cn.createStatement();
            
          ResultSet rs1 = st.executeQuery("select comment,comment_date,ratingname,lessonname from public.progresslessonname where objective_id="+objectiveid[0]+" AND student_id = "+studentid[0]+" AND COALESCE(generalcomment, FALSE) = FALSE ");
          
           while (rs1.next())
            {
          Progress p = new Progress();
          p.setComment(rs1.getString("comment"));
          p.setRating(rs1.getString("rating"));
          p.setLesson_name(rs1.getString("lessonname"));
           Timestamp stamp = rs1.getTimestamp("comment_date");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               String dateStr = sdfDate.format(stamp);
             p.setComment_date(dateStr);
             progress.add(p);
            }
           // select the latest rating to be presented as the final rating for this objective
        String consulta = "SELECT ratingname FROM public.progresslessonname where student_id = '"+studentid[0]+"' AND comment_date = (select max(comment_date)   from public.progress_report where student_id ="+studentid[0]+"AND objective_id ="+objectiveid[0]+"AND COALESCE(generalcomment, FALSE) = FALSE) AND objective_id ="+objectiveid[0]+"AND COALESCE(generalcomment, FALSE) = FALSE";
ResultSet rs2 = st.executeQuery(consulta);
while(rs2.next())
{
    finalrating= rs2.getString("rating");
}
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
//       Objective obj = new Objective();
//       
//       obj.setName( hsr.getParameter("seleccion3"));
//       Subject sub = new Subject();
//       sub.setName("test");
//      mv.addObject("objectives",obj);
//      mv.addObject("subjects",sub);

//        mv.addObject("progress", progress);
//        mv.addObject("finalrating",finalrating);
        String prog = new Gson().toJson(progress);
        String rating = new Gson().toJson(finalrating);
        JSONObject obj = new JSONObject();
        obj.put("progress", prog);
        obj.put("finalrating", rating);
          return obj.toString();
    }
    //load student demographics
    @RequestMapping("/progressbystudent/studentPage.htm")
    @ResponseBody
    public String studentPage(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
    {
     //    ModelAndView mv = new ModelAndView("progressbystudent");
    String[] studentIds = hsr.getParameterValues("selectStudent");
     Students student = new Students();
      JSONObject obj = new JSONObject();
    try {
            DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where StudentID = "+studentIds[0];
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
               
                student.setId_students(rs.getInt("StudentID"));
                student.setNombre_students(rs.getString("FirstName")+","+rs.getString("LastName"));
                student.setFecha_nacimiento(rs.getString("Birthdate"));
//                student.setFoto(rs.getString("PathToPicture"));
                student.setLevel_id(rs.getString("GradeLevel"));
//                student.setPlacement("Placement");
//                student.setSubstatus("Substatus");
               
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
     List<Subject> subjects = new ArrayList<>();
     subjects = this.getSubjects(student.getLevel_id());
    String info = new Gson().toJson(student);
    String sub = new Gson().toJson(subjects);
    obj.put("info", info);
    obj.put("sub",sub);
//    mv.addObject("student",student);
    
//     mv.addObject("subjects", this.getSubjects(student.getLevel_id()));//Integer.parseInt(alumnos.getLevel_id())));
  
         return obj.toString();
        
    }
    //loads list of objectives final rating & general comments based on the selected subject
     @RequestMapping("/progressbystudent/objGeneralcomments.htm")
    @ResponseBody
    public String objGeneralcomments(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
    {
        String[] subjectid = hsr.getParameterValues("selection");
        List<Progress> progress = new ArrayList<>();
        List<Objective> objectives = new ArrayList<>();
       try {
            DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();
             
            String consulta = "SELECT * FROM objective where subject_id = "+subjectid[0];
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
            Objective o = new Objective();
            o.setName(rs.getString("name"));
           o.setDescription(rs.getString("description"));
           String[] id = new String[1];
           id[0] = ""+rs.getInt("id");
            o.setId(id);
            objectives.add(o);
            }
            
            
          } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }   
       return subjectid[0];     
       } 
    
    
}
