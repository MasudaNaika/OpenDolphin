/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DefaultBrowserView.java
 *
 * Created on 2012/04/05, 17:07:46
 */

package open.dolphin.impl.img;

/**
 * 他プロセス連携
 * @author Life Sciences Computing Corporation.
 */
public class DefaultBrowserViewEx extends javax.swing.JPanel {

    /** Creates new form DefaultBrowserView */
    public DefaultBrowserViewEx() {
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
        otherProcess1Btn = new javax.swing.JButton();
        otherProcess2Btn = new javax.swing.JButton();
        otherProcess3Btn = new javax.swing.JButton();
        newdirBtn = new javax.swing.JButton();
        backdirBtn = new javax.swing.JButton();

        dirLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/about_16.gif"))); // NOI18N

        refreshBtn.setText("更新");

        settingBtn.setText("設定");

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

        newdirBtn.setText("新規");
        newdirBtn.setToolTipText("ディレクトリの新規作成");

        backdirBtn.setText("上へ");
        backdirBtn.setToolTipText("ディレクトリの新規作成");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dirLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addGap(94, 94, 94)
                        .addComponent(otherProcess1Btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otherProcess2Btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otherProcess3Btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backdirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newdirBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(settingBtn))
                    .addComponent(jScrollPane1))
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
                    .addComponent(otherProcess1Btn)
                    .addComponent(otherProcess2Btn)
                    .addComponent(otherProcess3Btn)
                    .addComponent(newdirBtn)
                    .addComponent(backdirBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );

        newdirBtn.getAccessibleContext().setAccessibleName("newdirBtn");
        backdirBtn.getAccessibleContext().setAccessibleDescription("一つ上の階層に戻る");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backdirBtn;
    private javax.swing.JLabel dirLbl;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newdirBtn;
    private javax.swing.JButton otherProcess1Btn;
    private javax.swing.JButton otherProcess2Btn;
    private javax.swing.JButton otherProcess3Btn;
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

    public javax.swing.JButton getOtherProcess1Btn() {
        return otherProcess1Btn;
    }

    public javax.swing.JButton getOtherProcess2Btn() {
        return otherProcess2Btn;
    }

    public javax.swing.JButton getOtherProcess3Btn() {
        return otherProcess3Btn;
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
