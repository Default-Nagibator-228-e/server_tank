package battles.maps.parser.map.spawn;

public class SpawnPositionType {
   public static final SpawnPositionType BLUE = new SpawnPositionType();
   public static final SpawnPositionType RED = new SpawnPositionType();
   public static final SpawnPositionType NONE = new SpawnPositionType();

   public static SpawnPositionType getType(String value) {
      return value.equals("blue")?BLUE:(value.equals("red")?RED:(value.equals("dm")?NONE:NONE));
   }
}
