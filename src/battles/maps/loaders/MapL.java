package battles.maps.loaders;

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
        name = "maps"
)
public class MapL implements Serializable {
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
    public Long id;
    @Column(
            name = "name",
            nullable = false
    )
    public String name;
    @Column(
            name = "mapid",
            nullable = false
    )
    public String mapid;
    @Column(
            name = "min_rank",
            nullable = false
    )
    public int min_rank;
    @Column(
            name = "max_rank",
            nullable = false
    )
    public int max_rank;

    @Column(
            name = "max_players",
            nullable = false
    )
    public int max_players;

    @Column(
            name = "tdm",
            nullable = false
    )
    public Boolean tdm;

    @Column(
            name = "ctf",
            nullable = false
    )
    public Boolean ctf;

    @Column(
            name = "skybox_id",
            nullable = false
    )
    public String skybox_id;

    @Column(
            name = "theme_id",
            nullable = false
    )
    public String theme_id;
    @Column(
            name = "spawn",
            nullable = false
    )
    public String spawn;
    @Column(
            name = "bonus",
            nullable = false
    )
    public String bonus;
    @Column(
            name = "flags",
            nullable = false
    )
    public String flags;
}
