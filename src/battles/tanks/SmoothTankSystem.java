package battles.tanks;

import battles.BattlefieldPlayerController;


public class SmoothTankSystem implements Runnable {
   private final float FACTOR_SMOOTH = 1.0F;
   public BattlefieldPlayerController controller;
   public Tank currentPoint;
   public Tank finalPoint;
   private boolean isFirstAdded = true;
   private boolean x_adding;
   private boolean y_adding;
   private boolean z_adding;

   public SmoothTankSystem(BattlefieldPlayerController controller, Tank finalPoint) {
      this.controller = controller;
      this.finalPoint = finalPoint;
      this.currentPoint = controller.tank;
   }

   public void run() {
      boolean x_end;
      boolean y_end;
      boolean z_end;
      do {
         if(this.isFirstAdded) {
            System.out.println(this.currentPoint.position == null);
            if(this.currentPoint.position.x <= this.finalPoint.position.x) {
               this.x_adding = true;
            } else {
               this.x_adding = false;
            }

            if(this.currentPoint.position.y <= this.finalPoint.position.y) {
               this.y_adding = true;
            } else {
               this.y_adding = false;
            }

            if(this.currentPoint.position.z <= this.finalPoint.position.z) {
               this.z_adding = true;
            } else {
               this.z_adding = false;
            }

            this.isFirstAdded = false;
         }

         this.currentPoint.position.x = this.x_adding?this.currentPoint.position.x + 1.0F:this.currentPoint.position.x - 1.0F;
         this.currentPoint.position.y = this.y_adding?this.currentPoint.position.y + 1.0F:this.currentPoint.position.y - 1.0F;
         this.currentPoint.position.z = this.z_adding?this.currentPoint.position.z + 1.0F:this.currentPoint.position.z - 1.0F;
         x_end = this.x_adding?this.currentPoint.position.x >= this.finalPoint.position.x:this.currentPoint.position.x <= this.finalPoint.position.x;
         y_end = this.y_adding?this.currentPoint.position.y >= this.finalPoint.position.y:this.currentPoint.position.y <= this.finalPoint.position.y;
         z_end = this.z_adding?this.currentPoint.position.z >= this.finalPoint.position.z:this.currentPoint.position.z <= this.finalPoint.position.z;
      } while(!x_end || !y_end || !z_end);

      this.controller.battle.setTank(this.controller, this.currentPoint);
   }
}
