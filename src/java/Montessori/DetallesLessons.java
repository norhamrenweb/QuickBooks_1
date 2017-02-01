/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author MacAragon
 */
public class DetallesLessons {
    
    private int id_lessons;
    private String nombre_lessons;
    private int id_level;
    private String nombre;
    private int id_subject;
    private String nombre_subject;
    private int id_subsection;
    private String nombre_sub_section;
    private String id_equipment;
    private String fecha_inicio;
    private String fecha_fin;
    private String nombre_archivo;
    private int id_students;
    private String nombre_students;

    public DetallesLessons(int id_lessons, String nombre_lessons, int id_level, String nombre, int id_subject, String nombre_subject, int id_subsection, String nombre_sub_section, String id_equipment, String fecha_inicio, String fecha_fin, String nombre_archivo, int id_students , String nombre_students) {
        this.id_lessons = id_lessons;
        this.nombre_lessons = nombre_lessons;
        this.id_level = id_level;
        this.nombre = nombre;
        this.id_subject = id_subject;
        this.nombre_subject = nombre_subject;
        this.id_subsection = id_subsection;
        this.nombre_sub_section = nombre_sub_section;
        this.id_equipment = id_equipment;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.nombre_archivo = nombre_archivo;
        this.id_students = id_students;
        this.nombre_students = nombre_students;
        
    }
    
    public DetallesLessons() {
        this.id_lessons = 0;
        this.nombre_lessons = "";
        this.id_level = 0;
        this.nombre = "";
        this.id_subject = 0;
        this.nombre_subject = "";
        this.id_subsection = 0;
        this.nombre_sub_section = "";
        this.id_equipment = "";
        this.fecha_inicio = "";
        this.fecha_fin = "";
        this.nombre_archivo = "";
        this.id_students = 0;
        this.nombre_students = "";
    }
    
    public DetallesLessons(String nombre_lessons, int id_level, String nombre, int id_subject, String nombre_subject, int id_subsection, String nombre_sub_section, String id_equipment, String fecha_inicio, String fecha_fin, String nombre_archivo, int id_students , String nombre_students) {
        
        this.nombre_lessons = nombre_lessons;
        this.id_level = id_level;
        this.nombre = nombre;
        this.id_subject = id_subject;
        this.nombre_subject = nombre_subject;
        this.id_subsection = id_subsection;
        this.nombre_sub_section = nombre_sub_section;
        this.id_equipment = id_equipment;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.nombre_archivo = nombre_archivo;
        this.id_students = id_students;
        this.nombre_students = nombre_students;
    }
    
    public int getId_lessons() {
        return id_lessons;
    }

    public void setId_lessons(int id_lessons) {
        this.id_lessons = id_lessons;
    }

    public String getNombre_lessons() {
        return nombre_lessons;
    }

    public void setNombre_lessons(String nombre_lessons) {
        this.nombre_lessons = nombre_lessons;
    }

    public int getId_level() {
        return id_level;
    }

    public void setId_level(int id_level) {
        this.id_level = id_level;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_subject() {
        return id_subject;
    }

    public void setId_subject(int id_subject) {
        this.id_subject = id_subject;
    }

    public String getNombre_subject() {
        return nombre_subject;
    }

    public void setNombre_subject(String nombre_subject) {
        this.nombre_subject = nombre_subject;
    }

    public int getId_subsection() {
        return id_subsection;
    }

    public void setId_subsection(int id_subsection) {
        this.id_subsection = id_subsection;
    }

    public String getNombre_sub_section() {
        return nombre_sub_section;
    }

    public void setNombre_sub_section(String nombre_subsection) {
        this.nombre_sub_section = nombre_subsection;
    }

    public String getId_equipment() {
        return id_equipment;
    }

    public void setId_equipment(String id_equipment) {
        this.id_equipment = id_equipment;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getNombre_archivo() {
        return nombre_archivo;
    }

    public void setNombre_archivo(String nombre_archivo) {
        this.nombre_archivo = nombre_archivo;
    }
    
    public int getId_students() {
        return id_students;
    }

    public void setId_students(int id_students) {
        this.id_students = id_students;
    }

    public String getNombre_students() {
        return nombre_students;
    }

    public void setNombre_students(String nombre_students) {
        this.nombre_students = nombre_students;
    }
    
    
    
}
