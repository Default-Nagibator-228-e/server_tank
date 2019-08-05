package main.database;

import battles.tanks.loaders.Prop;
import logger.remote.LogObject;
import main.netty.blackip.BlackIP;
import rmi.payments.mapping.Payment;
import users.User;
import users.chal.Chal;
import users.friends.Friends;
import users.garage.Garage;
import users.invites.Invite;
import users.karma.Karma;
import users.news.News;
import users.opros.Opros;

import java.util.List;

public interface DatabaseManager {
   void getUserByUid(int var1);

   User getUserById(String var1);

   News getNewsById(Long var1);

   User getUserByIdFromCache(String var1);

   Garage getGarageByUser(User var1);

   Friends getFriendByUser(User var1);

   Payment getPayByCod(String var1);

   Invite getInvByCod(String var1);

   void DelPayByCod(Payment var1);

   void DelInv(Invite var1);

   Prop getPrByName(String nickname);

   Karma getKarmaByUser(User var1);

   BlackIP getBlackIPbyAddress(String var1);

   Opros getOprosByHash(String var1);

   Payment getPaymentById(long var1);

   List collectLogs();

   List collectGarages();

   void update(User var1);

   void update(Chal var1);

   void update(Prop var1);

   void update(Garage var1);

   void update(Karma var1);

   void update(News var1);

   void update(Invite var1);

   void update(Payment var1);

   void update(Friends var1);

   void update(Opros var1);

   void update0(Opros var1);

   void register(User var1);

   void register(News var1);

   void register(BlackIP var1);

   void register(LogObject var1);

   void unregister(BlackIP var1);

   void unregister(News var1);

   void register(Opros var1);

   void unregister(Opros var1);

   void cache(User var1);

   void uncache(String var1);

   void initHallOfFame();

   boolean contains(String var1);

   int getCacheSize();
}
