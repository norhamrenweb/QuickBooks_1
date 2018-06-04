/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author nmohamed
 */
public class Objective {
    private ServletContext servlet;
    private String description;
    private String finalrating;
    private int progress;
    private String[] id;
    private String name;
    private List<Step> steps;
    private int nooflessonsplanned;
    private int nooflessonsdone;
    private boolean reportCard;
    Connection cn;

    public boolean isReportCard() {
        return reportCard;
    }

    public void setReportCard(boolean reportCard) {
        this.reportCard = reportCard;
    }

    
    public String getFinalrating() {
        return finalrating;
    }

    public void setFinalrating(String finalrating) {
        this.finalrating = finalrating;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getNooflessonsplanned() {
        return nooflessonsplanned;
    }

    public void setNooflessonsplanned(int nooflessonsplanned) {
        this.nooflessonsplanned = nooflessonsplanned;
    }

    public int getNooflessonsdone() {
        return nooflessonsdone;
    }

    public void setNooflessonsdone(int nooflessonsdone) {
        this.nooflessonsdone = nooflessonsdone;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

      
//      private ServletContext servlet;
    
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
    public String[] getId() {
        return id;
    }

    public void setId(String[] id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 public String fetchName(int id, ServletContext servlet)
    { String subName = null ;
        try {
             
            String consulta = "SELECT name FROM public.objective where id = "+id;
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
          
            while (rs.next())
            {
                subName = rs.getString("name");
                
            }
            //this.finalize();
            
        } catch (SQLException ex) {
            System.out.println("Error reading objectives: " + ex);
        }
       
        return subName;
    
    }   
}
