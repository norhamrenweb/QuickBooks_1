<%-- 
    Document   : testcomment
    Created on : 22-sep-2017, 11:26:11
    Author     : nmohamed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
     <%@ include file="infouser.jsp" %>
    <%@ include file="menu.jsp" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript">
    $(document).ready( function () {
          $('#fecha').datetimepicker({
            
            format: 'YYYY-MM-DD',
//            locale: userLang.valueOf(),
            daysOfWeekDisabled: [0, 6],
            useCurrent: false//Important! See issue #1075
            //defaultDate: '08:32:33',

  
        });
    });   

    function savecomment()
    {
        if (window.XMLHttpRequest) //mozilla
        {
            ajax = new XMLHttpRequest(); //No Internet explorer
        }
        else
        {
            ajax = new ActiveXObject("Microsoft.XMLHTTP");
        }
        ajax.onreadystatechange = funcionCallBacksavecomment;
        ajax.open("POST","savecomment.htm",true);
        ajax.send("");
  }

  function funcionCallBacksavecomment(){
        if (ajax.readyState===4){
                if (ajax.status===200){
                    
                   $('#confirmsave').modal('show');
                }
        }
        }

  </script>
    </head>
    <body>
        <h1>Class Observation</h1>
       
        <div id="contenedorDate">
                        <div class='col-xs-4'>
                            <div class="form-group">
                                <label class="control-label" for="fecha">Date</label>
                                <div class='input-group date' id='fecha'>
                                    <input type='text' name="TXTfecha" class="form-control" id="fecha"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
            <div class="col-xs-6 center-block form-group">
                        <label class="control-label">Observation</label>
                        <textarea class="form-control" name="TXTdescription" id="comments" placeholder="add comment" maxlength="200"></textarea>
                    </div>
         </div>  
         <div class="col-xs-12 text-center">
            <input type="submit" class="btn btn-success" id="savecomment"  value="Save" onclick="savecomment()">
            </div>
        <div class="col-xs-12 text-center">
            <input type="submit" class="btn btn-success" id="showcalendar"  value="View all comments" onclick="showcalendar()">
            </div>
        <div id="confirmsave" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header modal-header-delete">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">comment saved</h4>
      </div>
        
    </div>

  </div>
</div>
    </body>
</html>
