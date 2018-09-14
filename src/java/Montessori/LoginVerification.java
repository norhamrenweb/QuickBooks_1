/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author nmohamed
 */
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.beans.PropertyVetoException;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginVerification {

    public LoginVerification() {
    }

      
    public static ResultSet Query(Connection conn, String queryString) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(queryString);
        //stmt.close();
        //conn.close();
        return rs;
    }
 

    public User consultUserDB(String user, String password) throws Exception {
        User u = null;
        //user = 'shahad' and pswd = 'shahad1234' group = Spring
        String query = "select username,PersonID from Person where username = '" + user + "' and pswd = HASHBYTES('MD5', CONVERT(nvarchar(4000),'" + password + "'));";
        try {
            PoolC3P0_RenWeb pool = PoolC3P0_RenWeb.getInstance();
            try (Connection con_ah = pool.getConnection()) {
                Statement stmt = con_ah.createStatement(1004, 1007);

                ResultSet rs = stmt.executeQuery(query);// SQLQuery(query);
                // ResultSet rs = DBConect.ahBeforeFirst.executeQuery(query);
                if (!rs.next()) {
                    u = new User();//TARDO
                    u.setId(0);
                } else {
                    rs.beforeFirst();
                    while (rs.next()) {

                        u = new User();
                        u.setName(rs.getString("username"));
                        u.setPassword(password);
                        u.setId(rs.getInt("PersonID"));

                    }
                }
                con_ah.close();
            }
            
        } catch (IOException | SQLException | PropertyVetoException e) {

        }
        /*try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
        }
         */
        return u;
    }/*
    public int getSecurityGroupID(String name) throws SQLException{
        int sgid = 0;
        String query ="select groupid from SecurityGroups where Name like '"+name+"'";
        // ResultSet rs = SQLQuery(query);
        ResultSet rs = DBConect.ah.executeQuery(query);
            while(rs.next()){
                sgid = rs.getInt(1);
            }
        return sgid;
    }*/

    public HashMap getSecurityGroupID() throws SQLException {

        HashMap<Integer, String> mapGroups = new HashMap<Integer, String>();

        String query = "select groupid,Name from SecurityGroups";

        try {
            PoolC3P0_RenWeb pool = PoolC3P0_RenWeb.getInstance();
            try (Connection con_ah = pool.getConnection()) {
                Statement stmt = con_ah.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                // ResultSet rs = SQLQuery(query);
                // ResultSet rs = DBConect.ah.executeQuery(query);
                while (rs.next()) {
                    mapGroups.put(rs.getInt("groupid"), rs.getString("Name"));
                }
                con_ah.close();
            }
        } catch (IOException | SQLException | PropertyVetoException e) {

        }
        /*try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
        }
        try {
            if (con_ah != null) {
                con_ah.close();
            }
        } catch (Exception e) {
        }*/
        return mapGroups;
    }

    public ArrayList<String> fromGroupNames(int staffid) throws SQLException {
        ArrayList<String> aux = new ArrayList<>();
        HashMap<Integer, String> mapGroups = getSecurityGroupID();
        String query = "select groupid from SecurityGroupMembership where StaffID = " + staffid;

        try {
            PoolC3P0_RenWeb pool = PoolC3P0_RenWeb.getInstance();
            Connection con_ah = pool.getConnection();
            Statement stmt = con_ah.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = SQLQuery(query);
            //ResultSet rs = DBConect.ah.executeQuery(query);
            while (rs.next()) {
                aux.add(mapGroups.get(rs.getInt("groupid")));
            }
            con_ah.close();
        } catch (Exception e) {

        }
        /*
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
        }*/

        return aux;
    }

    public ArrayList<Integer> fromGroup(int staffid) throws SQLException {
        ArrayList<Integer> aux = new ArrayList<>();
        String query = "select groupid from SecurityGroupMembership where StaffID = " + staffid;
        // ResultSet rs = SQLQuery(query);
        //ResultSet rs = DBConect.ah.executeQuery(query);
        try {
            //DataFtp
            //PoolC3P0_Local pool_local = PoolC3P0_Local.getInstance();
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            Connection con_ah = pool_renweb.getConnection();
            Statement stmt = con_ah.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                aux.add(rs.getInt("groupid"));
            }
            con_ah.close();
        } catch (IOException | SQLException | PropertyVetoException e) {

        }

        return aux;
    }

    public boolean fromGroup(int groupid, int staffid) throws SQLException {
        boolean aux = false;
        String query = "select * from SecurityGroupMembership where groupid = " + groupid + " and StaffID = " + staffid;
        // ResultSet rs = SQLQuery(query);
        //    ResultSet rs = DBConect.ah.executeQuery(query);
        try {
            PoolC3P0_RenWeb pool_renweb = PoolC3P0_RenWeb.getInstance();
            Connection con_ah = pool_renweb.getConnection();
            Statement stmt = con_ah.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                aux = true;
            }
            con_ah.close();
        } catch (IOException ex) {
            Logger.getLogger(LoginVerification.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(LoginVerification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

}
