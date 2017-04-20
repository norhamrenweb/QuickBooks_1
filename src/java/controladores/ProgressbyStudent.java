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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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
@Scope("session")
public class ProgressbyStudent {
     Connection cn;
      
      private ServletContext servlet;
    
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
          Subject first = new Subject();
          first.setName("Select Subject");
          subjects.add(first);
           while (rs1.next())
            {
             Subject sub = new Subject();
             String[] ids = new String[1];
            ids[0]=""+rs1.getInt("CourseID");
             sub.setId(ids);
                subjects.add(sub);
            }
           //loop through subjects to get their names, skipping the first 
          for(Subject s:subjects.subList(1,subjects.size()))
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
//    @RequestMapping("/progressbystudent/objectivelistSubject.htm")
//    public ModelAndView objectivelistSubject(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
//        
//        ModelAndView mv = new ModelAndView("studentpage");
//        List<Objective> objectives = new ArrayList<>();
//       try {
//         DriverManagerDataSource dataSource;
//        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
//        this.cn = dataSource.getConnection();
//        
//        
//            
//             Statement st = this.cn.createStatement();
//             String subjectid = null;
//          
//            
//                subjectid = hsr.getParameter("seleccion2");
//            
//            
//          ResultSet rs1 = st.executeQuery("select name,id from public.objective where subject_id="+subjectid);
//         
//           
//           while (rs1.next())
//            {
//             String[] ids = new String[1];
//                Objective sub = new Objective();
//            ids[0] = ""+rs1.getInt("id");
//             sub.setId(ids);
//             sub.setName(rs1.getString("name"));
//                objectives.add(sub);
//            }
//          
//            
//        } catch (SQLException ex) {
//            System.out.println("Error leyendo Objectives: " + ex);
//        }
//        
//   
//        mv.addObject("objectives", objectives);
//        
//        
//        return mv;
//    }
    
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
                alumnos.setNextlevel(rs.getString("Placement"));
                alumnos.setSubstatus(rs.getString("Substatus"));
                listaAlumnos.add(alumnos);
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
       
        return listaAlumnos;
         
         
    }
    //OTEHER PAGINE
    @RequestMapping("/progressbystudent/newpage.htm")
    @ResponseBody
    public ModelAndView newpage(HttpServletRequest hsr, HttpServletResponse hsr1, Model model) throws Exception
    {
         ModelAndView mv = new ModelAndView("progressdetails");
            Objective o = new Objective();
                 String[] hi = hsr.getParameterValues("data");
                 servlet = hsr.getServletContext();
               JSONObject jsonObj = new JSONObject(hi[0]);
            List<Progress> progress = new ArrayList<>();
            String finalrating = null;
            String presenteddate = null;
            String attempteddate = null;
            String mastereddate = null;
            List<String> attemptdates = new ArrayList<>();
       try {
         DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        
             Statement st = this.cn.createStatement(1004,1007);
            
          ResultSet rs1 = st.executeQuery("select comment,comment_date,ratingname,lessonname from public.progresslessonname where objective_id="+jsonObj.getString("objectiveid")+" AND student_id = "+jsonObj.getString("studentid"));
          if(!rs1.next())
          { String message = "Student does not have lessons under the selected objective";
              mv.addObject("message",message);
          }
          else{
              rs1.beforeFirst();
           while (rs1.next())
            {
          Progress p = new Progress();
          p.setComment(rs1.getString("comment"));
          p.setRating(rs1.getString("ratingname"));
          p.setLesson_name(rs1.getString("lessonname"));
           Timestamp stamp = rs1.getTimestamp("comment_date");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               String dateStr = sdfDate.format(stamp);
             p.setComment_date(dateStr);
             progress.add(p);
            }
          // store an array of the attempted dates
           for(Progress x:progress)
           {
               if(x.getRating().equals("Attempted"))
               {
                   attemptdates.add(x.getComment_date());
               }
           }
 //           select the latest rating to be presented as the final rating for this objective
        String consulta = "SELECT rating.name FROM rating where id in(select rating_id from progress_report where student_id = '"+jsonObj.getString("studentid")+"' AND comment_date = (select max(comment_date)   from public.progress_report where student_id ="+jsonObj.getString("studentid")+" AND objective_id ="+jsonObj.getString("objectiveid")+" and generalcomment = false) AND objective_id ="+jsonObj.getString("objectiveid")+"and generalcomment = false )";
ResultSet rs2 = st.executeQuery(consulta);
while(rs2.next())
{
    finalrating= rs2.getString("name");
}
          consulta = "select min(comment_date) as date from progress_report where student_id ="+jsonObj.getString("studentid")+" and rating_id in (select id from rating where name = 'Presented') and objective_id ="+jsonObj.getString("objectiveid");  
          ResultSet rs3 = st.executeQuery(consulta);
          if(rs3.next()){
              rs3.beforeFirst();
while(rs3.next())
{
 Timestamp stamp = rs3.getTimestamp("date");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                if(stamp!=null){
               presenteddate = sdfDate.format(stamp);
                }
    
}
          }
consulta = "select min(comment_date) as date from progress_report where student_id ="+jsonObj.getString("studentid")+" and rating_id in (select id from rating where name = 'Attempted') and objective_id ="+jsonObj.getString("objectiveid");  
          ResultSet rs4 = st.executeQuery(consulta);
           if(rs4.next()){
              rs4.beforeFirst();
while(rs4.next())
{
    Timestamp stamp = rs4.getTimestamp("date");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
               if(stamp!=null){
               attempteddate = sdfDate.format(stamp);
               }
}
           }
consulta = "select min(comment_date) as date from progress_report where student_id ="+jsonObj.getString("studentid")+" and rating_id in (select id from rating where name = 'Mastered') and objective_id ="+jsonObj.getString("objectiveid");  
          ResultSet rs5 = st.executeQuery(consulta);
            if(rs5.next()){
              rs5.beforeFirst();
while(rs5.next())
{
    Timestamp stamp = rs5.getTimestamp("date");
               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                if(stamp!=null){
               mastereddate = sdfDate.format(stamp);
                }
  
}
            }
        String prog = new Gson().toJson(progress);
        String rating = new Gson().toJson(finalrating);
        JSONObject obj = new JSONObject();
//        obj.put("progress", prog);
//        obj.put("finalrating", rating);
//        obj.put("attempteddate",attempteddate);
//        obj.put("mastereddate",mastereddate);
//        obj.put("presenteddate",presenteddate);
         //return obj.toString();
        // model.addAttribute("holas");
        mv.addObject("progress", progress);
      mv.addObject("finalrating", finalrating);
       mv.addObject("attempteddate",attempteddate);
       mv.addObject("mastereddate",mastereddate);
      mv.addObject("presenteddate",presenteddate);
        mv.addObject("studentname", jsonObj.getString("studentname"));
        mv.addObject("gradelevel",jsonObj.getString("gradelevel"));
        mv.addObject("subject",jsonObj.getString("subject"));
        mv.addObject("attempteddates", attemptdates);
        mv.addObject("objective", o.fetchName(Integer.parseInt(jsonObj.getString("objectiveid")), servlet));}
//        mv.addObject(obj);
} catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return mv;
    }
    //based on student selected and objective selected
//    @RequestMapping("/progressbystudent/progressdetails.htm")
//    @ResponseBody
//    public String progressdetails(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
//    {
//             String[] hi = hsr.getParameterValues("data");
//               JSONObject jsonObj = new JSONObject(hi[0]);
//            List<Progress> progress = new ArrayList<>();
//            String finalrating = null;
//            String presenteddate = null;
//            String attempteddate = null;
//            String mastereddate = null;
//       try {
//         DriverManagerDataSource dataSource;
//        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
//        this.cn = dataSource.getConnection();
//        
//             Statement st = this.cn.createStatement();
//            
//          ResultSet rs1 = st.executeQuery("select comment,comment_date,ratingname,lessonname from public.progresslessonname where objective_id="+jsonObj.getString("objectiveid")+" AND student_id = "+jsonObj.getString("studentid")+" AND COALESCE(generalcomment, FALSE) = FALSE ");
//          
//           while (rs1.next())
//            {
//          Progress p = new Progress();
//          p.setComment(rs1.getString("comment"));
//          p.setRating(rs1.getString("rating"));
//          p.setLesson_name(rs1.getString("lessonname"));
//           Timestamp stamp = rs1.getTimestamp("comment_date");
//               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//               String dateStr = sdfDate.format(stamp);
//             p.setComment_date(dateStr);
//             progress.add(p);
//            }
//           // select the latest rating to be presented as the final rating for this objective
//        String consulta = "SELECT rating.name FROM rating where id in(select rating_id from progress_report where student_id = '"+jsonObj.getString("studentid")+"' AND comment_date = (select max(comment_date)   from public.progress_report where student_id ="+jsonObj.getString("studentid")+"AND objective_id ="+jsonObj.getString("objectiveid")+") AND objective_id ="+jsonObj.getString("objectiveid")+")";
//ResultSet rs2 = st.executeQuery(consulta);
//while(rs2.next())
//{
//    finalrating= rs2.getString("rating");
//}
//          consulta = "select min(comment_date) as date from progress_report where student_id ="+jsonObj.getString("studentid")+" and rating_id in (select id from rating where name = 'Presented') and objective_id ="+jsonObj.getString("objectiveid");  
//          ResultSet rs3 = st.executeQuery(consulta);
//while(rs3.next())
//{
// Timestamp stamp = rs3.getTimestamp("date");
//               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//               presenteddate = sdfDate.format(stamp);
//    
//}
//consulta = "select min(comment_date) as date from progress_report where student_id ="+jsonObj.getString("studentid")+" and rating_id in (select id from rating where name = 'Attempted') and objective_id ="+jsonObj.getString("objectiveid");  
//          ResultSet rs4 = st.executeQuery(consulta);
//while(rs4.next())
//{
//    Timestamp stamp = rs4.getTimestamp("date");
//               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//               attempteddate = sdfDate.format(stamp);
//  
//}
//consulta = "select min(comment_date) as date from progress_report where student_id ="+jsonObj.getString("studentid")+" and rating_id in (select id from rating where name = 'Mastered') and objective_id ="+jsonObj.getString("objectiveid");  
//          ResultSet rs5 = st.executeQuery(consulta);
//while(rs5.next())
//{
//    Timestamp stamp = rs5.getTimestamp("date");
//               SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//               mastereddate = sdfDate.format(stamp);
//  
//}
//        } catch (SQLException ex) {
//            System.out.println("Error: " + ex);
//        }
//
//        String prog = new Gson().toJson(progress);
//        String rating = new Gson().toJson(finalrating);
//        JSONObject obj = new JSONObject();
//        obj.put("progress", prog);
//        obj.put("finalrating", rating);
//        obj.put("attempteddate",attempteddate);
//        obj.put("mastereddate",mastereddate);
//        obj.put("presenteddate",presenteddate);
//         return obj.toString();
//         
//    }
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
                student.setFoto(rs.getString("PathToPicture"));
                student.setLevel_id(rs.getString("GradeLevel"));
                student.setNextlevel(rs.getString("NextGradeLevel"));
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
        String selection = hsr.getParameter("selection");
        String[] data = selection.split(",");
        String subjectid = data[0];
        String studentid = data[1];
//        List<Progress> progress = new ArrayList<>();
//        List<Objective> objectives = new ArrayList<>();
//        List<String> objname = new ArrayList<>();
//        List<String> objdscp = new ArrayList<>();
//        List<String> comment = new ArrayList<>();
//        List<Date> commentdate = new ArrayList<>();
//        List<Integer> objid = new ArrayList<>();
        List<DBRecords> result = new ArrayList<>();
       try {
            DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();
             
//            String consulta = "SELECT * FROM objective where subject_id = "+subjectid;
//            ResultSet rs = st.executeQuery(consulta);
//          String consulta = " SELECT objective.id,objective.name,objective.description,progress_report.comment,progress_report.comment_date FROM progress_report  INNER JOIN objective ON progress_report.objective_id = objective.id where generalcomment = TRUE AND student_id = "+studentid+" AND subject_id = "+subjectid;
//          ResultSet rs = st.executeQuery(consulta);
//          int i = 0;
//           while (rs.next())
//            {
//                DBRecords r = new DBRecords();
//                r.setCol1(rs.getString("name"));
//                r.setCol2(rs.getString("description"));
//                r.setCol3(rs.getString("comment"));
//                r.setCol4(""+rs.getDate("comment_date"));
//                r.setCol5(""+rs.getInt("id"));
//                result.add(r);
//                objname.add(rs.getString("name"));
//                objdscp.add(rs.getString("description"));
//                comment.add(rs.getString("comment"));
//                commentdate.add(rs.getDate("comment_date"));
//                objid.add(rs.getInt("id"));
             String consulta = " SELECT id,name,description from objective where subject_id = "+subjectid;
        ResultSet rs = st.executeQuery(consulta);
            while (rs.next())
           {
                DBRecords r = new DBRecords();
               r.setCol1(rs.getString("name"));
               r.setCol2(rs.getString("description"));
                 r.setCol5(""+rs.getInt("id"));
                result.add(r);
            }
        for(DBRecords r:result)
        {
            
            consulta = "SELECT * FROM progress_report where objective_id ="+r.getCol5()+"AND generalcomment = TRUE AND student_id ="+studentid;
            ResultSet rs1 = st.executeQuery(consulta);
            while(rs1.next())
            {
            r.setCol3(rs1.getString("comment"));
            r.setCol4(""+rs1.getDate("comment_date"));
         
            }

         }
            
          } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }  
       
//      String jname = new Gson().toJson(objname);
//       String jdscp = new Gson().toJson(objdscp);
//       String jcomm = new Gson().toJson(comment);
//       String jcommd = new Gson().toJson(commentdate);
//       JSONObject json = new JSONObject();
//      json.put("objname",objname);
//      json.put("dscp",objdscp);
//      json.put("comment",comment);
//      json.put("commentdate",commentdate);
        String off = new Gson().toJson(result);
       return off;  
 //           return pjson;
       } 
    
    @RequestMapping("/progressbystudent/saveGeneralcomment.htm")
    @ResponseBody
    public String saveGeneralcomment(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception 
    {
    String message = "Comment was not saved";
          JSONObject obj = new JSONObject();
    String[] hi = hsr.getParameterValues("data");
    JSONObject jsonObj = new JSONObject(hi[0]);
    String objectiveid = jsonObj.getString("objectiveid");
    String comment = jsonObj.getString("comment");
    String studentid = jsonObj.getString("studentid");
    try {
            DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
             Statement st = this.cn.createStatement();
             String consulta = "select id from progress_report where objective_id = "+objectiveid+" and generalcomment = TRUE";
             ResultSet rs = st.executeQuery(consulta);
             if(!rs.next()){
                st.executeUpdate("insert into progress_report(comment_date,comment,student_id,objective_id,generalcomment) values (now(),'"+comment+"','"+studentid+"','"+objectiveid+"',true)");
                message = "Comment successfully updated";
                
              }
              else{
                st.executeUpdate("update progress_report set comment_date = now(),comment = '"+comment+"' where objective_id = "+objectiveid+" AND student_id = '"+studentid+"' and generalcomment = true");
                message = "Comment successfully updated";
              }
             obj.put("message",message);
             obj.put("comment",comment);
             obj.put("objectiveid",objectiveid);
    }
    catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }  
       
    return obj.toString();
    }
}
