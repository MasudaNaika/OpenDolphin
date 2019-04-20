package open.dolphin.client;

import org.masudanaika.modanna.ui.ModannaTabbedPaneUI;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicTextPaneUI;
import open.dolphin.exception.DolphinException;
import open.dolphin.infomodel.DepartmentModel;
import open.dolphin.infomodel.LicenseModel;
import open.dolphin.project.Project;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * Dolphin Client のコンテキストクラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class ClientContextStub {

    //--------------------------------------------------------------------------
    private final String PLUGIN_LOCATION = "/META-INF/plugins/";
    private final String RESOURCE_LOCATION = "/open/dolphin/resources/";
    private final String TEMPLATE_LOCATION = "/open/dolphin/resources/templates/";
    private final String IMAGE_LOCATION = "/open/dolphin/resources/images/";
    private final String SCHEMA_LOCATION = "/open/dolphin/resources/schema/";
    private final String RESOURCE = "open.dolphin.resources.Dolphin";
    private final String PROPERTY_OS = "os.name";
    private final String OS_WIN = "windows";
    private final String OS_MAC = "mac";
    private final String OS_LINUX = "linux";
    private final String RESNAME_VERSION = "version";
    private final String RESNAME_APP_TITLE = "application.title";
    private final String TITLE_CONCAT = "-";
    //--------------------------------------------------------------------------

    private HashMap<String, Color> eventColorTable;
    private LinkedHashMap<String, String> toolProviders;

    private String pathToDolphin;

    private boolean dolphin;
    private boolean asp;
    private boolean i18n;
    
    private boolean nimbusLaf;

    /**
     * Creates a new ClientContextStub.
     * @param mode project mode
     */
    public ClientContextStub(String mode) {
        
        // Logger format
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
        
        asp = mode!=null && (mode.equals("asp")); 
        i18n = mode!=null && (mode.equals("i18n"));
        dolphin = !asp && !i18n;
            
        try {
            //--------------------------------------------------
            // Creates the directories in the user's home
            //--------------------------------------------------
            pathToDolphin = createDirectory(System.getProperty("user.home"), "OpenDolphin");
            createDirectory(pathToDolphin, "setting");
            createDirectory(pathToDolphin, "log");
            createDirectory(pathToDolphin, "pdf");
            createDirectory(pathToDolphin, "schema");
            createDirectory(pathToDolphin, "odt_template");
            createDirectory(pathToDolphin, "temp");
            
            //------------------------------
            // Configure Log4j properties
            //------------------------------
            String pathToSetting = createDirectory(pathToDolphin, "setting");
            Path log4jxml = Paths.get(pathToSetting, "log4j2.xml");
            if (!Files.exists(log4jxml)) {
                java.util.List<String> lines = new ArrayList<>();
                try (InputStreamReader isr = new InputStreamReader(getResourceAsStream("log4j2.xml"));
                        BufferedReader reader = new BufferedReader(isr);) {

                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        if (line.contains("_pathToLogFolder")) {
                            line = line.replace("_pathToLogFolder", getLogDirectory());
                        }
                        lines.add(line);
                    }
                }
                Files.write(log4jxml, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }
            try (InputStream in = new BufferedInputStream(Files.newInputStream(log4jxml))) {
                ConfigurationSource src = new ConfigurationSource(in);
                Configurator.initialize(null, src);
            }
            
            //----------------------------------------
            // Inits Velocity with custom log handler
            //----------------------------------------
//            Velocity.setProperty(Velocity.RUNTIME_LOG_INSTANCE, new CustomVelocityLogger());
            Velocity.setProperty("runtime.log", getLogDirectory() + File.separator + "velocity.log");
            Velocity.init();

            //------------------------------
            // Outputs the basic info.
            //------------------------------
            //-Djava.util.logging.SimpleFormatter.format='%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n' 
            Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(java.util.logging.Level.INFO, "boot.time = {0}", DateFormat.getDateTimeInstance().format(new Date()));
            logger.log(java.util.logging.Level.INFO, "os.name = {0}", System.getProperty("os.name"));
            logger.log(java.util.logging.Level.INFO, "java.version = {0}", System.getProperty("java.version"));
            logger.log(java.util.logging.Level.INFO, "dolphin.version = {0}", getVersion());
            logger.log(java.util.logging.Level.INFO, "base.directory = {0}", getBaseDirectory());
            logger.log(java.util.logging.Level.INFO, "setting.directory = {0}", getSettingDirectory());
            logger.log(java.util.logging.Level.INFO, "log.directory = {0}", getLogDirectory());
            logger.log(java.util.logging.Level.INFO, "pdf.directory = {0}", getPDFDirectory());
            logger.log(java.util.logging.Level.INFO, "schema.directory = {0}", getSchemaDirectory());
            logger.log(java.util.logging.Level.INFO, "temp.directory = {0}", getTempDirectory());
            logger.log(java.util.logging.Level.INFO, "locale = {0}", Locale.getDefault().toString());
            logger.log(java.util.logging.Level.INFO, "country = {0}", Locale.getDefault().getCountry());
            logger.log(java.util.logging.Level.INFO, "language = {0}", Locale.getDefault().getLanguage());

        } catch (IOException | DolphinException e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Creates a directory at the specified location.
     * @param parent parent directory
     * @param child  child directory name
     * @return path to the created directory
     * @throws DolphinException 
     */
    private String createDirectory(String parent, String child) throws DolphinException {

        File dir = new File(parent, child);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                String fmt = "Can not create the directory {0}.";
                String err = new MessageFormat(fmt).format(new Object[]{child});
                throw new DolphinException(err);
            }
        }
        return dir.getPath();
    }
    
    public LinkedHashMap<String, String> getToolProviders() {
        return toolProviders;
    }

    public VelocityContext getVelocityContext() {
        return new VelocityContext();
    }

    //-----------------------------------------------------------

    public boolean isMac() {
        return System.getProperty(PROPERTY_OS).toLowerCase().startsWith(OS_MAC);
    }

    public boolean isWin() {
        return System.getProperty(PROPERTY_OS).toLowerCase().startsWith(OS_WIN);
    }

    public boolean isLinux() {
        return System.getProperty(PROPERTY_OS).toLowerCase().startsWith(OS_LINUX);
    }
    
    public boolean isJaJp() {
        return Locale.getDefault().toString().equals("ja_JP");
    }

    public boolean isOpenDolphin() {
        return dolphin;
    }
    
    public boolean isAsp() {
        return asp;
    }
    
    public boolean isI18N() {
        return i18n;
    }

    //-----------------------------------------------------------

    private String getLocation(String dirName) {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseDirectory()).append(File.separator).append(dirName);
        return sb.toString();
    }

    public String getBaseDirectory() {
        return pathToDolphin;
    }
    
    public String getSettingDirectory() {
        return getLocation("setting");
    }

    public String getLogDirectory() {
        return getLocation("log");
    }
    
    public String getPDFDirectory() {
        return getLocation("pdf");
    }

    public String getSchemaDirectory() {
        return getLocation("schema");
    }
    
    public String getOdtTemplateDirectory() {
        return getLocation("odt_template");
    }
    
    public String getTempDirectory() {
        return getLocation("temp");
    }

    //-----------------------------------------------------------
    
    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle(RESOURCE);
    }
    
    public ResourceBundle getClaimBundle() {
        return ResourceBundle.getBundle("open.dolphin.order.ClaimResource");
    }
    
    public ResourceBundle getMyBundle(Class cls) {
        StringBuilder sb = new StringBuilder();
//        sb.append(cls.getPackage()).append(".resources.").append(cls.getSimpleName());
        String clsName = cls.getName();
        int index = clsName.lastIndexOf(".");
        sb.append(clsName.subSequence(0, index));   // package part
        sb.append(".resources.");                   // packageName.resources.
        sb.append(clsName.substring(index+1));      // packageName.resources.className
        String path = sb.toString();
        return ResourceBundle.getBundle(path);
    }

    public String getVersion() {
        return getString(RESNAME_VERSION);
    }

    public String getFrameTitle(String title) {
        try {
            String resTitle = getString(title);
            if (resTitle != null) {
                title = resTitle;
            }
        } catch (Exception e) {
        }
        StringBuilder buf = new StringBuilder();
        buf.append(title);
        buf.append(TITLE_CONCAT);
        buf.append(getString(RESNAME_APP_TITLE));
        buf.append(TITLE_CONCAT);
        buf.append(getString(RESNAME_VERSION));
        return buf.toString();
    }

    public URL getResource(String name) {
        if (!name.startsWith("/")) {
            name = RESOURCE_LOCATION + name;
        }
        return this.getClass().getResource(name);
    }

    public URL getImageResource(String name) {
        if (!name.startsWith("/")) {
            name = IMAGE_LOCATION + name;
        }
        return this.getClass().getResource(name);
    }

    public InputStream getResourceAsStream(String name) {
        if (!name.startsWith("/")) {
            name = RESOURCE_LOCATION + name;
        }
        return this.getClass().getResourceAsStream(name);
    }
    
    public InputStream getPluginResourceAsStream(String name) {
        if (!name.startsWith("/")) {
            name = PLUGIN_LOCATION + name;
        }
        return this.getClass().getResourceAsStream(name);
    }

    public InputStream getTemplateAsStream(String name) {
        if (!name.startsWith("/")) {
            name = TEMPLATE_LOCATION + name;
        }
        return this.getClass().getResourceAsStream(name);
    }

    public ImageIcon getImageIcon(String name) {
        if (name!=null) {
            return new ImageIcon(getImageResource(name));
        }
        return null;
    }
    
    public ImageIcon getImageIconArias(String name) {
        return this.getImageIcon(getString(name));
    }

    public ImageIcon getSchemaIcon(String name) {
        if (!name.startsWith("/")) {
            name = SCHEMA_LOCATION + name;
        }
        return new ImageIcon(this.getClass().getResource(name));
    }

    public LicenseModel[] getLicenseModel() {
        String[] desc = getStringArray("licenseDesc");
        String[] code = getStringArray("license");
        String codeSys = getString("licenseCodeSys");
        LicenseModel[] ret = new LicenseModel[desc.length];
        LicenseModel model;
        for (int i = 0; i < desc.length; i++) {
            model = new LicenseModel();
            model.setLicense(code[i]);
            model.setLicenseDesc(desc[i]);
            model.setLicenseCodeSys(codeSys);
            ret[i] = model;
        }
        return ret;
    }

    public DepartmentModel[] getDepartmentModel() {
        String[] desc = getStringArray("departmentDesc");
        String[] code = getStringArray("department");
        String codeSys = getString("departmentCodeSys");
        DepartmentModel[] ret = new DepartmentModel[desc.length];
        DepartmentModel model;
        for (int i = 0; i < desc.length; i++) {
            model = new DepartmentModel();
            model.setDepartment(code[i]);
            model.setDepartmentDesc(desc[i]);
            model.setDepartmentCodeSys(codeSys);
            ret[i] = model;
        }
        return ret;
    }

    public NameValuePair[] getNameValuePair(String key) {
        NameValuePair[] ret;
        String[] code = getStringArray(key + ".value");
        String[] name = getStringArray(key + ".name");
        int len = code.length;
        ret = new NameValuePair[len];

        for (int i = 0; i < len; i++) {
            ret[i] = new NameValuePair(name[i], code[i]);
        }
        return ret;
    }

    public HashMap<String, Color> getEventColorTable() {
        if (eventColorTable == null) {
            setupEventColorTable();
        }
        return eventColorTable;
    }

    private void setupEventColorTable() {
        // イベントカラーを定義する
        eventColorTable = new HashMap<>(10, 0.75f);
        eventColorTable.put("TODAY", getColor("color.TODAY_BACK"));
        eventColorTable.put("BIRTHDAY", getColor("color.BIRTHDAY_BACK"));
        eventColorTable.put("PVT", getColor("color.PVT"));
        eventColorTable.put("DOC_HISTORY", getColor("color.PVT"));
    }

    public String getString(String key) {
        return ResourceBundle.getBundle(RESOURCE).getString(key);
    }

    public String[] getStringArray(String key) {
        String line = getString(key);
        return line.split(",");
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public int[] getIntArray(String key) {
        String[] obj = getStringArray(key);
        int[] ret = new int[obj.length];
        for (int i = 0; i < obj.length; i++) {
            ret[i] = Integer.parseInt(obj[i]);
        }
        return ret;
    }

    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    public long[] getLongArray(String key) {
        String[] obj = getStringArray(key);
        long[] ret = new long[obj.length];
        for (int i = 0; i < obj.length; i++) {
            ret[i] = Long.parseLong(obj[i]);
        }
        return ret;
    }

    public float getFloat(String key) {
        return Float.parseFloat(getString(key));
    }

    public float[] getFloatArray(String key) {
        String[] obj = getStringArray(key);
        float[] ret = new float[obj.length];
        for (int i = 0; i < obj.length; i++) {
            ret[i] = Float.parseFloat(obj[i]);
        }
        return ret;
    }

    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    public double[] getDoubleArray(String key) {
        String[] obj = getStringArray(key);
        double[] ret = new double[obj.length];
        for (int i = 0; i < obj.length; i++) {
            ret[i] = Double.parseDouble(obj[i]);
        }
        return ret;
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public boolean[] getBooleanArray(String key) {
        String[] obj = getStringArray(key);
        boolean[] ret = new boolean[obj.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Boolean.parseBoolean(obj[i]);
        }
        return ret;
    }

    public Point lgetPoint(String name) {
        int[] data = getIntArray(name);
        return new Point(data[0], data[1]);
    }

    public Dimension getDimension(String name) {
        int[] data = getIntArray(name);
        return new Dimension(data[0], data[1]);
    }

    public Insets getInsets(String name) {
        int[] data = getIntArray(name);
        return new Insets(data[0], data[1], data[2], data[3]);
    }

    public Color getColor(String key) {
        int[] data = getIntArray(key);
        return new Color(data[0], data[1], data[2]);
    }

    public Color[] getColorArray(String key) {
        int[] data = getIntArray(key);
        int cnt = data.length / 3;
        Color[] ret = new Color[cnt];
        for (int i = 0; i < cnt; i++) {
            int bias = i * 3;
            ret[i] = new Color(data[bias], data[bias + 1], data[bias + 2]);
        }
        return ret;
    }

    public Class[] getClassArray(String name) {
        String[] clsStr = getStringArray(name);
        Class[] ret = new Class[clsStr.length];
        try {
            for (int i = 0; i < clsStr.length; i++) {
                ret[i] = Class.forName(clsStr[i]);
            }
            return ret;

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public int getHigherRowHeight() {
        return 20;
    }

    public int getMoreHigherRowHeight() {
        if (isMac()) {
            return 20;
        }
        String nimbus = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
        String laf = UIManager.getLookAndFeel().getClass().getName();
        if (laf.equals(nimbus)) {
            return 25;
        }
        
        return 20;
    }
    
    /**
     * Setup LookAndFeel、Mac button text and menu bar.
     */
    public void setupUI() {
        
        try {
            ResourceBundle bundle = getBundle();
            
            // Mac 
            if (isMac()) {
                // ScreenMenuBar
                System.setProperty("apple.laf.useScreenMenuBar", String.valueOf(true));
                
                // Cancel Text
                if (bundle.getString("cancelButtonText.mac")!=null) {
                    UIManager.put("OptionPane.cancelButtonText", bundle.getString("cancelButtonText.mac"));
                }
                // OK Text
                if (bundle.getString("okButtonText.mac")!=null) {
                    UIManager.put("OptionPane.okButtonText", bundle.getString("okButtonText.mac"));
                }
            }
            String lafName = Project.getString("lookAndFeel");
            if ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel".equals(lafName)) {
                lafName = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
                Project.setString("lookAndFeel", lafName);
            } // LAF
            else if (isWin() || isLinux()) {
                if (lafName != null) {
                    UIManager.setLookAndFeel(lafName);
                } else {
                    // Default=NimbusLookAndFeel
                    lafName = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
                    UIManager.setLookAndFeel(lafName);
                    Project.setString("lookAndFeel", lafName);
                }
            }
//masuda^ tweet
            Font FONT12 = new FontUIResource(Font.SANS_SERIF, Font.PLAIN, 12);
            Font FONT13 = new FontUIResource(Font.SANS_SERIF, Font.PLAIN, 13);
            ColorUIResource WHITE = new ColorUIResource(Color.WHITE);
            ColorUIResource DARKER_GRAY = new ColorUIResource(117, 117, 117);
            ColorUIResource LIGHT_GRAY = new ColorUIResource(Color.LIGHT_GRAY);
            ColorUIResource ROLLOVER_COLOR = new ColorUIResource(216, 234, 249);
            
            // JTree
            UIManager.put("Tree.paintLines", false);    // スタンプ箱で点線を表示しない
            UIManager.put("Tree.rowHeight", 20);        // 高さ指定
            UIManager.put("Tree.font", FONT12);

            UIManager.put("TextPane.border", new BorderUIResource(new EmptyBorder(5, 8, 5, 8)));
            UIManager.put("TextComponent.autoSelect", Boolean.FALSE);
            UIManager.put("TextField.font", FONT12);
            UIManager.put("Button.font", FONT12);
            UIManager.put("Table.font", FONT12);
            UIManager.put("TextPane.font", FONT13);
                
            nimbusLaf = UIManager.getLookAndFeel().getName().toLowerCase().startsWith("nimbus");
            if (nimbusLaf) {
                UIManager.put("TextPaneUI", BasicTextPaneUI.class.getName());
                UIManager.put("TextPane.selectionBackground", new ColorUIResource(57, 105, 138));
                UIManager.put("TextPane.selectionForeground", new ColorUIResource(Color.WHITE));
                UIManager.put("TextPane.border", new BorderUIResource(new EmptyBorder(4,6,4,6)));
            } else {
                // JTabbedPane
                Color bgColor = UIManager.getColor("Panel.background");
                UIManager.put("TabbedPaneUI", ModannaTabbedPaneUI.class.getName());
                UIManager.put("TabbedPane.font", FONT12);
                UIManager.put("TabbedPane.tabsOverlapBorder", false);
                UIManager.put("TabbedPane.tabRunOverlay", 0);
                UIManager.put("TabbedPane.tabInsets", new InsetsUIResource(3, 6, 3, 6));
                UIManager.put("TabbedPane.tabAreaInsets", new InsetsUIResource(5, 2, 5, 2));
                UIManager.put("TabbedPane.selectedTabPadInsets", new InsetsUIResource(0, 0, 0, 0));
                UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(0, 0, 0, 0));
                UIManager.put("TabbedPane.contentAreaColor", bgColor);
                UIManager.put("TabbedPane.background", bgColor);
                UIManager.put("TabbedPane.selectedForeground", WHITE);
                UIManager.put("TabbedPane.selectedBackground", DARKER_GRAY);
                UIManager.put("TabbedPane.borderColor", LIGHT_GRAY);
                UIManager.put("TabbedPane.rolloverColor", ROLLOVER_COLOR);
                UIManager.put("TabbedPane.selectedLabelShift", 0);
                UIManager.put("TabbedPane.labelShift", 0);
                UIManager.put("TabbedPane.tabCentered", true);
            }
//masuda$     
            // ToolBarの Dropdown menu制御
            UIManager.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace(System.err);
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
        }        
    }   
    
    public boolean isNimbusLaf() {
        return nimbusLaf;
    }
}