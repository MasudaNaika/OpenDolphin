package org.masudanaika.modanna.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author masuda, Masuda Naika
 */
public class ModannaTabbedPaneUI extends BasicTabbedPaneUI {

    private final Color selectedBackground = UIManager.getColor("TabbedPane.selectedBackground");
    private final Color borderColor = UIManager.getColor("TabbedPane.borderColor");
    private final Color rolloverColor = UIManager.getColor("TabbedPane.rolloverColor");
    
    private final boolean tabCentered = UIManager.getBoolean("TabbedPane.tabCentered");

    public static ComponentUI createUI(JComponent c) {
        return new ModannaTabbedPaneUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
    }

    @Override
    protected LayoutManager createLayoutManager() {
        if (tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT || !tabCentered) {
            return super.createLayoutManager();
        } else {
            return new CenterTabbedPaneLayout();
        }
    }
    
    // code form WindoesTabbedPaneUI
    @Override
    protected void setRolloverTab(int index) {

        int oldRolloverTab = getRolloverTab();
        super.setRolloverTab(index);
        Rectangle r1 = null;
        Rectangle r2 = null;
        if ((oldRolloverTab >= 0) && (oldRolloverTab < tabPane.getTabCount())) {
            r1 = getTabBounds(tabPane, oldRolloverTab);
        }
        if (index >= 0) {
            r2 = getTabBounds(tabPane, index);
        }
        if (r1 != null) {
            if (r2 != null) {
                tabPane.repaint(r1.union(r2));
            } else {
                tabPane.repaint(r1);
            }
        } else if (r2 != null) {
            tabPane.repaint(r2);
        }

    }
    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, 
            int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
    }
    
    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        
        Color c= tabPane.getBackgroundAt(tabIndex);
        if (isSelected) {
            c = selectedBackground;
        } else if (tabIndex == getRolloverTab()) {
            c = rolloverColor;
        }
        
        g.setColor(c);
        g.fillRect(x, y, w, h);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Color c = isSelected ? selectedBackground : borderColor;
        g.setColor(c);
        g.drawRect(x, y, w, h);
    }

    private class CenterTabbedPaneLayout extends TabbedPaneLayout {

        @Override
        protected void rotateTabRuns(int tabPlacement, int selectedRun) {
            // rotateするとわかりにくい
            // do nothing
        }

        @Override
        protected void calculateTabRects(int tabPlacement, int tabCount) {

            super.calculateTabRects(tabPlacement, tabCount);

            if (tabPlacement == TOP || tabPlacement == BOTTOM) {
                // 複数行にわたる場合は中央寄せする必要がない
                if (runCount == 1) {
                    // tabを中央寄せにする
                    Dimension size = tabPane.getSize();
                    Insets insets = tabPane.getInsets();
                    Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
                    int tabAreaWidth = size.width - insets.left - insets.right
                            - tabAreaInsets.left - tabAreaInsets.right;

                    // タブ幅合計を計算する
                    int tabWidth = 0;
                    for (int i = 0; i < tabCount; ++i) {
                        tabWidth += rects[i].width;
                    }

                    // X axisオフセットを加算する
                    int offset = (tabAreaWidth - tabWidth) / 2;
                    for (int i = 0; i < tabCount; ++i) {
                        rects[i].x += offset;
                    }
                }
            }
        }

    }

}
