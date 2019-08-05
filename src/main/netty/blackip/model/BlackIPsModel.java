package main.netty.blackip.model;

import collections.FastHashMap;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import main.netty.blackip.BlackIP;
import services.annotations.ServicesInject;

public class BlackIPsModel {
   @ServicesInject(
      target = DatabaseManagerImpl.class
   )
   private final DatabaseManager database = DatabaseManagerImpl.instance();
   private FastHashMap cache = new FastHashMap();

   public boolean contains(String ip) {
      if(this.cache.containsKey(ip)) {
         return true;
      } else {
         BlackIP obj = this.database.getBlackIPbyAddress(ip);
         boolean contains_ = obj != null;
         if(contains_) {
            this.cache.put(ip, Boolean.valueOf(true));
         }

         return contains_;
      }
   }

   public void block(String ip) {
      BlackIP blackIP = new BlackIP();
      blackIP.setIp(ip);
      this.database.register(blackIP);
   }

   public void unblock(String ip) {
      BlackIP blackIP = new BlackIP();
      blackIP.setIp(ip);
      this.database.unregister(blackIP);
   }
}
