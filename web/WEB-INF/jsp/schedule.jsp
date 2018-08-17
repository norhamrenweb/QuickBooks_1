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
            var colorsGrades = ["#2980B9", "#8E44AD", "#16A085",
                "#34495E", "#43D0C3", "#EBDEF0", "#AEB6BF"];
            var miMapa = new Map();
            var cont = 0;

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

                        if (!miMapa.has(data.gradeLevel)) {
                            var auxColor = colorsGrades[cont % colorsGrades.length];
                            miMapa.set(data.gradeLevel, auxColor);
                            cont++;
                        }

                        $(this).css("background-color", miMapa.get(data.gradeLevel));
                        $(this).css("border", miMapa.get(data.gradeLevel));

                        if (data.share === true) {
                            $(this).css("background-color", "gold");
                            $(this).css("color", "#666");
                            $(this).css("border", "1px solid gold");
                        }
                        tooltip = '<div class="tooltiptopicevent" style=" border: solid 1px '+miMapa.get(data.gradeLevel)+';width:auto;background:white;height:auto;position:absolute;z-index:10001;padding:10px 10px 10px 10px ;  line-height: 200%;">'
                                + '<tit style="color:'+miMapa.get(data.gradeLevel)+';">Grade Level: </tit>' + data.gradeLevel + '</br>' + '<tit style="color:'+miMapa.get(data.gradeLevel)+';">Title: </tit>' + data.title + '</br>' + '<tit style="color:'+miMapa.get(data.gradeLevel)+';">Objective:</tit> ' + data.nameobj + '</br> <tit style="color:'+miMapa.get(data.gradeLevel)+';">Created by:</tit> ' + data.createdby + '</div>';

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
                        url: 'loadschedule.htm?termid=' + termId_view + "&yearid=" + yearId_view ,
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
                        $("#legendPresentations").empty();
                        $("#legendPresentations").append("<li><span class='share'></span><label>Shared presentations </label></li>")
                        for (var clave of miMapa.keys()) {
                            //alert(clave);
                            $("#legendPresentations").append("<li>\n\
                                                            <span class='share' style='background-color:" + miMapa.get(clave) + ";'></span>\n\
                                                            <label>" + clave + "</label>\n\
                                                      </li>")
                        }
                    }
                });


            });
        </script>
    </head>
    <style>
        .legend { list-style: none; }
        .legend li { float: left; margin-right: 10px; }
        .legend span { border: 1px solid #ccc; float: left; width: 24px; height: 12px; margin: 2px; border-radius: 3px;}
        .legend .share { background-color: gold; }
        .tooltiptopicevent{
           
            border-radius: 10px;
        }
    </style>
    <body>
        <ul class="col-xs-12 legend" id="legendPresentations">
            <li>
                <span class="share"></span><label><spring:message code="etiq.sharedPresentations"/></label>
            </li>
        </ul>
        <div class="col-xs-12 container">
            <div id='calendar'></div>
        </div>

        <div class="divLoadStudent" id="loadingmessage">
            <div class="text-center"> 
                <img class="imgLoading" src='./recursos/img/large_loading2.gif'/>
            </div>
        </div>
    </body>
</html>
