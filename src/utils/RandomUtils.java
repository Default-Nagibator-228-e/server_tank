package utils;

public class RandomUtils {
   public static float getRandom(float min, float max) {
      return min == max?min:(float)((double)min + Math.random() * (double)(max - min + 1.0F));
   }
   public static int getRan(final int... args) {
      return args[(int)(getRandom(0f,new Float((args.length - 1)+ "")))];
   }
   public static Boolean getRandomper(float per) {
      float d = (float)(Math.random() * (double)(100.0F));
      return d <= per?true:false;
   }
}
