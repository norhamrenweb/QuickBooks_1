<%-- 
    Document   : Observations
    Created on : 08-feb-2018, 11:36:41
    Author     : Norhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            var studentid;
            $(document).ready(function () {
                $('#newcomment').attr('disabled', true);
                
                table = $('#table_students').DataTable({
                    "searching": true,
                    "paging": false,
                    "ordering": false,
                    "info": false,
                    columns: [
                        {data: 'id',
                            visible: false},
                        {data: 'name'}
                    ]
                });
                $('#table_students tbody').on('click', 'tr', function () {
                    var data = table.row(this).data();
                    studentid = data.id;
                    getsubjects(data.id);
                    $('#newcomment').attr('disabled', true);
                });
                
                
                $('#commentbutton').on('click', function () {
                    var idob = $('#objectives option:selected').val();
                    var comment = $('#commentcontent').val();
                    if(comment.length > 0)
                        newcomment(idob,comment);
                    else{
                        $('#messagediv').empty();
                        $('#messagediv').append('<h1>Comment field is empty!</h1>');
                        $('#myModal').modal('show');
                    }
                    $('#commentcontent').empty();
                    $('#commentModal').modal('hide');
                });
                
                $('#subjects').on('change', function() {
                    getobjectives( this.value );
                    $('#newcomment').attr('disabled', true);
                });
                
                $('#newcomment').on('click', function () {
                    $('#commentModal').modal('show');
                });
                
                $('#subjects').on('change', function() {
                    getobjectives( this.value );
                    $('#newcomment').attr('disabled', true);
                });
                
                $('#objectives').on('change', function() {
                    getcomments( this.value );
                });
                
                $('#grades').on('change', function() {
                    getstudents( this.value );
                });
            });
            
            function newcomment(idobjective,comment){
                $.ajax({
                    type: 'POST',
                    url: 'newcomment.htm?idstudent='+studentid+'&idobjective='+idobjective+'&comment='+comment,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        console.log(data);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function getsubjects(idstudent){
                $.ajax({
                    type: 'POST',
                    url: 'subjects.htm?idstudent='+idstudent,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        var json = JSON.parse(data);
                        var subjects = JSON.parse(json.subjects);
                        $('#subjects').empty();
                        $.each(subjects, function (i, subject) {
                            $('#subjects').append('<option value="'+subject.id+'">'+subject.name+'</option>');
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function getobjectives(idsubject){
                $.ajax({
                    type: 'POST',
                    url: 'objectives.htm?idsubject='+idsubject,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        var json = JSON.parse(data);
                        var objectives = JSON.parse(json.objectives);
                        $('#objectives').empty();
                        $.each(objectives, function (i, objective) {
                            $('#objectives').append('<option value="'+objective.id+'">'+objective.name+'</option>');
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function getcomments(idobjective){
                $.ajax({
                    type: 'POST',
                    url: 'comments.htm?idstudent='+studentid+'&idobjective='+idobjective,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        $('#newcomment').attr('disabled', false);
                        var json = JSON.parse(data);
                        console.log(json);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function getstudents(idgrade){
                $.ajax({
                    type: 'POST',
                    url: 'studentslevel.htm?idgrade='+idgrade,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        var json = JSON.parse(data);
                        var students = JSON.parse(json.subjects);
                        $('#tabla_st').empty();
                        $('#tabla_st').append('<table id="table_students"></table>');
                        $('#table_students').append(
                        '<thead>'+
                            '<tr>'+
                                '<td>ID</td>'+
                                '<td>Name students</td>'+
                            '</tr>'+
                        '</thead>'+
                        '<tbody>');
                        $.each(students, function (i, alumnos) {
                            $('#table_students').append(
                            '<tr>'+
                                '<td >'+alumnos.id_students+'</td>'+
                               '<td >'+alumnos.nombre_students+'</td>'+
                            '</tr>');
                        });
                        $('#table_students').append('</tbody>');    
              
                        table = $('#table_students').DataTable({
                            "searching": true,
                            "paging": false,
                            "ordering": false,
                            "info": false,
                            columns: [
                                {data: 'id',
                                    visible: false},
                                {data: 'name'}
                            ]
                        });
                        $('#table_students tbody').on('click', 'tr', function () {
                            var data = table.row(this).data();
                            studentid = data.id;
                            getsubjects(data.id);
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
        </script>
    </head>
    <body>
        <div class="col-xs-2" >
            <select id="grades">
                <c:forEach var="levels" items="${gradelevels}">
                    <option value="${levels.id[0]}">${levels.name}</option>
                </c:forEach>
            </select>
            <div id="tabla_st" class="col-xs-12 studentarea">
                <table id="table_students" class="display" >
                    <thead>
                        <tr>
                            <td>ID</td>
                            <td>Name students</td>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="alumnos" items="${students}" >
                        <tr>
                            <td >${alumnos.id_students}</td>
                            <td >${alumnos.nombre_students}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-xs-10">
            <div class="col-xs-12">
                <div class="col-xs-3">
                    <select id="subjects">
                        <option>Select level</option>
                    </select>
                </div>
                <div class="col-xs-6"></div>
                <div class="col-xs-3">
                    <select id="objectives">
                        <option>Select objectives</option>
                    </select>
                </div>
                
            </div>
            
                <button type="button" class="btn btn-primary btn-lg" id="newcomment">New comment</button> 
        </div>
        <!--<h1>Hello World!</h1>-->
        
        <div id="commentModal" class="modal fade" role="dialog">
             <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">New comment</h4>
                    </div>
                    <div class="modal-body text-center">
                        <textarea style="width:100%;" rows="7" id="commentcontent" required="required"></textarea>
                    </div>
<!--                    <div class="rating-input">
                        <input type="number" name="your_awesome_parameter" id="some_id" class="rating" data-clearable="X" data-icon-lib="iconsAragon fa" data-active-icon="icon-Pie_PieIzqSelect" data-inactive-icon="icon-Pie_PieIzqUnSelect" data-clearable-icon="fa-null" data-max="2" data-min="1" value="1,2" />
                        
                    </div>-->
                    <div class="modal-footer">
                        <button id="commentbutton" class="btn btn-primary btn-lg" value="Comment">Comment</button>
                    </div>
                </div>
            </div>
        </div>
            
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <!--        <h4 class="modal-title" id="myModalLabel">Modal title</h4>-->
                    </div>
                    <div id="messagediv" class="modal-body text-center">
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
