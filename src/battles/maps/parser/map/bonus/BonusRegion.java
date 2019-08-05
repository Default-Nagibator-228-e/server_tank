package battles.maps.parser.map.bonus;

import battles.maps.parser.Vector3d;
import battles.tanks.math.Vector3;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;

public class BonusRegion {
   private Vector3d max;
   private Vector3d min;
   private ArrayList type = new ArrayList();

   public Vector3d getMax() {
      return this.max;
   }

   @XmlElement(
      name = "max"
   )
   public void setMax(Vector3d max) {
      this.max = max;
   }

   public Vector3d getMin() {
      return this.min;
   }

   @XmlElement(
      name = "min"
   )
   public void setMin(Vector3d min) {
      this.min = min;
   }

   @XmlElement(
      name = "bonus-type"
   )
   public void setBonusType(String value) {
      this.type.add(BonusType.getType(value));
   }

   public ArrayList getType() {
      return this.type;
   }

   public String toString() {
      return "BONUS-REGION[TYPE = " + this.type + "] max: " + this.max + " min: " + this.min;
   }

   public battles.bonuses.BonusRegion toServerBonusRegion() {
      String[] convert = new String[this.type.size()];

      for(int i = 0; i < this.type.size(); ++i) {
         convert[i] = ((BonusType)this.type.get(i)).getValue();
      }

      return new battles.bonuses.BonusRegion(this.toVector3(this.max), this.toVector3(this.min), convert);
   }

   public Vector3 toVector3(Vector3d v) {
      return new Vector3(v.getX(), v.getY(), v.getZ());
   }
}
