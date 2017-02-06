<%-- 
    Document   : familymap2
    Created on : 27-ene-2017, 13:54:53
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mapping Families</title>


        <script>
            $().ready(function ()
            {
                $('.pasar').click(function () {
                    return !$('#origen option:selected').remove().appendTo('#destino');
                });
                $('.quitar').click(function () {
                    return !$('#destino option:selected').remove().appendTo('#origen');
                });
                $('.pasartodos').click(function () {
                    $('#origen option').each(function () {
                        $(this).remove().appendTo('#destino');
                    });
                });
                $('.quitartodos').click(function () {
                    $('#destino option').each(function () {
                        $(this).remove().appendTo('#origen');
                    });
                });
                $('.submit').click(function () {
                    $('#destino option').prop('selected', 'selected');
                });
            });
        </script>
        <style>
     
        </style>
    </head>
    <body>
        <h1>Mapping Families</h1>
        <form:form id="form" method="POST" >  
            <div class="col-xs-3">
                <select class="form-control" size="20" multiple name="origen[]" id="origen" style="width: 100% !important;">
                    <option value="1" >Los Perez</option>
                    <option value="2" >Los Garcia</option>
                    <option value="3" >Los Hernandez</option>
                    <option value="4" >Los Otros</option>
                </select>
            </div>

            <div class="col-xs-2">
                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                    <input type="button" class="btn pasar" value="<spring:message code="etiq.txtadd"/> »">
                </div>
                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                    <input type="button" class="btn quitar" value="« <spring:message code="etiq.txtremove"/>">
                </div>
                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                    <input type="button" class="btn pasartodos" value="<spring:message code="etiq.txtaddAll"/> »">
                </div>
                <div class="col-xs-12 text-center" style="padding-bottom: 10px;">
                    <input type="button" class="btn quitartodos" value="« <spring:message code="etiq.txtremoveAll"/>">
                </div>
            </div>

            <div class="col-xs-3">
                <select class="form-control submit" size="20" multiple name="destino[]" id="destino" style="width: 100% !important;"> 

                </select>
            </div>

            <div class="col-xs-4 text-center">
                <input type="submit" class="btn btn-success" value="<spring:message code="etiq.txtmapfamilies"/>">
            </div>
        </form:form>
        <%--<table border="1">
        <c:forEach items="${allfamily}">
                     var="family">
              <tr>
                  <td>
                      <c:out value="${family.familyName}"/>
                  </td>
                  <td>
                      <c:out value="${family.id}"/>
                  </td>   
                         
              </tr>
          </c:forEach>
          </table>--%>
    </body>
</html>
