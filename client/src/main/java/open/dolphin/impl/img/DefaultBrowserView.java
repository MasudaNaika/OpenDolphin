/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DefaultBrowserView.java
 *
 * Created on 2011/06/07, 21:06:46
 */

package open.dolphin.impl.img;

/**
 *
 * @author kazushi
 */
public class DefaultBrowserView extends javax.swing.JPanel {

    /** Creates new form DefaultBrowserView */
    public DefaultBrowserView() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dirLbl = new javax.swing.JLabel();
        refreshBtn = new javax.swing.JButton();
        settingBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        newdirBtn = new javax.swing.JButton();
        backdirBtn = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("open/dolphin/impl/img/resources/DefaultBrowser"); // NOI18N
        refreshBtn.setText(bundle.getString("refreshBtn.text")); // NOI18N

        settingBtn.setText(bundle.getString("settingBtn.text")); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table);

        newdirBtn.setText(bundle.getString("newdirBtn.text")); // NOI18N
        newdirBtn.setToolTipText(bundle.getString("newdirBtn.toolTipText")); // NOI18N

        backdirBtn.setText(bundle.getString("backdirBtn.text")); // NOI18N
        backdirBtn.setToolTipText(bundle.getString("backdirBtn.toolTipText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(dirLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                        .addComponent(backdirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newdirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(settingBtn))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(refreshBtn)
                    .addComponent(settingBtn)
                    .addComponent(dirLbl)
                    .addComponent(newdirBtn)
                    .addComponent(backdirBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );

        backdirBtn.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backdirBtn;
    private javax.swing.JLabel dirLbl;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newdirBtn;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton settingBtn;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JLabel getDirLbl() {
        return dirLbl;
    }

    public javax.swing.JButton getRefreshBtn() {
        return refreshBtn;
    }

    public javax.swing.JButton getSettingBtn() {
        return settingBtn;
    }

    public javax.swing.JTable getTable() {
        return table;
    }

//s.oh^ 2014/05/07 PDF・画像タブの改善
    public javax.swing.JButton getNewDirBtn() {
        return newdirBtn;
    }
//s.oh$

//s.oh^ 2014/05/30 PDF・画像タブの改善
    public javax.swing.JButton getBackDirBtn() {
        return backdirBtn;
    }
//s.oh$
}
