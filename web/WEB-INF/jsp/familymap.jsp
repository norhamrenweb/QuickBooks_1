<%-- 
    Document   : familymap
    Created on : 27-ene-2017, 12:30:25
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <%@ include file="menu.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Mapping Families</h1>
        
         <!--COMBO SECCION--> 
         <form:form id="form" method="POST" >                  
                           <div class="col-xs-3 center-block">
                               <label class="control-label">
 
</label>

                                       <select class="form-control select-subsection" id="subsection" name="TXTsubsection" >
                                           <jstl:forEach items="${allfamily}" var="family" >
                                               <option value="${family.familyName}" selected></option>
                                           </jstl:forEach>
                                           <jstl:forEach var="family" items="${allfamily}">
                                               <option value="${family.familyName}" ></option>
                                           </jstl:forEach>
                                       </select>
  
                               <input type="hidden" class="btn btn-default" name='accion' id="comboEquipment" value="comboEquipment"/>
                           </div>
                  </form:form>             
                           
    </body>
</html>
