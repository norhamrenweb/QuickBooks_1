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
        <title>Home</title>

        <script type="text/javascript">

            var userId = ${user.id};


            $(document).ready(function () {

                //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE

            <%--      var lessondelete = '<%= request.getParameter("messageDelete") %>'; --%>


                $('#table_id').DataTable({
                    aLengthMenu: [[5, 10, 20, -1], [5, 10, 20, "All"]],
                    iDisplayLength: 5,
                    order: [[5, "desc"]],
                    columnDefs: [
                        {"width": "10%", "targets": [0],
                            "visible": false,
                            "searchable": false},
                        {"width": "18%", "targets": [1]},
                        {"width": "7%", "targets": [2]},
                        {"width": "10%", "targets": [3]},
                        {"width": "10%", "targets": [4]},
                        {"width": "10%", "targets": [5]},
                        {"width": "10%", "targets": [6]},
                        {"width": "10%", "targets": [7]},
                        {"width": "25%", "targets": [8]}

                    ],
                    responsive: true
                });
                $('#table_datelessons').DataTable();

                $('#table_id tbody').on('click', 'tr', function () {
                    table = $('#table_id').DataTable();
                    data = table.row(this).data();
                    nameLessons = data[1];
                });

            });
            function deleteSelectSure(deleteLessonsSelected, deleteLessonsName) {

                $('#lessonDelete').empty();
                $('#lessonDelete').append(deleteLessonsName);
                $('#buttonDelete').val(deleteLessonsSelected);
                $('#deleteLesson').modal('show');
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

                        $('#nameLessonDetails').empty();
                        $('#nameLessonDetails').append('Details ' + nameLessons);
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
                var lessonName = LessonsName.substring(1, LessonsName.length);
                var path = LessonsSelected + "-" + lessonName;
                window.open("<c:url value="/lessonresources/loadResources.htm?LessonsSelected="/>" + path);

            }
            function rowselect(LessonsSelected)
            {

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
            function deleteSelect()
            {
                var lessonSelect = $('#buttonDelete').val();
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                ajax.onreadystatechange = funcionCallBackdeleteLesson;
                ajax.open("POST", "deleteLesson.htm?LessonsSelected=" + lessonSelect, true);
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
                        <td>Presentation Title</td>
                        <td>Grade Level</td>
                        <td>Subject</td>
                        <td>Objective</td>

                        <td>Date</td>
                        <td>Start Hour</td>
                        <td>End Hour</td>
                        <td><spring:message code="etiq.actionlessons"/></td>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="lecciones" items="${lessonslist}" >
                        <tr>
                            <td>${lecciones.id}</td>
                            <td>${lecciones.name}</td>
                            <td>${lecciones.level.name}</td>
                            <td>${lecciones.subject.name}</td>
                            <td>${lecciones.objective.name}</td>
                            <td>${lecciones.date}</td>
                            <td>${lecciones.start}</td>
                            <td>${lecciones.finish}</td>
                            <td>

                                <div class="sinpadding text-center">
                                    <input name="TXTid_lessons_attendance" class="btn-unbutton" type="image" src="<c:url value="/recursos/img/btn/btn_Attendance.svg"/>" value="${lecciones.id}" id="attendance" onclick="rowselect(${lecciones.id})" width="40px" data-placement="bottom" title="Progress">
                                </div>
                                <div class="sinpadding text-center">
                                    <input name="TXTid_lessons_detalles" type="image" src="<c:url value="/recursos/img/btn/btn_details.svg"/>" value="${lecciones.id}" id="details" onclick="detailsSelect(${lecciones.id})" width="40px" data-placement="bottom" title="Details">
                                </div>
                                <div class="sinpadding text-center">
                                    <input name="TXTid_lessons_modificar" type="image" src="<c:url value="/recursos/img/btn/btn_Edit.svg"/>" value="${lecciones.id}" id="modify" onclick="modifySelect(${lecciones.id})" width="40px" data-placement="bottom" title="Modify">
                                </div>
                                <div class="sinpadding text-center">
                                    <input class="delete" name="TXTid_lessons_eliminar" type="image" src="<c:url value="/recursos/img/btn/btn_delete.svg"/>" value="${lecciones.id}" id="delete" onclick="deleteSelectSure(${lecciones.id}, '${lecciones.name}')" width="40px" data-placement="bottom" title="Delete">
                                </div>
                                <div class="sinpadding text-center">
                                    <input class="resources" name="TXTid_lessons_resources" type="image" src="<c:url value="/recursos/img/btn/btn_Resources.png"/>" value="${lecciones.id}" id="resources" onclick="accessrsrcs(${lecciones.id}, '${lecciones.name}')" width="40px" data-placement="bottom" title="Resources">
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
                        <h4 id="nameLessonDetails" class="modal-title">Details</h4>
                    </div>
                    <div class="modal-body">
                        <div class="container-fluid">
                            <div class="col-xs-6">
                                <div class="row title">
                                    Students
                                </div>
                                <div id="studentarea" class="row studentarea">

                                </div>
                            </div>
                            <div class="col-xs-6">
                                <div class="row title">
                                    Description
                                </div>
                                <div class="row" id="commentDetails">

                                </div>
                                <div class="row title">
                                    Objective   
                                </div>
                                <div class="row" id ="objectivedetails">

                                </div>
                                <div class="row title">
                                    Method
                                </div>
                                <div class="row" id="methodDetails">

                                </div>
                                <div class="row title">
                                    Equipment 
                                </div>
                                <div class="row">
                                    <ul id="contentDetails">

                                    </ul>
                                </div>
                                <div class="row title">
                                    Created by 
                                </div>
                                <div class="row">
                                    <ul id="createBy">
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
                        <h4 class="modal-title">are you sure you want to delete?</h4>
                    </div>
                    <div id="lessonDelete" class="modal-body">

                    </div>
                    <div class="modal-footer text-center">
                        <button id="buttonDelete" type="button" class="btn btn-danger" data-dismiss="modal" onclick="deleteSelect()">Yes</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
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
                        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="refresh()">OK</button>
                    </div>
                </div>

            </div>
        </div>

    </body>
</html>
