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
   
var ajax;
   
   $(document).ready( function () {
            var userLang = navigator.language || navigator.userLanguage;
              var myDate = new Date();
              $( ".divClass" ).click(function() {


                      var idCLass = $(this).attr('data-idclass');
                      window.location.replace("<c:url value="/gradebook/loadRecords.htm?term=1&ClassSelected="/>"+idCLass);

              });
          //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE


       $('#fecha').datetimepicker({

                  format: 'YYYY-MM',
                  locale: userLang.valueOf(),
                  daysOfWeekDisabled: [0, 6],
                  useCurrent: true//Important! See issue #1075
                  //defaultDate: '08:32:33',

              });

      $('#fecha').on('dp.change', function(e){loadComments(); })
      loadComments();
   });
function deleteSelectSure(deleteLessonsSelected, deleteLessonsName) {

       $('#lessonDelete').empty();
       $('#lessonDelete').append(deleteLessonsName);
       $('#buttonDelete').val(deleteLessonsSelected);
       $('#deleteLesson').modal('show');
}
//  

function funcionCallBackdetailsLesson()
   {
          if (ajax.readyState===4){
               if (ajax.status===200){
                  var object = JSON.parse(ajax.responseText);
                  var s = JSON.parse(object.students);
                  var c =  JSON.parse(object.contents);
                 

                       $('#nameLessonDetails').empty();
                       $('#nameLessonDetails').append('Details '+nameLessons);
                       //$('#detailsStudents').empty();
                       $('#studentarea').append('<table id="detailsStudents" class="table table-striped">');
                       $.each(s, function (i,student){
                           $('#detailsStudents').append('<tr><td class="studentDetails">'+s[i].studentname+'</td></tr>');
                           $("tr:odd").addClass("par");
                           $("tr:even").addClass("impar");
                       //    $("tr:odd").css("background-color", "lightgray");
                       });
                       $('#contentDetails').empty();
                       $.each(c, function (i, content){
                           $('#contentDetails').append('<li>'+c[i]+'</li>');
                       });
                       
                       
                       $('#methodDetails').empty();
                       $('#methodDetails').append('<tr><td>'+object.method+'</td></tr>');
                       $('#commentDetails').empty();
                       $('#commentDetails').append('<tr><td>'+object.comment+'</td></tr>');
                       $('#detailsLesson').modal('show');
//                        });

                   }
               }
           }

 function loadComments(){
            if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
              
        var myObj = {};
                myObj["studentid"] = $('#lessonid').val();    
                myObj["dateString"] = $('#TXTfecha').val();
                var json = JSON.stringify(myObj);
        $.ajax({
                    type: 'POST',
                        url: 'loadComentsStudent.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {                          
                          var j = JSON.parse(data);
                          var semana="";
                          var cont1=0;
                          var cont2=0;
                          var cont3=0;
                          var cont4=0;
                          var oculto="hide";
                          $("#semana1").empty();
                          $("#semana2").empty();
                          $("#semana3").empty();
                          $("#semana4").empty();
                          
                          $.each(j, function (i,value){
                            var f = value;
               
                                $.each(f, function (i2,value2){
                                    
                                    var comentario = value2.observation; 
                                    var fechaCreacion = value2.date;
                                    var category = value2.type;
                                    var commentdate = value2.commentDate;
                                    var visible ="";    
                                    if(i <= 7){ 
                                        //semana= "semana1" REDUCIR CODIGO
                                        cont1 = cont1+180;
                                        if(cont1>$("#semana1").width()){ 
                                            visible = "hide"; 
                                        }                                       

                                        $("#semana1").append("<div class='divAdd "+visible+"'>\n\
                                                                  Comment Date: "+commentdate+"<br>\n\
                                                                  Create Date: "+fechaCreacion+"<br>\n\
                                                                  Type: "+category+"<br>\n\
                                                                  <div id='module"+cont1+"' class='container moreLess'>\n\
                                                                  <a data-comment='"+comentario+"' data-createdate='"+fechaCreacion.toString()+"' data-type='"+category+"' data-commentdate='"+commentdate+"' role='button' class='showMore collapsed' ></a>\n\
                                                                  \n\
                                                                  <p class='collapse' id='semana1"+cont1+"' aria-expanded='false'>\n\
                                                                          "+comentario+"\n\
                                                                      </p>\n\
                                                                  </div>\n\
                                                              </div>");
                                    }
                                    else{
                                        if(i<=14){
                                           //semana= "semana2"
                                            cont2 = cont2+180;
                                            if(cont2>$("#semana2").width())
                                            {
                                                visible = "hide";         
                                            }   
                                            $("#semana2").append("<div class='divAdd "+visible+"'>\n\
                                                                Comment Date: "+commentdate+"<br>\n\
                                                                Create Date: "+fechaCreacion+"<br>\n\
                                                                Type: "+category+"<br>\n\
                                                                <div id='module"+cont2+"' class='container moreLess'>\n\
                                                                <a data-comment='"+comentario+"' data-createdate='"+fechaCreacion.toString()+"' data-type='"+category+"' data-commentdate='"+commentdate+"' role='button' class='showMore collapsed' ></a>\n\
                                                                \n\
                                                                <p class='collapse' id='semana2"+cont2+"' aria-expanded='false'>\n\
                                                                        "+comentario+"\n\
                                                                    </p>\n\
                                                                </div>\n\
                                                            </div>");
                                                
                                        }
                                        else{
                                            if(i<=21){
                                               // semana= "semana3"
                                                cont3 = cont3+180;
                                                if(cont3>$("#semana3").width()){ 
                                                    visible = "hide"; 
                                                }                                       
                                                  
                                                $("#semana3").append("<div class='divAdd "+visible+"'>\n\
                                                                          Comment Date: "+commentdate+"<br>\n\
                                                                          Create Date: "+fechaCreacion+"<br>\n\
                                                                          Type: "+category+"<br>\n\
                                                                          <div id='module"+cont3+"' class='container moreLess'>\n\
                                                                          <a data-comment='"+comentario+"' data-createdate='"+fechaCreacion.toString()+"' data-type='"+category+"' data-commentdate='"+commentdate+"' role='button' class='showMore collapsed' ></a>\n\
                                                                          \n\
                                                                          <p class='collapse' id='semana3"+cont3+"' aria-expanded='false'>\n\
                                                                                  "+comentario+"\n\
                                                                              </p>\n\
                                                                          </div>\n\
                                                                      </div>");
                                      
                                             }
                                            else{ 
                                                //semana= "semana4"
                                                cont4 = cont4+180;
                                                 if(cont4>$("#semana4").width()){ 
                                                    visible = "hide"; 
                                                }                                       
                                                  
                                                $("#semana4").append("<div class='divAdd "+visible+"'>\n\
                                                                          Comment Date: "+commentdate+"<br>\n\
                                                                          Create Date: "+fechaCreacion+"<br>\n\
                                                                          Type: "+category+"<br>\n\
                                                                          <div id='module"+cont4+"' class='container moreLess'>\n\
                                                                          <a data-comment='"+comentario+"' data-createdate='"+fechaCreacion.toString()+"' data-type='"+category+"' data-commentdate='"+commentdate+"' role='button' class='showMore collapsed' ></a>\n\
                                                                          \n\
                                                                          <p class='collapse' id='semana4"+cont4+"' aria-expanded='false'>\n\
                                                                                  "+comentario+"\n\
                                                                              </p>\n\
                                                                          </div>\n\
                                                                      </div>");
                                            }
                                        }
                                    }   
                                });
                             
                          });
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });
    }
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
       ajax.open("POST","detailsLesson.htm?LessonsSelected="+LessonsSelected,true);
       ajax.send("");
 };
  function modifySelect(LessonsSelected)
   {
      window.open("<c:url value="/editlesson/start.htm?LessonsSelected="/>"+LessonsSelected);
 };
  function funcionCallBackdeleteLesson()
   {
          if (ajax.readyState===4){
               if (ajax.status===200){
               <%--    var lessondeleteconfirm = '<%= request.getParameter("messageDelete") %>'; --%>
                   var lessondeleteconfirm = "";
               var lessondeleteconfirm = JSON.parse(ajax.responseText);
                      if (lessondeleteconfirm.message === 'Presentation has progress records,it can not be deleted' ){
                           $('#lessonDeleteMessage').append('<H1>'+lessondeleteconfirm.message+'</H1>');
                           $('#deleteLessonMessage').modal('show');
                       }else {
                           $('#lessonDeleteMessage').append('<H1>'+lessondeleteconfirm.message+'</H1>');
                           $('#deleteLessonMessage').modal('show'); //  Presentation deleted successfully
                       };  
                       
                       

                   }
               }
           }
 function deleteSelect(LessonsSelected)
 {
      if (window.XMLHttpRequest) //mozilla
       {
           ajax = new XMLHttpRequest(); //No Internet explorer
       }
       else
       {
           ajax = new ActiveXObject("Microsoft.XMLHTTP");
       }
       
       ajax.onreadystatechange = funcionCallBackdeleteLesson;
       ajax.open("POST","deleteLesson.htm?LessonsSelected="+LessonsSelected,true);
   <%-- window.open("<c:url value="/homepage/deleteLesson.htm?LessonsSelected="/>"+LessonsSelected); --%>
       ajax.send("");
     
     
 };
   function refresh()
   {
        location.reload();
   }
     
  
   </script>
   <style>

       .moreLess{
           position: absolute; 
           height: 225px;
           
       }
        div[id^='module'] {
            width: 235px;
            padding: 0px;
            
        }

        div[id^='module'] p.collapse[aria-expanded="false"] {
            display: block;
            height: 20px !important;
            overflow: hidden;

        }

        div[id^='module'] p.collapsing[aria-expanded="false"] {
            height: 20px !important;
            width: 180px;
        }

        div[id^='module'] a.collapsed:after  {
            content: '+ Show More';
        }

        div[id^='module'] a:not(.collapsed):after {

            content: '- Show Less';
        }

      .title
       {
           font-size: medium;
           font-weight: bold;
           color: gray;
           margin-top: 5px;
           padding-left: 5px;
       }
       .par
       {
           background-color: lightgrey;
           
       }
       .impar
       {
          border-bottom: solid 1px grey;
       }
       .studentDetails{
           padding-top: 5px;
           padding-bottom: 5px;
           padding-left: 10px;
       }
       .modal-header-details
       {
           background-color: #99CC66;
       }
       .modal-header-delete
       {
           background-color: #CC6666;
       }
       .divClass{
           margin-top: 20px;
           min-height: 20px;
           color: #ffffff;
           padding: 5px;
       }
       .divAdd{
            color: #777777;
            height: 110px;
            width: 180px;
            background-color: rgba(255,255,255,0.5);
            margin-right: 10px;
            font-size: 12px;
            padding: 5px;
            display: line;
            float: left;
        }
 
       .scroll{
           overflow-x: scroll;
       }
       .tamFijo{
           height:  130px;
           display: flex;
           width:  1300px;
       }
       .semana1, .semana2, .semana3, .semana4
       {
           overflow-x: visible;
       }
       
       .complete{
            display:none;
        }

        .more{
            background:lightblue;
            color:navy;
            font-size:13px;
            padding:3px;
            cursor:pointer;
        }
        div[data-toggle="collapse"] {
            float: left;
        }
        .firstWeek
        {
            margin-top: 5px;
            background-color: #99CC66;
        }
        .secondWeek
        {
            margin-top: 5px;
            background-color: #cdcdcd;
        }
        .carousel-control
        {
            position: absolute;
            top: 25px;
            bottom: 0;
            left: 0;
            width: 15%;
            font-size: 30px;
            color: #fff;
            text-align: center;
            text-shadow: 0 1px 2px rgba(0,0,0,.6);
            background-color: rgba(0,0,0,0);
            filter: alpha(opacity=50);
            opacity: .5;
        }
   </style>
   </head>
   <body>
       <input type="hidden" id="lessonid" name="lessonid" value = ${studentId}>
       <div class="col-xs-12">
           <div class="col-sm-12" id="maincontainer">
               <div class="col-sm-12 center-block text-center">
                   <h2>CLASSROOM</h2>
               </div>
           </div>
            <div class="container">
 <%--              <table id="table_id" class="display" >
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
                                   <input name="TXTid_lessons_attendance" class="btn-unbutton" type="image" src="<c:url value="/recursos/img/btn/btn_Attendance.svg"/>" value="${lecciones.id}" id="attendance" onclick="rowselect(${lecciones.id})" width="40px" data-placement="bottom" title="Attendance">
                               </div>
                               <div class="col-xs-3">
                                   <input name="TXTid_lessons_detalles" type="image" src="<c:url value="/recursos/img/btn/btn_details.svg"/>" value="${lecciones.id}" id="details" onclick="detailsSelect(${lecciones.id})" width="40px" data-placement="bottom" title="Details">
                               </div>
                               <div class="col-xs-3">
                                   <input name="TXTid_lessons_modificar" type="image" src="<c:url value="/recursos/img/btn/btn_Edit.svg"/>" value="${lecciones.id}" id="modify" onclick="modifySelect(${lecciones.id})" width="40px" data-placement="bottom" title="Modify">
                               </div>
                               <div class="col-xs-3">
                                   <input class="delete" name="TXTid_lessons_eliminar" type="image" src="<c:url value="/recursos/img/btn/btn_delete.svg"/>" value="${lecciones.id}" id="delete" onclick="deleteSelectSure(${lecciones.id}, '${lecciones.name}')" width="40px" data-placement="bottom" title="Delete">
                               </div>
                           </td>
                       </tr>
                   </c:forEach>
                   </tbody>
           </table>
         
           --%>
           <div class="col-xs-12">
               <div class="col-xs-6" >

                   <div class='col-xs-6'>
                        <div class="form-group">
                            <label class="control-label" for="fecha">Date</label>
                            <div class='input-group date' id='fecha'>
                                <input type='text' name="TXTfecha" class="form-control" id="TXTfecha" onselect="loadComments()"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                   
               </div>
             
           </div>
           <div class="col-xs-12">
           <hr>
           </div>
            <script>
                $( window ).resize(function() {
                   loadComments();
                });
                 
           
                function moverDrech(x)
                {
                    if($("#semana"+x).children().not(".hide").length !== 1){
                        $("#semana"+x).children().not(".hide").next().removeClass("hide");
                        $("#semana"+x).children().not(".hide").first().addClass("hide");
                    }
                }
                function moverIzq(x)
                {
                    $("#semana"+x).children().not(".hide").prev().removeClass("hide");
                     
                    if($("#semana"+x).children().not(".hide").length*180 > $("#semana"+x).width()){
                         $("#semana"+x).children().not(".hide").last().addClass("hide");
                    }
                    if($("#semana"+x).children().not(".hide").length === 0){
                        $("#semana"+x).children().last().removeClass("hide");
                    }
                }
            
           </script>
            <div class="col-xs-12 firstWeek sinpadding">
                <div class="col-xs-2" >
                   <div class="col-xs-12 divClass sinpadding" data-idclass="1919">
                       <div class="col-xs-12 sinpadding">
                           <h4>First Week</h4>
                       </div>
                   </div>
               </div>
               <div class="col-xs-1">
                    <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('1')"></span>
               </div>
               <div class="col-xs-8 sinpadding" > 
                   <div class="semana1 tam1" id="semana1">
                   </div>
               </div>
               <div class="col-xs-1">
                   <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('1')"></span>
               </div>
           </div>
           <div class="col-xs-12 secondWeek sinpadding">
                <div class="col-xs-2">
                   <div class="col-xs-12 divClass sinpadding" data-idclass="1919">
                       <div class="col-xs-12 sinpadding">
                           <h4>Second Week</h4>
                       </div>
                   </div>
               </div>
                <div class="col-xs-1">
                   <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('2')"></span>
               </div>
               <div class="col-xs-8 sinpadding" > 
                   <div  class="semana2 tam2" id="semana2">
                   </div>
               </div>
               <div class="col-xs-1">
                   <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('2')"></span>
               </div>
           </div>
           
           <div class="col-xs-12 firstWeek sinpadding">
                <div class="col-xs-2">
                   <div class="col-xs-12 divClass sinpadding" data-idclass="1919">
                       <div class="col-xs-12 sinpadding">
                           <h4>Third Week</h4>
                       </div>
                   </div>
               </div>
               <div class="col-xs-1">
                   <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('3')"></span>
               </div>
               <div class="col-xs-8 sinpadding" > 
                   <div  class="semana3 tam3" id="semana3">
                   </div>
               </div>
               <div class="col-xs-1">
                   <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('3')"></span>
               </div>
           </div>
           <div class="col-xs-12 secondWeek sinpadding">
                <div class="col-xs-2" >
                   <div class="col-xs-12 divClass sinpadding" data-idclass="1919">
                       <div class="col-xs-12 sinpadding">
                           <h4>Fourth Week</h4>
                       </div>
                   </div>
               </div>
               <div class="col-xs-1">
                   <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('4')"></span>
               </div>
               <div class="col-xs-8 sinpadding" > 
                   <div  class="semana4 tam4" id="semana4">
                   </div>
               </div>
               <div class="col-xs-1 btn moveBtn">
                   <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('4')"></span>
               </div>
           </div>
                
           </div>
       <!-- Modal delete-->
<div id="detailsLesson" class="modal fade" role="dialog">
 <div class="modal-dialog modal-lg">

   <!-- Modal content-->
   <div class="modal-content">
     <div class="modal-header modal-header-details">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
       <h4 id="nameLessonDetails" class="modal-title">Details</h4>
     </div>
       <div class="modal-body">
           <div class="container-fluid">
               <div class="col-xs-6">
                   <div class="row title">
                       Students
                   </div>
                   <div id="studentarea" class="row studentarea">
       
                   </div>
               </div>
               <div class="col-xs-6">
                   <div class="row title">
                       Method
                   </div>
                   <div class="row" id="methodDetails">
                       
                   </div>
                   <div class="row title">
                       Lesson Description
                   </div>
                   <div class="row" id="commentDetails">
                       
                   </div>
                   <div class="row title">
                       Content
                   </div>
                   <div class="row">
                       <ul id="contentDetails">
                           
                       </ul>
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
<!-- Modal confirm delete-->
<div id="deleteLesson" class="modal fade" role="dialog">
 <div class="modal-dialog">

   <!-- Modal content-->
   <div class="modal-content">
     <div class="modal-header modal-header-delete">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
       <h4 class="modal-title">are you sure you want to delete?</h4>
     </div>
       <div id="lessonDelete" class="modal-body">
           
       </div>
     <div class="modal-footer text-center">
         <button id="buttonDelete" type="button" class="btn btn-danger" data-dismiss="modal" onclick="deleteSelect(value)">Yes</button>
       <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
     </div>
   </div>

 </div>
</div>
<!-- Modal lessons delete -->
<div id="deleteLessonMessage" class="modal fade" role="dialog">
 <div class="modal-dialog">

   <!-- Modal content-->
   <div class="modal-content">
     <div class="modal-header modal-header-delete">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
       <h4 class="modal-title"></h4>
     </div>
       <div id="lessonDeleteMessage" class="modal-body">
           <c:out value='${messageDelete}'/>
       </div>
     <div class="modal-footer text-center">
         <button type="button" class="btn btn-default" data-dismiss="modal" onclick="refresh()">OK</button>
     </div>
   </div>

 </div>
</div>
<script>
    
$(document).on("click", ".showMore", function () {
    var comment= $(this).data('comment');
    var createDate= $(this).data('createdate');
    var type = $(this).data('type');
    var commentDate = $(this).data('commentdate');
    
    $('#idCommentDate').text(commentDate);
    $('#idCreateDate').text(createDate);
    $('#idTypeComment').text(type);
    $('#idComment').text(comment);
       
    $('#showComment').modal('show');
});
</script>

<div id="showComment" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">
          
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header modal-header-details">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">More Information</h4>
      </div>
        <div class="modal-body">
            <div class="container-fluid">
                <div class="col-xs-12">
                        <div class="col-xs-4 center-block form-group">
                            <label class="control-label">Comment Date:</label>
                            <div id="idCommentDate"></div>                        
                        </div>
                        
                        <div class="col-xs-4 center-block form-group">
                            <label class="control-label">Create Date:</label>
                            <div id="idCreateDate"></div>                        
                        </div>   
                        <div class="col-xs-4 center-block form-group">
                            <label class="control-label">Type:</label>
                            <div id="idTypeComment"></div>                        
                        </div>
                </div> 
                
                <div  class="col-xs-12">
                    <div class="col-xs-12 form-group">
                            <label class="control-label">Comment:</label>
                            <div id="idComment"></div>                        
                    </div>
                </div>
            </div>
        </div>
    </div>

  </div>
</div> 
   </body>
</html>