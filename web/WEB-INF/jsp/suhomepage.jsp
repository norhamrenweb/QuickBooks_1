<%-- 
    Document   : SUhomepage
    Created on : 24-ene-2017, 12:12:34
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>QuickBooks Sync</title>

        
        <script>
        $( document  ).ready(function() {
            var UnConfig = ${check};
            if(UnConfig === 0){
                $('#Step2').addClass('hidden');
                $('#Step3').addClass('hidden');
                $('#Step1').removeClass('hidden'); 
            };
            
            $('.glyphicon-plus-sign').click(function() {
                
                $(this).toggleClass('glyphicon-plus-sign glyphicon-minus-sign');
            });
            $('#showMapFamilies').click(function() {
                $('#Step3').addClass('hidden');
                $('#Step2').removeClass('hidden');      
            });
            $('#showSync').click(function() {               
                $('#Step2').addClass('hidden');
                $('#Step3').removeClass('hidden'); 
            });
            $('#showConfig').click(function() {               
                $('#Step2').addClass('hidden');
                $('#Step1').removeClass('hidden'); 
            });
            $('#showMapFamiliesFromConfig').click(function() {               
                $('#Step1').addClass('hidden');
                $('#Step2').removeClass('hidden'); 
            });
            
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
            function funcionCallBackMapFamilies()
            {
                if (ajax.readyState === 4) {
                    if (ajax.status === 200) {
                        $('#divMapFamilies').removeClass('hidden');
                        document.getElementById("origen").innerHTML = ajax.responseText;
                    }
                }
            }
            
             function mapFamilies()
            {
                if (window.XMLHttpRequest) //mozilla
                {
                    ajax = new XMLHttpRequest(); //No Internet explorer
                } else
                {
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");
                }

                
                ajax.onreadystatechange = funcionCallBackMapFamilies;
                //var seleccion1 = document.getElementById("level").value;
                ajax.open("GET", "familymap2.htm?select2=start", true);
                ajax.send("");

            }
        </script>
        <style>
            i
            {
                padding: 5px;
            }
            .divButtonSync
            {
                margin-top: 10px;
                margin-bottom: 10px;
            }
            .divCircle
            {
                display: table;
                width: 16.66667%;
                height: 66px;
            }
            .circle
            {
                width: 100%;
                height: 100%;
                border-radius: 50%;
                background-color: lightgray;
                color: white;
                font-size: 40px;
                font-weight: bold;
                vertical-align: middle;
                display: table-cell;
            }
            
        </style>
    </head>
    <body>
<!--        PASO 1-->
<div class="container hidden" id="Step1">
    <div class="col-xs-2">

    </div>
    <div class="col-xs-8">
    <!--CIRCULO PASO 1-->
    <div class="col-xs-12 text-center center-block">
     <!--    <img src="recursos/img/images_QB/step1-01.png" alt="" width="100px"/>
       <div class="col-xs-offset-5 divCircle">
            <div class="circle">
            1   
            </div>
        </div>-->
    </div>
    <form:form id="form1" method ="post" action="suhomepage.htm?opcion=save">
                
                <div class="col-xs-12 panel-heading">
                    <i data-toggle="collapse" href="#ConfigQuickbooks" class="glyphicon glyphicon-plus-sign"></i>Config Quickbooks
                </div>
                <div id="ConfigQuickbooks" class="col-xs-12 panel-collapse collapse form-group">
                    <label>Quickbooks DB URL:</label>
                    <input class="form-control" type="text" value="${config.qbdburl}" name="qbdburl"/>

                    <label>Quickbooks DB user:</label>
                    <input class="form-control" type="text" value="${config.qbdbuser}" name="qbdbuser"/>

                    <label>Quickbooks DB pswd:</label>
                    <input class="form-control" type="text" value="${config.qbdbpswd}" name="qbdbpswd"/> 
                </div>
                
                <div class="col-xs-12 panel-heading">  
                    <i data-toggle="collapse" href="#ConfigRenweb" class="glyphicon glyphicon-plus-sign"></i>Config Renweb
                </div>
                <div id="ConfigRenweb" class="col-xs-12 panel-collapse collapse form-group">
                    <label>Renweb DB URL:</label>
                    <input class="form-control" type="text" value="${config.rwdburl}" name="rwdburl"/>

                    <label>Renweb DB user:</label>
                    <input class="form-control" type="text" value="${config.rwdbuser}" name="rwdbuser"/>
                
                    <label>Renweb DB pswd:</label>
                    <input class="form-control" type="text" value="${config.rwdbpswd}" name="rwdbpswd"/>
                </div>
                
                <div class="col-xs-12 panel-heading">
                    <i data-toggle="collapse" href="#ConfigTime" class="glyphicon glyphicon-plus-sign"></i>Config Time
                </div>
                <div id="ConfigTime" class="col-xs-12 panel-collapse collapse form-group">
                    <label>Sync Start Date:</label>
                    <input class="form-control" type="text" value="${config.startdate}" name="startdate"/>
                
                    <label>Item Name:</label>
                    <input class="form-control" type="text" value="${config.itemname}" name="itemname"/>
                </div>
                
                
                <div class="col-xs-12 panel-heading">
                    <i data-toggle="collapse" href="#ConfigEduweb" class="glyphicon glyphicon-plus-sign"></i>Config Eduweb
                </div>                                   
                <div id="ConfigEduweb" class="col-xs-12 panel-collapse collapse">
                    <label>Eduweb DB URL:</label>
                    <input class="form-control" type="text" value="${config.edudburl}" name="edudburl"/>

                    <Label>Eduweb DB user:</label>
                    <input class="form-control" type="text" value="${config.edudbuser}" name="edudbuser"/>

                    <label>Eduweb DB pswd:</label>
                    <input class="form-control" type="text" value="${config.edudbpswd}" name="edudbpswd"/>
                </div>
                
                <div class="col-xs-12 panel-heading text-right">
                    <input class="btn btn-success btn-sm" type="submit" value="Save" opcion="save config"/>   
                </div>
            
        </form:form>
    </div>
    <div class="col-xs-2">
        <button type="button" class="btn btn-default" id="showMapFamiliesFromConfig" title="Show Map Families">
             next step
        </button>
    </div>
</div>
<!--        PASO 2-->        
<div class="container hidden" id="Step2">
    <div class="col-xs-2">
        <button type="button" class="btn btn-default" id="showConfig" title="Show Configuration">
             previous step
        </button>
    </div>
    <div class="col-xs-8">
        <!--CIRCULO PASO 2-->
<!--        <div class="col-xs-12 text-center center-block">
            <div class="col-xs-offset-5 divCircle">
                <div class="circle">
                2   
                </div>
            </div>
        </div>-->
        <div class="col-xs-12 text-center center-block">
            <button class="btn btn-primary" onclick="mapFamilies()"> Load Families </button>
        </div>
        <div class="col-xs-12 text-center" >
             <h1>Mapping Families</h1>
         </div>
        <form:form id="form" method="POST" action="familymap2.htm?select2=map">
        <div class="col-xs-12 text-center" id="divMapFamilies">   

            <div class="col-xs-5">
                <div class="col-xs-12">QuickBooks Customers</div>
                <select class="form-control" size="20" name="origen" id="origen" style="width: 100% !important;">

                </select>
            </div>
         <div class="col-xs-2">

         </div>
            

            <div class="col-xs-5">
                <div class="col-xs-12">Renweb Family</div>
                <select class="form-control" size="20" name="destino" id="destino" style="width: 100% !important;"> 
                   
                </select>
            </div>
            <div class="col-xs-12 text-center">
                <input type="submit" class="btn btn-success" value="<spring:message code="etiq.txtmapfamilies"/>">
            </div>
        </div>
        </form:form>
    </div>
    <div class="col-xs-2">
        <button type="button" class="btn btn-default" id="showSync" title="Show Sync">
             next step
        </button>
    </div>
</div>
<!--        PASO 3-->

<div class="container" id="Step3">
    <div class="col-xs-2">
        <button type="button" class="btn btn-default" id="showMapFamilies" title="Map Families">
             previous step
        </button>
    </div>
    <div class="col-xs-8">
        <!--CIRCULO PASO 3-->
<!--        <div class="col-xs-12 text-center center-block">
            <div class="col-xs-offset-5 divCircle">
                <div class="circle">
                3   
                </div>
            </div>
        </div>-->
        <div class="col-xs-12 text-center ">
            Syncronized Quickbooks with RenWeb
        </div>
        <div class="col-xs-12 text-center ">
            <img src="recursos/img/images_QB/Syncronized.gif">
        </div>
        <div class="col-xs-6 col-xs-offset-3 divButtonSync">
            <a class="btn btn-success btn-block" href="suhomepage.htm?opcion=custsync"> Customer Sync </a>
        </div>
        <div class="col-xs-6 col-xs-offset-3 divButtonSync">
            <a class="btn btn-success btn-block" href="suhomepage.htm?opcion=paysync"> Payment Sync </a>
        </div>

        <div class="col-xs-6 col-xs-offset-3 divButtonSync">
            <a class="btn btn-success btn-block" href="suhomepage.htm?opcion=invoicesync"> Invoice Sync </a>
        </div>
        <div class="col-xs-6 col-xs-offset-3 divButtonSync">
            <a class="btn btn-success btn-block" href="suhomepage.htm?opcion=runsync"> Sync All </a>   
        </div>
    </div>

        <jstl:out value="${message1}"/>

</div>
        
    </body>
</html>
