<%-- 
    Document   : Observations
    Created on : 08-feb-2018, 11:36:41
    Author     : Norhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

    <body>
        <select>
            <c:forEach var="levels" items="${gradelevels}">
                <option value="${levels.id[0]}">${levels.name}</option>
            </c:forEach>
        </select>
        
        <table id="table_students" class="display" >
            <thead>
                <tr>
                    <td>ID</td>
                    <td>Name students</td>
                </tr>
            </thead>
            <c:forEach var="alumnos" items="${students}" >
                <tr>
                    <td >${alumnos.id_students}</td>
                    <td >${alumnos.nombre_students}</td>
                </tr>
            </c:forEach>
        </table>     
    </head>
        
        <!--<h1>Hello World!</h1>-->
    </body>
</html>
