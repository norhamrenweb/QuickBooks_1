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
    <link rel="stylesheet" href="recursos/css/bootstrap.min.css">
    <link rel="stylesheet" href="recursos/css/calendar.css">
       <script type="text/javascript" src="recursos/js/vendor/underscore-min.js"></script>
       <script type="text/javascript" src="recursos/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="recursos/js/calendar.js"></script>
    <script type="text/javascript" src="recursos/js/app.js"></script>
    
    <script type="text/javascript">
//      $(document).ready( function () {
//        $("#calendar").calendar(
//            { 
//              events_source:[
//		{
//			"id": "293",
//			"title": "This is warning class event with very long title to check how it fits to evet in day view",
//			"url": "http://www.example.com/",
//			"class": "event-warning",
//			"start": "1362938400000",
//			"end":   "1363197686300"
//		},
//		{
//			"id": "256",
//			"title": "Event that ends on timeline",
//			"url": "http://www.example.com/",
//			"class": "event-warning",
//			"start": "1363155300000",
//			"end":   "1363227600000"
//		},
//		{
//			"id": "276",
//			"title": "Short day event",
//			"url": "http://www.example.com/",
//			"class": "event-success",
//			"start": "1363245600000",
//			"end":   "1363252200000"
//		},
//		{
//			"id": "294",
//			"title": "This is information class ",
//			"url": "http://www.example.com/",
//			"class": "event-info",
//			"start": "1363111200000",
//			"end":   "1363284086400"
//		},
//		{
//			"id": "297",
//			"title": "This is success event",
//			"url": "http://www.example.com/",
//			"class": "event-success",
//			"start": "1363234500000",
//			"end":   "1363284062400"
//		},
//		{
//			"id": "54",
//			"title": "This is simple event",
//			"url": "http://www.example.com/",
//			"class": "",
//			"start": "1363712400000",
//			"end":   "1363716086400"
//		},
//		{
//			"id": "532",
//			"title": "This is inverse event",
//			"url": "http://www.example.com/",
//			"class": "event-inverse",
//			"start": "1364407200000",
//			"end":   "1364493686400"
//		},
//		{
//			"id": "548",
//			"title": "This is special event",
//			"url": "http://www.example.com/",
//			"class": "event-special",
//			"start": "1363197600000",
//			"end":   "1363629686400"
//		},
//		{
//			"id": "295",
//			"title": "Event 3",
//			"url": "http://www.example.com/",
//			"class": "event-important",
//			"start": "1364320800000",
//			"end":   "1364407286400"
//		}
//]       }
//    );    }); 
            
//            Clases de eventos
//            importante: event-important
//                        event-warning
            
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


	<script type="text/javascript">
		var calendar = $("#calendar").calendar(
			{
				tmpl_path: "<c:url value="/recursos/tmpls/" />",
                      
				events_source: [
		{
			"id": "293",
			"title": "This is warning class event with very long title to check how it fits to evet in day view",
                        "class": "event-warning",
			"start": "1506086822000",
		},
		{
			"id": "256",
			"title": "Event that ends on timeline",
			"url": "http://www.example.com/",
			"class": "event-warning",
			"start": "1363155300000",
			"end":   "1363227600000"
		},
		{
			"id": "276",
			"title": "Short day event",
			"url": "http://www.example.com/",
			"class": "event-success",
			"start": "1363245600000",
			"end":   "1363252200000"
		},
		{
			"id": "294",
			"title": "This is information class ",
			"url": "http://www.example.com/",
			"class": "event-info",
			"start": "1363111200000",
			"end":   "1363284086400"
		},
		{
			"id": "297",
			"title": "This is success event",
			"url": "http://www.example.com/",
			"class": "event-success",
			"start": "1363234500000",
			"end":   "1363284062400"
		},
		{
			"id": "54",
			"title": "This is simple event",
			"url": "http://www.example.com/",
			"class": "",
			"start": "1363712400000",
			"end":   "1363716086400"
		},
		{
			"id": "532",
			"title": "This is inverse event",
			"url": "http://www.example.com/",
			"class": "event-inverse",
			"start": "1364407200000",
			"end":   "1364493686400"
		},
		{
			"id": "548",
			"title": "This is special event",
			"url": "http://www.example.com/",
			"class": "event-special",
			"start": "1363197600000",
			"end":   "1363629686400"
		},
		{
			"id": "295",
			"title": "Event 3",
			"url": "http://www.example.com/",
			"class": "event-important",
			"start": "1364320800000",
			"end":   "1364407286400"
		}
]
			});			
	</script>

    </div>
    
</body>
</html>
