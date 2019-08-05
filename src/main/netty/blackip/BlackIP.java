package main.netty.blackip;

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
   name = "black_ips"
)
public class BlackIP implements Serializable {
   private static final long serialVersionUID = 429252039062757347L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   @Column(
      name = "idblack_ips",
      unique = true,
      nullable = false
   )
   private long id;
   @Column(
      name = "ip",
      unique = false,
      nullable = false
   )
   private String ip;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getIp() {
      return this.ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }
}
