package exceptions;

import logger.Logger;
import logger.Type;
import java.util.Arrays;

public class ServerException extends Exception {
   private static final long serialVersionUID = 1L;

   public ServerException(String error) {
      super(error);
      Logger.log(Type.ERROR, "Throw server exception with message: " + error);
   }

   public String toString() {
      return Arrays.toString(super.getStackTrace());
   }
}
