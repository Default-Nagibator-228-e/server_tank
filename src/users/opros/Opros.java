package users.opros;

import users.news.News;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Entity
@Table(
        name = "opros"
)
public class Opros implements Serializable {

    private static final long serialVersionUID = 234242234262L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    @Column(
            name = "id",
            unique = true,
            nullable = false
    )
    private Long id;

    @Column(
            name = "dat",
            nullable = false
    )
    private String dat;

    @Column(
            name = "zaag",
            nullable = false
    )
    private String za;

    @Column(
            name = "te",
            nullable = false
    )
    private String tex;

    @Column(
            name = "hash",
            nullable = false
    )
    private String hash;

    @Column(
            name = "no",
            nullable = false
    )
    private Boolean no;

    @Column(
            name = "clines",
            nullable = false
    )
    private String clines;

    public Long getId()
    {
        return id;
    }

    public String gethash()
    {
        return hash;
    }

    public String getDat()
    {
        return dat;
    }

    public String getZa()
    {
        return za;
    }

    public String getTex()
    {
        return tex;
    }

    public void setId(Long i)
    {
        id = i;
    }

    public void setDat(String s)
    {
        dat = s;
    }

    public void setZa(String s)
    {
        za = s;
    }

    public void setTex(String s)
    {
        tex = s;
    }

    public void sethash(String s)
    {
        hash = s;
    }

    public void setno(Boolean s)
    {
        no = s;
    }

    public Boolean getno()
    {
        return no;
    }

    public void setclines(String s)
    {
        clines = s;
    }

    public String getclines()
    {
        return clines;
    }
}
