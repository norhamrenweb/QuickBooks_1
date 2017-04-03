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
        <title>Welcome</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/bootstrap.css"/>" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/estilotabla.css"/>" />
    </head>
    <body>
           <div class="col-sm-12" style="margin-top: 10px;">

        <div class="panel panel-success">

            <div class="panel-body"align="center">

                <div class="container " style="margin-top: 10%; margin-bottom: 10%;">

                    <div class="panel panel-success" style="max-width: 35%;" align="left">

                        <div class="panel-heading form-group fondoGris">
                            <img class="img-responsive center-block" src="recursos/img/logoeduweb.png" alt="logo"/>
                        </div>

                        <div class="panel-body" >

                            <form name ="form1" action="userform.htm?opcion=login" method="post" >
                                <div
                                    <c:if test="${message != null}">
                                    <h5 style="color:blue">
                                        <c:out value="${message}"/>
                                    </h5>
                                </c:if>
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputEmail1"><spring:message code="etiq.txtuser"/></label> 
                                    <input type="text" class="form-control" name="txtusuario" id="txtusuario" placeholder="<spring:message code='etiq.txtinsertuser'/>" required="required">    
                                </div>
                                <div class="form-group">
                                    <label for="exampleInputPassword1"><spring:message code="etiq.txtpassword"/></label> 
                                    <input type="password" class="form-control" name="txtpassword" id="txtpassword" placeholder="<spring:message code='etiq.txtinsertpassword'/>" ><!--required="required"-->
                                </div>
                                <button type="submit" name="submit" value='<spring:message code="etiq.txtlogin"/>' style="width: 100%; font-size:1.1em;" class="btn btn-large btn btn-success btn-lg btn-block" ><b>Login</b></button>

                            </form>
                            <div class="center-block text-center">
                                <a class="btn" href="datosIdioma.htm?lenguaje=en"><spring:message code="etiq.txtenglish"/></a>
                                <a class="btn" href="datosIdioma.htm?lenguaje=es"><spring:message code="etiq.txtspanish"/></a>
                                <a class="btn" href="datosIdioma.htm?lenguaje=ar"><spring:message code="etiq.txtarabic"/></a>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
            <div class="panel-footer" align="center"><font style="color: #111">Copyright @2016, EduWeb Group, All Rights Reserved. </font></div>
        </div>
    </div>
                
                                <%--<form:form id="form1" method ="post" action="userform.htm?opcion=login" >
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

<jstl:out value="${message}"/>  --%>      
      
        
    </body>
</html>