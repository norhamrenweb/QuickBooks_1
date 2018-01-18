/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import atg.taglib.json.util.JSONObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.google.gson.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
/**
 *
 * @author nmohamed
 */
@Controller
public class CreateLessonControlador {
static Logger log = Logger.getLogger(CreateLessonControlador.class.getName());      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    @RequestMapping("/createlesson/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("createlesson");
        try{
       List <Lessons> ideas = new ArrayList();
        mv.addObject("listaAlumnos", this.getStudents());
        ResultSet rs = DBConect.ah.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");
        List <Level> grades = new ArrayList();
        Level l = new Level();
        l.setName("Select level");
        String[] aux= new String[1];
        aux[0]= "-1";
        l.setId(aux);
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
        ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT * FROM public.method");
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
            x.setDescription(rs1.getString("description"));
        methods.add(x);
        }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods",methods);
       //get lesson ideas
       ResultSet rs2 = DBConect.eduweb.executeQuery("SELECT * FROM public.lessons where idea = true");
        Lessons d = new Lessons();
       d.setName("Select an idea");
       ideas.add(d);
        while(rs2.next())
        {
         Lessons idea = new Lessons();   
        idea.setId(rs2.getInt("id")); 
        idea.setName(rs2.getString("name"));
        ideas.add(idea);
        }
        mv.addObject("ideas",ideas);
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        return mv;
    }
    
    @RequestMapping("/createlesson/startIdea.htm")
    public ModelAndView startIdea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("lessonidea");
        try{
       List <Lessons> ideas = new ArrayList();
        mv.addObject("listaAlumnos", this.getStudents());
        ResultSet rs = DBConect.ah.executeQuery("SELECT GradeLevel,GradeLevelID FROM AH_ZAF.dbo.GradeLevels");
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
        ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT * FROM public.method");
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
            x.setDescription(rs1.getString("description"));
        methods.add(x);
        }
            mv.addObject("gradelevels", grades);
            mv.addObject("methods",methods);
       //get lesson ideas
       ResultSet rs2 = DBConect.eduweb.executeQuery("SELECT * FROM public.lessons where idea = true");
        while(rs2.next())
        {
         Lessons idea = new Lessons();   
        idea.setId(rs2.getInt("id")); 
        idea.setName(rs2.getString("name"));
        ideas.add(idea);
        }
        mv.addObject("ideas",ideas);
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
      
        return mv;
    }
    
@RequestMapping("/createlesson/studentlistLevel.htm")
    public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("createlesson");
        
        List <Students> studentsgrades = new ArrayList();
        String[] levelid = hsr.getParameterValues("seleccion");
        String test = hsr.getParameter("levelStudent");
        if(levelid[0]!="")
        {
            studentsgrades =this.getStudentslevel(levelid[0]);
        }
        else{
            studentsgrades = this.getStudents();
        }
        mv.addObject("listaAlumnos",studentsgrades );
        
        return mv;
    }
    @RequestMapping("/createlesson/subjectlistLevel.htm")
    public ModelAndView subjectlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("createlesson");
         mv.addObject("subjects",this.getSubjects(hsr.getParameterValues("seleccion1")));
        
        return mv;
    }
        @RequestMapping("/createlesson/objectivelistSubject.htm")
    public ModelAndView objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("createlesson");
        
    //    mv.addObject("templatessubsection", hsr.getParameter("seleccion2"));
        mv.addObject("objectives", this.getObjectives(hsr.getParameterValues("seleccion2")));
        
        return mv;
    }
    @RequestMapping("/createlesson/contentlistObjective.htm")
    public ModelAndView contentlistObjective(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        ModelAndView mv = new ModelAndView("createlesson");
      
         mv.addObject("contents", this.getContent(hsr.getParameterValues("seleccion3")));
        
        return mv;
    }   
    
    public ArrayList<Students> getStudents() throws SQLException
    {
//        this.conectarOracle();
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        try {
            
             
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled' order by LastName";
            ResultSet rs = DBConect.ah.executeQuery(consulta);
          
            while (rs.next())
            {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("LastName")+", "+ rs.getString("FirstName")+" "+ rs.getString("MiddleName"));
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
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
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
            
            ResultSet rs1= DBConect.ah.executeQuery("select GradeLevel from AH_ZAF.dbo.GradeLevels where GradeLevelID ="+gradeid);
             while(rs1.next())
             {
             gradelevel = rs1.getString("GradeLevel");
             }
           
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled' and GradeLevel = '"+gradelevel+"' order by LastName";
            ResultSet rs = DBConect.ah.executeQuery(consulta);
          
            while (rs.next())
            {
                Students alumnos = new Students();
                alumnos.setId_students(rs.getInt("StudentID"));
                alumnos.setNombre_students(rs.getString("LastName")+", "+ rs.getString("FirstName")+" "+ rs.getString("MiddleName"));
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
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       
        return listaAlumnos;
         
         
    }
     @RequestMapping("/createlesson/createlesson.htm")
     public ModelAndView createlesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
        String message = new String();    
        try{
       HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
       Lessons newlesson = new Lessons();
       List<String> contentids;
       Subject subject = new Subject();
       Objective objective = new Objective();
       Level level = new Level();
       level.setName(hsr.getParameter("TXTlevel"));
       level.setId(hsr.getParameterValues("TXTlevel"));
       subject.setName(hsr.getParameter("TXTsubject"));
       subject.setId(hsr.getParameterValues("TXTsubject"));
       objective.setName(hsr.getParameter("TXTobjective"));
       objective.setId(hsr.getParameterValues("TXTobjective"));
       String[] test = hsr.getParameterValues("TXTcontent");
       //optional field, avoid null pointer exception
       if(test!=null && test.length>0){
       contentids=Arrays.asList(hsr.getParameterValues("TXTcontent"));
       newlesson.setContentid(contentids);
       }
       newlesson.setComments(hsr.getParameter("TXTdescription"));
       Method m = new Method();
       String[] test2 = hsr.getParameterValues("TXTmethod");
       //optional field, avoid null pointer exception
       if(test2!=null && test2.length>0){
       m.setId(hsr.getParameterValues("TXTmethod"));
       m.setName(hsr.getParameter("TXTmethod"));
       newlesson.setMethod(m);
       }
       else{
       m.setName("");
       newlesson.setMethod(m);
       }
       String[] ideaCheck = hsr.getParameterValues("ideaCheck");

      
     newlesson.setTeacherid(user.getId());
       
      newlesson.setLevel(level);
      newlesson.setSubject(subject);
      newlesson.setObjective(objective);
       

           newlesson.setName(hsr.getParameter("TXTnombreLessons"));
           newlesson.setTemplate(false);

       Createlesson c = new Createlesson(hsr.getServletContext());
       // gives a null pointer exception , need to fix  
     if(ideaCheck!= null){
       if(ideaCheck[0].equals("on")){
           c.newidea(newlesson);
           message = "Presentation idea created";
       }}
       else{
           java.sql.Timestamp timestampstart = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha") + " " + hsr.getParameter("TXThorainicio") + ":00.000");
           java.sql.Timestamp timestampend = java.sql.Timestamp.valueOf(hsr.getParameter("TXTfecha") + " " + hsr.getParameter("TXThorafin") + ":00.000");
           String[] studentIds = hsr.getParameterValues("destino[]");

           newlesson.setStart("" + timestampstart);
           newlesson.setFinish("" + timestampend);
           c.newlesson(studentIds, newlesson); 
           message = "Presentation created";
       }
        
        }catch(SQLException ex){
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        ModelAndView mv = new ModelAndView("redirect:/createlesson/start.htm", "message", message);
        return mv;
    }
//coge la nombre de subject seleccionado y devuelve la lista de lessons que son templates 
     @RequestMapping("/createlesson/namelistSubject.htm")
     public ModelAndView namelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
     {
      if((new SessionCheck()).checkSession(hsr))
           return new ModelAndView("redirect:/userform.htm?opcion=inicio");
     ModelAndView mv = new ModelAndView("createlesson");
     List<Lessons> lessons = new ArrayList<>();
     
      try {

            String subjectid = null;
            subjectid = hsr.getParameter("seleccionTemplate") ;
            ResultSet rs1 = DBConect.eduweb.executeQuery("select name,id from lesson_plan where subject_id= "+subjectid);
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
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
        
      
         mv.addObject("lessons", lessons);
     
     return mv;
     }
     @RequestMapping("/createlesson/copyfromIdea.htm")
     @ResponseBody
         public String copyfromIdea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
         {              
             ModelAndView mv = new ModelAndView("createlesson");
             JSONObject json = new JSONObject();
             String[] lessonplanid = hsr.getParameterValues("seleccionidea");
               Subject sub = new Subject();
               Objective obj = new Objective();
             Method meth = new Method();
             Level lev = new Level();
             int levelid = 0;
             List<String> contents = new ArrayList<>();
              DriverManagerDataSource dataSource;
               try {
        
            
             String[] oid = new String[1];
             String[] sid = new String[1];
             String[] mid = new String[1];
             String[] cid = new String[1];
             
            String consulta = "SELECT objective_id,subject_id,level_id,method_id FROM public.lessons where id ="+lessonplanid[0];
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
          
            while (rs.next())
            {
                oid[0] = ""+rs.getInt("objective_id");
                obj.setId(oid);
                sid[0] = ""+rs.getInt("subject_id");
                sub.setId(sid);
                mid[0] = ""+rs.getInt("method_id");
             meth.setId(mid);
             levelid= rs.getInt("level_id");
            }
           
        ResultSet rs2 = DBConect.eduweb.executeQuery("select content_id from public.lesson_content where lesson_id = "+lessonplanid[0]);
              
   while (rs2.next())
            {
              //  Content eq = new Content();
                String ids = null;
               ids= ""+rs2.getInt("content_id");
              
             //   eq.setId(ids);
              
                contents.add(ids);
            }
         
         json.put("level",""+levelid);
         json.put("subject",new Gson().toJson(sub));
         json.put("objective",new Gson().toJson(obj));
         json.put("method",new Gson().toJson(meth));
         json.put("content",new Gson().toJson(contents));
         
        } catch (SQLException ex) {
            System.out.println("Error  " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
               String hi = json.toString();
               String[] ids= new String[1];
               ids[0]=""+levelid;
                json.put("objectiveslist",new Gson().toJson(this.getObjectives(sub.getId())));
                 json.put("contentslist",new Gson().toJson(this.getContent(obj.getId())));
               
               json.put("subjectslist",new Gson().toJson(this.getSubjects(ids)));
               
               
               
               return json.toString();
      
         } 
       public ArrayList<Subject> getSubjects(String[] levelid) throws SQLException
       {
           
        ArrayList<Subject> subjects = new ArrayList<>();
        ArrayList<Subject> activesubjects = new ArrayList<>();
             
          ResultSet rs1 = DBConect.ah.executeQuery("select CourseID from Course_GradeLevel where GradeLevel IN (select GradeLevel from GradeLevels where GradeLevelID ="+levelid[0]+")");
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
           String[] aux = new String[1];
           aux[0]="-1";
           Subject sub = new Subject();
           sub.setId(aux);
           sub.setName("Select subject");
           activesubjects.add(sub);
           for(Subject su:subjects.subList(1,subjects.size()))
          {
              String[] ids = new String[1];
              ids=su.getId();
           ResultSet rs2 = DBConect.ah.executeQuery("select Title,Active from Courses where CourseID = "+ids[0]);
           while(rs2.next())
           {
            if(rs2.getBoolean("Active")== true)
               {
                   su.setName(rs2.getString("Title"));
                   activesubjects.add(su);
               }
           }
          }
           return activesubjects;
       }
        public ArrayList<Objective> getObjectives(String[] subjectid) throws SQLException
       {
           ArrayList<Objective> objectives = new ArrayList<>();
       try {
          ResultSet rs1 = DBConect.eduweb.executeQuery("select name,id from public.objective where subject_id="+subjectid[0]);
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
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       return objectives;
       }
        public ArrayList<Content> getContent(String[] objectiveid) throws SQLException
       {
           ArrayList<Content> contents = new ArrayList<>();
       try {
          ResultSet rs1 = DBConect.eduweb.executeQuery("SELECT name,id FROM public.content where public.content.id IN (select public.objective_content.content_id from public.objective_content where public.objective_content.objective_id ="+objectiveid[0]+")");
         
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
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       return contents;
       }
}


