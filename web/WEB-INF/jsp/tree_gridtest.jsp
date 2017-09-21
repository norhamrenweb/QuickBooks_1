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
        <title>Presentation Ideas</title>
        <script>

 $(document).ready(function(){
     
  
      
    });    
 
 
        
        var ajax;
        
   
    function funcionCallBackIdeaLessons()
    {
         if (ajax.readyState===4){
                if (ajax.status===200){
                    var data = JSON.parse(ajax.responseText);
                        $('#tg').treegrid({
        idField:'id',
        treeField:'text',
        columns:[[
            {title:'Task Name',field:'name',width:180},
            {field:'begin',title:'Persons',width:60,align:'right'},
            {field:'end',title:'Begin Date',width:80},
          {field:'progress',title:'End Date',width:80}
         
            
        ]]
    });
    $('#tg').treegrid('loadData', data);
   //myLoadFilter(data);
                }
            }
        }
            
    function editttree()
    {

    }   
   function delttree()
    {
     
    }    
   function funcionCallBackDelIdea()
   {

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
        ajax.onreadystatechange = funcionCallBackIdeaLessons;
        var seleccion1 = document.getElementById("level").value;
        ajax.open("POST","loadtree.htm?seleccion1="+seleccion1,true);
        
        ajax.send("");
       
    }
        function myLoadFilter(data){
        function setData(data){
            var todo = [];
            for(var i=0; i<data.length; i++){
                todo.push(data[i]);
            }
            while(todo.length){
                var node = todo.shift();
                if (node.children && node.children.length){
                    node.state = 'closed';
                    node.children1 = node.children;
                    node.children = undefined;
                    todo = todo.concat(node.children1);
                }
            }
        }
        
        setData(data);
        var tg = $(this);
        var opts = tg.treegrid('options');
        opts.onBeforeExpand = function(row){
            if (row.children1){
                tg.treegrid('append',{
                    parent: row[opts.idField],
                    data: row.children1
                });
                row.children1 = undefined;
                tg.treegrid('expand', row[opts.idField]);
            }
            return row.children1 == undefined;
        };
        return data;
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
        <h1 class="text-center">Student progress</h1>
        
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

  
    <table id="tg" style="width:600px;height:400px"></table>
        
        </div>

        
    </body>
</html>
