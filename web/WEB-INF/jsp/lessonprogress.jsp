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
        <link href="recursos/css/bootstrap-toggle.css" rel="stylesheet" type="text/css"/>
        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        
        <script src="recursos/js/bootstrap.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-toggle.js" type="text/javascript"></script>
<!--        <script src="recursos/js/bootstrap-modal.js" type="text/javascript"></script>-->
        <script src="recursos/js/moment.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script src="recursos/js/es.js" type="text/javascript"></script>
        <script src="recursos/js/ar.js" type="text/javascript"></script>
        
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

        <script>



 $(document).ready(function(){
       var userLang = navigator.language || navigator.userLanguage;
       var myDate = new Date();
         //Muestra calendario
         //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE
         var lessoncreate = '<%= request.getParameter("message") %>';
         
     if (lessoncreate === 'Lesson created' ){
     $('#myModal').modal({
        show: 'false'
    });
    }
 
         
        $('#fecha').datetimepicker({
            
            format: 'YYYY-MM-DD',
            locale: userLang.valueOf(),
            daysOfWeekDisabled: [0, 6],
            useCurrent: false//Important! See issue #1075
            //defaultDate: '08:32:33',

  
        });
        $('#horainicio').datetimepicker({
            format: 'HH:mm',
            locale: userLang.valueOf(),
            useCurrent: false, //Important! See issue #1075
            stepping: 5
        });
        $('#horafin').datetimepicker({
            
            format: 'HH:mm',
            locale: userLang.valueOf(),
            useCurrent: false, //Important! See issue #1075
            stepping: 5
        });
        
        $("#horainicio").on("dp.change", function (e) {
            $('#horafin').data("DateTimePicker").minDate(e.date);
        });
        
        $("#horafin").on("dp.change", function (e) {
            $('#horainicio').data("DateTimePicker").maxDate(e.date);
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

   $(document).ready( function () {
        $('#table_progress').DataTable(
                {
                   "columnDefs": [
                { "width": "5%", "targets": 0 },
                { "width": "10%", "targets": 1 },
                { "width": "5%", "targets": 2 }
                ] 
                });

    });
    
    
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
            width: 100%;
            }
        </style>
    </head>
    <%@ include file="menu.jsp" %>
    <body>
        
        
        <div class="container">
        <h1 class="text-center">Progress Lessons</h1>

        
        <form:form id="formStudents" method ="post" action="lessonprogress.htm?select6=saveRecords" >
          
                
            <fieldset>
                <legend>Lessons details</legend>

                <div class="col-xs-3 center-block">
                    Name lesson:<label class="control-label"><input type="hidden" class="form-control" name="TXTlessonid" value="${lessondetailes.id}"/> ${lessondetailes.name}</label>
                </div>

                <div class="col-xs-3 center-block">
                    Subject:<label class="control-label"> ${lessondetailes.subject.name}</label>
                </div>
                <div class="col-xs-3 center-block">
                    Objective:<label class="control-label"><input type="hidden" class="form-control" name="TXTobjectiveid" value="${lessondetailes.objective.id[0]}"/> ${lessondetailes.objective.name}</label>
                </div>  
            </fieldset>   
                
                            <div class="col-xs-12">
                <table id="table_progress" class="display" >
                    <thead>
                        <tr>
                            <td>Student Id</td>
                            <td>Student Name</td>
                            <td>Rating</td>
                            <td>Comment</td>
                            <td>Attendance Code</td>
                          
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${attendancelist}" >
                        <tr>
                            <td><input type="hidden" class="form-control" name="TXTstudentid" value="${record.studentid}"/>${record.studentid}</td>
                            <td>${record.studentname}</td>
                            <td>
                                
                                <select name="TXTrating">
                                    <c:if test="${empty record.rating}">
                                        <option selected>N/A</option>
                                    </c:if>
                                    <c:if test="${not empty record.rating}">
                                       <option selected>${record.rating}</option>
                                    </c:if>
                                    <%--<c:when test="${empty record.rating}">
                                        <option selected>${record.rating}</option>
                                    </c:when>--%>
                                    
                                    <option>Presented</option>
                                    <option>Attempted</option>
                                    <option>Mastered</option>
                                </select>
                            </td>
                            <td>
                                <textarea name="TXTcomment" >${record.comment}</textarea>
                            </td>
                            <td>
                                <select name="TXTattendance">
                                    <option></option>
                                    <option>P</option>
                                    <option>A</option>
                                    <option>T</option>
                                </select>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
           
            </div>
                        
                <div class="col-xs-12">   
                    <input type="submit" value="Save">
                </div>  
</form:form>
        
        




    </body>
</html>
