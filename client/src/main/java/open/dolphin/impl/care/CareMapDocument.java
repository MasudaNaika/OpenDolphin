package open.dolphin.impl.care;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;
import javax.swing.*;
import open.dolphin.client.*;
import open.dolphin.delegater.AppointmentDelegater;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.dto.ImageSearchSpec;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.project.Project;

/**
 * CareMap Document.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class CareMapDocument extends AbstractChartDocument {
    
    public static final String MARK_EVENT_PROP = "MARK_EVENT_PROP";
    public static final String PERIOD_PROP = "PERIOD_PROP";
    public static final String CALENDAR_PROP = "CALENDAR_PROP";
    public static final String SELECTED_DATE_PROP = "SELECTED_DATE_PROP";
    public static final String SELECTED_APPOINT_DATE_PROP = "SELECTED_DATE_PROP";
    public static final String APPOINT_PROP = "APPOINT_PROP";
    
//    private static final String[] orderNames = {"処方","注射","処置","指導","ラボテスト","生体検査","細菌検査", "手術","放射線", "その他"}
//    private static final String[] orderCodes = {"medOrder","injectionOrder","treatmentOrder","instractionChargeOrder","testOrder","physiologyOrder","bacteriaOrder","surgeryOrder","radiologyOrder","otherOrder"};
//    private static final String[] appointNames = { "再診", "検体検査", "画像診断", "その他" };
    
    private final String[] orderNames;
    private final String[] orderCodes;
    private final String[] appointNames;
    
    private static final Color[] appointColors = {
        ClientContext.getColor("color.EXAM_APPO"),
        ClientContext.getColor("color.TEST"),
        ClientContext.getColor("color.IMAGE"),
        new Color(251, 239, 128) };
    
    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;
//    private static final String TITLE = "治療履歴";
    
    private OrderHistoryPanel history;
    private AppointTablePanel appointTable;
    private ImageHistoryPanel imagePanel;
    private JPanel historyContainer;
    private final String imageEvent = "image"; //orderCodes[2]; //
    // Calendars
    private SimpleCalendarPanel c0;
    private SimpleCalendarPanel c1;
    private SimpleCalendarPanel c2;
    private Period selectedPeriod;
    private int origin;
    private PropertyChangeSupport myBoundSupport;
    private HashMap<Integer, SimpleCalendarPanel> cPool;
    private String selectedEvent;
    //private boolean updated;
    private JButton updateAppoBtn; // 予約の更新はこのボタンで行う
    
    // モジュール検索関連
    private List allModules;
    private List allAppointments;
    private List allImages;
    
//minagawa^ LSC Test    
    private JButton prevBtn;
    private JButton nextBtn;
    private JComboBox orderCombo;
//minagawa$    
    
    //private javax.swing.Timer taskTimer;
    
    /**
     * Creates new CareMap
     */
    public CareMapDocument() {
        // Resource injection
        java.util.ResourceBundle bundle = ClientContext.getMyBundle(CareMapDocument.class);
        setTitle(bundle.getString("title.document"));
        orderNames = bundle.getString("orderNames.comboBox").split(",");
        orderCodes = bundle.getString("methods.comboBox").split(",");
        appointNames = bundle.getString("appointNames").split(",");
    }
    
    /**
     * 初期化する。
     */
    private void initialize() {
        
        cPool = new HashMap<>(12, 0.75f);
        Chart chartCtx = getContext();
        
        JPanel myPanel = getUI();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        
        // 先月、今月、来月のカレンダーを生成する
        SimpleCalendarPanel.SimpleCalendarPool pool = SimpleCalendarPanel.SimpleCalendarPool
                .getInstance();
        c0 = pool.acquireSimpleCalendar(origin - 1);
        c1 = pool.acquireSimpleCalendar(origin);
        c2 = pool.acquireSimpleCalendar(origin + 1);
        c0.setChartContext(getContext());
        c1.setChartContext(getContext());
        c2.setChartContext(getContext());
        c0.setParent(this);
        c1.setParent(this);
        c2.setParent(this);
        cPool.put(origin - 1, c0);
        cPool.put(origin, c1);
        cPool.put(origin + 1, c2);
        
        final JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 11, 2));
        p.add(c0);
        p.add(c1);
        p.add(c2);
        
        prevBtn = new JButton(ClientContext.getImageIconArias("icon_back_small"));      
        prevBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // クリックされたら (c0 | c1 | c2) -> (c0=test | c1=c0 | c2=c1)
                SimpleCalendarPanel.SimpleCalendarPool pool = SimpleCalendarPanel.SimpleCalendarPool
                        .getInstance();
                origin--;
                SimpleCalendarPanel save = c0;
                SimpleCalendarPanel test = (SimpleCalendarPanel) cPool
                        .get(origin - 1);
                
                if (test != null) {
                    // Pool されていた場合
                    c0 = test;
                    
                } else {
                    // 新規に作成
                    c0 = pool.acquireSimpleCalendar(origin - 1);
                    c0.setChartContext(getContext());
                    c0.setParent(CareMapDocument.this);
                    
                    // カレンダの日をクリックした時に束縛属性通知を受けるリスナ
                    c0.addPropertyChangeListener(SELECTED_DATE_PROP, history);
                    c0.addPropertyChangeListener(SELECTED_DATE_PROP, imagePanel);
                    c0.addPropertyChangeListener(SELECTED_APPOINT_DATE_PROP, appointTable);
                    c0.addPropertyChangeListener(APPOINT_PROP, appointTable);
                    
                    cPool.put(origin - 1, c0);
                }
                
                c2 = c1;
                c1 = save;
                p.removeAll();
                p.add(c0);
                p.add(c1);
                p.add(c2);
                p.revalidate();
                
                // オーダ履歴の抽出期間全体が変化したので通知する
                Period p = new Period(this);
                p.setStartDate(c0.getFirstDate());
                p.setEndDate(c2.getLastDate());
                setSelectedPeriod(p);
            }
        });
        

        nextBtn = new JButton(ClientContext.getImageIconArias("icon_forward_small"));
        nextBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // クリックされたら (c0 | c1 | c2) -> (c0=c1 | c1=c2 | c2=test)
                SimpleCalendarPanel.SimpleCalendarPool pool = SimpleCalendarPanel.SimpleCalendarPool.getInstance();
                origin++;
                SimpleCalendarPanel save = c2;
                SimpleCalendarPanel test = (SimpleCalendarPanel) cPool.get(origin + 1);
                
                if (test != null) {
                    // Pool されていた場合
                    c2 = test;
                    
                } else {
                    // 新規に作成する
                    c2 = pool.acquireSimpleCalendar(origin + 1);
                    c2.setChartContext(getContext());
                    c2.setParent(CareMapDocument.this);
                    
                    // カレンダの日をクリックした時に束縛属性通知を受けるリスナ
                    c2.addPropertyChangeListener(SELECTED_DATE_PROP, history);
                    c2.addPropertyChangeListener(SELECTED_DATE_PROP, imagePanel);
                    c2.addPropertyChangeListener(SELECTED_APPOINT_DATE_PROP, appointTable);
                    c2.addPropertyChangeListener(APPOINT_PROP, appointTable);
                    
                    cPool.put(origin + 1, c2);
                }
                
                c0 = c1;
                c1 = save;
                p.removeAll();
                p.add(c0);
                p.add(c1);
                p.add(c2);
                p.revalidate();
                
                // オーダ履歴の抽出期間全体が変化したので通知する
                Period p = new Period(this);
                p.setStartDate(c0.getFirstDate());
                p.setEndDate(c2.getLastDate());
                setSelectedPeriod(p);
            }
        });
        
        // 予約表テーブルを生成する
        updateAppoBtn = new JButton(ClientContext.getImageIconArias("icon_save_small"));      
        updateAppoBtn.setEnabled(false);
        updateAppoBtn.addActionListener((ActionEvent e) -> {
            save();
        });
        appointTable = new AppointTablePanel(updateAppoBtn);
        appointTable.setParent(this);
        String borderText = ClientContext.getMyBundle(CareMapDocument.class).getString("borderText.appointment");
        appointTable.setBorder(BorderFactory.createTitledBorder(borderText));
        appointTable.setPreferredSize(new Dimension(500, 260));
        
        // オーダ履歴表示用テーブルを生成する
        history = new OrderHistoryPanel();
        //history.setParent(this);
        history.setPid(chartCtx.getPatient().getPatientId());
        
        // 画像履歴用のパネルを生成する
        imagePanel = new ImageHistoryPanel();
        imagePanel.setMyParent(this);
        imagePanel.setPid(chartCtx.getPatient().getPatientId());
        
        // 表示するオーダを選択する Combo, カレンダーの送る、戻るボタンを配置するパネル
        JPanel cp = new JPanel();
        cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));
        
        // オーダ選択用のコンボボックス
        
        orderCombo = new JComboBox(orderNames);
        Dimension dim = new Dimension(100, 26);
        orderCombo.setPreferredSize(dim);
        orderCombo.setMaximumSize(dim);
        ComboBoxRenderer r = new ComboBoxRenderer();
        orderCombo.setRenderer(r);
        orderCombo.addItemListener((ItemEvent e) -> {
            // オーダ選択が変更されたら
            if (e.getStateChange() == ItemEvent.SELECTED) {
                
                String event = getMarkCode();
                
                if (event.equals(imageEvent)) {
                    // 画像履歴が選択された場合 Image Panel に変更する
                    historyContainer.removeAll();
                    historyContainer.add(imagePanel, BorderLayout.CENTER);
                    historyContainer.revalidate();
                    // CareMapDocument.this.repaint();
                    getUI().repaint();
                    
                } else if (selectedEvent.equals(imageEvent)) {
                    // 現在のイベントが Image の場合は オーダ履歴用と入れ替える
                    historyContainer.removeAll();
                    historyContainer.add(history, BorderLayout.CENTER);
                    historyContainer.revalidate();
                    // CareMapDocument.this.repaint();
                    getUI().repaint();
                }
                
                // 選択されたオーダをイベント属性に設定する
                setSelectedEvent(event);
            }
        });
        cp.add(Box.createHorizontalGlue());
        cp.add(prevBtn);
        cp.add(Box.createHorizontalStrut(5));
        cp.add(orderCombo);
        cp.add(Box.createHorizontalStrut(5));
        cp.add(nextBtn);
        // cp.add(Box.createHorizontalStrut(30));
        cp.add(Box.createHorizontalGlue());
        JPanel han = new JPanel();
        han.setLayout(new BoxLayout(han, BoxLayout.X_AXIS));
        
        String labeltext = ClientContext.getMyBundle(CareMapDocument.class).getString("labelText.appointment");
        han.add(new JLabel(labeltext+"( "));
        for (int i = 0; i < appointNames.length; i++) {
            if (i != 0) {
                han.add(Box.createHorizontalStrut(7));
            }
            AppointLabel dl = new AppointLabel(appointNames[i],
                    new ColorFillIcon(appointColors[i], 10, 10, 1),
                    SwingConstants.CENTER);
            han.add(dl);
        }
        han.add(new JLabel(" )"));
        han.add(Box.createHorizontalStrut(7));
        Color birthC = ClientContext.getColor("color.BIRTHDAY_BACK");
        labeltext = ClientContext.getMyBundle(CareMapDocument.class).getString("labelText.birthDay");
        han.add(new JLabel(labeltext, new ColorFillIcon(birthC, 10, 10, 1),
                SwingConstants.CENTER));
        han.add(Box.createHorizontalStrut(11));
        cp.add(han);
        
        myPanel.add(p);
        myPanel.add(Box.createVerticalStrut(7));
        myPanel.add(cp);
        myPanel.add(Box.createVerticalStrut(7));
        
        // 検査履歴と画像歴の切り替えコンテナ
        historyContainer = new JPanel(new BorderLayout());
        historyContainer.add(history, BorderLayout.CENTER);
        borderText = ClientContext.getMyBundle(CareMapDocument.class).getString("borderText.history");
        historyContainer.setBorder(BorderFactory.createTitledBorder(borderText));
        myPanel.add(historyContainer);
        
        myPanel.add(Box.createVerticalStrut(7));
        myPanel.add(appointTable);
        
        myPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
        
        // イベントとリスナの関係を設定する
        
        // カレンダーセットの変更通知
        addPropertyChangeListener(CALENDAR_PROP, appointTable);
        
        c0.addPropertyChangeListener(APPOINT_PROP, appointTable);
        c1.addPropertyChangeListener(APPOINT_PROP, appointTable);
        c2.addPropertyChangeListener(APPOINT_PROP, appointTable);
        
        // カレンダーの日を選択した時に通知されるもの
        c0.addPropertyChangeListener(SELECTED_DATE_PROP, history);
        c1.addPropertyChangeListener(SELECTED_DATE_PROP, history);
        c2.addPropertyChangeListener(SELECTED_DATE_PROP, history);
        c0.addPropertyChangeListener(SELECTED_DATE_PROP, imagePanel);
        c1.addPropertyChangeListener(SELECTED_DATE_PROP, imagePanel);
        c2.addPropertyChangeListener(SELECTED_DATE_PROP, imagePanel);
        
        // カレンダ上の予約日を選択された時に通知されるもの
        c0.addPropertyChangeListener(SELECTED_APPOINT_DATE_PROP, appointTable);
        c1.addPropertyChangeListener(SELECTED_APPOINT_DATE_PROP, appointTable);
        c2.addPropertyChangeListener(SELECTED_APPOINT_DATE_PROP, appointTable);
    }
    
    @Override
    public void start() {
        initialize();
        enter();
        // 最初に選択されているオーダの履歴を表示する
        setSelectedEvent(getMarkCode());
        Period period = new Period(this);
        period.setStartDate(c0.getFirstDate());
        period.setEndDate(c2.getLastDate());
        setSelectedPeriod(period);
    }
    
    @Override
    public void stop() {  
    }
    
    /**
     * オーダを表示するカラーを返す。
     *
     * @param order
     *            オーダ名
     * @return カラー
     */
    public Color getOrderColor(String order) {
        Color ret = Color.PINK;
        return ret;
    }
    
    /**
     * 予約のカラーを返す。
     *
     * @param appoint
     *            予約名
     * @return カラー
     */
    public Color getAppointColor(String appoint) {
        
        if (appoint == null) {
            return Color.white;
        }
        
        Color ret = null;
        for (int i = 0; i < appointNames.length; i++) {
            if (appoint.equals(appointNames[i])) {
                ret = appointColors[i];
            }
        }
        return ret;
    }
    
    /**
     * プロパティチェンジリスナを追加する。
     *
     * @param prop プロパティ名
     * @param l リスナ
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        if (myBoundSupport == null) {
            myBoundSupport = new PropertyChangeSupport(this);
        }
        myBoundSupport.addPropertyChangeListener(prop, l);
    }
    
    /**
     * プロパティチェンジリスナを削除する。
     *
     * @param prop プロパティ名
     * @param l リスナ
     */
    @Override
    public void removePropertyChangeListener(String prop,
            PropertyChangeListener l) {
        if (myBoundSupport == null) {
            myBoundSupport = new PropertyChangeSupport(this);
        }
        myBoundSupport.removePropertyChangeListener(prop, l);
    }
    
    /**
     * 表示している期間内にあるモジュールの日をマークする。
     * @param newModules  表示している期間内にあるモジュールのリスト
     */
    public void setAllModules(List newModules) {
        
        if (newModules == null || newModules.isEmpty()) {
            return;
        }
        
        allModules = newModules;
        
        c0.setModuleList(selectedEvent, (ArrayList) allModules.get(0));
        c1.setModuleList(selectedEvent, (ArrayList) allModules.get(1));
        c2.setModuleList(selectedEvent, (ArrayList) allModules.get(2));
        
        history.setModuleList(allModules);
    }
    
    /**
     * 表示している期間内にある予約日をマークする。
     * @param allAppo 表示している期間内にある予約日のリスト
     */
    public void setAllAppointments(List allAppo) {
        
        if (allAppo == null || allAppo.isEmpty()) {
            return;
        }
        
        allAppointments = allAppo;
        
        c0.setAppointmentList((ArrayList) allAppointments.get(0));
        c1.setAppointmentList((ArrayList) allAppointments.get(1));
        c2.setAppointmentList((ArrayList) allAppointments.get(2));
        
        notifyCalendar();
    }
    
    /**otifyCalendar();
    }
    
    /**
     * 表示している期間内にある画像をマークする。
     * @param images
     */
    public void setAllImages(List images) {
        
        if (images == null || images.isEmpty()) {
            return;
        }
        
        allImages = images;
        
        c0.setImageList(selectedEvent, (ArrayList) allImages.get(0));
        c1.setImageList(selectedEvent, (ArrayList) allImages.get(1));
        c2.setImageList(selectedEvent, (ArrayList) allImages.get(2));
        
        imagePanel.setImageList(allImages);
    }
    
    /**
     * 抽出期間が変更された場合、現在選択されているイベントに応じ、 モジュールまたは画像履歴を取得する。
     * @param p
     */
    public void setSelectedPeriod(Period p) {
        //Period old = selectedPeriod;
        selectedPeriod = p;
        
        if (getSelectedEvent().equals(imageEvent)) {
            getImageList();
            
        } else {
            getModuleList(true);
        }
    }
    
    /**
     * カレンダーセットの変更通知をする。
     */
    private void notifyCalendar() {
        SimpleCalendarPanel[] sc = new SimpleCalendarPanel[3];
        sc[0] = c0;
        sc[1] = c1;
        sc[2] = c2;
        myBoundSupport.firePropertyChange("CALENDAR_PROP", null, sc);
    }
    
    public String getSelectedEvent() {
        return selectedEvent;
    }
    
    /**
     * 表示するオーダが変更された場合、選択されたイベントに応じ、 モジュールまたは画像履歴を取得する。
     * @param code
     */
    public void setSelectedEvent(String code) {
        //String old = selectedEvent;
        selectedEvent = code;
        
        if (getSelectedEvent().equals(imageEvent)) {
            getImageList();
            
        } else {
            getModuleList(false);
        }
    }
    
    /**
     * 設定されている curEvent と抽出期間からモジュールのリストを取得する。
     */
    private void getModuleList(final boolean appo) {
        
        if (selectedEvent == null || selectedPeriod == null) {
            return;
        }
        
        final ModuleSearchSpec spec = new ModuleSearchSpec();
        spec.setCode(ModuleSearchSpec.ENTITY_SEARCH);
        spec.setKarteId(getContext().getKarte().getId());
        spec.setEntity(selectedEvent);
        spec.setStatus("F");
        
        // カレンダ別に検索する
        Date[] fromDate = new Date[3];
        fromDate[0] = ModelUtils.getDateTimeAsObject(c0.getFirstDate() + "T00:00:00");
        fromDate[1] = ModelUtils.getDateTimeAsObject(c1.getFirstDate() + "T00:00:00");
        fromDate[2] = ModelUtils.getDateTimeAsObject(c2.getFirstDate() + "T00:00:00");
        spec.setFromDate(fromDate);
        
        Date[] toDate = new Date[3];
        toDate[0] = ModelUtils.getDateTimeAsObject(c0.getLastDate() + "T23:59:59");
        toDate[1] = ModelUtils.getDateTimeAsObject(c1.getLastDate() + "T23:59:59");
        toDate[2] = ModelUtils.getDateTimeAsObject(c2.getLastDate() + "T23:59:59");
        spec.setToDate(toDate);
        
        final DocumentDelegater ddl = new DocumentDelegater();
        
        DBTask task = new DBTask<List[], Void>(getContext()) {
            
            @Override
            public List[] doInBackground() throws Exception {
                List[] ret = new List[2];
                List modules = ddl.getModuleList(spec);
                ret[0] = modules;
		if (appo) {
                    List appointments = ddl.getAppoinmentList(spec);
                    ret[1] = appointments;
		}
                return ret;
            }
            
            @Override
            public void succeeded(List[] result) {
                setAllModules(result[0]);
                if (appo) {
                    setAllAppointments(result[1]);
                }
                prevBtn.setEnabled(true);
                nextBtn.setEnabled(true);
                orderCombo.setEnabled(true);
            }
        };
        
        prevBtn.setEnabled(false);
        nextBtn.setEnabled(false);
        orderCombo.setEnabled(false);
        task.execute();
    }
    
    /**
     * 設定されている抽出期間から画像履歴を取得する。
     */
    private void getImageList() {
        
        if (selectedPeriod == null) {
            return;
        }
        
        final ImageSearchSpec spec = new ImageSearchSpec();
        spec.setCode(ImageSearchSpec.PATIENT_SEARCH);
        spec.setKarteId(getContext().getKarte().getId());
        spec.setStatus("F");
        
        // カレンダ別に検索する
        Date[] fromDate = new Date[3];
        fromDate[0] = ModelUtils.getDateTimeAsObject(c0.getFirstDate() + "T00:00:00");
        fromDate[1] = ModelUtils.getDateTimeAsObject(c1.getFirstDate() + "T00:00:00");
        fromDate[2] = ModelUtils.getDateTimeAsObject(c2.getFirstDate() + "T00:00:00");
        spec.setFromDate(fromDate);
        
        Date[] toDate = new Date[3];
        toDate[0] = ModelUtils.getDateTimeAsObject(c0.getLastDate() + "T23:59:59");
        toDate[1] = ModelUtils.getDateTimeAsObject(c1.getLastDate() + "T23:59:59");
        toDate[2] = ModelUtils.getDateTimeAsObject(c2.getLastDate() + "T23:59:59");
        spec.setToDate(toDate);
        
        spec.setIconSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        
        final DocumentDelegater ddl = new DocumentDelegater();
        
        DBTask task = new DBTask<List, Void>(getContext()) {
            
            @Override
            public List doInBackground() throws Exception {
                return ddl.getImageList(spec);
            }
            
            @Override
            public void succeeded(List result) {
                setAllImages(result);
                prevBtn.setEnabled(true);
                nextBtn.setEnabled(true);
                orderCombo.setEnabled(true);
            }
        };
        
        prevBtn.setEnabled(false);
        nextBtn.setEnabled(false);
        orderCombo.setEnabled(false);
        task.execute();
    }
    
    @Override
    public void setDirty(boolean dirty) {
        if (isDirty() != dirty) {
            super.setDirty(dirty);
            updateAppoBtn.setEnabled(isDirty());
        }
    }
    
    /**
     * 新規及び変更された予約を保存する。
     */
    @Override
    public void save() {
        
        final ArrayList<AppointmentModel> results = new ArrayList<>();
        
        Iterator iter = cPool.entrySet().iterator();
        
        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry)iter.next();

            // カレンダー単位に抽出する
            SimpleCalendarPanel c = (SimpleCalendarPanel) entry.getValue();

            if (c.getRelativeMonth() >= 0) {
                
                ArrayList<AppointmentModel> list = c.getUpdatedAppoints();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    AppointmentModel appo = list.get(i);

                    // 関係構築
                    appo.setKarteBean(getContext().getKarte());
                    appo.setUserModel(Project.getUserModel());
                    
                    // 確定日、記録日、開始日
                    // 現状の実装はここまで
                    Date confirmed = new Date();
                    appo.setConfirmed(confirmed);
                    appo.setRecorded(confirmed);
                    if (appo.getStarted() == null) {
                        appo.setStarted(confirmed);
                    }
                    // 常にFINAL
                    appo.setStatus(IInfoModel.STATUS_FINAL);
                    
                    results.add(list.get(i));
                }
            }
        }
        
        if (results.isEmpty()) {                   
            if (myBoundSupport!=null) {
                setChartDocDidSave(true);
            }
            return;
        }
        
        final AppointmentDelegater adl = new AppointmentDelegater();
        
        DBTask task = new DBTask<Void, Void>(getContext()) {

            @Override
            protected Void doInBackground() throws Exception {
                adl.putAppointments(results);
                return null;
            }
            
            @Override
            public void succeeded(Void result) {
                setDirty(false);                   
                if (myBoundSupport!=null) {
                    setChartDocDidSave(true);
                }            
            }
        };
        
        task.execute();
    }
    
    private String getMarkCode() {
        // 履歴名を検索コード(EntityName)に変換
        int index = orderCombo.getSelectedIndex();
        return orderCodes[index];
    }
    
    /**
     * ComboBoxRenderer
     *
     */
    protected class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        
        private static final long serialVersionUID = 4661822065789099499L;
        
        public ComboBoxRenderer() {
            setOpaque(true);
            // setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            // Get the selected index. (The index param isn't
            // always valid, so just use the value.)
            
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            
            // Set the icon and text. If icon was null, say so.
            Icon icon = getOrderIcon((String) value);
            
            if (icon != null) {
                setIcon(icon);
                setText((String) value);
            } else {
                setText((String) value);
            }
            
            return (Component) this;
        }
        
        private Icon getOrderIcon(String name) {
            Icon ret = null;
            return ret;
        }
    }
}