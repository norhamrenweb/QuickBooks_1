/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.*;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.gson.*;
import org.springframework.ui.ModelMap;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

/**
 *
 * @author nmohamed
 */
@Controller
public class EditLessonControlador {
     Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    @RequestMapping("/editlesson/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
       ModelAndView mv = new ModelAndView("editlesson");
       Lessons data = new Lessons();
       Level l = new Level();
       ArrayList<Content> c = new ArrayList<>();
       ArrayList<Students> stud = new ArrayList<>();
        Objective o = new Objective();
        Subject s = new Subject();
        Method m = new Method();
        String[] id = new String[1];
       DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
         Statement st = this.cn.createStatement();
         String lessonid = hsr.getParameter("LessonsSelected");
         ResultSet rs = st.executeQuery("select * from lessons where id= "+lessonid);
         while(rs.next()){
             data.setComments(rs.getString("comments"));
            Timestamp stamp = rs.getTimestamp("start");
               Timestamp finish = rs.getTimestamp("finish");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
               String dateStr = sdfDate.format(stamp);
                String timeStr = sdfTime.format(stamp);  
                String timeStr2 = sdfTime.format(finish);
                data.setDate(""+ dateStr);
                data.setStart(timeStr);
                data.setFinish(timeStr2);
                data.setId(Integer.parseInt(lessonid));
                l.setId(new String[] {""+rs.getInt("level_id")});
                m.setId(new String[] {""+rs.getInt("method_id")});
                data.setName(rs.getString("name"));
                o.setId(new String[] {""+rs.getInt("objective_id")});
                s.setId(new String[] {""+rs.getInt("subject_id")});
                
         }
         id = s.getId() ;
       s.setName(s.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
       data.setSubject(s);
       id=null;
       id = m.getId();
       m.setName(m.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
       data.setMethod(m);
        id = o.getId();
       o.setName(o.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
        data.setObjective(o);
        id = l.getId();
       l.setName(l.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
       data.setLevel(l);
       ResultSet rs2 = st.executeQuery("select * from lesson_content where lesson_id = "+lessonid);
       while(rs2.next())
       {
           id[0] = rs2.getString("content_id");
           Content eq = new Content();
           eq.setId(id);
           c.add(eq);
       }
       for(Content x:c)
       {
       x.setName(x.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
       }
       ResultSet rs3 = st.executeQuery("select student_id from lesson_stud_att where lesson_id = "+lessonid);
   while(rs3.next())
   {
   Students learner = new Students();
   learner.setId_students(rs3.getInt("student_id"));
   stud.add(learner);
  
   }
   cn.close();
    dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
         Statement st2 = this.cn.createStatement();
         
       for(Students y:stud)
       {
           ResultSet rs4 =st2.executeQuery("SELECT FirstName,LastName FROM AH_ZAF.dbo.Students where StudentID = '"+y.getId_students()+"'");
           while(rs4.next())
           {
           y.setNombre_students(rs4.getString("FirstName")+","+rs4.getString("LastName"));
           }
       }
       
       cn.close();
       mv.addObject("data",data);
        mv.addObject("objectives",this.getObjectives(data.getSubject().getId()));
                 mv.addObject("contents",this.getContent(data.getObjective().getId()));
               
                 cn.close();
                  dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
               mv.addObject("subjects",this.getSubjects(data.getLevel().getId()));
         List <Lessons> ideas = new ArrayList();
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        mv.addObject("listaAlumnos", this.getStudents());
        Statement st4 = this.cn.createStatement();
        ResultSet rs4 = st4.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");
        List <Level> grades = new ArrayList();
        Level le = new Level();
        le.setName("Select level");
        grades.add(le);
        while(rs4.next())
        {
            Level x = new Level();
             String[] ids = new String[1];
             ids[0]=""+rs4.getInt("GradeLevelID");
            x.setId(ids);
            x.setName(rs4.getString("GradeLevel"));
        grades.add(x);
        }
        DriverManagerDataSource dataSource2;
        dataSource2 = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource2.getConnection();
         Statement st3 = this.cn.createStatement();
        ResultSet rs1 = st3.executeQuery("SELECT * FROM public.method");
        List <Method> methods = new ArrayList();
        Method me = new Method();
        me.setName("Select Method");
        methods.add(me);
        while(rs1.next())
        {
            Method x = new Method();
             String[] ids = new String[1];
             ids[0]=""+rs1.getInt("id");
            x.setId(ids);
            x.setName(rs1.getString("name"));
            x.setDescription(rs1.getString("description"));
        methods.add(x);
        }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods",methods);
       //get lesson ideas
       ResultSet rs5 = st3.executeQuery("SELECT * FROM public.lessons where idea = true");
        while(rs5.next())
        {
         Lessons idea = new Lessons();   
        idea.setId(rs5.getInt("id")); 
        idea.setName(rs5.getString("name"));
        ideas.add(idea);
        }
        mv.addObject("ideas",ideas);
       return mv;
       
    }
        public ArrayList<Subject> getSubjects(String[] levelid) throws SQLException
       {
           
        ArrayList<Subject> subjects = new ArrayList<>();
           Statement st = this.cn.createStatement();
             
          ResultSet rs1 = st.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID ="+levelid[0]+")");
           Subject s = new Subject();
          s.setName("Select Subject");
          subjects.add(s);
           
           while (rs1.next())
            {
             Subject sub = new Subject();
             String[] ids = new String[1];
            ids[0]=""+rs1.getInt("CourseID");
             sub.setId(ids);
            
                subjects.add(sub);
            }
           for(Subject su:subjects.subList(1,subjects.size()))
          {
              String[] ids = new String[1];
              ids=su.getId();
           ResultSet rs2 = st.executeQuery("select Title from Courses where CourseID = "+ids[0]);
           while(rs2.next())
           {
           su.setName(rs2.getString("Title"));
           }
          }
           return subjects;
       }
        public ArrayList<Objective> getObjectives(String[] subjectid) throws SQLException
       {
           ArrayList<Objective> objectives = new ArrayList<>();
       try {
        
             Statement st = this.cn.createStatement();
            
          ResultSet rs1 = st.executeQuery("select name,id from public.objective where subject_id="+subjectid[0]);
          Objective s = new Objective();
          s.setName("Select Objective");
          objectives.add(s);
           
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
       return objectives;
       }
        public ArrayList<Content> getContent(String[] objectiveid) throws SQLException
       {
           ArrayList<Content> contents = new ArrayList<>();
       try {
        
             Statement st = this.cn.createStatement();
            
            
          ResultSet rs1 = st.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id ="+objectiveid[0]+")");
         
           while (rs1.next())
            {
                Content eq = new Content();
                String[] id= new String[1];
               id[0]= ""+rs1.getInt("id");
              
                eq.setId(id);
              eq.setName(rs1.getString("name"));
               contents.add(eq);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo contents: " + ex);
        }
       return contents;
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
                alumnos.setNextlevel("Placement");
                alumnos.setSubstatus("Substatus");
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return listaAlumnos;
         
         
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
                alumnos.setNextlevel("Placement");
                alumnos.setSubstatus("Substatus");
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return listaAlumnos;
    }
         @RequestMapping("/editlesson/studentlistLevel.htm")
             public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("editlesson");
       
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
    @RequestMapping("/editlesson/subjectlistLevel.htm")
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("editlesson");
        
       try {
         DriverManagerDataSource dataSource;
       
           
            dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
             this.cn = dataSource.getConnection();
            
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
        }
        
        
         mv.addObject("subjects",this.getSubjects(hsr.getParameterValues("seleccion1")));
        
        return mv;
    }
        @RequestMapping("/editlesson/objectivelistSubject.htm")
    public ModelAndView objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("editlesson");
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
    //    mv.addObject("templatessubsection", hsr.getParameter("seleccion2"));
        mv.addObject("objectives", this.getObjectives(hsr.getParameterValues("seleccion2")));
        
        return mv;
    }
    @RequestMapping("/editlesson/contentlistObjective.htm")
    public ModelAndView contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("editlesson");
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();    
      
         mv.addObject("contents", this.getContent(hsr.getParameterValues("seleccion3")));
        
        return mv;
    }
    }
