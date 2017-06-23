/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.*;
import Montessori.Level;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.google.gson.*;

import atg.taglib.json.util.JSONObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;


/**
 *
 * @author nmohamed
 */
@Controller
public class LessonIdeaControlador {
     Connection cn;
     private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    @RequestMapping("/lessonidea/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    ModelAndView mv = new ModelAndView("lessonidea");
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
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
    mv.addObject("levels", grades);
    return mv;
    }
    @RequestMapping("/lessonidea/loadtree.htm")
    @ResponseBody
    public String loadtree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    ModelAndView mv = new ModelAndView("lessonidea");
     JSONObject json = new JSONObject();
      ArrayList<DBRecords> lessons = new ArrayList<>();
      ArrayList<String> subjects = new ArrayList<>();
      ArrayList<String> objectives = new ArrayList<>();
    try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
       
       
        Statement st = this.cn.createStatement();
        String[] lessonid = hsr.getParameterValues("seleccion1");
        ResultSet rs = st.executeQuery("SELECT lessons.id,lessons.subject_id,lessons.objective_id,objective.name as obj,lessons.name FROM lessons inner join objective on lessons.objective_id = objective.id where lessons.level_id= "+lessonid[0]+" and lessons.idea = true ");
        while(rs.next())
        {
            DBRecords l = new DBRecords();
            l.setCol1(""+rs.getInt("id"));
            l.setCol2(rs.getString("name"));
            l.setCol4(rs.getString("obj"));
            l.setCol3(""+rs.getInt("subject_id"));
            if(!objectives.contains(rs.getString("obj"))){
            objectives.add(rs.getString("obj"));
            }
         //   l.setCol5(rs.getString("obj"));
//            String[] sid = new String[1];
//            String[] oid = new String[1];
//            Subject s = new Subject();
//            sid[0] = ""+rs.getInt("subject_id");
            //s.setId(sid);
//            l.setSubject(s);
//            Objective o = new Objective();
//            oid[0] = ""+rs.getInt("objective_id");
//            o.setId(oid);
//            o.setName(rs.getString("obj"));
//            l.setObjective(o);
            lessons.add(l); 
          
        }
        for (DBRecords x :lessons)
        {
            Subject s = new Subject();
            String id = null;
            id = x.getCol3();
            x.setCol3(s.fetchName(Integer.parseInt(id), hsr.getServletContext()));
            if(!subjects.contains(x.getCol3())){
            subjects.add(x.getCol3());
            }
        }
  
    }catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        }
    String test = new Gson().toJson(lessons);
    Tree tree = new Tree();
    Node<String> rootNode = new Node<String>("root","A"," {\"disabled\":true}");
    int i = 0;
   int z = 0;
    for(String x:subjects)
    {
         
        Node<String> nodeC = new Node<String>(x,"L"+i," {\"disabled\":true}");
        rootNode.addChild(nodeC); 
      i++;
         for(String y:objectives)
    {
    
     for (DBRecords l:lessons){
         if(l.getCol3().equalsIgnoreCase(x)&&l.getCol4().equalsIgnoreCase(y))
         {
            Node<String> nodeA = new Node<String>(y,"C"+z," {\"disabled\":true}");
             nodeC.addChild(nodeA);
         z++;
       for (DBRecords k:lessons){
          if(k.getCol4().equalsIgnoreCase(y)){
         Node<String> nodeB = new Node<String>(k.getCol2(),k.getCol1()," {\"disabled\":false}"); 
         nodeA.addChild(nodeB);
          }
       }
       break;
         }
        }
    }
        
    }
   
    

        tree.setRootElement(rootNode);
        String test2 = this.generateJSONfromTree(tree);
    return test2;
    }
    public String generateJSONfromTree(Tree tree) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // buffer to write to string later
        JsonGenerator generator = factory.createJsonGenerator(out, JsonEncoding.UTF8);

        ObjectNode rootNode = generateJSON(tree.getRootElement(), mapper.createObjectNode());
        mapper.writeTree(generator, rootNode);

        return out.toString();
    }

    public ObjectNode generateJSON(Node<String> node, ObjectNode obN) throws JSONException {
        if (node == null) {
            return obN;
        }

        obN.put("text", node.getData());
        obN.put("id", node.getId());
        obN.put("state",node.getState());
        JSONObject j = new JSONObject();
//        j.put("opened",true);
//        j.put("disabled",false);
//        obN.put("state",j.toString());
        ArrayNode childN = obN.arrayNode();
        obN.set("children", childN);        
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            return obN;
        }

        Iterator<Node<String>> it = node.getChildren().iterator();
        while (it.hasNext()) {  
            childN.add(generateJSON(it.next(), new ObjectMapper().createObjectNode()));
        }
        return obN;
    }
     @RequestMapping("/lessonidea/editlessonidea.htm")
    public ModelAndView editlessonidea(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
    { 
       ModelAndView mv = new ModelAndView("editlessonidea");
        String lessonid = hsr.getParameter("seleccion1");
        Lessons data = new Lessons();
       Level l = new Level();
       ArrayList<Content> c = new ArrayList<>();
       ArrayList<Students> stud = new ArrayList<>();
        Objective o = new Objective();
        Subject s = new Subject();
        Method m = new Method();
        String[] id = new String[1];
        try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
       ResultSet rs = st.executeQuery("select * from lessons where id= "+lessonid);
         while(rs.next()){
             data.setComments(rs.getString("comments"));
           
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
        id=null;
        id = o.getId();
       o.setName(o.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
        data.setObjective(o);
         id=null;
        id = l.getId();
       l.setName(l.fetchName(Integer.parseInt(id[0]), hsr.getServletContext()));
       data.setLevel(l);
       id=null;
       ResultSet rs2 = st.executeQuery("select * from lesson_content where lesson_id = "+lessonid);
      
       List<String>cid =new ArrayList<>();
       while(rs2.next())
       {
           cid.add(rs2.getString("content_id")); 
          
       }
      
       data.setContentid(cid);
       mv.addObject("data",data);
       ArrayList<Objective> test = this.getObjectives(data.getSubject().getId());
        mv.addObject("objectives",this.getObjectives(data.getSubject().getId()));
                 mv.addObject("contents",this.getContent(data.getObjective().getId()));
                  cn.close();
                  dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
               mv.addObject("subjects",this.getSubjects(data.getLevel().getId()));
         List <Lessons> ideas = new ArrayList();
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
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
        }catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        
       return mv;
    }
      @RequestMapping("/lessonidea/deletetree.htm")
    public ModelAndView deletetree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception
    { 
       ModelAndView mv = new ModelAndView("redirect:/lessonidea/start.htm");
        String lessonid = hsr.getParameter("seleccion1");
        try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
        Statement st = this.cn.createStatement();
       st.executeUpdate("delete from lessons where id='"+lessonid+"'");
        }catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        
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
//          Objective s = new Objective();
//          s.setName("Select Objective");
//          objectives.add(s);
           
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
       
}    

