<%-- 
    Document   : calendar
    Created on : 13-jun-2016, 14:07:00
    Author     : Jesús Aragón
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@ include file="header.jsp" %>--%>
<%@ include file="infouser.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><spring:message code="etiq.txtcalendar"/></title>
<!--    <link rel="stylesheet" href="recursos/css/bootstrap.min.css">
    <link rel="stylesheet" href="recursos/css/calendar.css">
       <script type="text/javascript" src="recursos/js/vendor/underscore-min.js"></script>
       <script type="text/javascript" src="recursos/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="recursos/js/calendar.js"></script>
    <script type="text/javascript" src="recursos/js/app.js"></script>-->
    
    <script type="text/javascript">
    $(document).ready( function () {
 $('#calendar').show({
        // put your options and callbacks here
    })
     }); 

            
    </script>

</head>
<body>
    <div class="container">
        
<%--        <div class="page-header row">
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
		
	</div>--%>
       <div id="calendar"></div>



    </div>
    
</body>
</html>
