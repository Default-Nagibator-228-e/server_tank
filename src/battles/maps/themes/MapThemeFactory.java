package battles.maps.themes;

public class MapThemeFactory {
   public static MapTheme getDefaultMapTheme() {
      return new MapTheme() {
         {
            this.setAmbientSoundId("default_ambient_sound");
            this.setGameModeId("default");
         }
      };
   }

   public static MapTheme getMapTheme(final String soundId, final String gameMode) {
      return new MapTheme() {
         {
            this.setAmbientSoundId(soundId);
            this.setGameModeId(gameMode);
         }
      };
   }
}
