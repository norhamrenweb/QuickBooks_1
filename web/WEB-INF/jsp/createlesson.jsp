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
        <title>Create Lessons</title>
        
        <link href="recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
      
        <link href="recursos/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
        <link href="recursos/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        
        <script src="recursos/js/moment.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script src="recursos/js/es.js" type="text/javascript"></script>
        <script src="recursos/js/ar.js" type="text/javascript"></script>

        <script>
 $(document).ready(function(){
       var userLang = navigator.language || navigator.userLanguage;
       var myDate = new Date();
         //Muestra calendario
  
        $('#datetimepickerinicio').datetimepicker({
            
            format: 'DD-MM-YYYY HH:mm',
            locale: userLang.valueOf(),
            daysOfWeekDisabled: [0, 6],
            useCurrent: false,//Important! See issue #1075
            //defaultDate: '08:32:33',
            enabledHours: [8,9,10,11,12,13,14,15,16],
            sideBySide: true,
            stepping: 5
  
        });
        $('#datetimepickerfin').datetimepicker({
            
            format: 'DD-MM-YYYY HH:mm',
            locale: userLang.valueOf(),
            daysOfWeekDisabled: [0, 6],
            useCurrent: false //Important! See issue #1075
        });
        
        $("#datetimepickerinicio").on("dp.change", function (e) {
            $('#datetimepickerfin').data("DateTimePicker").minDate(e.date);
        });
        
        $("#datetimepickerfin").on("dp.change", function (e) {
            $('#datetimepickerinicio').data("DateTimePicker").maxDate(e.date);
        });
        
    
//       //Menu lateral
//        $('#nav-expander').on('click',function(e){
//      		e.preventDefault();
//      		$('body').toggleClass('nav-expanded');
//      	});
//      	$('#nav-close').on('click',function(e){
//      		e.preventDefault();
//      		$('body').removeClass('nav-expanded');
//      	});
//        $('#barralateral').mouseleave(function(o){
//      		o.preventDefault();
//      		$('body').removeClass('nav-expanded');
//      	});
 
     
    });            
            
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
        ajax.open("POST","createlesson.htm?select=studentlistLevel&seleccion="+seleccion,true);
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
//        ajax.open("GET","createlesson.htm?select=cargalistasubject&seleccion1="+seleccion1,true);
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
        ajax.open("POST","createlesson.htm?select=subjectlistLevel&seleccion1="+seleccion1,true);
        ajax.send("");
    }
        </script>
        <style>
            .sep
        </style>
    </head>
    <body>
        
        
        <div class="container">
        <h1 class="text-center">Create Lessons</h1>

        
        <form:form id="formStudents" method ="post" action="createlesson.htm?select=createlesson" >
            <fieldset>
                <legend>Options Date</legend>
                <div class="form-group">
                    <div class="row">

                        <div class='col-xs-4'>
                            <div class="form-group">
                                <label class="control-label" for="fechainicio"><spring:message code="etiq.txtstartdate"/></label>
                                <div class='input-group date' id='datetimepickerinicio'>
                                    <input type='text' name="TXTfechainicio" class="form-control" id="fechainicio" required="required"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class='col-xs-4'>
                            <div class="form-group">
                                <label class="control-label" for="fechafin"><spring:message code="etiq.txtenddate"/></label>
                                <div class='input-group date' id='datetimepickerfin'>
                                    <input type='text' name="TXTfechafin" class="form-control" required="required"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
 
                </div>
            </fieldset>
            <fieldset>
                <legend>Options Lessons</legend>
                <div class="col-xs-12 form-group">
                    <label class="control-label"><spring:message code="etiq.namelessons"/></label>
                    <input type="text" class="input-sm" name="TXTnombreLessons" required="" placeholder="<spring:message code="etiq.namelessons"/>">
                </div>

                <div class="col-xs-3 form-group">
                    <label class="control-label"><spring:message code="etiq.txtlevels"/></label>
                    <select class="form-control" name="levelsubject" id="levelsubject" onchange="comboSelectionLevel()">
                        
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
                </div>
                <div class="col-xs-3 center-block">
                    <label class="control-label"><spring:message code="etiq.txtsubsection"/></label>
                    <select class="form-control" id="idsubsection" name="TXTsubsection" onchange="comboSelectionSubsection()">
                       <c:forEach var="subsection" items="${subsection}">
                                <option value="${subsection}" >${subsection}</option>
                            </c:forEach>
                    </select>
                </div>
                <div class="col-xs-3 center-block">
                    <label class="control-label"><spring:message code="etiq.txtequipment"/></label>
                    <select class="form-control" id="equipment" name="TXTequipment" multiple onchange="comboSelectionEquipment()">
                       <c:forEach var="equipment" items="${equipment}">
                                <option value="${equipment}" >${equipment}</option>
                            </c:forEach>
                    </select>
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
                                <option selected value="allstudents" >All students</option>
                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels}" >${levels}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <select class="form-control" size="20" multiple name="origen[]" id="origen" style="width: 100% !important;">
                                <c:forEach var="alumnos" items="${listaAlumnos}">
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
