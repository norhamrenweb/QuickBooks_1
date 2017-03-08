/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
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
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 *
 * @author nmohamed
 */
public class CreateLessonControlador extends MultiActionController{
    
      Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
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
        DriverManagerDataSource dataSource2;
        dataSource2 = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource2.getConnection();
         Statement st2 = this.cn.createStatement();
        ResultSet rs1 = st2.executeQuery("SELECT name,id FROM public.method");
        List <Method> methods = new ArrayList();
        Method m = new Method();
        m.setName("Select Method");
        methods.add(m);
        while(rs1.next())
        {
            Method x = new Method();
             String[] ids = new String[1];
             ids[0]=""+rs1.getInt("id");
            x.setId(ids);
            x.setName(rs1.getString("name"));
        methods.add(x);
        }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods",methods);
        
        return mv;
    }

    public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
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
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
        List<Subject> subjects = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String[] levelid = new String[1];
//             String test = hsr.getParameter("seleccion1");
//            String consulta = "SELECT GradeLevelID FROM AH_ZAF.dbo.GradeLevels where GradeLevel ='"+hsr.getParameter("seleccion1")+"'";
//            ResultSet rs = st.executeQuery(consulta);
//          
//            while (rs.next())
//            {
//                levelid = rs.getInt("GradeLevelID");
//            }
//            cn.close();
            dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
             this.cn = dataSource.getConnection();
             st = this.cn.createStatement();
             levelid= hsr.getParameterValues("seleccion1");
          ResultSet rs1 = st.executeQuery("select CourseID,Title from subject where LevelID="+levelid[0]);
           Subject s = new Subject();
          s.setName("Select Subject");
          subjects.add(s);
           
           while (rs1.next())
            {
             Subject sub = new Subject();
             String[] ids = new String[1];
            ids[0]=""+rs1.getInt("Course_ID");
             sub.setId(ids);
             sub.setName(rs1.getString("Title"));
                subjects.add(sub);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
        }
        
        
         mv.addObject("subjects", subjects);
        
        return mv;
    }
    
    public ModelAndView objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
        List<Objective> objectives = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String subjectid = null;
          
            
                subjectid = hsr.getParameter("seleccion2");
            
            
          ResultSet rs1 = st.executeQuery("select name,id from public.objective where subject_id="+subjectid);
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
        
        mv.addObject("templatessubsection", hsr.getParameter("seleccion2"));
        mv.addObject("objectives", objectives);
        
        return mv;
    }
    
    public ModelAndView contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
        List<Content> contents = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String objectiveid = null;
            
                objectiveid = hsr.getParameter("seleccion3");
            
            
          ResultSet rs1 = st.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id ="+objectiveid+")");
          //String[] ids = new String[1];
           while (rs1.next())
            {
//             Equipment eq = new Equipment();
//            ids[0] = String.valueOf(rs1.getInt("id"));
//             eq.setId(ids);
//             eq.setName(rs1.getString("name"));
//                equipments.add(eq);
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
        
      
         mv.addObject("contents", contents);
        
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
    // get students filtrado por gradelevel
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
     public ModelAndView createlesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        String message = "Lesson created";
        ModelAndView mv = new ModelAndView("redirect:/createlesson.htm?select=start", "message", message);
       HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
         
        
       String[] studentIds = hsr.getParameterValues("destino[]");
       Lessons newlesson = new Lessons();
       String[] contentids;
       Subject subject = new Subject();
       Objective objective = new Objective();
       Level level = new Level();
       level.setName(hsr.getParameter("TXTlevel"));
       level.setId(hsr.getParameterValues("TXTlevel"));
       subject.setName(hsr.getParameter("TXTsubject"));
       subject.setId(hsr.getParameterValues("TXTsubject"));
       objective.setName(hsr.getParameter("TXTobjective"));
       objective.setId(hsr.getParameterValues("TXTobjective"));
       contentids=hsr.getParameterValues("TXTcontent");
       newlesson.setComments(hsr.getParameter("TXTcomments"));

       java.sql.Timestamp timestampstart = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha")+" "+hsr.getParameter("TXThorainicio")+":00.000");
     java.sql.Timestamp timestampend = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha")+" "+hsr.getParameter("TXThorafin")+":00.000");

       
       newlesson.setStart(""+timestampstart);
     newlesson.setFinish(""+timestampend);
     newlesson.setTeacherid(user.getId());
       
      newlesson.setLevel(level);
      newlesson.setSubject(subject);
      newlesson.setObjective(objective);
       newlesson.setContentid(contentids);
       
//       String test = hsr.getParameter("TXTloadtemplates");
//       if(test != null)
//       {        
//       newlesson.setName(hsr.getParameter("lessons"));
//       newlesson.setTemplate(true);
//       String x = hsr.getParameter("lessons");
//       newlesson.setId(Integer.parseInt(hsr.getParameter("lessons")));
//       }
//       else
//       {
           newlesson.setName(hsr.getParameter("TXTnombreLessons"));
           newlesson.setTemplate(false);
           
     //  }
       Createlesson c = new Createlesson(hsr.getServletContext());
       c.newlesson(studentIds,newlesson);
        
       
        
        return mv;
    }
//coge la nombre de subject seleccionado y devuelve la lista de lessons que son templates 
     public ModelAndView namelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
     {
     ModelAndView mv = new ModelAndView("createlesson");
     List<Lessons> lessons = new ArrayList<>();
     
      try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String subjectid = null;
        
                subjectid = hsr.getParameter("seleccionTemplate") ;
          
            
          ResultSet rs1 = st.executeQuery("select name,id from lesson_plan where subject_id= "+subjectid);
          Lessons l = new Lessons();
          l.setName("Select lesson name");
          lessons.add(l);
           while (rs1.next())
            {
                 Lessons ll = new Lessons();
                 ll.setName(rs1.getString("name"));
                 ll.setId(rs1.getInt("id"));
                lessons.add(ll);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }
        
      
         mv.addObject("lessons", lessons);
     
     return mv;
     }
         public ModelAndView loadLessonplan(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
         {
             ModelAndView mv = new ModelAndView("createlesson");
             String[] lessonplanid = hsr.getParameterValues("seleccionTemplate");
              List<Content> allcontents = new ArrayList<>();
               List<Content> subset = new ArrayList<>();
               List<Content> contents = new ArrayList<>();
               Objective sub = new Objective();
             try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String description=null;
             String[] objectiveid = new String[1];
            String consulta = "SELECT objective_id FROM public.lessons where id ="+lessonplanid[0];
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
            //    description = rs.getString("comments");
                objectiveid[0] = ""+rs.getInt("objective_id");
            }
            consulta = "select name from public.objective where id= "+objectiveid[0];
          ResultSet rs1 = st.executeQuery(consulta);
          
           while (rs1.next())
            {
                
                sub.setId(objectiveid);
                sub.setName(rs1.getString("name"));
            }
        ResultSet rs2 = st.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select content_id from public.objective_content where public.objective_content.objective_id = "+objectiveid[0]);
        // must change latter
        int i = 0;
   while (rs2.next())
            {
                Content eq = new Content();
                String[] ids = new String[1];
               ids[0]= ""+rs2.getInt("id");
              
                eq.setId(ids);
              eq.setName(rs2.getString("name"));
                allcontents.add(eq);
            }
    ResultSet rs3 = st.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select content_id from public.lesson_content where public.lesson_content.lessonplan_id = "+lessonplanid[0]+")");
       
   while (rs3.next())
            {
                Content eq = new Content();
             String[] ids = new String[1];
             ids[0] = ""+rs3.getInt("id");
                eq.setId(ids);
                eq.setName(rs3.getString("name"));
                contents.add(eq);
            }
        } catch (SQLException ex) {
            System.out.println("Error  " + ex);
        }
 
        mv.addObject("equipments",contents);
        
        for (Content e : allcontents)
        {
            String result = "";
        for(Content e2 :contents)
        {
            if(e.getName().equals(e2.getName()))
            {
                result ="match found";
               break;
            }
            
        }
        if (!result.equals("match found")){
        subset.add(e);
        }
        }
      
 
        mv.addObject("objective", sub);
               mv.addObject("allcontents",subset);
             return mv;
         
         } 
}


