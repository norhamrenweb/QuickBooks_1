/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author nmohamed
 */
public class ActivityLog {

    static Logger log = Logger.getLogger(ActivityLog.class.getName());

     public static void log(int userid, String studentid, String type,String note) {
        try {
            DBConect.eduweb.executeUpdate("insert into activitylog(userid,studentid,type,date,note) values('" + userid + "','" + studentid + "','" + type + "',now(),'"+note+"')");

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
    }

     
    public static void log(int userid, String type,String note) {
        try {
            DBConect.eduweb.executeUpdate("insert into activitylog(userid,type,date,note) values('" + userid + "','" + type + "',now(),'"+note+"')");

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            log.error(ex + errors.toString());
        }
    }

  

}
