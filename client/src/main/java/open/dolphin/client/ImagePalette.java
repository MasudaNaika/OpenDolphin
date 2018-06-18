package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.*;

/**
 * ImagePalette
 *
 * @author Minagawa,Kazushi. Digital Globe, Inc.
 */
public class ImagePalette extends JPanel implements DragSourceListener, DragGestureListener {

    private static final int DEFAULT_COLUMN_COUNT = 3;
    private static final int DEFAULT_IMAGE_WIDTH = 120;
    private static final int DEFAULT_IMAGE_HEIGHT = 120;
    private static final String[] DEFAULT_IMAGE_SUFFIX = {".jpg"};
    private static final int RES_COUNT = 57;
    private static final String RES_EXTENTION = ".JPG";
    private static final String RES_PREFIX = "img";
    private static final String RES_BASE = "/open/dolphin/resources/schema/";

    private int imageWidth;
    private int imageHeight;

    private DragSource dragSource;
    private Path imageDirectory;
    private String[] suffix = DEFAULT_IMAGE_SUFFIX;

    private ImageEntryJList<ImageEntry> imageList;
    private DefaultListModel<ImageEntry> jlistModel;

    public ImagePalette(String[] columnNames, int columnCount, int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        initComponent(columnCount);
    }

    public ImagePalette() {
        this(null, DEFAULT_COLUMN_COUNT, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
    }

    public String[] getimageSuffix() {
        return suffix;
    }

    public void setImageSuffix(String[] suffix) {
        this.suffix = suffix;
    }

    public void setupDefaultSchema() {

        for (int j = 1; j <= RES_COUNT; j++) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(RES_BASE);
                sb.append(RES_PREFIX);
                if (j < 10) {
                    sb.append("0");
                }
                sb.append(j).append(RES_EXTENTION);
                URL url = this.getClass().getResource(sb.toString());
                ImageEntry entry = new ImageEntry();
                entry.setUrl(url.toString());
                setImageIcon(entry);
                jlistModel.addElement(entry);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }

    }

    public Path getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(Path imageDirectory) {
        this.imageDirectory = imageDirectory;
        refresh();
    }

    public void dispose() {
        jlistModel.clear();
    }

    public void refresh() {

        if (imageDirectory == null || !Files.isDirectory(imageDirectory)) {
            return;
        }

        try {
            DirectoryStream<Path> ds = Files.newDirectoryStream(imageDirectory);
            for (Path path : ds) {
                String fileName = path.getFileName().toString();
                if (fileName.startsWith(".") || fileName.startsWith("__M") || Files.size(path) == 0L) {
                    continue;
                }
                URI uri = path.toUri();
                URL url = uri.toURL();
                ImageEntry entry = new ImageEntry();
                entry.setUrl(url.toString());
                setImageIcon(entry);
                jlistModel.addElement(entry);
            }
        } catch (Exception e) {
        }

    }

    private void initComponent(int columnCount) {

        // ListModelとListを設定
        jlistModel = new DefaultListModel();
        imageList = new ImageEntryJList(jlistModel);
        imageList.setMaxIconTextWidth(DEFAULT_IMAGE_WIDTH);

        JScrollPane scroll = new JScrollPane(imageList);
        scroll.getVerticalScrollBar().setUnitIncrement(imageHeight / 2);
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);

        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(imageList, DnDConstants.ACTION_COPY_OR_MOVE, this);
        
        // DragSourseを使う場合はsetDragEnabled(false)する
        imageList.setDragEnabled(false);

    }
    
    private void setImageIcon(ImageEntry entry) throws MalformedURLException {
        URL url = new URL(entry.getUrl());
        ImageIcon ic = new ImageIcon(url);
        ImageIcon icon = adjustImageSize(ic, imageWidth, imageHeight);
        entry.setImageIcon(icon);
        entry.setIconText(null);
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent event) {
    }

    @Override
    public void dragEnter(DragSourceDragEvent event) {
    }

    @Override
    public void dragOver(DragSourceDragEvent event) {
    }

    @Override
    public void dragExit(DragSourceEvent event) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent event) {
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {

        try {
            ImageEntry entry = imageList.getSelectedValue();
            if (entry != null) {
                Transferable t = new ImageEntryTransferable(entry);
                dragSource.startDrag(event, DragSource.DefaultCopyDrop, t, this);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }


    private ImageIcon adjustImageSize(ImageIcon icon, int width, int height) {

        if ((icon.getIconHeight() > height) || (icon.getIconWidth() > width)) {
            Image img = icon.getImage();
            float hRatio = (float) icon.getIconHeight() / height;
            float wRatio = (float) icon.getIconWidth() / width;
            int h, w;
            if (hRatio > wRatio) {
                h = height;
                w = (int) (icon.getIconWidth() / hRatio);
            } else {
                w = width;
                h = (int) (icon.getIconHeight() / wRatio);
            }
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);

        } else {
            return icon;
        }
    }
}
