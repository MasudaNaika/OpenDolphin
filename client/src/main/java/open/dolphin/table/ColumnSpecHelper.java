package open.dolphin.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import open.dolphin.project.Project;

/**
 * ColumnSpecHelper
 * @author masuda, Masuda Naika
 */
public class ColumnSpecHelper {
    
    private static final char CAMMA = ',';
    
    private JTable table;
    private final String specName;
    private final String[] columnNames;
    private final String[] propNames;
    private final Class[] columnClasses;
    private final int[] columnWidth;
    
    // カラム仕様リスト
    private List<ColumnSpec> columnSpecs;
    
    
    public ColumnSpecHelper(String specName, 
            String[] columnNames, String[] propNames, Class[] columnClasses, int[] columnWidth) {
        
        this.specName = specName;
        this.columnNames = columnNames;
        this.propNames = propNames;
        this.columnClasses = columnClasses;
        this.columnWidth = columnWidth;
    }
    
    public void setTable(JTable table) {
        this.table = table;
    }
    
    public List<ColumnSpec> getColumnSpecs() {
        return columnSpecs;
    }
    
    public String[] getTableModelColumnNames() {
        int len = columnSpecs.size();
        String[] names = new String[len];
        for (int i = 0; i < len; i++) {
            ColumnSpec cp = columnSpecs.get(i);
            names[i] = cp.getName();
        }
        return names;
    }
    
    public String[] getTableModelColumnMethods() {
        int len = columnSpecs.size();
        String[] methods = new String[len];
        for (int i = 0; i < len; i++) {
            ColumnSpec cp = columnSpecs.get(i);
            methods[i] = cp.getMethod();
        }
        return methods;
    }
    
    public Class[] getTableModelColumnClasses() {
        int len = columnSpecs.size();
        Class[] classes = new Class[len];
        for (int i = 0; i < len; i++) {
            ColumnSpec cp = columnSpecs.get(i);
            try {
                classes[i] = Class.forName(cp.getCls());
            } catch (ClassNotFoundException ex) {
            }
        }
        return classes;
    }
    
    public void updateColumnWidth() {
        SwingUtilities.invokeLater(() -> {
            updateColumnWidth(table);
        });
    }

    public void updateColumnWidth(JTable tbl) {

        for (int i = 0; i < columnSpecs.size(); ++i) {
            ColumnSpec cs = columnSpecs.get(i);
            int width = cs.getWidth();
            TableColumn tc = tbl.getColumnModel().getColumn(i);
            if (width != 0) {
                tc.setMaxWidth(Integer.MAX_VALUE);
                tc.setPreferredWidth(width);
                tc.setWidth(width);
                tc.setResizable(true);
            } else {
                tc.setMinWidth(0);
                tc.setMaxWidth(0);
                tc.setPreferredWidth(0);
                tc.setWidth(0);
                tc.setResizable(false);
            }
        }
        tbl.revalidate();
        tbl.repaint();

    }
    
    public void setColumnVisible(String propName, boolean visible) {

        if (table == null) {
            return;
        }

//        SwingUtilities.invokeLater(() -> {
            int col = getColumnPosition(propName);

            TableColumn tc = table.getColumnModel().getColumn(col);
            if (visible) {
                int width = columnSpecs.get(col).getWidth();
                tc.setMaxWidth(Integer.MAX_VALUE);
                tc.setPreferredWidth(width);
                tc.setWidth(width);
                tc.setResizable(true);
            } else {
                tc.setMinWidth(0);
                tc.setMaxWidth(0);
                tc.setPreferredWidth(0);
                tc.setWidth(0);
                tc.setResizable(false);
            }
            table.revalidate();
            table.repaint();

//        });

    }
    
    public int getColumnPosition(String propName) {

        for (int i = 0; i < columnSpecs.size(); ++i) {
            String name = columnSpecs.get(i).getMethod();
            if (name.equals(propName)) {
                return i;
            }
        }
        return -1;
    }
    
    public int getColumnPositionEndsWith(String propName) {

        for (int i = 0; i < columnSpecs.size(); ++i) {
            String name = columnSpecs.get(i).getMethod();
            if (name.endsWith(propName)) {
                return i;
            }
        }
        return -1;
    }
    
    public int getColumnPositionStartWith(String propName) {

        for (int i = 0; i < columnSpecs.size(); ++i) {
            String name = columnSpecs.get(i).getMethod();
            if (name.startsWith(propName)) {
                return i;
            }
        }
        return -1;
    }
    
    public void connect() {
        
        // Tableのカラム変更関連イベント
        table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent tcme) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent tcme) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent tcme) {
                int from = tcme.getFromIndex();
                int to = tcme.getToIndex();
                if (from == to) {
                    return;
                }
                ColumnSpec moved = columnSpecs.remove(from);
                columnSpecs.add(to, moved);
            }

            @Override
            public void columnMarginChanged(ChangeEvent ce) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent lse) {
            }
        });
    }
    
    public void loadProperty() {
        
        
        List<ColumnSpec> defaultSpecs = new ArrayList<>();
        
        for (int i = 0; i < columnNames.length; ++i) {
            ColumnSpec cs = new ColumnSpec();
            cs.setName(columnNames[i]);
            cs.setMethod(propNames[i]);
            cs.setCls(columnClasses[i].getName());
            cs.setWidth(columnWidth[i]);
            defaultSpecs.add(cs);
        }
        
        // preference から
        List<ColumnSpec> loadSpecs =new ArrayList<>();
        String line = Project.getString(specName);
        try {
            String[] params = line.split(",");
            int len = params.length / 4;

            // columnSpecリストを作成する
            for (int i = 0; i < len; i++) {
                int k = 4 * i;
                String name = params[k];
                String method = params[k + 1];
                String cls = params[k + 2];
                int width = 50;
                try {
                    width = Integer.parseInt(params[k + 3]);
                } catch (Exception ex) {
                }
                ColumnSpec cp = new ColumnSpec(name, method, cls, width);
                loadSpecs.add(cp);
            }
        } catch (Exception ex) {
        }

        columnSpecs = new ArrayList<>();
        // 合致するものは引き継ぐ。順番をキープ
        for (ColumnSpec loadCs : loadSpecs) {
            for (ColumnSpec defaultSpec : defaultSpecs) {
                if (loadCs.isSameSpec(defaultSpec)) {
                    columnSpecs.add(loadCs);
                    break;
                }
            }
        }
        // 不足分を追加する
        for (ColumnSpec defaultSpec : defaultSpecs) {
            boolean found = false;
            for (ColumnSpec test : columnSpecs) {
                if (defaultSpec.isSameSpec(test)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                columnSpecs.add(defaultSpec);
            }
        }
    }
    
    public void saveProperty() {
        saveProperty(table);
    }
    
    public void saveProperty(JTable tbl) {
        
        if (columnSpecs == null || tbl == null) {
            return;
        }

        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnSpecs.size(); i++) {
            if (!first) {
                sb.append(CAMMA);
            } else {
                first = false;
            }
            ColumnSpec cs = columnSpecs.get(i);
            cs.setWidth(tbl.getColumnModel().getColumn(i).getWidth());
            sb.append(cs.getName()).append(CAMMA);
            sb.append(cs.getMethod()).append(CAMMA);
            sb.append(cs.getCls()).append(CAMMA);
            sb.append(cs.getWidth());
        }
        String line = sb.toString();
        Project.setString(specName, line);

    }
    
    public JMenu createMenuItem() {

        final JMenu menu = new JMenu("表示カラム");
        for (ColumnSpec cs : columnSpecs) {
            final ColumnSpecMenuItem cbm = new ColumnSpecMenuItem(cs.getName());
            cbm.setColumnSpec(cs);
            if (cs.getWidth() != 0) {
                cbm.setSelected(true);
            }
            cbm.addActionListener((ActionEvent e) -> {
                // 全部非表示にしちゃう人があるらしい… ٩(๑`^´๑)۶
                boolean allHide = true;
                for (Component c : menu.getMenuComponents()) {
                    JCheckBoxMenuItem cbm1 = (JCheckBoxMenuItem) c;
                    if (cbm1.isSelected()) {
                        allHide = false;
                        break;
                    }
                }
                if (allHide) {
                    return;
                }
                if (cbm.isSelected()) {
                    cbm.getColumnSpec().setWidth(50);
                } else {
                    cbm.getColumnSpec().setWidth(0);
                }
                updateColumnWidth();
            });
            menu.add(cbm);
        }
        return menu;
    }
            
    private static class ColumnSpecMenuItem extends JCheckBoxMenuItem {
        
        private ColumnSpec cs;
        
        private ColumnSpecMenuItem(String name) {
            super(name);
        }
        
        private void setColumnSpec(ColumnSpec cs) {
            this.cs = cs;
        }
        private ColumnSpec getColumnSpec() {
            return cs;
        }
    }
}
