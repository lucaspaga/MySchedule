import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;


public class Cadastro extends JDialog implements ActionListener{
    private JCheckBox mostrarSenhaJCheckBox;
    private JTextField txtNewUser;
    private JButton btnLogin;
    private JLabel lblnewUser, lblNovaSenha, lblConfirmarSenha;
    private JPasswordField txtNovaSenha, txtConfirmarSenha;
    private CrudBD  cBD     = new CrudBD();
    private User    uDraft  = new User();
    private static ResourceBundle b;


    


    public Cadastro(JFrame fr, ResourceBundle bn){

        super(fr, true);
        b = bn;

        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        JPanel dados = new JPanel();
        dados.setLayout(new GridLayout(3,2));

        JPanel login = new JPanel();
        login.setLayout(new BorderLayout());

        mostrarSenhaJCheckBox = new JCheckBox(bn.getString("TelaCadastro.mostrarsenha"));
        mostrarSenhaJCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event){
                if(mostrarSenhaJCheckBox.isSelected()){
                    
                    txtNovaSenha.setEchoChar((char) 0);
                    txtConfirmarSenha.setEchoChar((char) 0);
                } else{
                    
                    txtNovaSenha.setEchoChar('*');
                    txtConfirmarSenha.setEchoChar('*');
                }
            }
        });   

        btnLogin = new JButton(bn.getString("TelaCadastro.criarconta"));

        lblNovaSenha = new JLabel(bn.getString("TelaCadastro.senha"));
        lblnewUser = new JLabel(bn.getString("TelaCadastro.usuario"));
        lblConfirmarSenha = new JLabel(bn.getString("TelaCadastro.confirmarsenha"));

        txtNovaSenha = new JPasswordField(10);
        txtNewUser = new JTextField(10);
        txtConfirmarSenha = new JPasswordField(10);

        c.setLayout(new GridLayout(3,2));

        dados.add(lblnewUser);
        dados.add(txtNewUser);
        
        dados.add(lblNovaSenha);
        dados.add(txtNovaSenha);

        dados.add(lblConfirmarSenha);
        dados.add(txtConfirmarSenha);

        login.add(btnLogin, BorderLayout.CENTER);
        login.add(mostrarSenhaJCheckBox, BorderLayout.EAST);


        c.add(dados);
        c.add(login);

        btnLogin.addActionListener(this);

  
        setSize(1000,200);

        setLocationRelativeTo(null);

        setTitle("My Schedule");

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin){
            

            String conifrmarSenha = new String(txtConfirmarSenha.getPassword());
            String novaSenha = new String(txtNovaSenha.getPassword());
            if(conifrmarSenha.equals(novaSenha)){
                uDraft.setUser(txtNewUser.getText());
                uDraft.setPass(novaSenha);
                cBD.incluirReg(uDraft);
                cBD.consultarReg(uDraft);
                JOptionPane.showMessageDialog(null, b.getString("tela.sucesso"));
                dispose();
                
    
            }else{
                JOptionPane.showMessageDialog(Cadastro.this, b.getString("erro.cadastro"), "Erro ao confirmar senha", JOptionPane.PLAIN_MESSAGE);
            }

        }
    }

   
    
}