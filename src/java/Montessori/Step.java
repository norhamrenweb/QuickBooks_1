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

public class Step {

    private ServletContext servlet;
    private String id;
    private String name;
    private int order;
    private int weight;
    private Objective obj;
    Connection cn;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Objective getObj() {
        return obj;
    }

    public void setObj(Objective obj) {
        this.obj = obj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Object getBean(String nombrebean, ServletContext servlet) {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }

    public void compareandupdate(List<Step> newsteps, String[] objid, ServletContext servlet) {
        Connection con = null;
        ResultSet rs = null;
        Statement stAux = null;
        try {
            PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            con = pool_local.getConnection();
            stAux = con.createStatement();
            
            String consulta = "SELECT * FROM obj_steps where obj_id = " + objid[0];
            rs = stAux.executeQuery(consulta);
            List<Step> old = new ArrayList<>();
            while (rs.next()) {
                Step s = new Step();
                s.setId("" + rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setOrder(rs.getInt("storder"));
                old.add(s);
            }
            int found = 0;

            for (Step y : old) {
                for (Step x : newsteps) {
                    if (y.getId().equals(x.getId())) {
                        found = 1;
                        break;
                    }
                }
                if (found == 0) {
                    //delete step
                    stAux.executeUpdate("delete from obj_steps where id =" + y.getId());

                }
            }

            for (Step x : newsteps) {
                String name;
                name = x.getName().replace("'", "\'\'");
                name = name.replace("\"", "\"\"");
                if (x.getId().equals("0")) {
                    //add new step
                    stAux.executeUpdate("insert into obj_steps(name,storder,obj_id) values('" + name + "','" + x.getOrder() + "','" + objid[0] + "')");
                } else {
                    for (Step y : old) {
                        if (y.getId().equals(x.getId())) {
                            //update step
                            stAux.executeUpdate("update obj_steps set name = '" + name + "',storder= '" + x.getOrder() + "' where id = " + x.getId());
                            break;
                        }
                    }

                }

            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error updating steps: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(Step.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Step.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

}
