package open.dolphin.letter;

import java.awt.BorderLayout;
import javax.swing.*;
import open.dolphin.client.Panel2;

/**
 * MedicalCertificateView
 * 
 * @author masuda, Masuda Naika
 */
public class MedicalCertificateView extends Panel2 {
    
    private JTextField patientNameFld;
    private JTextField patientAddressFld;
    private JTextField patientBirthdayFld;
    private JTextArea informedContent;
    private JScrollPane scrl_content;
    private JTextField confirmedFld;
    private JTextField hospitalAddressFld;
    private JTextField hospitalNameFld;
    private JTextField doctorNameFld;
    private JTextField diseaseFld;
    private JTextField hospitalTelephoneFld;
    private JTextField sexFld;
    
    private JLabel lbl_title;
    private JLabel lbl_ptName;
    private JLabel lbl_ptAddr;
    private JLabel lbl_ptBD;
    private JLabel lbl_disease;
    private JLabel lbl_diagComment;
    private JLabel lbl_sex;
    private JLabel lbl_stamp;
    
    
    public MedicalCertificateView() {
        initComponents();
    }
    
    private void initComponents() {
        
        lbl_title = new JLabel("診 断 書（フォーム）", JLabel.CENTER);
        lbl_ptName = new JLabel("氏　名");
        lbl_ptAddr = new JLabel("住　所");
        lbl_ptBD = new JLabel("生年月日");
        lbl_disease = new JLabel("傷 病 名");
        lbl_diagComment = new JLabel("上記の通り診断する。");
        lbl_sex = new JLabel("性別");
        lbl_stamp = new JLabel("印");
        
        patientNameFld = new JTextField(10);
        patientNameFld.setEditable(false);
        patientBirthdayFld = new JTextField(10);
        patientBirthdayFld.setEditable(false);
        sexFld = new JTextField(5);
        sexFld.setEditable(false);
        patientAddressFld = new JTextField();
        patientAddressFld.setEditable(false);
        diseaseFld = new JTextField();
        
        informedContent = new JTextArea();
        informedContent.setLineWrap(true);
        scrl_content = new JScrollPane(informedContent);
        
        confirmedFld = new JTextField(10);
        confirmedFld.setToolTipText("右クリックでカレンダーがポップアップします。");
        
        hospitalAddressFld= new JTextField(30);
        hospitalAddressFld.setHorizontalAlignment(JTextField.RIGHT);
        hospitalAddressFld.setEditable(false);
        hospitalNameFld = new JTextField(30);
        hospitalNameFld.setHorizontalAlignment(JTextField.RIGHT);
        hospitalNameFld.setEditable(false);
        hospitalTelephoneFld = new JTextField(10);
        hospitalTelephoneFld.setHorizontalAlignment(JTextField.RIGHT);
        hospitalTelephoneFld.setEditable(false);
        doctorNameFld = new JTextField(10);
        doctorNameFld.setHorizontalAlignment(JTextField.RIGHT);
        doctorNameFld.setEditable(false);
        
        // GroupLayout の生成
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        
        // 自動的にコンポーネント間のすき間をあけるようにする
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        // 水平方向のグループ
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        
        hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(lbl_ptName)
                .addComponent(lbl_ptBD)
                .addComponent(lbl_ptAddr)
                .addComponent(lbl_disease));
        
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(patientNameFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(patientBirthdayFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_sex)
                    .addComponent(sexFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                
                .addComponent(patientAddressFld)
                .addComponent(diseaseFld)
                .addComponent(lbl_diagComment)
                .addComponent(confirmedFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(scrl_content)
                    .addComponent(hospitalAddressFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(hospitalNameFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(hospitalTelephoneFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(doctorNameFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_stamp))));
        layout.setHorizontalGroup(hGroup);
        
        // 垂直方向のグループ
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_ptName)
                .addComponent(patientNameFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_ptBD)
                .addComponent(patientBirthdayFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_sex)
                .addComponent(sexFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_ptAddr)
                .addComponent(patientAddressFld));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_disease)
                .addComponent(diseaseFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addComponent(scrl_content);
        vGroup.addComponent(lbl_diagComment);
        vGroup.addComponent(confirmedFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        vGroup.addComponent(hospitalAddressFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        vGroup.addComponent(hospitalNameFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        vGroup.addComponent(hospitalTelephoneFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(doctorNameFld, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_stamp));
        layout.setVerticalGroup(vGroup);
        
        // 全体レイアウト
        setLayout(new BorderLayout());
        add(lbl_title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }
    
    public JTextField getConfirmedFld() {
        return confirmedFld;
    }

    public JTextField getDiseaseFld() {
        return diseaseFld;
    }

    public JTextField getDoctorNameFld() {
        return doctorNameFld;
    }

    public JTextField getHospitalAddressFld() {
        return hospitalAddressFld;
    }

    public JTextField getHospitalNameFld() {
        return hospitalNameFld;
    }

    public JTextArea getInformedContent() {
        return informedContent;
    }

    public JTextField getPatientAddress() {
        return patientAddressFld;
    }

    public JTextField getPatientBirthday() {
        return patientBirthdayFld;
    }

    public JTextField getPatientNameFld() {
        return patientNameFld;
    }

    public JTextField getHospitalTelephoneFld() {
        return hospitalTelephoneFld;
    }

    public JTextField getSexFld() {
        return sexFld;
    }
}
