<%-- 
    Document   : lessonList
    Created on : 17-nov-2016, 19:24:49
    Author     : Jesus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ include file="header.jsp" %>
    <%@ include file="bannerinfo.jsp" %>

    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <link href="recursos/css/lessons.css" rel="stylesheet" type="text/css"/>
        
<!--        dataTables-->
<!--<link rel="stylesheet" type="text/css" href="/DataTables/datatables.css">
 
<script type="text/javascript" charset="utf8" src="/DataTables/datatables.js"></script>-->
<!--<link href="recursos/css/dataTables/dataTables.bootstrap.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.bootstrap4.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.foundation.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.jqueryui.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.material.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.semanticui.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.uikit.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/jquery.dataTables.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/jquery.dataTables_themeroller.css" rel="stylesheet" type="text/css"/>-->

<link href="recursos/css/dataTables/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.bootstrap4.min.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.foundation.min.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.jqueryui.min.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.material.min.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.semanticui.min.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/dataTables.uikit.min.css" rel="stylesheet" type="text/css"/>
<link href="recursos/css/dataTables/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>



<!--<script src="recursos/js/dataTables/dataTables.bootstrap.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/jquery.dataTables.js" type="text/javascript"></script>-->
<script src="recursos/js/dataTables/jquery.dataTables.min.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/dataTables.bootstrap.min.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/dataTables.bootstrap4.min.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/dataTables.foundation.min.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/dataTables.jqueryui.min.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/dataTables.material.min.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/dataTables.semanticui.min.js" type="text/javascript"></script>
<script src="recursos/js/dataTables/dataTables.uikit.min.js" type="text/javascript"></script>

        <script>
    $(document).ready( function () {
    $('#table_id').DataTable();
} );    
    
        
    var ajax;
        
        function funcionCallBackModal()
    {
        if (ajax.readyState==4){
            if (ajax.status==200){
                document.getElementById("modal").innerHTML= ajax.responseText;
            }
        }
     }

    function ajaxModal(details)
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        ajax.onreadystatechange=funcionCallBackModal;
        var idLesson = details;
        ajax.open("GET","modalAjax.htm?idLesson="+idLesson +"&etiqClose=<spring:message code="etiq.txtclose"/>\
                &etiqStart=<spring:message code="etiq.txtstartdate"/>\
                &etiqEnd=<spring:message code="etiq.txtenddate"/>\
                &etiqFile=<spring:message code="etiq.nameFile"/>\
                &etiqLevel=<spring:message code="etiq.levellessons"/>\
                &etiqSubject=<spring:message code="etiq.subjectlessons"/>\
                &etiqSubsection=<spring:message code="etiq.subsectionlessons"/>" ,true);
        ajax.send("");
    }
        
        
        </script>

        <title><spring:message code="etiq.txtlessons"/></title>
    </head>
    <body>
        <div>
            <div class="col-xs-2 pull-right">
                <div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="create.htm?accion=inicio">
                               DataTables <button type="submit" id="crearLessons" value="Crear" class="btn btn-naranja"><spring:message code="etiq.txtcreate"/></button>
                            </form:form>
                        </div>
                </div>
            </div>
            <div class="col-sm-12" id="maincontainer">
                <div class="col-sm-12 center-block text-center">
                    <h2><spring:message code="etiq.txtactivities"/></h2>
                </div>
            </div>
            <div class="col-xs-12">
                <table id="table_id" class="display">
                    <thead>
                        <tr>
                            <td><spring:message code="etiq.txtstartdate"/></td>
                            <td><spring:message code="etiq.namelessons"/></td>
                            <td><spring:message code="etiq.levellessons"/></td>
                            <td><spring:message code="etiq.subjectlessons"/></td>
                            <td><spring:message code="etiq.subsectionlessons"/></td>
                            <td><spring:message code="etiq.equipmentlessons"/></td>
                            <td><spring:message code="etiq.actionlessons"/></td>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="lecciones" items="${listalecciones}">
                        <tr>
                            <td>${lecciones.fecha_inicio}</td>
                            <td>${lecciones.nombre_lessons}</td>
                            <td>${lecciones.nombre}</td>
                            <td>${lecciones.nombre_subject}</td>
                            <td>${lecciones.nombre_subsection}</td>
                            <td>
                                <c:forEach var="materiales" items="${lecciones.equipment}">
                                    <div class="nombreActividad">${materiales.nombre_activity_equipment} </div>  
                                </c:forEach>
                            </td>
                            <td>
                                <div class="col-xs-6 col-xs-offset-3">
                                    <div class="col-xs-4">
                                        <button name="TXTid_lessons_detalles" value="${lecciones.id_lessons}" class="btn btn-detalles" id="details" data-toggle="modal" data-target="#myModal" onclick="ajaxModal(${lecciones.id_lessons})"><!--<a href= javascript:popUp('/details.jsp')  target="_blank" onClick="window.open(this.href, this.target, 'width=300,height=400'); return false;">-->
                                            <span class="glyphicon glyphicon-list-alt" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="etiq.txtdetails"/>"></span>
                                        </button>
                                    </div>
                                    <div class="col-xs-4">
                                        <form:form id="formMod" action="modify.htm?accion=inicio">
                                            <button name="TXTid_lessons_modificar" type="submit" class="btn btn-modificar" id="modificarLessons" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="etiq.txtmodify"/>" value="${lecciones.id_lessons}">
                                                <span class="glyphicon glyphicon-pencil"></span>
                                            </button>
                                        </form:form>
                                    </div>
                                    <div class="col-xs-4">    
                                        <form:form id="formElim" action="lessonEliminate.htm">
                                            <button name="TXTid_lessons_eliminar" value="${lecciones.id_lessons}" class="btn btn-eliminar" data-toggle="tooltip" data-placement="bottom" title="<spring:message code="etiq.txtremove"/>">
                                                <span class="glyphicon glyphicon-trash"></span>
                                            </button>
                                       </form:form>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
            <div class="col-xs-2 pull-right">
                <div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="create.htm?accion=inicio">
                                <button type="submit" id="crearLessons" value="Crear" class="btn btn-naranja"><spring:message code="etiq.txtcreate"/></button>
                            </form:form>
                        </div>
                </div>
            </div>
            </div>
        </div>
        <div class="container">
            <!--<button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Open Modal</button>

            <!-- Modal -->
            <div class="modal fade" id="myModal" role="dialog">
                <div class="modal-dialog">
    
                    <!-- Modal content-->
                    <div class="modal-content" id="modal">
                        
                    </div>
                </div>
            </div>
  
        </div>
    </body>
</html>
