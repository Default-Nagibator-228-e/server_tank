package rmi.payments.mapping;

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
   name = "payment"
)
public class Payment implements Serializable {
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
      name = "summ",
      unique = false,
      nullable = false
   )
   private int sum;
   @Column(
      name = "cod",
      unique = true,
      nullable = false
   )
   private String cod;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public int getSum() {
      return this.sum;
   }

   public void setSum(int sum) {
      this.sum = sum;
   }

   public String getNickname() {
      return this.cod;
   }

   public void setNickname(String nickname) {
      this.cod = nickname;
   }
}
