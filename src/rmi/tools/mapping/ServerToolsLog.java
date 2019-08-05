package rmi.tools.mapping;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@org.hibernate.annotations.Entity
@Table(
   name = "tools_log"
)
public class ServerToolsLog implements Serializable {
   private static final long serialVersionUID = -6487154273879506245L;
}
