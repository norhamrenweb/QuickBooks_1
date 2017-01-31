<%-- 
    Document   : userform
    Created on : 24-ene-2017, 12:05:12
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="estilotabla.css"/>
    </head>
    <body>
        <h1>Eduweb</h1>
                
        <form:form id="form1" method ="post" action="userform.htm?opcion=login" >
            <table border="1">
                <tr>
                    <th>Nombre:</th>
                    <td>
                        <input type="text" name="txtusuario"/>
                    </td>
                </tr>
             
             
                <tr>
                    <td colspan="2">
                        <input type="submit" value="login"/>            
                    </td>
                </tr>
            </table>
        </form:form>

<jstl:out value="${message}"/>        
      
        
    </body>
</html>