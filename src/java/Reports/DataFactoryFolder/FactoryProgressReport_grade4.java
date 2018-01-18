/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

import Montessori.Objective;
import Montessori.Subject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import javax.servlet.ServletContext;

/**
 *
 * @author nmoha
 */
public class FactoryProgressReport_grade4 extends DataFactory {

    public FactoryProgressReport_grade4() {
        nameStudent = "";
        dob = "";
        age = "";
        grade = " grade 4";
        term = " term3/2018";
    }

    public Collection getDataSource(String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException {

        String studentId = idStudent;

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection cn = DriverManager.getConnection("jdbc:sqlserver://ah-zaf.odbc.renweb.com\\ah_zaf:1433;databaseName=ah_zaf", "AH_ZAF_CUST", "BravoJuggle+396");
        Statement st = cn.createStatement();
        String consulta = "";

        ResultSet rs = cargarAlumno(studentId, cn, st);

        Class.forName("org.postgresql.Driver");
        cn = DriverManager.getConnection("jdbc:postgresql://192.168.1.3:5432/Lessons?user=eduweb&password=Madrid2016");
        st = cn.createStatement();
        ArrayList<String> lessons = new ArrayList<>();
        java.util.Vector coll = new java.util.Vector();
        ArrayList<Subject> subjects = new ArrayList<>();
        rs = st.executeQuery("SELECT lesson_id from lesson_stud_att where student_id = '" + studentId + "' and attendance != 'null' and attendance !=' '");
        while (rs.next()) {
            lessons.add("" + rs.getInt("lesson_id"));
        }
        //select the subjects of these lessons
        ArrayList<String> subids = new ArrayList<>();
        for (String b : lessons) {
            ResultSet rs2 = st.executeQuery("select subject_id from lessons where id =" + b);

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
            Class.forName("org.postgresql.Driver");
            cn = DriverManager.getConnection("jdbc:postgresql://192.168.1.3:5432/Lessons?user=eduweb&password=Madrid2016");
            st = cn.createStatement();
            String[] id = x.getId();
            ArrayList<String> os = new ArrayList<>();
            for (String b : lessons) {
                ResultSet rs1 = st.executeQuery("select objective_id from lessons where id = " + b + " and subject_id =" + id[0]);
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
                ResultSet rs2 = st.executeQuery(consulta);
                if (!rs2.next()) {
                    indEliminarObjectives.add(counter);
                } else {
                    rs2 = st.executeQuery(consulta);
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
            
            if (os.size() > 0) {

                ArrayList<String> subjectName = x.fetchNameAndElective(Integer.parseInt(id[0]), servlet);
                if (subjectName.get(1).equals("false")) { // compruebo que no sea electivo
                    BeanWithList bean = new BeanWithList(subjectName.get(0), os, finalratings, nameStudent, dob, age, grade, term);
                    
                    coll.add(bean);
                    coll.add(bean);
                }
            }
            cn.close();
        }
        return coll;
        // return new JRBeanCollectionDataSource(coll);
    }

    @Override
    public String getNameReport() {
        return "progress_report_2017_gr4.jasper";
    }
}
