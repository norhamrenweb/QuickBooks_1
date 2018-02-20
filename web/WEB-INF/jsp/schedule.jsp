<%-- 
    Document   : schedule
    Created on : Feb 20, 2018, 1:37:04 PM
    Author     : norhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="infouser.jsp" %>
<link rel='stylesheet' href='fullcalendar/fullcalendar.css' />
<!--<script src='lib/jquery.min.js'></script>
<script src='lib/moment.min.js'></script>-->
<script src='fullcalendar/fullcalendar.js'></script>
<!------ Include the above in your HEAD tag ---------->

<!DOCTYPE html>
<html>
<head>

</head>
<body>
    <script>
        $(document).ready(function() {

    // page is now ready, initialize the calendar...
 var calendar = $('#calendar').fullCalendar({
        defaultView: 'month',
    header:{
        left:   'title',
    center: 'agendaWeek,month',
    right:  'today prev,next'
    },
    aspectRatio:'1.8',
        events: {
            url: 'loadschedule.htm',
            type: 'POST', // Send post data
            error: function() {
                alert('There was an error while fetching events.');
            }
        }
    });
   
});
        </script>
<div class="container">
<div id='calendar'></div>


</div>
</body>
</html>
