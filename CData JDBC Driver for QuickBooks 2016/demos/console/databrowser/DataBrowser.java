import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class DataBrowser extends ConsoleDemo{
  private static Connection conn = null;
  private static Properties languages = new Properties();
  private static Properties prop = new Properties();
  private static int ROWS_FOR_FORMATTING = 100;

  private static Connection GetConnection() throws Exception {
    return DriverManager.getConnection("jdbc:quickbooks", prop);
  }

  enum QUERY_TYPES {

    PROJECTION, UPDATE, LIST_TABLES, DESCRIBE_TABLE
  }

  public enum TERMS {

    SELECT, INSERT, UPDATE, DELETE, SHOW, DESCRIBE, TABLE, TABLES, SEMICOLON, HELP, QUIT
  }
  public static final HashMap<TERMS, String> TERMS_DICT = new HashMap<TERMS, String>() {
    {
      put(TERMS.SELECT, "SELECT");
      put(TERMS.INSERT, "INSERT");
      put(TERMS.UPDATE, "UPDATE");
      put(TERMS.DELETE, "DELETE");
      put(TERMS.SHOW, "SHOW");
      put(TERMS.DESCRIBE, "DESCRIBE");
      put(TERMS.TABLE, "TABLE");
      put(TERMS.TABLES, "TABLES");
      put(TERMS.SEMICOLON, ";");
      put(TERMS.HELP, "HELP");
      put(TERMS.QUIT, "QUIT");
    }
  };

  public enum ERRORS {

    INVALID, SHOW_EXPECTS_TERM
  }
  public static final HashMap<ERRORS, String> ERRORS_DICT = new HashMap<ERRORS, String>() {
    {
      put(ERRORS.SHOW_EXPECTS_TERM, getProperty("console_term"));
      put(ERRORS.INVALID, getProperty("console_error"));
    }
  };
  
  public static String getProperty(String input) {
    String lang = languages.getProperty(input);
    try {
      return new String(lang.getBytes("ISO-8859-1"), "UTF-8");
    } catch (Exception e) {
      return lang;
    }
  }

  private static void ResultToPrint(List<List<String>> resultAsList, List<Integer> maxLengthByColumn, int rowToPrint) {
    int resultAsListCount = resultAsList.size();
    int columnCount = maxLengthByColumn.size();
    String row = "";
    if (rowToPrint == 0 || rowToPrint == 1 || rowToPrint == resultAsListCount) {
      for (int j = 0; j < columnCount; j++) {
        row += "+-";
        int columnLength = maxLengthByColumn.get(j);
        for (int k = 0; k < columnLength; k++) {
          row += "-";
        }
        row += (j == columnCount - 1) ? "-+" : "-";
      }
      System.out.println(row);
    }
    if (rowToPrint < resultAsListCount){
      row = "";
      for (int j = 0; j < columnCount; j++) {
        int columnLength = maxLengthByColumn.get(j);
        String columnName = resultAsList.get(rowToPrint).get(j);
          if (columnName.length() > columnLength)
            columnName = columnName.substring(0, columnLength-2) + "..";
        row += "| " + columnName;
        int spaceToFill = columnLength - columnName.length();
        for (int k = 0; k < spaceToFill; k++) {
          row += " ";
        }
        row += (j == columnCount - 1) ? " |" : " ";
      }
      System.out.println(row);
    }
  }

  private static void AddRow(List<List<String>> result, List<String> row, List<Integer> maxLengthByColumn) {
    //format table based on first 100 rows
    if (result.size() < ROWS_FOR_FORMATTING){
	    for (int j = 0; j < row.size(); j++) {
	      int currentCellLength = row.get(j) == null ? 0 : row.get(j).length();
	      if (maxLengthByColumn.get(j) < currentCellLength) {
	        maxLengthByColumn.set(j, currentCellLength);
	      }
	    }
	  }
    result.add(row);
  }

  private static void RunUpdateQuery(Statement stat, String query) throws Exception {
    stat.executeUpdate(query);
    int updateCount = stat.getUpdateCount();
    System.out.println(updateCount + getProperty("console_rows_affect"));
  }

  private static int RunProjectionQuery(Statement stat, String query,
          List<List<String>> resultAsList, List<Integer> maxLengthByColumn) throws Exception {
    stat.execute(query);
    ResultSet rs = stat.getResultSet();
    ResultSetMetaData metadata = rs.getMetaData();
    int columnCount = metadata.getColumnCount();
    List<String> columnNames = new ArrayList<String>();
    for (int i = 1; i <= columnCount; i++) {
      columnNames.add(metadata.getColumnName(i));
      maxLengthByColumn.add(0);
    }

    AddRow(resultAsList, columnNames, maxLengthByColumn);

    int rowsPrinted = 0;
    while (rs.next()) {
      ArrayList<String> rowAsList = new ArrayList<String>(columnCount);
      for (int i = 1; i <= columnCount; i++) {
        rowAsList.add(rs.getString(i) == null ? "NULL" : rs.getString(i));
      }

      AddRow(resultAsList, rowAsList, maxLengthByColumn);
      
      if (resultAsList.size() > ROWS_FOR_FORMATTING){
        ResultToPrint(resultAsList, maxLengthByColumn, rowsPrinted);      
        rowsPrinted++;
      }
    }
    return rowsPrinted;
  }

  private static void RunTableListingQuery(Connection conn, Statement stat, String query,
          List<List<String>> resultAsList, List<Integer> maxLengthByColumn) throws Exception {
    String columnName = "Tables";
    List<String> columnNames = new ArrayList<String>();
    columnNames.add(columnName);
    int columnCount = columnNames.size();
    resultAsList.add(columnNames);
    maxLengthByColumn.add(columnName.length());

    DatabaseMetaData md = conn.getMetaData();
    ResultSet rs = md.getTables(null, null, "%", null);
    while (rs.next()) {
      ArrayList<String> rowAsList = new ArrayList<String>(columnCount);
      rowAsList.add(rs.getString(3));

      AddRow(resultAsList, rowAsList, maxLengthByColumn);
    }
  }

  private static int RunTableDescribeQuery(Connection conn, Statement stat, String query,
          List<List<String>> resultAsList, List<Integer> maxLengthByColumn) throws Exception {
    String[] columns = new String[]{"Column", "Type", "Null", "Key", "Default", "Extra"};
    int columnCount = columns.length;
    List<String> columnNames = new ArrayList<String>(columnCount);
    for (int i = 0; i < columnCount; i++) {
      columnNames.add(columns[i]);
      maxLengthByColumn.add(0);
    }

    AddRow(resultAsList, columnNames, maxLengthByColumn);

    ResultSet rs = null;
    rs = stat.executeQuery("SELECT * FROM " + query);
    ResultSetMetaData rsmd = rs.getMetaData();

    List<String> autoIncrementFields = new ArrayList<String>();
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      if (rsmd.isAutoIncrement(i)) {
        autoIncrementFields.add(rsmd.getColumnName(i));
      }
    }

    DatabaseMetaData meta = conn.getMetaData();

    rs = meta.getPrimaryKeys(null, null, query);
    List<String> primaryKeys = new ArrayList<String>();
    while (rs.next()) {
      primaryKeys.add(rs.getString("COLUMN_NAME"));
    }
    
    int rowsPrinted = 0;
    rs = meta.getColumns(null, null, query, null);
    while (rs.next()) {
      ArrayList<String> rowAsList = new ArrayList<String>(columnCount);
      String columnName = rs.getString("COLUMN_NAME");
      rowAsList.add(columnName);
      rowAsList.add(rs.getString("TYPE_NAME") + "(" + rs.getInt("COLUMN_SIZE") + ")");
      rowAsList.add(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable ? "YES" : "NO");
      rowAsList.add(primaryKeys.contains(columnName) ? "PRI" : "");
      rowAsList.add(rs.getString("COLUMN_DEF") == null ? "NULL" : rs.getString("COLUMN_DEF"));
      rowAsList.add(autoIncrementFields.contains(columnName) ? "autoincrement" : "");
      //int position = rsColumns.getInt("ORDINAL_POSITION");
      
      AddRow(resultAsList, rowAsList, maxLengthByColumn);

      if (resultAsList.size() > ROWS_FOR_FORMATTING){
        ResultToPrint(resultAsList, maxLengthByColumn, rowsPrinted);
        rowsPrinted++;
      }
    }
    return rowsPrinted;
  }

  public static void RunQuery(QUERY_TYPES queryType, String query) throws Exception {
    try {
      conn = GetConnection();
      Statement stat = conn.createStatement();
      List<List<String>> resultAsList = new ArrayList<List<String>>();
      List<Integer> maxLengthByColumn = new ArrayList<Integer>();

      int rowsPrinted = 0;

      switch (queryType) {
        case PROJECTION:
          rowsPrinted = RunProjectionQuery(stat, query, resultAsList, maxLengthByColumn);
          break;
        case UPDATE:
          RunUpdateQuery(stat, query);
          break;
        case LIST_TABLES:
          RunTableListingQuery(conn, stat, query, resultAsList, maxLengthByColumn);
          break;
        case DESCRIBE_TABLE:
          rowsPrinted = RunTableDescribeQuery(conn, stat, query, resultAsList, maxLengthByColumn);
          break;
      }

      stat.close();
      conn.close();

      if (queryType != QUERY_TYPES.UPDATE) {
        while (rowsPrinted <= resultAsList.size()){
          ResultToPrint(resultAsList, maxLengthByColumn, rowsPrinted);
          rowsPrinted++;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void PrintHelp() {
    System.out.println(getProperty("console_commands"));
    System.out.println(getProperty("console_expression"));
    System.out.println("DESCRIBE <tablename>");
    System.out.println("SHOW TABLES");
    System.out.println("QUIT");
    System.out.println("HELP");
  }

  public static void main(String[] args) throws Exception {
    Class.forName("cdata.jdbc.quickbooks.QuickBooksDriver");
    
    FileInputStream stream = new FileInputStream("./databrowser.properties");
    languages.load(stream);
    
    HashMap<ERRORS, String> ERRORS_DICT = new HashMap<ERRORS, String>();
    ERRORS_DICT.put(ERRORS.SHOW_EXPECTS_TERM, getProperty("console_term"));
    ERRORS_DICT.put(ERRORS.INVALID, getProperty("console_error"));
    
    String welcome_console = getProperty("welcome");
    System.out.println(welcome_console);
    System.out.println(getProperty("tips"));
    

    System.out.println();    
    
    PrintHelp();

    while (true) {
      System.out.print(">");
      String token = input();
      String tokenToUpper = token.toUpperCase();
      String semicolon = TERMS_DICT.get(TERMS.SEMICOLON);

      if (tokenToUpper.contains(TERMS_DICT.get(TERMS.SELECT))
              || tokenToUpper.contains(TERMS_DICT.get(TERMS.INSERT))
              || tokenToUpper.contains(TERMS_DICT.get(TERMS.UPDATE))
              || tokenToUpper.contains(TERMS_DICT.get(TERMS.DELETE))
              || tokenToUpper.contains(TERMS_DICT.get(TERMS.SHOW))
              || tokenToUpper.replace(semicolon, "").equals(TERMS_DICT.get(TERMS.HELP))
              || tokenToUpper.replace(semicolon, "").equals(TERMS_DICT.get(TERMS.QUIT))
              || tokenToUpper.contains(TERMS_DICT.get(TERMS.DESCRIBE))) {
        
        token = token.replace(semicolon, "");

        if (tokenToUpper.contains(TERMS_DICT.get(TERMS.SHOW))) {
          if (token.replace(semicolon, "").toUpperCase().contains(TERMS_DICT.get(TERMS.TABLES))) {
            RunQuery(QUERY_TYPES.LIST_TABLES, null);
          } else {
            System.out.println(ERRORS_DICT.get(ERRORS.SHOW_EXPECTS_TERM));
          }
        } else if (tokenToUpper.contains(TERMS_DICT.get(TERMS.DESCRIBE))) {
          RunQuery(QUERY_TYPES.DESCRIBE_TABLE, token.substring(token.indexOf(" ")));
        } else if (tokenToUpper.replace(semicolon, "").equals(TERMS_DICT.get(TERMS.HELP))) {
          PrintHelp();
        } else if (tokenToUpper.replace(semicolon, "").equals(TERMS_DICT.get(TERMS.QUIT))) {
          break;
        } else {
          if (tokenToUpper.contains(TERMS_DICT.get(TERMS.SELECT))) {
            RunQuery(QUERY_TYPES.PROJECTION, token);
          } else {
            RunQuery(QUERY_TYPES.UPDATE, token);
          }
        }
      } else {
        System.out.println(ERRORS_DICT.get(ERRORS.INVALID));
      }
    }

    System.exit(0);
  }
}

class ConsoleDemo {
  private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

  static String input() {
    try {
      return bf.readLine();
    } catch (IOException e) {
      System.out.print("error");
      System.out.println(": " + e.getMessage());
      return "";
    }
  }
  
  static char read() {
    return input().charAt(0);
  }

  static String prompt(String label) {
    return prompt(label, ":");
  }
  
  static String prompt(String label, String punctuation) {
    System.out.print(label + punctuation + " ");
    return input();
  }

  static String prompt(String label, String punctuation, String defaultVal) {
    System.out.print(label + " [" + defaultVal + "] " + punctuation + " ");
	  String response = input();
	  if(response.equals("")) return defaultVal;
	  else return response;
  }

  static char ask(String label) {
    return ask(label, "?");
  }
  
  static char ask(String label, String punctuation) {
    return ask(label, punctuation, "(y/n)");
  }
  
  static char ask(String label, String punctuation, String answers) {
    System.out.print(label + punctuation + " " + answers + " ");
    return Character.toLowerCase(read());
  }

  static void displayError(Exception e) {
    System.out.print("error");
      
    System.out.println(": " + e.getMessage());
    e.printStackTrace();
  }
}