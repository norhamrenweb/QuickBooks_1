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

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class LoginVerification {
    
    public LoginVerification(){}
    public static Connection SQLConnection() throws SQLException {
        System.out.println("database.SQLMicrosoft.SQLConnection()");
        String url = "jdbc:sqlserver://ah-zaf.odbc.renweb.com\\ah_zaf:1433;databaseName=ah_zaf";
        String loginName = "AH_ZAF_CUST";
        String password = "BravoJuggle+396";
        DriverManager.registerDriver(new SQLServerDriver());
        Connection cn = null;
        try {

            cn = DriverManager.getConnection(url, loginName, password);
        } catch (SQLException ex) {
            System.out.println("No se puede conectar con el Motor");
            System.err.println(ex.getMessage());
        }

        return cn;
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

    public static ResultSet SQLQuery(String queryString) throws SQLException {
        return Query(SQLConnection(), queryString);
    }

    public User consultUserDB(String user,String password) throws Exception {
        User u = null;
       //user = 'shahad' and pswd = 'shahad1234' group = Spring
        String query = "select * from Person where username = '"+user+"' and pswd = HASHBYTES('MD5', CONVERT(nvarchar(4000),'"+password+"'));";
     
         ResultSet rs = SQLQuery(query);
         if(!rs.next()) 
         {u=new User();
                 u.setId(0);}
         else{
             rs.beforeFirst();
            while(rs.next()){
               
                u = new User();
                u.setName(rs.getString("username"));
                u.setPassword(password);
                u.setId(rs.getInt("PersonID"));

            }}
        return u;
    }
    public int getSecurityGroupID(String name) throws SQLException{
        int sgid = 0;
        String query ="select groupid from SecurityGroups where Name like '"+name+"'";
         ResultSet rs = SQLQuery(query);
            while(rs.next()){
                sgid = rs.getInt(1);
            }
        return sgid;
    }
    
    public boolean fromGroup(int groupid, int staffid) throws SQLException{
        boolean aux  = false;
        String query = "select * from SecurityGroupMembership where groupid = "+groupid+" and StaffID = " + staffid;
        ResultSet rs = SQLQuery(query);
            while(rs.next()){
                aux = true;
            }
      
        return aux;
    }
    
}
