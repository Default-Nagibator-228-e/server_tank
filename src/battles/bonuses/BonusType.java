package battles.bonuses;

public enum BonusType
{
   ARMOR("ARMOR", 0),
   HEALTH("HEALTH", 1),
   DAMAGE("DAMAGE", 2),
   NITRO("NITRO", 3),
   GOLD("GOLD", 4),
   CRYSTALL("CRYSTALL", 5),
   CRYSTALL5("CRYSTALL5", 6),
   CRYSTALL10("CRYSTALL10", 7),
   CRYSTALL20("CRYSTALL20", 8),
   CRYSTALL50("CRYSTALL50", 9),
   CRYSTALL100("CRYSTALL100", 10);

   private BonusType(final String s, final int n) {
   }
}
