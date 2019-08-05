package logger.remote;

import logger.remote.types.LogType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity
@Table(
   name = "logs"
)
public class LogObject implements Serializable {
   private static final long serialVersionUID = -1008857344268923373L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   @Column(
      name = "id",
      nullable = false
   )
   private Long id;
   @Column(
      name = "date",
      nullable = false
   )
   private Date date;
   @Column(
      name = "type",
      nullable = false
   )
   @Enumerated(EnumType.STRING)
   private LogType type;
   @Column(
      name = "message",
      nullable = false
   )
   private String message;

   public Date getDate() {
      return this.date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public LogType getType() {
      return this.type;
   }

   public void setType(LogType type) {
      this.type = type;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String toString() {
      return this.date.toString() + " [" + this.type.toString() + "]: " + this.message;
   }
}
