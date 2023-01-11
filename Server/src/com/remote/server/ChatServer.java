package com.remote.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import com.remote.client.InterfaceClient;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.Vector;

public class ChatServer extends UnicastRemoteObject implements InterfaceServer {

    private final ArrayList<InterfaceClient> clients; 

    public ChatServer() throws RemoteException {
        super();
        this.clients = new ArrayList<>();
    }

    /* Se a lista for vazia, envia uma mensagem broadcast
       Se não for vazia, envia uma mensagem para todos os usuários da lista */
    @Override
    public synchronized void broadcastMsg(String message, List<String> list) throws RemoteException {
        if (list.isEmpty()) {
            int i = 0;
            while (i < clients.size()) {
                clients.get(i++).retrieveMessage(message);
            }
        } else {
            for (InterfaceClient client : clients) {
                for (int i = 0; i < list.size(); i++) {
                    if (client.getName().equals(list.get(i))) {
                        client.retrieveMessage(message);
                    }
                }
            }
        }
    }

    // Envia a mensagem apenas para um usuário
    @Override
    public synchronized void sendOnlyOnePersonMessage(String message, List<String> list) throws RemoteException {
        for (InterfaceClient client : clients) {
            for (int i = 0; i < list.size(); i++) {

                if (client.getName().equals(list.get(i))) {
                    client.retrieveMessage(message);
                }
            }
        }
    }

    // Adiciona um novo usuário a lista de usuários online
    @Override
    public synchronized void addUser(InterfaceClient client) throws RemoteException {
        this.clients.add(client);
    }

    // Retorna uma lista com todos os nomes dos usuários
    @Override
    public synchronized Vector<String> getUsernames(String name) throws RemoteException {
        Vector<String> list = new Vector<>();

        for (InterfaceClient client : clients) {
            if (!client.getName().equals(name)) {
                list.add(client.getName());
            }
        }
        return list;
    }

    // Encontra e remove múltiplos usuários do chat
    @Override
    public synchronized void deleteUser(List<String> clients) {
        for (int j = 0; j < this.clients.size(); j++) {
            for (int i = 0; i < clients.size(); i++) {
                try {
                    if (this.clients.get(j).getName().equals(clients.get(i))) {
                        this.clients.get(j).closeWhatsUT(clients.get(i) + " você foi banido do chat");
                        this.clients.remove(j);
                    }
                } catch (RemoteException ex) {
                    System.out.println("Erro: " + ex.getMessage());
                }
            }
        }
    }

    // Encontra o usuário e o remove do chat.
    @Override
    public synchronized void deleteUser(String clients) {
        for (int i = 0; i < this.clients.size(); i++) {
            try {
                if (this.clients.get(i).getName().equals(clients)) {
                    this.clients.remove(i);
                }
            } catch (RemoteException ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        }
    }

    // verifica se o nome do usuário já existe
    @Override
    public boolean ifUsernameExist(String username) throws RemoteException {
        boolean exist = false;

        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getName().equals(username)) {
                exist = true;
            }
        }

        return exist;
    }

    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {
            LocateRegistry.createRegistry(5432);
            Naming.rebind("rmi://localhost:5432/remote", new ChatServer());

        } catch (MalformedURLException | RemoteException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

        new ServerView().setVisible(true);
    }
}
