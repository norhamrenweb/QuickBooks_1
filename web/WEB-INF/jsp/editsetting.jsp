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
<title>New/Edit Contact</title>
</head>
<body>
    <div align="center">
        <h1>New/Edit Contact</h1>
        <form:form action="editsetting.htm?option=saveObjective" >
        <table>
            <tr>
                    <td>ID:</td>
                    <td><input type="text" name="id" value="${objective.id[0]}"/> </td>

                </tr>
            <tr>
                <td>Name:</td>
                <td><input type="text" name="name" value="${objective.name}"/> </td>
            
            </tr>
            <tr>
                <td>Description:</td>
                <td><input type="text" name="description" value="${objective.description}"/></td>
            </tr>
            
            <tr>
                <td colspan="2" align="center"><input type="submit" value="Save"></td>
            </tr>
        </table>
        </form:form>
    </div>
    <c:out value="${message}" />

</body>
</html>