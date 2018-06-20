package open.dolphin.client;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 * KartePanelの抽象クラス
 * 改行文字を表示するEditorKitもここにある
 *
 * @author masuda, Masuda Naika
 */
public abstract class KartePanel extends Panel2 {

    // タイムスタンプの foreground カラー
    private static final Color TIMESTAMP_FORE = Color.BLUE;
    // タイムスタンプのフォントサイズ
    private static final int TIMESTAMP_FONT_SIZE = 14;
    // タイムスタンプフォント
     private Font timeStampFont;
    private static final int tsHgap = 0;
    private static final int tsVgap = 3;
    // TextPaneの余白
    private static final int topMargin = 5;
    private static final int bottomMargin = 0;
    private static final int leftMargin = 5;
    private static final int rightMargin = 5;
    private static final int nimbusBottomMargin = 12;
    private static final Insets TEXT_PANE_MARGIN = new Insets(topMargin, leftMargin, bottomMargin, rightMargin);

    //private static final Dimension INITIAL_SIZE = new Dimension(1, 1);

    protected static final int hgap = 2;
    protected static final int vgap = 0;
    protected static final int rows = 1;
    protected static final int cols = 2;

    private JPanel timeStampPanel;
    private JLabel timeStampLabel;
    
    private int karteViewerIndex;
    

    // 抽象メソッド
    public abstract void initComponents(boolean editor);

    public abstract JTextPane getPTextPane();

    public abstract JTextPane getSoaTextPane();
    
    public abstract boolean isSinglePane();

    public final JLabel getTimeStampLabel() {
        return timeStampLabel;
    }

    public int getKarteViewerIndex() {
        return karteViewerIndex;
    }

    public void setKarteViewerIndex(int karteViewerIndex) {
        this.karteViewerIndex = karteViewerIndex;
    }

    protected void initCommonComponents() {
        
        timeStampFont = new Font(Font.DIALOG, Font.PLAIN, TIMESTAMP_FONT_SIZE);
        
        setBackground(Color.LIGHT_GRAY);

        timeStampLabel = new JLabel();
        timeStampPanel = new JPanel();
        timeStampPanel.setLayout(new FlowLayout(FlowLayout.CENTER, tsHgap, tsVgap));
        timeStampLabel.setForeground(TIMESTAMP_FORE);
        timeStampLabel.setFont(timeStampFont);
        timeStampPanel.add(timeStampLabel);
        timeStampPanel.setOpaque(true);
        setLayout(new BorderLayout());
        add(timeStampPanel, BorderLayout.NORTH);
        
        timeStampPanel.setBorder(BorderFactory.createEmptyBorder(2, 1, 2, 1));
    }

    // 継承クラスから呼ばれる
    protected final JTextPane createTextPane() {
        JTextPane textPane = new JTextPane();
        textPane.setBackground(GUIConst.KARTE_UNEDITABLE_BK_COLOR);
        textPane.setOpaque(true);
        textPane.setMargin(TEXT_PANE_MARGIN);
        textPane.setEditorKit(KartePanelEditorKit.getInstance());
        return textPane;
    }

    protected final JPanel getTimeStampPanel() {
        return timeStampPanel;
    }
    

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (ClientContext.getClientContextStub().isNimbusLaf()) {
            d.height += nimbusBottomMargin;
        }
        return d;
    }
}
