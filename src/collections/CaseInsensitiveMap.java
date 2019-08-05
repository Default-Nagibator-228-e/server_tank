package collections;

import collections.strings.CaseInsensitiveString;
import java.util.HashMap;

public class CaseInsensitiveMap extends HashMap {
   private static final long serialVersionUID = -6234177260678293672L;

   public Object put(CaseInsensitiveString key, Object obj) {
      return super.put(key, obj);
   }

   public Object get(CaseInsensitiveString key) {
      return super.get(key);
   }

   public Object remove(CaseInsensitiveString key) {
      return super.remove(key);
   }
}
