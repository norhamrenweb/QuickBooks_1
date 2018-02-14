<%-- 
    Document   : setup
    Created on : 24-ene-2018, 10:10:20
    Author     : Norhan
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
        <title>Setup</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>

            $(document).ready(function () {

                $('#generateReport').prop("disabled", true);
                $('#setup').hide();
                $('#report').hide();

                var userLang = navigator.language || navigator.userLanguage;

                $('#horainicio').datetimepicker({
                    format: 'DD/MM/YYYY',
                    locale: userLang.valueOf(),
                    useCurrent: false, //Important! See issue #1075
                    stepping: 5
                });

                $('#horafin').datetimepicker({
                    format: 'DD/MM/YYYY',
                    locale: userLang.valueOf(),
                    useCurrent: false, //Important! See issue #1075
                    stepping: 5
                });

                $("#horainicio").on("dp.change", function (e) {
                    $('#horafin').data("DateTimePicker").minDate(e.date);
                    if ($('#horafinInput').val() !== "" && $('#horainicioInput').val() !== "" && $('#destino > option').length > 0) {
                        $('#generateReport').prop("disabled", false);
                    } else {
                        $('#generateReport').prop("disabled", true);
                    }
                });

                $("#horafin").on("dp.change", function (e) {
                    $('#horainicio').data("DateTimePicker").maxDate(e.date);
                    if ($('#horafinInput').val() !== "" && $('#horainicioInput').val() !== "" && $('#destino > option').length > 0) {
                        $('#generateReport').prop("disabled", false);
                    } else {
                        $('#generateReport').prop("disabled", true);
                    }
                });


                $('.pasar').click(function () {
                    var exist = false;
                    $('#destino option').each(function () {
                        if ($('#origen option:selected').val() === $(this).val())
                            exist = true;
                    });

                    if (!exist)
                        !$('#origen option:selected').clone().appendTo('#destino');
                    $('#destino option').first().prop('selected', true);
                    if ($('#horafinInput').val() !== "" && $('#horainicioInput').val() !== "" && $('#destino > option').length > 0) {
                        $('#generateReport').prop("disabled", false);
                    } else {
                        $('#generateReport').prop("disabled", true);
                    }

                    return;
                });


                $('.quitar').click(function () {
                    !$('#destino option:selected').remove();
                    $('#destino option').first().prop('selected', true);

                    if ($('#horafinInput').val() !== "" && $('#horainicioInput').val() !== "" && $('#destino > option').length > 0) {
                        $('#generateReport').prop("disabled", false);
                    } else {
                        $('#generateReport').prop("disabled", true);
                    }

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
                    });

                    $('#destino option').first().prop('selected', true);
                    if ($('#horafinInput').val() !== "" && $('#horainicioInput').val() !== "" && $('#destino > option').length > 0) {
                        $('#generateReport').prop("disabled", false);
                    } else {
                        $('#generateReport').prop("disabled", true);
                    }
                });

                $('.quitartodos').click(function () {
                    $('#destino option').each(function () {
                        $(this).remove();
                    });
                    $('#generateReport').prop("disabled", true);

                });
                $("#generateReport").focus(function () {
                    $('#destino option').prop('selected', true);
                });
                
            });

            function changecolor() {
                var color = $('#headcolor').val();
                $('#infousuario').css("background-color", color);
            }
            function changecolor2() {
                var color = $('#bodycolor').val();
                $('body').css("background-color", color);
            }
            function mostrarActivityLog() {

                $('#setup').hide();
                $('#report').show();
            }
            function mostrarSetup() {
                $('#setup').show();
                $('#report').hide();
            }
        </script>
    </head>
    <body>

        <div class="container">
            <button type="button" class="btn btn-primary" onclick="mostrarActivityLog()">Activity Log</button>
            <button type="button" class="btn btn-primary" onclick="mostrarSetup()">Setup</button>

            <div id="setup" class="col-xs-12">
                <h1 class="text-center">Setup</h1>
                <form:form id="formConfig" method ="post" action="save.htm" >
                    <div class="col-xs-4 text-center">
                        <h2>Header Color</h2>
                        <input type="color" id="headcolor" name="headcolor" value="#ff0000" onchange="changecolor()">
                    </div>
                    <div class="col-xs-4 text-center">
                        <h2>Body Color</h2>
                        <input type="color" id="bodycolor" name="bodycolor" value="#ff0000" onchange="changecolor2()">
                    </div>
                    <div class="col-xs-4 text-center">
                        <input type="submit" class="btn btn-success" value="set">
                    </div>
                </form:form>
                <form:form enctype="multipart/form-data" id="formIcono" method ="post" action="/QuickBooks_1/setupicono" >
                    <div class="col-xs-12" style="margin-top: 30px">
                        <div class="col-xs-4" >
                            <input type="file" name="fileToUpload" id="icono" accept="image/*">
                        </div>
                        <div class="col-xs-4 text-center">
                            <input type="submit" onclick="$('#myModal').modal({show: 'true'});" class="btn btn-success" value="set">
                        </div>
                    </div>
                </form:form>
            </div>
            <div id="report" class="col-xs-12">
                <c:url var="post_url"  value="/html" />
                <form:form id="formStudents" method ="post" action="${post_url}" >

                    <div class='col-xs-4'>
                        <div class="form-group">
                            <label class="control-label" for="horainicio">Start hour</label> <span class="glyphicon glyphicon-exclamation-sign" style="color:red"></span>
                            <div class='input-group date' id='horainicio'>
                                <input  id='horainicioInput' type='text' name="TXThorainicio" class="form-control"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class='col-xs-4'>
                        <div class="form-group">
                            <label class="control-label" for="horafin">Finish hour</label> <span class="glyphicon glyphicon-exclamation-sign" style="color:red"></span>
                            <div class='input-group date' id='horafin'>
                                <input id='horafinInput' type='text' name="TXThorafin" class="form-control"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>
                    </div>


                    <div class="col-xs-4">
                        <input type="submit" class="btn btn-info" id="generateReport" value="Generate Report">
                    </div>


                    <div class="col-xs-12">
                        <input type="hidden" id="compartirid" name ="compartirid" value="">
                        <div id="shareselect" class="modal-body">
                            <div class="col-xs-12">
                                <div class="col-xs-4">
                                    <select class="form-control" size="20" multiple="" name="origen[]" id="origen" style="width: 100% !important;">  
                                        <c:forEach var="teacher" items="${teacherlist}" >
                                            <option value="${teacher.id}$${teacher.name}">${teacher.name}</option>
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
                                </div>

                                <div class="col-xs-4">
                                    <select class="form-control" size="20" multiple="" name="destino[]" id="destino" style="width: 100% !important;">
                                    </select>
                                </div>
                            </div>
                        </div
                    </form:form>
                </div>
            </div>

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
