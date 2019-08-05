package battles.maps.parser.map.spawn;

import battles.maps.parser.Vector3d;
import battles.tanks.math.Vector3;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
   name = "spawn-point"
)
public class SpawnPosition {
   private Vector3d position;
   private Vector3d rotation;
   private String type;

   public SpawnPosition() {
   }

   public SpawnPosition(Vector3d position, Vector3d rotation, String type) {
      this.position = position;
      this.rotation = rotation;
      this.type = type;
   }

   @XmlElement(
      name = "position"
   )
   public Vector3d getPosition() {
      return this.position;
   }

   public void setType(String value) {
      this.type = value;
   }

   @XmlAttribute(
      name = "type"
   )
   public String getType() {
      return this.type;
   }

   public SpawnPositionType getSpawnPositionType() {
      return SpawnPositionType.getType(this.type);
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public Vector3d getRotation() {
      return this.rotation;
   }

   public void setRotation(Vector3d rotation) {
      this.rotation = rotation;
   }

   public String toString() {
      return this.position + " direction:" + this.rotation;
   }

   public battles.managers.SpawnPosition toServerSpawnPosition() {
      return new battles.managers.SpawnPosition(this.toVector3(this.position), this.toVector3(this.rotation));
   }

   public Vector3 toVector3(Vector3d v) {
      return new Vector3(v.getX(), v.getY(), v.getZ());
   }

   public Vector3 getVector3() {
      return new Vector3(this.position.getX(), this.position.getY(), this.position.getZ()) {
         {
            this.rot = (double)SpawnPosition.this.rotation.getZ();
         }
      };
   }
}
