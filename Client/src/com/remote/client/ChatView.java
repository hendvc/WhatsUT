package com.remote.client;

import com.remote.server.InterfaceServer;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;

public class ChatView extends javax.swing.JFrame implements Runnable{
    private ChatClient client;
    private InterfaceServer server;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private Vector<String> listClients;
    private String name;
    private GroupLayout groupLayout;
  
    public ChatView(String name,InterfaceServer server) {
        initComponents();
        
        this.server = server;
        this.name = name;
        
        // Privilégio adquirido
        if(name.equals("adm")){
            listConnect.setComponentPopupMenu(jPopupMenu1);
        }
        
        this.setLocationRelativeTo(null);
        this.setTitle("WhatsUT [Usuário: " + name + "]");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("logo.png")));
        
        // Confirma se o cliente quer fechar o chat
        this.addWindowListener(new java.awt.event.WindowAdapter() {    
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    try {
                        server.deleteUser(name);
                    } catch (RemoteException ex) {
                        System.out.println("Erro: " + ex.getMessage());
                    }
                    System.exit(0);
            }
        });
        
        inputMsg.setForeground(Color.GRAY);
        inputMsg.setText("Digite aqui");
        inputMsg.addFocusListener(new FocusListener() {
        
        @Override
         public void focusGained(FocusEvent e) {
            if (inputMsg.getText().equals("Digite aqui")) {
                inputMsg.setText("");
                inputMsg.setForeground(Color.BLUE);
            }
        }

        @Override
         public void focusLost(FocusEvent e) {
            if (inputMsg.getText().isEmpty()) {
                inputMsg.setForeground(Color.RED);
                inputMsg.setText("Digite aqui");
            }
        }
        });
        
        listClients = new Vector<>();
        listConnect.setListData(listClients);
        
        try{
            client = new ChatClient(name,server,inputMsg,listMessage, listConnect);
        } catch (RemoteException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        
        Timer timer = new Timer();
        TimerTask tache = new TimerTask() {
            @Override
            public void run() {
                try {
                    int[] indices = listConnect.getSelectedIndices();
                    model.clear();
                    listClients = server.getUsernames(name);
                    int i=0;
                    while(i<listClients.size()){
                        model.addElement(listClients.get(i));
                        i++;
                    }
                    listConnect.setModel(model);
                    listConnect.setSelectedIndices(indices);
                } catch (RemoteException ex) {
                    System.out.println("Erro: " + ex.getMessage());
                }
            }
        };
        timer.schedule(tache,0,4000);
    }
    

    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        listConnect = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        inputMsg = new javax.swing.JTextArea();
        btnSend = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listMessage = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jMenuItem1.setText("Remover Usuários");
        jMenuItem1.setActionCommand("");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        listConnect.setBackground(new java.awt.Color(217, 219, 235));
        listConnect.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        listConnect.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Bryan", "Hend", "Patriarka", "Mbappe", "Neymar" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listConnect.setToolTipText("");
        jScrollPane1.setViewportView(listConnect);

        inputMsg.setColumns(20);
        inputMsg.setRows(5);
        inputMsg.setToolTipText("Digite aqui");
        inputMsg.setMargin(new java.awt.Insets(6, 0, 0, 16));
        jScrollPane2.setViewportView(inputMsg);
        inputMsg.getAccessibleContext().setAccessibleName("Digite aqui");

        btnSend.setBackground(new java.awt.Color(0, 255, 0));
        btnSend.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnSend.setText("Send");
        btnSend.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        listMessage.setEditable(false);
        listMessage.setBackground(new java.awt.Color(217, 219, 235));
        listMessage.setColumns(20);
        listMessage.setFont(new java.awt.Font("Dialog", 1, 12)); 
        listMessage.setRows(5);
        listMessage.setRequestFocusEnabled(false);
        jScrollPane3.setViewportView(listMessage);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 16)); 
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Usuários Conectados");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 16)); 
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Chat");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(207, 207, 207))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Verifica se a mensagem está vazia
    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) 
    {
        if(!inputMsg.getText().equals("")){
            if (!inputMsg.getText().equals("Digite aqui"))
            {
                client.sendMessage(listConnect.getSelectedValuesList());
                inputMsg.setText("");
            } else {
                JOptionPane.showMessageDialog(this,"Por favor, digite alguma coisa","Alert",JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,"Por favor, digite alguma coisa","Alert",JOptionPane.WARNING_MESSAGE);
        }
    }

    // Remove os clientes anteriormente selecionados
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        try {
            server.deleteUser(listConnect.getSelectedValuesList());
            listConnect.clearSelection();
        } catch (RemoteException ex) {
            System.out.println("Erro: " + ex.getMessage());
        } 
    }

    private javax.swing.JButton btnSend;
    private javax.swing.JTextArea inputMsg;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<String> listConnect;
    private javax.swing.JTextArea listMessage;

    // Pega os clientes que estão online e os coloca na JList (tela de usuários logados)
    @Override
    public void run() 
    {
        try {
            model.clear();


            listClients = server.getUsernames(name);
            
            int i = 0;
            
            while(i<listClients.size())
            {
                model.addElement(listClients.get(i));
                i++;
            }
            
            listConnect.setModel(model);
        } catch (RemoteException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }
}