package main.params;

import commands.Type;
import services.LobbysServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class OnlineStats {
   private static int online;
   private static Vector<String> inon = new Vector<>();
   private static ArrayList stat = new ArrayList();

   public static int getOnline() {
      return online;
   }

   public static void addOnline() {
      ++online;
      stat.add(Integer.valueOf(online));
   }

   public static void addinOnline(String b) {
      inon.add(b);
      LobbysServices.getInstance().chCommandToAllUsers(Type.LOBBY,"addon",b);
   }

   public static int inOnline(String b) {
      return inon.contains(b)?1:0;
   }

   public static void removeOnline() {
      --online;
   }

   public static void reminOnline(String b) {
      inon.remove(b);
      LobbysServices.getInstance().chCommandToAllUsers(Type.LOBBY,"addon",b);
   }

   public static int getMaxOnline() {
      return stat.size() == 0?0:((Integer)Collections.max(stat)).intValue();
   }
}
