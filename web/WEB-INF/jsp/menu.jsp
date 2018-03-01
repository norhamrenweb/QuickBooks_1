<script>
    $(document).ready(function () {
        //Menu lateral
        $('#nav-expander').on('click', function (e) {
            e.preventDefault();
            $('body').toggleClass('nav-expanded');
        });
        $('#nav-close').on('click', function (e) {
            e.preventDefault();
            $('body').removeClass('nav-expanded');
        });
        $('#barralateral').mouseleave(function (o) {
            o.preventDefault();
            $('body').removeClass('nav-expanded');
        });
    });

</script>
<!--MENU LATERAL-->
<div id="barralateral">
    <nav>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/homepage/loadLessons.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/Home-01.svg'/>" data-toggle="tooltip" data-placement="top" title="Home">
                </div>
            </a>
        </div>
         <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/observations/start.htm'/>" target="_blank">
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/notepad.svg'/>" data-toggle="tooltip" data-placement="top" title="Workspace">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/lessonarchive/loadLessons.htm'/>">
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/Calendar-01.svg'/>" data-toggle="tooltip" data-placement="top" title="Completed Presentations">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/lessonidea/start.htm'/>">
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/LessonsIdea.svg'/>" data-toggle="tooltip" data-placement="top" title="Presentations Ideas">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/createlesson/start.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/classroomDashboard-01.svg'/>" data-toggle="tooltip" data-placement="top" title="Create new presentation">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/progressbystudent/start.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/students-01.svg'/>" data-toggle="tooltip" data-placement="top" title="Students">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/reportControlador/start.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/Reports.svg'/>" data-toggle="tooltip" data-placement="top" title="Reports">
                </div>
            </a>
        </div>
        <c:choose>
            <c:when test = "${user.type == 0 || user.type == 2}">
                <div class="col-xs-12 iconosmenulateral">
                    <a href="<c:url value='/createsetting/start.htm'/>" >
                        <div class="center-block">
                            <img width="70%" src="<c:url value='/recursos/img/iconos/CreateSettings-01.svg'/>" data-toggle="tooltip" data-placement="top" title="Modify SOW">
                        </div>
                    </a>
                </div>
                <div class="col-xs-12 iconosmenulateral">
                    <a href="<c:url value='/setupControlador/start.htm'/>" >
                        <div class="center-block">
                            <img width="70%" src="<c:url value='/recursos/img/iconos/Setup-01.svg'/>" data-toggle="tooltip" data-placement="top" title="Setup">
                        </div>
                    </a>                    
                </div>
            </c:when>
            <c:when test = "${user.type == 1}">
                <div class="col-xs-12 iconosmenulateral">
                    <a href="<c:url value='/sowdisplay/start.htm'/>" >
                        <div class="center-block">
                            <img width="70%" src="<c:url value='/recursos/img/iconos/CreateSettings-01.svg'/>" data-toggle="tooltip" data-placement="top" title="SOW">
                        </div>
                    </a>
                </div>
            </c:when>
            <c:otherwise>
            </c:otherwise>
        </c:choose>
    </nav>    
</div>


<div>
    <div class="navbar-header pull-right">
        <a id="nav-expander" class="nav-expander fixed">
            <spring:message code="etiq.txtmenu"/>
        </a>
    </div>
</div>
</div>
<!--MENU LATERAL FINAL-->

