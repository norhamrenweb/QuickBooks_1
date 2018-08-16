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
        
        $(".iconosmenulateral").click(function () {
            changeTermYearModify();
        });
    });

</script>

<!--MENU LATERAL-->
<div id="barralateral">
    <nav>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/homepage/loadLessons.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/Home.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipHome'/>">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/observations/start.htm'/>" target="_blank">
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/WorkSpace.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipWorkspace'/>">
                </div>
            </a>
        </div>
        <div class="col-xs-12">
            <hr class="separadorMenu">
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/lessonarchive/loadLessons.htm'/>">
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/Calendar.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipComplPresen'/>">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/lessonidea/start.htm'/>">
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/LessonsIdea.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipPresIdeas'/>">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/createlesson/start.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/classroomDashboard.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipCreateNew'/>">
                </div>
            </a>
        </div>
        <div class="col-xs-12">
            <hr class="separadorMenu">
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/progressbystudent/start.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/students.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipStudents'/>">
                </div>
            </a>
        </div>
        <div class="col-xs-12 iconosmenulateral">
            <a href="<c:url value='/reportControlador/start.htm'/>" >
                <div class="center-block">
                    <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/Reports.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipReports'/>">
                </div>
            </a>
        </div>
        <div class="col-xs-12">
            <hr class="separadorMenu">
        </div>
        <c:choose>
            <c:when test = "${user.type == 0 || user.type == 2}">
                <div class="col-xs-12 iconosmenulateral">
                    <a href="<c:url value='/createsetting/start.htm'/>" >
                        <div class="center-block">
                            <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/CreateSettings.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipModifySOW'/>">
                        </div>
                    </a>
                </div>
                <div class="col-xs-12 iconosmenulateral">
                    <a href="<c:url value='/setupControlador/start.htm'/>" >
                        <div class="center-block">
                            <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/Setup.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipSetup'/>">
                        </div>
                    </a>                    
                </div>
            </c:when>
            <c:when test = "${user.type == 1}">
                <div class="col-xs-12 iconosmenulateral">
                    <a href="<c:url value='/sowdisplay/start.htm'/>" >
                        <div class="center-block">
                            <img width="70%" src="<c:url value='/recursos/img/iconos/menu_lateral/CreateSettings.svg'/>" data-toggle="tooltip" data-placement="top" title="<spring:message code='etiq.tootltipWorkspace'/>">
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

