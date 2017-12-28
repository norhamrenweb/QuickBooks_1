import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Helper {
  public static Properties properties = new Properties();

  private static Connection conn = null;
  private static String tableName = "";
 
  public static String getTableName() {
    return tableName;
  }

  public static void setTableName(String tableName) {
    Helper.tableName = tableName;
  }

  public static List<String> getTables() throws Exception {
    List<String> ret = new ArrayList<String>();
    DatabaseMetaData table_meta = getConnection().getMetaData();
    ResultSet set = table_meta.getTables(null, null, "%", null);
    while (set.next()) {
      ret.add(set.getString("TABLE_NAME"));
    }
    return ret;
  }
  
  public static void showException(java.awt.Component c, Exception ex) {
    StringBuilder sb = new StringBuilder(ex.getMessage());
    int i = 0;
    while ((i = sb.indexOf(" ", i + 60)) != -1) {
      sb.replace(i, i + 1, "\n");
    }
    javax.swing.JOptionPane.showMessageDialog(c,sb.toString(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
  }
  
  public static FillTable getModel(String name) throws Exception {
    setTableName(name);
    Statement st = getConnection().createStatement();
    ResultSet rs = st.executeQuery("SELECT * FROM [" + name + "]");
    return new FillTable(getConnection(), rs);
  }
  
  public static FillTable execute(String tableName, String query) throws Exception {
    setTableName(tableName);
    ResultSet rs;
    Statement st = getConnection().createStatement();
    boolean ret = st.execute(query);
    if (ret) {
      rs = st.getResultSet();
    } else {
      rs = st.executeQuery("SELECT * FROM [" + tableName + "] LIMIT 100");
    }
    return new FillTable(getConnection(), rs);
  }

  public static Connection getConnection() throws Exception {
    if (conn == null) {
      //Class.forName (template.driverName);
      conn = DriverManager.getConnection("jdbc:quickbooks", properties);
    }
    return conn;
  }

  public static void resetConnection() {
    conn = null;
  }
  
}