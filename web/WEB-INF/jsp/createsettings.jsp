<%-- 
    Document   : createlesson
    Created on : 30-ene-2017, 14:59:17
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <%@ include file="menu.jsp" %>
        
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SOW</title>
    
        <script>
$(document).ready(function(){


//  var i=1;
//  
// $("#add_row").click(function(){
//         
////      $('#addr'+i).html("<td class='text-center' style='width: 20%; vertical-align: middle;'>"+ (i+1) +"</td><td><input id='country"+i+"' name='country"+i+"' type='text' placeholder='Step'  class='form-control input-md'></td>");
////      var NameStep = $('input[id='+(i-1)+']').val();
//      var table = document.getElementById("tab_logic");
//        var rowCount = table.rows.length;
////      $('#steps').append("<li id="+(rowCount+1)+">"+ NameStep +"</li>");
////del step ,if the id is zero will not add to the del array or the updated array, always will be only in the new array,because it was a step that was added and removed while editing
//      $('#tab_logic').append("<tr>\n\
//                        <td class='text-center' style='width: 10%; vertical-align: middle;'>"+(rowCount+1) +"</td>\n\
//                        <td>\n\
//                            <input id='0' name='' value='' type='text' placeholder='Step' class='form-control input-md'>\n\
//                        </td>\n\
//\n\                     <td>\n\
//                          <a id='delete_row' class='pull-right btn btn-default' onclick='delstep()'>Del</a>\n\
//                        </td>\n\
//                        </tr>");
////      i++; 
// });
// $("#add_row2").click(function(){
//         
////      $('#addr'+i).html("<td class='text-center' style='width: 20%; vertical-align: middle;'>"+ (i+1) +"</td><td><input id='country"+i+"' name='country"+i+"' type='text' placeholder='Step'  class='form-control input-md'></td>");
////      var NameStep = $('input[id='+(i-1)+']').val();
//      var table = document.getElementById("tab_logic2");
//        var rowCount = table.rows.length;
////      $('#steps').append("<li id="+(rowCount+1)+">"+ NameStep +"</li>");
////del step ,if the id is zero will not add to the del array or the updated array, always will be only in the new array,because it was a step that was added and removed while editing
//      $('#tab_logic2').append("<tr>\n\
//                        <td class='text-center' style='width: 10%; vertical-align: middle;'>"+(rowCount+1) +"</td>\n\
//                        <td>\n\
//                            <input id='0' name='' value='' type='text' placeholder='Step' class='form-control input-md'>\n\
//                        </td>\n\
//\n\                     <td>\n\
//                          <a id='delete_row' class='pull-right btn btn-default' onclick='delstep2()'>Del</a>\n\
//                        </td>\n\
//                        </tr>");
////      i++; 
// });


$("#method").on('mouseover', 'option' , function(e) {
    
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

});

           
            var ajax;
            
            var subjectValue = $('#subject').select("selected").val();
            var objectiveValue = $('#objective').select("selected").val();
            var contentValue = $('#content').select("selected").val();
            var editValue = $('#method').select("selected").val();
//            function delstep(){
//                 var table = document.getElementById("tab_logic");
//       var rowCount = table.rows.length;
//         if(rowCount>1)
//         {
//      $("#tab_logic tr:eq("+(rowCount-1)+")").html('');
//        }
//            }
//            function delstep2(){
//                 var table = document.getElementById("tab_logic2");
//       var rowCount = table.rows.length;
//         if(rowCount>1)
//         {
//             $("#tab_logic2 tr:eq("+(rowCount-1)+")").html('');
//        }
//            }
//            function stepsEdit()
//            {
//                
//                $('#editsteps').modal('show');
//                
//                if($('#steps').children().length !== 0)
//                {
//                    $('#tab_logic2').empty();
//                    $.each(steps, function(i, item){
//                        //var paso = $('#step'+item.name).text();
////                        $('#country'+i).val(paso);
//                        $('#tab_logic2').append("<tr>\n\
//                        <td class='text-center' style='width: 10%; vertical-align: middle;'>"+(i+1) +"</td>\n\
//                        <td><input id='"+item.id+"' name='"+item.name+"' value='"+item.name+"' type='text' placeholder='Pass' class='form-control input-md'></td>\n\
//<td><button id='up_row' class='pull-right btn btn-default' onclick='upstep2()'><span class='glyphicon glyphicon-chevron-up'></span></button></td>\n\
//<td><button id='down_row' class='pull-right btn btn-default' onclick='upstep2()'><span class='glyphicon glyphicon-chevron-down'></span></button></td><td><button id='delete_row' class='pull-right btn btn-default' onclick='delstep2()'><span class='glyphicon glyphicon-remove'></span></button></td></tr>");
//                    });
//                }
//                else
//                {
//                    alert('No tiene pasos');
//                }
//            }
//            function stepsAdd()
//            {
//                
//                $('#addsteps').modal('show');
//            }
            
            function funcionCallBackSubject()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        document.getElementById("subject").innerHTML = ajax.responseText;
                        //Activamos el select de subject
                        var levelValue = $('#level').select("selected").val();
                        if( levelValue !== "?"){
                          $('#subject').attr("disabled", false);
                       }else{
                          $('#subject').attr("disabled", true);
                       };
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
                $('#steps').empty();
                steps = [];
                ajax.onreadystatechange = funcionCallBackObjective;
                var seleccion2 = document.getElementById("subject").value;
                ajax.open("GET", "objectivelistSubject.htm?seleccion2=" + seleccion2, true);

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
                    $('#editDescriptionObjective').focus();
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
                if(objective.nooflessons !== 'NaN'){
                    $('#nooflessons').text("This objective is currently linked to "+objective.nooflessonsplanned+" lessons");
                };
                $('#steps').children().remove();
                steps = objective.steps;
                $.each(steps, function(i, item) {
    //                var id = item.id;
                    $('#steps').append("<li id="+item.id+">"+item.name +"</li>");
                });
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
                //Al seleccionar un objective desactivamos el boton edit content
                if( contentValue !== null, contentValue !== ""){
                    //$('#addContent').attr("disabled", true);
                    $('#editContent').attr("disabled", true);
                };
                //Al seleccionar un objective desactivamos el boton del content
                if( contentValue !== null, contentValue !== ""){
                    //$('#addContent').attr("disabled", true);
                    $('#delContent').attr("disabled", true);
                };
                //Añadimos los content del objective
                $('#content').empty();
                $.each(content, function(i, item) { 
                    $('#content').append('<option value ="'+content[i].id+'" title ="'+content[i].description+'">' + content[i].name + '</option>');
                });
	}
}
            function comboSelectionContent()
            {
                 
             }
                

            
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
                            }else{
                                $('#buttomModalObjective').click();
                                $('#modal-objectiveLinkLessons').replaceWith('<div class="col-xs-12 text-center"><h3>'+ data +'</h3></div>');
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
            }
            function deleteContent()
            {
                var seleccion = document.getElementById("content").value;
                 $.ajax({
                    type: 'POST',
                        url: 'delContent.htm?id='+seleccion,
                        data: seleccion,
                        dataType: 'text' ,           
                     
                        success: function(data) {                          
                            if(data==='success')  {
                                $('#content option:selected').remove();
                            }else{
                                $('#buttomModalContent').click();
                                $('#modal-contentLinkLessons').replaceWith('<div class="col-xs-12 text-center"><h3>'+ data +'</h3></div>');
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
        var name = document.getElementById("namenewobjective").value;
        var description = document.getElementById("descriptionnewobjective").value;
        var subjectid = document.getElementById("subject").value;
        var myObj = {};
                myObj["name"] = name;
                myObj["description"] = description;
                var id = [subjectid]; 
                myObj["id"] = id;
                myObj["steps"] = steps;
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'addObjective.htm',
                        data: json,
                         datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {                          
                            var json = JSON.parse(data);                               
                        $('#objective').append('<option value = "'+json.id[0]+'" >' + json.name + '</option>');
                        $('#formAddobjetive').addClass("hidden");               
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
                myObj["id"] = [seleccion];
                 myObj["steps"] =steps;
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'editObjective.htm?sid='+subjectid,
                        data: json,
                       datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {
                            $('#objective').empty();
                            var json = JSON.parse(data);                            
                            $.each(json, function(i, item) { 
                            $('#objective').append('<option value = "'+json[i].id[0]+'" >' + json[i].name + '</option>');
                            });
                           $('#formEditobjetive').addClass("hidden");
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
                }
            function saveeditContent()
            {

        var seleccion = document.getElementById("content").value;
        var name = document.getElementById("editNameContent").value;
        var description = document.getElementById("editCommentsContent").value;
        var objid = document.getElementById("objective").value;
        var myObj = {};
                myObj["name"] = name;
                myObj["description"] = description;
                myObj["id"] = [seleccion];
          //      myObj["objid"] = objid;
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'editContent.htm?oid='+objid,
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {
                            $('#content').empty();
                            var json = JSON.parse(data);                            
                            $.each(json, function(i, item) { 
                            $('#content').append('<option value = "'+json[i].id[0]+'" title ="'+json[i].description+'">' + json[i].name + '</option>');
                            $('#formEditcontent').addClass("hidden");
                        });
                           
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
                } 
            function saveaddContent()
            {

     //   var seleccion = document.getElementById("objective").value;
        var name = document.getElementById("namenewcontent").value;
        var description = document.getElementById("commentsnewcontent").value;
        var objid = document.getElementById("objective").value;
        var myObj = {};
                myObj["name"] = name;
                myObj["description"] = description;
    //            myObj["id"] = seleccion;
                myObj["id"] = [objid];
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'addContent.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {                          
                            var json = JSON.parse(data);                               
                            $('#content').append('<option value = "'+json.id[0]+'" title ="'+json.description+'" >' + json.name + '</option>');
                            $('#formAddcontent').addClass("hidden");           
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
                }
            function deleteMethod()
            {
                var seleccion = document.getElementById("method").value;
                 $.ajax({
                    type: 'POST',
                        url: 'delMethod.htm?id='+seleccion,
                        data: seleccion,
                        dataType: 'text' ,           
                     
                        success: function(data) {                          
                            if(data==='success')  {
                                $('#method option:selected').remove();
           //         $('#objective').remove('option:selected');
                            }else{
                                $('#buttomModalMethod').click();
                                $('#modal-methodLinkLessons').replaceWith('<div class="col-xs-12 text-center"><h3>'+ data +'</h3></div>');
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
            }
            function saveaddMethod()
            {

     //   var seleccion = document.getElementById("objective").value;
        var name = document.getElementById("namenewmethod").value;
        var description = document.getElementById("commentsnewmethod").value;
        var myObj = {};
                myObj["name"] = name;
                myObj["description"] = description;
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'addMethod.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",
                        success: function(data) {                          
                            var json = JSON.parse(data);                               
                        $('#method').append('<option value = "'+json.id[0]+'" >' + json.name + '</option>');
                        $('#formAddmethod').addClass("hidden");               
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
                }
                function paintsteps()
                {
                    $('#steps').empty();
                    steps=[];
                    var table = document.getElementById("tab_logic2");
                    for (var i = 0, row; row = table.rows[i]; i++) {
  $('#steps').append("<li id="+row.cells[0]+">"+ row.cells[1].firstElementChild.value+"</li>");
//  id = [];
//  id[0]=row.cells[0]
  steps.push( {'id':row.cells[1].firstElementChild.id,'name':row.cells[1].firstElementChild.value ,'order':i });
   }  
                }
                 function paintnewsteps()
                {
                    $('#newsteps').empty();
                    steps=[];
                    var table = document.getElementById("tab_logic");
                    for (var i = 0, row; row = table.rows[i]; i++) {
  $('#newsteps').append("<li id="+row.cells[0]+">"+ row.cells[1].firstElementChild.value+"</li>");
//  id = [];
//  id[0]=row.cells[0]
  steps.push( {'id':row.cells[1].firstElementChild.id,'name':row.cells[1].firstElementChild.value ,'order':i });
   }  
                }
            function saveeditMethod()
            {

        var seleccion = document.getElementById("method").value;
        var name = document.getElementById("editNameMethod").value;
        var description = document.getElementById("editCommentsMethod").value;
        var myObj = {};
                myObj["name"] = name;
                myObj["description"] = description;
                myObj["id"] = [seleccion];
                var json = JSON.stringify(myObj);
            $.ajax({
                    type: 'POST',
                        url: 'editMethod.htm',
                        data: json,
                       datatype:"json",
                        contentType: "application/json",         
                     
                        success: function(data) {
                            $('#method').empty();
                            var json = JSON.parse(data);                            
                            $.each(json, function(i, item) { 
                            $('#method').append('<option value="'+json[i].id[0]+'" data-title="' + json[i].name + '" data-content="'+json[i].description+'">' + json[i].name + '</option>');
                            $('#formEditmethod').addClass("hidden");
                        });
                           
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });    
                }
            $(function () {
                $('#addObjective').click(function () {
                    $('#formAddobjetive').removeClass("hidden");
                    $('#formAddcontent').addClass("hidden");
                    $('#formEditcontent').addClass("hidden");
                    $('#namenewobjective').val('');
                    $('#descriptionnewobjective').val('');
                    $('#namenewobjective').focus();
                    //document.formpepi.select();
                    $('#objectiveSelectedForAdd').text($('#subject option:selected').text());
                });
                
                $('#editObjective').click(function () {
                    $('#formAddobjetive').addClass("hidden");
                    $('#formEditobjetive').removeClass("hidden");
                    $('#formAddcontent').addClass("hidden");
                    $('#formEditcontent').addClass("hidden");
                    $('#editDescriptionObjective').focus();
                    $('#objectiveSelectedForEdit').text($('#subject option:selected').text());
                });
                $('#content').click(function () {
                    $('#formAddobjetive').addClass("hidden");
                    $('#formEditobjetive').addClass("hidden");
                    $('#formAddcontent').addClass("hidden");
                    $('#formEditcontent').addClass("hidden");;
                    $('#editContent').attr("disabled", false);
                    $('#delContent').attr("disabled", false);
                    
                    //Al seleccionar un objective desactivamos el boton add
                    if( contentValue !== null, contentValue !== ""){
                       $('#addContent').attr("disabled", true);
                    };
                });
                $('#delContent').click(function () {
                    $('#formAddobjetive').addClass("hidden");
                    $('#formEditobjetive').addClass("hidden");
                    $('#formAddcontent').addClass("hidden");
                    $('#formEditcontent').addClass("hidden");
                    //$('#addContent').attr("disabled", false);
                    $('#editContent').attr("disabled", false);
                    $('#delContent').attr("disabled", false);
                    
                    //Al seleccionar un objective desactivamos el boton add
                    if( contentValue !== null, contentValue !== ""){
                       $('#addContent').attr("disabled", true);
                    };
                });
                $('#addContent').click(function () {
                    $('#formAddcontent').removeClass("hidden");
                    $('#formEditobjetive').addClass("hidden");
                    $('#namenewcontent').val('');
                    $('#commentsnewcontent').val('');
                    $('#namenewcontent').focus();
                    $('#contentSelectedForAdd').text($('#objective option:selected').text());
                });
                $('#editContent').click(function () {
                    $('#formAddcontent').addClass("hidden");
                    $('#formEditcontent').removeClass("hidden");
                    //Añadimos el nombre del content para editarlo
                    $('#editNameContent').val($('#content option:selected').text());
                    $('#editCommentsContent').val($('#content option:selected').attr("title"));
                    //Añadimos el nombre del objective para saber a que objective pertenece el content que estamos editando
                    $('#editCommentsContent').focus();
                    $('#contentSelectedForEdit').text($('#objective option:selected').text());
                });
                $('#method').click(function () {
                    $('#formAddmethod').addClass("hidden");
                    $('#formEditmethod').addClass("hidden");
                    $('#formAddmethod').addClass("hidden");
                    $('#addMethod').attr("disabled", false);
                    $('#editMethod').attr("disabled", false);
                    $('#delMethod').attr("disabled", false);
                    
                    //Al seleccionar un objective desactivamos el boton add
//                    if( editValue !== null, editValue !== ""){
//                       $('#editMethod').attr("disabled", true);
//                    };
                });
                $('#addMethod').click(function () {
                    $('#formAddmethod').removeClass("hidden");
                    $('#formEditmethod').addClass("hidden");
                    $('#namenewmethod').focus();
                });
                $('#editMethod').click(function () {
                    $('#formAddmethod').addClass("hidden");
                    $('#formEditmethod').removeClass("hidden");
                    //Añadimos el nombre del method para editarlo
                    $('#editNameMethod').val($('#method option:selected').text());
                    //Añadimos el comentario del method para editarlo
                    $('#editCommentsMethod').val($('#method option:selected').attr('data-content'));
                    //Añadimos el nombre del method para saber que estamos editando
                    $('#editNameMethod').focus();
                    $('#methodSelectedForEdit').text($('#method option:selected').text());
                });
                $('#delMethod').click(function () {
                    $('#methodSelectForDelete').text($('#method option:selected').text());
                });
                
                 $('#level').click(function () {
                    $('#objective').empty();
                    $('#namenewobjective').val('');
                    $('#descriptionnewobjective').val('');
                    $('#content').empty();
                });
            });
        </script>
        <style>
            textarea 
            {
                resize: none;
            }
             .popover{
                width: 500px;
            }
        </style>
    </head>
    <body>


        <div class="container">
            <h1 class="text-center">Create Scheme of Work</h1>


            <form:form id="formSettings" method ="post" action="createsetting.htm?select=createsetting" >

                <fieldset>
                    <legend>Select an item to edit</legend>

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
                            <select class="form-control" disabled="true" name="TXTsubject" id="subject" multiple size="10" onclick="comboSelectionSubject()">
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject.id[0]}" >${subject.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label">Objective</label>
                            <select class="form-control" disabled="true" name="TXTobjective" id="objective" multiple size="10" onclick="comboSelectionObjective()">
                                <c:forEach var="objective" items="${objectives}">
                                    <option value="${objective.id[0]}" title ="${objective.description}" >${objective.name}</option>
                                </c:forEach>
                            </select>
                            <div class="col-xs-12" style="padding-top: 10px;">
                                <div class="col-xs-4 text-center">
                                    <input type="button" class="btn btn-success" disabled data-toggle="tooltip" data-placement="bottom" value="add" id="addObjective"/>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" class="btn btn-warning" disabled data-toggle="tooltip" data-placement="bottom"  value="edit" id="editObjective"/>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" class="btn btn-danger" disabled data-toggle="modal" data-target="#confirmedDeleteObjective" data-placement="bottom" value="del" id="delObjective"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-3 center-block">
                            <label class="control-label">Content</label>
                            <select class="form-control" disabled="true" name="TXTcontent" id="content" multiple size="10" onclick="comboSelectionContent()">
                                <c:forEach var="content" items="${contents}">
                                    <option value="${content.id[0]}" title="${content.description}" >${content.name}</option>
                                </c:forEach>
                            </select>
                            <div class="col-xs-12" style="padding-top: 10px;">
                                <div class="col-xs-4 text-center">
                                    <input type="button" class="btn btn-success" disabled data-toggle="tooltip" data-placement="bottom" value="add" id="addContent">
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" class="btn btn-warning" disabled data-toggle="tooltip" data-placement="bottom" value="edit" id="editContent">
                                </div>
                                <div class="col-xs-4 text-center">
                                    <input type="button" class="btn btn-danger" disabled data-toggle="modal" data-target="#confirmedDeleteContent" data-placement="bottom" value="del" id="delContent" >
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
                            <input type="text" class="form-control" name="TXTnamenewobjective" id="namenewobjective"  placeholder="Name" >
                        </div>
                        <div class="col-xs-7 center-block form-group">
                            <label class="control-label">Description</label>
                            <textarea type="text" class="form-control" name="TXTnamenewobjective" id="descriptionnewobjective"  placeholder="Description"></textarea>
                        </div>
                     <div class="col-xs-2 center-block form-group">
                            <label class="control-label">Steps</label>
                            <ol id="newsteps">   
                            </ol>
                        </div>
                        <div class="col-xs-2 text-center form-group paddingLabel">
                            <div class="col-xs-8 center-block form-group paddingLabel">
                                <input type="button" name="addObjectiveSteps" value="Add Steps" class="btn btn-success" id="addObjectiveSteps" data-target=".bs-example-modal-lg" onclick="stepsAdd()"/>
                            </div>
                            <div class="col-xs-4 center-block form-group paddingLabel">
                                <input type="button" name="AddObjective" value="save" class="btn btn-success" id="AddObjective" data-target=".bs-example-modal-lg" onclick="saveaddObjective()"/> 
                            </div>
                        </div>
                </fieldset>
                <fieldset class="hidden" id="formEditobjetive">
                
                    <legend>Edit objective in <span id="objectiveSelectedForEdit"></span></legend>
                    <%--Edit objective--%>
                        <div class="col-xs-3 center-block form-group" id="addObjective">
                            <label class="control-label">Edit objective</label>
                            <input type="text" class="form-control" name="TXTeditNameObjective" id="editNameObjective"  placeholder="Name">
                        </div>
                        <div class="col-xs-4 center-block form-group">
                            <label class="control-label">Description</label>
                            <textarea type="text" class="form-control" rows="5" name="TXTeditDescriptionObjective" id="editDescriptionObjective"  placeholder="Description"></textarea>
                        </div>
                        <div class="col-xs-2 center-block form-group">
                            <label class="control-label">Steps</label>
                            <ol id="steps">   
                            </ol>
                        </div>
                        <div class="col-xs-2 center-block form-group paddingLabel">
                            <div class="col-xs-8 center-block form-group paddingLabel">
                                <input type="button" name="EditObjectiveSteps" value="Edit Steps" class="btn btn-success" id="editObjectiveSteps" data-target=".bs-example-modal-lg" onclick="stepsEdit()"/>
                            </div>
                            <div class="col-xs-4 center-block form-group paddingLabel">
                                <input type="button" name="AddObjective" value="Save" class="btn btn-success" id="savedEditObjective" data-target=".bs-example-modal-lg" onclick="saveeditObjective()"/>
                            </div>
                        </div>
                     <div class="col-xs-3 center-block form-group" id="addObjective">
                            <span id="nooflessons"></span>
                     
                        </div>
                </fieldset>
                <fieldset class="hidden" id="formAddcontent">
                    <legend>Add content to <span id="contentSelectedForAdd"></span></legend>  
                    <div class="col-xs-12" style="margin-top: 20px;">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Name new content</label>
                            <input type="text" class="form-control" name="TXTnamenewcontent" id="namenewcontent"  placeholder="Name new content">
                        </div>
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Description</label>
                            <input type="text" class="form-control" name="TXTnamenewcontent" id="commentsnewcontent"  placeholder="Description">
                        </div>
                        <div class="col-xs-3 center-block form-group paddingLabel">
                            <input type="button" name="AddContent" value="save" class="btn btn-success" id="AddContent" data-target=".bs-example-modal-lg" onclick="saveaddContent()"/>
                                
                            
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
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Description</label>
                            <input type="text" class="form-control" name="TXTnameeditcontent" id="editCommentsContent"  placeholder="Description">
                        </div>
                        <div class="col-xs-3 center-block form-group paddingLabel">
                            <input type="button" name="EditContent" value="save" class="btn btn-success" id="EditContent" data-target=".bs-example-modal-lg" onclick="saveeditContent()"/> 
                        </div>
                    </div>
                </fieldset>        
            </form:form>
                <fieldset>
                    <legend>Select a method to edit</legend>
                    <div class="col-xs-12">
                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Method</label>
                            <select class="form-control methodSelect" size="2" name="method" id="method">
                                <c:forEach var="method" items="${methods}">
                                    <option value="${method.id[0]}"  data-title="${method.name}" data-content="${method.description}">${method.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-9 form-group paddingLabel">
                            <div class="col-xs-2 text-center ">
                                <input type="button" class="btn btn-success" data-toggle="tooltip" data-placement="bottom" value="add" id="addMethod">
                            </div>
                            <div class="col-xs-2 text-center">
                                <input type="button" class="btn btn-warning" disabled data-toggle="tooltip" data-placement="bottom" value="edit" id="editMethod">
                            </div>
                            <div class="col-xs-2 text-center">
                                <input type="button" class="btn btn-danger" disabled data-toggle="modal" data-target="#confirmedDeleteMethod" data-placement="bottom" value="del" id="delMethod" >
                            </div>
                        </div>
                    </div>
                    
                </fieldset>
                <fieldset class="hidden" id="formAddmethod">
                    <legend>Add method</legend>  
                    <div class="col-xs-12">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Name new method</label>
                            <input type="text" class="form-control" name="TXTnamenewmethod" id="namenewmethod"  placeholder="Name new method">
                        </div>
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Description</label>
                            <input type="text" class="form-control" name="TXTnamenewmethod" id="commentsnewmethod"  placeholder="Description">
                        </div>
                        <div class="col-xs-3 center-block form-group paddingLabel">
                            <input type="button" name="Addmethod" value="save" class="btn btn-success" id="Addmethod" data-target=".bs-example-modal-lg" onclick="saveaddMethod()"/>
                        </div>
                    </div>
                </fieldset>
                <fieldset class="hidden" id="formEditmethod">
                    <legend>Edit method in <span id="methodSelectedForEdit"></span></legend>  
                    <div class="col-xs-12">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Edit method</label>
                            <input type="text" class="form-control" name="TXTnameeditethod" id="editNameMethod"  placeholder="Name new method">
                        </div>
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Description</label>
                            <input type="text" class="form-control" name="TXTcommenteditmethod" id="editCommentsMethod"  placeholder="Description">
                        </div>
                        <div class="col-xs-3 center-block form-group paddingLabel">
                            <input type="button" name="EditMethod" value="save" class="btn btn-success" id="EditMethod" data-target=".bs-example-modal-lg" onclick="saveeditMethod()"/> 
                        </div>
                    </div>
                </fieldset>
            <fieldset>
                <div class="col-xs-12 text-center">
                    <a class="btn btn-default" href="<c:url value="/sowdisplay/start.htm"/>">View Scheme of Work</a>  
                </div>
            </fieldset>
            
        </div>
               
        <div class="col-xs-12 text-center">
            
                     
        </div>
        <div id="modalConfirmeDeleteObjective">
            <!-- Modal -->
            <div class="modal fade" id="confirmedDeleteObjective" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Are you sure you want to delete this objective?</h4>
                        </div>
                        <div class="modal-body">
                            <button type="button" class="btn btn-default" data-dismiss="modal" id="" onclick="deleteObjective()">Yes</button>
                            <button type="button" class="btn btn-primary" data-dismiss="modal" >No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="modalConfirmeDeleteContent">
            <!-- Modal -->
            <div class="modal fade" id="confirmedDeleteContent" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Are you sure you want to delete this content?</h4>
                        </div>
                        <div class="modal-body">
                            <button type="button" class="btn btn-default" data-dismiss="modal" id="" onclick="deleteContent()">Yes</button>
                            <button type="button" class="btn btn-primary" data-dismiss="modal" >No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="modalConfirmeDeleteMethod">
            <!-- Modal -->
            <div class="modal fade" id="confirmedDeleteMethod" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Are you sure you want to delete this method?</h4>
                            <h1 id="methodSelectForDelete"></h1>
                        </div>
                        <div class="modal-body">
                            <button type="button" class="btn btn-default" data-dismiss="modal" id="" onclick="deleteMethod()">Yes</button>
                            <button type="button" class="btn btn-primary" data-dismiss="modal" >No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div> 
        <div id="modalObjective">
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-primary btn-lg hidden" data-toggle="modal" data-target="#myModal" id="buttomModalObjective">
                Launch demo modal
            </button>

            <!-- Modal -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Delete Objective</h4>
                        </div>
                        <div class="modal-body" id="modal-objectiveLinkLessons">
                            ...
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="modalContent">
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-primary btn-lg hidden" data-toggle="modal" data-target="#myModal" id="buttomModalContent">
                Launch demo modal
            </button>

            <!-- Modal -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Modal Content</h4>
                        </div>
                        <div class="modal-body" id="modal-contentLinkLessons">
                            ...
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="modalMethod">
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-primary btn-lg hidden" data-toggle="modal" data-target="#myModal" id="buttomModalMethod">
                Launch demo modal
            </button>

            <!-- Modal -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Modal Method</h4>
                        </div>
                        <div class="modal-body" id="modal-methodLinkLessons">
                            ...
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
         <script>
                    
                    
        $(document).ready(function(){


  var i=1;
  
 $("#add_row").click(function(){
         
//      $('#addr'+i).html("<td class='text-center' style='width: 20%; vertical-align: middle;'>"+ (i+1) +"</td><td><input id='country"+i+"' name='country"+i+"' type='text' placeholder='Step'  class='form-control input-md'></td>");
//      var NameStep = $('input[id='+(i-1)+']').val();
      var table = document.getElementById("tab_logic");
        var rowCount = table.rows.length;
//      $('#steps').append("<li id="+(rowCount+1)+">"+ NameStep +"</li>");
//del step ,if the id is zero will not add to the del array or the updated array, always will be only in the new array,because it was a step that was added and removed while editing
      $('#tab_logic').append("<tr>\n\
                        <td class='text-center' style='width:10%;'>"+(rowCount+1)+"</td>\n\
                        <td style='width:75%;'><input id='0' name='' value='' type='text' placeholder='Step' class='form-control input-md'></td>\n\
<td class='text-center' style='width:5%;'><button id='up_row' class='btn btn-default subir'><span class='glyphicon glyphicon-chevron-up'></span></button></td>\n\
<td class='text-center' style='width:5%;'><button id='down_row' class='btn btn-default bajar'><span class='glyphicon glyphicon-chevron-down'></span></button></td>\n\
<td class='text-center' style='width:5%;'><button id='delete_row' class='btn btn-default eliminar' ><span class='glyphicon glyphicon-remove'></span></button></td></tr>");
//      i++; 
 });
 $("#add_row2").click(function(){
         
//      $('#addr'+i).html("<td class='text-center' style='width: 20%; vertical-align: middle;'>"+ (i+1) +"</td><td><input id='country"+i+"' name='country"+i+"' type='text' placeholder='Step'  class='form-control input-md'></td>");
//      var NameStep = $('input[id='+(i-1)+']').val();
      var table = document.getElementById("tab_logic2");
        var rowCount = table.rows.length;
//      $('#steps').append("<li id="+(rowCount+1)+">"+ NameStep +"</li>");
//del step ,if the id is zero will not add to the del array or the updated array, always will be only in the new array,because it was a step that was added and removed while editing
      $('#tab_logic2').append("<tr>\n\
                        <td class='text-center' style='width:10%;'>"+(rowCount+1)+"</td>\n\
                        <td style='width:75%;'><input id='0' name='' value='' type='text' placeholder='Step' class='form-control input-md'></td>\n\
<td class='text-center' style='width:5%;'><button id='up_row' class='btn btn-default subir'><span class='glyphicon glyphicon-chevron-up'></span></button></td>\n\
<td class='text-center' style='width:5%;'><button id='down_row' class='btn btn-default bajar'><span class='glyphicon glyphicon-chevron-down'></span></button></td>\n\
<td class='text-center' style='width:5%;'><button id='delete_row' class='btn btn-default eliminar' ><span class='glyphicon glyphicon-remove'></span></button></td></tr>");
//      i++; 
 });



});
                function delstep(){
                 var table = document.getElementById("tab_logic");
       var rowCount = table.rows.length;
         if(rowCount>1)
         {
      $("#tab_logic tr:eq("+(rowCount-1)+")").html('');
        }
            }
//            function delstep2(){
//                 var row = $(this).parent().parent();
//       var rowCount = table.rows.length;
//         if(rowCount>1)
//         {
//             $("#tab_logic2 tr:eq("+(rowCount-1)+")").html('');
//        }
//            }
            function stepsEdit()
            {
                
                $('#editsteps').modal('show');
                
                if($('#steps').children().length !== 0)
                {
                    $('#tab_logic2').empty();
                    $.each(steps, function(i, item){
                        //var paso = $('#step'+item.name).text();
//                        $('#country'+i).val(paso);
                        $('#tab_logic2').append("<tr>\n\
                        <td class='text-center' style='width:10%;'>"+(i+1) +"</td>\n\
                        <td style='width:75%;'><input id='"+item.id+"' name='"+item.name+"' value='"+item.name+"' type='text' placeholder='Step' class='form-control input-md'></td>\n\
<td class='text-center' style='width:5%;'><button id='up_row' class='btn btn-default subir'><span class='glyphicon glyphicon-chevron-up'></span></button></td>\n\
<td class='text-center' style='width:5%;'><button id='down_row' class='btn btn-default bajar'><span class='glyphicon glyphicon-chevron-down'></span></button></td>\n\
<td class='text-center' style='width:5%;'><button id='delete_row' class='btn btn-default eliminar' ><span class='glyphicon glyphicon-remove'></span></button></td></tr>");
                    });
                }
                else
                {
//                    alert('No tiene pasos');
                }
            }
            function stepsAdd()
            {
                
                $('#addsteps').modal('show');
            }
            
            



    var fNuevo = function (data) {
        $("table").append(filaNueva);
    };
    var fMover = function () {
        var $tr = $(this).parents("tr:first");
        var $td = $(this).parents().parents().children("td:first");
        if ($(this).is(".subir")) {
            $tr.insertBefore($tr.prev());
            $('td:first-child').each(function() {
                $(this).html($(this).parent().index()+1);
            });
        } else {
            $tr.insertAfter($tr.next());
            $('td:first-child').each(function() {
                $(this).html($(this).parent().index()+1);
            });
        }
    };
    var fEliminar = function () {
        var $tr = $(this).parents("tr:first");
        $tr.remove();
         $('td:first-child').each(function() {
                $(this).html($(this).parent().index()+1);
            });
    };


    $(document).ready(function () {

        $(".nuevo").on("click", fNuevo);

        $("table")
            .on("click", ".subir", fMover)
            .on("click", ".bajar", fMover)
            .on("click", ".eliminar", fEliminar)
    });

                </script>        
                
        <div id="addsteps" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">

            <!-- Modal content-->
            <div class="modal-content">
              <div class="modal-header modal-header-details">
                  <button type="button" class="close" data-dismiss="modal" onclick="paintnewsteps()">Done</button>
                <h4 id="nameLessonDetails" class="modal-title"> Add the work that needs to be accomplished</h4>
              </div>
              <div class="modal-body">
                  <div class="container-fluid">
                  <div class="col-xs-12">
              
                     <table class="table table-bordered table-hover" id="tab_logic">
                        <tbody>
                        </tbody>
                    </table>
                  </div>
                    <div class="col-xs-12">
                    <a id="add_row" class="btn btn-default pull-left">Add Row</a>
                    </div>
               </div>
              </div>

                </div>
            </div>
        </div>
        <div id="editsteps" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">

            <!-- Modal content-->
            <div class="modal-content">
              <div class="modal-header modal-header-details">
                  <button type="button" class="close" data-dismiss="modal" onclick="paintsteps()">Done</button>
                <h4 id="nameLessonDetails" class="modal-title"> Add the work that needs to be accomplished</h4>
              </div>
              <div class="modal-body">
                  <div class="container-fluid">
                      
                  <div class="col-xs-12">
                      <table class="table table-bordered" id="tab_logic2">
                        <tbody>
<!--                        <tr id='addr0'>
                                <td class="text-center" style="width: 20%; vertical-align: middle;">
                                1
                                </td>
                                <td>
                                    <input type="text" id="country0" name='country0' placeholder='pass' class="form-control"/>
                                </td>
                            </tr>
                            <tr id='addr1'>
                                
                            </tr>-->
                        </tbody>
                    </table>
                  </div>
                    <div class="col-xs-12">
                    <a id="add_row2" class="btn btn-default pull-left">Add Row</a>
                    </div>
               </div>
              </div>
<!--    <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>-->
                </div>
            </div>
        </div>
    <%---    <%= request.getParameter("message")%>---%>
     
    </body>
</html>
