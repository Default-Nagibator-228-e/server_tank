package battles.tanks.colormaps;

import users.garage.enums.PropertyType;
import users.garage.items.Item;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class ColormapsFactory
{
   private static Map<String, Colormap> colormaps;

   static {
      ColormapsFactory.colormaps = new HashMap<String, Colormap>();
   }

   public static void addColormap(final String id, final Colormap colormap) {
      ColormapsFactory.colormaps.put(id, colormap);
   }

   public static Colormap getColormap(final Item id) {
      return ColormapsFactory.colormaps.get(id.getId());
   }

   private static Integer roundResult (double d1, int precise) {
      DecimalFormat df = new DecimalFormat("#");
      df.setRoundingMode(RoundingMode.CEILING);
      for (Number n : Arrays.asList(d1)) {
         d1 = n.doubleValue();
      }
      return Integer.parseInt(df.format(d1).replace(",0",""));
   }

   public static ColormapResistanceType getResistanceType(final PropertyType pType) {
      ColormapResistanceType type = null;
      switch (pType) {
         case FIRE_RESISTANCE: {
            type = ColormapResistanceType.FLAMETHROWER;
            break;
         }
         case FREEZE_RESISTANCE: {
            type = ColormapResistanceType.FREZEE;
            break;
         }
         case MECH_RESISTANCE: {
            type = ColormapResistanceType.SMOKY;
            break;
         }
         case PLASMA_RESISTANCE: {
            type = ColormapResistanceType.TWINS;
            break;
         }
         case RAIL_RESISTANCE: {
            type = ColormapResistanceType.RAILGUN;
            break;
         }
         case RICOCHET_RESISTANCE: {
            type = ColormapResistanceType.RICOCHET;
            break;
         }
         case THUNDER_RESISTANCE: {
            type = ColormapResistanceType.THUNDER;
            break;
         }
         case VAMPIRE_RESISTANCE: {
            type = ColormapResistanceType.ISIDA;
            break;
         }
      }
      return type;
   }
}