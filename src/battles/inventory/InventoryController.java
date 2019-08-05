package battles.inventory;

import logger.Logger;
import battles.effects.impl.ArmorEffect;
import battles.effects.impl.Mine;
import battles.effects.impl.NitroEffect;
import battles.effects.impl.DamageEffect;
import battles.effects.impl.HealthEffect;
import battles.effects.Effect;
import users.garage.items.Item;
import battles.tanks.math.Vector3;
import json.JSONUtils;
import commands.Type;
import battles.BattlefieldPlayerController;
import main.database.impl.DatabaseManagerImpl;
import services.annotations.ServicesInject;
import main.database.DatabaseManager;

public class InventoryController
{
   @ServicesInject(target = DatabaseManagerImpl.class)
   private DatabaseManager database;
   private Boolean d;
   private BattlefieldPlayerController player;

   public InventoryController(final BattlefieldPlayerController player,Boolean yt) {
      this.database = DatabaseManagerImpl.instance();
      this.player = player;
      d=yt;
   }

   public void init() {
      this.player.send(Type.BATTLE, new String[] { "init_inventory", JSONUtils.parseInitInventoryComand(this.player.getGarage(),d) });
   }

   public void activateItem(final String id, final Vector3 tankPos) {
      final Item item = this.player.getGarage().getItemById(id);
      if (item == null || item.count < 1) {
         return;
      }
      final Effect effect = this.getEffectById(id);
      if (!this.player.tank.isUsedEffect(effect.getEffectType())) {
         effect.activate(this.player, true, tankPos);
         this.onActivatedItem(item, effect.getDurationTime());
         final Item item2 = item;
         --item2.count;
         if (item.count <= 0) {
            this.player.getGarage().items.remove(item);
         }
         new Thread(() -> {
            this.player.getGarage().parseJSONData();
            this.database.update(this.player.getGarage());
         }).start();
      }
   }

   private void onActivatedItem(final Item item, final int durationTime) {
      this.player.send(Type.BATTLE, new String[] { "activate_item", item.id });
      this.player.battle.sendToAllPlayers(Type.BATTLE, new String[] { "enable_effect", this.player.getUser().getNickname(), String.valueOf(item.index), String.valueOf(durationTime) });
   }

   private Effect getEffectById(final String id) {
      Effect effect = null;
      switch (id) {
         case "health": {
            effect = (Effect)new HealthEffect();
            return effect;
         }
         case "double_damage": {
            effect = (Effect)new DamageEffect();
            return effect;
         }
         case "n2o": {
            effect = (Effect)new NitroEffect();
            return effect;
         }
         case "mine": {
            effect = (Effect)new Mine();
            return effect;
         }
         case "armor": {
            effect = (Effect)new ArmorEffect();
            return effect;
         }
         default:
            break;
      }
      Logger.log("Effect with id:" + id + " not found!");
      return effect;
   }
}