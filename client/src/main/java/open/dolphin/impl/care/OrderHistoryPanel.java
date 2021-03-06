package open.dolphin.impl.care;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.*;
import open.dolphin.table.ListTableModel;
import open.dolphin.table.StripeTableCellRenderer;
import open.dolphin.util.DolphinUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * オーダ履歴を表示するパネルクラス。 表示するオーダと抽出期間は PropertyChange で通知される。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class OrderHistoryPanel extends JPanel implements PropertyChangeListener {
    
    private ListTableModel<ModuleModel> tModel;
    
    private JTable table;
    
    private JLabel contents;
    
    private String pid;
    
    private Dimension contentSize = new Dimension(240, 300);
    
    /** Creates new OrderHistoryPanel */
    public OrderHistoryPanel() {
        
        super(new BorderLayout(5, 0));
        
        String columnLine = ClientContext.getMyBundle(OrderHistoryPanel.class).getString("columnNames");
        String[] columnNames = columnLine.split(",");
        int startNumRows = 0;
        
        // オーダの履歴(確定日|スタンプ名)を表示する TableModel
        // 各行は ModuleModel
        tModel = new ListTableModel<ModuleModel>(columnNames, startNumRows) {
            
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
            
            @Override
            public Object getValueAt(int row, int col) {
                
                ModuleModel module = getObject(row);
                if (module == null) {
                    return null;
                }
                ModuleInfoBean info = module.getModuleInfoBean();
                String ret = null;
                
                switch (col) {
                    
                    case 0:
                        ret = ModelUtils.getDateAsString(module.getStarted());
                        break;
                        
                    case 1:
                        ret = info.getStampName();
                        break;
                }
                
                return ret;
            }
        };
        
        table = new JTable(tModel);
        StripeTableCellRenderer rederer = new StripeTableCellRenderer();
        rederer.setTable(table);
        rederer.setDefaultRenderer();
        table.setRowHeight(ClientContext.getHigherRowHeight());
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // 行クリックで内容を表示する
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        ListSelectionModel m = table.getSelectionModel();
        m.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = table.getSelectedRow();
                    displayOrder(index);
                }
            }
        });
        setColumnWidth(new int[] { 50, 240 });

        //-----------------------------------------------
        // Copy 機能を実装する
        //-----------------------------------------------
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, DolphinUtils.getMenuShortcutKeyMaskEx());
        String actionText = ClientContext.getMyBundle(OrderHistoryPanel.class).getString("actionText.copy");
        final AbstractAction copyAction = new AbstractAction(actionText) {

            @Override
            public void actionPerformed(ActionEvent ae) {
                copyRow();
            }
        };
        table.getInputMap().put(copy, "Copy");
        table.getActionMap().put("Copy", copyAction);

        // 右クリックコピー
        table.addMouseListener(new MouseAdapter() {

            private void mabeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    ModuleModel m = tModel.getObject(row);
                    if (m == null) {
                        return;
                    }
                    JPopupMenu pop = new JPopupMenu();
                    JMenuItem item2 = new JMenuItem(copyAction);
                    pop.add(item2);
                    pop.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mabeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mabeShowPopup(e);
            }
        });
        
        JScrollPane scroller = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroller, BorderLayout.CENTER);
        
        // 内容表示用 TextArea
        contents = new JLabel();
        contents.setVerticalAlignment(SwingConstants.TOP);
        contents.setBackground(Color.white);
        contents.addMouseListener(new MouseAdapter() {

            private void mabeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    if (contents.getText().trim().equals("")) {
                        return;
                    }
                    JPopupMenu pop = new JPopupMenu();
                    String menuText = ClientContext.getMyBundle(OrderHistoryPanel.class).getString("actionText.copy");
                    JMenuItem item2 = new JMenuItem(menuText);
                    pop.add(item2);
                    item2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int row = table.getSelectedRow();
                            ModuleModel m = tModel.getObject(row);
                            if (m != null && (m.getModel() instanceof BundleDolphin)) {
                                BundleDolphin bd = (BundleDolphin)m.getModel();
                                StringSelection stsel = new StringSelection(bd.toString());
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);
                            }
                        }
                    });
                    pop.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mabeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mabeShowPopup(e);
            }
        });
        JScrollPane cs = new JScrollPane(contents,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cs.setPreferredSize(contentSize);
        cs.setMaximumSize(contentSize);
        add(cs, BorderLayout.EAST);
    }
    
    public void setColumnWidth(int[] columnWidth) {
        int len = columnWidth.length;
        for (int i = 0; i < len; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
        }
    }
    
    public String getPid() {
        return pid;
    }
    
    public void setPid(String val) {
        pid = val;
    }
    
    public void setModuleList(List allModules) {
        
        tModel.clear();
        
        if (allModules == null || allModules.isEmpty()) {
            return;
        }
        
        int size = allModules.size();
        List<ModuleModel> list = new ArrayList<>();
        
        for (int i = 0; i < size; i++) {
            List<ModuleModel> l = (List<ModuleModel>) allModules.get(i);
            if (l != null) {
                for (int j = 0; j < l.size(); j++) {
                    list.add(l.get(j));
                }
            }
        }
        
        tModel.setDataProvider(list);
    }
    
    /**
     * カレンダーの日が選択されたときに通知を受け、テーブルで日付が一致するオーダの行を選択する。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        
        String prop = e.getPropertyName();
        
        if (prop.equals(CareMapDocument.SELECTED_DATE_PROP)) {
            
            String date = (String) e.getNewValue();
            findDate(date);
        }
    }

    /**
     * 選択されている行をコピーする。
     */
    public void copyRow() {
        StringBuilder sb = new StringBuilder();
        int numRows = table.getSelectedRowCount();
        int[] rowsSelected = table.getSelectedRows();
        int numColumns = table.getColumnCount();

        for (int i = 0; i < numRows; i++) {

            StringBuilder s = new StringBuilder();
            for (int col = 0; col < numColumns; col++) {
                Object o = table.getValueAt(rowsSelected[i], col);
                if (o!=null) {
                    s.append(o.toString());
                }
                s.append(",");
            }
            if (s.length()>0) {
                s.setLength(s.length()-1);
            }
            sb.append(s.toString()).append("\n");

        }
        if (sb.length() > 0) {
            StringSelection stsel = new StringSelection(sb.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);
        }
    }
    
    /**
     * オーダ履歴のテーブル行がクリックされたとき、データモデルの ModuleModel を表示する。
     */
    private void displayOrder(int index) {
        
        contents.setText("");
        
        ModuleModel stamp = (ModuleModel) tModel.getObject(index);
        if (stamp == null) {
            return;
        }
        
        IInfoModel model = stamp.getModel();
        
        try {
            VelocityContext context = ClientContext.getVelocityContext();
            context.put("model", model);
            context.put("stampName", stamp.getModuleInfoBean().getStampName());
            
            // このスタンプのテンプレートファイルを得る
            String templateFile = stamp.getModel().getClass().getName() + ".vm";
            
            // Merge する
            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);
            InputStream instream = ClientContext
                    .getTemplateAsStream(templateFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    instream, "UTF-8"));
            Velocity.evaluate(context, bw, "stmpHolder", reader);
            bw.flush();
            bw.close();
            reader.close();
            contents.setText(sw.toString());
            
        } catch (ParseErrorException | MethodInvocationException | ResourceNotFoundException | IOException e) {
            System.out.println("Execption while setting the stamp text: "
                    + e.toString());
            e.printStackTrace(System.err);
        }
    }
    
    private void findDate(String date) {
        int size = tModel.getObjectCount();
        for (int i = 0; i < size; i++) {
            String rowDate = (String) tModel.getValueAt(i, 0);
            if (rowDate.equals(date)) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }
}