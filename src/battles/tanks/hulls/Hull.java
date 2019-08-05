package battles.tanks.hulls;

public class Hull {
   public float mass;
   public float power;
   public float maxForwardSpeed;
   public float maxBackwardSpeed;
   public float maxTurnSpeed;
   public float springDamping;
   public float drivingForceOffsetZ;
   public float smallVelocity;
   public float rayRestLengthCoeff;
   public float dynamicFriction;
   public float brakeFriction;
   public float sideFriction;
   public float spotTurnPowerCoeff;
   public float spotTurnDynamicFriction;
   public float spotTurnSideFriction;
   public float moveTurnPowerCoeffOuter;
   public float moveTurnPowerCoeffInner;
   public float moveTurnDynamicFrictionInner;
   public float moveTurnDynamicFrictionOuter;
   public float moveTurnSideFriction;
   public float moveTurnSpeedCoeffInner;
   public float moveTurnSpeedCoeffOuter;
   public float hp;

   public Hull(float mass,float power,float maxForwardSpeed,float maxBackwardSpeed,float maxTurnSpeed,float springDamping,float drivingForceOffsetZ,float smallVelocity,float rayRestLengthCoeff,float dynamicFriction,float brakeFriction,float sideFriction,float spotTurnPowerCoeff,float spotTurnDynamicFriction,float spotTurnSideFriction,float moveTurnPowerCoeffOuter,float moveTurnPowerCoeffInner,float moveTurnDynamicFrictionInner,float moveTurnDynamicFrictionOuter,float moveTurnSideFriction,float moveTurnSpeedCoeffInner,float moveTurnSpeedCoeffOuter, float hp) {
      this.mass = mass;
      this.power = power;
      this.maxForwardSpeed = maxForwardSpeed;
      this.maxBackwardSpeed = maxBackwardSpeed;
      this.maxTurnSpeed = maxTurnSpeed;
      this.springDamping = springDamping;
      this.drivingForceOffsetZ = drivingForceOffsetZ;
      this.smallVelocity = smallVelocity;
      this.rayRestLengthCoeff = rayRestLengthCoeff;
      this.dynamicFriction = dynamicFriction;
      this.brakeFriction = brakeFriction;
      this.sideFriction = sideFriction;
      this.spotTurnPowerCoeff = spotTurnPowerCoeff;
      this.spotTurnDynamicFriction = spotTurnDynamicFriction;
      this.spotTurnSideFriction = spotTurnSideFriction;
      this.moveTurnPowerCoeffOuter = moveTurnPowerCoeffOuter;
      this.moveTurnPowerCoeffInner = moveTurnPowerCoeffInner;
      this.moveTurnDynamicFrictionInner = moveTurnDynamicFrictionInner;
      this.moveTurnDynamicFrictionOuter = moveTurnDynamicFrictionOuter;
      this.moveTurnSideFriction = moveTurnSideFriction;
      this.moveTurnSpeedCoeffInner = moveTurnSpeedCoeffInner;
      this.moveTurnSpeedCoeffOuter = moveTurnSpeedCoeffOuter;
      this.hp = hp;
   }
}
