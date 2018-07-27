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
    <%@ include file="infouser.jsp" %>

    <head>
        <title>Edit Presentation</title>
        <script>


            var error = ${DatainBBDD};
            var mapStudents = new Map();
            $(document).ready(function () {
             //   sortSelect("subject");
                $("#linkRecommend").show();
                $("#studentsName").val("");
                $('#origen option').each(function () {
                    mapStudents[$(this).val()] = $(this).text();
                });
                $("#destino option").each(function ()
                {
                    $("#studentsName").val($("#studentsName").val().concat($(this).text().trim() + " | "));

                });
                $("#objectiveName").val($("#objective :selected").text());
                $("#subjectName").val($("#subject :selected").text());
                $("#levelName").val($("#level :selected").text());
                
                var subjectId = $("#subject :selected").val();
                
                sortSelect("subject");
                $("#subject").val(subjectId);
                
                
                $('#loadingmessage').hide();
                var userLang = navigator.language || navigator.userLanguage;
                var myDate = new Date();
                //Muestra calendario
                //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE
                var lessoncreate = '<%= request.getParameter("message")%>';

                if (lessoncreate === 'Presentation updated') {
                    $('#myModal').modal({
                        show: 'false'
                    });
                }
                //DESELECCIONA Method
                $("#deselectMethod").click(function () {
                    $("#method option:selected").prop("selected", false);
                });

                $("#showPropiertys").click(function () {
                    $("#contenedorPropiertys").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
                });
                $("#showDate").click(function () {
                    $("#contenedorDate").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
                });
                $("#showDetails").click(function () {
                    $("#contenedorDetails").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
                });
                $("#showStudents").click(function () {
                    $("#contenedorStudents").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
                });

                $("#ideaCheck").change(function () {
                    if ($(this).is(":checked")) {
                        $('#showDate').addClass("desactivada");
                        $('#showDate').off('click');
                        $("#contenedorDate").removeClass('in');
                        $('#showStudents').addClass("desactivada");
                        $('#showStudents').off('click');
                        $("#contenedorStudents").removeClass('in');

                        $("#horainicioInput").removeAttr('required');
                        $("#horafinInput").removeAttr('required');

                    } else if ($("#ideaCheck :not(:checked)"))
                    {
                        $('#showDate').removeClass("desactivada");
                        $('#showDate').on('click', function () {
                            $("#contenedorDate").toggleClass('in');
                        });
//                    $("#contenedorDate").removeClass('in');
                        $('#showStudents').removeClass("desactivada");
                        $('#showStudents').on('click', function () {
                            $("#contenedorDate").toggleClass('in');
                        });

                        $("#horainicioInput").attr('required', '');
                        $("#horafinInput").attr('required', '');
                        $("#contenedorStudents").addClass('in');
                    }
                });


                $("#NameLessons,#comments,#objective,#fechaInput,#horainicioInput,#horafinInput").change(function () {
                    if (($("#NameLessons").val().indexOf("'") === -1) && ($("#NameLessons").val().indexOf("\"") === -1) && document.getElementById("level").value !== 'Select level' && document.getElementById("objective").value !== 'Select Objective' && document.getElementById("objective").value !== '' && document.getElementById("NameLessons").value !== '' && document.getElementById("comments").value !== '') {
                        if ($("#ideaCheck:checked").length === 1) {
                            $('#saveEdit').attr('disabled', false);
                        } else { //no es idea
                            var numAlum = $('#destino option').length;
                            if ($('#fecha input').val() !== '' && $('#horainicio input').val() !== '' && $('#horafin input').val() !== '' && numAlum > 0) {
                                $('#saveEdit').attr('disabled', false);
                            } else {
                                $('#saveEdit').attr('disabled', true);
                            }
                        }
                    } else {
                        $('#saveEdit').attr('disabled', true);
                    }
                    if (($("#NameLessons").val().indexOf("'") !== -1) || ($("#NameLessons").val().indexOf("\"") !== -1)) {
                        $('#NameLessons').parent().addClass("has-error");
                        $('#NameLessons').parent().children().last().removeClass("hide");
                        $('#createOnClick').attr('disabled', true);
                    } else {
                        $('#NameLessons').parent().removeClass("has-error");
                        $('#NameLessons').parent().children().last().addClass("hide");
                    }
                    if ($(this).prop("id") === "objective" && $(this).val() !== "")
                        $("#linkRecommend").show();
                    else
                        $("#linkRecommend").hide();
                });

                $("#objective").change(function () {
                    if (($("#NameLessons").val().indexOf("'") === -1) && ($("#NameLessons").val().indexOf("\"") === -1) && document.getElementById("level").value !== 'Select level' && document.getElementById("objective").value !== 'Select Objective' && document.getElementById("objective").value !== '' && document.getElementById("NameLessons").value !== '' && document.getElementById("comments").value !== '') {
                        if ($("#ideaCheck:checked").length === 1) {
                            $('#saveEdit').attr('disabled', false);
                        } else { //no es idea
                            var numAlum = $('#destino option').length;
                            if ($('#fecha input').val() !== '' && $('#horainicio input').val() !== '' && $('#horafin input').val() !== '' && numAlum > 0) {
                                $('#saveEdit').attr('disabled', false);
                            } else {
                                $('#saveEdit').attr('disabled', true);
                            }
                        }
                    } else {
                        $('#saveEdit').attr('disabled', true);
                    }
                    if (($("#NameLessons").val().indexOf("'") !== -1) || ($("#NameLessons").val().indexOf("\"") !== -1)) {
                        $('#NameLessons').parent().addClass("has-error");
                        $('#NameLessons').parent().children().last().removeClass("hide");
                        $('#createOnClick').attr('disabled', true);
                    } else {
                        $('#NameLessons').parent().removeClass("has-error");
                        $('#NameLessons').parent().children().last().addClass("hide");
                    }

                });

                $("#saveEdit").focus(function () {
                    $('#destino option').prop('selected', true);
                });

                $("#method").on('mouseover', 'option', function (e) {

                    var $e = $(e.target);

                    if ($e.is('option')) {
                        $('#method').popover('destroy');
                        $("#method").popover({
                            animation: 'true',
                            trigger: 'hover',
                            placement: 'right',
                            title: $e.attr("data-title"),
                            content: $e.attr("data-content")
                        }).popover('show');
                    }
                });

                $('#fecha').datetimepicker({
                    format: 'YYYY-MM-DD',
                    locale: userLang.valueOf(),
                    daysOfWeekDisabled: [0, 6],
                    useCurrent: false,
                    minDate: '2000-01-01',
                    maxDate: '2099-01-01'
                            //Important! See issue #1075
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

//        QUITAR ESTUDIANTES DUPLICADOS
                var listStudentSelected = [];
                $('#destino option').each(function () {
                    listStudentSelected.push($(this).val());
                });

                jQuery.each(listStudentSelected, function (i, val) {
                    $("#origen option[value='" + val + "']").remove();

                });

                // ORDENAR DESTINO
                var selectOptions = $("#destino option");
                selectOptions.sort(function (a, b) {
                    if (a.text > b.text) {
                        return 1;
                    } else if (a.text < b.text) {
                        return -1;
                    } else {
                        return 0
                    }
                });
                $("#destino").empty().append(selectOptions);
                if (error === true) {
                    $("#msjError").removeClass("hide");
                }

                $('#fecha,#horainicio,#horafin').on('dp.change', function (e) {
                    if (($("#NameLessons").val().indexOf("'") === -1) && ($("#NameLessons").val().indexOf("\"") === -1) && document.getElementById("objective").value !== 'Select Objective' && document.getElementById("objective").value !== '' && document.getElementById("NameLessons").value !== '' && document.getElementById("comments").value !== '') {
                        if ($("#ideaCheck:checked").length === 1) {
                            $('#saveEdit').attr('disabled', false);
                        } else { //no es idea
                            var numAlum = $('#destino option').length;
                            if ($('#fecha input').val() !== '' && $('#horainicio input').val() !== '' && $('#horafin input').val() !== '' && numAlum > 0) {
                                $('#saveEdit').attr('disabled', false);
                            } else {
                                $('#saveEdit').attr('disabled', true);
                            }
                        }
                    } else {
                        $('#saveEdit').attr('disabled', true);
                    }
                    if (($("#NameLessons").val().indexOf("'") !== -1) || ($("#NameLessons").val().indexOf("\"") !== -1)) {
                        $('#NameLessons').parent().addClass("has-error");
                        $('#NameLessons').parent().children().last().removeClass("hide");
                        $('#saveEdit').attr('disabled', true);
                    } else {
                        $('#NameLessons').parent().removeClass("has-error");
                        $('#NameLessons').parent().children().last().addClass("hide");
                    }
                });
                $("#linkRecommend").click(function () {
                    var myObj = {};
                    myObj["id"] = $("#objective :selected").val()
                    var json = JSON.stringify(myObj);
                    $.ajax({
                        type: 'POST',
                        url: 'loadRecommend.htm',
                        data: json,
                        datatype: "json",
                        contentType: "application/json",
                        success: function (data) {
                            var datos = JSON.parse(data);
                            $("#recommendStudent label").remove();
                            $("#recommendStudent br").remove();
                            for (i = 0; i < datos.length; i++) {
                                // $("#recommendStudent").append(" <option value='"+datos[i]+"' >"+mapStudents[datos[i]]+"</option>")  
                                $("#recommendStudent").append("<label class='form-check-label'>\n\
                                                                <input class='form-check-input' type='checkbox' value='" + datos[i] + "' checked> \n\
                                                                    " + mapStudents[datos[i]] + "    \n\
                                                            </label><br>");
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            console.log(xhr.status);
                            console.log(xhr.responseText);
                            console.log(thrownError);
                        }


                    });
                });
                $("#btnRecommend").click(function () {


                    $("#recommendStudent label input:checked").each(function () {
                        var exist = false;
                        var value1 = $(this);
                        $('#destino option').each(function (index, value2) {
                            if (value1.val() === value2.value)
                                exist = true;

                        });
                        if (!exist) {
                            $("#destino").append(" <option value='" + value1.val() + "' >" + value1.parent().text().trim() + "</option>")
                        }
                    });

                    var numAlum = $('#destino option').length;
                    if (($("#NameLessons").val().indexOf("'") === -1) && ($("#NameLessons").val().indexOf("\"") === -1) && document.getElementById("objective").value !== 'Select Objective' && document.getElementById("objective").value !== '' && document.getElementById("NameLessons").value !== '' && document.getElementById("comments").value !== '' && $('#fecha input').val() !== '' && $('#horainicio input').val() !== '' && $('#horafin input').val() !== '' && numAlum > 0) {
                        $('#createOnClick').attr('disabled', false);
                    } else {
                        $('#createOnClick').attr('disabled', true);
                    }


                    $('#destino option').first().prop('selected', true);

                    $("#studentsName").val("");
                    $("#destino option").each(function ()
                    {
                        $("#studentsName").val($("#studentsName").val().concat($(this).text().trim() + " | "));

                    });

                    // $("#studentsName").val($("#destino").text().trim());
                    return;
                });

            });


            $().ready(function ()
            {
                $('.pasar').click(function () {
                    $('#origen option:selected').each(function () {
                        var exist = false;
                        var value1 = $(this);
                        $('#destino option').each(function (i, value2) {
                            if (value1.val() === value2.value)
                                exist = true;
                        });
                        if (!exist)
                            $("#destino").append(" <option value='" + value1.val() + "' >" + value1.text().trim() + "</option>")
                    });

                    var numAlum = $('#destino option').length;
                    if (($("#NameLessons").val().indexOf("'") === -1) && ($("#NameLessons").val().indexOf("\"") === -1) && document.getElementById("objective").value !== 'Select Objective' && document.getElementById("objective").value !== '' && document.getElementById("NameLessons").value !== '' && document.getElementById("comments").value !== '' && $('#fecha input').val() !== '' && $('#horainicio input').val() !== '' && $('#horafin input').val() !== '' && numAlum > 0) {
                        $('#saveEdit').attr('disabled', false);
                    } else {
                        $('#saveEdit').attr('disabled', true);
                    }
                    $("#studentsName").val("");
                    $("#destino option").each(function ()
                    {
                        $("#studentsName").val($("#studentsName").val().concat($(this).text().trim() + " | "));

                    });
                    //$('#destino option').first().prop('selected',true);                     
                    return;
                });


                $('.quitar').click(function () {
                    !$('#destino option:selected').remove();
                    // $('#destino option').first().prop('selected',true);

                    var alumnosSelected = $('#destino option').length;
                    var objectiveSelected = $('#objective').val();
                    if (alumnosSelected === 0 || (objectiveSelected === 0 || objectiveSelected === null || objectiveSelected === '')) {
                        $('#saveEdit').attr('disabled', true);
                    }
                    $("#studentsName").val("");
                    $("#destino option").each(function ()
                    {
                        $("#studentsName").val($("#studentsName").val().concat($(this).text().trim() + " | "));

                    });
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
                            $('#saveEdit').attr('disabled', true);
                        }
                    });

                    var numAlum = $('#destino option').length;
                    if (($("#NameLessons").val().indexOf("'") === -1) && ($("#NameLessons").val().indexOf("\"") === -1) && document.getElementById("objective").value !== 'Select Objective' && document.getElementById("objective").value !== '' && document.getElementById("NameLessons").value !== '' && document.getElementById("comments").value !== '' && $('#fecha input').val() !== '' && $('#horainicio input').val() !== '' && $('#horafin input').val() !== '' && numAlum > 0) {
                        $('#saveEdit').attr('disabled', false);
                    } else {
                        $('#saveEdit').attr('disabled', true);
                    }
                    $("#studentsName").val("");
                    $("#destino option").each(function ()
                    {
                        $("#studentsName").val($("#studentsName").val().concat($(this).text().trim() + " | "));

                    });
                    //$('#destino option').first().prop('selected',true);
                });

                $('.quitartodos').click(function () {
                    $('#destino option').each(function () {
                        $(this).remove();
                    });
                    $('#saveEdit').attr('disabled', true);
                    $("#studentsName").val("");
                });
            });

            var ajax;

            function funcionCallBackLevelStudent()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        $('#loadingmessage').hide();
                        document.getElementById("origen").innerHTML = ajax.responseText;
                    }
                }
            }


            function comboSelectionLevelStudent()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                ajax.onreadystatechange = funcionCallBackLevelStudent;
                var seleccion = document.getElementById("levelStudent").value;
                var alumnos = document.getElementById("destino").innerHTML;
                ajax.open("POST", "studentlistLevel.htm?seleccion=" + seleccion, true);
                ajax.send("");
            }

            function funcionCallBackSubject()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        $('#loadingmessage').hide();
                        $('#subject').empty();
                        var json = JSON.parse(ajax.responseText);
                        for (i in json) {
                            $('#subject').append("<option value='"
                                    + json[i].id[0] + "'>" + json[i].name + "</option>");
                        }
                        sortSelect("subject");
                        $('#subject').val(-1);
                    }
                }
            }
            function funcionCallBackObjective()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        var json = JSON.parse(ajax.responseText);
                        $('#objective').empty();
                        for (var i = 0; i < json.length; i += 1) {
                            if (i === 0)
                                $('#objective').append("<option value='"
                                        + -1 + "'>" + json[i].name + "</option>");
                            else
                                $('#objective').append("<option value='"
                                        + json[i].id[0] + "'>" + json[i].name + "</option>");

                        }
                    }
                }
            }


            function funcionCallBackIdeaLessons()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        $('#loadingmessage').hide();
                        var json = JSON.parse(ajax.responseText);
                        var level = json.level;
                        var subject = JSON.parse(json.subject).id;

                        var objective = JSON.parse(json.objective).id;
                        var method = JSON.parse(json.method).id;
                        var content = JSON.parse(json.content);
                        var subjects = JSON.parse(json.subjectslist);
                        var objectives = JSON.parse(json.objectiveslist);
                        var contents = JSON.parse(json.contentslist);
                        var comment = json.comment;

                        $("#comments").val(comment);
                        $('#level option[value="' + level + '"]').attr("selected", true);
                        $('#method option[value="' + method + '"]').attr("selected", true);
                        $('#subject').empty();
                        var pos1 = subject[0].toString();
                         $.each(subjects, function (i, item) {
                            var test = subjects[i].id;
                            if (typeof test !== "undefined") {
                                var test1 = test.toString();
                            }
                            ;
                            if (test1 === pos1) {
                                 $('#subject').append('<option selected value= "' + subjects[i].id + '">' + subjects[i].name + '</option>');
                            } else {
                                 $('#subject').append('<option value= "' + subjects[i].id + '">' + subjects[i].name + '</option>');
                            }
                        });
                        sortSelect("subject");
                        $('#objective').empty();
                        var pos1 = objective[0].toString();
                         $.each(objectives, function (i, item) {
                            var test = objectives[i].id;
                            if (typeof test !== "undefined") {
                                var test1 = test.toString();
                            }
                            ;
                            if (test1 === pos1) {
                                 $('#objective').append('<option selected value= "' + objectives[i].id + '">' + objectives[i].name + '</option>');
                            } else {
                                 $('#objective').append('<option value= "' + objectives[i].id + '">' + objectives[i].name + '</option>');
                            }
                        });
                         sortSelect("objective");
                        $('#content').empty();
                        var pos1 = content.toString();
                         $.each(contents, function (i, item) {
                            var test = contents[i].id;
                            if (typeof test !== "undefined") {
                                var test1 = test.toString();
                            }
                            ;
                            if (jQuery.inArray(test1, pos1) !== -1) {
                                 $('#content').append('<option selected value= "' + contents[i].id + '">' + contents[i].name + '</option>');
                            } else {
                                 $('#content').append('<option value= "' + contents[i].id + '">' + contents[i].name + '</option>');
                            }
                        });
                    }
                }
            }

            function funcionCallBackContent()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        $('#content').empty();
                        var json = JSON.parse(ajax.responseText);
                        for (var i = 0; i < json.length; i += 1)
                            $('#content').append("<option value='" + json[i].id[0] + "'>"
                                    + json[i].name + "</option>");
                    }
                }
            }

            function seleccionarTodos() {
                $('#destino option').first().prop('selected', true);
            }

            function comboSelectionLevel()
            {
                $('#content').empty();
                if ($('#level').val() !== -1) {
                    if (window.XMLHttpRequest) //mozilla
                    {
                        ajax = new XMLHttpRequest(); //No Internet explorer
                    } else
                    {
                        ajax = new ActiveXObject("Microsoft.XMLHTTP");
                    }
                    $("#levelName").val($("#level :selected").text());
                    $('#loadingmessage').show();
                    $('#createOnClick').attr('disabled', true);
                    $('#objective').val("");
                    $('#subject').val("");
                    $("#linkRecommend").hide();

                    ajax.onreadystatechange = funcionCallBackSubject;
                    var seleccion1 = document.getElementById("level").value;
                    ajax.open("POST", "subjectlistLevel.htm?seleccion1=" + seleccion1, true);
                    $('#objective').val("");
                    $('#subject').val("");
                    sortSelect("subject");
                    $('#subject').val(-1);
                    ajax.send("");
                }
            }
            function comboSelectionSubject()
            {
                $('#content').empty();
                $("#subjectName").val($("#subject :selected").text());
                $("#linkRecommend").hide();
                if ($('#subject').val() !== -1) {
                    if (window.XMLHttpRequest) //mozilla
                    {
                        ajax = new XMLHttpRequest(); //No Internet explorer
                    } else
                    {
                        ajax = new ActiveXObject("Microsoft.XMLHTTP");
                    }


                    ajax.onreadystatechange = funcionCallBackObjective;
                    var seleccion2 = document.getElementById("subject").value;
                    ajax.open("POST", "objectivelistSubject.htm?seleccion2=" + seleccion2, true);
                    // $('#objective').val("");
                    ajax.send("");
                }
            }

            function comboSelectionIdeaLessons()
            {

                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }
                if ($("#ideas :selected").text() !== "Select an idea") {
                    $('#loadingmessage').show();
                    ajax.onreadystatechange = funcionCallBackIdeaLessons;
                    var seleccionidea = document.getElementById("ideas").value;
                    //ajax.open("POST","createlesson.htm?select=objectivelistSubject&seleccion2="+seleccionTemplate,true);
                    ajax.open("POST", "copyfromIdea.htm?seleccionidea=" + seleccionidea, true);
                    ajax.send("");
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
                $("#objectiveName").val($("#objective :selected").text());

                if (document.getElementById("objective").value === 0 || document.getElementById("objective").value === '' || document.getElementById("destino").length === 0) {
                    $('#saveEdit').attr('disabled', true);
                } else {
                    $('#saveEdit').attr('disabled', false);
                }
                ajax.onreadystatechange = funcionCallBackContent;
                var seleccion3 = document.getElementById("objective").value;
                ajax.open("POST", "contentlistObjective.htm?seleccion3=" + seleccion3, true);
                ajax.send("");
            }


            $(function () {
                $('.required-icon').tooltip({
                    placement: 'left',
                    title: 'Required field'
                });
            });
        </script>
        <style>
            #recommendStudent label{
                font-weight: normal !important;
            }
            textarea 
            {
                resize: none;
            }
            .popover{
                width: 500px;
            }
            .unStyle
            {
                text-align: right;
                background-color: transparent !important;
                outline: none !important;
                box-shadow: none;
                border: none;
            }
            .desactivada
            {
                color: grey;
                text-decoration: line-through;
            }
            .btn span.glyphicon {    			
                opacity: 0;				
            }
            .btn.active span.glyphicon {				
                opacity: 1;				
            }
            /*STILOS CHECKBOX*/

            .checkbox {
                padding-left: 20px; }
            .checkbox label {
                display: inline-block;
                vertical-align: middle;
                position: relative;
                padding-left: 5px; }
            .checkbox label::before {
                content: "";
                display: inline-block;
                position: absolute;
                width: 17px;
                height: 17px;
                left: 0;
                margin-left: -20px;
                border: 1px solid #cccccc;
                border-radius: 3px;
                background-color: #fff;
                -webkit-transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
                -o-transition: border 0.15s ease-in-out, color 0.15s ease-in-out;
                transition: border 0.15s ease-in-out, color 0.15s ease-in-out; }
            .checkbox label::after {
                display: inline-block;
                position: absolute;
                width: 16px;
                height: 16px;
                left: 0;
                top: 0;
                margin-left: -20px;
                padding-left: 3px;
                padding-top: 1px;
                font-size: 11px;
                color: #555555; }
            .checkbox input[type="checkbox"],
            .checkbox input[type="radio"] {
                opacity: 0;
                z-index: 1;
                cursor: pointer;
            }
            .checkbox input[type="checkbox"]:focus + label::before,
            .checkbox input[type="radio"]:focus + label::before {
                outline: thin dotted;
                outline: 5px auto -webkit-focus-ring-color;
                outline-offset: -2px; }
            .checkbox input[type="checkbox"]:checked + label::after,
            .checkbox input[type="radio"]:checked + label::after {
                font-family: "fontprincipal";
                content: '✔';}
            .checkbox input[type="checkbox"]:indeterminate + label::after,
            .checkbox input[type="radio"]:indeterminate + label::after {
                display: block;
                content: "";
                width: 10px;
                height: 3px;
                background-color: #555555;
                border-radius: 2px;
                margin-left: -16.5px;
                margin-top: 7px;
            }
            .checkbox input[type="checkbox"]:disabled,
            .checkbox input[type="radio"]:disabled {
                cursor: not-allowed;
            }
            .checkbox input[type="checkbox"]:disabled + label,
            .checkbox input[type="radio"]:disabled + label {
                opacity: 0.65; }
            .checkbox input[type="checkbox"]:disabled + label::before,
            .checkbox input[type="radio"]:disabled + label::before {
                background-color: #eeeeee;
                cursor: not-allowed; }
            .checkbox.checkbox-circle label::before {
                border-radius: 50%; }
            .checkbox.checkbox-inline {
                margin-top: 0; }


            .checkbox-success input[type="checkbox"]:checked + label::before,
            .checkbox-success input[type="radio"]:checked + label::before {
                background-color: #99CC66;
                border-color: #99CC66; }
            .checkbox-success input[type="checkbox"]:checked + label::after,
            .checkbox-success input[type="radio"]:checked + label::after {
                color: #fff;}



            .checkbox-success input[type="checkbox"]:indeterminate + label::before,
            .checkbox-success input[type="radio"]:indeterminate + label::before {
                background-color: #99CC66;
                border-color: #99CC66;
            }

            .checkbox-success input[type="checkbox"]:indeterminate + label::after,
            .checkbox-success input[type="radio"]:indeterminate + label::after {
                background-color: #fff;
            }


            input[type="checkbox"].styled:checked + label:after,
            input[type="radio"].styled:checked + label:after {
                font-family: 'fontprincipal';
                content: '✔'; }
            input[type="checkbox"] .styled:checked + label::before,
            input[type="radio"] .styled:checked + label::before {
                color: #fff; }
            input[type="checkbox"] .styled:checked + label::after,
            input[type="radio"] .styled:checked + label::after {
                color: #fff; }

            /*=========================== nuevo */

            .required-field-block {
                position: relative;   
            }

            .required-field-block .required-icon {
                display: inline-block;
                vertical-align: middle;
                margin: -0.25em 0.25em 0em;
                background-color: #E8E8E8;
                border-color: #E8E8E8;
                padding: 0.5em 0.8em;
                color: rgba(0, 0, 0, 0.65);
                text-transform: uppercase;
                font-weight: normal;
                border-radius: 0.325em;
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                -ms-box-sizing: border-box;
                box-sizing: border-box;
                -webkit-transition: background 0.1s linear;
                -moz-transition: background 0.1s linear;
                transition: background 0.1s linear;
                font-size: 75%;
            }

            .required-field-block .required-icon {
                background-color: transparent;
                position: absolute;
                top: 0em;
                right: 0em;
                z-index: 10;
                margin: 0em;
                width: 30px;
                height: 30px;
                padding: 0em;
                text-align: center;
                -webkit-transition: color 0.2s ease;
                -moz-transition: color 0.2s ease;
                transition: color 0.2s ease;
            }

            .required-field-block .required-icon:after {
                position: absolute;
                content: "";
                right: 1px;
                top: 1px;
                z-index: -1;
                width: 0em;
                height: 0em;
                border-top: 0em solid transparent;
                border-right: 30px solid transparent;
                border-bottom: 30px solid transparent;
                border-left: 0em solid transparent;
                border-right-color: inherit;
                -webkit-transition: border-color 0.2s ease;
                -moz-transition: border-color 0.2s ease;
                transition: border-color 0.2s ease;
            }

            .required-field-block .required-icon .text {
                color: #B80000;
                font-size: 26px;
                margin: -3px 0 0 12px;
            }

            .marginSelect{
                margin-right: 17px !important;
            }
            .marginStudents{
                margin-right: 80% !important;
            }
        </style>
    </head>
    <body>
        <div class="container">

            <div id="msjError" class="hide col-xs-12 alert alert-warning alert-dismissable fade in">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong>Note: </strong>When removing learners, their progress data related to this presentation will be lost.
            </div>
            <h1 class="text-center">Edit Presentation</h1>


            <form:form id="formStudents" method ="post" action="save.htm?id=${id}" >
                <fieldset>
                    <legend id="showPropiertys">
                        Presentation name and description
                        <span class="col-xs-12 text-right glyphicon glyphicon-triangle-bottom">
                        </span>
                    </legend>
                    <div class="form-group collapse" id="contenedorPropiertys">
                        <div class="col-xs-6 center-block">
                            <label class="control-label">Presentation Name</label> 
                            <div class="required-field-block">
                                <input type="text" class="form-control" name="TXTnombreLessons" id="NameLessons" required="" placeholder="<spring:message code="etiq.namelessons"/>" value="${data.name}">
                                <div class="required-icon">
                                    <div class="text">*</div>
                                </div>
                            </div>

                            <span class="help-block hide">Invalid Name (Nor contain special characters)</span>
                        </div>               
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Presentation description</label> 
                            <div class="required-field-block">
                                <textarea class="form-control" name="TXTdescription" id="comments" placeholder="add description" maxlength="200"  spellcheck="true">${data.comments}</textarea>
                                <div class="required-icon">
                                    <div class="text">*</div>
                                </div>
                            </div>   

                        </div>
                        <!--                    <div class="col-xs-6 center-block checkbox checkbox-success">
                                                <input class="styled" type="checkbox" id="ideaCheck" name="ideaCheck">
                                                <label for="ideaCheck" >
                                                    Presentation idea
                                                </label>
                                                
                                            </div>-->
                    </div>
                </fieldset>
                <fieldset>
                    <legend id="showDate">
                        Select date and time
                        <span class="col-xs-12 text-right glyphicon glyphicon-triangle-bottom">
                            <!--                        <button type="button" class="unStyle" data-toggle="collapse" data-target="#contenedorDate" >
                                                        <span class="glyphicon glyphicon-triangle-bottom"></span>
                                                    </button>-->
                        </span>
                    </legend>
                    <div class="form-group collapse" id="contenedorDate">
                        <div class='col-xs-4'>
                            <div class="form-group">
                                <label class="control-label" for="fecha">Date</label> 
                                <div class='input-group date' id='fecha'>
                                    <div class="required-field-block">
                                        <input id='fechaInput' type='text' name="TXTfecha" class="form-control" id="fecha" required="required" value="${data.date}"/>
                                        <div class="required-icon">
                                            <div class="text">*</div>
                                        </div>
                                    </div>

                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class='col-xs-4'>
                            <div class="form-group">
                                <label class="control-label" for="horainicio">Start hour</label> 
                                <div class='input-group date' id='horainicio'>
                                    <div class="required-field-block">

                                        <input id='horainicioInput' type='text' name="TXThorainicio" class="form-control" required="required"  value="${data.start}"/>
                                        <div class="required-icon">
                                            <div class="text">*</div>
                                        </div>
                                    </div>


                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class='col-xs-4'>
                            <div class="form-group">
                                <label id='horafinInput' class="control-label" for="horafin">Finish hour</label> 
                                <div class='input-group date' id='horafin'>
                                    <div class="required-field-block">
                                        <input type='text' name="TXThorafin" class="form-control" required="required" value="${data.finish}"/>
                                        <div class="required-icon">
                                            <div class="text">*</div>
                                        </div>
                                    </div>

                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <fieldset>
                    <legend id="showDetails">
                        Presentation details
                        <span class="col-xs-12 text-right glyphicon glyphicon-triangle-bottom">
                            <!--                        <button type="button" class="unStyle" data-toggle="collapse" data-target="#contenedorDetails" >
                                                        <span class="glyphicon glyphicon-triangle-bottom"></span>
                                                    </button>-->
                        </span>
                    </legend>
                    <div class="form-group collapse" id="contenedorDetails">
                        <div class="col-xs-3 form-group">
                            <label class="control-label"><spring:message code="etiq.txtlevels"/></label>
                            <input type="hidden" id="levelName" name="levelName" value = "">

                            <div class="required-field-block">
                                <select class="form-control" name="TXTlevel" id="level" onchange="comboSelectionLevel()">
                                    <c:forEach var="levels" items="${gradelevels}">
                                        <c:if test="${levels.id[0] == data.level.id[0]}">
                                            <c:set var="mySelectVar" value="selected"></c:set>
                                        </c:if>
                                        <option value="${levels.id[0]}" ${(mySelectVar eq 'selected')?'selected' : ''}>${levels.name}</option>
                                        <c:set var="mySelectVar" value=""></c:set>
                                    </c:forEach>
                                </select>

                                <div class="required-icon marginSelect">
                                    <div class="text">*</div>
                                </div>
                            </div>


                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label"><spring:message code="etiq.txtsubject"/></label>
                            <input type="hidden" id="subjectName" name="subjectName" value = "">                            

                            <div class="required-field-block">
                                <select class="form-control" name="TXTsubject" id="subject"  onchange="comboSelectionSubject()">

                                    <c:forEach var="subject" items="${subjects}">
                                        <c:if test="${subject.id[0] == data.subject.id[0]}">
                                            <c:set var="mySelectVar" value="selected"></c:set>
                                        </c:if>
                                        <option value="${subject.id[0]}" ${(mySelectVar eq 'selected')?'selected' : ''} data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txthome"/>">${subject.name}</option>
                                        <c:set var="mySelectVar" value=""></c:set>     
                                    </c:forEach>

                                </select>

                                <div class="required-icon marginSelect">
                                    <div class="text">*</div>
                                </div>
                            </div>

                        </div>          

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Objective</label>
                            <input type="hidden" id="objectiveName" name="objectiveName" value = "">                                                        

                            <div class="required-field-block">
                                <select class="form-control" name="TXTobjective" id="objective" onchange="comboSelectionObjective()">
                                    <c:forEach var="objective" items="${objectives}">
                                        <c:if test="${objective.id[0] == data.objective.id[0]}">
                                            <c:set var="mySelectVar" value="selected"></c:set>
                                        </c:if>
                                        <option value="${objective.id[0]}" ${(mySelectVar eq 'selected')?'selected' : ''} >${objective.name}</option>
                                        <c:set var="mySelectVar" value=""></c:set>     
                                    </c:forEach>
                                </select>

                                <div class="required-icon marginSelect">
                                    <div class="text">*</div>
                                </div>
                            </div>
                            <div>
                                <a id="linkRecommend" data-toggle="modal" href="#recommendations" class="disabled">Recommended Students<br>for this objective</a>
                            </div> 

                        </div>

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Equipment</label>
                            <c:if test="${not empty data.contentid}">
                                <select class="form-control" name="TXTcontent" id="content" multiple>
                                    <c:forEach var="content" items="${contents}">   
                                        <c:set var="mySelectVar" value=""></c:set>
                                        <c:forEach var="selcontent" items="${data.contentid}" varStatus="i">
                                            <c:if test="${content.id[0] == data.contentid[i.count-1]}">                             
                                                <c:set var="mySelectVar" value="selected"></c:set>
                                            </c:if>
                                        </c:forEach>                      
                                        <option value="${content.id[0]}" ${(mySelectVar eq 'selected')?'selected' : ''}>${content.name}</option>
                                        <c:set var="mySelectVar" value=""></c:set>
                                    </c:forEach>
                                </select>
                            </c:if>
                            <c:if test="${empty data.contentid}">
                                <select class="form-control" name="TXTcontent" id="content" multiple>
                                    <c:forEach var="content" items="${contents}">
                                        <option value="${content.id[0]}" >${content.name}</option>
                                    </c:forEach>
                                </select>
                                <input type="button" class="btn btn-info" id="deselectContent" onclick="$('#content option:selected').prop('selected', false);" Title="deselect content" value="x">
                            </c:if>
                        </div>

                        <div class="col-xs-12" id="divLoadLessons" style="padding-left: 0px;">   
                            <div class="col-xs-3 center-block form-group">
                                <label class="control-label">Copy from idea</label>
                                <select class="form-control" name="ideas" id="ideas" onchange="comboSelectionIdeaLessons()">  
                                    <c:forEach var="idea" items="${ideas}">
                                        <option value="${idea.id}" >${idea.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                        </div>    
                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Method</label>
                            <select class="form-control" name="TXTmethod" size="2" id="method"> 
                                <c:forEach var="method" items="${methods}">
                                    <c:if test="${method.id[0] == data.method.id[0]}">
                                        <c:set var="mySelectVar" value="selected"></c:set>
                                    </c:if>
                                    <option value="${method.id[0]}"  data-title="${method.description}"  ${(mySelectVar eq 'selected')?'selected' : ''} data-content="${method.description}">${method.name}</option>
                                    <c:set var="mySelectVar" value=""></c:set>
                                </c:forEach>
                            </select>
                            <input type="button" class="btn btn-info" id="deselectMethod"  Title="deselect Method" value="x">
                        </div>

                        <!--                    <div class="col-xs-12 center-block form-group">
                                                <label class="control-label">Attachments</label>
                                                <input type="file" class="form-control" name="TXTfile" id="file">
                                            </div>-->
                    </div>
                </fieldset>
                <fieldset>
                    <div class="required-field-block">
                        <legend id="showStudents">
                            Select Learners 
                            <span class="col-xs-12 text-right glyphicon glyphicon-triangle-bottom"></span>
                        </legend>

                        <div class="required-icon marginStudents">
                            <div class="text">*</div>
                        </div>
                    </div>

                    <div class="form-group collapse in" id="contenedorStudents">
                        <div class="col-xs-12">
                            <div class="col-xs-2"></div>
                            <div class="col-xs-3">
                                <label>Filter</label>     
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <div class="col-xs-2">
                                <select class="form-control" name="levelStudent" id="levelStudent" style="width: 100% !important;" onchange="comboSelectionLevelStudent()">
                                    <c:forEach var="levels" items="${gradelevels}">
                                        <option value="${levels.id[0]}" >${levels.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-xs-3">
                                <input type="hidden" id="studentsName" name="studentsName" value = "">                                                        

                                <select class="form-control" size="20" multiple name="origen[]" id="origen" style="width: 100% !important;">
                                    <c:forEach var="alumnos" items="${listaAlumnos}" >
                                        <option value="${alumnos.id_students}" >${alumnos.nombre_students}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-xs-2">
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

                            <div class="col-xs-3">
                                <select class="form-control" size="20" multiple name="destino[]" id="destino" style="width: 100% !important;"> 
                                    <c:forEach var="studentsSelected" items="${data.students}">
                                        <option value="${studentsSelected.id_students}" selected="" >${studentsSelected.nombre_students}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div class="col-xs-12 text-center">
                    <input type="submit" class="btn btn-success" id="saveEdit" value="Save">
                </div>
            </form:form>

        </div>
        <%--        <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"></label>
                                <div class='input-group' style="margin-top:19px;">
                                    <form:form id="formCreate" action="createsetting.htm?select=start">
                                       <button type="submit" id="crearLessons" value="Crear" class="btn btn-success">Create Settings</button>
                                    </form:form>
                                </div>
                        </div>
                </div>--%>
        <!--<div class="col-xs-12">
        <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
          Launch demo modal
        </button>
        </div>-->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <!--        <h4 class="modal-title" id="myModalLabel">Modal title</h4>-->
                    </div>
                    <div class="modal-body text-center">
                        <H1><%= request.getParameter("message")%></H1>
                    </div>
                    <!--      <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary">Save changes</button>
                          </div>-->
                </div>
            </div>
        </div>
        <div class="modal fade" id="recommendations" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title"> Students to select</h4>
                    </div>
                    <div class="modal-body text-center">
                        <div class="row">
                            <div class="col-xs-offset-3 col-xs-6 form-check text-left" name="recommendName" id="recommendStudent">

                            </div>
                        </div>
                        <div class="row">
                            <div class="text-center">
                                <input id='btnRecommend' type="button" class="btn btn-primary" value="Save"/>
                            </div>
                        </div>
                    </div>
                    <!--      <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary">Save changes</button>
                          </div>-->
                </div>
            </div>
        </div>
        <div class="divLoadStudent" id="loadingmessage">
            <div class="text-center"> 
                <img class="imgLoading" src='../recursos/img/large_loading.gif'/>
            </div>
        </div>


    </body>
</html>
