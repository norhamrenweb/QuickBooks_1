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
        <title><spring:message code="etiq.classroomObservations"/></title>

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
                /*
                 $('[data-toggle="popover"]').popover({
                 html: true,
                 trigger: 'hover',
                 placement: 'bottom',
                 content: function(){return '<img src="https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg" />';}
                 });*/


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

                for (var j = 0; j < 1; j++) {
                    if (j === 1)
                        path = path.substring(0, i);
                    while (path[i] !== '/') {
                        path = path.substring(0, i);
                        i--;
                    }
                }
                loadComments();
                $('#editComment').modal('hide');
                $('#confirmsave').modal('show');
                path = path + "savecomment";
                var request = new XMLHttpRequest();
                request.open("POST", path);
                request.send(data);


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
                        var noCommentThisWeek = "<spring:message code="etiq.NoCommentWeek"/>";
                        //var j = JSON.parse(data);   
                        if ($('#comment' + id).parent().children().length === 1) {
                            $('#comment' + id).parent().append("<div class='divAdd'>"+noCommentThisWeek+"</div>");
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

                        var weeksCount = weeksInAMonth($("#TXTfecha").val().split("-")[0], $("#TXTfecha").val().split("-")[1]);
                        if (weeksCount > 4) {
                            $("#semana5").parent().parent().show();
                            if (weeksCount > 5)
                                $("#semana6").parent().parent().show();
                        }
                        $.each(j, function (i, value) {
                            var f = value;
                            $.each(f, function (i2, value2) {
                                var id = value2.id;
                                var comentario = value2.observation;
                                var comentarioExtenso = '';
                                if (comentario.length >= 90) {
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
                                var anchoDiv = 200;
                                var disableFoto = "";

                                if (booleanFoto === false)
                                    disableFoto = "disabled='disabled'"
                                if (userId !== idTeacher && userType !== 0) {
                                    disable = "disabled='disabled'";
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
                                } else if (dayWeek === "3") {//TERCERA SEMANA
                                    visible = "";
                                    numSemana = "#semana3";
                                    cont3 = cont3 + anchoDiv;
                                    if (cont3 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                } else if (dayWeek === "4") { //CUARTA SEMANA
                                    visible = "";
                                    numSemana = "#semana4";
                                    cont4 = cont4 + anchoDiv;
                                    if (cont4 > $(numSemana).width())
                                    {
                                        visible = "hide";
                                    }
                                } else if (dayWeek === "5") {//QUINTA SEMANA
                                    visible = "";
                                    numSemana = "#semana5";
                                    cont5 = cont5 + anchoDiv;
                                    if (cont5 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
                                } else { // SEXTA SEMANA
                                    visible = "";
                                    numSemana = "#semana6";
                                    cont6 = cont6 + anchoDiv;
                                    if (cont6 > $(numSemana).width()) {
                                        visible = "hide";
                                    }
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
                                                        <div class='project project-radius project-default'>\n\
                                                                                <div class='shape'>	\n\
                                                                                    <div class='shape-text'></div>\n\
                                                                                </div>\n\
                                                                                <div class='project-content projectProgcal'>\n\
                                                                                    \n\<strong>"+dateEtiq+":</strong> " + commentdate + " </strong> <br>\n\
                                                                                    <strong>"+obsEtiq+":</strong> " + comentario.substring(0, 90) + " " + comentarioExtenso + "<br>\n\
                                                                                    \n\<div class='col-xs-12 text-center sinpadding optionsObservations'>\n\
                                                                                    <div class='col-xs-3 text-center sinpadding'>\n\
                                                                                    <button type='button' class='btn btn-link showMore'  data-nameTeacher='" + nameTeacher + "' data-comment='" + comentario + "' data-createdate='" + fechaCreacion.toString() + "' data-type='" + category + "' data-commentdate='" + commentdate + "'>\n\
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
                                                                                    <span class=' glyphicon glyphicon-camera'></span></button>\n\</div>\n\</div>\n\</div>\n\</div>\n\</div>");
                            });

                        });
                        if (cont1 === 0)
                            $("#semana1").append(divVacio());
                        if (cont2 === 0)
                            $("#semana2").append(divVacio());
                        if (cont3 === 0)
                            $("#semana3").append(divVacio());
                        if (cont4 === 0)
                            $("#semana4").append(divVacio());
                        if (cont5 === 0)
                            $("#semana5").append(divVacio());
                        if (cont6 === 0)
                            $("#semana6").append(divVacio());

                        $(".popOverFoto").mouseover(function () {
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
                        });
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
          
            function divVacio(){
             var dateEtiq = "<spring:message code="etiq.NoCommentWeek"/>";
                               
             return "<div class='divAdd'><div class='project project-radius project-default'>\n\
                                                        <div class='shape'>\n\
                                                            <div class='shape-text'></div>\n\
                                                        </div>\n\
                                                        <div class='project-content'>\n\
                                                        "+dateEtiq+"\n\
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

        </script>
        <style>

            .projectProgcal{
                padding: 4px !important;
            }
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
                width: 200px;
                /* background-color: rgba(255,255,255,0.5);*/
                margin-right: 10px;
                font-size: 12px;
                padding: 5px;
                display: line;
                float: left;
                position: relative;
            }
            .optionsObservations{
                position: absolute;
                top: 75%;
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
                border: #99CC66 solid 3px;
                background-color: #9C63;
                border-radius: 10px;
            }
            h4
            {
                color: black;
                font-size: 20px;
            }
            .collapse {
                display: none;
                width: 180px;
            }

            .secondWeek
            {
                margin-top: 5px;
                border: #cdcdcd solid 3px;
                background-color: #cdcdcd63;;
                border-radius: 10px;
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
            .foto{
                width: 100% !important;
            }
            .container{
                width: 1300px;     
            }
        </style>
    </head>
    <body>
        <input type="hidden" id="lessonid" name="lessonid" value = ${studentId}>
        <input type="hidden" id="nameStudent" name="nameStudent" value = ${nameStudent}>
        <div class="col-xs-12">
            <div class="col-sm-12" id="maincontainer">
                <div class="col-sm-12 center-block text-center">
                    <h1><spring:message code="etiq.classroomObservations"/></h1>
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
                        if ($("#semana" + x).children().not(".hide").length * 200 > $("#semana" + x).width()) {
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
                                <h4><spring:message code="etiq.firstWeek"/></h4>
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
                                <h4><spring:message code="etiq.secondWeek"/></h4>
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
                                <h4><spring:message code="etiq.thirdWeek"/></h4>
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
                                <h4><spring:message code="etiq.fourthWeek"/></h4>
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
                                <h4><spring:message code="etiq.fifthWeek"/></h4>
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
                                <h4><spring:message code="etiq.sixthWeek"/></h4>
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
                            <h4 class="modal-title"><spring:message code="etiq.areUsure"/></h4>
                        </div>
                        <div id="lessonDelete" class="modal-body">

                        </div>
                        <div class="modal-footer text-center">
                            <button id="buttonDeleteObservation" type="button" class="btn btn-danger" data-dismiss="modal" onclick='deleteComentario(value)' value=""><spring:message code="etiq.yes"/></button>
                            <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="etiq.no"/></button>
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
                            <h2><spring:message code="etiq.enterClassObs"/></h2>
                        </div>
                        <div class="modal-body">
                            <div class="container-fluid">
                                <div id="contenedorDate">
                                    <div class='col-xs-4'>
                                        <div class="form-group">
                                            <label class="control-label" for="fecha"><spring:message code="etiq.commentDate"/></label>
                                            <div class='input-group date' id='fecha2'>
                                                <input type='text' name="TXTfecha" class="form-control" id="observationfecha"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-xs-6 center-block form-group">
                                        <label class="control-label"><spring:message code="etiq.observation"/></label>
                                        <textarea class="form-control" name="TXTdescription" id="observationcomments" placeholder="add comment" maxlength="1000"  spellcheck="true"></textarea>
                                    </div>
                                    <div class="col-xs-6 center-block form-group">
                                        <label class="control-label"><spring:message code="etiq.observationType"/></label>
                                        <select class="form-control" name="observationtype" id="observationtype" >
                                            <option value="" selected> <spring:message code="etiq.selectType"/></option> <!--if you change this value must change as well in savecomment function-->
                                         <option value="Physical"><spring:message code="etiq.physical"/></option>
                                        <option value="Intellectual"><spring:message code="etiq.intellectual"/></option>
                                        <option value="Literacy"><spring:message code="etiq.literacy"/></option>
                                        <option value="Emotional"><spring:message code="etiq.emotional"/></option>
                                        <option value="Social"><spring:message code="etiq.social"/></option>
                                        <option value="Other"><spring:message code="etiq.other"/></option>
                                        </select>
                                    </div>
                                </div>  
                                <div class="col-xs-12" >
                                    <input type="file" id="fileToUpload" accept="image/*">
                                </div>

                                <div class="col-xs-12 text-center">
                                    <input type="submit" class="btn btn-success" id="savecomment"  value="Save" onclick="updateComment()">
                                </div>
                                <div class="col-xs-12 text-center hidden" id="error1">
                                    <label><spring:message code="etiq.pleaseSelect"/></label>
                                </div>
                                <div class="col-xs-12 text-center hidden" id="error2">
                                    <label><spring:message code="etiq.pleaseMake"/></label>
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
                    <div class="modal-header modal-header-delete">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><spring:message code="etiq.commentSaved"/></h4>
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
                        <h4 class="modal-title"><spring:message code="etiq.moreInf"/></h4>
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
                        <button type="button"  onclick="deletePhoto()" class='btn btn-link'  value='' id='deleteFoto'><spring:message code="etiq.Delete"/></button>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleComment"></h4>
                    </div>
                    <div class="modal-body">
                        <img id="imagen" class="foto" src=""/>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>