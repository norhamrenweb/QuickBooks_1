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
        <title>Home</title>

    <script type="text/javascript">
    

    
    $(document).ready( function () {
        $('#table_id').DataTable({
        "aLengthMenu": [[5, 10, 20, -1], [5, 10, 20, "All"]],
        "iDisplayLength": 5,
        "columnDefs": [
            {
                "targets": [ 0 ],
                "visible": false,
                "searchable": false
            }]
    });
        $('#table_datelessons').DataTable();
       
//    $('#table_id tbody').on('click', 'tr', function () {
//        table = $('#table_id').DataTable();
//        data = table.row( this ).data();
//        data1 = data[0];
//        rowselect();
//    } ); 
} ); 
//   
var ajax;
 function funcionCallBackdetailsLesson()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                   var json = JSON.parse(ajax.responseText);
                  
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
                        ordering: false,
                        data: json,
                        
                        columns: [
                        { data: 'col1' },
                        { data: 'col2' },
                        { data: 'col3' },
                        { data: 'col4' },
                        { data: 'col5' }
                        ],
                        columnDefs: [
                        { width: 90, targets: 0 },
                        { width: 200, targets: 0 },
                        { width: 200, targets: 0 },
                        { width: 70, targets: 0 },
                        { width: 100, targets: 0 }
                        ]
                    } );
                    
                        //var tableObjective = $('#tableobjective').DataTable();

                        $.each(json, function(i, item) {
//                         var commentgeneral = $('#tableobjective tbody tr td:eq(2)').text();
                        $('#tableobjective tbody tr:eq('+ i +') td:eq(2)').empty();
                        $('#tableobjective tbody tr:eq('+ i +') td:eq(2)').append("<div class='input-group'>\n\
                <textarea rows='2' class='form-control commentGeneral' id='comment"+item.col5+"'>"+item.col3+"</textarea>\n\
<span class='input-group-btn'>\n\
<button type='button' class='btn btn-default btn-xs' value='"+item.col5+"' onclick='saveGeneralComment("+item.col5+")'>save</button>\n\
</span></div>");
//                        if(item.col4 === currentTime ){
//                        $('#tableobjective tbody tr:eq('+ i +') td:eq(3)').append("<div class='input-group'>"+currentTime+"</div>");
//                        }
                        $('#tableobjective tbody tr:eq('+ i +') td:eq(4)').empty();
                        $('#tableobjective tbody tr:eq('+ i +') td:eq(4)').append("<button type='button' class='btn-unbutton' value='"+item.col5+"' onclick='selectionObjective("+item.col5+")'>More details</button>");
//                        $('#tableobjective tbody tr:eq('+ i +') td:eq(4)').text("more details");
                        });
//                        var commentgeneral = $('#tableobjective tbody tr td:eq(2)').text();
//                        $('#tableobjective tbody tr td:eq(2)').empty();
//                        $('#tableobjective tbody tr td:eq(2)').append("<input value='"+commentgeneral+"'></input>");   
                           
                         
//     $('#tableobjective tbody tr td:eq(4)').on('click', 'tr', 'td:eq(4)', function () {
//        
//        var dataObjective = tableObjective.row( this ).data();
//        dataObjective1 = dataObjective['col5'];
//        selectionObjective();
//    } ); 
                    }
                }
            }
   function rowselect(LessonsSelected)
    {
        //ESTO PARA PINCHAR EN LA FILAvar LessonsSelected = data1;
        //var LessonsSelected = $(data1).html();
        //var LessonsSelected = 565;

        
        
//        if (window.XMLHttpRequest) //mozilla
//        {
//            ajax = new XMLHttpRequest(); //No Internet explorer
//        }
//        else
//        {
//            ajax = new ActiveXObject("Microsoft.XMLHTTP");
//        }
        
//ajax.onreadystatechange=funcionCallBackLessonsprogress;
        window.location.href = "lessonprogress.htm?select6=loadRecords&LessonsSelected="+LessonsSelected;
//        ajax.open("POST","lessonprogress.htm?select6=loadRecords&LessonsSelected="+LessonsSelected,true);
//        ajax.send("");
  };
   function detailsSelect(LessonsSelected)
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        ajax.onreadystatechange = funcionCallBackdetailsLesson;
        ajax.open("POST","homepage.htm?select3=detailsLesson&LessonsSelected="+LessonsSelected,true);
        ajax.send("");
  };
    
      
    
    </script>
    <style>
        .title
        {
            font-weight: bold;
            color: gray;
            margin-top: 5px;
            padding-left: 5px;
        }
    </style>
    </head>
    <body>
        <div class="col-xs-12">
            <div class="col-sm-12" id="maincontainer">
                <div class="col-sm-12 center-block text-center">
                    <h2><spring:message code="etiq.txtactivities"/></h2>
                </div>
            </div>
            <div class="container">
                <table id="table_id" class="display" >
                    <thead>
                        <tr>
                            <td>id</td>
                            <td>Lesson Name</td>
                            <td>Grade Level</td>
                            <td>Subject</td>
                            <td>Objective</td>
                            
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
                            <td>${lecciones.objective.name}</td>
                            <td>${lecciones.date}</td>
                            <td>${lecciones.start}</td>
                            <td>${lecciones.finish}</td>
                            <td>
                                <div class="col-xs-3">
                                    <input name="TXTid_lessons_attendance" class="btn-unbutton" type="image" src="recursos/img/btn/btn_Attendance.svg" value="${lecciones.id}" id="attendance" onclick="rowselect(${lecciones.id})" width="40px" data-placement="bottom" title="Attendance">
                                </div>
                                <div class="col-xs-3">
                                    <input name="TXTid_lessons_detalles" type="image" src="recursos/img/btn/btn_details.svg" value="${lecciones.id}" id="details" onclick="detailsSelect(${lecciones.id})" data-toggle="modal" data-target="#detailsLesson" width="40px" data-placement="bottom" title="Details">
                                </div>
                                <div class="col-xs-3">
                                    <input name="TXTid_lessons_modificar" type="image" src="recursos/img/btn/btn_Edit.svg" value="${lecciones.id}" id="modify" onclick="modifySelect(${lecciones.id})" width="40px" data-placement="bottom" title="Modify">
                                </div>
                                <div class="col-xs-3">
                                    <input name="TXTid_lessons_eliminar" type="image" src="recursos/img/btn/btn_delete.svg" value="${lecciones.id}" id="delete" onclick="delteSelect(${lecciones.id})" data-toggle="modal" data-target="#deleteLesson" width="40px" data-placement="bottom" title="Delete">
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
           
            </div>
        </div>
<!-- Modal delete-->
<div id="detailsLesson" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Details {Name Lessons}</h4>
      </div>
        <div class="modal-body">
            <div class="container-fluid">
                <div class="col-xs-6">
                    <div class="row">
                        Students
                    </div>
                    <div class="row studentarea">
                    <table class="table table-striped">
                        <tr>
                            <td>
                                Amani,Moosajee
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Iman,Abrahams	
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Sadhvi,Dayaram
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Micah,Chingaya	
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                         <tr>
                            <td>
                                Carla,Oloff
                            </td>
                        </tr>
                    </table>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="row title">
                        Method
                    </div>
                    <div class="row">
                        {Method Lessons}
                    </div>
                    <div class="row title">
                        Content 
                    </div>
                    <div class="row">
                        {Content Lessons}
                    </div>
                    <div class="row title">
                        Objective:   
                    </div>
                    <div class="row">
                        Learn ryhtmes
                    </div>
                </div>
            </div>
        </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
<!-- Modal delete-->
<div id="deleteLesson" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Modal Header</h4>
      </div>
      <div class="modal-body">
        <p>Some text in the modal.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
    </body>
</html>
