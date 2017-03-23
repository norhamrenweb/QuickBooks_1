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
        <title>Students</title>
        
        <link href="recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
      
        <link href="recursos/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
        <link href="recursos/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="recursos/css/bootstrap-toggle.css" rel="stylesheet" type="text/css"/>
        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        
        <script src="recursos/js/bootstrap.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-toggle.js" type="text/javascript"></script>
<!--        <script src="recursos/js/bootstrap-modal.js" type="text/javascript"></script>-->
        <script src="recursos/js/moment.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script src="recursos/js/es.js" type="text/javascript"></script>
        <script src="recursos/js/ar.js" type="text/javascript"></script>
        
        

 <link href="recursos/css/dataTables/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css"/>

    <link href="recursos/css/dataTables/dataTables.foundation.css" rel="stylesheet" type="text/css"/>

    <link href="recursos/css/dataTables/dataTables.jqueryui.css" rel="stylesheet" type="text/css"/>

    <link href="recursos/css/dataTables/dataTables.semanticui.css" rel="stylesheet" type="text/css"/>

<link href="recursos/css/dataTables/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>
    <link href="recursos/css/dataTables/jquery.dataTables_themeroller.css" rel="stylesheet" type="text/css"/>
    
    
    <script src="recursos/js/dataTables/dataTables.bootstrap.js" type="text/javascript"></script>

    <script src="recursos/js/dataTables/dataTables.bootstrap4.js" type="text/javascript"></script>

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

        <script>



 $(document).ready(function(){
             table = $('#table_students').DataTable(
                 {
                   "paging":   false,
                        "ordering": false,
                        "info":     false
                });
    $('#table_students tbody').on('click', 'tr', function () {
        
        data = table.row( this ).data();
        data1 = data[0];
        selectionStudent();
    } ); 
                
    });            
            
      
        
        var ajax;

    function funcionCallBackStudent()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("origen").innerHTML= ajax.responseText;
                    }
                }
            }
    
     function funcionCallBackSelectStudent()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("showinformation").innerHTML= ajax.responseText;
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
        var seleccion = document.getElementById("levelStudent").value;
        var alumnos = document.getElementById("destino").innerHTML;
        ajax.open("POST","progressbystudent.htm?option=studentlistLevel&seleccion="+seleccion,true);
        ajax.send("");
    }
    
  
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
        ajax.onreadystatechange = funcionCallBackSubject;
        var seleccion1 = document.getElementById("level").value;
        ajax.open("POST","progressbystudent.htm?option=subjectlistLevel&seleccion1="+seleccion1,true);
        
        ajax.send("");
       
    }
 
    function selectionStudent()
    {
        var selectStudent = data1;
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

        //$('#createOnClick').attr('disabled', true);
        ajax.onreadystatechange = funcionCallBackSelectStudent;
        //var selectStudent = document.getElementsByClassName("informationStudent").value;
        ajax.open("POST","progressbystudent.htm?option=studentPage&selectStudent="+selectStudent,true);
        
        ajax.send("");
       
    }
$(function() {
    $('#subject').change(function() {
//        $('#LoadTemplates').parent().attr("disabled",false);
//        $('#LoadTemplates').attr("disabled",false);
        $('#LoadTemplates').children().removeClass("disabled");
    });
    
    $('#LoadTemplates').change(function() {
         if ($("input:radio[name='options']:checked").val() === 'option1' ){
    $("#lessons").attr("disabled", true);
    $('#divCrearLessons').removeClass('hidden');
    $('#divLoadLessons').addClass('hidden');
//    $("#NameLessons").attr("disabled", true);
    } else {
    $("#lessons").attr("disabled", false);
    $('#divLoadLessons').removeClass('hidden');
    $('#divCrearLessons').addClass('hidden');
//    $("#NameLessons").attr("disabled", false);
    }
    });
//    $('#LoadTemplates').change(function() {
//         if (this.checked).{
//    $("#lessons").attr("disabled", true);
//    $('#divCrearLessons').removeClass('disabled');
//    $('#divLoadLessons').addClass('disabled');
////    $("#NameLessons").attr("disabled", true);
//    } else {
//    $("#lessons").attr("disabled", false);
//    $('#divLoadLessons').removeClass('disabled');
//    $('#divCrearLessons').addClass('disabled');
////    $("#NameLessons").attr("disabled", false);
//    }
//    });
})
        </script>
        <style>
            textarea 
            {
            resize: none;
            }
        </style>
    </head>
    <%@ include file="menu.jsp" %>
    <body>
        
        
        <div class="container">
        <h1 class="text-center">Create Lessons</h1>

        
        <form:form id="formStudents" method ="post" action="studentpage.htm?option=studentPage" >
      
            <fieldset>
                    <legend>Select students</legend>
                    <div class="col-xs-12">
                        <div class="col-xs-2">
                            <label>Filter</label>
                        </div>
                        <div class="col-xs-3">
                            
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="col-xs-2">
                            <select class="form-control" name="levelStudent" id="levelStudent" style="width: 100% !important;" onchange="comboSelectionLevelStudent()">

                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels.id[0]}">${levels.name}</option>
                                </c:forEach>
                            </select>
                        </div>
<%--                        <div class="col-xs-3">
                            <select class="form-control" size="20" name="origen[]" id="origen" style="width: 100% !important;">
                                <c:forEach var="alumnos" items="${listaAlumnos}">
                                    <option value="${alumnos.id_students}" >${alumnos.nombre_students}</option>
                                </c:forEach>
                            </select>
                        </div>--%>
                        <div class="col-xs-3 panel" style="overflow-y: scroll; height: 90%">
                            <table id="table_students" class="display" >
                               <thead>
                        <tr>
                            <td hidden=""></td>
                            <td>Name students</td>
                        </tr>
                               </thead>
                        <c:forEach var="alumnos" items="${listaAlumnos}" >
                        <tr>
                            <td hidden="">${alumnos.id_students}</td>
                            <td >${alumnos.nombre_students}</td>
                        </tr>
                        </c:forEach>
                            </table>
                               
                        </div>
                        <div class="col-xs-7" id="">
                            <div class="col-xs-12 text-center">Information Student</div>
                            <div class="col-xs-6">
                            <img src="ruta">
                            </div>
                            <div class="col-xs-6 ">
                                <table >
                                    <tr>
                                        <td>
                                            <select id="showinformation">
                                                <option>${student.nombre_students}</option>
                                                <option>${student.fecha_nacimiento}</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                                <label class="label">Name</label>
                                <input class="form-control" type="text" value="">
                            
                            <label>lastname</label>
                            <input class="form-control" type="text" readonly="" value="lastname student">
                            
                            <label>Birthday</label>
                            <input class="form-control" type="text" readonly="" value="Birthday student">
                            
                            <label>grade level</label>
                            <input class="form-control" type="text" readonly="" value="grade student">
                            
                            <Label>Subjec</Label>
                            <select class="form-control" >
                                <option>Subject1</option>
                                <option>Subject2</option>
                                <option>Subject1</option>
                            </select>
                            </div>
                        
                        </div>
                    </div>
                        
            </fieldset>
            <div class="col-xs-12 text-center">
            <input type="submit" class="btn btn-success" id="createOnClick" value="<spring:message code="etiq.txtcreate"/>">
        </div>
 <div class="col-xs-12">   
                    <input type="submit" value="Save">
                </div>  
        </form:form>
        
        </div>
<%--        <div class="col-xs-6">
                <div class="form-group">
                    <label class="control-label"></label>
                        <div class='input-group' style="margin-top:19px;">
                            <form:form id="formCreate" action="createsetting.htm?select=start">
                               <button type="submit" id="crearLessons" value="Crear" class="btn btn-success">Create Settings</button>
                            </form:form>
                        </div>
                </div>
        </div>--%>
<!--<div class="col-xs-12">
<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
  Launch demo modal
</button>
</div>-->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
<!--        <h4 class="modal-title" id="myModalLabel">Modal title</h4>-->
      </div>
      <div class="modal-body text-center">
       <H1><%= request.getParameter("message") %></H1>
      </div>
<!--      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>-->
    </div>
  </div>
</div>



    </body>
</html>
