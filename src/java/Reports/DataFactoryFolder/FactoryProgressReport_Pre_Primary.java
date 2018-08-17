/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

import Montessori.DBConect;
import Montessori.Objective;
import Montessori.Subject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nmoha
 */
public class FactoryProgressReport_Pre_Primary extends DataFactory {

    public FactoryProgressReport_Pre_Primary(String cTerm,String cYear) {
        nameStudent = "";
        dob = "";
        age = "";
        grade = "";
        term = "";
         this.currentTerm = cTerm;
        this.currentYear = cYear;
    }

    public Collection getDataSource(HttpServletRequest hsr, String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException {

        String studentId = idStudent;
        String consulta = "";
        ResultSet rs;
        cargarAlumno(studentId); // tarda 1

               String yearId = this.currentYear;
        String termId = this.currentTerm;

        java.util.Vector coll = new java.util.Vector();
        ArrayList<Subject> subjects = new ArrayList<>();

        try {
            consulta = "select Courses.CourseID from Roster inner join Classes"
                    + "  on Roster.ClassID = Classes.ClassID"
                    + "  inner join Courses on  Classes.CourseID = Courses.CourseID"
                    + "  where Classes.Term" + termId + "=1 and Roster.StudentID = " + idStudent + " and Classes.yearid = " + yearId + " and Courses.ReportCard = 1 and (department='Report Codes 1' or department='Report Codes 1, 2' )order by courses.RCPlacement";

            rs = DBConect.ah.executeQuery(consulta);
            while (rs.next()) {
                Subject su = new Subject();
                String[] id = new String[1];
                id[0] = "" + rs.getInt(1);
                su.setId(id);
                subjects.add(su);
            }

            /*consulta = "SELECT distinct subject_id FROM objective where year_id = " + yearId + " and reportcard=true";
            rs = DBConect.eduweb.executeQuery(consulta);
            while (rs.next()) {
                Subject su = new Subject();
                String[] id = new String[1];
                id[0] = "" + rs.getInt("subject_id");
                su.setId(id);
                subjects.add(su);
            }*/
            if (subjects.isEmpty()) {
                BeanWithList bean = new BeanWithList("", new ArrayList<String>(), new ArrayList<String>(), nameStudent, dob, age);
                coll.add(bean);
            }
            for (Subject x : subjects) {
                String[] id = x.getId();
                ArrayList<String> os = new ArrayList<>();
                ResultSet rs1 = DBConect.eduweb.executeQuery("select * from objective where subject_id = " + id[0]);
                while (rs1.next()) {
                    if (!os.contains("" + rs1.getInt("id"))) {
                        os.add("" + rs1.getInt("id"));
                    }
                }
                int counter = 0;
                ArrayList<String> finalratings = new ArrayList<>();
                ArrayList<Integer> indEliminarObjectives = new ArrayList<>();
                for (String d : os) {
                    Objective b = new Objective();
                    String name = b.fetchName(Integer.parseInt(d), servlet);
                    consulta = "SELECT distinct rating.name,progress_report.term_id "
                            + " FROM objective inner join progress_report on (objective.id = progress_report.objective_id) inner join rating on (progress_report.rating_id = rating.id)"
                            + " where year_id = " + yearId + " and reportcard=true and student_id=" + studentId + " and "
                            + " objective.id =" + d + " and progress_report.term_id=" + termId;
                    ResultSet rs2 = DBConect.eduweb.executeQuery(consulta);
                    if (!rs2.next()) {
                        indEliminarObjectives.add(counter);
                    } else {
                        ResultSet rs3 = DBConect.eduweb.executeQuery(consulta);
                        if (rs3.next()) {
                            finalratings.add(rs3.getString("name"));
                        }
                    }

                    os.set(counter, name);
                    counter = counter + 1;
                }
                x.setObjectives(os);
                Subject t = new Subject();

                for (int i = indEliminarObjectives.size() - 1; i >= 0; i--) {
                    int k = indEliminarObjectives.get(i);
                    os.remove(k);
                }
                //por semestre
                consulta = "SELECT comment,term_id FROM report_comments where supercomment=false and subject_id=" + id[0] + " and studentid=" + idStudent + "and yearterm_id=" + yearId + " and term_id=" + termId + " ORDER BY date_created DESC";
                ResultSet rs4 = DBConect.eduweb.executeQuery(consulta);

                while (rs4.next()) {
                    os.add(rs4.getString("comment"));
                }
                if (os.size() > 0) {
                    ArrayList<String> subjectName = x.fetchNameAndElective(Integer.parseInt(id[0]), servlet);
                    if (subjectName.get(1).equals("false") && existInExcludeList(subjectName.get(0))) { // compruebo que no sea electivo
                        BeanWithList bean = new BeanWithList(subjectName.get(0), os, finalratings, nameStudent, dob, age);
                        coll.add(bean);
                    }
                }
                
                
               
            }
            

        } catch (Exception e) {
            System.err.println("");
        }
        //  rs = DBConect.eduweb.executeQuery("SELECT lesson_id from lesson_stud_att where student_id = '" + studentId + "' and attendance != 'null' and attendance !=' '");
        /*   rs = DBConect.eduweb.executeQuery("SELECT * from lesson_stud_att inner join lessons on lessons.id = lesson_stud_att.lesson_id where student_id = '" + studentId + "' and attendance != 'null' and attendance !=' ' "
                + "and term_id = "+termId+" and yearterm_id = "+yearId);
        while (rs.next()) {
            lessons.add("" + rs.getInt("lesson_id"));
        }
        //select the subjects of these lessons
        ArrayList<String> subids = new ArrayList<>();
        for (String b : lessons) {
            ResultSet rs2 = DBConect.eduweb.executeQuery("select subject_id from lessons where id =" + b);

            while (rs2.next()) {
                // this for avoidind duplicate subjects
                if (!subids.contains("" + rs2.getInt("subject_id"))) {
                    subids.add("" + rs2.getInt("subject_id"));
                    Subject su = new Subject();
                    String[] id = new String[1];
                    id[0] = "" + rs2.getInt("subject_id");
                    su.setId(id);
                    subjects.add(su);
                }
            }
        }

        for (Subject x : subjects) {
            String[] id = x.getId();
            ArrayList<String> os = new ArrayList<>();
            for (String b : lessons) {
           //     select * from lessons inner join objective on lessons.objective_id = objective.id where lessons.id = 129 and lessons.subject_id = 383 and objective.name like 'R%'
                ResultSet rs1 = DBConect.eduweb.executeQuery("select * from lessons inner join objective on lessons.objective_id = objective.id "
                        + "where lessons.id = " + b + " and lessons.subject_id =" + id[0] + "and (objective.name like 'R_%' or objective.name like 'r_%')");
                while (rs1.next()) {
                    if (!os.contains("" + rs1.getInt("objective_id"))) {
                        os.add("" + rs1.getInt("objective_id"));
                    }
                }
            }
            int counter = 0;
            ArrayList<String> finalratings = new ArrayList<>();
            ArrayList<Integer> indEliminarObjectives = new ArrayList<Integer>();
            for (String d : os) {
                Objective b = new Objective();
                String name = b.fetchName(Integer.parseInt(d), servlet);
                consulta = "SELECT rating.name FROM rating where id in"
                        + "(select rating_id from progress_report where student_id = '" + studentId + "'"
                        + " AND comment_date = (select max(comment_date)   from public.progress_report "
                        + "where student_id = '" + studentId + "' AND objective_id = '" + d + "' "
                        + "and generalcomment = false and rating_id not in(6,7)) "
                        + "AND objective_id ='" + d + "'and generalcomment = false )";
                ResultSet rs2 = DBConect.eduweb.executeQuery(consulta);
                if (!rs2.next()) {
                    indEliminarObjectives.add(counter);
                } else {
                    rs2 = DBConect.eduweb.executeQuery(consulta);
                    while (rs2.next()) {
                        String var = rs2.getString("name");

                        var = rs2.getString("name");
                        //  finalratings.add("Mastered");//rs2.getString("name"));
                        finalratings.add(rs2.getString("name"));
                    }
                }
                os.set(counter, name);
                counter = counter + 1;
            }
            x.setObjectives(os);
            Subject t = new Subject();

            for (int i = indEliminarObjectives.size() - 1; i >= 0; i--) {
                int k = indEliminarObjectives.get(i);
                os.remove(k);
            }

            //obtener comentario del subject
            //  boolean existeComentario = false;
            consulta = "SELECT comment FROM report_comments where subject_id=" + id[0] + " and studentid=" + idStudent + " ORDER BY date_created DESC";
            ResultSet rs3 = DBConect.eduweb.executeQuery(consulta);
            if (rs3.next()) {
                //     existeComentario = true;
                os.add(rs3.getString("comment"));
            }
            //=============================
            //os.add("comentario sobre este subject");
            //FUNCIONA PERO LO HACE INNECESARIAMENTE 3 VECES NO SE PORQUE CUANDO LO HAGO FUERA FALLA.

            //============================================================
            if (os.size() > 0) {
                ArrayList<String> subjectName = x.fetchNameAndElective(Integer.parseInt(id[0]), servlet);
                if (subjectName.get(1).equals("false")) { // compruebo que no sea electivo
                    BeanWithList bean = new BeanWithList(subjectName.get(0), os, finalratings, nameStudent, dob, age);
                    coll.add(bean);
                }
            }
        }*/
        // FALTA TERMINAR PODRIAMOS TOMAR LOS SUBJECT_ID DE LOS ALUMNOS QUE SEAN ELECTIVOS Y COMPROBAR QUE NO EXISTAN EN LA UQERY SIGUIENTE
        // tarda 2
/*
        HashMap<Integer, String> idCourseDepartment = new HashMap<>();
        consulta = "SELECT CourseID,Department FROM Courses";
        rs = DBConect.ah.executeQuery(consulta);
        while (rs.next()) {
            idCourseDepartment.put(rs.getInt("CourseID"), rs.getString("Department"));
        }*/

        HashMap<Integer, String> idCourseDepartment = new HashMap<>();
        consulta = "SELECT CourseID,Title FROM Courses";
        rs = DBConect.ah.executeQuery(consulta);
        while (rs.next()) {
            idCourseDepartment.put(rs.getInt("CourseID"), rs.getString("Title"));
        }

        ArrayList<String> os = new ArrayList<>();
        ArrayList<String> as = new ArrayList<>();
        HashMap<Integer, String> commentsPerSubject = new HashMap<>();
        Subject s = new Subject();
        //consulta = "SELECT lessons.subject_id,progress_report.comment FROM progress_report join lessons on (progress_report.lesson_id = lessons.id) where student_id= '" + studentId + "'";
        consulta = "SELECT DISTINCT comment,subject_id FROM report_comments where term_id = " + termId + " and yearterm_id=" + yearId + " and studentid=" + studentId + " and supercomment = false";
        ResultSet rs2 = DBConect.eduweb.executeQuery(consulta);

        while (rs2.next()) {
            commentsPerSubject.put(rs2.getInt("subject_id"), rs2.getString("comment"));
        }

        if (!commentsPerSubject.isEmpty()) {
            Iterator it = commentsPerSubject.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it.next();
                //  ESTO HABRA QUE MODIFICARLO CUANDO SE ACTUALIC LA BBDD DE RENWEB
                if (idCourseDepartment.get((Integer) pair.getKey()).contains("Visual Arts: Art")
                        || idCourseDepartment.get((Integer) pair.getKey()).contains("Performing Arts: Music")
                        || idCourseDepartment.get((Integer) pair.getKey()).contains("Performing Arts: Creative Movement")) {
                    ArrayList<String> nombSubject = s.fetchNameAndElective((Integer) pair.getKey(), servlet);
                    os.add(nombSubject.get(0));
                    as.add("" + pair.getValue());
                    it.remove();
                }

            }
            /*while (rs.next()) {
            String comment = rs.getString("comment");
            //   if (!comment.equals("")) {
            //os.add("" + rs.getInt("subject_id"));
            ArrayList<String> nombSubject = s.fetchNameAndElective(rs.getInt("subject_id"), servlet);
            if (nombSubject.get(1).equals("true")) {
                os.add(nombSubject.get(0));
                as.add(comment);
            }
            //  }
        }*/
            //============================================================
            if (os.size() > 0) {
                BeanWithList bean = new BeanWithList("Comments", os, as, nameStudent, dob, age);
                coll.add(bean);
            }

            Iterator it2 = commentsPerSubject.entrySet().iterator();
            while (it2.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry) it2.next();
                //  ESTO HABRA QUE MODIFICARLO CUANDO SE ACTUALIC LA BBDD DE RENWEB
                if (idCourseDepartment.get((Integer) pair.getKey()).contains("Social Development")
                        || idCourseDepartment.get((Integer) pair.getKey()).contains("General")
                        || idCourseDepartment.get((Integer) pair.getKey()).contains("Social and Emotional Development")
                        || idCourseDepartment.get((Integer) pair.getKey()).contains("Emotional and Personal Development")) {
                    ArrayList<String> nombSubject = s.fetchNameAndElective((Integer) pair.getKey(), servlet);
                    ArrayList<String> os2 = new ArrayList<>();
                    os2.add("" + pair.getValue());
                    BeanWithList bean2 = new BeanWithList("" + nombSubject.get(0), os2, new ArrayList<String>(), nameStudent, dob, age);
                    coll.add(bean2);
                }
                it2.remove(); // avoids a ConcurrentModificationException
            }
        }
        /* //==========================================
        ArrayList<String> os2 = new ArrayList<>();
        os2.add("Informacion relacionada con Social Development");
        BeanWithList bean2 = new BeanWithList("SocialDevelopment", os2, new ArrayList<String>(), nameStudent, dob, age);
        coll.add(bean2);
        //============================

        ArrayList<String> os3 = new ArrayList<>();
        os3.add("Informacion relacionada con General");
        BeanWithList bean3 = new BeanWithList("General", os3, new ArrayList<String>(), nameStudent, dob, age);
        coll.add(bean3);*/
        //============================
        if (coll.isEmpty()) {
            BeanWithList bean = new BeanWithList("", new ArrayList<String>(), new ArrayList<String>(), nameStudent, dob, age);
            coll.add(bean);
        }

        return coll;

        // return new JRBeanCollectionDataSource(coll);
    }

    private boolean existInExcludeList(String  s){
         return !s.equals("Visual Arts: Art") && 
                 !s.equals("Performing Arts: Music") &&
                 !s.equals("Performing Arts: Creative Movement") &&
                 !s.equals("Social Development") &&
                 !s.equals("General") &&
                 !s.equals("Social and Emotional Development") &&
                 !s.equals("Emotional and Personal Development");
    }    
    @Override
    protected void cargarAlumno(String studentId) throws SQLException {
        String consulta = "SELECT * FROM Students where StudentId = '" + studentId + "'";
        ResultSet rs = DBConect.ah.executeQuery(consulta);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        while (rs.next()) {
            String resultado = getLinkSelfPortrait(studentId);
            if(resultado.equals(""))
                resultado= "AH-ZAF/configSchool/noImage.png";
                
            this.nameStudent = rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName") +"#ftp://david:david@192.168.1.36:21/"+resultado;
            this.dob = rs.getString("Birthdate");
            this.dob = dob.split(" ")[0];
            this.age = "" + (year - Integer.parseInt("" + dob.charAt(0) + dob.charAt(1) + dob.charAt(2) + dob.charAt(3)));
            this.grade = rs.getString("GradeLevel");
        }

    }
    private String getLinkSelfPortrait(String stdId){
        String resul ="";
        String consulta =" SELECT id,img_name FROM classobserv where student_id = "+stdId+
                        " and term_id ="+this.currentTerm+" and yearterm_id ="+this.currentYear+" and category='Self portrait' order by commentdate DESC";
        
        try {
            ResultSet rs = DBConect.eduweb.executeQuery(consulta);
            if(!rs.next()){
                return "";
            }
            else{
                resul = "MontessoriTesting/"+rs.getInt(1) +"/"+ rs.getInt(1) +"-"+rs.getString(2);
            }      
        } catch (SQLException ex) {
            Logger.getLogger(FactoryProgressReport_Pre_Primary.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resul;
    }
    @Override
    public String getNameReport() {
        return "Pre-Primary_Progress_Report_December2017_v2_4.jasper";
    }
}
