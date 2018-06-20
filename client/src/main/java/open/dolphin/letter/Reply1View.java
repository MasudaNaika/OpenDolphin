package open.dolphin.letter;

import java.awt.BorderLayout;
import javax.swing.*;
import open.dolphin.client.Panel2;

/**
 * Reply1View改
 * 頭の体操ｗ
 * 
 * @author masuda, Masuda Naika
 */
public class Reply1View extends Panel2 {

    private JTextField clientDept;
    private JTextField clientDoctor;
    private JTextField clientHospital;
    private JTextField consultantAddress;
    private JTextField consultantDoctor;
    private JTextField consultantHospital;
    private JTextField consultantTelephone;
    private JTextArea informedContent;
    private JScrollPane scrl_content;
    private JTextField patientBirthday;
    private JTextField patientName;
    private JTextField visitedDate;
    
    private JLabel atesakiLbl;
    private JLabel confirmed;
    private JLabel lbl_title;
    private JLabel lbl_clientHosp;
    private JLabel lbl_clientDept;
    private JLabel lbl_clientTantou;
    private JLabel lbl_ptName;
    private JLabel lbl_ptBirthday;
    private JLabel lbl_visitedDate;
    private JLabel lbl_setsumei;
    private JLabel lbl_thanks;
    private JLabel lbl_telephone;
    private JLabel lbl_stamp;
    private JLabel lbl_consultantTantou;
    
    public Reply1View() {
        initComponents();
    }
    
    private void initComponents() {
        
        lbl_title = new JLabel("紹介患者経過報告書（フォーム）", JLabel.CENTER);
        atesakiLbl = new JLabel("先生　御机下");
        confirmed = new JLabel("年月日");
        lbl_clientHosp = new JLabel("紹介元医療機関:");
        lbl_clientTantou = new JLabel("担当:");
        lbl_clientDept = new JLabel("科");
        lbl_ptName = new JLabel("患者氏名:");
        lbl_ptBirthday = new JLabel("生年月日:");
        lbl_visitedDate = new JLabel("受診年月日:");
        lbl_setsumei = new JLabel("拝見し、下記のとおり説明いたしました。");
        lbl_thanks = new JLabel("ご紹介戴き、ありがとうございました。取り急ぎ返信まで。");
        lbl_telephone = new JLabel("電話");
        lbl_consultantTantou = new JLabel("担当");
        lbl_stamp = new JLabel("印");
        
        clientHospital = new JTextField(30);
        clientDept = new JTextField(15);
        clientDoctor = new JTextField(10);
        patientName = new JTextField(10);
        patientName.setEditable(false);
        patientBirthday = new JTextField(15);
        patientBirthday.setEditable(false);
        visitedDate = new JTextField(10);
        visitedDate.setEditable(false);
        visitedDate.setToolTipText("右クリックでカレンダーから入力できます。PDF作成時、ハイフォンは漢字の年月日に変換されます。");
        informedContent = new JTextArea();
        informedContent.setLineWrap(true);
        scrl_content = new JScrollPane(informedContent);
        consultantAddress = new JTextField(30);
        consultantAddress.setEditable(false);
        consultantAddress.setHorizontalAlignment(JTextField.RIGHT);
        consultantTelephone = new JTextField(10);
        consultantTelephone.setEditable(false);
        consultantTelephone.setHorizontalAlignment(JTextField.RIGHT);
        consultantHospital = new JTextField(30);
        consultantHospital.setEditable(false);
        consultantHospital.setHorizontalAlignment(JTextField.RIGHT);
        consultantDoctor = new JTextField(10);
        consultantDoctor.setEditable(false);
        consultantDoctor.setHorizontalAlignment(JTextField.RIGHT);
        
        // GroupLayout の生成
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        
        // 自動的にコンポーネント間のすき間をあけるようにする
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        // 水平方向のグループ
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        
        hGroup.addGroup(layout.createParallelGroup()
                
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_clientHosp)
                    .addComponent(lbl_clientTantou)
                    .addComponent(lbl_ptName)
                    .addComponent(lbl_visitedDate))
                .addGroup(layout.createParallelGroup()
                    .addComponent(clientHospital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(clientDept, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_clientDept)
                        .addComponent(clientDoctor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(atesakiLbl))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(patientName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_ptBirthday)
                        .addComponent(patientBirthday, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(visitedDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_setsumei))))

            .addComponent(lbl_thanks)
                
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(scrl_content)
                .addComponent(confirmed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(consultantAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(lbl_telephone)
                    .addComponent(consultantTelephone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(consultantHospital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(lbl_consultantTantou)
                    .addComponent(consultantDoctor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_stamp)))
        );
        
        layout.setHorizontalGroup(hGroup);
        
        // 垂直方向のグループ
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addComponent(confirmed);
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_clientHosp)
                .addComponent(clientHospital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_clientTantou)
                .addComponent(clientDept, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_clientDept)
                .addComponent(clientDoctor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(atesakiLbl));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_ptName)
                .addComponent(patientName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_ptBirthday)
                .addComponent(patientBirthday, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_visitedDate)
                .addComponent(visitedDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_setsumei));
        vGroup.addComponent(scrl_content);
        vGroup.addComponent(lbl_thanks);
        vGroup.addComponent(consultantAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_telephone)
                .addComponent(consultantTelephone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addComponent(consultantHospital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_consultantTantou)
                .addComponent(consultantDoctor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_stamp));
        
        layout.setVerticalGroup(vGroup);

        // 全体レイアウト
        setLayout(new BorderLayout());
        add(lbl_title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }
    
    
    public JTextField getClientDept() {
        return clientDept;
    }

    public JTextField getClientDoctor() {
        return clientDoctor;
    }

    public JTextField getClientHospital() {
        return clientHospital;
    }

    public JTextArea getInformedContent() {
        return informedContent;
    }

    public JTextField getPatientBirthday() {
        return patientBirthday;
    }

    public JTextField getPatientName() {
        return patientName;
    }

    public JTextField getVisited() {
        return visitedDate;
    }

    public JLabel getConfirmed() {
        return confirmed;
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

    public JLabel getAtesakiLbl() {
        return atesakiLbl;
    }
}
