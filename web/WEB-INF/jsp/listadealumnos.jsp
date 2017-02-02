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
        
        var ajax;
        
 function funcionCallBackLevelStudent()
    {
           if (ajax.readyState==4){
                if (ajax.status==200){
                    document.getElementById("origen").innerHTML= ajax.responseText;
                    }
                }
            }
            

    function comboSelectionLevelStudent()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        ajax.onreadystatechange=funcionCallBackLevelStudent;
        var seleccion = document.getElementById("level").value;
        var alumnos = document.getElementById("destino").innerHTML;
        ajax.open("POST","listadealumnos.htm?select=cargalistagrade&seleccion="+seleccion,true);
        ajax.send("");
    }
     function funcionCallBackSubject()
    {
           if (ajax.readyState==4){
                if (ajax.status==200){
                    document.getElementById("idsubjects").innerHTML= ajax.responseText;
                    //document.getElementById("idequipment").innerHTML= "<option value=\"0\"><spring:message code="etiq.selectequipment"/></option>"
                    }
                }
            }
//    
//    function comboSelectionSubject()
//    {
//        if (window.XMLHttpRequest) //mozilla
//       {
//            ajax = new XMLHttpRequest(); //No Internet explorer
//        }
//        else
//        {
//            ajax = new ActiveXObject("Microsoft.XMLHTTP");
//        }
//
//        $('#createOnClick').attr('disabled', true);
//        ajax.onreadystatechange=funcionCallBackSubject;
//        var seleccion1 = document.getElementById("idsubjects").value;
//        ajax.open("GET","listadealumnos.htm?select=cargalistasubject&seleccion1="+seleccion1,true);
//        ajax.send("");
//    } 

    function comboSelectionLevel()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

        $('#createOnClick').attr('disabled', true);
        ajax.onreadystatechange=funcionCallBackSubject;
        var seleccion1 = document.getElementById("levelsubject").value;
        ajax.open("POST","listadealumnos.htm?select=cargalistasubject&seleccion1="+seleccion1,true);
        ajax.send("");
    }
        </script>
    </head>
    <body>
        
        <div class="panel-heading">
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=en'>English</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=es'>Español</a>
            <a class="btnBandera" href='datosIdioma.htm?lenguaje=ar'>Arabic</a>
        </div>
        <div class="container">
        <h1 class="text-center">Create Lessons</h1>

        
        <form:form id="formStudents" method ="post" action="listadealumnos.htm?select=createlesson" class="form-inline" >
            <fieldset>
                <legend>Options</legend>
                <div class="col-xs-12 form-group">
                    <label class="control-label"><spring:message code="etiq.namelessons"/></label>
                    <input type="text" class="input-sm" name="TXTnombreLessons" required="" placeholder="<spring:message code="etiq.namelessons"/>">
                </div>
                        <div class="col-xs-2">
                            <select class="form-control" name="levelsubject" id="levelsubject" style="width: 100% !important;" onchange="comboSelectionLevel()">
                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels}" >${levels}</option>
                                </c:forEach>
                            </select>
                        </div>
                    <div class="col-xs-3 center-block">
                        <label class="control-label"><spring:message code="etiq.txtsubject"/></label>
                        <select class="form-control select-subjects" id="idsubjects" name="TXTsubjects" onchange="comboSelectionSubject()">
                           <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject}" >${subject}</option>
                                </c:forEach>
                        </select>

                        <input type="hidden" class="btn btn-default" name='accion' id="subjectOnClick" value="subjectOnClick"/>

                    </div>
                        
            </fieldset>
            <fieldset>
                    <legend>Select students</legend>
                    <div class="col-xs-12">
                        <div class="col-xs-2"></div>
                        <div class="col-xs-3">
                            <label>Filter</label>

                            
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="col-xs-2">
                            <select class="form-control" name="level" id="level" style="width: 100% !important;" onchange="comboSelectionLevelStudent()">
                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels}" >${levels}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <select class="form-control" size="20" multiple name="origen[]" id="origen" style="width: 100% !important;">
                                <c:forEach var="alumnos" items="${listadealumnos}">
                                    <option value="${alumnos.id_students}" >${alumnos.nombre_students}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="col-xs-2">
                            <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                <input type="button" class="btn pasar" value="<spring:message code="etiq.txtadd"/> »">
                            </div>
                            <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                <input type="button" class="btn quitar" value="« <spring:message code="etiq.txtremove"/>">
                            </div>
                            <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                <input type="button" class="btn pasartodos" value="<spring:message code="etiq.txtaddAll"/> »">
                            </div>
                            <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                <input type="button" class="btn quitartodos" value="« <spring:message code="etiq.txtremoveAll"/>">
                            </div>
                        </div>
                
                        <div class="col-xs-3">
                            <select class="form-control submit" size="20" multiple name="destino[]" id="destino" style="width: 100% !important;"> 

                            </select>
                        </div>
                    </div>
                <div class="col-xs-2"></div>
            </fieldset>
        <div class="col-xs-12 text-center">
            <input type="submit" class="btn btn-success" value="<spring:message code="etiq.txtcreate"/>">
        </div>
        </form:form>
        
        </div>
        <c:out value="${message}"/>
    </body>
</html>
