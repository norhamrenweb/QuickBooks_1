<%-- 
    Document   : calendar
    Created on : 13-jun-2016, 14:07:00
    Author     : Jesús Aragón
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><spring:message code="etiq.txtcalendar"/></title>
    <link rel="stylesheet" href="recursos/css/bootstrap.min.css">
    <link rel="stylesheet" href="recursos/css/calendar.css">
  
    <script src="recursos/js/language/es-ES.js" type="text/javascript"></script>
    <script src="recursos/js/language/ar-SA.js" type="text/javascript"></script>
       

</head>
<body>
    <div class="container">
        
        <div class="page-header row">
            <div class="col-sm-4">
                <h3><spring:message code="etiq.txtcalendar"/> 2016-2017</h3>
		<small><spring:message code="etiq.txtstudentlessons"/> ....... </small>
            </div>
            <div class="col-sm-8">
                <div class="pull-right form-inline">
                    <div class="btn-group">
                            <button class="btn btn-primary" data-calendar-nav="prev"><< <spring:message code="etiq.txtprevious"/></button>
                            <button class="btn" data-calendar-nav="today"><spring:message code="etiq.txttoday"/></button>
                            <button class="btn btn-primary" data-calendar-nav="next"><spring:message code="etiq.txtnext"/> >></button>
                    </div>
                    <div class="btn-group">
                            <button class="btn btn-warning" data-calendar-view="year"><spring:message code="etiq.txtyear"/></button>
                            <button class="btn btn-warning active" data-calendar-view="month"><spring:message code="etiq.txtmonth"/></button>
                            <button class="btn btn-warning" data-calendar-view="week"><spring:message code="etiq.txtweek"/></button>
                            <button class="btn btn-warning" data-calendar-view="day"><spring:message code="etiq.txtday"/></button>
                    </div>
                </div>
            </div>
		
	</div>
        <div class="col-sm-9">
            <div id="calendar"></div>
        </div>
        <div class="col-sm-3">
            <select id="language" class="span12">
					<option value="en-US" select>Seleccione el idioma (por : en-US)</option>
					<option value="ar-SA">Arabe</option>
					<option value="nl-NL">Dutch</option>
					<option value="fr-FR">French</option>
					<option value="de-DE">German</option>
					<option value="el-GR">Greek</option>
					<option value="hu-HU">Hungarian</option>
					<option value="id-ID">Bahasa Indonesia</option>
					<option value="it-IT">Italian</option>
					<option value="pl-PL">Polish</option>
					<option value="pt-BR">Portuguese (Brazil)</option>
					<option value="ro-RO">Romania</option>
					<option value="es-CO">Spanish (Colombia)</option>
					<option value="es-MX">Spanish (Mexico)</option>
					<option value="es-ES">Spanish (Spain)</option>
					<option value="ru-RU">Russian</option>
					<option value="sk-SR">Slovak</option>
					<option value="sv-SE">Swedish</option>
					<option value="zh-CN">简体中文</option>
					<option value="zh-TW">繁體中文</option>
					<option value="ko-KR">한국어</option>
					<option value="th-TH">Thai (Thailand)</option>
				</select>
        </div>
    </div>
    <script type="text/javascript" src="recursos/js/vendor/underscore-min.js"></script>
    <script type="text/javascript" src="recursos/js/calendar.js"></script>
    <script type="text/javascript" src="recursos/js/app.js"></script>
    
    <script type="text/javascript">
        
        var calendar = $("#calendar").calendar(
            {
                
            });     
            
//            Clases de eventos
//            importante: event-important
//                        event-warning
            
    </script>
</body>
</html>
