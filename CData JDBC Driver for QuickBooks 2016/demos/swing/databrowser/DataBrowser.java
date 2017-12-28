import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;

public class DataBrowser extends JFrame {
  private JPanel panel;
  private JPanel pnlContent;
  private JPanel pnlControl;
  private JPanel pnlQuery;
  private JPanel pnlButtons;

  private JLabel lblTableName;
  private JLabel lblQuery;
  private JLabel lblIntroText;

  private JTextField txtQuery;

  private JButton btnConnect;
  private JButton btnSave;
  private JButton btnDelete;
  private JButton btnExecute;

  private JScrollPane scrollPane;
  private JComboBox cmbTableNames;
  private JTable table;


  private FillTable fillTable;

  private List<String> properties;
  private List<JTextField> listProperties = new ArrayList<JTextField>();
  
  private static Properties languages = new Properties();
  
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          FileInputStream stream = new FileInputStream("./databrowser.properties");
          languages.load(stream);
          Class.forName("cdata.jdbc.quickbooks.QuickBooksDriver");
          DataBrowser frame = new DataBrowser();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public static String getProperty(String input) {
    String lang = languages.getProperty(input);
    try {
      return new String(lang.getBytes("ISO-8859-1"), "UTF-8");
    } catch (Exception e) {
      return lang;
    }
  }

  /**
   * Create the frame.
   * @throws Exception 
   */
  public DataBrowser() throws Exception {
    setTitle("CData JDBC Driver for QuickBooks");

    properties = new ArrayList<String>();

    int length = properties.size();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 640, 750);
    setMinimumSize(new Dimension(640,600));
    pnlContent = new JPanel();
    pnlContent.setBorder(new EmptyBorder(5, 5, 5, 5));
    pnlContent.setLayout(new BorderLayout(0, 0));
    setContentPane(pnlContent);
    
    panel = new JPanel();
    panel.setBorder(new LineBorder(SystemColor.activeCaption));
    pnlContent.add(panel, BorderLayout.NORTH);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{117, 181, 131};
    gbl_panel.rowHeights = new int[]{0, 29, 25, 25, 28, 0, 0};
    gbl_panel.columnWeights = new double[]{0.0, 1.0, 1.0};
    gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    panel.setLayout(gbl_panel);
    
    lblIntroText = new JLabel(getProperty("sw_title"));

    GridBagConstraints gbc_lblIntroText = new GridBagConstraints();
    gbc_lblIntroText.fill = GridBagConstraints.BOTH;
    gbc_lblIntroText.gridwidth = 4;
    gbc_lblIntroText.insets = new Insets(0, 5, 5, 5);
    gbc_lblIntroText.gridx = 0;
    gbc_lblIntroText.gridy = 0;
    panel.add(lblIntroText, gbc_lblIntroText);

    for (int i=0;i<properties.size();i++){  
      JLabel label = new JLabel(properties.get(i) + ":  ");
      GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
      gbc_lblNewLabel.anchor = GridBagConstraints.NORTHEAST;
      gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
      gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
      gbc_lblNewLabel.gridx = 0;
      gbc_lblNewLabel.gridy = i+1;
      panel.add(label, gbc_lblNewLabel);

      JTextField txt = new JTextField();
      if (properties.get(i).toUpperCase().contains("PASSWORD"))
        txt = new JPasswordField();
        
      listProperties.add(txt);
      GridBagConstraints gbc_txtAccess = new GridBagConstraints();
      gbc_txtAccess.gridwidth = 2;
      gbc_txtAccess.fill = GridBagConstraints.HORIZONTAL;
      gbc_txtAccess.insets = new Insets(0, 0, 5, 5);
      gbc_txtAccess.gridx = 1;
      gbc_txtAccess.gridy = i+1;
      panel.add(txt, gbc_txtAccess);
      txt.setColumns(10);
    }


    btnConnect = new JButton(getProperty("sw_connect"));
    btnConnect.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        pnlContent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
          Helper.resetConnection();
          Helper.properties = new Properties();
          for(int i=0;i<listProperties.size();i++){
            if (listProperties.get(i).getText().equals(""))
              continue;
            Helper.properties.put(properties.get(i), listProperties.get(i).getText());
          }
    
          List<String> tableNames = Helper.getTables();   
          
          if (tableNames!=null){
            enableComponents();
            cmbTableNames.removeAllItems();
            for(String name:tableNames){
              cmbTableNames.addItem(name);
            }
          }
        } catch (Exception exc) {
          Helper.showException(pnlContent, exc);
        } finally {
          pnlContent.setCursor(Cursor.getDefaultCursor());
        }
      }

      private void enableComponents() {
        cmbTableNames.setEnabled(true);
        txtQuery.setEnabled(true);
        btnExecute.setEnabled(true);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(true);
      }
    });
    
    GridBagConstraints gbc_btnConnect = new GridBagConstraints();
    gbc_btnConnect.anchor = GridBagConstraints.EAST;
    gbc_btnConnect.fill = GridBagConstraints.VERTICAL;
    gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
    gbc_btnConnect.gridx = 2;
    gbc_btnConnect.gridy = length+1;
    panel.add(btnConnect, gbc_btnConnect);
    
    pnlQuery = new JPanel();
    pnlQuery.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, SystemColor.activeCaption));
    
    GridBagLayout gbl_pnlQuery = new GridBagLayout();
    gbl_pnlQuery.columnWidths = new int[]{117, 181};
    gbl_pnlQuery.rowHeights = new int[]{0, 0, 0};
    gbl_pnlQuery.columnWeights = new double[]{0.0, 1.0};
    gbl_pnlQuery.rowWeights = new double[]{0.0, 0.0, 0.0};
    pnlQuery.setLayout(gbl_pnlQuery);
    
    GridBagConstraints gbc_pnlQuery = new GridBagConstraints();
    gbc_pnlQuery.gridwidth = 4;
    gbc_pnlQuery.fill = GridBagConstraints.HORIZONTAL;
    gbc_pnlQuery.gridx = 0;
    gbc_pnlQuery.gridy = length+3;
    panel.add(pnlQuery, gbc_pnlQuery);
    
    lblTableName = new JLabel(getProperty("sw_table_name"));
    GridBagConstraints gbc_lblTableName = new GridBagConstraints();
    gbc_lblTableName.anchor = GridBagConstraints.NORTHEAST;
    gbc_lblTableName.fill = GridBagConstraints.VERTICAL;
    gbc_lblTableName.insets = new Insets(10, 0, 5, 5);
    gbc_lblTableName.gridx = 0;
    gbc_lblTableName.gridy = length+2;
    pnlQuery.add(lblTableName, gbc_lblTableName);
    
    cmbTableNames = new JComboBox();
    cmbTableNames.setEnabled(false);
    cmbTableNames.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        pnlContent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
          if (!cmbTableNames.isEnabled()) return;
          String selectedTable = (String) cmbTableNames.getSelectedItem();
          if (selectedTable != null)
            txtQuery.setText("SELECT * FROM " + "[" + selectedTable + "]" + " LIMIT 20");
        } catch (Exception exc) {
          Helper.showException(pnlContent, exc);
        } finally {
          pnlContent.setCursor(Cursor.getDefaultCursor());
        }
      }
    });
    GridBagConstraints gbc_comboBox = new GridBagConstraints();
    gbc_comboBox.anchor = GridBagConstraints.WEST;
    gbc_comboBox.insets = new Insets(5, 0, 5, 5);
    gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_comboBox.gridx = 1;
    gbc_comboBox.gridy = length+2;
    pnlQuery.add(cmbTableNames, gbc_comboBox);
    
    lblQuery = new JLabel(getProperty("sw_query"));
    GridBagConstraints gbc_lblQuery = new GridBagConstraints();
    gbc_lblQuery.anchor = GridBagConstraints.NORTHEAST;
    gbc_lblQuery.fill = GridBagConstraints.VERTICAL;
    gbc_lblQuery.insets = new Insets(0, 0, 5, 5);
    gbc_lblQuery.gridx = 0;
    gbc_lblQuery.gridy = length+3;
    pnlQuery.add(lblQuery, gbc_lblQuery);
    
    txtQuery = new JTextField();
    txtQuery.setEnabled(false);
    GridBagConstraints gbc_txtQuery = new GridBagConstraints();
    gbc_txtQuery.gridwidth = 2;
    gbc_txtQuery.insets = new Insets(0, 0, 5, 5);
    gbc_txtQuery.fill = GridBagConstraints.BOTH;
    gbc_txtQuery.gridx = 1;
    gbc_txtQuery.gridy = length+3;
    pnlQuery.add(txtQuery, gbc_txtQuery);
    txtQuery.setColumns(10);
    
    pnlButtons = new JPanel();
    GridBagLayout gbl_pnlButtons = new GridBagLayout();
    gbl_pnlButtons.columnWidths = new int[]{0};
    gbl_pnlButtons.rowHeights = new int[]{0};
    gbl_pnlButtons.columnWeights = new double[]{1.0};
    gbl_pnlButtons.rowWeights = new double[]{0.0};
    pnlButtons.setLayout(gbl_pnlButtons);
    
    GridBagConstraints gbc_pnlButtons = new GridBagConstraints();
    gbc_pnlButtons.anchor = GridBagConstraints.EAST;
    gbc_pnlButtons.gridx = 1;
    gbc_pnlButtons.gridy = length+4;
    pnlQuery.add(pnlButtons, gbc_pnlButtons);
    
    btnDelete = new JButton(getProperty("sw_delete"));
    btnDelete.setEnabled(false);
    pnlButtons.add(btnDelete, new GridBagConstraints(0, 0, 1, 0, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    
    btnSave = new JButton(getProperty("sw_save"));
    btnSave.setEnabled(false);
    pnlButtons.add(btnSave, new GridBagConstraints(0, 0, 0, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 10 + btnDelete.getPreferredSize().width), 0, 0));
    
    btnExecute = new JButton(getProperty("sw_execute"));
    btnExecute.setEnabled(false);
    pnlButtons.add(btnExecute, new GridBagConstraints(0, 0, 0, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 5, 15 + btnDelete.getPreferredSize().width + btnSave.getPreferredSize().width), 0, 0));
    
    btnDelete.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        pnlContent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
          if (!arg0.getComponent().isEnabled()) return;
          String name = Helper.getTableName();
          fillTable.delete(table.getSelectedRows());
          fillTable = Helper.execute(name, txtQuery.getText());
          table = new JTable(fillTable);
          table.setSurrendersFocusOnKeystroke(true);
          table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
          scrollPane.setViewportView(table);
        } catch (Exception exc) {
          Helper.showException(pnlContent, exc);
        } finally {
          pnlContent.setCursor(Cursor.getDefaultCursor());
        }
      }
    });
    btnSave.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        pnlContent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
          if (!arg0.getComponent().isEnabled()) return;
          String name = Helper.getTableName();
          fillTable.save();
          fillTable = Helper.execute(name, txtQuery.getText());
          table = new JTable(fillTable);
          table.setSurrendersFocusOnKeystroke(true);
          table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
          scrollPane.setViewportView(table);
        } catch (Exception exc) {
          Helper.showException(pnlContent, exc);
        } finally {
          pnlContent.setCursor(Cursor.getDefaultCursor());
        }
      }
    });
    btnExecute.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        pnlContent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
          if (!btnExecute.isEnabled()) return;
          fillTable = Helper.execute((String) cmbTableNames.getSelectedItem(), txtQuery.getText());
          table = new JTable(fillTable);
          table.setSurrendersFocusOnKeystroke(true);
          table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
          scrollPane.setViewportView(table);
        } catch (Exception exc) {
          Helper.showException(pnlContent, exc);
        } finally {
          pnlContent.setCursor(Cursor.getDefaultCursor());
        }
      }
    });
    
    scrollPane = new JScrollPane();
    scrollPane.setViewportBorder(new LineBorder(SystemColor.activeCaption));
    pnlContent.add(scrollPane);
    
    pnlControl = new JPanel();
    scrollPane.setColumnHeaderView(pnlControl);
  }
}