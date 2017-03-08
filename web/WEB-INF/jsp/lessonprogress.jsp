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

        
        <form:form id="formStudents" method ="post" action="createlesson.htm?select=createlesson" >
          
                
            <fieldset>
                <legend>Lessons details</legend>
                
                <div class="col-xs-3 center-block">
                    <label class="control-label">Name lesson</label>
                    <input type="text" class="form-control" name="TXTnombreLessons" id="NameLessons" required="" placeholder="${lessondetailes.name}">
                </div>
            
                <div class="col-xs-3 center-block">
                    <label class="control-label">Subject</label>
                    <input type="text" class="form-control" name="TXTnombreLessons" id="NameLessons" required="" placeholder="${lessondetailes.subject.name}">
                </div>
              <div class="col-xs-3 center-block">
                    <label class="control-label">Objective</label>
                    <input type="text" class="form-control" name="TXTnombreLessons" id="NameLessons" required="" placeholder="${lessondetailes.objective.name}">
                </div>  
               
                
                            <div class="col-xs-12">
                <table id="table_id" class="display" >
                    <thead>
                        <tr>
                            <td>Student Name</td>
                            <td>Rating</td>
                            <td>Comment</td>
                         
                          
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="record" items="${attendancelist}" >
                        <tr>
                            <td>${record.studentname}</td>
                            <td>${record.rating}</td>
                            <td>${record.comment}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
            </table>
           
            </div>
                        
                <div class="hidden col-xs-12" id="divLoadLessons" style="padding-left: 0px;">   
                   
               
           
                        
                
              
                
            </fieldset>
    
            
        </form:form>
        
        </div>




    </body>
</html>
