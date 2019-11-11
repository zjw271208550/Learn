package com.example.rmiserver.service;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Server远程方法接口
 */
public interface InfoRMI extends Remote {
    public void updateInfoValueById(String id, Integer value) throws RemoteException;
}
