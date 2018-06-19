package open.dolphin.client;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * １号カルテパネル
 *
 * @author masuda, Masuda Naika
 */
public final class KartePanel1 extends KartePanel {

    private JTextPane soaTextPane;
    
    
    @Override
    public void initComponents(boolean editor) {

        initCommonComponents();
        soaTextPane = createTextPane();

        if (editor) {
            JScrollPane scroll = new JScrollPane(soaTextPane);
            scroll.setBorder(null);
            add(scroll);
            // 取り消し線アクション登録
            KartePanelEditorKit.getInstance().registStrikeThroughAction(soaTextPane);
        } else {
            add(soaTextPane);
        }
    }

    @Override
    public JTextPane getSoaTextPane() {
        return soaTextPane;
    }

    @Override
    public JTextPane getPTextPane() {
        return null;
    }
    
    @Override
    public boolean isSinglePane() {
        return true;
    }

}
