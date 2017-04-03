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
        
        <link href="/QuickBooks_1/recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
      
        <link href="/QuickBooks_1/recursos/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
        <link href="/QuickBooks_1/recursos/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="/QuickBooks_1/recursos/css/bootstrap-toggle.css" rel="stylesheet" type="text/css"/>
        <script src="/QuickBooks_1/recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        
        <script src="/QuickBooks_1/recursos/js/bootstrap.js" type="text/javascript"></script>
        <script src="/QuickBooks_1/recursos/js/bootstrap-toggle.js" type="text/javascript"></script>
<!--        <script src="recursos/js/bootstrap-modal.js" type="text/javascript"></script>-->
        <script src="/QuickBooks_1/recursos/js/moment.js" type="text/javascript"></script>
        <script src="/QuickBooks_1/recursos/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script src="/QuickBooks_1/recursos/js/es.js" type="text/javascript"></script>
        <script src="/QuickBooks_1/recursos/js/ar.js" type="text/javascript"></script>
        
        

 <link href="/QuickBooks_1/recursos/css/dataTables/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css"/>

    <link href="/QuickBooks_1/recursos/css/dataTables/dataTables.foundation.css" rel="stylesheet" type="text/css"/>

    <link href="/QuickBooks_1/recursos/css/dataTables/dataTables.jqueryui.css" rel="stylesheet" type="text/css"/>

    <link href="/QuickBooks_1/recursos/css/dataTables/dataTables.semanticui.css" rel="stylesheet" type="text/css"/>

<link href="/QuickBooks_1/recursos/css/dataTables/jquery.dataTables.min.css" rel="stylesheet" type="text/css"/>
    <link href="/QuickBooks_1/recursos/css/dataTables/jquery.dataTables_themeroller.css" rel="stylesheet" type="text/css"/>
    
    
    <script src="/QuickBooks_1/recursos/js/dataTables/dataTables.bootstrap.js" type="text/javascript"></script>

    <script src="/QuickBooks_1/recursos/js/dataTables/dataTables.bootstrap4.js" type="text/javascript"></script>

    <script src="/QuickBooks_1/recursos/js/dataTables/dataTables.foundation.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.foundation.min.js" type="text/javascript"></script>-->
    <script src="/QuickBooks_1/recursos/js/dataTables/dataTables.jqueryui.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.jqueryui.min.js" type="text/javascript"></script>-->
    <script src="/QuickBooks_1/recursos/js/dataTables/dataTables.material.js" type="text/javascript"></script>
<!--    <script src="recursos/js/dataTables/dataTables.material.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.semanticui.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.semanticui.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.uikit.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/dataTables.uikit.min.js" type="text/javascript"></script>-->
    <script src="/QuickBooks_1/recursos/js/dataTables/jquery.dataTables.js"></script>
<!--    <script src="recursos/js/dataTables/jquery.dataTables.min.js" type="text/javascript"></script>-->
<!--    <script src="recursos/js/dataTables/jquery.js" type="text/javascript"></script>-->

        <script>



 $(document).ready(function(){
             table = $('#table_students').DataTable(
                {
                    "searching": false,
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
                    var json = JSON.parse(ajax.responseText);
                     var info = JSON.parse(json.info);
               var subjects = JSON.parse(json.sub);
                    $('#BOD').val(info.fecha_nacimiento);
                   $('#subjects').empty();
                     $.each(subjects, function(i, item) {
                         $('#subjects').append('<option>' + subjects[i].name + '</option>');
                   });
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
      //  var selectStudent = document.getElementsByClassName("nameStudent").value;
        ajax.open("POST","studentPage.htm?selectStudent="+selectStudent,true);
        
        ajax.send("");
       
    }
    function loadobjGeneralcomments()
    {  
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
       var selectSubject = document.getElementsByClassName("subjects").value; 
        ajax.open("POST","objGeneralcomments.htm?selectSubject="+selectSubject,true);
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
})
</script>
        <style>
            textarea 
            {
            resize: none;
            }
            .studentarea
            {            
            height: 500px;
            width: 100%;
            overflow-y: scroll;
            }
            .nameStudent
            {
            background-color: #D0D2D3;
            border-radius: 10px;
            margin-top: 20px;
            margin-bottom: 20px;
            padding-top: 10px;
            padding-bottom: 10px;
            }
            .tab-pane
            {
                padding-top: 20px;
            }
            .sinpadding
            {
                padding: 0px;
            }
            .borderRight
            {
                border-right: 1px #D0D2D3 double;
            }
        </style>
    </head>
    <%@ include file="menu.jsp" %>
    <body>
        
        
        <div class="container">
        <h1 class="text-center">Progress by Student</h1>

        
        <form:form id="formStudents" method ="post" action="studentpage.htm?option=studentPage" >
      
            <fieldset>
<!--                    <legend>Select student</legend>-->
                    <div class="col-xs-3">
                        <div class="col-xs-12">
                            <label>Filter</label>
                            <select class="form-control" name="levelStudent" id="levelStudent" style="width: 100% !important;" onchange="comboSelectionLevelStudent()">
                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels.id[0]}">${levels.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-12">
                            <label>By name</label>
                            <input class="form-control" name="nameStudent" id="nameStudent" style="width: 100% !important;" onchange="comboSelectionnameStudent()">
                        </div>
                        <div class="col-xs-12 studentarea">
                            <table id="table_students" class="display" >
                                <thead>
                                <tr>
                                    <td hidden=""></td>
                                    <td hidden="">Name students</td>
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
                    </div>    
                    <div class="col-xs-9" id="">
                        <div class="col-xs-12 text-center nameStudent">
                            <span>Jesús Aragón Gallego${student.nombre_students}</span>
                        </div>
                        <div class="col-xs-12 text-center">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#demographic">Demographic</a></li>
                                <li><a data-toggle="tab" href="#gradebook">Gradebook</a></li>
                                <li><a href="#">ReportCard</a></li>
<!--                                <li><a href="#">Menu 3</a></li>-->
                            </ul>
                        </div>
                        <div class="tab-content">
                            <div class="col-xs-12 tab-pane fade in active" id="demographic">
                                <div class="col-xs-6 text-center borderRight">
                                    <img src="recursos/img/Foto dibujo.jpg" alt='img' width="150px;"/>
                                </div>
                                <div class="col-xs-6">
                                    <div class="col-xs-12 sinpadding">
                                        <div class="col-xs-12">Grade:</div>
                                        <div class="col-xs-12">Grade4</div>
                                    </div>         
                                    <label class="label">Name</label>
                                    <input class="form-control" type="text" value="">

                                <label>lastname</label>
                                <input class="form-control" type="text" readonly="" value="lastname student">

                                <label>Birthday</label>
                                <input class="form-control" type="text" readonly="" value="Birthday student" id="BOD">

                                <label>grade level</label>
                                <input class="form-control" type="text" readonly="" value="grade student">
                                </div>
                            </div>
                            <div class="col-xs-12 tab-pane fade" id="gradebook">
<!--                                <div class="col-xs-6">
                                <Label>Level</Label>
                                <select class="form-control">
                                    <option>Level1</option>
                                    <option>Level2</option>
                                    <option>Level3</option>
                                </select>
                                </div>-->
                                <div class="col-xs-6" >
                                    <Label>Subject</Label>
                                    <select class="form-control" id="subjects" onchange="loadobjGeneralcomments()">
                                        
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
        </fieldset>
    </form:form> 
</div>

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
