package battles.managers;

import battles.tanks.math.Vector3;

public class SpawnPosition {
   public Vector3 position;
   public Vector3 orintation;

   public SpawnPosition(Vector3 position, Vector3 orintation) {
      this.position = position;
      this.orintation = orintation;
   }
}
