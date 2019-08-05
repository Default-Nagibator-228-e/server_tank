package battles.ctf;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.ctf.anticheats.CaptureTheFlagAnticheatModel;
import battles.ctf.flags.FlagServer;
import battles.ctf.flags.FlagState;
import battles.tanks.math.Vector3;
import commands.Type;
import json.JSONUtils;
import services.TanksServices;
import services.annotations.ServicesInject;
import java.util.ArrayList;
import java.util.Iterator;

public class CTFModel extends CaptureTheFlagAnticheatModel {
   private BattlefieldModel bfModel;
   private FlagServer blueFlag = new FlagServer();
   private FlagServer redFlag = new FlagServer();
   @ServicesInject(
      target = TanksServices.class
   )
   private TanksServices tanksServices = TanksServices.getInstance();

   public CTFModel(BattlefieldModel bfModel) {
      super(bfModel);
      this.bfModel = bfModel;
      this.blueFlag.flagTeamType = "BLUE";
      this.redFlag.flagTeamType = "RED";
      this.blueFlag.state = FlagState.BASE;
      this.redFlag.state = FlagState.BASE;
      this.blueFlag.position = bfModel.battleInfo.map.flagBluePosition;
      this.blueFlag.basePosition = this.blueFlag.position;
      this.redFlag.position = bfModel.battleInfo.map.flagRedPosition;
      this.redFlag.basePosition = this.redFlag.position;
   }

   public void attemptToTakeFlag(BattlefieldPlayerController taker, String flagTeamType) {
      FlagServer flag = this.getTeamFlag(flagTeamType);
      if(flag.owner == null) {
         if(taker.playerTeamType.equals(flagTeamType)) {
            FlagServer enemyFlag = this.getEnemyTeamFlag(flagTeamType);
            if(flag.state == FlagState.DROPED) {
               this.returnFlag(taker, flag);
               return;
            }

            if(enemyFlag.owner == taker) {
               if(this.onDeliveredFlag(taker, enemyFlag)) {
                  return;
               }

               this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"deliver_flag", taker.playerTeamType, taker.tank.id});
               enemyFlag.state = FlagState.BASE;
               enemyFlag.owner = null;
               taker.flag = null;
               if(enemyFlag.returnTimer != null) {
                  enemyFlag.returnTimer.stop = true;
                  enemyFlag.returnTimer = null;
               }

               int score = (taker.playerTeamType == "BLUE"?this.bfModel.battleInfo.redPeople:this.bfModel.battleInfo.bluePeople) * 10;
               this.tanksServices.addScore(taker.parentLobby, score);
               taker.statistic.addScore(score);
               this.bfModel.statistics.changeStatistic(taker);
               double fund = 0.0D;
               ArrayList otherTeam = new ArrayList();
               Iterator var10 = this.bfModel.players.iterator();

               BattlefieldPlayerController otherPlayer;
               while(var10.hasNext()) {
                  otherPlayer = (BattlefieldPlayerController)var10.next();
                  if(!otherPlayer.playerTeamType.equals(taker.playerTeamType) && !otherPlayer.playerTeamType.equals("NONE")) {
                     otherTeam.add(otherPlayer);
                  }
               }

               for(var10 = otherTeam.iterator(); var10.hasNext(); fund += Math.sqrt((double)otherPlayer.getUser().getRang() * 0.125D)) {
                  otherPlayer = (BattlefieldPlayerController)var10.next();
               }

               this.bfModel.tanksKillModel.addFund(fund);
               if(taker.playerTeamType == "BLUE") {
                  ++this.bfModel.battleInfo.scoreBlue;
                  this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_team_scores", "BLUE", String.valueOf(this.bfModel.battleInfo.scoreBlue)});
                  if(this.bfModel.battleInfo.numFlags == this.bfModel.battleInfo.scoreBlue) {
                     this.bfModel.tanksKillModel.restartBattle(false);
                  }
               } else {
                  ++this.bfModel.battleInfo.scoreRed;
                  this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_team_scores", "RED", String.valueOf(this.bfModel.battleInfo.scoreRed)});
                  if(this.bfModel.battleInfo.numFlags == this.bfModel.battleInfo.scoreRed) {
                     this.bfModel.tanksKillModel.restartBattle(false);
                  }
               }
            }
         } else {
            if(this.onTakeFlag(taker, flag)) {
               return;
            }

            this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"flagTaken", taker.tank.id, flagTeamType});
            flag.state = FlagState.TAKEN_BY;
            flag.owner = taker;
            taker.flag = flag;
            if(flag.returnTimer != null) {
               flag.returnTimer.stop = true;
               flag.returnTimer = null;
            }
         }

      }
   }

   public void dropFlag(BattlefieldPlayerController following, Vector3 posDrop) {
      FlagServer flag = this.getEnemyTeamFlag(following.playerTeamType);
      flag.state = FlagState.DROPED;
      flag.position = posDrop;
      flag.owner = null;
      following.flag = null;
      flag.returnTimer = new FlagReturnTimer(this, flag);
      flag.returnTimer.start();
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"flag_drop", JSONUtils.parseDropFlagCommand(flag)});
   }

   public void returnFlag(BattlefieldPlayerController following, FlagServer flag) {
      flag.state = FlagState.BASE;
      if(flag.owner != null) {
         flag.owner.flag = null;
         flag.owner = null;
      }

      flag.position = flag.basePosition;
      if(flag.returnTimer != null) {
         flag.returnTimer.stop = true;
         flag.returnTimer = null;
      }

      String id = following == null?null:following.tank.id;
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"return_flag", flag.flagTeamType, id});
      byte score = 5;
      if(following != null) {
         this.tanksServices.addScore(following.parentLobby, score);
         following.statistic.addScore(score);
         this.bfModel.statistics.changeStatistic(following);
      }

   }

   private FlagServer getTeamFlag(String teamType) {
      return teamType.equals("BLUE")?this.blueFlag:(teamType.equals("RED")?this.redFlag:null);
   }

   private FlagServer getEnemyTeamFlag(String teamType) {
      return teamType.equals("BLUE")?this.redFlag:(teamType.equals("RED")?this.blueFlag:null);
   }

   public FlagServer getRedFlag() {
      return this.redFlag;
   }

   public FlagServer getBlueFlag() {
      return this.blueFlag;
   }
}
