package main.database.impl;

import battles.tanks.loaders.Prop;
import rmi.payments.mapping.Payment;
import services.hibernate.HibernateService;
import users.User;
import users.chal.Chal;
import users.friends.Friends;
import users.garage.Garage;
import users.invites.Invite;
import users.karma.Karma;
import users.news.News;
import org.hibernate.Transaction;
import org.hibernate.Session;
import users.opros.Opros;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class Upd extends TimerTask {
    public HashMap<String,Karma> k = new HashMap<>();
    public HashMap<String,Garage> g = new HashMap<>();
    public HashMap<String,User> u = new HashMap<>();
    public HashMap<String,Friends> f = new HashMap<>();
    public HashMap<String,Payment> p = new HashMap<>();
    public HashMap<String,Prop> pr = new HashMap<>();
    public HashMap<Long,News> n = new HashMap<>();
    public HashMap<Long,Chal> c = new HashMap<>();
    public HashMap<Long, Invite> i = new HashMap<>();
    public HashMap<Long, Opros> o = new HashMap<>();
    private Timer t = new Timer();

    public Upd() {
        t.schedule(this,0,10000);
    }

    public void run() {
        Iterator gv = this.g.values().iterator();
        Iterator kv = this.k.values().iterator();
        Iterator uv = this.u.values().iterator();
        Iterator fv = this.f.values().iterator();
        Iterator pv = this.p.values().iterator();
        Iterator prv = this.pr.values().iterator();
        Iterator nv = this.n.values().iterator();
        Iterator cv = this.c.values().iterator();
        Iterator iv = this.i.values().iterator();
        Iterator ov = this.o.values().iterator();
        while(gv.hasNext()) {
            try {
                Garage item = (Garage)gv.next();
                gv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.g.remove(item.getUserId());
                tx.commit();
            } catch (Exception var51) {
                gv = this.g.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(kv.hasNext()) {
            try {
                Karma item = (Karma)kv.next();
                kv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.k.remove(item.getUserId());
                tx.commit();
            } catch (Exception var51) {
                kv = this.k.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(uv.hasNext()) {
            try {
                User item = (User)uv.next();
                uv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.u.remove(item.getNickname());
                tx.commit();
            } catch (Exception var51) {
                uv = this.u.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(fv.hasNext()) {
            try {
                Friends item = (Friends)fv.next();
                fv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.f.remove(item.getNick());
                tx.commit();
            } catch (Exception var51) {
                fv = this.f.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(pv.hasNext()) {
            try {
                Payment item = (Payment)pv.next();
                pv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.p.remove(item.getNickname());
                tx.commit();
            } catch (Exception var51) {
                pv = this.p.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(prv.hasNext()) {
            try {
                Prop item = (Prop)prv.next();
                prv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.pr.remove(item.get_name());
                tx.commit();
            } catch (Exception var51) {
                prv = this.pr.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(nv.hasNext()) {
            try {
                News item = (News)nv.next();
                nv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.n.remove(item.getId());
                tx.commit();
            } catch (Exception var51) {
                nv = this.n.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(cv.hasNext()) {
            try {
                Chal item = (Chal)cv.next();
                cv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.c.remove(item.getId());
                tx.commit();
            } catch (Exception var51) {
                cv = this.c.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(iv.hasNext()) {
            try {
                Invite item = (Invite)iv.next();
                iv.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.i.remove(item.getId());
                tx.commit();
            } catch (Exception var51) {
                iv = this.i.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        while(ov.hasNext()) {
            try {
                Opros item = (Opros)ov.next();
                ov.remove();
                Session session = HibernateService.getSessionFactory().getCurrentSession();
                Transaction tx = session.beginTransaction();
                session.update(item);
                this.o.remove(item.getId());
                tx.commit();
            } catch (Exception var51) {
                ov = this.o.values().iterator();
            }
            try {
                Thread.sleep(10);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
    }
}