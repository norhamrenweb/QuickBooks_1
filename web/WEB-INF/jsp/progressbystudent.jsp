<%-- 
    Document   : createlesson
    Created on : 30-ene-2017, 14:59:17
    Author     : nmohamed
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<!DOCTYPE html>
<html>
    <%@ include file="menu.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Students</title>
        
     


        <script>



 $(document).ready(function(){
            $('#tableobjective').DataTable();
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
    function funcionCallBackloadGeneralcomments()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                   var json = JSON.parse(ajax.responseText);
                  
                   //var i;
                    if(json.length === 0){
                        $('#divTableObjective').addClass('hidden');
                        $('#divNotObjective').removeClass('hidden');
                        
                    }else{
                        $('#divNotObjective').addClass('hidden');
                        $('#divTableObjective').removeClass('hidden');
                    };
                   $('#tableobjective').DataTable( {
                        destroy: true,
                        paging: true,
                        searching: false,
                        ordering: true,
                        data: json,
                        
                        columns: [
                        { data: 'col1' },
                        { data: 'col2' },
                        { data: 'col3' },
                        { data: 'col4' },
                        { data: 'col5' }
                        ]
                    
                    } );
                           var tableObjective = $('#tableobjective').DataTable();
     $('#tableobjective tbody').on('click', 'tr', function () {
        
        var dataObjective = tableObjective.row( this ).data();
        dataObjective1 = dataObjective['col5'];
        selectionObjective();
    } ); 
//                          var table="<tr><th>Objective Name</th><th>Objective description</th><th>Comment</th><th>Comment Date</th><th>ID</th></tr>";
////                          var x = xmlDoc.getElementsByTagName("CD");
//                          for (i = 0; i <json.length; i++) { 
//                            table += "<tr><td>" +
//                           json[i].col1 +
//                            "</td><td>" +
//                            json[i].col2 +
//                            "</td><td>" +
//                            json[i].col3 +
//                            "</td><td>" +
//                            json[i].col4 +
//                            "</td><td>" +
//                            json[i].col5 +
//                            "</td></tr>";
//                          }
                          //document.getElementById("tableobjective").innerHTML = table;
                          //$('#tableobjective').DataTable(); 
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
                    $('#student').text(info.nombre_students);
                    $('#studentid').val(info.id_students);
                    if(typeof info.foto === 'undefined'){
                        $('#foto').attr('src', '../recursos/img/NotPhoto.png');
                    }else{
                        $('#foto').attr('src', info.foto);
                    }
                    $('#subjects').empty();
                     $.each(subjects, function(i, item) {
                         $('#subjects').append('<option value= "'+subjects[i].id+'">' + subjects[i].name + '</option>');
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
    
    function selectionObjective()
    {
        var selectObjective = dataObjective1;
//        if (window.XMLHttpRequest) //mozilla
//        {
//            ajax = new XMLHttpRequest(); //No Internet explorer
//        }
//        else
//        {
//            ajax = new ActiveXObject("Microsoft.XMLHTTP");
//        }

        //$('#createOnClick').attr('disabled', true);
        //ajax.onreadystatechange = funcionCallBackSelectStudent;
      //  var selectStudent = document.getElementsByClassName("nameStudent").value;
        window.location.href = "progressdetails.htm";
        
        //ajax.send("");
       
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
//        var selectSubject = document.getElementById("subjects").value; 
//       var selectStudent = document.getElementById("studentid").value;
//        var d = { selectSubject:selectSubject, studentid:selectStudent};
//                $.ajax({
//            type: 'GET',
//            url: 'objGeneralcomments.htm',
//            contentType: 'application/json; charset=utf-8',
//            data: JSON.stringify(d),
//            dataType: 'json',
//            success: funcionCallBackloadGeneralcomments
//          
//        });
        ajax.onreadystatechange = funcionCallBackloadGeneralcomments;
        
       var selectSubject = document.getElementById("subjects").value; 
       var selectStudent = document.getElementById("studentid").value;
        ajax.open("POST","objGeneralcomments.htm?selection="+selectSubject+","+selectStudent,true);
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
            .containerPhoto
            {
                display: table;
                background-color: #d9edf7;
                border-right: 1px #D0D2D3 double;
                min-height: 300px;
            }
            .cell{
                display: table-cell;
                vertical-align: middle;
            }
            #divTableObjective{
                margin-top: 20px;
            }
        </style>
    </head>

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
                    <div class="col-xs-9">
                        <div class="col-xs-12 text-center nameStudent">
                            <span id="student"> </span>
                            <input type="hidden" id="studentid" name="studentid">
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
                                <div class="col-xs-6 text-center containerPhoto">
                                    <div class="cell">
                                        <img id="foto" src="../recursos/img/NotPhoto.png" alt='img' width="150px;"/>
                                    </div>
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
                                <div class="col-xs-12 hidden" id="divNotObjective">
                                    This Subject have not objectives
                                </div>
                                <div class="col-xs-12 hidden" id="divTableObjective">
                                    <table id="tableobjective" class="display">
                                        <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Description</th>
                                                <th>Comment general</th>
                                                <th>date</th>
                                                <th>id</th>
                                            </tr>
                                        </thead> 
                                    </table>
                                    
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
