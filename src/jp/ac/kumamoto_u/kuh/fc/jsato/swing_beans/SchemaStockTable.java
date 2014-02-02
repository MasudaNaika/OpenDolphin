/*
 * SchemStockTable.java
 *
 * Created on 2002/06/20, 8:03
 */

package jp.ac.kumamoto_u.kuh.fc.jsato.swing_beans;

import jp.ac.kumamoto_u.kuh.fc.jsato.*;
import netscape.ldap.*;

import java.beans.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.net.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.sun.image.codec.jpeg.*;

import java.io.*;
import java.text.*;
import javax.media.jai.*;

import open.dolphin.client.*;
import open.dolphin.project.*;

import java.awt.image.*;
import java.awt.image.renderable.*;


public class SchemaStockTable extends JPanel implements DragSourceListener, DragGestureListener {
    JButton uploadButton = null;
    
    private static final int ROW_HEIGHT = 96;
    
    JScrollPane imageView = null;
   
    private JTable imageTable;
    private ImageTableModel model;
    private static final String [] columnNames = {"", ""};
    private DragSource dragSource;
   
    private Vector createVector(URL url) {
        // create buffered image from the url
        PlanarImage ri = null;
        ri = JAI.create("url", url);
        if (ri == null) {
            System.out.println("Couldn't load " + url.toString());
            return null;
        }
        BufferedImage bf = null;
        try {
            bf = ri.getAsBufferedImage();
        } catch (Exception e) {
            // if unsupported file is opened, this exception (RuntimeException) is thrown.
            System.out.println("Couldn't get as buffered image: " + url.toString());
            //e.printStackTrace();
            return null;
        }
        if (bf == null) {
            System.out.println("Couldn't get as buffered image: " + url.toString());
            return null;
        }
        ImageIcon srcIcon = new ImageIcon((Image)bf);

        // create the icon
        //ImageIcon srcIcon = new ImageIcon(file);// ( this is cheap solution to support JPEG and GIF )
        
        if (srcIcon == null) return null;
        // store original path
        srcIcon.setDescription(url.getPath());
        // get the image of the icon
        Image img = srcIcon.getImage();
        // get the resized image
        Image dstImg = SchemaUtil.scaleImage(srcIcon, ROW_HEIGHT);
        if (dstImg == null) {
            img = null;
            return null;
        }

        // pack the two images to the vector
        Vector v = new Vector();
        v.addElement(dstImg);// small image (thumbnail)
        v.addElement(img);// large image (original)
        return v;
    }
    
    private Vector createVector(String file) {
        // create buffered image from the file
        //System.out.println("**** loading image file: " + file);
        PlanarImage ri = null;
        ri = JAI.create("fileload", file);
        if (ri == null) {
            System.out.println("Couldn't load " + file);
            return null;
        }
        BufferedImage bf = null;
        try {
            bf = ri.getAsBufferedImage();
        } catch (Exception e) {
            // if unsupported file is opened, this exception (RuntimeException) is thrown.
            System.out.println("Couldn't get as buffered image: " + file);
            //e.printStackTrace();
            return null;
        }
        if (bf == null) {
            System.out.println("Couldn't get as buffered image: " + file);
            return null;
        }
        ImageIcon srcIcon = new ImageIcon((Image)bf);

        // create the icon
        //ImageIcon srcIcon = new ImageIcon(file);// ( this is cheap solution to support JPEG and GIF )
        
        if (srcIcon == null) return null;
        // store original file path
        srcIcon.setDescription(file);
        // get the image of the icon
        Image img = srcIcon.getImage();
        // get the resized image
        Image dstImg = SchemaUtil.scaleImage(srcIcon, ROW_HEIGHT);
        if (dstImg == null) {
            img = null;
            return null;
        }

        // pack the two images to the vector
        Vector v = new Vector();
        v.addElement(dstImg);// small image (thumbnail)
        v.addElement(img);// large image (original)
        return v;
    }

    class MyCellRenderer extends JLabel implements TableCellRenderer {
        public MyCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
                                           Object value,
                                           boolean isSelected,
                                           boolean hasFocus,
                                           int row,
                                           int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            // get the thumbnail image and create the icon
            if ( ((Vector)value) != null &&
                ((Vector)value).size() > 0 &&
                (((Vector)value).firstElement()) != null ) {
                ImageIcon icon = new ImageIcon((Image)((Vector)value).firstElement());
                setIcon(icon);
            } else {
                ///////////////////////////////////////////////
                // 'unknown' icon is assigned
                setIcon(
                    new ImageIcon(getClass().getResource("/open/dolphin/resources/images/unknown.jpg"))
                );
                ///////////////////////////////////////////////
            }
            
            //=================================================================================
            if (isSelected && hasFocus && table != null) {
                ListSelectionModel model = table.getSelectionModel();
                if (model != null) {
                    if (model.getValueIsAdjusting() == false) {
                        if (uploadButton != null) {
                            uploadButton.setEnabled(true);
                        }
                    }
                }
            }
            //=================================================================================
            
            return this;
        }
    }
    
    public void sortArray(CollationKey[] keys) {
        CollationKey tmp;
        for (int i = 0; i < keys.length; i++) {
          for (int j = i + 1; j < keys.length; j++) {
             // Compare the keys
             if( keys[i].compareTo( keys[j] ) > 0 ) {
                // Swap keys[i] and keys[j] 
                tmp = keys[i];
                keys[i] = keys[j];
                keys[j] = tmp;
             }
          }
        }
    }

    public void traverseDir(File f) {
        if (f.isDirectory()) {
            String[] children = f.list();
            //============================
            // sort children by name
            Collator enUSCollator = Collator.getInstance(new Locale("en","US"));
            CollationKey[] keys = new CollationKey[children.length];
            for (int k = 0; k < keys.length; k ++) {
                keys[k] = enUSCollator.getCollationKey(children[k]);
            }
            sortArray(keys);
            for (int i = 0; i < keys.length; i++) {
                children[i] = keys[i].getSourceString();
            }
            //===========================
            for (int i=0; i<children.length; i++) {
                traverseDir(new File(f, children[i]));
            }
        } else {
            processFile(f);
        }
    }
    
    public void processFile(File f) {
        // add file path to the Vector foundFiles
        foundFiles.addElement(f.getPath());
    }

    Vector foundFiles = null;
    public void loadSchema(String schemaFolder) {
        File targetDir = new File(schemaFolder);
        
        if (targetDir.exists() == false) {
            //System.out.println("Schema folder doesn't exist.");
            String title = "警告";
            int type = JOptionPane.WARNING_MESSAGE;
            int option = JOptionPane.DEFAULT_OPTION;
            Object message = "シェーマ用に指定されたフォルダ\n" + targetDir.getPath() + "は見つかりませんでした。\nフォルダを確認するか、スタンプボックスで再設定して下さい。";
            Object[] options = {"OK"};
            JOptionPane op = new JOptionPane(message,type,option,null,options);
            JDialog dlg = op.createDialog(this, title);
            dlg.setVisible(true);
            /*
            // Create new directory with specified name.
            if (targetDir.mkdirs() == true) {
                System.out.println("Directory was created.");
            }
             */
            
            /*
            // Check the readability of the directory
            if (targetDir.isDirectory() == false || targetDir.canRead() == false) {
                System.out.println("Can't read target directory.");
            }
             */
        }
        if (targetDir.exists() == false) return;

        /*
        // Traverse target directory to find files in it.
        String flist[] = targetDir.list();
        Vector foundFiles = new Vector();
        for (int i=0; i < flist.length; ++i) {
            File file = new File(targetDir.getPath(), flist[i]);
            if (file.isFile() == false) {
                // target object is not a file
                continue;
            }
            // Append the file to the queue.
            foundFiles.addElement(file.getPath());
        }
         */
        foundFiles = new Vector();
        traverseDir(targetDir);
        
        int num = foundFiles.size();
        if (num <= 0) return;
        
        int nEven = (int)num/2;
        
        int nRow = nEven;
        if (num%2 == 1) {
            nRow++;
        }
        
        Object[][] vectors = new Object[nRow][2];
        for (int k = 0; k < nEven; ++k) {
            vectors[k][0] = createVector((String)foundFiles.elementAt(2 * k));
            vectors[k][1] = createVector((String)foundFiles.elementAt(2 * k + 1));
        }
        if (num%2 == 1) {
            vectors[nRow-1][0] = createVector((String)foundFiles.lastElement());
            vectors[nRow-1][1] = createVector(
                getClass().getResource("/open/dolphin/resources/images/white.jpg")
            );
        }
        
        // Create table
        model = new ImageTableModel(columnNames, 0);
        imageTable = new JTable(model);
        imageTable.setRowHeight(ROW_HEIGHT);
        imageTable.setCellSelectionEnabled(true);
        imageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        MouseListener action = (MouseListener)(GenericListener.create(
                                    MouseListener.class,
                                    "mouseClicked",
                                    this,
                                    "doMouseClick"));    
        imageTable.addMouseListener(action);
        
        // change the cell renderer to my own
        imageTable.setDefaultRenderer(model.getColumnClass(0), new MyCellRenderer());
        //
        if (imageView == null) {
            imageView = new JScrollPane();
        }
        imageView.setViewportView(imageTable);
        imageView.setPreferredSize(new Dimension(360,480));
        imageView.setMinimumSize(new Dimension(360,480));
        imageView.setMaximumSize(new Dimension(360,480));
        this.add(imageView);
        //
        for (int i=0; i < vectors.length; ++i) {
            model.addRow(vectors[i]);
        }
        //
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(imageTable, DnDConstants.ACTION_COPY_OR_MOVE, this); 
    }

    private LDAPConnection conn = null;

    public synchronized LDAPConnection getConnection() {
        if (conn != null && conn.isConnected()) {
            return conn;
        }
        
        try {
            conn = new LDAPConnection();
            //conn.connect("hakkoda.digital-globe.co.jp", 389, "cn=Manager,o=digital-globe","hanagui+");
            conn.connect("133.95.88.222", 389, "cn=Directory Manager,o=digital-globe","secret");
            //conn.connect("localhost", 389, "cn=Directory Manager,o=digital-globe","secret");
            return conn;
        } catch (LDAPException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    public synchronized void disconnectLDAP() {
        if (conn != null && conn.isConnected()) {
            try {
                conn.disconnect();
                conn = null;
            } catch (LDAPException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void showWarning(String message) {
        String title = "警告";
        int type = JOptionPane.WARNING_MESSAGE;
        int option = JOptionPane.DEFAULT_OPTION;
        Object[] options = {"OK"};
        JOptionPane op = new JOptionPane(message,type,option,null,options);
        JDialog dlg = op.createDialog(SchemaStockTable.this, title);
        dlg.setVisible(true);
    }
    
    JProgressBar progress = new JProgressBar();
    JLabel status = new JLabel();
    
    SchemaSTStartupTask task = null;
    javax.swing.Timer timer = null;
    
    SchemaSTStartupTask2 task2 = null;
    javax.swing.Timer timer2 = null;
    
    SchemaSTUploadTask uploadTask = null;
    javax.swing.Timer uploadTimer = null;
    
    public void maintenanceButton(String path) {
        if (path != null) {
            //
            if (uploadButton != null) {
                uploadButton.setEnabled(false);
            }
            if (imageTable != null) {
                if (imageTable.getSelectedRow() >= 0 && imageTable.getSelectedColumn() >= 0) {
                    if (uploadButton != null) {
                        uploadButton.setEnabled(true);
                    }
                }
            }
            //
            //=======================================================
            // store selected directory to the schema_directory.prop
            //DolphinContext.saveSchemaDirectory(path);//////////////////////////////////////////
            saveSchemaDirectory(path);
            //=======================================================
        } else {
            if (uploadButton != null) {
                uploadButton.setEnabled(false);
            }
            if (imageTable != null) {
                if (imageTable.getSelectedRow() >= 0 && imageTable.getSelectedColumn() >= 0) {
                    if (uploadButton != null) {
                        uploadButton.setEnabled(true);
                    }
                }
            }
            //
            //=======================================================
            // store selected directory to the schema_directory.prop
            // (in case that this class is used as a part of dolphin client program...)
            //DolphinContext.clearSchemaDirectory();/////////////////////////////////////////////
            clearSchemaDirectory();
            //=======================================================
        }
        
        SchemaStockTable.this.validate();
    }
    
    public boolean askParameters(Image small, String[] f) {
        String title = "シェーマ情報入力";
        int type = JOptionPane.QUESTION_MESSAGE;
        int option = JOptionPane.OK_CANCEL_OPTION;
        String message = "シェーマ情報";
        //JPanel pnl = new JPanel(new GridLayout(3,2,5,5));
        JPanel pnl = new JPanel();
        pnl.setMinimumSize(new Dimension(320,120));
        pnl.setPreferredSize(new Dimension(320,120));
        TitledBorder border = new TitledBorder((String)message);
        pnl.setBorder(border);

        pnl.add(new JLabel("項目名　"));
        JTextField fld1 = new JTextField("");
        fld1.setMaximumSize(new Dimension(240,24));
        fld1.setMinimumSize(new Dimension(240,24));
        fld1.setPreferredSize(new Dimension(240,24));
        pnl.add(fld1);

        pnl.add(new JLabel("作成者　"));
        JTextField fld2 = new JTextField("");
        fld2.setMaximumSize(new Dimension(240,24));
        fld2.setMinimumSize(new Dimension(240,24));
        fld2.setPreferredSize(new Dimension(240,24));
        pnl.add(fld2);

        pnl.add(new JLabel("コメント"));
        JTextField fld3 = new JTextField("");
        fld3.setMaximumSize(new Dimension(240,24));
        fld3.setMinimumSize(new Dimension(240,24));
        fld3.setPreferredSize(new Dimension(240,24));
        pnl.add(fld3);

        int result = javax.swing.JOptionPane.showConfirmDialog(
            SchemaStockTable.this, 
            pnl, title, option, type, 
            new ImageIcon(small)
        );
        if (result != 0) {
            // canceled
            return false;
        }
        
        //
        if (fld1.getText().length() > 0) {
            f[0] = fld1.getText();
        } else {
            f[0] = " ";
        }
        //
        if (fld2.getText().length() > 0) {
            f[1] = fld2.getText();
        } else {
            f[1] = " ";
        }
        //
        if (fld3.getText().length() > 0) {
            f[2] = fld3.getText();
        } else {
            f[2] = " ";
        }

        return true;
    }
    
    public void upload(LDAPAttributeSet attrs) {
        if (imageTable == null) return;
        int sRow = imageTable.getSelectedRow();
        int sCol = imageTable.getSelectedColumn();
        if (sRow < 0 || sCol < 0) return;
        System.out.println("UPLOAD {" + sRow + ", " + sCol + "}");
        Vector v = (Vector)imageTable.getValueAt(sRow,sCol);
        
        Image small = (Image)v.elementAt(0);
        Image large = (Image)v.elementAt(1);
        if (small == null || large == null) {
            return;
        }    
        
        try {
            String uid = UUID.generateUUID().replaceAll("-","");         
            attrs.add(new LDAPAttribute("objectclass", "person"));
            attrs.add(new LDAPAttribute("objectclass", "organizationalperson"));
            attrs.add(new LDAPAttribute("objectclass", "inetorgperson"));
            attrs.add(new LDAPAttribute("uid", uid));
            
            //----------------------------------------------------------------
            // image data
            byte[] ba = SchemaUtil.convertToJpegData(large, SchemaStockTable.this);
            if (ba == null) {
                showWarning("JPEGデータを作成できませんでした。");
            }
            System.out.println("byte array length: " + ba.length);
            if (ba.length > 131072) {
                showWarning(
                    (String)"登録するJPEG画像サイズ\n" + ba.length + " バイト\nはサーバに格納できる128KB制限を超えています。"
                );
                //System.out.println("*** Data size exceeded 128KB limit.");
                return;
            }
            attrs.add(new LDAPAttribute("jpegPhoto", ba));
            
            //----------------------------------------------------------------
            // thumbnail
            ba = SchemaUtil.convertToJpegData(small, SchemaStockTable.this);
            if (ba == null) {
                showWarning("JPEGデータを作成できませんでした。");
            }
            System.out.println("byte array length: " + ba.length);
            if (ba.length > 131072) {
                showWarning(
                    (String)"登録するJPEG画像サイズ\n" + ba.length + " バイト\nはサーバに格納できる128KB制限を超えています。"
                );
                //System.out.println("*** Data size exceeded 128KB limit.");
                return;
            }
            attrs.add(new LDAPAttribute("userPassword", ba));
            //-----------------------------------------------------------------
            
            String dn = "uid=" + uid + ",ou=Schema,ou=Library,o=digital-globe";
            //String dn = "uid=" + uid + ",ou=Schema,ou=library,o=digital-globe";

            System.out.println("TARGET dn: " + dn);
            
            LDAPEntry entry = new LDAPEntry(dn, attrs);
            LDAPConnection ld = getConnection();
            if (ld == null) {
                showWarning("LDAPサーバーに接続できません。");
                System.out.println("*** Couldn't create image directory.");
                disconnectLDAP();
                return;
            }

            ld.add(entry);
            Thread.sleep(100);

            System.out.println("*** Image directory was created at " + dn);

            disconnectLDAP();
            return;
        } catch (Exception e) {
            showWarning(e.getMessage());
            System.out.println("*** Couldn't create image entry.");
            e.printStackTrace();
            disconnectLDAP();
            return;
        }
    }
    
    public void addButtons() {
        status.setPreferredSize(new Dimension(180,14));
        this.add(status);

        //setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        progress.setPreferredSize(new Dimension(180,14));
        this.add(progress);

        //--------------------------------------------
        JButton btn = new JButton();
        btn.setText("フォルダ選択...");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        JFileChooser chooser = new JFileChooser("/");
                        if (chooser == null) return;
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        String path;
                        int selected = chooser.showOpenDialog(null);
                        if (selected == JFileChooser.APPROVE_OPTION) {
                            File file = chooser.getSelectedFile();
                            path = file.getPath();
                            //
                            if (dragSource != null) {
                                dragSource = null;
                            }
                            if (imageTable != null) {
                                imageView.remove(imageTable);
                                imageTable = null;
                            }
                            if (model != null) {
                                model = null;
                            }
                            //
                            startTableTask2(path);
                            //loadSchema(path);
                            //maintenanceButton(path);
                        } else if (selected == JFileChooser.CANCEL_OPTION) {
                            return;
                        }
                    }
                });
            }
        });
        this.add(btn);
        //--------------------------------------------
        
        //--------------------------------------------
        // hey, this is the clear button so that the user can dismiss its own directory.
        JButton btn2 = new JButton();
        btn2.setText("リセット");
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        if (dragSource != null) {
                            dragSource = null;
                        }
                        if (imageTable != null) {
                            imageView.remove(imageTable);
                            imageTable = null;
                        }
                        if (model != null) {
                            model = null;
                        }
                        //
                        startTableTask2(null);
                        //loadDefaultSchema();
                        //maintenanceButton(null);
                    }
                });
            }
        });
        this.add(btn2);
        //--------------------------------------------

        //--------------------------------------------
        // hey, this is the clear button so that the user can dismiss its own directory.
        uploadButton = new JButton();
        uploadButton.setText("アップロード");
        uploadButton.setEnabled(false);
        if (imageTable != null) {
            if (imageTable.getSelectedRow() >= 0 && imageTable.getSelectedColumn() >= 0) {
                uploadButton.setEnabled(true);
            }
        }
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                
                // demo user
                if (Project.getUserId().equals("demo")) {
                     JOptionPane.showMessageDialog(SchemaStockTable.this,
                                     (String)"この機能はデモユーザでは利用できません。",
                                     "Dolphin: 画像登録",
                                     JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                SwingUtilities.invokeLater(new Runnable(){
                    public void run() {
                        //upload();
                        uploadTask();
                    }
                });
            }
        });
        this.add(uploadButton);
        //--------------------------------------------
    }
    
    public SchemaStockTable(/*String schemaFolder*/) {
        super();        
        
        addButtons();
        
        
        startTableTask(/*schemaFolder*/);
        
        /*
        if (schemaFolder != null) {
            // load image files in this directory
            loadSchema(schemaFolder);
        } else {
            loadDefaultSchema();
        }
         */
    }
    
    public void startTableTask(/*String schemaFolder*/) {
        String schemaFolder = getSchemaDirectory();
        //System.out.println("schemaFolder: " + schemaFolder);
        
        task = new SchemaSTStartupTask(SchemaStockTable.this, schemaFolder);
        progress.setMinimum(0);
        progress.setMaximum(task.getLengthOfTask());
        progress.setValue(progress.getMinimum());
        status.setText("シェーマを読み込み中...");
        
        timer = new javax.swing.Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                progress.setValue(task.getCurrent());
                status.setText(task.getMessage());
                
                if (task.done()) {
                    //Toolkit.getDefaultToolkit().beep();
                    timer.stop();
                    // stop indeterminate progress bar
                    progress.setIndeterminate(false);
                    progress.setValue(progress.getMinimum());
                    status.setText("");
                    //btnSearch.setEnabled(true);
                }
            }
        });

        //btnSearch.setEnabled(false);
        // start indeterminate progress bar
        progress.setIndeterminate(true);
        task.go();
        timer.start();
    }
    
    public void startTableTask2(String schemaFolder) {
        task2 = new SchemaSTStartupTask2(SchemaStockTable.this, schemaFolder);
        progress.setMinimum(0);
        progress.setMaximum(task2.getLengthOfTask());
        progress.setValue(progress.getMinimum());
        status.setText("シェーマを読み込み中...");
        
        timer2 = new javax.swing.Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                progress.setValue(task2.getCurrent());
                status.setText(task2.getMessage());
                
                if (task2.done()) {
                    //Toolkit.getDefaultToolkit().beep();
                    timer2.stop();
                    // stop indeterminate progress bar
                    progress.setIndeterminate(false);
                    progress.setValue(progress.getMinimum());
                    status.setText("");
                    //btnSearch.setEnabled(true);
                }
            }
        });

        //btnSearch.setEnabled(false);
        // start indeterminate progress bar
        progress.setIndeterminate(true);
        task2.go();
        timer2.start();
    }
    
    public void uploadTask() {
        if (imageTable == null) return;
        int sRow = imageTable.getSelectedRow();
        int sCol = imageTable.getSelectedColumn();
        if (sRow < 0 || sCol < 0) return;
        Vector v = (Vector)imageTable.getValueAt(sRow,sCol);        
        Image small = (Image)v.elementAt(0);
        if (small == null) {
            return;
        }    

        LDAPAttributeSet attrs = new LDAPAttributeSet();
        
        String[] f = new String[3];
        if (true == askParameters(small, f)) {
            //System.out.println("f1" + f[0]);
            //System.out.println("f2" + f[1]);
            //System.out.println("f3" + f[2]);
            //
            attrs.add(new LDAPAttribute("cn", f[0]));
            attrs.add(new LDAPAttribute("sn", f[1]));
            attrs.add(new LDAPAttribute("description", f[2]));
        } else {
            return;
        }

        //===============================================================
        
        uploadTask = new SchemaSTUploadTask(SchemaStockTable.this, attrs);
        progress.setMinimum(0);
        progress.setMaximum(uploadTask.getLengthOfTask());
        progress.setValue(progress.getMinimum());
        status.setText("画像をアップロード...");
        
        uploadTimer = new javax.swing.Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                progress.setValue(uploadTask.getCurrent());
                status.setText(uploadTask.getMessage());
                
                if (task.done()) {
                    //Toolkit.getDefaultToolkit().beep();
                    uploadTimer.stop();
                    // stop indeterminate progress bar
                    progress.setIndeterminate(false);
                    progress.setValue(progress.getMinimum());
                    status.setText("");
                    
                    //btnSearch.setEnabled(true);
                }
            }
        });

        //btnSearch.setEnabled(false);
        // start indeterminate progress bar
        progress.setIndeterminate(true);
        uploadTask.go();
        uploadTimer.start();
    }
    
    private Vector generateVector (String name) {
        // create the icon
        URL url = getClass().getResource("/open/dolphin/resources/schema/" + name);
        ImageIcon srcIcon = new ImageIcon(url);
        if (srcIcon == null) return null;
        // store original file path
        srcIcon.setDescription(url.getPath());
        // get the image of the icon
        Image img = srcIcon.getImage();
        // get the resized image (thumbnail)
        Image dstImg = SchemaUtil.scaleImage(srcIcon, ROW_HEIGHT);
        if (dstImg == null) {
            img = null;
            return null;
        }

        // pack the two images to the vector
        Vector v = new Vector();
        v.addElement(dstImg);// small image (thumbnail)
        v.addElement(img);// large image (original)
        return v;
    }

    public void loadDefaultSchema() {
        Object[][] vectors = { 
            {generateVector ("img01.JPG"), generateVector ("img02.JPG")},
            {generateVector ("img03.JPG"), generateVector ("img04.JPG")},
            {generateVector ("img05.JPG"), generateVector ("img06.JPG")},
            {generateVector ("img07.JPG"), generateVector ("img08.JPG")},
            {generateVector ("img09.JPG"), generateVector ("img10.JPG")},
            {generateVector ("img11.JPG"), generateVector ("img12.JPG")},
            {generateVector ("img13.JPG"), generateVector ("img14.JPG")},
            {generateVector ("img15.JPG"), generateVector ("img16.JPG")},
            {generateVector ("img17.JPG"), generateVector ("img18.JPG")},
            {generateVector ("img19.JPG"), generateVector ("img20.JPG")},
            {generateVector ("img21.JPG"), generateVector ("img22.JPG")},
            {generateVector ("img23.JPG"), generateVector ("img24.JPG")},
            {generateVector ("img25.JPG"), generateVector ("img26.JPG")},
            {generateVector ("img27.JPG"), generateVector ("img28.JPG")},
            {generateVector ("img29.JPG"), generateVector ("img30.JPG")},
            {generateVector ("img31.JPG"), generateVector ("img32.JPG")},
            {generateVector ("img33.JPG"), generateVector ("img34.JPG")},
            {generateVector ("img35.JPG"), generateVector ("img36.JPG")},
            {generateVector ("img37.JPG"), generateVector ("img38.JPG")},
            {generateVector ("img39.JPG"), generateVector ("img40.JPG")},
            {generateVector ("img41.JPG"), generateVector ("img42.JPG")},
            {generateVector ("img43.JPG"), generateVector ("img44.JPG")},
            {generateVector ("img45.JPG"), generateVector ("img46.JPG")},
            {generateVector ("img47.JPG"), generateVector ("img48.JPG")},
            {generateVector ("img49.JPG"), generateVector ("img50.JPG")},
            {generateVector ("img51.JPG"), generateVector ("img52.JPG")},
            {generateVector ("img53.JPG"), generateVector ("img54.JPG")},
            {generateVector ("img55.JPG"), generateVector ("img56.JPG")},
            {generateVector ("img57.JPG"), generateVector ("img58.JPG")}
        };
        
        // Create table
        model = new ImageTableModel(columnNames, 0);
        imageTable = new JTable(model);
        imageTable.setRowHeight(ROW_HEIGHT);
        imageTable.setCellSelectionEnabled(true);
        
        MouseListener action = (MouseListener)(GenericListener.create(
                                    MouseListener.class,
                                    "mouseClicked",
                                    this,
                                    "doMouseClick"));    
        imageTable.addMouseListener(action);
        
        // change the cell renderer to my own
        imageTable.setDefaultRenderer(model.getColumnClass(0), new MyCellRenderer());
        //
        if (imageView == null) {
            imageView = new JScrollPane();
        }
        imageView.setViewportView(imageTable);
        imageView.setPreferredSize(new Dimension(360,480));
        this.add(imageView);
        //
        for (int i=0; i < vectors.length; ++i) {
            model.addRow(vectors[i]);
        }
        //
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(imageTable, DnDConstants.ACTION_COPY_OR_MOVE, this); 
    }

    ////////////////  Drag Support /////////////////////

    public void dragGestureRecognized(DragGestureEvent event) {
        /*
         try {
            int row = imageTable.getSelectedRow();
            int col = imageTable.getSelectedColumn();
            if (row != -1 && col != -1) {
                ImageIcon icon = (ImageIcon)imageTable.getValueAt(row, col);
                String title = (String)icon.getDescription ();
                Transferable t = new StringSelection(title);
                dragSource.startDrag(event, DragSource.DefaultCopyDrop, t, this);
            }
        }
         */
        
        try {
            int row = imageTable.getSelectedRow();
            int col = imageTable.getSelectedColumn();
            if (row != -1 && col != -1) {
                /*
                 ImageIcon icon = (ImageIcon)imageTable.getValueAt(row, col);
                 Transferable t = new ImageIconTransferable(icon);
                 */

                // get the vector at the drag target cell
                Vector v = (Vector)imageTable.getValueAt(row,col);
                // get the original image
                Image large = (Image)v.lastElement();
                // create transeferable image
                Transferable t = new ImageSelection(    
                    large,
                    large.getWidth(this),
                    large.getHeight(this)
                );
                dragSource.startDrag(event, DragSource.DefaultCopyDrop, t, this);
            }
        }     
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void dragDropEnd(DragSourceDropEvent event) { 
    }

    public void dragEnter(DragSourceDragEvent event) {
        //DragSourceContext DolphinContext = event.getDragSourceContext(); 
        //DolphinContext.setCursor(DragSource.DefaultCopyDrop);
    }

    public void dragOver(DragSourceDragEvent event) {
        //DragSourceContext DolphinContext = event.getDragSourceContext(); 
        //DolphinContext.setCursor(DragSource.DefaultCopyDrop);
    }
    
    public void dragExit(DragSourceEvent event) {
    }    

    public void dropActionChanged ( DragSourceDragEvent event) {
    }
    
    public final void doMouseClick (MouseEvent e) {
        JTable table = (JTable) e.getSource ();
        int row = table.getSelectedRow ();
        int col = table.getSelectedColumn ();
    }
   
   /**
    * テーブルモデルクラス。
    */
   class ImageTableModel extends DefaultTableModel {
      public ImageTableModel (String[] columnNames, int rows) {
         super (columnNames, rows);
      }
      
      public Class getColumnClass (int col) {
         return javax.swing.ImageIcon.class;
      }
      
      /**
       * セルが編集可能かどうかを返す。
       */
      public boolean isCellEditable (int row, int col) {
         return false;
      }
   }
   
   /*
   private ImageIcon createImageIcon (String name) {
      URL url = this.getClass().getResource("/open/dolphin/resources/schema/" +  name);      
      return new ImageIcon (url);
   }
    */
   
   //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
   //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
   //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
   
   //String installedDir = "c:/dolphin";//////////////////////
   String installedDir = ClientContext.getUserDirectory();
   
   public String getSchemaDirectory() {
        File f = new File(installedDir + File.separator + "schema_directory.prop");
        if ( ! f.exists()) {
            return null;             
        }
        
        String result = null;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
            Properties env = new Properties();
            env.load(in);
            in.close();
            //============================================
            result = env.getProperty("schemaDirectory");
            //============================================
        }
        catch (Exception e) {
            result = null;
        }
        return result;
    }
    
    public void saveSchemaDirectory(String val) {
        try {
            //=================================
            Properties env = new Properties();
            env.put("schemaDirectory", val);
            //=================================
            File f = new File(installedDir + File.separator + "schema_directory.prop");
            if ( f.exists()) {
                f.delete();      
            }
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
            env.store(out, "Information about schema directory is stored.");
            out.close();
        }
        catch (Exception e) {            
        }
    }
    
    public void clearSchemaDirectory() {
        try {
            File f = new File(installedDir + File.separator + "schema_directory.prop");
            if ( f.exists()) {
                f.delete();      
            }
        }
        catch (Exception e) {            
        }
    }
}