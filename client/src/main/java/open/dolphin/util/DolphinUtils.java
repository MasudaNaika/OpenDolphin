package open.dolphin.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 * JDK11 migration関連など
 * 
 * @author masuda, Masuda Naika
 */
public class DolphinUtils {
    
    private static final boolean JAVA8;
    
    private static final int MENU_SHORTCUT_KEY_MASK_EX;
    
    static {
        String javaVer = System.getProperty("java.version");
        if (javaVer != null) {
            JAVA8 = javaVer.startsWith("1.");
        } else {
            JAVA8 = true;
        }
        MENU_SHORTCUT_KEY_MASK_EX = setMenuShortcutKeyMaskEx();
    }
    
    public static boolean isJava8() {
        return JAVA8;
    }
    
    public static int getMenuShortcutKeyMaskEx() {
        return MENU_SHORTCUT_KEY_MASK_EX;
    }
    
    private static int setMenuShortcutKeyMaskEx() {
        // Event.CTRL_MASK = 1 << 1, windows
        if (isJava8()) {
            int modifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
            if (modifier == java.awt.Event.META_MASK) {
                return InputEvent.META_DOWN_MASK;
            }
            return InputEvent.CTRL_DOWN_MASK;
        } else {
            // JDK11
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
//            try {
//                Toolkit toolkit = Toolkit.getDefaultToolkit();
//                Method mt = toolkit.getClass().getDeclaredMethod("getMenuShortcutKeyMaskEx");
//                return (int) mt.invoke(toolkit);
//            } catch (Exception ex) {
//            }
//            return InputEvent.CTRL_DOWN_MASK;
        }
    }

    public static Rectangle modelToView(JTextComponent tc, int pos) throws BadLocationException {
        
        if (isJava8()) {
            return tc.modelToView(pos);
        } else {
            Rectangle2D r2d = tc.modelToView2D(pos);
            return r2d.getBounds();
//            try {
//                Method mt = tc.getClass().getDeclaredMethod("modelToView2D", int.class);
//                Rectangle2D r2d = (Rectangle2D) mt.invoke(tc, pos);
//                return r2d.getBounds();
//            } catch (Exception ex) {
//                return null;
//            }
        }
    }
    
    public static int viewToModel(JTextComponent tc, Point p) {

        if (isJava8()) {
            return tc.viewToModel(p);
        } else {
            return tc.viewToModel2D(p);
//            try {
//                Method mt = tc.getClass().getDeclaredMethod("viewToModel2D", Point.class);
//                return (int) mt.invoke(tc, p);
//            } catch (Exception ex) {
//                return -1;
//            }
        }
    }

}
