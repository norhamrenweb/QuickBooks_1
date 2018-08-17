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

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/css/bootstrap-select.min.css">
        <link rel="manifest" href="manifest.webapp">
        <!-- Latest compiled and minified JavaScript -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/bootstrap-select.min.js"></script>
        <title><spring:message code="etiq.WorkSpace"/></title>
        <script>
            var ajax;
            var userId = ${user.id};
            var userType = ${user.type};
            var studentid;
            var comments;
            var mapTeachers = ${teachers};
            var noCommentThisWeek = "<spring:message code="etiq.NoCommentWeek"/>";

            $(document).ready(function () {
                $("#infousuario").addClass("navbar-fixed-top");


                ////////////////////////////////////////////NUEVO//////////
                var userLang = navigator.language || navigator.userLanguage;
                var myDate = new Date();
                $(".divClass").click(function () {
                    var idCLass = $(this).attr('data-idclass');
                    window.location.replace("<c:url value="/gradebook/loadRecords.htm?term=1&ClassSelected="/>" + idCLass);
                });
                //VARIABLE CUANDO   HEMOS CREADO UNA LESSONS CORRECTAMENTE              
                //f

                $('#fecha2').datetimepicker({
                    format: 'YYYY-MM-DD',
//            locale: userLang.valueOf(),
                    daysOfWeekDisabled: [0, 6],
                    useCurrent: false//Important! See issue #1075
                            //defaultDate: '08:32:33',


                });
                $('#fecha').datetimepicker({
                    format: 'YYYY-MM',
                    locale: userLang.valueOf(),
                    daysOfWeekDisabled: [0, 6],
                    useCurrent: true//Important! See issue #1075
                            //defaultDate: '08:32:33',

                });
                $('#observationfecha').datetimepicker({
                    format: 'YYYY-MM-DD',
                    locale: userLang.valueOf(),
                    daysOfWeekDisabled: [0, 6],
                    useCurrent: true//Important! See issue #1075
                            //defaultDate: '08:32:33',

                });
                var today = new Date();
                $('#fechaClassroom').datetimepicker({
                    format: 'YYYY-MM-DD',
//            locale: userLang.valueOf(),
                    daysOfWeekDisabled: [0, 6],
                    maxDate: today,
                    useCurrent: false//Important! See issue #1075
                            //defaultDate: '08:32:33',


                });
                $('#fechaClassroom').on('dp.change', function (e) {
                    if (($('#observationfechaClassroom').val() !== "") && ($('#observationcommentsClassRoom').val() !== "") && ($('#observationtypeClassroom').val() !== "")) {
                        $('#savecommentClassroom').prop("disabled", false);
                    } else {
                        $('#savecommentClassroom').prop("disabled", true);
                    }
                });
                $('#observationcommentsClassroom,#observationtypeClassroom').change(function () {
                    if (($('#observationfechaClassroom').val() !== "") && ($('#observationcommentsClassroom').val() !== "") && ($('#observationtypeClassroom').val() !== "")) {
                        $('#savecommentClassroom').prop("disabled", false);
                    } else {
                        $('#savecommentClassroom').prop("disabled", true);
                    }
                });
                //loadComments();

                $('#fecha2').on('dp.change', function (e) {
                    if (($('#observationfecha').val() !== "") && ($('#observationcomments').val() !== "") && ($('#observationtype').val() !== "")) {
                        $('#savecomment').prop("disabled", false);
                    } else {
                        $('#savecomment').prop("disabled", true);
                    }
                });
                $('#observationcomments,#observationtype').change(function () {
                    if (($('#observationfecha').val() !== "") && ($('#observationcomments').val() !== "") && ($('#observationtype').val() !== "")) {
                        $('#savecomment').prop("disabled", false);
                    } else {
                        $('#savecomment').prop("disabled", true);
                    }
                });

                $(document).on("click", ".showMoreFuncion", function () {
                    var comment = $(this).data('comment');
                    var createDate = $(this).data('createdate');
                    var type = $(this).data('type');
                    var commentDate = $(this).data('commentdate');
                    var nameTeacher = $(this).data('nameteacher');

                    var typeComment = "<spring:message code="etiq.selectlevel"/>";


                    $('#idCommentDate').text(commentDate);
                    $('#idCreateDate').text(createDate);
                    $('#idTypeComment').text(type);
                    $('#idComment').text(comment);
                    $('#idTeacher').text(nameTeacher);
                    $('#showComment').modal('show');
                });
                ////////////////////////////////////////////NUEVO//////////

                /*   $("#newClassRoom").show();
                 $("#newcomment").hide();
                 */
                $("#commentsContainer").hide();
                $("#newcomment").hide();
                $("#newClassRoom").hide();

                $("#divHora").hide();
                $("#subjects").attr("disabled", true);
                $("#objectives").attr("disabled", true);
                $("#divNotas").hide();
                $("#divClassObsv").hide();
                $("#divSubjectObjectives").hide();

                $('#classroomCommentsButton').attr('disabled', true);
                $('#dayCommentsButton').attr('disabled', true);
                $('#newcomment').attr('disabled', true);
                table = $('#table_students').DataTable({
                    language: idioma,
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

                //#62b05d
                //#5cb85c
                $('#table_students tbody').on('click', 'tr', function () {

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
                    $('#divClassObsv').hide();
                    $('#newcomment').attr('disabled', true);
                    $('#classroomCommentsButton').attr('disabled', false);
                    $('#dayCommentsButton').attr('disabled', false);
                    $('#divHora').hide();
                    $('#divSubjectObjectives').hide();
                    $("#objectives").val("vacio");
                    $("#classroomCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});
                    $("#dayCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});


                    $("#classroomCommentsButton").click();
                    $("#newcomment").hide();
                    $("#divHora").hide();
                    $("#divNotas").show();
                    $("#divClassObsv").hide();
                    $("#divSubjectObjectives").show();
                    $("#recommend").prop("checked", "");

                    $("#commentsContainer").show();
                    /*      $("#newClassRoom").show();
                     $("#newcomment").hide();*/
                });

                $('#recommend').on('click', function () {
                    var myObj = {};
                    myObj["id"] = $('#objectives option:selected').val();
                    myObj["name"] = studentid;
                    myObj["link"] = $("#btnYearmTerm").attr("data-idterm"); // termId
                    myObj["type"] = $("#btnYearmTerm").attr("data-idyear");   //yearId       
                    var json = JSON.stringify(myObj);
                    $.ajax({
                        type: 'POST',
                        url: 'recommendStudent.htm',
                        data: json,
                        datatype: "json",
                        contentType: "application/json",
                        success: function (data) {

                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.status);
                            console.log(xhr.responseText);
                            console.log(thrownError);
                        }


                    });
                });

                $('#editcomment').on('click', function () {
                    var idob = $('#objectives option:selected').val();
                    var comment = $('#commentcontent2').val();
                    var idcomment = $('#idedit').val();
                    editcomment(idcomment, idob, comment, $('[name=steps2]').val(), $('[name=TXTrating2]').val());
                });

                $('#commentbutton').on('click', function () {
                    var idob = $('#objectives option:selected').val();
                    var comment = $('#commentcontent').val().trim();
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
                    $("#commentcontent").val("");
                    $("#hi").val("");
                    $(".rating-clear").click();
                    $("#myNewCommentTitle").text($("#objectives :selected").text());
                    $('#commentModal').modal('show');
                });
                $('#newClassRoom').on('click', function () {
                    $("#observationcommentsClassroom").val("");
                    $("#observationtypeClassroom").val("");
                    $("#savecommentClassroom").prop("disabled", "true");
                    $('#newClassRoomModal').modal('show');
                });
                $('#subjects').on('change', function () {
                    $('#steps_show').empty();
                    $('#steps_show2').empty();
                    $('#semana0').empty();
                    if (this.value !== 'vacio') {
                        getobjectives(this.value);
                        sortSelect("objectives");

                        $("#objectives").val("vacio");
                        var selectObject = "<spring:message code="etiq.selectObject"/>";
                        $("#objectives option[value='vacio']").text(selectObject);
                    } else {
                        $("#objectives").attr("disabled", true);
                        $("#divNotas").hide();
                    }
                    $('#newcomment').attr('disabled', true);
                });
                $('#objectives').on('change', function () {
                    $('#steps_show').empty();
                    $('#steps_show2').empty();
                    if (this.value !== 'vacio') {
                        $("#newcomment").show()
                        getcomments(this.value);
                    }
                });
                /*$('#grades').on('change', function () {
                 getstudents(this.value);
                 
                 
                 });
                 
                 *
                 *
                 *background-color: #337ab780;
                 padding: 5px !important;
                 border-radius: 10px;*/

                /*$(".imgLeft").on('click', function () {
                 if ($(this).val() === "classroomComments") {
                 $("#classroomCommentsButton").parent().css({"background-color": "rgba(51, 122, 183, 0.48)", "padding": "5% !important", "border-radius": "10px"});
                 $("#dayCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});
                 $("#newcomment").hide();
                 $("#divNotas").hide();
                 $("#divClassObsv").show();
                 $("#divSubjectObjectives").hide();
                 $("#divHora").show();
                 loadComments();
                 $('#fecha').on('dp.change', function (e) {
                 loadComments();
                 });
                 } else {
                 $("#dayCommentsButton").parent().css({"background-color": "rgba(51, 122, 183, 0.48)", "padding": "5%", "border-radius": "10px"});
                 $("#classroomCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});
                 
                 $("#newcomment").hide()
                 $("#divHora").hide();
                 $("#divNotas").show();
                 $("#divClassObsv").hide();
                 $("#divSubjectObjectives").show();
                 //$("#objectives").val("vacio")
                 $("#recommend").prop("checked", "");
                 }
                 });*/

                $("#dayCommentsButton").on('click', function () {
                    //$("#classroomCommentsButton").parent().css({"background-color": "rgba(51, 122, 183, 0.48)", "padding": "5% !important", "border-radius": "10px"});
                    //$("#dayCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});
                    $("#newcomment").hide();
                    $("#divNotas").hide();
                    $("#divClassObsv").show();
                    $("#divSubjectObjectives").hide();
                    $("#divHora").show();

                    $("#newClassRoom").show();
                    $("#newcomment").hide();

                    loadComments();
                    $('#fecha').on('dp.change', function (e) {
                        loadComments();
                    });
                });

                $("#classroomCommentsButton").on('click', function () {
                    //$("#dayCommentsButton").parent().css({"background-color": "rgba(51, 122, 183, 0.48)", "padding": "5%", "border-radius": "10px"});
                    // $("#classroomCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});
                    $("#newcomment").hide()
                    $("#divHora").hide();
                    $("#divNotas").show();
                    $("#divClassObsv").hide();
                    $("#divSubjectObjectives").show();
                    //$("#objectives").val("vacio")
                    $("#recommend").prop("checked", "");

                    /*$("#newcomment").show();*/
                    $("#newClassRoom").hide();

                });

                $("#levelStudent").val("");
                var selectlevel = "<spring:message code="etiq.selectlevel"/>";
                $("#level option[value='']").text(selectlevel);


                $("#fileToUploadClassroom,#fileToUpload").change(function () {

                    $(this).next().children().next().text($(this).val().split('/').pop().split('\\').pop());
                });

                $("#fileToUploadClassroom,#fileToUpload").mouseover(function () {

                    $("#fileToUploadClassroom").next().children().first().css("background-color", "#3074af");
                    $("#fileToUploadClassroom").next().children().first().css("color", "white");
                    $("#fileToUploadClassroom").next().children().first().css("border-color", "white");

                    $("#fileToUpload").next().children().first().css("background-color", "#3074af");
                    $("#fileToUpload").next().children().first().css("color", "white");
                    $("#fileToUpload").next().children().first().css("border-color", "white");
                });

                $("#fileToUploadClassroom,#fileToUpload").mouseout(function () {

                    $("#fileToUploadClassroom").next().children().first().css("background-color", "white");
                    $("#fileToUploadClassroom").next().children().first().css("color", "#3074af");
                    $("#fileToUploadClassroom").next().children().first().css("border-color", "#3074af");

                    $("#fileToUpload").next().children().first().css("background-color", "white");
                    $("#fileToUpload").next().children().first().css("color", "#3074af");
                    $("#fileToUpload").next().children().first().css("border-color", "#3074af");

                });

            });


            function updateComment() {
                var comments = $('#observationcomments').val();
                var fecha = $("#observationfecha").val();
                var tipo = $('#observationtype').val();
                var id = $('#idComentario').val();
                var myObj = {};
                myObj["observation"] = comments;
                myObj["dateString"] = fecha;
                myObj["type"] = tipo;
                myObj["id"] = id;
                var json = JSON.stringify(myObj);
                var data = new FormData();
                data.append("obj", json);
                data.append("fileToUpload", $('#fileToUpload')[0].files[0]);
                data.append("update", "true");
                var path = document.location.href;
                var i = path.length - 1;
                for (var j = 0; j < 2; j++) {
                    if (j === 1)
                        path = path.substring(0, i);
                    while (path[i] !== '/') {
                        path = path.substring(0, i);
                        i--;
                    }
                }
                loadComments();
                $('#editCommentModal').modal('hide');
                $('#confirmsave').modal('show');
                path = path + "savecomment";
                var request = new XMLHttpRequest();
                request.open("POST", path);
                request.send(data);
            }

            function editComentario(id) {
                var comment = $('#editComentario' + id).data('comment');
                var createDate = $('#editComentario' + id).data('createdate');
                var type = $('#editComentario' + id).data('type').trim();
                var commentDate = $('#editComentario' + id).data('commentdate');
                $('#observationfecha').val(commentDate);
                $('#observationtype').val(type);
                $('#observationcomments').val(comment);
                $('#idComentario').val(id);
                $('#editCommentModal').modal('show');
            }
            function ConfirmDeleteComentario(val) {
                $('#deleteObservation').modal('show');
                $('#buttonDeleteObservation').val(val);
            }

            function deletePhoto(id) {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                var idComment = $("#deleteFoto").val();
                var myObj = {};
                myObj["id"] = idComment;
                var json = JSON.stringify(myObj);
                $.ajax({
                    type: 'POST',
                    url: 'delFoto.htm',
                    data: json,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {

                        $('#modalimagen').modal('hide');
                        $("#verphoto" + idComment).attr("color", "#bab8b8");
                        $("#verphoto" + idComment).attr("disabled", "true");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }


                });
            }


            function deleteComentario(id) {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                var idComment = id;
                var myObj = {};
                myObj["id"] = idComment;
                var json = JSON.stringify(myObj);
                $.ajax({
                    type: 'POST',
                    url: 'delComentario.htm',
                    data: json,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {

                        var parentAux = $('#comment' + id).parent();
                        //var j = JSON.parse(data);   
                        if ($('#comment' + id).parent().children().length === 1) {
                            var idSemana = $('#comment' + id).parent().attr('id');
                            if (idSemana === "semana1" || idSemana === "semana2" || idSemana === "semana3")
                                $('#comment' + id).parent().append(divVacio("project-classroom1"));
                            else
                                $('#comment' + id).parent().append(divVacio("project-classroom2"));
                        }
                        $('#comment' + id).remove();
                        parentAux.children().not(".hide").last().next().removeClass("hide");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }


                });
            }
            function loadComments() {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                var myObj = {};
                myObj["studentid"] = studentid;
                myObj["dateString"] = $('#TXTfecha').val();
                var json = JSON.stringify(myObj);
                $.ajax({
                    type: 'POST',
                    url: 'loadComentsStudent.htm',
                    data: json,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        var j = JSON.parse(data);
                        var numSemana = "#semana1";
                        var cont1 = 0;
                        var cont2 = 0;
                        var cont3 = 0;
                        var cont4 = 0;
                        var cont5 = 0;
                        var cont6 = 0;
                        $("#semana1").empty();
                        $("#semana2").empty();
                        $("#semana3").empty();
                        $("#semana4").empty();
                        $("#semana5").empty();
                        $("#semana6").empty();
                        $("#semana5").parent().parent().parent().hide();
                        $("#semana6").parent().parent().parent().hide();
                        var weeksCount = weeksInAMonth($("#TXTfecha").val().split("-")[0], $("#TXTfecha").val().split("-")[1]);
                        if (weeksCount > 4) {
                            $("#semana5").parent().parent().parent().show();
                            if (weeksCount > 5)
                                $("#semana6").parent().parent().parent().show();
                        }
                        $.each(j, function (i, value) {
                            var f = value;
                            $.each(f, function (i2, value2) {
                                var id = value2.id;
                                var comentario = value2.observation;
                                var comentarioExtenso = '';
                                if (comentario.length >= 86) {
                                    var comentarioExtenso = '...';
                                }
                                var fechaCreacion = value2.date;
                                var category = value2.type;
                                var commentdate = value2.commentDate;
                                var visible = "";
                                var nameTeacher = value2.nameTeacher;
                                var disable = "";
                                var dayWeek = value2.numSemana;
                                var idTeacher = value2.logged_by;
                                var booleanFoto = value2.foto;
                                var anchoDiv = 195;
                                var disableFoto = "";
                                var classSemana = "project-classroom1";
                                var colorDisabled = "";

                                if (booleanFoto === false) {
                                    disableFoto = "disabled='disabled'"
                                    colorDisabled = "style='color:  #bab8b8 !important;'";
                                }
                                if (userId !== idTeacher && userType === 1) {
                                    disable = "disabled='disabled'";
                                    colorDisabled = "style='color:  #bab8b8 !important;'";
                                }

                                if (dayWeek === "1") { //PRIMERA SEMANA
                                    cont1 = cont1 + anchoDiv;
                                    if (cont1 > $(numSemana).width()) {
                                        visible = "hide";
                                    }

                                } else if (dayWeek === "2") { //SEGUNDA SEMANA
                                    visible = "";
                                    numSemana = "#semana2";
                                    cont2 = cont2 + anchoDiv;
                                    if (cont2 > $(numSemana).width())
                                    {
                                        visible = "hide";
                                    }
                                    classSemana = "project-classroom2";
                                } else if (dayWeek === "3") {//TERCERA SEMANA
                                    visible = "";
                                    numSemana = "#semana3";
                                    cont3 = cont3 + anchoDiv;
                                    if (cont3 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                    classSemana = "project-classroom1";
                                } else if (dayWeek === "4") { //CUARTA SEMANA
                                    visible = "";
                                    numSemana = "#semana4";
                                    cont4 = cont4 + anchoDiv;
                                    if (cont4 > $(numSemana).width())
                                    {
                                        visible = "hide";
                                    }
                                    classSemana = "project-classroom2";
                                } else if (dayWeek === "5") {//QUINTA SEMANA
                                    visible = "";
                                    numSemana = "#semana5";
                                    cont5 = cont5 + anchoDiv;
                                    if (cont5 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                    classSemana = "project-classroom1";
                                } else { // SEXTA SEMANA
                                    visible = "";
                                    numSemana = "#semana6";
                                    cont6 = cont6 + anchoDiv;
                                    if (cont6 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                    classSemana = "project-classroom2";
                                }
                                var path = document.location.href;
                                var i = path.length - 1;
                                while (path[i] !== '/') {
                                    path = path.substring(0, i);
                                    i--;
                                }

                                var dateEtiq = "<spring:message code="etiq.Date"/>";
                                var obsEtiq = "<spring:message code="etiq.observation"/>";

            <%--Create Date: " + fechaCreacion + "<br>\n\
            Type: " + category + "<br>\n\--%>
                                $(numSemana).append("<div id='comment" + id + "' value='" + commentdate + "' class='divAdd " + visible + "'>\n\
                                                        <div class='project project-radius " + classSemana + "'>\n\
                                                                                <div class='shape'>	\n\
                                                                                    <div class='shape-text'></div>\n\
                                                                                </div>\n\
                                                                                <div class='project-content projectProgcal'>\n\
                                                                                    \n\<strong>" + dateEtiq + ":</strong> " + commentdate + " </strong> <br>\n\
                                                                                    <strong>" + obsEtiq + ":</strong> " + comentario.substring(0, 86) + " " + comentarioExtenso + "<br>\n\
                                                                                    \n\<div class='col-xs-12 text-center sinpadding optionsObservations'>\n\
                                                                                    <div class='col-xs-3 text-center sinpadding'>\n\
                                                                                    <button type='button' class='btn btn-link showMoreFuncion'  data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "'>\n\
                                                                                    <span class='glyphicon glyphicon-list-alt'></span>\n\
                                                                                    </button>\n\
                                                                                    </div>\n\
                                                                                    <div class='col-xs-3 text-center sinpadding'>\n\
                                                                                    <button type='button' " + disable + " class='btn btn-link' data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "' onclick='editComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editComentario" + id + "'>\n\
                                                                                    <span class='glyphicon glyphicon-pencil'></span>\n\
                                                                                    </button>\n\
                                                                                    </div>\n\
                                                                                    <div class='col-xs-3 text-center sinpadding'>\n\
                                                                                    <button type='button'  " + disable + " class='btn btn-link' onclick='ConfirmDeleteComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='delete' id='ConfirmDeleteComentario" + id + "'>\n\
                                                                                    <span class=' glyphicon glyphicon-remove'></span>\n\
                                                                                    </button>\n\
                                                                                    </div>\n\
                                                                                    <div class='col-xs-3 text-center sinpadding'>\n\
                                                                                    <input type='hidden' id='date" + id + "' value='" + commentdate + "'/>\n\
                                                                                    <button type='button'  " + disableFoto + " onclick='verphoto(" + id + ")' class='popOverFoto btn btn-link' data-toggle='tooltip' data-placement='bottom' value='" + id + "' id='verphoto" + id + "'>\n\
                                                                                    <span class=' glyphicon glyphicon-camera' " + colorDisabled + "></span></button>\n\</div>\n\</div>\n\</div>\n\</div>\n\</div>");
                            });
                            //aqui meter los q no se muestran


                        });


                        showsCountHides();
                        if (cont1 === 0)
                            $("#semana1").append(divVacio("project-classroom1"));
                        if (cont2 === 0)
                            $("#semana2").append(divVacio("project-classroom2"));
                        if (cont3 === 0)
                            $("#semana3").append(divVacio("project-classroom1"));
                        if (cont4 === 0)
                            $("#semana4").append(divVacio("project-classroom2"));
                        if (cont5 === 0)
                            $("#semana5").append(divVacio("project-classroom1"));
                        if (cont6 === 0)
                            $("#semana6").append(divVacio("project-classroom2"));
                        /*$(".popOverFoto").mouseover(function () {
                         if ($(this).prop("disabled") === false) {
                         var id = $(this).val();
                         var imageTag = '<div class="divFoto" style="position:absolute;">' + '<img class="fotoComment"  id="imgPop" src="" alt="image" height="100" />' + '</div>';
                         if (window.XMLHttpRequest) //mozilla
                         {
                         ajax = new XMLHttpRequest(); //No Internet explorer
                         } else
                         {
                         ajax = new ActiveXObject("Microsoft.XMLHTTP");
                         }
                         
                         ajax.onreadystatechange = function () {
                         if (ajax.readyState === 4 && ajax.status === 200) {
                         if (ajax.responseText !== "") {
                         var json = JSON.parse(ajax.responseText);
                         $('#imgPop').attr("src", "data:" + json.ext + ";base64," + json.imagen);
                         }
                         }
                         };
                         ajax.open("POST", "getimage.htm?id=" + id + "&date=" + $('#date' + id).val(), true);
                         ajax.send("");
                         $(this).parent('div').append(imageTag);
                         }
                         
                         });*/
                        $(".popOverFoto").mouseleave(function () {
                            $(this).parent('div').children('div').remove();
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }

                });
            }

            function showsCountHides() {
                for (var i = 1; i < 7; i++) {
                    var semana = "#semana" + i;
                    var numHides = $(semana + " .hide").length;

                    $(semana).parent().next().children().children().remove();
                    $(semana).parent().prev().children().children().remove();

                    if ($(semana + " .hide").length > 0)
                        $(semana).parent().next().children().append("<div  style='text-align:  center;'>+" + numHides + "</div>");
                    else
                        $(semana).parent().next().children().append("<div  style='text-align:  center;'>+0</div>");

                    $(semana).parent().prev().children().append("<div  style='text-align:  center;'>+0</div>");
                }
            }

            function divVacio(classDiv) {
                return "<div class='divAdd'><div class='project project-radius " + classDiv + "'>\n\
                                                        <div class='shape'>\n\
                                                            <div class='shape-text'></div>\n\
                                                        </div>\n\
                                                        <div class='project-content'>\n\
                                                        " + noCommentThisWeek + "\n\
                                                        </div>\n\
                                                  </div></div>"
            }
            function weeksInAMonth(year, month_number) {
                var firstOfMonth = new Date(year, month_number - 1, 1);
                var day = firstOfMonth.getDay() || 6;
                day = day === 1 ? 0 : day;
                if (day) {
                    day--
                }
                var diff = 7 - day;
                var lastOfMonth = new Date(year, month_number, 0);
                var lastDate = lastOfMonth.getDate();
                if (lastOfMonth.getDay() === 1) {
                    diff--;
                }
                var result = Math.ceil((lastDate - diff) / 7);
                return result + 1;
            }
            ;
            function detailsSelect(LessonsSelected)
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }
                ajax.onreadystatechange = funcionCallBackdetailsLesson;
                ajax.open("POST", "detailsLesson.htm?LessonsSelected=" + LessonsSelected, true);
                ajax.send("");
            }
            ;
            function modifySelect(LessonsSelected)
            {
                window.open("<c:url value="/editlesson/start.htm?LessonsSelected="/>" + LessonsSelected);
            }
            ;
            function funcionCallBackdeleteLesson()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
            <%--    var lessondeleteconfirm = '<%= request.getParameter("messageDelete") %>'; --%>
                        var lessondeleteconfirm = "";
                        var lessondeleteconfirm = JSON.parse(ajax.responseText);
                        if (lessondeleteconfirm.message === 'Presentation has progress records,it can not be deleted') {
                            $('#lessonDeleteMessage').append('<H1>' + lessondeleteconfirm.message + '</H1>');
                            $('#deleteLessonMessage').modal('show');
                        } else {
                            $('#lessonDeleteMessage').append('<H1>' + lessondeleteconfirm.message + '</H1>');
                            $('#deleteLessonMessage').modal('show'); //  Presentation deleted successfully
                        }
                        ;
                    }
                }
            }
            function deleteSelect(LessonsSelected)
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                ajax.onreadystatechange = funcionCallBackdeleteLesson;
                ajax.open("POST", "deleteLesson.htm?LessonsSelected=" + LessonsSelected, true);
            <%-- window.open("<c:url value="/homepage/deleteLesson.htm?LessonsSelected="/>"+LessonsSelected); --%>
                ajax.send("");
            }
            ;
            function refresh()
            {
                location.reload();
            }

            function verphoto(id) {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                ajax.onreadystatechange = function () {
                    if (ajax.readyState === 4 && ajax.status === 200) {
                        if (ajax.responseText !== "") {
                            var json = JSON.parse(ajax.responseText);
                            $('#imagen').attr("src", "data:" + json.ext + ";base64," + json.imagen);
                            $('#modalimagen').modal('show');
                        }
                    }
                };
                ajax.open("POST", "getimage.htm?id=" + id + "&date=" + $('#date' + id).val(), true);
            <%-- window.open("<c:url value="/homepage/deleteLesson.htm?LessonsSelected="/>"+LessonsSelected); --%>
                ajax.send("");
                $("#deleteFoto").val(id);
            }
            /////////////////////////////////////////////

            function newcomment(idobjective, comment, steps, rating) {
                var boolGrade = !$("#cbUseGrade").is(":checked");
                $.ajax({
                    type: 'POST',
                    url: 'newcomment.htm?idstudent=' + studentid + '&idobjective=' + idobjective + '&comment=' + comment
                            + '&step=' + steps + '&rating=' + rating + '&cbUseGrade=' + boolGrade + '&yearId=' + yearId_view + '&termId=' + termId_view,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        if (data === 'succes') {
                            getcomments(idobjective);
                            $('#messagediv').empty();
                            $('#messagediv').append('<h4>Comment added successfully.</h4>');
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
                        $("#objectives").attr("disabled", true);
                        sortSelect("subjects");
                        $("#subjects").val("vacio");

                        var selectSubject = "<spring:message code="etiq.selectSubject"/>";
                        $("#subjects option[value='vacio']").text(selectSubject);

                        $("#divNotas").hide();
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
                        var selectObject = "<spring:message code="etiq.selectObject"/>";
                        $("#objectives option[value='vacio']").text(selectObject);
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
                    url: 'comments.htm?idstudent=' + studentid + '&idobjective=' + idobjective
                            + '&yearId=' + yearId_view + '&termId=' + termId_view,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        $('#newcomment').attr('disabled', false);
                        $('#recommend').attr('disabled', false);
                        var json = JSON.parse(data);
                        var steps = JSON.parse(json.steps);
                        comments = JSON.parse(json.comments);
                        var recommend = JSON.parse(json.recommend);

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

                            var rating = "";
                            editdelete = "";
                            var color = "";
                            var colorRating = "";
                            var editdelete = '<div class="col-xs-4 text-center sinpadding">' +
                                    '<button onclick="edit(' + comment.id + ')"  type="button" class="btn btn-link" id="editComentario' + comment.id + '">' +
                                    '<span class="glyphicon glyphicon-pencil"></span>' +
                                    '</button>' +
                                    '</div>' +
                                    '<div class="col-xs-4 text-center sinpadding">' +
                                    '<button type="button" class="btn btn-link" onclick="borrar(' + comment.id + ')" data-toggle="tooltip" data-placement="bottom" value="delete" id="ConfirmDeleteComentario' + comment.id + '">' +
                                    '<span class=" glyphicon glyphicon-remove"></span>' +
                                    '</button>' +
                                    '</div>';
                            /*if (comment.rating_name === 'Mastered') {
                             color = comment.color;
                             colorRating = "success";
                             } else if (comment.rating_name === 'Presented') {
                             color = comment.color;
                             colorRating = "warning";
                             } else if (comment.rating_name === 'Attempted') {
                             color = comment.color;
                             colorRating = "primary";
                             } else {
                             colorRating = "default";
                             color = comment.color;
                             }*/

                            colorRating = "warning";
                            color = comment.color;

                            if (comment.rating_name !== undefined &&
                                    comment.rating_name !== "")
                                rating = '<strong>Rating:</strong>'
                                        + comment.rating_name;

                            var tresPuntosComment = "...", tresPuntosStep = "...", textLastStep = lastStep(steps, comment.step_id);
                            if (cc.length < 120)
                                tresPuntosComment = "";
                            if (textLastStep.length < 70)
                                tresPuntosStep = "";

                            //ETIQUETAS
                            var presentNameEtq = "<spring:message code="etiq.namePresentation"/>";
                            var commentEtiq = "<spring:message code="etiq.comment"/>";
                            var lastStepEtiq = "<spring:message code="etiq.lastStepEtiq"/>";
                            var createdByEtiq = "<spring:message code="etiq.createByEtiq"/>";
                            var presentationEtiq = "<spring:message code="etiq.presentationEtiq"/>";

                            var presentationName = "";
                            if (comment.presentationName !== undefined && comment.presentationName !== "")
                                presentationName = "<p><strong>" + presentNameEtq + ": </strong> " + comment.presentationName + "</p>";

                            $('#semana0').append("<div class='divAddNotas' id='" + comment.id + "'> \n\
                                                    <div class='project project-radius project-" + colorRating + "'>\n\
                                                        <div class='shape' id='" + comment.id + "shape'>	\n\
                                                            <div class='shape-text'></div>\n\
                                                        </div>\n\
                                                        <div class='project-content'>\n\
                                                            <h3 class='lead'> <strong style='color:" + color + "'>" + comment.rating_name + "</strong><br>" + date.substring(0, 10) + "         <span class='badge badge-pill badge-success'>" + presentationEtiq + "</span></h3>\n\
     " + presentationName + "\
\n\
    <p><strong>" + commentEtiq + ": </strong>" + cc.substring(0, 120) + " " + tresPuntosComment + "</p>\
                                                                    <p><strong>" + lastStepEtiq + ": </strong>" + textLastStep.substring(0, 70) + " " + tresPuntosStep + "\n\
                                                                    </p>\n\
                                                                \n\
                                                                <div class = 'col-xs-12 text-center sinpadding optionsObservationsNotas' >\n\
                                                                    <div class='col-xs-4 text-center sinpadding'>\n\
                                                                            <button onclick='mostrarComentario(" + comment.id + ",\"" + createdByEtiq + "\",\"" + commentEtiq + "\")' type='button' class='btn btn-link showMore'>                         \n\
                                                                                <span class='glyphicon glyphicon-list-alt'></span>\n\
                                                                            </button>\n\
                                                                    </div>" + editdelete +
                                    "</div>\n\
                                                        </div>\n\
                                                    </div>\n\
                                                    <div id='datos" + comment.id + "' class='hide' data-comment='" + cc + "' data-step='" + textLastStep + "' data-created='" + comment.createdby + "'></div>\n\
                                                </div>");
                            if (comment.generalcomment)
                                $("#" + comment.id + " div span").first().text("General")

                            $('#' + comment.id + " div span").first().css({"background-color": "black"});
                            if (comment.createdby !== '${user.id}') {
                                $("#editComentario" + comment.id).attr("disabled", "true");
                                $("#ConfirmDeleteComentario" + comment.id).attr("disabled", "true");
                            }

                            $('#' + comment.id + " div").css("border-color", color);
                            $('#' + comment.id + "shape").css("border-color", "transparent " + color + " transparent transparent");
                            $('#' + comment.id + "shape").css("border-color", "rgba(255,255,255,0) " + color + " rgba(255,255,255,0) rgba(255,255,255,0)");
                        });
                        $("#divNotas").show();

                        if (recommend === true)
                            $("#recommend").prop("checked", "true");
                        else
                            $("#recommend").prop("checked", "");

                        //$("#semana0").children().last().hide();
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
                //$('#commentcontent2').val($("#datos"+id).data("comment"));
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

            function mostrarComentario(id, createdbyEtiq, CommentEtiq) {
                id = '' + id;
                $('#commentcomplete').empty();
                $.each(comments, function (i, comment) {
                    if (comment.id === id) {
                        /* $('#commentcomplete').append('<div><h4>Comment</h4></div>\n\
                         <div>' + comment.comment + '</div');*/
                        $('#commentcomplete').append("<strong>" + createdbyEtiq + ": </strong>" + mapTeachers[Number(comment.createdby)] + "<br><strong>" + CommentEtiq + ": </strong> " + comment.comment);
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

                return ret;
            }
            /*
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
             
             $('#table_students').DataTable().destroy();
             //$('#table_students').DataTable();
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
             $('#classroomCommentsButton').attr('disabled', false);
             $('#dayCommentsButton').attr('disabled', false);
             });
             
             $("#subjects").attr("disabled", true);
             $("#objectives").attr("disabled", true);
             $('#classroomCommentsButton').attr('disabled', true);
             $('#dayCommentsButton').attr('disabled', true);
             $("#divNotas").hide();
             },
             error: function (xhr, ajaxOptions, thrownError) {
             console.log(xhr.status);
             console.log(xhr.responseText);
             console.log(thrownError);
             }
             });
             }
             */
            /*var resizeId;
             $(window).resize(function () {
             clearTimeout(resizeId);
             resizeId = setTimeout(doneResizing, 500);
             });
             function doneResizing() {
             loadComments();
             }*/

            function moverDrech(x)
            {
                var rightHides = $("#semana2").parent().next().children("span").children().text().trim().split("+")[1];
                var leftHides = $("#semana2").parent().prev().children("span").children().text().trim().split("+")[1];
                var rightHides = parseInt(rightHides);
                var leftHides = parseInt(leftHides);

                if (!isNaN(rightHides)) {
                    if ($("#semana" + x).children().not(".hide").length > 1) {
                        $("#semana" + x).children().not(".hide").next().removeClass("hide");
                        $("#semana" + x).children().not(".hide").first().addClass("hide");
                        if (rightHides > 0)
                            rightHides--;
                        $("#semana" + x).parent().next().children("span").children().text("+" + rightHides);
                        if (!isNaN(leftHides)) {
                            leftHides++;
                            $("#semana" + x).parent().prev().children("span").children().text("+" + leftHides);

                        } else {
                            $("#semana" + x).parent().prev().children("span").children().text("+1");
                        }
                    }
                }
                //  < span class = "glyphicon glyphicon-chevron-left carousel-control" onclick = "moverIzq(" + x + ")" > < /span>
                ///< span class = "glyphicon glyphicon-chevron-right carousel-control" onclick = "moverDrech(" + x + ")" > < /span>

                //derecha  $("#semana" + x).parent().next().text().trim().split("+")[1]
                //izquierda $("#semana" + x).parent().prev().text().trim().split("+")[1]

                /*   $("#semana" + x).parent().prev().children("div").text();
                 $("#semana" + x).parent().next().children("div").text();
                 $(semana).parent().next().append("<div  style='text-align:  center;'>+" + numHides+"</div>");
                 **/
            }
            function moverIzq(x)
            {

                var rightHides = $("#semana2").parent().next().children("span").children().text().trim().split("+")[1];
                var leftHides = $("#semana2").parent().prev().children("span").children().text().trim().split("+")[1];
                var rightHides = parseInt(rightHides);
                var leftHides = parseInt(leftHides);

                /* if (!isNaN(rightHides)) {
                 
                 $("#semana" + x).children().not(".hide").prev().removeClass("hide");
                 if ($("#semana" + x).children().not(".hide").length * 195 >= $("#semana" + x).width()) {
                 $("#semana" + x).children().not(".hide").last().addClass("hide");
                 }
                 if ($("#semana" + x).children().not(".hide").length === 0) {
                 $("#semana" + x).children().last().removeClass("hide");
                 }
                 
                 if (!isNaN(leftHides)) {
                 if (leftHides > 0)
                 leftHides--;
                 $("#semana" + x).parent().next().children("span").children().text("+" + leftHides);
                 rightHides++;
                 $("#semana" + x).parent().prev().children("span").children().text("+" + rightHides);
                 
                 } else {
                 $("#semana" + x).parent().next().children("span").children().text("+1");
                 }
                 
                 }*/

                if (!isNaN(leftHides) && leftHides > 0) {

                    $("#semana" + x).children().not(".hide").prev().removeClass("hide");
                    if ($("#semana" + x).children().not(".hide").length * 195 >= $("#semana" + x).width()) {
                        $("#semana" + x).children().not(".hide").last().addClass("hide");
                    }
                    if ($("#semana" + x).children().not(".hide").length === 0) {
                        $("#semana" + x).children().last().removeClass("hide");
                    }

                    if (leftHides > 0)
                        leftHides--;
                    $("#semana" + x).parent().prev().children("span").children().text("+" + leftHides);
                    if (!isNaN(rightHides)) {
                        rightHides++;
                        $("#semana" + x).parent().next().children("span").children().text("+" + rightHides);

                    } else {
                        $("#semana" + x).parent().next().children("span").children().text("+1");
                    }

                }

            }

            function comboSelectionLevelStudent()
            {

                var seleccion = document.getElementById("levelStudent").value;
                $.ajax({
                    type: "POST",
                    url: "studentlistLevel.htm?seleccion=" + seleccion,
                    data: seleccion,
                    dataType: 'text',
                    success: function (data) {
                        var json = JSON.parse(data);
                        //var table = $('#table_students').DataTable();
                        $('#classroomCommentsButton').attr('disabled', true);
                        $('#dayCommentsButton').attr('disabled', true);
                        $("#divClassObsv").hide();
                        $("#divNotas").hide();

                        $('#divHora').hide();
                        $('#divSubjectObjectives').hide();
                        $("#objectives").val("vacio");

                        var selectObject = "<spring:message code="etiq.selectObject"/>";
                        $("#objectives option[value='vacio']").text(selectObject);

                        $("#classroomCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});
                        $("#dayCommentsButton").parent().css({"background-color": "", "padding": "", "border-radius": ""});
                        table.clear();
                        $.each(json, function (i) {
                            table.row.add({'id': json[i].id_students, 'name': json[i].nombre_students}).draw();
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }

                });
            }


            function saveobservation()
            {
                var observation = $("#observationcommentsClassroom").val();
                var date = $("#observationfechaClassroom").val();
                var type = $("#observationtypeClassroom :selected").text();
                var studentId = studentid;
                if (observation === "" || date === "" || type === "" || studentId === "" || type === "Select type")
                {
                    if (studentId === "") {
                        $('#error1').removeClass('hidden');
                    } else {
                        $('#error2').removeClass('hidden');
                    }
                } else {
                    var myObj = {};
                    myObj["observation"] = observation;
                    myObj["date"] = date;
                    myObj["type"] = type;
                    myObj["studentid"] = studentId;
                    myObj["termId"] = termId_view;
                    myObj["yearId"] = yearId_view;

                    var json = JSON.stringify(myObj);
                    var data = new FormData();
                    data.append("obj", json);
                    data.append("fileToUpload", $('#fileToUploadClassroom')[0].files[0]);
                    var path = document.location.href;
                    var i = path.length - 1;
                    for (var j = 0; j < 2; j++) {
                        if (j === 1)
                            path = path.substring(0, i);
                        while (path[i] !== '/') {
                            path = path.substring(0, i);
                            i--;
                        }
                    }
                    path = path + "savecomment";
                    var request = new XMLHttpRequest();
                    request.open("POST", path);
                    request.send(data);
                    $('#newClassRoomModal').modal('hide');
                    $('#confirmsaveClassroom').modal('show');
                    $("#observationcommentsClasroom").val("");
                    $("#observationfechaClasroom").val("");
                    $("#fileToUploadClassroom").val("");
                    $('#observationtypeClasroom option').filter(function () {
                        return ($(this).text() === 'Select type'); //To select Blue
                    }).prop('selected', true);
                    loadComments();
                }
            }


            function startDictation() {


                // if ('webkitSpeechRecognition' in window) {


                if (window.hasOwnProperty('webkitSpeechRecognition')) {

                    // var recognition = SpeechRecognition || webkitSpeechRecognition;
                    var recognition = new webkitSpeechRecognition();
                    recognition.continuous = false;
                    recognition.interimResults = false;

                    recognition.lang = "es-ES";
                    recognition.start();

                    recognition.onresult = function (e) {
                        document.getElementById('commentcontent').value
                                = e.results[0][0].transcript;
                        recognition.stop();
                    };

                    recognition.onerror = function (e) {
                        recognition.stop();
                    }

                }
            }

        </script>
        <style>

            .speech {
                border: 1px solid #DDD; 
                width: 100%; 
                padding: 0; 
                margin: 0
            }
            .speech input {border: 0; width: 240px; display: inline-block; height: 30px;}
            .speech img {float: right; width: 40px }


            body{
                margin-top: 120px;
            }
            .projectProgcal{
                padding: 4px !important;
            }

            /*nuevo nuevo nuevo*/
            table.dataTable tbody td {
                border-top: solid 1px #ddd;
            }

            .shape{    
                border-style: solid; border-width: 0 40px 30px 0; float:right; height: 0px; width: 0px;
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


            .project-classroom1 {    border-color: #fb8f03; }
            .project-classroom1 .shape{
                border-color: transparent #fb8f03 transparent transparent;
                border-color: rgba(255,255,255,0) #fb8f03 rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-classroom2 {    border-color: #337ab7; }
            .project-classroom2 .shape{
                border-color: transparent #337ab7 transparent transparent;
                border-color: rgba(255,255,255,0) #337ab7 rgba(255,255,255,0) rgba(255,255,255,0);
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

            .project-success {	border-color: #3d9941; }
            .project-success .shape{
                border-color: transparent #3d9941 transparent transparent;
                border-color: rgba(255,255,255,0) #3d9941 rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-primary {	border-color: #4c5e74; }
            .project-primary .shape{
                border-color: transparent #4c5e74 transparent transparent;
                border-color: rgba(255,255,255,0) #4c5e74 rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-info {	border-color: #5bc0de; }
            .project-info .shape{
                border-color: transparent #5bc0de transparent transparent;
                border-color: rgba(255,255,255,0) #5bc0de rgba(255,255,255,0) rgba(255,255,255,0);
            }

            .project-warning {	border-color: #755cda; }
            .project-warning .shape{
                border-color: transparent #755cda transparent transparent;
                border-color: rgba(255,255,255,0) #755cda rgba(255,255,255,0) rgba(255,255,255,0);
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

            .firstWeekNotas
            {
                margin-top: 5px;

                height: 100%;
                /*overflow-x: scroll;
                overflow-y: scroll;*/
                border-radius: 10px;
            }

            .divAddNotas{
                color: black;
                height: 225px;
                width: 290px;
                /* background-color: rgba(255,255,255,0.5);
                margin-right: 10px;*/
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
            .optionsObservations{
                position: absolute;
                top: 42%;
                left: 8%;
                width: 85%;
            }
            #objectives,#subjects{
                width: 100%;
            }
            #grades{

                width: 100%;
            }
            #table_students_filter{
                text-align: left;
                padding-left: 5px;
            }
            .newcomment{
                position: fixed;
                top: 88%;
                left: 7%;
                background-color: white;
            }
            .divFoto{
                position: absolute;
                width: 230px;
                z-index: 1;
                height: 180px;
            }
            .fotoComment{
                width: 100%;
                height: 100%;
                box-shadow: 0 15px 10px #777;
                border-radius: 10px;
            }
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
            .btnRemove{
                font-size: 10px;
                padding: 2px;
                position: relative;
                left: 105px;
            }
            .btnEdit{
                font-size: 10px;
                padding: 2px;
                position: relative;
                left: 100px;
            }
            .divClass{
                margin-top: 20px;
                min-height: 20px;
                color: #ffffff;
                padding: 5px;
            }
            .divAdd{
                /*color: #777777;*/
                height: 145px;
                width: 195px;
                /* background-color: rgba(255,255,255,0.5);*/

                font-size: 12px;
                padding: 5px;
                display: line;
                float: left;
                position: relative;
            }
            .optionsObservationsNotas{
                position: absolute;
                top: 82%;
                left: 0%;
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
                border: #fb8f03 solid 1px;
                border-radius: 7px;
                box-shadow: 0 2px 5px #fb8f03;
            }
            .firstWeek h4{
                color:  #fb8f03;
                font-size: x-large;
                font-weight: bolder;
                margin: 0px;
            }
            .firstWeek button{
                color:  #fb8f03 ;
                position: relative;
                top: 50px;
            }
            .firstWeek .col-xs-1{
                color:  #fb8f03 !important;
            }

            /* h4
             {
                 color: black;
                 font-size: 20px;
             }*/
            .collapse {
                display: none;
                width: 180px;
            }

            .secondWeek
            {
                margin-top: 5px;
                border: #337ab7 solid 1px;
                border-radius: 7px;
                box-shadow: 0 2px 5px #337ab7;
            }

            .secondWeek h4{
                color:  #337ab7;
                font-size: x-large;
                font-weight: bolder;
                margin: 0px;
            }

            .secondWeek .col-xs-1{
                color:  #337ab7 !important;
            }

            .secondWeek button{
                color:  #337ab7 ;
                position: relative;
                top: 50px;
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
            .btn-link
            {
                color: #777777 ;
            }
            .foto{
                width: 100% !important;
            }
            .uk-form-small{
                width: 100% !important;
            }
            #table_students{
                width: 100% !important;
                padding-right: 0px;
            }
            #table_students_filter{
                width:100%;
                padding-left: 5px;
                margin:0px;
            }
            .maskFile{
                position: relative;
                top: -22px;
                padding: 0px;
            }
            .maskFile button{
                background-color: #ffffff;
                color: #3074af;
                border-radius: 8px;
                border-color: #2f6fa7;
            }
            .maskFile label{
                font-weight: normal;
                color: #2f6fa7;
                padding-left: 5px;
            }
            #fileToUpload{
                z-index: 1;
                position: relative;
                opacity: 0;
                cursor: pointer;
            }
            #fileToUploadClassroom{
                z-index: 1;
                position: relative;
                opacity: 0;
                cursor: pointer;
            }
            .colorSuccess{
                color: #2f6fa7;
            }
            .studentarea
            {            
                height: 500px;
                width: 100%;
                overflow-y: scroll;
            }
            #table_students{
                width: 100% !important;
            }
            .dataTables_filter {
                display: block !important;
                float: left !important;
                text-align: left !important;
                padding-left: 0px;
            }
            #divCommentSubject{
                margin-top: 5px;
            }
            .dataTables_filter input {
                display: block;
                float: left;
                width: 100%;
                height: 34px;
                padding: 6px 12px;
                margin-left: 0px !important;
                font-size: 14px;
                line-height: 1.42857143;
                color: #555;
                background-color: #fff;
                background-image: none;
                border: 1px solid #ccc;
                border-radius: 4px;
                -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
                box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
                -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
                -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
                transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
            }
            div.dataTables_wrapper div.dataTables_filter input 
            {
                display: inline-block !important;
                width: 100% !important;
                margin-bottom: 0;
                margin-left: 0.5em;
            }
            .divFilter{
                margin-top: 10px;
                padding-left: 5px;
                padding-right: 0px;
            }
            .dt-merge-grid{
                padding-left: 5px;
            }
            #table_students_filter>label{
                    float: left;
                        width: 100%;
            }
            
        </style>
    </head>
    <body>
        <div class="row"><!-- inicio tabla students-->
            <div class="col-xs-3 col-md-2" >
                <!--<select id="grades">
                <c:forEach var="levels" items="${gradelevels}">
                    <option value="${levels.id[0]}">${levels.name}</option>
                </c:forEach>
            </select>-->

                <div class="col-xs-12 divFilter">
                    <label><spring:message code="etiq.filter"/></label>
                    <select class="form-control " name="levelStudent" id="levelStudent" style="width: 100% !important;" onchange="comboSelectionLevelStudent()">
                        <c:forEach var="levels" items="${gradelevels}">
                            <option value="${levels.id[0]}">${levels.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div id="tabla_st" class="col-xs-12 studentarea">
                    <table id="table_students" class="display" >
                        <thead>
                            <tr>
                                <td>ID</td>
                                <td><spring:message code="etiq.studentName"/></td>
                            </tr>
                        </thead>

                        <c:forEach var="alumnos" items="${students}" >
                            <tr>
                                <td >${alumnos.id_students}</td>
                                <td >${alumnos.nombre_students}</td>
                            </tr>
                        </c:forEach>

                    </table>
                </div>
            </div> <!-- fin tabla students-->


            <!--<div class="col-xs-1 sinpadding" >
                <div class="col-xs-12 text-left sinpadding">
                    <div class="col-xs-5 sinpadding">    
                        <input id="classroomCommentsButton" title="Class observations" class="imgLeft" type="image" value="classroomComments" src="<c:url value='/recursos/img/iconos/post-it.svg'/>" width="100px">

                    </div>
                </div>
                <div class="col-xs-12 text-left sinpadding">
                    <div class="col-xs-5 sinpadding">
                        <input id="dayCommentsButton" class="imgLeft" value="dayComments" title="Academics" type="image"  src="<c:url value='/recursos/img/iconos/computer-tool-for-education.svg'/>" width="100px">
                    </div>
                </div>
                <div class="col-xs-12 text-left sinpadding" id="divSubjectObjectives">
                    <div class="col-xs-12 marginTop sinpadding custom-select">
                        <select id="subjects">
                            <option>Select Subject</option>
                        </select>
                    </div>
                    <div class="col-xs-12 marginTop sinpadding custom-select">
                        <select id="objectives" class="custom-select">
                            <option>Select Objectives</option>
                        </select>
                    </div>
                    <div class="col-xs-12 marginTop sinpadding custom-select">
                        <input type="checkbox" name="recommend" id="recommend" value="recommend" disabled="true"> Recommend for next presentation
                    </div>
                </div>
                <div id="divHora" class='col-xs-12'>
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
            </div>-->
            <div class="col-xs-9 col-md-10" id="commentsContainer">
                <div class="col-xs-12 pestanas">
                    <ul class=" col-xs-12 col-md-8 col-lg-6 nav nav-tabs">
                        <li class="col-xs-6 sinpadding">
                            <a data-toggle="tab" id="classroomCommentsButton" class=" col-xs-12" style="display: flex;align-items: center;">
                                <div class="col-xs-9 sinpadding" style="text-align: center;">
                                    <spring:message code="etiq.academicObservations"/> 
                                </div>
                                <div class="col-xs-3 sinpadding">
                                    <img class="iconosPestanas" src='../recursos/img/iconos/post-it.svg' alt="<spring:message code='etiq.academicObservations'/> ">
                                </div>
                            </a>
                        </li>

                        <li class=" col-xs-6 sinpadding active">
                            <a data-toggle="tab" id="dayCommentsButton"  class=" col-xs-12" style="display: flex;align-items: center;">
                                <div class="col-xs-9 sinpadding" style="text-align: center;"> 
                                    <spring:message code="etiq.classroomObservations"/> 
                                </div>
                                <div class="col-xs-3 sinpadding">
                                    <img class="iconosPestanas" src='../recursos/img/iconos/computer-tool-for-education.svg' alt="<spring:message code='etiq.classroomObservations'/> " >
                                </div>  
                            </a>
                        </li>

                    </ul>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-12 text-left sinpadding" id="divSubjectObjectives">
                        <div class="col-xs-12 col-md-5">
                            <select id="subjects" class="custom-select form-control">
                                <option><spring:message code="etiq.selectSubject"/></option>
                            </select>
                        </div>
                        <div class="col-xs-12 col-md-5">
                            <select id="objectives" class="custom-select form-control">
                                <option><spring:message code="etiq.selectObject"/> </option>
                            </select>
                        </div>
                        <div class="col-xs-12 col-md-5 form-check">
                            <input class="form-check-input" type="checkbox" name="recommend" id="recommend" value="recommend" disabled="true">
                            <label class="form-check-label" for="recommend"><spring:message code="etiq.recommend"/> </label>
                        </div>
                    </div>
                    <div id="divHora" class='col-xs-12'>
                        <div class="form-group">
                            <label class="control-label" for="fecha"><spring:message code="etiq.Date"/></label>
                            <div class='input-group date' id='fecha'>
                                <input type='text' name="TXTfecha" class="form-control" id="TXTfecha" onselect="loadComments()"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                <!--<div class="col-xs-12 sinpadding" >
                    <div class="col-xs-12 text-left sinpadding">
                        <div class="col-xs-5 sinpadding">    
                            <input id="classroomCommentsButton" title="Class observations" class="imgLeft" type="image" value="classroomComments" src="<c:url value='/recursos/img/iconos/post-it.svg'/>" width="100px">

                        </div>
                    </div>
                    <div class="col-xs-12 text-left sinpadding">
                        <div class="col-xs-5 sinpadding">
                            <input id="dayCommentsButton" class="imgLeft" value="dayComments" title="Academics" type="image"  src="<c:url value='/recursos/img/iconos/computer-tool-for-education.svg'/>" width="100px">
                        </div>
                    </div>
                    <div class="col-xs-12 text-left sinpadding" id="divSubjectObjectives">
                        <div class="col-xs-12 marginTop sinpadding custom-select">
                            <select id="subjects">
                                <option>Select Subject</option>
                            </select>
                        </div>
                        <div class="col-xs-12 marginTop sinpadding custom-select">
                            <select id="objectives" class="custom-select">
                                <option>Select Objectives</option>
                            </select>
                        </div>
                        <div class="col-xs-12 marginTop sinpadding custom-select">
                            <input type="checkbox" name="recommend" id="recommend" value="recommend" disabled="true"> Recommend for next presentation
                        </div>
                    </div>
                    <div id="divHora" class='col-xs-12'>
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
                </div>-->


                <div class="col-xs-12" id="divClassObsv">
                    <input type="hidden" id="lessonid" name="lessonid" value = ${studentId}>
                    <input type="hidden" id="nameStudent" name="nameStudent" value = ${nameStudent}>
                    <div class="col-xs-12">
                        <div class="container col-xs-12">

                            <div class="col-xs-12 firstWeek sinpadding">
                                <div class="col-xs-12" >
                                    <div class="col-xs-12 sinpadding" data-idclass="1919" style="margin-top: 5px;">
                                        <div class="col-xs-12 sinpadding">
                                            <h4><spring:message code="etiq.firstWeek"/></h4>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('1')"></span>
                                    </div>
                                    <div class="col-xs-10 sinpadding" > 
                                        <div class="tam1" id="semana1">
                                        </div>
                                    </div>
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('1')"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12 secondWeek sinpadding">
                                <div class="col-xs-12">
                                    <div class="col-xs-12  sinpadding" data-idclass="1919"  style="margin-top: 5px;">
                                        <div class="col-xs-12 sinpadding">
                                            <h4><spring:message code="etiq.secondWeek"/> </h4>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('2')"></span>
                                    </div>
                                    <div class="col-xs-10 sinpadding" > 
                                        <div  class="semana2 tam2" id="semana2">
                                        </div>
                                    </div>
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('2')"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12 firstWeek sinpadding">
                                <div class="col-xs-12">
                                    <div class="col-xs-12 sinpadding" data-idclass="1919"  style="margin-top: 5px;">
                                        <div class="col-xs-12 sinpadding">
                                            <h4><spring:message code="etiq.thirdWeek"/></h4>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('3')"></span>
                                    </div>
                                    <div class="col-xs-10 sinpadding" > 
                                        <div  class="semana3 tam3" id="semana3">
                                        </div>
                                    </div>
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('3')"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12 secondWeek sinpadding">
                                <div class="col-xs-12" >
                                    <div class="col-xs-12 divClass sinpadding" data-idclass="1919"  style="margin-top: 5px;">
                                        <div class="col-xs-12 sinpadding">
                                            <h4><spring:message code="etiq.fourthWeek"/></h4>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('4')"></span>
                                    </div>
                                    <div class="col-xs-10 sinpadding" > 
                                        <div  class="semana4 tam4" id="semana4">
                                        </div>
                                    </div>
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('4')"></span>
                                    </div>
                                </div>
                            </div>


                            <div class="col-xs-12 firstWeek sinpadding">
                                <div class="col-xs-12">
                                    <div class="col-xs-12 divClass sinpadding" data-idclass="1919" style="margin-top: 5px;">
                                        <div class="col-xs-12 sinpadding">
                                            <h4><spring:message code="etiq.fifthWeek"/> </h4>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('5')"></span>
                                    </div>
                                    <div class="col-xs-10 sinpadding" > 
                                        <div  class="semana3 tam3" id="semana5">
                                        </div>
                                    </div>
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('5')"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12 secondWeek sinpadding">
                                <div class="col-xs-12" >
                                    <div class="col-xs-12 divClass sinpadding" data-idclass="1919" style="margin-top: 5px;">
                                        <div class="col-xs-12 sinpadding">
                                            <h4><spring:message code="etiq.sixthWeek"/> </h4>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('6')"></span>
                                    </div>
                                    <div class="col-xs-10 sinpadding" > 
                                        <div  class="semana4 tam4" id="semana6">
                                        </div>
                                    </div>
                                    <div class="col-xs-1">
                                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('6')"></span>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <!-- Modal confirm delete-->
                        <div id="deleteObservation" class="modal fade" role="dialog">
                            <div class="modal-dialog">

                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header modal-header-delete">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title"><spring:message code="etiq.areUsure"/></h4>
                                    </div>
                                    <div id="lessonDelete" class="modal-body">

                                    </div>
                                    <div class="modal-footer text-center">
                                        <button id="buttonDeleteObservation" type="button" class="btn btn-danger" data-dismiss="modal" onclick='deleteComentario(value)' value=""><h4 class="modal-title"><spring:message code="etiq.yes"/></button>
                                        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="etiq.no"/></button>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <!--<div id="editCommentModal" class="modal fade" role="dialog">
                            <input type='text' name="TXTfecha" class="hide" id="idComentario"/>
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">

                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h2>Enter a classroom observation</h2>
                                    </div>
                                    <div class="modal-body">
                                        <div class="container-fluid">
                                            <div id="contenedorDate">
                                                <div class='col-xs-4'>
                                                    <div class="form-group">
                                                        <label class="control-label" for="fecha">Comment Date</label>
                                                        <div class='input-group date' id='fecha2'>
                                                            <input type='text' name="TXTfecha" class="form-control" id="observationfecha"/>
                                                            <span class="input-group-addon">
                                                                <span class="glyphicon glyphicon-calendar"></span>
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-6 center-block form-group">
                                                <label class="control-label">Observation</label>
                                                <textarea class="form-control" name="TXTdescription" id="observationcomments" placeholder="add comment" maxlength="1000"  spellcheck="true"></textarea>
                                            </div>

                                            <div class="col-xs-12 text-center">
                                                <input type="submit" class="btn btn-primary" id="savecomment"  value="Save" onclick="updateComment()">
                                            </div>
                                            <div class="col-xs-12 text-center hidden" id="error1">
                                                <label>Please select a student first</label>
                                            </div>
                                            <div class="col-xs-12 text-center hidden" id="error2">
                                                <label>Please make sure to fill all data</label>
                                            </div>

                                        </div> 
                                    </div>
                                </div>
                            </div>
                        </div> -->
                        <div id="editCommentModal" class="modal fade" role="dialog">
                            <input type='text' name="TXTfecha" class="hide" id="idComentario"/>
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">

                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h2><spring:message code="etiq.enterClassObs"/> </h2>
                                    </div>
                                    <div class="modal-body">
                                        <div class="container-fluid">
                                            <div id="contenedorDate">
                                                <div class='col-xs-4'>
                                                    <div class="form-group">
                                                        <label class="control-label" for="fecha"><spring:message code="etiq.commentDate"/> </label>
                                                        <div class='input-group date' id='fecha2'>
                                                            <input type='text' name="TXTfecha" class="form-control" id="observationfecha"/>
                                                            <span class="input-group-addon">
                                                                <span class="glyphicon glyphicon-calendar"></span>
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-xs-6 center-block form-group">
                                                    <label class="control-label"><spring:message code="etiq.observation"/> </label>
                                                    <textarea class="form-control" name="TXTdescription" id="observationcomments" placeholder="<spring:message code='etiq.addComment'/>" maxlength="1000"  spellcheck="true"></textarea>
                                                </div>
                                                <div class="col-xs-6 center-block form-group">
                                                    <label class="control-label"><spring:message code="etiq.observationType"/></label>
                                                    <select class="form-control" name="observationtype" id="observationtype" >
                                                        <option value="" selected> <spring:message code="etiq.selectType"/> </option> <!--if you change this value must change as well in savecomment function-->
                                                        <option value="Physical"><spring:message code="etiq.physical"/></option>
                                                        <option value="Intellectual"><spring:message code="etiq.intellectual"/></option>
                                                        <option value="Literacy"><spring:message code="etiq.literacy"/></option>
                                                        <option value="Emotional"><spring:message code="etiq.emotional"/></option>
                                                        <option value="Social"><spring:message code="etiq.social"/></option>
                                                        <option value="myself"><spring:message code="etiq.myself"/></option>
                                                        <option value="Other"><spring:message code="etiq.other"/></option>
                                                    </select>
                                                </div>
                                            </div>  
                                            <div class="col-xs-12" >
                                                <input type="file" id="fileToUpload" accept="image/*">
                                                <div class="col-xs-12 text-left maskFile" >
                                                    <button> 
                                                        <spring:message code="etiq.upload"/> 
                                                        <span class="glyphicon glyphicon-cloud-upload" aria-hidden="true"></span> 
                                                    </button>
                                                    <label> 
                                                        <spring:message code="etiq.fileNotFound"/>
                                                    </label>
                                                </div>
                                            </div>

                                            <div class="col-xs-12 text-center">
                                                <input type="submit" class="btn btn-success" id="savecomment"  value="Save" onclick="updateComment()">
                                            </div>
                                            <div class="col-xs-12 text-center hidden" id="error1">
                                                <label><spring:message code="etiq.pleaseSelect"/></label>
                                            </div>
                                            <div class="col-xs-12 text-center hidden" id="error2">
                                                <label><spring:message code="etiq.pleaseMake"/> </label>
                                            </div>

                                        </div> 
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="confirmsave" class="modal fade" role="dialog">
                        <div class="modal-dialog">

                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title"><spring:message code="etiq.commentSaved"/> </h4>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div id="showComment" class="modal fade" role="dialog">
                        <div class="modal-dialog modal-lg">

                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header ">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title colorSuccess"><spring:message code="etiq.moreInf"/></h4>
                                </div>
                                <div class="modal-body">
                                    <div class="container-fluid">
                                        <div class="col-xs-12">
                                            <div class="col-xs-3 center-block form-group">
                                                <label class="control-label"><spring:message code="etiq.commentDate"/>:</label>
                                                <div id="idCommentDate"></div>                        
                                            </div>

                                            <div class="col-xs-3 center-block form-group">
                                                <label class="control-label"><spring:message code="etiq.createdOn"/>:</label>
                                                <div id="idCreateDate"></div>                        
                                            </div>   
                                            <div class="col-xs-3 center-block form-group">
                                                <label class="control-label"><spring:message code="etiq.type"/>:</label>
                                                <div id="idTypeComment"></div>                        
                                            </div>
                                            <div class="col-xs-3 center-block form-group">
                                                <label class="control-label"><spring:message code="etiq.teacher"/>:</label>
                                                <div id="idTeacher"></div>                        
                                            </div> 
                                        </div> 

                                        <div  class="col-xs-12">
                                            <div class="col-xs-12 form-group">
                                                <label class="control-label"><spring:message code="etiq.comment"/>:</label>
                                                <div id="idComment"></div>                        
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div> 

                    <div class="modal fade" id="modalimagen" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header col ">
                                    <button type="button"  onclick="deletePhoto()" class='btn btn-link'  value='' id='deleteFoto'><spring:message code="etiq.delete"/></button>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    <h4 class="modal-title colorSuccess" id="titleComment"></h4>
                                </div>
                                <div class="modal-body">
                                    <img id="imagen" class="foto" src=""/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 text-right">
                        <input class="btn-lg newcomment" type="image" id="newClassRoom" src="<c:url value='/recursos/img/iconos/add-comment(1).svg'/>" width="100px">
                    </div>
                </div>
                <div class="col-xs-12 col-md-12" id="divNotas">

                    <div class="col-xs-12 firstWeekNotas ">
                        <div class="col-xs-12 sinpadding" > 
                            <div class="semana1" id="semana0">
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 text-right">
                        <input class="btn-lg newcomment" type="image" id="newcomment" src="<c:url value='/recursos/img/iconos/add-comment(1).svg'/>" width="100px">
                    </div>
                </div>


                <div id="newClassRoomModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title colorSuccess" ><spring:message code="etiq.enterClassObs"/></h4>
                            </div>
                            <div class="modal-body text-center">
                                <div class="row">
                                    <div class='col-xs-6 form-group'>
                                        <label class="control-label" for="fechaClassroom"><spring:message code="etiq.Date"/></label>
                                        <div class='input-group date' id='fechaClassroom'>
                                            <input type='text' name="TXTfecha" class="form-control" id="observationfechaClassroom"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="col-xs-6 center-block form-group">
                                        <label class="control-label"><spring:message code="etiq.observationType"/></label>
                                        <select class="form-control" name="observationtype" id="observationtypeClassroom" >
                                            <option value="" selected> <spring:message code="etiq.selectType"/> </option> <!--if you change this value must change as well in savecomment function-->
                                            <option value="Physical"><spring:message code="etiq.physical"/></option>
                                            <option value="Intellectual"><spring:message code="etiq.intellectual"/></option>
                                            <option value="Literacy"><spring:message code="etiq.literacy"/></option>
                                            <option value="Emotional"><spring:message code="etiq.emotional"/></option>
                                            <option value="Social"><spring:message code="etiq.social"/></option>
                                            <option value="myself"><spring:message code="etiq.myself"/></option>
                                            <option value="Other"><spring:message code="etiq.other"/></option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row center-block form-group">
                                    <label class="control-label"><spring:message code="etiq.observation"/></label>
                                    <textarea class="form-control" name="TXTdescription" id="observationcommentsClassroom" placeholder="<spring:message code="etiq.addComment"/>" maxlength="1000"  spellcheck="true"></textarea>
                                </div>

                                <div class="row  center-block form-group" >
                                    <input type="file" id="fileToUploadClassroom" accept="image/*">
                                    <div class="col-xs-12 text-left maskFile" >
                                        <button> 
                                            <spring:message code="etiq.upload"/> 
                                            <span class="glyphicon glyphicon-cloud-upload" aria-hidden="true"></span> 
                                        </button>
                                        <label> 
                                            <spring:message code="etiq.fileNotFound"/>
                                        </label>
                                    </div>
                                </div>
                                <div class="row text-center hidden" id="error1">
                                    <label><spring:message code="etiq.pleaseSelect"/></label>

                                </div>
                                <div class="row text-center hidden" id="error2">
                                    <label><spring:message code="etiq.pleaseMake"/></label>
                                </div>
                                <div class="row text-center ">
                                    <div class="col-xs-12 text-center">
                                        <button type="button" class="btn btn-primary" id="savecommentClassroom"  value="Save" onclick="saveobservation()"><spring:message code="etiq.saveObservation"/></button>
                                    </div>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>

                <div id="confirmsaveClassroom" class="modal fade" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title"><spring:message code="etiq.commentSaved"/> </h4>
                            </div>

                        </div>

                    </div>
                </div> 
                <!--<h1>Hello World!</h1>-->

                <div id="commentModal" class="modal fade" role="dialog">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title colorSuccess" id="myNewCommentTitle"></h4>
                            </div>
                            <div class="modal-body text-center clearfix">
                                <div class="col-xs-6">
                                    <div class="col-xs-12">
                                        <label><spring:message code="etiq.steps"/></label>
                                    </div>
                                    <div id="steps_show" class="col-xs-12 text-left"></div>
                                    <div class="col-xs-12 text-left hidden" style="margin-top: 15px;">
                                        <input type="checkbox" id="cbUseGrade" value="cbUseGrade" checked> <label for="cbUseGrade"><spring:message code="etiq.useGrade"/></label>
                                    </div>
                                </div>
                                <div class="col-xs-6">


                                    <div class="speech">
                                        <textarea style="width:100%;" rows="7" id="commentcontent" required="required"></textarea>
                                        <img onclick="startDictation()" src="//i.imgur.com/cHidSVu.gif" />
                                    </div>
                                    <select name="TXTrating" id="hi" class="studentRating rating" style="margin-top:10px">
                                        <option></option>
                                        <option value="N/A"><spring:message code="etiq.na"/></option>
                                        <option value="Presented"><spring:message code="etiq.presented"/></option>
                                        <option value="Attempted"><spring:message code="etiq.attempted"/></option>
                                        <option value="Mastered"><spring:message code="etiq.mastered"/></option>
                                    </select>
                                    <input type="number" name="steps" id="some_id" class="rating" data-clearable="X" data-icon-lib="iconsAragon fa" data-active-icon="icon-Pie_PieIzqSelect" data-inactive-icon="icon-Pie_PieIzqUnSelect" data-clearable-icon="fa-null" data-max="15" data-min="1" value="0" />
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button id="commentbutton" class="btn btn-primary" data-dismiss="modal" aria-label="Close" value="Comment"><spring:message code="etiq.comment"/></button>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="editModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title colorSuccess" id="myModalLabel"><spring:message code="etiq.editComment"/></h4>
                            </div>
                            <div id="steps_show2" class="col-xs-6"></div>
                            <div class="modal-body text-center">
                                <textarea style="width:100%;" rows="7" id="commentcontent2" required="required"></textarea>
                                <select name="TXTrating2" id="hi2" class="studentRating rating">
                                    <option></option>
                                    <option value="N/A"><spring:message code="etiq.na"/>N/A</option>
                                    <option value="Presented"><spring:message code="etiq.presented"/></option>
                                    <option value="Attempted"><spring:message code="etiq.attempted"/></option>
                                    <option value="Mastered"><spring:message code="etiq.mastered"/></option>
                                </select>
                                <input type="number" name="steps2" id="some_id" class="rating editrating" data-clearable="X" data-icon-lib="iconsAragon fa" data-active-icon="icon-Pie_PieIzqSelect" data-inactive-icon="icon-Pie_PieIzqUnSelect" data-clearable-icon="fa-null" data-max="15" data-min="1" value="0" />
                            </div>
                            <div class="modal-footer">
                                <input type="hidden" id="idedit">
                                <button id="editcomment" class="btn btn-primary btn-lg" data-dismiss="modal" aria-label="Close"><spring:message code="etiq.edit"/></button>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="deleteModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <p class="modal-title" id="myModalLabel"><spring:message code="etiq.deleteComment"/></p>
                            </div>
                            <div id="steps_show2" class="col-xs-6"></div>
                            <div class="modal-body text-center">
                                <h3><spring:message code="etiq.areUsure"/></h3>
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
                                <h4 class="modal-title colorSuccess" id="myModalLabel"><spring:message code="etiq.Details"/></h4>
                            </div>
                            <div id="commentcomplete" class="modal-body text-left " style="margin:5px;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </body>
</html>
