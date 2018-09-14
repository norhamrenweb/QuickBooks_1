/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class ActivityLog {

    static Logger log = Logger.getLogger(ActivityLog.class.getName());

    public static void log(User user, String studentid, String type, String note) {

        studentid = studentid.replace("\'", "\'\'");
        studentid = studentid.replace("\"", "\"\"");
        note = note.replace("\'", "\'\'");
        note = note.replace("\"", "\"\"");
        try {
            PoolC3P0_Local pool = PoolC3P0_Local.getInstance();
            Connection con = pool.getConnection();
            Statement stAux = con.createStatement();
            stAux.executeUpdate("insert into activitylog(userid,username, studentid,type,date,note) values('" + user.getId() + "','" + user.getName() + "','" + studentid + "','" + type + "',now(),'" + note + "')");
            con.close();
        } catch (IOException | SQLException | PropertyVetoException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
    }

    public static void log(int userid, String type, String note) {
        Connection con = null;
        Statement stAux = null;
        //DBCPDataSource
        try {
            PoolC3P0_Local pool = PoolC3P0_Local.getInstance();
            con = pool.getConnection();
            stAux = con.createStatement();
            //  Statement stmt = con_ah.createStatement(1004, 1007); get ret
            stAux.executeUpdate("insert into activitylog(userid,type,date,note) values('" + userid + "','" + type + "',now(),'" + note + "')");
            con.close();
        } catch (Exception ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }  
    }

}
