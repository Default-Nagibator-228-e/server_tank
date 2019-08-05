package battles.ctf.flags;

import battles.BattlefieldPlayerController;
import battles.ctf.FlagReturnTimer;
import battles.tanks.math.Vector3;

public class FlagServer {
   public String flagTeamType;
   public BattlefieldPlayerController owner;
   public Vector3 position;
   public Vector3 basePosition;
   public FlagState state;
   public FlagReturnTimer returnTimer;
}
