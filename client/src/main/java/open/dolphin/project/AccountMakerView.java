/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.project;

/**
 *
 * @author kazushi
 */
public class AccountMakerView extends javax.swing.JPanel {

    /**
     * Creates new form AccountMakerView
     */
    public AccountMakerView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        accountMakeBtn = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("オンラインテスト"));

        accountMakeBtn.setText("評価用アカウント作成...");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .add(accountMakeBtn)
                .addContainerGap(86, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(accountMakeBtn)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accountMakeBtn;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JButton getAccountMakeBtn() {
        return accountMakeBtn;
    }
}