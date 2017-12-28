<%@ page import="java.io.File, java.io.FileInputStream" %>

<%!
String escapeCode(StringBuffer code) {
  for (int i = 0; i < code.length(); i++) {
    if (code.charAt(i) == '<') {
      if (code.charAt(i + 1) == '%') {
        code.replace(i, i + 2, "<FONT COLOR=blue>&lt;%");
        i += "<FONT COLOR=blue>&lt;%".length() - 2;
      }
      else code.replace(i, i + 1, "&lt;");
    }
    if (code.charAt(i) == '>') {
      if (code.charAt(i - 1) == '%') {
        code.replace(i - 1, i + 1, "%&gt;</FONT>");
        i += "%&gt;</FONT>".length() - 2;
      }
      else code.replace(i, i + 1, "&gt;");
    }
  }
  return code.toString();
}
%>

<%
String file = request.getQueryString();
if (file == null) file = "seecode.jsp";
%>

<center>
  <table width="90%">
    <tr>
    <td>
<%
    System.out.println("Opening file : "+request.getRealPath(file));
    File source = new File(request.getRealPath(file));
    FileInputStream f = new FileInputStream(source);
    byte[] data = new byte[(int) source.length()];
    f.read(data);
    String code = escapeCode(new StringBuffer(new String(data)));
    out.write("<pre>" + code + "</pre>");
    f.close();
%>
    </td>
    </tr>
  </table>
</center>
