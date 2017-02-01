/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Jesús Aragón
 */
public class Equipment {
    private int id_lesson;
    private int id_activity_equipment;
    private int id_subsection;
    private String nombre_activity_equipment;
    private ServletContext servlet;
    private Connection cn;
    private CachedRowSet rs;

    public Equipment(){
        
    }
    
    public Equipment(ServletContext svc) {
        this.id_activity_equipment = 0;
        this.id_subsection = 0;
        this.nombre_activity_equipment = "First choose a section";
        this.servlet = svc;
        this.id_lesson = -1;
    }
    
     public Equipment(String nombre_activity_equipment) {
        this.id_activity_equipment = 0;
        this.id_subsection = 0;
        this.nombre_activity_equipment = "";
    }
     
    public int getId_lesson() {
        return this.id_lesson;
    }

    public void setId_lesson(int id_lesson) {
        this.id_lesson = id_lesson;
    }

    public int getId_activity_equipment() {
        return id_activity_equipment;
    }

    public void setId_activity_equipment(int id_activity_equipment) {
        this.id_activity_equipment = id_activity_equipment;
    }

    public int getId_subsection() {
        return id_subsection;
    }

    public void setId_subsection(int id_subsection) {
        this.id_subsection = id_subsection;
    }

    public String getNombre_activity_equipment() {
        return nombre_activity_equipment;
    }

    public void setNombre_activity_equipment(String nombre_activity_equipment) {
        this.nombre_activity_equipment = nombre_activity_equipment;
    }
    
    private Object getBean(String nombreBean){
        ApplicationContext contexto = 
        WebApplicationContextUtils.getRequiredWebApplicationContext(this.servlet);
        Object bean = contexto.getBean(nombreBean);
        return bean;
    }

    private void conectarOracle() throws SQLException{
        DriverManagerDataSource drv =
                (DriverManagerDataSource)this.getBean("dataSource");
        this.cn = drv.getConnection();
        this.rs = RowSetProvider.newFactory().createCachedRowSet();
    }
     
    public ArrayList<Equipment> getMateriales(int id_lesson) throws SQLException{
        this.conectarOracle();
        ArrayList<Equipment> listaEquipment = new ArrayList<>();
        try {
            this.conectarOracle();
            String consulta = "select * from lessons_equipment, activity_equipment where lessons_equipment.id_lessons = " + id_lesson + " and lessons_equipment.id_equipment =activity_equipment.id_activity_equipment";
            this.rs.setCommand(consulta);
            this.rs.execute(this.cn);
            this.rs.beforeFirst();            
            while (rs.next())
            {
                Equipment equipment = new Equipment(this.servlet);
                equipment.setId_lesson(rs.getInt("id_lessons"));
                equipment.setNombre_activity_equipment(rs.getString("nombre_activity_equipment"));
                equipment.setId_activity_equipment(rs.getInt("id_activity_equipment"));
                equipment.setId_subsection(rs.getInt("id_subsection"));
                listaEquipment.add(equipment);
            }
        } catch (SQLException ex) {
            System.out.println("Error leyendo Lessons: " + ex);
        } 
        this.rs.close();
        this.cn.close();
        return listaEquipment;      
    }
    public ArrayList<Equipment> getMaterialsFromSubsection(int idSubsec) throws SQLException{
        this.conectarOracle();
        ArrayList<Equipment> listaequipment = new ArrayList<Equipment>();
        try {
            String consulta = "SELECT * FROM activity_equipment where id_subsection =" + idSubsec;
            this.rs.setCommand(consulta);
            this.rs.execute(this.cn);
            this.rs.beforeFirst();
            while (rs.next())
            {
                Equipment equipments = new Equipment();
                equipments.setId_activity_equipment(rs.getInt("id_activity_equipment"));
                equipments.setId_subsection(rs.getInt("id_subsection"));
                equipments.setNombre_activity_equipment(rs.getString("nombre_activity_equipment"));
                listaequipment.add(equipments);
            }

        } catch (SQLException ex) {
            System.out.println("Error leyendo equipment: " + ex);
        }
        this.rs.close();
        this.cn.close();
        return listaequipment;
    }
    
    public int insertEquipment (int idLesson, String[] idEquipment) throws SQLException{
        this.conectarOracle();
        int materialesAsignados = 0;
        if(idEquipment != null && idEquipment[0].compareTo("0") != 0){
            for (String idEquipment1 : idEquipment) {
                String insertMaterialLessons = "insert into lessons_equipment (id_lessons, id_equipment) values (?,?)";
                PreparedStatement pst1 = this.cn.prepareStatement(insertMaterialLessons);
                pst1.setInt(1, idLesson);
                pst1.setInt(2, Integer.parseInt(idEquipment1));
                pst1.executeUpdate();
            }
            
            materialesAsignados = idEquipment.length;
        }
        this.rs.close();
        this.cn.close();
        return materialesAsignados;
    }
    
    public void updateEquipment (int idLesson, String[] idEquipment) throws SQLException{
        this.conectarOracle();
        ArrayList<Integer> idLessonEquip = new ArrayList<>(); 
        String consulta = "select id_lessons_equipment from lessons_equipment where id_lessons= " + idLesson;
        this.rs.setCommand(consulta);
        this.rs.execute(this.cn);
        this.rs.beforeFirst();
        while (rs.next()){
            idLessonEquip.add(rs.getInt("id_lessons_equipment"));
        }
        
        if(idEquipment != null && !idLessonEquip.isEmpty()){
            //for(int i=0; i<idEquipment.length;i++){
                this.deleteEquipment(idLesson);
                
                this.insertEquipment(idLesson, idEquipment);
            //}
        }else if(idLessonEquip.isEmpty()){
            this.insertEquipment(idLesson, idEquipment);
        }else{
            this.deleteEquipment(idLesson);
        }
        this.rs.close();
        this.cn.close();
    }
    
    public void deleteEquipment(int idLesson) throws SQLException{
        this.conectarOracle();
        String consulta = "delete from lessons_equipment where id_lessons=" + idLesson;
        this.rs.setCommand(consulta);
        this.rs.execute(this.cn);
        this.rs.beforeFirst();
        this.rs.close();
        this.cn.close();
    }
}
