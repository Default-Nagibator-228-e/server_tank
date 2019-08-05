package battles.effects;

public enum EffectType {
   HEALTH {
      public String toString() {
         return "health";
      }
   },
   ARMOR {
      public String toString() {
         return "armor";
      }
   },
   DAMAGE {
      public String toString() {
         return "damage";
      }
   },
   NITRO {
      public String toString() {
         return "nitro";
      }
   },
   MINE {
      public String toString() {
         return "mine";
      }
   };

   private EffectType() {
   }

   // $FF: synthetic method
   EffectType(EffectType var3) {
      this();
   }
}
