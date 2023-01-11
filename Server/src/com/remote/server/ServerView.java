package com.remote.server;

import com.remote.client.InterfaceClient;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.DefaultListModel;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author Bryan 
 * @author Henderson
 * @author Matheus
 */
public class ServerView extends javax.swing.JFrame { 
    private Vector<String> listClients;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private InterfaceServer server;

    public ServerView() {
        initComponents();
        
        try {
            server = (InterfaceServer) Naming.lookup("rmi://localhost:5432/remote");
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        
        Timer timer = new Timer();
        TimerTask tache = new TimerTask() {
            @Override
            public void run() {
                try {
                    int[] indices = jList1.getSelectedIndices();
                    model.clear();
                    listClients = server.getUsernames("");
                    int i=0;
                    
                    while(i<listClients.size()){
                        model.addElement(listClients.get(i));
                        i++;
                    }
                    jList1.setModel(model);
                    jList1.setSelectedIndices(indices);
                } catch (RemoteException ex) {
                    System.out.println("Erro: " + ex.getMessage());
                }
            }
        };
        timer.schedule(tache,0,4000);
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Usuários Online");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Hakimi", "Messi", "Neymar", "Ziech" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );

        pack();
    }

    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
}
