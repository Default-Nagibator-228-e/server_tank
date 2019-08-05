package battles.tanks.weapons;

public class ShotData {
   public double autoAimingAngleDown;
   public double autoAimingAngleUp;
   public int numRaysDown;
   public int numRaysUp;
   public int reloadMsec;
   public float impactCoeff;
   public float kickback;
   public float turretRotationAccel;
   public float turretRotationSpeed;
   public String id;

   public ShotData(String id, double autoAimingAngleDown, double autoAimingAngleUp, int numRaysDown, int numRaysUp, int reloadMsec, float impactCoeff, float kickback, float turretRotationAccel, float turretRotationSpeed) {
      this.id = id;
      this.autoAimingAngleDown = autoAimingAngleDown;
      this.autoAimingAngleUp = autoAimingAngleUp;
      this.numRaysDown = numRaysDown;
      this.numRaysUp = numRaysUp;
      this.reloadMsec = reloadMsec;
      this.impactCoeff = impactCoeff;
      this.kickback = kickback;
      this.turretRotationAccel = turretRotationAccel;
      this.turretRotationSpeed = turretRotationSpeed;
   }
}
