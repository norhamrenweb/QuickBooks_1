<%-- 
    Document   : bannerinfo
    Created on : 12-jul-2016, 16:23:16
    Author     : Jesús Aragón
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
       
        
    </head>
        
        <div class="infousuario">
        <div class="panel-heading">
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=en'>English</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=es'>Español</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=ar'>Arabic</a>
        </div>
            <h1 class="text-center">Hi, <c:out value="${message}"/></h1>
            
        </div>        
   
</html>
