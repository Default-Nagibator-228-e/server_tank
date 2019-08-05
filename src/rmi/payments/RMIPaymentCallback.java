package rmi.payments;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIPaymentCallback extends Remote {
   boolean paymentAccepted(long var1, String var3, int var4) throws RemoteException;
}
