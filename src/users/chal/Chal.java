package users.chal;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity
@Table(
        name = "chall"
)
public class Chal implements Serializable {
    private static final long serialVersionUID = -2764032649003874382L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id",
            unique = true,
            nullable = false
    )
    private long id;
    @Column(
            name = "pid",
            unique = false,
            nullable = false
    )
    private String pid;
    @Column(
            name = "nazv",
            unique = false,
            nullable = false
    )
    private String nazv;
    @Column(
            name = "tpe",
            unique = false,
            nullable = false
    )
    private String tpe;
    @Column(
            name = "sort",
            unique = false,
            nullable = false
    )
    private int sort;
    @Column(
            name = "count",
            unique = false,
            nullable = false
    )
    private int count;
    @Column(
            name = "prew",
            unique = false,
            nullable = false
    )
    private String prew;
    @Column(
            name = "pid1",
            unique = true,
            nullable = false
    )
    private String pid1;
    @Column(
            name = "nazv1",
            unique = false,
            nullable = false
    )
    private String nazv1;
    @Column(
            name = "type1",
            unique = false,
            nullable = false
    )
    private long type1;
    @Column(
            name = "sort1",
            unique = false,
            nullable = false
    )
    private int sort1;
    @Column(
            name = "count1",
            unique = false,
            nullable = false
    )
    private int count1;
    @Column(
            name = "prew1",
            unique = false,
            nullable = false
    )
    private String prew1;
    @Column(
            name = "zv",
            unique = false,
            nullable = false
    )
    private int zv;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNazv() {
        return this.nazv;
    }

    public void setNazv(String nazv) {
        this.nazv = nazv;
    }

    public String getNazv1() {
        return this.nazv1;
    }

    public void setNazv1(String nazv1) {
        this.nazv1 = nazv1;
    }

    public long getTpe() {
        return Long.parseLong(this.tpe);
    }

    public void setTpe(long type) {
        this.tpe = type + "";
    }

    public long getType1() {
        return this.type1;
    }

    public void setType1(long type1) {
        this.type1 = type1;
    }

    public int getsort() {
        return this.sort;
    }

    public void setsort(int sort) {
        this.sort = sort;
    }

    public int getsort1() {
        return this.sort1;
    }

    public void setsort1(int sort1) {
        this.sort1 = sort1;
    }

    public int getcount() {
        return this.count;
    }

    public void setcount(int count) {
        this.count = count;
    }

    public int getcount1() {
        return this.count1;
    }

    public void setcount1(int count1) {
        this.count1 = count1;
    }

    public String getprew() {
        return this.prew;
    }

    public void setprew(String prew) {
        this.prew = prew;
    }

    public String getprew1() {
        return this.prew1;
    }

    public void setprew1(String prew1) {
        this.prew1 = prew1;
    }

    public int gzv() {
        return this.zv;
    }

    public void szv(int zv) {
        this.zv = zv;
    }

    public String gpid() {
        return this.pid;
    }

    public void spid(String pid) {
        this.pid = pid;
    }

    public String gpid1() {
        return this.pid1;
    }

    public void spid1(String pid1) {
        this.pid1 = pid1;
    }
}
