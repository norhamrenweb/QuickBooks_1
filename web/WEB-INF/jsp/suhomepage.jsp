<%-- 
    Document   : SUhomepage
    Created on : 24-ene-2017, 12:12:34
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
       
        
    </head>
    <body>
        <h1><jstl:out value="${message}"/></h1>
        <form:form id="form1" method ="post" action="suhomepage.htm?opcion=save">
            <table border="1">
                <tr>
                    <th>Quickbooks DB URL:</th>
                    <td>
                        <input type="text" name="qbdburl"/>
                    </td>
                </tr>
             <tr>
                    <th>Quickbooks DB user:</th>
                    <td>
                        <input type="text" name="qbdbuser"/>
                    </td>
                </tr>
                <tr>
                    <th>Quickbooks DB pswd:</th>
                    <td>
                        <input type="text" name="qbdbpswd"/>
                    </td>
                </tr>
                <tr>
                    <th>Renweb DB URL:</th>
                    <td>
                        <input type="text" name="rwdburl"/>
                    </td>
                </tr>
                <tr>
                    <th>Renweb DB user:</th>
                    <td>
                        <input type="text" name="rwdbuser"/>
                    </td>
                </tr>
                <tr>
                    <th>Renweb DB pswd:</th>
                    <td>
                        <input type="text" name="rwdbpswd"/>
                    </td>
                </tr>
                <tr>
                    <th>Sync Start Date:</th>
                    <td>
                        <input type="text" name="startdate"/>
                    </td>
                </tr>
                <tr>
                    <th>Item Name:</th>
                    <td>
                        <input type="text" name="itemname"/>
                    </td>
                </tr>
                <tr>
                    <th>Eduweb DB URL:</th>
                    <td>
                        <input type="text" name="edudburl"/>
                    </td>
                </tr>
                <tr>
                    <th>Eduweb DB user:</th>
                    <td>
                        <input type="text" name="edudbuser"/>
                    </td>
                </tr>
                <tr>
                    <th>Eduweb DB pswd:</th>
                    <td>
                        <input type="text" name="edudbpswd"/>
                    </td>
                </tr>
             
                <tr>
                    <td colspan="2">
                      <input type="submit" value="Save" opcion="save"/>   
                         
                    </td>
                </tr>
            </table>
        </form:form>
         <a href="suhomepage.htm?opcion=runsync"> run </a>  
        <br/>
        <jstl:out value="${message1}"/> 
        <br/>
        <a href="familymap2.htm?opcion=map"> Map Families </a>
    </body>
</html>
