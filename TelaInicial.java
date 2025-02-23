
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
public class TelaInicial extends JFrame implements ActionListener{

    private JButton login, cadastro;
    private JPasswordField txtSenha;
    private JCheckBox mostrarSenhaJCheckBox;
    private JLabel lblUser, lblSenha;
    private JTextField txtUser;
    private CrudBD  cBD     = new CrudBD();
    private User    uDraft  = new User();
    private JMenu x;
    private ResourceBundle bn;
    private String erro = "Senha ou usuário incorretos.";

    public TelaInicial(){

   

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JPanel botoes = new JPanel();
        botoes.setLayout(new FlowLayout());

        JPanel north = new JPanel();
        north.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titulo = new JLabel("My Schedule");
        north.add(titulo);

        x = new JMenu("Linguagem");
        JMenuItem port = new JMenuItem("Português");
        JMenuItem eng = new JMenuItem("English");
        JMenuItem fra = new JMenuItem("Français");
        JMenuItem esp = new JMenuItem("Español");
        JMenuItem ita = new JMenuItem("Italiano");
        x.add(port);
        x.add(eng);
        x.add(fra);
        x.add(esp);
        x.add(ita);

        mostrarSenhaJCheckBox = new JCheckBox("Mostrar Senha");
        mostrarSenhaJCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event){
                if(mostrarSenhaJCheckBox.isSelected()){
                    // Mostrar a senha como texto
                    txtSenha.setEchoChar((char) 0);
                } else{
                    // Ocultar a senha com a máscara padrão
                    txtSenha.setEchoChar('*');
                }
            }
        });   


        lblSenha = new JLabel("Senha: ");
        lblUser = new JLabel("Usuário: ");

        txtSenha = new JPasswordField(10);
        txtUser = new JTextField(10);

        c.setLayout(new FlowLayout());

        JMenuBar barra = new JMenuBar();
        setJMenuBar(barra);
        barra.add(x);

        c.add(north, BorderLayout.NORTH);

        JPanel c1 = new JPanel();
        c1.setLayout(new FlowLayout());

        c1.add(lblUser);
        c1.add(txtUser);

        c1.add(lblSenha);
        c1.add(txtSenha);


        c.add(c1, BorderLayout.CENTER);
        c.add(botoes, BorderLayout.SOUTH);

        login = new JButton("Fazer login");
        cadastro = new JButton("Fazer Cadastro");

        botoes.add(login);
        botoes.add(cadastro);
        botoes.add(mostrarSenhaJCheckBox);


        login.addActionListener(this);
        cadastro.addActionListener(this);

        setSize(400,200);

        setLocationRelativeTo(null);

        setTitle("My Schedule");
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        atualizarIdioma(new Locale("pt", "BR"));

        port.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(new Locale("pt", "BR"));
               
            }
        });

        eng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(Locale.US);
              
            }
        });

        esp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma( Locale.forLanguageTag("es-ES") );
               
            }
        });

        ita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(Locale.ITALY);
               
            }
        });

        fra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(Locale.FRANCE);
           
            }
        });
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == login)
        {
            uDraft.setUser(txtUser.getText());
            cBD.consultarReg(uDraft);
            String senha = new String(txtSenha.getPassword());
            if(senha.equals(uDraft.getPass())){
                new TelaPrincipal(txtUser.getText(), bn);
                dispose();
            }else{

                    JOptionPane.showMessageDialog(null, erro);
                
                
            }
            
    }
        else if(e.getSource() == cadastro){
            new Cadastro(this, bn);


    }}

    private void atualizarIdioma(Locale locale) {
        bn = ResourceBundle.getBundle("projeto", locale);

        login.setText(bn.getString("TelaInicial.login"));
        cadastro.setText(bn.getString("TelaInicial.cadastro"));
        lblUser.setText(bn.getString("TelaInicial.usuario"));
        lblSenha.setText(bn.getString("TelaInicial.senha"));
        mostrarSenhaJCheckBox.setText(bn.getString("TelaInicial.mostrarsenha"));
        x.setText(bn.getString("TelaInicial.linguagem"));
        erro = bn.getString("erro.senha");

        revalidate();
        repaint();


    }


    public static void main(String args[])
    {
    new TelaInicial();
    }
}