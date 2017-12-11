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
        <title>Presentation Resources</title>
        <script>

 $(document).ready(function(){
     
     // Instantiate the Bootstrap carousel
        $('.multi-item-carousel').carousel({
          interval: false
        });

        // for every slide in carousel, copy the next slide's item in the slide.
        // Do the same for the next, next item.
        $('.multi-item-carousel .item').each(function(){
          var next = $(this).next();
          if (!next.length) {
            next = $(this).siblings(':first');
          }
          next.children(':first-child').clone().appendTo($(this));

          if (next.next().length>0) {
            next.next().children(':first-child').clone().appendTo($(this));
          } else {
                $(this).siblings(':first').children(':first-child').clone().appendTo($(this));
          }
        });
        
       $('#txtUrl').html("This page's URL is"+window.location.href);
       
       var userLang = navigator.language || navigator.userLanguage;
       var myDate = new Date();
         //Muestra calendario
         //VARIABLE CUANDO HEMOS CREADO UNA LESSONS CORRECTAMENTE
         var lessoncreate = '<%= request.getParameter("message") %>';
    
     if (lessoncreate === 'Lesson created' ){
     $('#myModal').modal({
        show: 'false'
    });
    }
            $( "#showPropiertys" ).click(function() {
                    $("#contenedorPropiertys").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
            });
            $( "#showDate" ).click(function() {
                    $("#contenedorDate").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
            });
            $( "#showDetails" ).click(function() {
                    $("#contenedorDetails").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
            });
            $( "#showStudents" ).click(function() {
                    $("#contenedorStudents").toggleClass('in');
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
            });

            $( "#ideaCheck" ).change(function() {
                if($(this).is(":checked")) {
                    $('#showDate').addClass("desactivada");
                    $('#showDate').off('click');
                    $("#contenedorDate").removeClass('in');
                    $('#showStudents').addClass("desactivada");
                    $('#showStudents').off('click');
                    $("#contenedorStudents").removeClass('in');
                    
                }else if($("#ideaCheck :not(:checked)")) 
                {
                    $('#showDate').removeClass("desactivada");
                    $('#showDate').on('click', function(){ $("#contenedorDate").toggleClass('in');} );
//                    $("#contenedorDate").removeClass('in');
                    $('#showStudents').removeClass("desactivada");
                    $('#showStudents').on('click', function(){ $("#contenedorDate").toggleClass('in');} );
                    $("#contenedorStudents").addClass('in');   
                    }
            });
            

            
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
        
        
    });            
        
        $().ready(function() 
	{ 
                  
		$('.pasar').click(function() {
                    !$('#origen option:selected').remove().appendTo('#destino');
                    var alumnosSelected = $('#destino').length;
                    var objectiveSelected = $('#objective').val();
                    if(alumnosSelected !== 0 && objectiveSelected !== 0 && objectiveSelected !== null && objectiveSelected !== ''){
                        $('#createOnClick').attr('disabled', false);
                    }
                    return;
                });  
		$('.quitar').click(function() {
                    !$('#destino option:selected').remove().appendTo('#origen');
                    var alumnosSelected = $('#destino').length;
                    var objectiveSelected = $('#objective').val();
                    if(alumnosSelected === 0 || ( objectiveSelected === 0 || objectiveSelected === null || objectiveSelected === '')){
                        $('#createOnClick').attr('disabled', true);
                    }
                    return;  
                });
		$('.pasartodos').click(function() {
                    $('#origen option').each(function() { $(this).remove().appendTo('#destino'); });
                    var objectiveSelected = $('#objective').val();
                    if( objectiveSelected === 0 || objectiveSelected === null || objectiveSelected === ''){
                        $('#createOnClick').attr('disabled', true);
                    }
                });
		$('.quitartodos').click(function() {
                    $('#destino option').each(function() { $(this).remove().appendTo('#origen'); });
                    $('#createOnClick').attr('disabled', true);
                });
	});
        
        var ajax;
        
    function funcionCallBackLevelStudent()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("origen").innerHTML= ajax.responseText;
                    }
                }
            }
            

    function comboSelectionLevelStudent()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        ajax.onreadystatechange=funcionCallBackLevelStudent;
        var seleccion = document.getElementById("levelStudent").value;
        var alumnos = document.getElementById("destino").innerHTML;
        ajax.open("POST","studentlistLevel.htm?seleccion="+seleccion,true);
        ajax.send("");
    }
    function downloadFile(id){
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        var myObj = {};
                myObj["id"] = id;
                var json = JSON.stringify(myObj);
        $.ajax({
                        type: 'POST',
                        url: 'downloadFile.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {                          
                          
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }
                            
                               
                    });
    }
    function deleteLink(id){
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        var idResource = id;
        var lessonName = $('#lessonsName').val();
        var myObj = {};
                myObj["name"] = lessonName;
                myObj["id"] = idResource;
                var json = JSON.stringify(myObj);
        $.ajax({
                    type: 'POST',
                        url: 'delResources.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {                          
                          //var j = JSON.parse(data);    
                           $('#divRecurso'+id).remove(); 
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }
                            
                               
                    });
        }
    function loadInfResource(id){
            if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        var idResource = id;
        
        var myObj = {};
                myObj["id"] = idResource;
                var json = JSON.stringify(myObj);
        $.ajax({
                    type: 'POST',
                        url: 'loadInfResource.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {                          
                          var j = JSON.parse(data);
                          var title = j.name;
                          var link = j.link;
                          var type = j.type;
                          var id  = j.id;
                         // $('#tableobjective tbody tr').find(':button.btn-xs[value="' + json.objectiveid + '"]').parent().parent().parent().siblings('td:eq(2)').text(currentTime);   
                         // $('#showModalComment').click();
                          $('#editnewLink').modal('show');
                          $('#editLinkName').val(title);
                          $('#editLinkComments').val(link);
                          $('#resourceId').val(id);
                          //$('#resourceId').val(id);
                          
                          if(type === "Link"){
                            $("#selectLinkTipo :selected").text("Link");
                          }else{
                            $("#selectLinkTipo :selected").text("Video");
                          }
                          $("#EditLink").prop('disabled', false);
                          $('editnewLink').click();
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });
    }
     function saveEditLink(){
          if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        var nameResource = $('#editLinkName').val();
        var linkResource = $('#editLinkComments').val();
        var type = $('#selectLinkTipo').val();
        var idResource =  $('#resourceId').val();
        
        var myObj = {};
                myObj["name"] = nameResource;
                myObj["link"] = linkResource;
                myObj["type"] = type;
                myObj["id"] = idResource;
                var json = JSON.stringify(myObj);
        $.ajax({
                    type: 'POST',
                        url: 'updateResources.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                  
                        //$("#contenedorPropiertys").load(" #contenedorPropiertys");
                        success: function(data) {                          
                         // var j = JSON.parse(data);
                          var id = idResource;
                           var claseTipo = "label-success";
                            if(type === "Video"){ claseTipo = "label-primary"}
                            
                            //MODIFICAR EL JSON PARA QUE DEVUELVA EL ID OBTENIDO
                            $('#listResources').append("<div id = 'divRecurso"+id+"' class='list-group col-xs-12'>\n\
                                                    <div class='col-xs-10 text-center'>\n\
                                                         <a href="+linkResource+" class='list-group-item link'>\n\
                                                             "+nameResource+"<span class='label "+claseTipo+"'>"+type+"</span>\n\
                                                 </a>\n\
                                            </div>\n\
                                         <div class='col-xs-1 text-center'><input type='button' class='btn btn-warning editResource'  onclick='loadInfResource("+id+")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editResource"+id+" '/></div>\n\
                                         <div class='col-xs-1 text-center'><input type='button' class='btn btn-warning '  onclick='deleteLink("+id+")' data-toggle='tooltip' data-placement='bottom' value='delete' id='deleteLink("+id+")'/></div></div>");   
                            $('#divRecurso'+id).remove();
                        },
                        
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }
                    }); 
                        

     }
     
     
      
     function saveEditMethod(){
        
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        var nameResource = $('#editNameMethod').val();
        var linkResource = $('#editCommentsMethod').val();
        var type = $('#selectTipo').val();
        var lessonId =  $('#lessonid').val();
        var id;
        var myObj = {};
                myObj["name"] = nameResource;
                myObj["link"] = linkResource;
                myObj["type"] = type;
                myObj["lesson_id"] = lessonId;
                var json = JSON.stringify(myObj);
        $.ajax({
                    type: 'POST',
                        url: 'addResources.htm',
                        data: json,
                        datatype:"json",
                        contentType: "application/json",           
                     
                        success: function(data) {   //NO ENTRA A SUCCESS                       
                          //var j = JSON.parse(data);
                          id = data; 
                           var claseTipo = "label-success";
                            if(type === "Video"){ claseTipo = "label-primary"}
                            //MODIFICAR EL JSON PARA QUE DEVUELVA EL ID OBTENIDO
                            $('#listResources').append("<div id = 'divRecurso"+id+"' class='list-group col-xs-12'>\n\
                                                    <div class='col-xs-10 text-center'>\n\
                                                         <a href="+linkResource+" class='list-group-item link'>\n\
                                                             "+nameResource+"<span class='label "+claseTipo+"'>"+type+"</span>\n\
                                                 </a>\n\
                                            </div>\n\
                                         <div class='col-xs-1 text-center'><input type='button' class='btn btn-warning editResource'  onclick='loadInfResource("+id+")' data-toggle='tooltip' data-placement='bottom' value='edit' id='editResource"+id+" '/></div>\n\
                                         <div class='col-xs-1 text-center'><input type='button' class='btn btn-warning '  onclick='deleteLink("+id+")' data-toggle='tooltip' data-placement='bottom' value='delete' id='deleteLink("+id+")'/></div></div>");   
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                                console.log(xhr.status);
                                   console.log(xhr.responseText);
                                   console.log(thrownError);
                               }

                    });
                    }
     function funcionCallBackSubject()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("subject").innerHTML= ajax.responseText;
                    }
                }
            }
    function funcionCallBackObjective()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("objective").innerHTML= ajax.responseText;
                    }
                }
            }
    function funcionCallBackLoadTemplateLessons()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("lessons").innerHTML= ajax.responseText;
                    }
                }
            }    

    function funcionCallBackIdeaLessons()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    
                   var json = JSON.parse(ajax.responseText);
                   var level = json.level;
                   var subject =  JSON.parse(json.subject).id;
                   
                   var objective =  JSON.parse(json.objective).id;
                   var method =  JSON.parse(json.method).id;
                   var content =  JSON.parse(json.content);
                   var subjects = JSON.parse(json.subjectslist);
                   var objectives = JSON.parse(json.objectiveslist);
                   var contents = JSON.parse(json.contentslist);
                   $('#subject').empty();
                     $.each(subjects, function(i, item) {
                        var test = subjects[i].id;
                        if( test === subject){
                             $('#subject').append('<option selected value= "'+subjects[i].id+'">' + subjects[i].name + '</option>');
                        }
                        else{
                         $('#subject').append('<option value= "'+subjects[i].id+'">' + subjects[i].name + '</option>');
                    }
                   });
                    }
                }
            }
            
    function funcionCallBackContent()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("content").innerHTML= ajax.responseText;
                    }
                }
            }

    function comboSelectionLevel()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

        $('#createOnClick').attr('disabled', true);
        ajax.onreadystatechange = funcionCallBackSubject;
        var seleccion1 = document.getElementById("level").value;
        ajax.open("POST","subjectlistLevel.htm?seleccion1="+seleccion1,true);
        
        ajax.send("");
       
    }
    function comboSelectionSubject()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

        
        ajax.onreadystatechange=funcionCallBackObjective;
        var seleccion2 = document.getElementById("subject").value;
        ajax.open("POST","objectivelistSubject.htm?seleccion2="+seleccion2,true);

        ajax.send("");
        
    }  
    function comboSelectionLoadTemplateLessons()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        
        ajax.onreadystatechange=funcionCallBackLoadTemplateLessons;
        var seleccionSubject = document.getElementById("subject").value;
        ajax.open("POST","namelistSubject.htm?seleccionTemplate="+seleccionSubject,true);
        ajax.send("");
    }
     function comboSelectionIdeaLessons()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        
        
        ajax.onreadystatechange=funcionCallBackIdeaLessons;
        var seleccionidea = document.getElementById("ideas").value;
        //ajax.open("POST","createlesson.htm?select=objectivelistSubject&seleccion2="+seleccionTemplate,true);
        ajax.open("POST","copyfromIdea.htm?seleccionidea="+seleccionidea,true);
        ajax.send("");
    }
     function comboSelectionObjective()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

        if(document.getElementById("objective").value === 0 || document.getElementById("objective").value === '' || document.getElementById("destino").length === 0 ){
            $('#createOnClick').attr('disabled', true);
        }else{
            $('#createOnClick').attr('disabled', false);
        }
        ajax.onreadystatechange=funcionCallBackContent;
        var seleccion3 = document.getElementById("objective").value;
        ajax.open("POST","contentlistObjective.htm?seleccion3="+seleccion3,true);
        ajax.send("");
    }
    $(function () {
        $('#addLink').click(function () {
            
                    $("#editNameMethod").val("");
                    $("#editCommentsMethod").val("");
                    $('#addnewLink').modal('show');
                });
                
        $('#addFile').click(function () {
            
                   $('#addnewFile').modal('show');
                });
        /*$('.editResource').click(function () {
                  $('#editnewLink').modal('show');
               });
        $('.editResource2').click(function () {
                
               });*/
            })
            
        </script>
        <style>
            
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

        </style>
    </head>
    <body>
        <input type="hidden" id="lessonid" name="lessonid" value = ${lessonid}>
        <input type="hidden" id="lessonsName" name="lessonsName" value = ${lessonsName}>
        <!--<div class="modal-body text-center">
        <H1><%= request.getParameter("message") %></H1>
        </div>-->
        
        <div class="container">
        <h1 class="text-center">Presentation Resources</h1>
            <fieldset>
                <legend id="showPropiertys">
                    External links and videos
                    <span class="col-xs-12 text-right glyphicon glyphicon-triangle-bottom">
                    </span>
                </legend>
                <div class="form-group collapse" id="contenedorPropiertys">
                    <div  id = "listResources" class="list-group col-xs-12 link">
                        <c:forEach var="item" items="${others}">
                            <c:choose>
                               
                               <c:when test="${item.type =='Video'}">
                                   <div id = "divRecurso${item.id}" class="list-group col-xs-12">
                                   <div class="col-xs-10 text-center"><a href="${item.link}"  data-id="${item.id}" class="list-group-item link" >${item.name}<span class="label label-primary">${item.type}</span> </a></div> 
                                   <div class="col-xs-1 text-center"><input type="button" class="btn btn-warning editResource"  onclick="loadInfResource(${item.id})" data-toggle="tooltip" data-placement="bottom" value="edit" id="editResource(${item.id})"/></div>  
                                   <div class="col-xs-1 text-center"><input type="button" class="btn btn-warning"  onclick="deleteLink(${item.id})" data-toggle="tooltip" data-placement="bottom" value="delete" id="deleteLink(${item.id})"/></div>

                                   </div>
                                </c:when>
                                
                                <c:otherwise> 
                                   <div id = "divRecurso${item.id}" class="list-group col-xs-12">
                                   <div class="col-xs-10 text-center"> <a href="${item.link}" data-id="${item.id}"  class="list-group-item link">${item.name}<span class="label label-success">${item.type}</span></a></div>
                                   <div class="col-xs-1 text-center"><input type="button" class="btn btn-warning editResource"  onclick="loadInfResource(${item.id})" data-toggle="tooltip" data-placement="bottom" value="edit" id="editResource(${item.id})"/></div>
                                   <div class="col-xs-1 text-center"><input type="button" class="btn btn-warning"  onclick="deleteLink(${item.id})" data-toggle="tooltip" data-placement="bottom" value="delete" id="deleteLink(${item.id})"/></div>

                                   </div>
                                </c:otherwise> 
     
                            </c:choose>
                           
                        </c:forEach>
                    </div>
                    <div class="col-xs-4 text-center">
                        <input type="button" class="btn btn-warning"  data-toggle="tooltip" data-placement="bottom" value="add" id="addLink"/>
                    </div>
                </div>
            </fieldset>
            
            <fieldset>
                <legend id="showDetails">
                    Files
                    <span class="col-xs-12 text-right glyphicon glyphicon-triangle-bottom">
<!--                        <button type="button" class="unStyle" data-toggle="collapse" data-target="#contenedorDetails" >
                            <span class="glyphicon glyphicon-triangle-bottom"></span>
                        </button>-->
                    </span>
                </legend>
                <div class="form-group collapse" id="contenedorDetails">
                <div class="list-group">
                    <c:forEach var="item" items="${files}">  
                                   <div id = "divRecurso${item.id}" class="list-group col-xs-12 text-center">        
                                        <c:url var="post_url"  value="/upload" />    
                                        <form class=" text-center form-group" action="${post_url}" method="GET" enctype="multipart/form-data">  
                                                       <input type="hidden" id="lessonsName" name="lessonsName" value = ${lessonsName}> 
                                                       <input type="hidden" id="txtUrl" name="txtUrl" value="" />
                                                       <input type="hidden" id="lessonid" name="idNameFileDown" value = ${item.id}>                
                                                       <div class="col-xs-8 center-block form-group text-center " >
                                                           <a href="${item.link}"  data-id="${item.id}" class="list-group-item link fileNames" >
                                                               <span class="fileName">${item.name}</span>
                                                             <span class="label label-primary">${item.type}</span> 
                                                           </a>
                                                       </div> 
           
                                                   <input type="submit" value="Download" class="col-xs-2 center-block form-group paddingLabel btn btn-success">     
                                       </form>

                                        <div class="col-xs-1 text-center"><input type="button" class="btn btn-warning"  onclick="deleteLink(${item.id})" data-toggle="tooltip" data-placement="bottom" value="delete" id="deleteLink(${item.id})"/></div>

                                   </div>
                           
                    </c:forEach>
                </div>
                    <div class="col-xs-4 text-center">
                        <input type="button" class="btn btn-warning"  data-toggle="tooltip" data-placement="bottom" value="add" id="addFile"/>
                    </div>
                </div>
                
                 </fieldset>
        </div>
                    
  <div id="editnewLink" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">
      
    <input type="hidden" id="resourceId" name="resourceId" >
    
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header modal-header-details">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 id="nameEditNewLink" class="modal-title">Edit a link or video</h4>
      </div>
        <div class="modal-body">
            <div class="container-fluid">
                <div id="divLinks" class="col-xs-12">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Title</label>
                            <input type="text" class="form-control" name="TXTnameeditethod" id="editLinkName" >
                        </div>
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Link</label>
                            <input type="text" class="form-control" name="TXTcommenteditmethod" id="editLinkComments">
                        </div>   
                    <div class="col-xs-3 center-block form-group">
                        <label class="control-label">Tipo</label>
                        <select class="form-control" name="selectTipo" id="selectLinkTipo" placeholder="Type">
                            <option></option>
                            <option value ="Link">Link</option>
                            <option value ="Video">Video</option>
                        </select>
                    </div>
                        <div class="col-xs-3 center-block form-group paddingLabel">
                            <input type="button" name="EditMethod" diabled="true" value="save changes" class="btn btn-success" id="EditLink" data-target=".bs-example-modal-lg" onclick="saveEditLink()"/> 
                        </div>
                    </div>
                
            </div>
        </div>
<!--      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Save</button>
      </div>-->
    </div>

  </div>
</div>     
<div id="addnewLink" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header modal-header-details">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 id="nameNewLink" class="modal-title">Add new link</h4>
      </div>
        <div class="modal-body">
            <div class="container-fluid">
                <div class="col-xs-12">

                        <div class="col-xs-3 center-block form-group">
                            <label class="control-label">Title</label>
                            <input type="text" class="form-control" name="TXTnameeditethod" id="editNameMethod"  placeholder="Title">
                        </div>
                        <div class="col-xs-6 center-block form-group">
                            <label class="control-label">Link</label>
                            <input type="text" class="form-control" name="TXTcommenteditmethod" id="editCommentsMethod"  placeholder="Link">
                        </div>   
                    <div class="col-xs-3 center-block form-group">
                        <label class="control-label">Type</label>
                        <select class="form-control" name="selectTipo" id="selectTipo" placeholder="Tipo">placeholder="Link"
                            <option >Link</option>
                            <option>Video</option>
 
                        </select>
                    </div>
                        <div class="col-xs-3 center-block form-group paddingLabel">
                            <input type="button" name="EditMethod" disabled="true" value="save" class="btn btn-success" id="EditMethod" data-target=".bs-example-modal-lg" onclick="saveEditMethod()"/> 
                        </div>
                    </div>
                
            </div>
        </div>
<!--      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Save</button>
      </div>-->
    </div>

  </div>
</div>
<div id="addnewFile" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">
    
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header modal-header-details">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 id="nameLessonDetails" class="modal-title">Add new file</h4>
      </div>
        <div class="modal-body">
            <div class="container-fluid">
                <script>
                     $(document).ready(function(){
                         var arrayNamesFiles = [];
                         var existeNombre;
                    $('.fileNames').children('.fileName').each(function() {
                            var nameFile = $(this).text();
                            arrayNamesFiles.push(nameFile);

                    });

                    
                    $('#editNameMethod,#editCommentsMethod').focusout(function() {
                        if($('#editNameMethod').val() !== "" && $('#editCommentsMethod').val() !== "" && is_url($('#editCommentsMethod').val())){
                               $('#EditMethod').prop('disabled', false);
                        }
                        else{
                            $('#EditMethod').prop('disabled', true);
                            if(!is_url($('#editCommentsMethod').val()) && $('#editCommentsMethod').val() != "" ){      
                                 alert('Invalid URL')
                            }
                           
                        }
                    });
                    
                    $('#editLinkName,#editLinkComments').focusout(function() {
                        if($('#editLinkName').val() !== ""  && $('#editLinkComments').val() !== "" && is_url($('#editLinkComments').val())){
                               $('#EditLink').prop('disabled', false);
                           }
                        else{
                                $('#EditLink').prop('disabled', true);
                                if(!is_url($('#editLinkComments').val()) && $('#editLinkComments').val() != "" ){      
                                    alert('Invalid URL')
                                }
                           }
                    });
                    
                    $('#idNameFile,#file').focusout(function() {
                        existeNombre = "0";
                        $.each( arrayNamesFiles, function( index, value ){
                            if ( $('#idNameFile').val() === value) {
                                $('#submitSave').prop('disabled', true);
                                existeNombre = "1";
                                alert('Change name or select file');  
                            }
                        }); 
                        if($('#file').val() !== "") {
                           if(existeNombre === "0"){
                               $('#submitSave').prop('disabled', false);
                           }
                        }
                      });
                      
                     });
                     function is_url(str)
                    {
                      regexp =  /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
                           
                            if (regexp.test(str))
                            {
                              return true;
                            }
                            else
                            {
                              return false;
                            }
                    }
             </script>
             <c:url var="post_url"  value="/upload" />    
             <form class="col-xs-12 center-block form-group" action="${post_url}" method="POST" enctype="multipart/form-data">      
             <!--<form class="col-xs-12 center-block form-group" action="saveFile.htm" method="POST" enctype="multipart/form-data">-->
                        <div class="col-xs-10 center-block form-group">
                            <label class="control-label">Name</label>
                            
                            <input type="hidden" id="txtUrl" name="txtUrl" value="" />
                            <input type="hidden" id="lessonid" name="lessonid" value = ${lessonid}> 
                            <input type="hidden" id="lessonsName" name="lessonsName" value = ${lessonsName}> 

                            <input type="text" class=" col-xs-3 form-control" name="idNameFile" id="idNameFile"  placeholder="Name">
                            <input type="file" class=" col-xs-7 center-block form-control" name="fileToUpload" id="file">
                        
                        </div>
			
                            <input id = "submitSave" type="submit" disabled="true" value="save" class="col-xs-2 center-block form-group paddingLabel btn btn-success">     
            </form>
                
            </div>
        </div>
   
    </div>

  </div>
</div>
    </body>
</html>
