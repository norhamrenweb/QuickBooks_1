<%-- 
    Document   : listadealumnos
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
        <title>Create Lessons</title>
        
        <link href="recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        <script>
        $().ready(function() 
	{
		$('.pasar').click(function() { return !$('#origen option:selected').remove().appendTo('#destino'); });  
		$('.quitar').click(function() { return !$('#destino option:selected').remove().appendTo('#origen'); });
		$('.pasartodos').click(function() { $('#origen option').each(function() { $(this).remove().appendTo('#destino'); }); });
		$('.quitartodos').click(function() { $('#destino option').each(function() { $(this).remove().appendTo('#origen'); }); });
		$('.submit').click(function() { $('#destino option').prop('selected', 'selected'); });
	});
        </script>
    </head>
    <body>
        
        <div class="panel-heading">
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=en'>English</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=es'>Español</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=ar'>Arabic</a>
        </div>
        <h1 class="text-center">Crear Lessons</h1>

        
        <form:form id="formStudents" method ="post" action="listadealumnos.htm?select=addstudents" >
        <div class="col-xs-12">
            <label class="control-label"><spring:message code="etiq.txtname"/></label>
            <p class="clear"><input type="text" class="input-sm" name="TXTnombreLessons" value="<spring:message code="etiq.txtname"/>"></p>
        </div>
        <div class="col-xs-4">
            
<!--                <select class="form-control select-level" id="idlevel" name="TXTlevel" onchange="comboSelectionLevel()">          -->
                <select class="form-control" size="20" multiple name="origen[]" id="origen">
                    <c:forEach var="alumnos" items="${listadealumnos}">
                        <option value="${alumnos.id_students}" >${alumnos.nombre_students}</option>
                    </c:forEach>
                </select>
            
        </div>
        <div class="col-xs-2">
            <input type="button" class="pasar izq" value="Pasar »"><input type="button" class="quitar der" value="« Quitar"><br />
            <input type="button" class="pasartodos izq" value="Todos »"><input type="button" class="quitartodos der" value="« Todos">
        </div>
        </div>
        <div class="col-xs-4">
            
<!--                <select class="form-control select-level" id="idlevel" name="TXTlevel" onchange="comboSelectionLevel()">          -->
            <select class="form-control" size="20" multiple name="destino[]" id="destino"> 
                    
            </select>
            
        </div>
        <div class="col-xs-12">
            <p class="clear"><input type="submit" class="submit" value="Procesar formulario"></p>
        </div>
        </form:form>
    </body>
</html>
