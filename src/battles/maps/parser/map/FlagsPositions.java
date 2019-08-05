package battles.maps.parser.map;

import battles.maps.parser.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
   name = "ctf-flags"
)
class FlagsPositions {
   private Vector3d redFlag;
   private Vector3d blueFlag;

   public Vector3d getRedFlag() {
      return this.redFlag;
   }

   @XmlElement(
      name = "flag-red"
   )
   public void setRedFlag(Vector3d redFlag) {
      this.redFlag = redFlag;
   }

   public Vector3d getBlueFlag() {
      return this.blueFlag;
   }

   @XmlElement(
      name = "flag-blue"
   )
   public void setBlueFlag(Vector3d blueFlag) {
      this.blueFlag = blueFlag;
   }

   public String toString() {
      return "red flag: " + this.redFlag + " blue: " + this.blueFlag;
   }
}
