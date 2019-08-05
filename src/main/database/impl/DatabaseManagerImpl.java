package main.database.impl;

import battles.tanks.loaders.Prop;
import groups.UserGroupsLoader;
import lobby.top.HallOfFame;
import logger.Logger;
import logger.Type;
import logger.remote.LogObject;
import logger.remote.RemoteDatabaseLogger;
import main.Main;
import main.database.DatabaseManager;
import main.netty.blackip.BlackIP;
import org.hibernate.Transaction;
import rmi.payments.mapping.Payment;
import services.hibernate.HibernateService;
import users.TypeUser;
import users.User;
import users.chal.Chal;
import users.friends.Friends;
import users.garage.Garage;
import users.invites.Invite;
import users.karma.Karma;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import users.news.News;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import users.opros.Opros;

public class DatabaseManagerImpl extends Thread implements DatabaseManager {
   private static final DatabaseManagerImpl instance = new DatabaseManagerImpl();
   private Map cache;

   private DatabaseManagerImpl() {
      super("DatabaseManagerImpl THREAD");
      this.cache = new TreeMap(String.CASE_INSENSITIVE_ORDER);
   }

   public void register(User user) {
      this.configurateNewAccount(user);
      Friends friend = new Friends();
      friend.setNick(user.getNickname());
      Garage garage = new Garage();
      System.out.println("Зарегистрирован: " + user.getNickname());
      garage.parseJSONData();
      garage.setUserId(user.getNickname());
      Karma emptyKarma = new Karma();
      emptyKarma.setUserId(user.getNickname());
      user.setFriend(friend);
      user.setLastIP("");
      user.opro = "";
      Session session = HibernateService.getSessionFactory().getCurrentSession();
      try {
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.save(user);
         session.save(friend);
         session.save(garage);
         session.save(emptyKarma);
         session.getTransaction().commit();
      } catch (Exception var7) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var7.printStackTrace();
         RemoteDatabaseLogger.error(var7);
      }

   }

   public void update(Chal chal) {
      Main.bs.c.remove(chal.getId());
      Main.bs.c.put(chal.getId(),chal);
      /*
      Session session = null;
      Transaction tx = null;
      User user = null;
      if((user = (User)this.cache.get(karma.getUserId())) != null) {
         user.setKarma(karma);
      }

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         tx = session.beginTransaction();
         session.update(karma);
         tx.commit();
      } catch (Exception var6) {
         assert tx != null;
         if(tx.wasRolledBack()) {
            tx.rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
      */
   }

   public void update(Karma karma) {
      Main.bs.k.remove(karma.getUserId());
      Main.bs.k.put(karma.getUserId(),karma);
      /*
      Session session = null;
      Transaction tx = null;
      User user = null;
      if((user = (User)this.cache.get(karma.getUserId())) != null) {
         user.setKarma(karma);
      }

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         tx = session.beginTransaction();
         session.update(karma);
         tx.commit();
      } catch (Exception var6) {
         assert tx != null;
         if(tx.wasRolledBack()) {
            tx.rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
      */
   }

   public void update(Garage garage) {
      Main.bs.g.remove(garage.getUserId());
      Main.bs.g.put(garage.getUserId(),garage);
      /*
      Session session = null;
      Transaction tx = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         tx = session.beginTransaction();
         session.update(garage);
         tx.commit();
      } catch (Exception var5) {
         assert tx != null;
         if(tx.wasRolledBack()) {
            tx.rollback();
         }

         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }
      */
   }

   public void update(Prop prop) {
      Main.bs.pr.remove(prop.get_name());
      Main.bs.pr.put(prop.get_name(),prop);
      /*
      Session session = null;
      Transaction tx = null;
      User user = null;
      if((user = (User)this.cache.get(karma.getUserId())) != null) {
         user.setKarma(karma);
      }

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         tx = session.beginTransaction();
         session.update(karma);
         tx.commit();
      } catch (Exception var6) {
         assert tx != null;
         if(tx.wasRolledBack()) {
            tx.rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
      */
   }

   public void update(User user) {
      Main.bs.u.remove(user.getNickname());
      Main.bs.u.put(user.getNickname(),user);
      /*
      Session session = null;
      Transaction tx = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         tx = session.beginTransaction();
         session.update(user);
         tx.commit();
      } catch (Exception var5) {
         assert tx != null;
         if(tx.wasRolledBack()) {
            tx.rollback();
         }

         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }
      */
   }

   public void update(News news) {
      Main.bs.n.remove(news.getId());
      Main.bs.n.put(news.getId(),news);

      /*
      Session session = null;
      Transaction tx = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         tx = session.beginTransaction();
         session.update(friends);
         tx.commit();
      } catch (Exception var5) {
         assert tx != null;
         if(tx.wasRolledBack()) {
            tx.rollback();
         }

         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }
      */
   }

    public void update(Invite friends) {
        Main.bs.i.remove(friends.getId());
        Main.bs.i.put(friends.getId(),friends);

      /*
      Session session = null;
      Transaction tx = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         tx = session.beginTransaction();
         session.update(friends);
         tx.commit();
      } catch (Exception var5) {
         assert tx != null;
         if(tx.wasRolledBack()) {
            tx.rollback();
         }

         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }
      */
    }

   public void update(Opros opros) {
      Main.bs.o.remove(opros.getId());
      Main.bs.o.put(opros.getId(),opros);
   }

    public void update0(Opros opros) {
        Main.bs.o.remove(opros.getId());
        Session session = HibernateService.getSessionFactory().getCurrentSession();
        try {
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
                session.beginTransaction();
            }
            session.update(opros);
            session.getTransaction().commit();
        } catch (Exception var51) {
            if(session.getTransaction() != null) {
                session.getTransaction().rollback();
            }

            var51.printStackTrace();
            RemoteDatabaseLogger.error(var51);
        }
    }

   public void update(Friends friends) {
      Main.bs.f.remove(friends.getNick());
      Main.bs.f.put(friends.getNick(),friends);
   }

   public void getUserByUid(int nickname) {
      Session session = null;
      User user = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM User U WHERE U.uid = 1");
         ResultSet er;
         //e.setBigInteger("uid", BigInteger.valueOf(1));
         user = (User)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
   }

   public News getNewsById(Long id) {
      Session session = null;
      News news = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM News U WHERE U.id = l");
         e.setLong("l", id);
         news = (News)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
      return news;
   }

   public User getUserById(String nickname) {
      Session session = null;
      User user = null;
      if((user = this.getUserByIdFromCache(nickname)) != null) {
         return user;
      } else {
         try {
            session = HibernateService.getSessionFactory().getCurrentSession();
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
               session.beginTransaction();
            }
            Query e = session.createQuery("FROM User U WHERE U.nickname = :nickname");
            e.setString("nickname", nickname);
            user = (User)e.uniqueResult();
            session.getTransaction().commit();
         } catch (Exception var6) {
            if(session.getTransaction() != null) {
               session.getTransaction().rollback();
            }

            var6.printStackTrace();
            RemoteDatabaseLogger.error(var6);
         }

         return user;
      }
   }

   public Prop getPrByName(String nickname) {
      Session session = null;
      Prop user = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM Prop U WHERE U.name = :nickname");
         e.setString("nickname", nickname);
         user = (Prop)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }

      return user;
   }

   public static DatabaseManager instance() {
      return instance;
   }

   public void cache(User user) {
      if(user == null) {
         Logger.log(Type.ERROR, "DatabaseManagerImpl::cache user is null!");
      } else {
         this.cache.put(user.getNickname(), user);
      }
   }

   public void uncache(String id) {
      this.cache.remove(id);
   }

   public User getUserByIdFromCache(String nickname) {
      return (User)this.cache.get(nickname);
   }

   public boolean contains(String nickname) {
      return this.getUserById(nickname) != null;
   }

   public void configurateNewAccount(User user) {
      user.setCrystall(500);
      user.setNextScore(100);
      user.part = 1;
      user.setType(TypeUser.TESTER);
      user.setUserGroup(UserGroupsLoader.getUserGroup(TypeUser.TESTER));
      user.setEmail("");
   }

   public int getCacheSize() {
      return this.cache.size();
   }

   public void initHallOfFame() {
      Session session = null;
      new ArrayList();

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         List users = session.createCriteria(User.class).list();
         HallOfFame.getInstance().initHallFromCollection(users);
      } catch (Exception var4) {
         RemoteDatabaseLogger.error(var4);
      }

   }

   public Friends getFriendByUser(User user) {
      Session session = null;
      Friends friends = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM Friends F WHERE F.n = :nickname");
         e.setString("nickname", user.getNickname());
         friends = (Friends)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }

      return friends;
   }

    public Invite getInvByCod(String cod1) {
        Session session = null;
        Invite pay = null;

        try {
            session = HibernateService.getSessionFactory().getCurrentSession();
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
                session.beginTransaction();
            }
            Query e = session.createQuery("FROM Invite P WHERE P.cod = :cod");
            e.setString("cod", cod1);
            pay = (Invite)e.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception var6) {
            if(session.getTransaction() != null) {
                session.getTransaction().rollback();
            }

            var6.printStackTrace();
            RemoteDatabaseLogger.error(var6);
        }

        return pay;
    }

   public Payment getPayByCod(String cod1) {
      Session session = null;
      Payment pay = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM Payment P WHERE P.cod = :cod");
         e.setString("cod", cod1);
         pay = (Payment)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }

      return pay;
   }

   public void DelPayByCod(Payment cod1) {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.delete(cod1);
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
   }

   public void DelInv(Invite cod1) {
      Session session = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.delete(cod1);
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
   }

   public Garage getGarageByUser(User user) {
      Session session = null;
      Garage garage = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM Garage G WHERE G.userId = :nickname");
         e.setString("nickname", user.getNickname());
         garage = (Garage)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }

      return garage;
   }

   public Karma getKarmaByUser(User user) {
      Session session = null;
      Karma karma = null;
      if(user.getKarma() != null) {
         return user.getKarma();
      } else {
         try {
            session = HibernateService.getSessionFactory().getCurrentSession();
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
               session.beginTransaction();
            }
            Query e = session.createQuery("FROM Karma K WHERE K.userId = :nickname");
            e.setString("nickname", user.getNickname());
            karma = (Karma)e.uniqueResult();
            session.getTransaction().commit();
         } catch (Exception var6) {
            if(session.getTransaction() != null) {
               session.getTransaction().rollback();
            }
            var6.printStackTrace();
            RemoteDatabaseLogger.error(var6);
         }

         return karma;
      }
   }

   public BlackIP getBlackIPbyAddress(String address) {
      Session session = null;
      BlackIP ip = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM BlackIP B WHERE B.ip = :ip");
         e.setString("ip", address);
         ip = (BlackIP)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var5) {
         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }

      return ip;
   }

   public void register(BlackIP blackIP) {
      Session session = null;
      if(this.getBlackIPbyAddress(blackIP.getIp()) == null) {
         try {
            session = HibernateService.getSessionFactory().getCurrentSession();
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
               session.beginTransaction();
            }
            session.saveOrUpdate(blackIP);
            session.getTransaction().commit();
         } catch (Exception var5) {
            if(session.getTransaction() != null) {
               session.getTransaction().rollback();
            }

            var5.printStackTrace();
            RemoteDatabaseLogger.error(var5);
         }

      }
   }

   public void register(News news) {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.saveOrUpdate(news);
         session.getTransaction().commit();
      } catch (Exception var5) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }
   }

   public void register(Opros opros) {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.saveOrUpdate(opros);
         session.getTransaction().commit();
      } catch (Exception var5) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }
   }

   public void unregister(Opros opros) {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.delete(opros);
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
   }

   public void unregister(News news) {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.delete(news);
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
   }

   public void unregister(BlackIP blackIP) {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.delete(blackIP);
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
   }

   public void register(LogObject log) {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         session.save(log);
         session.getTransaction().commit();
      } catch (Exception var5) {
         try {
            session = HibernateService.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(log);
            session.getTransaction().commit();
         } catch (Exception var6) {
            if(session.getTransaction() != null) {
               session.getTransaction().rollback();
            }
            var6.printStackTrace();
         }
      }

   }

   public List collectLogs() {
      Session session = null;
      List logs = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         logs = session.createCriteria(LogObject.class).list();
         session.getTransaction().commit();
         return logs;
      } catch (Exception var5) {
         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
         return null;
      }
   }

   public Payment getPaymentById(long paymentId) {
      Session session = null;
      Payment payment = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM Payment p WHERE p.idPayment = :pid");
         e.setLong("pid", paymentId);
         payment = (Payment)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }
         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
      return payment;
   }

   public Opros getOprosByHash(String hash) {
      Session session = null;
      Opros opros = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         Query e = session.createQuery("FROM Opros o WHERE o.hash = :pid");
         e.setString("pid", hash);
         opros = (Opros)e.uniqueResult();
         session.getTransaction().commit();
      } catch (Exception var6) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }
         var6.printStackTrace();
         RemoteDatabaseLogger.error(var6);
      }
      return opros;
   }

   public void update(Payment payment) {
      Main.bs.p.remove(payment.getNickname());
      Main.bs.p.put(payment.getNickname(),payment);
   }

   public List collectGarages() {
      Session session = null;
      List garages = null;

      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         garages = session.createCriteria(Garage.class).list();
         session.getTransaction().commit();
         return garages;
      } catch (Exception var5) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }
         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
         return null;
      }
   }
}
