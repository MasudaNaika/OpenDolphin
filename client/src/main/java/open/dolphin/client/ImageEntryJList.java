package open.dolphin.client;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Insets;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * ImageEntryのJList
 * 
 * @author masuda, Masuda Naika
 */
public class ImageEntryJList<E extends ImageEntry> extends JList<E> {
    
    private int maxIconTextWidth;
    private static final Insets MARGIN = new Insets(5, 5, 5, 5);
    private static final Border BORDER = new EmptyBorder(MARGIN);
    
    private boolean iconVisible = true;
    
    public ImageEntryJList(DefaultListModel<E> model) {
        this(model, JList.HORIZONTAL_WRAP);
    }
    
    public ImageEntryJList(DefaultListModel<E> model, int layoutOrientation) {
        super(model);
        setLayoutOrientation(layoutOrientation);
        setVisibleRowCount(0);
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // レンダラ設定
        setCellRenderer(new ImageEntryJListCellRenderer<>());
        setDropMode(DropMode.INSERT);
        setDragEnabled(true);

    }

    public void setMaxIconTextWidth(int width) {
        maxIconTextWidth = width;
    }
    
    public void setIconVisible(boolean visible) {
        this.iconVisible = visible;
    }
    
    public boolean isIconVisible() {
        return iconVisible;
    }
    
    public void repaintCells() {
        // revalidate cell dimension trick
        setFixedCellHeight(0);
        setFixedCellHeight(-1);
        //repaint();
    }
    
    private class ImageEntryJListCellRenderer<E extends ImageEntry> extends DefaultListCellRenderer {
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            E entry = (E) value;
            setBorder(BORDER);
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);

            if (iconVisible) {
                setIcon(entry.getImageIcon());
                String text = entry.getIconText();
                if (text != null && !text.isEmpty()) {
                    FontMetrics fm = getFontMetrics(getFont());
                    text = getWidthLimitHtml(fm, maxIconTextWidth, text);
                }
                setText(text);
                setHorizontalTextPosition(JLabel.CENTER);
                setVerticalTextPosition(JLabel.BOTTOM);
            } else {
                setIcon(null);
                setText(entry.getIconText());
                setHorizontalTextPosition(JLabel.LEFT);
                setVerticalTextPosition(JLabel.CENTER);
            }
            
            return this;
        }
    }

    private String getWidthLimitHtml(FontMetrics fm, int maxWidth, String text) {

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        int len = text.length();
        int lineWidth = 0;
        for (int i = 0; i < len; ++i) {
            char c = text.charAt(i);
            int charWidth = fm.charWidth((int) c);
            if (maxWidth < charWidth + lineWidth) {
                sb.append("<br>");
                lineWidth = 0;
            }
            sb.append(c);
            lineWidth += charWidth;
        }

        sb.append("</html>");
        return sb.toString();
    }
}
