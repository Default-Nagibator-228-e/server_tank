package services.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateService {

   private static SessionFactory sessionFactory = (new Configuration().configure("hibernate.cfg.xml")).buildSessionFactory();

   public static SessionFactory getSessionFactory() {
      return sessionFactory;
   }
}
