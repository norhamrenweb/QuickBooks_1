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
        <script>
            $(document).ready(function () {

                // page is now ready, initialize the calendar...
                var calendar = $('#calendar').fullCalendar({
                    defaultView: 'month',
                    header: {
                        left: 'title',
                        center: 'agendaWeek,month',
                        right: 'today prev,next'
                    },
                    aspectRatio: '1.8',
                    eventMouseover: function (data, event, view) {

                        if (data.share === true) {
                            $(this).css("background-color", "gold");
                            $(this).css("color", "#666");
                            $(this).css("border", "1px solid gold");
                        }
                        tooltip = '<div class="tooltiptopicevent" style="width:auto;background:white;height:auto;position:absolute;z-index:10001;padding:10px 10px 10px 10px ;  line-height: 200%;">'
                                + 'Grade Level: ' + ': ' + data.gradeLevel + '</br>' + 'Title: ' + ': ' + data.title + '</br>' + 'Objective: ' + ': ' + data.nameobj + '</br> Created by: ' + data.createdby + '</div>';

                        $("body").append(tooltip);
                        $(this).mouseover(function (e) {
                            $(this).css('z-index', 10000);
                            $('.tooltiptopicevent').fadeIn('500');
                            $('.tooltiptopicevent').fadeTo('10', 1.9);
                        }).mousemove(function (e) {
                            $('.tooltiptopicevent').css('top', e.pageY + 10);
                            $('.tooltiptopicevent').css('left', e.pageX + 20);
                        });
                    },
                    eventMouseout: function (data, event, view) {
                        $(this).css('z-index', 8);

                        $('.tooltiptopicevent').remove();

                    },
                    dayClick: function () {
                        tooltip.hide()
                    },
                    eventResizeStart: function () {
                        tooltip.hide()
                    },
                    eventDragStart: function () {
                        tooltip.hide()
                    },
                    viewDisplay: function () {
                        tooltip.hide()
                    },
                    events: {
                        url: 'loadschedule.htm',
                        type: 'POST', // Send post data
                        error: function () {
                            alert('There was an error while fetching events.');
                        },
                    }, loading: function (bool) {
                        $('#loadingmessage').show(); // Add your script to show loading
                    },
                    eventAfterAllRender: function (view) {
                        $('#loadingmessage').hide(); // remove your loading 

                        $(".fc-content").each(function (index) {
                            var f = 2;
                            $(this).mouseenter().mouseleave();
                        });
                    }
                });

            });
        </script>
    </head>
    <style>
        .legend { list-style: none; }
        .legend li { float: left; margin-right: 10px; }
        .legend span { border: 1px solid #ccc; float: left; width: 12px; height: 12px; margin: 2px; }
        .legend .share { background-color: gold; }
    </style>
    <body>
        <ul class="legend">
            <li><span class="share"></span> Shared presentations </li>
        </ul>
        <div class="container">
            <div id='calendar'></div>
        </div>


        <div class="divLoadStudent" id="loadingmessage">
            <div class="text-center"> 
                <img class="imgLoading" src='./recursos/img/large_loading2.gif'/>
            </div>
        </div>
    </body>
</html>
