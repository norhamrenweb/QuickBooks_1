<%-- 
    Document   : setup
    Created on : 24-ene-2018, 10:10:20
    Author     : Norhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <%@ include file="menu.jsp" %>
    <head>
        <title>Setup</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            function changecolor(){
                var color = $('#headcolor').val();
                $('#infousuario').css("background-color",color);
            }
            function changecolor2(){
                var color = $('#bodycolor').val();
                $('body').css("background-color",color);
            }
        </script>
    </head>
    <body>
         <div class="container">
            <h1 class="text-center">Setup</h1>
            <form:form id="formConfig" method ="post" action="save.htm" >
                <div class="col-xs-4 text-center">
                    <h2>Header Color</h2>
                    <input type="color" id="headcolor" name="headcolor" value="#ff0000" onchange="changecolor()">
                </div>
                <div class="col-xs-4 text-center">
                    <h2>Body Color</h2>
                    <input type="color" id="bodycolor" name="bodycolor" value="#ff0000" onchange="changecolor2()">
                </div>
                <div class="col-xs-4 text-center">
                    <input type="submit" class="btn btn-success" value="set">
                </div>
            </form:form>
            <form:form enctype="multipart/form-data" id="formIcono" method ="post" action="/QuickBooks_1/setupicono" >
                <div class="col-xs-12" style="margin-top: 30px">
                    <div class="col-xs-4" >
                        <input type="file" name="fileToUpload" id="icono" accept="image/*">
                    </div>
                    <div class="col-xs-4 text-center">
                        <input type="submit" onclick="$('#myModal').modal({show: 'true'});" class="btn btn-success" value="set">
                    </div>
                </div>
            </form:form>
        </div>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleComment"><%= request.getParameter("message")%></h4>
                    </div>
                </div>
            </div>
        </div>   
    </body>
</html>
