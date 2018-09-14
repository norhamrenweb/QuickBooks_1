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
        <title><spring:message code="etiq.compPresentations"/></title>

        <script type="text/javascript">



            $(document).ready(function () {

                //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE

            <%--      var lessondelete = '<%= request.getParameter("messageDelete") %>'; --%>


                $('#table_id').DataTable({
                    language: idioma,
                    "aLengthMenu": [[5, 10, 20, -1], [5, 10, 20, "All"]],
                    "iDisplayLength": 5,
                    "order": [[5, "desc"]],
                    columnDefs: [
                        {"width": "0%", "targets": [0],
                            "visible": false,
                            "searchable": false},
                        {"width": "15%", "targets": [1]},
                        {"width": "7%", "targets": [2]},
                        {"width": "10%", "targets": [3]},
                        {"width": "20%", "targets": [4]},
                        {"width": "10%", "targets": [5]},
                        {"width": "10%", "targets": [6]},
                        {"width": "10%", "targets": [7]},
                        {"width": "18%", "targets": [8]}

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

        /*    function changeTermYear() {
                var year = $('#yearSelect option:selected').val();
                var term = $('#termSelect option:selected').val();
                var url = "<c:url value="/changeTermYear.htm"/>?yearid=" + year + "&termid=" + term;
                var nameYearAndTerm = $('#termSelect option:selected').text() + " / " + $('#yearSelect option:selected').text();
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
            function compartirSelect(id) {
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
                            $('#destino').append('<option value="' + t[i].id + '">'
                                    + t[i].name + '</option>');
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

            function compartirajax() {
                $('#destino option').prop('selected', true);
                var seleccion = $('#compartirid').val();
                var teachers = $('#destino').val();
                var obj = {};
                obj.id = seleccion;
                obj.teachers = teachers;
                $.ajax({
                    type: "POST",
                    url: "compartir.htm?obj=" + JSON.stringify(obj),
                    data: JSON.stringify(obj),
                    dataType: 'text',
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
                        var p = JSON.parse(object.steps);

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
                /*  if (ajax.readyState===4){
                 if (ajax.status===200){
                 var object = JSON.parse(ajax.responseText);
                 var s = JSON.parse(object.students);
                 var c =  JSON.parse(object.contents);
             
                 //                   var cntContent = (object.contents).toString();
                 //                   var Contents = cntContent.substr(1,cntContent.length - 2);
                 //                   var r = Contents.split(",");
                 //var tableObjective = $('#tableobjective').DataTable();
                 $('#nameLessonDetails').empty();
                 $('#nameLessonDetails').append('Details '+nameLessons);
                 //$('#detailsStudents').empty();
                 $('#studentarea').append('<table id="detailsStudents" class="table table-striped">');
                 $.each(s, function (i,student){
                 $('#detailsStudents').append('<tr><td class="studentDetails">'+s[i].studentname+'</td></tr>');
                 $("tr:odd").addClass("par");
                 $("tr:even").addClass("impar");
                 //    $("tr:odd").css("background-color", "lightgray");
                 });
                 $('#contentDetails').empty();
                 $.each(c, function (i, content){
                 $('#contentDetails').append('<li>'+c[i]+'</li>');
                 });
             
             
                 $('#methodDetails').empty();
                 $('#methodDetails').append('<tr><td>'+object.method+'</td></tr>');
                 $('#commentDetails').empty();
                 $('#commentDetails').append('<tr><td>'+object.comment+'</td></tr>');
                 $('#detailsLesson').modal('show');
                 //                        });
                 //                        var commentgeneral = $('#tableobjective tbody tr td:eq(2)').text();
                 //                        $('#tableobjective tbody tr td:eq(2)').empty();
                 //                        $('#tableobjective tbody tr td:eq(2)').append("<input value='"+commentgeneral+"'></input>");   
             
             
                 //     $('#tableobjective tbody tr td:eq(4)').on('click', 'tr', 'td:eq(4)', function () {
                 //        
                 //        var dataObjective = tableObjective.row( this ).data();
                 //        dataObjective1 = dataObjective['col5'];
                 //        selectionObjective();
                 //    } ); 
                 }
                 }*/
            }
            function rowselect(LessonsSelected)
            {
                //ESTO PARA PINCHAR EN LA FILAvar LessonsSelected = data1;
                //var LessonsSelected = $(data1).html();
                //var LessonsSelected = 565;



    //        if (window.XMLHttpRequest) //mozilla
    //        {
    //            ajax = new XMLHttpRequest(); //No Internet explorer
    //        }
    //        else
    //        {
    //            ajax = new ActiveXObject("Microsoft.XMLHTTP");
    //        }

    //ajax.onreadystatechange=funcionCallBackLessonsprogress;
                //       window.location.href = "/lessonprogress/loadRecords.htm?LessonsSelected="+LessonsSelected;
                window.open("<c:url value="/lessonprogress/loadRecords.htm?disable=t&LessonsSelected="/>" + LessonsSelected);
    //        ajax.open("POST","lessonprogress.htm?select6=loadRecords&LessonsSelected="+LessonsSelected,true);
    //        ajax.send("");
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
            function accessrsrcs(LessonsSelected, LessonsName)
            {
                var lessonName = LessonsName.substring(1, LessonsName.length);
                var path = LessonsSelected + "-" + lessonName;
            window.open("<c:url value="/lessonresources/loadResources.htm?LessonsSelected="/>" + path);

            }
            function funcionCallBackdeleteLesson()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
            <%--    var lessondeleteconfirm = '<%= request.getParameter("messageDelete") %>'; --%>
                        var lessondeleteconfirm = "";
                        var lessondeleteconfirm = JSON.parse(ajax.responseText);
    //                   var s = JSON.parse(object.students);
    //                   var c =  JSON.parse(object.contents);
    //                   
    //                        $('#nameLessonDetails').empty();
    //                        $('#nameLessonDetails').append('Details '+nameLessons);
    //                        //$('#detailsStudents').empty();
    //                        $('#studentarea').append('<table id="detailsStudents" class="table table-striped">');
    //                        $.each(s, function (i,student){
    //                            $('#detailsStudents').append('<tr><td class="studentDetails">'+s[i].studentname+'</td></tr>');
    //                            $("tr:odd").addClass("par");
    //                            $("tr:even").addClass("impar");
    //                        });
    //                        $('#contentDetails').empty();
    //                        $.each(c, function (i, content){
    //                            $('#contentDetails').append('<li>'+c[i]+'</li>');
    //                        });
    //                        
    //                        
    //                        $('#methodDetails').empty();
    //                        $('#methodDetails').append('<tr><td>'+object.method+'</td></tr>');
    //                        $('#commentDetails').empty();
    //                        $('#commentDetails').append('<tr><td>'+object.comment+'</td></tr>');
    //                         $('#lessonDeleteMessage').empty();
    //                         document.getElementById("lessonDeleteMessage").innerHTML = ajax.responseText;
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
            function refresh()
            {
                location.reload();
            }


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
        <div class="col-xs-12">
            <div class="col-sm-12" id="maincontainer">
                <div class="col-sm-12 center-block text-center">
                    <h2><spring:message code="etiq.compPresentations"/></h2>
                </div>
            </div>
            <div class="container">
                <table id="table_id" class="display" >
                    <thead>
                        <tr>
                            <td>id</td>

                            <td><spring:message code="etiq.PresentationTitle"/><!--Presentation Title--></td>
                            <td><spring:message code="etiq.GradeLevel"/><!--Grade Level--></td>
                            <td><spring:message code="etiq.Subject"/><!--Subject--></td>
                            <td><spring:message code="etiq.Objective"/><!--Objective--></td>

                            <td><spring:message code="etiq.Date"/><!--Date--></td>
                            <td><spring:message code="etiq.startHour"/><!--Date--></td>
                            <td><spring:message code="etiq.endHour"/><!--Date--></td>
                            <td><spring:message code="etiq.actionlessons"/></td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="lecciones" items="${lessonslist}" >
                            <tr>
                                <td>${lecciones.id}</td>
                                <td>
                                    ${lecciones.name}
                                    <c:if test="${lecciones.share==true}">
                                        <img style="width: 20px" src="<c:url value="/recursos/img/btn/compartido.png"/>">
                                    </c:if>
                                </td>
                                <td>${lecciones.level.name}</td>
                                <td>${lecciones.subject.name}</td>
                                <td>${lecciones.objective.name}</td>
                                <td>${lecciones.date}</td>
                                <td>${lecciones.start}</td>
                                <td>${lecciones.finish}</td>
                                <td>
                                    <div class="col-xs-4 text-center">
                                        <input name="TXTid_lessons_attendance" class="btn-unbutton" type="image" src="<c:url value="/recursos/img/btn/btn_Attendance.svg"/>" value="${lecciones.id}" id="attendance" onclick="rowselect(${lecciones.id})" width="40px" data-placement="bottom" title="<spring:message code="etiq.txtattendance"/>">
                                    </div>

                                    <div class="col-xs-4 text-center">
                                        <input name="TXTid_lessons_detalles" type="image" src="<c:url value="/recursos/img/btn/btn_details.svg"/>" value="${lecciones.id}" id="details" onclick="detailsSelect(${lecciones.id})" width="40px" data-placement="bottom" title="<spring:message code="etiq.Details"/>">
                                    </div>
                                    <%-- the user can not delete an archived presentation,must unarchive and remove progress records first--%>
                                    <%-- <c:if test="${user.type==0 || user.type ==2}">
                                         <div class="col-xs-3">
                                             <input class="delete" name="TXTid_lessons_eliminar" type="image" src="<c:url value="/recursos/img/btn/btn_delete.svg"/>" value="${lecciones.id}" id="delete" onclick="deleteSelectSure(${lecciones.id}, '${lecciones.name}')" width="40px" data-placement="bottom" title="Delete">
                                         </div>
                                     </c:if>--%>
                                    <div class="col-xs-4 text-center">
                                        <input class="resources" name="TXTid_lessons_resources" type="image" src="<c:url value="/recursos/img/btn/btn_Resources.png"/>" value="${lecciones.id}" id="resources" onclick="accessrsrcs(${lecciones.id}, '${lecciones.name}')" width="40px" data-placement="bottom" title="<spring:message code="etiq.Resources"/>">
                                    </div>

                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </div>
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

        <div id="compartirLesson" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Share presentation:</h4>
                    </div>
                    <input type="hidden" id="compartirid" name ="compartirid" value="">
                    <div id="shareselect" class="modal-body">
                        <div class="col-xs-12">
                            <div class="col-xs-4">
                                <select class="form-control" size="20" multiple="" name="origen[]" id="origen" style="width: 100% !important;">  
                                    <c:forEach var="teacher" items="${teacherlist}" >
                                        <option value="${teacher.id}">${teacher.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-xs-3">
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px; padding-top: 50px;">
                                    <input type="button" class="btn btn-success btn-block pasar" value="Add »">
                                </div>
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                    <input type="button" class="btn btn-danger btn-block quitar" value="« Remove">
                                </div>
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                    <input type="button" class="btn btn-success btn-block pasartodos" value="Add All »">
                                </div>
                                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                    <input type="button" class="btn btn-danger btn-block quitartodos" value="« Remove All">
                                </div>
                                <!--                            <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                                                                <input type="button" class="btn btn-danger btn-block test" value="test">
                                                            </div>-->
                            </div>

                            <div class="col-xs-4">
                                <select class="form-control" size="20" multiple="" name="destino[]" id="destino" style="width: 100% !important;">
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer text-center">
                        <input type="button" id="createOnClick" class="btn btn-success" value="Share" data-dismiss="modal" onclick="compartirajax()">
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

    </body>
</html>
