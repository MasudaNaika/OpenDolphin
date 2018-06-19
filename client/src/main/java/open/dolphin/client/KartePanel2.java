package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * ２号カルテパネル
 * @author masuda, Masuda Naika
 */
public final class KartePanel2 extends KartePanel {

    private JTextPane pTextPane;
    private JTextPane soaTextPane;
    
    
    @Override
    public void initComponents(boolean editor) {

        initCommonComponents();
        soaTextPane = createTextPane();
        pTextPane = createTextPane();

        if (editor) {
            ScrollableJPanel panel = new ScrollableJPanel();
            panel.setFixedWidth(true);
            panel.setLayout(new GridLayout(rows, cols, hgap, vgap));
            panel.add(soaTextPane);
            panel.add(pTextPane);
            JScrollPane scroll = new JScrollPane(panel);
            scroll.setBorder(null);
            add(scroll, BorderLayout.CENTER);
            // 取り消し線アクション登録
            KartePanelEditorKit.getInstance().registStrikeThroughAction(soaTextPane);
            KartePanelEditorKit.getInstance().registStrikeThroughAction(pTextPane);
        } else {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(rows, cols, hgap, vgap));
            panel.add(soaTextPane);
            panel.add(pTextPane);
            add(panel, BorderLayout.CENTER);
        }
    }
    
    @Override
    public JTextPane getSoaTextPane() {
        return soaTextPane;
    }

    @Override
    public JTextPane getPTextPane() {
        return pTextPane;
    }
    
    @Override
    public boolean isSinglePane() {
        return false;
    }
/*
    // KarteDocumentViewerのBoxLayoutがうまくやってくれるように
    @Override
    public Dimension getPreferredSize() {

        int w = getContainerWidth();
        int h = getTimeStampPanel().getPreferredSize().height;
        h -= 15;    // some adjustment

        int hsoa = soaTextPane.getPreferredSize().height;
        int hp = pTextPane != null
                ? pTextPane.getPreferredSize().height : 0;
        h += Math.max(hp, hsoa);

        return new Dimension(w, h);
    }
*/
}
