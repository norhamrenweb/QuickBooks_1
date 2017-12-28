<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.FileInputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%! Properties properties = new Properties();
    Properties languages = new Properties();
    ArrayList<String> tableNames = new ArrayList<String>();
    ResultSet queryResultSet = null;
    int rowsAffected = -1;
    String errorMessage ="";

    private String getProperty(String input) {
      String lang = languages.getProperty(input);
      try {
        return new String(lang.getBytes("ISO-8859-1"), "UTF-8");
      } catch (Exception e) {
        return lang;
      }
    }

    private void getResults(String query) {
      try{
        Class.forName("cdata.jdbc.quickbooks.QuickBooksDriver");
        Connection conn = DriverManager.getConnection("jdbc:quickbooks:", properties);
        Statement stmt = conn.createStatement();
        rowsAffected = -1;
        queryResultSet = null;
        if (query.toUpperCase().trim().startsWith("SELECT")) {
          stmt.execute(query);
          queryResultSet = stmt.getResultSet();
        } else {
          rowsAffected = stmt.executeUpdate(query);
        }
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
    }
%>

<%  request.setCharacterEncoding("UTF-8"); 
    String realpath = request.getRealPath("/");
    try{
      FileInputStream in = new FileInputStream(realpath+"/databrowser.properties");
      languages.load(in);
    } catch(Exception e) {
      out.println(e);
    }
    errorMessage ="";
    
    
    String action = "";
    if(request.getParameter("buttonPressed") != null) {
      action = request.getParameter("buttonPressed");
      if(action.equalsIgnoreCase("Connect")) {
        tableNames.clear();
        queryResultSet = null;
        rowsAffected = -1;
        try{
          Class.forName("cdata.jdbc.quickbooks.QuickBooksDriver");
          Connection conn = DriverManager.getConnection("jdbc:quickbooks:", properties);
          DatabaseMetaData metaData = conn.getMetaData();
          ResultSet tableResultSet = metaData.getTables(null, null, "%", null);
          while(tableResultSet.next()) {
            tableNames.add(tableResultSet.getString("TABLE_NAME"));
          }
        } catch(SQLException e) {
          errorMessage = e.getMessage();
        }
      }
      else if (action.equalsIgnoreCase("View data")) {
        if(request.getParameter("names") != null) {
          getResults("SELECT * FROM [" + request.getParameter("names") + "] LIMIT 20");
        }
        else errorMessage = getProperty("jsp_specify_table");
      }
      else if (action.equalsIgnoreCase("Execute query")) {
        String query = request.getParameter("query");
        getResults(query);
      }
    }
%>

<html>
  <head>
    <title><%=getProperty("jsp_title")%></title>
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
    <meta name="description" content="ProviderQB Demos - Data Browser">
  </head>
  <body>
    <div id="content">
      <h1><%=getProperty("jsp_demo")%></h1>
      <h2><%=getProperty("jsp_name")%></h2>
      <a href="seecode.jsp?DataBrowser.jsp">[<%=getProperty("jsp_see_code")%>]</a>
      <a href="index.jsp">[<%=getProperty("jsp_other_demo")%>]</a>
      <hr/>
      <table>
        <form name="form1" method="POST">
          <input type="hidden" name="buttonPressed">
          <tr>
           <td><input type="button" value="<%=getProperty("sw_connect")%>" onclick="connectButton()"></td>
          </tr>
          <tr>
            <td><%=getProperty("sw_table_name")%></td>
            <td>
              <select name = "names" style="width: 100px">
              <%  if (tableNames != null) {
                  for(String name : tableNames) { %>
                    <% if (name.equals(request.getParameter("names"))) {%>
                    <option value="<%=name%>" selected><%=name%></option>
                    <%} else {%>
                    <option value="<%=name%>"><%=name%></option>
                    <%}%>
                  <% }
                } %>
              </select>
            </td>
            <td><input type="button" value="<%=getProperty("sw_view_data")%>" onclick="viewDataButton()"></td>
          </tr>
          <tr>
            <td><%=getProperty("sw_query")%></td><td><input type="text" name="query" value="" size=50></td>
            <td><input type="button" value="<%=getProperty("sw_execute_query")%>" onclick="executeQueryButton()"></td>
          </tr>
        </form>
      </table>
      <%  if (queryResultSet != null) { %>
          <table>
            <tr>
            <%  for (int i = 1; i <= queryResultSet.getMetaData().getColumnCount(); i++) { %>
                <th><%=queryResultSet.getMetaData().getColumnName(i)%></th>
            <% } %>
            </tr>
          <%  while (queryResultSet.next()) { %>
              <tr>
              <%  for (int i = 1; i <= queryResultSet.getMetaData().getColumnCount(); i++) { %>
                  <td><%=queryResultSet.getString(i)%></td>
              <% } %>
              </tr>
          <% } %>
          </table>
      <% } %>
      <%  if (rowsAffected > -1) { %>
          <table>
            <tr>
                <th>Rows Affected</th>
            </tr>
            <tr>
                <td><%=rowsAffected%></td>
            </tr>
          </table>
      <% } %>
      <font color="red"><%=errorMessage%></font>
      <hr/>
      <%=getProperty("jsp_desc")%>
      <br/>
    </div>
    <div id="footer">
      <center>CData JDBC Driver for QuickBooks 2016 - Copyright (c) 2016 CData Software, Inc. - All rights reserved. - For more information, please visit our website at <a href="http://www.cdata.com/?demopg-RQR16V" target="_blank">www.cdata.com</a></center>
    </div>    
  </body></html>

<SCRIPT LANGUAGE="JavaScript">
  function connectButton() {
    document.form1.buttonPressed.value = "Connect"
    form1.submit()
  }
  function viewDataButton() {
    document.form1.buttonPressed.value = "View data"
    form1.submit()
  }
  function executeQueryButton() {
    document.form1.buttonPressed.value = "Execute query"
    form1.submit()
  }</SCRIPT>
