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
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class Step {
    
    private String[] id;
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
    private Object getBean(String nombrebean, ServletContext servlet)
    {
        ApplicationContext contexto = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet);
        Object beanobject = contexto.getBean(nombrebean);
        return beanobject;
    }
//         public String fetchName(int id, ServletContext servlet)
//    { String name = null ;
//        try {
//             DriverManagerDataSource dataSource;
//        dataSource = (DriverManagerDataSource)this.getBean("dataSource",servlet);
//        this.cn = dataSource.getConnection();
//             Statement st = this.cn.createStatement();
//             
//            String consulta = "SELECT name FROM public.content where id = "+id;
//            ResultSet rs = st.executeQuery(consulta);
//          
//            while (rs.next())
//            {
//                name = rs.getString("name");
//                
//            }
//            //this.finalize();
//            
//        } catch (SQLException ex) {
//            System.out.println("Error reading methods: " + ex);
//        }
//       
//        return name;
//    
//    }   
   
}
