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
    <%@ include file="infouser.jsp" %>
    <%@ include file="menu.jsp" %>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lessons progress</title>
        <script>



 $(document).ready(function(){

         //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE
        var lessoncreate = '<%= request.getParameter("message") %>';
         
     if (lessoncreate === 'Records successfully saved' ){
     $('#myModal').modal({
        show: 'false'
    });
    }
        
        $('#table_progress').DataTable(
                {
                   "columnDefs": [
                { "width": "5%", "targets": 0 },
                { "width": "20%", "targets": 1 },
                { "width": "5%", "targets": 2 },
                { "width": "45%", "targets": 3 },
                { "width": "25%", "targets": 4, "orderable": false }
                ] 
                });        
    });

$(function() {
$('select').change(function() {
    var itemsRating = []; // needs to be outside
    var itemsAttendance = []; // needs to be outside
var collectionRating = $(".rating");
var collectionAttendance = $(".attendance");

collectionRating.each(function() {
    itemsRating.push($(this).val());   
});
collectionAttendance.each(function() {
    itemsAttendance.push($(this).val());   
});
    var haynullRating = $.inArray('', itemsRating);
    var haynullAttendance = $.inArray('', itemsAttendance);
    
    if( haynullRating !== -1 || haynullAttendance !== -1){
        $('#buttonAchived').attr('disabled', true);
        $('#buttonAchived').parent().attr('disabled', true);
        
    }else{
        $('#buttonAchived').attr('disabled', false);
        $('#buttonAchived').parent().removeAttr('disabled');
    }
});
     $('#rellenarP').click(function() {

       var rellenarPresent = $(".attendance");
       rellenarPresent.each(function() {
            $(this).val("P");
        });
       
    });
    $('#rellenar').click(function() {

       var rellenarPresent = $(".attendance");
       rellenarPresent.each(function() {
            $(this).val("");
        });
       
    });
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

});
        </script>
        <style>
            textarea 
            {
            resize: none;
            width: 100%;
            }
            rating 
            {}
        </style>
    </head>
    <body>
        
        
        <div class="container">
            <h1 class="text-center">Lesson Progress</h1>
<p></p>

            <form:form id="formStudents" method ="post" action="saveRecords.htm" >


                <fieldset>
                    <legend>Lesson details</legend>

                    <div class="col-xs-3 center-block">
                        Lesson Name:  <label class="control-label"><input type="hidden" class="form-control" name="TXTlessonid" value="${lessondetailes.id}"/> ${lessondetailes.name}</label>
                    </div>

                    <div class="col-xs-3 center-block">
                        Subject:  <label class="control-label"> ${lessondetailes.subject.name}</label>
                    </div>
                    <div class="col-xs-3 center-block">
                        Objective:  <label class="control-label"><input type="hidden" class="form-control" name="TXTobjectiveid" value="${lessondetailes.objective.id[0]}"/> ${lessondetailes.objective.name}</label>
                    </div>  
                </fieldset>   
                <fieldset style="margin-top: 10px;">
                    <legend></legend>
                <div class="col-xs-12">
                    
                    <table id="table_progress" class="display" >
                        <thead>
                            <tr>
                                <td hidden="true">Student Id</td>
                                <td>Student Name</td>
                                <td>Rating</td>
                                <td>Comment</td>
                                <td>Attendance Code <input type="button" class="btn btn-xs btn-info" id="rellenarP" value="Fill P"> <input type="button" class="btn btn-xs btn-info" id="rellenar" value="Clear"></td>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="record" items="${attendancelist}" >
                                <tr class="rows">
                                    <td hidden="true"><input type="hidden" class="form-control" name="TXTstudentid" value="${record.studentid}"/>${record.studentid}</td>
                                    <td>${record.studentname}</td>
                                    <td>
                                        <select name="TXTrating" name="rating" class="rating">
                                            <c:if test="${empty record.rating}">
                                                <option selected></option>
                                            </c:if>
                                            <c:if test="${not empty record.rating}">
                                                <option selected>${record.rating}</option>
                                            </c:if>
                                            <option></option>
                                            <option>N/A</option>
                                            <option>Presented</option>
                                            <option>Attempted</option>
                                            <option>Mastered</option>
                                        </select>
                                    </td>
                                    <td>
                                        <textarea name="TXTcomment" >${record.comment}</textarea>
                                    </td>
                                    <td>
                                        <select name="TXTattendance" class="attendance">
                                            <c:if test="${empty record.attendancecode}">
                                                <option selected></option>
                                            </c:if>
                                            <c:if test="${not empty record.attendancecode}">
                                                <option selected>${record.attendancecode}</option>
                                            </c:if>
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
                </fieldset>
                    <select name="TXTinstructor"> 
                        <c:if test="${empty selectedinst.nombre_students}">
                                                <option selected></option>
                                            </c:if>
                                            <c:if test="${not empty selectedinst.nombre_students}">
                                                <option value="${selectedinst.id_students}" selected>${selectedinst.nombre_students}</option>
                                            </c:if>
                        <c:forEach var="name" items="${instructors}" >
                        <option value="${name.id_students}">${name.nombre_students}</option>
                        </c:forEach>
                    </select>
                <div class="col-xs-6 text-center">   
                    <input type="submit" class="btn btn-success" value="Save">
                </div>
                <div class="col-xs-6 text-center">
                    <input type="checkbox" disabled="true" data-width="200px" data-onstyle="success" data-offstyle="warning" data-toggle="toggle" data-on="Archived" data-off="Not Archived" name="buttonAchived" id="buttonAchived">
                </div>
            </form:form>
        </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleComment"><%= request.getParameter("message") %></h4>
                    </div>
                </div>
            </div>
        </div>    





    </body>
</html>
