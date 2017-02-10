<%-- 
    Document   : homepage
    Created on : 24-ene-2017, 12:12:45
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    <script src="recursos/js/language/es-ES.js" type="text/javascript"></script>
    <script src="recursos/js/language/ar-SA.js" type="text/javascript"></script>

    
    <script type="text/javascript">
        
        var calendar = $("#calendar").calendar(
            {
                
            });     
            
//            Clases de eventos
//            importante: event-important
//                        event-warning
            
    </script>
    </head>
    <body>
<%--        <h1><c:out value="${message}"/></h1>
        <a href="createlesson.htm?select=start">Create Lessons</a>--%>
        <div class="col-xs-6">


            <div class="col-xs-2 pull-right">
                <div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="createlesson.htm?select=start">
                               <button type="submit" id="crearLessons" value="Crear" class="btn btn-success"><spring:message code="etiq.txtcreatedate"/></button>
                            </form:form>
                        </div>
                </div>
            </div>
            <div class="col-xs-2 pull-right">
                <div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="createlesson.htm?select=start">
                                <button type="submit" id="crearLessons" value="Crear" class="btn btn-success" disabled="true"><spring:message code="etiq.txtcreatestudent"/></button>
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
        <div class="col-xs-6">
                            
        
        <div class="page-header row">
            <div class="col-xs-4">
                <h3><spring:message code="etiq.txtcalendar"/> 2016-2017</h3>
		<small><spring:message code="etiq.txtstudentlessons"/> ....... </small>
            </div>
            <div class="col-sm-8">
                <div class="pull-right form-inline">
                    <div class="btn-group">
                            <button class="btn btn-primary" data-calendar-nav="prev"><< <spring:message code="etiq.txtprevious"/></button>
                            <button class="btn" data-calendar-nav="today"><spring:message code="etiq.txttoday"/></button>
                            <button class="btn btn-primary" data-calendar-nav="next"><spring:message code="etiq.txtnext"/> >></button>
                    </div>
                    <div class="btn-group">
                            <button class="btn btn-warning" data-calendar-view="year"><spring:message code="etiq.txtyear"/></button>
                            <button class="btn btn-warning active" data-calendar-view="month"><spring:message code="etiq.txtmonth"/></button>
                            <button class="btn btn-warning" data-calendar-view="week"><spring:message code="etiq.txtweek"/></button>
                            <button class="btn btn-warning" data-calendar-view="day"><spring:message code="etiq.txtday"/></button>
                    </div>
                </div>
            </div>
            <div class="col-xs-12">
            <select id="language" class="span12">
					<option value="en-US" select>Seleccione el idioma (por : en-US)</option>
					<option value="ar-SA">Arabe</option>
					<option value="nl-NL">Dutch</option>
					<option value="fr-FR">French</option>
					<option value="de-DE">German</option>
					<option value="el-GR">Greek</option>
					<option value="hu-HU">Hungarian</option>
					<option value="id-ID">Bahasa Indonesia</option>
					<option value="it-IT">Italian</option>
					<option value="pl-PL">Polish</option>
					<option value="pt-BR">Portuguese (Brazil)</option>
					<option value="ro-RO">Romania</option>
					<option value="es-CO">Spanish (Colombia)</option>
					<option value="es-MX">Spanish (Mexico)</option>
					<option value="es-ES">Spanish (Spain)</option>
					<option value="ru-RU">Russian</option>
					<option value="sk-SR">Slovak</option>
					<option value="sv-SE">Swedish</option>
					<option value="zh-CN">简体中文</option>
					<option value="zh-TW">繁體中文</option>
					<option value="ko-KR">한국어</option>
					<option value="th-TH">Thai (Thailand)</option>
				</select>
        </div>
	</div>
        <div class="col-xs-12">
            <div id="calendar"></div>
        </div>
        
    </div>
    <script type="text/javascript" src="recursos/js/vendor/underscore-min.js"></script>
    <script type="text/javascript" src="recursos/js/calendar.js"></script>
    <script type="text/javascript" src="recursos/js/app.js"></script>
    <script type="text/javascript">
        
        var calendar = $("#calendar").calendar(
            {
                
            });     
            
//            Clases de eventos
//            importante: event-important
//                        event-warning
            
    </script>
    </body>
</html>
