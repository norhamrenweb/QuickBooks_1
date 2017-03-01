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
    <%@ include file="menu.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    <script src="recursos/js/language/es-ES.js" type="text/javascript"></script>
    <script src="recursos/js/language/ar-SA.js" type="text/javascript"></script>
  
<!--    <link href="recursos/css/dataTables/dataTables.bootstrap.css" rel="stylesheet" type="text/css"/>-->
 <link href="recursos/css/dataTables/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css"/>
<!--    <link href="recursos/css/dataTables/dataTables.bootstrap4.css" rel="stylesheet" type="text/css"/>-->
<!--    <link href="recursos/css/dataTables/dataTables.bootstrap4.min.css" rel="stylesheet" type="text/css"/>-->
    <link href="recursos/css/dataTables/dataTables.foundation.css" rel="stylesheet" type="text/css"/>
<!--    <link href="recursos/css/dataTables/dataTables.foundation.min.css" rel="stylesheet" type="text/css"/>-->
    <link href="recursos/css/dataTables/dataTables.jqueryui.css" rel="stylesheet" type="text/css"/>
<!--    <link href="recursos/css/dataTables/dataTables.jqueryui.min.css" rel="stylesheet" type="text/css"/>-->
<!--    <link href="recursos/css/dataTables/dataTables.material.css" rel="stylesheet" type="text/css"/>-->
<!--    <link href="recursos/css/dataTables/dataTables.material.min.css" rel="stylesheet" type="text/css"/>-->
    <link href="recursos/css/dataTables/dataTables.semanticui.css" rel="stylesheet" type="text/css"/>
<!--    <link href="recursos/css/dataTables/dataTables.semanticui.min.css" rel="stylesheet" type="text/css"/>-->
<!--    <link href="recursos/css/dataTables/dataTables.uikit.css" rel="stylesheet" type="text/css"/>-->
<!--    <link href="recursos/css/dataTables/dataTables.uikit.min.css" rel="stylesheet" type="text/css"/>-->
<!--      <link href="recursos/css/dataTables/jquery.dataTables.css" rel="stylesheet" type="text/css"/>-->
<link href="recursos/css/dataTables/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>
    <link href="recursos/css/dataTables/jquery.dataTables_themeroller.css" rel="stylesheet" type="text/css"/>
    
    
    <script src="recursos/js/dataTables/dataTables.bootstrap.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.bootstrap.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/dataTables.bootstrap4.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.bootstrap4.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/dataTables.foundation.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.foundation.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/dataTables.jqueryui.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.jqueryui.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/dataTables.material.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.material.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.semanticui.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.semanticui.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.uikit.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.uikit.min.js" type="text/javascript"></script>-->
    <script src="recursos/js/dataTables/jquery.dataTables.js"></script>
<!--    <script src="recursos/js/dataTables/jquery.dataTables.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/jquery.js" type="text/javascript"></script>-->
<style>
    .red{
        background-color: red !important;
    }
</style>
    <script type="text/javascript">
    

    
    $(document).ready( function () {
        $('#table_id').DataTable();
        $('#table_datelessons').DataTable();
//       
//    $('#table_id tbody').on('click', 'tr', function () {
//        table = $('#table_id').DataTable();
//        data = table.row( this ).data();
//        data1 = data[0];
//        rowselect();
    } ); 
//    } ); 
//   
//        
//        function funcionCallBackEquipment()
//    {
//           if (ajax.readyState===4){
//                if (ajax.status===200){
//                    document.getElementById("table_datelessons").innerHTML= ajax.responseText;
//                    }
//                }
//            }
// 
//
//
//   function rowselect()
//    {
//        var LessonsSelected = data1;
//        //var LessonsSelected = $(data1).html();
//        //var LessonsSelected = 565;
//
//        
//        
//        if (window.XMLHttpRequest) //mozilla
//        {
//            ajax = new XMLHttpRequest(); //No Internet explorer
//        }
//        else
//        {
//            ajax = new ActiveXObject("Microsoft.XMLHTTP");
//        }
//        
////        
//        ajax.onreadystatechange=funcionCallBackEquipment;
//        ajax.open("POST","homepage.htm?select3=loadLessonsTime&LessonsSelected="+LessonsSelected,true);
//        ajax.send("");
//  };

    
      
    
    </script>
    </head>
    <body>
<%--        <h1><c:out value="${message}"/></h1>
        <a href="createlesson.htm?select=start">Create Lessons</a>--%>
        <div class="col-xs-12">


            <div class="col-xs-6">
                <div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="createlesson.htm?select=start">
                               <button type="submit" id="crearLessons" value="Crear" class="btn btn-success"><spring:message code="etiq.txtcreatedate"/></button>
                            </form:form>
                        </div>
                </div>
            </div>
            <div class="col-xs-6">
                <%--<div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="createlesson.htm?select=start">
                                <button type="submit" id="crearLessons1" value="Crear" class="btn btn-success" disabled="true"><spring:message code="etiq.txtcreatestudent"/></button>
                            </form:form>
                        </div>
                </div>--%>
            </div>
            <div class="col-sm-12" id="maincontainer">
                <div class="col-sm-12 center-block text-center">
                    <h2><spring:message code="etiq.txtactivities"/></h2>
                </div>
            </div>
                
            <div class="col-xs-12">
                <table id="table_id" class="display" >
                    <thead>
                        <tr>
                            <td>id</td>
                            <td><spring:message code="etiq.namelessons"/></td>
                            <td><spring:message code="etiq.levellessons"/></td>
                            <td><spring:message code="etiq.subjectlessons"/></td>
                            <td><spring:message code="etiq.subsectionlessons"/></td>
                            <%--<td><spring:message code="etiq.equipmentlessons"/></td>--%>
                            
                            <td>Date</td>
                            <td>Start Hour</td>
                            <td>End Hour</td>
                            <td><spring:message code="etiq.actionlessons"/></td>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="lecciones" items="${lessonslist}" >
                        <tr>
                            <td>${lecciones.id}</td>
                            <td>${lecciones.name}</td>
                            <td>${lecciones.level.name}</td>
                            <td>${lecciones.subject.name}</td>
                            <td>${lecciones.subsection.name}</td>
                            <td>${lecciones.date}</td>
                            <td>${lecciones.start}</td>
                            <td>${lecciones.finish}</td>
<!--                            <td>equipment</td>-->
                            <td>
                                <div class="col-xs-4">
                                    <button name="TXTid_lessons_detalles" value="" class="btn btn-detalles" id="details" data-target=".bs-example-modal-lg">
                                        <span class="glyphicon glyphicon-list-alt" data-toggle="tooltip" data-placement="bottom" title="Detalles"></span>
                                    </button>
                                </div>
                                <div class="col-xs-4">
                                    <form id="form2" action='modify.htm'>
                                        <button name="TXTid_lessons_modificar" type="submit" class="btn btn-modificar" id="modificarLessons" data-toggle="tooltip" data-placement="bottom" title="modify" >
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </button>
                                    </form>    
                                </div>
                                <div class="col-xs-4">
                                    <button name="TXTid_lessons_eliminar" value="" class="btn btn-eliminar" data-toggle="tooltip" data-placement="bottom" title="Eliminar">
                                        <span class="glyphicon glyphicon-trash"></span>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
           
            </div>

            <%--<<div class="col-xs-6">
                <div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="create.htm?accion=inicio">
                                <button type="submit" id="crearLessons" value="Crear" class="btn btn-naranja"><spring:message code="etiq.txtcreate"/></button>
                            </form:form>
                        </div>
                </div>
</div>--%>

        </div>
        <%--<div class="col-xs-6">
            <div class="col-xs-12" id="maincontainer">
                <div class="col-xs-12 center-block text-center">
                    <h2><spring:message code="etiq.txtactivities"/></h2>
                </div>
            </div>
                <div class="col-xs-12">
                <table id="table_datelessons" class="display">
                    <thead>
                        <tr>
                            <td>Date</td>
                            <td>Hour start</td>
                            <td>Hour end</td>
                            <td><spring:message code="etiq.namelessons"/></td>
                            <td>Students</td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>2017-02-17</td>
                            <td>8:00</td>
                            <td>9:00</td>
                            <td>Estudiantes2</td>
                            <td>Pepe, Manolo, jaime</td>
                        </tr>
                        <tr>
                            <td>2017-02-20</td>
                            <td>8:00</td>
                            <td>9:00</td>
                            <td>Estudiantes2</td>
                            <td>Manolo, jaime</td>
                        </tr>
                        <tr>
                            <td>2017-02-20</td>
                            <td>9:00</td>
                            <td>10:00</td>
                            <td>Estudiantes2</td>
                            <td>Manolo, jaime</td>
                        </tr>
                        <c:forEach var="lecciones1" items="${lessonslist1}" >
                        <tr>
                            <td>fecha</td>
                            <td>Hour start</td>
                            <td>Hour end</td>
                            <td>${lecciones1.name}</td>
                            <td>students</td>
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
           
            </div> --%>
        
<%--        <div class="page-header row">
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
        </div>-->
        
    </div>
<!--    <script type="text/javascript" src="recursos/js/vendor/underscore-min.js"></script>
    <script type="text/javascript" src="recursos/js/calendar.js"></script>
    <script type="text/javascript" src="recursos/js/app.js"></script>
    <script type="text/javascript">
        
        var calendar = $("#calendar").calendar(
            {
                
            });     
            
//            Clases de eventos
//            importante: event-important
//                        event-warning
            
    </script>--%>
    </body>
</html>
