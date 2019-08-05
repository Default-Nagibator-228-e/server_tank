package network.listeners;

import java.util.ArrayList;
import java.util.Iterator;

public class DisconnectListener {
   private ArrayList listeners = new ArrayList();

   public void addListener(IDisconnectListener listener) {
      this.listeners.add(listener);
   }

   public void removeListener(IDisconnectListener listener) {
      this.listeners.remove(listener);
   }

   public void onEvent() {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         IDisconnectListener listener = (IDisconnectListener)var2.next();
         listener.onDisconnect();
      }

   }
}
