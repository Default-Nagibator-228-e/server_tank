package battles.bonuses;

import battles.timer.schedulers.GoldTimer;
import logger.Logger;
import logger.Type;
import battles.tanks.math.Vector3;
import utils.RandomUtils;
import java.util.Random;
import java.util.Vector;

import battles.BattlefieldModel;

public class BonusesSpawnService implements Runnable
{
   private static final int DISAPPEARING_TIME_DROP = 30;
   private static final int DISAPPEARING_TIME_MONEY = 300;
   public BattlefieldModel battlefieldModel;
   private Random random;
   private int inc;
   private int prevFund;
   private int crystallFund;
   private BonusType to = BonusType.NITRO;
   public int goldFund;
   public int nextGoldFund;
   public Vector<GoldTimer> dsff = new Vector<>();

   public BonusesSpawnService(final BattlefieldModel model) {
      this.random = new Random();
      this.inc = 0;
      this.prevFund = 0;
      this.battlefieldModel = model;
      this.nextGoldFund = 10;
   }

   public void spawnRandomDrop() {
      final int id = this.random.nextInt(4);
      BonusType bonusType = null;
      switch (id) {
         case 0: {
            bonusType = BonusType.ARMOR;
            break;
         }
         case 1: {
            bonusType = BonusType.HEALTH;
            break;
         }
         case 2: {
            bonusType = BonusType.DAMAGE;
            break;
         }
         case 3: {
            bonusType = BonusType.NITRO;
            break;
         }
      }
      for (int count = this.random.nextInt(4), i = 0; i < count; ++i) {
         this.spawnBonus(bonusType);
      }
   }

   public void spawnBonus(final BonusType type) {
      int index = 0;
      BonusRegion region = null;
      Bonus bonus = null;
      switch (type) {
         case GOLD: {
            if (this.battlefieldModel.battleInfo.map.goldsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.goldsRegions.size());
            region = this.battlefieldModel.battleInfo.map.goldsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.GOLD, this.inc, 300);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 300);
            break;
         }
         case CRYSTALL: {
            if (this.battlefieldModel.battleInfo.map.crystallsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.crystallsRegions.size());
            region = this.battlefieldModel.battleInfo.map.crystallsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.CRYSTALL, this.inc, 300);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 300);
            break;
         }
         case CRYSTALL5: {
            if (this.battlefieldModel.battleInfo.map.crystallsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.crystallsRegions.size());
            region = this.battlefieldModel.battleInfo.map.crystallsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.CRYSTALL5, this.inc, 300);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 300);
            break;
         }
         case CRYSTALL10: {
            if (this.battlefieldModel.battleInfo.map.crystallsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.crystallsRegions.size());
            region = this.battlefieldModel.battleInfo.map.crystallsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.CRYSTALL10, this.inc, 300);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 300);
            break;
         }
         case CRYSTALL20: {
            if (this.battlefieldModel.battleInfo.map.crystallsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.crystallsRegions.size());
            region = this.battlefieldModel.battleInfo.map.crystallsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.CRYSTALL20, this.inc, 300);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 300);
            break;
         }
         case CRYSTALL50: {
            if (this.battlefieldModel.battleInfo.map.crystallsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.crystallsRegions.size());
            region = this.battlefieldModel.battleInfo.map.crystallsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.CRYSTALL50, this.inc, 300);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 300);
            break;
         }
         case CRYSTALL100: {
            if (this.battlefieldModel.battleInfo.map.crystallsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.crystallsRegions.size());
            region = this.battlefieldModel.battleInfo.map.crystallsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.CRYSTALL100, this.inc, 300);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 300);
            break;
         }
         case ARMOR: {
            if (this.battlefieldModel.battleInfo.map.armorsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.armorsRegions.size());
            region = this.battlefieldModel.battleInfo.map.armorsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.ARMOR, this.inc, 30);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 30);
            break;
         }
         case DAMAGE: {
            if (this.battlefieldModel.battleInfo.map.damagesRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.damagesRegions.size());
            region = this.battlefieldModel.battleInfo.map.damagesRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.DAMAGE, this.inc, 30);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 30);
            break;
         }
         case HEALTH: {
            if (this.battlefieldModel.battleInfo.map.healthsRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.healthsRegions.size());
            region = this.battlefieldModel.battleInfo.map.healthsRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.HEALTH, this.inc, 30);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 30);
            break;
         }
         case NITRO: {
            if (this.battlefieldModel.battleInfo.map.nitrosRegions.size() <= 0) {
               break;
            }
            index = this.random.nextInt(this.battlefieldModel.battleInfo.map.nitrosRegions.size());
            region = this.battlefieldModel.battleInfo.map.nitrosRegions.get(index);
            bonus = new Bonus(this.getRandomSpawnPostiton(region), BonusType.NITRO, this.inc, 30);
            this.battlefieldModel.spawnBonus(bonus, this.inc, 30);
            break;
         }
      }
      ++this.inc;
   }

   public void battleFinished() {
      this.prevFund = 0;
      this.crystallFund = 0;
      this.goldFund = 0;
      this.nextGoldFund = 10;//(int)utils.RandomUtils.getRandom(700.0f, 730.0f);
      for(int d = 0;d<dsff.size();d++)
      {
         GoldTimer ds = dsff.get(d);
         (new Thread(ds)).stop();
         dsff.remove(d);
         ds = null;
      }
   }

   private Vector3 getRandomSpawnPostiton(final BonusRegion region) {
      final Vector3 f = new Vector3(0.0f, 0.0f, 0.0f);
      final Random rand = new Random();
      f.x = region.min.x + (region.max.x - region.min.x) * rand.nextFloat();
      f.y = region.min.y + (region.max.y - region.min.y) * rand.nextFloat();
      f.z = region.max.z;
      return f;
   }

   public void updatedFund() {
      final int deff = (int)this.battlefieldModel.tanksKillModel.getBattleFund() - this.prevFund;
      this.goldFund += deff;
      if (this.goldFund >= 10) {
         this.battlefieldModel.sendToAllPlayers(commands.Type.BATTLE,"gold;");
      }
      if (this.goldFund >= this.nextGoldFund) {
         dsff.add(new GoldTimer((int)RandomUtils.getRandom(30000.0f, 90000.0f),this));
         this.nextGoldFund = 10;
         this.goldFund = 0;
      }
      if(this.crystallFund>0) {
         this.crystallFund--;
      }else{
         for (int i = 0; i < (int)RandomUtils.getRandom(1.0f, 6.0f); ++i) {
            this.spawnBonus(to);
         }
         this.crystallFund = RandomUtils.getRan(1,5,10,20,50,100);
         to = this.crystallFund == 1 ? BonusType.CRYSTALL: this.crystallFund == 5 ? BonusType.CRYSTALL5: this.crystallFund == 10 ? BonusType.CRYSTALL10: this.crystallFund == 20? BonusType.CRYSTALL20: this.crystallFund == 50 ? BonusType.CRYSTALL50: BonusType.CRYSTALL100;
      }
      this.prevFund = (int)this.battlefieldModel.tanksKillModel.getBattleFund();
   }

   @Override
   public void run() {
      if (this.battlefieldModel.battleInfo.map.crystallsRegions.size() <= 0 && this.battlefieldModel.battleInfo.map.goldsRegions.size() <= 0) {
         this.battlefieldModel = null;
      }
      while (this.battlefieldModel != null) {
         try {
            Thread.sleep(40000L);
            if (this.battlefieldModel == null || this.battlefieldModel.players == null) {
               break;
            }
            this.spawnRandomDrop();
         }
         catch (InterruptedException e) {
            Logger.log(Type.ERROR, e.getMessage());
         }
      }
   }
}