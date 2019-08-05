package users.garage.enums;

public enum PropertyType {
   DAMAGE {
      public String toString() {
         return "damage";
      }
   },
   DAMAGE_PER_SECOND {
      public String toString() {
         return "damage_per_second";
      }
   },
   AIMING_ERROR {
      public String toString() {
         return "aiming_error";
      }
   },
   CONE_ANGLE {
      public String toString() {
         return "cone_angle";
      }
   },
   SHOT_FORCE {
      public String toString() {
         return "shot_force";
      }
   },
   PER_KR {
      public String toString() {
         return "per_kr";
      }
   },
   T_GOR {
      public String toString() {
         return "t_gor";
      }
   },
   KRIT {
      public String toString() {
         return "krit";
      }
   },
   PER_PROST {
      public String toString() {
         return "per_prost";
      }
   },
   MASS {
      public String toString() {
         return "mass";
      }
   },
   POWER {
      public String toString() {
         return "power";
      }
   },
   SHOT_FREQUENCY {
      public String toString() {
         return "shot_frequency";
      }
   },
   SHOT_RANGE {
      public String toString() {
         return "shot_range";
      }
   },
   TURN_SPEED {
      public String toString() {
         return "turn_speed";
      }
   },
   MECH_RESISTANCE {
      public String toString() {
         return "mech_resistance";
      }
   },
   PLASMA_RESISTANCE {
      public String toString() {
         return "plasma_resistance";
      }
   },
   RAIL_RESISTANCE {
      public String toString() {
         return "rail_resistance";
      }
   },
   VAMPIRE_RESISTANCE {
      public String toString() {
         return "vampire_resistance";
      }
   },
   ARMOR {
      public String toString() {
         return "armor";
      }
   },
   TURRET_TURN_SPEED {
      public String toString() {
         return "turret_turn_speed";
      }
   },
   FIRE_RESISTANCE {
      public String toString() {
         return "fire_resistance";
      }
   },
   THUNDER_RESISTANCE {
      public String toString() {
         return "thunder_resistance";
      }
   },
   FREEZE_RESISTANCE {
      public String toString() {
         return "freeze_resistance";
      }
   },
   RICOCHET_RESISTANCE {
      public String toString() {
         return "ricochet_resistance";
      }
   },
   HEALING_RADUIS {
      public String toString() {
         return "healing_radius";
      }
   },
   HEAL_RATE {
      public String toString() {
         return "heal_rate";
      }
   },
   VAMPIRE_RATE {
      public String toString() {
         return "vampire_rate";
      }
   },
   SPEED {
      public String toString() {
         return "speed";
      }
   },
   UNKNOWN {
      public String toString() {
         return "unknown";
      }
   };

   private PropertyType() {
   }

   // $FF: synthetic method
   PropertyType(PropertyType var3) {
      this();
   }
}
