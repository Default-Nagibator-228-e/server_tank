package users.invites;

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
        name = "invite"
)
public class Invite implements Serializable {
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
            name = "cod",
            nullable = false
    )
    private String cod;

    public Long getId()
    {
        return id;
    }

    public String getcod()
    {
        return cod;
    }

    public void setId(Long i)
    {
        id = i;
    }

    public void setcod(String s)
    {
        cod = s;
    }
}
