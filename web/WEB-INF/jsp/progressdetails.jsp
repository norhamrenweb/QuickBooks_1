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
           $(document).ready(function(){
                $('#tablelessons').DataTable();
                
                $("#contenedorAttempted").on("hide.bs.collapse", function(){
                    $("#showAttempteds").html('2<br><span class="glyphicon glyphicon-triangle-bottom"></span>');
                });
                $("#contenedorAttempted").on("show.bs.collapse", function(){
                  $("#showAttempteds").html('2<br><span class="glyphicon glyphicon-triangle-top"></span>');
                });
            });
        </script>
        <style>
            .attempted{
                color: #D0D2D3;
            }
             .containerProgress
            {
                display: table;
/*                background-color: #d9edf7;*/
                min-height: 200px;
            }
            .cellProgress
            {
                display: table-cell;
                vertical-align: middle;
                padding: 10px;
                
            }
            .spacediv
            {
                margin-top: 5px;
                margin-bottom: 5px;
            }
            .mastered
            {
                display: table;
                width: 50%;
                min-height: 100px;
                border-radius: 10px;
                background-color: #08c;
                color: white;
                font-size: xx-large;
                font-weight: bold;
            }
            #showAttempteds
            {
                background-color: transparent;
                border-width: 0px;
                border-style: none;
                border-color: transparent;
                border-image: initial;
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
                    <div class="col-xs-6">Attempted</div><div class="col-xs-6">20/04/2017
                    <button class="" data-toggle="collapse" data-target="#contenedorAttempted" id="showAttempteds">
                        2<br><span class="glyphicon glyphicon-triangle-bottom"></span>
                    </button>
                    </div>    
                </div>
                <div class="col-xs-12 collapse" id="contenedorAttempted">
                    <div class="col-xs-6 attempted">Attempted</div><div class="col-xs-6 attempted">20/04/2017</div>
                    <div class="col-xs-6 attempted">Attempted</div><div class="col-xs-6 attempted">20/04/2017</div>
                </div>    
                <div class="col-xs-12 spacediv">
                    <div class="col-xs-6">Mastered</div><div class="col-xs-6">03/05/2017</div>
                </div>
            </div>
            <div class="col-xs-6 center-block">
                <div class="col-xs-6 col-xs-offset-3 containerProgress">
                    <div class="cellProgress">
                        <div class="mastered">
                            <div class="cellProgress text-center">MASTERED</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-12" id="divTableLessons">
                <table id="tablelessons" class="display">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Date</th>
                            <th>Comment general</th>
                        </tr>
                    </thead> 
                </table>

            </div>
        </div>
        
    </body>
</html>
