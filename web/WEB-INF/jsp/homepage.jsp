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
    <%@ include file="menu.jsp" %>

    <head>
        <title><spring:message code="etiq.Home"/></title>

        <script type="text/javascript">

            var userId = ${user.id};

            $(document).ready(function () {
                //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE

            <%--      var lessondelete = '<%= request.getParameter("messageDelete") %>'; --%>
           
                
                $('.pasar').click(function () {
                    var exist = false;
                    $('#destino option').each(function () {
                        if ($('#origen option:selected').val() === $(this).val())
                            exist = true;
                    });

                    if (!exist)
                        !$('#origen option:selected').clone().appendTo('#destino');

                    var numAlum = $('#destino option').length;
                    if (numAlum > 0) {
                        $('#createOnClick').attr('disabled', false);
                    } else {
                        $('#createOnClick').attr('disabled', true);
                    }

                    $('#destino option').first().prop('selected', true);
                    return;
                });


                $('.quitar').click(function () {
                    !$('#destino option:selected').remove();
                    $('#destino option').first().prop('selected', true);

                    return;
                });
                $('.pasartodos').click(function () {
                    $('#origen option').each(function () {

                        var valueInsert = $(this).val();
                        var exist = false;
                        $('#destino option').each(function () {
                            if (valueInsert === $(this).val())
                                exist = true;
                        });

                        if (!exist)
                            $(this).clone().appendTo('#destino');

                        var objectiveSelected = $('#objective').val();
                        if (objectiveSelected === 0 || objectiveSelected === null || objectiveSelected === '') {
                            $('#createOnClick').attr('disabled', true);
                        }
                    });

                    var numAlum = $('#destino option').length;
                    if (($("#NameLessons").val().indexOf("'") === -1) && ($("#NameLessons").val().indexOf("\"") === -1) && document.getElementById("objective").value !== 'Select Objective' && document.getElementById("objective").value !== '' && document.getElementById("NameLessons").value !== '' && document.getElementById("comments").value !== '' && $('#fecha input').val() !== '' && $('#horainicio input').val() !== '' && $('#horafin input').val() !== '' && numAlum > 0) {
                        $('#createOnClick').attr('disabled', false);
                    } else {
                        $('#createOnClick').attr('disabled', true);
                    }

                    $('#destino option').first().prop('selected', true);
                });

                $('.quitartodos').click(function () {
                    $('#destino option').each(function () {
                        $(this).remove();
                    });
                });

                $('#table_id').DataTable({
                    language: idioma,
                    aLengthMenu: [[5, 10, 20, -1], [5, 10, 20, "All"]],
                    iDisplayLength: 5,
                    order: [[5, "desc"]],
                    columnDefs: [
                        {"width": "10%", "targets": [0],
                            "visible": false,
                            "searchable": false},
                        {"width": "15%", "targets": [1]},
                        {"width": "7%", "targets": [2]},
                        {"width": "10%", "targets": [3]},
                        {"width": "15%", "targets": [4]},
                        {"width": "15%", "targets": [5]},
                        {"width": "38%", "targets": [6]}

                    ],
                    responsive: true
                });

                /*var resizeId;
                 $(window).resize(function () {
                 clearTimeout(resizeId);
                 resizeId = setTimeout(doneResizing, 500);
                 });
                 function doneResizing() {
                 refresh();
                 }
                 */

                $('#table_datelessons').DataTable();

                $('#table_id tbody').on('click', 'tr', function () {
                    table = $('#table_id').DataTable();
                    data = table.row(this).data();
                    nameLessons = data[1];
                });
                //  var nP = localStorage.getItem("numberPage");
                var nP = window.name;
                if (nP === "")
                    nP = 0;

                $("#table_id").DataTable().page(Number(nP)).draw('page')

            });
           /* function changeTermYear() {
                var year = $('#yearSelect option:selected').val();
                var term = $('#termSelect option:selected').val();
                var url = "<c:url value="/changeTermYear.htm"/>?yearid=" + year + "&termid=" + term;
                var nameYearAndTerm = $('#termSelect option:selected').text() + " / " + $('#yearSelect option:selected').text();
                yearId_view =term;
                termId_view =year;
                $.ajax({
                    type: 'POST',
                    url: url,
                    contentType: "application/json",
                    success: function (data) {
                        $('#btnYearmTerm').text(nameYearAndTerm);
                        refresh();

                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);
                    }

                });
            }*/


            function deleteSelectSure(deleteLessonsSelected, deleteLessonsName) {

                $('#lessonDelete').empty();
                $('#lessonDelete').append(deleteLessonsName);
                $('#buttonDelete').val(deleteLessonsSelected);
                $('#deleteLesson').modal('show');

            }
            function sortTable() {
                var table, rows, switching, i, x, y, shouldSwitch;
                table = document.getElementById("table_id");
                switching = true;
                /*Make a loop that will continue until
                 no switching has been done:*/
                while (switching) {
                    //start by saying: no switching is done:
                    switching = false;
                    rows = table.getElementsByTagName("TR");
                    /*Loop through all table rows (except the
                     first, which contains table headers):*/
                    for (i = 1; i < (rows.length - 1); i++) {
                        //start by saying there should be no switching:
                        shouldSwitch = false;
                        /*Get the two elements you want to compare,
                         one from current row and one from the next:*/
                        x = rows[i].getElementsByTagName("TD")[4];
                        y = rows[i + 1].getElementsByTagName("TD")[4];
                        //check if the two rows should switch place:
                        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                            //if so, mark as a switch and break the loop:
                            shouldSwitch = true;
                            break;
                        }
                    }
                    if (shouldSwitch) {
                        /*If a switch has been marked, make the switch
                         and mark that a switch has been done:*/
                        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                        switching = true;
                    }
                }
            }
            function compartirSelect(id, idTeacher) {
                $('#origen option').show();
                $('#origen').find("[value=" + idTeacher + "]").hide();

                $.ajax({
                    type: "POST",
                    url: "cargarcompartidos.htm?seleccion=" + id,
                    data: id,
                    dataType: 'text',
                    success: function (data) {
                        var obj = JSON.parse(data);
                        var t = JSON.parse(obj.t);
                        $('#destino').empty();
                        $.each(t, function (i, teacher) {
                            if (t[i].id !== idTeacher)
                                $('#destino').append('<option value="' + t[i].id + '">' + t[i].name + '</option>');
                        });
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);

                        $('#compartirmessage').empty();
                        $('#compartirmessage').append("<h4> Error <h4>");
                    }
                });
                $('#compartirid').val(id);
                $('#compartirLesson').modal('show');
            }
            //   
            var ajax;
            function funcionCallBackdetailsLesson()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        var object = JSON.parse(ajax.responseText);
                        var s = JSON.parse(object.students);
                        var c = JSON.parse(object.contents);
                        var p = JSON.parse(object.steps);

                        $('#nameLessonDetails').empty();

                        var detailsLabel = "<spring:message code="etiq.Details"/>";

                        $('#nameLessonDetails').append(detailsLabel + " " + nameLessons);
                        $('#studentarea').append('<table id="detailsStudents" class="table table-striped">');
                        $.each(s, function (i, student) {
                            $('#detailsStudents').append('<tr><td class="studentDetails">' + s[i].studentname + '</td></tr>');
                            $("tr:odd").addClass("par");
                            $("tr:even").addClass("impar");
                        });
                        $('#contentDetails').empty();
                        $.each(c, function (i, content) {
                            $('#contentDetails').append('<li>' + c[i] + '</li>');
                        });

                        $('#steps').empty();
                        $.each(p, function (i, step) {
                            $('#steps').append('<li>' + p[i] + '</li>');
                        });


                        $('#methodDetails').empty();
                        $('#methodDetails').append('<tr><td>' + object.method + '</td></tr>');
                        $('#commentDetails').empty();
                        $('#commentDetails').append('<tr><td>' + object.comment + '</td></tr>');
                        $('#objectivedetails').empty();
                        $('#objectivedetails').append('<tr><td>' + object.objective + '</td></tr>');
                        $('#createBy').empty();
                        $('#createBy').append('<tr><td>' + object.nameteacher + '</td></tr>');
                        $('#detailsLesson').modal('show');

                    }
                }
            }
            function accessrsrcs(LessonsSelected, LessonsName)
            {
                changeTermYearModify();
                var lessonName = LessonsName.substring(1, LessonsName.length);
                var path = LessonsSelected + "-" + lessonName;
                window.open("<c:url value="/lessonresources/loadResources.htm?LessonsSelected="/>" + path);

            }
            function rowselect(LessonsSelected)
            {
                changeTermYearModify();
                window.open("<c:url value="/lessonprogress/loadRecords.htm?LessonsSelected="/>" + LessonsSelected);
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
                $('#studentarea').empty();
                ajax.onreadystatechange = funcionCallBackdetailsLesson;
                ajax.open("POST", "detailsLesson.htm?LessonsSelected=" + LessonsSelected, true);
                ajax.send("");
            }
            ;
            function modifySelect(LessonsSelected)
            {
                changeTermYearModify();

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
                        var presentationHasProgress = "<spring:message code="etiq.presentationHasProgress"/>";
                        var presentationDeleteSuc = "<spring:message code="etiq.presentationDeleteSuc"/>";

                        if (lessondeleteconfirm.message === 'Presentation has progress records,it can not be deleted') {
                            $('#lessonDeleteMessage').empty();
                            $('#lessonDeleteMessage').append('<H1>' + presentationHasProgress + '</H1>');
                            $('#deleteLessonMessage').modal('show');
                        } else {
                            $('#lessonDeleteMessage').empty();
                            $('#lessonDeleteMessage').append('<H1>' + presentationDeleteSuc + '</H1>');
                            $('#deleteLessonMessage').modal('show'); //  Presentation deleted successfully

                            refresh();
                        }
                        ;



                    }
                }
            }


            function deleteSelect()
            {
                var lessonSelect = $('#buttonDelete').val();
                var lessonsName = $("#lessonDelete").text().trim();
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }
                // localStorage.setItem("numberPage", $("#table_id").DataTable().page());
                window.name = $("#table_id").DataTable().page();
                // $("#table_id").DataTable().page(5).draw( 'page' )

                // $("#table_id").DataTable().row($("#delete"+lessonSelect).parent().parent()).node().remove();
                ajax.onreadystatechange = funcionCallBackdeleteLesson;
                ajax.open("POST", "deleteLesson.htm?LessonsSelected=" + lessonSelect + "&LessonsName=" + lessonsName, true);
            <%-- window.open("<c:url value="/homepage/deleteLesson.htm?LessonsSelected="/>"+LessonsSelected); --%>
                ajax.send("");
                //   refresh();
            }

            ;
            function refresh()
            {
                location.reload();
            }

            function compartirajax() {
                $('#destino option').prop('selected', true);
                var seleccion = $('#compartirid').val();
                var teachers = $('#destino').val();
                if (teachers === null)
                    teachers = [];
                var obj = {};
                obj.id = seleccion;
                obj.teachers = teachers;
                $.ajax({
                    type: "POST",
                    url: "compartir.htm", // + JSON.stringify(obj),
                    data: JSON.stringify(obj),
                    datatype: "json",
                    contentType: "application/json",
                    success: function (data) {
                        $('#destino').empty();
                        $('#compartirmessage').empty();
                        $('#compartirmessage').append("<h4>" + data + "<h4>");
                        $('#compartirmodal').modal('show');
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        console.log(xhr.status);
                        console.log(xhr.responseText);
                        console.log(thrownError);

                        $('#compartirmessage').empty();
                        $('#compartirmessage').append("<h4> Error <h4>");
                    }
                });
            }
            function showCalendar()
                        {
                                changeTermYearModify();
                                id = '11343';
                                window.open("<c:url value="/schedule.htm?id="/>" + id);
                        }
            /*var resizeId;
             $(window).resize(function () {
             clearTimeout(resizeId);
             resizeId = setTimeout(doneResizing, 500);
             });
             function doneResizing() {
             refresh();
             }*/

        </script>
        <style>

            #table_id_wrapper{
                font-size: small;
            }
            .uk-form-small{
                min-height: 0px;
            }

            #table_id{
                width: 100% !important;
            }
            .sinpadding
            {
                padding-left: 4px;
                padding-right: 4px;
                display: inline-block;
                width: 18%;
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
            #createBy{
                padding-left: 0px;
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
            .divButtonCompartir{
                margin-top: 10px;
                border-top: solid 1px lightgray;
                padding-top: 10px;
                width: 90%;
                margin-left: 5%;
            }
            #compartirLesson .modal-footer{
                border: none;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <div class="col-sm-12" id="maincontainer">
                <div class="col-sm-12 center-block text-center">
                    <h2><spring:message code="etiq.txtactivities"/></h2>
                </div>
            </div>
            <table id="table_id" class="display" >
                <thead>
                    <tr>
                        <td>id</td>
                        <td><spring:message code="etiq.PresentationTitle"/><!--Presentation Title--></td>
                        <td><spring:message code="etiq.GradeLevel"/><!--Grade Level--></td>
                        <td><spring:message code="etiq.Subject"/><!--Subject--></td>
                        <td><spring:message code="etiq.Objective"/><!--Objective--></td>

                        <td><spring:message code="etiq.Date"/><!--Date--></td>
                        <td><spring:message code="etiq.actionlessons"/></td>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="lecciones" items="${lessonslist}" >
                        <tr data-createdBy="${lecciones.teacherid}">
                            <td>${lecciones.id}</td>
                            <td>
                                ${lecciones.name}
                                <c:if test="${lecciones.share==true}">
                                    <img style="width: 20px" src="<c:url value="/recursos/img/btn/slideshare.svg"/>">
                                </c:if>
                            </td>
                            <td>${lecciones.level.name}</td>
                            <td>${lecciones.subject.name}</td>
                            <td>${lecciones.objective.name}</td>
                            <td>${lecciones.date} (${lecciones.start} / ${lecciones.finish})</td>
                            <td>
                                <div class="col-xs-4 col-md-2 text-center">
                                    <input name="TXTid_lessons_attendance" class="btn-unbutton" type="image" src="<c:url value="/recursos/img/btn/btn_Attendance.svg"/>" value="${lecciones.id}" id="attendance" onclick="rowselect(${lecciones.id})" width="40px" data-placement="bottom" title="<spring:message code="etiq.Progress"/>">
                                </div>
                                <div class="col-xs-4 col-md-2 text-center">
                                    <input name="TXTid_lessons_detalles" type="image" src="<c:url value="/recursos/img/btn/btn_details.svg"/>" value="${lecciones.id}" id="details" onclick="detailsSelect(${lecciones.id})" width="40px" data-placement="bottom" title="<spring:message code="etiq.Details"/>">
                                </div>
                                <div class="col-xs-4 col-md-2 text-center">
                                    <input name="TXTid_lessons_modificar" type="image" src="<c:url value="/recursos/img/btn/btn_Edit.svg"/>" value="${lecciones.id}" id="modify" onclick="modifySelect(${lecciones.id})" width="40px" data-placement="bottom" title="<spring:message code="etiq.Modify"/>">
                                </div>
                                <div class="col-xs-4 col-md-2 text-center">
                                    <input class="delete" name="TXTid_lessons_eliminar" type="image" src="<c:url value="/recursos/img/btn/btn_delete.svg"/>" value="${lecciones.id}" id="delete${lecciones.id}" onclick="deleteSelectSure(${lecciones.id}, '${lecciones.name}')" width="40px" data-placement="bottom" title="<spring:message code="etiq.Delete"/>">
                                </div>
                                <div class="col-xs-4 col-md-2 text-center">
                                    <input class="resources" name="TXTid_lessons_resources" type="image" src="<c:url value="/recursos/img/btn/btn_Resources.png"/>" value="${lecciones.id}" id="resources" onclick="accessrsrcs(${lecciones.id}, '${lecciones.name}')" width="40px" data-placement="bottom" title="<spring:message code="etiq.Resources"/>">
                                </div>
                                <div class="col-xs-4 col-md-2 text-center">
                                    <input class="resources" name="TXTid_lessons_compartir" type="image" src="<c:url value="/recursos/img/btn/compartir.png"/>" value="${lecciones.id}" id="resources" onclick="compartirSelect(${lecciones.id},${lecciones.teacherid})" width="40px" data-placement="bottom" title="<spring:message code="etiq.Share"/>">
                                </div>
                            </td>
                        </tr>  
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Modal delete-->
        <div id="detailsLesson" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header modal-header-details">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="nameLessonDetails" class="modal-title"><spring:message code="etiq.Details"/></h4>
                    </div>
                    <div class="modal-body">
                        <div class="container-fluid">
                            <div class="col-xs-6">
                                <div class="row title">
                                    <spring:message code="etiq.txtstudents"/>
                                </div>
                                <div id="studentarea" class="row studentarea">

                                </div>
                            </div>
                            <div class="col-xs-6 padding0">
                                <div class="col-xs-12 divDetails">
                                    <div class="col-xs-12 title">
                                        <spring:message code="etiq.txtdescription"/>:
                                    </div>
                                    <div class="col-xs-12" id="commentDetails">
                                    </div>
                                </div>
                                <div class="col-xs-12 divDetails">
                                    <div class="col-xs-12 title">
                                        <spring:message code="etiq.Objective"/>:   
                                    </div>
                                    <div class="col-xs-12" id ="objectivedetails">
                                    </div>
                                </div>
                                <div class="col-xs-12 divDetails">
                                    <div class="col-xs-12 title">
                                        <spring:message code="etiq.method"/>:
                                    </div>
                                    <div class="col-xs-12" id="methodDetails">

                                    </div>
                                </div>
                                <div class="col-xs-12 divDetails">
                                    <div class="col-xs-12 title">
                                        <spring:message code="etiq.txtequipment"/>: 
                                    </div>
                                    <div class="col-xs-12">
                                        <ul id="contentDetails">

                                        </ul>
                                    </div>
                                </div>
                                <div class="col-xs-12 divDetails">
                                    <div class="col-xs-12 title">
                                        <spring:message code="etiq.createByEtiq"/>: 
                                    </div>
                                    <div class="col-xs-12">
                                        <ul id="createBy">
                                        </ul>
                                    </div>
                                </div>
                                <div class="col-xs-12 divDetails">
                                    <div class="col-xs-12 title">
                                        <spring:message code="etiq.steps"/>:
                                    </div>
                                    <div class="col-xs-12">
                                        <ul id="steps" style="-webkit-padding-start: 15px;">
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="etiq.txtclose"/></button>
                    </div>
                </div>

            </div>
        </div>

        <!-- Modal confirm delete-->
        <div id="deleteLesson" class="modal fade" role="dialog">
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
                        <button id="buttonDelete" type="button" class="btn btn-danger" data-dismiss="modal" onclick="deleteSelect()"><spring:message code="etiq.yes"/></button>
                        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="etiq.no"/></button>
                    </div>
                </div>

            </div>
        </div>
        <!-- Modal lessons delete -->
        <div id="deleteLessonMessage" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header modal-header-delete">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"></h4>
                    </div>
                    <div id="lessonDeleteMessage" class="modal-body">
                        <c:out value='${messageDelete}'/>
                    </div>
                    <div class="modal-footer text-center">
                        <button type="button" class="btn btn-default" data-dismiss="modal">OK</button>
                    </div>
                </div>

            </div>
        </div>

        <div id="compartirLesson" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header modal-header-details">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><spring:message code="etiq.sharePresentation"/>:</h4>
                    </div>
                    <input type="hidden" id="compartirid" name ="compartirid" value="">
                    <div id="shareselect" class="modal-body">
                        <div class="col-xs-12">
                            <div class="col-xs-6 col-md-4">
                                <select class="form-control" size="20" multiple="" name="origen[]" id="origen" style="width: 100% !important;">  
                                    <c:forEach var="teacher" items="${teacherlist}" >
                                        <option value="${teacher.id}">${teacher.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="hidden-xs hidden-sm col-md-4">
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px; padding-top: 50px;">
                                    <input type="button" class="btn btn-success btn-block pasar" value="<spring:message code="etiq.txtadd"/> »">
                                </div>
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                    <input type="button" class="btn btn-danger btn-block quitar" value="« <spring:message code="etiq.txtremove"/>">
                                </div>
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                    <input type="button" class="btn btn-success btn-block pasartodos" value="<spring:message code="etiq.txtaddAll"/> »">
                                </div>
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                    <input type="button" class="btn btn-danger btn-block quitartodos" value="« <spring:message code="etiq.txtremoveAll"/>">
                                </div>
                                <!--                            <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                                                <input type="button" class="btn btn-danger btn-block test" value="test">
                                                            </div>-->
                            </div>

                            <div class="col-xs-6 col-md-4">
                                <select class="form-control" size="20" multiple="" name="destino[]" id="destino" style="width: 100% !important;">
                                </select>
                            </div>
                                
                            <div class="hidden-md hidden-lg col-xs-12"  style="padding-top:10px;">
                                <div class="col-xs-3 text-center" style="padding-right: 2px;padding-left: 2px;">
                                    <input type="button" style="font-size: small;" class="btn btn-success btn-block pasar" value="<spring:message code="etiq.txtadd"/> »">
                                </div>
                                <div class="col-xs-3 text-center" style="padding-right: 2px;padding-left: 2px;">
                                    <input type="button" style="font-size: small;"  class="btn btn-danger btn-block quitar" value="« <spring:message code="etiq.txtremove"/>">
                                </div>
                                <div class="col-xs-3 text-center" style="padding-right: 2px;padding-left: 2px;">
                                    <input type="button" style="font-size: small;"  class="btn btn-success btn-block pasartodos" value="<spring:message code="etiq.txtaddAll"/> »">
                                </div>
                                <div class="col-xs-3 text-center" style="padding-right: 2px;padding-left: 2px;">
                                    <input type="button" style="font-size: small;"  class="btn btn-danger btn-block quitartodos" value="« <spring:message code="etiq.txtremoveAll"/>">
                                </div>
                                <!--                            <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                                                <input type="button" class="btn btn-danger btn-block test" value="test">
                                                            </div>-->
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="col-xs-12 text-center divButtonCompartir">
                            <input type="button" id="createOnClick" class="btn btn-default" value="<spring:message code='etiq.share'/>" data-dismiss="modal" onclick="compartirajax()">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="compartirmodal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"> </h4>
                    </div>
                    <div id="compartirmessage" class="modal-body">

                    </div>
                </div>

            </div>
        </div>

        <div class="col-xs-12 text-center">
            <button type='button' class='btn btngreen_1' id="showcalendar"  value="View all" onclick="showCalendar()"><spring:message code="etiq.ViewSchedule"/></button>
        </div>
    </body>
</html>
