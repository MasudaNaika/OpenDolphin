package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;

/**
 * MainView改
 *
 * @author masuda, Masuda Naika
 */
public class MainView extends JPanel{

    private final JLabel dateLbl;
    private final JProgressBar progressBar;
    private final JLabel statusLbl;
    private final JTabbedPane tabbedPane;

    public MainView() {
        
        Font lblFont = new Font(Font.DIALOG, Font.PLAIN, 10);

        dateLbl = new JLabel();
        dateLbl.setFont(lblFont);
        statusLbl = new JLabel();
        statusLbl.setFont(lblFont);
        progressBar = new JProgressBar();
        
        Dimension pbSize = new Dimension(80, 15);
        progressBar.setMinimumSize(pbSize);
        progressBar.setPreferredSize(pbSize);
        progressBar.setMaximumSize(pbSize);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        panel.add(statusLbl);
        panel.add(Box.createHorizontalGlue());
        panel.add(progressBar);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(dateLbl);
        tabbedPane = new JTabbedPane();

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.SOUTH);
        this.add(tabbedPane, BorderLayout.CENTER);

    }


    public JLabel getDateLbl() {
        return dateLbl;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getStatusLbl() {
        return statusLbl;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
