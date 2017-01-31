<%-- 
    Document   : familymap2
    Created on : 27-ene-2017, 13:54:53
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" 
                  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
      <table border="1">
        <c:forEach items="${allfamily}"
                   var="family">
            <tr>
                <td>
                    <c:out value="${family.familyName}"/>
                </td>
                <td>
                    <c:out value="${family.id}"/>
                </td>   
                       
            </tr>
        </c:forEach>
        </table>
    </body>
</html>
