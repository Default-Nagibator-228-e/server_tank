package battles;

import battles.effects.EffectType;
import battles.tanks.Tank;
import battles.tanks.data.DamageTankData;
import battles.tanks.statistic.prizes.BattlePrizeCalculate;
import battles.tanks.weapons.WeaponUtils;
import battles.timer.QuartsJob;
import commands.Type;
import json.JSONUtils;
import lobby.battles.BattleInfo;
import services.TanksServices;
import services.annotations.ServicesInject;
import system.destroy.Destroyable;
import system.quartz.QuartzService;
import system.quartz.TimeType;
import system.quartz.impl.QuartzServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class TankKillModel implements Destroyable {
   private static final String QUARTZ_GROUP = TankKillModel.class.getName();
   private final String QUARTZ_NAME;
   private static final int EXPERIENCE_FOR_KILL = 10;
   private static final int TIME_TO_RESTART_BATTLE = 10000;
   private BattlefieldModel bfModel;
   @ServicesInject(
      target = TanksServices.class
   )
   private TanksServices tanksServices = TanksServices.getInstance();
   @ServicesInject(
      target = QuartzService.class
   )
   private QuartzService quartzService = QuartzServiceImpl.inject();
   private double battleFund;
   private BattleInfo battleInfo;

   public TankKillModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
      this.battleInfo = bfModel.battleInfo;
      this.QUARTZ_NAME = "TankKillModel " + this.hashCode() + " battle=" + this.battleInfo.battleId;
   }

   public synchronized void damageTank(BattlefieldPlayerController controller, BattlefieldPlayerController damager, float damage, boolean considerDD,boolean kr) {
      if(controller != null && damager != null) {
         Tank tank = controller.tank;
         if(!tank.state.equals("newcome") && !tank.state.equals("suicide")) {
            if(!this.bfModel.battleInfo.team || controller == damager || !controller.playerTeamType.equals(damager.playerTeamType) || this.bfModel.battleInfo.friendlyFire) {
               Integer resistance = null;
               try {
                  resistance = controller.tank.getColormap().getResistance(damager.tank.getWeapon().getEntity().getType());
               }catch(NullPointerException n){

               }
               damage = WeaponUtils.calculateDamageWithResistance(damage, resistance == null?0:resistance.intValue());
               if(tank.isUsedEffect(EffectType.ARMOR)) {
                  damage /= 2.0F;
               }

               if(damager.tank.isUsedEffect(EffectType.DAMAGE) && considerDD) {
                  damage *= 2.0F;
               }

               DamageTankData lastDamage = (DamageTankData)tank.lastDamagers.get(damager);
               DamageTankData damageData = new DamageTankData();
               damageData.damage = damage;
               damageData.timeDamage = System.currentTimeMillis();
               damageData.damager = damager;
               if(lastDamage != null) {
                  damageData.damage += lastDamage.damage;
               }

               if(damager.tank.isUsedEffect(EffectType.DAMAGE) && considerDD) {
                  damageData.damage /= 2.0F;
               }

               if(controller != damager) {
                  tank.lastDamagers.put(damager, damageData);
               }

               tank.health -= WeaponUtils.calculateHealth(tank, damage);
               this.changeHealth(tank, tank.health);
               if(tank.health <= 0) {
                  tank.health = 0;
                  if(damage > 0 && controller != damager) {
                     damager.send(Type.BATTLE, new String[]{"hp", tank.id, String.valueOf(damage), String.valueOf(0)});
                  }
                  this.killTank(controller, damager);
               }else {
                  if(kr) {
                     if (damage > 0 && controller != damager) {
                        damager.send(Type.BATTLE, new String[]{"hp", tank.id, String.valueOf(damage), String.valueOf(3)});
                     }
                  }else{
                     if (damage > 0 && controller != damager) {
                        damager.send(Type.BATTLE, new String[]{"hp", tank.id, String.valueOf(damage), String.valueOf(1)});
                     }
                  }
               }
               if(controller != damager) {
                  this.addFund(damage / controller.tank.getHull().hp);
               }
            }//ffff00
         }
      }
   }

   public boolean healPlayer(BattlefieldPlayerController healer, BattlefieldPlayerController target, float addHeal) {
      Tank targetTank = target.tank;
      if(addHeal>0 && healer.playerTeamType == target.playerTeamType) {
         healer.send(Type.BATTLE, new String[]{"hp", targetTank.id, String.valueOf(addHeal), String.valueOf(2)});
      }
      if(!targetTank.state.equals("newcome") && !targetTank.state.equals("suicide")) {
         if(targetTank.health >= 10000) {
            return false;
         } else {
            targetTank.health += WeaponUtils.calculateHealth(targetTank, addHeal);
            if(targetTank.health >= 10000) {
               targetTank.health = 10000;
            }
            this.changeHealth(targetTank, targetTank.health);
            if(healer != target) {
               this.addFund(addHeal / target.tank.getHull().hp);
            }
            return true;
         }
      } else {
         return false;
      }
   }

   public void changeHealth(Tank tank, int value) {
      if(tank != null) {
         tank.health = value;
         this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_health", tank.id, String.valueOf(tank.health)});
      }

   }

   public synchronized void killTank(BattlefieldPlayerController controller, BattlefieldPlayerController killer) {
      Tank tank = controller.tank;
      tank.state = "suicide";
      tank.getWeapon().stopFire();
      controller.clearEffects();
      controller.send(Type.BATTLE, new String[]{"local_user_killed"});
      if(killer == null) {
         this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"kill_tank", tank.id, "suicide"});
      } else {
         this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"kill_tank", tank.id, "killed", killer.tank.id});
         if(controller == killer) {
            controller.statistic.addDeaths();
            this.bfModel.statistics.changeStatistic(controller);
         } else {
            if(this.bfModel.battleInfo.team) {
               if(controller.playerTeamType.equals(killer.playerTeamType)) {
                  if(this.bfModel.battleInfo.friendlyFire) {
                     controller.statistic.addDeaths();
                     this.bfModel.statistics.changeStatistic(controller);
                  }
               } else {
                  killer.statistic.addKills(false);
                  controller.statistic.addDeaths();
                  if(killer.playerTeamType.equals("BLUE")) {
                     if(!this.battleInfo.battleType.equals("CTF")) {
                        ++this.bfModel.battleInfo.scoreBlue;
                     }

                     this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_team_scores", "BLUE", String.valueOf(this.bfModel.battleInfo.scoreBlue)});
                  } else if(killer.playerTeamType.equals("RED")) {
                     if(!this.battleInfo.battleType.equals("CTF")) {
                        ++this.bfModel.battleInfo.scoreRed;
                     }

                     this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_team_scores", "RED", String.valueOf(this.bfModel.battleInfo.scoreRed)});
                  }

                  this.bfModel.statistics.changeStatistic(killer);
                  this.bfModel.statistics.changeStatistic(controller);
               }
            } else {
               killer.statistic.addKills(true);
               controller.statistic.addDeaths();
               this.bfModel.statistics.changeStatistic(killer);
               this.bfModel.statistics.changeStatistic(controller);
            }

            if(this.bfModel.battleInfo.team) {
               LinkedHashMap killScore = controller.tank.lastDamagers;
               if(killScore.size() <= 1) {
                  this.tanksServices.addScore(killer.parentLobby, 10);
                  if(this.bfModel.battleInfo.team) {
                     killer.statistic.addScore(10);
                     this.bfModel.statistics.changeStatistic(killer);
                  }
               } else {
                  DamageTankData damager1 = (DamageTankData)killScore.get(killScore.keySet().toArray()[killScore.size() - 1]);
                  DamageTankData damager2 = (DamageTankData)killScore.get(killScore.keySet().toArray()[killScore.size() - 2]);
                  long currentTime = System.currentTimeMillis();
                  if(currentTime - damager1.timeDamage <= 10000L && currentTime - damager2.timeDamage <= 10000L) {
                     int var12;
                     int var13;
                     if(damager1.damage > damager2.damage) {
                        var12 = (int)(0.15D * (double)(100.0F / (controller.tank.getHull().hp / damager1.damage)));
                        var13 = 15 - var12;
                     } else if(damager2.damage > damager1.damage) {
                        var13 = (int)(0.15D * (double)(100.0F / (controller.tank.getHull().hp / damager2.damage)));
                        var12 = 15 - var13;
                     } else {
                        var12 = 7;
                        var13 = 7;
                     }

                     var12 = Math.abs(var12);
                     var13 = Math.abs(var13);
                     if(this.bfModel.battleInfo.team) {
                        damager1.damager.statistic.addScore(var12);
                        damager2.damager.statistic.addScore(var13);
                        this.bfModel.statistics.changeStatistic(damager1.damager);
                        this.bfModel.statistics.changeStatistic(damager2.damager);
                     }

                     this.tanksServices.addScore(damager1.damager.parentLobby, var12);
                     this.tanksServices.addScore(damager2.damager.parentLobby, var13);
                  } else {
                     killer.statistic.addScore(10);
                     this.bfModel.statistics.changeStatistic(killer);
                     this.tanksServices.addScore(killer.parentLobby, 10);
                  }
               }
            } else {
               int var11 = 10;
               if(controller.flag != null) {
                  var11 *= 2;
               }

               this.tanksServices.addScore(killer.parentLobby, var11);
            }
         }

         if(this.battleInfo.numKills > 0 && killer.statistic.getKills() >= (long)this.battleInfo.numKills) {
            this.restartBattle(false);
         }

         if(controller.flag != null) {
            this.bfModel.ctfModel.dropFlag(controller, controller.tank.position);
         }
      }

      this.bfModel.statistics.changeStatistic(controller);
      this.bfModel.respawnPlayer(controller, false);
      controller.tank.lastDamagers.clear();
   }

   public void restartBattle(boolean timeLimitFinish) {
      if(!timeLimitFinish && this.battleInfo.time > 0) {
         this.quartzService.deleteJob(this.bfModel.QUARTZ_NAME, BattlefieldModel.QUARTZ_GROUP);
      }
      this.calculatePrizes();
      this.bfModel.battleFinish();
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"battle_finish", JSONUtils.parseFishishBattle(this.bfModel.players, 10000)});
      this.quartzService.addJob(this.QUARTZ_NAME, QUARTZ_GROUP, new QuartsJob(this.bfModel), TimeType.MS, 10000L);
   }

   private void calculatePrizes() {
      if(this.bfModel != null && this.bfModel.players != null && this.bfModel.players.size() > 0) {
         ArrayList users = new ArrayList(this.bfModel.players.values());
         if(!this.bfModel.battleInfo.team) {
            Collections.sort(users);
            BattlePrizeCalculate.calc(users, (int)this.battleFund,false);
         } else {
            ArrayList redTeam = new ArrayList();
            ArrayList blueTeam = new ArrayList();
            Iterator var5 = users.iterator();

            while(var5.hasNext()) {
               BattlefieldPlayerController player = (BattlefieldPlayerController)var5.next();
               if(player.playerTeamType.equals("RED")) {
                  redTeam.add(player);
               } else if(player.playerTeamType.equals("BLUE")) {
                  blueTeam.add(player);
               }
            }

            BattlePrizeCalculate.calculateForTeam(redTeam, blueTeam, this.bfModel.battleInfo.scoreRed, this.bfModel.battleInfo.scoreBlue, 0.25D, (int)this.battleFund);
         }
      }
   }

   public void addFund(double value) {
      this.battleFund += value;
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_fund", String.valueOf((int)this.battleFund)});
      this.bfModel.bonusesSpawnService.updatedFund();
   }

   public double getBattleFund() {
      return this.battleFund;
   }

   public void setBattleFund(int value) {
      this.battleFund = (double)value;
   }

   public void destroy() {
      this.quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
   }
}
