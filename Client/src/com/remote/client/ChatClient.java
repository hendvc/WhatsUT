package com.remote.client;

import com.remote.server.InterfaceServer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JList;

public class ChatClient extends UnicastRemoteObject implements InterfaceClient{
    private final InterfaceServer server;
    private final String name;
    private final JTextArea input;
    private final JTextArea output;
    private final JList listConnect;
    
    public ChatClient(String name , InterfaceServer server,JTextArea jtext1,JTextArea jtext2, JList listConnect) throws RemoteException {
        this.name = name;
        this.server = server;
        this.input = jtext1;
        this.output = jtext2;
        this.listConnect = listConnect;
        server.addUser(this);
    }
    
    // Concatena mensagem na lista de mensagens anteriores
    @Override
    public void retrieveMessage(String message) throws RemoteException {
        System.out.println("Hakuna Matata");
        output.setText(output.getText() + "\n" + message);
    }
    
    /* Envia uma mensagem para o servidor, que consequentemente:
        -caso um usuário esteja selecionado, envia de forma privada
        -caso contrário, envia uma mensagem broadcast
    */
    @Override
    public void sendMessage(List<String> list) 
    {
        try {
            if (list.size() == 1) 
            {
                list.add(name);
                server.sendOnlyOnePersonMessage(name + " : " + "[privado] " + input.getText(),list);
                listConnect.clearSelection();
            } else {
                server.broadcastMsg(name + " : " + input.getText(),list);
            }
        } catch (RemoteException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }
    
    // Obter o nome do cliente já conectado
    @Override
    public String getName() 
    {
        return name;
    }

    // Desabilita o envio de mensagens
    @Override
    public void closeWhatsUT(String message) throws RemoteException {
        input.setEditable(false);
        input.setEnabled(false);
        JOptionPane.showMessageDialog(new JFrame(),message,"Alert",JOptionPane.WARNING_MESSAGE); 
    }
}