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
        <script>
            function newcomment(idstudent,idobjective,comment){
                $.ajax({
                    type: 'POST',
                    url: 'newcomment.htm?idstudent='+idstudent+'&idobjective='+idobjective+'&comment='+comment,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        console.log(data);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
                
            }
            
            function getsubjects(idstudent){
                $.ajax({
                    type: 'POST',
                    url: 'subjects.htm?idstudent='+idstudent,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        console.log(data);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function getobjectives(idsubject){
                $.ajax({
                    type: 'POST',
                    url: 'objectives.htm?idsubject='+idsubject,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        console.log(data);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function getcomments(idstudent,idobjective){
                $.ajax({
                    type: 'POST',
                    url: 'comments.htm?idstudent='+idstudent+'&idobjective='+idobjective,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        console.log(data);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function getstudents(idgrade){
                $.ajax({
                    type: 'POST',
                    url: 'studentslevel.htm?idgrade='+idgrade,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        console.log(data);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
        </script>
    </head>
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
            <tbody>
            <c:forEach var="alumnos" items="${students}" >
                <tr>
                    <td >${alumnos.id_students}</td>
                    <td >${alumnos.nombre_students}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>     
    </head>
        
        <!--<h1>Hello World!</h1>-->
    </body>
</html>
