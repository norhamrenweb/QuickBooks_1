/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author nmohamed
 */
public class Subject {

    private String[] id;
    private String name;
    Connection cn;
    private ServletContext servlet;
    private List<String> objectives;

    public Subject(){
        this.objectives = new ArrayList<>();
    }
    public Subject(Subject s){
        this.id = s.getId();
        this.name = s.getName();
        this.objectives = s.getObjectives();
    }
    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }

    private Object getBean(String nombrebean, ServletContext servlet) {
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

    public String fetchName(int id, ServletContext servlet) {
        String subjectName = null;
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            String consulta = "SELECT Title FROM Courses where CourseID = " + id;
            rs = stAux.executeQuery(consulta);

            while (rs.next()) {
                subjectName = rs.getString("Title");

            }
            con.close();
            //this.finalize();

        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        } catch (IOException ex) { 
            Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
        }

        return subjectName;

    }

    public ArrayList<String> fetchNameAndElective(int id, ServletContext servlet) {
        ArrayList<String> subjectName = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            String consulta = "SELECT Title,Elective FROM Courses where CourseID = " + id;
            rs = stAux.executeQuery(consulta);

            while (rs.next()) {
                subjectName.add(rs.getString("Title"));
                subjectName.add("" + rs.getBoolean("Elective"));
            }
            //this.finalize();
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        } catch (IOException ex) { 
            Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
        }

        return subjectName;
    }
}
