package battles.tanks.loaders;

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
        name = "Physics"
)
public class Prop implements Serializable {
    private static final long serialVersionUID = 2342422342L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id",
            nullable = false,
            unique = true
    )
    private long id;
    @Column(
            name = "name",
            nullable = false
    )
    private String name;
    @Column(
            name = "val",
            nullable = false
    )
    private String val;


    public String get_name() {
        return name;
    }

    public String get_val() {
        return val;
    }

    public void set_val(String v) {
        val = v;
    }
}
