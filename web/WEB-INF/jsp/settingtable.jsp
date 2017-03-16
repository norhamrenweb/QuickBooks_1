<%-- 
    Document   : settingform
    Created on : 16-mar-2017, 13:31:37
    Author     : nmohamed
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
         <div class="container" align="center">
        <h1>New/Edit Objective</h1>
        <form:form>
        <table>
            <thead>
                        <tr>
                            <td>Objective name</td>
                            <td>Description</td> 
                             <td>Action</td>  
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="objective" items="${objectives}" >
                        <tr>
                            <td>${objective.name}</td>
                              
                            <td>${objective.description}</td>
                              <td>
                        <a href="editsetting.htm?option=editObjective&id=${objective.id[0]}">Edit</a>
                        
                        <a href="settingtable.htm?option=deleteObjective&id=${objective.id[0]}">Delete</a>
                    </td>
                       
                        </tr>
                    </c:forEach>
                    </tbody>
        </table>
        
        </form:form>
    </div>
        <c:out value="${message}" />
    </body>
</html>
