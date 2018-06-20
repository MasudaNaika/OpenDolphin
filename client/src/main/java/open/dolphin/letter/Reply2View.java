package open.dolphin.letter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import open.dolphin.client.Panel2;

/**
 * Reply2View改
 *
 * @author masuda, Masuda Naika
 */
public class Reply2View extends Panel2 {

    private JLabel atesakiLbl;
    private JTextField clientDept;
    private JTextField clientDoctor;
    private JTextField clientHospital;
    private JTextField confirmed;
    private JTextField consultantAddress;
    private JTextField consultantDoctor;
    private JTextField consultantHospital;
    private JTextField consultantTelephone;
    private JTextArea informedContent;
    private JScrollPane scrl_content;
    private JLabel lbl_title;
    private JLabel lbl_houkokuShimasu;
    private JLabel lbl_keigu;
    private JLabel lbl_shoken;
    private JLabel lbl_telephone;
    private JLabel lbl_consultantDr;
    private JLabel lbl_stamp;
    private JLabel lbl_clientDept;
    private JLabel lbl_haikei;
    private JLabel lbl_jikaMasumasu;
    private JLabel lbl_tono;
    private JLabel lbl_kakkoTen;
    
    private JTextField patientBirthday;
    private JTextField patientName;
    private JTextField visited;
    
    public Reply2View() {
        initComponents();
    }
    
    private void initComponents() {
        
        lbl_title = new JLabel("ご　報　告（フォーム）");
        clientHospital = new JTextField(30);
        clientDept = new JTextField(15);
        lbl_clientDept = new JLabel("科");
        clientDoctor = new JTextField(10);
        atesakiLbl = new JLabel("先生　御机下");
        lbl_haikei = new JLabel("拝啓");
        lbl_jikaMasumasu = new JLabel("時下ますますご清祥の段、お慶び申し上げます。");
        visited = new JTextField(10);
        lbl_houkokuShimasu = new JLabel("に受診されました。下記ご報告させていただきます。");
        informedContent = new JTextArea();
        informedContent.setLineWrap(true);
        scrl_content = new JScrollPane(informedContent);
        lbl_shoken = new JLabel("所見等");
        confirmed = new JTextField(10);
        confirmed.setHorizontalAlignment(JTextField.RIGHT);
        confirmed.setEditable(false);
        consultantAddress = new JTextField(30);
        consultantAddress.setHorizontalAlignment(JTextField.RIGHT);
        consultantAddress.setEditable(false);
        consultantTelephone = new JTextField(10);
        consultantTelephone.setHorizontalAlignment(JTextField.RIGHT);
        consultantTelephone.setEditable(false);
        lbl_telephone = new JLabel("電話");
        consultantHospital = new JTextField(30);
        consultantHospital.setHorizontalAlignment(JTextField.RIGHT);
        consultantHospital.setEditable(false);
        consultantDoctor = new JTextField(10);
        consultantDoctor.setHorizontalAlignment(JTextField.RIGHT);
        consultantDoctor.setEditable(false);
        lbl_consultantDr = new JLabel("担当");
        lbl_stamp = new JLabel("印");
        patientName = new JTextField(10);
        patientName.setEditable(false);
        lbl_tono = new JLabel("殿（生年月日:");
        patientBirthday = new JTextField(15);
        patientBirthday.setEditable(false);
        lbl_kakkoTen = new JLabel("）、");
        lbl_keigu = new JLabel("敬具");
        
        final int vgap = 0;
        final int hgap = 0;
        
        // GroupLayout の生成
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JPanel sub = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sub.add(lbl_title);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(clientHospital);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(clientDept);
        sub.add(lbl_clientDept);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(clientDoctor);
        sub.add(atesakiLbl);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(lbl_haikei);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(lbl_jikaMasumasu);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(clientDoctor);
        sub.add(atesakiLbl);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(patientName);
        sub.add(lbl_tono);
        sub.add(patientBirthday);
        sub.add(lbl_kakkoTen);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(visited);
        sub.add(lbl_houkokuShimasu);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        sub.add(lbl_keigu);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        sub.add(lbl_shoken);
        add(sub);
        
        sub = new JPanel(new BorderLayout());
        sub.add(scrl_content, BorderLayout.CENTER);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        sub.add(confirmed);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        sub.add(consultantAddress);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        sub.add(lbl_telephone);
        sub.add(consultantTelephone);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        sub.add(consultantHospital);
        add(sub);
        
        sub = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        sub.add(lbl_consultantDr);
        sub.add(consultantDoctor);
        sub.add(lbl_stamp);
        add(sub);

    }
    
    public JTextField getClientDept() {
        return clientDept;
    }

    public void setClientDept(JTextField clientDept) {
        this.clientDept = clientDept;
    }

    public JTextField getClientDoctor() {
        return clientDoctor;
    }

    public void setClientDoctor(JTextField clientDoctor) {
        this.clientDoctor = clientDoctor;
    }

    public JTextField getClientHospital() {
        return clientHospital;
    }

    public void setClientHospital(JTextField clientHospital) {
        this.clientHospital = clientHospital;
    }

    public JTextField getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(JTextField confirmed) {
        this.confirmed = confirmed;
    }

    public JTextField getConsultantAddress() {
        return consultantAddress;
    }

    public void setConsultantAddress(JTextField consultantAddress) {
        this.consultantAddress = consultantAddress;
    }

    public JTextField getConsultantDoctor() {
        return consultantDoctor;
    }

    public void setConsultantDoctor(JTextField consultantDoctor) {
        this.consultantDoctor = consultantDoctor;
    }

    public JTextField getConsultantHospital() {
        return consultantHospital;
    }

    public void setConsultantHospital(JTextField consultantHospital) {
        this.consultantHospital = consultantHospital;
    }

    public JTextField getConsultantTelephone() {
        return consultantTelephone;
    }

    public void setConsultantTelephone(JTextField consultantTelephone) {
        this.consultantTelephone = consultantTelephone;
    }

    public JTextArea getInformedContent() {
        return informedContent;
    }

    public void setInformedContent(JTextArea informedContent) {
        this.informedContent = informedContent;
    }

    public JTextField getPatientBirthday() {
        return patientBirthday;
    }

    public void setPatientBirthday(JTextField patientBirthday) {
        this.patientBirthday = patientBirthday;
    }

    public JTextField getPatientName() {
        return patientName;
    }

    public void setPatientName(JTextField patientName) {
        this.patientName = patientName;
    }

    public JTextField getVisited() {
        return visited;
    }

    public void setVisited(JTextField visited) {
        this.visited = visited;
    }

    public JLabel getAtesakiLbl() {
        return atesakiLbl;
    }

}
