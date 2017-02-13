<%-- 
    Document   : SUhomepage
    Created on : 24-ene-2017, 12:12:34
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <%@ include file="infouser.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
       
        <link href="recursos/css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <script src="recursos/js/jquery-2.2.0.js" type="text/javascript"></script>
        <script src="recursos/js/bootstrap.js" type="text/javascript"></script>
        
        <script>
        $( document ).ready(function() {
            $('.glyphicon-plus-sign').click(function() {
                
                $(this).toggleClass('glyphicon-plus-sign glyphicon-minus-sign');
            });
        });
           
        </script>
        <style>
            i
            {
                padding: 5px;
            }
        </style>
    </head>
    <body>
        
        <form:form id="form1" method ="post" action="suhomepage.htm?opcion=save">
            <div class="col-lg-4 col-md-4 col-xs-12">
                
                <div class="col-xs-12 panel-heading">
                    <i data-toggle="collapse" href="#ConfigQuickbooks" class="glyphicon glyphicon-plus-sign"></i>Config Quickbooks
                </div>
                <div id="ConfigQuickbooks" class="col-xs-12 panel-collapse collapse form-group">
                    <label>Quickbooks DB URL:</label>
                    <input class="form-control" type="text" name="qbdburl"/>

                    <label>Quickbooks DB user:</label>
                    <input class="form-control" type="text" name="qbdbuser"/>

                    <label>Quickbooks DB pswd:</label>
                    <input class="form-control" type="text" name="qbdbpswd"/> 
                </div>
                
                <div class="col-xs-12 panel-heading">  
                    <i data-toggle="collapse" href="#ConfigRenweb" class="glyphicon glyphicon-plus-sign"></i>Config Renweb
                </div>
                <div id="ConfigRenweb" class="col-xs-12 panel-collapse collapse form-group">
                    <label>Renweb DB URL:</label>
                    <input class="form-control" type="text" name="rwdburl"/>

                    <label>Renweb DB user:</label>
                    <input class="form-control" type="text" name="rwdbuser"/>
                
                    <label>Renweb DB pswd:</label>
                    <input class="form-control" type="text" name="rwdbpswd"/>
                </div>
                
                <div class="col-xs-12 panel-heading">
                    <i data-toggle="collapse" href="#ConfigTime" class="glyphicon glyphicon-plus-sign"></i>Config Time
                </div>
                <div id="ConfigTime" class="col-xs-12 panel-collapse collapse form-group">
                    <label>Sync Start Date:</label>
                    <input class="form-control" type="text" name="startdate"/>
                
                    <label>Item Name:</label>
                    <input class="form-control" type="text" name="itemname"/>
                </div>
                
                
                <div class="col-xs-12 panel-heading">
                    <i data-toggle="collapse" href="#ConfigEduweb" class="glyphicon glyphicon-plus-sign"></i>Config Eduweb
                </div>    
                                
                <div id="ConfigEduweb" class="col-xs-12 panel-collapse collapse">
                    <label>Eduweb DB URL:</label>
                    <input class="form-control" type="text" name="edudburl"/>

                    <Label>Eduweb DB user:</label>
                    <input class="form-control" type="text" name="edudbuser"/>

                    <label>Eduweb DB pswd:</label>
                    <input class="form-control" type="text" name="edudbpswd"/>
                </div>
                <div class="col-xs-12 panel-heading text-right">
                    <input class="btn btn-sm" type="submit" value="Save" opcion="save config"/>   
                </div>
            </div>
        </form:form>
            </div>
        <div class="col-lg-4 col-md-4 col-xs-12 text-center">
            <label>Syncronized Quickbooks with RenWeb</label>
            <input type="image" src="recursos/img/images_QB/Syncronized.gif" href="suhomepage.htm?opcion=runsync" class="form-group">
        <br/>
        <jstl:out value="${message1}"/> 
        <br/>
        <div class="col-lg-12 col-md-12 col-xs-12 text-center form-group">
            <input class="btn btn-sm" type="submit" value="Customer Sync" opcion=""/> 
        </div>
        <div class="col-lg-12 col-md-12 col-xs-12 text-center form-group">
            <input class="btn btn-sm" type="submit" value="Invoice Sync" opcion=""/> 
        </div>
        <div class="col-lg-12 col-md-12 col-xs-12 text-center form-group">
            <input class="btn btn-sm" type="submit" value="Payment Sync" opcion=""/> 
        </div>
        </div>
        <div class="col-lg-4 col-md-4 col-xs-12">
        <a href="familymap2.htm?select2=start"> Map Families </a>
        </div>
    </body>
</html>
