/*
 * PatientSearchView.java
 *
 * Created on 2007/11/22, 18:43
 */

package open.dolphin.impl.psearch;

/**
 *
 * @author  kazushi
 */
public class PatientSearchView extends javax.swing.JPanel {
   
    
    /** Creates new form PatientSearchView */
    public PatientSearchView() {
        initComponents();
    }

    public javax.swing.JLabel getCountLbl() {
        return countLbl;
    }

    public javax.swing.JTable getTable() {
        return table;
    }

    public javax.swing.JTextField getKeywordFld() {
        return keywordFld;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new AddressTipsTable();
        keywordFld = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        autoIme = new javax.swing.JCheckBox();
        countLbl = new javax.swing.JLabel();
        sortItem = new javax.swing.JComboBox();
        tmpKarteButton = new javax.swing.JButton();

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

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("open/dolphin/impl/psearch/resources/PatientSearchView"); // NOI18N
        keywordFld.setToolTipText(bundle.getString("keywordFld.tooTipText")); // NOI18N

        jLabel1.setText(bundle.getString("jLabel1.text")); // NOI18N

        autoIme.setText(bundle.getString("autoIme.text")); // NOI18N

        countLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        countLbl.setText(bundle.getString("countLbl.text")); // NOI18N

        sortItem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Patient ID", "Name in Kana" }));

        tmpKarteButton.setText(bundle.getString("tmpKarteButton.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(autoIme)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(keywordFld, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tmpKarteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(countLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(keywordFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(countLbl)
                        .addComponent(tmpKarteButton)
                        .addComponent(jLabel2))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(autoIme)
                        .addComponent(sortItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoIme;
    private javax.swing.JLabel countLbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField keywordFld;
    private javax.swing.JComboBox sortItem;
    private javax.swing.JTable table;
    private javax.swing.JButton tmpKarteButton;
    // End of variables declaration//GEN-END:variables

    public javax.swing.JCheckBox getAutoIme() {
        return autoIme;
    }

    public void setAutoIme(javax.swing.JCheckBox autoIme) {
        this.autoIme = autoIme;
    }

    public javax.swing.JComboBox getSortItem() {
        return sortItem;
    }

    public javax.swing.JButton getTmpKarteButton() {
        return tmpKarteButton;
    }
    
//s.oh^ 2014/10/22 Icon表示
    public javax.swing.JLabel getSearchLabel() {
        return jLabel2;
    }
//s.oh$
}
