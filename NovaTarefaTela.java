import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.util.Calendar;
import java.util.ResourceBundle;



public class NovaTarefaTela extends JDialog implements ActionListener{
    
    private JLabel lblDescricao, lblData, lblHorario, lblTipo;
    private JTextField txtDescricao, txtData, txtHorario;
    private JButton btnCriar, btnLimpar;
    private Tarefa tarefa = new Tarefa(getName(), getName(), getTitle(), getWarningString(), getName());
    private RotinaDB rdb = new RotinaDB();
    private JRadioButton urgenteRadioButton, desejavelRadioButton, diariaRadioButton;

    public NovaTarefaTela(JFrame fr, int id, ResourceBundle bn){
        super(fr, "My Schedule");

        Container c = getContentPane();
        c.setLayout(new GridLayout(5,2));

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date initialTime = calendar.getTime();
        SpinnerDateModel timeModel = new SpinnerDateModel(initialTime, null, null, Calendar.MINUTE);
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);

        btnCriar = new JButton(bn.getString("TelaNovaTarefa.criartarefa"));
        btnLimpar = new JButton(bn.getString("TelaNovaTarefa.limparcampos"));

        btnCriar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                    tarefa = new Tarefa(getName(), getName(), getTitle(), getWarningString(), getName());
                    if(!txtDescricao.getText().equals("")){
                        tarefa.setDescricao(txtDescricao.getText());
                    }else{
                        tarefa.setDescricao("x");
                    };

                    Date selectedDate = dateChooser.getDate();
                    if (selectedDate != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = dateFormat.format(selectedDate);
                        if(dateString.substring(2,3).equals("/") && dateString.substring(5,6).equals("/")){
                            tarefa.setData(dateString);
                        }else{
                            tarefa.setData("x");
                        }
                        
                    }else{
                        tarefa.setData("x");
                    };
                    
                    Date selectedTime = (Date) timeSpinner.getValue();
                    if (selectedTime != null) {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        String timeString = timeFormat.format(selectedTime);
                        tarefa.setHorario(timeString);
                    } 
                    if (urgenteRadioButton.isSelected()) {
                        tarefa.setTipo("Urgente");
                    } else if (desejavelRadioButton.isSelected()) {
                        tarefa.setTipo("Desejável");
                    } else if (diariaRadioButton.isSelected()) {
                        tarefa.setTipo("Diária");
                    };

                    if(tarefa.getDescricao().equals("x") || tarefa.getData().equals("x")){
                        JOptionPane.showMessageDialog(null, bn.getString("erro.preencha"));
                    }else{
                        rdb.incluirReg(tarefa, id);
                        JOptionPane.showMessageDialog(null, bn.getString("tela.sucesso"));
                        dispose();
                    };
                    
                
            }
        });
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                dateChooser.setDate(null);
                dateChooser.setDate(null);
                Calendar resetCalendar = Calendar.getInstance();
                resetCalendar.set(Calendar.HOUR_OF_DAY, 0);
                resetCalendar.set(Calendar.MINUTE, 0);
                timeSpinner.setValue(resetCalendar.getTime());
                txtDescricao.setText("");
                txtData.setText("");
                txtHorario.setText("");
            }});

        urgenteRadioButton = new JRadioButton(bn.getString("TelaNovaTarefa.urgente"), true);
        desejavelRadioButton = new JRadioButton(bn.getString("TelaNovaTarefa.desejavel"), false);
        diariaRadioButton = new JRadioButton(bn.getString("TelaNovaTarefa.diaria"), false);
        JPanel tipoPainel = new JPanel();
        tipoPainel.setLayout(new FlowLayout());

        tipoPainel.add(urgenteRadioButton);
        tipoPainel.add(desejavelRadioButton);
        tipoPainel.add(diariaRadioButton);

        ButtonGroup group = new ButtonGroup();
        group.add(urgenteRadioButton);
        group.add(desejavelRadioButton);
        group.add(diariaRadioButton);

        lblDescricao = new JLabel(bn.getString("TelaNovaTarefa.descricao"));
        txtDescricao = new JTextField(10);

        lblData = new JLabel(bn.getString("TelaNovaTarefa.data"));
        txtData = new JTextField(10);

        lblHorario = new JLabel(bn.getString("TelaNovaTarefa.horario"));
        txtHorario = new JTextField(10);

        lblTipo = new JLabel(bn.getString("TelaNovaTarefa.tipo"));
        

        
        
        
        Date today = new Date();
        dateChooser.setMinSelectableDate(today);
        

        c.add(lblDescricao);
        c.add(txtDescricao);

        c.add(lblData);
        c.add(dateChooser);
        
        

        c.add(lblHorario);
        c.add(timeSpinner);

        c.add(lblTipo);
        c.add(tipoPainel);
        

        c.add(btnCriar);
        c.add(btnLimpar);

        
        

        setSize(1000,500);   
        setLocationRelativeTo(null);         
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e){

    }


   
} 

