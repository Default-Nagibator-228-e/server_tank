package users.karma;

public class UserKarmaActionFactory {
   public static UserKarmaAction getUserKarmaAction(UserKarmaActionType type, String comment) {
      return new UserKarmaAction(type, comment);
   }

   public static UserKarmaAction getUserKarmaAction(UserKarmaActionType type) {
      return new UserKarmaAction(type);
   }
}
