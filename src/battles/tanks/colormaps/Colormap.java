package battles.tanks.colormaps;

import battles.tanks.weapons.EntityType;
import java.util.HashMap;
import java.util.Vector;

public class Colormap
{
   public HashMap<ColormapResistanceType, Integer> resistances;
   public Vector<ColormapResistanceType> resistance = new Vector<ColormapResistanceType>();

   public Colormap() {
      this.resistances = new HashMap<ColormapResistanceType, Integer>();
   }

   public void addResistance(final ColormapResistanceType type, final int percent) {
      this.resistances.put(type, percent);
      resistance.add(type);
   }

   public Integer getResistance(final EntityType weaponType) {
      return this.resistances.get(this.getResistanceTypeByWeapon(weaponType));
   }

   private ColormapResistanceType getResistanceTypeByWeapon(final EntityType weaponType) {
      ColormapResistanceType type = null;
      switch (weaponType) {
         case SMOKY: {
            type = ColormapResistanceType.SMOKY;
            break;
         }
         case FLAMETHROWER: {
            type = ColormapResistanceType.FLAMETHROWER;
            break;
         }
         case FREZZE: {
            type = ColormapResistanceType.FREZEE;
            break;
         }
         case ISIDA: {
            type = ColormapResistanceType.ISIDA;
            break;
         }
         case RAILGUN: {
            type = ColormapResistanceType.RAILGUN;
            break;
         }
         case RICOCHET: {
            type = ColormapResistanceType.RICOCHET;
            break;
         }
         case THUNDER: {
            type = ColormapResistanceType.THUNDER;
            break;
         }
         case TWINS: {
            type = ColormapResistanceType.TWINS;
            break;
         }
      }
      return type;
   }
}