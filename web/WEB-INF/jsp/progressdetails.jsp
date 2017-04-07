<%-- 
    Document   : progressdetails
    Created on : 06-abr-2017, 10:18:54
    Author     : Jesus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
        <%@ include file="menu.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Progress Details</title>
        <script>
//            var ajax;
//             $(document).ready(function(){
//                 
//                var json = JSON.parse(ajax.responseText);
//                     var info = JSON.parse(json.info);
//               var subjects = JSON.parse(json.sub);
//                    $('#BOD').val(info.fecha_nacimiento);
//                    $('#student').text(info.nombre_students);
//                    $('#studentid').val(info.id_students);
//                    if(typeof info.foto === 'undefined'){
//                        $('#foto').attr('src', '../recursos/img/NotPhoto.png');
//                    }else{
//                        $('#foto').attr('src', info.foto);
//                    }
//                    $('#subjects').empty();
//                     $.each(subjects, function(i, item) {
//                         $('#subjects').append('<option value= "'+subjects[i].id+'">' + subjects[i].name + '</option>');
//                   });
//      
//    }); 
        </script>
        <style>
            .attempted{
                color: #D0D2D3;
            }
             .containerProgress
            {
                display: table;
/*                background-color: #d9edf7;*/
                min-height: 300px;
            }
            .cellProgress
            {
                display: table-cell;
                vertical-align: middle;
                
            }
            .spacediv
            {
                margin-top: 5px;
                margin-bottom: 5px;
            }
            .mastered
            {
                display: table;
                width: 100%;
                min-height: 100px;
                border-radius: 10px;
                background-color: #08c;
                color: white;
                font-size: xx-large;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="col-xs-12 text-center">
                <h2>${name}</h2>
            </div>
            
            <div class="col-xs-12" style="border-bottom: #08c solid 1px">
                <div class="col-xs-4">Level</div>
                <div class="col-xs-4">Subject</div>
                <div class="col-xs-4">Objective</div>
            </div>
            <div class="col-xs-12">
                <div class="col-xs-4">JP</div>
                <div class="col-xs-4">Math</div>
                <div class="col-xs-4">Fractions</div>
            </div>
            
            <div class="col-xs-6" style="margin-top: 30px;">
                <div class="col-xs-12 spacediv">
                    <div class="col-xs-6">Presented</div><div class="col-xs-6">03/04/2017</div>
                </div>
                <div class="col-xs-12 spacediv">
                    <div class="col-xs-6">Attempted</div><div class="col-xs-6">20/04/2017<span class="glyphicon glyphicon-triangle-bottom"></span></div>
                </div>
                <div class="col-xs-12" id="contenedorAttempted">
                    <div class="col-xs-6 attempted">Attempted</div><div class="col-xs-6 attempted">20/04/2017</div>
                    <div class="col-xs-6 attempted">Attempted</div><div class="col-xs-6 attempted">20/04/2017</div>
                </div>    
                <div class="col-xs-12 spacediv">
                    <div class="col-xs-6">Mastered</div><div class="col-xs-6">03/05/2017</div>
                </div>
            </div>
            <div class="col-xs-6 text-center">
                <div class="col-xs-6 text-center containerProgress">
                    <div class="cellProgress">
                        <div class="mastered">
                            <div class="cellProgress text-center">MASTERED</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 text-center">
                <table class="display">
                    
                </table>
            </div>
        </div>
        
    </body>
</html>
