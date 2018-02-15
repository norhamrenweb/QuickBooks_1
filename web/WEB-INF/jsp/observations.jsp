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
                <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/fonts/icons/iconsAragon.css"/>"/>

        <title>JSP Page</title>
        <script>
            var studentid;
            var comments;
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
                
                $('#editcomment').on('click', function () {
                    var idob = $('#objectives option:selected').val();
                    var comment = $('#commentcontent2').val();
                    var idcomment = $('#idedit').val();
                    editcomment(idcomment,idob,comment,$('[name=steps2]').val(),$('[name=TXTrating2]').val());
                });
                
                
                $('#commentbutton').on('click', function () {
                    var idob = $('#objectives option:selected').val();
                    var comment = $('#commentcontent').val();
                    if(comment.length > 0)
                        newcomment(idob,comment,$('[name=steps]').val(),$('[name=TXTrating]').val());
                    else{
                        $('#messagediv').empty();
                        $('#messagediv').append('<h1>Comment field is empty!</h1>');
                        $('#myModal').modal('show');
                    }
                    $('#commentcontent').empty();
                    $('#commentModal').modal('hide');
                });
                
                
                $('#newcomment').on('click', function () {
                    $('#commentModal').modal('show');
                });
                
                $('#subjects').on('change', function() {
                    $('#steps_show').empty();
                    $('#steps_show2').empty();
                    if(this.value !== 'vacio')
                        getobjectives( this.value );
                    $('#newcomment').attr('disabled', true);
                });
                
                $('#objectives').on('change', function() {
                    $('#steps_show').empty();
                    $('#steps_show2').empty();
                    if(this.value !== 'vacio')
                        getcomments( this.value );
                });
                
                $('#grades').on('change', function() {
                    getstudents( this.value );
                });
            });
            
            function newcomment(idobjective,comment,steps,rating){
                $.ajax({
                    type: 'POST',
                    url: 'newcomment.htm?idstudent='+studentid+'&idobjective='+idobjective+'&comment='+comment
                        +'&step='+steps+'&rating='+rating,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        if(data === 'succes'){
                            getcomments(idobjective);
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function editcomment(idcomment,idobjective,comment,steps,rating){
                $.ajax({
                    type: 'POST',
                    url: 'editcomment.htm?idcomment='+idcomment+'&idstudent='+studentid+'&idobjective='+idobjective+'&comment='+comment
                        +'&step='+steps+'&rating='+rating,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        if(data === 'succes'){
                            getcomments(idobjective);
                        }
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
                        
                        $('#steps_show').append("");
                        //¡CLAVE PARA STEPS!
                        //$('[data-value=13]').removeClass('hidden');
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
                        var steps = JSON.parse(json.steps);
                        comments = JSON.parse(json.comments);
                        $('#steps_show').empty();
                        $('#steps_show2').empty();
                        $.each(steps, function (i, step) {
                            $('#steps_show').append('<li>'+step.name+'</li>');
                            $('#steps_show2').append('<li>'+step.name+'</li>');
                        });
                        for(var i = 0;i < 16;i++){
                            if(i>steps.length)
                                $('[data-value='+i+']').addClass('hidden');
                            else
                                $('[data-value='+i+']').removeClass('hidden');
                        }
                        var j = 0;
                        $('#semana0').empty();
                        $('#semana1').empty();
                        $('#semana2').empty();
                        $.each(comments,function(i,comment){
                            var date = comment.comment_date+'';
                            var cc = comment.comment;
                            if(cc.length > 12)
                                cc=cc.substring(0,12);
                            var rating = "";
                            if(comment.rating_name !== undefined && 
                                    comment.rating_name !== "")
                                rating = '<strong>Rating:</strong>'
                                    +comment.rating_name;
                            $('#semana0').append('<div class="divAdd" id="'+comment.id+'">'
                            +'<div>\n\
                                <strong>Date:</strong>'+date.substring(0,10)
                            +'</div>\n\
                            <div>\n\
                                <strong>Observation: </strong>\n\
                                <div>'+cc+'</div>'
                            +'</div>\n\
                            <div>\n\
                                '+lastStep(steps,comment.step_id)+'\n\
                            </div>\n\
                            <div>\n\
                                '+rating+'\n\
                            </div>'+
                            '<div class="col-xs-12 text-center sinpadding optionsObservations">'+
                                '<div class="col-xs-4 text-center sinpadding">'+
                                '<button onclick="mostrarComentario('+comment.id+')" type="button" class="btn btn-link showMore">'+
                                '<span class="glyphicon glyphicon-list-alt"></span>'+
                                '</button>'+
                                '</div>'+
                                '<div class="col-xs-4 text-center sinpadding">'+
                                '<button onclick="edit('+comment.id+')"  type="button" class="btn btn-link" id="editComentario'+comment.id+'">'+
                                '<span class="glyphicon glyphicon-pencil"></span>'+
                                '</button>'+
                                '</div>'+
                                '<div class="col-xs-4 text-center sinpadding">'+
                                '<button type="button" class="btn btn-link" onclick="delComment('+comment.id+')" data-toggle="tooltip" data-placement="bottom" value="delete" id="ConfirmDeleteComentario'+comment.id+'">'+
                                '<span class=" glyphicon glyphicon-remove"></span>'+
                                '</button>'+
                                '</div>'+
                                '</div>'
                            );
                            if(comment.generalcomment)
                                $('#'+comment.id).css({"background-color": "#95b8e7"});;
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function edit(id){
                id=''+id;
                $.each(comments,function(i,comment){
                    if(comment.id===id){
                        if(comment.step_id !== undefined){
                            var step = (comment.step_id.split(",").length - 1);
                            $('.editrating [data-value='+step+']').click();
                        }else{
                            $('.editrating .rating-clear').click();
                        }
                        $('#commentcontent2').text(comment.comment);
                        $('#hi2').val(comment.rating_name);
                    }
                });
                $('#editModal').modal('show');
            }
            
            function delComment(id){
                $.ajax({
                    type: 'POST',
                    url: 'delcomment.htm?id='+id,
                    datatype: "text",
                    contentType: "application/json",
                    success: function (data) {
                        if(data==='succes'){
                            $('#'+id).remove();
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }
            
            function mostrarComentario(id){
                id=''+id;
                $('#commentcomplete').empty();
                $.each(comments, function (i, comment) {
                    if(comment.id===id){
                        $('#commentcomplete').append('<div><h4>Comment</h4></div>\n\
                            <div>'+comment.comment+'</div');
                        $('#completemodal').modal('show');
                    }
                });
            }
            
            function lastStep(steps,id){
                var ret = '';
                if(id===undefined)
                    return ret;
                var i = id.length;
                while(i>=0&&id.charAt(i)!==','){
                    i--;
                }
                if(id.charAt(i)===',')
                    i++;
                var idstep = id.substring(i,id.length);
                $.each(steps, function (i, step) {
                    if(step.id === idstep)
                        ret = step.name;
                });
                return '<strong>Last Step:</strong>'+ret;
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
        <style>

            .textarea 
            {
                resize: none;
                width: 100%;
            }
            i.rating
            {
                font-size: 20px;
            }
            i.rating-input
            {
                height:30px;
            }
            .iconsAragon
            {
                font-size: 25px;
                padding-left: 10px;
            }
            /*            .progress-bar
                        {
                            background-image: linear-gradient(to bottom,#ddd 0,#ddd 100%);
                        }*/
            .TXTcomment
            {
                width: 100%;

            }
            
            .firstWeek
            {
                margin-top: 5px;
                background-color: #ddd;
                height: 500px;
                overflow-x: scroll;
                overflow-y: scroll;
            }
            
            .divAdd{
                color: #777777;
                height: 150px;
                width: 200px;
                background-color: rgba(255,255,255,0.5);
                margin-right: 10px;
                font-size: 12px;
                padding: 5px;
                display: line;
                float: left;
                position: relative;
            }
        </style>
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
            <div class="col-xs-12 firstWeek">
                <div class="col-xs-12" style="margin-top:20px;" > 
                    <div class="semana1 tam1" id="semana0">
                    </div>
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
                     <div id="steps_show" class="col-xs-6"></div>
                    <div class="modal-body text-center">
                        <textarea style="width:100%;" rows="7" id="commentcontent" required="required"></textarea>
                        <select name="TXTrating" id="hi" class="studentRating rating">
                            <option></option>
                            <option value="N/A">N/A</option>
                            <option value="Presented">Presented</option>
                            <option value="Attempted">Attempted</option>
                            <option value="Mastered">Mastered</option>
                        </select>
                        <input type="number" name="steps" id="some_id" class="rating" data-clearable="X" data-icon-lib="iconsAragon fa" data-active-icon="icon-Pie_PieIzqSelect" data-inactive-icon="icon-Pie_PieIzqUnSelect" data-clearable-icon="fa-null" data-max="15" data-min="1" value="0" />
                    </div>
                    <div class="modal-footer">
                        <button id="commentbutton" class="btn btn-primary btn-lg" value="Comment">Comment</button>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="editModal" class="modal fade" role="dialog">
             <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">Edit comment</h4>
                    </div>
                     <div id="steps_show2" class="col-xs-6"></div>
                    <div class="modal-body text-center">
                        <textarea style="width:100%;" rows="7" id="commentcontent2" required="required"></textarea>
                        <select name="TXTrating2" id="hi2" class="studentRating rating">
                            <option></option>
                            <option>N/A</option>
                            <option>Presented</option>
                            <option>Attempted</option>
                            <option>Mastered</option>
                        </select>
                        <input type="number" name="steps2" id="some_id" class="rating editrating" data-clearable="X" data-icon-lib="iconsAragon fa" data-active-icon="icon-Pie_PieIzqSelect" data-inactive-icon="icon-Pie_PieIzqUnSelect" data-clearable-icon="fa-null" data-max="15" data-min="1" value="0" />
                    </div>
                    <div class="modal-footer">
                        <input type="hidden" id="idedit">
                        <button id="editcomment" class="btn btn-primary btn-lg">Edit</button>
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
        
        <div class="modal fade" id="completemodal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <!--        <h4 class="modal-title" id="myModalLabel">Modal title</h4>-->
                    </div>
                    <div id="commentcomplete" class="modal-body text-center">
                    </div>
                </div>
            </div>
        </div>
        
    </body>
</html>
