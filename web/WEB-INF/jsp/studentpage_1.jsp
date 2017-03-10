<%-- 
    Document   : createlesson
    Created on : 30-ene-2017, 14:59:17
    Author     : nmohamed
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>hola</title>
        
        <link href="recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
      
        <link href="recursos/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
        <link href="recursos/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="recursos/css/bootstrap-toggle.css" rel="stylesheet" type="text/css"/>
        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        
        <script src="recursos/js/bootstrap.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-toggle.js" type="text/javascript"></script>
<!--        <script src="recursos/js/bootstrap-modal.js" type="text/javascript"></script>-->
        <script src="recursos/js/moment.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script src="recursos/js/es.js" type="text/javascript"></script>
        <script src="recursos/js/ar.js" type="text/javascript"></script>
        

   
        <style>
            textarea 
            {
            resize: none;
            }
        </style>
    </head>

    <body>
        
        
        <div class="container">
      
 
  <div class="col-xs-12">
                <table id="table_progress" class="display" >
                    <thead>
                        <tr>
                            <td>Lesson name</td>
                  
                            <td>Rating</td>
                            <td>Comment</td>
                            <td>Date</td>
                          
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${progress}" >
                        <tr>
                            <td><input type="hidden" class="form-control" name="TXTstudentid" value="${record.studentid}"/>${record.studentid}</td>
                            <td>${record.lesson_name}</td>
                         <td>${record.comment_date}</td>
                            <td>${record.rating}</td>
                            <td>${record.comment}</td>
                           
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
           
            </div>
          

        
        </div>




    </body>
</html>
