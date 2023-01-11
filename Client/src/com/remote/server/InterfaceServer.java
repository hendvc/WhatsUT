package com.remote.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.remote.client.InterfaceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public interface InterfaceServer extends Remote{    
    void broadcastMsg(String message,List<String> list) throws RemoteException;
    
    void sendOnlyOnePersonMessage(String message, List<String> list) throws RemoteException;
    
    Vector<String> getUsernames(String name) throws RemoteException;
    
    void addUser(InterfaceClient client) throws RemoteException;
    
    void deleteUser(List<String> clients) throws RemoteException;
    
    void deleteUser(String clients) throws RemoteException;
    
    boolean ifUsernameExist(String username) throws RemoteException;
}