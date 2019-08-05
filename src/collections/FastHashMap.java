package collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FastHashMap implements Iterable {
   private ConcurrentHashMap map = new ConcurrentHashMap();

   public void put(Object key, Object value) {
      if(key != null && value != null) {
         this.map.put(key, value);
      }
   }

   public void remove(Object key) {
      if(key != null) {
         this.map.remove(key);
      }
   }

   public void remove(Object key, Object value) {
      if(key != null && value != null) {
         this.map.remove(key, value);
      }
   }

   public int size() {
      return this.map.size();
   }

   public void clear() {
      this.map.clear();
   }

   public Object get(Object key) {
      Object value = null;
      if(key != null && this.map.containsKey(key)) {
         value = this.map.get(key);
      }

      return value;
   }

   public boolean containsKey(Object key) {
      return key == null?false:this.map.containsKey(key);
   }

   public Collection values() {
      return this.map.values();
   }

   public Set entys() {
      return this.map.entrySet();
   }

   public Iterator iterator() {
      return this.map.values().iterator();
   }
}
