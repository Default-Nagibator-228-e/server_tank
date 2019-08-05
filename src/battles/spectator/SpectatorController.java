package battles.spectator;

import battles.tanks.loaders.WeaponsFactory;
import users.User;
import json.JSONUtils;
import commands.Type;
import logger.Logger;
import commands.Command;
import battles.BattlefieldModel;
import lobby.LobbyManager;
import network.listeners.IDisconnectListener;
import battles.spectator.comands.SpectatorControllerComandsConst;

public class SpectatorController extends SpectatorControllerComandsConst implements IDisconnectListener
{
   private static final String NULL_JSON_STRING = "{}";
   private LobbyManager lobby;
   private BattlefieldModel bfModel;
   private SpectatorModel specModel;
   public boolean inited;

   public SpectatorController(final LobbyManager lobby, final BattlefieldModel bfModel, final SpectatorModel specModel) {
      this.lobby = lobby;
      this.bfModel = bfModel;
      this.specModel = specModel;
   }

   public void executeCommand(final Command cmd) {
      switch (cmd.type) {
         case BATTLE:
            switch (cmd.args[0]) {
               case "spectator_user_init":
                  this.initUser();
                  break;
               case "i_exit_from_battle":
                  this.lobby.onExitFromBattle();
                  break;
               case "chat":
                  this.specModel.getChatModel().onMessage(cmd.args[1], this);
            }
            break;
         case PING:
            break;
         default:
            Logger.log("[executeCommand(Command)::SpectatorController] : non-battle command " + cmd);
      }
   }

   private void initUser() {
      try {
         this.inited = true;
         this.sendShotsData();
         this.bfModel.parseBonus(this);
         this.bfModel.effectsModel.sendInitData(this);
         if (this.bfModel.battleInfo.battleType.equals("CTF")) {
            this.sendCommand(Type.BATTLE, "init_ctf_model", JSONUtils.parseCTFModelData(this.bfModel));
         }
         this.sendCommand(Type.BATTLE, "init_gui_model", JSONUtils.parseBattleData(this.bfModel));
         this.bfModel.sendAllTanks(this);
         this.sendCommand(Type.BATTLE, "init_inventory", "{}");
         this.bfModel.battleMinesModel.initModel(this);
         this.bfModel.battleMinesModel.sendMines(this);
      }
      catch (Exception ex) {
         this.lobby.kick();
      }
   }

   public String getId() {
      return this.lobby.getLocalUser().getNickname();
   }

   public User getUser() {
      return this.lobby.getLocalUser();
   }

   public void sendCommand(final Type type, final String... args) {
      if (this.inited) {
         this.lobby.send(type, args);
      }
   }

   private void sendShotsData() {
      this.lobby.send(Type.BATTLE, "init_shots_data", WeaponsFactory.getJSONList());
   }

   public void onDisconnect() {
      try {
         this.bfModel.spectatorModel.removeSpectator(this);
      }catch (NullPointerException f)
      {

      }
   }
}