package open.dolphin.client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import open.dolphin.project.Project;

/**
 * ChartSplitPanel
 *
 * @author masuda, Masuda Naika, refactored on 2017/12/17
 */
public class ChartSplitPanel extends JPanel implements LayoutManager {

    private static final Cursor UD_CURSOR = new Cursor(Cursor.N_RESIZE_CURSOR);
    private static final Cursor LR_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

    private static final String PROP_INSPECTOR_WIDTH = "ChartSplitPanel.inspectorWidth";
    private static final String PROP_INSPECTOR_HEIGHTS = "ChartSplitPanel.inspectorHeights";

    private static final int SPLITTER_WIDTH = 5;

    private static final int DEFAULT_INSPECTOR_WIDTH = 278;
    private static final int[] DEFAULT_INSPECTOR_HEIGHTS = {45, 375, 184, 390, 100};
    
    private int inspectorWidth = DEFAULT_INSPECTOR_WIDTH;

    private Container inspectorPanel;
    private Container chartPanel;
    private final SplitPanel splitPanel;
    

    public ChartSplitPanel() {
        splitPanel = new SplitPanel(this);
    }

    public void setInspectorPanel(Container inspectorPanel) {
        this.inspectorPanel = inspectorPanel;
    }

    public void setChartPanel(Container chartPanel) {
        this.chartPanel = chartPanel;
    }
    
    private void setInspectorWidth(int width) {
        inspectorWidth = width;
        Dimension d = inspectorPanel.getSize();
        d.width = width;
        inspectorPanel.setPreferredSize(d);
    }
    
    private Container getInspectorPanel() {
        return inspectorPanel;
    }

    public void initComponents() {
        setLayout(this);
        add(inspectorPanel);
        add(splitPanel);
        add(chartPanel);
        loadSize();
    }

    public void saveSize() {

        // EDTからする必要あり
        SwingUtilities.invokeLater(() -> {

            // splitX
            Project.setInt(PROP_INSPECTOR_WIDTH, inspectorWidth);
            // inspectorHeights
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Component comp : inspectorPanel.getComponents()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(comp.getHeight());
            }

            Project.setString(PROP_INSPECTOR_HEIGHTS, sb.toString());
        });
    }

    public void loadSize() {

        inspectorWidth = Project.getInt(PROP_INSPECTOR_WIDTH, DEFAULT_INSPECTOR_WIDTH);

        int[] inspectorHeights = DEFAULT_INSPECTOR_HEIGHTS;

        String heights = Project.getString(PROP_INSPECTOR_HEIGHTS);
        if (heights != null && !heights.isEmpty()) {
            String[] tokens = heights.split(",");
            if (tokens.length == DEFAULT_INSPECTOR_HEIGHTS.length) {
                try {
                    inspectorHeights = new int[tokens.length];
                    for (int i = 0; i < tokens.length; ++i) {
                        inspectorHeights[i] = Integer.parseInt(tokens[i]);
                    }
                } catch (Exception ex) {
                    inspectorHeights = DEFAULT_INSPECTOR_HEIGHTS;
                }
            }
        }

        // PatientInspector全体の大きさを設定する
        Dimension pid = inspectorPanel.getPreferredSize();
        pid.width = inspectorWidth;
        inspectorPanel.setPreferredSize(pid);

        // 各inpsectorのサイズを設定する。実際のレイアウトはPatientInspectorのBoxLayoutに任せる
        Component[] comps = inspectorPanel.getComponents();
        for (int i = 0; i < comps.length; ++i) {
            Component comp = comps[i];
            if (isFixedHeight(comp)) {
                int height = comp.getPreferredSize().height;
                comp.setPreferredSize(new Dimension(inspectorWidth, height));
                comp.setMinimumSize(new Dimension(0, height));
                comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            } else {
                int height = inspectorHeights[i];
                comp.setPreferredSize(new Dimension(inspectorWidth, height));
            }
        }

    }
    
    // BasicInfoInspectorとPatientVisitInspector(CalendarCardPanel)はPreferredHeight固定
    private static boolean isFixedHeight(Component comp) {
        return GUIConst.FIXED_HEIGHT.equals(comp.getName());
    }

    @Override
    public void layoutContainer(Container parent) {
        
        int width = parent.getWidth();
        int height = parent.getHeight();
        int x = 0;
        inspectorPanel.setBounds(x, 0, inspectorWidth, height);
        x += inspectorWidth;
        splitPanel.setBounds(x, 0, SPLITTER_WIDTH, height);
        x += SPLITTER_WIDTH;
        chartPanel.setBounds(x, 0, width - x, height);
        
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calcLayoutSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calcLayoutSize();
    }

    private Dimension calcLayoutSize() {
        Dimension d = inspectorPanel.getPreferredSize();
        d.width += SPLITTER_WIDTH;
        return d;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }
    
//==============================================================================
    
    private static class SplitPanel extends Container implements ComponentListener,
            MouseListener, MouseMotionListener {

        // R173 また深ドラ行きたいなぁ…
        private static final Color KNOB_COLOR = new Color(173, 173, 173);

        private final ChartSplitPanel parent;
        private final List<Mark> markList;
        private final Mark knobMark;
        private Mark selectedHeightMark;
        
        // 変更モード
        private enum EDIT_MODE {
            NONE, HEIGHT, WIDTH;
        };

        private EDIT_MODE editMode = EDIT_MODE.NONE;
        
        private SplitPanel(ChartSplitPanel parent) {
            this.parent = parent;
            markList = new ArrayList<>();
            knobMark = new Mark(parent);
            addMouseListener(this);
        }

        @Override
        public void paint(Graphics g) {

            switch (editMode) {
                case NONE:
                case WIDTH:
                    g.setColor(KNOB_COLOR);
                    knobMark.setKnobY(getHeight() / 2);
                    g.fillPolygon(knobMark.getPolygon());
                    break;
                case HEIGHT:
                    g.setColor(Color.GRAY);
                    for (Mark heightMark : markList) {
                        if (heightMark.isVisible()) {
                            g.fillPolygon(heightMark.getPolygon());
                        }
                    }
                    break;
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {

            Point p = e.getPoint();

            switch (editMode) {
                case NONE:
                    if (knobMark.getPolygon().contains(p)) {
                        // ノブをクリック→高さ変更モードへ
                        editMode = EDIT_MODE.HEIGHT;
                        addComponentListener(this);
                        renewHeightMark();
                        repaint();
                    }
                    break;
                case HEIGHT:
                    // 高さ変更モード終了
                    editMode = EDIT_MODE.NONE;
                    markList.clear();
                    removeComponentListener(this);
                    repaint();
                    break;
                case WIDTH:
                    break;
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

            Point p = e.getPoint();

            switch (editMode) {
                case NONE:
                    if (knobMark.getPolygon().contains(p)) {
                        // 幅変更モード開始、ドラッグ開始
                        editMode = EDIT_MODE.WIDTH;
                        addMouseMotionListener(this);
                        setCursor(LR_CURSOR);
                    }
                    break;
                case HEIGHT:
                    selectedHeightMark = getSelectedHeightMark(p);
                    // 高さドラッグ開始
                    if (selectedHeightMark != null) {
                        addMouseMotionListener(this);
                        setCursor(UD_CURSOR);
                    }
                    break;
                case WIDTH:
                    break;
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            switch (editMode) {
                case NONE:
                    break;
                case HEIGHT:
                    if (selectedHeightMark != null) {
                        selectedHeightMark = null;
                        layoutLeftPanel();
                        parent.saveSize();
                        removeMouseMotionListener(this);
                    }
                    setCursor(DEFAULT_CURSOR);
                    break;
                case WIDTH:
                    editMode = EDIT_MODE.NONE;
                    parent.saveSize();
                    removeMouseMotionListener(this);
                    setCursor(DEFAULT_CURSOR);
                    break;
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {

            Point p = e.getPoint();

            switch (editMode) {
                case NONE:
                    break;
                case HEIGHT:
                    selectedHeightMark.setMarkY(p.y);
                    repaint();
                    break;
                case WIDTH:
                    int width = Math.max(p.x + getX(), 150);
                    width = Math.min(width, parent.getWidth());
                    parent.setInspectorWidth(width);
                    parent.revalidate();
                    break;
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        private Mark getSelectedHeightMark(Point p) {
            for (Mark heightMark : markList) {
                if (heightMark.isVisible() && heightMark.getPolygon().contains(p)) {
                    return heightMark;
                }
            }
            return null;
        }

        private void renewHeightMark() {

            markList.clear();
            Container panel = parent.getInspectorPanel();
            int cnt = panel.getComponentCount();
            if (cnt == 0) {
                return;
            }
            for (int i = 0; i < cnt; ++i) {
                Component comp = panel.getComponent(i);
                Mark mark = new Mark(parent);
                mark.setMarkY( comp.getY() + comp.getHeight());
                if (isFixedHeight(comp) || i == cnt - 1) {
                    mark.setVisible(false);
                }
                markList.add(mark);
            }
        }

        private void layoutLeftPanel() {

            Collections.sort(markList);

            Container panel = parent.getInspectorPanel();
            int y = 0;
            int cnt = panel.getComponentCount();
            for (int i = 0; i < cnt; ++i) {
                Component comp = panel.getComponent(i);
                if (i == 0) {
                    y = comp.getY();
                }
                Dimension d = comp.getSize();
                if (!isFixedHeight(comp)) {
                    d.height = markList.get(i).getMarkY() - y;
                    comp.setPreferredSize(d);
                }
                y += d.height;
            }

            parent.getInspectorPanel().revalidate();

            SwingUtilities.invokeLater(() -> {
                renewHeightMark();
                repaint();
            });
        }

        @Override
        public void componentResized(ComponentEvent e) {
            // 高さ編集モード時にウィンドウサイズを変更した場合追従させる
            if (!markList.isEmpty()) {
                renewHeightMark();
                repaint();
            }
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }

    }
    
//==============================================================================
    
    private static class Mark implements Comparable<Mark> {

        // knob mark, rectangle
        private static final int[] RECT_XPOINTS = {0, 0, 5, 5};
        private static final int[] RECT_YPOINTS = {-5, 5, 5, -5};

        // height mark, triagnle
        private static final int[] TRI_XPOINTS = {0, 5, 7};
        private static final int[] TRI_YPOINTS = {0, 5, -7};

        private final ChartSplitPanel parent;
        private int markY;
        private Polygon polygon;
        private boolean visible;

        private Mark(ChartSplitPanel parent) {
            this.parent = parent;
            visible = true;
        }

        private int getMarkY() {
            return markY;
        }

        private void setVisible(boolean visible) {
            this.visible = visible;
        }

        private boolean isVisible() {
            return visible;
        }

        private int validatePoint(int y) {
            y = Math.min(y, parent.getHeight());
            y = Math.max(y, 0);
            return y;
        }

        private void setKnobY(int y) {
            y = validatePoint(y);
            if (markY != y) {
                markY = y;
                polygon = new Polygon(RECT_XPOINTS, RECT_YPOINTS, RECT_XPOINTS.length);
                polygon.translate(0, markY);
            }
        }

        private void setMarkY(int y) {
            y = validatePoint(y);
            if (markY != y) {
                markY = y;
                polygon = new Polygon(TRI_XPOINTS, TRI_YPOINTS, TRI_XPOINTS.length);
                polygon.translate(0, markY);
            }
        }

        private Polygon getPolygon() {
            return polygon;
        }

        @Override
        public int compareTo(Mark o) {
            if (markY == o.markY) {
                return 0;
            } else {
                return markY > o.markY ? 1 : -1;
            }
        }

    }
}
