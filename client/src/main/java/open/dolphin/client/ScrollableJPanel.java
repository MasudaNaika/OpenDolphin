package open.dolphin.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * JScrollPaneに入れるJPanel
 * @author masuda, Masuda Naika
 */
public class ScrollableJPanel extends JPanel implements Scrollable {
    
    private boolean fixedWidth;
    
    public void setFixedWidth(boolean fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getParent().getSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {

        switch (orientation) {
            case SwingConstants.VERTICAL:
                return visibleRect.height / 10;
            case SwingConstants.HORIZONTAL:
                return visibleRect.width / 10;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {

        switch (orientation) {
            case SwingConstants.VERTICAL:
                return visibleRect.height;
            case SwingConstants.HORIZONTAL:
                return visibleRect.width;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        
        if (fixedWidth) {
            // 幅拡張しない
            return true;
        }
        // Viewport sizeまで広げる
        int portWidth = getPortSize().width;
        int height = getPreferredSize().width;
        return portWidth > height;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        // 縦はいつもViewport sizeまで広げる
        int portHeight = getPortSize().height;
        int height = getPreferredSize().height;
        return portHeight > height;
    }
    
    private Dimension getPortSize() {
        Container c = getParent();
        if (c != null) {
            return c.getSize();
        }
        return getSize();
    }
}
