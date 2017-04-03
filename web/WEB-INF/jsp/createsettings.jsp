<%-- 
    Document   : createlesson
    Created on : 30-ene-2017, 14:59:17
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="menu.jsp" %>
    <%@ include file="infouser.jsp" %>
        
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Lessons</title>
    
        <script>



            $(document).ready(function () {
                var userLang = navigator.language || navigator.userLanguage;
                var myDate = new Date();
                //Muestra calendario

                $('#fecha').datetimepicker({
                    format: 'YYYY-MM-DD',
                    locale: userLang.valueOf(),
                    daysOfWeekDisabled: [0, 6],
                    useCurrent: false//Important! See issue #1075
                            //defaultDate: '08:32:33',


                });
                $('#horainicio').datetimepicker({
                    format: 'HH:mm',
                    locale: userLang.valueOf(),
                    useCurrent: false, //Important! See issue #1075
                    stepping: 5
                });
                $('#horafin').datetimepicker({
                    format: 'HH:mm',
                    locale: userLang.valueOf(),
                    useCurrent: false, //Important! See issue #1075
                    stepping: 5
                });

                $("#horainicio").on("dp.change", function (e) {
                    $('#horafin').data("DateTimePicker").minDate(e.date);
                });

                $("#horafin").on("dp.change", function (e) {
                    $('#horainicio').data("DateTimePicker").maxDate(e.date);
                });


//       //Menu lateral
//        $('#nav-expander').on('click',function(e){
//      		e.preventDefault();
//      		$('body').toggleClass('nav-expanded');
//      	});
//      	$('#nav-close').on('click',function(e){
//      		e.preventDefault();
//      		$('body').removeClass('nav-expanded');
//      	});
//        $('#barralateral').mouseleave(function(o){
//      		o.preventDefault();
//      		$('body').removeClass('nav-expanded');
//      	});
            });

            $().ready(function ()
            {
                $('.pasar').click(function () {
                    return !$('#origen option:selected').remove().appendTo('#destino');
                });
                $('.quitar').click(function () {
                    return !$('#destino option:selected').remove().appendTo('#origen');
                });
                $('.pasartodos').click(function () {
                    $('#origen option').each(function () {
                        $(this).remove().appendTo('#destino');
                    });
                });
                $('.quitartodos').click(function () {
                    $('#destino option').each(function () {
                        $(this).remove().appendTo('#origen');
                    });
                });
                $('.submit').click(function () {
                    $('#destino option').prop('selected', 'selected');
                });
            });

            var ajax;

// function funcionCallBackLevelStudent()
//    {
//           if (ajax.readyState===4){
//                if (ajax.status===200){
//                    document.getElementById("origen").innerHTML= ajax.responseText;
//                    }
//                }
//            }
//            

//    function comboSelectionLevelStudent()
//    {
//        if (window.XMLHttpRequest) //mozilla
//        {
//            ajax = new XMLHttpRequest(); //No Internet explorer
//        }
//        else
//        {
//            ajax = new ActiveXObject("Microsoft.XMLHTTP");
//        }
//        
//        ajax.onreadystatechange=funcionCallBackLevelStudent;
//        var seleccion = document.getElementById("levelStudent").value;
//        var alumnos = document.getElementById("destino").innerHTML;
//        ajax.open("POST","createlesson.htm?select=studentlistLevel&seleccion="+seleccion,true);
//        ajax.send("");
//    }

            function funcionCallBackSubject()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        document.getElementById("subject").innerHTML = ajax.responseText;
                    }
                }
            }
            function funcionCallBackObjective()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        document.getElementById("objective").innerHTML = ajax.responseText;
                    }
                }
            }
//    function funcionCallBackLoadTemplateLessons()
//    {
//           if (ajax.readyState===4){
//                if (ajax.status===200){
//                    document.getElementById("lessons").innerHTML= ajax.responseText;
//                    }
//                }
//            }    
            //FUNCIONA PERO SOLO PUEDO PINTAR EQUIPMENT
//    function funcionCallBackTemplateLessons()
//    {
//           if (ajax.readyState===4){
//                if (ajax.status===200){
//                    document.getElementById("template").innerHTML= ajax.responseText;
//                    }
//                }
//            }
//    function funcionCallBackTemplateLessons()
//    {
//           if (ajax.readyState===4){
//                if (ajax.status===200){
//                    document.getElementById("template").innerHTML= ajax.responseText;
//                    }
//                }
//            }

           

            function comboSelectionLevel()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                $('#createOnClick').attr('disabled', true);
                ajax.onreadystatechange = funcionCallBackSubject;
                var seleccion1 = document.getElementById("level").value;
                ajax.open("POST", "createsetting.htm?select=subjectlistLevel&seleccion1=" + seleccion1, true);

                ajax.send("");

            }
            function comboSelectionSubject()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }


                ajax.onreadystatechange = funcionCallBackObjective;
                var seleccion2 = document.getElementById("subject").value;
                ajax.open("POST", "createsetting.htm?select=objectivelistSubject&seleccion2=" + seleccion2, true);

                ajax.send("");

            }

            function comboSelectionLoadTemplateLessons()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }


                ajax.onreadystatechange = funcionCallBackLoadTemplateLessons;
                var seleccionSubject = document.getElementById("subject").value;
                ajax.open("POST", "createsetting.htm?select=namelistSubject&seleccionTemplate=" + seleccionSubject, true);
                ajax.send("");
            }

             function funcionCallBackContent()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        document.getElementById("content").innerHTML = ajax.responseText;
                    }
                }
            }
            function comboSelectionObjective()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                ajax.onreadystatechange = funcionCallBackContent;
                var seleccion3 = document.getElementById("objective").value;
                ajax.open("POST", "createsetting.htm?select=contentlistObjective&seleccion3=" + seleccion3, true);
                ajax.send("");
            }
//FUNCIONES PARA AÑADIR SETTINGS
// function funcionaddSubject()
//    {
//           if (ajax.readyState===4){
//                if (ajax.status===200){
//                    document.getElementById("objective").innerHTML= ajax.responseText;
//                   }
//                }
//            }
//
//
//           function addObjective()
//            {
//                if (window.XMLHttpRequest) //mozilla
//                {
//                   ajax = new XMLHttpRequest(); //No Internet explorer
//                } else
//                {
//                   ajax = new ActiveXObject("Microsoft.XMLHTTP");
//                }
//
//                //    ajax.onreadystatechange = funcionaddSubject;
//
//                var namenewsubject = document.getElementById("subject").value;
//                var namenewobjective = document.getElementById("namenewobjective").value;
//                ajax.open("POST", "createsetting.htm?select=createsettingObjective&namenewsubject=" + namenewsubject + "&namenewobjective=" + namenewobjective, true);
//
//                ajax.send("");
//
//            }
            
            function funcionCallBackshowsetting()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        document.getElementById("nameobjective").innerHTML = ajax.responseText;
                    }
                }
            }
            
            function editObjective()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                ajax.onreadystatechange = funcionCallBackshowsetting;

                var id = document.getElementById("objective").value;
                ajax.open("POST", "createsetting.htm?select=createsettingshowsettingObjective&id=" + id, true);

                ajax.send("");

            }
            $(function () {
                $('#objective').click(function () {
                    $('#formobjetive').removeClass("hidden");
                    $('#formcontent').addClass("hidden");                   
                });
                $('#content').click(function () {
                    $('#formcontent').removeClass("hidden");
                    $('#formobjetive').addClass("hidden");
                });
            })
        </script>
        <style>
            textarea 
            {
                resize: none;
            }
        </style>
    </head>
    <body>


        <div class="container">
            <h1 class="text-center">Create Setting</h1>


            <form:form id="formSettings" method ="post" action="createsetting.htm?select=createsetting" >

                <fieldset>
                    <legend>Select setting</legend>

                    <div class="col-xs-12">
                        <div class="col-xs-3 form-group">
                            <label class="control-label"><spring:message code="etiq.txtlevels"/></label>
                            <select class="form-control" name="TXTlevel" id="level" onchange="comboSelectionLevel()">
                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels.id[0]}" >${levels.name}</option>
                                </c:forEach>
                            </select>

                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label"><spring:message code="etiq.txtsubject"/></label>
                            <select class="form-control" name="TXTsubject" id="subject" multiple size="10" onchange="comboSelectionSubject()">
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject.id[0]}" >${subject.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label">Objective</label>
                            <select class="form-control" name="TXTobjective" id="objective" multiple size="10" onchange="comboSelectionObjective()">
                                <c:forEach var="objective" items="${objectives}">
                                    <option value="${objective.id[0]}" >${objective.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label">Content</label>
                            <select class="form-control" name="TXTcontent" id="content" multiple size="10">
                                <c:forEach var="content" items="${contents}">
                                    <option value="${content.id[0]}" >${content.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </fieldset>
                <fieldset class="hidden" id="formobjetive">
                    <legend>Setting objetive</legend>  
                    <div class="col-xs-12" id="nameobjective">
                        <div class="col-xs-4">
                                <label class="control-label">Name</label>
                                <input type="text" class="form-control" name="TXTnameobjective" value="${objectiveEdit.name}">
                            </div>
                            <div class="col-xs-4">
                                <label class="control-label">Description</label>
                                <textarea type="text" class="form-control" name="TXTcommentobjective" id="commentsnewobjective" placeholder="${objectiveEdit.description}"></textarea>      
                            </div>
                        </div>

                        <div class="col-xs-3 center-block form-inline">
                            <button name="TXTid_lessons_detalles" value="" class="btn btn-detalles" id="details" data-target=".bs-example-modal-lg" onclick="addObjective()">
                                Add Objective
                                <!--<span class="glyphicon glyphicon-plus-sign" data-toggle="tooltip" data-placement="bottom" title="add objective"></span>-->
                            </button>
                            <button name="TXTid_lessons_detalles" value="" class="btn btn-detalles" id="details" data-target=".bs-example-modal-lg" onclick="delObjective()">
                                Delete 
                                <!--<span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-placement="bottom" title="delete objective"></span>-->
                            </button>
                            <button type="button" name="TXTid_lessons_detalles" value="" class="btn btn-detalles" id="details" data-target=".bs-example-modal-lg" onclick="editObjective()">
                                Edit<!--<span class="glyphicon glyphicon-pencil" data-toggle="tooltip" data-placement="bottom" title="edit objective"></span>-->
                            </button>
                        </div>

                    </div>

                </fieldset>
                <fieldset class="hidden" id="formcontent">
                    <legend>Setting content</legend>  
                    <div class="col-xs-12" style="margin-top: 20px;">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Name new objective</label>
                            <input type="text" class="form-control" name="TXTnamenewobjective" id="namenewobjective"  placeholder="Name new objective">
                        </div>
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Comments</label>
                            <input type="text" class="form-control" name="TXTnamenewobjective" id="commentsnewobjective"  placeholder="Comments">
                        </div>
                        <div class="col-xs-3 center-block form-inline">
                            <button name="TXTid_lessons_detalles" value="" class="btn btn-detalles" id="details" data-target=".bs-example-modal-lg" onclick="addObjective()">
                                <span class="glyphicon glyphicon-plus-sign" data-toggle="tooltip" data-placement="bottom" title="add objective"></span>
                            </button>
                            <button name="TXTid_lessons_detalles" value="" class="btn btn-detalles" id="details" data-target=".bs-example-modal-lg" onclick="delObjective()">
                                <span class="glyphicon glyphicon-remove-sign" data-toggle="tooltip" data-placement="bottom" title="delete objective"></span>
                            </button>
                            <button name="TXTid_lessons_detalles" value="" class="btn btn-detalles" id="details" data-target=".bs-example-modal-lg" onclick="editObjective()">
                                <span class="glyphicon glyphicon-pencil" data-toggle="tooltip" data-placement="bottom" title="edit objective"></span>
                            </button>
                        </div>

                    </div>
                </fieldset>

            </form:form>
                <fieldset>
                    <legend>Select Method</legend>

                    <div class="col-xs-12">
                        <div class="col-xs-3 center-block form-group">
                    <label class="control-label">Method</label>
                    <select class="form-control" name="method" id="method">
                    
                       <c:forEach var="method" items="${methods}">
                                <option value="${method.id[0]}" >${method.name}</option>
                            </c:forEach>
                    </select>
                </div>
                    </div>
                </fieldset>
        </div>
        <%= request.getParameter("message")%>
    </body>
</html>
