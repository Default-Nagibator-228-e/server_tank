package battles.tanks.math;

public class Vector3 {
   public float x = 0.0F;
   public float y = 0.0F;
   public float z = 0.0F;
   public double rot = 0.0D;

   public Vector3(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public double distanceTo(Vector3 to) {
      return Math.sqrt(this.pow2((double)(this.x - to.x)) + this.pow2((double)(this.y - to.y)) + this.pow2((double)(this.z - to.z)));
   }

   public double distanceToWithoutZ(Vector3 to) {
      return Math.sqrt(this.pow2((double)(this.x - to.x)) + this.pow2((double)(this.y - to.y)));
   }

   private double pow2(double value) {
      return Math.pow(value, 2.0D);
   }
}
