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
    <%@ include file="menu.jsp" %>
    <head>
        <title>View Scheme of work</title>
        <script>

 $(document).ready(function(){
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
//   $('#tree').jstree({
//        "plugins" : [ "wholerow", "checkbox", 'search' ]
//    });

         //COMPROMBAR QUE EL ID DEL ARBOL ES NUMERICO Y ACTIVAR LOS BOTONES
                $( "#tree" ).click(function() {
                    var ideaSelect = $("#tree").jstree("get_selected");
                    if($.isNumeric(ideaSelect[0])){
                        $('#editIdea').attr('disabled', false);
                        $('#editIdearsrc').attr('disabled', false);
                        $('#delIdea').attr('disabled', false);
                    }else{
                        $('#editIdea').attr('disabled', true);
                        $('#editIdearsrc').attr('disabled', true);
                        $('#delIdea').attr('disabled', true);
                    };
                        
                    
                    //$(this).html('Lesson name and description<span class="glyphicon glyphicon-triangle-bottom"></span>');
            });      
    });    
 
 
        
        var ajax;
        
   
    function funcionCallBackIdeaLessons()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                   $("#tree").jstree('destroy');
                   var node = JSON.parse(ajax.responseText);
                   var i = node.length;
                   $('#loadingmessage').hide();
                // direct data
                $('#tree').jstree({
                    "core" : {
                        "data" : node
        
                }, 
               "plugins" : [ "wholerow", "search","state", "types" ]  
                });
           
//            formattree();
    var to = false;
        if(to) { clearTimeout(to); }
    to = setTimeout(function () {
      $('#tree').children().children().children('a').children('i').removeClass('jstree-checkbox');
    }, 250); 
      //BUSCADOR LESSONS IDEA          
    var to = false;
    $('#findIdea').keyup(function () {
    if(to) { clearTimeout(to); }
    to = setTimeout(function () {
    var v = $('#findIdea').val();
    $('#tree').jstree(true).search(v);
    }, 250);
  }); 

                    }
                }
            }
            
    function editttree()
    {
//       if (window.XMLHttpRequest) //mozilla
//        {
//            ajax = new XMLHttpRequest(); //No Internet explorer
//        }
//        else
//        {
//            ajax = new ActiveXObject("Microsoft.XMLHTTP");
//        }
//
//       
//    //    ajax.onreadystatechange = funcionCallBackEditIdea;
//        var ideaSelect = $("#tree").jstree("get_selected");
//        ajax.open("POST","editlessonidea.htm?seleccion1="+ideaSelect,true);
//        
//        ajax.send("");
            var ideaSelect = $("#tree").jstree("get_selected");
        window.open("<c:url value="/editlessonidea.htm?LessonsSelected="/>"+ideaSelect);
    }   
        function editrsrctree()
    {
            var ideaSelect = $("#tree").jstree("get_selected");
        window.open("<c:url value="/lessonresources/loadResources.htm?LessonsSelected="/>"+ideaSelect);
    }   
   function delttree()
    {
       if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }

       
    ajax.onreadystatechange = funcionCallBackDelIdea;
        var ideaSelect = $("#tree").jstree("get_selected");
        ajax.open("POST","deletetree.htm?selected="+ideaSelect,true);
        
        ajax.send("");
        
    }    
   function funcionCallBackDelIdea()
   {
//       if (lessoncreate === 'Presentation created' ){
     $('#deleteLessonIdea').modal({
        show: 'false'
        });
//    }
       window.location.reload(true);
      
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
        $('#loadingmessage').show();

        $('#createOnClick').attr('disabled', true);
        ajax.onreadystatechange = funcionCallBackIdeaLessons;
        var seleccion1 = document.getElementById("level").value;
        ajax.open("POST","loadtree.htm?seleccion1="+seleccion1,true);
        
        ajax.send("");
       
    }
    
    
    
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
                .no_checkbox>i.jstree-checkbox
            {
                display:none
            }
            .red
            {
                color: red;
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
        <div class="container">
        <h1 class="text-center">Scheme of work display</h1>

        
        

<!--            <legend id="showDetails">
                    Presentation details
                    <span class="col-xs-12 text-right glyphicon glyphicon-triangle-bottom">
                        <button type="button" class="unStyle" data-toggle="collapse" data-target="#contenedorDetails" >
                            <span class="glyphicon glyphicon-triangle-bottom"></span>
                        </button>
                    </span>
                </legend>-->
                <div class="form-group" id="contenedorDetails">
                <div class="col-xs-3 form-group">
                    <label class="control-label">Select Grade Level</label>
                    <select class="form-control" name="TXTlevel" id="level" onchange="comboSelectionLevel()">
                        <c:forEach var="level" items="${levels}">
                            <option value="${level.id[0]}" >${level.name}</option>
                        </c:forEach>
                    </select>
                          
                </div>
                </div>
                <div class="col-xs-12 center-block">
                    <label class="control-label">Find Objectives</label>
                        <input id="findIdea" class="form-group" type="search">
                </div>
                <div class="col-xs-12 center-block" id="tree">
<!--                    <ul>
                        <li>Root node 1
                            <ul>
                                <li>Child node 1
                                    <ul>
                                        <li>Idea</li>
                                    </ul>
                                    </li>
                                <li><a href="#">Child node 2</a></li>
                            </ul>
                        </li>
                        <li>Root node 2</li>
                    </ul>-->
                </div>
<!--                <div class="col-xs-12 center-block" id="buttons">
                        <div class="col-xs-4 center-block">
                            <button type="button" class="btn btn-warning" onclick="editttree()" id="editIdea" disabled="true">View/Edit</button>
                        </div>
                        <div class="col-xs-4 center-block">
                            <button type="button" class="btn btn-warning" onclick="editrsrctree()" id="editIdearsrc" disabled="true">View/Edit Resources</button>
                        </div>
                        <div class="col-xs-4 center-block">
                            <button type="button"  class="btn btn-danger" onclick="delttree()" id="delIdea" disabled="true">Delete</button>
                        </div>
                </div> -->
                    
       
        
        </div>
<div class="modal fade" id="deleteLessonIdea" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
<!--        <h4 class="modal-title" id="myModalLabel">Modal title</h4>-->
      </div>
      <div class="modal-body text-center">
       <H1>Presentation idea deleted</H1>
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
