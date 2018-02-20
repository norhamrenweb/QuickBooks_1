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
                $("#subjects").attr("disabled", true);
                $("#objectives").attr("disabled", true);
                $("#divNotas").hide();

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
                    var table = $('#table_students').DataTable();
                    if ($(this).hasClass('selected')) {
                        $(this).removeClass('selected');
                    } else {
                        table.$('tr.selected').removeClass('selected');
                        $(this).addClass('selected');
                    }
                    var data = table.row(this).data();
                    studentid = data.id;
                    getsubjects(data.id);
                    $('#semana0').empty();
                    $('#newcomment').attr('disabled', true);
                });
                $('#editcomment').on('click', function () {
                    var idob = $('#objectives option:selected').val();
                    var comment = $('#commentcontent2').val();
                    var idcomment = $('#idedit').val();
                    editcomment(idcomment, idob, comment, $('[name=steps2]').val(), $('[name=TXTrating2]').val());
                });
                $('#commentbutton').on('click', function () {
                    var idob = $('#objectives option:selected').val();
                    var comment = $('#commentcontent').val();
                    if (comment.length > 0)
                        newcomment(idob, comment, $('[name=steps]').val(), $('[name=TXTrating]').val());
                    else {
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
                $('#subjects').on('change', function () {
                    $('#steps_show').empty();
                    $('#steps_show2').empty();
                    $('#semana0').empty();
                    if (this.value !== 'vacio')
                        getobjectives(this.value);
                    else {
                        $("#objectives").attr("disabled", true);
                        $("#divNotas").hide();

                    }
                    $('#newcomment').attr('disabled', true);
                });
                $('#objectives').on('change', function () {
                    $('#steps_show').empty();
                    $('#steps_show2').empty();
                    if (this.value !== 'vacio')
                        getcomments(this.value);
                });
                $('#grades').on('change', function () {
                    getstudents(this.value);
                });
            });
            function newcomment(idobjective, comment, steps, rating) {
                $.ajax({
                    type: 'POST',
                    url: 'newcomment.htm?idstudent=' + studentid + '&idobjective=' + idobjective + '&comment=' + comment
                            + '&step=' + steps + '&rating=' + rating,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        if (data === 'succes') {
                            getcomments(idobjective);
                            $('#messagediv').empty();
                            $('#messagediv').append('<h4>Comment create succesfully.</h4>');
                            $('#myModal').modal('show');
                        } else {
                            $('#messagediv').empty();
                            $('#messagediv').append('<h4>Error</h4>');
                            $('#myModal').modal('show');
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }

            function editcomment(idcomment, idobjective, comment, steps, rating) {
                $.ajax({
                    type: 'POST',
                    url: 'editcomment.htm?idcomment=' + idcomment + '&idstudent=' + studentid + '&idobjective=' + idobjective + '&comment=' + comment
                            + '&step=' + steps + '&rating=' + rating,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        if (data === 'succes') {
                            getcomments(idobjective);
                            $('#messagediv').empty();
                            $('#messagediv').append('<h4>Edit succesfully.</h4>');
                            $('#myModal').modal('show');
                        } else {
                            $('#messagediv').empty();
                            $('#messagediv').append('<h4>Error</h4>');
                            $('#myModal').modal('show');
                        }

                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }

            function getsubjects(idstudent) {
                $.ajax({
                    type: 'POST',
                    url: 'subjects.htm?idstudent=' + idstudent,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        var json = JSON.parse(data);
                        var subjects = JSON.parse(json.subjects);
                        $('#subjects').empty();
                        $.each(subjects, function (i, subject) {
                            $('#subjects').append('<option value="' + subject.id + '">' + subject.name + '</option>');
                        });
                        $("#subjects").attr("disabled", false);


                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }

            function getobjectives(idsubject) {
                $.ajax({
                    type: 'POST',
                    url: 'objectives.htm?idsubject=' + idsubject,
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
                            $('#objectives').append('<option value="' + objective.id + '">' + objective.name + '</option>');
                        });
                        $("#objectives").attr("disabled", false);
                        $("#divNotas").hide();


                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }

            function getcomments(idobjective) {
                $.ajax({
                    type: 'POST',
                    url: 'comments.htm?idstudent=' + studentid + '&idobjective=' + idobjective,
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
                            $('#steps_show').append('<li>' + step.name + '</li>');
                            $('#steps_show2').append('<li>' + step.name + '</li>');
                        });
                        for (var i = 0; i < 16; i++) {
                            if (i > steps.length)
                                $('[data-value=' + i + ']').addClass('hidden');
                            else
                                $('[data-value=' + i + ']').removeClass('hidden');
                        }
                        var j = 0;
                        $('#semana0').empty();
                        $.each(comments, function (i, comment) {
                            var date = comment.comment_date + '';
                            var cc = comment.comment;
                            if (cc.length > 100)
                                cc = cc.substring(0, 100);
                            var rating = "";
                            var editdelete = "";
                            var color = "";
                            var colorRating = "";
                            if (comment.createdby === '${user.id}') {
                                editdelete = '<div class="col-xs-4 text-center sinpadding">' +
                                        '<button onclick="edit(' + comment.id + ')"  type="button" class="btn btn-link" id="editComentario' + comment.id + '">' +
                                        '<span class="glyphicon glyphicon-pencil"></span>' +
                                        '</button>' +
                                        '</div>' +
                                        '<div class="col-xs-4 text-center sinpadding">' +
                                        '<button type="button" class="btn btn-link" onclick="borrar(' + comment.id + ')" data-toggle="tooltip" data-placement="bottom" value="delete" id="ConfirmDeleteComentario' + comment.id + '">' +
                                        '<span class=" glyphicon glyphicon-remove"></span>' +
                                        '</button>' +
                                        '</div>';
                            }
                            if (comment.rating_name === 'Mastered')
                                // color = 'goldenrod';
                                colorRating = "success";
                            else if (comment.rating_name === 'Presented')
                                //  color = 'darkgreen';
                                colorRating = "warning";
                            else if (comment.rating_name === 'Attempted')
                                // color = 'blueviolet';
                                colorRating = "primary";
                            else
                                colorRating = "default";
                            // color = 'black';
                            if (comment.rating_name !== undefined &&
                                    comment.rating_name !== "")
                                rating = '<strong>Rating:</strong>'
                                        + comment.rating_name;
                            /*$('#semana0').append('<div class="divAdd" id="' + comment.id + '">'
                             + '<div>\n\
                             <strong>Date:</strong>' + date.substring(0, 10)
                             + '</div>\n\
                             <div>\n\
                             <strong>Observation: </strong>\n\
                             <div>' + cc + '</div>'
                             + '</div>\n\
                             <div>\n\
                             ' + lastStep(steps, comment.step_id) + '\n\
                             </div>\n\
                             <div style="color:' + color + ';">\n\
                             ' + rating + '\n\
                             </div>' +
                             '<div class="col-xs-12 text-center sinpadding optionsObservations">' +
                             '<div class="col-xs-4 text-center sinpadding">' +
                             '<button onclick="mostrarComentario(' + comment.id + ')" type="button" class="btn btn-link showMore">' +
                             '<span class="glyphicon glyphicon-list-alt"></span>' +
                             '</button>' +
                             '</div>' +
                             editdelete +
                             '</div>'
                             );*/



                            $('#semana0').append("<div class='divAdd' id='" + comment.id + "'> \n\
                                                    <div class='project project-radius project-" + colorRating + "'>\n\
                                                        <div class='shape'>	\n\
                                                            <div class='shape-text'></div>\n\
                                                        </div>\n\
                                                        <div class='project-content'>\n\
                                                            <h3 class='lead'> <strong>" + comment.rating_name + "</strong><br>" + date.substring(0, 10) + "         <span class='badge badge-pill badge-success'>Presentation</span></h3>\n\
                                                                <p><strong>Observation: </strong>" + cc + "\
                                                                    <div>" + lastStep(steps, comment.step_id) + "\n\
                                                                    </div>\n\
                                                                \n\
                                                                </p>\n\
                                                                <div class = 'col-xs-12 text-center sinpadding optionsObservations' >\n\
                                                                    <div class='col-xs-4 text-center sinpadding'>\n\
                                                                            <button onclick='mostrarComentario(" + comment.id + ")' type='button' class='btn btn-link showMore'>                         \n\
                                                                                <span class='glyphicon glyphicon-list-alt'></span>\n\
                                                                            </button>\n\
                                                                    </div>" + editdelete +
                                    "</div>\n\
                                                        </div>\n\
                                                    </div>\n\
                                                </div>");
                            if (comment.generalcomment)                                 
                                $("#" + comment.id + " div span").first().text("General")
                            
                             $('#' + comment.id + " div span").first().css({"background-color": "black"});

                            ;
                        });

                        $("#divNotas").show();
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }

            function edit(id) {
                $('#idedit').val(id);
                id = '' + id;
                $.each(comments, function (i, comment) {
                    if (comment.id === id) {
                        if (comment.step_id !== undefined) {
                            var step = (comment.step_id.split(",").length - 1) + 1;
                            $('.editrating [data-value=' + step + ']').click();
                        } else {
                            $('.editrating .rating-clear').click();
                        }
                        $('#commentcontent2').empty();
                        $('#commentcontent2').val(comment.comment);
                        $('#hi2').val(comment.rating_name);
                    }
                });
                $('#editModal').modal('show');
            }

            function borrar(id) {
                $('#delcomment').empty();
                $('#delcomment').append(
                        '<button onclick="delComment(' + id + ')" class="btn btn-primary btn-lg" data-dismiss="modal" aria-label="Close">Delete</button>'
                        );
                $('#deleteModal').modal('show');
            }

            function delComment(id) {
                $.ajax({
                    type: 'POST',
                    url: 'delcomment.htm?id=' + id,
                    datatype: "text",
                    contentType: "application/json",
                    success: function (data) {
                        if (data === 'succes') {
                            $('#' + id).remove();
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }
                });
            }

            function mostrarComentario(id) {
                id = '' + id;
                $('#commentcomplete').empty();
                $.each(comments, function (i, comment) {
                    if (comment.id === id) {
                        $('#commentcomplete').append('<div><h4>Comment</h4></div>\n\
                            <div>' + comment.comment + '</div');
                        $('#completemodal').modal('show');
                    }
                });
            }

            function lastStep(steps, id) {
                var ret = '';
                if (id === undefined)
                    return ret;
                var i = id.length;
                while (i >= 0 && id.charAt(i) !== ',') {
                    i--;
                }
                if (id.charAt(i) === ',')
                    i++;
                var idstep = id.substring(i, id.length);
                $.each(steps, function (i, step) {
                    if (step.id === idstep)
                        ret = step.name;
                });
                return '<strong>Last Step: </strong>' + ret;
            }

            function getstudents(idgrade) {
                $.ajax({
                    type: 'POST',
                    url: 'studentslevel.htm?idgrade=' + idgrade,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        var json = JSON.parse(data);
                        var students = JSON.parse(json.subjects);
                        $('#tabla_st').empty();
                        $('#tabla_st').append('<table id="table_students"></table>');
                        $('#table_students').append(
                                '<thead>' +
                                '<tr>' +
                                '<td>ID</td>' +
                                '<td>Name students</td>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>');
                        $.each(students, function (i, alumnos) {
                            $('#table_students').append(
                                    '<tr>' +
                                    '<td >' + alumnos.id_students + '</td>' +
                                    '<td >' + alumnos.nombre_students + '</td>' +
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
                            var table = $('#table_students').DataTable();
                            if ($(this).hasClass('selected')) {
                                $(this).removeClass('selected');
                            } else {
                                table.$('tr.selected').removeClass('selected');
                                $(this).addClass('selected');
                            }

                            var data = table.row(this).data();
                            studentid = data.id;
                            getsubjects(data.id);
                        });

                        $("#subjects").attr("disabled", true);
                        $("#objectives").attr("disabled", true);
                        $("#divNotas").hide();
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

            table.dataTable tbody td {

                border-top: solid 1px #ddd;
            }
            .shape{    
                border-style: solid; border-width: 0 70px 40px 0; float:right; height: 0px; width: 0px;
                -ms-transform:rotate(360deg); /* IE 9 */
                -o-transform: rotate(360deg);  /* Opera 10.5 */
                -webkit-transform:rotate(360deg); /* Safari and Chrome */
                transform:rotate(360deg);
            }

            .lead {

                font-size: 16px;
                font-weight: 300;
                line-height: 1.4;
                margin-top: 5px;

            }

            .shape-text{
                color:#fff; font-size:12px; font-weight:bold; position:relative; right:-40px; top:2px; white-space: nowrap;
                -ms-transform:rotate(30deg); /* IE 9 */
                -o-transform: rotate(360deg);  /* Opera 10.5 */
                -webkit-transform:rotate(30deg); /* Safari and Chrome */
                transform:rotate(30deg);
            }

            .project {
                /*min-height:300px;
               // height:auto;*/
                width: 100%;
                height: 100%;
            }

            .project{
                background:#fff; border:1px solid #ddd; box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2); /*margin: 15px 0; */overflow:hidden;
            }

            .project-radius{
                border-radius:7px;
            }

            .project-default {    border-color: #999999; }
            .project-default .shape{
                border-color: transparent #999999 transparent transparent;
                border-color: rgba(255,255,255,0) #999999 rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-danger {    border-color: #d9534f; }
            .project-danger .shape{
                border-color: transparent #d9534f transparent transparent;
                border-color: rgba(255,255,255,0) #d9534f rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-success {	border-color: #5cb85c; }
            .project-success .shape{
                border-color: transparent #5cb85c transparent transparent;
                border-color: rgba(255,255,255,0) #5cb85c rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-primary {	border-color: #428bca; }
            .project-primary .shape{
                border-color: transparent #428bca transparent transparent;
                border-color: rgba(255,255,255,0) #428bca rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-info {	border-color: #5bc0de; }
            .project-info .shape{
                border-color: transparent #5bc0de transparent transparent;
                border-color: rgba(255,255,255,0) #5bc0de rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-warning {	border-color: #f0ad4e; }
            .project-warning .shape{
                border-color: transparent #f0ad4e transparent transparent;
                border-color: rgba(255,255,255,0) #f0ad4e rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-content {
                padding:0 20px 10px;
            }

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
               
                height: 100%;
                /*overflow-x: scroll;
                overflow-y: scroll;*/
                border-radius: 10px;
            }

            .divAdd{
                color: black;
                height: 225px;
                width: 290px;
                /* background-color: rgba(255,255,255,0.5);*/
                margin-right: 10px;
                font-size: 12px;
                padding: 5px;
                display: line;
                float: left;
                position: relative;
            }
            .marginTop{
                margin-top: 20px;
            }
            .semana1{
                padding-left: 4%;
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
            <div class="col-xs-12 marginTop sinpadding">
                <div class="col-xs-3 sinpadding">
                    <select id="subjects">
                        <option>Select Subject</option>
                    </select>
                </div>
            </div>
            <div class="col-xs-12 marginTop sinpadding">
                <div class="col-xs-3 sinpadding">
                    <select id="objectives">
                        <option>Select Objectives</option>
                    </select>
                </div>
            </div>


        </div>
        <div class="col-xs-10" id="divNotas">

            <div class="col-xs-12 firstWeek">
                <div class="col-xs-12" style="margin-top:20px;" > 
                    <div class="semana1 tam1" id="semana0">
                    </div>
                </div>
            </div>
            <div class="col-xs-12 text-right">
                <input class="btn-lg" type="image" id="newcomment" src="<c:url value='/recursos/img/iconos/add-comment(1).svg'/>" width="100px">
            </div>
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
                        <button id="commentbutton" class="btn btn-primary btn-lg" data-dismiss="modal" aria-label="Close" value="Comment">Comment</button>
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
                        <button id="editcomment" class="btn btn-primary btn-lg" data-dismiss="modal" aria-label="Close">Edit</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="deleteModal" class="modal fade" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <p class="modal-title" id="myModalLabel">Delete comment</p>
                    </div>
                    <div id="steps_show2" class="col-xs-6"></div>
                    <div class="modal-body text-center">
                        <h3>Are you sure?</h3>
                    </div>
                    <div class="modal-footer" id="delcomment">
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
