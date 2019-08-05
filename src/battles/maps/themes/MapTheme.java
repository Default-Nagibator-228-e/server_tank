package battles.maps.themes;

public class MapTheme {
   private String gameModeId;
   private String ambientSoundId;

   public String getAmbientSoundId() {
      return this.ambientSoundId;
   }

   protected void setAmbientSoundId(String ambientSoundId) {
      this.ambientSoundId = ambientSoundId;
   }

   public String getGameModeId() {
      return this.gameModeId;
   }

   protected void setGameModeId(String gameModeId) {
      this.gameModeId = gameModeId;
   }
}
