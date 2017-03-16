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


    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Settings</title>
        
        <link href="recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
      
        <link href="recursos/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
        <link href="recursos/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="recursos/css/bootstrap-toggle.css" rel="stylesheet" type="text/css"/>
        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        
        <script src="recursos/js/bootstrap.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-toggle.js" type="text/javascript"></script>
<!--        <script src="recursos/js/bootstrap-modal.js" type="text/javascript"></script>-->
        <script src="recursos/js/moment.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script src="recursos/js/es.js" type="text/javascript"></script>
        <script src="recursos/js/ar.js" type="text/javascript"></script>
        
        <script>
      
        
        var ajax;
        

    function funcionCallBackSubject()
    {
           if (ajax.readyState===4){
                if (ajax.status===200){
                    document.getElementById("origen").innerHTML= ajax.responseText;
                    }
                }
            }
//             function funcionCallBacktable()
//    {
//           if (ajax.readyState===4){
//                if (ajax.status===200){
//                  
//                    document.getElementById("table").innerHTML= ajax.responseText;
//                   
//                    }
//                }
//                    
//                }
//            
            


    
  
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
          

        ajax.onreadystatechange = funcionCallBackSubject;
        var seleccion1 = document.getElementById("level").value;
        ajax.open("GET","settings.htm?option=subjectlistLevel&seleccion1="+seleccion1,true);
        
        ajax.send("");
       
    }
    function drawtable2()
    {
      
        var seleccion2 = document.getElementById("origen").value;

    window.location.href = "settings.htm?option=objectivelistSubject&seleccion2="+seleccion2;

       
    };
 

        </script>
        <style>
            textarea 
            {
            resize: none;
            }
        </style>
    </head>
    <%@ include file="menu.jsp" %>
    <body>
        
        <div class="container">
        <h1 class="text-center">Create Lessons</h1>

        
       

        <form>    
            <fieldset>
                    <legend>Select subject</legend>
                    <div class="col-xs-12">
                        <div class="col-xs-2"></div>
                        <div class="col-xs-3">
                            <label>Filter</label>

                            
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="col-xs-2">
                            <select class="form-control" name="levelSubject" id="level" style="width: 100% !important;" onchange="comboSelectionLevel()">

                                <c:forEach var="levels" items="${gradelevels}">
                                    <option value="${levels.id[0]}" >${levels.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-xs-3">
                            <select class="form-control" size="20" name="origen[]" id="origen" style="width: 100% !important;">
                                <c:forEach var="subject" items="${subjects}">
                                    <option value="${subject.id[0]}" >${subject.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                <div class="col-xs-4"></div>
            </fieldset>
            <input type="checkbox" name="id" value="Java">Objectives<BR>
            <input type="checkbox" name="id" value="">Contents<BR>
            <input type="checkbox" name="id" value="">Methods<BR>
            <input type="button" value="Submit" onclick="drawtable2()"/>


   
        </form>

</div>

    </body>
</html>
