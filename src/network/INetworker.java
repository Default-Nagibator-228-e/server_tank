package network;

import java.io.IOException;

/** @deprecated */
@Deprecated
public interface INetworker {
   String onCommand() throws IOException;

   boolean send(String var1) throws IOException;

   void closeConnection();
}
