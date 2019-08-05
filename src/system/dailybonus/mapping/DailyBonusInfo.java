package system.dailybonus.mapping;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity
@Table(
   name = "daily_bonus_info"
)
public class DailyBonusInfo {
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   @Column(
      name = "uid",
      nullable = false,
      unique = true
   )
   private long id;
   @Column(
      name = "last_issue_bonuses",
      nullable = false
   )
   private Date lastIssueBonuses;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public Date getLastIssueBonuses() {
      return this.lastIssueBonuses;
   }

   public void setLastIssueBonuses(Date lastIssueBonuses) {
      this.lastIssueBonuses = lastIssueBonuses;
   }
}
