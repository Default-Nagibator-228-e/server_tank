package utils;

public class RankUtils {
   private static Rank[] ranks;

   public static void init() {
      ranks = new Rank[30];
      ranks[0] = new Rank(0, 99, "Новобранец");
      ranks[1] = new Rank(100, 499, "Рядовой");
      ranks[2] = new Rank(500, 1499, "Ефрейтор");
      ranks[3] = new Rank(1500, 3699, "Капрал");
      ranks[4] = new Rank(3700, 7099, "Мастер-капрал");
      ranks[5] = new Rank(7100, 12299, "Сержант");
      ranks[6] = new Rank(12300, 19999, "Штаб-сержант");
      ranks[7] = new Rank(20000, 28999, "Мастер-сержант");
      ranks[8] = new Rank(29000, 'ꀧ', "Первый сержант");
      ranks[9] = new Rank('ꀨ', '\udea7', "Сержант-майор");
      ranks[10] = new Rank('\udea8', 75999, "Уорэент-офицер 1");
      ranks[11] = new Rank(76000, 97999, "Уорэент-офицер 2");
      ranks[12] = new Rank(98000, 124999, "Уорэент-офицер 3");
      ranks[13] = new Rank(125000, 155999, "Уорэент-офицер 4");
      ranks[14] = new Rank(156000, 191999, "Уорэент-офицер 5");
      ranks[15] = new Rank(192000, 232999, "Младшый лейтенант");
      ranks[16] = new Rank(233000, 279999, "Лейтенант");
      ranks[17] = new Rank(280000, 331999, "Старший лейтенант");
      ranks[18] = new Rank(332000, 389999, "Капитан");
      ranks[19] = new Rank(390000, 454999, "Майор");
      ranks[20] = new Rank(455000, 526999, "Подполковник");
      ranks[21] = new Rank(527000, 605999, "Полковник");
      ranks[22] = new Rank(606000, 691999, "Бригадир");
      ranks[23] = new Rank(692000, 786999, "Генерал-майор");
      ranks[24] = new Rank(787000, 888999, "Генерал-лейнетант");
      ranks[25] = new Rank(889000, 999999, "Генерал");
      ranks[26] = new Rank(1000000, 1121999, "Маршал");
      ranks[27] = new Rank(1122000, 1254999, "Фельдмаршал");
      ranks[28] = new Rank(1255000, 1399999, "Командор");
      ranks[29] = new Rank(1400000, 0, "Генералиссимус");
   }

   public static int getUpdateNumber(int score) {
      Rank temp = getRankByScore(score);
      int rangId = getNumberRank(temp);
      int rang = rangId;
      boolean result = false;

      int result1;
      try {
         result1 = (int)((double)(score - ranks[rang - 1].max) * 1.0D / (double)(temp.max - ranks[rang - 1].max) * 10000.0D);
      } catch (Exception var6) {
         result1 = (int)((double)(score - 0) * 1.0D / (double)(temp.max - 0) * 10000.0D);
      }

      if(score > ranks[ranks.length - 1].min - 1) {
         result1 = 10000;
      } else if(score < 0) {
         result1 = 0;
      }

      return result1;
   }

   public static int getNumberRank(Rank rank) {
      for(int i = 0; i < ranks.length; ++i) {
         if(ranks[i] == rank) {
            return i;
         }
      }

      return -1;
   }

   public static Rank getRankByScore(int score) {
      Rank temp = ranks[0];
      if(score >= ranks[29].max) {
         temp = ranks[29];
      }

      Rank[] var5 = ranks;
      int var4 = ranks.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Rank rank = var5[var3];
         if(score >= rank.min && score <= rank.max) {
            temp = rank;
         }
      }

      return temp;
   }

   public static Rank getRankByIndex(int index) {
      return ranks[index];
   }

   public static int stringToInt(String src) {
      try {
         int ex = Integer.parseInt(src);
         if(ex <= 0) {
            ex = 5000000;
         }

         return ex >= ranks[29].min?ranks[29].min:ex;
      } catch (Exception var2) {
         return 50000000;
      }
   }
}
