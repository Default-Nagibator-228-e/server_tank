package test.server.configuration.entitys;

public class MineConfiguratorEntity {
   private int activationTimeMsec;
   private int farVisibilityRadius;
   private int nearVisibilityRadius;
   private double impactForce;
   private int minDistanceFromBase;
   private int radius;
   private int minDamage;
   private int maxDamage;

   public int getMinDamage() {
      return this.minDamage;
   }

   public void setMinDamage(int minDamage) {
      this.minDamage = minDamage;
   }

   public int getMaxDamage() {
      return this.maxDamage;
   }

   public void setMaxDamage(int maxDamage) {
      this.maxDamage = maxDamage;
   }

   public int getActivationTimeMsec() {
      return this.activationTimeMsec;
   }

   public void setActivationTimeMsec(int activationTimeMsec) {
      this.activationTimeMsec = activationTimeMsec;
   }

   public int getFarVisibilityRadius() {
      return this.farVisibilityRadius;
   }

   public void setFarVisibilityRadius(int farVisibilityRadius) {
      this.farVisibilityRadius = farVisibilityRadius;
   }

   public int getNearVisibilityRadius() {
      return this.nearVisibilityRadius;
   }

   public void setNearVisibilityRadius(int nearVisibilityRadius) {
      this.nearVisibilityRadius = nearVisibilityRadius;
   }

   public double getImpactForce() {
      return this.impactForce;
   }

   public void setImpactForce(double impactForce) {
      this.impactForce = impactForce;
   }

   public int getMinDistanceFromBase() {
      return this.minDistanceFromBase;
   }

   public void setMinDistanceFromBase(int minDistanceFromBase) {
      this.minDistanceFromBase = minDistanceFromBase;
   }

   public int getRadius() {
      return this.radius;
   }

   public void setRadius(int radius) {
      this.radius = radius;
   }
}
