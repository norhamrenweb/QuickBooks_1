<%-- 
    Document   : bannerinfo
    Created on : 12-jul-2016, 16:23:16
    Author     : Jesús Aragón
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
          <meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" type="image/png" href="<c:url value="/recursos/img/iconos/favicon.ico" />" >
        <link rel="apple-touch-icon" href="<c:url value="/recursos/img/iconos/favicon.ico"/>">
        <link rel="shortcut icon" href="<c:url value="/recursos/img/iconos/favicon.ico"/>">
        <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Montserrat">   

        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/style.css" />"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/menu-lateral.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/bootstrap.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/calendar.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/bootstrap-theme.min.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/bootstrap-datetimepicker.css"/>"/>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/bootstrap-toggle.css"/>"/>

        <script type="text/javascript" src="<c:url value="/recursos/js/jquery-2.2.0.js" />"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/bootstrap.js" />"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/jquery-ui-1.11.4.custom/jquery-ui.js" />"></script>
        
        <script type="text/javascript" src="<c:url value="/recursos/js/moment.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/transition.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/collapse.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/bootstrap-toggle.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/bootstrap-datetimepicker.js"/>"></script>
        <%--<script type="text/javascript" src="<c:url value="/scripts/json.min.js" /> "></script>--%>
        <script type="text/javascript" src="<c:url value="/recursos/js/es.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/ar.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/moment.js"/>"></script>

<!--        DATATABLES-->
<%--        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/dataTables.bootstrap.css"/>" />--%>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/dataTables.foundation.css"/>" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/dataTables.jqueryui.css"/>" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/dataTables.semanticui.css"/>" />
<%--        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/dataTables.material.css"/>" />--%>
<%--        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/dataTables.uikit.css"/>" />--%>
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/jquery.dataTables.css"/>" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/css/dataTables/jquery.dataTables_themeroller.css"/>" />
        
        <script type="text/javascript" src="<c:url value="/recursos/js/dataTables/jquery.dataTables.js"/>" ></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/dataTables/dataTables.bootstrap.js"/>" ></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/dataTables/dataTables.bootstrap4.js"/>" ></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/dataTables/dataTables.foundation.js"/>" ></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/dataTables/dataTables.jqueryui.js"/>" ></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/dataTables/dataTables.material.js"/>" ></script>
        <script type="text/javascript" src="<c:url value="/recursos/js/dataTables/dataTables.uikit.js"/>" ></script>
        
       
        
    </head>
        
        <div class="infousuario">
        <div class="panel-heading">
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=en'>English</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=es'>Español</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=ar'>Arabic</a>
        </div>
            <h1 class="text-center">Hi, <c:out value="${sessionScope.user.name}"/></h1>
            
        </div>        
   
</html>
