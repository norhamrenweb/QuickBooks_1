/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports.DataFactoryFolder;

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
public abstract class DataFactory{
    String nameStudent, dob, age, grade, term;
    public abstract Collection getDataSource(String idStudent, ServletContext servlet) throws SQLException, ClassNotFoundException;
    public abstract String getNameReport();
                
    protected ResultSet cargarAlumno(String studentId, Connection cn,Statement st ) throws SQLException{
        String consulta = "SELECT * FROM AH_ZAF.dbo.Students where StudentId = '" + studentId + "'";
        ResultSet rs = st.executeQuery(consulta);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        while (rs.next()) {
            nameStudent = rs.getString("LastName") + ", " + rs.getString("FirstName") + " " + rs.getString("MiddleName");
            dob = rs.getString("Birthdate");
            dob = dob.split(" ")[0];
            age = "" + (year - Integer.parseInt("" + dob.charAt(0) + dob.charAt(1) + dob.charAt(2) + dob.charAt(3)));
        }
        return rs;
    }
}
