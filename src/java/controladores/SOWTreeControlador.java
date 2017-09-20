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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;


/**
 *
 * @author nmohamed
 */
@Controller
public class SOWTreeControlador {
     Connection cn;
     static Logger log = Logger.getLogger(SOWTreeControlador.class.getName());
     private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    @RequestMapping("/sowdisplay/start.htm")
    public ModelAndView start(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    ModelAndView mv = new ModelAndView("sowdisplay");
    try{
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
    }catch(SQLException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        log.error(ex+errors.toString());
    }
    return mv;
    }
    @RequestMapping("/sowdisplay/loadtree.htm")
    @ResponseBody
    public String loadtree(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
    ModelAndView mv = new ModelAndView("sowdisplay");
     JSONObject json = new JSONObject();
      ArrayList<DBRecords> steps = new ArrayList<>();
      ArrayList<String> subjects = new ArrayList<>();
      ArrayList<String> objectives = new ArrayList<>();
       Tree tree = new Tree();
       Node<String> rootNode = new Node<String>("Subjects","A"," {\"disabled\":true}");
    try{
        DriverManagerDataSource dataSource;
        dataSource = (DriverManagerDataSource)this.getBean("dataSourceAH",hsr.getServletContext());
        this.cn = dataSource.getConnection();
       
       
        Statement st = this.cn.createStatement();
        String[] levelid = hsr.getParameterValues("seleccion1");
        ArrayList<Subject> subs = this.getSubjects(levelid);
         dataSource = (DriverManagerDataSource)this.getBean("dataSource",hsr.getServletContext());
        this.cn = dataSource.getConnection();
       
       
        st = this.cn.createStatement();
        for(Subject sub:subs){
            String[] sid = sub.getId();
        ResultSet rs = st.executeQuery("select obj_steps.id,obj_steps.name,objective.name as obj ,objective.subject_id from obj_steps inner join objective on obj_steps.obj_id = objective.id where objective.subject_id = '"+sid[0]+"'");
        
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
            steps.add(l); 
          
        }
        }
        for (DBRecords x :steps)
        {
            Subject s = new Subject();
            String id = null;
            id = x.getCol3();
            x.setCol3(s.fetchName(Integer.parseInt(id), hsr.getServletContext()));
            if(!subjects.contains(x.getCol3())){
            subjects.add(x.getCol3());
            }
        }
  
   
    String test = new Gson().toJson(steps);
   
    
    int i = 0;
   int z = 0;
    for(Subject x:subs)//subjects)
    {
         
        Node<String> nodeC = new Node<String>(x.getName(),"L"+i," {\"disabled\":true}");
        rootNode.addChild(nodeC); 
      i++;
      ArrayList<Objective> obj = this.getObjectives(x.getId());
         for(Objective y:obj)
    {
    
    Node<String> nodeA = new Node<String>(y.getName(),"C"+z," {\"disabled\":true}");
             nodeC.addChild(nodeA);
         z++;
     for (DBRecords l:steps){
         
         //match the subject with the objective
         if(l.getCol3().equalsIgnoreCase(x.getName())&&l.getCol4().equalsIgnoreCase(y.getName()))
         {
           
         //match the objective with the step
       for (DBRecords k:steps){
          if(k.getCol4().equalsIgnoreCase(y.getName())){
        Node<String> nodeB = new Node<String>(k.getCol2(),k.getCol1()," {\"disabled\":false}");
      
         nodeA.addChild(nodeB);
          }
       }
       break;
         }
        
        }
    }
        
    }
   
     }catch (SQLException ex) {
            System.out.println("Error: " + ex);
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
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

    public ArrayList<Subject> getSubjects(String[] levelid) throws SQLException
       {
           
        ArrayList<Subject> subjects = new ArrayList<>();
        ArrayList<Subject> activesubjects = new ArrayList<>();
        try{
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
           ResultSet rs2 = st.executeQuery("select Title,Active from Courses where CourseID = "+ids[0]);
           while(rs2.next())
           {
            if(rs2.getBoolean("Active")== true)
               {
                   su.setName(rs2.getString("Title"));
                   activesubjects.add(su);
               }
           }
          }
        }catch(SQLException ex){
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        log.error(ex+errors.toString());
        }
           return activesubjects;
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
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex+errors.toString());
        }
       return objectives;
       }
       

    
}    

