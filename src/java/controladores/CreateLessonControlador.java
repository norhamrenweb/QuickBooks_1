/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        ResultSet rs = st.executeQuery("SELECT GradeLevel FROM AH_ZAF.dbo.GradeLevels");
        List <String> grades = new ArrayList();
        grades.add("Select level");
        while(rs.next())
        {
        grades.add(rs.getString("GradeLevel"));
        }
        mv.addObject("gradelevels", grades);
        
        return mv;
    }

    public ModelAndView studentlistLevel(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        List <Students> studentsgrades = new ArrayList();
        studentsgrades =this.getStudentslevel(hsr.getParameter("seleccion"));
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
             int levelid = 0;
            String consulta = "SELECT GradeLevelID FROM AH_ZAF.dbo.GradeLevels where GradeLevel ='"+hsr.getParameter("seleccion1")+"'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                levelid = rs.getInt("GradeLevelID");
            }
            cn.close();
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
             this.cn = dataSource.getConnection();
             st = this.cn.createStatement();
          ResultSet rs1 = st.executeQuery("select nombre_subject from subject where id_level="+levelid);
           Subject s = new Subject();
          s.setName("Select Subsection");
          subjects.add(s);
           String[] ids = null;
           while (rs1.next())
            {
             Subject sub = new Subject();
            ids[0] = String.valueOf(rs1.getInt("id_subsection"));
             sub.setId(ids);
             sub.setName(rs1.getString("nombre_sub_section"));
                subjects.add(sub);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
        }
        
        
         mv.addObject("subjects", subjects);
        
        return mv;
    }
    
    public ModelAndView subsectionlistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
        List<Subsection> subsections = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             int subjectid = 0;
            String consulta = "SELECT id FROM public.subject where nombre_subject ='"+hsr.getParameter("seleccion2")+"'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                subjectid = rs.getInt("id");
            }
            
          ResultSet rs1 = st.executeQuery("select nombre_sub_section,id_subsection from subsection where id_subject="+subjectid);
          Subsection s = new Subsection();
          s.setName("Select Subsection");
          subsections.add(s);
           String[] ids = null;
           while (rs1.next())
            {
             Subsection sub = new Subsection();
            ids[0] = String.valueOf(rs1.getInt("id_subsection"));
             sub.setId(ids);
             sub.setName(rs1.getString("nombre_sub_section"));
                subsections.add(sub);
            }
          
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subsections: " + ex);
        }
        
        mv.addObject("templatessubsection", hsr.getParameter("seleccion2"));
        mv.addObject("subsections", subsections);
        
        return mv;
    }
    
    public ModelAndView equipmentlistSubsection(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
        List<Equipment> equipments = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             int subsectionid = 0;
            String consulta = "SELECT id_subsection FROM public.subsection where nombre_sub_section ='"+hsr.getParameter("seleccion3")+"'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                subsectionid = rs.getInt("id_subsection");
            }
            
          ResultSet rs1 = st.executeQuery("select nombre_activity_equipment,id_activity_equipment from activity_equipment where id_subsection="+subsectionid);
          String[] ids = null;
           while (rs1.next())
            {
             Equipment eq = new Equipment();
            ids[0] = String.valueOf(rs1.getInt("id_activity_equipment"));
             eq.setId(ids);
             eq.setName(rs1.getString("nombre_activity_equipment"));
                equipments.add(eq);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo equipments: " + ex);
        }
        
      
         mv.addObject("equipments", equipments);
        
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
     public ArrayList<Students> getStudentslevel(String grade) throws SQLException
    {
//        this.conectarOracle();
         if(!grade.equals("Select level")){
        ArrayList<Students> listaAlumnos = new ArrayList<>();
        try {
            
             Statement st = this.cn.createStatement();
           
            String consulta = "SELECT * FROM AH_ZAF.dbo.Students where Status = 'Enrolled' and GradeLevel ='"+grade+"'";
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
       
        return listaAlumnos;}
         else{ return this.getStudents();}
         
    }
     public ModelAndView createlesson(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createlesson");
       
         
        
       String[] studentIds = hsr.getParameterValues("destino[]");
       Lessons newlesson = new Lessons();
       String[] equipmentids;
       Subject subject = new Subject();
       Subsection subsection = new Subsection();
       Level level = new Level();
       level.setName(hsr.getParameter("TXTlevel"));
       level.setId(hsr.getParameterValues("TXTlevel"));
       subject.setName(hsr.getParameter("TXTsubject"));
       subject.setId(hsr.getParameterValues("TXTsubject"));
       subsection.setName(hsr.getParameter("TXTsubsection"));
       subsection.setId(hsr.getParameterValues("TXTsubsection"));
       equipmentids=hsr.getParameterValues("TXTequipment");
       
       newlesson.setDate(hsr.getParameter("TXTfecha"));
       newlesson.setStart(hsr.getParameter("TXThorainicio"));
       newlesson.setFinish(hsr.getParameter("TXThorafin"));
      newlesson.setLevel(level);
      newlesson.setSubject(subject);
      newlesson.setSubsection(subsection);
       newlesson.setEquipmentid(equipmentids);
       newlesson.setTemplate(false);
       String test = hsr.getParameter("TXTloadtemplates");
       if(test.equals("LoadTemplates"))
       {
       newlesson.setName(hsr.getParameter("lessons"));
       }
       else
       {
           newlesson.setName(hsr.getParameter("TXTnombreLessons"));
       }
       Createlesson c = new Createlesson(hsr.getServletContext());
       c.newlesson(studentIds,newlesson);
        
        mv.addObject("message", "Lesson created");
        
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
             int subjectid = 0;
            String consulta = "SELECT id FROM public.subject where nombre_subject ='"+hsr.getParameter("seleccionTemplate")+"'";
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                subjectid = rs.getInt("id");
            }
            
          ResultSet rs1 = st.executeQuery("select nombre_lessons,id_lessons from lessons where id_subject= "+subjectid+" and template = true");
          Lessons l = new Lessons();
          l.setName("Select lesson name");
          lessons.add(l);
           while (rs1.next())
            {
                 Lessons ll = new Lessons();
                 ll.setName(rs1.getString("nombre_lessons"));
                 ll.setId(rs1.getInt("id_lessons"));
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
              List<Equipment> allequipments = new ArrayList<>();
               List<Equipment> equipments = new ArrayList<>();
               Subsection sub = new Subsection();
             try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String description=null;
             String subsectionid[] = null;
            String consulta = "SELECT id_subsection,description FROM public.lessons where id_lessons ="+lessonplanid[0];
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                description = rs.getString("description");
                subsectionid[0] = String.valueOf(rs.getInt("id"));
            }
            
          ResultSet rs1 = st.executeQuery("select nombre_sub_section from public.subsection where id_subsection= "+subsectionid);
          
           while (rs1.next())
            {
                
                sub.setId(subsectionid);
                sub.setName("nombre_sub_section");
            }
        ResultSet rs2 = st.executeQuery("select id_activity_equipment,nombre_activity_equipment from public.activity_equipment where id_subsection= "+subsectionid);
        String[] ids = null;
   while (rs2.next())
            {
                Equipment eq = new Equipment();
               ids[0]= String.valueOf(rs2.getInt("id_activity_equipment"));
                eq.setId(ids);
                eq.setName(rs.getString("nombre_activity_equipment"));
                allequipments.add(eq);
            }
    ResultSet rs3 = st.executeQuery("SELECT nombre_activity_equipment,id_activity_equipment FROM public.activity_equipment where public.activity_equipment.id_activity_equipment IN (select id_equipment from public.lessons_equipment where id_lessons"+lessonplanid[0]+")");
       
   while (rs2.next())
            {
                Equipment eq = new Equipment();
                ids[0] = String.valueOf(rs2.getInt("id_activity_equipment"));
                eq.setId(ids);
                eq.setName(rs.getString("nombre_activity_equipment"));
                equipments.add(eq);
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo lessons: " + ex);
        }
        mv.addObject("allequipments",allequipments);
        mv.addObject("equipments",equipments);
        mv.addObject("subsection", sub);
             return mv;
         
         } 
}


