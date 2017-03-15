﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>

<head>
      <meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="apple-touch-icon" href="recursos/img/logo.ico">
        <link rel="shortcut icon" href="recursos/img/logo.ico">
        <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Montserrat">
        

        <link href="recursos/css/menu-lateral.css" rel="stylesheet" type="text/css"/>
        

        <script>


    $(document).ready(function(){
        
    
       //Menu lateral
        $('#nav-expander').on('click',function(e){
      		e.preventDefault();
      		$('body').toggleClass('nav-expanded');
      	});
      	$('#nav-close').on('click',function(e){
      		e.preventDefault();
      		$('body').removeClass('nav-expanded');
      	});
        $('#barralateral').mouseleave(function(o){
      		o.preventDefault();
      		$('body').removeClass('nav-expanded');
      	});
 
     
    });
        </script>

        
        <style>
            .btnBandera
            {
                padding-left: 2px;
                padding-right: 2px;
                margin-left: 0px;
                margin-right: 0px;
            }
            .contenedorBandera
            {
                padding-left: 0px;
                padding-right: 0px;
            }
            hr{
                padding-top: 8px;
                padding-bottom: 8px;
                color: white;
                size: 2px;
                margin-top: 0px;
                margin-bottom: 0px;
            }
        </style>
</head>
    
        <!--MENU LATERAL-->
        <div id="barralateral">
        <nav>
<!--<div class="main-menu">
 
            Include your navigation here-->
            
            <div class="col-xs-12 iconosmenulateral"><a href="homepage.htm?select3=loadLessons"><input type="image" src="recursos/img/iconos/home-01.svg" data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txthome"/>"></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="lessonList.htm?accion=loadLessons"><div class="center-block"><input type="image" src="recursos/img/iconos/Calendar-01.svg" data-toggle="tooltip" data-placement="top" title="Planned Lessons"></div></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="lessonList.htm?accion=loadLessons"><div class="center-block"><input type="image" src="recursos/img/iconos/classroomDashboard-01.svg" data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txtlessons"/>"></div></a></div>
            <%--<div class="col-xs-12 iconosmenulateral"><a href="messages.htm"><div class="center-block"><input type="image" src="recursos/img/iconos/messages-01.svg" data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txtmessages"/>"></div></a></div>--%>
            <div class="col-xs-12 iconosmenulateral"><a href="progressbystudent.htm?option=start"><div class="center-block"><input type="image" src="recursos/img/iconos/students-01.svg" data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txtstudents"/>"></div></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="settings.htm?option=start"><div class="center-block"><input type="image" src="recursos/img/iconos/CreateSettings-01.svg" data-toggle="tooltip" data-placement="top" title="Create Settings"></div></a></div>

            <div class="col-xs-12">
                <hr>
            </div>
            <div class="col-xs-12 iconosmenulateral">
                <div class="col-xs-4 center-block text-center contenedorBandera">
                    <a class="btnBandera" href='datosIdioma.htm?lenguaje=en'><img width="30px" height="20px" src="recursos/img/iconos/flags/flag_en.png" title="<spring:message code="etiq.txtenglish"/>" alt="<spring:message code="etiq.txtenglish"/>"></a>
                </div>
                <div class="col-xs-4 center-block text-center contenedorBandera">
                    <a class="btnBandera" href='datosIdioma.htm?lenguaje=es'><img width="30px" src="recursos/img/iconos/flags/flag_es.png" title="<spring:message code="etiq.txtspanish"/>" alt="<spring:message code="etiq.txtspanish"/>"></a>
                </div>
                <div class="col-xs-4 center-block text-center contenedorBandera">
                    <a class="btnBandera" href='datosIdioma.htm?lenguaje=ar'><img width="30px" src="recursos/img/iconos/flags/flag_ar.png" title="<spring:message code="etiq.txtarabic"/>" alt="<spring:message code="etiq.txtarabic"/>"></a>
                </div>
            </div>
        </nav>    
        </div>
            
 
            <div>
                <!--Include your brand here-->
                <!--<a class="navbar-brand" href="#">Off Canvas Menu</a>-->
                <div class="navbar-header pull-right">
                  <a id="nav-expander" class="nav-expander fixed">
                    <spring:message code="etiq.txtmenu"/>
                  </a>
                </div>
            </div>
        </div>
        <!--MENU LATERAL FINAL-->
    
