package open.dolphin.letter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import open.dolphin.client.Panel2;

/**
 * LetterView改
 * GroupLayoutの勉強
 * 
 * @author masuda, Masuda Naika
 */
public class LetterView extends Panel2 {
    
    private JLabel atesakiLbl;
    private JTextArea clinicalCourse;
    private JTextField confirmed;
    private JTextField consultantDept;
    private JTextField consultantDoctor;
    private JTextField consultantHospital;
    private JTextField disease;
    private JTextArea medication;
    private JTextArea pastFamily;
    private JTextField patientAge;
    private JTextField patientBirthday;
    private JTextField patientGender;
    private JTextField patientName;
    private JTextField purpose;
    private JTextField remarks;
    
    private JLabel lbl_title;
    private JLabel lbl_date;
    private JLabel lbl_consultHosp;
    private JLabel lbl_consultDept;
    private JLabel lbl_consultDr;
    private JLabel lbl_ptName;
    private JLabel lbl_ptBirthDay;
    private JLabel lbl_diagnosis;
    private JLabel lbl_purpose;
    private JLabel lbl_history;
    private JPanel pnl_clinicalCourse;
    private JLabel lbl_medication;
    private JLabel lbl_remarks;
    private JLabel lbl_ptSex;
    private JLabel lbl_ptAge;
    
    private JScrollPane scrl_medication;
    private JScrollPane scrl_clinicalCourse;
    private JScrollPane scrl_pastFamily;
    
    public LetterView() {
        initComponents();
    }
    
    private void initComponents() {
        
        lbl_title = new JLabel("診療情報提供書（フォーム）", JLabel.CENTER);
        lbl_date = new JLabel("年月日:");
        lbl_consultHosp = new JLabel("紹介先医療機関名:");
        lbl_consultDept = new JLabel("紹介先診療科:");
        lbl_consultDr = new JLabel("紹介先先生:");
        lbl_ptName = new JLabel("患者氏名:");
        lbl_ptBirthDay = new JLabel("生年月日:");
        lbl_diagnosis = new JLabel("傷病名:");
        lbl_purpose = new JLabel("紹介目的:");
        lbl_history = new JLabel("既往歴/家族歴:");
        pnl_clinicalCourse = new JPanel();
        pnl_clinicalCourse.setLayout(new BoxLayout(pnl_clinicalCourse, BoxLayout.Y_AXIS));
        pnl_clinicalCourse.add(new JLabel("症状経過:"));
        pnl_clinicalCourse.add(new JLabel("検査結果:"));
        pnl_clinicalCourse.add(new JLabel("治療経過:"));
        lbl_medication = new JLabel("現在の処方:");
        lbl_remarks = new JLabel("備考:");
        lbl_ptSex = new JLabel("性別:");
        lbl_ptAge = new JLabel("年齢:");
        atesakiLbl = new JLabel("先生　御机下");
        
        confirmed = new JTextField(15);
        confirmed.setEditable(false);
        consultantHospital = new JTextField(25);
        consultantDept = new JTextField(15);
        consultantDoctor = new JTextField(15);
        patientName = new JTextField(15);
        patientName.setEditable(false);
        patientBirthday = new JTextField(15);
        patientBirthday.setEditable(false);
        disease = new JTextField();
        purpose = new JTextField();
        pastFamily = new JTextArea();
        pastFamily.setLineWrap(true);
        scrl_pastFamily = new JScrollPane(pastFamily);
        scrl_pastFamily.setPreferredSize(new Dimension(400, 100));
        clinicalCourse = new JTextArea();
        clinicalCourse.setLineWrap(true);
        scrl_clinicalCourse = new JScrollPane(clinicalCourse);
        scrl_clinicalCourse.setPreferredSize(new Dimension(400, 200));
        medication = new JTextArea();
        medication.setLineWrap(true);
        scrl_medication = new JScrollPane(medication);
        scrl_medication.setPreferredSize(new Dimension(400, 100));
        remarks = new JTextField();
        patientAge = new JTextField(5);
        patientAge.setEditable(false);
        patientGender = new JTextField(5);
        patientGender.setEditable(false);

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
                .addComponent(lbl_date)
                .addComponent(lbl_consultHosp)
                .addComponent(lbl_consultDept)
                .addComponent(lbl_consultDr)
                .addComponent(lbl_ptName)
                .addComponent(lbl_ptBirthDay)
                .addComponent(lbl_diagnosis)
                .addComponent(lbl_purpose)
                .addComponent(lbl_history)
                .addComponent(pnl_clinicalCourse)
                .addComponent(lbl_medication)
                .addComponent(lbl_remarks));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(confirmed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(consultantHospital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(consultantDept, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(consultantDoctor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(atesakiLbl))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(patientName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_ptSex)
                    .addComponent(patientGender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(patientBirthday, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_ptAge)
                    .addComponent(patientAge, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addComponent(disease)
                .addComponent(purpose)
                .addComponent(scrl_pastFamily)
                .addComponent(scrl_clinicalCourse)
                .addComponent(scrl_medication)
                .addComponent(remarks)
        );
        layout.setHorizontalGroup(hGroup);
        
        // 垂直方向のグループ
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_date)
                .addComponent(confirmed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_consultHosp)
                .addComponent(consultantHospital, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_consultDept)
                .addComponent(consultantDept, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_consultDr)
                .addComponent(consultantDoctor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(atesakiLbl));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_ptName)
                .addComponent(patientName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_ptSex)
                .addComponent(patientGender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_ptBirthDay)
                .addComponent(patientBirthday, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lbl_ptAge)
                .addComponent(patientAge, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_diagnosis)
                .addComponent(disease, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lbl_purpose)
                .addComponent(purpose, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        vGroup.addGroup(layout.createParallelGroup()
                .addComponent(lbl_history)
                .addComponent(scrl_pastFamily));
        vGroup.addGroup(layout.createParallelGroup()
                .addComponent(pnl_clinicalCourse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(scrl_clinicalCourse));
        vGroup.addGroup(layout.createParallelGroup()
                .addComponent(lbl_medication)
                .addComponent(scrl_medication));
        vGroup.addGroup(layout.createParallelGroup()
                .addComponent(lbl_remarks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(remarks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(vGroup);
        
        // 全体レイアウト
        setLayout(new BorderLayout());
        add(lbl_title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }
    
    public javax.swing.JTextField getConsultantDept() {
        return consultantDept;
    }

    public javax.swing.JTextField getConsultantDoctor() {
        return consultantDoctor;
    }

    public javax.swing.JTextField getConsultantHospital() {
        return consultantHospital;
    }

    public javax.swing.JTextArea getClinicalCourse() {
        return clinicalCourse;
    }

    public javax.swing.JTextField getConfirmed() {
        return confirmed;
    }

    public javax.swing.JTextField getDisease() {
        return disease;
    }

    public javax.swing.JTextArea getMedication() {
        return medication;
    }

    public javax.swing.JTextArea getPastFamily() {
        return pastFamily;
    }

    public javax.swing.JTextField getPatientAge() {
        return patientAge;
    }

    public javax.swing.JTextField getPatientBirthday() {
        return patientBirthday;
    }

    public javax.swing.JTextField getPatientGender() {
        return patientGender;
    }

    public javax.swing.JTextField getPatientName() {
        return patientName;
    }

    public javax.swing.JTextField getPurpose() {
        return purpose;
    }

    public javax.swing.JTextField getRemarks() {
        return remarks;
    }

    public javax.swing.JLabel getAtesakiLbl() {
        return atesakiLbl;
    }
}
