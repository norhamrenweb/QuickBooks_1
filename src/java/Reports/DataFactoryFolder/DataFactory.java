/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

import Montessori.DBConect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import Montessori.*;

/**
 *
 * @author nmohamed
 */
public abstract class DataFactory {

    String nameStudent, dob, age, grade, term,termid="", yearid="";

    public abstract Collection getDataSource(String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException;

    public abstract String getNameReport();

    protected void cargarAlumno(String studentId) throws SQLException {
        String consulta = "SELECT * FROM AH_ZAF.dbo.Students where StudentId = '" + studentId + "'";
        ResultSet rs = DBConect.ah.executeQuery(consulta);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        while (rs.next()) {
            this.nameStudent = rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName");
            this.dob = rs.getString("Birthdate");
            this.dob = dob.split(" ")[0];
            this.age = "" + (year - Integer.parseInt("" + dob.charAt(0) + dob.charAt(1) + dob.charAt(2) + dob.charAt(3)));
            this.grade = rs.getString("GradeLevel");
        }
        
      
        ResultSet rs2 = DBConect.ah.executeQuery("select defaultyearid,defaulttermid from ConfigSchool where configschoolid = 1");
        while (rs2.next()) {
            termid = "" + rs2.getInt("defaulttermid");
            yearid = "" + rs2.getInt("defaultyearid");
        }
        String nameTerm="", nameYear="";
        
        ResultSet rs3 = DBConect.ah.executeQuery("select name from SchoolTerm where TermID = "+this.termid+" and YearID = "+this.yearid);
        while (rs3.next()) {
            nameTerm = "" + rs3.getString("name");
        }
        ResultSet rs4 = DBConect.ah.executeQuery("select SchoolYear from SchoolYear where yearID = "+this.yearid);
        while (rs4.next()) {
            nameYear = "" + rs4.getString("SchoolYear");
        }
        
        this.term = nameTerm+" / "+nameYear;
    }
}
