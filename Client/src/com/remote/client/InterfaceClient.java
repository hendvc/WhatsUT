package com.remote.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface InterfaceClient extends Remote{
    void retrieveMessage(String message) throws RemoteException;
    
    void sendMessage(List<String> list) throws RemoteException;
    
    String getName()throws RemoteException;
    
    void closeWhatsUT(String message) throws RemoteException;
}
