<%-- 
    Document   : createlesson
    Created on : 30-ene-2017, 14:59:17
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<!DOCTYPE html>
<html>
    <%@ include file="menu.jsp" %>
    <%@ include file="infouser.jsp" %>
        
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Lessons</title>
    
        <script>



          
          

            var ajax;
            var subjectValue = $('#subject').select("selected").val();
            var objectiveValue = $('#objective').select("selected").val();
            var contentValue = $('#content').select("selected").val();
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

            function comboSelectionLevel()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                $('#namenewobjective').empty();
                $('#descriptionnewobjective').empty();
                $('#content').empty();
                
                ajax.onreadystatechange = funcionCallBackSubject;
                var seleccion1 = document.getElementById("level").value;
                ajax.open("GET", "subjectlistLevel.htm?seleccion1=" + seleccion1, true);

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
                //Al seleccionar un subject activamos el boton Add
                if( subjectValue !== null, subjectValue !== ""){
                   $('#addObjective').attr("disabled", false);
                };
                //Al seleccionar un subject desactivamos el boton Edit
                if( objectiveValue !== null, objectiveValue !== ""){
                   $('#editObjective').attr("disabled", true);
                };
                //Ocultamos los div para add o edit otros niveles
                $('#formEditobjetive').addClass("hidden");
                $('#formAddcontent').addClass("hidden");
                $('#formEditcontent').addClass("hidden");
                //Activamos el select de objetives
                $('#objective').attr("disabled", false);
                //
                $('#objectiveSelectedForAdd').text($('#subject option:selected').text());
                
                $('#namenewobjective').empty();
                $('#descriptionnewobjective').empty();
                $('#content').empty();
                
                ajax.onreadystatechange = funcionCallBackObjective;
                var seleccion2 = document.getElementById("subject").value;
                ajax.open("GET", "objectivelistSubject.htm?seleccion2=" + seleccion2, true);

                ajax.send("");

            }

           function funcionCallBackEditDone()
           {}

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
//              
                    var seleccion3 = document.getElementById("objective").value;
                    //var p = "&seleccion3"
                    $.ajax({
                    type: "GET",
                        url: "contentlistObjective.htm?seleccion3="+seleccion3,
                        data: seleccion3,
                        dataType: 'text' ,           
                     
                        success: function(data) {
                            //console.log("success:",data);
                            
                            display(data);
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });
                function display(data) {
		var json = JSON.parse(data);
                var objective = JSON.parse(json.objective);
                var content = JSON.parse(json.content);
                
                //$('#editObjective').removeClass("disabled");
                //Ocultamos el div add objective
                $('#formAddobjetive').addClass("hidden");
                $('#formAddcontent').addClass("hidden");
                $('#formEditcontent').addClass("hidden");
                //Activamos el select de content
                $('#content').attr("disabled", false);
                //Cambiamos el nombre al que añadimos del objetive al que añadimos el content
                $('#contentSelectedForAdd').text($('#objective option:selected').text());
                //Mostramos los valores del objective seleccionado cuando editamos
                $('#editNameObjective').val(objective.name);
                $('#editDescriptionObjective').val(objective.description);
                //Al seleccionar un objective activamos el boton del Content
                if( objectiveValue !== null, objectiveValue !== ""){
                   $('#delObjective').attr("disabled", false);
                };
                //Al seleccionar un objective activamos el boton add Content
                if( objectiveValue !== null, objectiveValue !== ""){
                   $('#addContent').attr("disabled", false);
                };
                //Al seleccionar un objective activamos el boton edit
                if( objectiveValue !== null, objectiveValue !== ""){
                   $('#editObjective').attr("disabled", false);
                };
                //Al seleccionar un objective desactivamos el boton add
                if( subjectValue !== null, subjectValue !== ""){
                   $('#addObjective').attr("disabled", true);
                };
                
                if( contentValue !== null, contentValue !== ""){
                    //$('#addContent').attr("disabled", true);
                    $('#editContent').attr("disabled", true);
                };
                //Añadimos los content del objective
                $('#content').empty();
                $.each(content, function(i, item) { 
                    $('#content').append('<option value ="'+content[i].id+'">' + content[i].name + '</option>');
                });
	}
}
                
            var contentValue = $('#content').select("selected").val();
            $(function () {
                $('#addObjective').click(function () {
                    $('#formAddobjetive').removeClass("hidden");
                    $('#formcontent').addClass("hidden");
                    $('#objectiveSelectedForAdd').text($('#subject option:selected').text());
                });
                
                $('#editObjective').click(function () {
                    $('#formAddobjetive').addClass("hidden");
                    $('#formEditobjetive').removeClass("hidden");
                    $('#formcontent').addClass("hidden");
                    $('#objectiveSelectedForEdit').text($('#subject option:selected').text());
                });
                $('#content').click(function () {
                    $('#formAddobjetive').addClass("hidden");
                    $('#formEditobjetive').addClass("hidden");
                    $('#formAddcontent').addClass("hidden");
                    //$('#addContent').attr("disabled", false);
                    $('#editContent').attr("disabled", false);
                    
                    //Al seleccionar un objective desactivamos el boton add
                    if( contentValue !== null, contentValue !== ""){
                       $('#addContent').attr("disabled", true);
                    };
                });
                $('#addContent').click(function () {
                    $('#formAddcontent').removeClass("hidden");
                    $('#contentSelectedForAdd').text($('#objective option:selected').text());
                });
                $('#editContent').click(function () {
                    $('#formAddcontent').addClass("hidden");
                    $('#formEditcontent').removeClass("hidden");
                    //Añadimos el nombre del content para editarlo
                    $('#editNameContent').val($('#content option:selected').text());
                    //Añadimos el nombre del objective para saber a que objective pertenece el content que estamos editando
                    $('#contentSelectedForEdit').text($('#objective option:selected').text());
                });
                 $('#level').click(function () {
                    
                    //$('#subject').empty();
                    $('#objective').empty();
                    $('#namenewobjective').val('');
                    $('#descriptionnewobjective').val('');
                    $('#content').empty();
                });
            });
            function deleteObjective()
            {
                var seleccion = document.getElementById("objective").value;
                 $.ajax({
                    type: 'POST',
                        url: 'delObjective.htm?id='+seleccion,
                      data: seleccion,
                        dataType: 'text' ,           
                     
                        success: function(data) {                          
                           if(data==='success')  {           
                               $('#objective option:selected').remove();
           //         $('#objective').remove('option:selected');
                }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
            }
            function saveaddObjective()
            {

     //   var seleccion = document.getElementById("objective").value;
        var name = document.getElementById("namenewobjective").value;
        var description = document.getElementById("descriptionnewobjective").value;
        var subjectid = document.getElementById("subject").value;
        var myObj = {};
                myObj["name"] = name;
                myObj["description"] = description;
    //            myObj["id"] = seleccion;
                myObj["subjectid"] = subjectid;
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'addObjective.htm?data='+json,
                        data: json,
                        dataType: 'text' ,           
                     
                        success: function(data) {                          
                            var json = JSON.parse(data);                               
                    $('#objective').append('<option value = "'+json.id+'" >' + json.name + '</option>');
                                       
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
                }
            
            function saveeditObjective()
            {

        var seleccion = document.getElementById("objective").value;
        var name = document.getElementById("editNameObjective").value;
        var description = document.getElementById("editDescriptionObjective").value;
        var subjectid = document.getElementById("subject").value;
        var myObj = {};
                myObj["name"] = name;
                myObj["description"] = description;
                myObj["id"] = seleccion;
                myObj["subjectid"] = subjectid;
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'editObjective.htm?data='+json,
                        data: json,
                        dataType: 'text' ,           
                     
                        success: function(data) {
                            $('#objective').empty();
                            var json = JSON.parse(data);                            
                            $.each(json, function(i, item) { 
                    $('#objective').append('<option value = "'+json[i].id+'" >' + json[i].name + '</option>');
                });
                           
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
                }
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
                            <select class="form-control" name="TXTlevel" id="level" onclick="comboSelectionLevel()">
                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels.id[0]}" >${levels.name}</option>
                                </c:forEach>
                            </select>

                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label"><spring:message code="etiq.txtsubject"/></label>
                            <select class="form-control" name="TXTsubject" id="subject" multiple size="10" onclick="comboSelectionSubject()">
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject.id[0]}" >${subject.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label">Objective</label>
                            <select class="form-control" disabled="true" name="TXTobjective" id="objective" multiple size="10" onclick="comboSelectionObjective()">
                                <c:forEach var="objective" items="${objectives}">
                                    <option value="${objective.id[0]}" >${objective.name}</option>
                                </c:forEach>
                            </select>
                            <div class="col-xs-12" style="padding-top: 10px;">
                                <div class="col-xs-4 text-center">
                                    <input type="button" disabled data-toggle="tooltip" data-placement="bottom" value="add" id="addObjective"/>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" disabled data-toggle="tooltip" data-placement="bottom" value="edit" id="editObjective"/>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" disabled data-toggle="tooltip" data-placement="bottom" value="del" id="delObjective" onclick="deleteObjective()"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label">Content</label>
                            <select class="form-control" disabled="true" name="TXTcontent" id="content" multiple size="10">
                                <c:forEach var="content" items="${contents}">
                                    <option value="${content.id[0]}" >${content.name}</option>
                                </c:forEach>
                            </select>
                            <div class="col-xs-12" style="padding-top: 10px;">
                                <div class="col-xs-4 text-center">
                                    <input type="button" disabled data-toggle="tooltip" data-placement="bottom" value="add" id="addContent">
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" disabled data-toggle="tooltip" data-placement="bottom" value="edit" id="editContent">
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" disabled data-toggle="tooltip" data-placement="bottom" value="del" id="delContent" >
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
            </form:form>
            <form:form id="formpepi" method ="post"  >
                <fieldset class="hidden" id="formAddobjetive">
                    <legend>Add objective to <span id="objectiveSelectedForAdd"></span></legend>
                    <%--Add objective--%>
                        <div class="col-xs-3 center-block form-group" id="addObjective">
                            <label class="control-label">Name new objective</label>
                            <input type="text" class="form-control" name="TXTnamenewobjective" id="namenewobjective"  placeholder="Name">
                        </div>
                        <div class="col-xs-7 center-block form-group">
                            <label class="control-label">Comments</label>
                            <textarea type="text" class="form-control" name="TXTnamenewobjective" id="descriptionnewobjective"  placeholder="Comments"></textarea>
                        </div>
                        <div class="col-xs-2 center-block form-inline">
                            <input type="button" name="AddObjective" value="save" class="btn btn-detalles" id="AddObjective" data-target=".bs-example-modal-lg" onclick="saveaddObjective()"/>
                        </div>
                </fieldset>
                    <fieldset class="hidden" id="formEditobjetive">
                    <legend>Edit objective in <span id="objectiveSelectedForEdit"></span></legend>
                    <%--Edit objective--%>
                        <div class="col-xs-3 center-block form-group" id="addObjective">
                            <label class="control-label">Edit objective</label>
                            <input type="text" class="form-control" name="TXTeditNameObjective" id="editNameObjective"  placeholder="Name">
                        </div>
                        <div class="col-xs-7 center-block form-group">
                            <label class="control-label">Comments</label>
                            <textarea type="text" class="form-control" name="TXTeditDescriptionObjective" id="editDescriptionObjective"  placeholder="Comments"></textarea>
                        </div>
                        <div class="col-xs-2 center-block form-inline">
                            <input type="button" name="AddObjective" value="Save" class="btn btn-detalles" id="savedEditObjective" data-target=".bs-example-modal-lg" onclick="saveeditObjective()"/>
   
                        </div>
                </fieldset>
                <fieldset class="hidden" id="formAddcontent">
                    <legend>Add content to <span id="contentSelectedForAdd"></span></legend>  
                    <div class="col-xs-12" style="margin-top: 20px;">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Name new content</label>
                            <input type="text" class="form-control" name="TXTnamenewcontent" id="namenewcontent"  placeholder="Name new content">
                        </div>
<!--                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Comments</label>
                            <input type="text" class="form-control" name="TXTnamenewcontent" id="commentsnewcontent"  placeholder="Comments">
                        </div>-->
                        <div class="col-xs-3 center-block form-inline">
                            <input type="button" name="AddContent" value="save" class="btn btn-detalles" id="AddContent" data-target=".bs-example-modal-lg" onclick="addContent()"/>
                                
                            
                        </div>

                    </div>
                </fieldset>
                <fieldset class="hidden" id="formEditcontent">
                    <legend>Edit content in <span id="contentSelectedForEdit"></span></legend>  
                    <div class="col-xs-12" style="margin-top: 20px;">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Edit content</label>
                            <input type="text" class="form-control" name="TXTnameeditcontent" id="editNameContent"  placeholder="Name new content">
                        </div>
<!--                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Comments</label>
                            <input type="text" class="form-control" name="TXTnameeditcontent" id="editCommentsContent"  placeholder="Comments">
                        </div>-->
                        <div class="col-xs-3 center-block form-inline">
                            <button name="AddContent" value="" class="btn btn-detalles" id="EditContent" data-target=".bs-example-modal-lg" onclick="editContent()">
                                save
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
