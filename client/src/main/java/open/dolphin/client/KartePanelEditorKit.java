package open.dolphin.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import javax.swing.text.*;

/**
 * 改行文字を表示するEditorKit
 *
 * @author masuda, Masuda Naika
 * http://terai.xrea.jp/Swing/ParagraphMark.html
 * http://abebas.sub.jp/java/JavaPrograming/01_Editor/011.html
 */
public class KartePanelEditorKit extends StyledEditorKit {

    private static final Color COLOR = Color.GRAY;
    //private static final String CR = "↲";
    //private static final String EOF = "◀";
    private static final Image CR_ICON = ClientContext.getImageIcon("cr.png").getImage();
    private static final Image EOF_ICON = ClientContext.getImageIcon("eof.png").getImage();
    private static final int crMargin = 20;

    private final ViewFactory viewFactory;
    
    private static final String FONT_STRIKE_THROUGH = "font-strikethrough";
    
    private final StrikeThroughAction strikeThroughAction;

    private static final KartePanelEditorKit instance;

    static {
        instance = new KartePanelEditorKit();
    }

    public static KartePanelEditorKit getInstance() {
        return instance;
    }
    
    private KartePanelEditorKit() {
        viewFactory = new VisibleCrViewFactory();
        strikeThroughAction = new StrikeThroughAction();
    }

    @Override
    public ViewFactory getViewFactory() {
        return viewFactory;
    }
    
    private static final class VisibleCrViewFactory implements ViewFactory {

        @Override
        public View create(Element elem) {
            
            String kind = elem.getName();
            
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new WrapLabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new MyParagraphView(elem);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new MyComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }
    
    // Thread: Word wraping behaviour in JTextPane since Java 7
    // https://forums.oracle.com/forums/thread.jspa?messageID=10757680
    private static final class WrapLabelView extends LabelView {

        private WrapLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }

    private static final class MyComponentView extends ComponentView {

        private MyComponentView(Element elem) {
            super(elem);
        }

        // KartePane幅より広いスタンプの場合に直後の改行文字がwrapされないように
        @Override
        public float getPreferredSpan(int axis) {
            
            if (getDocument() instanceof KarteStyledDocument) {
                Component comp = getComponent();
                if (axis == View.X_AXIS && comp instanceof ComponentHolder) {
                    return getComponentHolderSpanX(comp);
                }
            }

            return super.getPreferredSpan(axis);
        }

        @Override
        public float getMaximumSpan(int axis) {

            if (getDocument() instanceof KarteStyledDocument) {
                Component comp = getComponent();
                if (axis == View.X_AXIS && comp instanceof ComponentHolder) {
                    return getComponentHolderSpanX(comp);
                }
            }
            
            return super.getMaximumSpan(axis);
        }

        private int getComponentHolderSpanX(Component comp) {
            
            KarteStyledDocument doc = (KarteStyledDocument) getDocument();
            int paneWidth = doc.getKartePane().getTextPane().getWidth() - crMargin;
            int compWidth = comp.getPreferredSize().width;
            
            if (paneWidth > compWidth) {
                return compWidth;
            } else {
                return paneWidth;
            }
        }
    }

    private static final class MyParagraphView extends ParagraphView {

        private MyParagraphView(Element elem) {
            super(elem);
        }

        @Override
        public void paint(Graphics g, Shape a) {
            
            super.paint(g, a);

            Document doc = getDocument();
            if (!(doc instanceof KarteStyledDocument)) {
                return;
            }
            KarteStyledDocument kdoc = (KarteStyledDocument) doc;
            if (!(kdoc.getKartePane().getTextPane().isEditable())) {
                return;
            }

            // 編集可の場合は改行文字を表示する
            try {
                Shape paragraph = modelToView(getEndOffset(), a, Position.Bias.Backward);
                Rectangle r = (paragraph == null) ? a.getBounds() : paragraph.getBounds();
                //int fontHeight = g.getFontMetrics().getHeight();
                Color old = g.getColor();
                g.setColor(COLOR);
                if (getEndOffset() != kdoc.getEndPosition().getOffset()) {
                    //g.drawString(CR, r.x + 1, r.y + (r.height + fontHeight) / 2 - 2);
                    g.drawImage(CR_ICON, r.x + 1, r.y + (r.height - 10) / 2 + 2, null);
                } else {
                    //g.drawString(EOF, r.x + 1, r.y + (r.height + fontHeight) / 2 - 2);
                    g.drawImage(EOF_ICON, r.x + 1, r.y + (r.height - 10) / 2 + 2, null);
                }
                g.setColor(old);
            } catch (BadLocationException e) {
                e.printStackTrace(System.err);
            }
        }
    }
    
    // 取り消し線ハクション
    public void registStrikeThroughAction(JEditorPane editor) {
        editor.getActionMap().put(FONT_STRIKE_THROUGH, strikeThroughAction);
    }
    
    public static class StrikeThroughAction extends StyledTextAction {

        /**
         * Constructs a new ItalicAction.
         */
        public StrikeThroughAction() {
            super(FONT_STRIKE_THROUGH);
        }

        /**
         * Toggles the strikethrough attribute.
         *
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                boolean strikethrough = !StyleConstants.isStrikeThrough(attr);
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setStrikeThrough(sas, strikethrough);
                setCharacterAttributes(editor, sas, false);
            }
        }
    }
}
