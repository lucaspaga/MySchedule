
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.PatternSyntaxException;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import java.util.*;

import com.toedter.calendar.JDateChooser;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class ConsultarTarefasTela extends JFrame implements ActionListener{
    User    uDraft  = new User();
    CrudBD  cBD     = new CrudBD();
    RotinaDB rBD = new RotinaDB();
    int     iOption;
    private Tarefa tarefa = new Tarefa();
    private JLabel lblFiltro, lblTarefa;
    private JTextField txtFiltro;
    private JButton btnData, btnDescricao, btnHorario, btnTipo, btnRemoverFiltro, btnAlterarTarefa, btnExcluirTarefa;
    private JRadioButton urgenteRadioButton, desejavelRadioButton, diariaRadioButton;
    private static JLabel lblStatus;
  


    public ConsultarTarefasTela(String user, int id, ResourceBundle bn){
        
        /// Importação dos dados do banco de para Arrays
        tarefa.setID(id);
        ArrayList<String> descricoes = rBD.consultarTarefa(tarefa);
        ArrayList<String> datas =  rBD.consultarData(tarefa);
        ArrayList<String> horarios =  rBD.consultarHorario(tarefa);
        ArrayList<String> tipos = rBD.consultarTipo(tarefa);
        
        String[][] data = new String[horarios.size()][];

        for (int i = 0; i<horarios.size(); i++) {
            String[] linha = {datas.get(i), descricoes.get(i), horarios.get(i), tipos.get(i)};
            data[i] = linha;
        }
       

        // Criação da tabela e definição dos nomes das colunas
        String[] columnNames = {bn.getString("tabela.colunadata"), bn.getString("tabela.colunatarefa"), 
                                bn.getString("tabela.colunahorario"), bn.getString("tabela.colunatipo")};

        DefaultTableModel model = new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(model);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Formatação das datas e chamada da função deletePastDates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        deletePastDates(model, dateFormat, rBD); //Apaga tarefas de dias passados


        // Criação de um poinel de scroll com a tabela inserida
        JScrollPane scrollPane = new JScrollPane(tabela);

        //Itens do menu file
        JMenu fileMenu = new JMenu(bn.getString("TelaPrincipal.file"));
        fileMenu.setMnemonic('F');

        //Item que chama a função exportarArquivo, que gera um arquivo .txt com a agenda
        JMenuItem exportarItem = new JMenuItem(bn.getString("TelaPrincipal.exportar"));
        exportarItem.setMnemonic('h');
        fileMenu.add(exportarItem);
        exportarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                exportarArquivo(tabela);
        }})
        ;

        //Item que volta pra tela principal
        JMenuItem homeItem = new JMenuItem("Home");
        homeItem.setMnemonic('h');
        fileMenu.add(homeItem);
        homeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               new TelaPrincipal(user, bn);
               dispose();
            }
        });

        //Item sobre
        JMenuItem aboutItem = new JMenuItem(bn.getString("TelaPrincipal.sobre"));
        aboutItem.setMnemonic('A');
        fileMenu.add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(ConsultarTarefasTela.this, bn.getString("tela.sobre"), "About", JOptionPane.PLAIN_MESSAGE);
            }
        });

        //Item para fechar o programa
        JMenuItem exitItem = new JMenuItem(bn.getString("TelaPrincipal.fechar"));
        exitItem.setMnemonic('x');
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });


        
        //Itens do menu conta
        JMenu contaMenu = new JMenu(bn.getString("TelaPrincipal.conta"));
        contaMenu.setMnemonic('C');

        //Item que troca a senha do usuário
        JMenuItem trocarSenhaItem = new JMenuItem(bn.getString("TelaPrincipal.trocarsenha"));
        trocarSenhaItem.setMnemonic('U');
        contaMenu.add(trocarSenhaItem);
        trocarSenhaItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                uDraft.clearObject();
                uDraft.setUser(user); //Seta o usuário a partir da String user
                cBD.consultarReg(uDraft); //Consulta as informações deste usuário no banco de dados
                String senha = JOptionPane.showInputDialog(null, bn.getString("trocarsenha.antiga"), "Confirmar usuario", JOptionPane.QUESTION_MESSAGE);//Pergunta ao usuário a senha antiga
                if(senha.equals(uDraft.getPass())){ // Caso a senha informada seja igual a armazenada no bd...
                    uDraft.clearObject();
                    uDraft.setUser(user); //Seta o usuário a partir da String user
                    uDraft.setPass(JOptionPane.showInputDialog(null, bn.getString("trocarsenha.nova"), "Alterar", JOptionPane.QUESTION_MESSAGE));//Seta a senha do usuário perguntando uma nova senha
                    cBD.alterarReg(uDraft); //altera a senha no banco de dados
                    cBD.consultarReg(uDraft);
                    JOptionPane.showMessageDialog(null, bn.getString("tela.sucesso"));
                }
            }
        });

        //Sai da conta e volta para tela de login
        JMenuItem sairItem = new JMenuItem(bn.getString("TelaPrincipal.sair"));
        sairItem.setMnemonic('S');
        contaMenu.add(sairItem);
        sairItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
                new TelaInicial();
            }
        });

        //Menu Compartilhar
        JMenu shareMenu = new JMenu(bn.getString("tabela.compartilhar"));
        shareMenu.setMnemonic('s');
        
        //Cria uma etiqueta para informar ao usuário se o servidor está escutando
        lblStatus = new JLabel(bn.getString("tabela.servidor"), SwingConstants.CENTER);
        lblStatus.setOpaque(true);
        lblStatus.setForeground(Color.RED);
        lblStatus.setBackground(Color.BLACK);

        //Item reponsável pelo papel de envio de arquvio txt do lado do cliente.
        JMenuItem enviarItem = new JMenuItem(bn.getString("tabela.enviar"));
        enviarItem.setMnemonic('e');
        shareMenu.add(enviarItem);
        enviarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                new Thread(() -> { // Inicia nova thread para que não haja travamento do programa enquanto o envio nõ finalize
                    //Definiçao de ip e porta de destino, e caminho para localização do arquivo
                    String serverAddress = JOptionPane.showInputDialog(bn.getString("servidor.ip"));
                    int port = 12345;
                    String filePath = "Agenda.txt";

                    File file = new File(filePath);

                    if (!file.exists()) { 
                        exportarArquivo(tabela);//Caso o arquivo não exista, é gerado um novo arquivo .txt
                    }else{
                         
                        file.delete();//Caso contrário, o arquivo é excluído e um novo é gerado
                        exportarArquivo(tabela);
                        
                    }
                    
                    try (Socket socket = new Socket(serverAddress, port); // Cria um socket que se conecta ao servidor no endereço IP e porta especificados
                         FileInputStream fileInputStream = new FileInputStream(filePath); // le o arquivo localizado no caminho especificado
                         BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream())) { //otimiza o envio dos dados do arquivo, permitindo o envio de blocos de dados ao invés de byte a byte.
        
                        byte[] buffer = new byte[4096];  //le blocos de dados de até 4096 bytes por vez
                        int bytesRead;
        
                        while ((bytesRead = fileInputStream.read(buffer)) != -1) { //loop que lê o arquivo em blocos até que o final do arquivo seja alcançado
                            bufferedOutputStream.write(buffer, 0, bytesRead);//Envia o arq para o servidor
                        }
                        bufferedOutputStream.flush(); //garante que qualquer dado ainda pendente no buffer seja enviado.
                        JOptionPane.showMessageDialog(ConsultarTarefasTela.this, bn.getString("servidor.enviado"), "", JOptionPane.PLAIN_MESSAGE);
        
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(ConsultarTarefasTela.this, bn.getString("servidor.erroenvio"), "", JOptionPane.ERROR_MESSAGE);
                    }
                }).start();
            }
        });

         //Item reponsável pelo papel de recebimento de arquvio txt do lado do servidor.
        JMenuItem receberItem = new JMenuItem(bn.getString("tabela.receber"));
        receberItem.setMnemonic('r');
        shareMenu.add(receberItem);
        receberItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                new Thread(() -> {
                    //Definiçao da porta onde o servidor estará escutando, e do caminho e nome do arquivo que será recebido e salvo
                    int port = 12345;
                    String outputFilePath = "Agenda_recebida.txt";
        
                    try (ServerSocket serverSocket = new ServerSocket(port)) { // Abre um ServerSocket na porta especificada para escutar conexões de entrada.
                        JOptionPane.showMessageDialog(null, bn.getString("servidor.escutando"));

                        SwingUtilities.invokeLater(() -> {
                            lblStatus.setForeground(Color.GREEN);//Muda a coloração do texto da etiqueta para verde, para indicar que o servidor está ativo.
                             });


                             new Thread(() -> { //Inicia uma nova thread que inicia uma contagem de 2 min para que feche o servidor ao fim do tempo.
                                try {
                                    Thread.sleep(120000); 
                                    if (serverSocket != null && !serverSocket.isClosed()) {
                                        serverSocket.close(); 
                                        JOptionPane.showMessageDialog(null, bn.getString("erro.minutos"));
                                        lblStatus.setForeground(Color.RED);
                                    }
                                } catch (InterruptedException | IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
        
                            
                        try (Socket clientSocket = serverSocket.accept(); //Espera que a conexão seja aceita
                             BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());//le os dados enviados pelo cliente
                             FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {//salvar o arquivo recebido no local especificad
        
                            byte[] buffer = new byte[4096];
                            int bytesRead;
        
                            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {//loop lê os dados do cliente em blocos de 4096 bytes e escreve esses blocos arquivo
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                            fileOutputStream.flush();//assegura que todos os dados foram gravados no disco.
        
                            JOptionPane.showMessageDialog(null, "Arquivo recebido com sucesso!");
                            serverSocket.close();//fecha o servidor
                        
        
                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erro ao receber arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
                        }

                        SwingUtilities.invokeLater(() -> {
                            lblStatus.setForeground(Color.RED);////Muda a coloração do texto da etiqueta para verde, para indicar que o servidor está fechado
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erro ao iniciar o servidor!", "Erro", JOptionPane.ERROR_MESSAGE);
                        SwingUtilities.invokeLater(() -> {
                            lblStatus.setForeground(Color.RED);
                        });
                    }
                }).start();
            }
        });

        //cria barra de menu
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        bar.add(fileMenu);


        bar.add(contaMenu);
        bar.add(shareMenu);


        lblFiltro = new JLabel(bn.getString("tabela.filtro"));
        txtFiltro = new JTextField();

        lblTarefa = new JLabel(bn.getString("tabela.tarefaselecionada"));
        
        txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
            
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }
            
            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }
            
            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }
            
            private void newFilter() {
                RowFilter<Object, Object> rowFilter = null;
                try {
                    rowFilter = RowFilter.regexFilter(txtFiltro.getText(), 0); 
                } catch (PatternSyntaxException e) {
                    return;
                }
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                sorter.setRowFilter(rowFilter);
                tabela.setRowSorter(sorter);
            }
        });

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        bar.add(contaMenu);

        JPanel botoes = new JPanel();
        botoes.setLayout(new FlowLayout());

        
        btnData = new JButton(bn.getString("tabela.colunadata"));
        btnDescricao = new JButton(bn.getString("tabela.colunatarefa"));
        btnHorario = new JButton(bn.getString("tabela.colunahorario"));
        btnTipo = new JButton(bn.getString("tabela.colunatipo"));

        btnAlterarTarefa = new JButton(bn.getString("tabela.alterartarefa"));
        btnExcluirTarefa = new JButton(bn.getString("tabela.excluirtarefa"));

        btnRemoverFiltro = new JButton("Remover Filtro");

        btnRemoverFiltro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                sorter.setRowFilter(null);
                tabela.setRowSorter(sorter);
                tabela.setBackground(null);

                tabela.repaint();
            }});

        btnData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JDateChooser dateChooser = new JDateChooser();
                dateChooser.setDateFormatString("dd/MM/yyyy");

                Date today = new Date();
                dateChooser.setMinSelectableDate(today);

            int option = JOptionPane.showOptionDialog(
                    null,
                    dateChooser,
                    "Selecione uma data",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);


            if (option == JOptionPane.OK_OPTION) {

                Date selectedDate = dateChooser.getDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String data = dateFormat.format(selectedDate);

                if (data != null && !data.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                    sorter.setRowFilter(RowFilter.regexFilter(data, 0));
                    tabela.setRowSorter(sorter);
                    tabela.setBackground(new Color(255, 0, 0, 128));

                    tabela.repaint();
                    
                
            }
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma data selecionada.");
            }
        }});

        btnDescricao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String descricao = JOptionPane.showInputDialog(ConsultarTarefasTela.this, "Digite a descrição:");
                if (descricao != null && !descricao.isEmpty()) {
                    
                        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                        sorter.setRowFilter(RowFilter.regexFilter (descricao, 1));
                        tabela.setRowSorter(sorter);
                        tabela.setBackground(new Color(255, 0, 0, 128));

                        tabela.repaint();
                    
                }
            }
        });

        btnHorario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
             Date initialTime = calendar.getTime();

                SpinnerDateModel timeModel = new SpinnerDateModel(initialTime, null, null, Calendar.MINUTE);
                JSpinner timeSpinner = new JSpinner(timeModel);
                JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
                timeSpinner.setEditor(timeEditor);
                

                int option = JOptionPane.showOptionDialog(
                    null,
                    timeSpinner,
                    "Selecione um Horário",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);


            if (option == JOptionPane.OK_OPTION) {

                Date selectedDate = timeModel.getDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String data = dateFormat.format(selectedDate);
                if (data != null && !data.isEmpty()) {
                
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                    sorter.setRowFilter(RowFilter.regexFilter(data, 2));
                    tabela.setRowSorter(sorter);
                    tabela.setBackground(new Color(255, 0, 0, 128));

                    tabela.repaint();
                    
                
            }
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma data selecionada.");
            }
        }});

        btnTipo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                urgenteRadioButton = new JRadioButton("Urgente", true);
                desejavelRadioButton = new JRadioButton("Desejável", false);
                diariaRadioButton = new JRadioButton("Diária", false);
                JPanel tipoPainel = new JPanel();
                tipoPainel.setLayout(new FlowLayout());

                tipoPainel.add(urgenteRadioButton);
                tipoPainel.add(desejavelRadioButton);
                tipoPainel.add(diariaRadioButton);

                ButtonGroup group = new ButtonGroup();
                group.add(urgenteRadioButton);
                group.add(desejavelRadioButton);
                group.add(diariaRadioButton);

                int result = JOptionPane.showConfirmDialog(null, tipoPainel, "Escolha uma opção", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                ActionListener okListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (Component component : tipoPainel.getComponents()) {
                            if (component instanceof JRadioButton) {
                                JRadioButton radioButton = (JRadioButton) component;
                                if (radioButton.isSelected()) {
                                    if (radioButton.getText() != null && !radioButton.getText().isEmpty()) {
                                        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                                        sorter.setRowFilter(RowFilter.regexFilter (radioButton.getText(), 3));
                                        tabela.setRowSorter(sorter);
                                        tabela.setBackground(new Color(255, 0, 0, 128));
                
                                        tabela.repaint();
                                    
                                }
                                }
                            }
                        }
                    }
                };
                if (result == JOptionPane.OK_OPTION) {
                    okListener.actionPerformed(null); 
        }
 
            }
        });

        btnAlterarTarefa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                performActionOnSelectedRowAlterar(tabela, bn, user, id);
                
            }
        });

        btnExcluirTarefa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performActionOnSelectedRowExcluir(tabela, rBD, bn, user, id);
                
            }
        });
        
        c.add(scrollPane, BorderLayout.NORTH);
        
        botoes.add(lblStatus);
        botoes.add(Box.createHorizontalStrut(20));
        botoes.add(lblFiltro);
        botoes.add(btnData);
        botoes.add(btnDescricao);
        botoes.add(btnHorario);
        botoes.add(btnTipo);
        botoes.add(btnRemoverFiltro);

        botoes.add(lblTarefa);
        botoes.add(btnAlterarTarefa);
        botoes.add(btnExcluirTarefa);
       

        c.add(botoes, BorderLayout.SOUTH);

        TableRowSorter<DefaultTableModel> sorter = new CustomTableRowSorter<>(model);
        sorter.setComparator(0, new Comparator<String>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            public int compare(String date1, String date2) {
                try {
                    Date d1 = dateFormat.parse(date1);
                    Date d2 = dateFormat.parse(date2);
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        sorter.setComparator(3, Comparator.comparing(Object::toString)); 
        sorter.setComparator(2, Comparator.comparing(Object::toString)); 
        sorter.setComparator(1, Comparator.comparing(Object::toString)); 
        tabela.setRowSorter(sorter);
        
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        
        sorter.setSortKeys(sortKeys);

        Color corHexadecimal = Color.decode("#2a2c57");
        botoes.setBackground(corHexadecimal);

        
        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setBackground(corHexadecimal);   
        cabecalho.setForeground(Color.decode("#ff7466"));  
        cabecalho.setFont(new Font("Serif", Font.BOLD, 15));

        TableCellRenderer defaultRenderer = cabecalho.getDefaultRenderer();
        cabecalho.setDefaultRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 0) {
                    label.setIcon(null);  
                }
                return label;
            }
        });

        lblFiltro.setForeground(Color.decode("#ff7466"));
        lblFiltro.setFont(new Font("Serif", Font.BOLD, 15));
        lblTarefa.setForeground(Color.decode("#ff7466"));
        lblTarefa.setFont(new Font("Serif", Font.BOLD, 15));

        setSize(1000,520);   
        setLocationRelativeTo(null);         
        setTitle("My Schedule");   
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void performActionOnSelectedRowAlterar(JTable tabela, ResourceBundle bn, String user, int id) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow != -1) {
            Tarefa altTarefa = new Tarefa();
            
            altTarefa.setData((String) tabela.getValueAt(selectedRow, 0));
            altTarefa.setDescricao((String) tabela.getValueAt(selectedRow, 1));
            altTarefa.setHorario((String) tabela.getValueAt(selectedRow, 2));
            altTarefa.setTipo((String) tabela.getValueAt(selectedRow, 3));
            new AlterarTarefaTela(null, altTarefa, bn, user, id);

            dispose();
            

        }else {
    
            JOptionPane.showMessageDialog(this, "Nenhuma tarefa selecionada.");
        }
    }

    private void performActionOnSelectedRowExcluir(JTable tabela, RotinaDB rdb, ResourceBundle bn, String user, int id) {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow != -1) {
            
            Tarefa altTarefa = new Tarefa();
            
            altTarefa.setData((String) tabela.getValueAt(selectedRow, 0));
            altTarefa.setDescricao((String) tabela.getValueAt(selectedRow, 1));
            altTarefa.setHorario((String) tabela.getValueAt(selectedRow, 2));
            altTarefa.setTipo((String) tabela.getValueAt(selectedRow, 3));
            
            rdb.consultarId(altTarefa);
            rdb.excluirReg(altTarefa, altTarefa.getID());
            JOptionPane.showMessageDialog(null, "Excluída com sucesso!");
            dispose();
            new ConsultarTarefasTela(user, id, bn);

            
            
            

        }else {
   
            JOptionPane.showMessageDialog(this, "Nenhuma tarefa selecionada.");
        }
    }


    public void actionPerformed(ActionEvent e){

    }
    static class CustomTableRowSorter<M extends DefaultTableModel> extends TableRowSorter<M> {
        public CustomTableRowSorter(M model) {
            super(model);
        }

       
        
        public boolean isSortable(int column) {
           
            return column != 0 && column != 1 && column != 2 && column != 3;
        }
    }

    private void deletePastDates(DefaultTableModel model, SimpleDateFormat dateFormat, RotinaDB rBD) {
        Date today = new Date();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            String dateString = (String) model.getValueAt(i, 0);
            try {
                Date date = dateFormat.parse(dateString);
                if (date.before(today)) {
                    model.removeRow(i);

                    Tarefa tarefaAnt = new Tarefa();

                    tarefaAnt.setData(dateString);

                    rBD.excluirRegData(tarefaAnt);
                    
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void exportarArquivo(JTable tabela){ //exporta os dados de uma tabela para um arquivo de texto.
        try (Formatter formatter = new Formatter(new File("Agenda.txt"))) {// escreve no arquivo "Agenda.txt". 
            int rowCount = tabela.getRowCount(); //armazena o número de linhas
            int columnCount = tabela.getColumnCount();//armazenam o número de colunas

            for (int column = 0; column < columnCount; column++) {
                formatter.format("%-15s", tabela.getColumnName(column)); //escrevee o nome de cada coluna no arquivo. O formato %-15s alinha os dados em colunas de 15 caracteres
            }
            formatter.format("%n"); //adiciona uma nova linha para separar os cabeçalhos dos dados.

            for (int column = 0; column < columnCount; column++) {//separar visualmente os cabeçalhos dos dados.
                formatter.format("-----------------");
            }
            formatter.format("%n");

            for (int row = 0; row < rowCount; row++) {//para cada linha, outro loop percorre cada coluna, escrevendo o conteúdo da célula e aplicando a formatação de 15 caracteres.
                for (int column = 0; column < columnCount; column++) {
                    formatter.format("%-15s", String.valueOf(tabela.getValueAt(row, column)));
                }
                formatter.format("%n");
            }

            JOptionPane.showMessageDialog(ConsultarTarefasTela.this, "Arquivo gerado", "About", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }



        }
    

    
    
