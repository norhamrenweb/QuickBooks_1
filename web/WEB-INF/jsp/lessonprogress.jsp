<%-- 
    Document   : createlesson
    Created on : 30-ene-2017, 14:59:17
    Author     : nmohamed
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <%--<%@ include file="menu.jsp" %>--%>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="<c:url value="/recursos/fonts/icons/iconsAragon.css"/>"/>
        <title>Presentation progress</title>
        <script>
            window.onbeforeunload = function exitAlert() {
                var textillo = "Changes you made may not be saved.";
                return textillo;
            };
            
            $(document).ready(function () {

                //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE

                var lessoncreate = '<%= request.getParameter("message")%>';
                var itemsRating = []; // needs to be outside
                var itemsAttendance = []; // needs to be outside
                var collectionRating = $(".studentRating");
                var collectionAttendance = $(".attendance");

                collectionRating.each(function () {
                    itemsRating.push($(this).val());
                });
                collectionAttendance.each(function () {
                    itemsAttendance.push($(this).val());
                });
                var haynullRating = $.inArray('', itemsRating);
                var haynullAttendance = $.inArray('0', itemsAttendance);

                if (haynullRating !== -1 || haynullAttendance !== -1) {
                    $('#buttonAchived').attr('disabled', true);
                    $('#buttonAchived').parent().attr('disabled', true);
                    $('#buttonAchived2').attr('disabled', true);
                    $('#buttonAchived2').parent().attr('disabled', true);
                    $('#buttonAchived')[0].checked = false;
                    $('#buttonAchived2')[0].checked = false;
                } else {
                    $('#buttonAchived').attr('disabled', false);
                    $('#buttonAchived').parent().removeAttr('disabled');
                    $('#buttonAchived2').attr('disabled', false);
                    $('#buttonAchived2').parent().removeAttr('disabled');
                }
                if (lessoncreate === 'Records successfully saved') {
                    $('#myModal').modal({
                        show: 'false'
                    });
                }
                var disable = "${disable}";
                if (disable === 't') {
                    $('#buttonAchived')[0].checked = true;
                    $('#buttonAchived2')[0].checked = true;
                }

                $('#table_progress').DataTable(
                        {
                            "searching": false,
                            "paging": false,
                            "aaSorting": [[1, "asc"]],
                            "columnDefs": [
                                {"width": "4%", "targets": 0},
                                {"width": "20%", "targets": 1},
                                {"width": "4%", "targets": 2},
                                {"width": "40%", "targets": 3},
                                {"width": "12%", "targets": 4, "orderable": false},
                                {"width": "20%", "targets": 5, "orderable": false}
                            ]

                        });

            });

            $(function () {
                $('select,hi,idSelectAttendance').change(function () {
                    var itemsRating = []; // needs to be outside
                    var itemsAttendance = []; // needs to be outside
                    var collectionRating = $(".studentRating");
                    var collectionAttendance = $(".attendance");

                    collectionRating.each(function () {
                        itemsRating.push($(this).val());
                    });
                    collectionAttendance.each(function () {
                        itemsAttendance.push($(this).val());
                    });
                    var haynullRating = $.inArray('', itemsRating);
                    var haynullAttendance = $.inArray('0', itemsAttendance);

                    if (haynullRating !== -1 || haynullAttendance !== -1) {
                        $('#buttonAchived').attr('disabled', true);
                        $('#buttonAchived').parent().attr('disabled', true);
                        $('#buttonAchived2').attr('disabled', true);
                        $('#buttonAchived2').parent().attr('disabled', true);
                        $('#buttonAchived')[0].checked = false;
                        $('#buttonAchived2')[0].checked = false;
                    } else {

                        $('#buttonAchived').attr('disabled', false);
                        $('#buttonAchived').parent().removeAttr('disabled');
                        $('#buttonAchived2').attr('disabled', false);
                        $('#buttonAchived2').parent().removeAttr('disabled');
                    }
                });

                $('#rellenarP').click(function () {

                    var rellenarPresent = $(".attendance");
                    rellenarPresent.each(function () {
                        $(this).val("P");
                    });

                    var itemsRating = []; // needs to be outside
                    var itemsAttendance = []; // needs to be outside
                    var collectionRating = $(".studentRating");
                    var collectionAttendance = $(".attendance");

                    collectionRating.each(function () {
                        itemsRating.push($(this).val());
                    });
                    collectionAttendance.each(function () {
                        itemsAttendance.push($(this).val());
                    });
                    var haynullRating = $.inArray('', itemsRating);
                    var haynullAttendance = $.inArray('0', itemsAttendance);

                    if (haynullRating !== -1 || haynullAttendance !== -1) {
                        $('#buttonAchived').attr('disabled', true);
                        $('#buttonAchived').parent().attr('disabled', true);
                        $('#buttonAchived2').attr('disabled', true);
                        $('#buttonAchived2').parent().attr('disabled', true);
                        $('#buttonAchived')[0].checked = false;
                        $('#buttonAchived2')[0].checked = false;
                    } else {
                        $('#buttonAchived').attr('disabled', false);
                        $('#buttonAchived').parent().removeAttr('disabled', true);
                        $('#buttonAchived2').attr('disabled', false);
                        $('#buttonAchived2').parent().removeAttr('disabled', true);
                    }

                });
                $('#rellenar').click(function () {

                    var rellenarNull = $(".attendance");
                    rellenarNull.each(function () {
                        var valorAttendance = $('option:selected', this).val();
                        if (valorAttendance !== '0') {
//                            $('option:selected', this).remove();
                            $('option:selected', this).attr('selected', false);
                        }
                    });

                    $(".attendance option[value='0']").attr('selected', true);


                    $('#buttonAchived').attr('disabled', true);
                    $('#buttonAchived').parent().attr('disabled', true);
                    $('#buttonAchived2').attr('disabled', true);
                    $('#buttonAchived2').parent().attr('disabled', true);
                    $('#buttonAchived')[0].checked = false;
                    $('#buttonAchived2')[0].checked = false;
                });

                $('#subject').change(function () {
//        $('#LoadTemplates').parent().attr("disabled",false);
//        $('#LoadTemplates').attr("disabled",false);
                    $('#LoadTemplates').children().removeClass("disabled");
                });

                $('#LoadTemplates').change(function () {
                    if ($("input:radio[name='options']:checked").val() === 'option1') {
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

                $("#buttonSave").click(function () {
                    $("#namePresentation").val($("form>fieldset> div> label").first().text());
                    $("#nameTeacher").val($("form>div>select :selected").text());
                });

            });
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
        </style>
    </head>
    <body>


        <div class="container">
            <h1 class="text-center">Presentation progress</h1>
            <p></p>

            <form:form id="formStudents" method ="post" action="saveRecords.htm" >

                <input type="hidden" class="form-control" id="namePresentation" name="namePresentation" value=""/>
                <input type="hidden" class="form-control" id="nameTeacher" name="nameTeacher" value=""/>

                <fieldset>
                    <legend>Presentation details</legend>

                    <div class="col-xs-4 center-block sinpadding">
                        Presentation Title:<br>
                        <label class="control-label"><input type="hidden" class="form-control" name="TXTlessonid" value="${lessondetailes.id}"/> ${lessondetailes.name}</label>
                    </div>

                    <div class="col-xs-4 center-block sinpadding">
                        Subject:<br>
                        <label class="control-label"> ${lessondetailes.subject.name}</label>
                    </div>
                    <div class="col-xs-4 center-block  sinpadding">
                        Objective:  <label class="control-label"><input type="hidden" class="form-control" name="TXTobjectiveid" value="${lessondetailes.objective.id[0]}"/> ${lessondetailes.objective.name}</label>
                    </div>
                    <div class="col-xs-12 center-block  sinpadding">
                        <c:forEach var="step" items="${steps}" varStatus="i">
                            <div class="col-xs-3 sinpadding">
                                <div class="col-xs-12 sinpadding">
                                    Step${(i.index +1)}:<br> <label class="control-label">${step.name}</label>
                                </div>
                            </div>
                        </c:forEach> 

                    </div>
                    <div class="col-xs-12 text-right">
                        <input type="checkbox" id="buttonAchived2" onchange="$('#buttonAchived')[0].checked = $('#buttonAchived2')[0].checked;">              
                        <label> Mark as completed </label>
                    </div>
                    <div class="col-xs-12 text-center">
                        <img style="width:5%" src="<c:url value="/recursos/img/iconos/lightbulb-idea.svg"/>"> <label> To mark the presentation as completed, you must fill all the students´ ratings and attendance codes</label>
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
                                    <td>Attendance 
                                        <input type="button" class="btn btn-xs btn-info" id="rellenarP" value="Fill P"> 
                                        <input type="button" class="btn btn-xs btn-info" id="rellenar" value="Clear">
                                    </td>
                                    <td>
                                        Work accomplished
                                    </td>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="record" items="${attendancelist}" >

                                    <tr class="rows">
                                        <input type="hidden" class="form-control" name="TXTstudentname" value="${record.studentname}"/>
                                        <td hidden="true"><input type="hidden" class="form-control" name="TXTstudentid" value="${record.studentid}"/>${record.studentid}</td>
                                        <td>${record.studentname}</td>
                                        <td>
                                            <select name="TXTrating" id="hi" class="studentRating rating">
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
                                            <textarea name="TXTcomment" class="TXTcomment" rows="2">${record.comment}</textarea>
                                        </td>
                                        <td>
                                            <select id="idSelectAttendance" name="TXTattendance" class="attendance">
                                                <%--<c:if test="${record.attendancecode eq '0'}">
                                                    <option value="0" selected> </option>
                                                    <option value="P">P</option>
                                                    <option value="A">A</option>
                                                    <option value="T">T</option>
                                                </c:if>
                                                <c:if test="${record.attendancecode}">
                                                    <option value="${record.attendancecode}" selected>${record.attendancecode}</option>
                                                    <option value="0"></option>
                                                    <option value="P">P</option>
                                                    <option value="A">A</option>
                                                    <option value="T">T</option>
                                                </c:if>--%>
                                                <c:choose>
                                                    <c:when test="${record.attendancecode=='0'}">
                                                        <option value="0" selected> </option>
                                                        <option value="P">P</option>
                                                        <option value="A">A</option>
                                                        <option value="T">T</option>
                                                    </c:when>    
                                                    <c:otherwise>
                                                        <option value="${record.attendancecode}" selected>${record.attendancecode}</option>
                                                        <option value="0"></option>
                                                        <option value="P">P</option>
                                                        <option value="A">A</option>
                                                        <option value="T">T</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </select>
                                        </td>
                                        <td>
                                            <script>
                                                $(document).ready(function () {
                                                    $('#${record.studentid}').children('.step1').on('click', function () {
                                                        if ($(this).hasClass("progress-bar-success")) {
                                                            $(this).removeClass('progress-bar-success');
                                                        } else {
                                                            $(this).addClass('progress-bar-success');
                                                        }
                                                    });
                                                    $('#${record.studentid}').children('.step2').on('click', function () {
                                                        if ($(this).hasClass("progress-bar-info")) {
                                                            $(this).removeClass('progress-bar-info');
                                                        } else {
                                                            $(this).addClass('progress-bar-info');
                                                        }
                                                    });
                                                    $('#${record.studentid}').children('.step3').on('click', function () {
                                                        if ($(this).hasClass("progress-bar-danger")) {
                                                            $(this).removeClass('progress-bar-danger');
                                                        } else {
                                                            $(this).addClass('progress-bar-danger');
                                                        }
                                                    });
                                                    $('#${record.studentid}').children('.step4').on('click', function () {
                                                        if ($(this).hasClass("progress-bar-warning")) {
                                                            $(this).removeClass('progress-bar-warning');
                                                        } else {
                                                            $(this).addClass('progress-bar-warning');

                                                        }
                                                    });

                                                });

                                            </script>
                                            <div id="${record.studentid}">
                                                <!--                                            <div class="progress-bar step1" role="progressbar" style="width:25%">
                                                                                              1
                                                                                            </div>
                                                                                            <div class="progress-bar step2" role="progressbar" style="width:25%">
                                                                                              2
                                                                                            </div>
                                                                                            <div class="progress-bar step3" role="progressbar" style="width:25%">
                                                                                              3
                                                                                            </div>
                                                                                            <div class="progress-bar step4" role="progressbar" style="width:25%">
                                                                                              4
                                                                                            </div>-->

                                                <%--  <input type="number" name="your_awesome_parameter" id="some_id" class="rating" data-clearable="remove" data-icon-lib="fa" data-active-icon="fa-heart" data-inactive-icon="fa-heart-o" data-clearable-icon="fa-trash-o" data-max="${fn:length(steps)}" data-min="1" value="${record.steps}" />--%>
                                                <input type="number" name="your_awesome_parameter" id="some_id" class="rating" data-clearable="X" data-icon-lib="iconsAragon fa" data-active-icon="icon-Pie_PieIzqSelect" data-inactive-icon="icon-Pie_PieIzqUnSelect" data-clearable-icon="fa-null" data-max="${fn:length(steps)}" data-min="1" value="${record.steps}" />

                                            </div> 
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                    </div>
                </fieldset>
                <div class="col-xs-4 text-center">   
                    <label class="control-label">
                        Presented by
                    </label>

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
                </div>
                <div class="col-xs-4 text-center">   
                    <input type="submit" id="buttonSave" class="btn btn-success" value="Save" onclick="window.onbeforeunload = null;">
                </div>
                <div class="col-xs-4 text-right">
                    <input type="checkbox" name="buttonAchived" id="buttonAchived" onclick="$('#buttonAchived2')[0].checked = $('#buttonAchived')[0].checked">              
                    <label> Mark as completed </label>
                </div>
            </form:form>
        </div>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="titleComment"><%= request.getParameter("message")%></h4>
                    </div>
                </div>
            </div>
        </div>    





    </body>
</html>
