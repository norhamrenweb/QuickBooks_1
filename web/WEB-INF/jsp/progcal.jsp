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


    <head>
        <title>Classroom Observation</title>

        <script type="text/javascript">

            var ajax;
            var userId = ${user.id};
            var userType = ${user.type};
            $(document).ready(function () {
                var userLang = navigator.language || navigator.userLanguage;
                var myDate = new Date();
                $(".divClass").click(function () {


                    var idCLass = $(this).attr('data-idclass');
                    window.location.replace("<c:url value="/gradebook/loadRecords.htm?term=1&ClassSelected="/>" + idCLass);
                });
                //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE              

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
                $('#fecha').on('dp.change', function (e) {
                    loadComments();
                })
                loadComments();
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
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        var object = JSON.parse(ajax.responseText);
                        var s = JSON.parse(object.students);
                        var c = JSON.parse(object.contents);
                        $('#nameLessonDetails').empty();
                        $('#nameLessonDetails').append('Details ' + nameLessons);
                        //$('#detailsStudents').empty();
                        $('#studentarea').append('<table id="detailsStudents" class="table table-striped">');
                        $.each(s, function (i, student) {
                            $('#detailsStudents').append('<tr><td class="studentDetails">' + s[i].studentname + '</td></tr>');
                            $("tr:odd").addClass("par");
                            $("tr:even").addClass("impar");
                            //    $("tr:odd").css("background-color", "lightgray");
                        });
                        $('#contentDetails').empty();
                        $.each(c, function (i, content) {
                            $('#contentDetails').append('<li>' + c[i] + '</li>');
                        });
                        $('#methodDetails').empty();
                        $('#methodDetails').append('<tr><td>' + object.method + '</td></tr>');
                        $('#commentDetails').empty();
                        $('#commentDetails').append('<tr><td>' + object.comment + '</td></tr>');
                        $('#detailsLesson').modal('show');
                        //                        });

                    }
                }
            }

            function updateComment() {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }


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
                $.ajax({
                    type: 'POST',
                    url: 'updateComment.htm',
                    data: json,
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        //var j = JSON.parse(data);
                        id = data;
                        //MODIFICAR EL JSON PARA QUE DEVUELVA EL ID OBTENIDO 
                        $('#editComment').modal('hide');
                        loadComments();
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }

                });
            }

            function editComentario(id) {
                var comment = $('#editComentario' + id).data('comment');
                var createDate = $('#editComentario' + id).data('createdate');
                var type = $('#editComentario' + id).data('type');
                var commentDate = $('#editComentario' + id).data('commentdate');
                $('#observationfecha').val(commentDate);
                $('#observationtype').val(type);
                $('#observationcomments').val(comment);
                $('#idComentario').val(id);
                $('#editComment').modal('show');
            }
            function ConfirmDeleteComentario(val) {
                $('#deleteObservation').modal('show');
                $('#buttonDeleteObservation').val(val);
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
                            $('#comment' + id).parent().append("<div class='divAdd'>No comments this week</div>");
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
                myObj["studentid"] = $('#lessonid').val();
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
                        $("#semana5").parent().parent().hide();
                        $("#semana6").parent().parent().hide();
                        
                        var weeksCount = weeksInAMonth($("#TXTfecha").val().split("-")[0],$("#TXTfecha").val().split("-")[1]);
                        if(weeksCount > 4){
                             $("#semana5").parent().parent().show();
                             if(weeksCount > 5) $("#semana6").parent().parent().show();
                        }
                        $.each(j, function (i, value) {
                            var f = value;      
                            $.each(f, function (i2, value2) {
                                var id = value2.id;
                                var comentario = value2.observation;
                                var comentarioExtenso = '';
                                if (comentario.length >= 57) {
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
                                if (userId !== idTeacher && userType !== 0) {
                                    disable = "disabled='disabled'";
                                }

                                if (dayWeek === "1") { //PRIMERA SEMANA
                                    cont1 = cont1 + 180;
                                    if (cont1 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                } else if (dayWeek === "2") { //SEGUNDA SEMANA
                                    visible = "";
                                    numSemana = "#semana2";
                                    cont2 = cont2 + 180;
                                    if (cont2 > $(numSemana).width())
                                    {
                                        visible = "hide";
                                    }
                                } else if (dayWeek === "3") {//TERCERA SEMANA
                                    visible = "";
                                    numSemana = "#semana3";
                                    cont3 = cont3 + 180;
                                    if (cont3 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                } else if (dayWeek === "4") { //CUARTA SEMANA
                                    visible = "";
                                    numSemana = "#semana4";
                                    cont4 = cont4 + 180;
                                    if (cont4 > $(numSemana).width())
                                    {
                                        visible = "hide";
                                    }
                                } else if (dayWeek === "5") {//QUINTA SEMANA
                                    visible = "";
                                    numSemana = "#semana5";
                                    cont5 = cont5 + 180;
                                    if (cont5 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                } else { // SEXTA SEMANA
                                    visible = "";
                                    numSemana = "#semana6";
                                    cont6 = cont6 + 180;
                                    if (cont6 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                }
            <%--Create Date: " + fechaCreacion + "<br>\n\
            Type: " + category + "<br>\n\--%>
                                $(numSemana).append("<div id='comment" + id + "' class='divAdd " + visible + "'>\n\
                                <strong>Date:</strong> " + commentdate + "<br>\n\
                                <strong>Observation:</strong> " + comentario.substring(0, 57) + " " + comentarioExtenso + "<br>\n\
\n\<div class='col-xs-12 text-center sinpadding optionsObservations'>\n\
<div class='col-xs-4 text-center sinpadding'>\n\
<button type='button' class='btn btn-link showMore'  data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "'>\n\
<span class='glyphicon glyphicon-list-alt'></span>\n\
</button>\n\
</div>\n\
<div class='col-xs-4 text-center sinpadding'>\n\
<button type='button' " + disable + " class='btn btn-link' data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "' onclick='editComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editComentario" + id + "'>\n\
<span class='glyphicon glyphicon-pencil'></span>\n\
</button>\n\
</div>\n\
<div class='col-xs-4 text-center sinpadding'>\n\
<button type='button'  " + disable + " class='btn btn-link' onclick='ConfirmDeleteComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='delete' id='ConfirmDeleteComentario" + id + "'>\n\
<span class=' glyphicon glyphicon-remove'></span>\n\
</button>\n\
</div>\n\
</div>\n\
</div>");
                            });
                            /*
                             var semana = "";
                             var cont1 = 0;
                             var cont2 = 0;
                             var cont3 = 0;
                             var cont4 = 0;
                             var oculto = "hide";
                             $("#semana1").empty();
                             $("#semana2").empty();
                             $("#semana3").empty();
                             $("#semana4").empty();
                             
                             
                             $.each(j, function (i, value) {
                             var f = value;
                             
                             $.each(f, function (i2, value2) {
                             var id = value2.id;
                             var comentario = value2.observation;
                             var comentarioExtenso = '';
                             if (comentario.length >= 57) {
                             var comentarioExtenso = '...';
                             }
                             var fechaCreacion = value2.date;
                             var category = value2.type;
                             var commentdate = value2.commentDate;
                             var visible = "";
                             var nameTeacher = value2.nameTeacher;
                             var disable = "";
                             var idTeacher = value2.logged_by;
                             
                             if (userId !== idTeacher && userType !== 0) {
                             disable = "disabled='disabled'";
                             }
                             
                             if (i <= 7) {
                             //semana= "semana1" REDUCIR CODIGO
                             cont1 = cont1 + 180;
                             if (cont1 > $("#semana1").width()) {
                             visible = "hide";
                             }
                             
                             <%--Create Date: " + fechaCreacion + "<br>\n\
Type: " + category + "<br>\n\--%>
                             $("#semana1").append("<div id='comment" + id + "' class='divAdd " + visible + "'>\n\
                             <strong>Date:</strong> " + commentdate + "<br>\n\
                             <strong>Observation:</strong> " + comentario.substring(0, 57) + " " + comentarioExtenso + "<br>\n\
                             \n\<div class='col-xs-12 text-center sinpadding optionsObservations'>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' class='btn btn-link showMore'  data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "'>\n\
                             <span class='glyphicon glyphicon-list-alt'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' " + disable + " class='btn btn-link' data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "' onclick='editComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editComentario" + id + "'>\n\
                             <span class='glyphicon glyphicon-pencil'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button'  " + disable + " class='btn btn-link' onclick='ConfirmDeleteComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='delete' id='ConfirmDeleteComentario" + id + "'>\n\
                             <span class=' glyphicon glyphicon-remove'></span>\n\
                             </button>\n\
                             </div>\n\
                             </div>\n\
                             </div>");
                             } else {
                             if (i <= 14) {
                             //semana= "semana2"
                             cont2 = cont2 + 180;
                             if (cont2 > $("#semana2").width())
                             {
                             visible = "hide";
                             }
                             $("#semana2").append("<div id='comment" + id + "' class='divAdd " + visible + "'>\n\
                             <strong>Date:</strong> " + commentdate + "<br>\n\
                             <strong>Observation:</strong> " + comentario.substring(0, 57) + " " + comentarioExtenso + "<br>\n\
                             \n\<div class='col-xs-12 text-center sinpadding optionsObservations'>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' class='btn btn-link showMore'  data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "'>\n\
                             <span class='glyphicon glyphicon-list-alt'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' " + disable + " class='btn btn-link' data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "' onclick='editComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editComentario" + id + "'>\n\
                             <span class='glyphicon glyphicon-pencil'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' " + disable + " class='btn btn-link' onclick='ConfirmDeleteComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='delete' id='ConfirmDeleteComentario" + id + "'>\n\
                             <span class=' glyphicon glyphicon-remove'></span>\n\
                             </button>\n\
                             </div>\n\
                             </div>\n\
                             </div>");
                             } else {
                             if (i <= 21) {
                             // semana= "semana3"
                             cont3 = cont3 + 180;
                             if (cont3 > $("#semana3").width()) {
                             visible = "hide";
                             }
                             
                             $("#semana3").append("<div id='comment" + id + "' class='divAdd " + visible + "'>\n\
                             <strong>Date:</strong> " + commentdate + "<br>\n\
                             <strong>Observation:</strong> " + comentario.substring(0, 57) + " " + comentarioExtenso + "<br>\n\
                             \n\<div class='col-xs-12 text-center sinpadding optionsObservations'>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' class='btn btn-link showMore'  data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "'>\n\
                             <span class='glyphicon glyphicon-list-alt'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' " + disable + " class='btn btn-link' data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "' onclick='editComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editComentario" + id + "'>\n\
                             <span class='glyphicon glyphicon-pencil'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' " + disable + " class='btn btn-link' onclick='ConfirmDeleteComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='delete' id='ConfirmDeleteComentario" + id + "'>\n\
                             <span class=' glyphicon glyphicon-remove'></span>\n\
                             </button>\n\
                             </div>\n\
                             </div>\n\
                             </div>");
                             
                             } else {
                             //semana= "semana4"
                             cont4 = cont4 + 180;
                             if (cont4 > $("#semana4").width()) {
                             visible = "hide";
                             }
                             
                             $("#semana4").append("<div id='comment" + id + "' class='divAdd " + visible + "'>\n\
                             <strong>Date:</strong> " + commentdate + "<br>\n\
                             <strong>Observation:</strong> " + comentario.substring(0, 57) + " " + comentarioExtenso + "<br>\n\
                             \n\<div class='col-xs-12 text-center sinpadding optionsObservations'>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' class='btn btn-link showMore'  data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "'>\n\
                             <span class='glyphicon glyphicon-list-alt'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' " + disable + " class='btn btn-link' data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "' onclick='editComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editComentario" + id + "'>\n\
                             <span class='glyphicon glyphicon-pencil'></span>\n\
                             </button>\n\
                             </div>\n\
                             <div class='col-xs-4 text-center sinpadding'>\n\
                             <button type='button' " + disable + " class='btn btn-link' onclick='ConfirmDeleteComentario(" + id + ")' data-toggle='tooltip' data-placement='bottom' value='delete' id='ConfirmDeleteComentario" + id + "'>\n\
                             <span class=' glyphicon glyphicon-remove'></span>\n\
                             </button>\n\
                             </div>\n\
                             </div>\n\
                             </div>");
                             }
                             }
                             }
                             });*/

                        });
                        if (cont1 === 0)
                            $("#semana1").append("<div class='divAdd'>No comments this week</div>");
                        if (cont2 === 0)
                            $("#semana2").append("<div class='divAdd'>No comments this week</div>");
                        if (cont3 === 0)
                            $("#semana3").append("<div class='divAdd'>No comments this week</div>");
                        if (cont4 === 0)
                            $("#semana4").append("<div class='divAdd'>No comments this week</div>");
                        if (cont5 === 0)
                            $("#semana5").append("<div class='divAdd'>No comments this week</div>");
                        if (cont6 === 0)
                            $("#semana6").append("<div class='divAdd'>No comments this week</div>");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }

                });
            }
            
            function weeksInAMonth(year, month_number) {
                            var firstOfMonth = new Date(year, month_number - 1, 1);
                            var day = firstOfMonth.getDay() || 6;
                            day = day === 1 ? 0 : day;
                            if (day) { day-- }
                            var diff = 7 - day;
                            var lastOfMonth = new Date(year, month_number, 0);
                            var lastDate = lastOfMonth.getDate();
                            if (lastOfMonth.getDay() === 1) {
                                diff--;
                            }
                            var result = Math.ceil((lastDate - diff) / 7);
                            return result + 1;
                        };
                        
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
                color: #777777;
                height: 110px;
                width: 180px;
                background-color: rgba(255,255,255,0.5);
                margin-right: 10px;
                font-size: 12px;
                padding: 5px;
                display: line;
                float: left;
                position: relative;
            }
            .optionsObservations{
                position: absolute;
                bottom: 0px;
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
            .collapse {
                display: none;
                width: 180px;
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
            .btn-link
            {
                color: #777777 !important;
            }
        </style>
    </head>
    <body>
        <input type="hidden" id="lessonid" name="lessonid" value = ${studentId}>
        <input type="hidden" id="nameStudent" name="nameStudent" value = ${nameStudent}>
        <div class="col-xs-12">
            <div class="col-sm-12" id="maincontainer">
                <div class="col-sm-12 center-block text-center">
                    <h1>CLASSROOM OBSERVATIONS</h1>
                </div>
                <div class="col-sm-12 center-block text-center">
                    <h2>${nameStudent}</h2> 
                </div>
            </div>
            <div class="container">
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
                    /* $(window).resize(function () {
                     loadComments();
                     
                     });*/
    
                    var resizeId;
                    $(window).resize(function () {
                        clearTimeout(resizeId);
                        resizeId = setTimeout(doneResizing, 500);
                    });


                    function doneResizing() {
                         loadComments();
                    }
                    
                    function moverDrech(x)
                    {
                        if ($("#semana" + x).children().not(".hide").length > 1) {
                            $("#semana" + x).children().not(".hide").next().removeClass("hide");
                            $("#semana" + x).children().not(".hide").first().addClass("hide");
                        }
                    }
                    function moverIzq(x)
                    {
                        $("#semana" + x).children().not(".hide").prev().removeClass("hide");
                        if ($("#semana" + x).children().not(".hide").length * 180 > $("#semana" + x).width()) {
                            $("#semana" + x).children().not(".hide").last().addClass("hide");
                        }
                        if ($("#semana" + x).children().not(".hide").length === 0) {
                            $("#semana" + x).children().last().removeClass("hide");
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
                    <div class="col-xs-1">
                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('4')"></span>
                    </div>
                </div>


                <div class="col-xs-12 firstWeek sinpadding">
                    <div class="col-xs-2">
                        <div class="col-xs-12 divClass sinpadding" data-idclass="1919">
                            <div class="col-xs-12 sinpadding">
                                <h4>Fifth Week</h4>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-1">
                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('5')"></span>
                    </div>
                    <div class="col-xs-8 sinpadding" > 
                        <div  class="semana3 tam3" id="semana5">
                        </div>
                    </div>
                    <div class="col-xs-1">
                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('5')"></span>
                    </div>
                </div>
                <div class="col-xs-12 secondWeek sinpadding">
                    <div class="col-xs-2" >
                        <div class="col-xs-12 divClass sinpadding" data-idclass="1919">
                            <div class="col-xs-12 sinpadding">
                                <h4>Sixth Week</h4>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-1">
                        <span class="glyphicon glyphicon-chevron-left carousel-control" onclick="moverIzq('6')"></span>
                    </div>
                    <div class="col-xs-8 sinpadding" > 
                        <div  class="semana4 tam4" id="semana6">
                        </div>
                    </div>
                    <div class="col-xs-1">
                        <span class="glyphicon glyphicon-chevron-right carousel-control" onclick="moverDrech('6')"></span>
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
                            <h4 class="modal-title">are you sure you want to delete?</h4>
                        </div>
                        <div id="lessonDelete" class="modal-body">

                        </div>
                        <div class="modal-footer text-center">
                            <button id="buttonDeleteObservation" type="button" class="btn btn-danger" data-dismiss="modal" onclick='deleteComentario(value)' value="">Yes</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                        </div>
                    </div>

                </div>
            </div>

            <script>

                $(document).on("click", ".showMore", function () {
                    var comment = $(this).data('comment');
                    var createDate = $(this).data('createdate');
                    var type = $(this).data('type');
                    var commentDate = $(this).data('commentdate');
                    var nameTeacher = $(this).data('nameteacher');
                    $('#idCommentDate').text(commentDate);
                    $('#idCreateDate').text(createDate);
                    $('#idTypeComment').text(type);
                    $('#idComment').text(comment);
                    $('#idTeacher').text(nameTeacher);
                    $('#showComment').modal('show');
                });
            </script>

            <div id="editComment" class="modal fade" role="dialog">
                <input type='text' name="TXTfecha" class="hide" id="idComentario"/>
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">

                        <div class="modal-header modal-header-details">
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
                                    <div class="col-xs-6 center-block form-group">
                                        <label class="control-label">Observation</label>
                                        <textarea class="form-control" name="TXTdescription" id="observationcomments" placeholder="add comment" maxlength="1000"></textarea>
                                    </div>
                                    <div class="col-xs-6 center-block form-group">
                                        <label class="control-label">Observation type</label>
                                        <select class="form-control" name="observationtype" id="observationtype" >
                                            <option value="" selected>Select type</option> <!--if you change this value must change as well in savecomment function-->
                                            <option value="Physical">Physical</option>
                                            <option value="Intellectual">Intellectual</option>
                                            <option value="Literacy">Literacy</option>
                                            <option value="Emotional">Emotional</option>
                                            <option value="Social">Social</option>
                                        </select>
                                    </div>
                                </div>  
                                <div class="col-xs-12 text-center">
                                    <input type="submit" class="btn btn-success" id="savecomment"  value="Save" onclick="updateComment()">
                                </div>
                                <div class="col-xs-12 text-center hidden" id="error1">
                                    <label>Please select a student first</label>
                                </div>
                                <div class="col-xs-12 text-center hidden" id="error2">
                                    <label>Please make sure to fill all data</label>
                                </div>
                                <div id="confirmsave" class="modal fade" role="dialog">
                                    <div class="modal-dialog">

                                        <!-- Modal content-->
                                        <div class="modal-content">
                                            <div class="modal-header modal-header-delete">
                                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                <h4 class="modal-title">comment saved</h4>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div> 
                        </div>
                    </div>
                </div>
            </div> 
        </div>

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
                                <div class="col-xs-3 center-block form-group">
                                    <label class="control-label">Comment Date:</label>
                                    <div id="idCommentDate"></div>                        
                                </div>

                                <div class="col-xs-3 center-block form-group">
                                    <label class="control-label">Created on:</label>
                                    <div id="idCreateDate"></div>                        
                                </div>   
                                <div class="col-xs-3 center-block form-group">
                                    <label class="control-label">Type:</label>
                                    <div id="idTypeComment"></div>                        
                                </div>
                                <div class="col-xs-3 center-block form-group">
                                    <label class="control-label">Teacher:</label>
                                    <div id="idTeacher"></div>                        
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