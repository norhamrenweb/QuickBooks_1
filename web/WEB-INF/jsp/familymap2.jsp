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
        <form:form id="form" method="POST" action="familymap2.htm?select2=map">  
            <div class="col-xs-3">
                <div class="col-xs-12">QuickBooks Customers</div>
                <select class="form-control" size="20" name="origen" id="origen" style="width: 100% !important;">
                     <c:forEach var="custs" items="${QBcust}">
                                    <option value="${custs.id}" >${custs.name}</option>
                                </c:forEach>
                </select>
            </div>

            

            <div class="col-xs-3">
                <div class="col-xs-12">Renweb Family</div>
                <select class="form-control" size="20" name="destino" id="destino" style="width: 100% !important;"> 
                     <c:forEach var="family" items="${RWfamily}">
                            <option value="${family.id}" >${family.familyName}</option>
                            </c:forEach>
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
