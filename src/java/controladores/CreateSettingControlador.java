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
public class CreateSettingControlador extends MultiActionController{
    
      Connection cn;
      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
       
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
        
        ModelAndView mv = new ModelAndView("createsettings");
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
            dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
             this.cn = dataSource.getConnection();
             st = this.cn.createStatement();
             levelid= hsr.getParameterValues("seleccion1");
             String namesubject= hsr.getParameter("namesubject");
          ResultSet rs1 = st.executeQuery("select nombre_subject,id from subject where id_level="+levelid[0]);
           Subject s = new Subject();
          s.setName("Select Subject");
          subjects.add(s);
           
           while (rs1.next())
            {
             Subject sub = new Subject();
             String[] ids = new String[1];
            ids[0]=""+rs1.getInt("id");
             sub.setId(ids);
             sub.setName(rs1.getString("nombre_subject"));
                subjects.add(sub);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Subjects: " + ex);
        }
        
        
         mv.addObject("subjects", subjects);
        
        return mv;
    }
    
    public ModelAndView subsectionlistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
        List<Subsection> subsections = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String subjectid = null;
          
            
                subjectid = hsr.getParameter("seleccion2");
            
            
          ResultSet rs1 = st.executeQuery("select nombre_sub_section,id_subsection from public.subsection where id_subject="+subjectid);
          Subsection s = new Subsection();
          s.setName("Select Subsection");
          subsections.add(s);
           
           while (rs1.next())
            {
             String[] ids = new String[1];
                Subsection sub = new Subsection();
            ids[0] = ""+rs1.getInt("id_subsection");
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
             String subsectionid = null;
            
                subsectionid = hsr.getParameter("seleccion3");
            
            
          ResultSet rs1 = st.executeQuery("select nombre_activity_equipment,id_activity_equipment from activity_equipment where id_subsection="+subsectionid);
          String[] ids = new String[1];
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
     public ModelAndView createsetting(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        
        ModelAndView mv = new ModelAndView("createsettings");
        HttpSession sesion = hsr.getSession();
        User user = (User) sesion.getAttribute("user");
         
        int level = Integer.parseInt( hsr.getParameter("TXTlevel"));
        int subject = Integer.parseInt( hsr.getParameter("TXTsubject"));
        String namesubsection = hsr.getParameter("TXTnamenewsubsection");
        

        String consulta = "insert into subsection(id_subject,nombre_sub_section) values (?,?)";
        PreparedStatement pst = this.cn.prepareStatement(consulta);
        pst.setInt(1, subject);
        pst.setString(2, namesubsection);

        int registros = pst.executeUpdate();
        
         

        
        mv.addObject("message", registros);
        
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
         //   String consulta = "SELECT id FROM public.subject where nombre_subject ='"+hsr.getParameter("seleccionTemplate")+"'";
          //  ResultSet rs = st.executeQuery(consulta);
          
//            while (rs.next())
//            {
                subjectid = hsr.getParameter("seleccionTemplate") ;
            //}
            
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
               List<Equipment> subset = new ArrayList<>();
               List<Equipment> equipments = new ArrayList<>();
               Subsection sub = new Subsection();
             try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
        
            
             Statement st = this.cn.createStatement();
             String description=null;
             String subsectionid[] = new String[1];
            String consulta = "SELECT id_subsection,description FROM public.lessons where id_lessons ="+lessonplanid[0];
            ResultSet rs = st.executeQuery(consulta);
          
            while (rs.next())
            {
                description = rs.getString("description");
                subsectionid[0] = ""+rs.getInt("id_subsection");
            }
            consulta = "select nombre_sub_section from public.subsection where id_subsection= "+subsectionid[0];
          ResultSet rs1 = st.executeQuery(consulta);
          
           while (rs1.next())
            {
                
                sub.setId(subsectionid);
                sub.setName(rs1.getString("nombre_sub_section"));
            }
        ResultSet rs2 = st.executeQuery("select id_activity_equipment,nombre_activity_equipment from public.activity_equipment where id_subsection= "+subsectionid[0]);
        // must change latter
        int i = 0;
   while (rs2.next())
            {
                Equipment eq = new Equipment();
                String[] ids = new String[1];
               ids[0]= ""+rs2.getInt("id_activity_equipment");
              
                eq.setId(ids);
                eq.setName(rs2.getString("nombre_activity_equipment"));
                allequipments.add(eq);
            }
    ResultSet rs3 = st.executeQuery("SELECT nombre_activity_equipment,id_activity_equipment FROM public.activity_equipment where public.activity_equipment.id_activity_equipment IN (select id_equipment from public.lessons_equipment where public.lessons_equipment.id_lessons = "+lessonplanid[0]+")");
       
   while (rs3.next())
            {
                Equipment eq = new Equipment();
             String[] ids = new String[1];
             ids[0] = ""+rs3.getInt("id_activity_equipment");
                eq.setId(ids);
                eq.setName(rs3.getString("nombre_activity_equipment"));
                equipments.add(eq);
            }
        } catch (SQLException ex) {
            System.out.println("Error  " + ex);
        }
 
        mv.addObject("equipments",equipments);
        
        for (Equipment e : allequipments)
        {
            String result = "";
        for(Equipment e2 :equipments)
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
      
 
        mv.addObject("subsection", sub);
               mv.addObject("allequipments",subset);
             return mv;
         
         } 
}


