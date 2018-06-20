package open.dolphin.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import open.dolphin.client.GUIConst;

/**
 * ストライプテーブルのセルレンダラ
 *
 * @author masuda, Masuda Naika
 */
public class StripeTableCellRenderer extends DefaultTableCellRenderer {

    private static final Border emptyBorder = BorderFactory.createEmptyBorder();
    private static final Color[] ROW_COLORS = {GUIConst.TABLE_EVEN_COLOR, GUIConst.TABLE_ODD_COLOR};
    private static final int ROW_HEIGHT = 18;

    private JTable table;

    public StripeTableCellRenderer() {
        super();
    }

    public StripeTableCellRenderer(JTable table) {
        super();
        setTable(table);
    }

    public final void setTable(JTable table) {
        this.table = table;
        table.setRowHeight(ROW_HEIGHT);
        table.setFillsViewportHeight(true);   // viewportは広げておく
        //table.setShowVerticalLines(false);
        //table.setShowHorizontalLines(false);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setUI(new StripeTableUI());
    }

    public void setDefaultRenderer() {
        table.setDefaultRenderer(Object.class, this);
    }

    // 選択・非選択の色分けはここでする。特に指定したいときは後で上書き
    // ストライプはStripeTableUIが描画する
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        setOpaque(true);
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
            ((JComponent) table.getDefaultRenderer(Boolean.class)).setOpaque(true);
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
            ((JComponent) table.getDefaultRenderer(Boolean.class)).setOpaque(false);
        }

        // 選択したときにcellに枠がつくのを消す。Nimbusでは効果がないｗ
        this.setBorder(emptyBorder);

        return this;
    }

    private static class StripeTableUI extends BasicTableUI {

        @Override
        public void paint(Graphics g, JComponent c) {

            // Paint zebra background stripes
            Insets insets = c.getInsets();
            int x = insets.left;
            int y = insets.top;
            int width = c.getWidth() - insets.left - insets.right;
            int height = c.getHeight() - insets.top - insets.bottom;
            int numRows = table.getRowCount();
            Rectangle visibleRect = c.getVisibleRect();
            Rectangle rowRect = new Rectangle();

            for (int row = 0; row < numRows || y < height; ++row) {
                int rowHeight = row < numRows
                        ? table.getRowHeight(row)
                        : table.getRowHeight();
                rowHeight = Math.min(rowHeight, height - y);
                rowRect.setBounds(x, y, width, rowHeight);
                if (c.isPaintingForPrint() || rowRect.intersects(visibleRect)) {
                    g.setColor(ROW_COLORS[row & 1]);
                    g.fillRect(x, y, width, rowHeight);
                }
                y += rowHeight;
            }

            super.paint(g, c);
        }
    }
}
