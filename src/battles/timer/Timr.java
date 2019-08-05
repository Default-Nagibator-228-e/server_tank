package battles.timer;

import main.Main;
import services.hibernate.HibernateService;
import users.chal.Chal;
import org.hibernate.Session;

import java.util.List;

public class Timr implements Runnable {
    public long time;

    public Timr(long time) {
        this.time = time;
        (new Thread(this)).start();
    }

    public void run() {
        try {
            Thread.sleep(this.time);
            Main.ych = false;
            Session session = HibernateService.getSessionFactory().getCurrentSession();
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
                session.beginTransaction();
            }
            List<Chal> product = (List<Chal>) session.createQuery("from Chal").list();
            if(product.size() > 0)
            {
                for(int bt = 0;bt < product.size(); bt++)
                {
                    if(product.get(bt).getsort() == 5)
                    {
                        product.get(bt).setprew("");
                    }
                }
            }
            session.getTransaction().commit();
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

    }
}