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
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author nmohamed
 */
public class Level {

    private String[] id;
    private String name;
    Connection cn;
    private ServletContext servlet;

//      private ServletContext servlet;
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
        String levelName = null;
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            con = pool_renweb.getConnection();
            stAux = con.createStatement();
            String consulta = "SELECT GradeLevel FROM GradeLevels where GradeLevelID = " + id;
            rs = stAux.executeQuery(consulta);

            while (rs.next()) {
                levelName = rs.getString("GradeLevel");

            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error leyendo Alumnos: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Level.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 

        return levelName;
    }

}
