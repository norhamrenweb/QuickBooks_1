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
            <div class="col-xs-12 iconosmenulateral"><a href="<c:url value="/homepage/loadLessons.htm"/>" ><input type="image" src="<c:url value="/recursos/img/iconos/home-01.svg"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txthome"/>"></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="lessonList.htm?accion=loadLessons"><div class="center-block"><input type="image" src="<c:url value="/recursos/img/iconos/Calendar-01.svg"/>" data-toggle="tooltip" data-placement="top" title="View calendar"></div></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="<c:url value="/lessonidea/start.htm"/>"><div class="center-block"><input type="image" src="<c:url value="/recursos/img/iconos/LessonsIdea.svg"/>" data-toggle="tooltip" data-placement="top" title="Lessons Idea"></div></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="<c:url value="/createlesson/start.htm"/>" ><div class="center-block"><input type="image" src="<c:url value="/recursos/img/iconos/classroomDashboard-01.svg"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txtlessons"/>"></div></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="<c:url value="/progressbystudent/start.htm"/>" ><div class="center-block"><input type="image" src="<c:url value="/recursos/img/iconos/students-01.svg"/>" data-toggle="tooltip" data-placement="top" title="<spring:message code="etiq.txtstudents"/>"></div></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="<c:url value="/createsetting/start.htm"/>" ><div class="center-block"><input type="image" src="<c:url value="/recursos/img/iconos/CreateSettings-01.svg"/>" data-toggle="tooltip" data-placement="top" title="Create Settings"></div></a></div>
            <div class="col-xs-12 iconosmenulateral"><a href="<c:url value="/createsetting/start.htm"/>" ><div class="center-block"><input type="image" src="<c:url value="/recursos/img/iconos/Reports.svg"/>" data-toggle="tooltip" data-placement="top" title="Reports"></div></a></div>
<!--             <div class="col-xs-12">
                <hr>
            </div>
           <div class="col-xs-12 iconosmenulateral">
                <div class="col-xs-4 center-block text-center contenedorBandera">
                    <a class="btnBandera" href='datosIdioma.htm?lenguaje=en'><img width="30px" height="20px" src="<c:url value="/recursos/img/iconos/flags/flag_en.png"/>" title="<spring:message code="etiq.txtenglish"/>" alt="<spring:message code="etiq.txtenglish"/>"></a>
                </div>
                <div class="col-xs-4 center-block text-center contenedorBandera">
                    <a class="btnBandera" href='datosIdioma.htm?lenguaje=es'><img width="30px" src="<c:url value="/recursos/img/iconos/flags/flag_es.png"/>" title="<spring:message code="etiq.txtspanish"/>" alt="<spring:message code="etiq.txtspanish"/>"></a>
                </div>
                <div class="col-xs-4 center-block text-center contenedorBandera">
                    <a class="btnBandera" href='datosIdioma.htm?lenguaje=ar'><img width="30px" src="<c:url value="/recursos/img/iconos/flags/flag_ar.png"/>" title="<spring:message code="etiq.txtarabic"/>" alt="<spring:message code="etiq.txtarabic"/>"></a>
                </div>
            </div>-->
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

