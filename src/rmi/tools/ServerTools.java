package rmi.tools;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerTools extends Remote {
   void restart() throws RemoteException;
}
