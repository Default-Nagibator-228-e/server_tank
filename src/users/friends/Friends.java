package users.friends;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;

@Entity
@org.hibernate.annotations.Entity
@Table(name = "dryzi")

public class Friends implements Serializable {

    private static final long serialVersionUID = 23424223424356L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            nullable = false,
            unique = true
    )
    private long id;
    @Column(
            name = "nick",
            //nullable = false,
            unique = true
    )
    private String n;
    @Column(
            name = "indr"
            //nullable = true
    )
    private String i;
    @Column(
            name = "outdr"
            //nullable = true
    )
    private String o;
    @Column(
            name = "dr"
            //nullable = true
    )
    private String d;

    public Friends() {
        n = "";
        i = "";
        o = "";
        d = "";
    }
    public String getNick() {
        return n;
    }

    public void setNick(String r) {
        n = r;
    }

    public String getInn() {
        return i;
    }

    public Vector<String> getInnV() {
        String[] gh = i.split("э");
        Vector<String> rt = new Vector<String>();
        for(int d = 0; d<gh.length ; d++)
        {
            if(!gh[d].isEmpty()) {
                rt.add(gh[d]);
            }
        }
        return rt;
    }

    public void setInn(Vector<String> r) {
        String f = "";
        for(int g = 0;g<r.size();g++)
        {
            if(!r.get(g).isEmpty()) {
                    if (f == "") {
                        f += r.get(g);
                    } else {
                        f += "э" + r.get(g);
                    }
            }
        }
        i = f;
    }

    public String getOutt() {
        return o;
    }

    public Vector<String> getOuttV() {
        String[] gh = o.split("э");
        Vector<String> rt = new Vector<String>();
        for(int d = 0; d<gh.length ; d++)
        {
            if(!gh[d].isEmpty()) {
                rt.add(gh[d]);
            }
        }
        return rt;
    }

    public void setOutt(Vector<String> r) {
        String f = "";
        for(int g = 0;g<r.size();g++)
        {
            if(!r.get(g).isEmpty()) {
                    if (f == "") {
                        f += r.get(g);
                    } else {
                        f += "э" + r.get(g);
                    }
            }
        }
        o = f;
    }

    public String getDr() {
        return d;
    }

    public Vector<String> getDrV() {
        String[] gh = d.split("э");
        Vector<String> rt = new Vector<String>();
        for(int d = 0; d<gh.length ; d++)
        {
            if(!gh[d].isEmpty()) {
                rt.add(gh[d]);
            }
        }
        return rt;
    }

    public void setDR(Vector<String> r) {
        String f = "";
        for(int g = 0;g<r.size();g++)
        {
            if(!r.get(g).isEmpty()) {
                    if (f == "") {
                        f += r.get(g);
                    } else {
                        f += "э" + r.get(g);
                    }
            }
        }
        d = f;
    }
}
