import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FillTable extends AbstractTableModel {
  private ResultSet resultSet;
  private int rowCount;
  private int columnCount;

  private ArrayList<Object> data = new ArrayList<Object>();
  private boolean hasEmptyRow = false;
  private List<Object> updateRows = new ArrayList<Object>();
  private List<Object> insertRows = new ArrayList<Object>();
  private ArrayList<Integer> updateColumnIndex = new ArrayList<Integer>();
  private Connection conn = null;

  public FillTable(Connection conn, ResultSet resultSet) throws Exception {
    this.conn = conn;
    setRS(resultSet);
    addEmptyRow();
  }

  public void setRS(ResultSet resultSet) throws Exception {
    this.resultSet = resultSet;
    ResultSetMetaData metaData = resultSet.getMetaData();
    rowCount = 0;
    columnCount = metaData.getColumnCount();
    while (resultSet.next()) {
      Object[] row = new Object[columnCount];
      for (int j = 0; j < columnCount; j++) {
        row[j] = resultSet.getObject(j + 1);
      }
      data.add(row);
      rowCount++;
    }
  }

  public int getColumnCount() {
    return columnCount;
  }

  public int getRowCount() {
    return rowCount;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    Object[] row = (Object[]) data.get(rowIndex);
    return row[columnIndex];
  }

  public String getColumnName(int columnIndex) {
    try {
      ResultSetMetaData metaData = resultSet.getMetaData();
      return metaData.getColumnName(columnIndex + 1);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    try {
      return !resultSet.getMetaData().isReadOnly(columnIndex + 1);
    } catch (Exception ex) {
      return false;
    }
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (rowIndex <= data.size() - 1) {
      Object[] row = (Object[]) data.get(rowIndex);
      row[columnIndex] = aValue;
      fireTableCellUpdated(rowIndex, columnIndex);
      if (rowIndex < data.size() - 1) { // Only add to the updateRows if it's not a new row
        if ( !updateRows.contains(row) && !insertRows.contains(row)) {
          updateRows.add(row);
        }
        if(!updateColumnIndex.contains(columnIndex)) {
          updateColumnIndex.add(columnIndex);
        }
      } else if (rowIndex == data.size() - 1) {
        if ( !updateRows.contains(row) &&
        !insertRows.contains(row))insertRows.add(row);
        if (!aValue.equals("")) hasEmptyRow = false;
      }
      if (!hasEmptyRow) {
        addEmptyRow();
      }
    }
  }

  private void addEmptyRow() {
    Object[] row = new Object[columnCount];
    data.add(row);
    rowCount++;
    hasEmptyRow = true;
  }

  public void save() throws Exception {
    PreparedStatement pstmt;
    StringBuilder query_insert = new StringBuilder("INSERT INTO ");   //SQL Syntax: INSERT INTO TableName (Column1, Column2, Column3, Column4) VALUES (?,?,?,?)
    String query_update = "UPDATE ";        //SQL Syntax: UPDATE TableName SET Column1=?,Column2=? where Id =?
    ResultSetMetaData metaData = resultSet.getMetaData();
    String primaryKey = getPrimaryKey().toLowerCase();
    int parameterCount = 0;
    String parameter = "";
    for (Object obj : insertRows) {
      Object[] row = (Object[]) obj;
      boolean isAllNull = true;
      query_insert.append(metaData.getTableName(1)).append("(");
      for (int i = 0; i < columnCount; i++) {
        if(row[i] != null) {
          if (parameterCount == 0) {
            query_insert.append("[");
            parameter += "?";
            parameterCount++;
          } else {
            query_insert.append(",[");
            parameter += ",?";
          }
          query_insert.append(metaData.getColumnName(i + 1)).append("]");
        }
        if(i == columnCount - 1){
          query_insert.append(") VALUES (").append(parameter).append(")");
        }
      }

      pstmt = conn.prepareStatement(query_insert.toString());
      for (int i = 0; i < row.length; i++) {
        if (row[i] != null ) {
          pstmt.setString(parameterCount, row[i].toString());
          parameterCount += 1;
          isAllNull = false;
        }
      }

      if (!isAllNull) {
        pstmt.execute();
      }
    }

    for (Object obj : updateRows) {
      Object[] row = (Object[]) obj;
      ArrayList<Integer> colsToUpdate = new ArrayList<Integer>();
      boolean isAllNull = true;
      int idIndex = -1;

      //Get index of Id field 
      for (int i = 0; i < row.length; i++) {
        if (metaData.getColumnName(i + 1).toLowerCase().equals(primaryKey)) {
          idIndex = i;
          break;
        }
      }

      if (idIndex > -1) {
        colsToUpdate.add(idIndex);
      } 
      else {
        throw new Exception(DataBrowser.getProperty("sw_id_error"));
      }
      
      query_update+=metaData.getTableName(1)+" SET ";
      for (int i=0;i<columnCount;i++){
        if(!getColumnName(i).toLowerCase().equals(primaryKey) && !metaData.isReadOnly(i+1)  && row[i] != null && !row[i].toString().equals("")) { 
          if( updateColumnIndex.contains(i)) {
            query_update+="[" + getColumnName(i)+"]=?,";
            colsToUpdate.add(i);
          }
        }
      }
      query_update = query_update.substring(0, query_update.length() - 1);
      query_update += String.format("WHERE %s = ?", primaryKey);
      pstmt = conn.prepareStatement(query_update);

      for (int i : colsToUpdate) {
        if (metaData.getColumnName(i + 1).toLowerCase().equals(primaryKey)) {
          pstmt.setString(colsToUpdate.size(), row[i].toString());   //set where id=?
        } else if (metaData.isReadOnly(i + 1)) {
          continue;
        } else {
          pstmt.setString(colsToUpdate.indexOf(i), row[i] == null ? "" : row[i].toString());
          isAllNull = false;
        }
      }

      if (!isAllNull) {
        pstmt.execute();
      }
    }
    insertRows = new ArrayList<Object>();
    updateRows = new ArrayList<Object>();
    updateColumnIndex = new ArrayList<Integer>();
  }

  public void delete(int[] selectedRows) throws Exception {
    PreparedStatement pstmt;
    String columnName = getPrimaryKey();
    String query_delete = String.format("DELETE FROM [%s] WHERE %s = ?", resultSet.getMetaData().getTableName(1), columnName);//SQL Syntax: DELETE FROM TableName where Id = ?
    pstmt = conn.prepareStatement(query_delete);
    for (int i : selectedRows) {
      Object[] row = (Object[]) data.get(i);
      if (row[0] != null) {
        pstmt.setString(1, row[0].toString());
        pstmt.execute();
      }
    }
  }

  public void closeRS() {
    try {
      if (this.resultSet != null) this.resultSet.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  private String getPrimaryKey() throws Exception {
    String primaryKeyName = null;
    if(resultSet != null) {
	  String tableName = resultSet.getMetaData().getTableName(1);
      ResultSet res = conn.getMetaData().getPrimaryKeys(null, null, tableName);
      while(res.next()) {
        primaryKeyName = res.getString("column_name");
      }
    }
    if (primaryKeyName == null) primaryKeyName = "id";
    return primaryKeyName;
  }
}