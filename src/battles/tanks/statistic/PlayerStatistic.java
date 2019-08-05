package battles.tanks.statistic;

import users.User;

public class PlayerStatistic implements Comparable<PlayerStatistic> {
   private long kills;
   private int deaths;
   private int prize;
   private int lprize;
   private int zv;
   private long score;
   private User u;

   public PlayerStatistic(int kills, int deaths, int score, User us) {
      this.kills = (long)kills;
      this.deaths = deaths;
      this.score = (long)score;
      u = us;
   }

   public void addKills(boolean killsEqualsScore) {
      ++u.y;
      ++this.kills;
      u.yp = u.p == 0 ? u.y : (100*u.y)/u.p;
      if(killsEqualsScore) {
         this.score = this.kills;
      }
   }

   public void addDeaths() {
      ++u.p;
      ++this.deaths;
      u.yp = (100*u.y)/u.p;
   }

   public void addScore(int value) {
      this.score += (long)value;
   }

   public void setScore(long value) {
      this.score = value;
   }

   public long getScore() {
      return this.score;
   }

   public long getKills() {
      return this.kills;
   }

   public void setKills(long kills) {
      this.kills = kills;
   }

   public int getDeaths() {
      return this.deaths;
   }

   public void setDeaths(int deaths) {
      this.deaths = deaths;
   }

   public int getPrize() {
      return this.prize;
   }

   public void setPrize(int prize) {
      this.prize = prize;
   }

   public int getLPrize() {
      return this.lprize;
   }

   public void setLPrize(int lprize) {
      this.lprize = lprize;
   }

   public int gz() {
      return this.zv;
   }

   public void sz(int z) {
      this.zv = z;
   }

   public void clear() {
      this.kills = 0L;
      this.deaths = 0;
      this.prize = 0;
      this.zv = 0;
      this.score = 0L;
   }

   public float getKD() {
      return (float)(this.kills / (long)this.deaths);
   }

   public String toString() {
      return "score: " + this.score + " kills: " + this.kills + " deaths: " + this.deaths + " prize: " + this.prize;
   }

   public int compareTo(PlayerStatistic arg0) {
      return (int)(arg0.getScore() - this.score);
   }
}
