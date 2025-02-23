import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.util.ResourceBundle;




public class TelaPrincipal extends JFrame implements ActionListener{
    private JLabel displayJLabel;
    private JLabel usuario;
    private JButton btnNovaTarefa, btnConsultarTarefas;
    User    uDraft  = new User();
    CrudBD  cBD     = new CrudBD();
    int     iOption;


    public TelaPrincipal(String user, ResourceBundle bn){

        JMenu fileMenu = new JMenu(bn.getString("TelaPrincipal.file"));
        fileMenu.setMnemonic('F');

        JMenuItem aboutItem = new JMenuItem(bn.getString("TelaPrincipal.sobre"));
        aboutItem.setMnemonic('A');
        fileMenu.add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
              
                    JOptionPane.showMessageDialog(TelaPrincipal.this, bn.getString("tela.sobre"), "About", JOptionPane.PLAIN_MESSAGE);
            
                
            }
        });

        JMenuItem exitItem = new JMenuItem(bn.getString("TelaPrincipal.fechar"));
        exitItem.setMnemonic('x');
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        bar.add(fileMenu);

        JMenu contaMenu = new JMenu(bn.getString("TelaPrincipal.conta"));
        contaMenu.setMnemonic('C');

        JMenuItem trocarSenhaItem = new JMenuItem(bn.getString("TelaPrincipal.trocarsenha"));
        trocarSenhaItem.setMnemonic('U');
        contaMenu.add(trocarSenhaItem);
        trocarSenhaItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                uDraft.clearObject();
                uDraft.setUser(user);
                cBD.consultarReg(uDraft);
                String senha = JOptionPane.showInputDialog(null, bn.getString("trocarsenha.antiga"), "Confirmar usuario", JOptionPane.QUESTION_MESSAGE);
                if(senha.equals(uDraft.getPass())){
                    uDraft.clearObject();
                    uDraft.setUser(user);
                    uDraft.setPass(JOptionPane.showInputDialog(null, bn.getString("trocarsenha.nova"), "Alterar", JOptionPane.QUESTION_MESSAGE));
                    cBD.alterarReg(uDraft);
                    cBD.consultarReg(uDraft);
                    JOptionPane.showMessageDialog(null, "Senha Alterada.");
                }
            }
        });

        JMenuItem sairItem = new JMenuItem(bn.getString("TelaPrincipal.sair"));
        sairItem.setMnemonic('S');
        contaMenu.add(sairItem);
        sairItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
                new TelaInicial();
            }
        });

        bar.add(contaMenu);

        btnNovaTarefa = new JButton(bn.getString("TelaPrincipal.novatarefa"));
        btnConsultarTarefas = new JButton(bn.getString("TelaPrincipal.consultartarefas"));

        JPanel botoes = new JPanel();
        botoes.setLayout(new FlowLayout());
        

        botoes.add(btnNovaTarefa);
        botoes.add(btnConsultarTarefas);

        btnNovaTarefa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                uDraft.setUser(user);
                cBD.consultarId(uDraft);
                new NovaTarefaTela(TelaPrincipal.this, uDraft.getID(), bn);
                
            }
            
        });
        btnConsultarTarefas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                uDraft.setUser(user);
                cBD.consultarId(uDraft);
                new ConsultarTarefasTela(user, uDraft.getID(), bn);
                dispose();
                
            }
            
        });

        Color corHexadecimal = Color.decode("#2a2c57");

        botoes.setBackground(corHexadecimal);


        usuario = new JLabel(user);
        usuario.setForeground(Color.decode("#ff7466"));
        usuario.setFont(new Font("Serif", Font.BOLD, 20));

        ImageIcon imagem = new ImageIcon("Logo.jpg");


        displayJLabel = new JLabel(imagem);
        

        getContentPane().setBackground(corHexadecimal);
        add(displayJLabel, BorderLayout.CENTER);
        add(usuario, BorderLayout.NORTH);
        add(botoes, BorderLayout.SOUTH);

                
        setSize(1000,500);   
        setLocationRelativeTo(null);         
        setTitle("My Schedule");   
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource()==btnConsultarTarefas){
            uDraft.setUser(JOptionPane.showInputDialog(null, "Digite o Usuário:", "Consultar", JOptionPane.QUESTION_MESSAGE));
            cBD.consultarId(uDraft);
            JOptionPane.showMessageDialog(null, "ID: " + uDraft.getID());
        }
    }
    
}
