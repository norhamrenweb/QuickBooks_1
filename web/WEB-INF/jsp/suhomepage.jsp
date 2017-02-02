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
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
       
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <body>
        <h1 class="text-center"><jstl:out value="${message}"/></h1>
        <form:form id="form1" method ="post" action="suhomepage.htm?opcion=save">
            <div class="col-xs-6">
                <div class="col-xs-12">Config Quickbooks</div>
                <div class="col-xs-12">
                    <div class="col-xs-6">Quickbooks DB URL:</div>
                    <div class="col-xs-6">
                        <input type="text" name="qbdburl"/>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-6">Quickbooks DB user:</div>
                    <div class="col-xs-6">
                        <input type="text" name="qbdbuser"/>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-6">Quickbooks DB pswd:</div>
                    <div class="col-xs-6">
                        <input type="text" name="qbdbpswd"/>
                    </div>
                </div>
                
                <div class="col-xs-12"><a data-toggle="collapse" href="#collapse2">Config Renweb</a></div>
                <div id="collapse2" class="panel-collapse collapse">
                    
                
                <div class="col-xs-12">
                    <div class="col-xs-6">Renweb DB URL:</div>
                    <div class="col-xs-6">
                        <input type="text" name="rwdburl"/>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-6">Renweb DB user:</div>
                    <div class="col-xs-6">
                        <input type="text" name="rwdbuser"/>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-6">Renweb DB pswd:</div>
                    <div class="col-xs-6">
                        <input type="text" name="rwdbpswd"/>
                    </div>
                </div>
                    </div>
                <div class="col-xs-12">Config Time</div>
                <div class="col-xs-12">
                    <div class="col-xs-6">Sync Start Date:</div>
                    <div class="col-xs-6">
                        <input type="text" name="startdate"/>
                    </div>
                </div>
               <div class="col-xs-12">
                    <div class="col-xs-6">Item Name:</div>
                    <div class="col-xs-6">
                        <input type="text" name="itemname"/>
                    </div>
                </div>
                
                <div class="panel-group">
                    <div class="panel panel-default">
                      <div class="panel-heading">
                        <h4 class="panel-title">
                          <a data-toggle="collapse" href="#collapse1">Config Eduweb</a>
                        </h4>
                      </div>
                      <div id="collapse1" class="panel-collapse collapse">
                                    <div class="col-xs-6">Eduweb DB URL:</div>
                                    <div class="col-xs-6">
                                        <input type="text" name="edudburl"/>
                                    </div>

                                    <div class="col-xs-6">Eduweb DB user:</div>
                                    <div class="col-xs-6">
                                        <input type="text" name="edudbuser"/>
                                    </div>

                                    <div class="col-xs-6">Eduweb DB pswd:</div>
                                    <div class="col-xs-6">
                                        <input type="text" name="edudbpswd"/>
                                    </div>
                      </div>
                    </div>
                </div>
               
             
                <div class="col-xs-12 text-center">
                      <input type="submit" value="Save" opcion="save"/>   
                </div>
            </table>
        </form:form>
            </div>
        <div class="col-xs-3" style="background-color: grey">
            <button href="suhomepage.htm?opcion=runsync"> run </button> 
        <br/>
        <jstl:out value="${message1}"/> 
        <br/>
        </div>
        <div class="col-xs-3">
        <a href="familymap2.htm?opcion=map"> Map Families </a>
        </div>
    </body>
</html>
