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
        <title><spring:message code="etiq.setup"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
            <button type="button" class="btn btn-primary" onclick="mostrarActivityLog()"><spring:message code="etiq.activLog"/></button>
            <!--            <button type="button" class="btn btn-primary" onclick="mostrarSetup()">Setup</button>-->

            <div id="setup" class="col-xs-12">
                <h1 class="text-center"><spring:message code="etiq.setup"/></h1>
                <form:form id="formConfig" method ="post" action="save.htm" >
                    <div class="col-xs-4 text-center">
                        <h2><spring:message code="etiq.headerColor"/></h2>
                        <input type="color" id="headcolor" name="headcolor" value="#ff0000" onchange="changecolor()">
                    </div>

                    <div class="col-xs-4 text-center">
                        <h2><spring:message code="etiq.bodyColor"/></h2>
                        <input type="color" id="bodycolor" name="bodycolor" value="#ff0000" onchange="changecolor2()">
                    </div>
                    <div class="col-xs-4 text-center">
                        <input type="submit" class="btn btn-success" value="<spring:message code="etiq.set"/>">
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
                    <div class="col-xs-12 " style="margin-top: 20px;">
                        <div class='col-xs-6 col-md-4 col-lg-3 sinpadding' style="padding-right: 5px !important;">
                            <div class="form-group">
                                <label class="control-label" for="horainicio"><spring:message code="etiq.dateFrom"/></label> <span class="glyphicon glyphicon-exclamation-sign" style="color:red"></span>
                                <div class='input-group date' id='horainicio'>
                                    <input  id='horainicioInput' type='text' name="TXThorainicio" class="form-control"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class='col-xs-6 col-md-4 col-lg-3 sinpadding' style="padding-left: 5px !important;">
                            <div class="form-group">
                                <label class="control-label" for="horafin"><spring:message code="etiq.to"/></label> <span class="glyphicon glyphicon-exclamation-sign" style="color:red"></span>
                                <div class='input-group date' id='horafin'>
                                    <input id='horafinInput' type='text' name="TXThorafin" class="form-control"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="col-xs-12 sinpadding">
                        <input type="hidden" id="compartirid" name ="compartirid" value="">
                        <div id="shareselect" class="modal-body">
                            <div class="col-xs-12 sinpadding">
                                <div class="col-xs-4 sinpadding">
                                    <select class="form-control" size="20" multiple="" name="origen[]" id="origen" style="width: 100% !important;">  
                                        <c:forEach var="teacher" items="${teacherlist}" >
                                            <option value="${teacher.id}$${teacher.name}">${teacher.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-xs-4">
                                    <div class="col-xs-10">
                                        <div class="col-xs-12 text-center btnStudents" style="padding-top: 50px;">
                                            <input type="button" class="btn btn-success btn-block pasar" value="<spring:message code="etiq.txtadd"/> »">
                                        </div>
                                        <div class="col-xs-12 text-center btnStudents" >
                                            <input type="button" class="btn btn-danger btn-block quitar" value="« <spring:message code="etiq.txtremove"/>">
                                        </div>
                                        <div class="col-xs-12 text-center btnStudents">
                                            <input type="button" class="btn btn-success btn-block pasartodos" value="<spring:message code="etiq.txtaddAll"/> »">
                                        </div>
                                        <div class="col-xs-12 text-center btnStudents" >
                                            <input type="button" class="btn btn-danger btn-block quitartodos" value="« <spring:message code="etiq.txtremoveAll"/>">
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-4 sinpadding">
                                    <select class="form-control" size="20" multiple="" name="destino[]" id="destino" style="width: 100% !important;">
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>   
                    <div class="col-xs-12 text-center">
                        <input type="submit" class="btn btn-info" id="generateReport" value="<spring:message code='etiq.generateReport'/>" style="margin-top: 20px;">
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
